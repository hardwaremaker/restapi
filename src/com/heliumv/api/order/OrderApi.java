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
package com.heliumv.api.order;

import java.io.IOException;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
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

import org.apache.commons.codec.binary.Base64;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;
import org.modelmapper.ModelMapper;
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
import com.heliumv.bl.AuftragItemPriceCalculator;
import com.heliumv.bl.ItemPriceCalculationResult;
import com.heliumv.factory.IArtikelCall;
import com.heliumv.factory.IAuftragCall;
import com.heliumv.factory.IAuftragpositionCall;
import com.heliumv.factory.IAuftragreportCall;
import com.heliumv.factory.IDruckerCall;
import com.heliumv.factory.IGlobalInfo;
import com.heliumv.factory.ILagerCall;
import com.heliumv.factory.ILieferscheinCall;
import com.heliumv.factory.IMandantCall;
import com.heliumv.factory.IMediaCall;
import com.heliumv.factory.IPersonalCall;
import com.heliumv.factory.query.AuftragQueryOffline;
import com.heliumv.factory.query.AuftragpositionQuery;
import com.heliumv.tools.BelegpositionVerkaufHelper;
import com.heliumv.tools.FilterKriteriumCollector;
import com.heliumv.tools.JasperPrintHelper;
import com.heliumv.tools.StringHelper;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikellagerplaetzeDto;
import com.lp.server.artikel.service.LagerplatzDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragHandlerFeature;
import com.lp.server.auftrag.service.AuftragQueryResult;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.lieferschein.service.LieferscheinFac;
import com.lp.server.partner.service.IAddressContact;
import com.lp.server.personal.service.AnwesenheitsbestaetigungDto;
import com.lp.server.system.service.BelegPruefungDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MediastandardDto;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.util.HvOptional;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.fastlanereader.service.query.QueryParametersFeatures;
import com.lp.server.util.fastlanereader.service.query.QueryResult;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

import net.sf.jasperreports.engine.JRException;

@Service("hvOrder")
@Path("/api/v1/order/")
public class OrderApi extends BaseApi implements IOrderApi  {
	private static Logger log = LoggerFactory.getLogger(OrderApi.class) ;

	public final static int MAX_CONTENT_LENGTH_ORDER = 30*1024*1024;
	public final static int MAX_CONTENT_LENGTH_SIGNATURE = 1*1024*1024;
	
	@Autowired
	private IMandantCall mandantCall ;
	
	@Autowired
	private IAuftragCall auftragCall ;
	@Autowired
	private IAuftragreportCall auftragReportCall;
	@Autowired
	private IAuftragpositionCall auftragpositionCall;
		
	@Autowired
	private AuftragQueryOffline offlineOrderQuery ;
	
	@Autowired
	private AuftragpositionQuery orderPositionQuery ;
	
	@Autowired
	private IGlobalInfo globalInfo ;
	
	@Autowired
	private ModelMapper modelMapper ;

	@Autowired
	private IAuftragpositionCall auftragPositionCall ;
	
	@Autowired
	private DeliverableOrderPositionEntryMapper deliverableOrderPositionEntryMapper ;
	@Autowired
	private IArtikelCall artikelCall ;
	@Autowired
	private ILieferscheinCall lieferscheinCall ;
	@Autowired
	private ILagerCall lagerCall ;
	@Autowired
	private DeliveryOrderEntryMapper deliveryOrderEntryMapper ;
	@Autowired
	private IOrderService orderService ;
	@Autowired
	private IDocumentService documentService;
	@Autowired
	private IDocumentCategoryService orderDocService;
	@Autowired
	private IPersonalCall personalCall;
	@Autowired
	private IDruckerCall druckerCall;
	@Autowired
	private AuftragItemPriceCalculator orderItemPriceCalculator;
	@Autowired
	private IMediaCall mediaCall;
	
	@GET
	@Produces({FORMAT_JSON, FORMAT_XML})
	public List<OrderEntry> getOrders(
			@QueryParam(Param.USERID) String userId,
			@QueryParam(Param.LIMIT) Integer limit,
			@QueryParam(Param.STARTINDEX) Integer startIndex,
			@QueryParam("filter_cnr") String filterCnr,
			@QueryParam("filter_customer") String filterCustomer, 
			@QueryParam("filter_project") String filterProject,
			@QueryParam("filter_withHidden") Boolean filterWithHidden,
			@QueryParam("filter_myopen") Boolean filterMyOpen,
			@QueryParam("filter_representativeSign") String representativeSign) throws NamingException, RemoteException {
		List<OrderEntry> orders = new ArrayList<OrderEntry>();
		if(null == connectClient(userId)) return orders;
		if(!mandantCall.hasModulAuftrag()) {
			respondNotFound();
			return orders;
		}

		orders = orderService.getOrders(limit, startIndex, 
				filterCnr, filterCustomer, filterProject, filterWithHidden, 
				filterMyOpen, representativeSign);
		return orders;
	}	
	
	@GET
	@Path("{orderid}/position")
	@Produces({FORMAT_JSON, FORMAT_XML})
	public List<OrderpositionEntry> getPositions(
			@PathParam(Param.ORDERID) Integer orderId,
			@QueryParam(Param.USERID) String userId,
			@QueryParam(Param.LIMIT) Integer limit,
			@QueryParam(Param.STARTINDEX) Integer startIndex) {
		List<OrderpositionEntry> positions = new ArrayList<OrderpositionEntry>() ;
		try {
			if(connectClient(userId) == null) return positions;
			if(!mandantCall.hasModulAuftrag()) {
				respondNotFound();
				return positions;
			}
			
			FilterKriteriumCollector collector = new FilterKriteriumCollector();
			collector.add(orderPositionQuery.getOrderIdFilter(orderId));
			
			QueryParameters params = orderPositionQuery.getDefaultQueryParameters(collector);
			params.setLimit(limit);
			params.setKeyOfSelectedRow(startIndex);

			QueryResult result = orderPositionQuery.setQuery(params) ;
			positions = orderPositionQuery.getResultList(result) ;
		} catch(NamingException e) {
			respondUnavailable(e) ;
		} catch(RemoteException e) {
			respondUnavailable(e) ;
		} finally {
		}
		return positions ;
	}
	
	@GET
	@Path("position")
	@Produces({FORMAT_JSON, FORMAT_XML})
	public List<OrderpositionsEntry> getOrderPositions(
			@QueryParam(Param.USERID) String userId,
			@QueryParam(Param.LIMIT) Integer limit,
			@QueryParam(Param.STARTINDEX) Integer startIndex,
			@QueryParam("filter_cnr") String filterCnr,
			@QueryParam("filter_customer") String filterCustomer, 
			@QueryParam("filter_project") String filterProject,
			@QueryParam("filter_withHidden") Boolean filterWithHidden,
			@QueryParam("filter_myopen") Boolean filterMyOpen,
			@QueryParam("filter_representativeSign") String representativeSign) throws NamingException, RemoteException {	
		List<OrderpositionsEntry> entries = new ArrayList<OrderpositionsEntry>() ;

		if(null == connectClient(userId)) return entries ;
		if(!mandantCall.hasModulAuftrag()) {
			respondNotFound() ;
			return entries ;
		}
		
		List<OrderEntry> orders = orderService.getOrders(limit, 
				startIndex, filterCnr, filterCustomer, filterProject, 
				 filterWithHidden, filterMyOpen, representativeSign) ;
		
		for (OrderEntry orderEntry : orders) {
			FilterKriteriumCollector collector = new FilterKriteriumCollector() ;
			collector.add(orderPositionQuery.getOrderIdFilter(orderEntry.getId())) ;
			
			QueryParameters params = orderPositionQuery.getDefaultQueryParameters(collector) ;
			params.setLimit(Integer.MAX_VALUE) ;
			params.setKeyOfSelectedRow(0) ;

			QueryResult positionResult = orderPositionQuery.setQuery(params) ;
			List<OrderpositionEntry> posEntries = orderPositionQuery.getResultList(positionResult) ;	
			
			addPositionEntries(entries, orderEntry.getId(), posEntries);
		}
		
		return entries ;
	}
	
	private void addPositionEntries(List<OrderpositionsEntry> allEntries, Integer orderId, List<OrderpositionEntry> posEntries) {
		for (OrderpositionEntry orderpositionEntry : posEntries) {
			OrderpositionsEntry entry = new OrderpositionsEntry(orderId, orderpositionEntry) ;			
			allEntries.add(entry) ;
		}
	}
	
	@GET
	@Path("offline")
	@Produces({FORMAT_JSON, FORMAT_XML})
	public OfflineOrderEntry getOfflineOrders(
			@HeaderParam(ParamInHeader.TOKEN) String headerUserId,
			@QueryParam(Param.USERID) String userId,
			@QueryParam(Param.LIMIT) Integer limit,
			@QueryParam(Param.STARTINDEX) Integer startIndex,
			@QueryParam("filter_cnr") String filterCnr,
			@QueryParam("filter_customer") String filterCustomer, 
			@QueryParam("filter_delivery_customer") String filterDeliveryCustomer, 
			@QueryParam("filter_project") String filterProject,
			@QueryParam("filter_withHidden") Boolean filterWithHidden,
			@QueryParam("filter_representativeSign") String representativeSign) throws NamingException, RemoteException {
		OfflineOrderEntry entry = new OfflineOrderEntry();

		if(null == connectClient(headerUserId, userId)) return entry ;
		if(!mandantCall.hasModulAuftrag()) {
			respondNotFound() ;
			return entry ;
		}
		
		FilterKriteriumCollector collector = new FilterKriteriumCollector() ;
		collector.add(offlineOrderQuery.getFilterCnr(StringHelper.removeXssDelimiters(filterCnr))) ;
		collector.add(offlineOrderQuery.getFilterProject(StringHelper.removeXssDelimiters(filterProject))) ;
		collector.add(offlineOrderQuery.getFilterCustomer(StringHelper.removeXssDelimiters(filterCustomer))) ;
		collector.add(offlineOrderQuery.getFilterDeliveryCustomer(StringHelper.removeXssDelimiters(filterDeliveryCustomer))) ;
		collector.add(offlineOrderQuery.getFilterWithHidden(filterWithHidden));
		collector.add(offlineOrderQuery.getFilterRepresentativeShortSign(representativeSign));
//			FilterBlock filterCrits = collector.createFilterBlock();
		
		QueryParametersFeatures params = offlineOrderQuery.getFeatureQueryParameters(collector) ;
		params.setLimit(limit) ;
		params.setKeyOfSelectedRow(startIndex) ;
		params.addFeature(AuftragHandlerFeature.ADRESSE_KOMPLETT) ;
		params.addFeature(AuftragHandlerFeature.ADRESSE_ANSCHRIFT);
		params.addFeature(AuftragHandlerFeature.ADRESSE_IST_LIEFERADRESSE);
		AuftragQueryResult result = (AuftragQueryResult) offlineOrderQuery.setQuery(params) ;

		List<OrderEntry> orders = offlineOrderQuery.getResultList(result) ;
		List<OrderpositionsEntry> positions = new ArrayList<OrderpositionsEntry>() ;
		HashMap<String, IAddressContact> distinctAddresses = new HashMap<String, IAddressContact>() ;
		
		int orderIndex = 0 ;
		
		for (OrderEntry orderEntry : orders) {
			collector = new FilterKriteriumCollector() ;
			collector.add(orderPositionQuery.getOrderIdFilter(orderEntry.getId())) ;
			collector.add(orderPositionQuery.getIsIdentFilter()) ;				
//				filterCrits = new FilterBlock(collector.asArray(), "AND")  ;
			
			QueryParameters posParams = orderPositionQuery.getDefaultQueryParameters(collector) ;
			posParams.setLimit(Integer.MAX_VALUE) ;
			posParams.setKeyOfSelectedRow(0) ;

			QueryResult positionResult = orderPositionQuery.setQuery(posParams) ;
			List<OrderpositionEntry> posEntries = orderPositionQuery.getResultList(positionResult) ;	
			
			addPositionEntries(positions, orderEntry.getId(), posEntries);	

			try {
				IAddressContact orderAddress = result.getFlrData()[orderIndex].getAddressContact() ;
				distinctAddresses.put(
						orderAddress.getPartnerAddress().getPartnerId().toString() + (
								orderAddress.getContactAddress() != null
									? ("|" +orderAddress.getContactAddress().getPartnerId().toString()) : ""), orderAddress) ;
			} catch(IndexOutOfBoundsException e) {					
			}
			
			++orderIndex ;
		}
		entry.setOrders(orders) ;
		entry.setOrderpositions(positions) ;

		List<OrderAddressContact> resultAddresses = new ArrayList<OrderAddressContact>();
		for (IAddressContact orderAddress : distinctAddresses.values()) {
//				OrderAddressContact newAddress = modelMapper.map(orderAddress, OrderAddressContact.class) ;
			OrderAddressContact newAddress = new OrderAddressContact() ;
			newAddress.setPartnerAddress(modelMapper.map(orderAddress.getPartnerAddress(), OrderAddress.class)) ;
			if(orderAddress.getContactAddress() != null) {
				newAddress.setContactAddress(modelMapper.map(orderAddress.getContactAddress(), OrderAddress.class));
			}
			resultAddresses.add(newAddress) ;
		}
		
		entry.setAddresses(resultAddresses);		
		return entry ;
	}
	
	@GET
	@Path("comments")
	@Produces({FORMAT_JSON, FORMAT_XML})
	public OrderComments getOrderComments (
			@HeaderParam(ParamInHeader.TOKEN) String headerToken,
			@QueryParam(Param.USERID) String userId,
			@QueryParam(Param.ORDERID) Integer orderId,
			@QueryParam(Param.ORDERCNR) String orderCnr,
			@QueryParam("addInternalComment") Boolean addInternalComment,
			@QueryParam("addExternalComment") Boolean addExternalComment) {
		OrderComments comments = new OrderComments() ;

		try {
			if(null == connectClient(headerToken, userId)) return comments ;
			
			if(!mandantCall.hasModulAuftrag()) {
				respondNotFound() ;
				return comments ;
			}

			if(orderId == null && StringHelper.isEmpty(orderCnr)) {
				respondBadRequestValueMissing(Param.ORDERID);
				return comments ;
			}
			
			AuftragDto auftragDto = findAuftragByIdCnr(orderId, orderCnr) ;
			if(auftragDto == null) {
				return comments ;
			}

			comments.setId(auftragDto.getIId());
			if(addInternalComment == null || addInternalComment) {
				comments.setInternalComment(auftragDto.getXInternerkommentar()) ;
			}
			if(addExternalComment == null || addExternalComment) {
				comments.setExternalComment(auftragDto.getXExternerkommentar()) ;
			}
		} catch(RemoteException e) {
			respondUnavailable(e); 
		} catch(NamingException e) {
			respondUnavailable(e) ;
		} catch(EJBExceptionLP e) {
			respondBadRequest(e) ;
		}
		
		return comments ;
	}
	
	@GET
	@Path("deliverable")
	@Produces({FORMAT_JSON, FORMAT_XML})
	public DeliverableOrderEntry getDeliverableOrderStatus (
			@HeaderParam(ParamInHeader.TOKEN) String headerToken,
			@QueryParam(Param.USERID) String userId,
			@QueryParam(Param.ORDERID) Integer orderId,
			@QueryParam(Param.ORDERCNR) String orderCnr) throws NamingException, RemoteException {
		DeliverableOrderEntry orderEntry = new DeliverableOrderEntry() ;
		if(null == connectClient(headerToken, userId)) return orderEntry ;

		if(!mandantCall.hasModulAuftrag()) {
			respondNotFound() ;
			return orderEntry ;
		}

		AuftragDto auftragDto = findAuftragByIdCnr(orderId, orderCnr) ;
		if(auftragDto == null) {
			return orderEntry ;
		}
		orderEntry.setId(auftragDto.getIId());
		orderEntry.setCnr(auftragDto.getCNr());
		
		OrderDocumentStatus orderStatus = OrderDocumentStatus.fromString(auftragDto.getStatusCNr()); 
		if(!(orderStatus.equals(OrderDocumentStatus.OPEN) || 
				orderStatus.equals(OrderDocumentStatus.PARTIALLYDONE))) {
			respondExpectationFailed(EJBExceptionLP.FEHLER_STATUS) ;
			return orderEntry ;
		}
		orderEntry.setStatus(orderStatus) ;

		List<AuftragpositionDto> positionDtos = findFilteredAuftragpositionByAuftragOffeneMenge(auftragDto.getIId());
		sortAuftragpositionByLagerplatz(auftragDto.getLagerIIdAbbuchungslager(), positionDtos) ;
		orderEntry.setPositionEntries(new DeliverableOrderPositionEntryList(transform(auftragDto, positionDtos))) ;

		Collection<LieferscheinDto> lsDtos = findFilteredLieferscheinByAuftrag(auftragDto.getIId()) ;
		orderEntry.setDeliveryEntries(new DeliveryOrderEntryList(transformLieferschein(lsDtos)));
		return orderEntry ;
	}
	
	private Collection<LieferscheinDto> findFilteredLieferscheinByAuftrag(Integer orderId) throws RemoteException {
		Collection<LieferscheinDto> entries = new ArrayList<LieferscheinDto>() ;
		LieferscheinDto[] lsDtos = lieferscheinCall.lieferscheinFindByAuftrag(orderId) ;
		for (LieferscheinDto lieferscheinDto : lsDtos) {
			if(lieferscheinDto.getLagerIId() != null) {
				if(!lagerCall.hatRolleBerechtigungAufLager(lieferscheinDto.getLagerIId())) {
					continue ;
				}				
			}
			
			String status = lieferscheinDto.getStatusCNr() ;
			if(LieferscheinFac.LSSTATUS_ANGELEGT.equals(status)) {
				entries.add(lieferscheinDto) ;
			}					
		}
		return entries ; 
	}
	
	private List<AuftragpositionDto> findFilteredAuftragpositionByAuftragOffeneMenge(Integer orderId) throws RemoteException {
		List<AuftragpositionDto> entries = new ArrayList<AuftragpositionDto>();
		AuftragpositionDto[] dtos = auftragPositionCall.auftragpositionFindByAuftragOffeneMenge(orderId);
		for (AuftragpositionDto auftragpositionDto : dtos) {
			if(auftragpositionDto.isIdent()) {
				entries.add(auftragpositionDto) ;
			}
		}
		
		return entries ;
	}
	
	private List<AuftragpositionDto> sortAuftragpositionByLagerplatz(
			final Integer lagerId, List<AuftragpositionDto> positionDtos) throws RemoteException {
		Collections.sort(positionDtos, new java.util.Comparator<AuftragpositionDto>() {
			@Override
			public int compare(AuftragpositionDto arg0, AuftragpositionDto arg1) {
				try {					
					ArtikellagerplaetzeDto lpDto0 = lagerCall.artikellagerplaetzeFindByArtikelIIdLagerIId(arg0.getArtikelIId(), lagerId) ;
					ArtikellagerplaetzeDto lpDto1 = lagerCall.artikellagerplaetzeFindByArtikelIIdLagerIId(arg1.getArtikelIId(), lagerId) ;
					
					if(lpDto0 == null && lpDto1 == null) {
						return arg0.getISort().compareTo(arg1.getISort()) ;
					}
					
					if(lpDto0 == null) {
						return 1 ;
					}
					
					if(lpDto1 == null) {
						return -1 ;
					}
					
					int i = lpDto0.getLagerplatzDto().getCLagerplatz().compareToIgnoreCase(lpDto1.getLagerplatzDto().getCLagerplatz()) ;
					if(i == 0) {
						i = arg0.getISort().compareTo(arg1.getISort()) ;
					}
					return i ;
				} catch(RemoteException e) {
				}
				return 0;
			}
		}) ;
		return positionDtos ;
	}
	
	private List<DeliverableOrderPositionEntry> transform(Collection<AuftragpositionDto> dtos) throws RemoteException {
		List<DeliverableOrderPositionEntry> entries = new ArrayList<DeliverableOrderPositionEntry>() ;
		
		for (AuftragpositionDto positionDto : dtos) {
			ArtikelDto artikelDto = artikelCall.artikelFindByPrimaryKeySmallOhneExc(positionDto.getArtikelIId()) ;
			entries.add(deliverableOrderPositionEntryMapper.mapEntry(positionDto, artikelDto)) ;
		}

		return entries ;
	}
	
	private List<DeliverableOrderPositionEntry> transform(AuftragDto auftragDto, Collection<AuftragpositionDto> dtos) throws RemoteException {
		List<DeliverableOrderPositionEntry> entries = new ArrayList<DeliverableOrderPositionEntry>() ;
		if (!lagerCall.hatRolleBerechtigungAufLager(auftragDto.getLagerIIdAbbuchungslager())) {
			
			return transform(dtos);
		}
		
		for (AuftragpositionDto positionDto : dtos) {
			ArtikelDto artikelDto = artikelCall.artikelFindByPrimaryKeySmallOhneExc(positionDto.getArtikelIId()) ;
			
			List<LagerplatzDto> lagerplatzDtos = lagerCall.lagerplatzFindByArtikelIIdLagerIIdOhneExc(
					artikelDto.getIId(), auftragDto.getLagerIIdAbbuchungslager());
			entries.add(deliverableOrderPositionEntryMapper.mapEntry(positionDto, artikelDto, lagerplatzDtos)) ;
		}

		return entries ;
	}
	
	private List<DeliveryOrderEntry> transformLieferschein(Collection<LieferscheinDto> dtos) {
		List<DeliveryOrderEntry> entries = new ArrayList<DeliveryOrderEntry>() ;
		for (LieferscheinDto lsDto : dtos) {
			entries.add(deliveryOrderEntryMapper.mapEntry(lsDto)) ;
		}
		return entries ;
	}
	
	private AuftragDto findAuftragByIdCnr(Integer orderId, String orderCnr) throws NamingException, RemoteException {
		if(orderId == null && StringHelper.isEmpty(orderCnr)) {
			respondBadRequestValueMissing(Param.ORDERID);
			return null ;
		}

		AuftragDto auftragDto = null ;
		if(orderId != null) {
			auftragDto = auftragCall.auftragFindByPrimaryKeyOhneExc(orderId) ;
		}
		
		if(auftragDto == null  && !StringHelper.isEmpty(orderCnr)) {
			auftragDto = auftragCall.auftragFindByCnr(orderCnr) ;
		}

		try {		
			if(auftragDto == null || !auftragDto.getMandantCNr().equals(globalInfo.getTheClientDto().getMandant())) {
				respondNotFound() ;
				return null ;
			}
		} catch(NullPointerException e ) {
			log.error("Nullpointer", e);
		}
		
		return auftragDto ;
	}

	@Override
	@POST
	@Path("{" + Param.ORDERID + "}/document")
	public void createDocument(
			@PathParam(Param.ORDERID) Integer orderId,
			@QueryParam(Param.USERID) String userId,
			@QueryParam(Param.TYPE) String type,
			@QueryParam(Param.KEYWORDS) String keywords,
			@QueryParam(Param.GROUPING) String grouping,
			@QueryParam(Param.SECURITYLEVEL) Long securitylevel,
			MultipartBody body) throws RemoteException, NamingException {
		if(connectClient(userId, MAX_CONTENT_LENGTH_ORDER) == null) {
			return;
		}

		HvValidateBadRequest.notNull(orderId, Param.ORDERID);
		HvValidateBadRequest.notNull(body, "body"); 
		
		for (Attachment attachment : body.getAllAttachments()) {
			documentService.createDoc(orderDocService, orderId, 
					null, new DocumentMetadata(type, keywords, grouping, securitylevel), attachment);
		}
	}
	
	@Override
	@GET
	@Path("{" + Param.ORDERID + "}/settlementofhours")
	@Produces({FORMAT_JSON, FORMAT_XML})
	public SettlementOfHoursEntry getSettlementOfHours(
			@PathParam(Param.ORDERID) Integer orderId,
			@QueryParam(Param.USERID) String userId) throws RemoteException, NamingException {
		SettlementOfHoursEntry entry = new SettlementOfHoursEntry();
		if(connectClient(userId) == null) {
			return entry;
		}
		HvValidateBadRequest.notNull(orderId, Param.ORDERID);
		AuftragDto auftragDto = findAuftragByIdCnr(orderId, null);
		if(auftragDto == null) {
			return entry;
		}
		
		entry.setId(auftragDto.getIId());
		try {
			JasperPrintLP print = auftragReportCall.printZeitbestaetigung(
					auftragDto.getIId(), false);
			byte[] pdfContent = JasperPrintHelper.asPdf(print);
			entry.setContent(pdfContent);
			Integer serialnumber = (Integer)print.getMapParameters().get("P_LFDNR");
			entry.setSerialNumber(serialnumber);
//			entry.setLastPagePng(PdfHelper.asPng(pdfContent));
			entry.setLastPagePng(JasperPrintHelper.asJpeg(
					printSignatureTemplate(print, auftragDto.getIId(), serialnumber)));
			entry.setMimeType("pdf");
			entry.setName(auftragDto.getCNr());
		} catch(JRException e) {
			log.error("JRException Order '" + auftragDto.getCNr() + "'", e);
		} catch(IOException e) {		
			log.error("IOException '" + auftragDto.getCNr() + "'", e);
//		} catch(EJBExceptionLP e) {
//			log.error("EJBEx '" + auftragDto.getCNr() + "'", e);
		}

		return entry;
	}
	
	private JasperPrintLP printSignatureTemplate(
			JasperPrintLP original, Integer orderId, Integer serialNumber) throws RemoteException {
		HvOptional<Integer> varianteId = druckerCall
				.varianteAuftragzeitbestaetigungUnterschrift();
		if(!varianteId.isPresent()) return original;
		
		return auftragReportCall.printZeitbestaetigung(
				orderId, false, varianteId.get(), serialNumber);
	}
	
	@Override
	@POST
	@Path("{" + Param.ORDERID + "}/settlementofhours")
	@Consumes({FORMAT_JSON, FORMAT_JSONutf8, FORMAT_XML})
	public void postSettlementOfHoursSignature(
			SettlementOfHoursSignatureEntry signatureEntry) throws RemoteException {
		HvValidateBadRequest.notNull(signatureEntry, "signatureEntry");
		HvValidateBadRequest.notEmpty(signatureEntry.getUserId(), "userId");
		HvValidateBadRequest.notNull(signatureEntry.getId(), "id");
		HvValidateBadRequest.notNull(signatureEntry.getOrderId(), "orderId");
		HvValidateBadRequest.notEmpty(signatureEntry.getSignatureContent(), "signatureContent");
		HvValidateBadRequest.notNull(signatureEntry.getSignatureMs(), "signatureMs");
		
		if(connectClient(signatureEntry.getUserId(), MAX_CONTENT_LENGTH_SIGNATURE) == null) {
			return;
		}

		Integer bestaetigungId = createBestaetigung(signatureEntry);
		if(bestaetigungId != null) {
			try {
				personalCall.createAnwesenheitsbestaetigungEmail(bestaetigungId);
			} catch(Exception e) {
				throw e;
			}
		} else {
			
		}
	}
	
	private Integer createBestaetigung(SettlementOfHoursSignatureEntry signatureEntry) {
		AnwesenheitsbestaetigungDto dto = new AnwesenheitsbestaetigungDto();
		dto.setAuftragIId(signatureEntry.getOrderId());
		dto.setCBemerkung(StringHelper.trimCut(signatureEntry.getRemark(), 80));
		dto.setDatenformatCNr("image/" + signatureEntry.getSignatureType());
		dto.setOPdf(Base64.decodeBase64(signatureEntry.getPdfContent()));
		dto.setOUnterschrift(Base64.decodeBase64(
				StringHelper.cutPrefix(
						signatureEntry.getSignatureContent(), "data:image/png;base64,")));
		dto.setTUnterschrift(new Timestamp(signatureEntry.getSignatureMs()));
		dto.setPersonalIId(globalInfo.getTheClientDto().getIDPersonal());
		dto.setILfdnr(signatureEntry.getSerialNumber());
		dto.setCName(signatureEntry.getSignerName());
		return personalCall.createAnwesenheitsbestaetigung(dto);
	}

	@Override
	@POST
	@Path("{" + Param.ORDERID + "}/itemposition")
    @Consumes({FORMAT_JSON, FORMAT_JSONutf8, FORMAT_XML})
    @Produces({ FORMAT_JSON, FORMAT_XML })
    public CreatedOrderPositionEntry createIdentPosition(
							 @QueryParam(Param.USERID) String userId,
							 @PathParam(Param.ORDERID) Integer orderId,
							 CreateOrderItemPositionEntry positionEntry) throws RemoteException {
		if (connectClient(userId) == null)
			return null;
		
		HvValidateBadRequest.notNull(positionEntry, "positionEntry");
		HvValidateBadRequest.notNull(orderId, "orderId");

		AuftragDto abDto = auftragCall.auftragFindByPrimaryKeyOhneExc(orderId);
		HvValidateNotFound.notNull(abDto, "orderId", orderId);

		CreatedOrderPositionEntry createdEntry = new CreatedOrderPositionEntry(orderId, abDto.getCNr());

		if (!abDto.getMandantCNr().equals(globalInfo.getMandant())) {
			respondNotFound(Param.ORDERID, orderId);
			return createdEntry;
		}
		
		if(!AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT.equals(abDto.getStatusCNr())) {
			respondExpectationFailed(EJBExceptionLP.FEHLER_STATUS);
			return createdEntry;
		}
		
		HvOptional<AuftragpositionDto> abpos = setupOrderPosition(abDto, positionEntry);
		if(!abpos.isPresent()) return createdEntry;

		return updatePositionCreatedEntry(createdEntry, abpos.get());
    }
	
	@Override
	@PUT
	@Path("/{" + Param.ORDERID + "}/itemposition/{" + Param.POSITIONID + "}")
	@Consumes({FORMAT_JSON, FORMAT_JSONutf8, FORMAT_XML})	
	@Produces({FORMAT_JSON, FORMAT_XML})
	public CreatedOrderPositionEntry updateIdentPosition(
			@QueryParam(Param.USERID) String userId,
			@PathParam(Param.ORDERID) Integer orderId,
			@PathParam(Param.POSITIONID) Integer positionId,
			CreateOrderItemPositionEntry positionEntry) throws RemoteException {
		if(connectClient(userId) == null) return null;
		HvValidateBadRequest.notNull(positionEntry, "positionEntry");
		HvValidateBadRequest.notNull(orderId, "orderId");
		HvValidateBadRequest.notNull(positionId, "positionId");
		
		AuftragDto abDto = auftragCall.auftragFindByPrimaryKeyOhneExc(orderId);
		HvValidateNotFound.notNull(abDto, Param.ORDERID, orderId);

		CreatedOrderPositionEntry createdEntry = new CreatedOrderPositionEntry(
				orderId, abDto.getCNr());

		if (!abDto.getMandantCNr().equals(globalInfo.getMandant())) {
			respondNotFound(Param.ORDERID, orderId);
			return createdEntry;
		}

		if(!AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT.equals(abDto.getStatusCNr())) {
			respondExpectationFailed(EJBExceptionLP.FEHLER_STATUS) ;
			return createdEntry;
		}
		
		HvOptional<AuftragpositionDto> ejbPos = HvOptional.ofNullable(
				auftragpositionCall
					.auftragpositionFindByPrimaryKeyOhneExc(positionId));
		if(!ejbPos.isPresent() || !ejbPos.get().getBelegIId().equals(abDto.getIId())) {
			respondNotFound("positionId", positionId);
			return createdEntry;
		}
	
		boolean update = LocaleFac.POSITIONSART_IDENT
				.equals(ejbPos.get().getPositionsartCNr());
		HvOptional<AuftragpositionDto> newPos = 
				setupOrderPosition(abDto, positionEntry);
		if(!newPos.isPresent()) return createdEntry;
		
		if(update) {
			newPos.get().setIId(ejbPos.get().getIId());			
		} else {
			// TODO: Server behandelt ungleiche Positionsarten nicht
			auftragpositionCall.removeAuftragposition(ejbPos.get());
		}

		return updatePositionCreatedEntry(createdEntry, newPos.get());
	}

	@Override
	@POST
	@Path("{" + Param.ORDERID + "}/textposition")
    @Consumes({FORMAT_JSON, FORMAT_JSONutf8, FORMAT_XML})
    @Produces({ FORMAT_JSON, FORMAT_XML })
    public CreatedOrderPositionEntry createTextPosition(
							 @QueryParam(Param.USERID) String userId,
							 @PathParam(Param.ORDERID) Integer orderId,
							 CreateOrderTextPositionEntry positionEntry) throws RemoteException {
		if (connectClient(userId) == null)
			return null;
		
		HvValidateBadRequest.notNull(positionEntry, "positionEntry");
		HvValidateBadRequest.notNull(orderId, "orderId");

		AuftragDto abDto = auftragCall.auftragFindByPrimaryKeyOhneExc(orderId);
		HvValidateNotFound.notNull(abDto, "orderId", orderId);

		CreatedOrderPositionEntry createdEntry = new CreatedOrderPositionEntry(orderId, abDto.getCNr());

		if (!abDto.getMandantCNr().equals(globalInfo.getMandant())) {
			respondNotFound(Param.ORDERID, orderId);
			return createdEntry;
		}

		if(!AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT.equals(abDto.getStatusCNr())) {
			respondExpectationFailed(EJBExceptionLP.FEHLER_STATUS);
			return createdEntry;
		}
		
		HvOptional<AuftragpositionDto> abpos = 
				setupOrderPosition(abDto, positionEntry.getText());
		if(!abpos.isPresent()) return createdEntry;

		return updatePositionCreatedEntry(createdEntry, abpos.get());
    }
   
	@Override
	@POST
	@Path("{" + Param.ORDERID + "}/textblock")
    @Consumes({FORMAT_JSON, FORMAT_JSONutf8, FORMAT_XML})
    @Produces({ FORMAT_JSON, FORMAT_XML })
    public CreatedOrderPositionEntry createTextblockPosition(
							 @QueryParam(Param.USERID) String userId,
							 @PathParam(Param.ORDERID) Integer orderId,
							 CreateOrderTextblockPositionEntry positionEntry) throws RemoteException {
		if (connectClient(userId) == null)
			return null;
		
		HvValidateBadRequest.notNull(positionEntry, "positionEntry");
		HvValidateBadRequest.notNull(orderId, "orderId");

		AuftragDto abDto = auftragCall.auftragFindByPrimaryKeyOhneExc(orderId);
		HvValidateNotFound.notNull(abDto, "orderId", orderId);

		CreatedOrderPositionEntry createdEntry = new CreatedOrderPositionEntry(orderId, abDto.getCNr());

		if (!abDto.getMandantCNr().equals(globalInfo.getMandant())) {
			respondNotFound(Param.ORDERID, orderId);
			return createdEntry;
		}
		
		if(!AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT.equals(abDto.getStatusCNr())) {
			respondExpectationFailed(EJBExceptionLP.FEHLER_STATUS);
			return createdEntry;
		}
		
		HvOptional<AuftragpositionDto> abpos = 
				setupOrderPosition(abDto, positionEntry);
		if(!abpos.isPresent()) return createdEntry;

		return updatePositionCreatedEntry(createdEntry, abpos.get());
    }
	

	@Override
	@DELETE
	@Path("/position/{" + Param.POSITIONID + "}")
	public void removePosition (
			@QueryParam(Param.USERID) String userId,
			@PathParam(Param.POSITIONID) Integer positionId) throws RemoteException {
		if(connectClient(userId) == null) return ;
		if(positionId == null) {
			respondBadRequestValueMissing(Param.POSITIONID);
			return ;
		}

		AuftragpositionDto abposDto = 
			auftragpositionCall.auftragpositionFindByPrimaryKeyOhneExc(positionId) ;
		if(abposDto == null) {
			respondNotFound(Param.POSITIONID, positionId.toString());
			return ;
		}
		AuftragDto abDto = 
				auftragCall.auftragFindByPrimaryKeyOhneExc(abposDto.getBelegIId()) ;
		if(abDto == null) {
			respondNotFound(Param.POSITIONID, positionId.toString());
			return ;
		}
		
		if(!AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT.equals(abDto.getStatusCNr())) {
			respondExpectationFailed(EJBExceptionLP.FEHLER_STATUS) ;
			return ;
		}		
		
		auftragpositionCall.removeAuftragposition(abposDto);
	}
	
	
	@Override
	@POST
	@Path("{" + Param.ORDERID + "}/manualitemposition")
    @Consumes({FORMAT_JSON, FORMAT_JSONutf8, FORMAT_XML})
    @Produces({ FORMAT_JSON, FORMAT_XML })
    public CreatedOrderPositionEntry createManualIdentPosition(
							 @QueryParam(Param.USERID) String userId,
							 @PathParam(Param.ORDERID) Integer orderId,
							 CreateOrderManualItemPositionEntry positionEntry) throws RemoteException {
		if (connectClient(userId) == null)
			return null;
		
		HvValidateBadRequest.notNull(positionEntry, "positionEntry");
		HvValidateBadRequest.notNull(orderId, "orderId");

		AuftragDto abDto = auftragCall.auftragFindByPrimaryKeyOhneExc(orderId);
		HvValidateNotFound.notNull(abDto, "orderId", orderId);

		CreatedOrderPositionEntry createdEntry = new CreatedOrderPositionEntry(orderId, abDto.getCNr());

		if (!abDto.getMandantCNr().equals(globalInfo.getMandant())) {
			respondNotFound(Param.ORDERID, orderId);
			return createdEntry;
		}

		if(!AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT.equals(abDto.getStatusCNr())) {
			respondExpectationFailed(EJBExceptionLP.FEHLER_STATUS);
			return createdEntry;
		}
		
		HvOptional<AuftragpositionDto> abpos = setupOrderPosition(abDto, positionEntry);
		if(!abpos.isPresent()) return createdEntry;

		return updatePositionCreatedEntry(createdEntry, abpos.get());
    }
	
	@Override
	@PUT
	@Path("{" + Param.ORDERID + "}/manualitemposition/{" + Param.POSITIONID + "}")
    @Consumes({FORMAT_JSON, FORMAT_JSONutf8, FORMAT_XML})
    @Produces({ FORMAT_JSON, FORMAT_XML })
    public CreatedOrderPositionEntry updateManualItemPosition(
							 @QueryParam(Param.USERID) String userId,
							 @PathParam(Param.ORDERID) Integer orderId,	
							 @PathParam(Param.POSITIONID) Integer positionId,
							 CreateOrderManualItemPositionEntry positionEntry) throws RemoteException {
		if (connectClient(userId) == null) return null;
		
		HvValidateBadRequest.notNull(positionEntry, "positionEntry");
		HvValidateBadRequest.notNull(orderId, Param.ORDERID);
		HvValidateBadRequest.notNull(positionId, Param.POSITIONID);
		
		AuftragDto abDto = auftragCall.auftragFindByPrimaryKeyOhneExc(orderId);
		HvValidateNotFound.notNull(abDto, Param.ORDERID, orderId);

		CreatedOrderPositionEntry createdEntry = new CreatedOrderPositionEntry(orderId, abDto.getCNr());

		if (!abDto.getMandantCNr().equals(globalInfo.getMandant())) {
			respondNotFound(Param.ORDERID, orderId);
			return createdEntry;
		}

		if(!AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT.equals(abDto.getStatusCNr())) {
			respondExpectationFailed(EJBExceptionLP.FEHLER_STATUS);
			return createdEntry;
		}

		HvOptional<AuftragpositionDto> ejbPos = HvOptional.ofNullable(
				auftragpositionCall
					.auftragpositionFindByPrimaryKeyOhneExc(positionId));
		if(!ejbPos.isPresent() || !ejbPos.get().getBelegIId().equals(abDto.getIId())) {
			respondNotFound(Param.POSITIONID, positionId);
			return createdEntry;
		}
	
		boolean update = LocaleFac.POSITIONSART_HANDEINGABE
				.equals(ejbPos.get().getPositionsartCNr());
		HvOptional<AuftragpositionDto> newPos = 
				setupOrderPosition(abDto, positionEntry);
		if(!newPos.isPresent()) return createdEntry;
		
		if(update) {
			newPos.get().setIId(ejbPos.get().getIId());
			newPos.get().setArtikelIId(ejbPos.get().getArtikelIId());
		} else {
			// TODO: Server behandelt ungleiche Positionsarten nicht
			auftragpositionCall.removeAuftragposition(ejbPos.get());
		}

		return updatePositionCreatedEntry(createdEntry, newPos.get());
    }
	

	private CreatedOrderPositionEntry updatePositionCreatedEntry(
			CreatedOrderPositionEntry createdEntry, 
			AuftragpositionDto posDto) throws RemoteException {
		if(posDto.getIId() == null) {
			Integer createdId = auftragpositionCall.createAuftragposition(posDto);	
			createdEntry.setOrderPositionId(createdId);
		} else {
			auftragpositionCall.updateAuftragposition(posDto);
			createdEntry.setOrderPositionId(posDto.getIId());
		}
		
		return createdEntry;
	}

    private HvOptional<AuftragpositionDto> setupOrderPosition(AuftragDto abDto,
    		CreateOrderItemPositionEntry positionEntry) throws RemoteException{
		ArtikelDto artikelDto = findArtikelByIdCnr(
				positionEntry.getItemId(), positionEntry.getItemCnr()) ;
		if(artikelDto == null) {
			return HvOptional.empty();
		}
		
		return setupOrderPositionByItem(artikelDto, abDto, positionEntry);
    }

    private HvOptional<AuftragpositionDto> setupOrderPosition(AuftragDto abDto,
    		CreateOrderManualItemPositionEntry positionEntry) throws RemoteException {

		AuftragpositionDto abposDto = new AuftragpositionDto();
		abposDto.setPositionsartCNr(LocaleFac.POSITIONSART_HANDEINGABE);
		if(mandantCall.hasFunctionKostentraeger()) {
			HvValidateBadRequest.notNull(
					positionEntry.getCostBearingUnitId(), "costBearingUnit");
			abposDto.setKostentraegerIId(positionEntry.getCostBearingUnitId());		
		}

		abposDto.setBArtikelbezeichnunguebersteuert(Helper.getShortTrue());
		abposDto.setCBez(positionEntry.getDescription());
		abposDto.setCZusatzbez(positionEntry.getDescription2());
		abposDto.setBMwstsatzuebersteuert(Helper.getShortTrue());		
		abposDto.setBNettopreisuebersteuert(Helper.getShortFalse());
		abposDto.setBRabattsatzuebersteuert(Helper.getShortFalse());
		abposDto.setNEinzelpreis(positionEntry.getNettoPrice()); 
		abposDto.setNNettoeinzelpreis(positionEntry.getNettoPrice());
		

		HvValidateBadRequest.notNull(
				positionEntry.getTaxDescriptionId(), "taxDescriptionId");
		MwstsatzDto mwstsatzDto = mandantCall.mwstsatzDtoZuDatum(
				positionEntry.getTaxDescriptionId(), abDto.getTBelegdatum());
		HvValidateBadRequest.notValid(mwstsatzDto != null,
				"taxDescriptionId", positionEntry.getTaxDescriptionId().toString());

		abposDto.setNMwstbetrag(Helper.getMehrwertsteuerBetragFuerNetto(
				abposDto.getNNettoeinzelpreis(), 
				BigDecimal.valueOf(mwstsatzDto.getFMwstsatz())));
		BigDecimal d = abposDto.getNNettoeinzelpreis().add(abposDto.getNMwstbetrag());
		abposDto.setNBruttoeinzelpreis(Helper.rundeKaufmaennisch(d, 4));
		abposDto.setMwstsatzIId(mwstsatzDto.getIId());

		abposDto.setBGesehen(Helper.getShortFalse());
		abposDto.setBHvmauebertragen(Helper.getShortTrue());
		abposDto.setBPauschal(Helper.getShortFalse());
		abposDto.setEinheitCNr(positionEntry.getUnitCnr());
		abposDto.setNMaterialzuschlagKurs(null);
		abposDto.setTMaterialzuschlagDatum(null);
		abposDto.setNMenge(positionEntry.getAmount());
		abposDto.setNOffeneMenge(abposDto.getNMenge());
		abposDto.setAuftragpositionstatusCNr(AuftragServiceFac.AUFTRAGPOSITIONSTATUS_OFFEN);
		abposDto.setBelegIId(abDto.getIId());
		abposDto.setTUebersteuerbarerLiefertermin(abDto.getDLiefertermin());
		abposDto.setNRabattbetrag(BigDecimal.ZERO);
		abposDto.setFRabattsatz(new Double(0.0d));
		abposDto.setFZusatzrabattsatz(new Double(0.0d));
		abposDto.setNMaterialzuschlag(BigDecimal.ZERO); 

		return HvOptional.of(abposDto);    	
    }
    
	private HvOptional<AuftragpositionDto> setupOrderPositionByItem(
			ArtikelDto artikelDto, AuftragDto abDto, 
			CreateOrderItemPositionEntry positionEntry) throws RemoteException  {

		AuftragpositionDto abposDto = new AuftragpositionDto();
		abposDto.setPositionsartCNr(LocaleFac.POSITIONSART_IDENT);
		abposDto.setArtikelIId(artikelDto.getIId());
		if(mandantCall.hasFunctionKostentraeger()) {
			HvValidateBadRequest.notNull(
					positionEntry.getCostBearingUnitId(), "costBearingUnit");
			abposDto.setKostentraegerIId(positionEntry.getCostBearingUnitId());		
		}

		abposDto.setBArtikelbezeichnunguebersteuert(Helper.getShortFalse());
		abposDto.setCBez(artikelDto.getArtikelsprDto() != null ? artikelDto.getArtikelsprDto().getCBez() : null);
//		abposDto.setCZusatzbez(artikelDto.getArtikelsprDto().getCZbez());
		abposDto.setCZusatzbez(artikelDto.getArtikelsprDto() != null ? artikelDto.getArtikelsprDto().getCZbez() : null);
		abposDto.setBMwstsatzuebersteuert(Helper.getShortFalse());
		abposDto.setBNettopreisuebersteuert(Helper.getShortFalse());
		abposDto.setBGesehen(Helper.getShortFalse());
		abposDto.setBHvmauebertragen(Helper.getShortTrue());
		abposDto.setBPauschal(Helper.getShortFalse());
		abposDto.setEinheitCNr(artikelDto.getEinheitCNr());
		abposDto.setNMaterialzuschlagKurs(null);
		abposDto.setTMaterialzuschlagDatum(null);
		abposDto.setNMenge(positionEntry.getAmount());
		abposDto.setNOffeneMenge(abposDto.getNMenge());
		abposDto.setAuftragpositionstatusCNr(AuftragServiceFac.AUFTRAGPOSITIONSTATUS_OFFEN);
		abposDto.setBelegIId(abDto.getIId());
		abposDto.setTUebersteuerbarerLiefertermin(abDto.getDLiefertermin());

		orderItemPriceCalculator.setAuftragDto(abDto);
		ItemPriceCalculationResult result = 
				orderItemPriceCalculator.calculate(artikelDto.getIId(), abposDto.getNMenge()) ;
		
		if(result.getVerkaufspreisDtoZielwaehrung().isEmpty()) {
			abposDto.setMwstsatzIId(orderItemPriceCalculator.getMwstSatzDto().getIId()) ;
		} else {
			abposDto.setMwstsatzIId(result.getVerkaufspreisDtoZielwaehrung().mwstsatzIId);			
		}
		abposDto.setNBruttoeinzelpreis(result.getVerkaufspreisDtoZielwaehrung().bruttopreis);
		abposDto.setNMwstbetrag(result.getVerkaufspreisDtoZielwaehrung().mwstsumme);
		abposDto.setNEinzelpreis(result.getVerkaufspreisDtoZielwaehrung().einzelpreis); 
		abposDto.setNNettoeinzelpreis(result.getVerkaufspreisDtoZielwaehrung().nettopreis); 
		abposDto.setNRabattbetrag(result.getVerkaufspreisDtoZielwaehrung().rabattsumme);
		abposDto.setFRabattsatz(result.getVerkaufspreisDtoZielwaehrung().rabattsatz);
		abposDto.setFZusatzrabattsatz(result.getVerkaufspreisDtoZielwaehrung().getDdZusatzrabattsatz());
		abposDto.setNMaterialzuschlag(result.getVerkaufspreisDtoZielwaehrung().bdMaterialzuschlag); 

		return HvOptional.of(abposDto);
	}

	private HvOptional<AuftragpositionDto> setupOrderPosition(AuftragDto abDto, String text) throws RemoteException {
		AuftragpositionDto abposDto = new AuftragpositionDto() ;
		abposDto.setPositionsartCNr(LocaleFac.POSITIONSART_TEXTEINGABE);
		abposDto.setAuftragpositionstatusCNr(AuftragServiceFac.AUFTRAGPOSITIONSTATUS_OFFEN);
		abposDto.setBArtikelbezeichnunguebersteuert(Helper.getShortFalse());
		abposDto.setBMwstsatzuebersteuert(Helper.getShortFalse());
		abposDto.setBNettopreisuebersteuert(Helper.getShortFalse());
		abposDto.setBelegIId(abDto.getIId());
		
		List<SeriennrChargennrMitMengeDto> snrDtos = new ArrayList<SeriennrChargennrMitMengeDto>();
		abposDto.setSeriennrChargennrMitMenge(snrDtos);
		
		return HvOptional.of(setupOrderPosition(abposDto, text));
	}
	
	private HvOptional<AuftragpositionDto> setupOrderPosition(AuftragDto abDto, 
			CreateOrderTextblockPositionEntry positionEntry) throws RemoteException {
		MediastandardDto mediaDto = findMediastandardByIdCnr(
				positionEntry.getTextblockId(), positionEntry.getTextblockCnr(),
				HvOptional.ofNullable(positionEntry.getLocaleCnr()));
		if(mediaDto == null) {
			return HvOptional.empty();
		}
		
		AuftragpositionDto abposDto = new AuftragpositionDto() ;
		abposDto.setPositionsartCNr(LocaleFac.POSITIONSART_TEXTBAUSTEIN);
		abposDto.setAuftragpositionstatusCNr(AuftragServiceFac.AUFTRAGPOSITIONSTATUS_OFFEN);
		abposDto.setBArtikelbezeichnunguebersteuert(Helper.getShortFalse());
		abposDto.setBMwstsatzuebersteuert(Helper.getShortFalse());
		abposDto.setBNettopreisuebersteuert(Helper.getShortFalse());
		abposDto.setBelegIId(abDto.getIId());
		abposDto.setMediastandardIId(mediaDto.getIId());
		
		List<SeriennrChargennrMitMengeDto> snrDtos = new ArrayList<SeriennrChargennrMitMengeDto>();
		abposDto.setSeriennrChargennrMitMenge(snrDtos);
		
		return HvOptional.of(abposDto);
	}
	
	private AuftragpositionDto setupOrderPosition(AuftragpositionDto posDto, String text) throws RemoteException {
		String posText = text == null ? "." 
				: StringHelper.trimCut(text, SystemFac.MAX_LAENGE_EDITORTEXT);
		
		posDto.setXTextinhalt(posText);
		return posDto;
	}
	
	private List<AuftragpositionDto> filterAbPositionItemId(
			Integer orderId, Integer itemId) throws RemoteException {
		// TODO: Ja, es waere sinnvoller gleich nur solche Positionen vom Server zu bekommen
		AuftragpositionDto dtos[] = auftragpositionCall
				.auftragpositionFindByAuftrag(orderId);
		return BelegpositionVerkaufHelper
				.filterItemId(Arrays.asList(dtos), itemId);
	}
	
	private ArtikelDto findArtikelByIdCnr(Integer artikelId, String artikelCnr) throws RemoteException {
		if(artikelId == null && StringHelper.isEmpty(artikelCnr)) {
			respondBadRequestValueMissing(Param.ITEMID);
			return null;
		}
		
		ArtikelDto artikelDto = null;
		if(artikelId != null) {
			artikelDto = artikelCall.artikelFindByPrimaryKeySmallOhneExc(artikelId);
		}
		
		if(artikelDto == null && !StringHelper.isEmpty(artikelCnr)) {
			artikelDto = artikelCall.artikelFindByCNrOhneExc(artikelCnr) ;
		}
		
		if(artikelDto == null) {
			respondItemNotFound(artikelId, artikelCnr);
			return null ;
		}
		
		return artikelDto ;
	}
	
	private MediastandardDto findMediastandardByIdCnr(
			Integer mediaId, String mediaCnr, HvOptional<String> locale) throws RemoteException {
		if(mediaId == null && StringHelper.isEmpty(mediaCnr)) {
			respondBadRequestValueMissing(Param.ITEMID);
			return null;
		}
		
		HvOptional<MediastandardDto> media = HvOptional.empty();
		if(mediaId != null) {
			media = mediaCall.mediaFindByPrimaryKey(mediaId);
		}
		
		if(!media.isPresent() && !StringHelper.isEmpty(mediaCnr)) {
			media = mediaCall.mediaFindByCnr(mediaCnr, locale);
		}
		
		if(!media.isPresent()) {
			respondMediaNotFound(mediaId, mediaCnr);
			return null ;
		}
		
		return media.get() ;
	}
	
	private void respondItemNotFound(Integer id, String cnr) {
		if(id != null) {
			respondNotFound(Param.ITEMID, id.toString());
		} else {
			respondNotFound(Param.ITEMCNR, cnr) ;
		}
	}
	
	private void respondMediaNotFound(Integer id, String cnr) {
		if(id != null) {
			respondNotFound(Param.ITEMID, id.toString());
		} else {
			respondNotFound(Param.ITEMCNR, cnr) ;
		}		
	}
	
	@Override
	@POST
	@Path("{" + Param.ORDERID + "}/activate")
	@Produces({FORMAT_JSON,FORMAT_JSONutf8, FORMAT_XML})
	public OrderActivationEntry postActivateOrder(
			@PathParam(Param.ORDERID) Integer orderId,
			@QueryParam(Param.USERID) String userId,
			@QueryParam("verified") Boolean verified) throws RemoteException, NamingException {
		OrderActivationEntry response = new OrderActivationEntry();
		response.setId(orderId);
		
		if (connectClient(userId) == null)
			return response;
		
		HvValidateBadRequest.notNull(orderId, "orderId");
		HvValidateBadRequest.notValid(Boolean.TRUE.equals(verified), "verified");
		
		AuftragDto abDto = auftragCall.auftragFindByPrimaryKeyOhneExc(orderId);
		HvValidateNotFound.notNull(abDto, "orderId", orderId);

		if (!abDto.getMandantCNr().equals(globalInfo.getMandant())) {
			respondNotFound(Param.ORDERID, orderId);
			return response;
		}

		if (!AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT.equals(abDto.getStatusCNr())) {
			respondExpectationFailed(EJBExceptionLP.FEHLER_STATUS);
			return response;
		}

		BelegPruefungDto pruefungDto = auftragCall.calculateActivate(abDto.getIId());
		return transformOrderCheck(pruefungDto);
	}
	
	private OrderActivationEntry transformOrderCheck(BelegPruefungDto pruefungDto) {
		OrderActivationEntry entry = new OrderActivationEntry();
		entry.setId(pruefungDto.getBelegId());
		entry.setCalculationTimestampMs(
				pruefungDto.getBerechnungsZeitpunkt().getTime());
		entry.setCustomerExpectsVATorderHasNoneVATpositions(
				pruefungDto.isKundeHatUstAberNichtUstPositionen());
		entry.setCustomerHasNoVATorderHasVATpositions(
				pruefungDto.isKundeHatKeineUstAberUstPositionen());
		return entry;
	}
	
	@Override
	@POST
	@Path("{" + Param.ORDERID + "}/sortbyitemnumber")
	@Produces({FORMAT_JSON,FORMAT_JSONutf8, FORMAT_XML})
	public OrderSortedEntry postSortOrderByItemnumber(
			@PathParam(Param.ORDERID) Integer orderId,
			@QueryParam(Param.USERID) String userId) throws RemoteException, NamingException {

		OrderSortedEntry response = new OrderSortedEntry();
		response.setId(orderId);
		
		if (connectClient(userId) == null)
			return response;
		
		HvValidateBadRequest.notNull(orderId, Param.ORDERID);
		
		AuftragDto abDto = auftragCall.auftragFindByPrimaryKeyOhneExc(orderId);
		HvValidateNotFound.notNull(abDto, Param.ORDERID, orderId);

		if (!abDto.getMandantCNr().equals(globalInfo.getMandant())) {
			respondNotFound(Param.ORDERID, orderId);
			return response;
		}

		if (!AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT.equals(abDto.getStatusCNr())) {
			respondExpectationFailed(EJBExceptionLP.FEHLER_STATUS);
			return response;
		}

		auftragpositionCall.sortiereNachArtikelnummer(orderId);
		response.setSorted(true);
		return response;
	}
}
