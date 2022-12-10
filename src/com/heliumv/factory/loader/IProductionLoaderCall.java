package com.heliumv.factory.loader;

import java.util.Set;

import com.heliumv.api.production.ProductionEntry;

public interface IProductionLoaderCall {

	ProductionEntry losFindByPrimaryKeyOhneExc(Integer id, Set<IProductionLoaderAttribute> attributes);
	
	ProductionEntry losFindByCnrOhneExc(String cnr, Set<IProductionLoaderAttribute> attributes);
}
