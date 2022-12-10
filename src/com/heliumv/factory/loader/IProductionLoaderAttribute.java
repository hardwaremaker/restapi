package com.heliumv.factory.loader;

import com.heliumv.api.production.ProductionEntry;
import com.lp.server.fertigung.service.LosDto;

public interface IProductionLoaderAttribute {
	
	ProductionEntry load(ProductionEntry entry, LosDto losDto);
}
