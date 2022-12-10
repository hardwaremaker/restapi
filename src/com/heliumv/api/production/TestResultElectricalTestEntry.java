package com.heliumv.api.production;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.partlist.TestTypeEnum;

@XmlRootElement
public class TestResultElectricalTestEntry extends TestResultEntry {

	private Boolean executed;
	
	public TestResultElectricalTestEntry() {
		setTestType(TestTypeEnum.ELECTRICAL_TEST);
	}

	public Boolean getExecuted() {
		return executed;
	}

	public void setExecuted(Boolean executed) {
		this.executed = executed;
	}

}
