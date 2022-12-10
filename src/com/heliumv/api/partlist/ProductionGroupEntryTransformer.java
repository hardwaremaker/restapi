package com.heliumv.api.partlist;

import com.heliumv.api.BaseFLRTransformer;
import com.lp.server.system.fastlanereader.service.TableColumnInformation;

public class ProductionGroupEntryTransformer extends
		BaseFLRTransformer<ProductionGroupEntry> {

	@Override
	public ProductionGroupEntry transformOne(Object[] flrObject,
			TableColumnInformation columnInformation) {
		ProductionGroupEntry entry = new ProductionGroupEntry();
		entry.setId((Integer) flrObject[0]);
		entry.setDescription((String) flrObject[1]);
		entry.setSortOrder((Integer)flrObject[2]);
		return entry;
	}
}
