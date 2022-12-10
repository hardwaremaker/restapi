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
package com.heliumv.api.item;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.rmi.RemoteException;

import javax.jcr.RepositoryException;
import javax.naming.NamingException;
import javax.ws.rs.core.Response;

import com.heliumv.api.document.DocumentInfoEntryList;
import com.heliumv.api.production.ProductionEntryList;
import com.heliumv.api.system.PropertyLayoutEntryList;
import com.lp.server.partner.service.CustomerPricelistReportDto;
import com.lp.util.EJBExceptionLP;


public interface IItemApiV11 extends IItemApi {
	/**
	 * Liefert eine Liste aller Lagerst&auml;nde dieses Artikels</br>
	 * <p>Es werden nur Lager geliefert, die einen Lagerstand > 0 haben. Es werden nur jene
	 * Lagerst&auml;nde geliefert, f&uuml;r die der Benutzer das Recht hat das jeweilige Lager zu benutzen.</p>
	 * 
	 * @param userId der angemeldete API Benutzer
	 * @param itemCnr die gesuchte Artikelnummer
	 * @param returnItemInfo mit <code>true</code> werden neben den Lagerst&auml;nden auch die Daten des
	 * betreffenden Artikels zur&uuml;ckgeliefert.
	 * @param addStockPlaceInfos (optional) mit <code>true</code> die Infos der zugewiesenen Lagerpl&auml;tze liefern
	 * @param returnAllStocks (optional) mit <code>true</code> auch jene Lager liefern, die einen Lagerstand = 0 haben, 
	 *  f&uuml;r die der Benutzer aber das Recht hat das jeweilige Lager zu benutzen.
	 * @return eine (leere) Liste von Lagerst&auml;nden
	 */
	StockAmountEntryList getStockAmountList(
			String userId, 
			String itemCnr, 
			Boolean returnItemInfo, 
			Boolean addStockPlaceInfos,
			Boolean returnAllStocks) ;
	
	/**
	 * Einen Artikel anhand seiner Artikelnummer ermitteln</br>
	 * 
	 * @param userId des bei HELIUM V angemeldeten API Benutzer
	 * @param cnr (optional) die gesuchte Artikelnummer
	 * @param serialnumber (optional) die Seriennummer des Artikels</br>
	 * <p>Eineindeutige Artikel k&ouml;nnen &uuml;ber ihre Seriennummer ermittelt werden. Dabei wird
	 * zuerst im aktuellen Lagerstand gesucht, danach in den Abgangsbuchungen. Ist die <code>cnr</code>
	 * ebenfalls angegeben, muss der Artikel der &uuml;ber die Seriennummer ermittelt wurde mit der 
	 * angegebenen Artikelnummer &uuml;bereinstimmen.</p>
	 * @param itemBarcode (optional) der EAN / Barcode des Artikels</br>
	 * @param addComments (optional) mit <code>true</code> die Artikelkommentar der Art text/html ebenfalls liefern
	 * @param addStockAmountInfos (optional) mit <code>true</code> die allgemeinen Lagerstandsinformationen liefern
	 * @param addProducerInfos (optional) mit <code>true</code> die herstellerspezifischen Infos (Artikelnummer,
	 *   Bezeichnung des Herstellers, Leadin, ...) liefern
	 * @param addStockPlaceInfos (optional) mit <code>true</code> die Infos der zugewiesenen Lagerpl&auml;tze liefern
	 * @param addDocuments (optional) mit <code>true</code> die Infos &uuml;ber die vom angemeldeten Benutzer 
	 *   sichtbaren Dokumente aus der Dokumentenablage liefern
	 * @param addCommentsMedia (optional) mit <code>true</code> die Infos &uuml;ber die Artikelkommentare liefern
	 * @return den Artikel sofern vorhanden. Gibt es den Artikel/Seriennummer nicht wird mit 
	 * StatusCode <code>NOT_FOUND (404)</code> geantwortet
	 * @throws NamingException 
	 * @throws RemoteException 
	 */
	ItemV1Entry findItemV1ByAttributes(
			String userId,
			String cnr, 
			String serialnumber,
			String itemBarcode,
			Boolean addComments, 
			Boolean addStockAmountInfos, 
			Boolean addProducerInfos,
			Boolean addStockPlaceInfos,
			Boolean addDocuments,
			Boolean addCommentsMedia) throws RemoteException, NamingException ;
	
	BigDecimal getPrice(
			String userId,
			Integer itemId,
			Integer customerId,
			BigDecimal amount,
			String unitCnr) throws NamingException, RemoteException, EJBExceptionLP ;
	
	/**
	 * Eine Liste aller Artikel ermitteln.</br>
	 * <p>Das Ergebnis kann dabei durch Filter eingeschr&auml;nkt werden</p>
	 * 
	 * @param userId des angemeldeten HELIUM V Benutzer
	 * @param limit die maximale Anzahl von zur&uuml;ckgelieferten Datens&auml;tzen
	 * @param startIndex die Index-Nummer desjenigen Satzes mit dem begonnen werden soll
	 * @param filterCnr die (optionale) Artikelnummer nach der die Suche eingeschr&auml;nkt werden soll
	 * @param filterTextSearch der (optionale) Text der die Suche einschr&auml;nkt 
	 * @param filterDeliveryCnr auf die (optionale) Lieferantennr. bzw Bezeichnung einschr&auml;nken
	 * @param filterItemGroupClass auf die (optionale) Artikelgruppe bzw. Artikelklasse einschr&auml;nken
	 * @param filterItemReferenceNr auf die (optionale) Artikelreferenznummer einschr&auml;nken
	 * @param filterWithHidden mit <code>true</code> werden auch versteckte Artikel in die Suche einbezogen
	 * @param filterItemgroupId die (optionale) IId der Artikelgruppe in der die Artikel gesucht werden. Die
	 *  cnr wird aus der angegebenen iid ermittelt und dann auch f&uuml;r die Artikelklasse verwendet
	 * @param filterCustomerItemCnr die (optionale) Kundenartikelnummer nach der gesucht werden kann 
	 * @param filterShopGroupIds die (optionale) auch leere Liste von Shopgruppen IIds in denen die Artikel
	 *   gesucht werden sollen
	 * @param orderBy definiert die Sortierung der Daten. Es k&ouml;nnen durch Komma getrennt mehrere
	 *  Datenfelder angegeben werden. Getrennt durch Leerzeichen kann "asc" (aufsteigend) oder auch "desc" (absteigend)
	 *  sortiert werden. "cnr desc" oder auch "cnr asc, customerItemCnr desc", oder auch "cnr, customerItemCnr desc"
	 * @return eine (leere) Liste von <code>ItemEntry</code>
	 */
	ItemEntryList getItemsList(String userId, Integer limit, Integer startIndex, 
			String filterCnr, String filterTextSearch, String filterDeliveryCnr, String filterItemGroupClass,
			String filterItemReferenceNr,
			Boolean filterWithHidden, Integer filterItemgroupId, String filterCustomerItemCnr,
			String filterShopGroupIds, String orderBy) throws RemoteException, NamingException, EJBExceptionLP, Exception ;
	
	/**
	 * Eine Liste aller Artikelgruppen ermitteln</br>
	 * <p>Diese Methode ist f&uuml;r die Verwendung in Listboxen/Comboboxen gedacht</p>
	 * 
	 * @param userId des angemeldeten HELIUM V Benutzer
	 * @return eine (leere) Liste aller Artikelgruppen
	 */
	ItemGroupEntryList getItemGroupsList(
			String userId) throws RemoteException, NamingException, EJBExceptionLP, Exception ;
	
	/**
	 * Eine Liste aller Shopgruppen ermitteln</br>
	 * 
	 * @param userId des angemeldeten HELIUM V Benutzer
	 * @param limit (optional) die maximale Anzahl von zu liefernden Shopgruppen. Bei 0 werden alle geliefert
	 * @param startIndex (optional) die Index-Nummer desjenigen Satzes mit dem begonnen werden soll
	 * @return eine (leere) Liste von <code>ShopGroupEntry</code>
	 * 
	 * @throws RemoteException
	 * @throws NamingException
	 * @throws EJBExceptionLP
	 * @throws Exception
	 */
	ShopGroupEntryList getShopGroupsList(
			String userId,
			Integer limit,
			Integer startIndex) throws RemoteException, NamingException, EJBExceptionLP, Exception ;

	/**
	 * Verteilt die Restmenge der Charge zu gleichen Teilen auf ihre Verwendung in den Losen
	 * und liefert als Ergebnis eine Liste der betroffenen Lose.</br>
	 * Die Lose in der Liste sind nur mit ihrer <code>id</code> und <code>cnr</code> bef&uuml;llt.
	 * F&uuml;r Detailinformation der Lose sind die Ressourcen der <code>ProductionApi</code> zu verwenden.</br> 
	 * Ist die Chargennummer nicht mehr auf Lager, so wird eine leere Liste retourniert.
	 * 
	 * @param userId des angemeldeten HELIUM V Benutzer
	 * @param itemId Id des Artikels
	 * @param chargennr Chargennummer des Artikels
	 * @return eine Liste von Losen, auf die die Charge aufgeteilt wurde
	 * @throws RemoteException
	 */
	ProductionEntryList discardRemainingChargenumber(
			String userId, 
			Integer itemId,
			String chargennr) throws RemoteException;

	/**
	 * Das Etikett des Artikels ausdrucken</br>
	 * <p>Das Artikeletikett wird auf dem Drucker ausgedruckt, der dem Arbeitsplatz (des mobilen Endger&auml;tes)
	 * zugewiesen worden ist</p>
	 * 
	 * @param userId  des angemeldeten HELIUM V Benutzers
	 * @param itemId Id des Artikels
	 * @param identity (optional) Serien-/Chargennummer des Artikels
	 * @param identityStockId (optional) Id des Lagers der Serien-/Chargennummer, f&uuml;r den Andruck des Lagerstandes der Serien-/Chargennummer
	 * @param amount (optional) Menge des Artikels
	 * @param copies (optional) Anzahl der Exemplare. Wenn nicht angegeben wird 1 Exemplar gedruckt
	 * @return itemId sofern das Artikeletikett gedruckt werden konnte
	 * @throws RemoteException
	 */
	Integer printLabel(
			String userId, 
			Integer itemId, 
			String identity, 
			Integer identityStockId, 
			BigDecimal amount,
			Integer copies) throws RemoteException;

	/**
	 * Ein Dokument anhand seiner Cnr eines Artikels ermitteln
	 * 
	 * @param userId des angemeldeten HELIUM V Benutzers
	 * @param itemId Id des Artikels
	 * @param documentCnr Id des Dokumentes
	 * @return das Dokument sofern vorhanden. Gibt es das Dokument nicht wird mit StatusCode <code>NOT_FOUND (404)</code> geantwortet
	 * @throws RepositoryException
	 * @throws IOException
	 */
	Response getDocument(
			String userId, 
			Integer itemId, 
			String documentCnr) throws RepositoryException, IOException;

	/**
	 * Eine Liste aller Dokumente des Artikels aus der Dokumentenablage ermitteln.</br>
	 * Es werden alle Dokumente angef&uuml;hrt, die f&uuml;r der angemeldeten Benutzer sichtbar sind.
	 * Ein Element der Liste ent&auml;hlt aber nicht die tats&auml;chlichen Daten des Dokuments, sondern
	 * nur Metainformation. 
	 * 
	 * @param userId des angemeldeten HELIUM V Benutzers
	 * @param itemId Id des Artikels
	 * @return eine (leere) Liste mit Infos &uuml;ber die gefundenen Dokumente
	 * @throws RepositoryException
	 * @throws IOException
	 */
	DocumentInfoEntryList getDocuments(
			String userId, 
			Integer itemId) throws RepositoryException, IOException;

	ItemCommentMediaInfoEntryList getCommentsMedia(
			String userId, 
			Integer itemId) throws RemoteException, NamingException;

	Response getCommentMedia(
			String userId, 
			Integer itemCommentId, 
			Integer itemId) throws RemoteException, NamingException, UnsupportedEncodingException;

	/**
	 * Eine Liste aller Beschreibungen der Eigenschaften des Artikels.</br>
	 * Der Artikel kann optional &uuml;ber die Id oder Artikelnummer bestimmt werden.
	 * 
	 * @param userId des angemeldeten HELIUM V Benutzers
	 * @param itemId (optional) Id des Artikels. Ist auch die Artikelnummer angegeben hat die Id Priorit&auml;t
	 * @param itemCnr (optional) Artikelnummer
	 * @return eine (leere) Liste aller Beschreibungen der Eigenschaften des Artikels
	 * @throws RemoteException
	 */
	PropertyLayoutEntryList getPropertyLayouts(
			String userId,
			Integer itemId,
			String itemCnr) throws RemoteException;
	
	/**
	 * Liefert die angeforderte Eigenschaft eines Artikels.</br>
	 * Der Artikel kann optional &uuml;ber die Id oder Artikelnummer bestimmt werden.
	 * 
	 * @param propertyId Id der Eigenschaft des Artikels
	 * @param userId des angemeldeten HELIUM V Benutzers
	 * @param itemId (optional) Id des Artikels. Ist auch die Artikelnummer angegeben hat die Id Priorit&auml;t
	 * @param itemCnr (optional) Artikelnummer
	 * @return Eigenschaft des Artikels sofern vorhanden, ansonsten wird mit StatusCode <code>NOT_FOUND (404)</code> geantwortet
	 * @throws RemoteException
	 */
	ItemPropertyEntry getItemProperty(
			Integer propertyId,
			String userId,
			Integer itemId,
			String itemCnr) throws RemoteException;
	
	/**
	 * Erzeugt eine neue Eigenschaft eines Artikels &uuml;ber die Id der Beschreibung.</br>
	 * Ist zur Id der Beschreibung bereits eine entsprechende Eigenschaft existent, so wird mit StatusCode <code>CONFLICT (409)</code> geantwortet.</br>
	 * Eine Ausnahme bilden Eigenschaften, die mit ihrem Inhalt dem Defaultwert ihrer Beschreibung entsprechen. 
	 * In diesem Fall wird diese Eigenschaft aktualisiert.
	 * 
	 * @param userId des angemeldeten HELIUM V Benutzers
	 * @param createEntry die Daten zur Anlage @see {#link {@link CreateItemPropertyEntry}
	 * @return die neu angelegte Eigenschaft
	 * @throws RemoteException
	 */
	ItemPropertyEntry createItemProperty(
			String userId,
			CreateItemPropertyEntry createEntry) throws RemoteException;
	
	/**
	 * Aktualisiert die Daten einer Eigenschaft eines Artikels.</br>
	 * Die Eigenschaft muss bereits existieren, sonst wird mit StatusCode <code>NOT_FOUND (404)</code> geantwortet
	 * 
	 * @param userId des angemeldeten HELIUM V Benutzers
	 * @param updateEntry die Daten zur Aktualisierung @see {{@link UpdateItemPropertyEntry}
	 * @return die aktualisierte Eigenschaft
	 * @throws RemoteException
	 */
	ItemPropertyEntry updateItemProperty(
			String userId,
			UpdateItemPropertyEntry updateEntry) throws RemoteException;

	/**
	 * Liefert eine (leere) Liste aller Artikel die die angegebene Herstellernummer exakt enthalten</br>
	 * <p>Es werden alle Artikel geliefert, die die angegebene Herstellernummer enthalten, egal
	 * um welchen Hersteller es sich handelt. Beginnt die hier angegebene Nummer mit einem
	 * bekannten (Barcode)Lead-In, wird dieser vor der Suche entfernt. </p>
	 * @param userId des angemeldeten HELIUM V Benutzers
	 * @param barcode die Herstellernummer
	 * @param addComments (optional) mit <code>true</code> die Artikelkommentar der Art text/html ebenfalls liefern
	 * @param addStockAmountInfos (optional) mit <code>true</code> die allgemeinen Lagerstandsinformationen liefern
	 * @param addProducerInfos (optional) mit <code>true</code> die herstellerspezifischen Infos (Artikelnummer,
	 *   Bezeichnung des Herstellers, Leadin, ...) liefern
	 * @param addStockPlaceInfos (optional) mit <code>true</code> die Infos der zugewiesenen Lagerpl&auml;tze liefern
	 * @param addDocuments (optional) mit <code>true</code> die Infos &uuml;ber die vom angemeldeten Benutzer 
	 *   sichtbaren Dokumente aus der Dokumentenablage liefern
	 * @param addCommentsMedia (optional) mit <code>true</code> die Infos &uuml;ber die Artikelkommentare liefern
	 * @return eine (leere) Liste jener Artikel, die in den Bestelldaten die Herstellernummer enthalten
	 * @throws RemoteException
	 * @throws NamingException
	 */
	ItemV1EntryList findItemV1ByManufacturerBarcode(String userId, String barcode, Boolean addComments,
			Boolean addStockAmountInfos, Boolean addProducerInfos, Boolean addStockPlaceInfos, Boolean addDocuments,
			Boolean addCommentsMedia) throws RemoteException, NamingException;

	/**
	 * Die Artikelpreisliste ermitteln</br>
	 * <p>Abh&auml;ngig vom Umfang der Artikel bzw. der Preisliste kann dieser Aufruf auch 
	 * mehrere Sekunden oder sogar Minuten dauern. Die R&uuml;ckgabe kann auch mehrere MB gro&szlig; sein.
	 * 
	 * @param userId ist der Token der durch die Anmeldung (<code>login</code>) erhalten wurde
	 * @param pricelistCnr wird die Preislistenkennung angegeben, wird der Preis entsprechend der
	 *   Preisliste ausgegeben. Wird keine Kennung angegeben, wird die "Verkaufspreisbasis" des
	 *   Artikels ausgegeben
	 * @param filterItemgroupCnr optional nur Artikel mit dieser Artikelgruppe selektieren
	 * @param filterItemgroupId optional nur Artikel mit dieser ArtikelgruppenId selektieren
	 * @param filterItemclassCnr optional nur Artikel mit dieser Artikelklassenummer
	 * @param filterItemclassId optional nur Artikel mit dieser ArtikelklassenId
	 * @param filterItemRangeFrom optional nur Artikel ab dieser Artikelnummer
	 * @param filterItemRangeTo optional nur Artikel bis (einschlie&szlig;lich) zu dieser Artikelnummer
	 * @param filterShopgroupCnr optional nur Artikel mit dieser Shopgruppekennung (oder Kindern dieser
	 *   Shopgruppe) selektieren
	 * @param filterShopgroupId optional nur Artikel mit dieser ShopgruppenId (oder Kindern dieser
	 *   ShopgruppenId) selektieren
	 * @param filterValidityDate optional mit dem angebenen Preisg&uuml;ltigkeitsdatum im ISO8601 Format 
	 * (Beispiel: JJJJ-MM-TT"T"hh:mm ... 2013-12-31T14:00Z oder 2013-12-31T14:59+01:00)
	 * @param filterWithHidden mit <code>true</code> werden auch versteckte Artikel in die Preisliste aufgenommen
	 * @return die (leere) Liste aller Artikel entsprechend der Kriterien mit dem Preis
	 * @throws RemoteException
	 */
	CustomerPricelistReportDto getPriceList(String userId, String pricelistCnr, String filterItemgroupCnr,
			Integer filterItemgroupId, String filterItemclassCnr, Integer filterItemclassId, String filterItemRangeFrom,
			String filterItemRangeTo, String filterShopgroupCnr, Integer filterShopgroupId, String filterValidityDate,
			Boolean filterWithHidden) throws RemoteException;

	/**
	 * Eine (leere) Liste aller verf&uuml;gbaren Preislisten
	 * @param userId ist der Token der durch die Anmeldung (<code>login</code>) erhalten wurde
	 * @return eine (leere) Liste aller verf&uuml;gbaren Preislisten
	 * @throws RemoteException 
	 */
	PriceListEntryList getPriceLists(String userId) throws RemoteException;
}
