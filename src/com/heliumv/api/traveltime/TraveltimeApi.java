package com.heliumv.api.traveltime;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.heliumv.api.BaseApi;
import com.heliumv.api.HvValidateBadRequest;
import com.heliumv.api.todo.TodoEntryType;
import com.heliumv.factory.IAuftragCall;
import com.heliumv.factory.IGlobalInfo;
import com.heliumv.factory.IKundeCall;
import com.heliumv.factory.IMandantCall;
import com.heliumv.factory.IPersonalCall;
import com.heliumv.factory.IZeiterfassungCall;
import com.heliumv.factory.query.DailyAllowanceQuery;
import com.heliumv.tools.FilterKriteriumCollector;
import com.heliumv.tools.ShortHelper;
import com.heliumv.tools.StringHelper;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.personal.service.DiaetenDto;
import com.lp.server.personal.service.FahrzeugDto;
import com.lp.server.personal.service.ReiseDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.util.fastlanereader.service.query.FilterBlock;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.fastlanereader.service.query.QueryResult;
import com.lp.util.EJBExceptionLP;

@Service("hvTraveltime")
@Path("/api/v1/traveltime")
public class TraveltimeApi extends BaseApi implements ITraveltimeApi {
	private static Logger log = LoggerFactory.getLogger(TraveltimeApi.class);

	@Autowired 
	private IGlobalInfo globalInfo;
	@Autowired
	private IMandantCall mandantCall;
	@Autowired
	private IPersonalCall personalCall;
	@Autowired
	private IAuftragCall auftragCall;
	@Autowired
	private IKundeCall kundeCall;
	@Autowired
	private IZeiterfassungCall zeiterfassungCall;
	@Autowired
	private DailyAllowanceQuery dailyAllowanceQuery;
	
	@Override
	@POST
	@Path("/batch")
	@Consumes({BaseApi.FORMAT_JSON, BaseApi.FORMAT_XML})
	public void bookBatch(
			@QueryParam(Param.USERID) String userId,
			TravelTimeRecordingBatchEntryList batchEntries) throws NamingException, RemoteException {
		if(connectClient(userId, 300000) == null) return;
		
		HvValidateBadRequest.notNull(batchEntries, "entrylist");
		HvValidateBadRequest.notNull(batchEntries.getEntries(), "entries");
	
		validateApi();
		
		boolean success = true;
		for (TravelTimeRecordingBatchEntry entry : batchEntries.getEntries()) {
			success &= bookBatchEntry(entry, userId);
		}
		if(!success) {
			// TODO: Nachricht an "Vorgesetzten" senden, es sind Stammdaten falsch
		}
	}

	private void validateApi() throws NamingException, RemoteException {
		if(mandantCall.hasFunctionReisezeiten()) return;
			
		throw new EJBExceptionLP(
				EJBExceptionLP.FEHLER_BERECHTIGUNG_ZUSATZFUNKTION,
				MandantFac.ZUSATZFUNKTION_REISEZEITEN) ;
	}
	
	private boolean bookBatchEntry(TravelTimeRecordingBatchEntry entry,
			String userId) throws NamingException, RemoteException {
		try {
			if(TodoEntryType.ORDER.equals(entry.getTodoType())) {
				return bookBatchOrderTime(entry, userId);
			}
			
			return false;
		} catch(NullPointerException e) {
			log.warn("createReisezeit() failed Nullpointer:", e);
			return false;
		} catch(EJBExceptionLP e) {
			log.warn("createReisezeit() failed:", e);
			return false;
		} catch(Throwable t) {
			log.warn("createReisezeit() failed:", t);
			return false;
		}
	}
	
	private boolean bookBatchOrderTime(
			TravelTimeRecordingBatchEntry entry,
			String userId) throws RemoteException, NamingException {
		ReiseDto rDto = createDefaultReiseDto(entry);
		AuftragDto auftragDto = auftragCall
				.auftragFindByPrimaryKeyOhneExc(entry.getHvid()) ;
		if(isValidOrder(auftragDto, entry.getHvid())) {
			rDto.setBelegartCNr(LocaleFac.BELEGART_AUFTRAG);
			rDto.setIBelegartid(entry.getHvid());
			updateDiaeten(rDto, auftragDto);
			updateZeit(rDto);
			zeiterfassungCall.createReise(rDto);
			return true;
		} else {
			return false;
		}
	}

	private void updateZeit(ReiseDto reiseDto) {
		int tries = 10;
		ReiseDto otherDto = null;
		do {
			otherDto = zeiterfassungCall.reiseFindByPersonalIIdTZeitOhneExc(
						reiseDto.getPersonalIId(), reiseDto.getTZeit());
			if(otherDto != null) {
				--tries;
				Timestamp ts = reiseDto.getTZeit();
				ts.setTime(ts.getTime() + 1007);
				reiseDto.setTZeit(ts);
			}		
		} while(otherDto != null && (tries >= 0));
		
		if(otherDto != null) {
			log.warn("Too many tries to find unique traveltime for personalId " +
					reiseDto.getPersonalIId() + " at " + reiseDto.getTZeit().toString());
		}
	}
	
	// TODO: Es wird einfach der erste Eintrag der Diaeten genommen.
	// Das Thema ist deutlich komplexer. Denn in Oesterreich wird abhaengig davon, ob
	// es ein Ort an der Grenze (von Oesterreich) ist, weniger Taggeld berechnet. 
	// Es gibt Orte wie London, Mailand, New York bei denen mehr als fuer den Rest des
	// Landes berechnet wird. Insofern ist LKZ zu wenig. Die Aenderung im Ort/Plz und
	// Diaeten wird vorerst nach hinten verschoben.
	private void updateDiaeten(ReiseDto reiseDto, AuftragDto auftragDto) throws RemoteException {
		if(reiseDto.getDiaetenIId() != null) return;
		
		KundeDto kundeDto = kundeCall
				.kundeFindByPrimaryKeyOhneExc(auftragDto.getKundeIIdLieferadresse());
		Integer landId = kundeDto.getPartnerDto().getLandplzortDto().getIlandID();
		DiaetenDto[] diaetenDtos = zeiterfassungCall.diaetenByLandId(landId);
		if(diaetenDtos.length != 0) {
			reiseDto.setDiaetenIId(diaetenDtos[0].getIId());
			
			if(diaetenDtos.length > 1) {
				log.warn("Order '" + auftragDto.getCNr() + "' (Id:" + auftragDto.getIId() + ") " + 
						"has deliveryCountry '" + kundeDto.getPartnerDto().getLandplzortDto().getLandDto().getCLkz() + "' " + 
						"which has '" + diaetenDtos.length + "' entries. Used the first (Id:" + diaetenDtos[0].getIId() + ").");
			}
		} else {
			log.warn("Order '" + auftragDto.getCNr() + "' (Id:" + auftragDto.getIId() + ") " + 
					"has deliveryCountry '" + kundeDto.getPartnerDto().getLandplzortDto().getLandDto().getCLkz() + "' " + 
					"which has '" + diaetenDtos.length + "' entries.");
		}
	}
	
	private boolean isValidOrder(AuftragDto auftragDto, Integer orderId) throws NamingException {
		if(auftragDto == null) {
			log.warn("Order Id '" + orderId + "' doesnt exist (anymore).");
			return false ;
		}
		
		if(!auftragDto.getMandantCNr().equals(globalInfo.getMandant())) {
			log.warn("Order Id '" + orderId + "' is in different tenant ("
					+ auftragDto.getMandantCNr() + "/" + globalInfo.getMandant() + ").");
			return false;
		}

// TODO: Vorerst keine Pruefung des Auftragstatus (ghp, 21.1.2019)
//		if(!Helper.isOneOf(auftragDto.getStatusCNr(),
//				AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT,
//				AuftragServiceFac.AUFTRAGSTATUS_TEILERLEDIGT,AuftragServiceFac.AUFTRAGSTATUS_ERLEDIGT)) {
//			log.warn("Order Id '" + orderId + "' has wrong state '" + auftragDto.getStatusCNr() + "'.");
//			return false;
//		}
		
		return true;
	}
	
	private ReiseDto createDefaultReiseDto(TravelTimeRecordingBatchEntry entry) {
		ReiseDto rDto = new ReiseDto();
		rDto.setPersonalIId(globalInfo.getTheClientDto().getIDPersonal());
		rDto.setBBeginn(ShortHelper.fromBool(entry.isStartOfTravel()));
		rDto.setTZeit(getTimestamp(entry));
		rDto.setCKommentar(StringHelper.trimCut(entry.getRemark(), 80));
		rDto.setFVerrechenbar(new Double("100"));
		rDto.setDiaetenIId(entry.getDailyAllowanceId());
		updateFahrzeug(rDto, entry);

		return rDto;
	}
	
	private void updateFahrzeug(ReiseDto reiseDto, TravelTimeRecordingBatchEntry entry) {
		if(entry.getMileage() != null) {
			if(entry.isStartOfTravel()) {
				reiseDto.setIKmbeginn(entry.getMileage());
			} else {
				reiseDto.setIKmende(entry.getMileage());
			}
		}

		if(entry.getCarId() == null && StringHelper.isEmpty(entry.getPlateNumber())) {
			return;
		}
		
		if(entry.getCarId() != null) {
			FahrzeugDto fahrzeugDto = personalCall
					.fahrzeugByPrimaryKeyOhneExc(entry.getCarId());
			if(fahrzeugDto != null && fahrzeugDto.getMandantCNr().equals(globalInfo.getMandant())) {
				reiseDto.setFahrzeugIId(fahrzeugDto.getIId());
			} else {
				log.warn("Car Id '" + entry.getCarId() + "' doesnt exist (anymore).");
				reiseDto.setCFahrzeug("Id '" + entry.getCarId() + "' fehlt!");					
			}
		} else {
			reiseDto.setCFahrzeug(StringHelper.trimCut(entry.getPlateNumber(), 15));
		}	
	}
	
	private Timestamp getTimestamp(TravelTimeRecordingBatchEntry entry) {
		Calendar c = Calendar.getInstance();
		c.set(entry.getYear(), entry.getMonth() - 1, entry.getDay(),
				entry.getHour(), entry.getMinute(), entry.getSecond());
		c.set(Calendar.MILLISECOND, 0);
		return new Timestamp(c.getTimeInMillis()) ;		
	}
	
	
	@GET
	@Path("/api")
	@Produces({BaseApi.FORMAT_JSON, BaseApi.FORMAT_XML})
	public TravelTimeApiInfo getApiInfo(
			@QueryParam(Param.USERID) String userId) throws NamingException, RemoteException {
		TravelTimeApiInfo info = new TravelTimeApiInfo();
		if(connectClient(userId) == null) return info;
		
		info.setEnabled(mandantCall.hasFunctionReisezeiten());
		return info;
	}
	
	@GET
	@Path("/dailyallowance")
	@Produces({FORMAT_JSON, FORMAT_XML})
	@Override
	public DailyAllowanceEntryList getDailyAllowances(
			@QueryParam(Param.USERID) String userId,
			@QueryParam(Param.LIMIT) Integer limit,
			@QueryParam(Param.STARTINDEX) String startKey) throws RemoteException, NamingException {
		if(connectClient(userId) == null) return new DailyAllowanceEntryList();
		
		FilterKriteriumCollector collector = new FilterKriteriumCollector();
	
		QueryParameters params = dailyAllowanceQuery.getDefaultQueryParameters(collector);
		params.setLimit(limit);
		params.setKeyOfSelectedRow(startKey);
	
		QueryResult result = dailyAllowanceQuery.setQuery(params);
		return new DailyAllowanceEntryList(dailyAllowanceQuery.getResultList(result));
	}
}
