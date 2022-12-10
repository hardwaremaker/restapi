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
package com.heliumv.api.worktime;

import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.heliumv.api.BaseApi;
import com.heliumv.api.HvEJBExceptionLPExceptionMapper;
import com.heliumv.api.HvValidateBadRequest;
import com.heliumv.api.HvValidateNotFound;
import com.heliumv.api.BaseApi.Param;
import com.heliumv.api.item.ItemEntry;
import com.heliumv.api.order.SettlementOfHoursEntry;
import com.heliumv.api.todo.TodoEntryType;
import com.heliumv.factory.IArtikelCall;
import com.heliumv.factory.IAuftragCall;
import com.heliumv.factory.IAuftragpositionCall;
import com.heliumv.factory.IFertigungCall;
import com.heliumv.factory.IGlobalInfo;
import com.heliumv.factory.IHvmaCall;
import com.heliumv.factory.IJudgeCall;
import com.heliumv.factory.IMandantCall;
import com.heliumv.factory.INachrichtenCall;
import com.heliumv.factory.IParameterCall;
import com.heliumv.factory.IPersonalCall;
import com.heliumv.factory.IProjektCall;
import com.heliumv.factory.ISystemCall;
import com.heliumv.factory.IZeiterfassungCall;
import com.heliumv.factory.query.ArtikelArbeitszeitQuery;
import com.heliumv.factory.query.ZeitdatenQuery;
import com.heliumv.tools.FilterKriteriumCollector;
import com.heliumv.tools.JasperPrintHelper;
import com.heliumv.tools.StringHelper;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LossollarbeitsplanDto;
import com.lp.server.personal.service.HvmaLizenzEnum;
import com.lp.server.personal.service.HvmalizenzDto;
import com.lp.server.personal.service.MaschineDto;
import com.lp.server.personal.service.MaschinenzeitdatenDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.personal.service.SonderzeitenAntragEmailDto;
import com.lp.server.personal.service.SonderzeitenDto;
import com.lp.server.personal.service.TaetigkeitDto;
import com.lp.server.personal.service.ZeitdatenDto;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.personal.service.ZeitsaldoDto;
import com.lp.server.projekt.service.ProjektDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.fastlanereader.service.query.QueryResult;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;
import com.lp.util.barcode.BarcodeAuftragKombiTaetigkeit;
import com.lp.util.barcode.BarcodeException;
import com.lp.util.barcode.BarcodeLosKombiAg;
import com.lp.util.barcode.BarcodeLosKombiTaetigkeit;
import com.lp.util.barcode.BarcodeLosTaetigkeit;
import com.lp.util.barcode.BarcodeMaschineStopp;
import com.lp.util.barcode.BarcodeVisitor;
import com.lp.util.barcode.HvBarcodeDecoder;
import com.lp.util.barcode.UnknownBarcodeException;

import net.sf.jasperreports.engine.JRException;

/**
 * 
 * Funktionalit&auml;t rund um die Zeit(daten)erfassung</br>
 *
 * Generell gilt, dass nur am HELIUM V angemeldete REST-API Benutzer diese Funktionen durchf&uuml;hren k&ouml;nnen.
 * Weiters werden die Rechte des Benutzers ber&uuml;cksichtigt. Er kann - wenn er darf - im Namen einer 
 * anderen Person/Mitarbeiter die Buchungen durchf&uuml;hren.
 *
 * <p>Der Benutzer der API ist daf&uuml;r verantwortlich, dass chronologisch richtige
 * Zeitbuchungen entstehen, da der HELIUM V Server zum gegebenen Zeitpunkt (noch)
 * nicht in Zukunft schauen kann.</p>
 * 
 * <p>Weiterf&uuml;hrende Dokumentation kann im 
 * <a href="http://www.heliumv.com/documentation?token=Zeiterfassung">HELIUM V Benutzerhandbuch</a> nachgelesen werden.
 * 
 * @author Gerold
 */
@Service("hvWorktime")
@Path("/api/v1/worktime/")
public class WorktimeApi extends BaseApi implements IWorktimeApi {
	private static Logger log = LoggerFactory.getLogger(WorktimeApi.class) ;
	
	@Autowired
	private IAuftragCall auftragCall ;
	
	@Autowired
	private IAuftragpositionCall auftragpositionCall ;
	
	@Autowired
	private IJudgeCall judgeCall ;
	
	@Autowired
	private IFertigungCall fertigungCall ;
	
	@Autowired
	private IMandantCall mandantCall ;
	
	@Autowired
	private IProjektCall projektCall ;
	
	@Autowired
	private IZeiterfassungCall zeiterfassungCall ;
	
	@Autowired
	private IPersonalCall personalCall ;
	
	@Autowired
	private ArtikelArbeitszeitQuery workItemQuery ;
	
	@Autowired 
	private IGlobalInfo globalInfo ;

	@Autowired
	private ZeitdatenQuery zeitdatenQuery ;
	
	@Autowired
	private IParameterCall parameterCall ;
	@Autowired
	private IArtikelCall artikelCall ;
	@Autowired
	private INachrichtenCall nachrichtenCall;
	
	@Autowired
	private HvEJBExceptionLPExceptionMapper hvEJBExceptionLPException;
	@Autowired
	private ISystemCall systemCall;
	@Autowired
	private IHvmaCall hvmaCall;
	
	public enum BookingValidation {
		Ok,
		Document,
		Activity,
		Staff,
		OrderId,
		OrderPositionId,
		ProjectId,
		RecordingEnum,
		ProductionId,
		ProductionWorkplanId,
	}
	
	@GET
	@Path("/{year}/{month}/{day}")
	@Produces({FORMAT_JSON, FORMAT_XML})
	@Override
	public List<ZeitdatenEntry> getWorktimeEntries(
			@QueryParam("userId") String userId,
			@PathParam("year") Integer year,
			@PathParam("month") Integer month,
			@PathParam("day") Integer day,
			@QueryParam("forStaffId") Integer forStaffId,
			@QueryParam("forStaffCnr") String forStaffCnr,
			@QueryParam("limit") Integer limit) {
		List<ZeitdatenEntry> entries = new ArrayList<ZeitdatenEntry>();
		if(connectClient(userId) == null) return entries ;

		Integer personalId = globalInfo.getTheClientDto().getIDPersonal() ;
		try {
			ValidPersonalId validator = new ValidPersonalId(personalId, forStaffId, forStaffCnr);
			if(!validator.validate()) return entries ;
			personalId = validator.getStaffIdToUse() ;

			FilterKriteriumCollector collector = new FilterKriteriumCollector() ;
			collector.add(zeitdatenQuery.getFilterForPersonalId(personalId)) ;
			collector.addAll(zeitdatenQuery.getFilterForDate(year, month, day)) ;
			
			QueryParameters params = zeitdatenQuery.getDefaultQueryParameters(collector) ;
			params.setLimit(limit) ;
//			params.setKeyOfSelectedRow(startIndex) ;

			QueryResult result = zeitdatenQuery.setQuery(params) ;
			entries = zeitdatenQuery.getResultList(result) ;
		} catch(NamingException e) {
			respondUnavailable(e) ;
		} catch(RemoteException e) {
			respondUnavailable(e);
		}
		
		return entries ;
	}
	
	@DELETE
	@Path("/{worktimeId}")
	@Override
	public void removeWorktime(
			@QueryParam("userId") String userId,
			@PathParam("worktimeId") Integer worktimeId,
			@QueryParam("forStaffId") Integer forStaffId,
			@QueryParam("forStaffCnr") String forStaffCnr) {
		if(connectClient(userId) == null) return ;
		Integer personalId = globalInfo.getTheClientDto().getIDPersonal() ;
		try {
			ValidPersonalId validator = new ValidPersonalId(personalId, forStaffId, forStaffCnr);
			if(!validator.validate()) return ;
			personalId = validator.getStaffIdToUse() ;
			
			ZeitdatenDto zDto = zeiterfassungCall.zeitdatenFindByPrimaryKey(worktimeId) ;
			if(zDto == null) {
				respondBadRequest("worktimeId", worktimeId.toString()) ;
				return ;
			}

			if(personalId.equals(zDto.getPersonalIId())) {
				zeiterfassungCall.removeZeitdaten(zDto) ;
			} else {
				respondUnauthorized() ;
			}
		} catch(NamingException e) {
			respondUnavailable(e) ;
		} catch(RemoteException e) {			
			respondUnavailable(e) ;
		} catch(EJBExceptionLP e) {
			respondBadRequest(e) ;
		}
	}
	
	@POST
	@Path("/coming/")
	@Consumes({"application/json", "application/xml"})
	public void bookComing(TimeRecordingEntry entry) throws NamingException, RemoteException {
		bookTimeEntry(entry, ZeiterfassungFac.TAETIGKEIT_KOMMT) ;
	}

	
//	@POST
//	@Path("/coming/{userid}/{year}/{month}/{day}/{hour}/{minute}/{second}")
//	public Response bookComing(
//			@PathParam("userId") String userId,
//			@PathParam("year") Integer year,
//			@PathParam("month") Integer month,
//			@PathParam("day") Integer day,
//			@PathParam("hour") Integer hour,
//			@PathParam("minute") Integer minute,
//			@PathParam("second") Integer second) {
//		return bookComing(convertFrom(userId, year, month, day, hour, minute, second)) ;
//	}
	
	@POST
	@Path("/going/")
	@Consumes({"application/json", "application/xml"})
	public void bookGoing(TimeRecordingEntry entry)  throws NamingException, RemoteException {
		bookTimeEntry(entry, ZeiterfassungFac.TAETIGKEIT_GEHT) ;
	}

	
	@POST
	@Path("/pausing/")
	@Consumes({"application/json", "application/xml"})
	public void bookPausing(TimeRecordingEntry entry) throws NamingException, RemoteException {
		bookTimeEntry(entry, ZeiterfassungFac.TAETIGKEIT_UNTER) ;
	}

	@POST
	@Path("/stopping/")
	@Consumes({"application/json", "application/xml"})
	public void bookStopping(TimeRecordingEntry entry) throws NamingException, RemoteException {
		bookTimeEntry(entry, ZeiterfassungFac.TAETIGKEIT_ENDE) ;
	}
	
	@POST
	@Path("/order/")
	@Consumes({"application/json", "application/xml"})
	public void bookOrder(OrderRecordingEntry entry) throws NamingException, RemoteException {
		if(connectClient(entry.getUserId()) == null) return;
		
		if(!isValidBeleg(LocaleFac.BELEGART_AUFTRAG)) {
			respondUnauthorized();
			return;
		}

		bookOrderImpl(entry, true);
	}

	private BookingValidation bookOrderImpl(OrderRecordingEntry entry, boolean verify) throws NamingException, RemoteException {
		ValidPersonalId validator = new ValidPersonalId(
				globalInfo.getTheClientDto().getIDPersonal(), 
				entry.getForStaffId(), entry.getForStaffCnr());
		if(!validator.validate()) return BookingValidation.Staff;

		entry.setForStaffId(validator.getStaffIdToUse());
	
		if(verify) {
			if(!isValidOrderId(entry.getOrderId())) {
				respondBadRequest("orderId",  entry.getOrderId().toString()) ;				
				return BookingValidation.OrderId;
			}
			
			if(!isValidOrderPositionId(entry.getOrderId(), entry.getOrderPositionId())) {
				respondBadRequest("orderPositionId",  entry.getOrderPositionId().toString()) ;
				return BookingValidation.OrderPositionId;
			}			
		}
		
		ZeitdatenDto zDto = createDefaultZeitdatenDto(entry) ;
		zDto.setIBelegartid(entry.getOrderId()) ;
		zDto.setIBelegartpositionid(entry.getOrderPositionId()) ;
		zeiterfassungCall.createAuftragZeitdaten(zDto, true, true, true) ;
		return BookingValidation.Ok;
	}
	
	@POST
	@Path("/production/")
	@Consumes({"application/json", "application/xml"})
	public void bookProduction(ProductionRecordingEntry entry) throws NamingException, RemoteException {
		if(connectClient(entry.getUserId()) == null) return ;
		if(!isValidBeleg(LocaleFac.BELEGART_LOS)) {
			respondUnauthorized(); 
			return ;
		}
			
		bookProductionImpl(entry, true);
	}
	
	private BookingValidation bookProductionImpl(
			ProductionRecordingEntry entry, boolean verify) throws NamingException, RemoteException {
		ValidPersonalId validator = new ValidPersonalId(
				globalInfo.getTheClientDto().getIDPersonal(), 
				entry.getForStaffId(), entry.getForStaffCnr());
		if(!validator.validate()) return BookingValidation.Staff;

		entry.setForStaffId(validator.getStaffIdToUse());

		if(verify) {
			if(!isValidProductionId(entry.getProductionId())) {
				respondBadRequest("productionId", entry.getProductionId().toString()) ;
				return BookingValidation.ProductionId;
			}
			
			if(!isValidProductionWorkplanId(
					entry.getProductionId(), entry.getProductionWorkplanId())) {
				respondBadRequest("productionWorkplanId", "" + entry.getProductionWorkplanId());
				return BookingValidation.ProductionWorkplanId;
			}			
		}
		
		ZeitdatenDto zDto = createDefaultZeitdatenDto(entry) ;
		zDto.setCBelegartnr(LocaleFac.BELEGART_LOS) ;
		zDto.setIBelegartid(entry.getProductionId()) ;
		zDto.setIBelegartpositionid(entry.getProductionWorkplanId());
		zeiterfassungCall.createZeitdaten(zDto, true, true, true) ;
		
		if (entry.getMachineId() != null && entry.getProductionWorkplanId() != null) {
			MachineRecordingEntry machineRecEntry = 
					new TimeRecordingEntryMapper<MachineRecordingEntry>(MachineRecordingEntry.class)
						.mapTimeEntryToGeneric(entry);
			machineRecEntry.setMachineId(entry.getMachineId());
			machineRecEntry.setProductionWorkplanId(entry.getProductionWorkplanId());
			machineRecEntry.setMachineRecordingType(MachineRecordingType.START);
			
			bookMachineImpl(machineRecEntry);
		}
		return BookingValidation.Ok;	
	}
	
	@POST
	@Path("/project/")
	@Consumes({"application/json", "application/xml"})
	public void bookProject(ProjectRecordingEntry entry) throws NamingException, RemoteException {
		if(connectClient(entry.getUserId()) == null) return;
		if(!isValidBeleg(LocaleFac.BELEGART_PROJEKT)) {
			respondUnauthorized();
			return;
		}

		bookProjectImpl(entry, true);
	}
	
	private BookingValidation bookProjectImpl(ProjectRecordingEntry entry, boolean verify) throws NamingException, RemoteException {
		ValidPersonalId validator = new ValidPersonalId(
				globalInfo.getTheClientDto().getIDPersonal(), 
				entry.getForStaffId(), entry.getForStaffCnr());
		if(!validator.validate()) return BookingValidation.Staff;

		entry.setForStaffId(validator.getStaffIdToUse());

		if(verify) {
			if(!isValidProjectId(entry.getProjectId())) {
				respondBadRequest("projectId",  entry.getProjectId().toString()) ;
				return BookingValidation.ProjectId;
			}			
		}
		
		ZeitdatenDto zDto = createDefaultZeitdatenDto(entry) ;
		if(entry.getWorkItemId() == null) {
			entry.setWorkItemId(getDefaultWorkItemId());
		}
		
		zDto.setIBelegartid(entry.getProjectId()) ;
		zDto.setArtikelIId(entry.getWorkItemId()) ;
		zeiterfassungCall.createProjektZeitdaten(zDto, true, true, true) ;
		return BookingValidation.Ok;
	}
	
	
	private Integer getDefaultWorkItemId() throws RemoteException {
		String itemCnr = parameterCall.getDefaultArbeitszeitArtikelCnr();
		if(itemCnr == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_DEFAULT_ARBEITSZEITARTIKEL_NICHT_DEFINIERT, "");
		}

		ArtikelDto itemDto = artikelCall.artikelFindByCNrOhneExc(itemCnr);
		return itemDto == null ? null : itemDto.getIId();
	}
	
	@POST
	@Path("/batch")
	@Consumes({"application/json", "application/xml"})
	public void bookBatch(
			@QueryParam(Param.USERID) String userId,
			TimeRecordingBatchEntryList batchEntries) throws NamingException, RemoteException {
		if(connectClient(userId, 300000) == null) return ;

		HvValidateBadRequest.notNull(batchEntries, "entrylist");
		HvValidateBadRequest.notNull(batchEntries.getEntries(), "entries");
		
		zeiterfassungCall.enableRecordZeitdaten();
		boolean licenceOffline = isLicenceOffline();
		boolean success = true;
		for (TimeRecordingBatchEntry entry : batchEntries.getEntries()) {
			success &= bookBatchEntry(entry, userId, licenceOffline);
		}
		if(!success) {
			nachrichtenCall.nachrichtZeitdatenpruefen(
					zeiterfassungCall.getRecordedZeitdaten().getPersonalIId());
		}
		zeiterfassungCall.disableRecordZeitdaten();
	}
	
	private boolean isLicenceOffline() {
		Integer lizenzId = globalInfo.getTheClientDto().getHvmaLizenzId();
		if(lizenzId == null) return false;
		HvmalizenzDto lizenzDto = hvmaCall.lizenzFindByPrimaryKey(lizenzId);
		return HvmaLizenzEnum.Offline.getText().equals(lizenzDto.getCNr());
	}

	private boolean bookBatchEntry(TimeRecordingBatchEntry entry, 
			String userId, boolean licenceOffline) throws NamingException, RemoteException {
		try {
			if(TodoEntryType.PROJECT.equals(entry.getTodoType())) {
				bookBatchProjectTime(entry, userId, licenceOffline);
				return true;
			}

			if(TodoEntryType.ORDER.equals(entry.getTodoType())) {
				bookBatchOrderTime(entry, userId, licenceOffline);
				return true;
			}
			
			if(TodoEntryType.PRODUCTION.equals(entry.getTodoType())) {
				bookBatchProductionTime(entry, userId);
				return true;
			}
			
			bookBatchPersonalTime(entry, userId);
			return true;
		} catch(NullPointerException e) {
			log.warn("createZeitdaten() failed Nullpointer:", e);
			zeiterfassungCall.createZeitdatenpruefen(
					zeiterfassungCall.getRecordedZeitdaten(), 
					EJBExceptionLP.FEHLER_NULLPOINTEREXCEPTION, "");
			return false;
		} catch(EJBExceptionLP e) {
			log.warn("createZeitdaten() failed:", e);
			String s = hvEJBExceptionLPException.toString(e);
			zeiterfassungCall.createZeitdatenpruefen(
					zeiterfassungCall.getRecordedZeitdaten(), 
					e.getCode(), s);
			return false;
		} catch(Throwable t) {
			log.warn("createZeitdaten() failed:", t);
			zeiterfassungCall.createZeitdatenpruefen(
					zeiterfassungCall.getRecordedZeitdaten(), 
					EJBExceptionLP.FEHLER_NULLPOINTEREXCEPTION, t.getMessage());
			return false;
		}
	}
	
	private BookingValidation bookBatchPersonalTime(TimeRecordingBatchEntry entry, 
			String userId) throws RemoteException, NamingException {
		TimeRecordingBatchEnum recordingEnum = entry.getRecordingEnum();
		if(TimeRecordingBatchEnum.Coming.equals(recordingEnum)) {
			return bookTimeEntryImpl(entry, ZeiterfassungFac.TAETIGKEIT_KOMMT, false);
		}
		
		if(TimeRecordingBatchEnum.Leaving.equals(recordingEnum)) {
			return bookTimeEntryImpl(entry, ZeiterfassungFac.TAETIGKEIT_GEHT, false);
		}
		
		if(TimeRecordingBatchEnum.Pause.equals(recordingEnum)) {
			return bookTimeEntryImpl(entry, ZeiterfassungFac.TAETIGKEIT_UNTER, false);
		}
		
		HvValidateBadRequest.notValid(false, "personal.recordingEnum", 
			recordingEnum != null ? recordingEnum.toString(): "null");
		return BookingValidation.RecordingEnum;
	}

	private BookingValidation bookBatchProductionTime(TimeRecordingBatchEntry entry, 
			String userId) throws RemoteException, NamingException, EJBExceptionLP {
		TimeRecordingBatchEnum recordingEnum = entry.getRecordingEnum();
		if(TimeRecordingBatchEnum.Start.equals(recordingEnum)) {
			ProductionRecordingEntry productionEntry = mapBatchEntryToProduction(entry);
			productionEntry.setUserId(userId);
			
			setupDefaultArbeitszeitartikelImpl(productionEntry);
			return bookProductionImpl(productionEntry, false);
		}
		
		if(TimeRecordingBatchEnum.Stop.equals(recordingEnum)) {
			entry.setUserId(userId);
			return bookTimeEntryImpl(entry, ZeiterfassungFac.TAETIGKEIT_ENDE, false);
		}
		
		HvValidateBadRequest.notValid(false, "production.recordingEnum", recordingEnum.toString());
		return BookingValidation.RecordingEnum;
	}

	private ProductionRecordingEntry mapBatchEntryToProduction(TimeRecordingBatchEntry entry) throws RemoteException {
		ProductionRecordingEntry p = new ProductionRecordingEntry();
		p.setProductionId(entry.getHVID());
		p.setProductionWorkplanId(entry.getHvDetailId());
		p.setYear(entry.getYear());
		p.setMonth(entry.getMonth());
		p.setDay(entry.getDay());
		p.setHour(entry.getHour());
		p.setMinute(entry.getMinute());
		p.setSecond(entry.getSecond()); 
		p.setWhere(entry.getWhere());
		p.setRemark(entry.getRemark());
		
		if(entry.getHvDetailId() != null) {
			LossollarbeitsplanDto arbeitsplanDto = fertigungCall
					.lossollarbeitsplanFindByPrimaryKeyOhneExc(entry.getHvDetailId());
			p.setWorkItemId(arbeitsplanDto.getArtikelIIdTaetigkeit());
		}
		
		if(StringHelper.hasContent(entry.getDetailItemCnr())) {
			ArtikelDto artikelDto = artikelCall.artikelFindByCNrOhneExc(entry.getDetailItemCnr());
			if(artikelDto != null && artikelDto.isArbeitszeit()) {
				p.setWorkItemId(artikelDto.getIId());
			} else {
				log.warn("Item '" + entry.getDetailItemCnr() + "' is no worktime item. Ignored!");
			}
		}

		return p;
	}
	
	private BookingValidation bookBatchProjectTime(
			TimeRecordingBatchEntry entry, 
			String userId, boolean licenceOffline) throws RemoteException, NamingException, EJBExceptionLP {
		TimeRecordingBatchEnum recordingEnum = entry.getRecordingEnum();
		if(TimeRecordingBatchEnum.Start.equals(recordingEnum)) {
			ProjectRecordingEntry projectEntry = mapBatchEntryToProject(entry);
			projectEntry.setUserId(userId);
			
			prepareProjectEntry(projectEntry, licenceOffline);
			return bookProjectImpl(projectEntry, false);
		}
		
		if(TimeRecordingBatchEnum.Stop.equals(recordingEnum)) {
			entry.setUserId(userId);
			return bookTimeEntryImpl(entry, ZeiterfassungFac.TAETIGKEIT_ENDE, false);
		}
		
		HvValidateBadRequest.notValid(false, "project.recordingEnum", recordingEnum.toString());
		return BookingValidation.RecordingEnum;
	}
	
	private void prepareProjectEntry(ProjectRecordingEntry projectEntry,
			boolean licenceOffline) throws RemoteException {
		if(!licenceOffline || projectEntry.getWorkItemId() != null) return;

		Integer taetigkeitId = hvmaCall.getTaetigkeitIdProjekt();
		if(taetigkeitId == null) return;
		
		projectEntry.setWorkItemId(taetigkeitId);
	}
	
	private ProjectRecordingEntry mapBatchEntryToProject(TimeRecordingBatchEntry entry) {
		ProjectRecordingEntry p = new ProjectRecordingEntry();
		p.setProjectId(entry.getHVID());
		p.setYear(entry.getYear());
		p.setMonth(entry.getMonth());
		p.setDay(entry.getDay());
		p.setHour(entry.getHour());
		p.setMinute(entry.getMinute());
		p.setSecond(entry.getSecond()); 
		p.setWhere(entry.getWhere());
		p.setRemark(entry.getRemark());
		return p;
	}
	
	private BookingValidation bookBatchOrderTime(TimeRecordingBatchEntry entry, 
			String userId, boolean licenceOffline) throws RemoteException, NamingException, EJBExceptionLP {	
		TimeRecordingBatchEnum recordingEnum = entry.getRecordingEnum();
		if(TimeRecordingBatchEnum.Start.equals(recordingEnum)) {
			OrderRecordingEntry orderEntry = mapBatchEntryToOrder(entry);
			orderEntry.setUserId(userId);
			
			prepareOrderEntry(orderEntry, licenceOffline);
			
			if(orderEntry.getOrderPositionId() == null) {
				setupDefaultOrderPosition(orderEntry);
			}
			setupDefaultArbeitszeitartikel(orderEntry);
			
			return bookOrderImpl(orderEntry, false);
		}
		
		if(TimeRecordingBatchEnum.Stop.equals(recordingEnum)) {
			entry.setUserId(userId);
			return bookTimeEntryImpl(entry, ZeiterfassungFac.TAETIGKEIT_ENDE, false);
		}
		
		HvValidateBadRequest.notValid(false, "order.recordingEnum", recordingEnum.toString());
		return BookingValidation.RecordingEnum;
	}

	private void prepareOrderEntry(OrderRecordingEntry orderEntry,
			boolean licenceOffline) throws RemoteException {
		if(!licenceOffline || orderEntry.getWorkItemId() != null) return;
		
		Integer taetigkeitId = hvmaCall.getTaetigkeitIdAuftrag();
		if(taetigkeitId != null) {
			orderEntry.setWorkItemId(taetigkeitId);
			return;
		}

		setupArbeitszeitartikelAusPosition(orderEntry);
	}

	private boolean isBebuchbar(AuftragpositionDto  auftragpositionDto) {
		return !auftragpositionDto.isStorniert();
	}
	
	private void setupDefaultOrderPosition(OrderRecordingEntry orderEntry) throws NamingException, RemoteException {
		if(orderEntry.getOrderPositionId() == null) {
			AuftragpositionDto[] dtos = auftragpositionCall.auftragpositionFindByAuftrag(orderEntry.getOrderId());
			for (AuftragpositionDto auftragpositionDto : dtos) {
				if(auftragpositionDto.isMengenbehaftet() && isBebuchbar(auftragpositionDto)) {
					orderEntry.setOrderPositionId(auftragpositionDto.getIId());
					return;
				}
			}			
		}
		
		log.info("No valid orderposition for orderId '" + orderEntry.getOrderId() + ".");
	}
	
	private void setupDefaultArbeitszeitartikel(OrderRecordingEntry orderEntry) throws NamingException, RemoteException {
		if(orderEntry.getWorkItemId() == null) {
			setupArbeitszeitartikelAusPosition(orderEntry);
		}
		
		setupDefaultArbeitszeitartikelImpl(orderEntry);
	}
	
	private void setupArbeitszeitartikelAusPosition(
			OrderRecordingEntry orderEntry) throws RemoteException {
		if(orderEntry.getOrderPositionId() != null) {
			AuftragpositionDto posDto = auftragpositionCall
					.auftragpositionFindByPrimaryKeyOhneExc(orderEntry.getOrderPositionId());
			if(posDto != null) {
				ArtikelDto artikelDto = artikelCall.artikelFindByPrimaryKeySmallOhneExc(posDto.getArtikelIId());
				if(artikelDto != null && ArtikelFac.ARTIKELART_ARBEITSZEIT.equals(artikelDto.getArtikelartCNr())) {
					orderEntry.setWorkItemId(posDto.getArtikelIId());
				}
			}
		}		
	}

	private void setupDefaultArbeitszeitartikelImpl(
			DocumentRecordingEntry documentEntry) throws NamingException, RemoteException {		
		if(documentEntry.getWorkItemId() == null) {
			setupArbeitszeitartikelAusPersonalverfuegbarkeit(documentEntry);
		}
		
		if(documentEntry.getWorkItemId() == null) {
			documentEntry.setWorkItemId(getDefaultWorkItemId());
		}
	}

	private void setupArbeitszeitartikelAusPersonalverfuegbarkeit(
			DocumentRecordingEntry recordingEntry) throws RemoteException, NamingException {
		if(parameterCall.isArbeitszeitartikelAusPersonalverfuegbarkeit()) {
			ValidPersonalId validator = new ValidPersonalId(
					globalInfo.getTheClientDto().getIDPersonal(), 
					recordingEntry.getForStaffId(), recordingEntry.getForStaffCnr());
			if(!validator.validate()) return ;
			recordingEntry.setWorkItemId(
					personalCall.getArtikelIIdHoechsterWertPersonalverfuegbarkeit(validator.getStaffIdToUse()));
		}					
	}
	
	private OrderRecordingEntry mapBatchEntryToOrder(TimeRecordingBatchEntry entry) throws RemoteException {
		OrderRecordingEntry p = new OrderRecordingEntry();
		p.setOrderId(entry.getHVID());
		p.setOrderPositionId(entry.getHvDetailId());
		p.setYear(entry.getYear());
		p.setMonth(entry.getMonth());
		p.setDay(entry.getDay());
		p.setHour(entry.getHour());
		p.setMinute(entry.getMinute());
		p.setSecond(entry.getSecond()); 
		p.setWhere(entry.getWhere());
		p.setRemark(entry.getRemark());
		
		if(StringHelper.hasContent(entry.getDetailItemCnr())) {
			ArtikelDto artikelDto = artikelCall.artikelFindByCNrOhneExc(entry.getDetailItemCnr());
			if(artikelDto != null && artikelDto.isArbeitszeit()) {
				p.setWorkItemId(artikelDto.getIId());
				
				AuftragpositionDto[] posDtos = auftragpositionCall
						.auftragpositionFindByAuftrag(p.getOrderId());
				for (AuftragpositionDto posDto : posDtos) {
					if(artikelDto.getIId().equals(posDto.getArtikelIId())) {
						p.setOrderPositionId(posDto.getIId());
						break;
					}
				}
			} else {
				log.warn("Item '" + entry.getDetailItemCnr() + "' is no worktime item. Ignored!");
			}
		}

		return p;
	}
	
	
	private ZeitdatenDto createDefaultZeitdatenDto(DocumentRecordingEntry entry) {
		ZeitdatenDto zDto = new ZeitdatenDto() ;
		zDto.setPersonalIId(entry.getForStaffId()) ;
		zDto.setArtikelIId(entry.getWorkItemId()) ;
		zDto.setCBemerkungZuBelegart(entry.getRemark()) ;
		zDto.setXKommentar(entry.getExtendedRemark()) ;
		Timestamp t = getTimestamp(entry) ;
		zDto.setTZeit(t) ;
// PJ21377 Nacherfassen und Zeitbestaetigung		
//		zDto.setTAendern(t);
		zDto.setCWowurdegebucht(StringHelper.trim(entry.getWhere()));
		return zDto ;
	}	
	
	private boolean isValidProjectId(Integer projectId) throws NamingException, RemoteException {
		ProjektDto projektDto = projektCall.projektFindByPrimaryKeyOhneExc(projectId) ;
		if(projektDto == null) return false ;
		
		return projektDto.getMandantCNr().equals(globalInfo.getMandant()) ;
	}
		
	private boolean isValidProductionId(Integer productionId) throws RemoteException, NamingException {
		LosDto losDto = fertigungCall.losFindByPrimaryKeyOhneExc(productionId) ;
		return isValidProduction(losDto);
	}
	
	private boolean isValidProduction(LosDto losDto) throws RemoteException {
		if(losDto == null) return false ;
		
		if(!losDto.getMandantCNr().equals(globalInfo.getMandant())) return false ;
		if(FertigungFac.STATUS_GESTOPPT.equals(losDto.getStatusCNr())) return false ;
		if(FertigungFac.STATUS_ANGELEGT.equals(losDto.getStatusCNr())  && 
				!parameterCall.isZeitdatenAufAngelegteLoseBuchbar()) return false ;
		if(FertigungFac.STATUS_ERLEDIGT.equals(losDto.getStatusCNr()) &&
				!parameterCall.isZeitdatenAufErledigteBuchbar()) return false ;
		return true ;
	}
	
	private boolean isValidProductionWorkplanId(Integer productionId, Integer workplanId) throws RemoteException, NamingException {
		if(workplanId == null) return false;
		LossollarbeitsplanDto arbeitsplanDto = fertigungCall
				.lossollarbeitsplanFindByPrimaryKeyOhneExc(workplanId);
		if(arbeitsplanDto == null) return false;
		if(!arbeitsplanDto.getLosIId().equals(productionId)) return false;
		
		return true;
	}
	
	protected class ValidPersonalId {
		private Integer staffId ;
		private String staffCnr ;
		private Integer myPersonalId ;
		private Integer staffIdToUse ;

		public ValidPersonalId(Integer myPersonalId) {
			this.myPersonalId = myPersonalId ;
		}
		
		public ValidPersonalId(Integer myPersonalId, Integer forStaffId, String forStaffCnr) {			
			this.myPersonalId = myPersonalId ;
			this.staffId = forStaffId ;
			this.staffCnr = forStaffCnr ;
			this.staffIdToUse = null ;
		}

		public Integer getStaffIdToUse() {
			return staffIdToUse ;
		}
		
		public boolean validate(Integer forStaffId, String forStaffCnr) throws RemoteException, NamingException {
			this.staffId = forStaffId ;
			this.staffCnr = forStaffCnr ;
			this.staffIdToUse = null ;
			return validate() ;
		}

		public boolean validate() throws RemoteException, NamingException {
			if(staffId != null) {
				PersonalDto forPers = personalCall.byPrimaryKeySmall(staffId) ;
				return validatePersonalDto("staffId", staffId.toString(), forPers) ;
			}
			
			if(staffCnr != null) {
				PersonalDto forPers = personalCall.byCPersonalnrMandantCNrOhneExc(staffCnr) ;
				return validatePersonalDto("staffCnr", staffCnr, forPers) ;
			}

			staffIdToUse = myPersonalId ;
			return true ;
		}
		
		private boolean validatePersonalDto(String fieldName, String value, PersonalDto forPers) throws NamingException {
			if(forPers != null) {
				if(judgeCall.hasPersSichtbarkeitAlle()) {
					staffIdToUse = forPers.getIId() ;
					return true ;
				}

				if(judgeCall.hasPersSichtbarkeitAbteilung()) {
					PersonalDto mePers = personalCall.byPrimaryKeySmall(myPersonalId) ;
					if(mePers.getKostenstelleIIdAbteilung() != null) {
						if(mePers.getKostenstelleIIdAbteilung().equals(
								forPers.getKostenstelleIIdAbteilung())){
							staffIdToUse = forPers.getIId() ;
							return true ;
						}						
					}
				}				
			}

			respondBadRequest(fieldName, value);
			return false ;
		}
	}
	
	private boolean isValidOrderId(Integer orderId) throws NamingException {
		AuftragDto auftragDto = auftragCall.auftragFindByPrimaryKeyOhneExc(orderId) ;
		if(auftragDto == null) return false ;
		
		return auftragDto.getMandantCNr().equals(globalInfo.getMandant()) ;
	}
	
	
	private boolean isValidOrderPositionId(Integer orderId, Integer positionId) throws NamingException {
		AuftragpositionDto auftragPositionDto = auftragpositionCall.auftragpositionFindByPrimaryKeyOhneExc(positionId) ;
		if(auftragPositionDto == null) return false ;
		
		return orderId.equals(auftragPositionDto.getBelegIId()) ;
	}
	
	@GET
	@Path("/activities/")
	@Produces({FORMAT_JSON, FORMAT_XML})
	@Override
	public List<ItemEntry> getActivities(
			@QueryParam("userid") String userId,
			@QueryParam("limit") Integer limit,
			@QueryParam("startIndex") Integer startIndex, 
			@QueryParam("filter_cnr") String filterCnr) {
		List<ItemEntry> activities = new ArrayList<ItemEntry>() ;
		try {
			if(null == connectClient(userId)) return activities ;

			FilterKriteriumCollector collector = new FilterKriteriumCollector() ;
			collector.add(workItemQuery.getFilterArtikelNummer(filterCnr)) ;

			QueryParameters params = workItemQuery.getDefaultQueryParameters(collector);
			params.setLimit(limit) ;
			params.setKeyOfSelectedRow(startIndex) ;

			QueryResult result = workItemQuery.setQuery(params) ;
			activities = workItemQuery.getResultList(result) ;	
		} catch(NamingException e) {
			respondUnavailable(e) ;
		} catch(RemoteException e) {
			respondUnavailable(e) ;
		}
		
		return activities ;
	}	
	
	@GET
	@Path("/specialactivities/")
	@Produces({FORMAT_JSON, FORMAT_XML})
	public List<SpecialActivity> getSpecialActivities(
			@QueryParam("userid") String userId) {
		List<SpecialActivity> activities = new ArrayList<SpecialActivity>() ;
		
		try {			
			if(connectClient(userId) == null) return activities ;
			boolean hasRechtNurBuchen = judgeCall.hasPersZeiteingabeNurBuchen() ;
			
			if(hasRechtNurBuchen) {
				activities = zeiterfassungCall.getAllSprSondertaetigkeitenNurBDEBuchbar(globalInfo.getTheClientDto().getLocUiAsString());
			} else {
				activities = zeiterfassungCall.getAllSprSondertaetigkeiten(globalInfo.getTheClientDto().getLocUiAsString());
			}
		} catch(RemoteException e) {
			respondUnavailable(e) ;
		} catch(NamingException e) {
			respondUnavailable(e) ;
		}
		
		return activities ;
	}

	
	@GET
	@Path("/documenttypes/")
	@Produces({FORMAT_JSON, FORMAT_XML})
	public List<DocumentType> getDocumentTypes(
			@QueryParam(Param.USERID) String userId) {
		List<DocumentType> documentTypes = new ArrayList<DocumentType>() ;
		
		try {
			if(connectClient(userId) == null) return documentTypes ;

			documentTypes = zeiterfassungCall.getBebuchbareBelegarten(globalInfo.getTheClientDto()) ;
		} catch(NamingException e) {
			respondUnavailable(e) ;
		}
		
		return documentTypes ;
	}

	
	
	private void bookTimeEntry(TimeRecordingEntry entry, 
			String bookingType) throws NamingException, RemoteException {
		if(connectClient(entry.getUserId()) == null) return ; 
		bookTimeEntryImpl(entry, bookingType, true);
	}
	
	public BookingValidation bookTimeEntryImpl(TimeRecordingEntry entry,
			String bookingType, boolean verify) throws NamingException, RemoteException {		
		ValidPersonalId validator = new ValidPersonalId(
				globalInfo.getTheClientDto().getIDPersonal(), 
				entry.getForStaffId(), entry.getForStaffCnr());
		if(!validator.validate()) return BookingValidation.Staff;

		entry.setForStaffId(validator.getStaffIdToUse());

		Integer taetigkeitIId = getTaetigkeitIId(bookingType);
		if(verify) {
			if (null == taetigkeitIId) {
				respondBadRequest(bookingType, "undefined"); 
				return BookingValidation.Activity;
			}			
		}
		
		Timestamp timestamp = getTimestamp(entry);
		bucheZeitPersonalID(validator.getStaffIdToUse(), timestamp,
				taetigkeitIId, entry.getWhere());
		return BookingValidation.Ok;
	}
	
	private Timestamp getTimestamp(TimeRecordingEntry entry) {
		Calendar c = Calendar.getInstance();
		c.set(entry.getYear(), entry.getMonth() - 1, entry.getDay(),
				entry.getHour(), entry.getMinute(), entry.getSecond());
		return new Timestamp(c.getTimeInMillis()) ;		
	}
	
	private void bucheZeitPersonalID(Integer personalIId, Timestamp timestamp, 
			Integer taetigkeitIId, String station) throws RemoteException, NamingException {
		ZeitdatenDto zd = new ZeitdatenDto();
		zd.setPersonalIId(personalIId) ;
		zd.setTZeit(timestamp);
		zd.setTaetigkeitIId(taetigkeitIId);		
		zd.setCWowurdegebucht(StringHelper.trim(station)) ;
// PJ21377 Nacherfassen und Zeitbestaetigung		
//		zd.setTAendern(timestamp);
	
		zeiterfassungCall.createZeitdaten(zd, true, true, false);
	}
	
	
	private Integer getTaetigkeitIId(String cNr) throws NamingException {
		TaetigkeitDto taetigkeitDto = zeiterfassungCall.taetigkeitFindByCNr(cNr.trim()) ;
		return taetigkeitDto == null ? null : taetigkeitDto.getIId() ;
	}
	
	private boolean isValidBeleg(String belegart) throws NamingException {
		List<DocumentType> allowedTypes = zeiterfassungCall.getBebuchbareBelegarten() ;
		for (DocumentType documentType : allowedTypes) {
			if(belegart.equals(documentType.getId())) return true ;
		}

		return false ;
	}
	
	@POST
	@Path("/machine/")
	@Consumes({"application/json", "application/xml"})
	public void bookMachine(MachineRecordingEntry entry) throws RemoteException, NamingException {
		if (connectClient(entry.getUserId()) == null) return;
		
		HvValidateBadRequest.notNull(entry.getMachineId(), "machineId");
		HvValidateBadRequest.notNull(entry.getProductionWorkplanId(), "productionWorkplanId");
		HvValidateBadRequest.notNull(entry.getMachineRecordingType(), "machineRecordingType");
		
		ValidPersonalId validator = new ValidPersonalId(
				globalInfo.getTheClientDto().getIDPersonal(), 
				entry.getForStaffId(), entry.getForStaffCnr());
		if(!validator.validate()) return ;
		entry.setForStaffId(validator.getStaffIdToUse());
		
		bookMachineImpl(entry);
	}
	
	private void bookMachineImpl(MachineRecordingEntry entry) throws RemoteException, NamingException {
		MaschineDto maschineDto = zeiterfassungCall.maschineFindByPrimaryKey(entry.getMachineId());
		HvValidateNotFound.notNull(maschineDto, "machineId", entry.getMachineId());
		
		LossollarbeitsplanDto laDto = fertigungCall.lossollarbeitsplanFindByPrimaryKey(entry.getProductionWorkplanId());
		HvValidateNotFound.notNull(laDto, "productionWorkplanId", entry.getProductionWorkplanId());
		
		if (MachineRecordingType.START.equals(entry.getMachineRecordingType())) {
			bookMachineStartImpl(entry);
		} else if (MachineRecordingType.STOP.equals(entry.getMachineRecordingType())) {
			bookMachineStopImpl(entry);
		} else {
			respondNotFound("machineRecordingType", entry.getMachineRecordingType().getText());
		}
	}

	public void bookMachineStopImpl(MachineRecordingEntry entry) throws RemoteException, NamingException {
		zeiterfassungCall.stopMaschine(entry.getMachineId(), 
				entry.getProductionWorkplanId(), getTimestamp(entry));
	}

	private void bookMachineStartImpl(MachineRecordingEntry entry) throws RemoteException, NamingException {
		MaschinenzeitdatenDto mDto = new MaschinenzeitdatenDto();
		mDto.setMaschineIId(entry.getMachineId());
		mDto.setLossollarbeitsplanIId(entry.getProductionWorkplanId());
		mDto.setPersonalIIdGestartet(entry.getForStaffId());
		mDto.setTVon(getTimestamp(entry));
		mDto.setCBemerkung(entry.getRemark());
		zeiterfassungCall.createMaschinenzeitdaten(mDto);
	}
	
	protected class TimeRecordingEntryMapper<T extends TimeRecordingEntry> {
		private Class<T> entryClass;
		
		public TimeRecordingEntryMapper(Class<T> entryClass) {
			this.entryClass = entryClass;
		}
		
		public T mapTimeEntryToGeneric(TimeRecordingEntry timeRecEntry) {
			try {
				T entry = entryClass.newInstance();
				entry.setYear(timeRecEntry.getYear());
				entry.setMonth(timeRecEntry.getMonth());
				entry.setDay(timeRecEntry.getDay());
				entry.setHour(timeRecEntry.getHour());
				entry.setMinute(timeRecEntry.getMinute());
				entry.setSecond(timeRecEntry.getSecond()); 
				entry.setWhere(timeRecEntry.getWhere());
				entry.setForStaffCnr(timeRecEntry.getForStaffCnr());
				entry.setForStaffId(timeRecEntry.getForStaffId());
				return entry;
			} catch (InstantiationException e) {
				
			} catch (IllegalAccessException e) {
				
			}
			return null;
		}
	}
	
	@GET
	@Path("/timebalance/")
	@Produces({FORMAT_JSON, FORMAT_XML})
	public TimeBalanceEntry getTimeBalance(
			@QueryParam(Param.USERID) String userId,
			@QueryParam("year") Integer year,
			@QueryParam("month") Integer month,
			@QueryParam("day") Integer day,
			@QueryParam("forStaffId") Integer forStaffId,
			@QueryParam("forStaffCnr") String forStaffCnr) {
		if (connectClient(userId) == null) return null;

		HvValidateBadRequest.notNull(year, "year"); 
		HvValidateBadRequest.notNull(month, "month"); 
		HvValidateBadRequest.notNull(day, "day");
		
		if (!mandantCall.hasModulZeiterfassung()) {
			respondNotFound();
			return null;
		}
		
		if (!judgeCall.hasPersZeiterfassungDarfMonatsabrechnungDrucken()) {
			respondUnauthorized();
			return null;
		}
		
		Integer personalId = globalInfo.getTheClientDto().getIDPersonal();
		ValidPersonalId validator = new ValidPersonalId(personalId, forStaffId, forStaffCnr);
		try {
			if (!validator.validate()) return null;
			personalId = validator.getStaffIdToUse();
			TimeBalanceEntry entry = getTimeBalanceImpl(year, month, day, personalId);
			return entry;
		} catch(NamingException e) {
			respondUnavailable(e) ;
		} catch(RemoteException e) {
			respondUnavailable(e);
		}
		
		return null;
	}

	private TimeBalanceEntry getTimeBalanceImpl(Integer year, Integer month,
			Integer day, Integer personalId) throws EJBExceptionLP, RemoteException {
		Calendar c = Calendar.getInstance();
		c.set(year, month - 1, day - 1 , 0, 0, 0);
		ZeitsaldoDto zeitsaldoDto = zeiterfassungCall.erstelleMonatsabrechnungZeitsaldo(
				personalId, c.get(Calendar.YEAR), c.get(Calendar.MONTH), new Date(c.getTimeInMillis()));
	
		return new TimeBalanceEntryMapper().mapEntry(zeitsaldoDto);
	}
	
	@POST
	@Path("/barcode/")
	@Consumes({FORMAT_JSON, FORMAT_XML})
	@Override
	public void bookBarcode(BarcodeRecordingEntry entry) throws RemoteException, NamingException, BarcodeException {
		if (connectClient(entry.getUserId()) == null) return;
		
		if (!mandantCall.hasModulZeiterfassung()) {
			respondNotFound();
			return;
		}
		
		ValidPersonalId validator = new ValidPersonalId(
				globalInfo.getTheClientDto().getIDPersonal(), 
				entry.getForStaffId(), entry.getForStaffCnr());
		if(!validator.validate()) return ;
		entry.setForStaffId(validator.getStaffIdToUse());
		
		HvValidateBadRequest.notEmpty(entry.getBarcode(), "barcode");
		
		BookRecordingVisitor bookRecordingVisitor = new BookRecordingVisitor();
		BarcodeValidator barcodeValidator = new BarcodeValidator(entry);
		barcodeValidator.acceptVisitor(bookRecordingVisitor);
		
		HvBarcodeDecoder decoder = systemCall.createHvBarcodeDecoder();
		decoder.acceptBarcodeVisitor(barcodeValidator);
		
		try {
			decoder.decode(entry.getBarcode());
		} catch (UnknownBarcodeException ex) {
			log.error("BarcodeException", ex);
			HvValidateBadRequest.notValid(false, "barcode", entry.getBarcode());
		} catch (BarcodeException ex) {
			log.error("BarcodeException", ex);
			HvValidateBadRequest.notValid(false, "barcode", entry.getBarcode());
		}
	}
	
	protected class BarcodeValidator implements BarcodeVisitor {
		private static final String CODE_MACHINE_OVERRIDE = "??";
		private IBookRecordingVisitor bookRecordingVisitor;
		private BarcodeRecordingEntry barcodeRecEntry;
		
		public void acceptVisitor(IBookRecordingVisitor visitor) {
			bookRecordingVisitor = visitor;
		}
		
		private boolean hasBookRecordingVisitor() {
			return bookRecordingVisitor != null;
		}
		
		public BarcodeValidator(BarcodeRecordingEntry entry) {
			this.barcodeRecEntry = entry;
		}
		
		@Override
		public void visitLosKombiAg(List<BarcodeLosKombiAg> barcodeList) {
			try {
				if (!isValidBeleg(LocaleFac.BELEGART_LOS)) {
					respondUnauthorized();
					return;
				}
				TimeRecordingEntryMapper<ProductionRecordingEntry> timeRecMapper = new TimeRecordingEntryMapper<ProductionRecordingEntry>(ProductionRecordingEntry.class);

				for (BarcodeLosKombiAg losKombiAg : barcodeList) {
					LosDto losDto = fertigungCall.losFindByCNrMandantCNrOhneExc(losKombiAg.getLosCnr());
					HvValidateBadRequest.notValid(isValidProduction(losDto), Param.PRODUCTIONCNR, losKombiAg.getLosCnr());
					
					ProductionRecordingEntry productionRecEntry = timeRecMapper.mapTimeEntryToGeneric(barcodeRecEntry);
					productionRecEntry.setProductionId(losDto.getIId());

					Integer arbeitsgangNr = validIntegerNotNull(losKombiAg.getArbeitsgangNr(), "workstep");
					Integer unterarbeitsgangNr = validInteger(losKombiAg.getUnterarbeitsgangNr(), "subworkstep");

					LossollarbeitsplanDto lossollarbeitsplanDto = fertigungCall.lossollarbeitsplanFindByLosIIdArbeitsgangUnterarbeitsgangSingleResult(
							losDto.getIId(), arbeitsgangNr, unterarbeitsgangNr);
					HvValidateNotFound.notNull(lossollarbeitsplanDto, "workplan", arbeitsgangNr + (unterarbeitsgangNr != null ? "." + unterarbeitsgangNr : ""));
					productionRecEntry.setProductionWorkplanId(lossollarbeitsplanDto.getIId());
					productionRecEntry.setWorkItemId(lossollarbeitsplanDto.getArtikelIIdTaetigkeit());
					
					setupMachine(productionRecEntry, losKombiAg.getMaschineCnr());
					
					if (hasBookRecordingVisitor()) {
						bookRecordingVisitor.visitProduction(productionRecEntry);
					}
				}
			} catch (RemoteException e) {
				log.error("RemoteException", e);
				respondUnavailable(e);
			} catch (NamingException e) {
				log.error("RemoteException", e);
				respondUnavailable(e);
			}
		}

		private Integer validIntegerNotNull(String toInteger, String key) {
			HvValidateBadRequest.notEmpty(toInteger, key);
			return validInteger(toInteger, key);
		}
		
		private Integer validInteger(String toInteger, String key) {
			if (StringHelper.isEmpty(toInteger)) return null;
			
			try {
				return Integer.parseInt(toInteger);
			} catch (NumberFormatException exc) {
				HvValidateBadRequest.notValid(false, key, toInteger);
			}
			return null;
		}

		private void setupMachine(ProductionRecordingEntry productionRecEntry, String machineCnr) {
			if (StringHelper.isEmpty(machineCnr)) return;
			
			productionRecEntry.setMachineId(validMachineId(machineCnr));
		}
		
		private void setupMachine(MachineRecordingEntry machineRecEntry, String machineCnr) {
			if (StringHelper.isEmpty(machineCnr)) return;

			machineRecEntry.setMachineId(validMachineId(machineCnr));
		}
		
		private Integer validMachineId(String machineCnr) {
			if (CODE_MACHINE_OVERRIDE.equals(machineCnr)) {
				HvValidateBadRequest.notNull(barcodeRecEntry.getMachineId(), "machineId");
				MaschineDto maschineDto = zeiterfassungCall.maschineFindByPrimaryKeyOhneExc(barcodeRecEntry.getMachineId());
				HvValidateNotFound.notNull(maschineDto, "machineId", barcodeRecEntry.getMachineId());
				return maschineDto.getIId();
			} else {
				MaschineDto maschineDto = zeiterfassungCall.maschineFindByCIdentifikationsnrOhneExc(machineCnr);
				HvValidateNotFound.notNull(maschineDto, "machineCnr", machineCnr);
				return maschineDto.getIId();
			}
		}
		
		@Override
		public void visitLosKombiTaetigkeit(List<BarcodeLosKombiTaetigkeit> barcodeList) {
			try {
				if (!isValidBeleg(LocaleFac.BELEGART_LOS)) {
					respondUnauthorized();
					return;
				}
				TimeRecordingEntryMapper<ProductionRecordingEntry> timeRecMapper = new TimeRecordingEntryMapper<ProductionRecordingEntry>(ProductionRecordingEntry.class);

				for (BarcodeLosKombiTaetigkeit losKombi : barcodeList) {
					LosDto losDto = fertigungCall.losFindByCNrMandantCNrOhneExc(losKombi.getLosCnr());
					HvValidateBadRequest.notValid(isValidProduction(losDto), Param.PRODUCTIONCNR, losKombi.getLosCnr());
					
					ProductionRecordingEntry productionRecEntry = timeRecMapper.mapTimeEntryToGeneric(barcodeRecEntry);
					productionRecEntry.setProductionId(losDto.getIId());
					
					ArtikelDto artikelDto = artikelCall.artikelFindByCNrOhneExc(losKombi.getTaetigkeitCnr());
					HvValidateNotFound.notNull(artikelDto, "activity", losKombi.getTaetigkeitCnr());
					productionRecEntry.setWorkItemId(artikelDto.getIId());
					
					LossollarbeitsplanDto[] arbeitsplaene = fertigungCall.lossollarbeitsplanFindByLosIIdArtikelIIdTaetigkeit(losDto.getIId(), artikelDto.getIId());
					HvValidateNotFound.notValid(arbeitsplaene != null && arbeitsplaene.length > 0, "workplan", losKombi.getTaetigkeitCnr());
					productionRecEntry.setProductionWorkplanId(arbeitsplaene[0].getIId());
					
					setupMachine(productionRecEntry, losKombi.getMaschineCnr());
					
					if (hasBookRecordingVisitor()) {
						bookRecordingVisitor.visitProduction(productionRecEntry);
					}
				}
			} catch (NamingException e) {
				log.error("RemoteException", e);
				respondUnavailable(e);
			} catch (RemoteException e) {
				log.error("RemoteException", e);
				respondUnavailable(e);
			}
		}

		@Override
		public void visitAuftragKombiTaetigkeit(List<BarcodeAuftragKombiTaetigkeit> barcodeList) {
			try {
				if (!isValidBeleg(LocaleFac.BELEGART_AUFTRAG)) {
					respondUnauthorized();
					return;
				}

				TimeRecordingEntryMapper<OrderRecordingEntry> timeRecMapper = new TimeRecordingEntryMapper<OrderRecordingEntry>(OrderRecordingEntry.class);
				for (BarcodeAuftragKombiTaetigkeit auftragKombi : barcodeList) {
					AuftragDto auftragDto = auftragCall.auftragFindByCnr(auftragKombi.getAuftragCnr());
					HvValidateNotFound.notNull(auftragDto, Param.ORDERCNR, auftragKombi.getAuftragCnr());
					
					OrderRecordingEntry orderRecEntry = timeRecMapper.mapTimeEntryToGeneric(barcodeRecEntry);
					orderRecEntry.setOrderId(auftragDto.getIId());
					
					ArtikelDto artikelDto = artikelCall.artikelFindByCNrOhneExc(auftragKombi.getTaetigkeitCnr());
					HvValidateNotFound.notNull(artikelDto, "activity", auftragKombi.getTaetigkeitCnr());
					orderRecEntry.setWorkItemId(artikelDto.getIId());
					
					if (hasBookRecordingVisitor()) {
						bookRecordingVisitor.visitOrder(orderRecEntry);
					}
				}
			} catch (RemoteException e) {
				log.error("RemoteException", e);
				respondUnavailable(e);
			} catch (NamingException e) {
				log.error("RemoteException", e);
				respondUnavailable(e);
			}
		}

		@Override
		public void visitMaschineStopp(List<BarcodeMaschineStopp> barcodeList) {
			try {
				TimeRecordingEntryMapper<MachineRecordingEntry> timeRecMapper = new TimeRecordingEntryMapper<MachineRecordingEntry>(MachineRecordingEntry.class);
				
				for (BarcodeMaschineStopp maschineStopp : barcodeList) {
					LosDto losDto = fertigungCall.losFindByCNrMandantCNrOhneExc(maschineStopp.getLosCnr());
					HvValidateBadRequest.notValid(isValidProduction(losDto), Param.PRODUCTIONCNR, maschineStopp.getLosCnr());
					
					Integer arbeitsgangNr = validIntegerNotNull(maschineStopp.getArbeitsgangNr(), "workstep");
					Integer unterarbeitsgangNr = validInteger(maschineStopp.getUnterarbeitsgangNr(), "subworkstep");

					LossollarbeitsplanDto lossollarbeitsplanDto = fertigungCall.lossollarbeitsplanFindByLosIIdArbeitsgangUnterarbeitsgangSingleResult(
							losDto.getIId(), arbeitsgangNr, unterarbeitsgangNr);
					HvValidateNotFound.notNull(lossollarbeitsplanDto, "workplan", arbeitsgangNr + (unterarbeitsgangNr != null ? "." + unterarbeitsgangNr : ""));
					
					MachineRecordingEntry machineRecEntry = timeRecMapper.mapTimeEntryToGeneric(barcodeRecEntry);
					machineRecEntry.setProductionWorkplanId(lossollarbeitsplanDto.getIId());
					
					setupMachine(machineRecEntry, maschineStopp.getMaschineCnr());
					HvValidateBadRequest.notNull(machineRecEntry.getMachineId(), "machineCnr");
					
					machineRecEntry.setMachineRecordingType(MachineRecordingType.STOP);
					
					if (hasBookRecordingVisitor()) {
						bookRecordingVisitor.visitMachineStop(machineRecEntry);
					}
				}
			} catch (RemoteException e) {
				log.error("RemoteException", e);
				respondUnavailable(e);
			} catch (NamingException e) {
				log.error("RemoteException", e);
				respondUnavailable(e);
			}
		}

		@Override
		public void visitLosTaetigkeit(List<BarcodeLosTaetigkeit> barcodeList) {
			try {
				if (!isValidBeleg(LocaleFac.BELEGART_LOS)) {
					respondUnauthorized();
					return;
				}
				TimeRecordingEntryMapper<ProductionRecordingEntry> timeRecMapper = new TimeRecordingEntryMapper<ProductionRecordingEntry>(ProductionRecordingEntry.class);
	
				for (BarcodeLosTaetigkeit losTaetigkeit : barcodeList) {
					LosDto losDto = fertigungCall.losFindByCNrMandantCNrOhneExc(losTaetigkeit.getLosCnr());
					HvValidateBadRequest.notValid(isValidProduction(losDto), Param.PRODUCTIONCNR, losTaetigkeit.getLosCnr());
					
					ProductionRecordingEntry productionRecEntry = timeRecMapper.mapTimeEntryToGeneric(barcodeRecEntry);
					productionRecEntry.setProductionId(losDto.getIId());
					
					ArtikelDto artikelDto = artikelCall.artikelFindByCNrOhneExc(losTaetigkeit.getTaetigkeitCnr());
					HvValidateNotFound.notNull(artikelDto, "activity", losTaetigkeit.getTaetigkeitCnr());
					productionRecEntry.setWorkItemId(artikelDto.getIId());
					
					if (hasBookRecordingVisitor()) {
						bookRecordingVisitor.visitProduction(productionRecEntry);
					}
				}
			} catch (NamingException e) {
				log.error("RemoteException", e);
				respondUnavailable(e);
			} catch (RemoteException e) {
				log.error("RemoteException", e);
				respondUnavailable(e);
			}
		}
		
	}
	
	protected interface IBookRecordingVisitor {
		void visitProduction(ProductionRecordingEntry entry) throws RemoteException, NamingException;
		void visitMachineStop(MachineRecordingEntry entry) throws RemoteException, NamingException;
		void visitOrder(OrderRecordingEntry entry) throws RemoteException, NamingException;
	}
	
	protected class BookRecordingVisitor implements IBookRecordingVisitor {
		public void visitProduction(ProductionRecordingEntry entry) throws RemoteException, NamingException {
			bookProductionImpl(entry, false);
		}
		
		public void visitMachineStop(MachineRecordingEntry entry) throws RemoteException, NamingException {
			bookMachineStopImpl(entry);
		}
		
		public void visitOrder(OrderRecordingEntry entry) throws RemoteException, NamingException {
			bookOrderImpl(entry, false);
		}
	}
	
	@Override
	@POST
	@Path("/specialtimes")
	@Consumes({"application/json", "application/xml"})
	public void bookSpecialTimes(
			@QueryParam(Param.USERID) String userId,
			SpecialTimesEntryList entries) throws RemoteException, NamingException {
		if(connectClient(userId, 300000) == null) return;

		HvValidateBadRequest.notNull(entries, "entrylist");
		HvValidateBadRequest.notNull(entries.getEntries(), "entries");
	
		boolean success = true;
		for (SpecialTimesEntry entry : entries.getEntries()) {
			success &= bookSpecialTimeEntry(entry, userId);
		}
/*
 * Pruefung, ob ein Eintrag erzeugt wurde (oder nicht), vorerst
 * deaktiviert. Falls createSonderzeitenVonBis fehlschlaegt, gibt
 * es eine EJB-Exception
 *
 		if(!success) {
			nachrichtenCall.nachrichtZeitdatenpruefen(
					zeiterfassungCall.getRecordedZeitdaten().getPersonalIId());
		}		
 */
	}
	
	private boolean bookSpecialTimeEntry(SpecialTimesEntry entry, String userId) throws RemoteException, NamingException {
		SonderzeitenDto dto = mapSpecialTimesEntry(entry);
		if(dto == null) return false;
	
		dto.setPersonalIId(globalInfo.getTheClientDto().getIDPersonal());
		boolean created = zeiterfassungCall.createSonderzeitenVonBis(dto, 
				new Timestamp(entry.getFromDateMs()), new Timestamp(entry.getToDateMs())) != null;
		if(created) {
			SpecialTimesEnum time = entry.getTimeType();
			SonderzeitenAntragEmailDto emailDto =
					new SonderzeitenAntragEmailDto(
							time.equals(SpecialTimesEnum.Holiday),
							time.equals(SpecialTimesEnum.Illness), 
							new Date(entry.getFromDateMs()), 
							new Date(entry.getToDateMs()), 
							globalInfo.getTheClientDto().getIDPersonal());
			zeiterfassungCall.createSonderzeitenEmail(emailDto);
		}
		
		return created;
	}

	private SonderzeitenDto mapSpecialTimesEntry(SpecialTimesEntry entry) throws NamingException {
		SonderzeitenDto dto = new SonderzeitenDto();
		if(entry.isHalfDay()) {
			dto.setBHalbtag(Helper.getShortTrue());
			dto.setBTag(Helper.getShortFalse());
		} else {
			dto.setBHalbtag(Helper.getShortFalse());
			dto.setBTag(Helper.getShortTrue());
		}

		String bookingType = mapSpecialTimesEnum(entry.getTimeType());
		if(bookingType == null) {
			respondBadRequest("timetype", entry.getTimeType().toString());
			return null;
		}
		
		Integer taetigkeitIId = getTaetigkeitIId(bookingType);
		if(taetigkeitIId == null) {
			respondBadRequest("timetype", bookingType);
			return null;
		}
		
		dto.setTaetigkeitIId(taetigkeitIId);
		dto.setTDatum(new Timestamp(entry.getFromDateMs()));
		return dto;
	}

	private String mapSpecialTimesEnum(SpecialTimesEnum e) {
		if(e.equals(SpecialTimesEnum.Holiday)) return ZeiterfassungFac.TAETIGKEIT_URLAUBSANTRAG;
		if(e.equals(SpecialTimesEnum.TimeCompensation)) return ZeiterfassungFac.TAETIGKEIT_ZAANTRAG;
		if(e.equals(SpecialTimesEnum.Illness)) return ZeiterfassungFac.TAETIGKEIT_KRANKANTRAG;
		return null;
	}
	
	@Override
	@GET
	@Path("/monthlyreport")
	@Produces({FORMAT_JSON, FORMAT_XML})
	public MonthlyReportEntry getMonthlyReport(
			@QueryParam(Param.USERID) String userId,
			@QueryParam(Param.PERSONALID) Integer personalId,
			@QueryParam("year") Integer year,
			@QueryParam("month") Integer month, 
			@QueryParam("selectoption") MonthlyReportSelectEnum selectOption,
			@QueryParam("sortoption") MonthlyReportSortEnum sortOption,
			@QueryParam("toendofmonth") Boolean toEndOfMonth,
			@QueryParam(Filter.HIDDEN) Boolean withHidden)
			throws RemoteException, NamingException {
		MonthlyReportEntry entry = new MonthlyReportEntry();
		if(connectClient(userId) == null) {
			return entry;
		}

		if (!mandantCall.hasModulZeiterfassung()) {
			respondNotFound();
			return entry;
		}

//		if (!judgeCall.hasPersZeiterfassungDarfMonatsabrechnungDrucken()) {
//			respondUnauthorized();
//			return entry;
//		}

		if(personalId == null) {
			personalId = globalInfo.getTheClientDto().getIDPersonal();
		}
		Calendar c = Calendar.getInstance();
		
		if(year == null) {
			year = c.get(Calendar.YEAR);
		}
		if(month == null) {
			month = c.get(Calendar.MONTH);
		}
		if(selectOption == null) {
			selectOption = MonthlyReportSelectEnum.THIS_PERSON;
		}
		if(sortOption == null) {
			sortOption = MonthlyReportSortEnum.PERSONALCNR;
		}
		if(toEndOfMonth == null) {
			toEndOfMonth = Boolean.TRUE;
		}
		if(withHidden == null) {
			withHidden = Boolean.FALSE;
		}
		
		Date toDate = null;
		Double onlyIfHoursBiggerThan = null;
		
		try {
			JasperPrintLP print = zeiterfassungCall.printMonatsabrechnung(
					personalId, year, month, selectOption, sortOption,
					toEndOfMonth, toDate, onlyIfHoursBiggerThan, withHidden);
			byte[] pdfContent = JasperPrintHelper.asPdf(print);
			entry.setPdfContent(pdfContent);
			entry.setLastPagePng(JasperPrintHelper.asJpeg(print));
		} catch(JRException e) {
			log.error("JRException", e);
		} catch(IOException e) {		
			log.error("IOException", e);
		}

		return entry;
	}
}
