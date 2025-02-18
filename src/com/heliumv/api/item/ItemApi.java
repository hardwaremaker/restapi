/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2014 HELIUM V IT-Solutions GmbH
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published 
 * by the Free Software Foundation, either version 3 of theLicense, or 
 * (at your option) any later version.
 * 
 * According to sec. 7 of the GNU Affero General Public License, version 3, 
 * the terms of the AGPL are supplemented with the following terms:
 * 
 * "HELIUM V" and "HELIUM 5" are registered trademarks of 
 * HELIUM V IT-Solutions GmbH. The licensing of the program under the 
 * AGPL does not imply a trademark license. Therefore any rights, title and
 * interest in our trademarks remain entirely with us. If you want to propagate
 * modified versions of the Program under the name "HELIUM V" or "HELIUM 5",
 * you may only do so if you have a written permission by HELIUM V IT-Solutions 
 * GmbH (to acquire a permission please contact HELIUM V IT-Solutions
 * at trademark@heliumv.com).
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact: developers@heliumv.com
 ******************************************************************************/
package com.heliumv.api.item;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.heliumv.api.BaseApi;
import com.heliumv.api.HvValidateNotFound;
import com.heliumv.api.stock.StockPlaceEntryList;
import com.heliumv.api.stock.StockPlaceEntryMapper;
import com.heliumv.factory.IArtikelCall;
import com.heliumv.factory.IGlobalInfo;
import com.heliumv.factory.ILagerCall;
import com.heliumv.factory.IPanelCall;
import com.heliumv.factory.legacy.AllLagerEntry;
import com.heliumv.factory.legacy.PaneldatenPair;
import com.heliumv.factory.loader.IItemLoaderAttribute;
import com.heliumv.factory.query.ItemQuery;
import com.heliumv.feature.FeatureFactory;
import com.heliumv.tools.StringHelper;
import com.lp.server.artikel.service.ArtgruDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.artikel.service.LagerplatzDto;
import com.lp.server.artikel.service.SeriennrChargennrAufLagerDto;
import com.lp.server.system.service.PanelFac;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.fastlanereader.service.query.QueryResult;
import com.lp.util.EJBExceptionLP;

/**
 * Funktionalit&auml;t rund um die Resource <b>Artikel</b></br>
 * <p>Grundvoraussetzung f&uuml;r eine erfolgreiche Benutzung dieser Resource ist,
 * dass der HELIUM V Mandant das Modul "Artikel" installiert hat. F&uuml;r praktisch
 * alle Zugriffe auf den Artikel muss der API Benutzer zumindest Leserechte auf
 * den Artikel haben.
 * </p>
 * 
 * @author Gerold
 */
@Service("hvItem")
@Path("/api/v1/item")
public class ItemApi extends BaseApi implements IItemApi {
	@Autowired
	protected IGlobalInfo globalInfo ;
	
	@Autowired
	protected IArtikelCall artikelCall ;

//	@Autowired
//	private IArtikelLoaderCall artikelLoaderCall ;
//	@Autowired
//	private ItemLoaderComments itemloaderComments ;
//	@Autowired
//	private ItemLoaderStockinfoSummary itemloaderStockinfoSummary ;
//	@Autowired
//	private ItemLoaderProducerInfo itemLoaderProducerInfo ;
//	@Autowired
//	private ItemLoaderStockplaceInfo itemLoaderStockplaceInfo;
//	@Autowired
//	private ItemLoaderDocuments itemLoaderDocuments;
//	@Autowired
//	private ItemLoaderCommentsMedia itemLoaderCommentsMedia;
	
	@Autowired
	protected ILagerCall lagerCall ;

	@Autowired
	protected ItemQuery itemQuery ;
	
//	@Autowired
//	private IParameterCall parameterCall ;
	
	@Autowired
	protected IPanelCall panelCall ;
	
	@Autowired
	private ItemEntryMapper itemEntryMapper ;
	
	@Autowired
	private ItemGroupEntryMapper itemgroupEntryMapper ;
	
	@Autowired
	private ItemPropertyEntryMapper itempropertyEntryMapper ;
	
	@Autowired
	private ModelMapper modelMapper ;
	
	@Autowired
	protected ItemListBuilder itemlistBuilder ;
	
	@Autowired
	private FeatureFactory featureFactory ;
	
	@Autowired
	private ItemIdentyEntryMapper itemIdentityEntryMapper ;
	@Autowired
	private StockPlaceEntryMapper stockPlaceEntryMapper;
	
	@Autowired
	private IItemService itemService;
	
	@Override
	@GET
	@Path("/list")
	@Produces({FORMAT_JSON, FORMAT_XML})
	public List<ItemEntry> getItems(
			@QueryParam(Param.USERID) String userId,
			@QueryParam(Param.LIMIT) Integer limit,
			@QueryParam(Param.STARTINDEX) Integer startIndex, 
			@QueryParam(Filter.CNR) String filterCnr, 
			@QueryParam("filter_textsearch") String filterTextSearch,
			@QueryParam("filter_deliverycnr") String filterDeliveryCnr,
			@QueryParam("filter_itemgroupclass") String filterItemGroupClass,
			@QueryParam("filter_itemreferencenr") String filterItemReferenceNr,
			@QueryParam(Filter.HIDDEN) Boolean filterWithHidden) {
		List<ItemEntry> itemEntries = new ArrayList<ItemEntry>() ;
		try {
			if(connectClient(userId) == null) return itemEntries ;

			featureFactory.getObject().applyItemListFilter(itemlistBuilder, filterCnr, 
					filterTextSearch, filterDeliveryCnr, filterItemGroupClass, filterItemReferenceNr, filterWithHidden, null, null, null);
//			if(!featureFactory.hasCustomerPartlist()) {
//				itemlistBuilder
//				.clear()
//				.addFilterCnr(filterCnr)
//				.addFilterTextSearch(filterTextSearch)
//				.addFilterDeliveryCnr(filterDeliveryCnr)
//				.addFilterItemGroupClass(filterItemGroupClass)
//				.addFilterItemReferenceNr(filterItemReferenceNr)
//				.addFilterWithHidden(filterWithHidden);				
//			} else {
//				Integer partnerId = featureFactory.getPartnerIdFromAnsprechpartnerId() ;
//				itemlistBuilder
//					.clear()
//					.addFilterStuecklistenPartner(partnerId)
//					.addFilterTextSearch(filterCnr);				
//			}
			
			QueryParameters params = itemQuery.getDefaultQueryParameters(itemlistBuilder.andFilterBlock()) ;
			params.setLimit(limit) ;
			params.setKeyOfSelectedRow(startIndex) ;
			
			QueryResult result = itemQuery.setQuery(params) ;			
			itemEntries = itemQuery.getResultList(result) ;	
		} catch(RemoteException e) {
			respondUnavailable(e) ;
		} catch(NamingException e) {
			respondUnavailable(e) ;
		} catch(EJBExceptionLP e) {
			respondBadRequest(e) ;
		} catch(Exception e) {
			respondBadRequest(new EJBExceptionLP(e));
		}
		
		return itemEntries ;
	}


	@Override
	@GET
	@Produces({FORMAT_JSON, FORMAT_XML})
	public ItemEntry findItemByAttributes(
			@QueryParam(Param.USERID) String userId,
			@QueryParam(Param.ITEMCNR) String cnr, 
			@QueryParam("itemSerialnumber") String serialnumber,
			@QueryParam("addComments") Boolean addComments, 
			@QueryParam("addStockAmountInfos") Boolean addStockAmountInfos) throws RemoteException, NamingException {

		if(StringHelper.isEmpty(cnr) && StringHelper.isEmpty(serialnumber)) {
			respondBadRequest("itemCnr", cnr) ;
			return null ;
		}

		if(connectClient(userId) == null) return null ;

/*		
		Set<IItemLoaderAttribute> attributes = getAttributes(addComments, addStockAmountInfos, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE) ;
		ItemEntryInternal itemEntryInternal = findItemBySerialnumberOrCnr(serialnumber, cnr, null, attributes) ;
		if(itemEntryInternal == null) return null ;
		
		return mapFromInternal(itemEntryInternal) ;
*/
		Set<IItemLoaderAttribute> attributes = 
			itemService.getAttributes(addComments, addStockAmountInfos,
					Boolean.FALSE, Boolean.FALSE,
					Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
		ItemEntryInternal itemEntryInternal = itemService
				.findItemByAttributes(null, cnr, serialnumber, null, attributes) ;
		if(itemEntryInternal == null) return null ;
		
		return itemService.mapFromInternal(itemEntryInternal);

	}
	
/*
 	protected Set<IItemLoaderAttribute> getAttributes(Boolean addComments, Boolean addStockAmountInfos, Boolean addProducerInfos, 
			Boolean addStockplaceInfos, Boolean addDocuments, Boolean addCommentsMedia) {
		Set<IItemLoaderAttribute> attributes = new HashSet<IItemLoaderAttribute>() ;
		if(Boolean.TRUE.equals(addComments)) {
			attributes.add(itemloaderComments) ;
		}
		if(Boolean.TRUE.equals(addStockAmountInfos)) {
			attributes.add(itemloaderStockinfoSummary) ;
		}
		if(Boolean.TRUE.equals(addProducerInfos)) {
			attributes.add(itemLoaderProducerInfo) ;
		}
		if (Boolean.TRUE.equals(addStockplaceInfos)) {
			attributes.add(itemLoaderStockplaceInfo);
		}
		if (Boolean.TRUE.equals(addDocuments)) {
			attributes.add(itemLoaderDocuments);
		}
		if (Boolean.TRUE.equals(addCommentsMedia)) {
			attributes.add(itemLoaderCommentsMedia);
		}
		return attributes ;
	}
 */

/*	
	protected ItemEntryInternal findItemBySerialnumberOrCnr(
			String serialnumber, String cnr, String barcode, Set<IItemLoaderAttribute> attributes) {
		try {
			if(!StringHelper.isEmpty(serialnumber)) {
				ItemEntryInternal itemEntry = findItemEntryBySerialnumberCnr(serialnumber, cnr, attributes) ;
				if(itemEntry == null) {
					respondNotFound() ;				
				}
				return itemEntry ;
			}

			if(!StringHelper.isEmpty(cnr)) {
				ItemEntryInternal itemEntry = findItemEntryByCnrImpl(cnr, attributes) ;
				if(itemEntry == null) {
					respondNotFound() ;				
				}
				return itemEntry ;				
			}

			if(!StringHelper.isEmpty(barcode)) {
				ItemEntryInternal itemEntry = findItemEntryByEanImpl(barcode, attributes) ;
				if(itemEntry == null) {
					respondNotFound() ;				
				}
				return itemEntry ;				
			}
		} catch(RemoteException e) {
			respondUnavailable(e) ;
		} catch(NamingException e) {
			respondUnavailable(e) ;
		} catch(EJBExceptionLP e) {
			respondBadRequest(e) ;
		}
		
		return null;		
	}
*/	
	@Override
	@GET
	@Path("/stocks")
	@Produces({FORMAT_JSON, FORMAT_XML})
	public List<StockAmountEntry> getStockAmount(
			@QueryParam(Param.USERID) String userId, 
			@QueryParam(Param.ITEMCNR) String itemCnr,
			@QueryParam("returnItemInfo") Boolean returnItemInfo) {
		return getStockAmountImpl(userId, itemCnr, returnItemInfo, Boolean.FALSE, Boolean.FALSE);
	}


	protected List<StockAmountEntry> getStockAmountImpl(String userId,
			String itemCnr, Boolean returnItemInfo, Boolean addStockPlaceInfos, 
			Boolean returnAllStocks) {
		List<StockAmountEntry> stockEntries = new ArrayList<StockAmountEntry>() ;
 		if(StringHelper.isEmpty(itemCnr)) {
			respondBadRequestValueMissing("itemCnr") ;
			return stockEntries ;
		}
		if(connectClient(userId) == null) return stockEntries ;
		if(returnItemInfo == null) returnItemInfo = false ;
		
		try {
			ArtikelDto itemDto = artikelCall.artikelFindByCNrOhneExc(itemCnr) ;
			if(itemDto == null) {
				respondNotFound("itemCnr", itemCnr);
				return stockEntries ;
			}

			StockEntryMapper stockMapper = new StockEntryMapper();
			List<AllLagerEntry> stocks = lagerCall.getAllLager();
			for (AllLagerEntry allLagerEntry : stocks) {
				if(lagerCall.hatRolleBerechtigungAufLager(allLagerEntry.getStockId())) {
					LagerDto lagerDto = lagerCall.lagerFindByPrimaryKeyOhneExc(allLagerEntry.getStockId()) ;
					if(lagerDto.getBVersteckt() > 0) continue ;
					
					BigDecimal amount = lagerCall.getLagerstandOhneExc(itemDto.getIId(), lagerDto.getIId()) ;
					if(amount.signum() == 1 || Boolean.TRUE.equals(returnAllStocks)) {
						StockAmountEntry stockAmountEntry = new StockAmountEntry(
								returnItemInfo ? mapFromInternal(itemEntryMapper.mapEntry(itemDto)) : null,
								stockMapper.mapEntry(lagerDto), amount) ;
				
						stockAmountEntry.setItemIdentityList(getIdentities(itemDto, allLagerEntry.getStockId())); 
						
						if (Boolean.TRUE.equals(addStockPlaceInfos)) {
							stockAmountEntry.setStockplaceList(getStockplaces(itemDto, lagerDto.getIId()));
						}
						stockEntries.add(stockAmountEntry) ;
					}
				}
			}
		} catch(RemoteException e) {
			respondUnavailable(e) ;
		} catch(EJBExceptionLP e) {
			respondBadRequest(e) ;
		}
		return stockEntries ;
	}

	private StockPlaceEntryList getStockplaces(ArtikelDto itemDto, Integer stockId) {
		List<LagerplatzDto> lagerplatzDtos = 
				lagerCall.lagerplatzFindByArtikelIIdLagerIIdOhneExc(itemDto.getIId(), stockId);
		return new StockPlaceEntryList(stockPlaceEntryMapper.mapEntries(lagerplatzDtos));
	}
	
	private ItemIdentityEntryList getIdentities(ArtikelDto itemDto, Integer stockId) {
		if(!itemDto.istArtikelSnrOderchargentragend()) {
			return null;
		}
		SeriennrChargennrAufLagerDto[] dtos = 
				lagerCall.getAllSerienChargennrAufLagerInfoDtos(itemDto.getIId(), stockId);
		if(dtos == null || dtos.length == 0) {
			return null ;
		}
		List<ItemIdentityEntry> entries = new ArrayList<ItemIdentityEntry>();
		for (SeriennrChargennrAufLagerDto dto : dtos) {
			entries.add(itemIdentityEntryMapper.mapEntry(dto));
		}
		return new ItemIdentityEntryList(entries);
	}

	@Override
	@GET
	@Path("/groups")
	@Produces({FORMAT_JSON, FORMAT_XML})
	public ItemGroupEntryList getItemGroups(
			@QueryParam(Param.USERID) String userId) {
		ItemGroupEntryList itemgroups = new ItemGroupEntryList() ;
		if(connectClient(userId) == null) return itemgroups ;
		try {
			List<ArtgruDto> dtos = artikelCall.artikelgruppeFindByMandantCNr() ; 			
			itemgroups.setEntries(itemgroupEntryMapper.mapEntry(dtos)) ;
		} catch(RemoteException e) {
			respondUnavailable(e) ;
		} catch(EJBExceptionLP e) {
			respondBadRequest(e) ;
		}
		
		return itemgroups ;
	}


	@Override
	@GET
	@Path("/properties")
	@Produces({FORMAT_JSON, FORMAT_XML})
	public ItemPropertyEntryList getItemProperties(
			@QueryParam(Param.USERID) String userId,
			@QueryParam(Param.ITEMCNR) String itemCnr) {
		ItemPropertyEntryList properties = new ItemPropertyEntryList() ;

		if(connectClient(userId) == null) return properties ;
		try {
			ArtikelDto itemDto = artikelCall.artikelFindByCNrOhneExc(itemCnr) ;
			if(itemDto == null) {
				respondNotFound(Param.ITEMCNR, itemCnr);
				return properties ;
			}

			properties.setEntries(getItemPropertiesFromIdImpl(itemDto.getIId(), itemDto.getArtgruIId()));
			return properties ;
		} catch(NamingException e) {
			respondUnavailable(e) ;
		} catch(RemoteException e) {
			respondUnavailable(e) ;
		} catch(EJBExceptionLP e) {
			respondBadRequest(e) ;
		}

		return properties ;
	}
	
	@Override
	@GET
	@Path("/{" + Param.ITEMID + "}/properties")
	@Produces({FORMAT_JSON, FORMAT_XML})
	public ItemPropertyEntryList getItemPropertiesFromId(
			@QueryParam(Param.USERID) String userId,
			@PathParam(Param.ITEMID) Integer itemId) {
		ItemPropertyEntryList properties = new ItemPropertyEntryList() ;

		if(connectClient(userId) == null) return properties ;
		try {
			ArtikelDto itemDto = artikelCall.artikelFindByPrimaryKeySmallOhneExc(itemId) ;
			HvValidateNotFound.notNull(itemDto, "itemId", itemId);
			
			properties.setEntries(getItemPropertiesFromIdImpl(itemId, itemDto.getArtgruIId()));
			return properties ;
		} catch(NamingException e) {
			respondUnavailable(e) ;
		} catch(RemoteException e) {
			respondUnavailable(e) ;
		} catch(EJBExceptionLP e) {
			respondBadRequest(e) ;
		}

		return properties ;
	}

	private List<ItemPropertyEntry> getItemPropertiesFromIdImpl(Integer itemId, Integer itemgroupId) throws NamingException, RemoteException {
//		PaneldatenDto[] dtos = panelCall.paneldatenFindByPanelCNrCKey(
//				PanelFac.PANEL_ARTIKELEIGENSCHAFTEN, itemId.toString()) ;
//		List<ItemPropertyEntry> properties = itempropertyEntryMapper.mapEntry(dtos) ;
				
		List<PaneldatenPair> entries = panelCall.paneldatenFindByPanelCNrCKeyBeschreibung(PanelFac.PANEL_ARTIKELEIGENSCHAFTEN, itemId.toString()) ;
//		List<PaneldatenPair> entries = panelCall.panelbeschreibungFindByPanelCNrCKey(PanelFac.PANEL_ARTIKELEIGENSCHAFTEN, itemId.toString(), itemgroupId) ;
		List<ItemPropertyEntry> properties = itempropertyEntryMapper.mapEntry(entries) ;
		
		return properties ;	
	}
/*
	private ItemEntryInternal findItemEntryBySerialnumberCnr(String serialnumber, String cnr, Set<IItemLoaderAttribute> attributes) throws RemoteException, NamingException {
		Integer itemId = lagerCall.artikelIdFindBySeriennummerOhneExc(serialnumber) ;
		if(itemId == null) return null ;
		
		ArtikelDto artikelDto = artikelCall.artikelFindByPrimaryKeySmallOhneExc(itemId) ;
		if(artikelDto == null) return null ;
		
		if(!StringHelper.isEmpty(cnr)) {
			if(!artikelDto.getCNr().equals(cnr)) return null ;
		}
		
		return artikelLoaderCall.artikelFindByCNrOhneExc(artikelDto.getCNr(), attributes) ;
	}
	
	private ItemEntryInternal findItemEntryByCnrImpl(String cnr, 
			Set<IItemLoaderAttribute> attributes) throws RemoteException, NamingException {
		ItemEntryInternal itemEntry = artikelLoaderCall.artikelFindByCNrOhneExc(cnr, attributes) ;
		return itemEntry ;
	}

	private ItemEntryInternal findItemEntryByEanImpl(String ean,
			Set<IItemLoaderAttribute> attributes) throws RemoteException, NamingException {
		ItemEntryInternal itemEntry = artikelLoaderCall.artikelFindByEanOhneExc(ean, attributes) ;
		return itemEntry ;
	}
*/	
	protected ItemEntry mapFromInternal(ItemEntryInternal itemEntryInternal) {
		return itemService.mapFromInternal(itemEntryInternal);
/*
 		if(itemEntryInternal == null) return null ;
		
		ItemEntry itemEntry = modelMapper.map(itemEntryInternal, ItemEntry.class) ;
		return itemEntry ;
 */
	}
	
	protected ModelMapper getModelMapper() {
		return modelMapper ;
	}

//	protected ItemLoaderComments getItemLoaderComments() {
//		return itemloaderComments ;
//	}
//	
//	protected ItemLoaderStockinfoSummary getItemLoaderStockinfoSummary() {
//		return itemloaderStockinfoSummary ;
//	}
}
