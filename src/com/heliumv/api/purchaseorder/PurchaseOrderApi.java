package com.heliumv.api.purchaseorder;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.heliumv.api.BaseApi;
import com.heliumv.api.HvValidateBadRequest;
import com.heliumv.api.HvValidateNotFound;
import com.heliumv.api.document.DocumentMetadata;
import com.heliumv.api.document.IDocumentCategoryService;
import com.heliumv.api.document.IDocumentService;
import com.heliumv.api.item.ItemIdentityEntry;
import com.heliumv.api.item.ItemIdentityEntryList;
import com.heliumv.api.supplier.SupplierEntryMapper;
import com.heliumv.api.system.CnrFormatter;
import com.heliumv.api.system.SystemService;
import com.heliumv.factory.IArtikelCall;
import com.heliumv.factory.IBestellpositionCall;
import com.heliumv.factory.IBestellungCall;
import com.heliumv.factory.IBestellungReportCall;
import com.heliumv.factory.IBestellvorschlagCall;
import com.heliumv.factory.IFinanzCall;
import com.heliumv.factory.IGlobalInfo;
import com.heliumv.factory.IJudgeCall;
import com.heliumv.factory.ILagerCall;
import com.heliumv.factory.ILieferantCall;
import com.heliumv.factory.ILocaleCall;
import com.heliumv.factory.IMandantCall;
import com.heliumv.factory.IParameterCall;
import com.heliumv.factory.IWareneingangCall;
import com.heliumv.factory.query.PurchaseOrderProposalQuery;
import com.heliumv.factory.query.PurchaseOrderQuery;
import com.heliumv.tools.CollectionTools;
import com.heliumv.tools.FilterKriteriumCollector;
import com.heliumv.tools.IDetect;
import com.heliumv.tools.ISelect;
import com.heliumv.tools.StringHelper;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.bestellung.service.BestellpositionDto;
import com.lp.server.bestellung.service.BestellpositionFac;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.bestellung.service.BestellungFac;
import com.lp.server.bestellung.service.BestellungHandlerFeature;
import com.lp.server.bestellung.service.BestellvorschlagDto;
import com.lp.server.bestellung.service.BestellvorschlagHandlerFeature;
import com.lp.server.bestellung.service.CreateBestellvorschlagDto;
import com.lp.server.bestellung.service.RueckgabeWEPMitReelIDDto;
import com.lp.server.bestellung.service.WareneingangDto;
import com.lp.server.bestellung.service.WareneingangspositionDto;
import com.lp.server.finanz.service.FinanzServiceFac;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.system.service.WechselkursDto;
import com.lp.server.util.ArtikelId;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParametersFeatures;
import com.lp.server.util.fastlanereader.service.query.QueryResult;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Service("hvPurchaseOrder")
@Path("/api/v1/purchaseorder/")
public class PurchaseOrderApi extends BaseApi implements IPurchaseOrderApi {
	private static Logger log = LoggerFactory.getLogger(PurchaseOrderApi.class) ;
	public static final int MAX_CONTENT_LENGTH_GOODSRECEIPT = 6500000 ;

	public class ApiFilter extends Filter {
		public final static String RECEIPTPOSSIBLE = BASE + "receiptpossible";
	}

	@Autowired
	private IGlobalInfo globalInfo ;
	
	@Autowired
	private IMandantCall mandantCall ;
	
	@Autowired
	private IBestellungCall bestellungCall;
	@Autowired
	private IBestellpositionCall bestellpositionCall;
	@Autowired
	private IWareneingangCall wareneingangCall ;
	@Autowired
	private ILieferantCall lieferantCall ;
	@Autowired
	private ILagerCall lagerCall;
	@Autowired
	private IArtikelCall artikelCall;
	@Autowired
	private IFinanzCall finanzCall;
	@Autowired
	private IBestellungReportCall bestellungReportCall;
	@Autowired
	private ILocaleCall localeCall;
	@Autowired
	private IParameterCall parameterCall;

	@Autowired
	private PurchaseOrderEntryMapper purchaseOrderEntryMapper;
	@Autowired
	private GoodsReceiptEntryMapper goodsReceiptEntryMapper;
	@Autowired
	private PurchaseOrderPositionEntryMapper purchaseOrderPositionEntryMapper;
	@Autowired
	private SystemService systemService;
	@Autowired
	private CnrFormatter cnrFormatter;
	@Autowired
	private SupplierEntryMapper supplierEntryMapper;
	@Autowired
	private PurchaseOrderQuery purchaseOrderQuery;
	@Autowired
	private IJudgeCall judgeCall;
	@Autowired
	private IBestellvorschlagCall bestellvorschlagCall;
	@Autowired
	private PurchaseOrderProposalQuery purchaseOrderProposalQuery;
	@Autowired
	private PurchaseOrderProposalEntryMapper purchaseOrderProposalEntryMapper;
	@Autowired
	private IDocumentService documentService;
	@Autowired
	private IDocumentCategoryService goodsReceiptDocService;

	@GET
	@Produces({FORMAT_JSON, FORMAT_XML})
	public PurchaseOrderEntryList getPurchaseOrder(
			@HeaderParam(ParamInHeader.TOKEN) String headerToken,
			@QueryParam(Param.USERID) String userId,
			@QueryParam(Param.PURCHASEORDERCNR) String purchaseOrderCnr,
			@QueryParam(Param.PURCHASEORDERID) Integer purchaseOrderId,
			@QueryParam(Param.SUPPLIERCNR) String supplierCnr,
			@QueryParam(ApiFilter.RECEIPTPOSSIBLE) Boolean filterReceiptPossible,
			@QueryParam(Add.SUPPLIERDETAIL) Boolean addSupplierDetail) throws RemoteException {
		PurchaseOrderEntryList orderEntryList = new PurchaseOrderEntryList();
		if(connectClient(headerToken, userId) == null) return orderEntryList;

		if(!StringHelper.isEmpty(purchaseOrderCnr) || purchaseOrderId != null) {
			BestellungLieferantDto dto = requireBestellungDto(
					purchaseOrderId, purchaseOrderCnr, filterReceiptPossible);
			if(dto != null) {
				PurchaseOrderEntry entry = purchaseOrderEntryMapper.mapEntry(dto.getBestellungDto());
				List<PurchaseOrderEntry> entries = new ArrayList<PurchaseOrderEntry>();
				entries.add(entry);
				orderEntryList.setEntries(entries);
			}			
		} else {
			LieferantDto[] lieferantDtos = requireLieferantDto(supplierCnr);
			if(lieferantDtos != null) {
				if(lieferantDtos.length > 1) {
					respondExpectationFailed(Param.SUPPLIERCNR, supplierCnr);
				} else {
					if(lieferantDtos.length == 1) {
						List<BestellungDto> besDtos = bestellungCall
								.bestellungFindByLieferantIIdBestelladresseMandantCNrFilter(
										lieferantDtos[0].getIId(), Boolean.TRUE.equals(filterReceiptPossible));
						List<PurchaseOrderEntry> entries = new ArrayList<PurchaseOrderEntry>();
						for (BestellungDto bestellungDto : besDtos) {
							PurchaseOrderEntry entry = purchaseOrderEntryMapper.mapEntry(bestellungDto);
							entries.add(entry);
						}
						
						orderEntryList.setEntries(entries);
					} else {
						respondNotFound(Param.SUPPLIERCNR, supplierCnr);
					} 
				}
			}
		}
		
		if (Boolean.TRUE.equals(addSupplierDetail)) {
			addSupplierDetail(orderEntryList);
		}
		
		return orderEntryList;
	}
	
	private void addSupplierDetail(PurchaseOrderEntryList orderEntryList) throws RemoteException {
		for (PurchaseOrderEntry purchaseOrder : orderEntryList.getEntries()) {
			LieferantDto lieferantDto = lieferantCall.lieferantFindByPrimaryKeyOhneExc(purchaseOrder.getSupplierId());
			if (lieferantDto != null) {
				purchaseOrder.setSupplierEntry(supplierEntryMapper.mapDetailEntry(lieferantDto));
			}
		}
	}

	@GET
	@Path("item")
	@Produces({FORMAT_JSON, FORMAT_XML})
	public PurchaseOrderPositionEntry getItemInfo(
			@HeaderParam(ParamInHeader.TOKEN) String headerToken,
			@QueryParam(Param.USERID) String userId,
			@QueryParam(Param.PURCHASEORDERID) Integer purchaseOrderId,
			@QueryParam(Param.ITEMCNR) String itemCnr,
			@QueryParam(ApiFilter.RECEIPTPOSSIBLE) Boolean filterReceiptPossible
	) throws RemoteException {
		if(purchaseOrderId == null) {
			respondBadRequestValueMissing(Param.PURCHASEORDERID);
			return null;
		}
		if(StringHelper.isEmpty(itemCnr)) {
			respondBadRequestValueMissing(Param.ITEMCNR);
			return null;
		}
		
		if(connectClient(headerToken, userId) == null) return null;
		
		BestellungLieferantDto dto = requireBestellungDto(purchaseOrderId, null, true);
		if(dto == null) {
			return null;
		}
		
		List<ArtikelDto> itemDtos = findPossibleItems(dto, itemCnr);
		BestellpositionArtikelDto besposArtikelDto = findItemInPurchaseOrder(dto, itemDtos, filterReceiptPossible);
		return besposArtikelDto != null ? 
				purchaseOrderPositionEntryMapper.mapEntry(
						besposArtikelDto.getPosDto(), besposArtikelDto.getArtikelDto()) : null;
	}
	
//	protected ItemV1Entry mapFromInternal(ItemEntryInternal itemEntryInternal) {
//		if(itemEntryInternal == null) return null ;
//		
//		ItemV1Entry itemEntry = modelMapper.map(itemEntryInternal, ItemV1Entry.class) ;
//		return itemEntry ;
//	}
//	
	private BestellpositionArtikelDto findItemInPurchaseOrder(BestellungLieferantDto dto,
			List<ArtikelDto> possibleItems, final Boolean filterReceiptPossible) throws RemoteException { 
		BestellpositionDto[] posDtos = bestellpositionCall
				.bestellpositionFindByBestellung(dto.getBestellungDto().getIId());
		final boolean[] foundButDelivered = {false};
		for (ArtikelDto itemDto : possibleItems) { 
			final ArtikelDto theItemDto = itemDto;
			BestellpositionDto foundPosDto = CollectionTools.detect(posDtos, new IDetect<BestellpositionDto>() {				
				@Override
				public boolean accept(BestellpositionDto besPosDto) {
					boolean b = theItemDto.getIId().equals(besPosDto.getArtikelIId());
					if(b && Boolean.TRUE.equals(filterReceiptPossible)) {
						if(BestellpositionFac.BESTELLPOSITIONSTATUS_ERLEDIGT
								.equals(besPosDto.getBestellpositionstatusCNr())
							|| BestellpositionFac.BESTELLPOSITIONSTATUS_GELIEFERT
								.equals(besPosDto.getBestellpositionstatusCNr())) {
							foundButDelivered[0] = true;
							b = false;
						} else { 
							foundButDelivered[0] = false;
						}
					}
					return b;
				}
			});
			
			if(foundPosDto != null) {
				respondOkay();
				return new BestellpositionArtikelDto(foundPosDto, theItemDto);
//			} else {
//				if(foundButDelivered[0]) { 
//					respondExpectationFailed();
//				}
			}
		}
		
		if(foundButDelivered[0]) {
			respondExpectationFailed();
		} else {
			respondNotFound();
		}
		
		return null;
	}
	
	private Collection<BestellpositionDto> listPurchaseOrder(BestellungLieferantDto dto,
			 final Boolean filterReceiptPossible) throws RemoteException { 
		BestellpositionDto[] posDtos = bestellpositionCall
				.bestellpositionFindByBestellung(dto.getBestellungDto().getIId());

		return CollectionTools.select(posDtos, new ISelect<BestellpositionDto>() {
			@Override
			public boolean select(BestellpositionDto element) {
				return Boolean.TRUE.equals(filterReceiptPossible) 
					? !Helper.isOneOf(element.getBestellpositionstatusCNr(),
							BestellpositionFac.BESTELLPOSITIONSTATUS_ERLEDIGT,
							BestellpositionFac.BESTELLPOSITIONSTATUS_GELIEFERT) 
					: true;
			}
		});
	}
	
	private List<ArtikelDto> createItemList(ArtikelDto artikelDto) {
		List<ArtikelDto> items = new ArrayList<ArtikelDto>();
		items.add(artikelDto);
		return items;
	}
	
	private List<ArtikelDto> findPossibleItems(BestellungLieferantDto dto, String itemCnr) throws RemoteException {
		ArtikelDto artikelDto = artikelCall
				.artikelFindByArtikelnrlieferant(itemCnr, dto.getLieferantDto().getIId());
		if(artikelDto != null) {
			return createItemList(artikelDto);
		}
		List<ArtikelDto> dtos = artikelCall.artikelFindByArtikelnrhersteller(itemCnr);
		if(dtos.size() != 0) {
			return dtos;
		}
		
		artikelDto = artikelCall.artikelFindByCNrOhneExc(itemCnr);
		if(artikelDto != null) {
			return createItemList(artikelDto);
		}
		
		respondNotFound(Param.ITEMCNR, itemCnr);
		return new ArrayList<ArtikelDto>(); 
	}
	
	@GET
	@Path("itemlist")
	@Produces({FORMAT_JSON, FORMAT_XML})
	public PurchaseOrderPositionEntryList getListItemInfo(
			@HeaderParam(ParamInHeader.TOKEN) String headerToken,
			@QueryParam(Param.USERID) String userId,
			@QueryParam(Param.PURCHASEORDERID) Integer purchaseOrderId,
			@QueryParam(ApiFilter.RECEIPTPOSSIBLE) Boolean filterReceiptPossible
	) throws RemoteException {
		if(purchaseOrderId == null) {
			respondBadRequestValueMissing(Param.PURCHASEORDERID);
			return null;
		}
		
		if(connectClient(headerToken, userId) == null) return null;
		
		BestellungLieferantDto dto = requireBestellungDto(purchaseOrderId, null, true);
		if(dto == null) {
			return null;
		}
	
		Collection<BestellpositionDto> besposDtos = listPurchaseOrder(dto, filterReceiptPossible);
		List<PurchaseOrderPositionEntry> entries = new ArrayList<PurchaseOrderPositionEntry>();
		for (BestellpositionDto posDto : besposDtos) {
			if (posDto.getArtikelIId() != null) {
				ArtikelDto artikelDto = artikelCall
						.artikelFindByPrimaryKeySmall(posDto.getArtikelIId());
				PurchaseOrderPositionEntry entry = 
						purchaseOrderPositionEntryMapper.mapEntry(posDto, artikelDto);
				entries.add(entry);
			}
		}
		
		return new PurchaseOrderPositionEntryList(entries);
	}
		
	@GET
	@Path("goodsreceipt")
	@Produces({FORMAT_JSON, FORMAT_XML})
	public GoodsReceiptEntry getGoodsReceipt(
			@HeaderParam(ParamInHeader.TOKEN) String headerToken,
			@QueryParam(Param.USERID) String userId,
			@QueryParam(Param.PURCHASEORDERCNR) String purchaseOrderCnr,
			@QueryParam(Param.PURCHASEORDERID) Integer purchaseOrderId,
			@QueryParam("deliveryslipcnr") String deliveryslipCnr,		
			@QueryParam(ApiFilter.RECEIPTPOSSIBLE) Boolean filterReceiptPossible
	) throws RemoteException {
		GoodsReceiptEntry receiptEntry = new GoodsReceiptEntry();
		
		if(StringHelper.isEmpty(deliveryslipCnr)) {
			respondBadRequestValueMissing("deliveryslipcnr");
			return receiptEntry;
		}
		
		if(connectClient(headerToken, userId) == null) return receiptEntry;

		BestellungLieferantDto dto = requireBestellungDto(
				purchaseOrderId, purchaseOrderCnr, filterReceiptPossible);
		if(dto == null) {
			return receiptEntry;
		}
		
		WareneingangDto[] weDtos = wareneingangCall.wareneingangFindByBestellungIId(dto.getBestellungDto().getIId());
		final String slipCnr = deliveryslipCnr;
		WareneingangDto weDto = CollectionTools.detect(weDtos, new IDetect<WareneingangDto>() {
			public boolean accept(WareneingangDto element) {
				return slipCnr.equals(element.getCLieferscheinnr());
			};
		});
		if(weDto == null) {
			respondNotFound("deliveryslipcnr", deliveryslipCnr);
			return receiptEntry;
		}
		
		return goodsReceiptEntryMapper.mapEntry(weDto);
	}

	@Override
	@GET
	@Path("goodsreceipts")
	@Produces({FORMAT_JSON, FORMAT_JSONutf8, FORMAT_XML})
	public GoodsReceiptEntryList getGoodsReceipts(
			@HeaderParam(ParamInHeader.TOKEN) String headerToken,
			@QueryParam(Param.USERID) String userId,
			@QueryParam(Param.PURCHASEORDERCNR) String purchaseOrderCnr,
			@QueryParam(Param.PURCHASEORDERID) Integer purchaseOrderId,
			@QueryParam("deliveryslipcnr") String deliveryslipCnr,		
			@QueryParam(ApiFilter.RECEIPTPOSSIBLE) Boolean filterReceiptPossible
	) throws RemoteException {
		GoodsReceiptEntryList receipts = new GoodsReceiptEntryList();
				
		if(connectClient(headerToken, userId) == null) return receipts;

		BestellungLieferantDto dto = requireBestellungDto(
				purchaseOrderId, purchaseOrderCnr, filterReceiptPossible);
		if(dto == null) {
			return receipts;
		}
		
		WareneingangDto[] weDtos = wareneingangCall.wareneingangFindByBestellungIId(dto.getBestellungDto().getIId());
		Collection<WareneingangDto> wes = Arrays.asList(weDtos);
		if (StringHelper.hasContent(deliveryslipCnr)) {
			final String slipCnr = deliveryslipCnr;
			wes = CollectionTools.select(weDtos, new ISelect<WareneingangDto>() {
				@Override
				public boolean select(WareneingangDto element) {
					return slipCnr.equals(element.getCLieferscheinnr());
				}
			});			
		}

		receipts.setEntries(goodsReceiptEntryMapper.mapEntries(wes));
		return receipts;
	}

	@POST
	@Path("goodsreceipt")
	@Produces({FORMAT_JSON, FORMAT_XML})
	@Consumes({FORMAT_JSON, FORMAT_XML})
	public CreatedGoodsReceiptPositionEntry createGoodsReceipt(
			@HeaderParam(ParamInHeader.TOKEN) String headerToken,
			@QueryParam(Param.USERID) String userId,
			@QueryParam(ApiFilter.RECEIPTPOSSIBLE) Boolean filterReceiptPossible,
			CreateGoodsReceiptPositionEntry createEntry
	) throws RemoteException {
		CreatedGoodsReceiptPositionEntry receiptEntry = new CreatedGoodsReceiptPositionEntry();
		
		if(createEntry == null) {
			respondBadRequestValueMissing("createGoodsReceiptEntry");
			return receiptEntry;
		}
		if(createEntry.getPurchaseOrderPositionId() == null) {
			respondBadRequestValueMissing("purchaseOrderPositionId");
			return receiptEntry;
		}
		if(StringHelper.isEmpty(createEntry.getDeliverySlipCnr()) &&
				createEntry.getGoodsReceiptId() == null) {
			respondBadRequestValueMissing("goodsReceiptId");
			return receiptEntry;
		}
		
		if(connectClient(headerToken, userId) == null) return receiptEntry;

		BestellpositionDto posDto = bestellpositionCall
				.bestellpositionFindByPrimaryKey(createEntry.getPurchaseOrderPositionId());
		if(posDto == null) {
			respondNotFound("purchaseOrderPositionId",
					createEntry.getPurchaseOrderPositionId().toString());
			return receiptEntry;
		}
		
		BestellungLieferantDto dto = requireBestellungDto(
				posDto.getBestellungIId(), null, filterReceiptPossible);
		if(dto == null) {
			return receiptEntry;
		}
		
		WareneingangDto weDto = null;
		if(createEntry.getGoodsReceiptId() == null) {
			List<WareneingangDto> weDtos = wareneingangCall
					.wareneingangFindByLieferscheinnummer(
							dto.getBestellungDto().getIId(), createEntry.getDeliverySlipCnr());
			if(weDtos.size() > 0) { 
				weDto = weDtos.get(0);
			}
		} else {
			weDto = wareneingangCall.wareneingangFindByPrimaryKeyOhneExc(createEntry.getGoodsReceiptId());
		}
		
		if(weDto == null && StringHelper.hasContent(createEntry.getDeliverySlipCnr())) {
			weDto = newWareneingangDto(dto, createEntry.getDeliverySlipCnr());
			Integer weId = wareneingangCall.createWareneingang(weDto);
			weDto = wareneingangCall.wareneingangFindByPrimaryKeyOhneExc(weId);
		}
		
		if(createEntry.getItemIdentity() == null) {
			createEntry.setItemIdentity(new ItemIdentityEntryList());
		}

		if(weDto == null) {
			String msg = createEntry.getDeliverySlipCnr() + "|" 
					+ createEntry.getGoodsReceiptId().toString() + "|";
			log.error("Couldn't find WE (" + msg + ")");
			respondExpectationFailed("we-not-found", msg);
			return receiptEntry;
		}
		
		WareneingangspositionDto knownWePosDto = findWeposition(
				weDto.getIId(), createEntry.getPurchaseOrderPositionId());
		BestellpositionDto bestellpositionDto = null;
		if(knownWePosDto == null) {
			bestellpositionDto = bestellpositionCall
					.bestellpositionFindByPrimaryKey(createEntry.getPurchaseOrderPositionId());
		}
		WareneingangspositionDto weposDto = null;
		weposDto = knownWePosDto != null 
				? updateWareneingangspositionDto(knownWePosDto, createEntry)   
				: newWareneingangpositionDto(weDto, bestellpositionDto, createEntry);
				
		if(weposDto == null) {
			return receiptEntry;
		}
		Integer modifiedWePosId = weposDto.getIId();
		if(weposDto.getIId() == null) {
			modifiedWePosId = wareneingangCall.createWareneingangsposition(weposDto);
		} else {
			wareneingangCall.updateWareneingangsposition(weposDto, false);
		}
		
		bestellungReportCall.printWepEtikett(modifiedWePosId);
		
		BestellpositionDto besposDto = bestellpositionCall
				.bestellpositionFindByPrimaryKey(createEntry.getPurchaseOrderPositionId());
		besposDto.getNOffeneMenge();
		receiptEntry.setOpenQuantity(besposDto.getNOffeneMenge());
		receiptEntry.setGoodsReceiptId(weDto.getIId());
		receiptEntry.setGoodsReceiptPositionId(modifiedWePosId);
		receiptEntry.setPurchaseOrderPositionId(createEntry.getPurchaseOrderPositionId());
		
		return receiptEntry;
	}

	private WareneingangspositionDto findWeposition(final Integer wareneingangId, Integer bestellpositionId) throws RemoteException {		
		WareneingangspositionDto[] knownWePosDtos = wareneingangCall
				.wareneingangspositionFindByBestellpositionIId(bestellpositionId);
		WareneingangspositionDto knownWePosDto = CollectionTools.detect(
				knownWePosDtos, new IDetect<WareneingangspositionDto>() {
			@Override
			public boolean accept(WareneingangspositionDto element) {
				return element.getWareneingangIId().equals(wareneingangId);
			}			
		});
		
		return knownWePosDto;
	}
	
	private WareneingangspositionDto newWareneingangpositionDto(
			WareneingangDto weDto, BestellpositionDto bestellpositionDto, CreateGoodsReceiptPositionEntry createEntry) {
		WareneingangspositionDto posDto = new WareneingangspositionDto();
		posDto.setWareneingangIId(weDto.getIId());
		posDto.setBestellpositionIId(createEntry.getPurchaseOrderPositionId());
		posDto.setNGeliefertemenge(createEntry.getAmount());
//		posDto.setNAnteiligefixkosten(BigDecimal.ZERO);
//		posDto.setNAnteiligetransportkosten(BigDecimal.ZERO);
		posDto.setNGelieferterpreis(bestellpositionDto.getNNettogesamtpreis());
//		posDto.setNRabattwert(BigDecimal.ZERO);
//		posDto.setNEinstandspreis(BigDecimal.ZERO);
//		posDto.setNFixkosten4Bestellposition(BigDecimal.ZERO);
		List<SeriennrChargennrMitMengeDto> snrDtos = new ArrayList<SeriennrChargennrMitMengeDto>();
		if(!addItemIdentities(snrDtos, createEntry.getItemIdentity())) {
			return null;
		}
		posDto.setSeriennrChargennrMitMenge(snrDtos);
		return posDto;
	}
	
	private WareneingangspositionDto updateWareneingangspositionDto(
			WareneingangspositionDto posDto, CreateGoodsReceiptPositionEntry createEntry) {
		posDto.setNGeliefertemenge(posDto.getNGeliefertemenge().add(createEntry.getAmount()));
		if(posDto.getNGeliefertemenge().signum() < 0) {
			respondExpectationFailed("identityEntry.amount", "<0");
			return null;
		}
		
		if(createEntry.getAmount().signum() >= 0) {
			if(!addItemIdentities(posDto.getSeriennrChargennrMitMenge(), createEntry.getItemIdentity())) {
				return null;
			}			
		} else {
			if(!removeItemIdentities(posDto.getSeriennrChargennrMitMenge(), createEntry.getItemIdentity())) {
				return null;
			}
		}
		return posDto;
	}
	
	private boolean addItemIdentities(
			List<SeriennrChargennrMitMengeDto> snrDtos, ItemIdentityEntryList identities) {
		for(ItemIdentityEntry identityEntry : identities.getEntries()) {
			if(identityEntry.getAmount() == null) {
				respondBadRequestValueMissing("identityEntry.amount"); 
				return false ;
			}
			if(StringHelper.isEmpty(identityEntry.getIdentity())) {
				respondBadRequestValueMissing("identityEntry.identity"); 
				return false ;
			}
			
			if(identityEntry.getAmount().signum() > 0) {
				SeriennrChargennrMitMengeDto dto = new SeriennrChargennrMitMengeDto() ;
				dto.setNMenge(identityEntry.getAmount());
				dto.setCSeriennrChargennr(identityEntry.getIdentity());
				snrDtos.add(dto) ;				
			} else {
				respondBadRequest("identityEntry.amount",
						identityEntry.getAmount().toPlainString());
				return false ;
			}
		}

		return true ;
	}
	
	private boolean removeItemIdentities(
			List<SeriennrChargennrMitMengeDto> snrDtos, ItemIdentityEntryList identities) {
		for(ItemIdentityEntry identityEntry : identities.getEntries()) {
			if(identityEntry.getAmount() == null) {
				respondBadRequestValueMissing("identityEntry.amount"); 
				return false ;
			}
			if(StringHelper.isEmpty(identityEntry.getIdentity())) {
				respondBadRequestValueMissing("identityEntry.identity"); 
				return false ;
			}
			
			if(identityEntry.getAmount().signum() < 0) {
				if(!removeIdentity(snrDtos, identityEntry.getIdentity(), identityEntry.getAmount().abs())) {
					respondBadRequest("identityEntry.amount",
							identityEntry.getAmount().toPlainString());
					return false ;					
				}				
			} else {
				respondBadRequest("identityEntry.amount",
						identityEntry.getAmount().toPlainString());
				return false ;
			}
		}

		filterZeroAmount(snrDtos);
		return true ;
	}

	
	private boolean removeIdentity(List<SeriennrChargennrMitMengeDto> snrs, String identity, BigDecimal amount) {
		for (SeriennrChargennrMitMengeDto snr : snrs) {
			if(identity.equals(snr.getCSeriennrChargennr())) {
				BigDecimal removedAmount = amount.min(snr.getNMenge());
				snr.setNMenge(snr.getNMenge().subtract(removedAmount));
				amount = amount.subtract(removedAmount);
				if(amount.signum() == 0) break;
			}
		}

		return amount.signum() == 0;
	}
	
	private Collection<SeriennrChargennrMitMengeDto> filterZeroAmount(List<SeriennrChargennrMitMengeDto> snrs) {
		Iterator<SeriennrChargennrMitMengeDto> it = snrs.iterator();
		while(it.hasNext()) {
			SeriennrChargennrMitMengeDto elem = it.next(); 
			if(elem.getNMenge().signum() == 0) {
				it.remove();
			}
		}
		return snrs;
//		return CollectionTools.reject(snrs, new IReject<SeriennrChargennrMitMengeDto>() {
//			@Override
//			public boolean reject(SeriennrChargennrMitMengeDto element) {
//				return element.getNMenge().signum() == 0;
//			}
//		});
	}
	
	@GET
	@Path("goodsreceipt/{receiptid}/position")
	@Produces({FORMAT_JSON, FORMAT_XML})
	public GoodsReceiptPositionEntryList getGoodsReceiptPositions(
			@PathParam(Param.RECEIPTID) Integer goodsreceiptId,
			@HeaderParam(ParamInHeader.TOKEN) String headerToken,
			@QueryParam(Param.USERID) String userId
	) throws RemoteException {
		GoodsReceiptPositionEntryList entries = new GoodsReceiptPositionEntryList();
		if(connectClient(headerToken, userId) == null) return entries;
		
		WareneingangspositionDto[] dtos = wareneingangCall
				.wareneingangspositionFindByWareneingangIId(goodsreceiptId);
		if(dtos.length > 0) {
			if(!isValidWareneingang(dtos, Boolean.TRUE)) {
				return entries;
			}
			
			entries.setEntries(goodsReceiptEntryMapper.mapEntry(dtos));
		}
		
		return entries;
	}
	
	@GET
	@Path("goodsreceipt/{receiptid}/position/{positionid}/print")
	@Produces({FORMAT_JSON, FORMAT_XML})
	public Integer printGoodsReceiptPosition(
			@PathParam(Param.RECEIPTID) Integer goodsreceiptId,
			@PathParam("positionid") Integer goodsreceiptPositionId,
			@HeaderParam(ParamInHeader.TOKEN) String headerToken,
			@QueryParam(Param.USERID) String userId,
			@QueryParam(Param.CHARGENR) String identity
	) throws RemoteException {
		if(goodsreceiptId == null) {
			respondBadRequestValueMissing(Param.RECEIPTID);
			return null;
		}
		if(goodsreceiptPositionId == null) {
			respondBadRequestValueMissing("positionId");
			return null;
		}
		
		if(connectClient(headerToken, userId) == null) return null;
		
		WareneingangspositionDto posDto = wareneingangCall.wareneingangspositionFindByPrimaryKey(goodsreceiptPositionId);
		if(!goodsreceiptId.equals(posDto.getWareneingangIId())) {
			respondNotFound("positionid", goodsreceiptPositionId.toString());
			return null;
		}
		
		bestellungReportCall.printWepEtikett(posDto.getIId(), identity);
		
		return posDto.getIId();
	}
	
	private boolean isValidWareneingang(WareneingangspositionDto[] posDtos,
			Boolean filterReceiptPossible) throws RemoteException {
		if(posDtos == null || posDtos.length == 0) {
			return true;
		}
		
		WareneingangDto weDto = wareneingangCall
				.wareneingangFindByPrimaryKeyOhneExc(posDtos[0].getWareneingangIId());
		if(weDto == null) {
			respondNotFound();
			return false;
		}
		
		return requireBestellungDto(weDto.getBestellungIId(), null, filterReceiptPossible) != null;
	}
	
	private WareneingangDto newWareneingangDto(
			BestellungLieferantDto dto, String deliveryslipCnr) throws RemoteException {
		WareneingangDto weDto = new WareneingangDto();
		weDto.setBestellungIId(dto.getBestellungDto().getIId());
		weDto.setLagerIId(dto.getLieferantDto().getLagerIIdZubuchungslager());
		weDto.setCLieferscheinnr(deliveryslipCnr);
		Timestamp t = Helper.cutTimestamp(new Timestamp(System.currentTimeMillis()));
		weDto.setTWareneingangsdatum(t);
		weDto.setTLieferscheindatum(t);
		
		WechselkursDto wechselkursDto = localeCall.getKursZuDatum(
				dto.getBestellungDto().getWaehrungCNr(), new Date(t.getTime()));
		weDto.setNWechselkurs(wechselkursDto.getNKurs());		
		weDto.setDGemeinkostenfaktor(parameterCall.getGemeinkostenFaktor(t));

		return weDto;
	}
	
	private LieferantDto[] requireLieferantDto(String lieferantCnr) throws RemoteException {
		if(StringHelper.isEmpty(lieferantCnr)) {
			respondBadRequestValueMissing(Param.SUPPLIERCNR);
			return null;
		}
		
		KontoDto kontoDto = finanzCall
				.kontoFindByCnrKontotypMandantOhneExc(
						lieferantCnr, FinanzServiceFac.KONTOTYP_KREDITOR);
		if(kontoDto == null) {
			respondNotFound(Param.SUPPLIERCNR, lieferantCnr);
			return null;
		}
		
		return lieferantCall.lieferantFindByKontoId(kontoDto.getIId());
	}
	
	/**
	 * Ermittelt Bestellung und Lieferant fuer die Bestellung-Id.
	 * Die Bestellung muss im geforderten Zustand sein (filterReceiptPossible),
	 * ausserdem muss der Anwender das Recht haben auf das durch den Lieferanten  
	 * geforderte Lager buchen zu d&uuml;rfen.
	 * @param purchaseOrderId die Id der Bestellung
	 * @param purchaseOrderCnr die Cnr der Bestellung
	 * @param filterReceiptPossible true wenn die gesuchte Bestellung noch einen Wareneingang zulaesst
	 * @return Bestellung und Lieferant, sofern die BestellId/Cnr zulaessig ist
	 * @throws RemoteException
	 */
	private BestellungLieferantDto requireBestellungDto(Integer purchaseOrderId,
			String purchaseOrderCnr, Boolean filterReceiptPossible) throws RemoteException {
		if(!mandantCall.hasModulBestellung()) {
			respondNotFound();
			return null;
		}
		
		String origPurchaseOrderCnr = purchaseOrderCnr;
		purchaseOrderCnr = cnrFormatter.trimToCnrLength(purchaseOrderCnr);
		if (origPurchaseOrderCnr != null 
				&& !origPurchaseOrderCnr.equals(purchaseOrderCnr)) {
			log.info(Param.PURCHASEORDERCNR + " '" + origPurchaseOrderCnr + "' cut to '" + purchaseOrderCnr + "'");
		}
		
		BestellungDto dto = new BestellungFinder(this)
				.find(purchaseOrderId, purchaseOrderCnr, Param.PURCHASEORDERCNR);
		if(dto == null) { 
			return null;
		}
		
		if(!isValidPurchaseOrder(dto, filterReceiptPossible)) {
			return null;
		}
		
		LieferantDto lieferantDto = lieferantCall
				.lieferantFindByPrimaryKeyOhneExc(dto.getLieferantIIdBestelladresse());
		if(lieferantDto == null) { 
			respondNotFound();
			return null;
		}
		
		if(!lagerCall.hatRolleBerechtigungAufLager(
				lieferantDto.getLagerIIdZubuchungslager())) {
			respondNotFound();
			return null;
		}
		
		BestellungLieferantDto blDto = new BestellungLieferantDto(dto);
		blDto.setLieferantDto(lieferantCall
				.lieferantFindByPrimaryKeyOhneExc(dto.getLieferantIIdBestelladresse()));
		return blDto;		
	}
	
	private boolean isValidPurchaseOrder(BestellungDto dto, Boolean filterReceiptPossible) {
		if(dto == null) {
			return false;
		}

		if(Boolean.TRUE.equals(filterReceiptPossible)) {
			boolean statusOk = Helper.isOneOf(dto.getStatusCNr(),
					BestellungFac.BESTELLSTATUS_OFFEN, BestellungFac.BESTELLSTATUS_BESTAETIGT,
					BestellungFac.BESTELLSTATUS_TEILERLEDIGT);
			if(!statusOk) {
				respondExpectationFailed("status", 
						PurchaseOrderStatus.fromString(dto.getStatusCNr()).toString());
				return false;
			}
		}

		return true;
	}
	

	private class BestellungLieferantDto {
		private BestellungDto bestellungDto;
		private LieferantDto lieferantDto;
		
		public BestellungLieferantDto(BestellungDto bestellungDto) {
			setBestellungDto(bestellungDto);
		}

		public BestellungDto getBestellungDto() {
			return bestellungDto;
		}

		public void setBestellungDto(BestellungDto bestellungDto) {
			this.bestellungDto = bestellungDto;
		}

		public LieferantDto getLieferantDto() {
			return lieferantDto;
		}

		public void setLieferantDto(LieferantDto lieferantDto) {
			this.lieferantDto = lieferantDto;
		}
	}
	
	private class BestellpositionArtikelDto {
		private BestellpositionDto posDto;
		private ArtikelDto artikelDto;
		
		public BestellpositionArtikelDto() {
		}
		
		public BestellpositionArtikelDto(BestellpositionDto posDto, ArtikelDto artikelDto) {
			setPosDto(posDto);
			setArtikelDto(artikelDto);
		}
		
		public BestellpositionDto getPosDto() {
			return posDto;
		}
		public void setPosDto(BestellpositionDto posDto) {
			this.posDto = posDto;
		}
		public ArtikelDto getArtikelDto() {
			return artikelDto;
		}
		public void setArtikelDto(ArtikelDto artikelDto) {
			this.artikelDto = artikelDto;
		}
		
	}
	private class BestellungFinder extends FinderIdCnr<BestellungDto> {
		public BestellungFinder(BaseApi api) {
			 super(api);
		}
		
		@Override
		protected BestellungDto findById(Integer id) throws RemoteException {
			return bestellungCall.bestellungFindByPrimaryKeyOhneExc(id);
		}
		
		@Override
		protected BestellungDto findByCnr(String cnr) throws RemoteException {
			return bestellungCall.bestellungFindByCNrMandantCNr(cnr);
		}

		@Override
		protected boolean accept(BestellungDto entity) throws RemoteException {
			return entity.getMandantCNr().equals(globalInfo.getMandant())
					&& (!BestellungFac.BESTELLUNGART_RAHMENBESTELLUNG_C_NR.equals(entity.getBestellungartCNr()));
		}
	}
	
	@POST
	@Path("goodsreceiptreel")
	@Produces({FORMAT_JSON, FORMAT_XML})
	@Consumes({FORMAT_JSON, FORMAT_XML})
	public CreatedGoodsReceiptPositionReelEntry createGoodsReceiptReel(
			@HeaderParam(ParamInHeader.TOKEN) String headerToken,
			@QueryParam(Param.USERID) String userId,
			@QueryParam(ApiFilter.RECEIPTPOSSIBLE) Boolean filterReceiptPossible,
			CreateGoodsReceiptPositionReelEntry createEntry
	) throws RemoteException {
		CreatedGoodsReceiptPositionReelEntry receiptEntry = new CreatedGoodsReceiptPositionReelEntry();
		
		if(createEntry == null) {
			respondBadRequestValueMissing("createGoodsReceiptReelEntry");
			return receiptEntry;
		}
		if(createEntry.getPurchaseOrderPositionId() == null) {
			respondBadRequestValueMissing("purchaseOrderPositionId");
			return receiptEntry;
		}
		if(StringHelper.isEmpty(createEntry.getDeliverySlipCnr()) &&
				createEntry.getGoodsReceiptId() == null) {
			respondBadRequestValueMissing("goodsReceiptId");
			return receiptEntry;
		}
		
		if(connectClient(headerToken, userId) == null) return receiptEntry;

		BestellpositionDto posDto = bestellpositionCall
				.bestellpositionFindByPrimaryKey(createEntry.getPurchaseOrderPositionId());
		if(posDto == null) {
			respondNotFound("purchaseOrderPositionId",
					createEntry.getPurchaseOrderPositionId().toString());
			return receiptEntry;
		}
		
		BestellungLieferantDto dto = requireBestellungDto(
				posDto.getBestellungIId(), null, filterReceiptPossible);
		if(dto == null) {
			return receiptEntry;
		}
		
		WareneingangDto weDto = null;
		if(createEntry.getGoodsReceiptId() == null) {
			List<WareneingangDto> weDtos = wareneingangCall
					.wareneingangFindByLieferscheinnummer(
							dto.getBestellungDto().getIId(), createEntry.getDeliverySlipCnr());
			if(weDtos.size() > 0) { 
				weDto = weDtos.get(0);
			}
		} else {
			weDto = wareneingangCall.wareneingangFindByPrimaryKeyOhneExc(createEntry.getGoodsReceiptId());
		}
		
		if(weDto == null && StringHelper.hasContent(createEntry.getDeliverySlipCnr())) {
			weDto = newWareneingangDto(dto, createEntry.getDeliverySlipCnr());
			Integer weId = wareneingangCall.createWareneingang(weDto);
			weDto = wareneingangCall.wareneingangFindByPrimaryKeyOhneExc(weId);
			if(weDto == null) {
				log.error("Couldn't find created WE for Id '" + weId.toString() + "'.");
				respondExpectationFailed("weId", weId.toString());
				return receiptEntry;
			}
		}
		
		if(weDto == null) {
			String msg = createEntry.getDeliverySlipCnr() + "|" 
					+ createEntry.getGoodsReceiptId().toString() + "|";
			log.error("Couldn't find WE (" + msg + ")");
			respondExpectationFailed("we-not-found", msg);
			return receiptEntry;
		}
		
		RueckgabeWEPMitReelIDDto rc = wareneingangCall.wareneingangspositionMitReelIDBuchen(weDto.getIId(),
				posDto.getIId(), createEntry.getAmount(), createEntry.getDateCode(), createEntry.getExpirationDate());
		
		bestellungReportCall.printWepEtikett(rc.getWeposIId(), rc.getChargennummer());
	
		systemService.logBarcode(rc.getChargennummer(), createEntry.getBarcode());
		
		BestellpositionDto besposDto = bestellpositionCall
				.bestellpositionFindByPrimaryKey(createEntry.getPurchaseOrderPositionId());
		receiptEntry.setOpenQuantity(besposDto.getNOffeneMenge());
		receiptEntry.setGoodsReceiptId(weDto.getIId());
		receiptEntry.setPurchaseOrderPositionId(createEntry.getPurchaseOrderPositionId());
		receiptEntry.setGoodsReceiptPositionId(rc.getWeposIId());
		receiptEntry.setIdentity(rc.getChargennummer());

	
		return receiptEntry;
	}
	
	@GET
	@Path("/list")
	@Produces({FORMAT_JSON, FORMAT_XML})
	@Override
	public PurchaseOrderEntryList getPurchaseOrderList(
			@QueryParam(Param.USERID) String userId,
			@QueryParam(Param.LIMIT) Integer limit,
			@QueryParam(Param.STARTINDEX) Integer startIndex,
			@QueryParam(Filter.CNR) String filterCnr,
			@QueryParam(Filter.STATUS) String filterStatus,
			@QueryParam(Filter.SUPPLIER) String filterSupplier, 
			@QueryParam(Filter.ITEMCNR) String filterItemCnr) throws RemoteException, NamingException {
		PurchaseOrderEntryList entryList = new PurchaseOrderEntryList();
		
		if (connectClient(userId) == null) return entryList;
		
		if (!judgeCall.hasBestellungCRUD()) {
			respondUnauthorized();
			return entryList;
		}
		
		FilterKriteriumCollector collector = new FilterKriteriumCollector();
		collector.add(buildFilterStatus(filterStatus));
		collector.add(purchaseOrderQuery.getFilterCnr(filterCnr));
		collector.add(purchaseOrderQuery.getFilterSuppliername(filterSupplier));
		collector.add(purchaseOrderQuery.getFilterItemCnr(filterItemCnr));
				
		QueryParametersFeatures params = purchaseOrderQuery.getFeatureQueryParameters(collector);
		params.setLimit(limit);
		params.setKeyOfSelectedRow(startIndex);
		params.addFeature(BestellungHandlerFeature.BESTELLUNG_DATA);
		
		QueryResult result = purchaseOrderQuery.setQuery(params);
		entryList.setEntries(purchaseOrderQuery.getResultList(result));
		
		return entryList;
	}

	private FilterKriterium buildFilterStatus(String filterStatus) {
		if(StringHelper.isEmpty(filterStatus)) return null;
		
		List<PurchaseOrderStatus> restStatus = new ArrayList<PurchaseOrderStatus>();
		String[] tokens = filterStatus.trim().split(",");
		for (String statusString : tokens) {
			restStatus.add(PurchaseOrderStatus.lookup(statusString));
		}
		
		return purchaseOrderQuery.getFilterStatus(restStatus);
	}
	
	@GET
	@Path("proposal")
	@Produces({FORMAT_JSON, FORMAT_XML})
	@Override
	public PurchaseOrderProposalPositionEntryList getPurchaseOrderProposalPositionList(
			@QueryParam(Param.USERID) String userId,
			@QueryParam(Param.LIMIT) Integer limit,
			@QueryParam(Param.STARTINDEX) Integer startIndex,
			@QueryParam(Filter.ITEMCNR) String filterItemCnr,
			@QueryParam(Filter.NOTED) Boolean filterNoted) throws RemoteException, NamingException {
		PurchaseOrderProposalPositionEntryList entryList = new PurchaseOrderProposalPositionEntryList();
		
		if (connectClient(userId) == null) return entryList;
		
		if (!judgeCall.hasBestellungCRUD()) {
			respondUnauthorized();
			return entryList;
		}
		
		FilterKriteriumCollector collector = new FilterKriteriumCollector();
		collector.add(purchaseOrderProposalQuery.getFilterItemCnr(filterItemCnr));
		collector.add(purchaseOrderProposalQuery.getFilterNoted(filterNoted));
		
		QueryParametersFeatures params = purchaseOrderProposalQuery.getFeatureQueryParameters(collector);
		params.setLimit(limit);
		params.setKeyOfSelectedRow(startIndex);
		params.addFeature(BestellvorschlagHandlerFeature.BESTELLVORSCHLAG_DATA);
		
		QueryResult result = purchaseOrderProposalQuery.setQuery(params);
		entryList.setEntries(purchaseOrderProposalQuery.getResultList(result));
		entryList.setRowCount(entryList.getEntries().size());
		
		return entryList;
	}
	
	@POST
	@Path("proposalposition")
	@Consumes({FORMAT_JSON, FORMAT_XML})
	@Produces({FORMAT_JSON, FORMAT_XML})
	public CreatedPurchaseOrderProposalPositionEntry createPurchaseOrderProposalPosition(
			@QueryParam(Param.USERID) String userId,
			CreatePurchaseOrderProposalPositionEntry createEntry) throws EJBExceptionLP, RemoteException {
		if (connectClient(userId) == null) return new CreatedPurchaseOrderProposalPositionEntry();
		
		HvValidateBadRequest.notNull(createEntry, "createPurchaseOrderProposalPositionEntry");
		HvValidateBadRequest.notNull(createEntry.getAmount(), "amount");

		ArtikelDto artikelDto = findArtikelByIdCnr(createEntry.getItemId(), createEntry.getItemCnr());
		createEntry.setItemId(artikelDto.getIId());
		
		if (Boolean.TRUE.equals(createEntry.isNoted())) {
			List<BestellvorschlagDto> currentItemProposals = 
					bestellvorschlagCall.bestellvorschlagFindByArtikelIdVormerkungMandantCNr(createEntry.getItemId());
			if (!currentItemProposals.isEmpty()) {
				respondExpectationFailedAdditionalHeader("proposalpositionId", currentItemProposals.get(0).getIId().toString());
				return null;
			}
		}
		
		bestellvorschlagCall.pruefeBearbeitenDesBestellvorschlagsErlaubt();
		try {
			Integer createdId = bestellvorschlagCall.createBestellvorschlag(newCreateBestellvorschlagDto(createEntry));
			CreatedPurchaseOrderProposalPositionEntry createdEntry = new CreatedPurchaseOrderProposalPositionEntry();
			createdEntry.setProposalPositionId(createdId);
			return createdEntry;
		} finally {
			bestellvorschlagCall.removeLockDesBestellvorschlagesWennIchIhnSperre();
		}
	}

	private ArtikelDto findArtikelByIdCnr(Integer artikelId, String artikelCnr) throws RemoteException {
		HvValidateBadRequest.notValid(artikelId != null || !StringHelper.isEmpty(artikelCnr), "itemId or itemCnr");
		
		ArtikelDto artikelDto = null ;
		if(artikelId != null) {
			artikelDto = artikelCall.artikelFindByPrimaryKeySmallOhneExc(artikelId) ;
			HvValidateNotFound.notNull(artikelDto, "itemId", artikelId);
		}
		
		if(artikelDto == null && !StringHelper.isEmpty(artikelCnr)) {
			artikelDto = artikelCall.artikelFindByCNrOhneExc(artikelCnr) ;
			HvValidateNotFound.notNull(artikelDto, "itemCnr", artikelCnr);
		}
		
		return artikelDto ;
	}

	private CreateBestellvorschlagDto newCreateBestellvorschlagDto(CreatePurchaseOrderProposalPositionEntry createEntry) {
		CreateBestellvorschlagDto createDto = new CreateBestellvorschlagDto(new ArtikelId(createEntry.getItemId()), createEntry.getAmount());
		createDto.setLiefertermin(createEntry.getDeliveryDateMs() != null 
				? new Timestamp(createEntry.getDeliveryDateMs()) 
				: null);
		createDto.setVormerkung(Boolean.TRUE.equals(createEntry.isNoted()));
		return createDto;
	}
	
	@PUT
	@Path("proposalposition/{" + Param.PROPOSALPOSITIONID + "}")
	@Consumes({FORMAT_JSON, FORMAT_XML})
	public void updatePurchaseOrderProposalPosition(
			@QueryParam(Param.USERID) String userId,
			@PathParam(Param.PROPOSALPOSITIONID) Integer proposalPositionId,
			UpdatePurchaseOrderProposalPositionEntry updateEntry) throws EJBExceptionLP, RemoteException {
		if (connectClient(userId) == null) return;
		
		HvValidateBadRequest.notNull(proposalPositionId, "proposalPositionId");
		HvValidateBadRequest.notNull(updateEntry, "createPurchaseOrderProposalPositionEntry");
		HvValidateBadRequest.notNull(updateEntry.getAmount(), "amount");

		BestellvorschlagDto bestellvorschlagDto = bestellvorschlagCall.bestellvorschlagFindByPrimaryKeyOhneExc(proposalPositionId);
		HvValidateNotFound.notNull(bestellvorschlagDto, Param.PROPOSALPOSITIONID, proposalPositionId);
		
		mapBestellvorschlagDto(bestellvorschlagDto, updateEntry);
		bestellvorschlagCall.pruefeBearbeitenDesBestellvorschlagsErlaubt();
		try {
			bestellvorschlagCall.updateBestellvorschlag(bestellvorschlagDto);
		} finally {
			bestellvorschlagCall.removeLockDesBestellvorschlagesWennIchIhnSperre();
		}
	}

	private void mapBestellvorschlagDto(BestellvorschlagDto bestellvorschlagDto,
			UpdatePurchaseOrderProposalPositionEntry updateEntry) {
		bestellvorschlagDto.setNZubestellendeMenge(updateEntry.getAmount());
		if (updateEntry.getDeliveryDateMs() != null) {
			bestellvorschlagDto.setTLiefertermin(new Timestamp(updateEntry.getDeliveryDateMs()));
		}
	}
	
	@DELETE
	@Path("proposalposition/{" + Param.PROPOSALPOSITIONID + "}")
	@Override
	public void removePurchaseOrderProposalPosition(
			@QueryParam(Param.USERID) String userId,
			@PathParam(Param.PROPOSALPOSITIONID) Integer proposalPositionId) throws EJBExceptionLP, RemoteException {
		if (connectClient(userId) == null) return;
		
		HvValidateBadRequest.notNull(proposalPositionId, "proposalPositionId");

		BestellvorschlagDto bestellvorschlagDto = bestellvorschlagCall.bestellvorschlagFindByPrimaryKeyOhneExc(proposalPositionId);
		HvValidateNotFound.notNull(bestellvorschlagDto, Param.PROPOSALPOSITIONID, proposalPositionId);
		
		bestellvorschlagCall.pruefeBearbeitenDesBestellvorschlagsErlaubt();
		try {
			bestellvorschlagCall.removeBestellvorschlag(proposalPositionId);
		} finally {
			bestellvorschlagCall.removeLockDesBestellvorschlagesWennIchIhnSperre();
		}
	}
	
	@GET
	@Path("proposalposition/{" + Param.PROPOSALPOSITIONID + "}")
	@Produces({FORMAT_JSON, FORMAT_XML})
	@Override
	public PurchaseOrderProposalPositionEntry getPurchaseOrderProposalPosition(
			@QueryParam(Param.USERID) String userId,
			@PathParam(Param.PROPOSALPOSITIONID) Integer proposalPositionId) {
		if (connectClient(userId) == null) return null;
		
		HvValidateBadRequest.notNull(proposalPositionId, "proposalPositionId");

		BestellvorschlagDto bestellvorschlagDto = bestellvorschlagCall.bestellvorschlagFindByPrimaryKeyOhneExc(proposalPositionId);
		HvValidateNotFound.notNull(bestellvorschlagDto, Param.PROPOSALPOSITIONID, proposalPositionId);
		
		PurchaseOrderProposalPositionEntry entry = purchaseOrderProposalEntryMapper.mapEntry(bestellvorschlagDto);
		return entry;
	}
	
	private String buildConnectId(String userId, String headerToken) {
		return StringHelper.isEmpty(userId) ? headerToken : userId;
	}
	
	@Override
	@POST
	@Path("goodsreceipt/{receiptid}/document")
	public void createDocument(
			@PathParam(Param.RECEIPTID) Integer goodsreceiptId,
			@HeaderParam(ParamInHeader.TOKEN) String headerToken,
			@QueryParam(Param.USERID) String userId,
			@QueryParam(Param.TYPE) String type,
			@QueryParam(Param.KEYWORDS) String keywords,
			@QueryParam(Param.GROUPING) String grouping,
			@QueryParam(Param.SECURITYLEVEL) Long securitylevel,
			MultipartBody body) throws RemoteException, NamingException {
		if(connectClient(buildConnectId(userId, headerToken), 
				MAX_CONTENT_LENGTH_GOODSRECEIPT) == null) {
			return;
		}

		HvValidateBadRequest.notNull(goodsreceiptId, Param.RECEIPTID);
		HvValidateBadRequest.notNull(body, "body"); 
	
		WareneingangDto dto = wareneingangCall.wareneingangFindByPrimaryKeyOhneExc(goodsreceiptId);
		HvValidateNotFound.notNull(dto, Param.RECEIPTID, goodsreceiptId);
		
		for (Attachment attachment : body.getAllAttachments()) {
			documentService.createDoc(goodsReceiptDocService, goodsreceiptId, 
					null, new DocumentMetadata(type, keywords, grouping, securitylevel), attachment);
		}
	}
	
	@Override
	@POST
	@Path("goodsreceipt/deliveryslipcnr/document")
	public void createDocumentViaDeliverySlipCnr(
			@HeaderParam(ParamInHeader.TOKEN) String headerToken,
			@QueryParam(Param.USERID) String userId,
			@QueryParam(Param.PURCHASEORDERID) Integer purchaseOrderId,
			@QueryParam("deliveryslipcnr") String deliveryslipCnr,
			@QueryParam(ApiFilter.RECEIPTPOSSIBLE) Boolean filterReceiptPossible,			
			@QueryParam(Param.TYPE) String type,
			@QueryParam(Param.KEYWORDS) String keywords,
			@QueryParam(Param.GROUPING) String grouping,
			@QueryParam(Param.SECURITYLEVEL) Long securitylevel,
			MultipartBody body) throws RemoteException, NamingException {
		if(connectClient(buildConnectId(userId, headerToken),
				MAX_CONTENT_LENGTH_GOODSRECEIPT) == null) {
			return;
		}

		HvValidateBadRequest.notNull(purchaseOrderId, Param.PURCHASEORDERID);
		HvValidateBadRequest.notEmpty("deliveryslipcnr", deliveryslipCnr);
		HvValidateBadRequest.notNull(body, "body"); 

		BestellungLieferantDto dto = requireBestellungDto(
				purchaseOrderId, null, filterReceiptPossible);
		if(dto == null) {
			return;
		}
		
		WareneingangDto[] weDtos = wareneingangCall
				.wareneingangFindByBestellungIId(dto.getBestellungDto().getIId());
/*
  		WareneingangDto weDto = CollectionTools.detect(weDtos, weElement -> {
			return deliveryslipCnr.equals(weElement.getCLieferscheinnr());});
 */
		final String slipCnr = deliveryslipCnr;	
		WareneingangDto weDto = CollectionTools.detect(weDtos, new IDetect<WareneingangDto>() {
			public boolean accept(WareneingangDto element) {
				return slipCnr.equals(element.getCLieferscheinnr());
			};
		});
		
		if(weDto == null) {
			weDto = newWareneingangDto(dto, deliveryslipCnr);
			Integer weId = wareneingangCall.createWareneingang(weDto);
			weDto = wareneingangCall.wareneingangFindByPrimaryKeyOhneExc(weId);
		}
		
		for (Attachment attachment : body.getAllAttachments()) {
			documentService.createDoc(goodsReceiptDocService, weDto.getIId(), 
					null, new DocumentMetadata(type, keywords, grouping, securitylevel), attachment);
		}
	}
}
