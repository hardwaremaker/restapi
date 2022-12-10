package com.heliumv.api.stock;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.heliumv.api.BaseApi;
import com.heliumv.api.HvValidateBadRequest;
import com.heliumv.api.HvValidateExpectationFailed;
import com.heliumv.api.HvValidateNotFound;
import com.heliumv.api.item.IItemService;
import com.heliumv.api.item.ItemEntryInternal;
import com.heliumv.api.item.ItemEntryMapper;
import com.heliumv.api.item.ItemIdentityEntry;
import com.heliumv.api.item.ItemIdentityEntryList;
import com.heliumv.api.item.ItemV1Entry;
import com.heliumv.api.item.ItemV1EntryList;
import com.heliumv.api.item.StockAmountInfoEntryMapper;
import com.heliumv.api.item.StockEntry;
import com.heliumv.api.item.StockEntryMapper;
import com.heliumv.factory.IArtikelCall;
import com.heliumv.factory.ILagerCall;
import com.heliumv.factory.IPanelCall;
import com.heliumv.factory.legacy.AllLagerEntry;
import com.heliumv.factory.loader.IItemLoaderAttribute;
import com.heliumv.tools.StringHelper;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.server.artikel.service.ArtgruDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikellagerplaetzeDto;
import com.lp.server.artikel.service.HandlagerbewegungDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.artikel.service.LagerFac;
import com.lp.server.artikel.service.LagerplatzDto;
import com.lp.server.artikel.service.LagerstandInfoDto;
import com.lp.server.artikel.service.LagerstandInfoEntryDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.system.service.PanelbeschreibungDto;
import com.lp.server.system.service.PaneldatenDto;
import com.lp.server.util.ArtikelId;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Service("hvStock")
@Path("/api/v1/stock")
public class StockApi extends BaseApi implements IStockApi {

	@Autowired
	private ILagerCall lagerCall ;
	
	@Autowired
	private IArtikelCall artikelCall;
	
	@Autowired
	private ItemEntryMapper itemEntryMapper;
	
	@Autowired
	private StockAmountInfoEntryMapper stockAmountInfoEntryMapper;
	
	@Autowired
	private StockPlaceEntryMapper stockPlaceEntryMapper;
	@Autowired
	private IItemService itemService;
	@Autowired
	private IPanelCall panelCall;
	
	@Override
	@GET
	@Produces({FORMAT_JSON, FORMAT_XML})	
	public StockEntryList getStockList(
			@QueryParam(Param.USERID) String userId) {
		StockEntryList stockEntries = new StockEntryList() ;
		if(connectClient(userId) == null) return stockEntries ;

		try {
			List<StockEntry> entries = new ArrayList<StockEntry>() ;
			StockEntryMapper stockMapper = new StockEntryMapper() ;
			List<AllLagerEntry> stocks = lagerCall.getAllLager() ;
			for (AllLagerEntry allLagerEntry : stocks) {
				if(lagerCall.hatRolleBerechtigungAufLager(allLagerEntry.getStockId())) {
					LagerDto lagerDto = lagerCall.lagerFindByPrimaryKeyOhneExc(allLagerEntry.getStockId()) ;
					if(lagerDto.getBVersteckt() > 0) continue ;
					
					entries.add(stockMapper.mapEntry(lagerDto)) ;
				}
			}
			
			stockEntries.setEntries(entries) ;
		} catch(RemoteException e) {
			respondUnavailable(e) ;
		} catch(EJBExceptionLP e) {
			respondBadRequest(e);
		}
		
		return stockEntries ;
	}

	
//	@GET
//	@Path("/{" + Param.ITEMID + "}/stockplace/list")
//	@Produces({FORMAT_JSON, FORMAT_XML})
//	public StockPlaceEntryList getStockPlaceList(
//			@QueryParam(Param.USERID) String userId,
//			@PathParam(Param.ITEMID) Integer itemId,
//			@QueryParam(Param.STOCKID) Integer stockId) {
//		if(connectClient(userId) == null) return new StockPlaceEntryList();
//		
//		List<LagerplatzDto> lagerplatzDtos;
//		if (stockId == null) {
//			lagerplatzDtos = lagerCall.lagerplatzFindByArtikelIIdOhneExc(itemId);
//		} else {
//			lagerplatzDtos = new ArrayList<LagerplatzDto>();
//		}
//
//		StockPlaceEntryList entries = new StockPlaceEntryList(stockPlaceEntryMapper.mapEntries(lagerplatzDtos));
//		return entries;
//	}
//	
	@Override
	@POST
	@Path("/{" + Param.STOCKID + "}/place")
	@Consumes({FORMAT_JSON, FORMAT_XML})
	public Integer createStockPlace(
			@QueryParam(Param.USERID) String userId,
			@PathParam(Param.STOCKID) Integer stockId,
			StockPlacePostEntry postEntry) throws RemoteException {
		if(connectClient(userId) == null) return null;

		HvValidateBadRequest.notNull(stockId, Param.STOCKID);
		HvValidateBadRequest.notNull(postEntry, "stockplace");
		HvValidateBadRequest.notNull(postEntry.getItemId(), "itemId");
		
		LagerDto lagerDto = findValidateLagerById(stockId);
		if (lagerDto == null) return null;
		
		ArtikelDto artikelDto = artikelCall.artikelFindByPrimaryKeySmallOhneExc(postEntry.getItemId());
		HvValidateNotFound.notNull(artikelDto, "itemId", postEntry.getItemId());

		LagerplatzDto lagerplatzDto = findValidateLagerplatzByIdOrName(postEntry.getStockplaceId(), postEntry.getStockplaceName(), stockId);

		ArtikellagerplaetzeDto artikellagerplatzDto = 
				lagerCall.artikellagerplaetzeFindByArtikelIIdLagerplatzIIdOhneExc(postEntry.getItemId(), lagerplatzDto.getIId());
		
		if (artikellagerplatzDto != null) {
			return lagerplatzDto.getIId();
		}
		
		artikellagerplatzDto = new ArtikellagerplaetzeDto();
		artikellagerplatzDto.setArtikelIId(postEntry.getItemId());
		artikellagerplatzDto.setLagerplatzIId(lagerplatzDto.getIId());
		lagerCall.createArtikellagerplaetze(artikellagerplatzDto); 
		
		return lagerplatzDto.getIId();
	}


	private LagerDto findValidateLagerById(Integer stockId) {
		LagerDto lagerDto = lagerCall.lagerFindByPrimaryKeyOhneExc(stockId);
		HvValidateNotFound.notNull(lagerDto, Param.STOCKID, stockId);
		
		if (!lagerCall.hatRolleBerechtigungAufLager(stockId)) {
			respondForbidden(); 
			return null;
		}
		return lagerDto;
	}
	
	private LagerplatzDto findValidateLagerplatzByIdOrName(Integer id, String name, Integer stockId) {
		if (id != null) {
			return findValidateLagerplatzById(id, stockId);
		}
		
		HvValidateBadRequest.notValid(name != null, Param.STOCKPLACEID + " && " + Param.STOCKPLACENAME, name);
		
		LagerplatzDto lagerplatzDto = lagerCall.lagerplatzFindByCLagerplatzLagerIIdOhneExc(name, stockId);
		HvValidateNotFound.notNull(lagerplatzDto, "name", name);
		return lagerplatzDto;
	}

	private LagerplatzDto findValidateLagerplatzById(Integer id, Integer stockId) {
		LagerplatzDto lagerplatzDto = lagerCall.lagerplatzFindByPrimaryKeyOhneExc(id);
		HvValidateNotFound.notNull(lagerplatzDto, "stockplaceId", id);
		
		HvValidateNotFound.notValid(stockId.equals(lagerplatzDto.getLagerIId()), "stockplaceId", id.toString());
		
		return lagerplatzDto;
	}

	@Override
	@DELETE
	@Path("/{" + Param.STOCKID + "}/place/{" + Param.STOCKPLACEID + "}")
	@Consumes({FORMAT_JSON, FORMAT_XML})
	public void deleteStockPlace(
			@QueryParam(Param.USERID) String userId,
			@PathParam(Param.STOCKID) Integer stockId,
			@PathParam(Param.STOCKPLACEID) Integer stockplaceid,
			@QueryParam(Param.ITEMID) Integer itemId) throws RemoteException {
		if(connectClient(userId) == null) return;

		HvValidateBadRequest.notNull(stockId, Param.STOCKID);
		HvValidateBadRequest.notNull(stockplaceid, Param.STOCKPLACEID);
		HvValidateBadRequest.notNull(itemId, Param.ITEMID);
		
		LagerDto lagerDto = findValidateLagerById(stockId);
		if (lagerDto == null) return;
		
		ArtikelDto artikelDto = artikelCall.artikelFindByPrimaryKeySmallOhneExc(itemId);
		HvValidateNotFound.notNull(artikelDto, "itemId", itemId);

		findValidateLagerplatzById(stockplaceid, stockId);

		ArtikellagerplaetzeDto artikellagerplatzDto = 
				lagerCall.artikellagerplaetzeFindByArtikelIIdLagerplatzIIdOhneExc(itemId, stockplaceid);
		HvValidateNotFound.notNull(artikellagerplatzDto, 
				Param.ITEMID + " on " + Param.STOCKPLACEID, 
				Param.ITEMID + "=" + itemId + " and " + Param.STOCKPLACEID + "=" + stockplaceid);
		
		lagerCall.removeArtikellagerplaetze(artikellagerplatzDto.getIId());
	}

	@Override
	@GET
	@Path("/{" + Param.STOCKID + "}/place")
	@Produces({FORMAT_JSON, FORMAT_XML})	
	public StockPlaceEntry findStockPlace(
			@QueryParam(Param.USERID) String userId,
			@PathParam(Param.STOCKID) Integer stockId,
			@QueryParam(Param.STOCKPLACEID) Integer stockplaceId,
			@QueryParam(Param.STOCKPLACENAME) String stockplaceName,
			@QueryParam(Add.STOCKAMOUNTINFOS) Boolean addStockAmountInfos) throws RemoteException {
		if(connectClient(userId) == null) return null;

		HvValidateBadRequest.notNull(stockId, Param.STOCKID);
		
		LagerDto lagerDto = findValidateLagerById(stockId);
		if (lagerDto == null) return null;
		
		LagerplatzDto lagerplatzDto = findValidateLagerplatzByIdOrName(stockplaceId, stockplaceName, stockId);

		List<ArtikelDto> artikelAufLagerplatz = findArtikelAufLagerplatz(lagerplatzDto);
		ItemV1EntryList itemEntries = new ItemV1EntryList();
		itemEntries.getEntries().addAll(itemEntryMapper.mapV1EntriesSmall(artikelAufLagerplatz));

		StockPlaceEntry stockPlaceEntry = stockPlaceEntryMapper.mapEntry(lagerplatzDto);
		stockPlaceEntry.setItems(itemEntries);
		
		if (Boolean.TRUE.equals(addStockAmountInfos)) {
			addStockAmountInfoEntries(stockPlaceEntry.getItems(), stockId);
		}
		
		return stockPlaceEntry;
	}
	
	private List<ArtikelDto> findArtikelAufLagerplatz(LagerplatzDto lagerplatzDto) {
		List<ArtikellagerplaetzeDto> artikellagerplaetze = 
				lagerCall.artikellagerplaetzeFindByLagerplatzIId(lagerplatzDto.getIId());
		List<ArtikelDto> artikelAufLagerplatz = getArtikel(artikellagerplaetze);
		return artikelAufLagerplatz;
	}

	private void addStockAmountInfoEntries(ItemV1EntryList itemEntries, Integer lagerIId) {
		for (ItemV1Entry itemEntry : itemEntries.getEntries()) {
			itemEntry.setStockAmountInfo(stockAmountInfoEntryMapper.mapEntry(itemEntry.getId(), lagerIId));
		}
	}

	private List<ArtikelDto> getArtikel(List<ArtikellagerplaetzeDto> artikellagerplaetze) {
		List<ArtikelDto> artikelList = new ArrayList<ArtikelDto>();
		for (ArtikellagerplaetzeDto artikellagerplatz : artikellagerplaetze) {
			ArtikelDto artikelDto = artikelCall.artikelFindByPrimaryKeySmall(artikellagerplatz.getArtikelIId());
			artikelList.add(artikelDto);
		}
		return artikelList;
	}
	
/*	
	@Override
	@GET
	@Path("/iteminfo")
	@Produces({FORMAT_JSON, FORMAT_XML})	
	public StockInventoryInfoEntry getStockInfoList(
			@QueryParam(Param.USERID) String userId,
			@QueryParam(Param.STOCKPLACETYPE) String stockplaceTypes) throws NamingException, RemoteException {		
		if(StringHelper.isEmpty(stockplaceTypes)) {
			stockplaceTypes = LagerFac.LAGERART_PERSOENLICH;
		}
		
		if(connectClient(userId) == null) return new StockInventoryInfoEntry();
		
		String[] lagerarten = stockplaceTypes.split(",");
		
		Collection<LagerstandInfoDto> lagerstandInfos = lagerCall
				.getLagerstandAllerArtikelEinesMandanten(lagerarten);
		
		return transform(lagerstandInfos);
	}
*/
	
/*	
	@Override
	@GET
	@Path("/itemgroupinfo")
	@Produces({FORMAT_JSON, FORMAT_XML})	
	public StockInventoryInfoEntry getStockInfoListFromItemGroup(
			@QueryParam(Param.USERID) String userId,
			@QueryParam(Param.ITEMGROUPCNR) String itemGroupCnr,
			@QueryParam(Param.STOCKID) Integer stockId) throws NamingException, RemoteException {				
		if(connectClient(userId) == null) return new StockInventoryInfoEntry();

		HvValidateBadRequest.notEmpty(itemGroupCnr, Param.ITEMGROUPCNR);
		
		ArtgruDto agDto = artikelCall.artikelgruppeFindByCnrOhneExc(itemGroupCnr);
		HvValidateNotFound.notNull(agDto, Param.ITEMGROUPCNR, itemGroupCnr);
		
		Collection<LagerstandInfoDto> lagerstandInfos = lagerCall
				.getLagerstandAllerArtikelEinerGruppe(stockId, agDto.getIId());
		return transform(lagerstandInfos);
	}
*/	
	private StockInventoryInfoEntry transform(
			Collection<LagerstandInfoDto> lagerstandInfos) throws NamingException, RemoteException {
		
		/* 
		 * Grundidee:
		 * - Liste aller Artikel mit den ganzen Daten
		 * - Je Lager eine Liste der ArtikelId und der Menge
		 */
		Map<ArtikelId, ItemV1Entry> itemCache = 
				new HashMap<ArtikelId, ItemV1Entry>();
		Set<IItemLoaderAttribute> attributes = itemService.getAttributes(
				Boolean.TRUE, Boolean.FALSE, Boolean.FALSE,
				Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE);
		List<StockInventoryEntry> allStocks = new ArrayList<StockInventoryEntry>();
		for (LagerstandInfoDto lagerstandInfoDto : lagerstandInfos) {
			lagerstandInfoDto.getLagerId();
			StockInventoryEntry stockInventoryEntry = new StockInventoryEntry();
			stockInventoryEntry.setId(lagerstandInfoDto.getLagerId());
			
			LagerDto lagerDto = lagerCall
					.lagerFindByPrimaryKeyOhneExc(stockInventoryEntry.getId());
			stockInventoryEntry.setCnr(lagerDto.getCNr());
			
			List<StockInfoItemEntry> entries = new ArrayList<StockInfoItemEntry>();
			for (LagerstandInfoEntryDto infoEntry : lagerstandInfoDto.getEntries()) {
				StockInfoItemEntry entry = new StockInfoItemEntry();
				entry.setId(infoEntry.getArtikelId().id());
				entry.setAmount(infoEntry.getAmount());
				entry.setMinimum(infoEntry.getStockMinimum());
				entry.setNominal(infoEntry.getStockNominal());
				entries.add(entry);
				
				ItemV1Entry itemEntry = itemCache.get(infoEntry.getArtikelId());
				if(itemEntry == null) {
					ItemEntryInternal internalItem = itemService.findItemByIdAttributes(
							infoEntry.getArtikelId().id(), attributes);
					itemCache.put(infoEntry.getArtikelId(),
							itemService.mapFromInternalV1(internalItem));
				}
			}
			StockInfoItemEntryList entryList = new StockInfoItemEntryList(entries);
			stockInventoryEntry.setEntries(entryList);
			allStocks.add(stockInventoryEntry);
		}
		
		StockInventoryInfoEntry infoEntry = new StockInventoryInfoEntry();
		infoEntry.setEntries(new StockInventoryEntryList(allStocks));
		List<ItemV1Entry> itemEntries = new ArrayList<ItemV1Entry>();
		itemEntries.addAll(itemCache.values());
		infoEntry.setItems(new ItemV1EntryList(itemEntries));
		return infoEntry;
	}
	
//	@Override
//	@GET
//	@Path("/{" + Param.STOCKID + "}/movement/{itemid}")
//	@Produces({FORMAT_JSON, FORMAT_XML})
//	public StockMovementEntryList getStockMovements(
//			@QueryParam(Param.USERID) String userId,
//			@PathParam(Param.STOCKID) Integer stockId,
//			@PathParam(Param.ITEMID) Integer itemId) throws RemoteException {
//		if(connectClient(userId) == null) return null;
//
//		HvValidateBadRequest.notNull(stockId, Param.STOCKID);
//		return new StockMovementEntryList();
//	}
	
	@Override
	@POST
	@Path("/{" + Param.STOCKID + "}/movement/{itemid}")
	@Consumes({FORMAT_JSON, FORMAT_XML})
	public Integer createStockMovementReceipt(
			@QueryParam(Param.USERID) String userId,
			@PathParam(Param.STOCKID) Integer stockId,
			@PathParam(Param.ITEMID) Integer itemId,
			StockMovementEntry postEntry) throws RemoteException {
		if(connectClient(userId) == null) return null;

		HvValidateBadRequest.notNull(stockId, Param.STOCKID);
		HvValidateBadRequest.notNull(itemId, Param.ITEMID);
		HvValidateBadRequest.notNull(postEntry, "entry");
		HvValidateBadRequest.notNull(postEntry.getComment(), "comment");
		HvValidateBadRequest.notNull(postEntry.getAmount(), "amount");
		
		LagerDto lagerDto = lagerCall.lagerFindByPrimaryKeyOhneExc(stockId) ;
		HvValidateNotFound.notNull(lagerDto, Param.STOCKID, stockId);
		if (!lagerCall.hatRolleBerechtigungAufLager(lagerDto.getIId())) {
			respondForbidden();
			return -1;
		}

		ArtikelDto artikelDto = artikelCall.artikelFindByPrimaryKeySmallOhneExc(itemId);
		HvValidateNotFound.notNull(artikelDto, Param.ITEMID, itemId);
		HvValidateExpectationFailed.identItemRequired(artikelDto);
		
		HandlagerbewegungDto dto = createHandlagerbewegung(lagerDto, artikelDto, postEntry);
		addExistingPanelData(artikelDto, dto.getSeriennrChargennrMitMenge());
		
		dto.setBAbgang(Helper.getShortFalse());
		dto.setNEinstandspreis(postEntry.getPrice() == null 
				? lagerCall.getGemittelterGestehungspreisEinesLagers(
						artikelDto.getIId(), lagerDto.getIId())
				: postEntry.getPrice());
		
		Integer movementId = lagerCall.createHandlagerbewegung(dto);
		return movementId;
	}
	
	private HandlagerbewegungDto createHandlagerbewegung(LagerDto lagerDto, 
			ArtikelDto artikelDto, StockMovementEntry receipt) throws RemoteException {
		HandlagerbewegungDto dto = new HandlagerbewegungDto();
		dto.setArtikelIId(artikelDto.getIId());
		dto.setLagerIId(lagerDto.getIId());
		dto.setCKommentar(receipt.getComment());
		HvValidateBadRequest.notValid(receipt.getAmount().signum() == 1, "amount", receipt.getAmount().toPlainString());
		dto.setNMenge(receipt.getAmount());
		
		if (artikelDto.istArtikelSnrOderchargentragend()) {
			dto.setSeriennrChargennrMitMenge(transform(receipt.getIdentities()));
		}

		return dto;
	}
	
	private List<SeriennrChargennrMitMengeDto> transform(ItemIdentityEntryList identities) {
		List<SeriennrChargennrMitMengeDto> erpIdentities = new ArrayList<SeriennrChargennrMitMengeDto>();
		
		if (identities == null) {
			identities = new ItemIdentityEntryList();
		}

		for (ItemIdentityEntry identity : identities.getEntries()) {
			SeriennrChargennrMitMengeDto snrDto = new SeriennrChargennrMitMengeDto(
					identity.getIdentity(), identity.getAmount());
			erpIdentities.add(snrDto);
		}

		return erpIdentities;
	}

	/**
	 * Diese ganze Methode ist ein Workaround fuer ein EJB Verhalten</br>
	 * <p>Ein chargenbehafteter Artikel erwartet immer Chargeneigenschaften
	 * und damit ein PaneldatenDto[] im SerienChargennrMitMengeDto. Nur
	 * wenn dies != null ist, wird letztendlich im bucheZu() ein Datensatz
	 * fuer die Tabelle WW_ARTIKELSNRCHNR angelegt.</p>
	 * 
	 * @param artikelDto
	 * @param identities
	 */
	private void addExistingPanelData(ArtikelDto artikelDto,
			List<SeriennrChargennrMitMengeDto> identities) {
		if (!artikelDto.isChargennrtragend()) return;
		
		PanelbeschreibungDto[] panelDtos = panelCall
				.panelbeschreibungChargenFindByArtikelgruppeId(artikelDto.getArtgruIId());
		if (panelDtos == null || panelDtos.length == 0) return;
	
		for (SeriennrChargennrMitMengeDto identityDto : identities) {
			PaneldatenDto[] panelDatenvorhanden = lagerCall
					.getLetzteChargeninfosEinesArtikels(artikelDto.getIId(),
							identityDto.getCSeriennrChargennr());
			if (panelDatenvorhanden == null) {
				// Es gibt die Charge (und damit Paneldaten) noch nicht
				panelDatenvorhanden = new PaneldatenDto[0];
			}
			
			identityDto.setPaneldatenDtos(panelDatenvorhanden);
		}
	}
	
	@Override
	@DELETE
	@Path("/{" + Param.STOCKID + "}/movement/{itemid}")
	@Consumes({FORMAT_JSON, FORMAT_XML})
	public Integer createStockMovementIssue(
			@QueryParam(Param.USERID) String userId,
			@PathParam(Param.STOCKID) Integer stockId,
			@PathParam(Param.ITEMID) Integer itemId,
			StockMovementEntry postEntry) throws RemoteException {
		if(connectClient(userId) == null) return null;

		HvValidateBadRequest.notNull(stockId, Param.STOCKID);
		HvValidateBadRequest.notNull(itemId, Param.ITEMID);
		HvValidateBadRequest.notNull(postEntry, "entry");
		HvValidateBadRequest.notNull(postEntry.getComment(), "comment");
		HvValidateBadRequest.notNull(postEntry.getAmount(), "amount");
		
		LagerDto lagerDto = lagerCall.lagerFindByPrimaryKeyOhneExc(stockId) ;
		HvValidateNotFound.notNull(lagerDto, Param.STOCKID, stockId);
		if (!lagerCall.hatRolleBerechtigungAufLager(lagerDto.getIId())) {
			respondForbidden();
			return -1;
		}

		ArtikelDto artikelDto = artikelCall.artikelFindByPrimaryKeySmallOhneExc(itemId);
		HvValidateNotFound.notNull(artikelDto, Param.ITEMID, itemId);
		HvValidateExpectationFailed.identItemRequired(artikelDto);
		
		HandlagerbewegungDto dto = createHandlagerbewegung(lagerDto, artikelDto, postEntry);
		dto.setBAbgang(Helper.getShortTrue());
		dto.setNVerkaufspreis(postEntry.getPrice() == null 
				? lagerCall.getGemittelterGestehungspreisEinesLagers(
						artikelDto.getIId(), lagerDto.getIId())
				: postEntry.getPrice());
		
		Integer movementId = lagerCall.createHandlagerbewegung(dto);
		return movementId;
	}
	
	@Override
	@PUT
	@Path("/{" + Param.STOCKID + "}/movement/{itemid}")
	@Consumes({FORMAT_JSON, FORMAT_XML})
	public Integer createStockMovementChange(
			@QueryParam(Param.USERID) String userId,
			@PathParam(Param.STOCKID) Integer stockId,
			@PathParam(Param.ITEMID) Integer itemId,
			StockMovementChangeEntry changeEntry) throws RemoteException {
		if(connectClient(userId) == null) return null;

		HvValidateBadRequest.notNull(stockId, Param.STOCKID);
		HvValidateBadRequest.notNull(itemId, Param.ITEMID);
		HvValidateBadRequest.notNull(changeEntry, "entry");
		HvValidateBadRequest.notNull(changeEntry.getComment(), "comment");
		HvValidateBadRequest.notNull(changeEntry.getAmount(), "amount");
		HvValidateBadRequest.notNull(changeEntry.getTargetStockId(), "targetStockId");
		
		LagerDto lagerDto = lagerCall.lagerFindByPrimaryKeyOhneExc(stockId) ;
		HvValidateNotFound.notNull(lagerDto, Param.STOCKID, stockId);
		if (!lagerCall.hatRolleBerechtigungAufLager(lagerDto.getIId())) {
			respondForbidden();
			return -1;
		}

		LagerDto targetLagerDto = lagerCall.lagerFindByPrimaryKeyOhneExc(changeEntry.getTargetStockId()) ;
		HvValidateNotFound.notNull(targetLagerDto, "targetStockId", changeEntry.getTargetStockId());
		if (!lagerCall.hatRolleBerechtigungAufLager(targetLagerDto.getIId())) {
			respondForbidden();
			return -1;
		}

		ArtikelDto artikelDto = artikelCall.artikelFindByPrimaryKeySmallOhneExc(itemId);
		HvValidateNotFound.notNull(artikelDto, Param.ITEMID, itemId);
		HvValidateExpectationFailed.identItemRequired(artikelDto);
		HvValidateBadRequest.notValid(changeEntry.getAmount().signum() == 1,
				"amount", changeEntry.getAmount().toPlainString());
		
		BigDecimal price = changeEntry.getPrice() == null 
				? lagerCall.getGemittelterGestehungspreisEinesLagers(
						artikelDto.getIId(), lagerDto.getIId())
				: changeEntry.getPrice();
		List<SeriennrChargennrMitMengeDto> erpIdentities = null;
		if (artikelDto.istArtikelSnrOderchargentragend()) {
			erpIdentities = transform(changeEntry.getIdentities());
		}
		Integer movementId = lagerCall.bucheUm(artikelDto.getIId(), lagerDto.getIId(),
				targetLagerDto.getIId(), changeEntry.getAmount(),
				erpIdentities, changeEntry.getComment(), price);
		return movementId;
	}
}
