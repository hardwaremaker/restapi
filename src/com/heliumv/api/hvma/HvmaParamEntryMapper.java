package com.heliumv.api.hvma;

import com.lp.server.personal.service.HvmabenutzerParameterDto;

public class HvmaParamEntryMapper {
	public HvmaParamEntry mapDto(HvmabenutzerParameterDto dto) {
		HvmaParamEntry entry = new HvmaParamEntry(dto.getCNr());
		entry.setCategory(dto.getKategorie());
		entry.setValue(dto.getWert());
		return entry;
	}
}
