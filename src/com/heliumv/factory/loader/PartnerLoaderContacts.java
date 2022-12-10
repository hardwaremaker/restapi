package com.heliumv.factory.loader;

import java.rmi.RemoteException;

import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.api.customer.AnsprechpartnerMapper;
import com.heliumv.api.customer.ContactEntryList;
import com.heliumv.api.customer.IPartnerEntry;
import com.heliumv.factory.IAnsprechpartnerCall;
import com.heliumv.factory.IPartnerCall;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

public class PartnerLoaderContacts {

	@Autowired
	private AnsprechpartnerMapper ansprechpartnerMapper ;
	@Autowired
	private IAnsprechpartnerCall ansprechpartnerCall ;	
	@Autowired
	private IPartnerCall partnerCall;

	public IPartnerEntry load(IPartnerEntry partnerEntry, PartnerDto partnerDto) {
		ContactEntryList contactEntries = new ContactEntryList();
		try {
			AnsprechpartnerDto[] ansprechpartner = ansprechpartnerCall.ansprechpartnerFindByPartnerIId(partnerDto.getIId());
			if (ansprechpartner != null) {
				for (AnsprechpartnerDto ansprDto : ansprechpartner) {
					if (Helper.short2boolean(ansprDto.getBVersteckt())) {
						continue;
					}
					PartnerDto ansprechpartnerPartner = partnerCall.partnerFindByPrimaryKey(ansprDto.getPartnerIIdAnsprechpartner());
					ansprDto.setPartnerDto(ansprechpartnerPartner);
					contactEntries.getContactEntries().add(ansprechpartnerMapper.mapAnsprechpartner(ansprDto));
				}
			}
		} catch (EJBExceptionLP e) {
		} catch (RemoteException e) {
		} catch (NamingException e) {
		}
		
		partnerEntry.setContactEntries(contactEntries);
		return partnerEntry;
	}
}
