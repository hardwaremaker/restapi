package com.heliumv.api.hvma;

import java.rmi.RemoteException;
import java.util.Locale;

import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
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
import com.heliumv.api.user.HvmaLogonEntry;
import com.heliumv.factory.ILogonCall;
import com.heliumv.factory.IMandantCall;
import com.heliumv.tools.StringHelper;
import com.lp.server.personal.service.HvmaLizenzEnum;
import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;

@Service("hvHvma")
@Path("/api/v1/hvma")
public class HvmaApi extends BaseApi implements IHvmaApi {
	private static Logger log = LoggerFactory.getLogger(HvmaApi.class);

	@Autowired
	private ILogonCall logonCall ;
	@Autowired
	private IMandantCall mandantCall ;
//	@Autowired
//	private IHvmaCall hvmaCall;
//	@Autowired
//	private HvmaRechtEnumMapper rechtEnumMapper;
	@Autowired
	private IHvmaService hvmaService;
	
//	@Autowired
//	private HvmaParamEntryMapper paramEntryMapper;
	
	private Locale buildLocale(String localeString) {
		if(StringHelper.isEmpty(localeString)) {
			return mandantCall.getLocaleDesHauptmandanten();
		} else {
			if(localeString.length() != 4) return null;
			Locale locale = new Locale(
					localeString.substring(0, 2),
					localeString.substring(2, 4)) ;
			return locale ;
		}
	}
	
	@Override
	@POST
	@Path("/logon")
	@Consumes({"application/json", "application/xml"})
	@Produces({FORMAT_JSON, FORMAT_XML})
	public LoggedOnHvmaEntry logonHvma(
			HvmaLogonEntry logonEntry) throws NamingException, RemoteException {
		HvValidateBadRequest.notNull(logonEntry, "logonEntry"); 
		HvValidateBadRequest.notEmpty(logonEntry.getUsername(), "username"); 
		HvValidateBadRequest.notEmpty(logonEntry.getPassword(), "password"); 
		HvValidateBadRequest.notNull(logonEntry.getLicence(), "licence");
		
		try {
			Locale theLocale = buildLocale(logonEntry.getLocaleString());
			String s = getServletRequest() != null ? getServletRequest().getRemoteAddr() : "";
			String userName = logonEntry.getUsername() + "|" + s;
			TheClientDto theClientDto = logonCall.logonHvma(userName, 
					logonEntry.getPassword().toCharArray(), 
					theLocale, logonEntry.getClient(),
					logonEntry.getLicence(), logonEntry.getResource());
			if(theClientDto == null) {
				respondUnauthorized();
				return new LoggedOnHvmaEntry();
			}
			
			LoggedOnHvmaEntry entry = new LoggedOnHvmaEntry() ;
			entry.setToken(theClientDto.getIDUser());
			entry.setClient(theClientDto.getMandant());
			entry.setLocaleString(theClientDto.getLocMandantAsString().trim());
			entry.setResource(theClientDto.getHvmaResource());
			return entry ;
		} catch(EJBExceptionLP e) {
			log.warn("EJBEx", e);
			// respondBadRequest(e) ;
			// ABSICHTLICH Unauthorized um einem Angreifer keine Hinweise zu geben
			if(e.getCode() == EJBExceptionLP.FEHLER_ZAHL_ZU_GROSS) {
				respondTooManyRequests(); 
			} else {
				respondUnauthorized() ;
			}
		} catch(Exception e) {
			log.warn("Ex", e);
			respondUnauthorized() ;
		}
		
		return null ;
	}

	@Override
	@GET
	@Path("/privilege")
	@Produces({FORMAT_JSON, FORMAT_XML})
	public HvmarechtEnumEntryList getHvmaRechte(
			@HeaderParam(ParamInHeader.TOKEN) String headerToken,
			@QueryParam(Param.USERID) String userId,
			@QueryParam(Param.LICENCECNR) String licenceCnr) throws NamingException, RemoteException {
		HvmarechtEnumEntryList entryList = new HvmarechtEnumEntryList();
		if(connectClient(headerToken, userId) == null) return entryList;

		if(StringHelper.isEmpty(licenceCnr)) {
			entryList.setEntries(hvmaService.mobilePrivileges());
		} else {
			try {
				HvmaLizenzEnum licence = HvmaLizenzEnum.fromString(licenceCnr);
				entryList.setEntries(hvmaService.mobilePrivileges(licence));
			} catch(IllegalArgumentException e) {
				respondNotFound(Param.LICENCECNR, licenceCnr);
			}
		}
		return entryList;	
	}
	
	@Override
	@GET
	@Path("/param")
	@Produces({FORMAT_JSON, FORMAT_XML})
	public HvmaParamEntryList getHvmaParams(
			@HeaderParam(ParamInHeader.TOKEN) String headerToken,
			@QueryParam(Param.USERID) String userId) throws NamingException, RemoteException {
		HvmaParamEntryList entryList = new HvmaParamEntryList();
		if(connectClient(headerToken, userId) == null) return entryList;
		
		entryList.setEntries(hvmaService.mobileParameters());
		return entryList;
	}
}
