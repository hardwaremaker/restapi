package com.heliumv.api.system;

import com.heliumv.api.BaseFLRTransformer;
import com.heliumv.tools.StringHelper;
import com.lp.server.system.fastlanereader.service.TableColumnInformation;

public class ItemUnitEntryTransformer extends BaseFLRTransformer<ItemUnitEntry> {
	
	@Override
	public ItemUnitEntry transformOne(Object[] flrObject, TableColumnInformation columnInformation) {
		if(flrObject.length != 4) {
			throw new IllegalArgumentException("Expected 4 Elements (EinheitHandler)");
		}
		ItemUnitEntry entry = new ItemUnitEntry();
		entry.setCnr(StringHelper.trim((String) flrObject[0]));
		entry.setDescription((String) flrObject[2]);
		entry.setDimension((Integer) flrObject[3]);
		return entry;
	}
}
