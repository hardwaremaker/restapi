package com.heliumv.api.production;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.partlist.TestTypeEnum;

@XmlRootElement
public class TestResultForceMeasurementEntry extends TestResultEntry {

	private BigDecimal value;
	
	public TestResultForceMeasurementEntry() {
		setTestType(TestTypeEnum.FORCE_MEASUREMENT);
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

}
