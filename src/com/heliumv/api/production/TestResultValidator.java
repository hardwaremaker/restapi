package com.heliumv.api.production;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.api.HvValidateBadRequest;
import com.heliumv.api.HvValidateNotFound;
import com.heliumv.api.partlist.TestTypeEnum;
import com.heliumv.factory.IFertigungServiceCall;
import com.lp.server.fertigung.service.LospruefplanDto;

public class TestResultValidator {

	@Autowired
	private IFertigungServiceCall fertigungServiceCall;
	
	public TestResultValidator() {
	}

	public void validate(TestResultEntry testResultEntry) {
		HvValidateBadRequest.notNull(testResultEntry.getTestType(), "testType");
		
		if (TestTypeEnum.CRIMP_WITH_ISOLATION.equals(testResultEntry.getTestType())
				&& testResultEntry instanceof TestResultCrimpingIsolationEntry) {
			new TestResultCrimpingIsolationValidator().validate((TestResultCrimpingIsolationEntry) testResultEntry);
			
		} else if (TestTypeEnum.CRIMP_WITHOUT_ISOLATION.equals(testResultEntry.getTestType())
				&& testResultEntry instanceof TestResultCrimpingEntry) {
			new TestResultCrimpingTestValidator().validate((TestResultCrimpingEntry) testResultEntry);
			
		} else if (TestTypeEnum.DIMENSIONAL_TEST.equals(testResultEntry.getTestType())
				&& testResultEntry instanceof TestResultDimensionalTestEntry) {
			new TestResultDimensonalTestValidator().validate((TestResultDimensionalTestEntry) testResultEntry);
			
		} else if (TestTypeEnum.ELECTRICAL_TEST.equals(testResultEntry.getTestType())
				&& testResultEntry instanceof TestResultElectricalTestEntry) {
			new TestResultElectricalTestValidator().validate((TestResultElectricalTestEntry) testResultEntry);
			
		} else if (TestTypeEnum.FORCE_MEASUREMENT.equals(testResultEntry.getTestType())
				&& testResultEntry instanceof TestResultForceMeasurementEntry) {
			new TestResultForceMeasurementValidator().validate((TestResultForceMeasurementEntry) testResultEntry);
			
		} else if (TestTypeEnum.OPTICAL_TEST.equals(testResultEntry.getTestType())
				&& testResultEntry instanceof TestResultOpticalTestEntry) {
			new TestResultOpticalTestValidator().validate((TestResultOpticalTestEntry) testResultEntry);
			
		} else if (TestTypeEnum.MATERIAL_STATUS.equals(testResultEntry.getTestType())
				&& testResultEntry instanceof TestResultMaterialStatusEntry) {
			new TestResultMaterialStatusTestValidator().validate((TestResultMaterialStatusEntry)testResultEntry);
			
		} else if (TestTypeEnum.OPEN_TEST.equals(testResultEntry.getTestType())
				&& testResultEntry instanceof TestResultOpenTestEntry) {
			new TestResultOpenTestValidator().validate((TestResultOpenTestEntry) testResultEntry);
			
		} else {
			HvValidateBadRequest.notValid(false, testResultEntry.getTestType().getText(), "testType");
		}
		
	}

	protected class TestResultBaseValidator<T extends TestResultEntry> {
		
		public void validate(T testResultEntry) {
			HvValidateBadRequest.notNull(testResultEntry.getProductionTestPlanId(), "productionTestPlanId");
			LospruefplanDto pruefplanDto = fertigungServiceCall.lospruefplanFindByPrimaryKey(
					testResultEntry.getProductionTestPlanId());
			HvValidateNotFound.notNull(pruefplanDto == null || pruefplanDto.getIId() == null ? null : "", 
					"productionTestPlanId", testResultEntry.getProductionTestPlanId());
		}
	}
	
	protected class TestResultCrimpingTestValidator extends TestResultBaseValidator<TestResultCrimpingEntry> {
		
		@Override
		public void validate(TestResultCrimpingEntry testResultEntry) {
			super.validate(testResultEntry);
			HvValidateBadRequest.notNull(testResultEntry.getCrimpHeightWire(), "crimpHeightWire");
			HvValidateBadRequest.notNull(testResultEntry.getCrimpWidthWire(), "crimpWidthWire");
//			HvValidateBadRequest.notNull(testResultEntry.getStrippingForceSecondStrand(), "strippingForceSecondStrand");
//			HvValidateBadRequest.notNull(testResultEntry.getStrippingForceStrand(), "strippingForceStrand");
		}
	}
	
	protected class TestResultCrimpingIsolationValidator extends TestResultBaseValidator<TestResultCrimpingIsolationEntry> {
		@Override
		public void validate(TestResultCrimpingIsolationEntry testResultEntry) {
			super.validate(testResultEntry);
			HvValidateBadRequest.notNull(testResultEntry.getCrimpHeightWire(), "crimpHeightWire");
			HvValidateBadRequest.notNull(testResultEntry.getCrimpWidthWire(), "crimpWidthWire");
			HvValidateBadRequest.notNull(testResultEntry.getCrimpHeightIsolation(), "crimpHeightIsolation");
			HvValidateBadRequest.notNull(testResultEntry.getCrimpWidthIsolation(), "crimpWidthIsolation");
//			HvValidateBadRequest.notNull(testResultEntry.getStrippingForceSecondStrand(), "strippingForceSecondStrand");
//			HvValidateBadRequest.notNull(testResultEntry.getStrippingForceStrand(), "strippingForceStrand");
		}
	}
	
	protected class TestResultDimensonalTestValidator extends TestResultBaseValidator<TestResultDimensionalTestEntry> {
		@Override
		public void validate(TestResultDimensionalTestEntry testResultEntry) {
			super.validate(testResultEntry);
			HvValidateBadRequest.notNull(testResultEntry.getValue(), "value");
		}
	}

	protected class TestResultForceMeasurementValidator extends TestResultBaseValidator<TestResultForceMeasurementEntry> {
		@Override
		public void validate(TestResultForceMeasurementEntry testResultEntry) {
			super.validate(testResultEntry);
			HvValidateBadRequest.notNull(testResultEntry.getValue(), "value");
		}
	}

	protected class TestResultElectricalTestValidator extends TestResultBaseValidator<TestResultElectricalTestEntry> {
		@Override
		public void validate(TestResultElectricalTestEntry testResultEntry) {
			super.validate(testResultEntry);
			HvValidateBadRequest.notNull(testResultEntry.getExecuted(), "executed");
		}
	}

	protected class TestResultOpticalTestValidator extends TestResultBaseValidator<TestResultOpticalTestEntry> {
		@Override
		public void validate(TestResultOpticalTestEntry testResultEntry) {
			super.validate(testResultEntry);
			HvValidateBadRequest.notNull(testResultEntry.getExecuted(), "executed");
		}
	}
	
	protected class TestResultMaterialStatusTestValidator extends TestResultBaseValidator<TestResultMaterialStatusEntry> {
		@Override
		public void validate(TestResultMaterialStatusEntry testResultEntry) {
			super.validate(testResultEntry);
			HvValidateBadRequest.notNull(testResultEntry.getMaterialStatus(), "materialStatus");
		}
	}

	protected class TestResultOpenTestValidator extends TestResultBaseValidator<TestResultOpenTestEntry> {
		@Override
		public void validate(TestResultOpenTestEntry testResultEntry) {
			super.validate(testResultEntry);
			HvValidateBadRequest.notNull(testResultEntry.getExecuted(), "executed");
		}
	}
}
