package com.heliumv.api.forecast;

import java.util.ArrayList;
import java.util.List;

import com.lp.server.forecast.service.KommdruckerDto;

public class PickingPrinterEntryMapper {

	public PickingPrinterEntry mapEntry(KommdruckerDto dto) {
		PickingPrinterEntry entry = new PickingPrinterEntry();
		entry.setId(dto.getIId());
		entry.setCnr(dto.getCNr());
		entry.setDescription(dto.getCBez());
		return entry;
	}

	public List<PickingPrinterEntry> mapEntries(List<KommdruckerDto> dtos) {
		List<PickingPrinterEntry> entries = new ArrayList<PickingPrinterEntry>();
		for (KommdruckerDto dto : dtos) {
			entries.add(mapEntry(dto));
		}
		return entries;
	}
}
