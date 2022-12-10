package com.heliumv.api.purchaseorder;

import java.rmi.RemoteException;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.factory.IArtikelCall;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.bestellung.service.BestellvorschlagDto;
import com.lp.util.Helper;

public class PurchaseOrderProposalEntryMapper {
	
	@Autowired
	private IArtikelCall artikelCall;

	public PurchaseOrderProposalPositionEntry mapEntry(BestellvorschlagDto vorschlagDto) {
		PurchaseOrderProposalPositionEntry entry = new PurchaseOrderProposalPositionEntry();
		entry.setId(vorschlagDto.getIId());
		entry.setDeliveryDateMs(vorschlagDto.getTLiefertermin().getTime());
		entry.setNoted(Helper.short2Boolean(vorschlagDto.getBVormerkung()));
		entry.setAmount(vorschlagDto.getNZubestellendeMenge());
		
		try {
			ArtikelDto artikelDto = artikelCall.artikelFindByPrimaryKeySmallOhneExc(vorschlagDto.getIArtikelId());
			entry.setItemCnr(artikelDto.getCNr());
		} catch (RemoteException e) {
		}
		
		return entry;
	}

}
