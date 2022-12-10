package com.heliumv.api.item;

import java.util.ArrayList;
import java.util.List;

import com.lp.server.artikel.service.VkpfartikelpreislisteDto;
import com.lp.util.Helper;

public class PriceListEntryMapper {

	public PriceListEntry mapEntry(VkpfartikelpreislisteDto dto) {
		if(dto == null) throw new NullPointerException("vkpfartikelpreislisteDto");
		return mapEntryImpl(dto);
	}
	
	public List<PriceListEntry> mapEntries(List<VkpfartikelpreislisteDto> dtos) {		
		List<PriceListEntry> entries = new ArrayList<PriceListEntry>();
		for (VkpfartikelpreislisteDto dto : dtos) {
			entries.add(mapEntryImpl(dto));
		}
		return entries;
	}
	
	public List<PriceListEntry> mapEntries(VkpfartikelpreislisteDto[] dtos) {
		List<PriceListEntry> entries = new ArrayList<PriceListEntry>();
		for (VkpfartikelpreislisteDto dto : dtos) {
			entries.add(mapEntryImpl(dto));
		}
		return entries;		
	}

	private PriceListEntry mapEntryImpl(VkpfartikelpreislisteDto dto) {
		PriceListEntry entry = new PriceListEntry();
		entry.setId(dto.getIId());
		entry.setCnr(dto.getCNr());
		entry.setCurrency(dto.getWaehrungCNr());
		entry.setActive(Helper.isTrue(dto.getBPreislisteaktiv()));
		return entry;
	}
}
