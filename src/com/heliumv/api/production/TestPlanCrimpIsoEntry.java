package com.heliumv.api.production;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.partlist.TestTypeEnum;

@XmlRootElement
public class TestPlanCrimpIsoEntry extends TestPlanCrimpEntry {

	private BigDecimal crimpWidthIsolation;
	private BigDecimal crimpHeightIsolation;
	private BigDecimal crimpHeightIsolationTolerance;
	private BigDecimal crimpWidthIsolationTolerance;

	public TestPlanCrimpIsoEntry() {
		setTestType(TestTypeEnum.CRIMP_WITH_ISOLATION);
		setDoublestrike(Boolean.FALSE);
	}

	public BigDecimal getCrimpHeightIsolation() {
		return crimpHeightIsolation;
	}

	public void setCrimpHeightIsolation(BigDecimal crimpHeightIsolation) {
		this.crimpHeightIsolation = crimpHeightIsolation;
	}

	public BigDecimal getCrimpWidthIsolation() {
		return crimpWidthIsolation;
	}

	public void setCrimpWidthIsolation(BigDecimal crimpWidthIsolation) {
		this.crimpWidthIsolation = crimpWidthIsolation;
	}

	public BigDecimal getCrimpHeightIsolationTolerance() {
		return crimpHeightIsolationTolerance;
	}

	public void setCrimpHeightIsolationTolerance(
			BigDecimal crimpHeightIsolationTolerance) {
		this.crimpHeightIsolationTolerance = crimpHeightIsolationTolerance;
	}

	public BigDecimal getCrimpWidthIsolationTolerance() {
		return crimpWidthIsolationTolerance;
	}

	public void setCrimpWidthIsolationTolerance(
			BigDecimal crimpWidthIsolationTolerance) {
		this.crimpWidthIsolationTolerance = crimpWidthIsolationTolerance;
	}

}
