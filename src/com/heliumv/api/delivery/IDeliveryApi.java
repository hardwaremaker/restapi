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
package com.heliumv.api.delivery;

import java.rmi.RemoteException;

import javax.naming.NamingException;

import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;

import com.lp.util.EJBExceptionLP;

public interface IDeliveryApi {

	/**
	 * Eine auftragsbezogene Lieferscheinposition erzeugen/&auml;ndern</br>
	 * <p>Gibt es f&uuml;r diese Auftragsposition noch keine Lieferscheinposition, wird diese
	 * neu erzeugt. Ansonsten werden die Daten aus positionEntry zu der bereits bestehenden
	 * Lieferscheinposition hinzugef&uuml;gt.</p>
	 * 
	 * @param userId ist der beim Logon ermittelte "token" 
	 * @param positionEntry die zus&auml;tzlichen Positionsdaten
	 * @return Informationen zu der (neu) entstandenen Lieferscheinposition
	 * @throws RemoteException
	 */
	CreatedDeliveryPositionEntry createOrUpdatePositionFromOrder(
			String userId, CreateDeliveryPositionEntry positionEntry) throws RemoteException;
	
	/**
	 * Die angegebene LieferscheinpositionId l&ouml;schen
	 * @param userId ist der beim Logon ermittelte "token" 
	 * @param positionId die zu entfernende LieferscheinpositionId
	 * @throws RemoteException
	 */
	void removePosition (
			String userId, Integer positionId) throws RemoteException;

	/**
	 * Die durch die AuftragspositionId definierte Lieferscheinposition entfernen</br>
	 * <p>Es wird (derzeit) nur dann die Lieferscheinposition entfernt, wenn es genau eine(!) Lieferscheinposition
	 * f&uuml;r die angegebene Auftragsposition gibt</p>
	 * @param userId ist der beim Logon ermittelte "token" 
	 * @param deliveryId den Lieferschein den es betrifft 
	 * @param orderpositionId die zu dieser AuftragspositionId zugeh&ouml;rige Lieferscheinposition 
	 * @throws RemoteException
	 */
	void removePositionByOrder(
			String userId, Integer deliveryId,
			Integer orderpositionId) throws RemoteException;
	
	/**
	 * Den Lieferschein als "geliefert" betrachten und ausdrucken</br>
	 * <p>Der Lieferschein wird auf dem Drucker ausgedruckt, der dem Arbeitsplatz (des mobilen Endger&auml;tes)
	 * zugewiesen worden ist</p>
	 * 
	 * @param userId ist der beim Logon ermittelte "token" 
	 * @param deliveryId die Id des Lieferscheins
	 * @param sortCnr erm&ouml;glicht - sofern angegeben - die Sortierung der erfassten Lieferscheinposition
	 *   nach Artikelnummer (<code>ITEMNUMBER</code>) oder nach der Reihenfolge der 
	 *   Auftragspositionen (<code>ORDERPOSITION</code>)
	 * @return deliveryId sofern der Lieferschein berechnet, aktiviert und gedruckt werden konnte
	 * @throws RemoteException
	 */
	Integer printDelivery(
			String userId, 
			Integer deliveryId,
			String sortCnr) throws RemoteException;
	

	/**
	 * Eine neue Lieferscheinposition zu einem freien Lieferschein hinzuf&uuml;gen
	 * 
	 * <p>Freier Lieferschein bedeutet, dass dieser Lieferschein keinen Bezug zu 
	 * einem Auftrag hat.</p>
	 * 
	 * @param userId ist der beim Logon ermittelte "token" 
	 * @param positionEntry die hinzuzuf&uuml;genden Lieferscheinpositionsdaten
	 * @return die neu erzeugte Lieferscheinposition
	 * @throws RemoteException
	 */
	CreatedDeliveryPositionEntry createPosition(
			String userId,
			CreateItemDeliveryPositionEntry positionEntry) throws RemoteException;
	
	/**
	 * Eine bestehende freie Lieferscheinposition ab&auml;ndern
	 * 
	 * @param userId  ist der beim Logon ermittelte "token" 
	 * @param positionId die Id der abzu&auml;ndernden Lieferscheinposition
	 * @param positionEntry die neuen Lieferscheinpositionsdaten
	 * @return die neuen Lieferscheinpositionsdaten
	 * @throws RemoteException
	 */
	CreatedDeliveryPositionEntry updatePosition(
			String userId,
			Integer positionId,			
			CreateItemDeliveryPositionEntry positionEntry) throws RemoteException;

	/**
	 * Eine neue freie Lieferscheinposition f&uuml;r einen Kundenlieferschein erstellen</br>
	 * <p>Bei Bedarf wird ein neuer Lieferschein angelegt. Dies wird dadurch mitgeteilt, dass
	 * positionEntry.deliveryId null ist. Sollen f&uuml;r den Lieferschein weitere Positionen
	 * erfasst werden, muss die - hier erhaltene - deliveryId in den folgenden Anfragen angegeben
	 * werden. Die Kundennummer - customerNr - muss immer angegeben werden. Achtung: customerNr
	 * ist die dem Kunden zugewiesene Kundennummer. Sie ist nicht mit der customerId zu verwechseln!</p>

	 * <p>Der betroffene Lieferschein muss ein freier Lieferschein sein, d.h. er
	 * darf keinen Bezug zu einem Auftrag haben.</p>
	 * 
	 * @param userId ist der beim Logon ermittelte "token" 
	 * @param positionEntry die neuen Lieferscheinpositionsdaten
	 * @return die neuen Lieferscheinpositionsdaten
	 * @throws RemoteException
	 */
	CreatedDeliveryPositionEntry createCustomerPosition(
			String userId,
			CreateItemCustomerDeliveryPositionEntry positionEntry) throws RemoteException;

	/**
	 * Legt Dateien in die Dokumentenablage eines Lieferscheins ab.
	 * <p>Die maximale Upload-Gr&ouml;&szlig;e ist derzeit auf 500KiB gestellt</p>
	 * 
	 * @param deliveryId Id des Lieferscheins
	 * @param userId ist der beim Logon ermittelte "Token"
	 * @param type ist die Belegart (optional)
	 * @param keywords sind die Schlagworte des Dokuments (optional)
	 * @param grouping ist die Gruppierung (optional)
	 * @param securitylevel ist die Sicherheitsstufe (optional)
	 * @param body ist der <code>multipart/form-data</code> des Requests, der den/die Dokumente enth&auml;lt
	 * @throws RemoteException
	 * @throws NamingException
	 */
	void createDocument(
			Integer deliveryId, 
			String userId, 
			String type,
			String keywords, 
			String grouping, 
			Long securitylevel,
			MultipartBody body) throws RemoteException, NamingException;

	/**
	 * Das Versandetikett des Lieferscheins ausdrucken</br>
	 * <p>Das Versandetikett wird auf dem Drucker ausgedruckt, der dem Arbeitsplatz (des mobilen Endger&auml;tes)
	 * zugewiesen worden ist</p>
	 * 
	 * @param userId ist der beim Logon ermittelte "token" 
	 * @param deliveryId die Id des Lieferscheins
	 * @param activateDelivery wenn <code>true</code> wird der Lieferschein auch berechnet und aktiviert
	 * @return deliveryId sofern der Lieferschein optional berechnet/aktiviert und gedruckt werden konnte
	 * @throws RemoteException
	 */
	Integer printDispatchLabel(
			String userId, 
			Integer deliveryId,
			Boolean activateDelivery) throws RemoteException;

	/**
	 * Das Warenausgangsetikett des Lieferscheins ausdrucken</br>
	 * <p>Das Warenausgangsetikett wird auf dem Drucker ausgedruckt, der dem Arbeitsplatz (des mobilen Endger&auml;tes)
	 * zugewiesen worden ist</p>
	 * 
	 * @param userId ist der beim Logon ermittelte "token" 
	 * @param deliveryId die Id des Lieferscheins
	 * @param activateDelivery wenn <code>true</code> wird der Lieferschein auch berechnet und aktiviert
	 * @return deliveryId sofern der Lieferschein optional berechnet/aktiviert und gedruckt werden konnte
	 * @throws RemoteException
	 */
	Integer printGoodsIssueLabel(
			String userId, 
			Integer deliveryId,
			Boolean activateDelivery) throws RemoteException;
	
	/**
	 * Die Liste aller angelegten Kundenlieferscheine des Kunden</br>
	 * <p>Der Kunde wird durch seine "Kundennummer" identifiziert. Es werden nur jene Lieferscheine
	 * angezeigt, f&uuml;r dessen Lager der Anwender eine Berechtigung hat.</p>
	 * 
	 * @param userId ist der beim Logon ermittelte "token" 
	 * @param customerCnr Die Kundennummer muss immer angegeben werden. Achtung: customerCNr
	 * ist die dem Kunden zugewiesene Kundennummer. Sie ist nicht mit der customerId zu verwechseln!
	 * @return eine Liste aller im Status ANGELEGT befindlichen Kundenlieferscheine der Kundennummer
	 * @throws RemoteException
	 */
	DeliveryEntryList getCustomerDeliverableSlips(
			String userId,
			Integer customerCnr) throws RemoteException;
	
	/**
	 * Die Liste der Positionen dieses Kundenlieferscheins</br>
	 * <p>Der Lieferschein muss sich noch im Status ANGELEGT befinden, ansonsten gibt es eine
	 * entsprechende Fehlermeldung (Expectation failed, falscher Status)
	 * 
	 * @param userId  ist der beim Logon ermittelte "token" 
	 * @param customerCnr Die Kundennummer muss immer angegeben werden. Achtung: customerCNr
	 * ist die dem Kunden zugewiesene Kundennummer. Sie ist nicht mit der customerId zu verwechseln!
	 * @param deliveryId die Id des Kundenlieferscheins
	 * @param itemId ist die (optionale) Id des Artikels auf den die Positionsinfo eingeschr&auml;nkt werden soll
	 * @param itemCnr ist die (optionale) Artikelnummer auf den die Positionsinfo eingeschr&auml;nkt werden soll 
	 * @return eine Liste der Positionen des angegebenen Lieferscheins. Wurde <code>itemId</code> oder <code>itemCnr</code> angegeben, 
	 *   enth&auml;lt die Liste nur noch diese eine entsprechende Position
	 * @throws RemoteException
	 */
	DeliveryPositionEntryList getCustomerDeliverableSlipPositions(
			String userId,Integer customerCnr,
			Integer deliveryId,
			Integer itemId,
			String itemCnr) throws RemoteException;

	/**
	 * Die Liste der Positionen dieses Kundenlieferscheins</br>
	 * <p>Der Lieferschein muss sich noch im Status ANGELEGT befinden, ansonsten gibt es eine
	 * entsprechende Fehlermeldung (Expectation failed, falscher Status)</p>
	 * 
	 * <p>Der Kunde darf keine keine Liefersperre haben. Falls er f&uuml;r die
	 * Lieferung gesperrt, wird dies mit Bad Request (KUNDE_GESPERRT) beantwortet.</p>
	 * 
	 * @param userId  ist der beim Logon ermittelte "token" 
	 * @param deliveryId die Id des Kundenlieferscheins
	 * @param itemId ist die (optionale) Id des Artikels auf den 
	 *   die Positionsinfo eingeschr&auml;nkt werden soll
	 * @param itemCnr ist die (optionale) Artikelnummer auf den
	 *   die Positionsinfo eingeschr&auml;nkt werden soll 
	 * @param orderPositionId ist die (optionale) Id der Auftragsposition 
	 *   auf die die Positionsinfo eingeschr&auml;nkt werden soll
	 * @return die Daten des Lieferscheins. Eine Liste der Positionen
	 *  des angegebenen Lieferscheins (positionData).
	 *  Die jeweiligen Positionsarten sind dann in ihren entsprechenden Listen 
	 *  (itemPositions f&uuml;r Artikelpositionen bzw. textPositions f&uuml;r Textpositionen).
	 *   Wurde <code>itemId</code>, <code>itemCnr</code> oder <code>orderPositionId</code>
	 *   angegeben, sind nur noch diese Positionen enthalten
	 * @throws RemoteException
	 */
	DeliveryData getCustomerDeliverableSlipPositionsByDeliveryId(
			String userId, Integer deliveryId, Integer itemId,
			String itemCnr, Integer orderPositionId) throws RemoteException;

	/**
	 * Eine Liste aller Lieferscheine gem&auml;&szlig; den Filterkriterien
	 * 
	 * @param userId userId ist der beim Logon ermittelte "token"
	 * @param limit (optional) die maximale Anzahl von Eintr&auml;gen die ermittelt werden sollen
	 * @param startIndex (optional) der StartIndex
	 * @param filterCnr (optional) schr&auml;nkt die Ausgabe auf Lieferscheine ein, die diesen Teil der Lieferscheinnummer beinhalten 
	 * @param filterStatus (optional) die zu filternden Status (aus <code>DeliveryDocumentStatus</code>) getrennt durch Komma
	 * @param filterOrderCnr (optional) schr&auml;nkt die Ausgabe auf Lieferscheine ein, die mit diesem Auftrag verkn&uuml;pft sind
	 * @param filterCustomer (optional) schr&auml;nkt die Ausgabe auf Lieferscheine ein, die f&uuml;r diesen Kundennamen ausgestellt wurden
	 * @return eine (leere) Liste von Lieferscheinen
	 * @throws RemoteException
	 * @throws EJBExceptionLP
	 * @throws NamingException
	 */
	DeliveryEntryList getListDeliveryEntry(
			String userId, 
			Integer limit, 
			Integer startIndex, 
			String filterCnr,
			String filterStatus, 
			String filterOrderCnr, 
			String filterCustomer) throws RemoteException, EJBExceptionLP, NamingException;


	/**
	 * Eine neue freie Lieferscheinposition f&uuml;r einen Kundenlieferschein erstellen</br>
	 * <p>Bei Bedarf wird ein neuer Lieferschein angelegt. Dies wird dadurch mitgeteilt, dass
	 * positionEntry.deliveryId null ist. Sollen f&uuml;r den Lieferschein weitere Positionen
	 * erfasst werden, muss die - hier erhaltene - deliveryId in den folgenden Anfragen angegeben
	 * werden. Die Kundennummer - customerNr - muss immer angegeben werden. Achtung: customerNr
	 * ist die dem Kunden zugewiesene Kundennummer. Sie ist nicht mit der customerId zu verwechseln!</p>
	 * <p>Es wird automatisch zuerst nach offenen Forecast-Positionen f&uuml;r den angegbenen
	 * Artikel gesucht. Sollte es solche nicht geben, wird nach offenen Auftragspositionen 
	 * gesucht. Gibt es auch keine Auftragsfunktionen wird abgebrochen.</p>
	 * <p>Sollte der Fall eintreten, dass die zu buchende Menge auf mehrere Belegpositionen
	 * (Forecast-Position, Auftragposition) aufgeteilt wird, wird nur die letzte erzeugte
	 * Lieferscheinposition und somit auch nur die dort gebuchte Menge als Ergebnis gebracht.</p>
	 * <p>Im HELIUM V Server muss die entsprechende Zusatzfunktionsberechtigung freigeschaltet sein.<p>
	 * 
	 * @param userId ist der beim Logon ermittelte "token" 
	 * @param positionEntry die neuen Lieferscheinpositionsdaten
	 * @return die neuen Lieferscheinpositionsdaten
	 * @throws RemoteException
	 */
	CreatedDeliveryPositionEntry createForecastOrderCustomerPosition(
			String userId,
			CreateItemCustomerDeliveryPositionEntry positionEntry) throws RemoteException;

	/**
	 * Die Liste aller angelegten Kundenlieferscheine des Kunden</br>
	 * <p>Der Kunde wird durch seine Id identifiziert. Es werden nur jene Lieferscheine
	 * angezeigt, f&uuml;r dessen Lager der Anwender eine Berechtigung hat.</p>
	 * 
	 * @param userId ist der beim Logon ermittelte "token" 
	 * @param customerId Die Id des Kunden muss immer angegeben werden.
	 * @return eine Liste aller im Status ANGELEGT befindlichen Kundenlieferscheine des
	 * betreffenden Kunden.
	 * 
	 * @throws RemoteException
	 */
	DeliveryEntryList getCustomerIdDeliverableSlips(
			String userId, Integer customerId) throws RemoteException;


	/**
	 * Einen Lieferschein erzeugen</br>
	 * <p>Wird customerCnr oder customerId angegeben, wird ein freier Lieferschein
	 * f&uuml;r den angegebenen Kunden erzeugt. Bei einem freien Lieferschein
	 * d&uuml;rfen bis zu 10 offene Lieferscheine (dieses Kunden) angelegt werden.</p>
	 * <p>Wird orderCnr oder orderId angegeben, wird ein auftragsbezogener 
	 * Lieferschein erzeugt</p>
	 * 
	 * Generell wird &uuml;berpr&uuml;ft, ob der durch das token identifizierte
	 * HELIUM V Anwender das Recht hat, auf das Abbuchungslager des Lieferscheins
	 * zuzugreifen.
	 *  
	 * @param userId ist das beim Logon ermittelte "token" 
	 * @param customerCnr die (optionale) Kundennummer 
	 * @param customerId die (optionale) Id des Kunden. Bei einem freien 
	 *   Lieferschein muss entweder die customerCnr oder die customerId 
	 *   angegeben werden
	 * @param orderCnr die (optionale) Auftragsnummer
	 * @param orderId die (optionale) Id des Auftrags. Bei einem auftragsbezogenen 
	 *   Lieferschein muss entweder die orderCnr oder die orderId angegeben
	 *   werden.
	 * @return die Id des Lieferscheins
	 * @throws RemoteException
	 */
	Integer createDelivery(String userId, Integer customerCnr, 
			Integer customerId, String orderCnr, Integer orderId)
			throws RemoteException;

	/**
	 * Erzeugen einer Artikelposition im Lieferschein</br>
	 * <p>Es kann sich sowohl um einen freien Lieferschein als auch einen 
	 * auftragsbezogenen handeln.</p>
	 * 
	 * <p>Der Lieferschein muss im Status ANGELEGT sein.</p>
	 * 
	 * <p>Wenn eine Auftragsposition geliefert werden soll, reicht es, die
	 * <code>orderPositionId</code> und die Menge/Seriennr/Chargen anzugeben.</br>
	 * Bei der Lieferung einer Auftragsposition wird bei mehrfachen Aufrufen
	 * dieser Funktion immer zur Lieferscheinposition mit Auftragsbezug addiert.
	 * </p>
	 * 
	 * <p>Bei einem freien Lieferschein, oder bei einem auftragsbezogenen
	 * Lieferschein dessen Auftrag diesen Artikel nicht kennt, wird nur dann zur bestehenden
	 * Lieferscheinposition addiert, wenn der Parameter 
	 * <code>LIEFERSCHEIN_POSITIONEN_MIT_RESTAPI_VERDICHTEN</code> aktiviert ist,
	 * ansonsten werden neue Lieferscheinpositionen angelegt.</p>
	 * 
	 * <p>Tipp: Verwenden Sie <code>orderPositionId</code> immer dann, wenn
	 * der Artikel im Zuge des Auftrags geliefert werden soll. Verwenden Sie 
	 * <code>itemId/itemCnr</code> immer dann, wenn der Artikel zwar geliefert
	 * werden soll, aber keinen Auftragsbezug hat.</p>
	 *   
	 * @param userId ist der beim Logon ermittelte "token" 
	 * @param deliveryId ist die Id des Lieferscheins
	 * @param positionEntry die hinzuzuf&uuml;genden Lieferscheinpositionsdaten
	 * @return die neu erzeugte Lieferscheinposition
	 * @throws RemoteException
	 */
	CreatedDeliveryPositionEntry createIdentPosition(
			String userId, Integer deliveryId,
			CreateDeliveryItemPositionEntry positionEntry) throws RemoteException;

	/**
	 * Eine Texteingabe-Position zu einem Lieferschein hinzuf&uuml;gen</br>
	 * <p>Es kann sich sowohl um einen freien als auch einen
	 * auftragsbezogenen Lieferschein handeln.</p>
	 * 
	 * @param userId ist der beim Logon ermittelte "token" 
	 * @param deliveryId ist die Id des Lieferscheins
	 * @param positionEntry die hinzuzuf&uuml;genden Textposition
	 * @return die neu erzeugte Lieferscheinposition
	 * @throws RemoteException
	 */
	CreatedDeliveryPositionEntry createTextPosition(
			String userId, Integer deliveryId,
			CreateDeliveryTextPositionEntry positionEntry) throws RemoteException;

	/**
	 * &Auml;ndert die angegebene Lieferscheinposition auf eine 
	 * Textposition</br>
	 * 
	 * <p>Der Lieferschein muss im Status ANGELEGT sein.</p>
	 * 
	 * <p>Unterscheiden sich die Positionsarten der bereits bestehenden 
	 * Position und der abzu&auml;ndernden, so wird prinzipiell eine 
	 * neue Position angelegt.</p>
	 * 
	 * @param userId  ist der beim Logon ermittelte "token" 
	 * @param deliveryId ist die Id des Lieferscheins
	 * @param positionId ist die abzu&auml;ndernde Lieferscheinposition
	 * @param positionEntry der neue/abge&auml;nderte Texteintrag
	 * @return die ge&auml;nderte Lieferscheinposition
	 * 
	 * @throws RemoteException
	 */
	CreatedDeliveryPositionEntry updateTextPosition(String userId, Integer deliveryId, Integer positionId,
			CreateDeliveryTextPositionEntry positionEntry) throws RemoteException;

	/**
	 * &Auml;ndert die angegebene Lieferscheinposition auf eine Artikelposition</br>
	 * <p>Der Lieferschein muss im Status ANGELEGT sein.</p>
	 *
	 * <p>Unterscheiden sich die Positionsarten der bereits bestehenden 
	 * Position und der abzu&auml;ndernden, so wird prinzipiell eine 
	 * neue Position angelegt.</br>
	 * Auch wenn es sich bei der bestehenden Position um eine 
	 * Artikelposition handelt, kann nicht davon 
	 * ausgegangen werden, dass die Id der neuen Position die gleiche bleibt.
	 * Wird mit der Id der Position weitergearbeitet, ist die im 
	 * Response enthaltende <code>deliveryPositionId</code> zu 
	 * verwenden.</p>
	 *
	 * @param userId ist der beim Logon ermittelte "token" 
	 * @param deliveryId ist die Id des Lieferscheins
	 * @param positionId ist die abzu&auml;ndernde Lieferscheinposition
	 * @param positionEntry die neue/abge&auml;nderte Artikelposition
	 * @return die ge&auml;nderte Lieferscheinposition
	 * 
	 * @throws RemoteException
	 */
	CreatedDeliveryPositionEntry updateItemPosition(String userId, Integer deliveryId, Integer positionId,
			CreateDeliveryItemPositionEntry positionEntry) throws RemoteException;

	DeliverySlipSignatureEntry printDeliverySlipSignature(
			String userId, Integer deliveryId, String sortcnr) throws RemoteException;

	void postDeliverySlipSignature(
			String userId, Integer deliveryId,
			DeliverySlipSignaturePostEntry signatureEntry)
			throws RemoteException;
}
