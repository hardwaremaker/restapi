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
package com.heliumv.api.production;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.jcr.RepositoryException;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
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

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.heliumv.api.BaseApi;
import com.heliumv.api.HvEJBExceptionLPExceptionMapper;
import com.heliumv.api.HvValidateBadRequest;
import com.heliumv.api.HvValidateNotFound;
import com.heliumv.api.document.DocumentCategory;
import com.heliumv.api.document.DocumentInfoEntryList;
import com.heliumv.api.document.DocumentMetadata;
import com.heliumv.api.document.IDocumentCategoryService;
import com.heliumv.api.document.IDocumentService;
import com.heliumv.api.document.RawDocument;
import com.heliumv.api.item.IItemCommentService;
import com.heliumv.api.item.ItemCommentFilter;
import com.heliumv.api.item.ItemCommentMediaInfoEntryList;
import com.heliumv.api.item.ItemHintEntry;
import com.heliumv.api.item.ItemLockEntry;
import com.heliumv.api.machine.IMachineApi;
import com.heliumv.api.machine.MachineApi;
import com.heliumv.api.machine.MachineEntryList;
import com.heliumv.api.machine.MachineQueryFilter;
import com.heliumv.api.worktime.IWorktimeApi;
import com.heliumv.api.worktime.MachineRecordingEntry;
import com.heliumv.api.worktime.MachineRecordingType;
import com.heliumv.api.worktime.TimeRecordingEntry;
import com.heliumv.api.worktime.WorktimeApi;
import com.heliumv.factory.IArtikelCall;
import com.heliumv.factory.IArtikelkommentarCall;
import com.heliumv.factory.IFertigungCallJudge;
import com.heliumv.factory.IFertigungReportCall;
import com.heliumv.factory.IFertigungServiceCall;
import com.heliumv.factory.ILagerCall;
import com.heliumv.factory.IMandantCall;
import com.heliumv.factory.IParameterCall;
import com.heliumv.factory.IStuecklisteCall;
import com.heliumv.factory.IZeiterfassungCall;
import com.heliumv.factory.loader.IProductionLoaderAttribute;
import com.heliumv.factory.loader.ProductionLoaderAdditionalStatus;
import com.heliumv.factory.loader.ProductionLoaderAdditionalTargetMaterials;
import com.heliumv.factory.loader.ProductionLoaderAdditionalWorksteps;
import com.heliumv.factory.loader.ProductionLoaderCall;
import com.heliumv.factory.loader.ProductionLoaderCommentsMedia;
import com.heliumv.factory.loader.ProductionLoaderCustomer;
import com.heliumv.factory.loader.ProductionLoaderDeliveredAmount;
import com.heliumv.factory.loader.ProductionLoaderDocuments;
import com.heliumv.factory.loader.ProductionLoaderItem;
import com.heliumv.factory.loader.ProductionLoaderProductionWorksteps;
import com.heliumv.factory.loader.ProductionLoaderTestPlan;
import com.heliumv.factory.query.OffeneAgQuery;
import com.heliumv.factory.query.ProductionQuery;
import com.heliumv.tools.FilterKriteriumCollector;
import com.heliumv.tools.NumberHelper;
import com.heliumv.tools.StringHelper;
import com.heliumv.types.MimeTypeEnum;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelkommentarDto;
import com.lp.server.artikel.service.ArtikelkommentarsprDto;
import com.lp.server.artikel.service.ArtikelsperrenSperrenDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.artikel.service.SperrenDto;
import com.lp.server.fertigung.service.BedarfsuebernahmeDto;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LosHandlerFeature;
import com.lp.server.fertigung.service.LosablieferungDto;
import com.lp.server.fertigung.service.LosablieferungTerminalDto;
import com.lp.server.fertigung.service.LosistmaterialDto;
import com.lp.server.fertigung.service.LossollarbeitsplanDto;
import com.lp.server.fertigung.service.LossollmaterialDto;
import com.lp.server.fertigung.service.LoszusatzstatusDto;
import com.lp.server.fertigung.service.OffeneAgsHandlerFeature;
import com.lp.server.fertigung.service.OffeneAgsQueryResult;
import com.lp.server.fertigung.service.PruefergebnisDto;
import com.lp.server.fertigung.service.ZusatzstatusDto;
import com.lp.server.personal.service.MaschineDto;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.stueckliste.service.FertigungsgruppeDto;
import com.lp.server.stueckliste.service.MontageartDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.system.service.KeyvalueDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParametersFeatures;
import com.lp.server.util.fastlanereader.service.query.QueryResult;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Service("hvProduction")
@Path("/api/v1/production/")
public class ProductionApi extends BaseApi implements IProductionApi {
	private static Logger log = LoggerFactory.getLogger(ProductionApi.class) ;

	public final static int MAX_CONTENT_LENGTH_DOCUMENT = 30*1024*1024 ;
	
	@Autowired
	private IArtikelCall artikelCall ;
	@Autowired
	private IFertigungCallJudge fertigungCall ;	
	@Autowired
	private ILagerCall lagerCall ;
	@Autowired
	private IStuecklisteCall stuecklisteCall ;
	@Autowired
	private IMandantCall mandantCall ;
	@Autowired
	private ProductionQuery productionQuery ;
	@Autowired
	private OffeneAgQuery offeneAgQuery ;
	@Autowired
	private IZeiterfassungCall zeiterfassungCall ;
	@Autowired
	private IFertigungReportCall fertigungReportCall;
	@Autowired
	private IFertigungServiceCall fertigungServiceCall;
		
	@Autowired
	private ProductionLoaderAdditionalWorksteps productionLoaderWorksteps;
	@Autowired
	private ProductionLoaderAdditionalTargetMaterials productionLoaderTargetMaterials;
	@Autowired
	private ProductionLoaderCustomer productionLoaderCustomer;
	@Autowired
	private ProductionLoaderItem productionLoaderItem;
	@Autowired
	private ProductionLoaderAdditionalStatus productionLoaderAdditionalStatus;
	@Autowired
	private ProductionLoaderDeliveredAmount productionLoaderDeliveredAmount;
	@Autowired
	private ProductionLoaderProductionWorksteps productionLoaderProductionWorksteps;
	@Autowired
	private ProductionLoaderCall productionLoaderCall;
	@Autowired
	private TestResultEntryMapper testResultEntryMapper;
	@Autowired
	private ProductionWorkstepEntryMapper productionWorkstepEntryMapper;
	@Autowired
	private TestResultValidator testResultValidator;
	@Autowired
	private IWorktimeApi worktimeApi;
	@Autowired
	private IMachineApi machineApi;
	@Autowired
	private IDocumentService documentService;
	@Autowired
	private IDocumentCategoryService productionDocService;
	@Autowired
	private IArtikelkommentarCall artikelkommentarCall;
	@Autowired
	private IParameterCall parameterCall;
	@Autowired
	private HvEJBExceptionLPExceptionMapper hvEJBExceptionLPException;
	@Autowired
	private IProductionService productionService;
	@Autowired
	private MaterialRequirementEntryMapper materialRequirementEntryMapper;
	@Autowired
	private IItemCommentService itemCommentService;
	@Autowired
	private ProductionLoaderDocuments productionLoaderDocuments;
	@Autowired
	private ProductionLoaderCommentsMedia productionLoaderCommentsMedia;
	@Autowired
	private ProductionLoaderTestPlan productionLoaderTestPlan;

//	private ILagerCall lagerCallProxy ;
	
	private ILagerCall getLagerCall() {
		return lagerCall ;
//		if(lagerCallProxy == null) {
//			lagerCallProxy = (ILagerCall) LagerCallProxy.newInstance(lagerCall) ;
//		}
//		return lagerCallProxy ;
	}
	
	@GET
	@Path("/{userid}")
	@Produces({FORMAT_JSON, FORMAT_XML})
	public List<ProductionEntry> getProductions(
			@PathParam(Param.USERID) String userId,
			@QueryParam(Param.LIMIT) Integer limit,
			@QueryParam(Param.STARTINDEX) Integer startIndex,
			@QueryParam(Param.FILTER_CNR) String filterCnr,
			@QueryParam("filter_customer") String filterCustomer, 
			@QueryParam("filter_project") String filterProject,
			@QueryParam("filter_itemcnr") String filterItemCnr,
			@QueryParam("filter_productiongroup") String filterProductionGroup) throws RemoteException, NamingException, Throwable {
		List<ProductionEntry> productions = new ArrayList<ProductionEntry>() ;

		if(connectClient(userId) == null)  return productions ;

		if(!mandantCall.hasModulLos()) {
			respondNotFound() ;
			return productions ;
		}
		
		FilterKriteriumCollector collector = new FilterKriteriumCollector() ;
		collector.add(buildFilterCnr(filterCnr)) ;
		collector.add(productionQuery.getFilterItemCnr(filterItemCnr));
		
		if (filterProductionGroup != null) {
			FertigungsgruppeDto fertGruDto = stuecklisteCall.fertigungsgruppeFindByMandantCNrCBezOhneExc(filterProductionGroup);
			if (fertGruDto == null) {
				respondNotFound("productionGroup", filterProductionGroup);
				return productions;
			}
			collector.add(productionQuery.getFilterProductionGroupId(fertGruDto.getIId()));
		}
 
		collector.add(productionQuery.getFilterBuchbareLose());

		QueryParametersFeatures params = productionQuery.getFeatureQueryParameters(collector);
		params.setLimit(limit) ;
		params.setKeyOfSelectedRow(startIndex) ;
		params.addFeature(LosHandlerFeature.LOS_DATA);
	
		QueryResult result = productionQuery.setQuery(params) ;
		productions = productionQuery.getResultList(result) ;
		
		return productions ;
	}

	@GET
	@Path("/list/{userid}")
	@Produces({FORMAT_JSON, FORMAT_XML})
	public ProductionEntryList getListProductions(
			@PathParam(Param.USERID) String userId,
			@QueryParam(Param.LIMIT) Integer limit,
			@QueryParam(Param.STARTINDEX) Integer startIndex,
			@QueryParam(Param.FILTER_CNR) String filterCnr,
			@QueryParam("filter_customer") String filterCustomer, 
			@QueryParam("filter_project") String filterProject,
			@QueryParam("filter_itemcnr") String filterItemCnr,
			@QueryParam("filter_productiongroup") String filterProductionGroup,
			@QueryParam("filter_status") String filterStatus) throws RemoteException, NamingException {
		ProductionEntryList entryList = new ProductionEntryList();

		if(connectClient(userId) == null)  return entryList;

		if(!mandantCall.hasModulLos()) {
			respondNotFound();
			return entryList;
		}
		
		FilterKriteriumCollector collector = new FilterKriteriumCollector() ;
		collector.add(buildFilterCnr(filterCnr)) ;
		collector.add(productionQuery.getFilterItemCnr(filterItemCnr));
		
		if (filterProductionGroup != null) {
			FertigungsgruppeDto fertGruDto = stuecklisteCall.fertigungsgruppeFindByMandantCNrCBezOhneExc(filterProductionGroup);
			if (fertGruDto == null) {
				respondNotFound("productionGroup", filterProductionGroup);
				return entryList;
			}
			collector.add(productionQuery.getFilterProductionGroupId(fertGruDto.getIId()));
		}
	
		collector.add(buildFilterStatus(filterStatus));

		QueryParametersFeatures params = productionQuery.getFeatureQueryParameters(collector);
		params.setLimit(limit);
		params.setKeyOfSelectedRow(startIndex);
		params.addFeature(LosHandlerFeature.LOS_DATA);
	
		QueryResult result = productionQuery.setQuery(params);
		entryList.setEntries(productionQuery.getResultList(result));
		
		return entryList ;
	}
	
	private FilterKriterium buildFilterStatus(String filterStatus) {
		if(StringHelper.isEmpty(filterStatus)) return null;
		
		List<ProductionStatus> restStatus = new ArrayList<ProductionStatus>();
		String[] tokens = filterStatus.trim().split(",");
		for (String statusString : tokens) {
			restStatus.add(ProductionStatus.lookup(statusString));
		}
		
		return productionQuery.getFilterStatus(restStatus);
	}
	
	
	@POST
	@Path("/materialwithdrawal/")
	@Consumes({"application/json", "application/xml"})
	public void bucheMaterialNachtraeglichVomLagerAb(
			@HeaderParam(ParamInHeader.TOKEN) String headerUserId,
			MaterialWithdrawalEntry materialEntry,
			@QueryParam("userId") String userId) throws NamingException, RemoteException {

		ArtikelDto itemDto = null ;
		LagerDto lagerDto = null ;
		
		try {
			HvValidateBadRequest.notNull(materialEntry, "materialwithdrawal"); 
			HvValidateBadRequest.notEmpty(materialEntry.getLotCnr(), "lotCnr");  
			HvValidateBadRequest.notEmpty(materialEntry.getItemCnr(), "itemCnr"); 
			HvValidateBadRequest.notNull(materialEntry.getAmount(), "amount");
			HvValidateBadRequest.notValid(materialEntry.getAmount().signum() != 0, "amount", "0");
			
			if(connectClient(headerUserId, userId) == null) return ;
			
			if(!mandantCall.hasModulLos()) {
				respondNotFound() ;
				return ;
			}
			
			if(!fertigungCall.darfGebeMaterialNachtraeglichAus()) {
				respondUnauthorized() ;
				return ;
			}
			
			LosDto losDto = findLosByCnr(materialEntry.getLotCnr()) ;
			HvValidateNotFound.notNull(losDto, "lotCnr", materialEntry.getLotCnr()); 
			
			if(!isValidLosState(losDto)) return ;
			
			itemDto = findItemByCnr(materialEntry.getItemCnr()) ;
			HvValidateNotFound.notNull(itemDto, "itemCnr", materialEntry.getItemCnr()); 
			
			if(materialEntry.getTargetMaterialId() == null) {
				MontageartDto montageartDto = getMontageart() ;
		
				if(montageartDto == null) {
					respondBadRequest("montageart", "no value defined") ;
					return ;
				}				
			}

			lagerDto = getLager(materialEntry.getStockCnr(), materialEntry.getStockId()) ;
			if(lagerDto == null) {
				return ;				
			}

			if(materialEntry.getAmount().signum() > 0) {
				MaterialRuecknahme ausgabe = new MaterialRuecknahme(losDto.getIId(), lagerDto.getIId(), itemDto) ;
				if(!ausgabe.verifyAmounts(materialEntry.getAmount(), materialEntry.getIdentities())) {
					return ;
				}
				
				gebeMaterialNachtraeglichAus(lagerDto.getIId(), 
						losDto.getIId(), itemDto, materialEntry) ;
			} else {
				BigDecimal amountToReturn = materialEntry.getAmount().abs() ;
				
				MaterialRuecknahme ruecknahme = new MaterialRuecknahme(losDto.getIId(), lagerDto.getIId(), itemDto) ;
				ruecknahme.setReturn(materialEntry.getReturn());
				if(!ruecknahme.verifyAmounts(amountToReturn, materialEntry.getIdentities())) {
					return ;
				}
				
				BigDecimal amountNotReturned = ruecknahme.returnItem(
						amountToReturn, materialEntry.getIdentities(), false) ;
				if(amountNotReturned.signum() == 0) {
					amountNotReturned = ruecknahme.returnItem(
						amountToReturn, materialEntry.getIdentities(), true) ;
				}
				
				if(amountNotReturned.signum() != 0) {
					respondBadRequest(EJBExceptionLP.FEHLER_ZUWENIG_AUF_LAGER) ;
					appendBadRequestData("stock-available", amountToReturn.subtract(amountNotReturned).toPlainString()) ;
				}
			}
		} catch(EJBExceptionLP e) {
			respondBadRequest(e) ;
			if(e.getCode() == EJBExceptionLP.FEHLER_ZUWENIG_AUF_LAGER) {
				try {
					BigDecimal lagerStand = getLagerCall().getLagerstandOhneExc(itemDto.getIId(), lagerDto.getIId()) ;
					appendBadRequestData("stock-available", lagerStand.toPlainString()) ;
				} catch(RemoteException r) {
					respondUnavailable(r) ;
				}
			}
		}
	}
	
	private void gebeMaterialNachtraeglichAus(Integer stockId, Integer losId,
			ArtikelDto itemDto, MaterialWithdrawalEntry materialEntry) 
		throws NamingException, RemoteException, EJBExceptionLP {

		LossollmaterialDto lossollmaterialDto = 
				materialEntry.getTargetMaterialId() != null  
					? findLossollmaterialDto(materialEntry.getTargetMaterialId())
					: createLossollmaterialDto(losId, materialEntry, itemDto, stockId, getMontageart()) ;		
		LosistmaterialDto losistmaterialDto = createLosistmaterialDto(stockId, materialEntry.getAmount()) ;

		List<SeriennrChargennrMitMengeDto> hvIdentities = null ;
		if(itemDto.istArtikelSnrOderchargentragend()) {
			hvIdentities = transform(materialEntry.getIdentities()) ;
		}
		
		if(materialEntry.getTargetMaterialId() == null) {
			lossollmaterialDto.setNMenge(BigDecimal.ZERO);			
		}
		
		fertigungCall.gebeMaterialNachtraeglichAus(lossollmaterialDto,
				losistmaterialDto, hvIdentities, materialEntry.getTargetMaterialId() != null) ;
	}
	
	private LossollmaterialDto findLossollmaterialDto(Integer sollmaterialId) throws RemoteException, NamingException {
		return fertigungCall.lossollmaterialFindByPrimaryKey(sollmaterialId);
	}
	
	private LossollmaterialDto createLossollmaterialDto(Integer losId, 
			MaterialWithdrawalEntry materialEntry, ArtikelDto itemDto, 
			Integer stockId, MontageartDto montageartDto) throws RemoteException, NamingException {
		BigDecimal sollPreis = getSollPreis(itemDto.getIId(), stockId) ;
		LossollmaterialDto lossollmaterialDto = new LossollmaterialDto() ;
		lossollmaterialDto.setBNachtraeglich(Helper.boolean2Short(true)) ;
		lossollmaterialDto.setArtikelIId(itemDto.getIId()) ;
		lossollmaterialDto.setEinheitCNr(itemDto.getEinheitCNr()) ;
		lossollmaterialDto.setLosIId(losId) ;
		lossollmaterialDto.setMontageartIId(montageartDto.getIId());
		lossollmaterialDto.setNMenge(materialEntry.getAmount());
		lossollmaterialDto.setNSollpreis(sollPreis);
		return lossollmaterialDto ;
	}
	
	public class MaterialRuecknahme {
		private Integer stockId ;
		private Integer losId ;
		private ArtikelDto itemDto ;
		private boolean isReturn ;
		
		public MaterialRuecknahme(Integer losId, Integer stockId, ArtikelDto itemDto) {
			this.losId = losId ;
			this.stockId = stockId ;
			this.itemDto = itemDto ;
			isReturn = false ;
		}

		/**
		 * Mengen &uuml;berpr&uuml;fen</b>
		 * <p>Die Gesamtsumme der identity.amount muss ident mit der angegebenen Menge sein<p>
		 * <p>Es d&uuml;rfen nur positive Mengen in den identities vorhanden sein.</p>
		 * @param amount
		 * @param identities
		 * @return
		 */
		public boolean verifyAmounts(BigDecimal amount, List<IdentityAmountEntry> identities) {
			if(!itemDto.istArtikelSnrOderchargentragend()) return true ;
			
			HvValidateBadRequest.notNull(identities, "identities");
			
			BigDecimal amountIdentities = BigDecimal.ZERO ;
			for (IdentityAmountEntry entry : identities) {
				if(entry.getAmount() == null) {
					respondBadRequestValueMissing("amount");
					appendBadRequestData(entry.getIdentity(), "amount missing");
					return false ;
				}
				
				if(entry.getAmount().signum() != 1) {
					respondBadRequest("amount", "positive");
					appendBadRequestData(entry.getIdentity(), entry.getAmount().toPlainString());
					return false ;
				}
				
				amountIdentities = amountIdentities.add(entry.getAmount()) ;
			}
			
			if(amountIdentities.compareTo(amount.abs()) != 0) {
				respondBadRequest("totalamount != identityamount", amount.toPlainString()) ;
				appendBadRequestData("identityamount", amountIdentities.toPlainString()) ;
				return false ;
			}
			
			return true ;
		}
		
		public boolean isReturn() {
			return isReturn ;
		}
		
		public void setReturn(boolean value) {
			isReturn = value ;
		}
		
		public BigDecimal returnItem(BigDecimal amount, List<IdentityAmountEntry> identities,
				boolean updateDb) throws NamingException, RemoteException {
			LossollmaterialDto[] sollDtos = fertigungCall.lossollmaterialFindByLosIIdOrderByISort(getLosId()) ;
			if(sollDtos.length < 1) return amount ;
			
			List<IdentityAmountEntry> workIdentities = cloneIdentities(identities);

			int startIndex = sollDtos.length ;
			while(startIndex != -1 && amount.signum() != 0) {
				startIndex = findAcceptableLossollmaterialDtoIndex(sollDtos, getItemDto().getIId(), amount, startIndex) ;
				if(-1 == startIndex) break ;
				
				LosistmaterialDto[] istDtos = fertigungCall.losistmaterialFindByLossollmaterialIId(sollDtos[startIndex].getIId()) ;
				for(int i = 0 ; i < istDtos.length && amount.signum() > 0; i++) {
					if(!istDtos[i].isAbgang()) continue ;

					BigDecimal amountToRemove = amount.min(istDtos[i].getNMenge().abs()) ;
					if(amountToRemove.signum() <= 0) continue ;

					if(getItemDto().istArtikelSnrOderchargentragend()) {
						amountToRemove = calculateAmountToRemove(
								amountToRemove, istDtos[i], sollDtos[startIndex], workIdentities, updateDb) ;
					}
					
					BigDecimal newAmount = istDtos[i].getNMenge().subtract(amountToRemove) ;
					if(updateDb) {
//						if(!getItemDto().istArtikelSnrOderchargentragend()) {
//							if(isReturn()) {
//								fertigungCall.updateLossollmaterialMenge(sollDtos[startIndex].getIId(), newAmount) ;
//							}
//							fertigungCall.updateLosistmaterialMenge(istDtos[i].getIId(), newAmount) ;
//						}

//						if(isReturn()) {
//							fertigungCall.updateLossollmaterialMenge(sollDtos[startIndex].getIId(), newAmount) ;
//						}
						if(!getItemDto().istArtikelSnrOderchargentragend()) {
							fertigungCall.updateLosistmaterialMenge(istDtos[i].getIId(), newAmount) ;
						}
					}

					amount = amount.subtract(amountToRemove) ;
				}
			}
			
			return amount ;
		}
	
		protected List<IdentityAmountEntry> cloneIdentities(List<IdentityAmountEntry> identities) {
			if(identities == null) return null ;
			
			List<IdentityAmountEntry> workIdenties = new ArrayList<IdentityAmountEntry>() ;
			for (IdentityAmountEntry identity : identities) {
				IdentityAmountEntry workEntry = new IdentityAmountEntry();
				workEntry.setAmount(BigDecimal.ZERO.add(identity.getAmount()));
				workEntry.setIdentity("" + identity.getIdentity()) ;
				workIdenties.add(workEntry) ;
			}
			return workIdenties ;
		}
	
		protected int findAcceptableLossollmaterialDtoIndex(LossollmaterialDto[] sollDtos,
				Integer itemId, BigDecimal amount, int startIndex) {
			if(startIndex > sollDtos.length || startIndex <= 0) return -1 ;

			for(int i = startIndex - 1; i >= 0; i--) {
				if(itemId.equals(sollDtos[i].getArtikelIId())) {
					return i ;
				}
			}

			return -1 ;
		}
	
		protected BigDecimal calculateAmountToRemove(BigDecimal amountToRemove, 
				LosistmaterialDto istMaterialDto, LossollmaterialDto sollMaterialDto, List<IdentityAmountEntry> workIdentities, boolean updateDb) throws NamingException, RemoteException {

			List<SeriennrChargennrMitMengeDto> allSnrs = lagerCall
					.getAllSeriennrchargennrEinerBelegartposition(
							LocaleFac.BELEGART_LOS, istMaterialDto.getIId());
			System.out.println("Snrs: " + allSnrs.size());

			BigDecimal amountSnr = BigDecimal.ZERO;
			for (SeriennrChargennrMitMengeDto snr : allSnrs) {
				for (IdentityAmountEntry workEntry : workIdentities) {
					if (snr.getCSeriennrChargennr().compareTo(workEntry.getIdentity()) == 0) {
						BigDecimal snrAvailableAmount = amountToRemove
								.min(workEntry.getAmount());
						if (snrAvailableAmount.signum() > 0) {

							System.out.println("Snr: " + workEntry.getIdentity()
									+ " amount: " + workEntry.getAmount());

							workEntry.setAmount(workEntry.getAmount().subtract(
									snrAvailableAmount));
							amountSnr = amountSnr.add(snrAvailableAmount);

							if (updateDb) {
//								bucheZu(sollMaterialDto.getIId(), amountSnr, workEntry.getIdentity()) ;
								bucheZu(sollMaterialDto.getIId(), snrAvailableAmount, workEntry.getIdentity()) ;
							}
						}
					}
				}
			}

			return amountSnr ;
		}
		
		protected void bucheZu(Integer sollmaterialId, BigDecimal amount, String identity) throws RemoteException, NamingException, EJBExceptionLP {
	
			LosistmaterialDto istmaterialDto = new LosistmaterialDto() ;
			istmaterialDto.setBAbgang(Helper.boolean2Short(false)) ;
			istmaterialDto.setLagerIId(getStockId());
			istmaterialDto.setLossollmaterialIId(sollmaterialId) ;
			istmaterialDto.setNMenge(amount);

			fertigungCall.createLosistmaterial(istmaterialDto, identity) ;
		}
		
		protected Integer getStockId() {
			return stockId ;
		}
		protected Integer getLosId() {
			return losId ;
		}
		protected ArtikelDto getItemDto() {
			return itemDto ;
		}
	}
		
	private List<SeriennrChargennrMitMengeDto> transform(List<IdentityAmountEntry> identities) {
		List<SeriennrChargennrMitMengeDto> hvIdentities = new ArrayList<SeriennrChargennrMitMengeDto>() ;
		if(identities == null) return null ;

		for (IdentityAmountEntry identity : identities) {
			SeriennrChargennrMitMengeDto snrDto = new SeriennrChargennrMitMengeDto() ;
			snrDto.setCSeriennrChargennr(identity.getIdentity()) ;
			snrDto.setNMenge(identity.getAmount()) ;
			hvIdentities.add(snrDto) ;
		}
		
		return hvIdentities.size() == 0 ? null : hvIdentities ;
	}
	
 	private MontageartDto getMontageart() throws NamingException, RemoteException, EJBExceptionLP {
		MontageartDto[] montagearts = stuecklisteCall.montageartFindByMandantCNr() ;
		return montagearts.length > 0 ? montagearts[0] : null ;
	}
	
 	private LagerDto getLager(String stockCnr, Integer stockId) throws NamingException, RemoteException {
 		LagerDto lagerDto = null ;
 		if(stockId != null) {
 			lagerDto = getLagerCall().lagerFindByPrimaryKeyOhneExc(stockId) ;
 			if(lagerDto == null) {
 				respondNotFound("stockId", stockId.toString()) ;
 				return null ;
 			}
 			
 			return lagerDto ;
  		}

		if(StringHelper.isEmpty(stockCnr)) {
			respondBadRequestValueMissing("stockCnr") ;
			return null ;
		}
		
		lagerDto = getLagerCall().lagerFindByCnrOhnExc(stockCnr) ; 		
		if(lagerDto == null) {
			respondNotFound("stockCnr", stockCnr);
		}

		return lagerDto ;
 	}
 	
	
	private BigDecimal getSollPreis(Integer itemId, Integer stockId) throws NamingException, RemoteException {
		return getLagerCall().getGemittelterGestehungspreisEinesLagers(itemId, stockId);		
	}
	
	
	private LosistmaterialDto createLosistmaterialDto(Integer stockId, BigDecimal amount) {
		LosistmaterialDto losistmaterialDto = new LosistmaterialDto();
		losistmaterialDto.setLagerIId(stockId);
		losistmaterialDto.setBAbgang(new Short((short) (amount.signum() > 0 ? 1 : 0))) ;
		losistmaterialDto.setNMenge(amount);
		return losistmaterialDto ;
	}
	
	private boolean isValidLosState(LosDto losDto) {
		if(FertigungFac.STATUS_STORNIERT.equals(losDto.getStatusCNr())) {
			respondBadRequest(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_STORNIERT) ;
			return false ;
		}

		if(FertigungFac.STATUS_ANGELEGT.equals(losDto.getStatusCNr())) {
			respondBadRequest(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_NOCH_NICHT_AUSGEGEBEN) ;
			return false ;
		}
		
		if(FertigungFac.STATUS_ERLEDIGT.equals(losDto.getStatusCNr())) {
			respondBadRequest(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_ERLEDIGT) ;
			return false ;
		}
		
		return true ;
	}
	
	private LosDto findLosByCnr(String cnr) throws NamingException {
		return fertigungCall.losFindByCNrMandantCNrOhneExc(cnr) ;
	}
	
	private ArtikelDto findItemByCnr(String cnr) throws RemoteException, NamingException {
		return artikelCall.artikelFindByCNrOhneExc(cnr) ;
	}
	
	private ArtikelDto findItemByIId(Integer iId) throws RemoteException {
		return artikelCall.artikelFindByPrimaryKeySmallOhneExc(iId);
	}
	
//	private LagerDto findLagerDtoById(Integer stockId) throws NamingException {
//		return getLagerCall().lagerFindByPrimaryKeyOhneExc(stockId) ;
//	}
//	
//	private void processMaterialBuchung(LosDto losDto, BigDecimal menge) throws RemoteException, NamingException {
//		if(parameterCall.isKeineAutomatischeMaterialbuchung()) return ;
//		
//		if(parameterCall.isBeiLosErledigenMaterialNachbuchen()) {
//			fertigungCall.bucheMaterialAufLos(losDto, menge) ;
//		} else {
//			if(losDto.getStuecklisteIId() == null) return ; 
//			
//			StuecklisteDto stklDto = stuecklisteCall.stuecklisteFindByPrimaryKey(losDto.getStuecklisteIId()) ;
//			if(!stklDto.isMaterialbuchungbeiablieferung()) return ;
//
//			fertigungCall.bucheMaterialAufLos(losDto, menge) ;
//		}
//	}
//	
//	
//	private void processAblieferung(LosDto losDto, BigDecimal menge) throws NamingException, RemoteException {
//		boolean isLosErledigen = judgeCall.hasFertDarfLosErledigen() ;
//		
//		LosablieferungDto losablieferungDto = new LosablieferungDto() ;
//		losablieferungDto.setLosIId(losDto.getIId()) ;
//		losablieferungDto.setNMenge(menge) ;
//		fertigungCall.createLosablieferung(losablieferungDto, isLosErledigen) ;
//	}
	
	private FilterKriterium buildFilterCnr(String cnr) {
		if(cnr == null || cnr.trim().length() == 0) return null ;
	
		FilterKriteriumDirekt fk = new FilterKriteriumDirekt(
				"flrlos." + FertigungFac.FLR_LOS_C_NR, StringHelper.removeSqlDelimiters(cnr), 
				FilterKriterium.OPERATOR_LIKE, "",
				FilterKriteriumDirekt.PROZENT_LEADING, // Auswertung als '%XX'
				true, // wrapWithSingleQuotes
				false, Facade.MAX_UNBESCHRAENKT);		
		fk.wrapWithProzent() ;
		fk.wrapWithSingleQuotes() ;
		return fk ;
	}
	
//	private FilterKriterium buildFilterItemNumber(String cnr) throws NamingException, RemoteException {
//		if(cnr == null || cnr.trim().length() == 0) return null ;
//
//		int itemLengthAllowed = parameterCall.getMaximaleLaengeArtikelnummer() ;
//		
//		FilterKriteriumDirekt fk = new FilterKriteriumDirekt(
//				"flrlos." + FertigungFac.FLR_LOS_FLRSTUECKLISTE + "."
//				+ StuecklisteFac.FLR_STUECKLISTE_FLRARTIKEL + ".c_nr", StringHelper.removeSqlDelimiters(cnr),
//				FilterKriterium.OPERATOR_LIKE, "",
//				FilterKriteriumDirekt.PROZENT_TRAILING, // Auswertung als 'XX%'
//				true, // wrapWithSingleQuotes
//				true, itemLengthAllowed);		
//		fk.wrapWithProzent() ;
//		fk.wrapWithSingleQuotes() ;
//		return fk ;
//	}
	
	@GET
	@Path("/openwork")
	@Produces({FORMAT_JSON, FORMAT_XML})
	public OpenWorkEntryList getOpenWorkEntries(
			@QueryParam(Param.USERID) String userId,
			@QueryParam(Param.LIMIT) Integer limit,
			@QueryParam(Param.STARTINDEX) Integer startIndex,
			@QueryParam(Filter.CNR) String filterCnr,
			@QueryParam("filter_customer") String filterCustomer, 
			@QueryParam("filter_itemcnr") String filterItemCnr,
			@QueryParam("filter_startdate") Long startDateMs,
			@QueryParam("filter_enddate") Long endDateMs,
			@QueryParam("filter_machinegroup") Integer filterMachinegroup,
			@QueryParam("filter_nextworkstep") Boolean filterNextWorkstep,
			@QueryParam("filter_inproductiononly") Boolean filterInProductionOnly) throws RemoteException, NamingException, EJBExceptionLP {
		if(connectClient(userId) == null) return new OpenWorkEntryList() ;

		return getOpenWorkEntriesImpl(limit, startIndex, startDateMs, endDateMs, null, 
				Boolean.FALSE, filterMachinegroup, filterNextWorkstep, filterInProductionOnly);
	}
	
	private FilterKriteriumCollector createFilterBase(FilterKriteriumCollector collector, 
			Long startDateMs, Long endDateMs, 
			Integer filterProductiongroupId, Integer filterMachinegroup) {
		collector.add(offeneAgQuery.getFilterBeginnDatum(startDateMs)) ; 
		collector.add(offeneAgQuery.getFilterEndeDatum(endDateMs)) ; 			

		collector.add(offeneAgQuery.getFilterProductiongroupId(filterProductiongroupId));
		collector.add(offeneAgQuery.getFilterMachinegroup(filterMachinegroup));
		return collector;
	}
	
	private FilterKriteriumCollector createFilterPlanningView(FilterKriteriumCollector collector) {
		collector.add(offeneAgQuery.getFilterExcludeStopped());
		collector.add(offeneAgQuery.getFilterExclude100Percent());
		collector.add(offeneAgQuery.getFilterExcludeInfoArtikel());
		return collector;
	}

	private FilterKriteriumCollector createFilterInProductionOnly(FilterKriteriumCollector collector) {
		collector.add(offeneAgQuery.getFilterInProductionOnly());
		return collector;
	}

	public OpenWorkEntryList getOpenWorkEntriesImpl(Integer limit, 
			Integer startIndex, Long startDateMs, Long endDateMs, 
			Integer filterProductiongroupId, Boolean filterPlanningView,
			Integer filterMachinegroup, Boolean filterNextWorkstep,
			Boolean filterInProductionOnly) throws NamingException, RemoteException {
		OpenWorkEntryList entries = new OpenWorkEntryList() ;
	
		if(!mandantCall.hasModulLos()) {
			respondNotFound() ;
			return entries ;
		}

		FilterKriteriumCollector collector = new FilterKriteriumCollector() ;
		collector = createFilterBase(collector, startDateMs, endDateMs, filterProductiongroupId, filterMachinegroup);
		if(Boolean.TRUE.equals(filterPlanningView)) {
			collector = createFilterPlanningView(collector);
		}
		if(Boolean.TRUE.equals(filterInProductionOnly)) {
			collector = createFilterInProductionOnly(collector);
		}
		
//			collector.add(buildFilterCnr(filterCnr)) ;

		QueryParametersFeatures params = offeneAgQuery.getFeatureQueryParameters(collector) ;
		params.setLimit(limit) ;
		params.setKeyOfSelectedRow(startIndex) ;
		params.addFeature(OffeneAgsHandlerFeature.AUFTRAGS_DATUM);
		params.addFeature(OffeneAgsHandlerFeature.PROJEKT_NR);
		params.addFeature(OffeneAgsHandlerFeature.ARTIKEL_KBEZ);
		if (Boolean.TRUE.equals(filterNextWorkstep)) {
			params.addFeature(OffeneAgsHandlerFeature.NUR_NAECHSTER_AG);
		}
		if(Boolean.TRUE.equals(filterPlanningView)) {
			params.addFeature(OffeneAgsHandlerFeature.ISTZEIT_ERMITTELN);
		}
		if (Boolean.TRUE.equals(filterInProductionOnly)) {
			params.addFeature(OffeneAgsHandlerFeature.ABGELIEFERTE_MENGE);
		}
		
		log.debug("Query offeneAg started");
		OffeneAgsQueryResult result = (OffeneAgsQueryResult) offeneAgQuery.setQuery(params) ;
		entries.setEntries(offeneAgQuery.getResultList(result)) ;
		log.debug("Query offeneAg done");
		
		return entries ;
	}	
	
	@PUT
	@Path("/openwork")
	@Consumes({FORMAT_JSON, FORMAT_XML})
	public void updateOpenWorkEntry(
			@HeaderParam(ParamInHeader.TOKEN) String headerUserId,
			OpenWorkUpdateEntry updateEntry,
			@QueryParam(Param.USERID) String userId
	) throws NamingException, RemoteException, EJBExceptionLP {
		HvValidateBadRequest.notNull(updateEntry, "updateEntry");

		if(connectClient(headerUserId, userId) == null) return ;
		
		updateOpenWorkEntryImpl(updateEntry);
	}

	@PUT
	@Path("/openworklist")
	@Consumes({FORMAT_JSON, FORMAT_XML})
	public void updateOpenWorkEntryList(
			@HeaderParam(ParamInHeader.TOKEN) String headerUserId,
			OpenWorkUpdateEntryList updateList,
			@QueryParam(Param.USERID) String userId
	) throws NamingException, RemoteException, EJBExceptionLP {
		if (connectClient(headerUserId, userId) == null) return;
		
		HvValidateBadRequest.notNull(updateList, "updateList");
		HvValidateBadRequest.notNull(updateList.getEntries(), "updateList.entries");
		
		Map<Integer, LosTerminEntry> terminMap = findLosTerminEntries(updateList.getEntries()); 
		updateLosTermine(updateList.getEntries(), terminMap);

		for (OpenWorkUpdateEntry updateEntry : updateList.getEntries()) {
			if(!updateOpenWorkEntryImpl(updateEntry)) {
				return ;
			}
		}
	}

	
	private boolean updateOpenWorkEntryImpl(OpenWorkUpdateEntry updateEntry)
			throws NamingException, RemoteException {
		if(updateEntry.getId() == null) {
			respondBadRequestValueMissing("updateEntry.id") ;
			return false ;			
		}

		if(StringHelper.isEmpty(updateEntry.getProductionCnr())) {
			respondBadRequestValueMissing("updateEntry.lotCnr");
			return false ;
		}

		LosDto losDto = fertigungCall
				.losFindByCNrMandantCNrOhneExc(updateEntry.getProductionCnr()) ;
		if(losDto == null) {
			respondNotFound("updateEntry.lotCnr", updateEntry.getProductionCnr());
			return false ;
		}
		LossollarbeitsplanDto arbeitsplanDto = fertigungCall
				.lossollarbeitsplanFindByPrimaryKey(updateEntry.getId()) ;
		// Minimale Absicherung gegen Versuche die Id "frei" zu ermitteln
		if(!arbeitsplanDto.getLosIId().equals(losDto.getIId())) {
			respondNotFound("updateEntry.id", updateEntry.getId().toString());
			return false ;
		}
		
		MaschineDto maschineDto = null ;
		if(updateEntry.getMachineId() != null) {
			maschineDto = zeiterfassungCall.maschineFindByPrimaryKey(updateEntry.getMachineId()) ;
		}
		
		Calendar beginCal = Calendar.getInstance() ;
		beginCal.setTime(losDto.getTProduktionsbeginn());

		Calendar newCal = Calendar.getInstance() ;
		newCal.setTimeInMillis(updateEntry.getWorkItemStartDate());
		newCal.set(Calendar.HOUR_OF_DAY, 0);
		newCal.set(Calendar.MINUTE, 0) ;
		newCal.set(Calendar.SECOND, 0) ;
		newCal.set(Calendar.MILLISECOND, 0) ;
		
		if(newCal.compareTo(beginCal) < 0) {
			respondBadRequest("updateEntry.workItemStartDate", 
					newCal.toString() + " < production.startDate" );
			return false ;
		}


		int dayDiff = daysBetween(beginCal, newCal) ;
		arbeitsplanDto.setIMaschinenversatztage(dayDiff) ;
		if(maschineDto != null) {
			arbeitsplanDto.setMaschineIId(maschineDto.getIId()); 
		}
		if(updateEntry.getMachineOffsetMs() != null) {
			arbeitsplanDto.setIMaschinenversatzMs(updateEntry.getMachineOffsetMs()) ;
		} else {
			arbeitsplanDto.setIMaschinenversatzMs(0) ;
		}
		
		fertigungCall.updateLossollarbeitsplan(arbeitsplanDto, false) ;
		return true ;
	}
	

	@PUT
	@Path("/doneopenworklist")
	@Consumes({FORMAT_JSON, FORMAT_XML})
	public void updateDoneOpenWorkEntryList(
			@HeaderParam(ParamInHeader.TOKEN) String headerUserId,
			OpenWorkUpdateEntryList updateList,
			@QueryParam(Param.USERID) String userId
	) throws NamingException, RemoteException, EJBExceptionLP {
		if (connectClient(headerUserId, userId) == null) return;
		
		HvValidateBadRequest.notNull(updateList, "updateList");
		HvValidateBadRequest.notNull(updateList.getEntries(), "updateList.entries");
		
		for (OpenWorkUpdateEntry updateEntry : updateList.getEntries()) {
			if(!updateDoneOpenWorkEntryImpl(updateEntry)) {
				return ;
			}
		}
	}

	private boolean updateDoneOpenWorkEntryImpl(OpenWorkUpdateEntry updateEntry)
			throws NamingException, RemoteException {
		if(updateEntry.getId() == null) {
			respondBadRequestValueMissing("updateEntry.id") ;
			return false ;			
		}

		if(StringHelper.isEmpty(updateEntry.getProductionCnr())) {
			respondBadRequestValueMissing("updateEntry.lotCnr");
			return false ;
		}

		LosDto losDto = fertigungCall
				.losFindByCNrMandantCNrOhneExc(updateEntry.getProductionCnr()) ;
		if(losDto == null) {
			respondNotFound("updateEntry.lotCnr", updateEntry.getProductionCnr());
			return false ;
		}
		LossollarbeitsplanDto arbeitsplanDto = fertigungCall
				.lossollarbeitsplanFindByPrimaryKey(updateEntry.getId()) ;
		// Minimale Absicherung gegen Versuche die Id "frei" zu ermitteln
		if(!arbeitsplanDto.getLosIId().equals(losDto.getIId())) {
			respondNotFound("updateEntry.id", updateEntry.getId().toString());
			return false ;
		}
		
		arbeitsplanDto.setBFertig(Helper.getShortTrue());		
		fertigungCall.updateLossollarbeitsplan(arbeitsplanDto, false) ;
		return true ;
	}
		
	private int daysBetween(Calendar startDate, Calendar endDate) {  
		Calendar date = (Calendar) startDate.clone();
		int daysBetween = 0;
		while (date.before(endDate)) {
			date.add(Calendar.DAY_OF_MONTH, 1);
			daysBetween++;
		}
		return daysBetween;
	}

	private class LosTerminEntry {
		private Long starttimeMs;
		private Long finishtimeMs;
		
		public Long getStarttimeMs() {
			return starttimeMs;
		}
		public void setStarttimeMs(Long starttimeMs) {
			this.starttimeMs = starttimeMs;
		}
		public Long getFinishtimeMs() {
			return finishtimeMs;
		}
		public void setFinishtimeMs(Long finishtimeMs) {
			this.finishtimeMs = finishtimeMs;
		}
	}
	
	private void updateLosTermine(List<OpenWorkUpdateEntry> entries, Map<Integer, LosTerminEntry> terminMap) throws RemoteException {
		for (Map.Entry<Integer, LosTerminEntry> terminEntry : terminMap.entrySet()) {
			LosDto losDto = fertigungCall.losFindByPrimaryKeyOhneExc(terminEntry.getKey()) ;
			if(losDto == null) {
				continue;
			}
			
			Long starttimeMs = terminEntry.getValue().getStarttimeMs() ;
			if(starttimeMs == null || starttimeMs == 0l) {
				starttimeMs = losDto.getTProduktionsbeginn().getTime();
			}
			Long finishtimeMs = terminEntry.getValue().getFinishtimeMs();
			if(finishtimeMs == null || finishtimeMs == 0l) {
				finishtimeMs = losDto.getTProduktionsende().getTime();
			}
			
			fertigungCall.terminverschieben(losDto.getIId(), 
					new Timestamp(starttimeMs), new Timestamp(finishtimeMs));
		}
	}
	
	private Map<Integer, LosTerminEntry> findLosTerminEntries(List<OpenWorkUpdateEntry> entries) throws RemoteException {
		Map<Integer, LosTerminEntry> termineMap = new HashMap<Integer, LosTerminEntry>();
		for (OpenWorkUpdateEntry openWorkEntry : entries) {
			if(openWorkEntry.getStarttimeMs() == null && openWorkEntry.getFinishtimeMs() == null) {
				continue;
			}
			if(openWorkEntry.getId() == null) {
				return termineMap;			
			}

			if(StringHelper.isEmpty(openWorkEntry.getProductionCnr())) {
				return termineMap;			
			}

			LosDto losDto = fertigungCall
					.losFindByCNrMandantCNrOhneExc(openWorkEntry.getProductionCnr()) ;
			if(losDto == null) {
				return termineMap;			
			}
			
			LossollarbeitsplanDto arbeitsplanDto = fertigungCall
					.lossollarbeitsplanFindByPrimaryKey(openWorkEntry.getId()) ;
			if(!arbeitsplanDto.getLosIId().equals(losDto.getIId())) {
				return termineMap;			
			}

			LosTerminEntry terminEntry = termineMap.get(losDto.getIId());
			if(terminEntry == null) {
				terminEntry = new LosTerminEntry();
				terminEntry.setStarttimeMs(openWorkEntry.getStarttimeMs());
				terminEntry.setFinishtimeMs(openWorkEntry.getFinishtimeMs());
				termineMap.put(losDto.getIId(), terminEntry);
			} else {
				if(openWorkEntry.getStarttimeMs() != null) {
					terminEntry.setStarttimeMs(openWorkEntry.getStarttimeMs());
				}
				if(openWorkEntry.getFinishtimeMs() != null) {
					terminEntry.setFinishtimeMs(openWorkEntry.getFinishtimeMs());
				}
			}
		}
		return termineMap;			
	}
	
	@Override
	@GET
	@Produces({FORMAT_JSON, FORMAT_XML})
	public ProductionEntry findProductionByAttributes(
			@QueryParam(Param.USERID) String userId,
			@QueryParam(Param.PRODUCTIONID) Integer id, 
			@QueryParam(Param.PRODUCTIONCNR) String cnr, 
			@QueryParam("addPartlistItem") Boolean addPartlistItem, 
			@QueryParam("addCustomer") Boolean addCustomer, 
			@QueryParam("addAdditionalStatus") Boolean addAdditionalStatus,
			@QueryParam("addWorksteps") Boolean addWorksteps,
			@QueryParam("addTargetMaterials") Boolean addTargetMaterials,
			@QueryParam("addDeliveredAmount") Boolean addDeliveredAmount,
			@QueryParam("addProductionWorksteps") Boolean addProductionWorksteps,
			@QueryParam(Add.DOCUMENTS) Boolean addDocuments,
			@QueryParam(Add.COMMENTSMEDIA) Boolean addCommentsMedia,
			@QueryParam(Add.TESTPLAN) Boolean addTestPlan) {
		
		if (connectClient(userId) == null) return null;

		if (StringHelper.isEmpty(cnr) && id == null) {
			respondBadRequest(Param.PRODUCTIONCNR, cnr);
			return null;
		}
		
		Set<IProductionLoaderAttribute> attributes = getAttributes(
				addPartlistItem, addCustomer, addAdditionalStatus, addWorksteps, 
				addTargetMaterials, addDeliveredAmount, addProductionWorksteps,
				addDocuments, addCommentsMedia, addTestPlan);
		
		return findProductionByIdOrCnr(id, cnr, attributes);
	}
	
	protected ProductionEntry findProductionByIdOrCnr(Integer id, String cnr, Set<IProductionLoaderAttribute> attributes) {
		if (id != null) {
			ProductionEntry entry = findProductionEntryByIdImpl(id, attributes);
			if (entry == null) {
				respondNotFound();
			}
			return entry;
		}
		
		ProductionEntry entry = findProductionEntryByCnrImpl(cnr, attributes);
		if (entry == null) {
			respondNotFound();
		}
		return entry;
	}

	private ProductionEntry findProductionEntryByCnrImpl(String cnr, Set<IProductionLoaderAttribute> attributes) {
		ProductionEntry productionEntry = productionLoaderCall.losFindByCnrOhneExc(cnr, attributes);
		return productionEntry;
	}

	private ProductionEntry findProductionEntryByIdImpl(Integer id, Set<IProductionLoaderAttribute> attributes) {
		ProductionEntry productionEntry = productionLoaderCall.losFindByPrimaryKeyOhneExc(id, attributes);
		return productionEntry;
	}

	protected Set<IProductionLoaderAttribute> getAttributes(Boolean addPartlistItem, 
			Boolean addCustomer, Boolean addAdditionalStatus, Boolean addWorksteps, 
			Boolean addTargetMaterials, Boolean addDeliveredAmount, Boolean addProductionWorksteps, 
			Boolean addDocuments, Boolean addCommentsMedia, Boolean addTestPlan) {
		Set<IProductionLoaderAttribute> attributes = new HashSet<IProductionLoaderAttribute>();
		if (Boolean.TRUE.equals(addPartlistItem)) {
			attributes.add(productionLoaderItem);
		}
		if (Boolean.TRUE.equals(addCustomer)) {
			attributes.add(productionLoaderCustomer);
		}
		if (Boolean.TRUE.equals(addAdditionalStatus)) {
			attributes.add(productionLoaderAdditionalStatus);
		}
		if (Boolean.TRUE.equals(addWorksteps)) {
			attributes.add(productionLoaderWorksteps);
		}
		if (Boolean.TRUE.equals(addTargetMaterials)) {
			attributes.add(productionLoaderTargetMaterials);
		}
		if (Boolean.TRUE.equals(addDeliveredAmount)) {
			attributes.add(productionLoaderDeliveredAmount);
		}
		if (Boolean.TRUE.equals(addProductionWorksteps)) {
			attributes.add(productionLoaderProductionWorksteps);
		}
		if (Boolean.TRUE.equals(addDocuments)) {
			attributes.add(productionLoaderDocuments);
		}
		if (Boolean.TRUE.equals(addCommentsMedia)) {
			attributes.add(productionLoaderCommentsMedia);
		}
		if (Boolean.TRUE.equals(addTestPlan)) {
			attributes.add(productionLoaderTestPlan);
		}
		return attributes;
	}
	
	public ProductionWorkstepEntryList getProductionWorkstepsImpl(Integer productionId) throws EJBExceptionLP, RemoteException {
		ProductionWorkstepEntryList list = new ProductionWorkstepEntryList();
		LossollarbeitsplanDto[] dtos = fertigungCall.lossollarbeitsplanFindByLosIId(productionId);
		
		for (LossollarbeitsplanDto dto : dtos) {
			ProductionWorkstepEntry entry = productionWorkstepEntryMapper.mapEntry(dto);
			list.getEntries().add(entry);
		}
		return list;
	}
	
	@GET
	@Path("{" + Param.PRODUCTIONID + "}/targetmaterial")
	@Produces({FORMAT_JSON, FORMAT_XML})
	public ProductionTargetMaterialEntryList getTargetMaterials(
			@PathParam(Param.PRODUCTIONID) Integer productionid,
			@QueryParam(Param.USERID) String userId,
			@QueryParam(Add.STOCKPLACEINFOS) Boolean addStockPlaceInfos) throws RemoteException, EJBExceptionLP {
		
		if (connectClient(userId) == null) return new ProductionTargetMaterialEntryList();
		
		LosDto losDto = fertigungCall.losFindByPrimaryKeyOhneExc(productionid);
		HvValidateNotFound.notNull(losDto, Param.PRODUCTIONID, productionid);

		return productionService.getTargetMaterials(productionid, addStockPlaceInfos);
	}

	@GET
	@Path("{" + Param.PRODUCTIONID + "}/targetmaterial/{" + Param.ITEMID + "}")
	@Produces({FORMAT_JSON, FORMAT_XML})
	public ProductionTargetMaterialEntryList getTargetMaterialsForItemId(
			@PathParam(Param.PRODUCTIONID) Integer productionid,
			@PathParam(Param.ITEMID) Integer itemid,			
			@QueryParam(Param.USERID) String userId,
			@QueryParam(Add.STOCKPLACEINFOS) Boolean addStockPlaceInfos) throws RemoteException, EJBExceptionLP {		
		
		if (connectClient(userId) == null) return new ProductionTargetMaterialEntryList();
		
		LosDto losDto = fertigungCall.losFindByPrimaryKeyOhneExc(productionid);
		HvValidateNotFound.notNull(losDto, Param.PRODUCTIONID, productionid);
		
		ArtikelDto artikelDto = findItemByIId(itemid);
		HvValidateNotFound.notNull(artikelDto, Param.ITEMID, itemid);
		
		LossollmaterialDto[] dtos = fertigungCall.lossollmaterialFindyByLosIIdArtikelIId(productionid, itemid);
		
		return productionService.getTargetMaterials(dtos, addStockPlaceInfos);
	}
	
	@PUT
	@Path("{" + Param.PRODUCTIONID + "}/dates")
	@Produces({FORMAT_JSON, FORMAT_XML})
	public void putMoveProductionDate(
			@PathParam(Param.PRODUCTIONID) Integer productionid,
			@QueryParam(Param.USERID) String userId,
			@QueryParam("start") Long startTimeMs,
			@QueryParam("finish") Long finishTimeMs) throws RemoteException {
		
		if (connectClient(userId) == null) return; 
		HvValidateBadRequest.notValid(!(startTimeMs == null && finishTimeMs == null), "finish");
		
		LosDto losDto = fertigungCall.losFindByPrimaryKeyOhneExc(productionid);
		HvValidateNotFound.notNull(losDto, Param.PRODUCTIONID, productionid);
		
		if(startTimeMs == null) {
			startTimeMs = losDto.getTProduktionsbeginn().getTime();
		}
		if(finishTimeMs == null) {
			finishTimeMs = losDto.getTProduktionsende().getTime();
		}

		fertigungCall.terminverschieben(losDto.getIId(), 
				new Timestamp(startTimeMs), new Timestamp(finishTimeMs));
	}
	
	@GET
	@Path("{" + Param.PRODUCTIONID + "}/printpackinglabel")
	public Response printPackingLabel(
			@PathParam(Param.PRODUCTIONID) Integer productionId,
			@QueryParam(Param.USERID) String userId) throws RemoteException, NamingException {
		if (connectClient(userId) == null) return null;
		
		HvValidateBadRequest.notNull(productionId, Param.PRODUCTIONID);
		
		if(!mandantCall.hasModulLos()) {
			respondNotFound() ;
			return null;
		}
		
		LosDto losDto = fertigungCall.losFindByPrimaryKeyOhneExc(productionId);
		HvValidateNotFound.notNull(losDto, Param.PRODUCTIONID, productionId);
		
		if (!isValidLosState(losDto)) return null;
		
		Integer amount = calculateNumberOfLabels(losDto);
		try {
			JasperPrintLP print = fertigungReportCall.printLosverpackungsetiketten(productionId, amount);

			final ByteArrayOutputStream baos = new ByteArrayOutputStream(100000) ;
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(print.getPrint());
			oos.close();
			StreamingOutput so = new StreamingOutput() {				
				@Override
				public void write(OutputStream os) throws IOException, WebApplicationException {
					os.write(baos.toByteArray()) ;
				}
			} ;
			String filename = encodeFileName(losDto.getCNr()) + ".jrprint" ;
			fertigungCall.setzeVPEtikettGedruckt(productionId);
			
			return Response.ok()
				.header("Content-Disposition", "filename=" + filename)
				.header("Content-Type", "application/octet-stream")
				.entity(so).build() ;
		} catch(Throwable e) {
			log.error("Exception", e) ;
			return Response.status(404).build();
		}
	}
	
	private Integer calculateNumberOfLabels(LosDto losDto) throws RemoteException, NamingException {
		if (losDto.getStuecklisteIId() == null) {
			return new Integer(1);
		}
		
		StuecklisteDto stuecklisteDto = stuecklisteCall.stuecklisteFindByPrimaryKey(losDto.getStuecklisteIId());
		ArtikelDto artikelDto = artikelCall.artikelFindByPrimaryKeySmallOhneExc(stuecklisteDto.getArtikelIId());
		if (artikelDto == null 
				|| NumberHelper.isNullOrZero(artikelDto.getNVerpackungsmittelmenge())
				|| NumberHelper.isNullOrZero(losDto.getNLosgroesse())) {
			return new Integer(1);
		}
		
		BigDecimal bd = losDto.getNLosgroesse().divide(
				artikelDto.getNVerpackungsmittelmenge(), 0, RoundingMode.CEILING);
		Integer numberOfLabels = new Integer(bd.intValue() < 1 ? 1 : bd.intValue());
		return numberOfLabels;
	}

	@GET
	@Path("{" + Param.PRODUCTIONID + "}/printproductionsupplynote")
	public Response printProductionSupplyNote(
			@PathParam(Param.PRODUCTIONID) Integer productionId,
			@QueryParam(Param.USERID) String userId) throws RemoteException {
		if (connectClient(userId) == null) return null;
		
		HvValidateBadRequest.notNull(productionId, Param.PRODUCTIONID);
		
		if(!mandantCall.hasModulLos()) {
			respondNotFound() ;
			return null;
		}

		LosDto losDto = fertigungCall.losFindByPrimaryKeyOhneExc(productionId);
		HvValidateNotFound.notNull(losDto, Param.PRODUCTIONID, productionId);
		
		if (!isValidLosState(losDto)) return null;
		
		try {
			JasperPrintLP print = fertigungReportCall.printFertigungsbegleitschein(productionId, Boolean.FALSE);

			final ByteArrayOutputStream baos = new ByteArrayOutputStream(100000) ;
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(print.getPrint());
			oos.close();
			StreamingOutput so = new StreamingOutput() {				
				@Override
				public void write(OutputStream os) throws IOException, WebApplicationException {
					os.write(baos.toByteArray()) ;
				}
			} ;
			String filename = encodeFileName(losDto.getCNr()) + ".jrprint" ;
			return Response.ok()
				.header("Content-Disposition", "filename=" + filename)
				.header("Content-Type", "application/octet-stream")
				.entity(so).build() ;
		} catch(Throwable e) {
			log.error("Exception", e) ;
//			return getInternalServerError() ;
			return Response.status(404).build();
//			return Response.ok()
//					.header("Content-Type", "application/json")
//					.entity("{\"error\" : 42, \"code:\" : \"" + e.getMessage() + "\"}").build();
//			return Response.ok()
//				.header("Content-Type", "text/html")
//				.entity("<html>Ein Fehler ist aufgetreten</html>").build() ;
		}
	}
	
	private String encodeFileName(String filename) {
		return filename.replace("/", "").replace("<", "").replace(">", "") ;
	}
	
	@GET
	@Path("{" + Param.PRODUCTIONID + "}/testplan")
	@Produces({FORMAT_JSON, FORMAT_XML})
	public TestPlanEntryList getTestPlan(
			@QueryParam(Param.USERID) String userId,
			@PathParam(Param.PRODUCTIONID) Integer productionId) throws RemoteException, EJBExceptionLP {
		if (connectClient(userId) == null) return new TestPlanEntryList();

		if(!mandantCall.hasFunctionPruefplan()) {
			respondNotFound();
			return new TestPlanEntryList();
		}
		
		validateProductionId(productionId);
		
		return productionService.getTestPlanEntries(productionId);
	}

	@POST
	@Path("{" + Param.PRODUCTIONID + "}/delivery")
	@Consumes({FORMAT_JSON, FORMAT_XML})
	@Produces({FORMAT_JSON, FORMAT_XML})
	public Integer createDelivery(
			@PathParam(Param.PRODUCTIONID) Integer productionId,
			DeliveryPostEntry deliveryPostEntry) throws RemoteException, NamingException {
		if (connectClient(deliveryPostEntry.getUserId(), 10000) == null) return null;
		
		if(!fertigungCall.darfLosAbliefern()) {
			respondUnauthorized();
			return null;
		}

		HvValidateBadRequest.notNull(productionId, Param.PRODUCTIONID);
		LosDto losDto = fertigungCall.losFindByPrimaryKeyOhneExc(productionId);
		HvValidateNotFound.notNull(losDto, Param.PRODUCTIONID, productionId);
		
		validateDeliveryEntry(deliveryPostEntry, losDto);
		
		Integer deliveryId = createDeliveryImpl(losDto, deliveryPostEntry);
		
		if (deliveryId != null) {
			fertigungReportCall.printAblieferEtikett(deliveryId, null);
		}
		
		if (Boolean.TRUE.equals(deliveryPostEntry.getBookStopping())) {
			((WorktimeApi) worktimeApi).bookTimeEntryImpl(
					getTimeRecordingEntry(deliveryPostEntry.getTimeRecordingEntry()),
					ZeiterfassungFac.TAETIGKEIT_ENDE, true);
		}

		if (Boolean.TRUE.equals(deliveryPostEntry.getBookMachineStopping())) {
			MachineRecordingEntry entry = mapTimeRecordingEntryToMachine(deliveryPostEntry.getTimeRecordingEntry());
			entry.setProductionWorkplanId(deliveryPostEntry.getProductionWorkplanId());
			LossollarbeitsplanDto lDto = fertigungCall.lossollarbeitsplanFindByPrimaryKey(deliveryPostEntry.getProductionWorkplanId());
			HvValidateNotFound.notNull(lDto, "productionWorkplanId", deliveryPostEntry.getProductionWorkplanId());
			
			entry.setMachineId(lDto.getMaschineIId());
			entry.setMachineRecordingType(MachineRecordingType.STOP);
			
			((WorktimeApi) worktimeApi).bookMachineStopImpl(entry);
		}
		return deliveryId;
	}

	private MachineRecordingEntry mapTimeRecordingEntryToMachine(TimeRecordingEntry entry) {
		MachineRecordingEntry mEntry = new MachineRecordingEntry();
		mEntry.setYear(entry.getYear());
		mEntry.setMonth(entry.getMonth());
		mEntry.setDay(entry.getDay());
		mEntry.setHour(entry.getHour());
		mEntry.setMinute(entry.getMinute());
		mEntry.setSecond(entry.getSecond()); 
		mEntry.setWhere(entry.getWhere());
		
		return mEntry;
	}

	private TimeRecordingEntry getTimeRecordingEntry(TimeRecordingEntry entry) {
		if (entry != null) return entry;
		
		Calendar c = Calendar.getInstance();
		entry = new TimeRecordingEntry();
		entry.setDay(c.get(Calendar.DAY_OF_MONTH));
		entry.setMonth(c.get(Calendar.MONTH));
		entry.setYear(c.get(Calendar.YEAR));
		entry.setMinute(c.get(Calendar.MINUTE));
		entry.setHour(c.get(Calendar.HOUR_OF_DAY));
		entry.setSecond(c.get(Calendar.SECOND));
		
		return entry;		
	}

	/**
	 * @param productionId
	 */
	private void validateProductionId(Integer productionId) {
		HvValidateBadRequest.notNull(productionId, Param.PRODUCTIONID);
		LosDto losDto = fertigungCall.losFindByPrimaryKeyOhneExc(productionId);
		HvValidateNotFound.notNull(losDto, "productionId", productionId);
	}
	
	private void validateDeliveryEntry(DeliveryPostEntry entry, LosDto losDto) throws RemoteException, NamingException {
		if (losDto.getStuecklisteIId() != null) {
			StuecklisteDto stuecklisteDto = stuecklisteCall.stuecklisteFindByPrimaryKey(losDto.getStuecklisteIId());
			ArtikelDto artikelDto = artikelCall.artikelFindByPrimaryKeySmall(stuecklisteDto.getArtikelIId());
			
			if (artikelDto.istArtikelSnrOderchargentragend()) {
				HvValidateBadRequest.notNull(entry.getAmount(), "amount");
				HvValidateBadRequest.notNull(entry.getIdentities(), "identities");
				HvValidateBadRequest.notValid(entry.getScrapAmount() == null, "scrapAmount not null");
				HvValidateBadRequest.notValid(!entry.getIdentities().isEmpty(), "identities are empty", "");
				
				BigDecimal sumIdentityAmounts = BigDecimal.ZERO;
				for (IdentityAmountEntry identityEntry : entry.getIdentities()) {
					sumIdentityAmounts = sumIdentityAmounts.add(identityEntry.getAmount());
				}
				HvValidateBadRequest.notValid(entry.getAmount().compareTo(sumIdentityAmounts) == 0, "amount", entry.getAmount().toPlainString());
			} else {
				HvValidateBadRequest.notValid(entry.getAmount() != null || entry.getScrapAmount() != null, "amount && scrapAmount null");
				HvValidateBadRequest.notValid(entry.getIdentities() == null || entry.getIdentities().isEmpty(), "identities not empty");
			}
		} else {
			HvValidateBadRequest.notValid(entry.getAmount() != null || entry.getScrapAmount() != null, "amount && scrapAmount null");
			HvValidateBadRequest.notValid(entry.getIdentities() == null || entry.getIdentities().isEmpty(), "identities not empty");
		}
		
		if (entry.getTestResultEntries() != null && !entry.getTestResultEntries().getEntries().isEmpty()) {
			HvValidateNotFound.notValid(mandantCall.hasFunctionPruefplan(), "testResultEntries", "");
			validateTestResultEntries(entry.getTestResultEntries().getEntries());
		}
	}
	
	private void validateTestResultEntries(List<TestResultEntry> testResultEntries) {
		for (TestResultEntry entry : testResultEntries) {
			testResultValidator.validate(entry);
		}
	}

	private Integer createDeliveryImpl(LosDto losDto, DeliveryPostEntry deliveryPostEntry) throws EJBExceptionLP, RemoteException {
		// wg. Uebersetzung der Fehlermeldungen
//		if (!isValidLosStateForDelivery(losDto)) return null;
		
		if (!lagerCall.hatRolleBerechtigungAufLager(
				deliveryPostEntry.getStockId() != null ? deliveryPostEntry.getStockId() : losDto.getLagerIIdZiel())) {
			respondForbidden(); 
			return null;
		}
		LosablieferungTerminalDto losablieferungDto = new LosablieferungTerminalDto();
		losablieferungDto.setLagerIId(deliveryPostEntry.getStockId());
		losablieferungDto.setLosIId(losDto.getIId());
		losablieferungDto.setNMenge(deliveryPostEntry.getAmount());
		losablieferungDto.setMengeSchrott(deliveryPostEntry.getScrapAmount());
		losablieferungDto.setBAendereLosgroesseUmSchrottmenge(deliveryPostEntry.getIncreaseLotSizeByScrapAmount());
		losablieferungDto.setSeriennrChargennrMitMenge(transform(deliveryPostEntry.getIdentities()));
		
		LosablieferungDto losablieferungResultDto = fertigungCall.createLosablieferungFuerTerminal(losablieferungDto);
		
		if (losablieferungResultDto != null 
				&& deliveryPostEntry.getTestResultEntries() != null 
				&& !deliveryPostEntry.getTestResultEntries().getEntries().isEmpty()) {
			createTestResultsImpl(losablieferungResultDto.getIId(), deliveryPostEntry.getTestResultEntries().getEntries());
		}
		
		return losablieferungResultDto != null ? losablieferungResultDto.getIId() : null;
	}
	
	private boolean isValidLosStateForDelivery(LosDto losDto) {
		if(FertigungFac.STATUS_STORNIERT.equals(losDto.getStatusCNr())) {
			respondBadRequest(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_STORNIERT) ;
			return false ;
		}

		if(FertigungFac.STATUS_ANGELEGT.equals(losDto.getStatusCNr())) {
			respondBadRequest(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_NOCH_NICHT_AUSGEGEBEN) ;
			return false ;
		}
		
		if(FertigungFac.STATUS_ERLEDIGT.equals(losDto.getStatusCNr())) {
			respondBadRequest(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_ERLEDIGT) ;
			return false ;
		}
		
		if(FertigungFac.STATUS_AUSGEGEBEN.equals(losDto.getStatusCNr())) {
			respondBadRequest(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_AUSGEGEBEN);
			return false;
		}

		if(FertigungFac.STATUS_GESTOPPT.equals(losDto.getStatusCNr())) {
			respondBadRequest(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_GESTOPPT);
			return false;
		}
		return true ;
	}

	@POST
	@Path("/testresult")
	@Consumes({FORMAT_JSON, FORMAT_XML})
	@Produces({FORMAT_JSON, FORMAT_XML})
	public void createTestResults(
			TestResultPostEntry testResultPostEntry) {
		if (connectClient(testResultPostEntry.getUserId(), 10000) == null) return;
		
		if(!mandantCall.hasFunctionPruefplan()) {
			respondNotFound();
		}

		HvValidateBadRequest.notNull(testResultPostEntry.getProductionDeliveryId(), "productionDeliveryId");
		LosablieferungDto losablieferungDto = fertigungCall.losablieferungFindByPrimaryKeyOhneExc(
				testResultPostEntry.getProductionDeliveryId());
		HvValidateNotFound.notNull(losablieferungDto, "productionDeliveryId", testResultPostEntry.getProductionDeliveryId());

		HvValidateBadRequest.notNull(testResultPostEntry.getTestResultEntries(), "testResultEntries");
		createTestResultsImpl(testResultPostEntry.getProductionDeliveryId(), 
				testResultPostEntry.getTestResultEntries().getEntries());
	}
	
	private void createTestResultsImpl(Integer productionDeliveryId, List<TestResultEntry> testResultEntries) {
		validateTestResultEntries(testResultEntries);
		
		List<PruefergebnisDto> pruefergebnisse = mapTestResultEntries(
				testResultEntries, productionDeliveryId);
		
		fertigungServiceCall.updatePruefergebnisse(pruefergebnisse, productionDeliveryId);
	}

	private List<PruefergebnisDto> mapTestResultEntries(
			List<TestResultEntry> testResultPostEntries, Integer losablieferungIId) {
		List<PruefergebnisDto> pruefergebnisse = new ArrayList<PruefergebnisDto>();
		for (TestResultEntry entry : testResultPostEntries) {
			PruefergebnisDto dto = testResultEntryMapper.mapDto(entry, losablieferungIId);
			pruefergebnisse.add(dto);
		}
		return pruefergebnisse;
	}

	@GET
	@Path("{" + Param.PRODUCTIONID + "}/printlabel")
	public Response printLabel(
			@PathParam(Param.PRODUCTIONID) Integer productionId,
			@QueryParam(Param.USERID) String userId,
			@QueryParam("copies") Integer copies,
			@QueryParam("amount") BigDecimal amount,
			@QueryParam("reportType") String reportType) throws IOException {
		if (connectClient(userId) == null) return null;
		
		HvValidateBadRequest.notNull(productionId, Param.PRODUCTIONID);
		HvValidateBadRequest.notNull(copies, "copies");
		HvValidateBadRequest.notNull(amount, "amount");
		
		if(!mandantCall.hasModulLos()) {
			respondNotFound() ;
			return null;
		}

		LosDto losDto = fertigungCall.losFindByPrimaryKeyOhneExc(productionId);
		HvValidateNotFound.notNull(losDto, Param.PRODUCTIONID, productionId);
		
		JasperPrintLP print = fertigungReportCall.printLosetikett(productionId, amount, copies, reportType);
		
		final ByteArrayOutputStream baos = new ByteArrayOutputStream(100000) ;
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(print.getPrint());
		oos.close();
		StreamingOutput so = new StreamingOutput() {				
			@Override
			public void write(OutputStream os) throws IOException, WebApplicationException {
				os.write(baos.toByteArray()) ;
			}
		} ;
		String filename = encodeFileName(losDto.getCNr()) + ".jrprint" ;
		return Response.ok()
			.header("Content-Disposition", "filename=" + filename)
			.header("Content-Type", "application/octet-stream")
			.entity(so).build() ;
	}
	
	@GET
	@Path("{" + Param.PRODUCTIONID + "}/workstep/{" + Param.PRODUCTIONWORKSTEPID + "}/machine")
	public MachineEntryList getAvailableMachinesForProductionWorkstep(
			@PathParam(Param.PRODUCTIONID) Integer productionId,
			@PathParam(Param.PRODUCTIONWORKSTEPID) Integer productionWorkstepId,
			@QueryParam(Param.USERID) String userId) throws RemoteException, EJBExceptionLP, NamingException {
		if (connectClient(userId) == null) return null;
		
		if(!mandantCall.hasModulLos()) {
			respondNotFound() ;
			return null;
		}

		LosDto losDto = fertigungCall.losFindByPrimaryKeyOhneExc(productionId);
		HvValidateNotFound.notNull(losDto, Param.PRODUCTIONID, productionId);
		
		LossollarbeitsplanDto lossollarbeitsplanDto = fertigungCall.lossollarbeitsplanFindByPrimaryKey(productionWorkstepId);
		HvValidateNotFound.notNull(lossollarbeitsplanDto, Param.PRODUCTIONWORKSTEPID, productionWorkstepId);
		HvValidateBadRequest.notValid(lossollarbeitsplanDto.getLosIId().equals(losDto.getIId()), 
				Param.PRODUCTIONWORKSTEPID + ".productionid", Param.PRODUCTIONID);

		return getAvailableMachinesForProductionWorkstepImpl(productionWorkstepId);
	}

	private MachineEntryList getAvailableMachinesForProductionWorkstepImpl(
			Integer productionWorkstepId) throws NamingException,
			RemoteException {
		List<Integer> filterInMachineIds = stuecklisteCall.getMoeglicheMaschinen(productionWorkstepId);
		MachineQueryFilter machineQueryFilter = new MachineQueryFilter();
		machineQueryFilter.setFilterInMachineIds(filterInMachineIds);
		MachineEntryList machineEntryList = ((MachineApi)machineApi).getMachinesImpl(1000, 0, machineQueryFilter);
		return machineEntryList;
	}
	
	@PUT
	@Path("{" + Param.PRODUCTIONID + "}/workstep/{" + Param.PRODUCTIONWORKSTEPID + "}/machine")
	public void updateMachineOfProductionWorkstep(
			@PathParam(Param.PRODUCTIONID) Integer productionId,
			@PathParam(Param.PRODUCTIONWORKSTEPID) Integer productionWorkstepId,
			@QueryParam(Param.USERID) String userId,
			@QueryParam(Param.MACHINEID) Integer machineId) throws RemoteException, EJBExceptionLP, NamingException {
		if (connectClient(userId) == null) return;
		
		if(!mandantCall.hasModulLos()) {
			respondNotFound() ;
			return ;
		}

		LosDto losDto = fertigungCall.losFindByPrimaryKeyOhneExc(productionId);
		HvValidateNotFound.notNull(losDto, Param.PRODUCTIONID, productionId);
		
		LossollarbeitsplanDto lossollarbeitsplanDto = fertigungCall.lossollarbeitsplanFindByPrimaryKey(productionWorkstepId);
		HvValidateNotFound.notNull(lossollarbeitsplanDto, Param.PRODUCTIONWORKSTEPID, productionWorkstepId);
		HvValidateBadRequest.notValid(lossollarbeitsplanDto.getLosIId().equals(losDto.getIId()), 
				Param.PRODUCTIONWORKSTEPID + ".productionid", Param.PRODUCTIONID);
		
		HvValidateBadRequest.notNull(machineId, Param.MACHINEID);
		List<Integer> filterInMachineIds = stuecklisteCall.getMoeglicheMaschinen(productionWorkstepId);
		HvValidateBadRequest.notValid(filterInMachineIds.contains(machineId), Param.MACHINEID);
		
		if (lossollarbeitsplanDto.getMaschineIId() != null) {
			zeiterfassungCall.stopMaschine(lossollarbeitsplanDto.getMaschineIId(), 
					lossollarbeitsplanDto.getIId(), new Timestamp(System.currentTimeMillis()));
		}
		lossollarbeitsplanDto.setMaschineIId(machineId);
		lossollarbeitsplanDto = fertigungCall.updateLossollarbeitsplan(lossollarbeitsplanDto);
	}
	
	@POST
	@Path("{" + Param.PRODUCTIONID + "}/document")
	public void createDocument(
			@PathParam(Param.PRODUCTIONID) Integer productionId,
			@QueryParam(Param.USERID) String userId,
			@QueryParam(Param.TYPE) String type,
			@QueryParam(Param.KEYWORDS) String keywords,
			@QueryParam(Param.GROUPING) String grouping,
			@QueryParam(Param.SECURITYLEVEL) Long securitylevel,
			MultipartBody body) throws RemoteException, NamingException {
		if(connectClient(userId, MAX_CONTENT_LENGTH_DOCUMENT) == null) {
			return;
		}

		HvValidateBadRequest.notNull(productionId, Param.PRODUCTIONID);
		HvValidateBadRequest.notNull(body, "body"); 
		
		for (Attachment attachment : body.getAllAttachments()) {
			documentService.createDoc(productionDocService, productionId, 
					null, new DocumentMetadata(type, keywords, grouping, securitylevel), attachment);
		}
	}
	
	@POST
	@Path("/emitproduction")
	@Override
	@Produces({FORMAT_JSON, FORMAT_XML})
	public EmitProductionEntry emitProduction(
			@QueryParam(Param.USERID) String userId, 
			@QueryParam(Param.PRODUCTIONID) Integer productionId,
			@QueryParam(Param.PRODUCTIONCNR) String cnr,
			@QueryParam("overrideLockWarning") Boolean overrideLockWarning,
			@QueryParam("overrideMissingPartsWarning") Boolean overrideMissingPartsWarning)
			throws RemoteException, NamingException {
		EmitProductionEntry emitEntry = new EmitProductionEntry();
				
		if (connectClient(userId) == null) return emitEntry;
		
		if(!mandantCall.hasModulLos()) {
			respondNotFound() ;
			return emitEntry;
		}

		ProductionEntry entry = findProductionByIdOrCnr(productionId, 
				cnr, new HashSet<IProductionLoaderAttribute>());
		if(entry == null) {
			return emitEntry;
		}

		emitEntry.setId(entry.getId());
		LosDto losDto = fertigungCall.losFindByPrimaryKeyOhneExc(emitEntry.getId());
		addPartlistInformation(emitEntry, losDto.getStuecklisteIId());
	
		if(!Boolean.TRUE.equals(overrideLockWarning) && emitEntry.isLocked()) {
			return emitEntry;
		}
		
		boolean warneBeiFehlmengen = 
				parameterCall.isWarnungWennFehlmengeEntsteht();
		if(Boolean.TRUE.equals(overrideMissingPartsWarning)) {
			warneBeiFehlmengen = false;
		}

		try {
			EJBExceptionLP postEx = fertigungCall.gebeLosAus(
					emitEntry.getId(), true, warneBeiFehlmengen);
			emitEntry.setEmitted(true);
			if(postEx != null) {
				emitEntry.setPostMessage(hvEJBExceptionLPException.toString(postEx));
			}
		} catch(EJBExceptionLP e) {
			if(e.getCode() == EJBExceptionLP.FEHLER_FERTIGUNG_AUSGABE_ES_WUERDEN_FEHLMENGEN_ENTSTEHEN) {
				emitEntry.setMissingPartsWarning(true);
			} else {
				throw e;
			}
		}
		
		String s = fertigungCall.getArtikelhinweiseAllerLossollpositionen(emitEntry.getId());
		if(StringHelper.hasContent(s)) {
			emitEntry.setRequiredPositionHints(s);
		}
	
		return emitEntry;
	}
	
	private EmitProductionEntry addPartlistInformation(
			EmitProductionEntry emitEntry, Integer stuecklisteId) 
					throws NamingException, RemoteException {
		if(stuecklisteId == null) return emitEntry;
	
		StuecklisteDto stklDto = stuecklisteCall.stuecklisteFindByPrimaryKey(stuecklisteId);
		List<KeyvalueDto> hints = artikelkommentarCall
				.artikelhinweiseFindByArtikelIId(stklDto.getArtikelIId(), LocaleFac.BELEGART_LOS);
		emitEntry.setItemHints(new ItemHintEntryList(transformHints(stklDto.getArtikelIId(), hints)));
		
		List<ArtikelsperrenSperrenDto> assDtos = artikelCall.artikelsperrenSperrenFindByArtikelIId(stklDto.getArtikelIId());
		emitEntry.setItemLocks(new ItemLockEntryList(transformSperren(stklDto.getArtikelIId(), assDtos)));
		
		return emitEntry;
	}
	
	private List<ItemHintEntry> transformHints(Integer itemId, List<KeyvalueDto> hints) {
		List<ItemHintEntry> hintEntries = new ArrayList<ItemHintEntry>() ;
		if(hints == null) return hintEntries ;

		for (KeyvalueDto keyvalueDto : hints) {
			ItemHintEntry hint = new ItemHintEntry(itemId);
			hint.setMimeType(MimeTypeEnum.fromString(keyvalueDto.getCDatentyp()));
			hint.setContent(keyvalueDto.getCValue());
			hint.setCnr(keyvalueDto.getCKey());
			hintEntries.add(hint) ;
		}
		
		return hintEntries;
	}
	
	private List<ItemLockEntry> transformSperren(Integer itemId, List<ArtikelsperrenSperrenDto> locks) {
		List<ItemLockEntry> lockEntries = new ArrayList<ItemLockEntry>();
		if(locks == null) return lockEntries;
		
		for (ArtikelsperrenSperrenDto assDto : locks) {
			lockEntries.add(transformLock(assDto));
		}
		
		return lockEntries;
	}

	private ItemLockEntry transformLock(ArtikelsperrenSperrenDto assDto) {
		ItemLockEntry lockEntry = new ItemLockEntry();
		lockEntry.setCause(assDto.getCGrund());
		SperrenDto sperrenDto = assDto.getSperrenDto();
		lockEntry.setLockId(sperrenDto.getIId());
		lockEntry.setLocked(Helper.isTrue(sperrenDto.getBGesperrt()));
		lockEntry.setLockedByProduction(Helper.isTrue(sperrenDto.getBDurchfertigung()));
		lockEntry.setLockedPartlist(Helper.isTrue(sperrenDto.getBGesperrtstueckliste()));
		lockEntry.setLockedProduction(Helper.isTrue(sperrenDto.getBGesperrtlos()));
		return lockEntry;
	}
	
	@POST
	@Path("/changetoproduction")
	@Override
	public Integer changeToProduction(
			@QueryParam(Param.USERID) String userId, 
			@QueryParam(Param.PRODUCTIONID) Integer productionId,
			@QueryParam(Param.PRODUCTIONCNR) String cnr)
			throws RemoteException, NamingException {
				
		if (connectClient(userId) == null) return null;
		
		if(!mandantCall.hasModulLos()) {
			respondNotFound() ;
			return null;
		}

		ProductionEntry entry = findProductionByIdOrCnr(productionId, 
				cnr, new HashSet<IProductionLoaderAttribute>());
		if(entry == null) {
			return null;
		}
		
		fertigungCall.setzeLosInProduktion(entry.getId());
		return entry.getId();
	}
	
	@POST
	@Path("/doneproduction")
	@Override
	@Produces({FORMAT_JSON, FORMAT_XML})
	public DoneProductionEntry doneProduction(
			@QueryParam(Param.USERID) String userId, 
			@QueryParam(Param.PRODUCTIONID) Integer productionId,
			@QueryParam(Param.PRODUCTIONCNR) String cnr,
			@QueryParam("ignoreProcessedBy") Boolean ignoreProcessedBy,
			@QueryParam("overrideNotCompleted") Boolean overrideNotCompleted)
			throws RemoteException, NamingException {
		DoneProductionEntry doneEntry = new DoneProductionEntry();
		
		if (connectClient(userId) == null) return doneEntry;
		
		if(!mandantCall.hasModulLos()) {
			respondNotFound();
			return doneEntry;
		}

		ProductionEntry entry = findProductionByIdOrCnr(productionId, 
				cnr, new HashSet<IProductionLoaderAttribute>());
		if(entry == null) {
			return doneEntry;
		}
		
		doneEntry.setId(entry.getId());
	
		if(!Boolean.TRUE.equals(ignoreProcessedBy)) {
			String s = zeiterfassungCall.istBelegGeradeInBearbeitung(LocaleFac.BELEGART_LOS, entry.getId());
			if(StringHelper.hasContent(s)) {
				doneEntry.setNowProcessedBy(s);
				return doneEntry;
			}
		}

		if(!Boolean.TRUE.equals(overrideNotCompleted)) {
			BigDecimal amount = fertigungCall.getErledigteMenge(entry.getId());
			LosDto losDto = fertigungCall.losFindByPrimaryKeyOhneExc(entry.getId());
			if(losDto.getNLosgroesse().compareTo(amount) > 0) {
				doneEntry.setAmount(losDto.getNLosgroesse());
				doneEntry.setDeliveredAmount(amount);
				doneEntry.setMissingAmount(true);
				return doneEntry;
			}
		}
		
		EJBExceptionLP postEx = fertigungCall.manuellErledigen(entry.getId(), false);
		doneEntry.setDone(true);
		if(postEx != null) {
			doneEntry.setPostMessage(hvEJBExceptionLPException.toString(postEx));
		}
		
		return doneEntry;
	}

	@GET
	@Path("/printpapers")
	@Override
	@Produces({FORMAT_JSON, FORMAT_XML})
	public PrintProductionEntry printPapers(
			@QueryParam(Param.USERID) String userId, 
			@QueryParam(Param.PRODUCTIONID) Integer productionId,
			@QueryParam(Param.PRODUCTIONCNR) String cnr,
			@QueryParam("printProductionsupplynote") Boolean printProductionsupplynote,
			@QueryParam("printerProductionsupplynote") String printerProductionsupplynote,
			@QueryParam("printAnalysissheet") Boolean printAnalysissheet,
			@QueryParam("printerAnalysissheet") String printerAnalysissheet) {
		PrintProductionEntry printEntry = new PrintProductionEntry();
		
		if (connectClient(userId) == null) return printEntry;
		
		if(!mandantCall.hasModulLos()) {
			respondNotFound();
			return printEntry;
		}

		ProductionEntry entry = findProductionByIdOrCnr(productionId, 
				cnr, new HashSet<IProductionLoaderAttribute>());
		if(entry == null) {
			return printEntry;
		}
		
		printEntry.setId(entry.getId());
	
		if(Boolean.TRUE.equals(printProductionsupplynote)) {
			fertigungReportCall
				.printFertigungsbegleitschein(
						printEntry.getId(), printerProductionsupplynote);
			printEntry.setPrintProductionpaper(true);
		}
		
		if(Boolean.TRUE.equals(printAnalysissheet)) {
			printAusgabeListe(printEntry, printerAnalysissheet);
			printEntry.setPrintAnalysissheet(true);
		}
		
		return printEntry;
	}
	
	private void printAusgabeListe(PrintProductionEntry printEntry, String printerAnalysisSheet) {
		// Vorerst nach der Standardsortierung. Kann beispielsweise durch Umgebungsvariablen
		// ersetzt bzw. ergaenzt werden
		fertigungReportCall.printAusgabeListe(
			printEntry.getId(), Helper.SORTIERUNG_NACH_IDENT,
				true, false, null, null, printerAnalysisSheet);	
	}
	
	@PUT
	@Path("changeproductionordersize")
	@Override
	public Integer changeProductionOrderSize(
			@QueryParam(Param.USERID) String userId, 
			@QueryParam(Param.PRODUCTIONID) Integer productionId,
			@QueryParam(Param.PRODUCTIONCNR) String productionCnr,
			@QueryParam("productionordersize") BigDecimal productionOrderSize) throws RemoteException {
		if (connectClient(userId) == null) return null;
		
		if(!mandantCall.hasModulLos()) {
			respondNotFound() ;
			return null;
		}

		ProductionEntry entry = findProductionByIdOrCnr(productionId, 
				productionCnr, new HashSet<IProductionLoaderAttribute>());
		if(entry == null) {
			return null;
		}
		HvValidateBadRequest.notNull(productionOrderSize, "productionordersize");
		
		fertigungCall.aendereLosgroesse(entry.getId(), productionOrderSize);
		setzeLoszusatzstatusBeiLosgroessenaenderung(entry);
		
		return entry.getId();
	}

	private void setzeLoszusatzstatusBeiLosgroessenaenderung(ProductionEntry entry) throws RemoteException {
		String zusatzstatusParam = parameterCall.getGroessenaenderungZusatzstatus();
		if (!StringHelper.hasContent(zusatzstatusParam)) {
			return;
		}
		
		ZusatzstatusDto zusatzstatusDto = fertigungCall.zusatzstatusFindByMandantCNrCBezOhneExc(zusatzstatusParam);
		if (zusatzstatusDto == null) {
			return;
		}
		
		LoszusatzstatusDto loszusatzstatusDto = fertigungCall.loszusatzstatusFindByLosIIdZusatzstatusIIdOhneExc(
				entry.getId(), zusatzstatusDto.getIId());
		if (loszusatzstatusDto == null) {
			loszusatzstatusDto = new LoszusatzstatusDto();
			loszusatzstatusDto.setLosIId(entry.getId());
			loszusatzstatusDto.setZusatzstatusIId(zusatzstatusDto.getIId());
			fertigungCall.createLoszusatzstatus(loszusatzstatusDto);
		}
	}
	
	@POST
	@Path("/materialrequirements")
	@Consumes({FORMAT_JSON, FORMAT_XML})
	@Override
	public void createMaterialRequirements(
			MaterialRequirementPostEntry materialRequirementPostEntry) throws Throwable {
		HvValidateBadRequest.notNull(materialRequirementPostEntry, "materialRequirementPostEntry");
		
		if (connectClient(materialRequirementPostEntry.getUserId(), 60000000) == null) return;
		
		if(!mandantCall.hasModulLos()) {
			respondNotFound() ;
			return;
		}
		
		boolean enabled = mandantCall.hasFunctionHvma2() ||
				mandantCall.hasFunctionHvmaZeiterfassung();
		if (!enabled) {
			respondUnauthorized();
			return;
		}
		
		if (materialRequirementPostEntry.getMaterialRequirementEntries() != null) {
			createMaterialRequirementsImpl(materialRequirementPostEntry.getMaterialRequirementEntries().getEntries());
		}
		
		if (Boolean.TRUE.equals(materialRequirementPostEntry.getPrintSynchronisationPaper())) {
			fertigungReportCall.printBedarfsuebernahmeSynchronisierungOnServer();
		}
	}

	private void createMaterialRequirementsImpl(List<MaterialRequirementEntry> entries) throws Throwable {
		int count = 1;
		for (MaterialRequirementEntry entry : entries) {
			try {
				BedarfsuebernahmeDto dto = materialRequirementEntryMapper.mapDto(entry);
				fertigungCall.createBedarfsuebernahme(dto);
				count++;
			} catch (Throwable ex) {
				log.error("createBedarfsuebernahme() failed: entry " + count 
						+ " of " + entries.size() + ": " + entry.asString(), ex);
				throw ex;
			}
		}
	}
	
	@GET
	@Path("/{" + Param.PRODUCTIONID + "}/commentmedia/list")
	@Produces({FORMAT_JSON, FORMAT_XML})
	public ItemCommentMediaInfoEntryList getCommentsMedia(
			@QueryParam(Param.USERID) String userId,
			@PathParam(Param.PRODUCTIONID) Integer productionId) throws RemoteException, NamingException {
		if (connectClient(userId) == null) return new ItemCommentMediaInfoEntryList();
		
		HvValidateBadRequest.notNull(productionId, Param.PRODUCTIONID);

		LosDto losDto = fertigungCall.losFindByPrimaryKeyOhneExc(productionId);
		HvValidateNotFound.notNull(losDto, Param.PRODUCTIONID, productionId);
		
		if (losDto.getStuecklisteIId() == null) {
			return new ItemCommentMediaInfoEntryList(); 
		}
		StuecklisteDto stuecklisteDto = stuecklisteCall.stuecklisteFindByPrimaryKey(losDto.getStuecklisteIId());
		
		ItemCommentFilter commentFilter = itemCommentService.createDefaultCommentFilter();
		commentFilter.setDocCategories(EnumSet.of(DocumentCategory.PRODUCTION));
		
		return itemCommentService.getCommentsMedia(stuecklisteDto.getArtikelIId(), itemCommentService.createDefaultCommentFilter());
	}

	@GET
	@Path("/{" + Param.PRODUCTIONID + "}/commentmedia/{" + Param.ITEMCOMMENTID + "}")
	public Response getCommentMedia(
			@QueryParam(Param.USERID) String userId,
			@PathParam(Param.ITEMCOMMENTID) Integer itemCommentId,
			@PathParam(Param.PRODUCTIONID) Integer productionId) throws RemoteException, NamingException, UnsupportedEncodingException {
		if (connectClient(userId) == null) return null;
		
		HvValidateBadRequest.notNull(productionId, Param.PRODUCTIONID);
		HvValidateBadRequest.notNull(itemCommentId, Param.ITEMCOMMENTID);

		LosDto losDto = fertigungCall.losFindByPrimaryKeyOhneExc(productionId);
		HvValidateNotFound.notNull(losDto, Param.PRODUCTIONID, productionId);
		
		if (losDto.getStuecklisteIId() == null) {
			respondNotFound();
			return null; 
		}
		StuecklisteDto stuecklisteDto = stuecklisteCall.stuecklisteFindByPrimaryKey(losDto.getStuecklisteIId());
		
		final ArtikelkommentarsprDto kommentarSprDto = artikelkommentarCall.artikelkommentarsprFindByPrimaryKeyOhneExc(itemCommentId);
		HvValidateNotFound.notNull(kommentarSprDto, Param.ITEMCOMMENTID, itemCommentId);
		
		ArtikelkommentarDto kommentarDto = artikelkommentarCall.artikelkommentarFindByPrimaryKey(itemCommentId);
		HvValidateNotFound.notValid(kommentarDto.getArtikelIId().equals(stuecklisteDto.getArtikelIId()), Param.ITEMCOMMENTID, itemCommentId.toString());
		
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
	@Path("/{" + Param.PRODUCTIONID + "}/document/list")
	@Produces({FORMAT_JSON, FORMAT_XML})
	public DocumentInfoEntryList getDocuments(
			@QueryParam(Param.USERID) String userId,
			@PathParam(Param.PRODUCTIONID) Integer productionId) throws RepositoryException, IOException {
		if (connectClient(userId) == null) return null;
		
		if(!mandantCall.hasModulLos()) {
			respondNotFound() ;
			return new DocumentInfoEntryList();
		}
		
		HvValidateBadRequest.notNull(productionId, Param.PRODUCTIONID);
		
		LosDto losDto = fertigungCall.losFindByPrimaryKeyOhneExc(productionId);
		HvValidateNotFound.notNull(losDto, Param.PRODUCTIONID, productionId);
		
		return documentService.getDocs(productionDocService, productionId, null);
	}
	
	@GET
	@Path("/{" + Param.PRODUCTIONID + "}/document")
	public Response getDocument(
			@QueryParam(Param.USERID) String userId,
			@PathParam(Param.PRODUCTIONID) Integer productionId,
			@QueryParam(Param.DOCUMENTCNR) String documentCnr) throws RepositoryException, IOException {
		if (connectClient(userId) == null) return null;
		
		if(!mandantCall.hasModulLos()) {
			respondNotFound() ;
			return null;
		}
		
		HvValidateBadRequest.notNull(productionId, Param.PRODUCTIONID);
		HvValidateBadRequest.notNull(documentCnr, Param.DOCUMENTCNR);
		
		LosDto losDto = fertigungCall.losFindByPrimaryKeyOhneExc(productionId);
		HvValidateNotFound.notNull(losDto, Param.PRODUCTIONID, productionId);
		
		final RawDocument document = documentService.getDoc(productionDocService, productionId, "", documentCnr);
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
	@Path("/printlabels")
	@Override
	@Produces({FORMAT_JSON, FORMAT_XML})
	@Consumes({FORMAT_JSON, FORMAT_XML})
	public PrintProductionLabelEntry printLabels(
			@QueryParam(Param.USERID) String userId, 
			@QueryParam(Param.PRODUCTIONID) Integer productionId,
			@QueryParam(Param.PRODUCTIONCNR) String cnr,
			@QueryParam("printLabel") Boolean printLabel,
			@QueryParam("printerLabel") String printerLabel,
			@QueryParam("printLabelQuantity") BigDecimal printLabelQuantity,
			@QueryParam("printLabelCopies") Integer printLabelCopies,
			@QueryParam("printLabelComment") String printLabelComment) throws RemoteException, EJBExceptionLP {
		PrintProductionLabelEntry printEntry = new PrintProductionLabelEntry();

		if (connectClient(userId) == null) return printEntry;
		
		if(!mandantCall.hasModulLos()) {
			respondNotFound();
			return printEntry;
		}

		ProductionEntry entry = findProductionByIdOrCnr(productionId, cnr, 
				new HashSet<IProductionLoaderAttribute>());
		if(entry == null) {
			return printEntry;
		}
		
		printEntry.setId(entry.getId());
	
		if (Boolean.TRUE.equals(printLabel)) {
			printLosetikett(printEntry, printerLabel, printLabelQuantity, printLabelCopies, printLabelComment);
			printEntry.setPrintLabel(true);
		}
		
		return printEntry;
	}

	private void printLosetikett(PrintProductionLabelEntry printEntry, String printer, BigDecimal quantity, Integer copies, String comment) throws RemoteException, EJBExceptionLP {
		BigDecimal menge = quantity != null 
				? quantity
				: fertigungCall.getMengeDerJuengstenLosablieferung(printEntry.getId());
		Integer exemplare = copies != null
				? copies
				: new Integer(1);
				
		fertigungReportCall.printLosEtikettOnServer(printEntry.getId(), menge, 
				comment, false, exemplare, printer);
	}
	
	@POST
	@Path("/stopproduction")
	@Override
	@Produces({FORMAT_JSON, FORMAT_XML})
	public StopProductionEntry stopProduction(
			@QueryParam(Param.USERID) String userId, 
			@QueryParam(Param.PRODUCTIONID) Integer productionId,
			@QueryParam(Param.PRODUCTIONCNR) String productionCnr) throws RemoteException {
		StopProductionEntry stopEntry = new StopProductionEntry();
		
		if (connectClient(userId) == null) return stopEntry;
		
		if(!mandantCall.hasModulLos()) {
			respondNotFound() ;
			return stopEntry;
		}

		ProductionEntry entry = findProductionByIdOrCnr(productionId, 
				productionCnr, new HashSet<IProductionLoaderAttribute>());
		if(entry == null) {
			return stopEntry;
		}
		stopEntry.setId(entry.getId());
		
		try {
			fertigungCall.stoppeProduktion(entry.getId());
			stopEntry.setStopped(true);
		} catch (EJBExceptionLP e) {
			stopEntry.setPostMessage(hvEJBExceptionLPException.toString(e));
		}
		
		return stopEntry;
	}
}
