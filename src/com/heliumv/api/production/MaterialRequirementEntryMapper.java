package com.heliumv.api.production;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lp.server.fertigung.service.BedarfsuebernahmeDto;
import com.lp.util.Helper;

public class MaterialRequirementEntryMapper {
	private static Logger log = LoggerFactory.getLogger(MaterialRequirementEntryMapper.class) ;

	public BedarfsuebernahmeDto mapDto(MaterialRequirementEntry entry) {
		BedarfsuebernahmeDto dto = new BedarfsuebernahmeDto();
		dto.setBAbgang(Helper.boolean2Short(!Boolean.TRUE.equals(entry.getReturned())));
		dto.setBzusaetzlich(Helper.boolean2Short(Boolean.TRUE.equals(entry.getAdditional())));
		dto.setCArtikelbezeichnung(entry.getItemDescription());
		dto.setCArtikelnummer(entry.getItem());
		dto.setCKommentar(entry.getComment());
		dto.setCLosnummer(entry.getProductionCnr());
		dto.setLosIId(entry.getProductionId());
		dto.setNWunschmenge(entry.getDesiredQuantity());
		dto.setPersonalIIdAendern(entry.getFromStaffId());
		dto.setPersonalIIdAnlegen(entry.getFromStaffId());
		dto.setTAendern(entry.getAcquisitionTimeMs() != null ? new Timestamp(entry.getAcquisitionTimeMs()) : null);
		dto.setTAnlegen(dto.getTAendern());
		dto.setTWunschtermin(entry.getDesiredDateMs() != null ? new Timestamp(entry.getDesiredDateMs()) : null);
		try {
			dto.setOMedia(entry.getData() != null 
					? Base64.decodeBase64(entry.getData()) 
					: null);
		} catch (Throwable e) {
			log.error("Could not decode data string with base64 decoder to byte array. From " + entry.asString(), e);
		}
		
		return dto;
	}

	public List<BedarfsuebernahmeDto> mapDtos(MaterialRequirementEntryList materialList) {
		List<BedarfsuebernahmeDto> dtos = new ArrayList<BedarfsuebernahmeDto>();
		if (materialList == null) return dtos;
		
		for (MaterialRequirementEntry entry : materialList.getEntries()) {
			dtos.add(mapDto(entry));
		}
		return dtos;
	}
}
