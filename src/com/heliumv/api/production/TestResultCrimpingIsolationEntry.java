package com.heliumv.api.production;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.partlist.TestTypeEnum;

@XmlRootElement
public class TestResultCrimpingIsolationEntry extends TestResultCrimpingEntry {

	private BigDecimal crimpHeightIsolation;
	private BigDecimal crimpWidthIsolation;

	public TestResultCrimpingIsolationEntry() {
		setTestType(TestTypeEnum.CRIMP_WITH_ISOLATION);
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

}
