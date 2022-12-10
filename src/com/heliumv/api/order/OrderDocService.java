package com.heliumv.api.order;

import java.rmi.RemoteException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.api.document.DocumentMetadata;
import com.heliumv.api.document.IDocumentCategoryService;
import com.heliumv.factory.IAuftragCall;
import com.heliumv.factory.IKundeCall;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.system.jcr.service.JCRDocDto;
import com.lp.server.system.jcr.service.JCRDocFac;
import com.lp.server.system.jcr.service.docnode.DocNodeAuftrag;
import com.lp.server.system.jcr.service.docnode.DocPath;

public class OrderDocService implements IDocumentCategoryService {
	private static Logger log = LoggerFactory.getLogger(OrderDocService.class) ;
	
	@Autowired
	private IAuftragCall auftragCall;
	@Autowired
	private IKundeCall kundeCall;

	@Override
	public JCRDocDto setupDoc(Integer id, String cnr, DocumentMetadata metadata)
			throws RemoteException {
		AuftragDto auftragDto = findAuftrag(id, cnr);
		if (auftragDto == null) return null;
		
		JCRDocDto docDto = new JCRDocDto();
		DocPath docPath = getDocPath(auftragDto);
		docDto.setDocPath(docPath);

		docDto.setsBelegnummer(auftragDto.getCNr());
		docDto.setsTable("AUFTRAG");
		docDto.setsRow(auftragDto.getIId().toString());
		docDto.setlSicherheitsstufe(metadata.getSecurityLevel() != null ? 
				metadata.getSecurityLevel() : JCRDocFac.SECURITY_ARCHIV);
		
		KundeDto kundeDto = kundeCall.kundeFindByPrimaryKeyOhneExc(auftragDto.getKundeIIdAuftragsadresse());
		if (kundeDto != null) {
			docDto.setlPartner(kundeDto.getPartnerIId());
		}
		
		return docDto;
	}

	private AuftragDto findAuftrag(Integer id, String cnr) throws RemoteException {
		if (id != null) {
			AuftragDto auftragDto = auftragCall.auftragFindByPrimaryKeyOhneExc(id);
			if (auftragDto == null) log.error("Could not find order with id = " + id);
			return auftragDto;
		}
		
		if (cnr != null) {
			AuftragDto auftragDto = auftragCall.auftragFindByCnr(cnr);
			if (auftragDto == null) log.error("Could not find order with cnr = " + cnr);
			return auftragDto;
		}
		
		return null;
	}

	@Override
	public DocPath getDocPath(Integer id, String cnr) throws RemoteException {
		AuftragDto auftragDto = findAuftrag(id, cnr);
		if (auftragDto == null) return null;

		return getDocPath(auftragDto);
	}
	
	private DocPath getDocPath(AuftragDto auftragDto) {
		return new DocPath(new DocNodeAuftrag(auftragDto));
	}
}
