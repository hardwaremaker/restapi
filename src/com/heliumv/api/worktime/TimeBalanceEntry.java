package com.heliumv.api.worktime;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TimeBalanceEntry {
	
	private BigDecimal timeBalance;
	private BigDecimal availableVacation;
	private String unitVacation;
	
	public TimeBalanceEntry() {
	}

	public BigDecimal getTimeBalance() {
		return timeBalance;
	}

	public void setTimeBalance(BigDecimal timeBalance) {
		this.timeBalance = timeBalance;
	}

	public BigDecimal getAvailableVacation() {
		return availableVacation;
	}

	public void setAvailableVacation(BigDecimal availableVacation) {
		this.availableVacation = availableVacation;
	}

	public String getUnitVacation() {
		return unitVacation;
	}

	public void setUnitVacation(String unitVacation) {
		this.unitVacation = unitVacation;
	}

}
