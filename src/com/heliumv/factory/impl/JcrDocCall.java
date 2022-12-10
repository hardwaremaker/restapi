package com.heliumv.factory.impl;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;

import javax.jcr.RepositoryException;
import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.factory.BaseCall;
import com.heliumv.factory.IGlobalInfo;
import com.heliumv.factory.IJCRDocCall;
import com.lp.server.system.jcr.ejb.DokumentbelegartPK;
import com.lp.server.system.jcr.ejb.DokumentgruppierungPK;
import com.lp.server.system.jcr.service.DokumentbelegartDto;
import com.lp.server.system.jcr.service.DokumentgruppierungDto;
import com.lp.server.system.jcr.service.JCRDocDto;
import com.lp.server.system.jcr.service.JCRDocFac;
import com.lp.server.system.jcr.service.JCRRepoInfo;
import com.lp.server.system.jcr.service.docnode.DocNodeBase;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.util.EJBExceptionLP;

public class JcrDocCall extends BaseCall<JCRDocFac> implements IJCRDocCall {
	@Autowired
	private IGlobalInfo globalInfo ;
	
	public JcrDocCall() {
		super(JCRDocFac.class) ;
	}
	
	@Override
	public void addNewDocumentOrNewVersionOfDocument(JCRDocDto jcrDocDto) throws NamingException, RemoteException {
		getFac().addNewDocumentOrNewVersionOfDocument(jcrDocDto, globalInfo.getTheClientDto());
	}
	
	@Override
	public void addNewDocumentOrNewVersionOfDocumentWithinTransaction(
			JCRDocDto jcrDocDto) throws NamingException, RemoteException {
		getFac().addNewDocumentOrNewVersionOfDocumentWithinTransaction(jcrDocDto, globalInfo.getTheClientDto());
	}

	@Override
	public JCRRepoInfo checkIfNodeExists(DocPath docPath) throws NamingException,
			RemoteException, EJBExceptionLP {
		return getFac().checkIfNodeExists(docPath);
	}
	
	@Override
	public DokumentbelegartDto dokumentbelegartFindByCnrMandantCnrOhneExc(String cnr) {
		return getFac().dokumentbelegartfindbyPrimaryKeyOhneExc(new DokumentbelegartPK(globalInfo.getMandant(), cnr));
	}
	
	@Override
	public DokumentgruppierungDto dokumentgruppierungFindByCnrMandantCnrOhneExc(String cnr) {
		return getFac().dokumentgruppierungfindbyPrimaryKeyOhneExc(new DokumentgruppierungPK(globalInfo.getMandant(), cnr));
	}
	
	@Override
	public List<DocNodeBase> getDocNodeChildrenFromNode(DocPath docPath) throws RepositoryException, IOException {
		return getFac().getDocNodeChildrenFromNode(docPath, globalInfo.getTheClientDto());
	}
	
	@Override
	public JCRDocDto getData(JCRDocDto jcrDocDto) {
		return getFac().getData(jcrDocDto);
	}
}
