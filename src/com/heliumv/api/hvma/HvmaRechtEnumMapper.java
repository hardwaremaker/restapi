package com.heliumv.api.hvma;

import java.util.ArrayList;
import java.util.List;

import com.lp.server.personal.service.HvmaRechtEnum;
import com.lp.server.personal.service.HvmarechtDto;

public class HvmaRechtEnumMapper {

	public HvmaRechtEnum mapDto(HvmarechtDto rechtDto) {
		return rechtDto.getCnrAsEnum();
	}
	
	public List<HvmaRechtEnum> mapDtos(List<HvmarechtDto> rechtDtos) {
		List<HvmaRechtEnum> entries = new ArrayList<HvmaRechtEnum>();
		for (HvmarechtDto rechtDto : rechtDtos) {
			entries.add(mapDto(rechtDto));
		}
		return entries;
	}
}
