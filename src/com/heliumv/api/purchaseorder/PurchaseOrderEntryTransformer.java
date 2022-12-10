package com.heliumv.api.purchaseorder;

import com.heliumv.api.BaseFLRTransformerFeatureData;
import com.lp.server.bestellung.service.IBestellungFLRData;
import com.lp.server.system.fastlanereader.service.TableColumnInformation;

public class PurchaseOrderEntryTransformer extends BaseFLRTransformerFeatureData<PurchaseOrderEntry, IBestellungFLRData> {

	@Override
	public PurchaseOrderEntry transformOne(Object[] flrObject, TableColumnInformation columnInformation) {
		PurchaseOrderEntry entry = new PurchaseOrderEntry();
		entry.setId((Integer) flrObject[0]);
		entry.setCnr((String) flrObject[2]);
		entry.setSupplierName((String) flrObject[3]);
		entry.setSupplierCity((String) flrObject[4]);
		
		return entry;
	}

	@Override
	protected void transformFlr(PurchaseOrderEntry entry, IBestellungFLRData flrData) {
		entry.setSupplierId(flrData.getLieferantId());
		entry.setStatus(PurchaseOrderStatus.fromString(flrData.getStatusCnr()));
	}
}
