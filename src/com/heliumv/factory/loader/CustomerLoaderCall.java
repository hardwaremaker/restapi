package com.heliumv.factory.loader;

import java.rmi.RemoteException;
import java.util.Set;

import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.api.customer.CustomerDetailEntry;
import com.heliumv.api.customer.CustomerService;
import com.heliumv.factory.IKundeCall;
import com.lp.server.partner.service.KundeDto;

public class CustomerLoaderCall implements ICustomerLoaderCall {

	@Autowired
	private IKundeCall kundeCall;
	@Autowired
	private CustomerService customerService;
	
	@Override
	public CustomerDetailEntry kundeFindByPrimaryKeyOhneExc(Integer id, Set<ICustomerLoaderAttribute> attributes) {
		KundeDto kundeDto;
		try {
			kundeDto = kundeCall.kundeFindByPrimaryKeyOhneExc(id);
			if (kundeDto == null) return null;
			
			return loadCustomer(kundeDto, attributes);
		} catch (RemoteException e) {
		}
		return null;
	}
	
	public CustomerDetailEntry loadCustomer(KundeDto kundeDto, Set<ICustomerLoaderAttribute> attributes) {
		CustomerDetailEntry entry;
		try {
			entry = customerService.getCustomerDetailEntry(kundeDto);
			entry = addAttributes(entry, kundeDto, attributes);
			return entry;
		} catch (RemoteException e) {
		} catch (NamingException e) {
		}
		return null;
	}

	private CustomerDetailEntry addAttributes(CustomerDetailEntry entry, KundeDto kundeDto,
			Set<ICustomerLoaderAttribute> attributes) {
		for (ICustomerLoaderAttribute loaderAttribute : attributes) {
			loaderAttribute.load(entry, kundeDto);
		}
		return entry;
	}

}
