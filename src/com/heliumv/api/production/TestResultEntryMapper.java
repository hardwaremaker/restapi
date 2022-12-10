package com.heliumv.api.production;

import java.rmi.RemoteException;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.api.partlist.TestTypeEnum;
import com.heliumv.factory.ISystemMultilanguageCall;
import com.lp.server.fertigung.service.PruefergebnisDto;
import com.lp.util.Helper;

public class TestResultEntryMapper {

	@Autowired
	private ISystemMultilanguageCall systemMultilanguageCall;
	
	public PruefergebnisDto mapDto(TestResultEntry entry, Integer deliveryId) {
		PruefergebnisDto pruefergebnisDto = new PruefergebnisDto();
		
		pruefergebnisDto.setLosablieferungIId(deliveryId);
		pruefergebnisDto.setLospruefplanIId(entry.getProductionTestPlanId());
		
		if (TestTypeEnum.CRIMP_WITH_ISOLATION.equals(entry.getTestType())) {
			mapDto(pruefergebnisDto, (TestResultCrimpingIsolationEntry)entry);
			
		} else if (TestTypeEnum.CRIMP_WITHOUT_ISOLATION.equals(entry.getTestType())) {
			mapDto(pruefergebnisDto, (TestResultCrimpingEntry)entry);
			
		} else if (TestTypeEnum.DIMENSIONAL_TEST.equals(entry.getTestType())) {
			mapDto(pruefergebnisDto, (TestResultDimensionalTestEntry)entry);
			
		} else if (TestTypeEnum.ELECTRICAL_TEST.equals(entry.getTestType())) {
			mapDto(pruefergebnisDto, (TestResultElectricalTestEntry)entry);
			
		} else if (TestTypeEnum.FORCE_MEASUREMENT.equals(entry.getTestType())) {
			mapDto(pruefergebnisDto, (TestResultForceMeasurementEntry)entry);
			
		} else if (TestTypeEnum.OPTICAL_TEST.equals(entry.getTestType())) {
			mapDto(pruefergebnisDto, (TestResultOpticalTestEntry)entry);

		} else if (TestTypeEnum.MATERIAL_STATUS.equals(entry.getTestType())) {
			mapDto(pruefergebnisDto, (TestResultMaterialStatusEntry)entry);
			
		} else if (TestTypeEnum.OPEN_TEST.equals(entry.getTestType())) {
			mapDto(pruefergebnisDto, (TestResultOpenTestEntry)entry);
		}
		
		return pruefergebnisDto;
	}
	
	private void mapDto(PruefergebnisDto dto, TestResultMaterialStatusEntry entry) {
		try {
			String status = systemMultilanguageCall.getTextRespectUISpr(
					"fert.pruefergebnis.materialstatus." + entry.getMaterialStatus().name().toLowerCase());
			dto.setCWert(status);
		} catch (RemoteException e) {
		}
	}

	private void mapDto(PruefergebnisDto dto, TestResultOpticalTestEntry entry) {
		dto.setBWert(Helper.boolean2Short(entry.getExecuted().booleanValue()));
	}

	private void mapDto(PruefergebnisDto dto, TestResultForceMeasurementEntry entry) {
		dto.setNWert(entry.getValue());
	}

	private void mapDto(PruefergebnisDto dto, TestResultElectricalTestEntry entry) {
		dto.setBWert(Helper.boolean2Short(entry.getExecuted().booleanValue()));
	}

	private void mapDto(PruefergebnisDto dto, TestResultDimensionalTestEntry entry) {
		dto.setNWert(entry.getValue());
	}

	private void mapDto(PruefergebnisDto dto, TestResultCrimpingEntry entry) {
		dto.setNCrimpbreitDraht(entry.getCrimpWidthWire());
		dto.setNCrimphoeheDraht(entry.getCrimpHeightWire());
		dto.setNAbzugskraftLitze(entry.getStrippingForceStrand());
		dto.setNAbzugskraftLitze2(entry.getStrippingForceSecondStrand());
	}

	private void mapDto(PruefergebnisDto dto, TestResultCrimpingIsolationEntry entry) {
		mapDto(dto, (TestResultCrimpingEntry)entry);
		dto.setNCrimpbreiteIsolation(entry.getCrimpWidthIsolation());
		dto.setNCrimphoeheIsolation(entry.getCrimpHeightIsolation());
	}

	private void mapDto(PruefergebnisDto dto, TestResultOpenTestEntry entry) {
		dto.setBWert(Helper.boolean2Short(entry.getExecuted().booleanValue()));
	}
}
