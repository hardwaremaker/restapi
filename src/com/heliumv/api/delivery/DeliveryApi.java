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
package com.heliumv.api.delivery;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.jcr.RepositoryException;
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
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.apache.commons.codec.binary.Base64;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.heliumv.api.BaseApi;
import com.heliumv.api.HvValidateBadRequest;
import com.heliumv.api.HvValidateExpectationFailed;
import com.heliumv.api.HvValidateNotFound;
import com.heliumv.api.document.DocumentInfoEntryList;
import com.heliumv.api.document.DocumentMetadata;
import com.heliumv.api.document.IDocumentCategoryService;
import com.heliumv.api.document.IDocumentService;
import com.heliumv.api.document.RawDocument;
import com.heliumv.api.item.ItemIdentityEntry;
import com.heliumv.api.item.ItemIdentityEntryList;
import com.heliumv.api.item.ItemIdentyEntryMapper;
import com.heliumv.bl.ItemPriceCalculationResult;
import com.heliumv.bl.LieferscheinItemPriceCalculator;
import com.heliumv.factory.IArtikelCall;
import com.heliumv.factory.IAuftragCall;
import com.heliumv.factory.IAuftragpositionCall;
import com.heliumv.factory.IDruckerCall;
import com.heliumv.factory.IGlobalInfo;
import com.heliumv.factory.IJudgeCall;
import com.heliumv.factory.IKundeCall;
import com.heliumv.factory.ILagerCall;
import com.heliumv.factory.ILieferscheinCall;
import com.heliumv.factory.ILieferscheinReportCall;
import com.heliumv.factory.ILieferscheinpositionCall;
import com.heliumv.factory.IMandantCall;
import com.heliumv.factory.IParameterCall;
import com.heliumv.factory.query.DeliveryQuery;
import com.heliumv.tools.FilterKriteriumCollector;
import com.heliumv.tools.JasperPrintHelper;
import com.heliumv.tools.StringHelper;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.lieferschein.service.BestaetigterLieferscheinDto;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.lieferschein.service.LieferscheinFac;
import com.lp.server.lieferschein.service.LieferscheinHandlerFeature;
import com.lp.server.lieferschein.service.LieferscheinpositionDto;
import com.lp.server.lieferschein.service.LieferscheinpositionFac;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.SystemFac;
import com.lp.server.util.HvOptional;
import com.lp.server.util.LieferscheinId;
import com.lp.server.util.collection.CollectionTools;
import com.lp.server.util.collection.ISelect;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParametersFeatures;
import com.lp.server.util.fastlanereader.service.query.QueryResult;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

import net.sf.jasperreports.engine.JRException;

@Service("hvDelivery")
@Path("/api/v1/delivery")
public class DeliveryApi extends BaseApi implements IDeliveryApi {
	private static Logger log = LoggerFactory.getLogger(DeliveryApi.class) ;

	public final static int MAX_CONTENT_LENGTH_DELIVERY = 500000 ;
	public final static int MAX_CONTENT_LENGTH_SIGNATURE = 1*1024*1024;

	@Autowired
	private IGlobalInfo globalInfo ;
	
	@Autowired
	private ILieferscheinCall lieferscheinCall ;
	@Autowired
	private IAuftragCall auftragCall ;
	@Autowired
	private IAuftragpositionCall auftragPositionCall ;
	@Autowired
	private ILieferscheinpositionCall lieferscheinpositionCall ;
	@Autowired
	private ILagerCall lagerCall ;
	@Autowired
	private ILieferscheinReportCall lieferscheinReportCall ;
	@Autowired
	private IMandantCall mandantCall ;
	@Autowired
	private IArtikelCall artikelCall ;
	@Autowired
	private LieferscheinItemPriceCalculator deliveryItemPriceCalculator ;
	@Autowired
	private IKundeCall kundeCall ;
//	@Autowired
//	private IPersonalCall personalCall ;
//	@Autowired
//	private ILocaleCall localeCall ;
	@Autowired
	private IJudgeCall judgeCall ;
	@Autowired
	private IDocumentService documentService;
	@Autowired
	private IDocumentCategoryService deliveryDocService;
	@Autowired
	private DeliveryEntryMapper deliveryEntryMapper ;
	@Autowired
	private DeliveryPositionEntryMapper deliveryPositionEntryMapper;
	@Autowired
	private ItemIdentyEntryMapper itemIdentityEntryMapper;
	@Autowired
	private IParameterCall parameterCall;
	@Autowired
	private DeliveryQuery deliveryQuery;
	@Autowired
	private DeliveryTextPositionEntryMapper deliveryTextPositionEntryMapper;
	@Autowired
	private IDruckerCall druckerCall;

//	@Override
//	@POST
//	@Path("/aviso")
//	@Produces({FORMAT_XML})
//	public String createDispatchNotification(
//			@QueryParam(Param.USERID) String userId, 
//			@QueryParam(Param.DELIVERYID) Integer deliveryId, 
//			@QueryParam(Param.DELIVERYCNR) String deliveryCnr) {
//		try {
//			if(connectClient(userId) == null) return null ;
//			if(deliveryId != null) {
//				LieferscheinDto lsDto = lieferscheinCall.lieferscheinFindByPrimaryKey(deliveryId) ; 
//				return createDispatchNotificationImpl(lsDto) ;
//			}
//			
//			if(!StringHelper.isEmpty(deliveryCnr)) {
//				return createDispatchNotificationCnr(deliveryCnr) ;
//			}
//
//			respondBadRequestValueMissing(Param.DELIVERYID) ;
//		} catch(NamingException e) {
//			respondUnavailable(e) ;
//		} catch(RemoteException e) {
//			respondUnavailable(e);
//		} catch(EJBExceptionLP e) {
//			respondBadRequest(e) ;
//		}
//
//		return null;
//	}
//
//	private String createDispatchNotificationCnr(String deliveryCnr) {
//		try {
//			LieferscheinDto lsDto = lieferscheinCall.lieferscheinFindByCNr(deliveryCnr) ;
//			if(lsDto == null) {
//				respondNotFound(Param.DELIVERYCNR, deliveryCnr);
//				return null ;
//			}
//			
//			return createDispatchNotificationImpl(lsDto) ;
//		} catch(NamingException e) {
//			respondUnavailable(e) ;
//		} catch(RemoteException e) {
//			respondUnavailable(e);
//		} catch(EJBExceptionLP e) {
//			respondBadRequest(e) ;
//		}
//
//		return null;
//	}
//
//	private String createDispatchNotificationImpl(LieferscheinDto deliveryDto) throws NamingException, RemoteException {
//		return null ;
////		return lieferscheinCall.createLieferscheinAviso(deliveryDto, globalInfo.getTheClientDto()) ;
//	}
	
	@Override
	@POST
	public Integer createDelivery(
			@QueryParam(Param.USERID) String userId,
			@QueryParam(Param.CUSTOMERCNR) Integer customerCnr,
			@QueryParam(Param.CUSTOMERID) Integer customerId,
			@QueryParam(Param.ORDERCNR) String orderCnr,
			@QueryParam(Param.ORDERID) Integer orderId) throws RemoteException {
		if(connectClient(userId) == null) return null;
		
		HvOptional<Integer> deliveryId = HvOptional.empty();
		if(Param.isInteger(customerCnr) || Param.isInteger(customerId)) {
			deliveryId = createDeliveryByCustomer(customerId, customerCnr);
		} else if(Param.isInteger(orderId)|| Param.isString(orderCnr)) {
			deliveryId = createDeliveryByOrder(orderId, orderCnr);
		}
		
		return deliveryId.get();
	}
	
	
	private HvOptional<Integer> createDeliveryByCustomer(
			Integer customerId, Integer customerCnr) throws RemoteException {

		KundeDto kundeDto = findKundeByIdCnr(customerId, customerCnr);
		if(kundeDto == null) {
			respondBadRequestValueMissing(Param.CUSTOMERID);
			return HvOptional.empty();
		}
		
		if(!validateKundeDto(kundeDto)) {
			return HvOptional.empty();
		}

		LieferscheinDto lsDto = findCreateLieferschein(kundeDto, null);
		if(lsDto == null) {
			return HvOptional.empty();
		}
		
		if(!validateLieferschein(lsDto)) {
			return HvOptional.empty() ;
		}			

		return HvOptional.of(lsDto.getIId());
	}
	
	private HvOptional<Integer> createDeliveryByOrder(
			Integer orderId, String orderCnr) throws RemoteException {
		HvOptional<AuftragDto> auftrag = findAuftragByIdCnr(orderId, orderCnr);
		if(!auftrag.isPresent()) {
			respondBadRequestValueMissing(Param.ORDERID);
			return HvOptional.empty();
		}

		if(!lagerCall.hatRolleBerechtigungAufLager(
				auftrag.get().getLagerIIdAbbuchungslager())) {
			respondForbidden(); 
			return HvOptional.empty();
		}
			
		return HvOptional.of(
				auftragCall.erzeugeLieferscheinAusAuftrag(auftrag.get().getIId()));
	}
	
	private HvOptional<AuftragDto> findAuftragByIdCnr(Integer orderId, String orderCnr) throws RemoteException {
		if(orderId == null && orderCnr == null) {
			respondBadRequestValueMissing(Param.ORDERID);
			return HvOptional.empty();
		}

		HvOptional<AuftragDto> auftrag = HvOptional.empty();
		if(orderId != null) {
			auftrag = HvOptional.of(
					auftragCall.auftragFindByPrimaryKeyOhneExc(orderId));
		}
		if(!auftrag.isPresent() && !StringHelper.isEmpty(orderCnr)) {
			auftrag = HvOptional.of(
					auftragCall.auftragFindByCnr(orderCnr));
		}
		
		if(!auftrag.isPresent() || !auftrag.get().getMandantCNr()
				.equals(globalInfo.getTheClientDto().getMandant())) {
			respondNotFound();
			return HvOptional.empty();
		}
		
		return auftrag;
	}

	@Override
	@POST
	@Path("/{" + Param.DELIVERYID + "}/itemposition")
	@Consumes({FORMAT_JSON, FORMAT_XML})	
	@Produces({FORMAT_JSON, FORMAT_XML})
	public CreatedDeliveryPositionEntry createIdentPosition(
			@QueryParam(Param.USERID) String userId,
			@PathParam(Param.DELIVERYID) Integer deliveryId,
			CreateDeliveryItemPositionEntry positionEntry) throws RemoteException {
		if(connectClient(userId) == null) return null;
		HvValidateBadRequest.notNull(positionEntry, "positionEntry");
		HvValidateBadRequest.notNull(deliveryId, "deliveryId");
		
		LieferscheinDto lsDto = lieferscheinCall.lieferscheinFindByPrimaryKey(deliveryId) ;
		HvValidateNotFound.notNull(lsDto, "deliveryId", deliveryId);

		CreatedDeliveryPositionEntry createdEntry = new CreatedDeliveryPositionEntry() ;
		createdEntry.setDeliveryId(lsDto.getIId());
		createdEntry.setDeliveryCnr(lsDto.getCNr());		

		if(!LieferscheinFac.LSSTATUS_ANGELEGT.equals(lsDto.getStatusCNr())) {
			respondExpectationFailed(EJBExceptionLP.FEHLER_STATUS) ;
			return createdEntry;
		}

		if(!lagerCall.hatRolleBerechtigungAufLager(lsDto.getLagerIId())) {
			respondForbidden(); 
			return createdEntry;
		}
		
		HvOptional<LieferscheinpositionDto> lspos =
				setupDeliveryPosition(lsDto, positionEntry, false);
		if(!lspos.isPresent()) return createdEntry;

		return updatePositionCreatedEntry(createdEntry, lspos.get());
	}	
	
	private HvOptional<LieferscheinpositionDto> setupDeliveryPosition(
			LieferscheinDto lsDto, 
			CreateDeliveryItemPositionEntry positionEntry, boolean update) throws RemoteException {
		if(positionEntry.getOrderPositionId() != null) {
			// Auftragsbezug in der position mit LS ohne Auftragsbezug
			if(lsDto.getAuftragIId() == null) {
				respondExpectationFailed("orderPositionId",
						positionEntry.getOrderPositionId().toString());
				return HvOptional.empty();
			}
			
			HvOptional<AuftragDto> abDto = HvOptional.ofNullable(
					auftragCall.auftragFindByPrimaryKeyOhneExc(lsDto.getAuftragIId()));			
			return setupDeliveryPositionByOrderPos(abDto, lsDto, positionEntry, update);	
		}
		
		ArtikelDto artikelDto = findArtikelByIdCnr(
				positionEntry.getItemId(), positionEntry.getItemCnr()) ;
		if(artikelDto == null) {
			return HvOptional.empty();
		}
		
		return setupDeliveryPositionByItem(artikelDto, lsDto, positionEntry, update);
	}
	
	private HvOptional<LieferscheinpositionDto> setupDeliveryPositionByOrderPos(
			HvOptional<AuftragDto> ab, LieferscheinDto lsDto, 
			CreateDeliveryItemPositionEntry positionEntry, boolean update) throws RemoteException {
		HvOptional<AuftragpositionDto> abpos = HvOptional.of(auftragPositionCall
				.auftragpositionFindByPrimaryKeyOhneExc(positionEntry.getOrderPositionId())) ;
		if(!abpos.isPresent()) {
			respondBadRequest(Param.ORDERPOSITIONID, positionEntry.getOrderPositionId().toString());
			return HvOptional.empty();
		}

		if(!ab.isPresent()) {
			respondExpectationFailed("orderId", lsDto.getAuftragIId().toString());
			return HvOptional.empty();
		}

		AuftragpositionDto abposDto = abpos.get();
		AuftragDto abDto = ab.get();
		if(!abDto.getIId().equals(abposDto.getBelegIId())) {
			respondBadRequest(Param.ORDERPOSITIONID, positionEntry.getOrderPositionId().toString());
			return HvOptional.empty();
		}
		
		Collection<LieferscheinpositionDto> lsposDtos = filterLsPosition(lsDto.getIId(), positionEntry.getOrderPositionId()) ;
		if(lsposDtos.isEmpty()) {
			LieferscheinpositionDto lsposDto = new LieferscheinpositionDto() ;
			lsposDto.setLieferscheinpositionartCNr(abposDto.getPositionsartCNr());
			lsposDto.setArtikelIId(abposDto.getArtikelIId());
			lsposDto.setKostentraegerIId(abposDto.getKostentraegerIId());
			lsposDto.setCLvposition(abposDto.getCLvposition());
			lsposDto.setBArtikelbezeichnunguebersteuert(abposDto.getBArtikelbezeichnunguebersteuert());
			lsposDto.setCBez(abposDto.getCBez());
			lsposDto.setCZusatzbez(abposDto.getCZusatzbez());
			lsposDto.setBMwstsatzuebersteuert(abposDto.getBMwstsatzuebersteuert());
			lsposDto.setBNettopreisuebersteuert(abposDto.getBNettopreisuebersteuert());
			lsposDto.setBRabattsatzuebersteuert(abposDto.getBRabattsatzuebersteuert());
			lsposDto.setEinheitCNr(abposDto.getEinheitCNr());
			lsposDto.setFRabattsatz(abposDto.getFRabattsatz());
			lsposDto.setFZusatzrabattsatz(abposDto.getFZusatzrabattsatz());
			lsposDto.setNMaterialzuschlagKurs(abposDto.getNMaterialzuschlagKurs());
			lsposDto.setTMaterialzuschlagDatum(abposDto.getTMaterialzuschlagDatum());
			lsposDto.setLieferscheinIId(lsDto.getIId());
			lsposDto.setMwstsatzIId(abposDto.getMwstsatzIId());
			lsposDto.setNBruttoeinzelpreis(abposDto.getNBruttoeinzelpreis());
			lsposDto.setNMenge(positionEntry.getAmount());
			lsposDto.setNMwstbetrag(abposDto.getNMwstbetrag());
			lsposDto.setNEinzelpreis(abposDto.getNEinzelpreis());
			lsposDto.setNNettoeinzelpreis(abposDto.getNNettoeinzelpreis());
			lsposDto.setNRabattbetrag(abposDto.getNRabattbetrag());
			lsposDto.setTypCNr(abposDto.getTypCNr());
			lsposDto.setVerleihIId(abposDto.getVerleihIId());		
			lsposDto.setAuftragpositionIId(abposDto.getIId());
			lsposDto.setLagerIId(abDto.getLagerIIdAbbuchungslager());

			List<SeriennrChargennrMitMengeDto> snrDtos = new ArrayList<SeriennrChargennrMitMengeDto>() ;
			if(!addItemIdentities(snrDtos, positionEntry.getItemIdentity())) {
				return HvOptional.empty();
			}

			lsposDto.setSeriennrChargennrMitMenge(snrDtos) ;				
			return HvOptional.of(lsposDto);
		}

		if(lsposDtos.size() != 1) {
			respondExpectationFailed(EJBExceptionLP.FEHLER_BEI_FIND);
			return HvOptional.empty();
		}
		
		LieferscheinpositionDto lsposDto = lsposDtos.iterator().next();
		if(update) {
			lsposDto.setNMenge(positionEntry.getAmount());
		} else {
			lsposDto.setNMenge(lsposDto.getNMenge().add(positionEntry.getAmount())) ;			
		}
		if(lsposDto.getSeriennrChargennrMitMenge() == null || update) {
			lsposDto.setSeriennrChargennrMitMenge(new ArrayList<SeriennrChargennrMitMengeDto>());
		}
		List<SeriennrChargennrMitMengeDto> snrDtos = lsposDto.getSeriennrChargennrMitMenge(); 
		return HvOptional.ofNullable(
				addItemIdentities(snrDtos, positionEntry.getItemIdentity()) ? lsposDto : null);
	}

	private HvOptional<LieferscheinpositionDto> setupDeliveryPositionByItem(
			ArtikelDto artikelDto, LieferscheinDto lsDto, 
			CreateDeliveryItemPositionEntry positionEntry, boolean update) throws RemoteException  {

		Collection<LieferscheinpositionDto> lsposDtos = filterLsPositionItemId(lsDto.getIId(), artikelDto.getIId()) ;
		LieferscheinpositionDto lsposDto = null;
		
		boolean useExisting = lsposDtos.size() > 0 && 
				(update || parameterCall.getLieferscheinPositionenMitAPIVerdichten());
		
//		if(lsposDtos.isEmpty() || !parameterCall.getLieferscheinPositionenMitAPIVerdichten()) {
		if(!useExisting) {
			lsposDto = new LieferscheinpositionDto();
			lsposDto.setLieferscheinpositionartCNr(LocaleFac.POSITIONSART_IDENT);
			lsposDto.setArtikelIId(artikelDto.getIId());
			if(mandantCall.hasFunctionKostentraeger()) {
//				lsposDto.setKostentraegerIId(artikelDto.getlsDto.getKostentraegerIId());			
			}
//			lsposDto.setCLvposition(abposDto.getCLvposition());
			lsposDto.setBArtikelbezeichnunguebersteuert((short)0);
			lsposDto.setCBez(artikelDto.getArtikelsprDto() != null ? artikelDto.getArtikelsprDto().getCBez() : null);
			lsposDto.setCZusatzbez(artikelDto.getArtikelsprDto().getCZbez());
			lsposDto.setBMwstsatzuebersteuert(Helper.getShortFalse());
			lsposDto.setBNettopreisuebersteuert(Helper.getShortFalse());
			lsposDto.setBKeinlieferrest(Helper.getShortFalse());
			lsposDto.setEinheitCNr(artikelDto.getEinheitCNr());
			lsposDto.setNMaterialzuschlagKurs(null);
			lsposDto.setTMaterialzuschlagDatum(null);
			lsposDto.setLieferscheinIId(lsDto.getIId());
			lsposDto.setNMenge(positionEntry.getAmount());
			lsposDto.setLagerIId(lsDto.getLagerIId());		
			
			List<SeriennrChargennrMitMengeDto> snrDtos = new ArrayList<SeriennrChargennrMitMengeDto>() ;
			if(!addItemIdentities(snrDtos, positionEntry.getItemIdentity())) {
				return HvOptional.empty();
			}
			lsposDto.setSeriennrChargennrMitMenge(snrDtos) ;				
		} else {
			// Immer die erste Position verwenden, egal wieviele Positionen des Artikels es gibt
			lsposDto = lsposDtos.iterator().next();
			if(update) {
				lsposDto.setNMenge(positionEntry.getAmount());
			} else {
				lsposDto.setNMenge(lsposDto.getNMenge().add(positionEntry.getAmount()));				
			}
			
			if(lsposDto.getSeriennrChargennrMitMenge() == null || update) {
				lsposDto.setSeriennrChargennrMitMenge(new ArrayList<SeriennrChargennrMitMengeDto>());
			}
			List<SeriennrChargennrMitMengeDto> snrDtos = lsposDto.getSeriennrChargennrMitMenge() ; 
			if(!addItemIdentities(snrDtos, positionEntry.getItemIdentity())) {
				return HvOptional.empty();
			}
		}

		deliveryItemPriceCalculator.setLieferscheinDto(lsDto);
		ItemPriceCalculationResult result = 
				deliveryItemPriceCalculator.calculate(artikelDto.getIId(), lsposDto.getNMenge()) ;
		
		if(result.getVerkaufspreisDtoZielwaehrung().isEmpty()) {
			lsposDto.setMwstsatzIId(deliveryItemPriceCalculator.getMwstSatzDto().getIId()) ;
		} else {
			lsposDto.setMwstsatzIId(result.getVerkaufspreisDtoZielwaehrung().mwstsatzIId);			
		}
		lsposDto.setNBruttoeinzelpreis(result.getVerkaufspreisDtoZielwaehrung().bruttopreis);
		lsposDto.setNMwstbetrag(result.getVerkaufspreisDtoZielwaehrung().mwstsumme);
		lsposDto.setNEinzelpreis(result.getVerkaufspreisDtoZielwaehrung().einzelpreis); 
		lsposDto.setNNettoeinzelpreis(result.getVerkaufspreisDtoZielwaehrung().nettopreis); 
		lsposDto.setNRabattbetrag(result.getVerkaufspreisDtoZielwaehrung().rabattsumme);
		lsposDto.setFRabattsatz(result.getVerkaufspreisDtoZielwaehrung().rabattsatz);
		lsposDto.setFZusatzrabattsatz(result.getVerkaufspreisDtoZielwaehrung().getDdZusatzrabattsatz());
		lsposDto.setNMaterialzuschlag(result.getVerkaufspreisDtoZielwaehrung().bdMaterialzuschlag); 

//		lsposDto.setNRabattbetrag(abposDto.getNRabattbetrag());

//		lsposDto.setTypCNr(abposDto.getTypCNr());
//		lsposDto.setVerleihIId(null);		
//		lsposDto.setAuftragpositionIId(null);

		return HvOptional.of(lsposDto);
	}

	@Override
	@PUT
	@Path("/{" + Param.DELIVERYID + "}/itemposition/{" + Param.POSITIONID + "}")
	@Consumes({FORMAT_JSON, FORMAT_XML})	
	@Produces({FORMAT_JSON, FORMAT_XML})
	public CreatedDeliveryPositionEntry updateItemPosition(
			@QueryParam(Param.USERID) String userId,
			@PathParam(Param.DELIVERYID) Integer deliveryId,
			@PathParam(Param.POSITIONID) Integer positionId,
			CreateDeliveryItemPositionEntry positionEntry) throws RemoteException {
		if(connectClient(userId) == null) return null;
		HvValidateBadRequest.notNull(positionEntry, "positionEntry");
		HvValidateBadRequest.notNull(deliveryId, "deliveryId");
		HvValidateBadRequest.notNull(positionId, "positionId");
		
		LieferscheinDto lsDto = lieferscheinCall.lieferscheinFindByPrimaryKey(deliveryId);
		HvValidateNotFound.notNull(lsDto, Param.DELIVERYID, deliveryId);

		CreatedDeliveryPositionEntry createdEntry = new CreatedDeliveryPositionEntry() ;
		createdEntry.setDeliveryId(lsDto.getIId());
		createdEntry.setDeliveryCnr(lsDto.getCNr());		

		if(!LieferscheinFac.LSSTATUS_ANGELEGT.equals(lsDto.getStatusCNr())) {
			respondExpectationFailed(EJBExceptionLP.FEHLER_STATUS) ;
			return createdEntry;
		}

		if(!lagerCall.hatRolleBerechtigungAufLager(lsDto.getLagerIId())) {
			respondForbidden(); 
			return createdEntry;
		}
		
		HvOptional<LieferscheinpositionDto> ejbPos = HvOptional.ofNullable(
				lieferscheinpositionCall
					.lieferscheinpositionFindByPrimaryKey(positionId));
		if(!ejbPos.isPresent() || !ejbPos.get().getLieferscheinIId().equals(lsDto.getIId())) {
			respondNotFound("positionId", positionId);
			return createdEntry;
		}
		
		HvOptional<LieferscheinpositionDto> newPos = 
				setupDeliveryPosition(lsDto, positionEntry, true);
		if(!newPos.isPresent()) return createdEntry;
		
		if(ejbPos.get().getPositionsartCNr().equals(newPos.get().getPositionsartCNr())) {
			// Text durch anderen Text ersetzen
			newPos.get().setIId(ejbPos.get().getIId());			
		} else {
			// TODO: Server behandelt ungleiche Positionsarten nicht
			lieferscheinpositionCall.removeLieferscheinposition(ejbPos.get().getIId());
		}

		return updatePositionCreatedEntry(createdEntry, newPos.get());
	}


	@Override
	@POST
	@Path("/{" + Param.DELIVERYID + "}/textposition")
	@Consumes({FORMAT_JSON, FORMAT_XML})	
	@Produces({FORMAT_JSON, FORMAT_XML})
	public CreatedDeliveryPositionEntry createTextPosition(
			@QueryParam(Param.USERID) String userId,
			@PathParam(Param.DELIVERYID) Integer deliveryId,
			CreateDeliveryTextPositionEntry positionEntry) throws RemoteException {
		if(connectClient(userId) == null) return null;
		HvValidateBadRequest.notNull(positionEntry, "positionEntry");
		HvValidateBadRequest.notNull(deliveryId, "deliveryId");
		
		LieferscheinDto lsDto = lieferscheinCall.lieferscheinFindByPrimaryKey(deliveryId);
		HvValidateNotFound.notNull(lsDto, "deliveryId", deliveryId);

		CreatedDeliveryPositionEntry createdEntry = new CreatedDeliveryPositionEntry() ;
		createdEntry.setDeliveryId(lsDto.getIId());
		createdEntry.setDeliveryCnr(lsDto.getCNr());		

		if(!LieferscheinFac.LSSTATUS_ANGELEGT.equals(lsDto.getStatusCNr())) {
			respondExpectationFailed(EJBExceptionLP.FEHLER_STATUS) ;
			return createdEntry;
		}

		if(!lagerCall.hatRolleBerechtigungAufLager(lsDto.getLagerIId())) {
			respondForbidden(); 
			return createdEntry;
		}
		
		LieferscheinpositionDto lsposDto = 
				setupDeliveryPosition(lsDto, positionEntry.getText()) ;
		if(lsposDto == null) return createdEntry ;

		return updatePositionCreatedEntry(createdEntry, lsposDto);
	}
	
	@Override
	@PUT
	@Path("/{" + Param.DELIVERYID + "}/textposition/{" + Param.POSITIONID + "}")
	@Consumes({FORMAT_JSON, FORMAT_XML})	
	@Produces({FORMAT_JSON, FORMAT_XML})
	public CreatedDeliveryPositionEntry updateTextPosition(
			@QueryParam(Param.USERID) String userId,
			@PathParam(Param.DELIVERYID) Integer deliveryId,
			@PathParam(Param.POSITIONID) Integer positionId,
			CreateDeliveryTextPositionEntry positionEntry) throws RemoteException {
		if(connectClient(userId) == null) return null;
		HvValidateBadRequest.notNull(positionEntry, "positionEntry");
		HvValidateBadRequest.notNull(deliveryId, "deliveryId");
		HvValidateBadRequest.notNull(positionId, "positionId");
		
		LieferscheinDto lsDto = lieferscheinCall.lieferscheinFindByPrimaryKey(deliveryId);
		HvValidateNotFound.notNull(lsDto, Param.DELIVERYID, deliveryId);

		CreatedDeliveryPositionEntry createdEntry = new CreatedDeliveryPositionEntry() ;
		createdEntry.setDeliveryId(lsDto.getIId());
		createdEntry.setDeliveryCnr(lsDto.getCNr());		

		if(!LieferscheinFac.LSSTATUS_ANGELEGT.equals(lsDto.getStatusCNr())) {
			respondExpectationFailed(EJBExceptionLP.FEHLER_STATUS) ;
			return createdEntry;
		}

		if(!lagerCall.hatRolleBerechtigungAufLager(lsDto.getLagerIId())) {
			respondForbidden(); 
			return createdEntry;
		}
		
		HvOptional<LieferscheinpositionDto> pos = HvOptional.ofNullable(
				lieferscheinpositionCall
					.lieferscheinpositionFindByPrimaryKey(positionId));
		if(!pos.isPresent() || !pos.get().getLieferscheinIId().equals(lsDto.getIId())) {
			respondNotFound("positionId", positionId);
			return createdEntry;
		}
		LieferscheinpositionDto lsposDto = 
				setupDeliveryPosition(lsDto, positionEntry.getText());
		
		if(pos.get().getPositionsartCNr().equals(lsposDto.getPositionsartCNr())) {
			// Text durch anderen Text ersetzen
			lsposDto.setIId(pos.get().getIId());			
		} else {
			// TODO: Server behandelt ungleiche Positionsarten nicht
			lieferscheinpositionCall.removeLieferscheinposition(pos.get().getIId());
		}

		return updatePositionCreatedEntry(createdEntry, lsposDto);
	}
	
	private CreatedDeliveryPositionEntry updatePositionCreatedEntry(
			CreatedDeliveryPositionEntry createdEntry, 
			LieferscheinpositionDto posDto) throws RemoteException {
		if(posDto.getIId() == null) {
			Integer createdId = lieferscheinpositionCall.createLieferscheinposition(
					posDto, false, new ArrayList<SeriennrChargennrMitMengeDto>());	
			createdEntry.setDeliveryPositionId(createdId);
		} else {
			Integer createdId = lieferscheinpositionCall.updateLieferscheinposition(
						posDto, new ArrayList<SeriennrChargennrMitMengeDto>());
			createdEntry.setDeliveryPositionId(createdId != null ? createdId : posDto.getIId());
		}

		HvOptional<LieferscheinpositionDto> pos = 
				HvOptional.of(lieferscheinpositionCall
						.lieferscheinpositionFindByPrimaryKey(
								createdEntry.getDeliveryPositionId()));
		if(pos.isPresent()) {
			LieferscheinpositionDto lsposDto = pos.get();
			if(lsposDto.getSeriennrChargennrMitMenge() == null) {
				lsposDto.setSeriennrChargennrMitMenge(
						new ArrayList<SeriennrChargennrMitMengeDto>());
			}

			createdEntry.setDeliveredAmount(lsposDto.getNMenge());
			createdEntry.setItemIdentity(
					transform(lsposDto.getSeriennrChargennrMitMenge()));
		}
		
		return createdEntry;
	}
	
	@Override
	@GET
	@Path("/print")
	public Integer printDelivery(
			@QueryParam(Param.USERID) String userId, 
			@QueryParam(Param.DELIVERYID) Integer deliveryId,
			@QueryParam("sort") String sortcnr) throws RemoteException {
		if(connectClient(userId) == null) return null ;
		if(deliveryId == null) {
			respondBadRequestValueMissing(Param.DELIVERYID) ;
			return null ;
		}
		
		DeliveryPositionSort sortMode = DeliveryPositionSort.NOTINITIALIZED;
		if(StringHelper.hasContent(sortcnr)) {
			try {
				sortMode = DeliveryPositionSort.fromId(sortcnr);				
			} catch(IllegalArgumentException e) {
				respondBadRequest("sort", sortcnr);
				return null;
			}
		}
		
		LieferscheinDto lsDto = lieferscheinCall.lieferscheinFindByPrimaryKey(deliveryId) ;
		if(lsDto == null) {
			respondNotFound(Param.DELIVERYID, deliveryId.toString());
			return null ;
		}
		
		if(!LieferscheinFac.LSSTATUS_ANGELEGT.equals(lsDto.getStatusCNr())) {
			respondExpectationFailed(EJBExceptionLP.FEHLER_STATUS) ;
			return null ;
		}
		
		if(sortMode == DeliveryPositionSort.NOTINITIALIZED) {
			sortMode = parameterCall.getLieferscheinpositionSortierung();
		}
		
		if(DeliveryPositionSort.ITEMNUMBER.equals(sortMode)) {
			lieferscheinpositionCall.sortiereNachArtikelnummer(deliveryId);
		}
		if(DeliveryPositionSort.ORDERPOSITION.equals(sortMode)) {
			lieferscheinpositionCall.sortiereNachAuftragsnummer(deliveryId);
		}

		lieferscheinCall.berechneAktiviereBelegControlled(deliveryId);
		lieferscheinReportCall.printLieferscheinOnServer(deliveryId) ;
		return deliveryId ;
	}
	
	
	@POST
	@Path("/positionfromorder")
	@Consumes({FORMAT_JSON, FORMAT_XML})	
	@Produces({FORMAT_JSON, FORMAT_XML})
	public CreatedDeliveryPositionEntry createOrUpdatePositionFromOrder(
			@QueryParam(Param.USERID) String userId,
			CreateDeliveryPositionEntry positionEntry) throws RemoteException {
		if(connectClient(userId) == null) return null ;
		if(positionEntry == null) {
			respondBadRequestValueMissing("positionEntry");
			return null ;
		}

		// Es werden (derzeit) nur auftragsbezogene LS unterstuetzt
		if(positionEntry.getOrderPositionId() == null) {
			respondBadRequestValueMissing(Param.ORDERPOSITIONID);
			return null ;
		}
				
		LieferscheinDto lsDto = null ; 
		// LS kann optional angegeben werden. Falls angegeben, muss er existieren
		if(positionEntry.getDeliveryId() != null) {
			lsDto = lieferscheinCall.lieferscheinFindByPrimaryKey(positionEntry.getDeliveryId()) ; 
			if(lsDto == null) {
				respondNotFound(Param.DELIVERYID, positionEntry.getDeliveryId().toString());
				return null ;
			}

			if(!LieferscheinFac.LSSTATUS_ANGELEGT.equals(lsDto.getStatusCNr())) {
				respondExpectationFailed(EJBExceptionLP.FEHLER_STATUS) ;
				return null ;
			}

			if(!lagerCall.hatRolleBerechtigungAufLager(lsDto.getLagerIId())) {
				respondUnauthorized(); 
				return null ;
			}					
		}

		if(positionEntry.getOrderId() == null) {
			respondBadRequestValueMissing(Param.ORDERID);
			return null ;
		}

		AuftragDto abDto = auftragCall.auftragFindByPrimaryKeyOhneExc(positionEntry.getOrderId()) ;
		if(abDto == null) {
			respondBadRequest(Param.ORDERID, positionEntry.getOrderId().toString());
			return null ;
		}
		
		// Wurde kein LS angegeben, wird ein neuer erzeugt
		if(lsDto == null) {
			if(!lagerCall.hatRolleBerechtigungAufLager(abDto.getLagerIIdAbbuchungslager())) {
				respondUnauthorized(); 
				return null ;
			}
			
			Integer deliveryId = auftragCall.erzeugeLieferscheinAusAuftrag(positionEntry.getOrderId()) ;
			lsDto = lieferscheinCall.lieferscheinFindByPrimaryKey(deliveryId) ;
		}
		
		if(lsDto == null) {
			respondBadRequest(Param.ORDERID, positionEntry.getOrderId().toString());
			return null ;
		}

		CreatedDeliveryPositionEntry createEntry = new CreatedDeliveryPositionEntry() ;
		createEntry.setDeliveryId(lsDto.getIId());
		createEntry.setDeliveryCnr(lsDto.getCNr());		
		createEntry.setOrderId(abDto.getIId());
		createEntry.setOrderPositionId(positionEntry.getOrderPositionId());
		
		LieferscheinpositionDto lsposDto = setupDeliveryPosition(lsDto, abDto, positionEntry) ;
		if(lsposDto == null) return createEntry ;

		Integer createdId = null ;
		if(lsposDto.getIId() == null) {
			createdId = lieferscheinpositionCall.createLieferscheinposition(
					lsposDto, false, new ArrayList<SeriennrChargennrMitMengeDto>()) ;			
		} else {
			createdId = lieferscheinpositionCall.updateLieferscheinposition(
					lsposDto, new ArrayList<SeriennrChargennrMitMengeDto>()) ;
		}	
		createEntry.setDeliveryPositionId(createdId) ;
		
		lsposDto = lieferscheinpositionCall.lieferscheinpositionFindByPrimaryKey(createdId) ;
		if(lsposDto.getSeriennrChargennrMitMenge() == null) {
			lsposDto.setSeriennrChargennrMitMenge(new ArrayList<SeriennrChargennrMitMengeDto>());
		}
		createEntry.setDeliveredAmount(lsposDto.getNMenge());
		
		AuftragpositionDto abposDto = auftragPositionCall
				.auftragpositionFindByPrimaryKeyOhneExc(positionEntry.getOrderPositionId()) ;
		if(abposDto != null) {
			createEntry.setAmount(abposDto.getNMenge());
			List<SeriennrChargennrMitMengeDto> snrDtos = lsposDto.getSeriennrChargennrMitMenge() ;
			addItemIdentities(snrDtos, createEntry.getItemIdentity()) ; 
			createEntry.setOpenAmount(abposDto.getNOffeneMenge()) ;
		}
		
		return createEntry;
	}	
	
	
	private LieferscheinpositionDto setupDeliveryPosition(
			LieferscheinDto lsDto, AuftragDto abDto, CreateDeliveryPositionEntry positionEntry) throws RemoteException {
		AuftragpositionDto abposDto = auftragPositionCall.auftragpositionFindByPrimaryKeyOhneExc(positionEntry.getOrderPositionId()) ;
		if(abposDto == null) {
			respondBadRequest(Param.ORDERPOSITIONID, positionEntry.getOrderPositionId().toString());
			return null ;
		}

		if(!abDto.getIId().equals(abposDto.getBelegIId())) {
			respondBadRequest(Param.ORDERID, positionEntry.getOrderPositionId().toString());
			return null ;
		}
		
		Collection<LieferscheinpositionDto> lsposDtos = filterLsPosition(lsDto.getIId(), positionEntry.getOrderPositionId()) ;
		if(lsposDtos.isEmpty()) {
			LieferscheinpositionDto lsposDto = new LieferscheinpositionDto() ;
			lsposDto.setLieferscheinpositionartCNr(abposDto.getPositionsartCNr());
			lsposDto.setArtikelIId(abposDto.getArtikelIId());
			lsposDto.setKostentraegerIId(abposDto.getKostentraegerIId());
			lsposDto.setCLvposition(abposDto.getCLvposition());
			lsposDto.setBArtikelbezeichnunguebersteuert(abposDto.getBArtikelbezeichnunguebersteuert());
			lsposDto.setCBez(abposDto.getCBez());
			lsposDto.setCZusatzbez(abposDto.getCZusatzbez());
			lsposDto.setBMwstsatzuebersteuert(abposDto.getBMwstsatzuebersteuert());
			lsposDto.setBNettopreisuebersteuert(abposDto.getBNettopreisuebersteuert());
			lsposDto.setBRabattsatzuebersteuert(abposDto.getBRabattsatzuebersteuert());
			lsposDto.setEinheitCNr(abposDto.getEinheitCNr());
			lsposDto.setFRabattsatz(abposDto.getFRabattsatz());
			lsposDto.setFZusatzrabattsatz(abposDto.getFZusatzrabattsatz());
			lsposDto.setNMaterialzuschlagKurs(abposDto.getNMaterialzuschlagKurs());
			lsposDto.setTMaterialzuschlagDatum(abposDto.getTMaterialzuschlagDatum());
			lsposDto.setLieferscheinIId(lsDto.getIId());
			lsposDto.setMwstsatzIId(abposDto.getMwstsatzIId());
			lsposDto.setNBruttoeinzelpreis(abposDto.getNBruttoeinzelpreis());
			lsposDto.setNMenge(positionEntry.getAmount());
			lsposDto.setNMwstbetrag(abposDto.getNMwstbetrag());
			lsposDto.setNEinzelpreis(abposDto.getNEinzelpreis());
			lsposDto.setNNettoeinzelpreis(abposDto.getNNettoeinzelpreis());
			lsposDto.setNRabattbetrag(abposDto.getNRabattbetrag());
			lsposDto.setTypCNr(abposDto.getTypCNr());
			lsposDto.setVerleihIId(abposDto.getVerleihIId());		
			lsposDto.setAuftragpositionIId(abposDto.getIId());
			lsposDto.setLagerIId(abDto.getLagerIIdAbbuchungslager());

			List<SeriennrChargennrMitMengeDto> snrDtos = new ArrayList<SeriennrChargennrMitMengeDto>() ;
			if(!addItemIdentities(snrDtos, positionEntry.getItemIdentity())) {
				return null ;
			}
			lsposDto.setSeriennrChargennrMitMenge(snrDtos) ;				
			return lsposDto ;
		}

		if(lsposDtos.size() != 1) {
			respondExpectationFailed(EJBExceptionLP.FEHLER_BEI_FIND);
			return null ;
		}
		
		LieferscheinpositionDto lsposDto = lsposDtos.iterator().next() ;
		lsposDto.setNMenge(lsposDto.getNMenge().add(positionEntry.getAmount())) ;
		if(lsposDto.getSeriennrChargennrMitMenge() == null) {
			lsposDto.setSeriennrChargennrMitMenge(new ArrayList<SeriennrChargennrMitMengeDto>());
		}
		List<SeriennrChargennrMitMengeDto> snrDtos = lsposDto.getSeriennrChargennrMitMenge() ; 
		return addItemIdentities(snrDtos, positionEntry.getItemIdentity()) ? lsposDto : null ; 
	}

	private LieferscheinpositionDto setupDeliveryPosition(
			LieferscheinDto lsDto, ArtikelDto artikelDto, CreateItemDeliveryPositionEntry positionEntry) throws RemoteException  {

		Collection<LieferscheinpositionDto> lsposDtos = filterLsPositionItemId(lsDto.getIId(), artikelDto.getIId()) ;
		LieferscheinpositionDto lsposDto = null;
		if(lsposDtos.isEmpty() || !parameterCall.getLieferscheinPositionenMitAPIVerdichten()) {
			lsposDto = new LieferscheinpositionDto() ;
			lsposDto.setLieferscheinpositionartCNr(LocaleFac.POSITIONSART_IDENT);
			lsposDto.setArtikelIId(artikelDto.getIId());
			if(mandantCall.hasFunctionKostentraeger()) {
//				lsposDto.setKostentraegerIId(artikelDto.getlsDto.getKostentraegerIId());			
			}
//			lsposDto.setCLvposition(abposDto.getCLvposition());
			lsposDto.setBArtikelbezeichnunguebersteuert((short)0);
			lsposDto.setCBez(artikelDto.getArtikelsprDto() != null ? artikelDto.getArtikelsprDto().getCBez() : null);
			lsposDto.setCZusatzbez(artikelDto.getArtikelsprDto().getCZbez());
			lsposDto.setBMwstsatzuebersteuert((short)0);
			lsposDto.setBNettopreisuebersteuert((short)0);
			lsposDto.setEinheitCNr(artikelDto.getEinheitCNr());
			lsposDto.setNMaterialzuschlagKurs(null);
			lsposDto.setTMaterialzuschlagDatum(null);
			lsposDto.setLieferscheinIId(lsDto.getIId());
			lsposDto.setNMenge(positionEntry.getAmount());
			lsposDto.setLagerIId(lsDto.getLagerIId());		
			
			List<SeriennrChargennrMitMengeDto> snrDtos = new ArrayList<SeriennrChargennrMitMengeDto>() ;
			if(!addItemIdentities(snrDtos, positionEntry.getItemIdentity())) {
				return null ;
			}
			lsposDto.setSeriennrChargennrMitMenge(snrDtos) ;				
		} else {
//			if(lsposDtos.size() != 1) {
//				respondExpectationFailed(EJBExceptionLP.FEHLER_BEI_FIND);
//				return null ;
//			}

			// Immer die erste Position verwenden, egal wieviele Positionen des Artikels es gibt
			lsposDto = lsposDtos.iterator().next() ;
			lsposDto.setNMenge(lsposDto.getNMenge().add(positionEntry.getAmount())) ;
			if(lsposDto.getSeriennrChargennrMitMenge() == null) {
				lsposDto.setSeriennrChargennrMitMenge(new ArrayList<SeriennrChargennrMitMengeDto>());
			}
			List<SeriennrChargennrMitMengeDto> snrDtos = lsposDto.getSeriennrChargennrMitMenge() ; 
			if(!addItemIdentities(snrDtos, positionEntry.getItemIdentity())) {
				return null;
			}
		}

		deliveryItemPriceCalculator.setLieferscheinDto(lsDto);
		ItemPriceCalculationResult result = 
				deliveryItemPriceCalculator.calculate(artikelDto.getIId(), lsposDto.getNMenge()) ;
		
		if(result.getVerkaufspreisDtoZielwaehrung().isEmpty()) {
			lsposDto.setMwstsatzIId(deliveryItemPriceCalculator.getMwstSatzDto().getIId()) ;
		} else {
			lsposDto.setMwstsatzIId(result.getVerkaufspreisDtoZielwaehrung().mwstsatzIId);			
		}
		lsposDto.setNBruttoeinzelpreis(result.getVerkaufspreisDtoZielwaehrung().bruttopreis);
		lsposDto.setNMwstbetrag(result.getVerkaufspreisDtoZielwaehrung().mwstsumme);
		lsposDto.setNEinzelpreis(result.getVerkaufspreisDtoZielwaehrung().einzelpreis); 
		lsposDto.setNNettoeinzelpreis(result.getVerkaufspreisDtoZielwaehrung().nettopreis); 
		lsposDto.setNRabattbetrag(result.getVerkaufspreisDtoZielwaehrung().rabattsumme);
		lsposDto.setFRabattsatz(result.getVerkaufspreisDtoZielwaehrung().rabattsatz);
		lsposDto.setFZusatzrabattsatz(result.getVerkaufspreisDtoZielwaehrung().getDdZusatzrabattsatz());
		lsposDto.setNMaterialzuschlag(result.getVerkaufspreisDtoZielwaehrung().bdMaterialzuschlag); 

//		lsposDto.setNRabattbetrag(abposDto.getNRabattbetrag());

//		lsposDto.setTypCNr(abposDto.getTypCNr());
//		lsposDto.setVerleihIId(null);		
//		lsposDto.setAuftragpositionIId(null);

		return lsposDto ;
	}

	private LieferscheinpositionDto setupDeliveryPosition(
			LieferscheinDto lsDto, String text) throws RemoteException  {
		LieferscheinpositionDto lsposDto = new LieferscheinpositionDto() ;
		lsposDto.setLieferscheinpositionartCNr(LocaleFac.POSITIONSART_TEXTEINGABE);
		lsposDto.setBArtikelbezeichnunguebersteuert(Helper.getShortFalse());
		lsposDto.setBMwstsatzuebersteuert(Helper.getShortFalse());
		lsposDto.setBNettopreisuebersteuert(Helper.getShortFalse());
		lsposDto.setBKeinlieferrest(Helper.getShortFalse());
		lsposDto.setLieferscheinIId(lsDto.getIId());
		
		List<SeriennrChargennrMitMengeDto> snrDtos = new ArrayList<SeriennrChargennrMitMengeDto>();
		lsposDto.setSeriennrChargennrMitMenge(snrDtos);
		
		return setupDeliveryPosition(lsposDto, text);
	}
	
	private LieferscheinpositionDto setupDeliveryPosition(LieferscheinpositionDto posDto, String text) throws RemoteException {
		String posText = text == null ? "." 
				: StringHelper.trimCut(text, SystemFac.MAX_LAENGE_EDITORTEXT);
		
		posDto.setXTextinhalt(posText);
		return posDto;
	}
	
	private LieferscheinpositionDto updateDeliveryPosition(
			ArtikelDto artikelDto, LieferscheinDto lsDto, LieferscheinpositionDto lsposDto, CreateItemDeliveryPositionEntry positionEntry) throws RemoteException  {
		deliveryItemPriceCalculator.setLieferscheinDto(lsDto);
		ItemPriceCalculationResult result = 
				deliveryItemPriceCalculator.calculate(artikelDto.getIId(), positionEntry.getAmount()) ;
		
		lsposDto.setArtikelIId(artikelDto.getIId());
		if(mandantCall.hasFunctionKostentraeger()) {
//			lsposDto.setKostentraegerIId(artikelDto.getlsDto.getKostentraegerIId());			
		}
//		lsposDto.setCLvposition(abposDto.getCLvposition());
		lsposDto.setBArtikelbezeichnunguebersteuert((short)0);
		lsposDto.setCBez(artikelDto.getArtikelsprDto().getCBez());
		lsposDto.setCZusatzbez(artikelDto.getArtikelsprDto().getCZbez());
		lsposDto.setBMwstsatzuebersteuert((short)0);
		lsposDto.setBNettopreisuebersteuert((short)0);
		lsposDto.setBRabattsatzuebersteuert((short)0);
		lsposDto.setEinheitCNr(artikelDto.getEinheitCNr());
		lsposDto.setFRabattsatz(0.0);
		lsposDto.setFZusatzrabattsatz(0.0);
		lsposDto.setNMaterialzuschlagKurs(null);
		lsposDto.setTMaterialzuschlagDatum(null);
		lsposDto.setLieferscheinIId(lsDto.getIId());
		lsposDto.setNMenge(positionEntry.getAmount());

		if(result.getVerkaufspreisDtoZielwaehrung().isEmpty()) {
			lsposDto.setMwstsatzIId(deliveryItemPriceCalculator.getMwstSatzDto().getIId()) ;
		} else {
			lsposDto.setMwstsatzIId(result.getVerkaufspreisDtoZielwaehrung().mwstsatzIId);			
		}
		lsposDto.setNBruttoeinzelpreis(result.getVerkaufspreisDtoZielwaehrung().bruttopreis);
		lsposDto.setNMwstbetrag(result.getVerkaufspreisDtoZielwaehrung().mwstsumme);
		lsposDto.setNEinzelpreis(result.getVerkaufspreisDtoZielwaehrung().einzelpreis); 
		lsposDto.setNNettoeinzelpreis(result.getVerkaufspreisDtoZielwaehrung().nettopreis); 
		lsposDto.setNRabattbetrag(result.getVerkaufspreisDtoZielwaehrung().rabattsumme);
		lsposDto.setFRabattsatz(result.getVerkaufspreisDtoZielwaehrung().rabattsatz);
		lsposDto.setFZusatzrabattsatz(result.getVerkaufspreisDtoZielwaehrung().getDdZusatzrabattsatz());
		lsposDto.setNMaterialzuschlag(result.getVerkaufspreisDtoZielwaehrung().bdMaterialzuschlag); 

//		lsposDto.setTypCNr(abposDto.getTypCNr());
//		lsposDto.setVerleihIId(null);		
//		lsposDto.setAuftragpositionIId(null);
		lsposDto.setLagerIId(lsDto.getLagerIId());

		List<SeriennrChargennrMitMengeDto> snrDtos = new ArrayList<SeriennrChargennrMitMengeDto>() ;
		if(!addItemIdentities(snrDtos, positionEntry.getItemIdentity())) {
			return null ;
		}
		lsposDto.setSeriennrChargennrMitMenge(snrDtos) ;				
		return lsposDto ;
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
	
	@DELETE
	@Path("/position")
	public void removePosition (
			@QueryParam(Param.USERID) String userId,
			@QueryParam(Param.POSITIONID) Integer positionId) throws RemoteException {
		if(connectClient(userId) == null) return ;
		if(positionId == null) {
			respondBadRequestValueMissing(Param.POSITIONID);
			return ;
		}

		LieferscheinpositionDto lsposDto = 
				lieferscheinpositionCall.lieferscheinpositionFindByPrimaryKey(positionId) ;
		if(lsposDto == null) {
			respondNotFound(Param.POSITIONID, positionId.toString());
			return ;
		}
		LieferscheinDto lsDto = 
				lieferscheinCall.lieferscheinFindByPrimaryKey(lsposDto.getLieferscheinIId()) ;
		if(lsDto == null) {
			respondNotFound(Param.POSITIONID, positionId.toString());
			return ;
		}
		
		if(!LieferscheinFac.LSSTATUS_ANGELEGT.equals(lsDto.getStatusCNr())) {
			respondExpectationFailed(EJBExceptionLP.FEHLER_STATUS) ;
			return ;
		}		
		
		lieferscheinpositionCall.removeLieferscheinposition(positionId);
	}
	
	@DELETE
	@Path("/positionfromorder")
	public void removePositionByOrder(
			@QueryParam(Param.USERID) String userId,
			@QueryParam(Param.DELIVERYID) Integer deliveryId,
			@QueryParam(Param.ORDERPOSITIONID) Integer orderpositionId) throws RemoteException {
		if(connectClient(userId) == null) return ;
		if(deliveryId == null) {
			respondBadRequestValueMissing(Param.DELIVERYID);
			return ;
		}

		LieferscheinDto lsDto = lieferscheinCall.lieferscheinFindByPrimaryKey(deliveryId) ; 
		if(lsDto == null) {
			respondNotFound(Param.DELIVERYID, deliveryId.toString());
			return ;
		}
		if(!LieferscheinFac.LSSTATUS_ANGELEGT.equals(lsDto.getStatusCNr())) {
			respondExpectationFailed(EJBExceptionLP.FEHLER_STATUS) ;
			return ;
		}
		
		if(orderpositionId == null) {
			respondBadRequestValueMissing(Param.ORDERPOSITIONID);
			return ;
		}
		
		Collection<LieferscheinpositionDto> entries = filterLsPosition(deliveryId, orderpositionId) ;
		if(entries.isEmpty()) {
			respondNotFound(Param.ORDERPOSITIONID, orderpositionId.toString());
			return ;
		}
		
		if(entries.size() != 1) {
			respondExpectationFailed(EJBExceptionLP.FEHLER_BEI_FIND);
			return ;
		}

		lieferscheinpositionCall.removeLieferscheinposition(entries.iterator().next().getIId()) ;
	}
	
	private Collection<LieferscheinpositionDto> filterLsPosition(
			Integer deliveryId, Integer orderpositionId) throws RemoteException {
		LieferscheinpositionDto dtos[] = lieferscheinpositionCall
				.lieferscheinpositionFindByAuftragpositionIId(orderpositionId) ;
		Collection<LieferscheinpositionDto> entries = new ArrayList<LieferscheinpositionDto>();
		for (LieferscheinpositionDto lieferscheinpositionDto : dtos) {
			if(lieferscheinpositionDto.getLieferscheinIId().equals(deliveryId))  {
				entries.add(lieferscheinpositionDto) ;
			}			
		}
		
		return entries ;
	}
	
	private Collection<LieferscheinpositionDto> filterLsPositionItemId(
			Integer deliveryId, Integer itemId) throws RemoteException {
		LieferscheinpositionDto dtos[] = lieferscheinpositionCall
				.getLieferscheinPositionenByLieferschein(deliveryId);
		Collection<LieferscheinpositionDto> entries = new ArrayList<LieferscheinpositionDto>();
		for (LieferscheinpositionDto lieferscheinpositionDto : dtos) {
			if(itemId.equals(lieferscheinpositionDto.getArtikelIId()))  {
				entries.add(lieferscheinpositionDto) ;
			}			
		}
		
		return entries ;
	}
	
	@GET
	@Path("/deliverable")
	@Produces({FORMAT_JSON, FORMAT_XML})
	public DeliverableDeliveryEntry getDeliveryEntry (
			@HeaderParam(ParamInHeader.TOKEN) String headerToken,
			@QueryParam(Param.USERID) String userId,
			@QueryParam(Param.DELIVERYID) Integer deliveryId,
			@QueryParam(Param.DELIVERYCNR) String deliveryCnr) throws RemoteException {
		DeliverableDeliveryEntry deliveryEntry = new DeliverableDeliveryEntry() ;
		if(null == connectClient(headerToken, userId)) return deliveryEntry ;

		if(!mandantCall.hasModulLieferschein()) {
			respondNotFound() ;
			return deliveryEntry ;
		}

		LieferscheinDto lsDto = findLieferscheinByIdCnr(deliveryId, deliveryCnr) ;
		if(lsDto == null) {
			return deliveryEntry ;
		}
		
		if(lsDto.getLagerIId() != null) {
			if(!lagerCall.hatRolleBerechtigungAufLager(lsDto.getLagerIId())) {
				respondForbidden(); 
				return deliveryEntry ;
			}
		}

		// Ein freier Lieferschein darf keinen Auftragsbezug haben
		if(lsDto.getAuftragIId() != null) {
			respondExpectationFailed();
			return deliveryEntry ;
		}
		
		deliveryEntry.setId(lsDto.getIId());
		deliveryEntry.setCnr(lsDto.getCNr());
		
		DeliveryDocumentStatus deliveryStatus = DeliveryDocumentStatus.fromString(lsDto.getStatusCNr()); 
		deliveryEntry.setStatus(deliveryStatus) ;

		if(!(deliveryStatus.equals(DeliveryDocumentStatus.NEW))) {
			respondExpectationFailed(EJBExceptionLP.FEHLER_STATUS) ;
			return deliveryEntry ;
		}

		return deliveryEntry ;		
	}


	private LieferscheinDto findLieferscheinByIdCnr(Integer deliveryId, String deliveryCnr) throws RemoteException {
		if(deliveryId == null && StringHelper.isEmpty(deliveryCnr)) {
			respondBadRequestValueMissing(Param.DELIVERYID);
			return null ;
		}

		LieferscheinDto lsDto = null ;
		if(deliveryId != null) {
			lsDto = lieferscheinCall.lieferscheinFindByPrimaryKey(deliveryId) ;
		}
		
		if(lsDto == null  && !StringHelper.isEmpty(deliveryCnr)) {
			lsDto = lieferscheinCall.lieferscheinFindByCNr(deliveryCnr) ;
		}

		if(lsDto == null || !lsDto.getMandantCNr().equals(globalInfo.getTheClientDto().getMandant())) {
			respondNotFound() ;
			return null ;
		}
		
		return lsDto ;
	}

	private KundeDto findKundeByIdCnr(Integer customerId, Integer customerCnr) throws RemoteException {
		if(customerId == null && customerCnr == null) {
			respondBadRequestValueMissing(Param.CUSTOMERID);
			return null ;
		}

		KundeDto kundeDto = null;
		if(customerId != null) {
			kundeDto = kundeCall.kundeFindByPrimaryKeyOhneExc(customerId);
		}
		if(kundeDto == null && customerCnr != null) {
			kundeDto = kundeCall.kundeFindByKundenummerOhneExc(customerCnr);
		}
	
		if(kundeDto == null || !kundeDto.getMandantCNr().equals(globalInfo.getTheClientDto().getMandant())) {
			respondNotFound();
			return null;
		}
		
		return kundeDto;
	}
	
	private ArtikelDto findArtikelByIdCnr(Integer artikelId, String artikelCnr) throws RemoteException {
		if(artikelId == null && StringHelper.isEmpty(artikelCnr)) {
			respondBadRequestValueMissing(Param.ITEMID);
			return null ;
		}
		
		ArtikelDto artikelDto = null ;
		if(artikelId != null) {
			artikelDto = artikelCall.artikelFindByPrimaryKeySmallOhneExc(artikelId) ;
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
	
	@POST
	@Path("/position")
	@Consumes({FORMAT_JSON, FORMAT_XML})	
	@Produces({FORMAT_JSON, FORMAT_XML})
	public CreatedDeliveryPositionEntry createPosition(
			@QueryParam(Param.USERID) String userId,
			CreateItemDeliveryPositionEntry positionEntry) throws RemoteException {
		if(connectClient(userId) == null) return null ;
		if(positionEntry == null) {
			respondBadRequestValueMissing("positionEntry");
			return null ;
		}

		if(positionEntry.getDeliveryId() == null) {
			respondBadRequestValueMissing(Param.DELIVERYID) ;
			return null ;
		}

		LieferscheinDto lsDto = lieferscheinCall.lieferscheinFindByPrimaryKey(positionEntry.getDeliveryId()) ; 
		if(lsDto == null) {
			respondNotFound(Param.DELIVERYID, positionEntry.getDeliveryId().toString());
			return null ;
		}

		if(!LieferscheinFac.LSSTATUS_ANGELEGT.equals(lsDto.getStatusCNr())) {
			respondExpectationFailed(EJBExceptionLP.FEHLER_STATUS) ;
			return null ;
		}

		if(!lagerCall.hatRolleBerechtigungAufLager(lsDto.getLagerIId())) {
			respondForbidden(); 
			return null ;
		}					

		// Freier Lieferschein darf keinen Auftragsbezug haben
		if(lsDto.getAuftragIId() != null) {
			respondExpectationFailed();
			return null ;
		}
		
		ArtikelDto artikelDto = findArtikelByIdCnr(positionEntry.getItemId(), positionEntry.getItemCnr()) ;
		if(artikelDto == null) {
			return null ;
		}
		
		CreatedDeliveryPositionEntry createEntry = new CreatedDeliveryPositionEntry() ;
		createEntry.setDeliveryId(lsDto.getIId());
		createEntry.setDeliveryCnr(lsDto.getCNr());		
		
		LieferscheinpositionDto lsposDto = 
				setupDeliveryPosition(lsDto, artikelDto, positionEntry) ;
		if(lsposDto == null) return createEntry ;

		Integer createdId = null ;
		if(lsposDto.getIId() == null) {
			createdId = lieferscheinpositionCall.createLieferscheinposition(
					lsposDto, false, new ArrayList<SeriennrChargennrMitMengeDto>()) ;			
		} else {
			createdId = lieferscheinpositionCall.updateLieferscheinposition(
					lsposDto, new ArrayList<SeriennrChargennrMitMengeDto>()) ;
		}	
		createEntry.setDeliveryPositionId(createdId) ;
		
		lsposDto = lieferscheinpositionCall.lieferscheinpositionFindByPrimaryKey(createdId) ;
		if(lsposDto.getSeriennrChargennrMitMenge() == null) {
			lsposDto.setSeriennrChargennrMitMenge(new ArrayList<SeriennrChargennrMitMengeDto>());
		}

		createEntry.setDeliveredAmount(lsposDto.getNMenge());
		createEntry.setItemIdentity(transform(lsposDto.getSeriennrChargennrMitMenge()));
		return createEntry;
	}	

	@PUT
	@Path("/position/{positionid}")
	@Consumes({FORMAT_JSON, FORMAT_XML})	
	@Produces({FORMAT_JSON, FORMAT_XML})
	public CreatedDeliveryPositionEntry updatePosition(
			@QueryParam(Param.USERID) String userId,
			@PathParam(Param.POSITIONID) Integer positionId,			
			CreateItemDeliveryPositionEntry positionEntry) throws RemoteException {
		if(connectClient(userId) == null) return null ;
		if(positionEntry == null) {
			respondBadRequestValueMissing("positionEntry");
			return null ;
		}
		if(positionId == null) {
			respondBadRequestValueMissing(Param.POSITIONID);
			return null ;
		}
		
		if(positionEntry.getDeliveryId() == null) {
			respondBadRequestValueMissing(Param.DELIVERYID) ;
			return null ;
		}

		LieferscheinDto lsDto = lieferscheinCall.lieferscheinFindByPrimaryKey(positionEntry.getDeliveryId()) ; 
		if(lsDto == null) {
			respondNotFound(Param.DELIVERYID, positionEntry.getDeliveryId().toString());
			return null ;
		}

		if(!LieferscheinFac.LSSTATUS_ANGELEGT.equals(lsDto.getStatusCNr())) {
			respondExpectationFailed(EJBExceptionLP.FEHLER_STATUS) ;
			return null ;
		}

		if(!lagerCall.hatRolleBerechtigungAufLager(lsDto.getLagerIId())) {
			respondForbidden(); 
			return null ;
		}
		
		LieferscheinpositionDto lsposDto = lieferscheinpositionCall.lieferscheinpositionFindByPrimaryKey(positionId) ;
		if(lsposDto == null) {
			respondBadRequest(Param.POSITIONID, positionId.toString());
			return null ;
		}
		
		if(!lsposDto.getLieferscheinIId().equals(lsDto.getIId())) {
			respondBadRequest(Param.POSITIONID, positionId.toString());
			return null;
		}

		ArtikelDto artikelDto = findArtikelByIdCnr(positionEntry.getItemId(), positionEntry.getItemCnr()) ;
		if(artikelDto == null) {
			return null ;
		}
		
		CreatedDeliveryPositionEntry createEntry = new CreatedDeliveryPositionEntry() ;
		createEntry.setDeliveryId(lsDto.getIId());
		createEntry.setDeliveryCnr(lsDto.getCNr());		
		
		updateDeliveryPosition(artikelDto, lsDto, lsposDto, positionEntry) ;

		Integer createdId = null ;
		if(lsposDto.getIId() == null) {
			createdId = lieferscheinpositionCall.createLieferscheinposition(
					lsposDto, false, new ArrayList<SeriennrChargennrMitMengeDto>()) ;			
		} else {
			createdId = lieferscheinpositionCall.updateLieferscheinposition(
					lsposDto, new ArrayList<SeriennrChargennrMitMengeDto>()) ;
		}	
		createEntry.setDeliveryPositionId(createdId) ;
		
		lsposDto = lieferscheinpositionCall.lieferscheinpositionFindByPrimaryKey(createdId) ;
		if(lsposDto.getSeriennrChargennrMitMenge() == null) {
			lsposDto.setSeriennrChargennrMitMenge(new ArrayList<SeriennrChargennrMitMengeDto>());
		}

		createEntry.setDeliveredAmount(lsposDto.getNMenge());
		createEntry.setItemIdentity(transform(lsposDto.getSeriennrChargennrMitMenge()));
		return createEntry;
	}	
	
	private void respondItemNotFound(Integer id, String cnr) {
		if(id != null) {
			respondNotFound(Param.ITEMID, id.toString());
		} else {
			respondNotFound(Param.ITEMCNR, cnr) ;
		}
	}
	
	@GET
	@Path("/customerdeliverables/{" + Param.CUSTOMERCNR + "}")
	@Produces({FORMAT_JSON, FORMAT_XML})
	public DeliveryEntryList getCustomerDeliverableSlips(
			@QueryParam(Param.USERID) String userId,
			@PathParam(Param.CUSTOMERCNR) Integer customerCnr
	) throws RemoteException {
		DeliveryEntryList entryList = new DeliveryEntryList();
		if(connectClient(userId) == null) return entryList;
		
		if(!mandantCall.hasModulLieferschein()) {
			respondNotFound();
			return entryList;
		}
		
		KundeDto kundeDto = findKundeDto(customerCnr) ;
		if(kundeDto == null) {
			respondBadRequest("customercnr", customerCnr.toString());
			return entryList;
		}
		
		if(!validateKundeDto(kundeDto)) {
			return entryList; 
		}
		
		List<LieferscheinDto> lsDtos = filterAngelegteLieferscheine(kundeDto.getIId());
		entryList.setEntries(transformLieferschein(lsDtos));
		return entryList;
	}
	
	
	@GET
	@Path("/customerdeliverablesid/{" + Param.CUSTOMERID + "}")
	@Produces({FORMAT_JSON, FORMAT_XML})
	@Override
	public DeliveryEntryList getCustomerIdDeliverableSlips(
			@QueryParam(Param.USERID) String userId,
			@PathParam(Param.CUSTOMERID) Integer customerId
	) throws RemoteException {
		DeliveryEntryList entryList = new DeliveryEntryList();
		if(connectClient(userId) == null) return entryList;
		
		if(!mandantCall.hasModulLieferschein()) {
			respondNotFound();
			return entryList;
		}
		
		KundeDto kundeDto = kundeCall.kundeFindByPrimaryKeyOhneExc(customerId);
		if(kundeDto == null) {
			respondBadRequest(Param.CUSTOMERID, customerId.toString());
			return entryList;
		}

		if(!kundeDto.getMandantCNr().equals(globalInfo.getMandant())) {
			respondBadRequest(Param.CUSTOMERID, customerId.toString());
			return entryList;
		}

		if(!validateKundeDto(kundeDto)) {
			return entryList; 
		}
		
		List<LieferscheinDto> lsDtos = filterAngelegteLieferscheine(kundeDto.getIId());
		entryList.setEntries(transformLieferschein(lsDtos));
		return entryList;
	}
	
	private List<DeliveryEntry> transformLieferschein(Collection<LieferscheinDto> dtos) {
		List<DeliveryEntry> entries = new ArrayList<DeliveryEntry>() ;
		for (LieferscheinDto lsDto : dtos) {
			entries.add(deliveryEntryMapper.mapEntry(lsDto)) ;
		}
		return entries ;
	}
	
	
	private LieferscheinpositionDto[] filterPositionsByItemId(
			LieferscheinpositionDto[] posDtos, final Integer itemId) {
		return CollectionTools.select(posDtos, new ISelect<LieferscheinpositionDto>() {
			public boolean select(LieferscheinpositionDto element) {
				return itemId.equals(element.getArtikelIId());
			};
		}).toArray(new LieferscheinpositionDto[0]);
	}
	
	private LieferscheinpositionDto[] filterPositionsByOrderPositionId(
			LieferscheinpositionDto[] posDtos, final Integer orderPositionId) {
		return CollectionTools.select(posDtos, new ISelect<LieferscheinpositionDto>() {
			public boolean select(LieferscheinpositionDto element) {
				return orderPositionId.equals(element.getAuftragpositionIId());
			};
		}).toArray(new LieferscheinpositionDto[0]);
	}

	@Override
	@GET
	@Path("/{" + Param.DELIVERYID + "}/positions")
	@Produces({FORMAT_JSON, FORMAT_XML})
	public DeliveryData getCustomerDeliverableSlipPositionsByDeliveryId(
			@QueryParam(Param.USERID) String userId,
			@PathParam(Param.DELIVERYID) Integer deliveryId,
			@QueryParam(Param.ITEMID) Integer itemId,
			@QueryParam(Param.ITEMCNR) String itemCnr,
			@QueryParam(Param.ORDERPOSITIONID) Integer orderPositionId
	) throws RemoteException {
		DeliveryData dd = new DeliveryData();
		if(connectClient(userId) == null) return dd;
		
		if(!mandantCall.hasModulLieferschein()) {
			respondNotFound();
			return dd;
		}
		
		LieferscheinDto lsDto = findLieferscheinByIdCnr(deliveryId, null);
		if(lsDto == null) {
			return dd;			
		}

		if(!LieferscheinFac.LSSTATUS_ANGELEGT.equals(lsDto.getStatusCNr())) {
			respondExpectationFailed(EJBExceptionLP.FEHLER_STATUS);
			return dd;
		}

		if(!lagerCall.hatRolleBerechtigungAufLager(lsDto.getLagerIId())) {
			respondForbidden();
			return dd;
		}	
	
		LieferscheinpositionDto[] posDtos = lieferscheinpositionCall
				.getLieferscheinPositionenByLieferschein(lsDto.getIId());
		if(itemId != null || !StringHelper.isEmpty(itemCnr)) {
			ArtikelDto artikelDto = findArtikelByIdCnr(itemId, itemCnr);
			if(artikelDto == null) {
				return dd;
			}
	
			posDtos = filterPositionsByItemId(posDtos, artikelDto.getIId());
		}
		
		if(orderPositionId != null) {
			posDtos = filterPositionsByOrderPositionId(posDtos, orderPositionId);		
		}

		dd = createDeliveryData(lsDto, posDtos);
		return dd;
	}
	
	private DeliveryData createDeliveryData(LieferscheinDto lsDto, 
			LieferscheinpositionDto[] posDtos) throws RemoteException {
		DeliveryData dd = new DeliveryData();
		dd.setDeliveryId(lsDto.getIId());

		for (LieferscheinpositionDto posDto : posDtos) {			
			PositionDataEntry posData = createFrom(posDto);
			if(posData.getDataType().equals(PositionDataType.NOTINITIALIZED)) {
				continue;
			}
			
			dd.getPositionData().getEntries().add(posData);

			if(posData.getDataType().equals(PositionDataType.ITEM)) {
				ArtikelDto artikelDto = artikelCall
						.artikelFindByPrimaryKeySmallOhneExc(posDto.getArtikelIId());
				DeliveryPositionEntry entry = deliveryPositionEntryMapper
						.mapEntry(lsDto, posDto, artikelDto);
				dd.getItemPositions().getEntries().add(entry);
			} else if(posData.getDataType().equals(PositionDataType.TEXT)) {
				DeliveryTextPositionEntry entry = deliveryTextPositionEntryMapper.mapEntry(posDto);
				dd.getTextPositions().getEntries().add(entry);
			}
		}
		
		return dd;
	}
	
	private PositionDataEntry createFrom(LieferscheinpositionDto posDto) {
		PositionDataEntry posData = new PositionDataEntry(posDto.getIId());
		posData.setIsort(posDto.getISort());
		String artCnr = posDto.getLieferscheinpositionartCNr();
		if(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT.equals(artCnr)) {
			posData.setDataType(PositionDataType.ITEM);
		} else if(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_TEXTEINGABE.equals(artCnr)) {
			posData.setDataType(PositionDataType.TEXT);
		} else {
			posData.setDataType(PositionDataType.NOTINITIALIZED);
		}
		
		return posData;
	}
	
	
	@GET
	@Path("/customerdeliverables/{" + Param.CUSTOMERCNR + "}/{" + Param.DELIVERYID + "}")
	@Produces({FORMAT_JSON, FORMAT_XML})
	public DeliveryPositionEntryList getCustomerDeliverableSlipPositions(
			@QueryParam(Param.USERID) String userId,
			@PathParam(Param.CUSTOMERCNR) Integer customerCnr,
			@PathParam(Param.DELIVERYID) Integer deliveryId,
			@QueryParam(Param.ITEMID) Integer itemId,
			@QueryParam(Param.ITEMCNR) String itemCnr
	) throws RemoteException {
		DeliveryPositionEntryList entryList = new DeliveryPositionEntryList();
		if(connectClient(userId) == null) return entryList;
		
		if(!mandantCall.hasModulLieferschein()) {
			respondNotFound();
			return entryList;
		}
		
		KundeDto kundeDto = findKundeDto(customerCnr) ;
		if(kundeDto == null) {
			respondBadRequest("customercnr", customerCnr.toString());
			return entryList;
		}
		
		if(!validateKundeDto(kundeDto)) {
			return entryList; 
		}
		
		LieferscheinDto lsDto = findLieferscheinByIdCnr(deliveryId, null);
		if(lsDto == null) {
			return entryList;
		}
		
		if(!lsDto.getKundeIIdLieferadresse().equals(kundeDto.getIId())) {
			respondNotFound(Param.DELIVERYID, deliveryId.toString());
		}
		
		if(!LieferscheinFac.LSSTATUS_ANGELEGT.equals(lsDto.getStatusCNr())) {
			respondExpectationFailed(EJBExceptionLP.FEHLER_STATUS);
			return entryList;
		}
		
		LieferscheinpositionDto[] posDtos = lieferscheinpositionCall
				.getLieferscheinPositionenByLieferschein(lsDto.getIId());
		if(itemId != null || !StringHelper.isEmpty(itemCnr)) {
			ArtikelDto artikelDto = findArtikelByIdCnr(itemId, itemCnr);
			if(artikelDto == null) {
				return entryList;
			}
			
			posDtos = filterPositionsByItemId(posDtos, artikelDto.getIId());
		}
		entryList.setEntries(transformLieferschein(lsDto, posDtos));
		return entryList;
	}
	
	private List<DeliveryPositionEntry> transformLieferschein(
			LieferscheinDto lsDto, LieferscheinpositionDto[] posDtos) throws RemoteException {
		List<DeliveryPositionEntry> entries = new ArrayList<DeliveryPositionEntry>();
		if(!lagerCall.hatRolleBerechtigungAufLager(lsDto.getLagerIId())) {
			return entries;
		}
		
		for (LieferscheinpositionDto posDto : posDtos) {
			if(posDto.getArtikelIId() == null) continue;
			
			ArtikelDto artikelDto = artikelCall.artikelFindByPrimaryKeySmallOhneExc(posDto.getArtikelIId());
			DeliveryPositionEntry entry = deliveryPositionEntryMapper.mapEntry(lsDto, posDto, artikelDto);
			entries.add(entry);
		}
		
		return entries;
	}

	@POST
	@Path("/customerposition")
	@Consumes({FORMAT_JSON, FORMAT_XML})	
	@Produces({FORMAT_JSON, FORMAT_XML})
	public CreatedDeliveryPositionEntry createCustomerPosition(
			@QueryParam(Param.USERID) String userId,
			CreateItemCustomerDeliveryPositionEntry positionEntry) throws RemoteException {
		if(connectClient(userId) == null) return null ;
		if(positionEntry == null) {
			respondBadRequestValueMissing("positionEntry");
			return null ;
		}

		KundeDto kundeDto = findKundeByIdCnr(
				positionEntry.getCustomerId(), positionEntry.getCustomerNr());
		if(kundeDto == null) {
			return null;
		}
		
		if(!validateKundeDto(kundeDto)) {
			return null ; 
		}
		
		LieferscheinDto lsDto = findCreateLieferschein(kundeDto, positionEntry.getDeliveryId()) ;
		if(lsDto == null) {
			return null ;
		}
		if(!validateLieferschein(lsDto)) {
			return null ;
		}
		
		ArtikelDto artikelDto = findArtikelByIdCnr(positionEntry.getItemId(), positionEntry.getItemCnr()) ;
		if(artikelDto == null) {
			return null ;
		}
		
		CreatedDeliveryPositionEntry createEntry = new CreatedDeliveryPositionEntry() ;
		createEntry.setDeliveryId(lsDto.getIId());
		createEntry.setDeliveryCnr(lsDto.getCNr());		
		
		LieferscheinpositionDto lsposDto = 
				setupDeliveryPosition(lsDto, artikelDto, positionEntry) ;
		if(lsposDto == null) return createEntry ;

		Integer createdId = null ;
		if(lsposDto.getIId() == null) {
			createdId = lieferscheinpositionCall.createLieferscheinposition(
					lsposDto, false, new ArrayList<SeriennrChargennrMitMengeDto>()) ;			
		} else {
			createdId = lieferscheinpositionCall.updateLieferscheinposition(
					lsposDto, new ArrayList<SeriennrChargennrMitMengeDto>()) ;
		}	
		createEntry.setDeliveryPositionId(createdId) ;
		
		lsposDto = lieferscheinpositionCall.lieferscheinpositionFindByPrimaryKey(createdId) ;
		if(lsposDto.getSeriennrChargennrMitMenge() == null) {
			lsposDto.setSeriennrChargennrMitMenge(new ArrayList<SeriennrChargennrMitMengeDto>());
		}

		createEntry.setDeliveredAmount(lsposDto.getNMenge());
		createEntry.setItemIdentity(transform(lsposDto.getSeriennrChargennrMitMenge()));
		return createEntry;
	}	
	
	private ItemIdentityEntryList transform(List<SeriennrChargennrMitMengeDto> ejbEntities) {
		List<ItemIdentityEntry>  itemIdentities = new ArrayList<ItemIdentityEntry>();
		for(SeriennrChargennrMitMengeDto identity : ejbEntities) {
			itemIdentities.add(itemIdentityEntryMapper.mapEntry(identity));
		}
		return new ItemIdentityEntryList(itemIdentities);
	}
	
	private KundeDto findKundeDto(Integer customerNr) throws RemoteException {
		return kundeCall.kundeFindByKundenummerOhneExc(customerNr) ;
	}
	
	private boolean validateKundeDto(KundeDto kundeDto) {
		if(kundeDto.getTLiefersperream() != null) {
			respondBadRequest(EJBExceptionLP.FEHLER_KUNDE_GESPERRT);
			return false ;
		}

		return true ;
	}
	
	private boolean validateLieferschein(LieferscheinDto lsDto) {
		if(!LieferscheinFac.LSSTATUS_ANGELEGT.equals(lsDto.getStatusCNr())) {
			respondExpectationFailed(EJBExceptionLP.FEHLER_STATUS) ;
			return false ;
		}

		if(!lagerCall.hatRolleBerechtigungAufLager(lsDto.getLagerIId())) {
			respondForbidden(); 
			return false ;
		}					

		// Freier Lieferschein darf keinen Auftragsbezug haben
		if(lsDto.getAuftragIId() != null) {
			respondExpectationFailed();
			return false ;
		}
		
		return true ;
	}
	
	private LieferscheinDto findCreateLieferschein(KundeDto kundeDto, Integer deliveryId) throws RemoteException {
		if(deliveryId != null) {
			LieferscheinDto lsDto = findLieferscheinByIdCnr(deliveryId, null) ;
			if(lsDto != null) {
				if(!kundeDto.getIId().equals(lsDto.getKundeIIdLieferadresse())) {
					lsDto = null ;
				}
			}
			if(lsDto == null) {
				respondNotFound(Param.DELIVERYID, deliveryId.toString());
			}
			
			return lsDto ;
		}
		
		int count = filterAngelegteLieferscheine(kundeDto.getIId()).size() ;
		if(count > 10) {
			respondExpectationFailed(EJBExceptionLP.FEHLER_LIEFERSCHEIN_ZU_VIELE_ANGELEGTE);
			return null ;
		}
		
		LieferscheinDto lsDto = lieferscheinCall.setupDefaultLieferschein(kundeDto) ;
		Integer lsId = lieferscheinCall.createLieferschein(lsDto) ;		
		return lieferscheinCall.lieferscheinFindByPrimaryKey(lsId) ;
	}
	
	private List<LieferscheinDto> filterAngelegteLieferscheine(Integer kundeId) throws RemoteException {
		List<LieferscheinDto> acceptedLs = new ArrayList<LieferscheinDto>();
		LieferscheinDto[] lsDto = lieferscheinCall.lieferscheinFindByKundeIIdLieferadresseMandantCNr(kundeId) ;
		for (LieferscheinDto lieferscheinDto : lsDto) {
			if(LieferscheinFac.LSSTATUS_ANGELEGT.equals(lieferscheinDto.getStatusCNr()) 
				&& lieferscheinDto.getAuftragIId() == null
				&& lagerCall.hatRolleBerechtigungAufLager(lieferscheinDto.getLagerIId())) {
				acceptedLs.add(lieferscheinDto) ;
			}
		}
		
		return acceptedLs ;
	}
	

	@Override
	@POST
	@Path("/{" + Param.DELIVERYID + "}/document")
	public void createDocument(
			@PathParam(Param.DELIVERYID) Integer deliveryId,
			@QueryParam(Param.USERID) String userId,
			@QueryParam(Param.TYPE) String type,
			@QueryParam(Param.KEYWORDS) String keywords,
			@QueryParam(Param.GROUPING) String grouping,
			@QueryParam(Param.SECURITYLEVEL) Long securitylevel,
			MultipartBody body) throws RemoteException, NamingException {
		if(connectClient(userId, MAX_CONTENT_LENGTH_DELIVERY) == null) {
			return;
		}

		HvValidateBadRequest.notNull(deliveryId, Param.DELIVERYID);
		HvValidateBadRequest.notNull(body, "body"); 
		
		for (Attachment attachment : body.getAllAttachments()) {
			documentService.createDoc(deliveryDocService, deliveryId, 
					null, new DocumentMetadata(type, keywords, grouping, securitylevel), attachment);
		}
	}

	@Override
	@GET
	@Path("/printdispatchlabel")
	public Integer printDispatchLabel(
			@QueryParam(Param.USERID) String userId, 
			@QueryParam(Param.DELIVERYID) Integer deliveryId,
			@QueryParam("activateDelivery") Boolean activateDelivery) throws RemoteException {
		if(connectClient(userId) == null) return null ;
		
		HvValidateBadRequest.notNull(deliveryId, Param.DELIVERYID);

		LieferscheinDto lsDto = lieferscheinCall.lieferscheinFindByPrimaryKey(deliveryId) ;
		HvValidateNotFound.notNull(lsDto, Param.DELIVERYID, deliveryId);
		
		LieferscheinpositionDto[] lspositionen = lieferscheinpositionCall.getLieferscheinPositionenByLieferschein(deliveryId);
		HvValidateExpectationFailed.noPositions(lspositionen);
		
		if (Boolean.TRUE.equals(activateDelivery)) {
			lieferscheinCall.berechneAktiviereBelegControlled(deliveryId);
		}
		lieferscheinReportCall.printVersandetikett(deliveryId) ;
		return deliveryId ;
	}

	@Override
	@GET
	@Path("/printgoodsissuelabel")
	public Integer printGoodsIssueLabel(
			@QueryParam(Param.USERID) String userId, 
			@QueryParam(Param.DELIVERYID) Integer deliveryId,
			@QueryParam("activateDelivery") Boolean activateDelivery) throws RemoteException {
		if(connectClient(userId) == null) return null ;
		
		HvValidateBadRequest.notNull(deliveryId, Param.DELIVERYID);

		LieferscheinDto lsDto = lieferscheinCall.lieferscheinFindByPrimaryKey(deliveryId) ;
		HvValidateNotFound.notNull(lsDto, Param.DELIVERYID, deliveryId);
		
		LieferscheinpositionDto[] lspositionen = lieferscheinpositionCall.getLieferscheinPositionenByLieferschein(deliveryId);
		HvValidateExpectationFailed.noPositions(lspositionen);
		
		if (Boolean.TRUE.equals(activateDelivery)) {
			lieferscheinCall.berechneAktiviereBelegControlled(deliveryId);
		}
		lieferscheinReportCall.printLieferscheinWAEtikett(deliveryId) ;
		return deliveryId ;
	}
	
	@GET
	@Path("/list")
	@Produces({FORMAT_JSON, FORMAT_XML})
	@Override
	public DeliveryEntryList getListDeliveryEntry(
			@QueryParam(Param.USERID) String userId,
			@QueryParam(Param.LIMIT) Integer limit,
			@QueryParam(Param.STARTINDEX) Integer startIndex,
			@QueryParam(Filter.CNR) String filterCnr,
			@QueryParam(Filter.STATUS) String filterStatus,
			@QueryParam(Filter.ORDERCNR) String filterOrderCnr,
			@QueryParam(Filter.CUSTOMER) String filterCustomer) throws RemoteException, EJBExceptionLP, NamingException {
		DeliveryEntryList entryList = new DeliveryEntryList();
		if (connectClient(userId) == null) return entryList;
		
		if (!judgeCall.hasLieferscheinCRUD()) {
			respondUnauthorized();
			return entryList;
		}
		
		FilterKriteriumCollector collector = new FilterKriteriumCollector();
		collector.add(deliveryQuery.getFilterCnr(filterCnr));
		collector.add(buildFilterStatus(filterStatus));
		collector.add(deliveryQuery.getFilterOrder(filterOrderCnr));
		collector.add(deliveryQuery.getFilterCustomername(filterCustomer));
		
		QueryParametersFeatures params = deliveryQuery.getFeatureQueryParameters(collector);
		params.setLimit(limit);
		params.setKeyOfSelectedRow(startIndex);
		params.addFeature(LieferscheinHandlerFeature.LIEFERSCHEIN_DATA);
		
		QueryResult result = deliveryQuery.setQuery(params);
		entryList.setEntries(deliveryQuery.getResultList(result));
		
		return entryList;
	}

	private FilterKriterium buildFilterStatus(String filterStatus) {
		if (StringHelper.isEmpty(filterStatus)) return null;
		
		List<DeliveryDocumentStatus> restStatus = new ArrayList<DeliveryDocumentStatus>();
		String[] tokens = filterStatus.trim().split(",");
		for (String statusString : tokens) {
			restStatus.add(DeliveryDocumentStatus.lookup(statusString));
		}
		
		return deliveryQuery.getFilterStatus(restStatus);
	}

	@GET
	@Path("/{" + Param.DELIVERYID + "}/document/list")
	@Produces({FORMAT_JSON, FORMAT_XML})
	public DocumentInfoEntryList getDocuments(
			@QueryParam(Param.USERID) String userId,
			@PathParam(Param.DELIVERYID) Integer deliveryId) throws RepositoryException, IOException {
		if (connectClient(userId) == null) return null;
		
		if(!mandantCall.hasModulLieferschein()) {
			respondNotFound();
			return new DocumentInfoEntryList();
		}
		HvValidateBadRequest.notNull(deliveryId, Param.DELIVERYID);
		
		LieferscheinDto lieferscheinDto = lieferscheinCall.lieferscheinFindByPrimaryKeyOhneExc(deliveryId);
		HvValidateNotFound.notNull(lieferscheinDto, Param.DELIVERYID, deliveryId);
		
		return documentService.getDocs(deliveryDocService, deliveryId, null);
	}
	
	@GET
	@Path("/{" + Param.DELIVERYID + "}/document")
	public Response getDocument(
			@QueryParam(Param.USERID) String userId,
			@PathParam(Param.DELIVERYID) Integer deliveryId,
			@QueryParam(Param.DOCUMENTCNR) String documentCnr) throws RepositoryException, IOException {
		if (connectClient(userId) == null) return null;
		
		if(!mandantCall.hasModulLieferschein()) {
			respondNotFound();
			return null;
		}
		
		HvValidateBadRequest.notNull(deliveryId, Param.DELIVERYID);
		HvValidateBadRequest.notNull(documentCnr, Param.DOCUMENTCNR);
		
		LieferscheinDto lieferscheinDto = lieferscheinCall.lieferscheinFindByPrimaryKeyOhneExc(deliveryId);
		HvValidateNotFound.notNull(lieferscheinDto, Param.DELIVERYID, deliveryId);
		
		final RawDocument document = documentService.getDoc(deliveryDocService, deliveryId, "", documentCnr);
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
	
	@POST
	@Path("/forecastcustomerposition")
	@Consumes({FORMAT_JSON, FORMAT_XML})	
	@Produces({FORMAT_JSON, FORMAT_XML})
	@Override
	public CreatedDeliveryPositionEntry createForecastOrderCustomerPosition(
			@QueryParam(Param.USERID) String userId,
			CreateItemCustomerDeliveryPositionEntry positionEntry) throws RemoteException {
		if(connectClient(userId) == null) return null;
		HvValidateBadRequest.notNull(positionEntry, "positionEntry");
		HvValidateBadRequest.notNull(positionEntry.getCustomerNr(), "customerNr");

		KundeDto kundeDto = findKundeDto(positionEntry.getCustomerNr());
		if(kundeDto == null) {
			respondBadRequest("customernr", positionEntry.getCustomerNr().toString());
			return null;
		}
		
		if(!validateKundeDto(kundeDto)) {
			return null; 
		}
		
		LieferscheinDto lsDto = findCreateLieferschein(kundeDto, positionEntry.getDeliveryId()) ;
		if(lsDto == null) {
			return null;
		}

		if(!validateForecastLieferschein(lsDto)) {
			return null ;
		}
		
		ArtikelDto artikelDto = findArtikelByIdCnr(positionEntry.getItemId(), positionEntry.getItemCnr()) ;
		if(artikelDto == null) {
			return null ;
		}
		
		CreatedDeliveryPositionEntry createEntry = new CreatedDeliveryPositionEntry() ;
		createEntry.setDeliveryId(lsDto.getIId());
		createEntry.setDeliveryCnr(lsDto.getCNr());		
		
		LieferscheinpositionDto lsposDto = setupForecastDeliveryPosition(artikelDto, lsDto, positionEntry) ;
		Integer createdId = lieferscheinpositionCall.createLieferscheinpositionVerteilen(
					lsposDto, false, new ArrayList<SeriennrChargennrMitMengeDto>());			
		createEntry.setDeliveryPositionId(createdId) ;
		
		lsposDto = lieferscheinpositionCall.lieferscheinpositionFindByPrimaryKey(createdId) ;
		if(lsposDto.getSeriennrChargennrMitMenge() == null) {
			lsposDto.setSeriennrChargennrMitMenge(new ArrayList<SeriennrChargennrMitMengeDto>());
		}

//		createEntry.setDeliveredAmount(lsposDto.getNMenge());
		createEntry.setDeliveredAmount(positionEntry.getAmount());
		createEntry.setItemIdentity(transform(lsposDto.getSeriennrChargennrMitMenge()));
		return createEntry;
	}
	
	private LieferscheinpositionDto setupForecastDeliveryPosition(
			ArtikelDto artikelDto, LieferscheinDto lsDto, CreateItemDeliveryPositionEntry positionEntry) throws RemoteException  {
		LieferscheinpositionDto lsposDto = null;
		lsposDto = new LieferscheinpositionDto() ;
		lsposDto.setLieferscheinpositionartCNr(LocaleFac.POSITIONSART_IDENT);
		lsposDto.setArtikelIId(artikelDto.getIId());
		if(mandantCall.hasFunctionKostentraeger()) {
//				lsposDto.setKostentraegerIId(artikelDto.getlsDto.getKostentraegerIId());			
		}
		lsposDto.setBArtikelbezeichnunguebersteuert((short)0);
		lsposDto.setCBez(artikelDto.getArtikelsprDto() != null ? artikelDto.getArtikelsprDto().getCBez() : null);
		lsposDto.setCZusatzbez(artikelDto.getArtikelsprDto().getCZbez());
		lsposDto.setBMwstsatzuebersteuert((short)0);
		lsposDto.setBNettopreisuebersteuert((short)0);
		lsposDto.setEinheitCNr(artikelDto.getEinheitCNr());
		lsposDto.setNMaterialzuschlagKurs(null);
		lsposDto.setTMaterialzuschlagDatum(null);
		lsposDto.setLieferscheinIId(lsDto.getIId());
		lsposDto.setNMenge(positionEntry.getAmount());
		lsposDto.setLagerIId(lsDto.getLagerIId());		
		
		List<SeriennrChargennrMitMengeDto> snrDtos = new ArrayList<SeriennrChargennrMitMengeDto>() ;
		if(!addItemIdentities(snrDtos, positionEntry.getItemIdentity())) {
			return null ;
		}
		lsposDto.setSeriennrChargennrMitMenge(snrDtos) ;				

		deliveryItemPriceCalculator.setLieferscheinDto(lsDto);
		ItemPriceCalculationResult result = 
				deliveryItemPriceCalculator.calculate(artikelDto.getIId(), lsposDto.getNMenge()) ;
		
		if(result.getVerkaufspreisDtoZielwaehrung().isEmpty()) {
			lsposDto.setMwstsatzIId(deliveryItemPriceCalculator.getMwstSatzDto().getIId()) ;
		} else {
			lsposDto.setMwstsatzIId(result.getVerkaufspreisDtoZielwaehrung().mwstsatzIId);			
		}
		lsposDto.setNBruttoeinzelpreis(result.getVerkaufspreisDtoZielwaehrung().bruttopreis);
		lsposDto.setNMwstbetrag(result.getVerkaufspreisDtoZielwaehrung().mwstsumme);
		lsposDto.setNEinzelpreis(result.getVerkaufspreisDtoZielwaehrung().einzelpreis); 
		lsposDto.setNNettoeinzelpreis(result.getVerkaufspreisDtoZielwaehrung().nettopreis); 
		lsposDto.setNRabattbetrag(result.getVerkaufspreisDtoZielwaehrung().rabattsumme);
		lsposDto.setFRabattsatz(result.getVerkaufspreisDtoZielwaehrung().rabattsatz);
		lsposDto.setFZusatzrabattsatz(result.getVerkaufspreisDtoZielwaehrung().getDdZusatzrabattsatz());
		lsposDto.setNMaterialzuschlag(result.getVerkaufspreisDtoZielwaehrung().bdMaterialzuschlag); 

		return lsposDto ;
	}
	
	private boolean validateForecastLieferschein(LieferscheinDto lsDto) {
		if(!LieferscheinFac.LSSTATUS_ANGELEGT.equals(lsDto.getStatusCNr())) {
			respondExpectationFailed(EJBExceptionLP.FEHLER_STATUS) ;
			return false ;
		}

		if(!lagerCall.hatRolleBerechtigungAufLager(lsDto.getLagerIId())) {
			respondForbidden(); 
			return false ;
		}					

		return true ;
	}
	
	@Override
	@GET
	@Path("/{" + Param.DELIVERYID + "}/printsignature")
	@Produces({FORMAT_JSON, FORMAT_XML})
	public DeliverySlipSignatureEntry printDeliverySlipSignature(
			@QueryParam(Param.USERID) String userId, 
			@PathParam(Param.DELIVERYID) Integer deliveryId,
			@QueryParam("sort") String sortcnr) throws RemoteException {
		if(connectClient(userId) == null) return null;
		HvValidateBadRequest.notNull(deliveryId, "deliveryId");
		
		DeliveryPositionSort sortMode = DeliveryPositionSort.NOTINITIALIZED;
		if(StringHelper.hasContent(sortcnr)) {
			try {
				sortMode = DeliveryPositionSort.fromId(sortcnr);				
			} catch(IllegalArgumentException e) {
				respondBadRequest("sort", sortcnr);
				return null;
			}
		}
		
		LieferscheinDto lsDto = lieferscheinCall.lieferscheinFindByPrimaryKey(deliveryId);
		HvValidateNotFound.notNull(lsDto, Param.DELIVERYID, deliveryId);

		DeliverySlipSignatureEntry entry = new DeliverySlipSignatureEntry();
		entry.setId(deliveryId);

		if(!LieferscheinFac.LSSTATUS_ANGELEGT.equals(lsDto.getStatusCNr())) {
			respondExpectationFailed(EJBExceptionLP.FEHLER_STATUS) ;
			return entry;
		}
		
		if(sortMode == DeliveryPositionSort.NOTINITIALIZED) {
			sortMode = parameterCall.getLieferscheinpositionSortierung();
		}
		
		if(DeliveryPositionSort.ITEMNUMBER.equals(sortMode)) {
			lieferscheinpositionCall.sortiereNachArtikelnummer(deliveryId);
		}
		if(DeliveryPositionSort.ORDERPOSITION.equals(sortMode)) {
			lieferscheinpositionCall.sortiereNachAuftragsnummer(deliveryId);
		}

		try {
			lieferscheinCall.berechneAktiviereBelegControlled(deliveryId);
			JasperPrintLP[] prints = lieferscheinReportCall.printLieferschein(deliveryId);
			byte[] pdfContent = JasperPrintHelper.asPdf(prints[0]);
			entry.setContent(pdfContent);
			Integer serialnumber = (Integer)prints[0].getMapParameters().get("P_LFDNR");
			entry.setSerialNumber(serialnumber);
	
			entry.setLastPagePng(JasperPrintHelper.asJpeg(
					printLieferscheinSignatureTemplate(prints[0], deliveryId, serialnumber)));
			entry.setMimeType("pdf");
			entry.setName(lsDto.getCNr());
		} catch(JRException e) {
			log.error("Ignored JRException", e);
		} catch(IOException e) {
			log.error("Ignored IOException", e);
		}
		
		return entry;
	}
	
	private JasperPrintLP printLieferscheinSignatureTemplate(
			JasperPrintLP original, Integer deliveryId, Integer serialNumber) throws RemoteException {
		HvOptional<Integer> varianteId = druckerCall
				.varianteLieferscheinUnterschrift();
		if(!varianteId.isPresent()) return original;
		
		JasperPrintLP[] prints = lieferscheinReportCall
				.printLieferschein(deliveryId, varianteId.get(), serialNumber);
		return prints[0];
	}

	@Override
	@POST
	@Path("/{" + Param.DELIVERYID + "}/printsignature")
	@Consumes({FORMAT_JSON, FORMAT_XML})
	public void postDeliverySlipSignature(
			@QueryParam(Param.USERID) String userId, 
			@PathParam(Param.DELIVERYID) Integer deliveryId,
			DeliverySlipSignaturePostEntry signatureEntry) throws RemoteException {
		HvValidateBadRequest.notNull(signatureEntry, "signatureEntry");
		HvValidateBadRequest.notEmpty(signatureEntry.getSignatureContent(), "signatureContent");
		HvValidateBadRequest.notNull(signatureEntry.getSignatureMs(), "signatureMs");
		
		if(connectClient(userId, MAX_CONTENT_LENGTH_SIGNATURE) == null) {
			return;
		}

		BestaetigterLieferscheinDto dto = new BestaetigterLieferscheinDto();
		dto.setBemerkung(signatureEntry.getRemark());
		dto.setLieferscheinId(new LieferscheinId(deliveryId));
		dto.setPdf(Base64.decodeBase64(signatureEntry.getPdfContent()));
		dto.setSerialNumber(signatureEntry.getSerialNumber());
		dto.setUnterschreiber(signatureEntry.getSignerName());
		dto.setUnterschrift(Base64.decodeBase64(
				StringHelper.cutPrefix(
						signatureEntry.getSignatureContent(), "data:image/png;base64,")));
		dto.setZeitpunkt(new Timestamp(signatureEntry.getSignatureMs()));
		lieferscheinCall.archiveSignedResponse(dto);
	}
}
