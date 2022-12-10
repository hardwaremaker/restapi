package com.heliumv.factory.loader;

import java.util.Set;

import com.heliumv.api.customer.CustomerDetailEntry;
import com.lp.server.partner.service.KundeDto;

public interface ICustomerLoaderCall {

	CustomerDetailEntry kundeFindByPrimaryKeyOhneExc(Integer id, Set<ICustomerLoaderAttribute> attributes);
	
	CustomerDetailEntry loadCustomer(KundeDto kundeDto, Set<ICustomerLoaderAttribute> attributes);
}
