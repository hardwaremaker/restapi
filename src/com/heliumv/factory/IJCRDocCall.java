package com.heliumv.factory;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;

import javax.jcr.RepositoryException;
import javax.naming.NamingException;

import com.lp.server.system.jcr.service.DokumentbelegartDto;
import com.lp.server.system.jcr.service.DokumentgruppierungDto;
import com.lp.server.system.jcr.service.JCRDocDto;
import com.lp.server.system.jcr.service.JCRRepoInfo;
import com.lp.server.system.jcr.service.docnode.DocNodeBase;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.util.EJBExceptionLP;

public interface IJCRDocCall {
	void addNewDocumentOrNewVersionOfDocument(JCRDocDto jcrDocDto) throws NamingException, RemoteException;

	void addNewDocumentOrNewVersionOfDocumentWithinTransaction(
			JCRDocDto jcrDocDto) throws NamingException, RemoteException;

	JCRRepoInfo checkIfNodeExists(DocPath docPath) throws NamingException, RemoteException, EJBExceptionLP;

	DokumentbelegartDto dokumentbelegartFindByCnrMandantCnrOhneExc(String cnr);

	DokumentgruppierungDto dokumentgruppierungFindByCnrMandantCnrOhneExc(String cnr);

	List<DocNodeBase> getDocNodeChildrenFromNode(DocPath docPath) throws RepositoryException, IOException;

	JCRDocDto getData(JCRDocDto jcrDocDto);
}
