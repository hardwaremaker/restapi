package com.heliumv.api.worktime;

public enum MonthlyReportSortEnum {
	PERSONALCNR(0),
	NAME(1),
	DEPARTMENT(2),
	COSTCENTER(3),
	DEPARTMENT_COSTCENTER_NAME(4);
	
	MonthlyReportSortEnum(int value) {
		this.value = value;
	}
	
	public int hvConst() {
		return value;
	}

	private int value;
}

