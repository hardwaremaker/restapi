package com.heliumv.factory.loader;

import java.math.BigDecimal;
import java.rmi.RemoteException;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.api.production.ProductionEntry;
import com.heliumv.factory.IFertigungCall;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LosablieferungDto;

public class ProductionLoaderDeliveredAmount implements IProductionLoaderAttribute {

	@Autowired 
	private IFertigungCall fertigungCall;

	@Override
	public ProductionEntry load(ProductionEntry entry, LosDto losDto) {
		LosablieferungDto[] losablieferungen;
		try {
			losablieferungen = fertigungCall.losablieferungFindByLosIId(entry.getId());
			BigDecimal summe = BigDecimal.ZERO;
			
			for (LosablieferungDto dto : losablieferungen) {
				summe = summe.add(dto.getNMenge());
			}
			entry.setDeliveredAmount(summe);
		} catch (RemoteException e) {
		}
		
		return entry;
	}

}
