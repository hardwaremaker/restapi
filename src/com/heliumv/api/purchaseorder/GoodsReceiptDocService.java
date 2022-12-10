package com.heliumv.api.purchaseorder;

import java.rmi.RemoteException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.api.delivery.DeliveryDocService;
import com.heliumv.api.document.DocumentMetadata;
import com.heliumv.api.document.IDocumentCategoryService;
import com.heliumv.factory.IBestellungCall;
import com.heliumv.factory.ILieferantCall;
import com.heliumv.factory.IWareneingangCall;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.bestellung.service.WareneingangDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.system.jcr.service.JCRDocDto;
import com.lp.server.system.jcr.service.JCRDocFac;
import com.lp.server.system.jcr.service.docnode.DocNodeWareneingang;
import com.lp.server.system.jcr.service.docnode.DocPath;

public class GoodsReceiptDocService implements IDocumentCategoryService {
	private static Logger log = LoggerFactory.getLogger(DeliveryDocService.class) ;
	
	@Autowired
	private IWareneingangCall	wareneingangCall;
	@Autowired
	private IBestellungCall bestellungCall;
	@Autowired
	private ILieferantCall lieferantCall;
	
	@Override
	public JCRDocDto setupDoc(Integer id, String cnr, DocumentMetadata metadata) throws RemoteException {
		WareneingangDto wareneingangDto = findWareneingang(id);
		if (wareneingangDto == null) return null;
		BestellungDto bestellungDto = findBestellung(wareneingangDto.getBestellungIId());
		
		JCRDocDto docDto = new JCRDocDto();
		docDto.setDocPath(getDocPath(wareneingangDto, bestellungDto));
		docDto.setsBelegnummer(wareneingangDto.getIId().toString());
		docDto.setsTable("WARENEINGANG");
		docDto.setsRow(wareneingangDto.getIId().toString());
		docDto.setlSicherheitsstufe(metadata.getSecurityLevel() != null ?
				metadata.getSecurityLevel() : JCRDocFac.SECURITY_ARCHIV);

		LieferantDto lieferantDto = lieferantCall.lieferantFindByPrimaryKeyOhneExc(
				bestellungDto.getLieferantIIdBestelladresse());
		if (lieferantDto != null) {
			docDto.setlPartner(lieferantDto.getPartnerIId());
		}
		
		return docDto;
	}

	private WareneingangDto findWareneingang(Integer id) throws RemoteException {
		if (id != null) {
			WareneingangDto wareneingangDto = wareneingangCall.wareneingangFindByPrimaryKeyOhneExc(id);
			if (wareneingangDto == null) {
				log.error("Could not find goodsreceipt with id = " + id);
			}
			return wareneingangDto;
		}
		
		return null;
	}

	private BestellungDto findBestellung(Integer id) throws RemoteException {
		if (id != null) {
			BestellungDto bestellungDto = bestellungCall.bestellungFindByPrimaryKeyOhneExc(id);
			if (bestellungDto == null) {
				log.error("Could not find purchaseorder with id = " + id);
			}
			return bestellungDto;
		}
		
		return null;
	}
	
	@Override
	public DocPath getDocPath(Integer id, String cnr) throws RemoteException {
		WareneingangDto wareneingangDto = findWareneingang(id);
		if (wareneingangDto == null) return null;
		BestellungDto bestellungDto = findBestellung(wareneingangDto.getBestellungIId());
		if (bestellungDto == null) return null;
		
		return getDocPath(wareneingangDto, bestellungDto);
	}
	
	private DocPath getDocPath(WareneingangDto wareneingangDto, BestellungDto bestellungDto) {
		return new DocPath(new DocNodeWareneingang(wareneingangDto, bestellungDto));
	}
}
