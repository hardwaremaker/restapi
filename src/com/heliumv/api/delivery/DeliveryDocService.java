package com.heliumv.api.delivery;

import java.rmi.RemoteException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.api.document.DocumentMetadata;
import com.heliumv.api.document.IDocumentCategoryService;
import com.heliumv.factory.IKundeCall;
import com.heliumv.factory.ILieferscheinCall;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.system.jcr.service.JCRDocDto;
import com.lp.server.system.jcr.service.JCRDocFac;
import com.lp.server.system.jcr.service.docnode.DocNodeLieferschein;
import com.lp.server.system.jcr.service.docnode.DocPath;

public class DeliveryDocService implements IDocumentCategoryService {
	private static Logger log = LoggerFactory.getLogger(DeliveryDocService.class) ;
	
	@Autowired
	private ILieferscheinCall lieferscheinCall;
	@Autowired
	private IKundeCall kundeCall;

	@Override
	public JCRDocDto setupDoc(Integer id, String cnr, DocumentMetadata metadata) throws RemoteException {
		LieferscheinDto lieferscheinDto = findLieferschein(id, cnr);
		if (lieferscheinDto == null) return null;
		
		JCRDocDto docDto = new JCRDocDto();
		docDto.setDocPath(getDocPath(lieferscheinDto));
		docDto.setsBelegnummer(lieferscheinDto.getCNr());
		docDto.setsTable("LIEFERSCHEIN");
		docDto.setsRow(lieferscheinDto.getIId().toString());
		docDto.setlSicherheitsstufe(metadata.getSecurityLevel() != null ?
				metadata.getSecurityLevel() : JCRDocFac.SECURITY_ARCHIV);
		
		KundeDto kundeDto = kundeCall.kundeFindByPrimaryKeyOhneExc(lieferscheinDto.getKundeIIdLieferadresse());
		if (kundeDto != null) {
			docDto.setlPartner(kundeDto.getPartnerIId());
		}
		
		return docDto;
	}

	private LieferscheinDto findLieferschein(Integer id, String cnr) throws RemoteException {
		if (id != null) {
			LieferscheinDto lieferscheinDto = lieferscheinCall.lieferscheinFindByPrimaryKeyOhneExc(id);
			if (lieferscheinDto == null) log.error("Could not find delivery with id = " + id);
			return lieferscheinDto;
		}
		
		if (cnr != null) {
			LieferscheinDto lieferscheinDto = lieferscheinCall.lieferscheinFindByCNr(cnr);
			if (lieferscheinDto == null) log.error("Could not find delivery with cnr = " + cnr);
			return lieferscheinDto;
		}
		
		return null;
	}

	@Override
	public DocPath getDocPath(Integer id, String cnr) throws RemoteException {
		LieferscheinDto lieferscheinDto = findLieferschein(id, cnr);
		if (lieferscheinDto == null) return null;
		
		return getDocPath(lieferscheinDto);
	}
	
	private DocPath getDocPath(LieferscheinDto lieferscheinDto) {
		return new DocPath(new DocNodeLieferschein(lieferscheinDto));
	}
}
