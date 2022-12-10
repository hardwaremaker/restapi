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
package com.heliumv.api.order;

import java.rmi.RemoteException;
import java.util.List;

import javax.naming.NamingException;

import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;

public interface IOrderApi {
	
	/**
	 * Eine Liste aller Auftr&auml;ge liefern, die den Filterkriterien entsprechen
	 * @param userId ist der beim Logon ermittelte "token"
	 * @param limit die maximale Anzahl von Eintr&auml;gen die ermittelt werden sollen
	 * @param startIndex der StartIndex 
	 * @param filterCnr die zu filternde Auftragsnummer
	 * @param filterCustomer der zu filternde Kundenname
	 * @param filterProject der zu filternde Projektname des Auftrags
	 * @param filterWithHidden mit <code>true</code> werden auch versteckte Auftr&auml;ge geliefert
	 * @param representativeSign sofern angegeben, das/die zu filternde Kurzzeichen des Vertreters. 
	 * Es k&ouml;nnen mehrere Kurzzeichen angegeben werden, wenn diese durch , (Komma) voneinander
	 * getrennt werden. Beispiel: <code>abc</code> oder <code>ab,cd</code>  
	 * @return eine (leere) List aller Auftr&auml;ge, die den Filterkriterien entsprechen
	 * @throws RemoteException 
	 * @throws NamingException 
	 */
	List<OrderEntry> getOrders(
		String userId, Integer limit, Integer startIndex, String filterCnr,
		String filterCustomer, String filterProject, 
		Boolean filterWithHidden, Boolean filterMyOpen,
		String representativeSign) throws NamingException, RemoteException ;
	
	/**
	 * Die Liste der Positionen f&uuml;r den angegeben Auftrag ermitteln
	 * @param orderId ist die Auftrag-Id f&uuml;r die die Positionen ermittelt werden sollen
	 * @param userId ist der beim Logon ermittelte "token"
	 * @param limit ist die maximale Anzahl von zu liefernden Positionsdaten. Default 50
	 * @param startIndex ist der Index ab dem die Positionen geliefert werden sollen
	 * @return
	 */
	List<OrderpositionEntry> getPositions(
			Integer orderId,
			String userId,
			Integer limit,
			Integer startIndex) ;
	
	/**
	 * Eine Liste aller Positionen die den Filterkriterien f&uuml;r Auftr&auml;ge entsprechen ermitteln
	 * 
	 * @param userId ist der beim Logon ermittelte "token"
	 * @param limit die maximale Anzahl von zu liefernden Auftr&auml;gen
	 * @param startIndex der Startindex der Auftr&auml;ge
	 * @param filterCnr die zu filternde Auftragsnummer
	 * @param filterCustomer der zu filternde Kundenname
	 * @param filterProject der zu filternde Projektname des Auftrags
	 * @param filterWithHidden mit <code>true</code> werden auch versteckte Auftr&auml;ge geliefert
	 * @param representativeSign sofern angegeben, das/die zu filternde Kurzzeichen des Vertreters. 
	 * Es k&ouml;nnen mehrere Kurzzeichen angegeben werden, wenn diese durch , (Komma) voneinander
	 * getrennt werden. Beispiel: <code>abc</code> oder <code>ab,cd</code>  
	 * @return eine Liste von <code>OrderpositionsEntry</code>
	 * @throws RemoteException 
	 * @throws NamingException 
	 */
	List<OrderpositionsEntry> getOrderPositions(
			String userId,
			Integer limit,
			Integer startIndex,
			String filterCnr,
			String filterCustomer, 
			String filterProject,
			Boolean filterWithHidden, 
			Boolean filterMyOpen,
			String representativeSign) throws NamingException, RemoteException;
	
	/**
	 * Liefert die Auftragsdaten f&uuml;r eine sp&auml;tere Offline-Verarbeitung<br>
	 * 
	 * <p>Es werden s&auml;mtliche den Kritieren entsprechenden Auftr&auml;ge geliefert.
	 * Zus&auml;tzlich die Positionen f&uuml;r dieser Auftr&auml;ge und die unterschiedlichen
	 * Lieferadressen.</p>
	 * 
	 * @param headerUserId ist der beim Logon ermittelte Token. (optional) 
	 * @param userId ist der beim Logon ermittelte "token"
	 * @param limit die maximale Anzahl von zu liefernden Auftr&auml;gen
	 * @param startIndex der Startindex der Auftr&auml;ge
	 * @param filterCnr die zu filternde Auftragsnummer
	 * @param filterCustomer der zu filternde Kundenname des Auftraggebers
	 * @param filterProject der zu filternde Projektname des Auftrags
	 * @param filterDeliveryCustomer der zu filternde Kundename des Empf&auml;ngers
	 * @param filterWithHidden mit <code>true</code> werden auch versteckte Auftr&auml;ge geliefert
	 * @param representativeSign sofern angegeben, das/die zu filternde Kurzzeichen des Vertreters. 
	 * Es k&ouml;nnen mehrere Kurzzeichen angegeben werden, wenn diese durch , (Komma) voneinander
	 * getrennt werden. Beispiel: <code>abc</code> oder <code>ab,cd</code>  
	 * @return eine Liste von <code>OfflineOrderEntry</code>
	 * @throws NamingException
	 * @throws RemoteException
	 */
	OfflineOrderEntry getOfflineOrders(
			String headerUserId,
			String userId,
			Integer limit,
			Integer startIndex,
			String filterCnr,
			String filterCustomer, 
			String filterDeliveryCustomer, 
			String filterProject,
			Boolean filterWithHidden,
			String representativeSign) throws NamingException, RemoteException;
	
	/**
	 * Die Auftragskommentare ermitteln<br>
	 * <p>Einer der Parameter <code>orderId</code> oder <code>orderCnr</code> muss angegeben werden.</p>
	 * 
	 * @param headerToken ist der beim Logon ermittelte Token
	 * @param orderId ist die (optionale) Id des Auftrags
	 * @param orderCnr ist die (optionale) Auftragsnummer
	 * @param userId ist der beim Logon ermittelte "token"
	 * @param addInternalComment wenn true oder nicht angegeben wird der interne Kommentar ermittelt
	 * @param addExternalComment wenn true oder nicht angegeben wird der externe Kommentar ermittelt
	 * @return
	 */
	OrderComments getOrderComments (
			String headerToken,
			String userId,
			Integer orderId,
			String orderCnr,
			Boolean addInternalComment,
			Boolean addExternalComment) ;	
	
	/**
	 * Ermittelt den "Lieferstatus" eines Auftrags
	 * 
	 * @param headerToken ist der beim Logon ermittelte Token
	 * @param userId ist der beim Logon ermittelte "token"
	 * @param orderId ist die (optionale) Id des Auftrags. Ist auch die orderCnr angegeben, hat die orderId Priorit&auml;t
	 * @param orderCnr ist die (optionale) Auftragsnummer.
	 * @return den Status eines lieferbaren Auftrags
	 * @throws NamingException
	 * @throws RemoteException
	 */
	DeliverableOrderEntry getDeliverableOrderStatus (
			String headerToken,
			String userId,
			Integer orderId,
			String orderCnr) throws NamingException, RemoteException ;

	/**
	 * Legt Dateien in die Dokumentenablage eines Auftrags ab.
	 * <p>Die maximale Upload-Gr&ouml;&szlig;e ist derzeit auf 500KiB gestellt</p>
	 *  
	 * @param orderId Id des Auftrags
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
			Integer orderId, 
			String userId, 
			String type,
			String keywords, 
			String grouping, 
			Long securitylevel,
			MultipartBody body) throws RemoteException, NamingException;

	SettlementOfHoursEntry getSettlementOfHours(Integer orderId, String userId) throws RemoteException, NamingException;

	void postSettlementOfHoursSignature(SettlementOfHoursSignatureEntry signatureEntry) throws RemoteException;

	/**
	 * Eine Artikel-Position zu einem Auftrag hinzuf&uuml;gen</br>
	 * <p>Der Auftrag muss im Status ANGELEGT sein.</p>
	 * 
	 * @param userId  ist der beim Logon ermittelte "token" 
	 * @param orderId ist die Id des Auftrags
	 * @param positionEntry die hinzuzuf&uuml;genden Identposition
	 * @return die neu erzeugte Auftragsposition
	 * @throws RemoteException
	 */
	CreatedOrderPositionEntry createIdentPosition(String userId, Integer orderId,
			CreateOrderItemPositionEntry positionEntry) throws RemoteException;

	/**
	 * Eine Texteingabe-Position zu einem Auftrag hinzuf&uuml;gen</br>
	 * <p>Der Auftrag muss im Status ANGELEGT sein.</p>
	 * 
	 * @param userId ist der beim Logon ermittelte "token" 
	 * @param orderId ist die Id des Auftrags
	 * @param positionEntry die hinzuzuf&uuml;genden Textposition
	 * @return die neu erzeugte Auftragsposition
	 * @throws RemoteException
	 */
	CreatedOrderPositionEntry createTextPosition(String userId, Integer orderId,
			CreateOrderTextPositionEntry positionEntry) throws RemoteException;

	/**
	 * Eine Textbaustein-Position zu einem Auftrag hinzuf&uuml;gen</br>
	 * <p>Der Auftrag muss im Status ANGELEGT sein.</p>
	 * <p>Es k&ouml;nnen auch Textbausteine verwendet werden, die Bilder
	 * enthalten</p>
	 * <p>Bei auf Texten basierende Textbausteinen kann die <code>localeCnr</code>
	 * angegeben werden. Wird sie nicht angegeben, wird der Textbaustein mit 
	 * der Locale des angemeldeten Benutzers verwendet. Der Textbaustein muss
	 * in dieser Locale existieren.</p>
	 * @param userId ist der beim Logon ermittelte "token" 
	 * @param orderId ist die Id des Auftrags
	 * @param positionEntry die hinzuzuf&uuml;genden Textbausteinposition
	 * @return die neu erzeugte Auftragsposition
	 * @throws RemoteException
	 */	
	CreatedOrderPositionEntry createTextblockPosition(String userId, Integer orderId,
			CreateOrderTextblockPositionEntry positionEntry) throws RemoteException;

	/**
	 * Die angegebene Auftragsposition l&ouml;schen</br>
	 * <p>Der Auftrag muss im Status ANGELEGT sein.</p>
	 * 
	 * @param userId ist der beim Logon ermittelte "token"
	 * @param positionId die zu entfernende AuftragspositionId
	 * @throws RemoteException
	 */
	void removePosition(String userId, Integer positionId) throws RemoteException;

	/**
	 * &Auml;ndert die angegebene Auftragsposition auf eine Identposition</br>
	 * 
	 * <p>Der Auftrag muss im Status ANGELEGT sein.</p>
	 * 
	 * <p>Unterscheiden sich die Positionsarten der bereits bestehenden 
	 * Position und der abzu&auml;ndernden, so wird prinzipiell eine 
	 * neue Position angelegt.</p>
	 * <p>Auch wenn es sich bei der bestehenden Position um eine
	 * Artikelposition handelt, kann nicht davon ausgegangen werden,
	 * dass die Id der neuen Position die gleiche bleibt. Wird mit der
	 * Id der Position weitergearbeitet, ist die im Response enthaltende
	 * <code>orderPositionId</code> zu verwenden.</p>

	 * @param userId  ist der beim Logon ermittelte "token" 
	 * @param orderId ist die Id des Auftrags
	 * @param positionId ist die abzu&auml;ndernde Auftragsposition
	 * @param positionEntry die neue/abge&auml;nderte Identposition
	 * @return die ge&auml;nderte Auftragsposition
	 * 
	 * @throws RemoteException
	 */
	CreatedOrderPositionEntry updateIdentPosition(String userId, Integer orderId, Integer positionId,
			CreateOrderItemPositionEntry positionEntry) throws RemoteException;

	/**
	 * Eine Handeingabe-Position zu einem Auftrag hinzuf&uuml;gen</br>
	 * <p>Der Auftrag muss im Status ANGELEGT sein.</p>
	 * 
	 * @param userId  ist der beim Logon ermittelte "token" 
	 * @param orderId ist die Id des Auftrags
	 * @param positionEntry die hinzuzuf&uuml;genden Handeingabeposition
	 * @return die neu erzeugte Auftragsposition
	 * @inputWrapped com.heliumv.api.order.CreateOrderManualItemPositionEntry
	 * @returnWrapped com.heliumv.api.order.CreatedOrderPositionEntry
	 * @throws RemoteException
	 */
	CreatedOrderPositionEntry createManualIdentPosition(String userId, Integer orderId,
			CreateOrderManualItemPositionEntry positionEntry) throws RemoteException;

	/**
	 * Einen Auftrag aktivieren</br>
	 * <p>Der Auftrag wird vollst&auml;ndig durchgerechnet und der
	 * Status des Auftrags auf OFFEN gestellt, sofern dies m&ouml;glich ist.</p>
	 *
	 * <p>Der Auftrag muss im Status ANGELEGT sein. Es wird das AUFTRAG_CUD
	 * Recht ben&ouml;tigt.</p>
	 * 
	 * <p>Bitte stellen Sie sicher, dass der Auftrag alle jene Informationen 
	 * enth&auml;lt, die bei einem etwaigen Druck der Auftragsbest&auml;tigung
	 * notwendig sind. Zum Beispiel Preise in den Positionen, Steuers&auml;tze,
	 *  Konditionen usw.</p>
	 *  
	 * <p>Beim Aktivieren wird eine Belegpr&uuml;fung durchgef&uuml;hrt. Je nach
	 * gesetzten Parametern kann es verschiedenen Situation geben (Nullpreise
	 * in der Position erlaubt, steuerlose Positionen obwohl Steuer erwartet
	 * wird, steuerbehaftete Position obwohl steuerlose Positionen erwartet
	 * werden, ...) die zum Abbruch der Aktivierung f&uuml;hren, oder 
	 * Eintr&auml;ge im OrderActivationEntry hinterlassen.</p>
	 * 
	 * @param orderId ist die Id des zu aktivierenden Auftrags
	 * @param userId ist der beim Logon ermittelte "token"
	 * @param verified muss true sein um die Funktion ausf&uuml;hren zu k&ouml;nnen
	 * 
	 * @return Informationen zur Belegpr&uuml;fung
	 * 
	 * @throws RemoteException
	 * @throws NamingException
	 */
	OrderActivationEntry postActivateOrder(Integer orderId, String userId,
			Boolean verified) throws RemoteException, NamingException;

	/**
	 * &Auml;ndert die angegebene Auftragsposition auf eine Handeingabeposition</br>
	 * 
	 * <p>Der Auftrag muss im Status ANGELEGT sein.</p>
	 * 
	 * <p>Unterscheiden sich die Positionsarten der bereits bestehenden 
	 * Position und der abzu&auml;ndernden, so wird prinzipiell eine 
	 * neue Position angelegt.</p>
	 * <p>Auch wenn es sich bei der bestehenden Position um eine
	 * Handeingabeposition handelt, kann nicht davon ausgegangen werden,
	 * dass die Id der neuen Position die gleiche bleibt. Wird mit der
	 * Id der Position weitergearbeitet, ist die im Response enthaltende
	 * <code>orderPositionId</code> zu verwenden.</p>

	 * @param userId  ist der beim Logon ermittelte "token" 
	 * @param orderId ist die Id des Auftrags
	 * @param positionId ist die abzu&auml;ndernde Auftragsposition
	 * @param positionEntry die neue/abge&auml;nderte Handeingabeposition
	 * @return die ge&auml;nderte Auftragsposition
	 * 
	 * @throws RemoteException
	 */
	CreatedOrderPositionEntry updateManualItemPosition(String userId,
			Integer orderId, Integer positionId,
			CreateOrderManualItemPositionEntry positionEntry) throws RemoteException;

	/**
	 * Die Positionen eines Auftrags aufsteigend nach Artikelnummer sortieren</br>
	 *	
	 * <p>Der Auftrag muss im Status ANGELEGT sein.</p>
	 * 
	 * @param orderId ist die Id des zu sortierenden Auftrags
	 * @param userId ist der beim Logon ermittelte "token"
	 * @return Information zur Sortierung
	 * 
	 * @throws RemoteException
	 * @throws NamingException
	 */
	OrderSortedEntry postSortOrderByItemnumber(Integer orderId, 
			String userId) throws RemoteException, NamingException;
}
