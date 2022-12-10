package com.heliumv.api.production;

import java.rmi.RemoteException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.api.document.DocumentMetadata;
import com.heliumv.api.document.IDocumentCategoryService;
import com.heliumv.factory.IFertigungCall;
import com.heliumv.factory.IKundeCall;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.system.jcr.service.JCRDocDto;
import com.lp.server.system.jcr.service.JCRDocFac;
import com.lp.server.system.jcr.service.docnode.DocNodeLos;
import com.lp.server.system.jcr.service.docnode.DocPath;

public class ProductionDocService implements IDocumentCategoryService {
	private static Logger log = LoggerFactory.getLogger(ProductionDocService.class) ;

	@Autowired
	private IFertigungCall fertigungCall;
	@Autowired
	private IKundeCall kundeCall;
	
	@Override
	public JCRDocDto setupDoc(Integer id, String cnr, DocumentMetadata metadata) throws RemoteException {
		LosDto losDto = findLos(id, cnr);
		if (losDto == null) return null;
		
		JCRDocDto docDto = new JCRDocDto();
		DocPath docPath = getDocPath(losDto);
		docDto.setDocPath(docPath);

		docDto.setsBelegnummer(losDto.getCNr());
		docDto.setsTable("LOS");
		docDto.setsRow(losDto.getIId().toString());
		docDto.setlSicherheitsstufe(metadata.getSecurityLevel() != null ? 
				metadata.getSecurityLevel() : JCRDocFac.SECURITY_ARCHIV);
		
		KundeDto kundeDto = kundeCall.kundeFindByPrimaryKeyOhneExc(losDto.getKundeIId());
		if (kundeDto != null) {
			docDto.setlPartner(kundeDto.getPartnerIId());
		}
		
		return docDto;
	}

	private LosDto findLos(Integer id, String cnr) {
		if (id != null) {
			LosDto losDto = fertigungCall.losFindByPrimaryKeyOhneExc(id);
			if (losDto == null) log.error("Could not find lot with id = " + id);
			return losDto;
		}
		
		if (cnr != null) {
			LosDto losDto = fertigungCall.losFindByCNrMandantCNrOhneExc(cnr);
			if (losDto == null) log.error("Could not find lot with cnr = " + cnr);
			return losDto;
		}
		
		log.warn("Could not find lot cause of id and cnr is null");
		return null;
	}

	@Override
	public DocPath getDocPath(Integer id, String cnr) {
		LosDto losDto = findLos(id, cnr);
		if (losDto == null) return null;
		
		return getDocPath(losDto);
	}
	
	private DocPath getDocPath(LosDto losDto) {
		return new DocPath(new DocNodeLos(losDto));
	}
}
