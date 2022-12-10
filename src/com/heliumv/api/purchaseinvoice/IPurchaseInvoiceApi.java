package com.heliumv.api.purchaseinvoice;

import java.rmi.RemoteException;

import javax.naming.NamingException;

import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;

public interface IPurchaseInvoiceApi {

	/**
	 * Legt Dateien in die Dokumentenablage eines Eingangsrechnung ab.
	 * <p>Die maximale Upload-Gr&ouml;&szlig;e ist derzeit auf 500KiB gestellt</p>
	 *  
	 * @param purchaseinvoiceId ist die Id der Eingangsrechnung
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
			Integer purchaseinvoiceId, 
			String userId, 
			String type,
			String keywords, 
			String grouping, 
			Long securitylevel,
			MultipartBody body) throws RemoteException, NamingException;
	
	void createMobileInvoices(
			String userId,
			PostPurchaseInvoices invoices) throws RemoteException;	
}
