package com.heliumv.factory.loader;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.api.customer.CustomerDetailEntry;
import com.lp.server.partner.service.KundeDto;

public class CustomerLoaderContacts implements ICustomerLoaderAttribute {

	@Autowired
	private PartnerLoaderContacts partnerLoaderContacts;
	
	@Override
	public CustomerDetailEntry load(CustomerDetailEntry entry, KundeDto kundeDto) {
		partnerLoaderContacts.load(entry, kundeDto.getPartnerDto());
		return entry;
	}

}
