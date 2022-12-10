package com.heliumv.api.system;

import com.heliumv.api.BaseFLRTransformer;
import com.lp.server.system.fastlanereader.service.TableColumnInformation;

public class CostBearingUnitEntryTransformer extends BaseFLRTransformer<CostBearingUnitEntry> {

	@Override
	public CostBearingUnitEntry transformOne(Object[] flrObject, TableColumnInformation columnInformation) {
		if(flrObject.length != 2) {
			throw new IllegalArgumentException("Expected 2 Elements (KostentraegerHandler)!");
		}
		CostBearingUnitEntry entry = new CostBearingUnitEntry();
		entry.setId((Integer) flrObject[0]);
		entry.setDescription((String) flrObject[1]);
		return entry;
	}

}
