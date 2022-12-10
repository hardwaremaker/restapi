package com.heliumv.api.delivery;

import com.lp.server.lieferschein.service.LieferscheinDto;

public class DeliveryEntryMapper {
	public DeliveryEntry mapEntry(LieferscheinDto lsDto) {
		DeliveryEntry entry = new DeliveryEntry(lsDto.getIId()) ;
		entry.setDeliveryCnr(lsDto.getCNr());
		entry.setStatus(DeliveryDocumentStatus.fromString(lsDto.getStatusCNr()));
		return entry ;
	}
}
