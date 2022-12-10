package com.heliumv.api.document;

import java.io.IOException;
import java.rmi.RemoteException;

import javax.jcr.RepositoryException;
import javax.naming.NamingException;

import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;

public interface IDocumentApi {

	/**
	 * Speichert Dokumente zu bestehenden Belegen ab, die &uuml;ber die Kategorie des Belegs und
	 * der Id oder Nummer des Belegs identifiziert werden. Es muss entweder die Id oder die Nummer
	 * des Belegs &uuml;bermittelt werden.
	 * <p>Die maximale Upload-Gr&ouml;&szlig;e ist derzeit auf 500KiB gestellt</p>
	 * 
	 * @param userId ist der Token der durch die Anmeldung (<code>login</code>) erhalten wurde
	 * @param id ist die Id des Belegs (optional)
	 * @param cnr ist die Nummer des Belegs (optional)
	 * @param category ist die Kategorie des Belegs
	 * @param type ist die Belegart (optional)
	 * @param keywords sind die Schlagworte des Dokuments (optional)
	 * @param grouping ist die Gruppierung (optional)
	 * @param securitylevel ist die Sicherheitsstufe (optional)
	 * @param body ist der <code>multipart/form-data</code> des Requests, der den/die Dokumente enth&auml;lt
	 * @throws RemoteException
	 * @throws NamingException
	 */
	void createDocument(			
			String userId,
			Integer id,			
			String cnr,
			DocumentCategory category,
			String type,
			String keywords,
			String grouping,
			Long securitylevel,
			MultipartBody body) throws RemoteException, NamingException;

	/**
	 * Eine Liste aller Dokumente eines Belegs aus der Dokumentenablage ermitteln.</br>
	 * Es werden alle Dokumente angef&uuml;hrt, die f&uuml;r der angemeldeten Benutzer sichtbar sind.
	 * Ein Element der Liste ent&auml;hlt aber nicht die tats&auml;chlichen Daten des Dokuments, sondern
	 * nur Metainformation. </br>
	 * Der Beleg wird &uuml;ber die Id oder Nummer des Belegs identifiziert. Es muss entweder die Id
	 * oder die Nummer des Belegs &uuml;bermittelt werden.
	 * 
	 * @param userId ist der Token der durch die Anmeldung (<code>login</code>) erhalten wurde
	 * @param id ist die Id des Belegs (optional)
	 * @param cnr ist die Nummer des Belegs (optional)
	 * @param category ist die Kategorie des Belegs
	 * @return eine (leere) Liste mit Infos &uuml;ber die gefundenen Dokumente
	 * @throws RepositoryException
	 * @throws IOException
	 */
	DocumentInfoEntryList getDocuments(
			String userId, 
			Integer id, 
			String cnr, 
			DocumentCategory category) throws RepositoryException, IOException;
}
