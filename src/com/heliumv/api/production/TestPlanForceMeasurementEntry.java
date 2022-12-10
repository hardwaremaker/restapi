package com.heliumv.api.production;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.partlist.TestTypeEnum;

@XmlRootElement
public class TestPlanForceMeasurementEntry extends TestPlanEntry {

	private String itemCnrContact;
	private String itemDescriptionContact;
	
	private String itemCnrStrand;
	private String itemDescriptionStrand;
	
	private BigDecimal minimumValue;

	public TestPlanForceMeasurementEntry() {
		setTestType(TestTypeEnum.FORCE_MEASUREMENT);
	}

	public String getItemCnrContact() {
		return itemCnrContact;
	}

	public void setItemCnrContact(String itemCnrContact) {
		this.itemCnrContact = itemCnrContact;
	}

	public String getItemDescriptionContact() {
		return itemDescriptionContact;
	}

	public void setItemDescriptionContact(String itemDescriptionContact) {
		this.itemDescriptionContact = itemDescriptionContact;
	}

	public String getItemCnrStrand() {
		return itemCnrStrand;
	}

	public void setItemCnrStrand(String itemCnrStrand) {
		this.itemCnrStrand = itemCnrStrand;
	}

	public String getItemDescriptionStrand() {
		return itemDescriptionStrand;
	}

	public void setItemDescriptionStrand(String itemDescriptionStrand) {
		this.itemDescriptionStrand = itemDescriptionStrand;
	}

	public BigDecimal getMinimumValue() {
		return minimumValue;
	}

	public void setMinimumValue(BigDecimal minimumValue) {
		this.minimumValue = minimumValue;
	}

}
