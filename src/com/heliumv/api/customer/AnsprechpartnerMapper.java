package com.heliumv.api.customer;

import java.rmi.RemoteException;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.factory.IAnsprechpartnerCall;
import com.heliumv.factory.IPartnerCall;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.AnsprechpartnerfunktionDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

public class AnsprechpartnerMapper {
	@Autowired
	private PartnerEntryMapper partnerEntryMapper ;
	@Autowired
	private IAnsprechpartnerCall ansprechpartnerCall;
	@Autowired
	private IPartnerCall partnerCall;
	
	protected PartnerEntryMapper getPartnerMapper() {
		return partnerEntryMapper ;
	}
	
	public IPartnerEntry mapAnsprechpartner(IPartnerEntry result, AnsprechpartnerDto ansprechpartnerDto) {
		if(ansprechpartnerDto != null) {
			PartnerDto partnerDto = ansprechpartnerDto.getPartnerDto() ;
			if(partnerDto != null) {
				getPartnerMapper().mapPartnerEntry(result, partnerDto) ;
			}
			
			result.setEmail(ansprechpartnerDto.getCEmail());
		}
		
		return result ;
	}
	
	public ContactEntry mapAnsprechpartner(AnsprechpartnerDto ansprechpartnerDto) {
		ContactEntry contactEntry = new ContactEntry();
		if (ansprechpartnerDto == null) {
			return contactEntry;
		}
		
		PartnerDto partnerDtoAnsprechpartner = ansprechpartnerDto.getPartnerDto() ;
		if(partnerDtoAnsprechpartner != null) {
			getPartnerMapper().mapPartnerEntry(contactEntry, partnerDtoAnsprechpartner) ;
		}
		contactEntry.setId(ansprechpartnerDto.getIId());
		contactEntry.setiSort(ansprechpartnerDto.getISort());
		contactEntry.setMobilePhone(ansprechpartnerDto.getCHandy());
		contactEntry.setDepartment(ansprechpartnerDto.getCAbteilung());
		contactEntry.setCompanyEmail(ansprechpartnerDto.getCEmail());
		contactEntry.setValidFromMs(ansprechpartnerDto.getDGueltigab() != null ? ansprechpartnerDto.getDGueltigab().getTime() : null);
		
		if (ansprechpartnerDto.getAnsprechpartnerfunktionIId() != null) {
			try {
				AnsprechpartnerfunktionDto funktionDto = ansprechpartnerCall.ansprechpartnerfunktionFindByPrimaryKey(
						ansprechpartnerDto.getAnsprechpartnerfunktionIId());
				contactEntry.setRole(funktionDto.getBezeichnung());
			} catch (EJBExceptionLP e) {
			} catch (RemoteException e) {
			}
		}

		if (Helper.short2boolean(ansprechpartnerDto.getBDurchwahl())) {
			contactEntry.setCompanyPhone(partnerCall.getPartnerTelefonnummerMitDurchwahl(
					ansprechpartnerDto.getPartnerIId(), ansprechpartnerDto.getCTelefon()));
		} else {
			contactEntry.setCompanyPhone(ansprechpartnerDto.getCTelefon());
		}
		return contactEntry;
	}
}
