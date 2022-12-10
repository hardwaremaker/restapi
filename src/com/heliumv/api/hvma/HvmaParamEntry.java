package com.heliumv.api.hvma;

import com.heliumv.api.BaseEntryCnr;

public class HvmaParamEntry extends BaseEntryCnr {
	private String category;
	private String value;
	
	public HvmaParamEntry() {
	}
	
	public HvmaParamEntry(String cnr) {
		super(cnr);
	}
	
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
