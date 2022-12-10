package com.heliumv.api.finance;

import com.heliumv.api.BaseFLRTransformer;
import com.lp.server.system.fastlanereader.service.TableColumnInformation;

public class CurrencyEntryTransformer extends BaseFLRTransformer<CurrencyEntry> {

	@Override
	public CurrencyEntry transformOne(Object[] flrObject, 
			TableColumnInformation columnInformation) {
		if(flrObject.length != 3) {
			throw new IllegalArgumentException("Expected 3 Elements!");
		}

		CurrencyEntry entry = new CurrencyEntry();
		entry.setCnr((String) flrObject[0]);
		entry.setDescription((String) flrObject[2]);
		return entry ;
	}
}
