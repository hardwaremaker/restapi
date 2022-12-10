package com.heliumv.api.production;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TestResultPostEntry {

	private String userId;
	private Integer productionDeliveryId;
	private TestResultEntryList testResultEntries;
	
	public TestResultPostEntry() {
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getProductionDeliveryId() {
		return productionDeliveryId;
	}

	public void setProductionDeliveryId(Integer productionDeliveryId) {
		this.productionDeliveryId = productionDeliveryId;
	}

	public TestResultEntryList getTestResultEntries() {
		return testResultEntries;
	}

	public void setTestResultEntries(TestResultEntryList testResultEntries) {
		this.testResultEntries = testResultEntries;
	}

}
