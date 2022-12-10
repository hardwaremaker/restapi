package com.heliumv.api.system;

import com.heliumv.api.BaseFLRTransformer;
import com.lp.server.system.fastlanereader.service.TableColumnInformation;

public class CountryEntryTransformer extends BaseFLRTransformer<CountryEntry> {
	@Override
	public CountryEntry transformOne(Object[] flrObject, 
			TableColumnInformation columnInformation) {
		if(flrObject.length != 6) {
			throw new IllegalArgumentException("Expected 6 Elements!");
		}
	
		CountryEntry entry = new CountryEntry();
		entry.setId((Integer) flrObject[0]);
		entry.setCnr((String) flrObject[1]);
		entry.setName((String) flrObject[2]);
		entry.setCurrency((String) flrObject[5]); 
		return entry ;
	}
}
