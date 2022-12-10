package com.heliumv.api.staff;

import java.rmi.RemoteException;

import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.factory.IPartnerCall;
import com.lp.server.personal.service.PersonalDto;

public class StaffEntryMapper {

	@Autowired
	private IPartnerCall partnerCall;
	
	public StaffEntry mapEntry(PersonalDto personalDto) {
		StaffEntry entry = new StaffEntry();
		if (personalDto == null) return entry;
		
		entry.setId(personalDto.getIId());
		entry.setIdentityCnr(personalDto.getCAusweis());
		entry.setPersonalNr(personalDto.getCPersonalnr());
		entry.setShortMark(personalDto.getCKurzzeichen());

		try {
			personalDto.setPartnerDto(partnerCall.partnerFindByPrimaryKey(personalDto.getPartnerIId()));
			entry.setFirstName(personalDto.getPartnerDto().getCName2vornamefirmazeile2());
			entry.setName(personalDto.getPartnerDto().getCName1nachnamefirmazeile1());
		} catch (RemoteException e) {
		} catch (NamingException e) {
		}
		
		return entry;
	}
}
