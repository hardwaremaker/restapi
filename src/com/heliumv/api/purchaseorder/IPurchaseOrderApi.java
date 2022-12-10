package com.heliumv.api.purchaseorder;

import java.rmi.RemoteException;

import javax.naming.NamingException;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.QueryParam;

import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;

import com.heliumv.api.BaseApi.Param;
import com.heliumv.api.BaseApi.ParamInHeader;
import com.heliumv.api.purchaseorder.PurchaseOrderApi.ApiFilter;
import com.lp.util.EJBExceptionLP;

public interface IPurchaseOrderApi {
	/** 
	 * Eine (offene) Bestellung ermitteln</br>
	 * <p>Ist die Bestellnummer - entweder die Bestellnummer oder die Id - bekannt, wird
	 * nur diese Bestellnummer betrachtet. Ist die Bestellnummer unbekannt, aber daf&uuml;r
	 * die Lieferantennummer k&ouml;nnen dar&uuml;ber die Bestellungen ermittelt werden.</p>
	 * <p>Die <code>purchaseOrderId</code> wird immer als erstes herangezogen, sofern sie 
	 * angegeben wird. Fehlt sie, aber die <code>Bestellnummer</code> ist angegeben, so wird
	 * diese verwendet. Fehlt auch diese, wird die Lieferantennummer herangezogen. F&uuml;r
	 * die Lieferantennummer k&ouml;nnen mehrere (offene) Bestellungen vorhanden sein.</p>
	 * 
	 * @param headerToken  ist der (optionale) beim Logon ermittelte "token" im Header
	 * @param userId  ist der (optionale) beim Logon ermittelte "token"
	 * @param purchaseOrderCnr ist die (optionale) gesuchte HELIUM V Bestellnummer 
	 * @param purchaseOrderId ist die (optionale) Id der gesuchtes HELIUM V Bestellung
	 * @param supplierCnr ist die (optionale) Lieferantennummer 
	 * @param filterReceiptPossible mit <code>true</code> werden nur Bestellungen  
	 * ermittelt, die im Status OFFEN, BESTAETIGT oder TEILERLEDIGT haben. Mit <code>false</code>
	 * werden alle Bestellungen ermittelt, die der purchaseOrderId, purchaseOrderCnr oder 
	 * supplierCnr entsprechen.
	 * @param addSupplierDetail (optional) mit <code>true</code> wird zus&auml;tzlich der <code>SupplierDetailEntry</code> 
	 *   des Lieferanten ermittelt und ausgegeben
	 * @return
	 * @throws RemoteException
	 */
	PurchaseOrderEntryList getPurchaseOrder(
			String headerToken,
			String userId,
			String purchaseOrderCnr,
			Integer purchaseOrderId,
			String supplierCnr,
			Boolean filterReceiptPossible,
			Boolean addSupplierDetail
	) throws RemoteException;

	/**
	 * Information &uuml;ber einen etwaigen Wareneingang zu einer Bestellung ermitteln</br>
	 * <p>Es muss entweder die <code>purchaseOrderCnr</code> oder die <code>purchaseOrderId</code>
	 * angegeben werden. Existiert bereits ein Wareneingang mit der angegebenen Lieferscheinnummer, 
	 * so werden die Daten zum Wareneingang &uuml;bermittelt, ansonsten wird ein 404-Status 
	 * zur&uuml;ckgeliefert.</p>
	 * @param headerToken  ist der (optionale) beim Logon ermittelte "token" im Header
	 * @param userId  ist der (optionale) beim Logon ermittelte "token"
	 * @param purchaseOrderCnr ist die (optionale) Bestellnummer
	 * @param purchaseOrderId ist die (optionale) Id der Bestellnummer.
	 * @param deliveryslipCnr ist die Lieferantenlieferscheinnummer f&uuml;r die der Wareneingang
	 * ermittelt werden soll
	 * @param filterReceiptPossible
	 * @return
	 * @throws RemoteException
	 */
	GoodsReceiptEntry getGoodsReceipt(
			String headerToken,
			String userId,
			String purchaseOrderCnr,
			Integer purchaseOrderId,
			String deliveryslipCnr,		
			Boolean filterReceiptPossible
	) throws RemoteException;

	/**
	 * Artikelinformationen zu einer Bestellposition ermitteln</br>
	 * <p>Generell gilt, dass die Artikelnummer zu in einer der Bestellpositionen 
	 * in der angegebenen Bestellungen geh&ouml;ren muss. Zuerst wird die Artikelnummer 
	 * als Artikelnummer des Artikellieferanten interpretiert. Ist sie dort unbekannt, 
	 * wird sie als Herstellerartikelnummer interpretiert. Ist sie auch dort unbekannt,
	 * wird sie als Artikelnummer ("Artikel CNR") interpretiert.</p>
	 * @param headerToken ist der (optionale) beim Logon ermittelte "token" im Header
	 * @param userId ist der (optionale) beim Logon ermittelte "token"
	 * @param purchaseOrderId ist die Id der Bestellung
	 * @param itemCnr die gesuchte Artikelnummer
	 * @param filterReceiptPossible true wenn nur noch Positionen in der Bestellung 
	 * ber&uuml;cksichtigt werden sollen, die noch offen sind.
	 * @return die Bestellposition passend zum gew&uuml;nschten Artikel
	 * @throws RemoteException
	 */
	PurchaseOrderPositionEntry getItemInfo(
			String headerToken,
			String userId,
			Integer purchaseOrderId,
			String itemCnr,
			Boolean filterReceiptPossible
	) throws RemoteException;

	/**
	 * Eine Wareneingangsposition erstellen
	 * 
	 * @param headerToken ist der (optionale) beim Logon ermittelte "token" im Header
	 * @param userId ist der (optionale) beim Logon ermittelte "token"
	 * @param filterReceiptPossible wenn <code>true</code> wird der Wareneingang nur 
	 * dann positiv verarbeitet, wenn f&uuml;r die entsprechende Bestellposition tats&auml;chlich
	 * noch eine offene Menge existiert. Mit <code>false</code> kann eine beabsichtigte
	 * &Uuml;berlieferung durchgef&uuml;hrt werden.
	 * @return Informationen &uuml;ber die neu erzeugte/abge&auml;nderte 
	 * Wareneingangsposition
	 * @throws RemoteException
	 */
	CreatedGoodsReceiptPositionEntry createGoodsReceipt(
			String headerToken,
			String userId,
			 Boolean filterReceiptPossible,
			CreateGoodsReceiptPositionEntry createEntry
	) throws RemoteException;
	
	/**
	 * Liefert die Liste aller Wareneingangspositionen
	 * 
	 * @param goodsreceiptId ist die Id des Wareneingangs
	 * @param headerToken ist der (optionale) beim Logon ermittelte "token" im Header
	 * @param userId ist der (optionale) beim Logon ermittelte "token"
	 * @return die Liste aller Wareneingangspositionen
	 * @throws RemoteException
	 */
	GoodsReceiptPositionEntryList getGoodsReceiptPositions(
			Integer goodsreceiptId,
			String headerToken,
			String userId
	) throws RemoteException;
	
	/**
	 * Das Wareneingangspositions-Etikett drucken</br>
	 * <p>Wird keine Identity (Chargen- oder Seriennummer) angegeben, werden
	 * f&uuml;r alle Positionen des Wareneingangs die Etiketten gedruckt. 
	 * Ansonsten nur f&uuml;r jene Positionen auf die die Idenity zutrifft.</p>
	 * 
	 * @param goodsreceiptId ist die Id des Wareneingangs
	 * @param goodsreceiptPositionId ist die Id der Wareneingangsposition
	 * @param headerToken ist der (optionale) beim Logon ermittelte "token" im Header
	 * @param userId ist der (optionale) beim Logon ermittelte "token"
	 * @param identity ist die (optionale) Chargennummer f&uuml;r die das/die 
	 *  Etiketten gedruckt werden sollen.
	 * @return die WareneingangspositionId
	 * @throws RemoteException
	 */
	Integer printGoodsReceiptPosition(
			Integer goodsreceiptId,
			Integer goodsreceiptPositionId,
			String headerToken,
			String userId,
			String identity
	) throws RemoteException;
	
	/**
	 * Eine Liste aller Bestellpositionen (ber&uuml;cksichtigt werden nur Ident-Artikel oder Handeingaben)
	 * 
	 * @param headerToken ist der (optionale) beim Logon ermittelte "token" im Header
	 * @param userId userId ist der (optionale) beim Logon ermittelte "token"
	 * @param purchaseOrderId ist die Id der Bestellung
	 * @param filterReceiptPossible true wenn nur noch Positionen in der Bestellung 
	 * ber&uuml;cksichtigt werden sollen, die noch offen sind.
	 * @return eine (leere) Liste aller Bestellpositionen
	 * @throws RemoteException
	 */
	PurchaseOrderPositionEntryList getListItemInfo(
			@HeaderParam(ParamInHeader.TOKEN) String headerToken,
			@QueryParam(Param.USERID) String userId,
			@QueryParam(Param.PURCHASEORDERID) Integer purchaseOrderId,
			@QueryParam(ApiFilter.RECEIPTPOSSIBLE) Boolean filterReceiptPossible
	) throws RemoteException;

	/**
	 * Eine Liste aller Bestellungen gem&auml;&szlig; den Filterkriterien
	 * 
	 * @param userId userId ist der beim Logon ermittelte "token"
	 * @param limit (optional) die maximale Anzahl von Eintr&auml;gen die ermittelt werden sollen
	 * @param startIndex (optional) der StartIndex
	 * @param filterCnr (optional) schr&auml;nkt die Ausgabe auf Bestellungen ein, die diesen Teil der Bestellnummer beinhalten 
	 * @param filterStatus (optional) die zu filternden Status (aus <code>PurchaseOrderStatus</code>) getrennt durch Komma
	 * @param filterSupplier (optional) schr&auml;nkt die Ausgabe auf Bestellungen ein, die f&uuml;r diesen Lieferantennamen ausgestellt wurden 
	 * @param filterItemCnr (optional) schr&auml;nkt die Ausgabe auf Bestellungen ein, die Positionen mit diesem Artikel beinhalten
	 * @return eine (leere) Liste von Bestellungen
	 * @throws RemoteException
	 * @throws NamingException
	 */
	PurchaseOrderEntryList getPurchaseOrderList(
			String userId,
			Integer limit, 
			Integer startIndex, 
			String filterCnr,
			String filterStatus, 
			String filterSupplier,
			String filterItemCnr) throws RemoteException, NamingException;

	/**
	 * Eine Liste aller Positionen eines Bestellvorschlags gem&auml;&szlig; den Filterkriterien
	 * 
	 * @param userId ist der beim Logon ermittelte "token"
	 * @param limit (optional) die maximale Anzahl von Eintr&auml;gen die ermittelt werden sollen
	 * @param startIndex (optional) der StartIndex
	 * @param filterItemCnr (optional) schr&auml;nkt die Ausgabe auf diese zu filternde Artikelnummer ein
	 * @param filterNoted (optional) wenn <code>true</code> werden nur als vorgemerkt markierte Positionen geliefert,
	 *  wenn <code>false</code> nur alle nicht vorgemerkten Positionen
	 * @return eine (leere) Liste von Bestellvorschlagspositionen
	 * @throws RemoteException
	 * @throws NamingException
	 */
	PurchaseOrderProposalPositionEntryList getPurchaseOrderProposalPositionList(
			String userId, 
			Integer limit,
			Integer startIndex, 
			String filterItemCnr, 
			Boolean filterNoted) throws RemoteException, NamingException;

	/**
	 * Liefert eine Position des Bestellvorschlags
	 * 
	 * @param userId ist der beim Logon ermittelte "token"
	 * @param proposalPositionId Id der angefragten Position des Bestellvorschlags
	 * @return Position des Bestellvorschlags
	 */
	PurchaseOrderProposalPositionEntry getPurchaseOrderProposalPosition(
			String userId, 
			Integer proposalPositionId);

	/**
	 * Eine Position des Bestellvorschlags erstellen</br>
	 * Wenn diese Position vorgemerkt werden soll (&uuml;ber die Property <code>CreatePurchaseOrderProposalPositionEntry.noted</code>)
	 * dann darf es noch keine vorgemerkte Bestellvorschlagsposition des Artikels geben, f&uuml;r den eine neue Position erzeugt werden soll.
	 * Ist dies der Fall wird der Request mit einem <code>EXPECTATION_FAILED (417)</code> abgelehnt. Im Header <code>X_HV_ADDITIONAL_ERROR_VALUE</code>
	 * in dieser Response ist dabei die Id der bereits existenten Bestellvorschlagsposition enthalten.</br>
	 * Wird die Position nicht vorgemerkt, ist eine beliebige Anzahl an Positionen des jeweiligen Artikels erlaubt.
	 * 
	 * @param userId ist der beim Logon ermittelte "token"
	 * @param createEntry die neuen Bestellvorschlagpositionsdaten
	 * @return die neu erzeugten Bestellvorschlagpositionsdaten
	 * @throws EJBExceptionLP
	 * @throws RemoteException
	 */
	CreatedPurchaseOrderProposalPositionEntry createPurchaseOrderProposalPosition(
			String userId,
			CreatePurchaseOrderProposalPositionEntry createEntry) throws EJBExceptionLP, RemoteException;

	/**
	 * Eine bestehende Position des Bestellvorschlags ab&auml;ndern
	 * 
	 * @param userId ist der beim Logon ermittelte "token"
	 * @param proposalPositionId Id der abzu&auml;ndernden Position des Bestellvorschlags
	 * @param updateEntry die neuen Bestellvorschlagspositiondaten
	 * @throws EJBExceptionLP
	 * @throws RemoteException
	 */
	void updatePurchaseOrderProposalPosition(
			String userId, 
			Integer proposalPositionId,
			UpdatePurchaseOrderProposalPositionEntry updateEntry) throws EJBExceptionLP, RemoteException;

	/**
	 * Eine bestehende Position des Bestellvorschlags l&ouml;schen
	 * 
	 * @param userId ist der beim Logon ermittelte "token"
	 * @param proposalPositionId Id der abzu&auml;ndernden Position des Bestellvorschlags
	 * @throws EJBExceptionLP
	 * @throws RemoteException
	 */
	void removePurchaseOrderProposalPosition(
			String userId, 
			Integer proposalPositionId) throws EJBExceptionLP, RemoteException;


	/**
	 * Legt Dateien in die Dokumentenablage eines Wareneingangs ab.</br>
	 * <p>Die maximale Upload-Gr&ouml;&szlig;e ist derzeit auf 6500KiB gestellt</p>
	 * 
	 * <p>Entweder <code>headerToken</code> oder <code>userId</code> muss
	 * angegeben werden. </p>
	 * 
	 * @param goodsreceiptId Id des Wareneingangs
	 * @param headerToken  ist der (optionale) beim Logon ermittelte "token" im Header
	 * @param userId  ist der (optionale) beim Logon ermittelte "token"
	 * @param type ist die Belegart (optional, "Auftragsbest&auml;tigung", "E-Mail", "kopiertes Dokument", 
	 *  ... vom HELIUM V Anwender definierte Belegarten)
	 * @param keywords sind die Schlagworte des Dokuments (optional)
	 * @param grouping ist die Gruppierung (optional, "Archiv", "Dokumente", "Gruppe-0", ... vom HELIUM V Anwender definierte Gruppen)
	 * @param securitylevel ist die Sicherheitsstufe (optional, 0 = keine, 1 = niedrig, 2 = mittel, 3 = hoch, 99 = Archiv)
	 * @param body ist der <code>multipart/form-data</code> des Requests, der den/die Dokumente enth&auml;lt
	 * @throws RemoteException
	 * @throws NamingException
	 */
	void createDocument(Integer goodsreceiptId, String headerToken, String userId, String type, String keywords,
			String grouping, Long securitylevel, MultipartBody body) throws RemoteException, NamingException;


	/**
	 * Legt Dateien in die Dokumentenablage eines Wareneingangs ab.</br>
	 * 
	 * <p>Die maximale Upload-Gr&ouml;&szlig;e ist derzeit auf 6500KiB gestellt</p>
	 * 
	 * <p>Entweder <code>headerToken</code> oder <code>userId</code> muss
	 * angegeben werden. </p>
	 * 
	 * @param headerToken ist der (optionale) beim Logon ermittelte "token" im Header
	 * @param userId ist der (optionale) beim Logon ermittelte "token"
	 * @param purchaseOrderId ist die Id der Bestellung 
	 * @param deliveryslipCnr ist die Lieferscheinnummer des Lieferanten. Gibt es bereits
	 *   einen Wareneingang mit dieser Lieferscheinnummer, wird dieser verwendet. Ansonsten
	 *   wird ein Wareneingang mit dieser Lieferscheinnummer angelegt.
	 * @param filterReceiptPossible Bei <code>true</code> werden die Dokumente zur Bestellung  
	 * hinzugef&uul;gt sofern sie sich im Status OFFEN, BESTAETIGT oder TEILERLEDIGT befindet. Mit <code>false</code>
	 * wird die Verarbeitung abgebrochen, wenn sich die Bestellung nicht im obigen Status befindet
	 * @param type ist die Belegart (optional, "Auftragsbest&auml;tigung", "E-Mail", "kopiertes Dokument", 
	 *  ... vom HELIUM V Anwender definierte Belegarten)
	 * @param keywords  sind die Schlagworte des Dokuments (optional)
	 * @param grouping ist die Gruppierung (optional, "Archiv", "Dokumente", "Gruppe-0", ... vom HELIUM V Anwender definierte Gruppen)
	 * @param securitylevel  ist die Sicherheitsstufe (optional, 0 = keine, 1 = niedrig, 2 = mittel, 3 = hoch, 99 = Archiv)
	 * @param body ist der <code>multipart/form-data</code> des Requests, der den/die Dokumente enth&auml;lt
	 * @throws RemoteException
	 * @throws NamingException
	 */
	void createDocumentViaDeliverySlipCnr(String headerToken, String userId, Integer purchaseOrderId,
			String deliveryslipCnr, Boolean filterReceiptPossible, String type, String keywords, String grouping,
			Long securitylevel, MultipartBody body) throws RemoteException, NamingException;

	/**
	 * Ermittelt die Liste aller Wareneinga&auml;nge einer Bestellung</br>
	 * 
	 * <p>Entweder <code>headerToken</code> oder <code>userId</code> muss
	 * angegeben werden. </p>
	 * 
	 * <p>Entweder <code>purchaseOrderCnr</code> oder <code>purchaseOrderId</code>
	 * muss angegeben werden. </p>
	 * 
	 * @param headerToken ist der (optionale) beim Logon ermittelte "token" im Header
	 * @param userId ist der (optionale) beim Logon ermittelte "token"
	 * @param purchaseOrderCnr die Bestellnummer (optional)
	 * @param purchaseOrderId die Id der Bestellnummer (optional)
	 * @param deliveryslipCnr die (optionale) externe Lieferscheinnummer auf die 
	 *    eingeschr&auml;nkt werden soll. Wird sie nicht angegeben, werden alle 
	 *    Wareneing&auml;nge die f&uuml;r die Bestellung bekannt sind &uuml;bermittelt
	 * @param filterReceiptPossible mit <code>true</code> werden nur Bestellungen  
	 * ermittelt, die im Status OFFEN, BESTAETIGT oder TEILERLEDIGT haben. Mit <code>false</code>
	 * ist der Status der Bestellung egal
	 * @return eine (leere) Liste aller Wareneing&auml;nge f&uuml;r die Bestellung
	 * @throws RemoteException
	 */
	GoodsReceiptEntryList getGoodsReceipts(String headerToken, String userId, String purchaseOrderCnr,
			Integer purchaseOrderId, String deliveryslipCnr, Boolean filterReceiptPossible) throws RemoteException;
}
