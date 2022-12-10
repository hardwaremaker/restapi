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
package com.heliumv.api.system;

import java.rmi.RemoteException;

import javax.naming.NamingException;

/**
 * Systeminformationen abfragen
 * 
 * @author Gerold
 */
public interface ISystemApi {
	/**
	 * Pr&uuml;ft, ob vom API-Server ein Zugriff auf den HELIUM V Server m&ouml;glich ist.</b>
	 * <p>Liefert zus&auml;tzlich Informationen &uuml;ber den verbundenen HELIUM V Server</p>
	 *  
	 * @return verschiedene Informationen &uuml;ber das System
	 */
	PingResult ping() ;

	/**
	 * Pr&uuml;ft, ob ein Zugriff auf den API-Server m&ouml;glich ist.</b>
	 * <p>Liefert zus&auml;tzlich Informationen &uuml;ber den API Server</p>
	 *  
	 * @return verschiedene Informationen &uuml;ber das System
	 */
	LocalPingResult localping() ;
	
	/**
	 * Einen Log-Eintrag erzeugen
	 * @param logEntry enth&auml;lt die Daten des Logeintrags
	 */
	void logMessage(LogMessageEntry logEntry);
	
	/**
	 * Einen "unbekannten" Barcode protokollieren
	 * @param entry der Inhalt/Zeitstempel des Barcodes
	 */
	void logBarcode(LogBarcodeEntry entry);

	/**
	 * Die Liste aller im System bekannten L&auml;nder
	 * @param userId
	 * @param limit
	 * @param startKey
	 * @return die (leere) Liste aller im System bekannten L&auml;nder
	 * @throws RemoteException
	 * @throws NamingException
	 */
	CountryEntryList getCountries(String userId, Integer limit, String startKey)
			throws RemoteException, NamingException;

	/**
	 * Eine Liste aller im System befindlichen Kostentr&auml;ger ermitteln</br>
	 * 
	 * @param userId
	 * @param limit
	 * @param startKey
	 * @return eine (leere) Liste der Kostentr&auml;ger
	 * @throws RemoteException
	 * @throws NamingException
	 */
	CostBearingUnitEntryList getCostBearingUnits(String userId, Integer limit, String startKey)
			throws RemoteException, NamingException;

	/**
	 * Eine Liste aller im System befindlichen Textbausteine ermitteln</br>
	 * 
	 * @param userId
	 * @param limit
	 * @param startKey
	 * @param filterWithHidden
	 * @param addContents wenn <code>true</code> werden auch die Inhalte der Textbausteine
	 * - also die Texte, oder Bildinhalte - &uuml;bermittelt
	 * @return
	 * @throws RemoteException
	 * @throws NamingException
	 */
	TextblockEntryList getTextBlocks(String userId, Integer limit, String startKey, Boolean filterWithHidden,
			Boolean addContents) throws RemoteException, NamingException;

	/**
	 * Eine Liste aller im System befindlichen Mehrwertsteuersatzbezeichnungen
	 * 
	 * @param userId
	 * @param limit
	 * @param startKey
	 * @return
	 * @throws RemoteException
	 * @throws NamingException
	 */
	TaxDescriptionEntryList getTaxDescriptions(String userId, Integer limit, String startKey)
			throws RemoteException, NamingException;

	/**
	 * Eine Liste aller im System befindlichen Einheiten (Artikel)</br>
	 * <p><code>description</code> wird dabei in der Sprache geliefert, mit
	 * der der API-Anwender angemeldet ist. Sollte keine &Uuml;bersetzung 
	 * f&uuml;r die Sprache verf&uuml;gbar sein, ist die Property nicht vorhanden/leer.</p>
	 * 
	 * @param userId
	 * @param cnr die optionale Einheit die ermittelt werden soll
	 * @param limit
	 * @param startKey
	 * @return eine (leere) Liste aller im System bekannten Einheiten
	 * @throws RemoteException
	 * @throws NamingException
	 */
	ItemUnitEntryList getItemUnits(String userId, String cnr, Integer limit, String startKey)
			throws RemoteException, NamingException;
}