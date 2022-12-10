package com.heliumv.api.production;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.partlist.TestTypeEnum;

@XmlRootElement
public class TestPlanElectricalTestEntry extends TestPlanEntry {

	private String itemCnr;
	private String itemDescription;

	public TestPlanElectricalTestEntry() {
		setTestType(TestTypeEnum.ELECTRICAL_TEST);
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
