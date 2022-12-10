package com.heliumv.factory.loader;

import java.rmi.RemoteException;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.api.customer.CustomerDetailEntry;
import com.heliumv.api.customer.CustomerEntryMapper;
import com.heliumv.api.production.ProductionEntry;
import com.heliumv.factory.IAuftragCall;
import com.heliumv.factory.IKundeCall;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.partner.service.KundeDto;

public class ProductionLoaderCustomer implements IProductionLoaderAttribute {
	
	@Autowired
	private IKundeCall kundeCall;
	@Autowired
	private IAuftragCall auftragCall;
	@Autowired
	private CustomerEntryMapper customerEntryMapper ;

	@Override
	public ProductionEntry load(ProductionEntry entry, LosDto losDto) {
		try {
			Integer kundeIId = getKundeIId(losDto);
			if (kundeIId == null) return entry;
			
			KundeDto kundeDto = kundeCall.kundeFindByPrimaryKeyOhneExc(kundeIId);
			CustomerDetailEntry customerDetailEntry = kundeDto != null 
					? customerEntryMapper.mapDetailEntry(kundeDto) : null;
			entry.setCustomer(customerDetailEntry);
		} catch (RemoteException e) {
		}
		
		return entry;
	}
	
	private Integer getKundeIId(LosDto losDto) {
		if (losDto.getKundeIId() != null) return losDto.getKundeIId();
		
		if (losDto.getAuftragIId() != null) {
			AuftragDto auftragdto = auftragCall.auftragFindByPrimaryKeyOhneExc(losDto.getAuftragIId());
			return auftragdto.getKundeIIdAuftragsadresse();
		}
		
		return null;
	}

}
