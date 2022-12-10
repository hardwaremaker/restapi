package com.heliumv.api.worktime;

import com.lp.server.personal.service.ZeitsaldoDto;

public class TimeBalanceEntryMapper {

	public TimeBalanceEntry mapEntry(ZeitsaldoDto zeitsaldoDto) {
		TimeBalanceEntry entry = new TimeBalanceEntry();
		entry.setTimeBalance(zeitsaldoDto.getNSaldo());
		entry.setAvailableVacation(zeitsaldoDto.getNVerfuegbarerurlaub());
		entry.setUnitVacation(zeitsaldoDto.getEinheitVerfuegbarerUrlaub());
		
		return entry;
	}

}
