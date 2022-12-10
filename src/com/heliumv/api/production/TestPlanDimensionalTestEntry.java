package com.heliumv.api.production;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.partlist.TestTypeEnum;

@XmlRootElement
public class TestPlanDimensionalTestEntry extends TestPlanEntry {

	private String itemCnrStrand;
	private String itemDescriptionStrand;
	
	private BigDecimal value;
	private BigDecimal tolerance;

	public TestPlanDimensionalTestEntry() {
		setTestType(TestTypeEnum.DIMENSIONAL_TEST);
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

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public BigDecimal getTolerance() {
		return tolerance;
	}

	public void setTolerance(BigDecimal tolerance) {
		this.tolerance = tolerance;
	}

}
