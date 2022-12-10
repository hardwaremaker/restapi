package com.heliumv.api.invoice;

import java.math.BigDecimal;
import java.rmi.RemoteException;

import javax.naming.NamingException;

import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;

public interface IInvoiceApi {
	/**
	 * Eine Liste aller - den Filterkriterien entsprechenden - Rechnungen ermitteln.
	 * 
	 * @param userId ist der Token der durch die Anmeldung (<code>login</code>) erhalten wurde
	 * @param limit limit die Anzahl der zu liefernden Datens&auml;tze. Default sind 50
	 * @param startIndex startIndex der x-te Eintrag ab dem die Datens&auml;tze geliefert werden sollen
	 * @param filterCnr schr&auml;nkt die Ausgabe auf Rechnungen ein, die diesen Teil der Rechnungsnummer beinhalten  
	 * @param filterCustomer schr&auml;nkt die Ausgabe auf Rechnungen ein, die f&uuml;r diesen Kundennamen ausgestellt wurden
	 * @param filterProject nur Rechnungen die diese Projekt- bzw. Bestellnummer beinhalten werden ausgegeben
	 * @param textSearch Rechnungen (und deren Positionen) die diesen Text beinhalten werden ausgegeben
	 * @param statisticsAddress Einschr&auml;nkung auf die Statistikadresse
	 * @param filterWithOpen mit <code>true</code> werden nur offene Rechnungen geliefert. Ohne Angabe bzw. mit <code>false</code>
	 *   werden alle Rechnungen ausgegeben
	 * @param addCustomerDetail mit <code>true</code> wird zus&auml;tzlich der <code>CustomerDetailEntry</code> des Kunden
	 *   ermittelt und ausgegeben
	 * @return eine (leere) Liste aller Rechnungen die den Filterkriterien entsprechen
	 * @throws RemoteException
	 * @throws NamingException
	 */
	InvoiceEntryList getInvoices(
			String userId,
			Integer limit,
			Integer startIndex, 
			String filterCnr,
			String filterCustomer,
			String filterProject,		
			String textSearch,		
			String statisticsAddress,					
			Boolean filterWithOpen,
			Boolean addCustomerDetail) throws RemoteException, NamingException ;

	/**
	 * Erstellt eine Zahlung f&uuml;r die angegebene Rechnung
	 * 
	 * @param userId  ist der Token der durch die Anmeldung (<code>login</code>) erhalten wurde
	 * @param invoiceId ist die Id der Rechnung 
	 * @param paymentEntry sind die Daten der Zahlung
	 * @return
	 * @throws RemoteException
	 */
	Integer createCashPayment(
			String userId,
			Integer invoiceId,
			InvoiceCashPaymentPostEntry paymentEntry) throws RemoteException ;

	/**
	 * Speichert einen (zus&auml;tzlichen) Zahlungsbeleg zu einer bestehenden Zahlung ab.
	 * <p>Die maximale Upload-Gr&ouml;&szlig;e ist derzeit auf 500KiB gestellt</p>
	 * 
	 * @param userId  ist der Token der durch die Anmeldung (<code>login</code>) erhalten wurde
	 * @param invoiceId ist die Id der Rechnung 
	 * @param paymentId ist die Id der Zahlung 
	 * @param cashCnr ist die eindeutige Nummer der Kassa
	 * @param body ist der <code>multipart/form-data</code> des Requests, der den/die Zahlungsbelege als "Datei" enth&auml;lt
	 * @return 0 wenn der Dateianhang nicht zur Zahlung hinzugef&uuml;gt werden konnte, ansonsten > 0
	 * @throws NamingException
	 * @throws RemoteException
	 */
	Integer attachDocumentToCashPayment(
		String userId,
		Integer invoiceId,
		Integer paymentId,			
		String cashCnr,
		MultipartBody body) throws NamingException, RemoteException ;
	

	/**
	 * L&ouml;scht eine zuvor durchgef&uuml;hrte Zahlung</br>
	 * <p>Wird der Zahlbetrag f&uuml;r das angegebene Zahldatum f&uuml;r die angegebene Id der
	 * Rechnung nicht gefunden, wird der Statuscode 404 zur&uuml;ckgeliefert. Konnte die Zahlung
	 * storniert werden, wird 204 geantwortet.</p>
	 * @param userId ist der Token der durch die Anmeldung (<code>login</code>) erhalten wurde
	 * @param invoiceId ist die Id der Rechnung 
	 * @param year das Jahr der Zahlung (beispielsweise: 2016)
	 * @param month das Monat der Zahlung (beginnend bei 1, beispielsweise: 7 f&uumlr Juli)
	 * @param day der Tag der Zahlung (beginnend bei 1, beispielsweise: 13 f&uuml;r den 13ten) 
	 * @param amount der zu l&ouml;schende Zahlbetrag
	 * @throws RemoteException
	 */
	void removeCashPayment(
			String userId,
			Integer invoiceId,
			Integer year,
			Integer month,
			Integer day,			
			BigDecimal amount) throws RemoteException;
	
}
