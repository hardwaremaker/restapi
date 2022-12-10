package com.heliumv.api.worktime;

public enum MonthlyReportSelectEnum {
	THIS_PERSON(0),
	ALL(1),
	WORKER(2),
	EMPLOYER(3),
	MY_DEPARTMENT(4);
	
	MonthlyReportSelectEnum(int value) {
		this.value = value;
	}
	
	public int hvConst() {
		return value;
	}
	
	private int value;
}
