package com.heliumv.api.hvma;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.factory.IHvmaCall;
import com.lp.server.personal.service.HvmaLizenzEnum;
import com.lp.server.personal.service.HvmaRechtEnum;
import com.lp.server.personal.service.HvmabenutzerParameterDto;

public class HvmaService implements IHvmaService {
	@Autowired
	private IHvmaCall hvmaCall;
	@Autowired
	private HvmaParamEntryMapper paramEntryMapper;
	@Autowired
	private HvmaRechtEnumMapper rechtEnumMapper;

	@Override
	public List<HvmaRechtEnum> mobilePrivileges() throws RemoteException {
		return rechtEnumMapper.mapDtos(hvmaCall.getHvmaRechte());
	}
	
	@Override
	public List<HvmaRechtEnum> mobilePrivileges(HvmaLizenzEnum licence) throws RemoteException {
		return rechtEnumMapper.mapDtos(hvmaCall.getHvmaRechte(licence));
	}
	
	@Override
	public List<HvmaParamEntry> mobileParameters() {
		return mapHvmaBenutzerParameter(hvmaCall.parameterMobil());
	}
	
	private List<HvmaParamEntry> mapHvmaBenutzerParameter(List<HvmabenutzerParameterDto> params) {
		List<HvmaParamEntry> entries = new ArrayList<HvmaParamEntry>();
		for (HvmabenutzerParameterDto dto : params) {
			entries.add(paramEntryMapper.mapDto(dto));
		}
		return entries;
	}
}
