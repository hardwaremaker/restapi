package com.heliumv.api.production;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.partlist.TestTypeEnum;

@XmlRootElement
public class TestPlanMultipleCrimpIsoEntry extends TestPlanCrimpIsoEntry {

	private String itemCnrSecondStrand;
	private String itemDescriptionSecondStrand;

	private BigDecimal strippingForceSecondStrand;

	public TestPlanMultipleCrimpIsoEntry() {
		setTestType(TestTypeEnum.CRIMP_WITH_ISOLATION);
		setDoublestrike(Boolean.TRUE);
	}

	public String getItemCnrSecondStrand() {
		return itemCnrSecondStrand;
	}

	public void setItemCnrSecondStrand(String itemCnrSecondStrand) {
		this.itemCnrSecondStrand = itemCnrSecondStrand;
	}

	public String getItemDescriptionSecondStrand() {
		return itemDescriptionSecondStrand;
	}

	public void setItemDescriptionSecondStrand(String itemDescriptionSecondStrand) {
		this.itemDescriptionSecondStrand = itemDescriptionSecondStrand;
	}

	public BigDecimal getStrippingForceSecondStrand() {
		return strippingForceSecondStrand;
	}

	public void setStrippingForceSecondStrand(BigDecimal strippingForceSecondStrand) {
		this.strippingForceSecondStrand = strippingForceSecondStrand;
	}

}
