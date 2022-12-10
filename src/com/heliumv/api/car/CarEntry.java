package com.heliumv.api.car;

import com.heliumv.api.BaseEntryId;

public class CarEntry extends BaseEntryId {
	private String description;
	private String numberPlate;

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getNumberPlate() {
		return numberPlate;
	}
	public void setNumberPlate(String numberPlate) {
		this.numberPlate = numberPlate;
	}
}
