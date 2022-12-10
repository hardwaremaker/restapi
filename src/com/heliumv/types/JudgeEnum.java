package com.heliumv.types;

public enum JudgeEnum {
	/**
	 * Darf Anwender eine Monatsabrechnung drucken?
	 */
	PrintMonthlyReport("PrintMonthlyReport", "PERS_ZEITERFASSUNG_MONATSABRECHNUNG_DRUCKEN")
	;
	
	private JudgeEnum(String externValue, String cnr) {
		this.value = value;
		this.cnr = cnr;
	}
	
	public String getText() {
		return value;
	}
	
	private String value;
	private String cnr;
}
