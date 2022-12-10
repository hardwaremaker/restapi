package com.heliumv.api.partlist;

import com.lp.server.stueckliste.service.MontageartDto;

public class MountingMethodEntryMapper {

	public MountingMethodEntry mapEntry(MontageartDto dto) {
		MountingMethodEntry entry = new MountingMethodEntry();
		entry.setId(dto.getIId());
		entry.setiSort(dto.getISort());
		entry.setDescription(dto.getCBez());
		entry.setItemId(dto.getArtikelIId());
		
		return entry;
	}

}
