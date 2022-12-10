package com.heliumv.api.purchaseinvoice;

import java.rmi.RemoteException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.api.document.DocumentMetadata;
import com.heliumv.api.document.IDocumentCategoryService;
import com.heliumv.factory.IEingangsrechnungCall;
import com.heliumv.factory.ILieferantCall;
import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.system.jcr.service.JCRDocDto;
import com.lp.server.system.jcr.service.JCRDocFac;
import com.lp.server.system.jcr.service.docnode.DocNodeEingangsrechnung;
import com.lp.server.system.jcr.service.docnode.DocPath;

public class PurchaseInvoiceDocService implements IDocumentCategoryService {
	private static Logger log = LoggerFactory.getLogger(PurchaseInvoiceDocService.class) ;

	@Autowired
	private IEingangsrechnungCall eingangsrechnungCall;
	@Autowired
	private ILieferantCall lieferantCall;
	
	@Override
	public JCRDocDto setupDoc(Integer id, String cnr, DocumentMetadata metadata) throws RemoteException {
		EingangsrechnungDto erDto = findEingangsrechnung(id, cnr);
		if (erDto == null) return null;
		
		JCRDocDto docDto = new JCRDocDto();
		DocPath docPath = getDocPath(erDto);
		docDto.setDocPath(docPath);
		
		docDto.setsBelegnummer(erDto.getCNr());
		docDto.setsTable("EINGANGSRECHNUNG");
		docDto.setsRow(erDto.getIId().toString());
		docDto.setlSicherheitsstufe(metadata.getSecurityLevel() != null ? 
				metadata.getSecurityLevel() : JCRDocFac.SECURITY_ARCHIV);

		LieferantDto lieferantDto = lieferantCall.lieferantFindByPrimaryKeyOhneExc(erDto.getLieferantIId());
		docDto.setlPartner(lieferantDto.getPartnerIId());
		
		return docDto;
	}
	
	private EingangsrechnungDto findEingangsrechnung(Integer id, String cnr) throws RemoteException {
		if (id != null) {
			EingangsrechnungDto erDto = eingangsrechnungCall.eingangsrechnungFindByPrimaryKeyOhneExc(id);
			if (erDto == null) log.error("Could not find purchase invoice with id = " + id);
			return erDto;
		}
		
		if (cnr != null) {
			EingangsrechnungDto erDto = eingangsrechnungCall.eingangsrechnungFindByCNrMandantCNr(cnr);
			if (erDto == null) log.error("Could not find purchase invoice with id = " + id);
			return erDto;
		}
		
		log.warn("Could not find purchase invoice cause of id and cnr is null");
		return null;
	}
	
	@Override
	public DocPath getDocPath(Integer id, String cnr) throws RemoteException {
		EingangsrechnungDto erDto = findEingangsrechnung(id, cnr);
		if (erDto == null) return null;
		
		return getDocPath(erDto);
	}
	
	private DocPath getDocPath(EingangsrechnungDto erDto) {
		return new DocPath(new DocNodeEingangsrechnung(erDto));
	}
}
