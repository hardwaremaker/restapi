package com.heliumv.factory.loader;

import java.rmi.RemoteException;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.api.partlist.IPartlistApi;
import com.heliumv.api.partlist.PartlistApi;
import com.heliumv.api.production.ProductionEntry;
import com.lp.server.fertigung.service.LosDto;

public class ProductionLoaderAdditionalWorksteps implements IProductionLoaderAttribute {

	@Autowired
	private IPartlistApi partlistApi;

	@Override
	public ProductionEntry load(ProductionEntry entry, LosDto losDto) {
		if (losDto.getStuecklisteIId() == null) return entry;
		
		try {
			entry.setWorksteps(((PartlistApi)partlistApi).getWorkstepsImpl(losDto.getStuecklisteIId()));
		} catch (RemoteException e) {
		}
		
		return entry;
	}

}
