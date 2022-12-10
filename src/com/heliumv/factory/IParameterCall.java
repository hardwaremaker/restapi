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

import com.heliumv.api.delivery.DeliveryPositionSort;
import com.heliumv.api.system.CnrFormat;
import com.lp.server.system.service.ArbeitsplatzDto;
import com.lp.server.system.service.ArbeitsplatzkonfigurationDto;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.util.EJBExceptionLP;

public interface IParameterCall {
	public enum AuslastungsZeitenberechnung {
		SOLLZEIT(0),
		RESTZEIT(1),
		UNBEKANNT(-1);
		
		AuslastungsZeitenberechnung(int value) {
			this.value = value ;
		}
		
		private int value ;
	}
	
	public enum AuslastungsAnzeigeDetailAg {
		STANDARD(0),
		PROJEKT_KBEZ(1),
		UNBEKANNT(-1);
		
		AuslastungsAnzeigeDetailAg(int value) {
			this.value = value ;
		}
		
		private int value ;
	}
	
	boolean isZeitdatenAufErledigteBuchbar() throws RemoteException ;
	boolean isZeitdatenAufAngelegteLoseBuchbar() throws RemoteException ;

	boolean isPartnerSucheWildcardBeidseitig() throws RemoteException ;

	/**
	 * Wird die Materialbuchung automatisch durchgefuehrt?
	 * 
	 * @return true wenn keine automatische Materialbuchung durchgefuehrt wird
	 */
	boolean isKeineAutomatischeMaterialbuchung() throws RemoteException ;

	boolean isBeiLosErledigenMaterialNachbuchen() throws RemoteException ;

	/**
	 * Die maximal erlaubte Laenge einer Artikelnummer
	 * 
	 * @return die maximal erlaubte Laenge einer Artikelnummer
	 * @throws RemoteException
	 */
	int getMaximaleLaengeArtikelnummer() throws RemoteException ;
	
	/**
	 * Im Direktfilter nach Artikelgruppe/Klasse anstatt Referenznummer suchen
	 * 
	 * @return true wenn die Gruppe bzw. Klasse anstatt der Referenznummer verwendet wird
	 * @throws RemoteException
	 */
	boolean isArtikelDirektfilterGruppeKlasseStattReferenznummer() throws RemoteException ;
	
	Integer getGeschaeftsjahr() throws RemoteException;
	
	Integer getGeschaeftsjahr(String mandantCNr) throws RemoteException;

	String getMailAdresseAdmin() throws RemoteException;

	AuslastungsZeitenberechnung getAuslastungszeitenBerechnung() throws RemoteException;
	
	/**
	 * Einen Parameter vom System laden</br>
	 * <p>Diese Methode bitte nur benutzen, wenn ein Parameter von "aussen" angefordert wird. 
	 * Innerhalb der API bitte die entsprechenden benannten Methoden verwenden</p>
	 * 
	 * @param category ist die Kategorie
	 * @param cnr ist der gesuchte Parameter
	 * @return ParametermandantDto
	 * 
	 * @throws RemoteException
	 */
	ParametermandantDto getMandantParameter(String category, String cnr) throws RemoteException;

	AuslastungsAnzeigeDetailAg getAuslastungsAnzeigeDetailAg() throws RemoteException;
	
	/**
	 * Bestimmt Artikel (true) oder Kunde (false) den Mwstsatz bei Preisberechnungen
	 * 
	 * @return true wenn der Artikel den MwstSatz bestimmt, ansonsten der Kunde 
	 * @throws RemoteException
	 * @deprecated see {@link #isArtikelBestimmtMwstsatz()}. 
	 */
	boolean getKundenPositionsKontierung() throws RemoteException ; 
	
	
	/**
	 * Bestimmt Artikel (true) oder Kunde (false) den Mwstsatz bei Preisberechnungen
	 * 
	 * @return true wenn der Artikel den MwstSatz bestimmt, ansonsten der Kunde 
	 * @throws RemoteException
	 */
	boolean isArtikelBestimmtMwstsatz() throws RemoteException ;	
	
	ArbeitsplatzDto arbeitsplatzFindByCTypCGeraetecode(
			String cTyp, String cGeraetecode) throws RemoteException ;
	ArbeitsplatzDto arbeitsplatzFindByCPcname() throws RemoteException;

	ArbeitsplatzkonfigurationDto arbeitsplatzkonfigurationFindByPrimaryKey(Integer arbeitsplatzId) ;
	void updateArbeitsplatzkonfiguration(ArbeitsplatzkonfigurationDto konfigurationDto) ;

	/**
	 * Die Minutenanzahl zu der Arbeitsg&auml;nge beim Einlasten gerastert werden
	 * @return Minutenzahl der Rasterung
	 */
	int getAuslastungsAnzeigeEinlastungsRaster() throws RemoteException;
	
	/**
	 * Die kleinste Minutenanzahl zwischen zwei Arbeitsg&auml;ngen
	 * @return Minuten zwischen zwei Arbeitsg&auml;ngen
	 */
	int getAuslastungsAnzeigePufferdauer() throws RemoteException;
	
	/**
	 * Der Standard-Arbeitszeitartikel
	 * @return null wenn leer/nicht definiert, ansonsten die (m&ouml;glicherweise falsche) Artikel-Cnr
	 * @throws RemoteException
	 */
	String getDefaultArbeitszeitArtikelCnr() throws RemoteException;
	
	/**
	 * Soll der Arbeitszeitartikel aus der Personalverf&uuml;gbarkeit ermittelt werden?
	 * @return true wenn der Arbeitszeitartikel aus der Personalverf&uuml;gbarkeit ermittelt werden soll
	 * @throws RemoteException
	 */
	boolean isArbeitszeitartikelAusPersonalverfuegbarkeit() throws RemoteException;

	boolean isSollsatzgroessePruefen() throws RemoteException;
	
	boolean isAblieferungBuchtEnde() throws RemoteException;
	
	boolean isAutofertigAblieferungTerminal() throws RemoteException;
	
	Double getGemeinkostenFaktor(Timestamp timestamp) throws RemoteException;
	
	/**
	 * Sollen beim Buchen einer Lieferscheinposition die Positionen verdichtet/zusammengefasst werden</br>
	 * <p>D.h. f&uuml;r einen Artikel gibt es - sofern verdichtet - immer nur eine einzige Position. Sollten
	 * vorher schon mehrere Positionen vorhanden gewesen sein, bleiben diese und es wird die erste verwendet.</p>
	 * @param mandantCnr
	 * @return
	 * @throws RemoteException
	 */
	boolean getLieferscheinPositionenMitAPIVerdichten() throws RemoteException;

	CnrFormat getBelegnummernformat() throws RemoteException;
	
	/**
	 * Soll eine "Warnung" ausgegeben werden, wenn Fehlmengen beim Ausgeben des
	 * Loses entst&uuml;nden?
	 * @return true wenn eine Warnung ausgegeben werden soll, falls Fehlmengen beim Ausgeben des Loses entst&uuml;nden
	 * @throws RemoteException
	 */
	boolean isWarnungWennFehlmengeEntsteht() throws RemoteException;
	
	String getGroessenaenderungZusatzstatus();

	DeliveryPositionSort getLieferscheinpositionSortierung() throws RemoteException;
	DeliveryPositionSort getLieferscheinpositionSortierung(Integer arbeitsplatzId) throws RemoteException;
}
