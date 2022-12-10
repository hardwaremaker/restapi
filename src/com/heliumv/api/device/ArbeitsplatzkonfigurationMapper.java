package com.heliumv.api.device;

import com.lp.server.system.service.ArbeitsplatzDto;
import com.lp.server.system.service.ArbeitsplatzkonfigurationDto;

public class ArbeitsplatzkonfigurationMapper {

	public DeviceConfigEntry mapArbeitsplatzkonfiguration(
			ArbeitsplatzDto arbeitsplatzDto, ArbeitsplatzkonfigurationDto konfigurationDto) {
		DeviceConfigEntry entry = new DeviceConfigEntry();
		mapArbeitsplatz(entry, arbeitsplatzDto);
		mapArbeitsplatzkonfiguration(entry, konfigurationDto) ;
		return entry ;
	}
	
	private void mapArbeitsplatz(DeviceConfigEntry entry, ArbeitsplatzDto dto) {
		entry.setCnr(dto.getCGeraetecode());
		entry.setDeviceType(dto.getCTyp()) ;
		entry.setDeviceTag(dto.getCPcname());		
	}
	
	private void mapArbeitsplatzkonfiguration(DeviceConfigEntry entry, ArbeitsplatzkonfigurationDto dto) {
		entry.setSystemConfig(dto.getCSystem());
		entry.setUserConfig(dto.getCUser());
	}
}
