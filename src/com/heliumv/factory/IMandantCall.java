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
package com.heliumv.factory;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Locale;

import javax.naming.NamingException;

import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.ModulberechtigungDto;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.MwstsatzbezDto;
import com.lp.server.system.service.ZahlungszielDto;
import com.lp.server.system.service.ZusatzfunktionberechtigungDto;
import com.lp.util.EJBExceptionLP;

public interface IMandantCall {
	
	Locale getLocaleDesHauptmandanten() throws EJBExceptionLP ;
	String getMandantEmailAddress() throws RemoteException, EJBExceptionLP ;
	MandantDto mandantFindByPrimaryKey(String mandantCnr) throws RemoteException, EJBExceptionLP ;
	MandantDto mandantFindByPrimaryKey() throws RemoteException, EJBExceptionLP ;

	ModulberechtigungDto[] modulberechtigungFindByMandantCnr(String mandantCnr) ;

	ZusatzfunktionberechtigungDto[] zusatzfunktionberechtigungFindByMandantCnr(String mandantCnr)  ;
	
	boolean hasModulAngebot();
	boolean hasModulAngebot(String mandantCnr) ;

	boolean hasModulArtikel()  ;
	boolean hasModulArtikel(String mandantCnr) ;
	
	boolean hasModulAuftrag() ;
	boolean hasModulAuftrag(String mandantCnr)  ;
	
	boolean hasModulProjekt()  ;
	boolean hasModulProjekt(String mandantCnr) ;
	
	boolean hasModulLieferschein()  ;
	boolean hasModulLieferschein(String mandantCnr) ;

	boolean hasModulLos() ;
	boolean hasModulLos(String mandantCnr) ;
	
	/**
	 * Existiert ein Modul fuer den Mandanten?</br>
	 * <p><b>Bitte diese Funktion nur benutzen wenn es unbedingt notwendig ist. Besser ist es,
	 * die speziellen Methoden wie hasModulProjekt()...hasModulAngebot()... zu benutzen</b></p>
	 * 
	 * @param moduleName ist der betreffende Modulname aus LocaleFac.*
	 * 
	 * @return true wenn der angemeldete Client Zugriff auf das Modul hat
	 * @throws NamingException
	 */
	boolean hasNamedModul(String moduleName) ;
	
	boolean hasFunctionProjektZeiterfassung() ;
	boolean hasFunctionProjektZeiterfassung(String mandantCnr) ;

	boolean hasFunctionAngebotsZeiterfassung() ;
	boolean hasFunctionAngebotsZeiterfassung(String mandantCnr) ;	
	
	boolean hasFunctionZentralerArtikelstamm() ;
	boolean hasFunctionZentralerArtikelstamm(String mandantCnr) ;	

	boolean hasFunctionKostentraeger() ;
	boolean hasFunctionKostentraeger(String mandantCnr) ;	

	boolean hasFunctionHvmaZeiterfassung(String mandantCnr);
	boolean hasFunctionHvmaZeiterfassung();

	boolean hasFunctionHvma2(String mandantCnr);
	boolean hasFunctionHvma2();

	MwstsatzDto mwstSatzDtoFindByPrimaryKey(Integer mwstsatzId) throws RemoteException ;	
	MwstsatzbezDto mwstsatzbezDtoFindByPrimaryKey(Integer mwstsatzBezId) throws RemoteException ;
	MwstsatzDto mwstsatzDtoZuDatum(Integer mwstsatzbezId, Timestamp datum) throws RemoteException ;

	boolean hasFunctionPruefplan();
	boolean hasFunctionPruefplan(String mandantCnr);
	
	boolean hasModulZeiterfassung();
	boolean hasModulZeiterfassung(String mandantCnr);
	
	boolean hasModulForecast();
	boolean hasModulForecast(String mandantCnr);
	
	public boolean hasModulBestellung();
	public boolean hasModulBestellung(String mandantCnr);
	
	boolean hasFunctionReisezeiten();
	boolean hasFunctionReisezeiten(String mandantCnr);
	ZahlungszielDto zahlungszielFindByCnr(String cnr);
}
