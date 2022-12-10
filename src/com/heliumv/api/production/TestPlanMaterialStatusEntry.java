package com.heliumv.api.production;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.partlist.TestTypeEnum;

@XmlRootElement
public class TestPlanMaterialStatusEntry extends TestPlanEntry {

	public TestPlanMaterialStatusEntry() {
		setTestType(TestTypeEnum.MATERIAL_STATUS);
	}

}
