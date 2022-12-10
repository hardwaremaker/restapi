package com.heliumv.api.purchaseorder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.lp.server.bestellung.service.WareneingangDto;
import com.lp.server.bestellung.service.WareneingangspositionDto;

public class GoodsReceiptEntryMapper {
	public GoodsReceiptEntry mapEntry(WareneingangDto dto) {
		GoodsReceiptEntry entry = new GoodsReceiptEntry();
		entry.setId(dto.getIId());
		entry.setDeliverySlipCnr(dto.getCLieferscheinnr());
		entry.setPurchaseOrderId(dto.getBestellungIId());
		return entry;
	}
	
	public List<GoodsReceiptEntry> mapEntries(Collection<WareneingangDto> dtos) {
		List<GoodsReceiptEntry> entries = new ArrayList<GoodsReceiptEntry>();
		for (WareneingangDto dto : dtos) {
			entries.add(mapEntry(dto));
		}
		return entries;
	}
	
	public GoodsReceiptPositionEntry mapEntry(WareneingangspositionDto posDto) {
		GoodsReceiptPositionEntry entry = new GoodsReceiptPositionEntry();
		entry.setId(posDto.getIId());
		return entry;
	}
	
	public List<GoodsReceiptPositionEntry> mapEntry(WareneingangspositionDto[] posDtos) {
		List<GoodsReceiptPositionEntry> entries = new ArrayList<GoodsReceiptPositionEntry>();
		for(WareneingangspositionDto posDto : posDtos) {
			entries.add(mapEntry(posDto));
		}
		
		return entries ;		
	}
}
