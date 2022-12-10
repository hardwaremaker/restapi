package com.heliumv.api.production;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.partlist.TestTypeEnum;

@XmlRootElement
public class TestPlanCrimpEntry extends TestPlanEntry {

	private Boolean doublestrike;
	private String itemCnrContact;
	private String itemDescriptionContact;
	
	private String itemCnrStrand;
	private String itemDescriptionStrand;
	
	private BigDecimal crimpHeightWire;
	private BigDecimal crimpWidthWire;
	private BigDecimal strippingForceStrand;
	private BigDecimal crimpHeightWireTolerance;
	private BigDecimal crimpWidthWireTolerance;

	public TestPlanCrimpEntry() {
		setTestType(TestTypeEnum.CRIMP_WITHOUT_ISOLATION);
		setDoublestrike(Boolean.FALSE);
	}

	public String getItemCnrContact() {
		return itemCnrContact;
	}

	public void setItemCnrContact(String itemCnrContact) {
		this.itemCnrContact = itemCnrContact;
	}

	public String getItemDescriptionContact() {
		return itemDescriptionContact;
	}

	public void setItemDescriptionContact(String itemDescriptionContact) {
		this.itemDescriptionContact = itemDescriptionContact;
	}

	public String getItemCnrStrand() {
		return itemCnrStrand;
	}

	public void setItemCnrStrand(String itemCnrStrand) {
		this.itemCnrStrand = itemCnrStrand;
	}

	public String getItemDescriptionStrand() {
		return itemDescriptionStrand;
	}

	public void setItemDescriptionStrand(String itemDescriptionStrand) {
		this.itemDescriptionStrand = itemDescriptionStrand;
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

	public BigDecimal getCrimpHeightWireTolerance() {
		return crimpHeightWireTolerance;
	}

	public void setCrimpHeightWireTolerance(BigDecimal crimpHeightWireTolerance) {
		this.crimpHeightWireTolerance = crimpHeightWireTolerance;
	}

	public BigDecimal getCrimpWidthWireTolerance() {
		return crimpWidthWireTolerance;
	}

	public void setCrimpWidthWireTolerance(BigDecimal crimpWidthWireTolerance) {
		this.crimpWidthWireTolerance = crimpWidthWireTolerance;
	}

	public Boolean getDoublestrike() {
		return doublestrike;
	}

	public void setDoublestrike(Boolean doublestrike) {
		this.doublestrike = doublestrike;
	}

}
