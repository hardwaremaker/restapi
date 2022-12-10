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
package com.heliumv.api.user;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
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
import com.heliumv.api.HvValidateBadRequest;
import com.heliumv.api.system.TenantEntry;
import com.heliumv.api.system.TenantEntryList;
import com.heliumv.factory.IClientCall;
import com.heliumv.factory.ILogonCall;
import com.heliumv.factory.IMandantCall;
import com.heliumv.feature.FeatureFactory;
import com.heliumv.session.HvSessionManager;
import com.heliumv.tools.StringHelper;
import com.lp.server.benutzer.service.LogonFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;

@Service("hvUser")
@Path("/api/v1/")
public class UserApi extends BaseApi implements IUserApi {
	private static Logger log = LoggerFactory.getLogger(UserApi.class) ;

	@Autowired
	private ILogonCall logonCall ;
	
	@Autowired
	private IClientCall clientCall ;
	
	@Autowired
	private IMandantCall mandantCall ;
	@Autowired
	private FeatureFactory featureFactory ;
	
	@Autowired
	private HvSessionManager sessionManager ;

	@Override
	@POST
	@Path("/logon/")
	@Consumes({"application/json", "application/xml"})
	@Produces({FORMAT_JSON, FORMAT_XML})
	public LoggedOnEntry logon(LogonEntry logonEntry) throws NamingException, RemoteException {
		HvValidateBadRequest.notNull(logonEntry, "logonEntry"); 
		HvValidateBadRequest.notEmpty(logonEntry.getUsername(), "username"); 
		HvValidateBadRequest.notEmpty(logonEntry.getPassword(), "password"); 
		
		try {
			Locale theLocale = buildLocale(logonEntry.getLocaleString());

			String s = getServletRequest() != null ? getServletRequest().getRemoteAddr() : "" ;
			String userName = logonEntry.getUsername() + "|" + s ;
			TheClientDto theClientDto = logonCall.logon(userName, 
					logonEntry.getPassword().toCharArray(), theLocale, logonEntry.getClient()) ;
			if(theClientDto == null) {
				respondUnauthorized();
				return new LoggedOnEntry();
			}
			
			LoggedOnEntry entry = new LoggedOnEntry() ;
			entry.setToken(theClientDto.getIDUser()) ;
			entry.setClient(theClientDto.getMandant()) ;
			entry.setLocaleString(theClientDto.getLocMandantAsString().trim()) ;
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
	
	
	@GET
	@Path("/logout/{token}")
	public void logoutPathParam(@PathParam("token") String token) throws NamingException, RemoteException {
		logoutImpl(token) ;
	}
	
	@GET
	@Path("/logout")
	public void logout(@QueryParam(Param.USERID) String userId) throws NamingException, RemoteException {	
		logoutImpl(userId) ;
	}
	
	protected void logoutImpl(String userId) throws NamingException, RemoteException {
		HvValidateBadRequest.notEmpty(userId, Param.USERID); 
		TheClientDto theClientDto = clientCall.theClientFindByUserLoggedIn(userId) ;
		if(theClientDto != null) {
			logonCall.logout(theClientDto) ;
			
			sessionManager.setRequest(getServletRequest());
			sessionManager.expire(userId) ;
			respondOkay();
		}
	}
	
	@Override
	@POST
	@Path("/logonapp")
	@Consumes({"application/json", "application/xml"})
	@Produces({FORMAT_JSON, FORMAT_XML})
	public LoggedOnTenantEntry logonExternal(LogonTenantEntry logonEntry) throws RemoteException, NamingException {
		LoggedOnTenantEntry loggedOnEntry = new LoggedOnTenantEntry() ;
		HvValidateBadRequest.notNull(logonEntry, "logonEntry"); 
		HvValidateBadRequest.notEmpty(logonEntry.getUsername(), "username"); 
		HvValidateBadRequest.notEmpty(logonEntry.getPassword(), "password");
 
		try {
			Locale theLocale = buildLocale(logonEntry.getLocaleString());
			String s = getServletRequest().getRemoteAddr() ;
			TheClientDto theClientDto = logonCall.logonExtern(
					LogonFac.AppType.Stueckliste, logonEntry.getUsername(), 
					logonEntry.getPassword().toCharArray(), theLocale, logonEntry.getTenantCnr(), s) ;
			if(theClientDto == null) {
				respondUnauthorized() ;
				return loggedOnEntry ;
			}
			
			sessionManager.setRequest(getServletRequest());
			sessionManager.create(theClientDto.getIDUser()) ;
			
			LoggedOnTenantEntry entry = new LoggedOnTenantEntry() ;
			entry.setToken(theClientDto.getIDUser()) ;
			entry.setClient(theClientDto.getMandant()) ;
			entry.setLocaleString(theClientDto.getLocMandantAsString().trim()) ;
			entry.setValid(true);
			return entry ;
		} catch(EJBExceptionLP e) {
			if(e.getCode() == EJBExceptionLP.FEHLER_KEINE_STUECKLISTEN_FUER_PARTNERID) {
				respondNotFound() ;
				return loggedOnEntry ;
			}
			if(e.getCode() == EJBExceptionLP.FEHLER_KUNDENSTUECKLISTEMANDANT_NICHT_EINDEUTIG) {
				List<TenantEntry> tenants = new ArrayList<TenantEntry>() ;
				List<Object> mandants = e.getAlInfoForTheClient() ;
				for (Object o : mandants) {
					MandantDto mandantDto = mandantCall.mandantFindByPrimaryKey((String)o) ;

					TenantEntry entry = new TenantEntry(mandantDto.getCNr()) ;
					entry.setDescription(mandantDto.getCKbez()) ;
					tenants.add(entry) ;
				}
				respondExpectationFailed(EJBExceptionLP.FEHLER_KUNDENSTUECKLISTEMANDANT_NICHT_EINDEUTIG);
				LoggedOnTenantEntry entry = new LoggedOnTenantEntry() ;
				entry.setPossibleTenants(new TenantEntryList(tenants));
				entry.setValid(false);
				return entry ;
			}
			if(e.getCode() == EJBExceptionLP.FEHLER_ZAHL_ZU_GROSS) {
				respondTooManyRequests(); 
			} else {
				// ABSICHTLICH Unauthorized um einem Angreifer keine Hinweise zu geben
				respondUnauthorized() ;
			}
		} catch(Exception e) {
			respondUnauthorized() ;
		}
		
		return loggedOnEntry ;
	}
	
	@POST
	@Path("/password")
	@Consumes({"application/json", "application/xml"})
	public void changePassword(
			@QueryParam(Param.USERID) String userId,
			ChangePasswordEntry passwordEntry) throws NamingException, RemoteException, EJBExceptionLP, Exception {
		if(connectClient(userId) == null) return ;
		if(!featureFactory.hasCustomerPartlist()) {
			respondBadRequest(BaseApi.HvErrorCode.EXPECTATION_FAILED);
			return ;
		}
		if(passwordEntry == null || StringHelper.isEmpty(passwordEntry.getPassword())) {
			respondBadRequest("passwordEntry", "null");
			return ;
		}
		
		featureFactory.getObject().changePassword(passwordEntry.getPassword());
	}
	
	
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
	@Path("/logonidcard/{idcard}")
	@Consumes({"application/json", "application/xml"})
	@Produces({FORMAT_JSON, FORMAT_XML})
	public LoggedOnEntry logonIdCard(
			@PathParam("idcard") String idCard,
			LogonIdCardEntry logonEntry) throws NamingException, RemoteException {
		Context env = (Context) new InitialContext().lookup("java:comp/env");
		String hvUser = (String) env.lookup("heliumv.credentials.user");
		String hvPassword= (String) env.lookup("heliumv.credentials.password");
		
		try {
			Locale theLocale = buildLocale(logonEntry.getLocaleString());
			String s = getServletRequest() != null ? getServletRequest().getRemoteAddr() : "" ;
			String userName = hvUser + "|" + s ;
			TheClientDto theClientDto = logonCall.logonIdCard(userName, 
					hvPassword.toCharArray(), theLocale, logonEntry.getClient(), idCard) ;
			if(theClientDto == null) {
				respondUnauthorized() ;
				return null ;
			}
			
			LoggedOnEntry entry = new LoggedOnEntry() ;
			entry.setToken(theClientDto.getIDUser()) ;
			entry.setClient(theClientDto.getMandant()) ;
			entry.setLocaleString(theClientDto.getLocMandantAsString().trim()) ;
			return entry ;
		} catch(EJBExceptionLP e) {
			// respondBadRequest(e) ;
			// ABSICHTLICH Unauthorized um einem Angreifer keine Hinweise zu geben
			if(e.getCode() == EJBExceptionLP.FEHLER_ZAHL_ZU_GROSS) {
				respondTooManyRequests(); 
			} else {
				respondUnauthorized() ;
			}
		} catch(Exception e) {
			respondUnauthorized() ;
		}
		
		return null ;
		
	}
}
