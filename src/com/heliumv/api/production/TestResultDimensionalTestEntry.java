package com.heliumv.api.production;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.partlist.TestTypeEnum;

@XmlRootElement
public class TestResultDimensionalTestEntry extends TestResultEntry {

	private BigDecimal value;
	
	public TestResultDimensionalTestEntry() {
		setTestType(TestTypeEnum.DIMENSIONAL_TEST);
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

}
