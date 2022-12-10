package com.heliumv.factory.loader;

import java.rmi.RemoteException;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.api.production.IProductionService;
import com.heliumv.api.production.ProductionEntry;
import com.lp.server.fertigung.service.LosDto;
import com.lp.util.EJBExceptionLP;

public class ProductionLoaderTestPlan implements IProductionLoaderAttribute {

	@Autowired
	private IProductionService productionService;

	@Override
	public ProductionEntry load(ProductionEntry entry, LosDto losDto) {
		try {
			entry.setTestPlanEntries(productionService.getTestPlanEntries(losDto.getIId()));
		} catch (RemoteException e) {
		} catch (EJBExceptionLP e) {
		}
		
		return entry;
	}

}