package com.heliumv.api.supplier;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.api.customer.PartnerEntryMapper;
import com.lp.server.partner.service.LieferantDto;

public class SupplierEntryMapper {

	@Autowired
	private PartnerEntryMapper partnerMapper ;

	public SupplierDetailEntry mapDetailEntry(LieferantDto lieferantDto) {
		SupplierDetailEntry supplier = new SupplierDetailEntry();
		
		if (lieferantDto != null) {
			supplier.setId(lieferantDto.getIId());
			if (lieferantDto.getPartnerDto() != null) {
				partnerMapper.mapPartnerEntry(supplier, lieferantDto.getPartnerDto());
			}
		}
		
		return supplier;
	}
}
