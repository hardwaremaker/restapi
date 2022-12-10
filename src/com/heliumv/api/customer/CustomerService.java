package com.heliumv.api.customer;

import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Set;

import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.factory.IPersonalCall;
import com.heliumv.factory.IVkPreisfindungCall;
import com.heliumv.factory.loader.CustomerLoaderContacts;
import com.heliumv.factory.loader.ICustomerLoaderAttribute;
import com.heliumv.factory.loader.ICustomerLoaderCall;
import com.lp.server.artikel.service.VkpfartikelpreislisteDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.personal.service.PersonalDto;

public class CustomerService {
	@Autowired
	private IPersonalCall personalCall ;
	@Autowired
	private IVkPreisfindungCall vkpreisfindungCall ;
	@Autowired
	private CustomerEntryMapper customerEntryMapper ;
	@Autowired
	private ICustomerLoaderCall customerLoaderCall;
	@Autowired
	private CustomerLoaderContacts customerLoaderContacts;
	
	
	public CustomerDetailEntry getCustomerDetailEntry(KundeDto kundeDto) throws RemoteException, NamingException {
		PersonalDto personalDto = getPersonal(kundeDto.getPersonaliIdProvisionsempfaenger()) ;
		VkpfartikelpreislisteDto preislisteDto = getPreisliste(kundeDto.getVkpfArtikelpreislisteIIdStdpreisliste()) ;			
		return customerEntryMapper.mapDetailEntry(kundeDto, personalDto, preislisteDto) ;		
	}
	
	private PersonalDto getPersonal(Integer personalId) throws NamingException, RemoteException {
		if(personalId == null) return null ;
		return personalCall.byPrimaryKeySmall(personalId) ;
	}	

	private VkpfartikelpreislisteDto getPreisliste(Integer preislisteId) throws NamingException, RemoteException {
		if(preislisteId == null) return null ;			
		return vkpreisfindungCall.vkpfartikelpreislisteFindByPrimaryKey(preislisteId) ;
	}
	
	public CustomerDetailEntry getCustomerDetailEntry(KundeDto kundeDto, Boolean addContacts) {
		Set<ICustomerLoaderAttribute> attributes = getAttributes(addContacts);
		return customerLoaderCall.loadCustomer(kundeDto, attributes);
	}

	private Set<ICustomerLoaderAttribute> getAttributes(Boolean addContacts) {
		Set<ICustomerLoaderAttribute> attributes = new HashSet<ICustomerLoaderAttribute>();
		if (Boolean.TRUE.equals(addContacts)) {
			attributes.add(customerLoaderContacts);
		}
		return attributes;
	}
}
