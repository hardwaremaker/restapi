package com.heliumv.api.delivery;

import com.lp.server.lieferschein.service.LieferscheinpositionDto;

public class DeliveryTextPositionEntryMapper {
	public DeliveryTextPositionEntry mapEntry(LieferscheinpositionDto posDto) {
		DeliveryTextPositionEntry entry = new DeliveryTextPositionEntry(posDto.getIId());
		entry.setText(posDto.getXTextinhalt());
		return entry;
	}
}
