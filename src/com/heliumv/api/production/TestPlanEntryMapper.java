package com.heliumv.api.production;

import java.math.BigDecimal;
import java.rmi.RemoteException;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.api.partlist.TestTypeEnum;
import com.heliumv.factory.IArtikelCall;
import com.heliumv.factory.IFertigungCall;
import com.heliumv.factory.IStuecklisteCall;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LospruefplanDto;
import com.lp.server.fertigung.service.LossollmaterialDto;
import com.lp.server.stueckliste.service.PruefartDto;
import com.lp.server.stueckliste.service.PruefkombinationDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

public class TestPlanEntryMapper {

	@Autowired
	private IStuecklisteCall stuecklisteCall;
	@Autowired
	private IArtikelCall artikelCall;
	@Autowired
	private IFertigungCall fertigungCall;
	
	protected class ArtikelHolder {
		private ArtikelDto artikelDtoKontakt;
		private ArtikelDto artikelDtoLitze;
		private ArtikelDto artikelDtoLitze2;
		
		public ArtikelDto getArtikelDtoKontakt() {
			return artikelDtoKontakt;
		}
		public void setArtikelDtoKontakt(ArtikelDto artikelDtoKontakt) {
			this.artikelDtoKontakt = artikelDtoKontakt;
		}
		public ArtikelDto getArtikelDtoLitze() {
			return artikelDtoLitze;
		}
		public void setArtikelDtoLitze(ArtikelDto artikelDtoLitze) {
			this.artikelDtoLitze = artikelDtoLitze;
		}
		public ArtikelDto getArtikelDtoLitze2() {
			return artikelDtoLitze2;
		}
		public void setArtikelDtoLitze2(ArtikelDto artikelDtoLitze2) {
			this.artikelDtoLitze2 = artikelDtoLitze2;
		}
		public Integer getArtikelDtoKontaktIId() {
			return getArtikelDtoKontakt() != null ? getArtikelDtoKontakt().getIId() : null;
		}
		public Integer getArtikelDtoLitzeIId() {
			return getArtikelDtoLitze() != null ? getArtikelDtoLitze().getIId() : null;
		}
		public Integer getArtikelDtoLitze2IId() {
			return getArtikelDtoLitze2() != null ? getArtikelDtoLitze2().getIId() : null;
		}
		public boolean isAllNull() {
			return getArtikelDtoKontakt() == null && getArtikelDtoLitze() == null && getArtikelDtoLitze2() == null;
		}
	}
	
	public TestPlanEntry mapEntry(LospruefplanDto pruefplanDto) throws RemoteException, EJBExceptionLP {
		ArtikelHolder artikelHolder = loadData(pruefplanDto);
		PruefkombinationDto pruefkombinationDto = getPruefkombinationDto(pruefplanDto, artikelHolder);

		PruefartDto pruefartDto = stuecklisteCall.pruefartFindByPrimaryKey(pruefplanDto.getPruefartIId());
		TestTypeEnum testType = TestTypeEnum.fromString(pruefartDto.getCNr());
		
		TestPlanEntry testPlanEntry = new TestPlanEntry();
		if (TestTypeEnum.CRIMP_WITH_ISOLATION.equals(testType)) {
			testPlanEntry = mapCrimpingIsolationEntry(artikelHolder, pruefplanDto, pruefkombinationDto);
		} else if (TestTypeEnum.CRIMP_WITHOUT_ISOLATION.equals(testType)) {
			testPlanEntry = mapCrimpEntry(artikelHolder, pruefplanDto, pruefkombinationDto);
		} else if (TestTypeEnum.DIMENSIONAL_TEST.equals(testType)) {
			testPlanEntry = mapDimensionalTestEntry(artikelHolder, pruefplanDto, pruefkombinationDto);
		} else if (TestTypeEnum.ELECTRICAL_TEST.equals(testType)) {
			testPlanEntry = mapElectricalTestEntry(artikelHolder);
		} else if (TestTypeEnum.FORCE_MEASUREMENT.equals(testType)) {
			testPlanEntry = mapForceMeasurementEntry(artikelHolder, pruefkombinationDto);
		} else if (TestTypeEnum.OPTICAL_TEST.equals(testType)) {
			testPlanEntry = mapOpticalTestEntry(artikelHolder);
		} else if (TestTypeEnum.OPEN_TEST.equals(testType)) {
			testPlanEntry = mapOpenTestEntry();
// aktuell nicht ueber die API liefern (PJ20778)
//		} else if (TestTypeEnum.MATERIAL_STATUS.equals(testType)) {
//			testPlanEntry = mapMaterialstatusTestEntry();
		} else {
			throw new IllegalArgumentException("No valid test type '" + testType + "'");
		}
		
		testPlanEntry.setId(pruefplanDto.getIId());
		testPlanEntry.setProductionId(pruefplanDto.getLosIId());
		testPlanEntry.setComment(pruefkombinationDto.getPruefkombinationsprDto() != null ?
				pruefkombinationDto.getPruefkombinationsprDto().getCBez() : null);
		testPlanEntry.setDescription(pruefartDto.getBezeichnung());
		testPlanEntry.setSortOrder(pruefplanDto.getISort());
		
		return testPlanEntry;
	}

	private PruefkombinationDto getPruefkombinationDto(LospruefplanDto pruefplanDto, ArtikelHolder artikelHolder) {
		PruefkombinationDto pruefkombinationDto = null;
		Integer pruefkombinationIId = stuecklisteCall.pruefeObPruefplanInPruefkombinationVorhanden(
				pruefplanDto.getPruefartIId(), artikelHolder.getArtikelDtoKontaktIId(), 
				artikelHolder.getArtikelDtoLitzeIId(), artikelHolder.getArtikelDtoLitze2IId(), 
				pruefplanDto.getVerschleissteilIId(), pruefplanDto.getPruefkombinationId());
		pruefkombinationDto = pruefkombinationIId != null ? 
				stuecklisteCall.pruefkombinationFindByPrimaryKey(pruefkombinationIId) : null;
//		if (!artikelHolder.isAllNull()) {
//			Integer pruefkombinationIId = stuecklisteCall.pruefeObPruefplanInPruefkombinationVorhanden(
//					pruefplanDto.getPruefartIId(), artikelHolder.getArtikelDtoKontaktIId(), 
//					artikelHolder.getArtikelDtoLitzeIId(), artikelHolder.getArtikelDtoLitze2IId(), 
//					pruefplanDto.getVerschleissteilIId(), pruefplanDto.getPruefkombinationId());
//			pruefkombinationDto = pruefkombinationIId != null ? 
//					stuecklisteCall.pruefkombinationFindByPrimaryKey(pruefkombinationIId) : null;
//		} else if (pruefplanDto.getPruefkombinationId() != null) {
//			pruefkombinationDto = stuecklisteCall.pruefkombinationFindByPrimaryKey(pruefplanDto.getPruefkombinationId());
//		}
		
		return pruefkombinationDto != null ? pruefkombinationDto : new PruefkombinationDto();
	}

	private ArtikelHolder loadData(LospruefplanDto pruefplanDto) throws RemoteException, EJBExceptionLP {
		ArtikelHolder holder = new ArtikelHolder();
		if (pruefplanDto.getLossollmaterialIIdKontakt() != null) {
			LossollmaterialDto lossollmaterialDto = fertigungCall.lossollmaterialFindByPrimaryKey(
					pruefplanDto.getLossollmaterialIIdKontakt());
			holder.setArtikelDtoKontakt(artikelCall.artikelFindByPrimaryKeySmall(lossollmaterialDto.getArtikelIId()));
		}
		
		if (pruefplanDto.getLossollmaterialIIdLitze() != null) {
			LossollmaterialDto lossollmaterialDto = fertigungCall.lossollmaterialFindByPrimaryKey(
					pruefplanDto.getLossollmaterialIIdLitze());
			holder.setArtikelDtoLitze(artikelCall.artikelFindByPrimaryKeySmall(lossollmaterialDto.getArtikelIId()));
		}

		if (pruefplanDto.getLossollmaterialIIdLitze2() != null) {
			LossollmaterialDto lossollmaterialDto = fertigungCall.lossollmaterialFindByPrimaryKey(
					pruefplanDto.getLossollmaterialIIdLitze2());
			holder.setArtikelDtoLitze2(artikelCall.artikelFindByPrimaryKeySmall(lossollmaterialDto.getArtikelIId()));
		}

		return holder;
	}
	
	private TestPlanEntry mapForceMeasurementEntry(ArtikelHolder artikelHolder, PruefkombinationDto pruefkombinationDto) {
		TestPlanForceMeasurementEntry entry = new TestPlanForceMeasurementEntry();
		entry.setTestType(TestTypeEnum.FORCE_MEASUREMENT);

		entry.setItemCnrContact(artikelHolder.getArtikelDtoKontakt().getCNr());
		entry.setItemDescriptionContact(artikelHolder.getArtikelDtoKontakt().formatBezeichnung());

		entry.setItemCnrStrand(artikelHolder.getArtikelDtoLitze().getCNr());
		entry.setItemDescriptionStrand(artikelHolder.getArtikelDtoLitze().formatBezeichnung());
		
		entry.setMinimumValue(pruefkombinationDto.getNWert());
		
		return entry;
	}

	private TestPlanElectricalTestEntry mapElectricalTestEntry(ArtikelHolder artikelHolder) {
		TestPlanElectricalTestEntry entry = new TestPlanElectricalTestEntry();
		entry.setTestType(TestTypeEnum.ELECTRICAL_TEST);
		
		if (artikelHolder.getArtikelDtoKontakt() == null) return entry;
		
		entry.setItemCnr(artikelHolder.getArtikelDtoKontakt().getCNr());
		entry.setItemDescription(artikelHolder.getArtikelDtoKontakt().formatBezeichnung());
		
		return entry;
	}
	
	private TestPlanOpticalTestEntry mapOpticalTestEntry(ArtikelHolder artikelHolder) {
		TestPlanOpticalTestEntry entry = new TestPlanOpticalTestEntry();
		entry.setTestType(TestTypeEnum.OPTICAL_TEST);

		if (artikelHolder.getArtikelDtoKontakt() == null) return entry;
		
		entry.setItemCnr(artikelHolder.getArtikelDtoKontakt().getCNr());
		entry.setItemDescription(artikelHolder.getArtikelDtoKontakt().formatBezeichnung());

		return entry;
	}

	private TestPlanDimensionalTestEntry mapDimensionalTestEntry(ArtikelHolder artikelHolder, 
			LospruefplanDto pruefplanDto, PruefkombinationDto pruefkombinationDto) 
			throws RemoteException, EJBExceptionLP {
		TestPlanDimensionalTestEntry entry = new TestPlanDimensionalTestEntry();
		entry.setTestType(TestTypeEnum.DIMENSIONAL_TEST);
		
		if (artikelHolder.getArtikelDtoLitze() != null) {
			entry.setItemCnrStrand(artikelHolder.getArtikelDtoLitze().getCNr());
			entry.setItemDescriptionStrand(artikelHolder.getArtikelDtoLitze().formatBezeichnung());
		}
		
		if (pruefplanDto.getLossollmaterialIIdLitze() != null) {
			LossollmaterialDto lossollmaterialDto = fertigungCall.lossollmaterialFindByPrimaryKey(
					pruefplanDto.getLossollmaterialIIdLitze());
			LosDto losDto = fertigungCall.losFindByPrimaryKeyOhneExc(pruefplanDto.getLosIId());
			entry.setValue(lossollmaterialDto.getNMenge().divide(
					losDto.getNLosgroesse(), BigDecimal.ROUND_HALF_EVEN));
			entry.setTolerance(pruefkombinationDto.getNToleranzWert());
		}
		return entry;
	}

	private TestPlanCrimpIsoEntry mapCrimpingIsolationEntry(ArtikelHolder artikelHolder, 
			LospruefplanDto pruefplanDto, PruefkombinationDto pruefkombinationDto) {
		TestPlanCrimpIsoEntry entry;
		if (Helper.short2boolean(pruefplanDto.getBDoppelanschlag())) {
			entry = new TestPlanMultipleCrimpIsoEntry();
			((TestPlanMultipleCrimpIsoEntry) entry).setItemCnrSecondStrand(artikelHolder.getArtikelDtoLitze2().getCNr());
			((TestPlanMultipleCrimpIsoEntry) entry).setItemDescriptionSecondStrand(artikelHolder.getArtikelDtoLitze2().formatBezeichnung());
			((TestPlanMultipleCrimpIsoEntry) entry).setStrippingForceSecondStrand(pruefkombinationDto.getNAbzugskraftLitze2());
			entry.setTestType(TestTypeEnum.MULTIPLE_CRIMP_WITH_ISOLATION);
		} else {
			entry = new TestPlanCrimpIsoEntry();
			entry.setTestType(TestTypeEnum.CRIMP_WITH_ISOLATION);
		}
		
		setTestPlanCrimpProperties(artikelHolder, pruefkombinationDto, entry);
		entry.setCrimpHeightIsolation(pruefkombinationDto.getNCrimphoeheIsolation());
		entry.setCrimpHeightIsolationTolerance(pruefkombinationDto.getNToleranzCrimphoeheIsolation());
		entry.setCrimpWidthIsolation(pruefkombinationDto.getNCrimpbreiteIsolation());
		entry.setCrimpWidthIsolationTolerance(pruefkombinationDto.getNToleranzCrimpbreiteIsolation());
		
		return entry;
	}

	private TestPlanCrimpEntry mapCrimpEntry(ArtikelHolder artikelHolder, 
			LospruefplanDto pruefplanDto, PruefkombinationDto pruefkombinationDto) {
		TestPlanCrimpEntry entry;
		if (Helper.short2boolean(pruefplanDto.getBDoppelanschlag())) {
			entry = new TestPlanMultipleCrimpEntry();
			((TestPlanMultipleCrimpEntry) entry).setItemCnrSecondStrand(artikelHolder.getArtikelDtoLitze2().getCNr());
			((TestPlanMultipleCrimpEntry) entry).setItemDescriptionSecondStrand(artikelHolder.getArtikelDtoLitze2().formatBezeichnung());
			((TestPlanMultipleCrimpEntry) entry).setStrippingForceSecondStrand(pruefkombinationDto.getNAbzugskraftLitze2());
			entry.setTestType(TestTypeEnum.MULTIPLE_CRIMP_WITHOUT_ISOLATION);
		} else {
			entry = new TestPlanCrimpEntry();
			entry.setTestType(TestTypeEnum.CRIMP_WITHOUT_ISOLATION);
		}
		
		setTestPlanCrimpProperties(artikelHolder, pruefkombinationDto, entry);

		return entry;
	}

	private void setTestPlanCrimpProperties(ArtikelHolder artikelHolder, 
			PruefkombinationDto pruefkombinationDto, TestPlanCrimpEntry entry) {
		entry.setCrimpHeightWire(pruefkombinationDto.getNCrimphoeheDraht());
		entry.setCrimpHeightWireTolerance(pruefkombinationDto.getNToleranzCrimphoeheDraht());
		entry.setCrimpWidthWire(pruefkombinationDto.getNCrimpbreitDraht());
		entry.setCrimpWidthWireTolerance(pruefkombinationDto.getNToleranzCrimpbreitDraht());
		entry.setStrippingForceStrand(pruefkombinationDto.getNAbzugskraftLitze());
		
		entry.setItemCnrContact(artikelHolder.getArtikelDtoKontakt().getCNr());
		entry.setItemDescriptionContact(artikelHolder.getArtikelDtoKontakt().formatBezeichnung());

		entry.setItemCnrStrand(artikelHolder.getArtikelDtoLitze().getCNr());
		entry.setItemDescriptionStrand(artikelHolder.getArtikelDtoLitze().formatBezeichnung());
	}

	private TestPlanEntry mapMaterialstatusTestEntry() {
		return new TestPlanMaterialStatusEntry();
	}

	private TestPlanEntry mapOpenTestEntry() {
		return new TestPlanOpenTestEntry();
	}
}
