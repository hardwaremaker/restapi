package com.heliumv.api.delivery;

import com.heliumv.api.BaseFLRTransformerFeatureData;
import com.heliumv.factory.query.TableColumnInformationMapper;
import com.lp.server.lieferschein.service.ILieferscheinFLRData;
import com.lp.server.system.fastlanereader.service.TableColumnInformation;

public class DeliveryEntryTransformer extends BaseFLRTransformerFeatureData<DeliveryEntry, ILieferscheinFLRData> {

	private TableColumnInformationMapper flrMapper = new TableColumnInformationMapper() ;

	@Override
	public DeliveryEntry transformOne(Object[] flrObject, TableColumnInformation columnInformation) {
		DeliveryEntry entry = new DeliveryEntry();

		flrMapper.setTableColumnInformation(columnInformation);
		flrMapper.setValues(entry, flrObject);
		
		return entry;
	}

	@Override
	protected void transformFlr(DeliveryEntry entry, ILieferscheinFLRData flrData) {
		entry.setCustomerId(flrData.getKundeIdLieferadresse());
		entry.setStatus(DeliveryDocumentStatus.fromString(flrData.getStatusCnr()));
	}
}
