package com.heliumv.factory.impl;

import java.math.BigDecimal;
import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.factory.BaseCall;
import com.heliumv.factory.IGlobalInfo;
import com.heliumv.factory.IMaterialCall;
import com.lp.server.artikel.service.MaterialFac;

public class MaterialCall extends BaseCall<MaterialFac> implements IMaterialCall {
	@Autowired
	private IGlobalInfo globalInfo ;

	public MaterialCall() {
		super(MaterialFac.class);
	}

	@Override
	public BigDecimal getMaterialzuschlagVKInZielwaehrung(Integer artikelIId,
			Date datGueltigkeitsdatumI, String waehrungCNrZielwaehrung) {
		return getFac().getMaterialzuschlagVKInZielwaehrung(artikelIId,null, 
			datGueltigkeitsdatumI, waehrungCNrZielwaehrung, globalInfo.getTheClientDto()) ;
	}
}
