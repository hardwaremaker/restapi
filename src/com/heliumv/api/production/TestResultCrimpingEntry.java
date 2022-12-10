package com.heliumv.api.production;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.partlist.TestTypeEnum;

@XmlRootElement
public class TestResultCrimpingEntry extends TestResultEntry {

	private BigDecimal crimpHeightWire;
	private BigDecimal crimpWidthWire;
	private BigDecimal strippingForceStrand;
	private BigDecimal strippingForceSecondStrand;

	public TestResultCrimpingEntry() {
		setTestType(TestTypeEnum.CRIMP_WITHOUT_ISOLATION);
	}

	public BigDecimal getCrimpHeightWire() {
		return crimpHeightWire;
	}

	public void setCrimpHeightWire(BigDecimal crimpHeightWire) {
		this.crimpHeightWire = crimpHeightWire;
	}

	public BigDecimal getCrimpWidthWire() {
		return crimpWidthWire;
	}

	public void setCrimpWidthWire(BigDecimal crimpWidthWire) {
		this.crimpWidthWire = crimpWidthWire;
	}

	public BigDecimal getStrippingForceStrand() {
		return strippingForceStrand;
	}

	public void setStrippingForceStrand(BigDecimal strippingForceStrand) {
		this.strippingForceStrand = strippingForceStrand;
	}

	public BigDecimal getStrippingForceSecondStrand() {
		return strippingForceSecondStrand;
	}

	public void setStrippingForceSecondStrand(BigDecimal strippingForceSecondStrand) {
		this.strippingForceSecondStrand = strippingForceSecondStrand;
	}

}
