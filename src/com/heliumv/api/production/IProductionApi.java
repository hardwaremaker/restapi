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
package com.heliumv.api.production;

import java.io.IOException;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.List;

import javax.naming.NamingException;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;

import com.heliumv.api.machine.MachineEntryList;
import com.lp.util.EJBExceptionLP;

public interface IProductionApi {
	/**
	 * Die Liste der den Filterkriterien entsprechenden Lose</p>
	 * 
	 * @param userId
	 *            ist der beim Logon ermittelte "token"
	 * @param limit
	 *            die maximale Anzahl von Eintr&auml;gen die ermittelt werden
	 *            sollen
	 * @param startIndex
	 *            der StartIndex
	 * @param filterCnr
	 *            die zu filternde Losnummer
	 * @param filterCustomer
	 *            der zu filternde Kundenname
	 * @param filterProject
	 *            der zu filternde Projektname
	 * @param filterItemCnr
	 *            die zu filternde Artikelnummer
	 * @param filterProductionGroup die zu filternde Fertigungsgruppe
	 * @return eine (leere) Liste von Losen entsprechend den Filterkriterien
	 */
	List<ProductionEntry> getProductions(String userId, Integer limit,
			Integer startIndex, String filterCnr, String filterCustomer,
			String filterProject, String filterItemCnr,
			String filterProductionGroup) throws NamingException, RemoteException, Throwable;

	/**
	 * Eine Materialentnahme bzw. -r&uuml;ckgabe nachtr&auml;glich durchf&uuml;hren</br>
	 * <p>
	 * Im Gegensatz zum HELIUM V Client d&uuml;rfen nur Materialien
	 * zur&uuml;ckgegeben werden, die auch tats&auml;chlich ausgegeben worden
	 * sind.
	 * </p>
	 * <p>ist materialEntry.targetMaterialId gesetzt, wird eine Entnahme/R&uuml;ckgabe auf
	 * die damit angegebene Sollposition durchgef&uuml;hrt, ansonsten wird diese ohne
	 * ohne Bezug zu einer Sollposition durchgef&uuml;hrt.
	 * </p>
	 * 
	 * @param headerUserId
	 *            ist der beim Logon ermittelte Token. (optional)
	 * @param materialEntry
	 *            sind die Daten zur Materialentnahme wie Artikelnummer, Menge,
	 *            Serien- bzw. Chargennummer und Lager
	 * @param userId
	 *            ist der beim Logon ermittelte "token"
	 * @throws RemoteException
	 * @throws NamingException
	 */
	void bucheMaterialNachtraeglichVomLagerAb(String headerUserId,
			MaterialWithdrawalEntry materialEntry, String userId) throws NamingException, RemoteException;

	/**
	 * Eine Liste aller offenen Arbeitsg&auml;nge ermitteln</br>
	 * 
	 * @param userId  ist der beim Logon ermittelte "token"
	 * @param limit die maximale Anzahl von Eintr&auml;gen
	 * @param startIndex
	 * @param filterCnr
	 * @param filterCustomer
	 * @param filterItemCnr
	 * @param filterMachinegroup
	 * @param filterNextWorkstep liefert bei Aktivierung nur den n&auml;chsten nicht fertigen Arbeitsgang
	 * @param filterInProductionOnly liefert bei Aktivierung nur offene Arbeitsg&auml;nge aus Losen, die
	 * sich in Produktion befinden
	 * @return eine (leere) Liste aller offenen Arbeitsg&auml;nge
	 * @throws RemoteException
	 * @throws NamingException
	 * @throws EJBExceptionLP
	 */
	OpenWorkEntryList getOpenWorkEntries(String userId, Integer limit,
			Integer startIndex, String filterCnr, String filterCustomer,
			String filterItemCnr,
			Long startDateMs,
			Long endDateMs, 
			Integer filterMachinegroup, 
			Boolean filterNextWorkstep,
			Boolean filterInProductionOnly) throws RemoteException, NamingException, EJBExceptionLP;

	OpenWorkEntryList getOpenWorkEntriesImpl(Integer limit,
			Integer startIndex, Long startDateMs, Long endDateMs, Integer filterProductiongroupId, Boolean filterPlanningView, 
			Integer filterMachinegroup, Boolean filterNextWorkstep, Boolean filterInProductionOnly) 
					throws NamingException, RemoteException ;
	
	/**
	 * Einen offen Arbeitsgang modifizieren</br>
	 * <p>Es kann die Resource (Maschine), die Startzeit und der Tagesversatz abge&auml;ndert werden.
	 * 
	 * @param headerUserId ist der (optionale) beim Logon ermittelte "Token"
	 * @param updateEntry
	 * @param userId ist der beim Logon ermittelte "Token". Entweder headerUserId oder userId muss gesetzt sein
	 * @throws NamingException
	 * @throws RemoteException
	 * @throws EJBExceptionLP
	 */
	void updateOpenWorkEntry(
			String headerUserId,
			OpenWorkUpdateEntry updateEntry,
			String userId
	) throws NamingException, RemoteException, EJBExceptionLP ;
	
	void updateOpenWorkEntryList(
			String headerUserId,
			OpenWorkUpdateEntryList updateList,
			String userId
	) throws NamingException, RemoteException, EJBExceptionLP ;		
	
	/**
	 * Ein Los anhand seiner Losnummer ermitteln
	 * 
	 * @param userId ist der beim Logon ermittelte "Token"
	 * @param id die (optional) Id des Loses. Ist auch die cnr angegeben, hat die id Priorit&auml;t
	 * @param cnr die (optional) Losnummer
	 * @param addPartlistItem (optional) mit <code>true</code> den zugrundeliegenden Artikel der verwendeten St&uuml;ckliste ebenfalls liefern
	 * @param addCustomer (optional) mit <code>true</code> den Kunden dieses Loses ebenfalls liefern
	 * @param addAdditionalStatus (optional) mit <code>true</code> die Loszusatzstatus mitliefern
	 * @param addWorksteps (optional) mit <code>true</code> die Arbeitsg&auml;nge der St&uuml;ckliste mitliefern
	 * @param addTargetMaterials (optional) mit <code>true</code> die Lossollmaterialen mitliefern
	 * @param addDeliveredAmount (optional) mit <code>true</code> die abgelieferte Menge liefern
	 * @param addProductionWorksteps (optional) mit <code>true</code> die die Arbeitsg&auml;nge des Loses liefern
	 * @param addDocuments (optional) mit <code>true</code> die Infos &uuml;ber die vom angemeldeten Benutzer 
	 *   sichtbaren Dokumente aus der Dokumentenablage liefern
	 * @param addCommentsMedia (optional) mit <code>true</code> die Infos &uuml;ber die Artikelkommentare des St&uuml;cklistenartikels liefern.
	 *  Es werden dabei aber nur jene Artikelkommentare ber&uuml;cksichtigt, die der Fertigung zugewiesen wurden.
	 * @param addTestPlan (optional) mit <code>true</code> den Lospr&uuml;fplan des angegebenen Loses mitliefern
	 * @return das Los sofern vorhanden. Gibt es das Los nicht wird mit StatusCode <code>NOT_FOUND (404)</code> geantwortet
	 */
	ProductionEntry findProductionByAttributes(String userId, Integer id, String cnr,
			Boolean addPartlistItem, Boolean addCustomer, Boolean addAdditionalStatus,
			Boolean addWorksteps, Boolean addTargetMaterials, Boolean addDeliveredAmount,
			Boolean addProductionWorksteps, Boolean addDocuments,
			Boolean addCommentsMedia, Boolean addTestPlan);
	
	/**
	 * Ermittelt die Lossollmaterialien eines Loses
	 * 
	 * @param productionid die Id des Loses
	 * @param userId ist der beim Logon ermittelte "Token"
	 * @param addStockPlaceInfos (optional) mit <code>true</code> die Infos der zugewiesenen Lagerpl&auml;tze liefern.
	 * Ber&uuml;cksichtigt werden dabei nur jene L&auml;ger, die im betreffenden Los zur Loslagerentnahme definiert sind
	 * und f&uuml;r die der Benutzer das Recht hat, das jeweilige Lager zu benutzen.
	 * @return eine (leere) Liste der Lossollmaterialien des betreffenden Loses
	 * @throws RemoteException
	 */
	ProductionTargetMaterialEntryList getTargetMaterials(
			Integer productionid, 
			String userId,
			Boolean addStockPlaceInfos) throws RemoteException ;
	
	/**
	 * Ermittelt die Lossollmaterialien des Artikels f&uuml;r das Los
	 * 
	 * @param productionid die Id des Loses
	 * @param itemid die Id des gesuchten Artikels
	 * @param userId ist der beim Logon ermittelte "Token"
	 * @param addStockPlaceInfos (optional) mit <code>true</code> die Infos der zugewiesenen Lagerpl&auml;tze liefern.
	 * Ber&uuml;cksichtigt werden dabei nur jene L&auml;ger, die im betreffenden Los zur Loslagerentnahme definiert sind
	 * und f&uuml;r die der Benutzer das Recht hat, das jeweilige Lager zu benutzen. 
	 * @return eine (leere) Liste der Lossollmaterialien f&uuml;r den gesuchten Artikel
	 * @throws RemoteException
	 */
	ProductionTargetMaterialEntryList getTargetMaterialsForItemId(
			Integer productionid, 
			Integer itemid, 
			String userId,
			Boolean addStockPlaceInfos) throws RemoteException;

	
	/**
	 * Den Losproduktionsbeginn oder (auch) Losproduktionsende auf neue Datumswerte setzen
	 * 
	 * @param productionid die Id des zu &auml;ndernden Loses
	 * @param userId ist der beim Logon ermittelte "Token"
	 * @param startTimeMs (optionaler) neuer Beginntermin. Mindestens einer der beiden
	 *   Termine muss angegeben werden. Wird startTimeMs nicht angegeben, so wird der 
	 *   aktuelle Beginntermin verwendet
	 * @param finishTimeMs (optionaler) neuer Endetermin. Mindestens einer der beiden 
	 *   Termine muss angegeben werden. Wird finishTimeMs nicht angegeben, so wird der
	 *   aktuelle Endetermin verwendet
	 * @throws RemoteException
	 * @throws EJBExceptionLP
	 */
	void putMoveProductionDate(
			Integer productionid,
			String userId,
			Long startTimeMs,
			Long finishTimeMs) throws RemoteException;
	
	/**
	 * Einen Fertigungsbegleitschein eines Loses als JRPrint erhalten.
	 * 
	 * @param productionId das zu druckende Los
	 * @param userId ist der beim Logon ermittelte "Token"
	 * @return 
	 */
	Response printProductionSupplyNote(Integer productionId, String userId) throws RemoteException;
	
	/**
	 * Erzeugt eine Losablieferung des angebenen Loses
	 * 
	 * @param productionId Id des Loses
	 * @param deliveryPostEntry Datenstruktur der Losablieferung
	 * @throws RemoteException
	 * @throws NamingException
	 */
	Integer createDelivery(
			Integer productionId,
			DeliveryPostEntry deliveryPostEntry) throws RemoteException, NamingException;
	
	/**
	 * Pru&uuml;fergebnisse einer Losablieferung erzeugen
	 * 
	 * @param testResultPostEntry Datenstruktur der Pr&uuml;fergebnisse
	 */
	void createTestResults(
			TestResultPostEntry testResultPostEntry);
	
	/**
	 * Ermittelt den Lospr&uuml;fplan des angegebenen Loses
	 * 
	 * @param userId ist der beim Logon ermittelte "Token"
	 * @param productionId Id des Loses
	 * @return eine (leere) Liste des Lospr&uuml;fplans
	 * @throws RemoteException
	 * @throws EJBExceptionLP
	 */
	TestPlanEntryList getTestPlan(
			String userId,
			Integer productionId) throws RemoteException, EJBExceptionLP;
	
	/**
	 * Ein Verpackungsetikett eines Loses als JRPrint erhalten.
	 * 
	 * @param productionId das Los des zu druckenden Verpackungsetiketts
	 * @param userId ist der beim Logon ermittelte "Token"
	 * @return
	 * @throws RemoteException
	 * @throws NamingException
	 */
	Response printPackingLabel(
			Integer productionId,
			String userId) throws RemoteException, NamingException;

	/**
	 * Etiketten eines Loses als JRPrint erhalten.
	 * 
	 * @param productionId das Los des zu druckenden Verpackungsetiketts
	 * @param userId ist der beim Logon ermittelte "Token"
	 * @param copies Anzahl der Exemplare
	 * @param amount Menge
	 * @param reportType (optional) Name einer Reportvariante des Losetiketts
	 * @return
	 * @throws RemoteException
	 * @throws IOException 
	 */
	Response printLabel(
			Integer productionId,
			String userId,
			Integer copies,
			BigDecimal amount,
			String reportType) throws RemoteException, IOException;
	
	/**
	 * Ermittelt und liefert eine Liste von Maschinen, die f&uuml;r den Losarbeitsgang
	 * eingesetzt werden d&uuml;rfen
	 * 
	 * @param productionId die Id des Loses des Arbeitsgangs
	 * @param productionWorkstepId Id des Arbeitsgangs des Loses
	 * @param userId ist der beim Logon ermittelte "Token"
	 * @return Liste von Maschinen, die f&uuml;r diesen Arbeitsgang eingesetzt werden d&uuml;rfen
	 * @throws RemoteException
	 * @throws EJBExceptionLP
	 * @throws NamingException
	 */
	MachineEntryList getAvailableMachinesForProductionWorkstep(
			Integer productionId,
			Integer productionWorkstepId,
			String userId) throws RemoteException, EJBExceptionLP, NamingException;
	
	/**
	 * Die Maschine eines Losarbeitsgangs setzen. Die vorher gesetzte Maschine wird
	 * - sofern vorhanden - gestoppt.
	 * 
	 * @param productionId die Id des Loses des Arbeitsgangs
	 * @param productionWorkstepId Id des Arbeitsgangs des Loses
	 * @param userId ist der beim Logon ermittelte "Token"
	 * @param machineId die Id der neu zu setzenden Maschine des Losarbeitsgangs
	 * @throws RemoteException
	 * @throws EJBExceptionLP
	 * @throws NamingException 
	 */
	void updateMachineOfProductionWorkstep(
			Integer productionId,
			Integer productionWorkstepId,
			String userId,
			Integer machineId) throws RemoteException, EJBExceptionLP, NamingException;

	/**
	 * Legt Dateien in die Dokumentenablage eines Loses ab.
	 * <p>Die maximale Upload-Gr&ouml;&szlig;e ist derzeit auf 500KiB gestellt</p>
	 *  
	 * @param productionId ist die Id des Loses
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
			Integer productionId, 
			String userId, 
			String type,
			String keywords, 
			String grouping, 
			Long securitylevel,
			MultipartBody body) throws RemoteException, NamingException;
	

	/**
	 * Ein Los ausgeben
	 * 
	 * @param userId ist der beim Logon ermittelte "Token"
	 * @param productionId ist die (optionale) Id des Loses. Ist die Id angegeben, hat sie Vorrang.
	 * @param cnr ist die (optionale) Losnummer.
	 * @param overrideLockWarning true wenn eventuell vorhandene Artikelsperren ignoriert werden sollen, 
	 *    false sind Sperren vorhanden, wird das Los nicht ausgegeben
	 * @param overrideMissingPartsWarning true wenn das Los auch ausgegeben werden soll, sofern 
	 * Fehlmengen entst&uuml;nden. Bei false wird das Los nur ausgegeben, wenn entweder keine Fehlmenge
	 * entst&uuml;nde, oder der Systemparameter "FEHLMENGE_ENTSTEHEN_WARNUNG" auf 0 steht.
	 * @return Informationen zur Losausgabe. Das k&ouml;nnen Hinweistexte sein, oder
	 * auch Gr&uuml;nde, warum das Losausgeben nicht erfolgreich war
	 * @throws RemoteException
	 * @throws NamingException
	 */
	EmitProductionEntry emitProduction(String userId, 
			Integer productionId, 
			String cnr, 
			Boolean overrideLockWarning,
			Boolean overrideMissingPartsWarning) throws RemoteException, NamingException;

	/**
	 * Setzt das Los in den Status "IN PRODUKTION" sofern es im AUSGEGEBEN Status ist
	 * @param userId ist der beim Logon ermittelte "Token"
	 * @param productionId ist die (optionale) Id des Loses. Ist die Id angegeben, hat sie Vorrang.
	 * @param cnr ist die (optionale) Losnummer.
	 * @return die Id des Los welches nun im Status IN PRODUKTION ist
	 * @throws RemoteException
	 * @throws NamingException
	 */
	Integer changeToProduction(String userId, 
			Integer productionId, String cnr) throws RemoteException, NamingException;

	/**
	 * Eine Los als erledigt markieren</br>
	 * <p>Das Los wird nur dann als erledigt markiert, wenn die abgelieferte Menge 
	 * zumindest der Losgr&ouml;&szlig;e entspricht. Andernfalls wird im Result 
	 * die property <code>missingAmount</code> auf true gesetzt. In diesem Falle
	 * wird das Los nicht erledigt, au&szlig;er es wird mittels <code>overrideNotCompleted</code>
	 * &uuml;bersteuert.</p>
	 * <p>Gibt es auf dem Los eine offene Zeitbuchung, wird der Name der betroffenen
	 * Person in der property processedBy angegeben. Das Los wird dann nicht erledigt.
	 * Dies gelingt erst, wenn mittels <code>ignoreProcessedBy</code> &uuml;bersteuert 
	 * wird.</p> 
	 * 
	 * @param userId ist der beim Logon ermittelte "Token"
	 * @param productionId ist die (optionale) Id des Loses. Ist die Id angegeben, hat sie Vorrang.
	 * @param cnr  ist die (optionale) Losnummer.
	 * @param ignoreProcessedBy mit true wird eine eventuell vorhandene offene Zeitbuchung
	 * auf das Los ignoriert. 
	 * @param overrideNotCompleted mit true kann die Mengenpr&uuml;fung &uuml;bersteuert
	 * werden. Eine zu geringe Losablieferung wird dann also ignoriert.
	 * @return Informationen zum erledigten Los
	 * @throws RemoteException
	 * @throws NamingException
	 */
	DoneProductionEntry doneProduction(String userId, Integer productionId, String cnr, Boolean ignoreProcessedBy,
			Boolean overrideNotCompleted) throws RemoteException, NamingException;

	PrintProductionEntry printPapers(String userId, Integer productionId, String cnr, Boolean printProductionpaper,
			String printerProductionpaper, Boolean printAnalysissheet, String printerAnalysissheet);

	/**
	 * &Auml;ndert die Losgr&ouml;&szlig;e
	 * 
	 * @param userId ist der beim Logon ermittelte "Token"
	 * @param productionId ist die (optionale) Id des Loses. Ist die Id angegeben, hat sie Vorrang.
	 * @param productionCnr ist die (optionale) Losnummer.
	 * @param productionOrderSize die neue Losgr&ouml;&szlig;e
	 * @return die Id des Loses von welchem die Losgr&ouml;&szlig;e ge&auml;ndert wurde
	 * @throws RemoteException
	 */
	Integer changeProductionOrderSize(
			String userId, 
			Integer productionId, 
			String productionCnr, 
			BigDecimal productionOrderSize) throws RemoteException;

	void createMaterialRequirements(
			MaterialRequirementPostEntry postMaterialRequirementEntry) throws Throwable;

	/**
	 * Druckt Etiketten der Fertigung &uuml;ber den Server aus.
	 * 
	 * @param userId Ist der beim Logon ermittelte "Token"
	 * @param productionId Ist die (optionale) Id des Loses. Ist die Id angegeben, hat sie Vorrang.
	 * @param cnr Ist die (optionale) Losnummer.
	 * @param printLabel (optional) mit <code>true</code> den Druck der Losetiketten ausl&ouml;sen
	 * @param printerLabel (optional) Drucker &uuml;ber den das Losetikett gedruckt werden soll.
	 * Wenn nicht angegeben wird der mobile Default-Etikettendrucker (siehe Mandanten- und Arbeitsplatzparameter) verwendet.
	 * @param printLabelQuantity (optional) die Mengenangabe des Losetiketts. Wenn nicht angegeben ist sie die Menge der letzten Losablieferung.
	 * @param printLabelCopies (optional) die Anzahl der Exemplare des Losetiketts. Wenn nicht angegeben ist sie 1.
	 * @param printLabelComment (optional) Kommentar des Losetiketts
	 * @return
	 * @throws RemoteException
	 * @throws EJBExceptionLP
	 */
	PrintProductionLabelEntry printLabels(String userId, Integer productionId, String cnr, Boolean printLabel,
			String printerLabel, BigDecimal printLabelQuantity, Integer printLabelCopies, String printLabelComment)
			throws RemoteException, EJBExceptionLP;
	
	/**
	 * Den Arbeitsgang als fertig markieren.</br>
	 * <p>Es gibt derzeit keine weitere &UUml;berpr&uuml;fung ob das Los storniert ist.</p>
	 * 
	 * @param headerUserId
	 * @param updateList
	 * @param userId
	 * @throws NamingException
	 * @throws RemoteException
	 * @throws EJBExceptionLP
	 */
	void updateDoneOpenWorkEntryList(
			String headerUserId,
			OpenWorkUpdateEntryList updateList,
			String userId
	) throws NamingException, RemoteException, EJBExceptionLP;

	/**
	 * Ein Los stoppen
	 * 
	 * @param userId ist der beim Logon ermittelte "Token"
	 * @param productionId ist die (optionale) Id des Loses. Ist die Id angegeben, hat sie Vorrang.
	 * @param productionCnr ist die (optionale) Losnummer.
	 * @return
	 * @throws RemoteException
	 */
	StopProductionEntry stopProduction(
			String userId, 
			Integer productionId, 
			String productionCnr) throws RemoteException;

}