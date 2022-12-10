package com.heliumv.api.todo;

public enum TodoDetailTravelEnum {
	No(0),
	Passive(1),
	Active(2);
	
	TodoDetailTravelEnum(int value) {
		this.value = value;
	}
	
	public int hvConst() {
		return value;
	}

	public static TodoDetailTravelEnum lookup(int value) {
		for (TodoDetailTravelEnum status : TodoDetailTravelEnum.values()) {
			if (status.hvConst() == value) {
				return status;
			}
		}
		throw new IllegalArgumentException("No enum '" + value + "'");
	}
	
	private int value;
}
