package com.heliumv.api.production;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;

import com.heliumv.api.partlist.TestTypeEnum;

@XmlRootElement
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "testType")
@JsonSubTypes({
    @JsonSubTypes.Type(value = TestResultCrimpingIsolationEntry.class, name = "CRIMP_WITH_ISOLATION"),
    @JsonSubTypes.Type(value = TestResultMultipleCrimpingIsolationEntry.class, name = "MULTIPLE_CRIMP_WITH_ISOLATION"), 
    @JsonSubTypes.Type(value = TestResultCrimpingEntry.class, name = "CRIMP_WITHOUT_ISOLATION"), 
    @JsonSubTypes.Type(value = TestResultMultipleCrimpingEntry.class, name = "MULTIPLE_CRIMP_WITHOUT_ISOLATION"), 
    @JsonSubTypes.Type(value = TestResultOpticalTestEntry.class, name = "OPTICAL_TEST"), 
    @JsonSubTypes.Type(value = TestResultElectricalTestEntry.class, name = "ELECTRICAL_TEST"), 
    @JsonSubTypes.Type(value = TestResultForceMeasurementEntry.class, name = "FORCE_MEASUREMENT"), 
    @JsonSubTypes.Type(value = TestResultDimensionalTestEntry.class, name = "DIMENSIONAL_TEST"), 
    @JsonSubTypes.Type(value = TestResultMaterialStatusEntry.class, name = "MATERIAL_STATUS"),
    @JsonSubTypes.Type(value = TestResultOpenTestEntry.class, name = "OPEN_TEST") 
})
public class TestResultEntry {

	private Integer productionTestPlanId;
	private TestTypeEnum testType;

	public TestResultEntry() {
	}

	public Integer getProductionTestPlanId() {
		return productionTestPlanId;
	}

	public void setProductionTestPlanId(Integer productionTestPlanId) {
		this.productionTestPlanId = productionTestPlanId;
	}

	public TestTypeEnum getTestType() {
		return testType;
	}

	public void setTestType(TestTypeEnum testType) {
		this.testType = testType;
	}
}
