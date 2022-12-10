package com.heliumv.api.purchaseorder;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.heliumv.api.BaseFLRTransformerFeatureData;
import com.lp.server.bestellung.service.IBestellvorschlagFLRData;
import com.lp.server.system.fastlanereader.service.TableColumnInformation;

public class PurchaseOrderProposalPositionEntryTransformer
		extends BaseFLRTransformerFeatureData<PurchaseOrderProposalPositionEntry, IBestellvorschlagFLRData> {

	@Override
	public PurchaseOrderProposalPositionEntry transformOne(Object[] flrObject,
			TableColumnInformation columnInformation) {
		PurchaseOrderProposalPositionEntry entry = new PurchaseOrderProposalPositionEntry();
		entry.setId((Integer)flrObject[0]);
		entry.setAmount((BigDecimal)flrObject[columnInformation.getViewIndex("bes.zubestellendeMenge")]);
		entry.setItemCnr((String)flrObject[columnInformation.getViewIndex("bes.artikelcnr")]);
		entry.setDeliveryDateMs(((Timestamp)flrObject[columnInformation.getViewIndex("bes.bestelltermin")]).getTime());
		
		return entry;
	}

	@Override
	protected void transformFlr(PurchaseOrderProposalPositionEntry entry, IBestellvorschlagFLRData flrData) {
		entry.setNoted(flrData.isNoted());
	}

}
