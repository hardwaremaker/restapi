package com.heliumv.api.item;

import com.lp.server.artikel.service.SeriennrChargennrAufLagerDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;

public class ItemIdentyEntryMapper {
	public ItemIdentityEntry mapEntry(SeriennrChargennrAufLagerDto dto) {
		ItemIdentityEntry entry = new ItemIdentityEntry();
		entry.setAmount(dto.getNMenge());
		entry.setIdentity(dto.getCSeriennrChargennr());
		entry.setVersion(getNullIfEmpty(dto.getCVersion()));
		entry.setBundleAmount(dto.getBdGebindemenge());
		entry.setBundleIdentity(dto.getSGebinde()) ; 
		return entry ;
	}
	
	public ItemIdentityEntry mapEntry(SeriennrChargennrMitMengeDto dto) {
		ItemIdentityEntry entry = new ItemIdentityEntry();
		entry.setAmount(dto.getNMenge());
		entry.setIdentity(dto.getCSeriennrChargennr());
		entry.setVersion(getNullIfEmpty(dto.getCVersion()));
		entry.setBundleAmount(dto.getNGebindemenge());
//		entry.setBundleIdentity(dto.get);
		return entry ;		
	}
	
	private String getNullIfEmpty(String any) {
		if(any == null || any.isEmpty()) return null;
		return any;
	} 
}
