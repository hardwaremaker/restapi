package com.heliumv.api.project;

import java.rmi.RemoteException;

import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.api.HvNamingException;
import com.heliumv.api.HvValidateBadRequest;
import com.heliumv.api.HvValidateNotFound;
import com.heliumv.api.document.DocumentMetadata;
import com.heliumv.api.document.IDocumentCategoryService;
import com.heliumv.factory.IProjektCall;
import com.heliumv.factory.IProjektServiceCall;
import com.heliumv.tools.StringHelper;
import com.lp.server.projekt.service.BereichDto;
import com.lp.server.projekt.service.ProjektDto;
import com.lp.server.system.jcr.service.JCRDocDto;
import com.lp.server.system.jcr.service.JCRDocFac;
import com.lp.server.system.jcr.service.docnode.DocNodeProjekt;
import com.lp.server.system.jcr.service.docnode.DocPath;

public class ProjectDocService implements IDocumentCategoryService {

	@Autowired
	private IProjektCall projektCall;
	@Autowired
	private IProjektServiceCall projektServiceCall;
	
	@Override
	public JCRDocDto setupDoc(Integer id, String cnr, DocumentMetadata metadata) throws RemoteException {
		try {
			ProjektDto projektDto = findProject(id, cnr);
			if(projektDto == null) return null;
			
			JCRDocDto docDto = new JCRDocDto();
			DocPath docPath = getDocPath(projektDto);
			docDto.setDocPath(docPath);

			docDto.setsBelegnummer(projektDto.getCNr());
			docDto.setsTable("PROJEKT");
			docDto.setsRow(projektDto.getIId().toString());
			docDto.setlSicherheitsstufe(metadata.getSecurityLevel() != null ? 
					metadata.getSecurityLevel() : JCRDocFac.SECURITY_ARCHIV);
			docDto.setlPartner(projektDto.getPartnerIId());
			return docDto;		
		} catch(NamingException e) {
			throw new HvNamingException(e);
		}
	}

	@Override
	public DocPath getDocPath(Integer id, String cnr) throws RemoteException {
		try {
			ProjektDto projektDto = findProject(id, cnr);
			HvValidateNotFound.notNull(projektDto, "projectid", id);
			return getDocPath(projektDto);			
		} catch(NamingException e) {
			throw new HvNamingException(e);		
		}
	}

	private ProjektDto findProject(Integer id, String cnr) throws RemoteException, NamingException {
		if(id != null) {
			ProjektDto projektDto = projektCall.projektFindByPrimaryKeyOhneExc(id);
			HvValidateNotFound.notNull(projektDto, "projectid", id);
			return projektDto;
		}
		
		if(StringHelper.hasContent(cnr)) {
			ProjektDto projektDto = projektCall.projektFindByCNrOhneExc(cnr);
			HvValidateNotFound.notNull(projektDto, "cnr", cnr);
			return projektDto;
		}
		
		HvValidateBadRequest.notEmpty(cnr, "cnr");
		return null;
	}
	
	private DocPath getDocPath(ProjektDto projectDto) {
		projectDto.getBereichIId();
		BereichDto bereichDto = projektServiceCall.bereichFindByPrimaryKey(projectDto.getBereichIId());
		return new DocPath(new DocNodeProjekt(projectDto, bereichDto));
	}
}
