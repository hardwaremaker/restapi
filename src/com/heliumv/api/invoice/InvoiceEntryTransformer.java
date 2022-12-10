package com.heliumv.api.invoice;

import com.heliumv.api.BaseFLRTransformerFeatureData;
import com.lp.server.rechnung.service.IRechnungFLRData;
import com.lp.server.system.fastlanereader.service.TableColumnInformation;

public class InvoiceEntryTransformer extends BaseFLRTransformerFeatureData<InvoiceEntry, IRechnungFLRData> {
	@Override
	public InvoiceEntry transformOne(Object[] flrObject,
			TableColumnInformation columnInformation) {
		InvoiceEntry entry = new InvoiceEntry() ;
		entry.setId((Integer) flrObject[0]) ;
		entry.setCnr((String) flrObject[2]) ;
		entry.setCustomerName((String) flrObject[3]) ;
		entry.setCustomerCity((String) flrObject[4]) ;
		return entry ;
	}
	
	@Override
	protected void transformFlr(InvoiceEntry entry, IRechnungFLRData flrData) {
		entry.setCurrency(flrData.getCurrency());
		entry.setGrossValue(flrData.getGrossValue());
		entry.setNetValue(flrData.getNetValue());
		entry.setOpenGrossValue(flrData.getOpenGrossValue());
		entry.setCustomerId(flrData.getKundeId());
		entry.setStatus(InvoiceDocumentStatus.fromString(flrData.getStatusCnr()));
		entry.setProject(flrData.getProject());
	}
}
