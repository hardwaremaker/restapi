package com.heliumv.api.item;

import com.lp.server.system.fastlanereader.service.TableColumnInformation;

public class ItemV11SP3925EntryTransformer extends ItemV11EntryTransformer {
	@Override
	public ItemEntry transformOne(Object[] flrObject,
			TableColumnInformation columnInformation) {
		ItemEntry entry =  super.transformOne(flrObject, columnInformation);

		// SP3925 available nie setzen, da Consumer mit dem Available nicht umgehen kann
		entry.setAvailable((Boolean) null);
		return entry ;
	}
}
