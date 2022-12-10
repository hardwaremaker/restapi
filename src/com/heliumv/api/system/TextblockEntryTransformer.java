package com.heliumv.api.system;

import com.heliumv.api.BaseFLRTransformer;
import com.heliumv.types.MimeTypeEnum;
import com.lp.server.system.fastlanereader.service.TableColumnInformation;

public class TextblockEntryTransformer extends BaseFLRTransformer<TextblockEntry> {

	@Override
	public TextblockEntry transformOne(Object[] flrObject, TableColumnInformation columnInformation) {
		if(flrObject.length != 5 ) {
			throw new IllegalArgumentException("Expected 5 Elements (MediaStandardHandler)!");
		}
		TextblockEntry entry = new TextblockEntry();
		entry.setId((Integer) flrObject[0]);
		entry.setDescription((String) flrObject[1]);
		entry.setLocaleCnr((String) flrObject[2]);
		entry.setMimeType(MimeTypeEnum.fromString((String)flrObject[3]));
		entry.setHidden(flrObject[4] != null);
		return entry;
	}
}
