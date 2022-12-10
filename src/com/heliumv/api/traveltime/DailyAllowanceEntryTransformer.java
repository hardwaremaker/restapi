package com.heliumv.api.traveltime;

import com.heliumv.api.BaseFLRTransformer;
import com.lp.server.system.fastlanereader.service.TableColumnInformation;

public class DailyAllowanceEntryTransformer extends BaseFLRTransformer<DailyAllowanceEntry> {
	@Override
	public DailyAllowanceEntry transformOne(Object[] flrObject, 
			TableColumnInformation columnInformation) {
		if(flrObject.length != 3) {
			throw new IllegalArgumentException("Expected 3 Elements!");
		}
	
		DailyAllowanceEntry entry = new DailyAllowanceEntry();
		entry.setId((Integer) flrObject[0]);
		entry.setDescription((String) flrObject[1]);
		entry.setCnr((String) flrObject[2]);
		return entry ;
	}
}
