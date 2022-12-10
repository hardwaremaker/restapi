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
package com.heliumv.factory.impl;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Locale;

import javax.naming.NamingException;

import com.heliumv.annotation.HvCallrate;
import com.heliumv.factory.BaseCall;
import com.heliumv.factory.ILogonCall;
import com.lp.server.benutzer.service.LogonFac;
import com.lp.server.personal.service.HvmaLizenzEnum;
import com.lp.server.system.service.TheClientDto;
import com.lp.util.Helper;

public class LogonCall extends BaseCall<LogonFac> implements ILogonCall {

	public LogonCall() {
		super(LogonFac.class);
	}
	
	@HvCallrate(maxCalls=5, durationMs=10000)
	public TheClientDto logon(String benutzer, char[] kennwort,
			Locale uILocale, String sMandantI) throws NamingException, RemoteException {
		return logonImpl(benutzer, kennwort, uILocale, sMandantI) ;
	}
	
	@HvCallrate(maxCalls=5, durationMs=10000)
	public TheClientDto logonIdCard(String benutzer, char[] kennwort,
			Locale uILocale, String sMandantI, String cAusweis) throws NamingException, RemoteException {
		return logonImpl(benutzer, kennwort, uILocale, sMandantI, cAusweis) ;
	}

	public TheClientDto programmedLogon(
			String benutzer, char[] kennwort, Locale uILocale, String sMandantI) throws NamingException, RemoteException {
		return logonImpl(benutzer, kennwort, uILocale, sMandantI) ;		
	}
	
	protected TheClientDto logonImpl(String benutzer, char[] kennwort, Locale uILocale, String sMandantI)  throws NamingException, RemoteException {
		return logonImpl(benutzer, kennwort, uILocale, sMandantI, null);
	}

	protected TheClientDto logonImpl(String benutzer, char[] kennwort,
			Locale uILocale, String sMandantI, String cAusweis)  throws NamingException, RemoteException {
		TheClientDto theClientDto = getFac().logonMobil(
			benutzer, buildCredentials(benutzer, kennwort), 
			uILocale, sMandantI, new Timestamp(System.currentTimeMillis()), cAusweis);
		
		return theClientDto;	
	}

	private char[] buildCredentials(String user, char[] kennwort) {
		String logonCredential = user ;
		int indexPipe = user.indexOf("|") ;
		if(indexPipe > 0) {
			logonCredential = user.substring(0, indexPipe) ;
		}
		return Helper.getMD5Hash(
				(logonCredential + new String(kennwort)).toCharArray());
	}
	
	public void logout(TheClientDto theClientDto) throws NamingException, RemoteException {
		getFac().logout(theClientDto) ;
	}
	
	@Override
	@HvCallrate(maxCalls=5, durationMs=10000)
	public TheClientDto logonExtern(int appType, String benutzer,
			char[] kennwort, Locale uiLocale, String mandantCnr, String source) throws NamingException,
			RemoteException {
		return getFac().logonExtern(appType, benutzer, kennwort, uiLocale, mandantCnr, source) ;
	}
	
	@Override
	@HvCallrate(maxCalls=5, durationMs=10000)
	public TheClientDto logonHvma(String benutzer, char[] kennwort,
			Locale uILocale, String sMandantI, 
			HvmaLizenzEnum licence, String resource) throws NamingException, RemoteException {
		TheClientDto theClientDto = getFac().logonHvma(
				benutzer, buildCredentials(benutzer, kennwort), 
				uILocale, sMandantI, new Timestamp(System.currentTimeMillis()),
				licence, resource);
			
		return theClientDto;	
	}
}
