package com.heliumv.api.system;

import com.heliumv.api.BaseEntryId;

public class TaxDescriptionEntry extends BaseEntryId {
	private String description;
//	private BigDecimal tax;
//	private Long validSinceMs;
	
	public TaxDescriptionEntry() {
	}
	
	public TaxDescriptionEntry(Integer flrId) {
		super(flrId);
	}

	/**
	 * Die (in die Client-Sprache &uuml;bersetzte) Mehrwertsteuersatzbezeichnung
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

//	public BigDecimal getTax() {
//		return tax;
//	}
//
//	public void setTax(BigDecimal tax) {
//		this.tax = tax;
//	}
//
//	public Long getValidSinceMs() {
//		return validSinceMs;
//	}
//
//	public void setValidSinceMs(Long validSinceMs) {
//		this.validSinceMs = validSinceMs;
//	}
}
