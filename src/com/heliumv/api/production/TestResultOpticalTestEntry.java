package com.heliumv.api.production;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.partlist.TestTypeEnum;

@XmlRootElement
public class TestResultOpticalTestEntry extends TestResultEntry {

	private Boolean executed;
	
	public TestResultOpticalTestEntry() {
		setTestType(TestTypeEnum.OPTICAL_TEST);
	}

	public Boolean getExecuted() {
		return executed;
	}

	public void setExecuted(Boolean executed) {
		this.executed = executed;
	}

}
