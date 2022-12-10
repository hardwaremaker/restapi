package com.heliumv.factory;

import java.math.BigDecimal;
import java.sql.Date;

public interface IMaterialCall {
	BigDecimal getMaterialzuschlagVKInZielwaehrung(Integer artikelIId,
			Date datGueltigkeitsdatumI, String waehrungCNrZielwaehrung) ;

}
