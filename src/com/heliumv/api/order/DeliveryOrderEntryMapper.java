package com.heliumv.api.order;

import com.heliumv.api.delivery.DeliveryDocumentStatus;
import com.lp.server.lieferschein.service.LieferscheinDto;

public class DeliveryOrderEntryMapper {
	public DeliveryOrderEntry mapEntry(LieferscheinDto lsDto) {
		DeliveryOrderEntry entry = new DeliveryOrderEntry(lsDto.getIId()) ;
		entry.setDeliveryCnr(lsDto.getCNr());
		entry.setStatus(DeliveryDocumentStatus.fromString(lsDto.getStatusCNr()));
		return entry ;
	}
}
