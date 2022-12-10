package com.heliumv.api.production;

import com.heliumv.api.partlist.TestTypeEnum;

public class TestPlanOpticalTestEntry extends TestPlanEntry {

	private String itemCnr;
	private String itemDescription;

	public TestPlanOpticalTestEntry() {
		setTestType(TestTypeEnum.OPTICAL_TEST);
	}

	public String getItemCnr() {
		return itemCnr;
	}

	public void setItemCnr(String itemCnr) {
		this.itemCnr = itemCnr;
	}

	public String getItemDescription() {
		return itemDescription;
	}

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

}
