package com.heliumv.api.stock;

import java.util.ArrayList;
import java.util.List;

import com.lp.server.artikel.service.LagerplatzDto;

public class StockPlaceEntryMapper {

	public StockPlaceEntry mapEntry(LagerplatzDto lagerplatzDto) {
		StockPlaceEntry entry = new StockPlaceEntry();
		if (lagerplatzDto == null) return entry;
		
		entry.setId(lagerplatzDto.getIId());
		entry.setName(lagerplatzDto.getCLagerplatz());
		
		return entry;
	}

	public List<StockPlaceEntry> mapEntries(List<LagerplatzDto> dtos) {
		List<StockPlaceEntry> entries = new ArrayList<StockPlaceEntry>();
		if (dtos == null || dtos.isEmpty()) return entries;
		
		for (LagerplatzDto lagerplatzDto : dtos) {
			entries.add(mapEntry(lagerplatzDto));
		}
		
		return entries;
	}
}
