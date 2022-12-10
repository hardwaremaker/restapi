package com.heliumv.api.purchaseorder;

import com.lp.server.bestellung.service.BestellungDto;

public class PurchaseOrderEntryMapper {
	public PurchaseOrderEntry mapEntry(BestellungDto dto) {
		PurchaseOrderEntry entry = new PurchaseOrderEntry();
		entry.setId(dto.getIId());
		entry.setCnr(dto.getCNr());
		entry.setSupplierId(dto.getLieferantIIdBestelladresse());
		entry.setStatus(PurchaseOrderStatus.fromString(dto.getStatusCNr()));
		return entry;
	}
}
