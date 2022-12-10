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

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.jcr.RepositoryException;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.heliumv.annotation.HvFlrMapper;
import com.heliumv.api.HvValidateBadRequest;
import com.heliumv.api.HvValidateNotFound;
import com.heliumv.api.document.DocumentInfoEntryList;
import com.heliumv.api.document.IDocumentCategoryService;
import com.heliumv.api.document.IDocumentService;
import com.heliumv.api.document.RawDocument;
import com.heliumv.api.production.ProductionEntryList;
import com.heliumv.api.production.ProductionEntryMapper;
import com.heliumv.api.system.PropertyLayoutEntry;
import com.heliumv.api.system.PropertyLayoutEntryList;
import com.heliumv.api.system.PropertyService;
import com.heliumv.factory.IArtikelReportCall;
import com.heliumv.factory.IArtikelkommentarCall;
import com.heliumv.factory.IKundeCall;
import com.heliumv.factory.IVkPreisfindungCall;
import com.heliumv.factory.legacy.AllArtikelgruppeEntry;
import com.heliumv.factory.legacy.PaneldatenPair;
import com.heliumv.factory.loader.IArtikelLoaderCall;
import com.heliumv.factory.loader.IItemLoaderAttribute;
import com.heliumv.factory.query.ItemV11Query;
import com.heliumv.factory.query.ShopGroupQuery;
import com.heliumv.feature.FeatureFactory;
import com.heliumv.tools.StringHelper;
import com.lp.server.artikel.service.ArtgruDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelkommentarDto;
import com.lp.server.artikel.service.ArtikelkommentarsprDto;
import com.lp.server.artikel.service.ArtikellisteHandlerFeature;
import com.lp.server.artikel.service.ArtklaDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.artikel.service.LagerbewegungDto;
import com.lp.server.artikel.service.ShopgruppeDto;
import com.lp.server.artikel.service.VerkaufspreisDto;
import com.lp.server.artikel.service.VkpfartikelpreislisteDto;
import com.lp.server.artikel.service.VkpreisfindungDto;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.partner.service.CustomerPricelistReportDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.system.service.CreatePaneldatenResult;
import com.lp.server.system.service.PanelbeschreibungDto;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.fastlanereader.service.query.QueryParametersFeatures;
import com.lp.server.util.fastlanereader.service.query.QueryResult;
import com.lp.server.util.fastlanereader.service.query.SortierKriterium;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

/**
 * Funktionalit&auml;t rund um die Resource <b>Artikel</b></br>
 * <p>Grundvoraussetzung f&uuml;r eine erfolgreiche Benutzung dieser Resource ist,
 * dass der HELIUM V Mandant das Modul "Artikel" installiert hat. F&uuml;r praktisch
 * alle Zugriffe auf den Artikel muss der API Benutzer zumindest Leserechte auf
 * den Artikel haben.
 * </p>
 * <p>&Auml;nderungen in dieser Version<p>
 * <p>Die Lagerst&auml;nde <code>stockslist</code> werden nun als typisiertes Ergebnis geliefert</p>
 * @author Gerold
 */
@Service("hvItemV11")
@Path("/api/v11/item")
public class ItemApiV11 extends ItemApi implements IItemApiV11 {	
	@Autowired
	private FeatureFactory featureFactory ;
		
	@Autowired
	private IKundeCall kundeCall ;
	
	@Autowired
	private IVkPreisfindungCall vkpreisfindungCall ;
	
	@Autowired
	private ItemV11Query itemV11Query ;
	
	@Autowired
	private ShopGroupQuery shopGroupQuery ;
	
	@Autowired
	private ProductionEntryMapper productionEntryMapper;
	
	@Autowired
	private IArtikelReportCall artikelReportCall;
	@Autowired
	private IDocumentService documentService;
	@Autowired
	private IDocumentCategoryService itemDocService;
	@Autowired
	private IArtikelkommentarCall artikelkommentarCall;
	@Autowired
	private IItemCommentService itemCommentService;
	@Autowired
	private ItemPropertyEntryMapper itempropertyEntryMapper ;
	@Autowired
	private ItemPropertyValidator itemPropertyValidator;
	@Autowired
	private PropertyService propertyService;
	@Autowired
	private IItemService itemService;
	@Autowired
	private IArtikelLoaderCall artikelLoaderCall;
	@Autowired
	private PriceListEntryMapper priceListEntryEntryMapper ;

	@Override
	@GET
	@Path("/stockslist")
	@Produces({FORMAT_JSON, FORMAT_XML})	
	public StockAmountEntryList getStockAmountList(
			@QueryParam(Param.USERID) String userId,
			@QueryParam(Param.ITEMCNR) String itemCnr, 
			@QueryParam(Return.ITEMINFO) Boolean returnItemInfo,
			@QueryParam(Add.STOCKPLACEINFOS) Boolean addStockPlaceInfos,
			@QueryParam(Return.ALLSTOCKS) Boolean returnAllStocks) {
		StockAmountEntryList stockAmounts = new StockAmountEntryList() ;
		stockAmounts.setEntries(getStockAmountImpl(userId, itemCnr, returnItemInfo, addStockPlaceInfos, returnAllStocks));
		return stockAmounts ;
	}
	
	@Override
	@GET
	@Path("/itemv1")
	@Produces({FORMAT_JSON, FORMAT_XML})
	public ItemV1Entry findItemV1ByAttributes(
			@QueryParam(Param.USERID) String userId,
			@QueryParam(Param.ITEMCNR) String cnr, 
			@QueryParam("itemSerialnumber") String serialnumber,
			@QueryParam("itemBarcode") String itemBarcode,
			@QueryParam("addComments") Boolean addComments, 
			@QueryParam("addStockAmountInfos") Boolean addStockAmountInfos,
			@QueryParam("addProducerInfos") Boolean addProducerInfos,
			@QueryParam(Add.STOCKPLACEINFOS) Boolean addStockPlaceInfos,
			@QueryParam(Add.DOCUMENTS) Boolean addDocuments,
			@QueryParam(Add.COMMENTSMEDIA) Boolean addCommentsMedia) throws RemoteException, NamingException {

		if(StringHelper.isEmpty(cnr) && StringHelper.isEmpty(serialnumber) && StringHelper.isEmpty(itemBarcode)) {
			respondBadRequest("itemCnr", cnr) ;
			return null ;
		}

		if(connectClient(userId) == null) return null ;

		Set<IItemLoaderAttribute> attributes = itemService.getAttributes(
				addComments, addStockAmountInfos, addProducerInfos, 
				addStockPlaceInfos, addDocuments, addCommentsMedia, Boolean.FALSE);		
		ItemEntryInternal itemEntryInternal = itemService
				.findItemByAttributes(null, cnr, serialnumber, itemBarcode, attributes);
		if(itemEntryInternal == null) return null ;
		return itemService.mapFromInternalV1(itemEntryInternal);

/*		
		Set<IItemLoaderAttribute> attributes = getAttributes(addComments, addStockAmountInfos, addProducerInfos, 
				addStockPlaceInfos, addDocuments, addCommentsMedia) ;		
		ItemEntryInternal itemEntryInternal = findItemBySerialnumberOrCnr(serialnumber, cnr, itemBarcode, attributes) ;
		if(itemEntryInternal == null) return null ;
		
		return mapFromInternalV1(itemEntryInternal) ;
*/		
	}

	protected ItemV1Entry mapFromInternalV1(ItemEntryInternal itemEntryInternal) {
		return itemService.mapFromInternalV1(itemEntryInternal);
/*		
		if(itemEntryInternal == null) return null ;	
		ItemV1Entry itemEntry = new ItemV1Entry() ;
		itemEntry.setAvailable(itemEntryInternal.isAvailable());
		itemEntry.setBillOfMaterialType(itemEntryInternal.getBillOfMaterialType());
		itemEntry.setCnr(itemEntryInternal.getCnr());
		itemEntry.setComments(itemEntryInternal.getComments());
		itemEntry.setCosts(itemEntryInternal.getCosts());
		itemEntry.setDescription(itemEntryInternal.getDescription());
		itemEntry.setDescription2(itemEntryInternal.getDescription2());
		itemEntry.setHasChargenr(itemEntryInternal.getHasChargenr());
		itemEntry.setHasSerialnr(itemEntryInternal.getHasSerialnr());
		itemEntry.setHidden(itemEntryInternal.getHidden());
		itemEntry.setId(itemEntryInternal.getId());
		itemEntry.setIndex(itemEntryInternal.getIndex());
		itemEntry.setItemclassCnr(itemEntryInternal.getItemclassCnr());
		itemEntry.setItemgroupCnr(itemEntryInternal.getItemgroupCnr());
		itemEntry.setName(itemEntryInternal.getName());
		itemEntry.setProducerInfoEntry(itemEntryInternal.getProducerInfo());
		itemEntry.setReferenceNumber(itemEntryInternal.getReferenceNumber());
		itemEntry.setRevision(itemEntryInternal.getRevision());
		itemEntry.setShortName(itemEntryInternal.getShortName());
		itemEntry.setStockAmount(itemEntryInternal.getStockAmount());
		itemEntry.setStockAmountInfo(itemEntryInternal.getStockAmountInfo());
		itemEntry.setTypeCnr(itemEntryInternal.getTypeCnr());
		itemEntry.setUnitCnr(itemEntryInternal.getUnitCnr());
		itemEntry.setPackagingEntries(itemEntryInternal.getPackagingEntries());
		itemEntry.setStockplaceInfoEntries(itemEntryInternal.getStockplaceInfoEntries());
		itemEntry.setDocumentInfoEntries(itemEntryInternal.getDocumentInfoEntries());
		itemEntry.setItemCommentMediaInfoEntries(itemEntryInternal.getItemCommentMediaInfoEntries());
		return itemEntry;
*/		
	}
	
	@GET
	@Path("/{" + Param.ITEMID + "}/price")
	public BigDecimal getPrice(
			@QueryParam(Param.USERID) String userId,
			@PathParam(Param.ITEMID) Integer itemId,
			@QueryParam(Param.CUSTOMERID) Integer customerId,
			@QueryParam("amount") BigDecimal amount,
			@QueryParam("unit") String unitCnr) throws NamingException, RemoteException, EJBExceptionLP {
		if(connectClient(userId) == null) return null ;

		ArtikelDto itemDto = artikelCall.artikelFindByPrimaryKeySmallOhneExc(itemId) ;
		if(itemDto == null) {
			respondNotFound(Param.ITEMID, itemId.toString());
			return null ;
		}
		
		KundeDto customerDto = null ;
		if(customerId == null) {
			if(!featureFactory.hasCustomerPartlist()) {
				respondBadRequestValueMissing(Param.CUSTOMERID);
				return null ;
			}
			customerDto = kundeCall.kundeFindByAnsprechpartnerIdcNrMandantOhneExc(
					featureFactory.getAnsprechpartnerId()) ;
		} else {
			customerDto = kundeCall.kundeFindByPrimaryKeyOhneExc(customerId) ;
		}

		if(customerDto == null || !globalInfo.getMandant().equals(customerDto.getMandantCNr())) {
			respondNotFound();
			return null ;
		}
		
		// TODO Umrechnen in die Lagereinheit		
		java.sql.Date d = new Date(System.currentTimeMillis()) ;
		
		Integer mwstsatzbeId = itemDto.getMwstsatzbezIId() ;
		if(mwstsatzbeId == null) {
			mwstsatzbeId = customerDto.getMwstsatzbezIId() ;
		}
		
		VkpreisfindungDto vkpreisfindungDto = vkpreisfindungCall.verkaufspreisfindung(
				itemDto.getIId(), customerDto.getIId(), amount, d,
				customerDto.getVkpfArtikelpreislisteIIdStdpreisliste(), 
				mwstsatzbeId, customerDto.getCWaehrung()) ;

		BigDecimal p = getPriceFromPreisfindung(vkpreisfindungDto) ;
		return p ;
		
	}
	
	private BigDecimal getMinimumPrice(BigDecimal minimum, VerkaufspreisDto priceDto) {
		if(priceDto != null && priceDto.nettopreis != null) {
			return null == minimum ? priceDto.nettopreis : minimum.min(priceDto.nettopreis) ; 
		}
		
		return minimum;		
	}
	
	private BigDecimal getPriceFromPreisfindung(VkpreisfindungDto vkPreisDto) {
		BigDecimal p = getMinimumPrice(null, vkPreisDto.getVkpStufe3()) ;
		if(p != null) return p ;

		p = getMinimumPrice(null, vkPreisDto.getVkpStufe2()) ;
		if(p != null) return p ;

		p = getMinimumPrice(null, vkPreisDto.getVkpStufe1()) ;
		if(p != null) return p ;

		p = getMinimumPrice(null, vkPreisDto.getVkpPreisbasis())  ;
		if(p != null) return p ;
		
		return BigDecimal.ZERO ;
	}
	
	@Override
	@GET
	@Path("/list")
	@Produces({FORMAT_JSON, FORMAT_XML})
	public ItemEntryList getItemsList(
			@QueryParam(Param.USERID) String userId,
			@QueryParam(Param.LIMIT) Integer limit,
			@QueryParam(Param.STARTINDEX) Integer startIndex, 
			@QueryParam(Filter.CNR) String filterCnr, 
			@QueryParam("filter_textsearch") String filterTextSearch,
			@QueryParam("filter_deliverycnr") String filterDeliveryCnr,
			@QueryParam("filter_itemgroupclass") String filterItemGroupClass,
			@QueryParam("filter_itemreferencenr") String filterItemReferenceNr,
			@QueryParam(Filter.HIDDEN) Boolean filterWithHidden,
			@QueryParam("filter_itemgroupid") Integer filterItemgroupId,
			@QueryParam("filter_customeritemcnr") String filterCustomerItemCnr,
			@QueryParam("filter_itemgroupids") String filterItemgroupIds,
			@QueryParam(Param.ORDERBY) String orderBy) throws RemoteException, NamingException, EJBExceptionLP, Exception {
		if(connectClient(userId) == null) return new ItemEntryList() ;
		ShopGroupIdList shopGroupIdList = null ;
		if(StringHelper.hasContent(filterItemgroupIds)) {
			String[] ids = filterItemgroupIds.split("\\,") ;
			ArrayList<Integer> integerIds = new ArrayList<Integer>() ;
			for (String stringId : ids) {
				try {
					integerIds.add(Integer.parseInt(stringId) ) ;
				} catch(NumberFormatException e) {
					respondBadRequest("filter_itemgroupids", "[" + stringId + "] not numeric or parseable. (" + filterItemgroupIds + ")");
					return new ItemEntryList() ;
				}
			}
			shopGroupIdList = new ShopGroupIdList(integerIds) ;
		}
		return getItemsListImpl(limit, startIndex, filterCnr, filterTextSearch,
				filterDeliveryCnr, filterItemGroupClass, filterItemReferenceNr,
				filterWithHidden, filterItemgroupId, filterCustomerItemCnr, shopGroupIdList, orderBy) ;
	}
	
	public ItemEntryList getItemsListImpl(
			Integer limit,
			Integer startIndex, 
			String filterCnr, 
			String filterTextSearch,
			String filterDeliveryCnr,
			String filterItemGroupClass,
			String filterItemReferenceNr,
			Boolean filterWithHidden,
			Integer filterItemgroupId, 
			String filterCustomerItemCnr,
			ShopGroupIdList filterItemgroupIds, String orderBy) throws RemoteException, NamingException, EJBExceptionLP, Exception {
		
		featureFactory.getObject().applyItemListFilter(itemlistBuilder, filterCnr,
				filterTextSearch, filterDeliveryCnr, filterItemGroupClass, filterItemReferenceNr, 
				filterWithHidden, filterItemgroupId, filterCustomerItemCnr, filterItemgroupIds);
		
		QueryParametersFeatures params = itemV11Query.getFeatureQueryParameters(itemlistBuilder.andFilterBlock()) ;
		params.setLimit(limit) ;
		params.setKeyOfSelectedRow(startIndex) ;
		params.addFeature(ArtikellisteHandlerFeature.EINHEIT_CNR) ;

		if(featureFactory.hasCustomerPartlist()) {
			params.addFeatureValue(ArtikellisteHandlerFeature.KUNDENARTIKELNUMMER_CNR,
					featureFactory.getPartnerIdFromAnsprechpartnerId().toString());
			params.addFeature(ArtikellisteHandlerFeature.SOKO);
		}
		
		params.setSortKrit(buildOrderBy(orderBy, ItemEntry.class));
		QueryResult result = itemV11Query.setQuery(params) ;
		
		ItemEntryList entryList = new ItemEntryList() ;
		entryList.setEntries(itemV11Query.getResultList(result)) ;	
		entryList.setRowCount(result.getRowCount());
		
		return entryList ;		
	}
	
	private SortierKriterium[] buildOrderBy(String orderBy, Class<?> theClass) {
		if(StringHelper.isEmpty(orderBy)) return null ;

		String[] orderbyTokens = orderBy.split(",") ;
		if(orderbyTokens.length == 0) return null ;

		List<SortierKriterium> crits = new ArrayList<SortierKriterium>() ;
		Method[] methods = theClass.getMethods() ;
		
		for (String orderbyToken : orderbyTokens) {
			String[] token = orderbyToken.split(" ") ;
			boolean ascending = true ;
			if(token.length > 1) {
				ascending = "asc".equals(token[1]) ;
			}
			
			for(Method theMethod : methods) {
				if(!theMethod.getName().startsWith("set")) continue ;

				String variable = theMethod.getName().substring(3).toLowerCase() ;
				if(token[0].toLowerCase().equals(variable)) {
					
					HvFlrMapper flrMapper = theMethod.getAnnotation(HvFlrMapper.class) ;
					String sortName = flrMapper != null ? flrMapper.flrFieldName() : variable ;
					if(sortName.length() == 0) {
						throw new IllegalArgumentException("Unknown mapping for '" + token[0] + "'") ;
					}
					
					SortierKriterium crit = new SortierKriterium(sortName, true, 
							ascending ? SortierKriterium.SORT_ASC : SortierKriterium.SORT_DESC) ;
					crits.add(crit) ;
				}
			}
		}		
		
		return crits.toArray(new SortierKriterium[0]) ;
	}
	
	@Override
	@GET
	@Path("/grouplist")
	@Produces({FORMAT_JSON, FORMAT_XML})
	public ItemGroupEntryList getItemGroupsList(
			@QueryParam(Param.USERID) String userId) throws RemoteException, NamingException, EJBExceptionLP, Exception {
		if(connectClient(userId) == null) return new ItemGroupEntryList() ;
		List<AllArtikelgruppeEntry> entries = artikelCall.getAllArtikelgruppeSpr() ;
		
		List<ItemGroupEntry> groupEntries = new ArrayList<ItemGroupEntry>();
		for (AllArtikelgruppeEntry artikelGruppeEntry : entries) {
			ItemGroupEntry groupEntry = new ItemGroupEntry() ;
			groupEntry.setId(artikelGruppeEntry.getId());
			groupEntry.setCnr(artikelGruppeEntry.getKey());
			groupEntry.setDescription(artikelGruppeEntry.getDescription());
			
			groupEntries.add(groupEntry) ;
		}
		
		ItemGroupEntryList entryList = new ItemGroupEntryList() ;
		entryList.setEntries(groupEntries);
		return entryList ;
	}
	
	
	@Override
	@GET
	@Path("/shopgrouplist")
	@Produces({FORMAT_JSON, FORMAT_XML})
	public ShopGroupEntryList getShopGroupsList(
			@QueryParam(Param.USERID) String userId,
			@QueryParam(Param.LIMIT) Integer limit,
			@QueryParam(Param.STARTINDEX) Integer startIndex) throws RemoteException, NamingException, EJBExceptionLP, Exception {
		if(connectClient(userId) == null) return new ShopGroupEntryList() ;

		QueryParameters params = shopGroupQuery.getDefaultQueryParameters() ;
		params.setLimit(limit) ;
		params.setKeyOfSelectedRow(startIndex) ;

		QueryResult result = shopGroupQuery.setQuery(params) ;
		List<ShopGroupEntry> entries = shopGroupQuery.getResultList(result) ;		
		ShopGroupEntryList entryList = new ShopGroupEntryList(entries) ;
		return entryList ;		
	}
	
	@Override
	@PUT
	@Path("/{" + Param.ITEMID + "}/discardremaining")
	@Produces({FORMAT_JSON, FORMAT_XML})
	public ProductionEntryList discardRemainingChargenumber(
			@QueryParam(Param.USERID) String userId,
			@PathParam(Param.ITEMID) Integer itemId,
			@QueryParam(Param.CHARGENR) String chargennr) throws RemoteException {
		if (connectClient(userId) == null) return new ProductionEntryList();
		
		HvValidateBadRequest.notNull(itemId, Param.ITEMID);
		HvValidateBadRequest.notNull(chargennr, Param.CHARGENR);
		
		ArtikelDto artikelDto = artikelCall.artikelFindByPrimaryKeySmallOhneExc(itemId);
		HvValidateNotFound.notNull(artikelDto, Param.ITEMID, itemId);
		
		LagerbewegungDto[] lagerbewegungen = lagerCall.lagerbewegungFindByArtikelIIdCSeriennrChargennr(
				artikelDto.getIId(), chargennr);
		HvValidateBadRequest.notValid(lagerbewegungen != null && lagerbewegungen.length > 0, Param.CHARGENR, chargennr);
		
		Set<Integer> lagerIIds = new HashSet<Integer>();
		for (LagerbewegungDto lagerbewegungDto : lagerbewegungen) {
			lagerIIds.add(lagerbewegungDto.getLagerIId());
		}
		if (!lagerCall.hatRolleBerechtigungAufLager(lagerIIds)) {
			respondForbidden();
			return new ProductionEntryList();
		}
		
		List<LosDto> betroffeneLose = lagerCall.getAlleBetroffenenLose(artikelDto.getIId(), chargennr);
		betroffeneLose = lagerCall.chargennummerWegwerfen(artikelDto.getIId(), chargennr, betroffeneLose);
		
		ProductionEntryList productionEntries = new ProductionEntryList(
				productionEntryMapper.mapEntriesIdCnr(betroffeneLose));
		return productionEntries;
	}
	
	@Override
	@GET
	@Path("/{" + Param.ITEMID + "}/printlabel")
	@Produces({FORMAT_JSON, FORMAT_XML})
	public Integer printLabel(
			@QueryParam(Param.USERID) String userId,
			@PathParam(Param.ITEMID) Integer itemId,
			@QueryParam(Param.CHARGENR) String identity,
			@QueryParam("identitystockid") Integer identityStockId,
			@QueryParam(Param.AMOUNT) BigDecimal amount,
			@QueryParam(Param.COPIES) Integer copies) throws RemoteException {
		if (connectClient(userId) == null) return null;
		
		HvValidateBadRequest.notNull(itemId, Param.ITEMID);
		
		ArtikelDto artikelDto = artikelCall.artikelFindByPrimaryKeySmallOhneExc(itemId);
		HvValidateNotFound.notNull(artikelDto, Param.ITEMID, itemId);
		
		if (identityStockId != null) {
			LagerDto lagerDto = lagerCall.lagerFindByPrimaryKeyOhneExc(identityStockId);
			HvValidateBadRequest.notValid(lagerDto != null, "identitystockid", String.valueOf(identityStockId));
		}
		
		String[] identities = StringHelper.hasContent(identity) ? new String[] {identity} : null;
		printArtikelEtikett(itemId, amount, identities, identityStockId, copies);
		
		return itemId;
	}
	
	private void printArtikelEtikett(Integer itemId, BigDecimal amount, String[] identities, Integer identityStockId,
			Integer copies) throws RemoteException, EJBExceptionLP {
		Integer exemplare = copies != null ? copies : new Integer(1);
		artikelReportCall.printArtikelEtikett(itemId, null, amount, identities, identityStockId, null, exemplare);
	}

	@GET
	@Path("/{" + Param.ITEMID + "}/document/list")
	@Produces({FORMAT_JSON, FORMAT_XML})
	public DocumentInfoEntryList getDocuments(
			@QueryParam(Param.USERID) String userId,
			@PathParam(Param.ITEMID) Integer itemId) throws RepositoryException, IOException {
		if (connectClient(userId) == null) return null;
		
		HvValidateBadRequest.notNull(itemId, Param.ITEMID);
		
		ArtikelDto artikelDto = artikelCall.artikelFindByPrimaryKeySmallOhneExc(itemId);
		HvValidateNotFound.notNull(artikelDto, Param.ITEMID, itemId);
		
		return documentService.getDocs(itemDocService, itemId, null);
	}
	
	@GET
	@Path("/{" + Param.ITEMID + "}/document")
	public Response getDocument(
			@QueryParam(Param.USERID) String userId,
			@PathParam(Param.ITEMID) Integer itemId,
			@QueryParam(Param.DOCUMENTCNR) String documentCnr) throws RepositoryException, IOException {
		if (connectClient(userId) == null) return null;
		
		HvValidateBadRequest.notNull(itemId, Param.ITEMID);
		HvValidateBadRequest.notNull(documentCnr, Param.DOCUMENTCNR);
		
		ArtikelDto artikelDto = artikelCall.artikelFindByPrimaryKeySmallOhneExc(itemId);
		HvValidateNotFound.notNull(artikelDto, Param.ITEMID, itemId);
		
		final RawDocument document = documentService.getDoc(itemDocService, itemId, "", documentCnr);
		HvValidateNotFound.notNull(document, Param.DOCUMENTCNR, documentCnr);
		
		StreamingOutput so = new StreamingOutput() {				
			@Override
			public void write(OutputStream os) throws IOException, WebApplicationException {
				os.write(document.getRawData()) ;
			}
		} ;
		String filename = URLEncoder.encode(document.getDocumentInfoEntry().getFilename(), "UTF-8");
		return Response.ok()
			.header("Content-Disposition", "filename=" + filename)
			.header("Content-Type", "application/octet-stream")
			.entity(so).build() ;
	}
	
	@GET
	@Path("/{" + Param.ITEMID + "}/commentmedia/list")
	@Produces({FORMAT_JSON, FORMAT_XML})
	public ItemCommentMediaInfoEntryList getCommentsMedia(
			@QueryParam(Param.USERID) String userId,
			@PathParam(Param.ITEMID) Integer itemId) throws RemoteException, NamingException {
		if (connectClient(userId) == null) return new ItemCommentMediaInfoEntryList();
		
		HvValidateBadRequest.notNull(itemId, Param.ITEMID);
		
		ArtikelDto artikelDto = artikelCall.artikelFindByPrimaryKeySmallOhneExc(itemId);
		HvValidateNotFound.notNull(artikelDto, Param.ITEMID, itemId);
		
		return itemCommentService.getCommentsMedia(itemId, itemCommentService.createDefaultCommentFilter());
	}

	@GET
	@Path("/{" + Param.ITEMID + "}/commentmedia/{" + Param.ITEMCOMMENTID + "}")
	public Response getCommentMedia(
			@QueryParam(Param.USERID) String userId,
			@PathParam(Param.ITEMCOMMENTID) Integer itemCommentId,
			@PathParam(Param.ITEMID) Integer itemId) throws RemoteException, NamingException, UnsupportedEncodingException {
		if (connectClient(userId) == null) return null;
		
		HvValidateBadRequest.notNull(itemId, Param.ITEMID);
		HvValidateBadRequest.notNull(itemCommentId, Param.ITEMCOMMENTID);
		
		ArtikelDto artikelDto = artikelCall.artikelFindByPrimaryKeySmallOhneExc(itemId);
		HvValidateNotFound.notNull(artikelDto, Param.ITEMID, itemId);
		
		final ArtikelkommentarsprDto kommentarSprDto = artikelkommentarCall.artikelkommentarsprFindByPrimaryKeyOhneExc(itemCommentId);
		HvValidateNotFound.notNull(kommentarSprDto, Param.ITEMCOMMENTID, itemCommentId);
		
		ArtikelkommentarDto kommentarDto = artikelkommentarCall.artikelkommentarFindByPrimaryKey(itemCommentId);
		HvValidateNotFound.notValid(kommentarDto.getArtikelIId().equals(artikelDto.getIId()), Param.ITEMCOMMENTID, itemCommentId.toString());
				
		StreamingOutput so = new StreamingOutput() {				
			@Override
			public void write(OutputStream os) throws IOException, WebApplicationException {
				os.write(kommentarSprDto.getOMedia()) ;
			}
		} ;
		String filename = URLEncoder.encode(kommentarSprDto.getCDateiname(), "UTF-8");
		return Response.ok()
			.header("Content-Disposition", "filename=" + filename)
			.header("Content-Type", "application/octet-stream")
			.entity(so).build() ;
	}
	
	@GET
	@Path("/propertylayouts")
	@Produces({FORMAT_JSON, FORMAT_XML})
	public PropertyLayoutEntryList getPropertyLayouts(
			@QueryParam(Param.USERID) String userId,
			@QueryParam(Param.ITEMID) Integer itemId,
			@QueryParam(Param.ITEMCNR) String itemCnr) throws RemoteException {
		PropertyLayoutEntryList layouts = new PropertyLayoutEntryList();
		if (connectClient(userId) == null) return layouts;
		
		ArtikelDto artikelDto = findArtikelByIdCnr(itemId, itemCnr);
		
		layouts.setEntries(getItemPropertyLayoutsImpl(artikelDto.getArtgruIId()));
		return layouts;
	}
	
	private List<PropertyLayoutEntry> getItemPropertyLayoutsImpl(Integer artgruIId) {
		PanelbeschreibungDto[] beschreibungen = panelCall.panelbeschreibungArtikelFindByArtikelgruppeId(artgruIId);
		List<PropertyLayoutEntry> layoutEntries = itempropertyEntryMapper.mapEntry(beschreibungen);

		return layoutEntries;
	}

	@GET
	@Path("/property/{" + Param.PROPERTYID + "}")
	@Produces({FORMAT_JSON, FORMAT_XML})
	public ItemPropertyEntry getItemProperty(
			@PathParam(Param.PROPERTYID) Integer propertyId,
			@QueryParam(Param.USERID) String userId,
			@QueryParam(Param.ITEMID) Integer itemId,
			@QueryParam(Param.ITEMCNR) String itemCnr) throws RemoteException {
		if (connectClient(userId) == null) return null;
		
		ArtikelDto artikelDto = findArtikelByIdCnr(itemId, itemCnr);
		
		itemPropertyValidator.setArtikelDto(artikelDto);
		itemPropertyValidator.validPaneldaten(propertyId);
		
		return mapEntryFull(propertyId);
	}
	
	@POST
	@Path("/property")
	@Consumes({FORMAT_JSON, FORMAT_XML})
	@Produces({FORMAT_JSON, FORMAT_XML})
	public ItemPropertyEntry createItemProperty(
			@QueryParam(Param.USERID) String userId,
			CreateItemPropertyEntry createEntry) throws RemoteException {
		if (connectClient(userId) == null) return null;
		
		HvValidateBadRequest.notNull(createEntry, "createItemPropertyEntry");
		ArtikelDto artikelDto = findArtikelByIdCnr(createEntry.getItemId(), createEntry.getItemCnr());
		
		HvValidateBadRequest.notNull(createEntry.getLayoutId(), "layoutId");
		HvValidateBadRequest.notEmpty(createEntry.getContent(), "content");
		
		return createItemPropertyImpl(artikelDto, createEntry);
	}
	
	private ItemPropertyEntry createItemPropertyImpl(ArtikelDto artikelDto, CreateItemPropertyEntry createEntry) throws RemoteException, EJBExceptionLP {
		itemPropertyValidator.setArtikelDto(artikelDto);
		CreatePaneldatenResult paneldatenResult = propertyService.createPaneldaten(createEntry, itemPropertyValidator);
		if (paneldatenResult.hasEjbExceptions()) {
			buildResponse(paneldatenResult.getEjbExceptions().get(0), "layoutId", createEntry.getLayoutId().toString());
			return null;
		}

		return mapEntryFull(paneldatenResult.getPaneldatenIId());
	}

	@PUT
	@Path("/property")
	@Consumes({FORMAT_JSON, FORMAT_XML})
	@Produces({FORMAT_JSON, FORMAT_XML})
	public ItemPropertyEntry updateItemProperty(
			@QueryParam(Param.USERID) String userId,
			UpdateItemPropertyEntry updateEntry) throws RemoteException {
		if (connectClient(userId) == null) return null;
		
		HvValidateBadRequest.notNull(updateEntry, "updateItemPropertyEntry");
		ArtikelDto artikelDto = findArtikelByIdCnr(updateEntry.getItemId(), updateEntry.getItemCnr());
		
		HvValidateBadRequest.notNull(updateEntry.getPropertyId(), "propertyId");
		
		return updateItemPropertyImpl(artikelDto, updateEntry);
	}
	
	private ItemPropertyEntry updateItemPropertyImpl(ArtikelDto artikelDto, UpdateItemPropertyEntry updateEntry) throws RemoteException, EJBExceptionLP {
		itemPropertyValidator.setArtikelDto(artikelDto);
		propertyService.updatePaneldaten(updateEntry, itemPropertyValidator);
		
		return mapEntryFull(updateEntry.getPropertyId());
	}
	
	private ItemPropertyEntry mapEntryFull(Integer paneldatenId) throws RemoteException, EJBExceptionLP {
		PaneldatenPair paneldatenPair = panelCall.paneldatenFindByPrimaryKey(paneldatenId);
		return itempropertyEntryMapper.mapEntry(paneldatenPair);
	}
	
	private ArtikelDto findArtikelByIdCnr(Integer itemId, String itemCnr) throws RemoteException {
		if (itemId == null && StringHelper.isEmpty(itemCnr)) {
			HvValidateBadRequest.notNull(null, "itemId");	
		}
		
		ArtikelDto artikelDto = null;
		if (itemId != null) {
			artikelDto = artikelCall.artikelFindByPrimaryKeySmallOhneExc(itemId);
			HvValidateNotFound.notNull(artikelDto, "itemId", itemId);
		}
		
		artikelDto = artikelCall.artikelFindByCNrOhneExc(itemCnr);
		HvValidateNotFound.notNull(artikelDto, "itemCnr", itemCnr);
		
		return artikelDto;
	}
	
	@Override
	@GET
	@Path("/itemslistmanufacturer")
	@Produces({FORMAT_JSON, FORMAT_XML})
	public ItemV1EntryList findItemV1ByManufacturerBarcode(
			@QueryParam(Param.USERID) String userId,
			@QueryParam("manufacturerbarcode") String barcode, 
			@QueryParam(Add.COMMENTS) Boolean addComments, 
			@QueryParam(Add.STOCKAMOUNTINFOS) Boolean addStockAmountInfos,
			@QueryParam(Add.PRODUCERINFOS) Boolean addProducerInfos,
			@QueryParam(Add.STOCKPLACEINFOS) Boolean addStockPlaceInfos,
			@QueryParam(Add.DOCUMENTS) Boolean addDocuments,
			@QueryParam(Add.COMMENTSMEDIA) Boolean addCommentsMedia) throws RemoteException, NamingException {
		if(StringHelper.isEmpty(barcode)) {
			respondBadRequest("manufacturerbarcode", barcode);
			return new ItemV1EntryList();
		}

		if(connectClient(userId) == null) return new ItemV1EntryList();

		Set<IItemLoaderAttribute> attributes = itemService.getAttributes(
				addComments, addStockAmountInfos, addProducerInfos, 
				addStockPlaceInfos, addDocuments, addCommentsMedia, Boolean.FALSE);
		List<ItemEntryInternal> items = artikelLoaderCall.artikelFindByHerstellerbarcode(barcode, attributes);
		
		List<ItemV1Entry> resultItems = new ArrayList<ItemV1Entry>();
		for (ItemEntryInternal internalItem : items) {
			resultItems.add(itemService.mapFromInternalV1(internalItem));
		}
		return new ItemV1EntryList(resultItems);
	}
	
	@Override
	@GET
	@Path("/pricelists")
	@Produces({FORMAT_JSON, FORMAT_XML})
	public PriceListEntryList getPriceLists(
			@QueryParam(Param.USERID) String userId) throws RemoteException {
		PriceListEntryList pricelists = new PriceListEntryList() ;
		
		if(connectClient(userId) == null) return pricelists ;
		VkpfartikelpreislisteDto[] dtos = vkpreisfindungCall.vkpfartikelpreislisteFindByMandant(); 			
		pricelists.setEntries(priceListEntryEntryMapper.mapEntries(dtos)) ;
		
		return pricelists ;
	}

	@Override
	@GET
	@Path("/pricelist")
	@Produces({FORMAT_JSON, FORMAT_XML})
	public CustomerPricelistReportDto getPriceList(
			@QueryParam(Param.USERID) String userId,
			@QueryParam(Param.PRICELISTCNR) String pricelistCnr,
			@QueryParam("filter_itemgroupcnr") String filterItemgroupCnr,
			@QueryParam("filter_itemgroupid") Integer filterItemgroupId,
			@QueryParam("filter_itemclasscnr") String filterItemclassCnr,
			@QueryParam("filter_itemclassid") Integer filterItemclassId,
			@QueryParam("filter_start_itemcnr") String filterItemRangeFrom,
			@QueryParam("filter_stop_itemcnr") String filterItemRangeTo,
			@QueryParam("filter_shopgroupcnr") String filterShopgroupCnr,
			@QueryParam("filter_shopgroupid") Integer filterShopgroupId,
			@QueryParam("filter_validitydate") String filterValidityDate,			
			@QueryParam("filter_withHidden") Boolean filterWithHidden) throws RemoteException {
		
		if(connectClient(userId) == null) return null;
		Integer pricelistId = verifyPricelist(pricelistCnr);
		Integer shopgroupId = verifyShopgroup(filterShopgroupId, filterShopgroupCnr);
		Integer itemgroupId = verifyItemgroup(filterItemgroupId, filterItemgroupCnr);
		Integer itemclassId = verifyItemclass(filterItemclassId, filterItemclassCnr);
		Date validityDate = verifyValidityDate(filterValidityDate);
		Boolean hidden = Boolean.TRUE.equals(filterWithHidden);
		CustomerPricelistReportDto r = artikelReportCall.vkPreislisteRaw(pricelistId, itemgroupId, itemclassId,
				shopgroupId, false, filterItemRangeFrom, filterItemRangeTo, hidden, validityDate);
		return r;
	}
	
	private Integer verifyPricelist(String pricelistCnr) throws RemoteException {
		if(StringHelper.hasContent(pricelistCnr)) {
			VkpfartikelpreislisteDto dto = vkpreisfindungCall.vkpfartikelpreislisteFindByCnr(pricelistCnr);
			HvValidateNotFound.notNull(dto, Param.PRICELISTCNR, pricelistCnr);
			HvValidateNotFound.notValid(Helper.isTrue(dto.getBPreislisteaktiv()),  Param.PRICELISTCNR, pricelistCnr);
			return dto.getIId();			
		} 
		
		return null;
	}
	
	
	private Integer verifyItemgroup(Integer itemgroupId, String itemgroupCnr) throws RemoteException {
		if(itemgroupId != null) {
			ArtgruDto artgruDto = artikelCall.artikelgruppeFindByPrimaryKeyOhneExc(itemgroupId);
			HvValidateNotFound.notNull(artgruDto, "filter_itemgroupid", itemgroupId);
			return artgruDto.getIId();
		}
		if(StringHelper.hasContent(itemgroupCnr)) {
			ArtgruDto artgruDto = artikelCall.artikelgruppeFindByCnrOhneExc(itemgroupCnr);
			HvValidateNotFound.notNull(artgruDto, "filter_itemgroupcnr", itemgroupCnr);
			return artgruDto.getIId();			
		}
		
		return null;
	}
	
	private Integer verifyItemclass(Integer itemclassId, String itemclassCnr) throws RemoteException {
		if(itemclassId != null) {
			ArtklaDto artklaDto = artikelCall.artikelklasseFindByPrimaryKeyOhneExc(itemclassId);
			HvValidateNotFound.notNull(artklaDto, "filter_itemclassid", itemclassId);
			return artklaDto.getIId();
		}
		if(StringHelper.hasContent(itemclassCnr)) {
			ArtklaDto artklaDto = artikelCall.artikelklasseFindByCnrOhneExc(itemclassCnr);
			HvValidateNotFound.notNull(artklaDto, "filter_itemclasscnr", itemclassCnr);
			return artklaDto.getIId();
		}
		
		return null;
	}
	
	private Integer verifyShopgroup(Integer shopgroupId, String shopgroupCnr) throws RemoteException {
		if(shopgroupId != null) {
			ShopgruppeDto dto = artikelCall.shopgruppeFindByPrimaryKeyOhneExc(shopgroupId);
			HvValidateNotFound.notNull(dto, "filter_shopgroupid", shopgroupId);
			return dto.getIId();
		}
		if(StringHelper.hasContent(shopgroupCnr)) {
			ShopgruppeDto dto = artikelCall.shopgruppeFindByCNrMandantOhneExc(shopgroupCnr);
			HvValidateNotFound.notNull(dto, "filter_shopgroupcnr", shopgroupCnr);
			return dto.getIId();
		}
		return null;
	}
	
	
	private Date verifyValidityDate(String validityDate) {
		if(StringHelper.isEmpty(validityDate)) {
			return Helper.cutDate(new Date(System.currentTimeMillis()));
		}
		
		try {
			Calendar c = DatatypeConverter.parseDateTime(validityDate) ;
			return new Date(c.getTimeInMillis());
		} catch(IllegalArgumentException e) {
			System.out.println("illegalargument" + e.getMessage()) ;
		}

		HvValidateBadRequest.notValid(false, "filter_validitydate", validityDate);
		return null;
	}
}
