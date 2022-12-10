package com.heliumv.api.document;

import java.io.IOException;
import java.rmi.RemoteException;

import javax.jcr.RepositoryException;
import javax.naming.NamingException;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;

public interface IDocumentService {

	void createDoc(IDocumentCategoryService docService, Integer id, String cnr, DocumentMetadata metadata, 
			Attachment attachment) throws RemoteException, NamingException;
	
	DocumentInfoEntryList getDocs(IDocumentCategoryService docService, Integer id, String cnr) throws RemoteException, RepositoryException, IOException;

	RawDocument getDoc(IDocumentCategoryService docService, Integer id, String cnr, String documentCnr) throws RepositoryException, IOException;
}
