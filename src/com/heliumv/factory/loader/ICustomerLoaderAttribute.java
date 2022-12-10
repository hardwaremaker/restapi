package com.heliumv.factory.loader;

import com.heliumv.api.customer.CustomerDetailEntry;
import com.lp.server.partner.service.KundeDto;

public interface ICustomerLoaderAttribute {

	CustomerDetailEntry load(CustomerDetailEntry entry, KundeDto kundeDto);
}
