package com.heliumv.factory.loader;

import java.rmi.RemoteException;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.api.production.IProductionApi;
import com.heliumv.api.production.ProductionApi;
import com.heliumv.api.production.ProductionEntry;
import com.lp.server.fertigung.service.LosDto;

public class ProductionLoaderProductionWorksteps implements IProductionLoaderAttribute {

	@Autowired
	IProductionApi productionApi;
	
	@Override
	public ProductionEntry load(ProductionEntry entry, LosDto losDto) {
		try {
			entry.setProductionWorksteps(((ProductionApi)productionApi).getProductionWorkstepsImpl(losDto.getIId()));
		} catch (RemoteException e) {
		}

		return entry;
	}

}
