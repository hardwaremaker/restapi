package com.heliumv.api.document;

import java.rmi.RemoteException;

import com.lp.server.system.jcr.service.JCRDocDto;
import com.lp.server.system.jcr.service.docnode.DocPath;

public interface IDocumentCategoryService {

	JCRDocDto setupDoc(Integer id, String cnr, DocumentMetadata metadata) throws RemoteException;

	DocPath getDocPath(Integer id, String cnr) throws RemoteException;
}
