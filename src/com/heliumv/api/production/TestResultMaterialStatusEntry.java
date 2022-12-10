package com.heliumv.api.production;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.partlist.TestTypeEnum;

@XmlRootElement
public class TestResultMaterialStatusEntry extends TestResultEntry {

	private TestResultMaterialStatus materialStatus;
	
	public TestResultMaterialStatusEntry() {
		setTestType(TestTypeEnum.MATERIAL_STATUS);
	}
	
	public TestResultMaterialStatus getMaterialStatus() {
		return materialStatus;
	}
	
	public void setMaterialStatus(TestResultMaterialStatus materialStatus) {
		this.materialStatus = materialStatus;
	}
}
