package com.heliumv.api.item;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class PackagingInfoEntry {
	private String barcode ;
	private BigDecimal amount ;
	
	public PackagingInfoEntry() {
	}
	
	public PackagingInfoEntry(String barcode, BigDecimal amount) {
		this.barcode = barcode ;
		this.amount = amount ;
	}
	
	/**
	 * Der (optionale) Barcode</br>
	 * <p>Diese Property ist nicht gesetzt, wenn der Artikel in einer
	 * Verpackungsmenge vorliegt</p>
	 * @return null oder der Barcode
	 */
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	
	/**
	 * Die Menge des Artikels innerhalb der Verpackung</br>
	 * <p>Die Kiste Mineralwasser hat den Barcode "ABC" und 
	 * ent&auml;lt 6 Flaschen.</p> 
	 * @return die Verpackungsmenge (hier also 6)
	 */
	public BigDecimal getAmount() {
		return amount;
	}
	
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
}
