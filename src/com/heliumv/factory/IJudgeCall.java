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

import javax.naming.NamingException;

import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;

public interface IJudgeCall {
	
	boolean hatRecht(String rechtCnr)  ;
	
	boolean hasPersSichtbarkeitAlle()  ;
	boolean hasPersSichtbarkeitAlle(TheClientDto theClientDto) ;
	
	boolean hasPersSichtbarkeitAbteilung() ;
	boolean hasPersSichtbarkeitAbteilung(TheClientDto theClientDto) ;
	
	boolean hasPersZeiteingabeNurBuchen() ;
	boolean hasPersZeiteingabeNurBuchen(TheClientDto theClientDto);

	boolean hasPersDarfKommtGehtAendern();
	boolean hasPersDarfKommtGehtAendern(TheClientDto theClientDto) ;
	
	boolean hasFertDarfLosErledigen() ;
	boolean hasFertDarfLosErledigen(TheClientDto theClientDto) ;

	/**
	 * Darf ein Los erzeugt(Create)/ge&auml;ndert(Update)/gel&ouml;scht(Delete) werden?
	 * @return
	 * @throws NamingException
	 */
	boolean hasFertLosCUD() ;
	boolean hasFertLosCUD(TheClientDto theClientDto) ;

	/**
	 * Darf f&uuml;r ein Los Sollmaterial erzeugt/ge&auml;ndert/gel&ouml;scht werden?
	 * @return
	 * @throws NamingException
	 */
	boolean hasFertDarfSollmaterialCUD() ;
	boolean hasFertDarfSollmaterialCUD(TheClientDto theClientDto) ;

	/**
	 * Darf Istmaterial nachtr&auml;glich gebucht werden?
	 * 
	 * @return
	 * @throws NamingException
	 */
	boolean hasFertDarfIstmaterialManuellNachbuchen() ;
	boolean hasFertDarfIstmaterialManuellNachbuchen(TheClientDto theClientDto) ;
	
	/**
	 * Lese- oder Schreibrecht auf der Rechnung
	 * @return true wenn entweder Lese- oder Schreibrecht auf der Rechnung
	 */
	boolean hasRechnungCRUD() ; 
	boolean hasRechnungCRUD(TheClientDto theClientDto) ;

	/**
	 * Schreibrecht auf der Rechnung?
	 * @return
	 */
	boolean hasRechnungCUD() ;
	boolean hasRechnungCUD(TheClientDto theClientDto);

	/**
	 * Leserecht auf der Rechnung?
	 * @return
	 */
	boolean hasRechnungR() ;
	boolean hasRechnungR(TheClientDto theClientDto) ;
	
	void addLock(String lockedTable, Integer id) throws RemoteException, EJBExceptionLP ;	
	void removeLock(String lockedTable, Integer id) throws RemoteException, EJBExceptionLP ;	
	
	boolean hasFertDarfLosAbliefern() ;
	boolean hasFertDarfLosAbliefern(TheClientDto theClientDto) ;
	
	boolean hasPersZeiterfassungDarfMonatsabrechnungDrucken();
	boolean hasPersZeiterfassungDarfMonatsabrechnungDrucken(TheClientDto theClientDto);

	boolean hasDokumenteSicherheitsstufe0CU();
	boolean hasDokumenteSicherheitsstufe0CU(TheClientDto theClientDto);
	boolean hasDokumenteSicherheitsstufe1CU();
	boolean hasDokumenteSicherheitsstufe1CU(TheClientDto theClientDto);
	boolean hasDokumenteSicherheitsstufe2CU();
	boolean hasDokumenteSicherheitsstufe2CU(TheClientDto theClientDto);
	boolean hasDokumenteSicherheitsstufe3CU();
	boolean hasDokumenteSicherheitsstufe3CU(TheClientDto theClientDto);
	boolean hasDokumenteSicherheitsstufe99CU();
	boolean hasDokumenteSicherheitsstufe99CU(TheClientDto theClientDto);

	boolean hasLieferscheinR();
	boolean hasLieferscheinR(TheClientDto theClientDto);
	boolean hasLieferscheinCUD();
	boolean hasLieferscheinCUD(TheClientDto theClientDto);
	boolean hasLieferscheinCRUD();
	boolean hasLieferscheinCRUD(TheClientDto theClientDto);
	
	boolean hasBestellungR();
	boolean hasBestellungR(TheClientDto theClientDto);
	boolean hasBestellungCUD();
	boolean hasBestellungCUD(TheClientDto theClientDto);
	boolean hasBestellungCRUD();
	boolean hasBestellungCRUD(TheClientDto theClientDto);

	boolean hasAuftragCUD();
	boolean hasAuftragR();
	boolean hasAuftragCRUD();

	boolean hasProjektCUD();
	boolean hasProjektR();
	boolean hasProjektCRUD();
}
