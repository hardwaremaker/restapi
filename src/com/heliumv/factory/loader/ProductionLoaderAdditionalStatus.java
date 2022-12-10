package com.heliumv.factory.loader;

import java.rmi.RemoteException;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.api.production.ProductionAdditionalStatusEntry;
import com.heliumv.api.production.ProductionAdditionalStatusEntryList;
import com.heliumv.api.production.ProductionEntry;
import com.heliumv.factory.IFertigungCall;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LoszusatzstatusDto;
import com.lp.server.fertigung.service.ZusatzstatusDto;

public class ProductionLoaderAdditionalStatus implements IProductionLoaderAttribute {
	@Autowired 
	private IFertigungCall fertigungCall;

	@Override
	public ProductionEntry load(ProductionEntry entry, LosDto losDto) {
		try {
			LoszusatzstatusDto[] loszusatzstatusDtos = fertigungCall.loszusatzstatusFindByLosIIdOhneExc(losDto.getIId());
			ProductionAdditionalStatusEntryList additionalStatuses = new ProductionAdditionalStatusEntryList();
			for (LoszusatzstatusDto lzDto : loszusatzstatusDtos) {
				ZusatzstatusDto zusatzstatusDto = fertigungCall.zusatzstatusFindByPrimaryKey(lzDto.getZusatzstatusIId());
				if (zusatzstatusDto == null) continue;
				ProductionAdditionalStatusEntry statusEntry = new ProductionAdditionalStatusEntry();
				statusEntry.setStatus(zusatzstatusDto.getCBez());
				statusEntry.setDateMs(lzDto.getTAendern().getTime());
				additionalStatuses.getEntries().add(statusEntry);
			}
			entry.setAdditionalStatuses(additionalStatuses);
		} catch (RemoteException e) {
		}
		
		return entry;
	}

}
