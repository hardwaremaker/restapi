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

import javax.naming.NamingException;

public interface IUserApi {
	/**
	 * Erm&ouml;glicht das Anmelden.
	 * 
	 * 
	 * @param logonEntry Pflichtfelder sind username und password.
	 * Fehlt logonEntry.client wird der Hauptmandant verwendet
	 * Fehlt logonEntry.localeString wird die Sprache des Hauptmandanten verwendet
	 * @return null wenn die Anmeldung nicht erfolgreich war, ansonsten die Anmeldung
	 */
	LoggedOnEntry logon(LogonEntry logonEntry) throws NamingException, RemoteException ;
	
	/**
	 * Erm&ouml;glicht das Abmelden.</br>
	 * 
	 * @param token wurde zuvor von einem "logon" ermittelt.
	 */
	void logoutPathParam(String token) throws NamingException, RemoteException;
	
	/**
	 * Erm&ouml;glicht das Abmelden.</br>
	 * 
	 * @param userId wurde zuvor von einem "logon" ermittelt.
	 */
	void logout(String userId) throws NamingException, RemoteException ;

	LoggedOnTenantEntry logonExternal(LogonTenantEntry logonEntry) throws NamingException, RemoteException ;
	
	/**
	 * Erm&ouml;glicht das Abmelden &uuml;ber die Ausweisnummer eines Personals.</br>
	 * 
	 * @param idCard Ausweisnummer des anzumeldenden Personals
	 * @param logonEntry 
	 * Fehlt logonEntry.client wird der Hauptmandant verwendet
	 * Fehlt logonEntry.localeString wird die Sprache des Hauptmandanten verwendet
	 * @return null wenn die Anmeldung nicht erfolgreich war, ansonsten die Anmeldung
	 * @throws NamingException
	 * @throws RemoteException
	 */
	LoggedOnEntry logonIdCard(String idCard,
			LogonIdCardEntry logonEntry) throws NamingException, RemoteException;
}
