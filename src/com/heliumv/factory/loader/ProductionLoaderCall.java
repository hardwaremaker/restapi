package com.heliumv.factory.loader;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.api.production.ProductionEntry;
import com.heliumv.api.production.ProductionEntryMapper;
import com.heliumv.factory.IFertigungCall;
import com.lp.server.fertigung.service.LosDto;

public class ProductionLoaderCall implements IProductionLoaderCall {
	@Autowired
	private IFertigungCall fertigungCall;
	@Autowired
	private ProductionEntryMapper productionEntryMapper;
	
	@Override
	public ProductionEntry losFindByPrimaryKeyOhneExc(Integer id, Set<IProductionLoaderAttribute> attributes) {
		LosDto losDto = fertigungCall.losFindByPrimaryKeyOhneExc(id);
		if (losDto == null) return null;
		ProductionEntry entry = productionEntryMapper.mapEntry(losDto);
		entry = addAttributes(entry, losDto, attributes);
		
		return entry;
	}

	@Override
	public ProductionEntry losFindByCnrOhneExc(String cnr, Set<IProductionLoaderAttribute> attributes) {
		LosDto losDto = fertigungCall.losFindByCNrMandantCNrOhneExc(cnr);
		if (losDto == null) return null;
		ProductionEntry entry = productionEntryMapper.mapEntry(losDto);
		entry = addAttributes(entry, losDto, attributes);
		
		return entry;
	}

	private ProductionEntry addAttributes(ProductionEntry entry, LosDto losDto, Set<IProductionLoaderAttribute> attributes) {
		for (IProductionLoaderAttribute loaderAttribute : attributes) {
			loaderAttribute.load(entry, losDto);
		}
		
		return entry;
	}

}
