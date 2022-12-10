package com.heliumv.api.system;

public class CnrFormat {
	
	private Integer digitsReceiptNumber;
	private Integer digitsBusinessYear;
	private Integer digitsRandomNumber;
	private Integer digitsTenantIdentifier;
	private String tag;
	private String tenantIdentifier;

	public CnrFormat(Integer digitsReceiptNumber, Integer digitsBusinessYear, Integer digitsRandomNumber, String tag) {
		setDigitsReceiptNumber(digitsReceiptNumber);
		setDigitsBusinessYear(digitsBusinessYear);
		setDigitsRandomNumber(digitsRandomNumber);
		setTag(tag);
	}

	public CnrFormat(Integer digitsReceiptNumber, Integer digitsBusinessYear, Integer digitsRandomNumber,
			String tag, Integer digitsTenantIdentifier, String tenantIdentifier) {
		this(digitsReceiptNumber, digitsBusinessYear, digitsRandomNumber, tag);
		setDigitsTenantIdentifier(digitsTenantIdentifier);
		setTenantIdentifier(tenantIdentifier);
	}

	public Integer getDigitsReceiptNumber() {
		return digitsReceiptNumber;
	}
	
	public void setDigitsReceiptNumber(Integer digitsReceiptNumber) {
		this.digitsReceiptNumber = digitsReceiptNumber;
	}
	
	public Integer getDigitsBusinessYear() {
		return digitsBusinessYear;
	}
	
	public void setDigitsBusinessYear(Integer digitsBusinessYear) {
		this.digitsBusinessYear = digitsBusinessYear;
	}
	
	public Integer getDigitsRandomNumber() {
		return digitsRandomNumber;
	}
	
	public void setDigitsRandomNumber(Integer digitsRandomNumber) {
		this.digitsRandomNumber = digitsRandomNumber;
	}
	
	public Integer getDigitsTenantIdentifier() {
		return digitsTenantIdentifier;
	}
	
	public void setDigitsTenantIdentifier(Integer digitsTenantIdentifier) {
		this.digitsTenantIdentifier = digitsTenantIdentifier;
	}
	
	public String getTag() {
		return tag;
	}
	
	public void setTag(String tag) {
		this.tag = tag;
	}
	
	public String getTenantIdentifier() {
		return tenantIdentifier;
	}
	
	public void setTenantIdentifier(String tenantIdentifier) {
		this.tenantIdentifier = tenantIdentifier;
	}
	
	public Integer getLength() {
		Integer length = new Integer(0);
		length += getDigitsBusinessYear() != null ? getDigitsBusinessYear() : 0;
		length += getDigitsRandomNumber() != null ? getDigitsRandomNumber() : 0;
		length += getDigitsReceiptNumber() != null ? getDigitsReceiptNumber() : 0;
		length += getTag() != null ? getTag().length() : 0;
		length += getDigitsTenantIdentifier() != null ? getDigitsTenantIdentifier() : 0;
		
		return length;
	}
}
