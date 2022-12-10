package com.heliumv.api.purchaseorder;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.BaseEntryId;

@XmlRootElement
public class PurchaseOrderProposalPositionEntry extends BaseEntryId {
	private String itemCnr;
	private BigDecimal amount;
	private Boolean noted;
	private Long deliveryDateMs;

	/**
	 * Artikelnummer der Position
	 * 
	 * @return Artikelnummer
	 */
	public String getItemCnr() {
		return itemCnr;
	}
	public void setItemCnr(String itemCnr) {
		this.itemCnr = itemCnr;
	}
	
	/**
	 * Bestellmenge der Position
	 * 
	 * @return die zu bestellende Menge
	 */
	public BigDecimal getAmount() {
		return amount;
	}
	
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	/**
	 * Wurde dieser Artikel vorgemerkt?
	 * 
	 * @return <code>true<code>, wenn die Position vorgemerkt wurde
	 */
	public Boolean isNoted() {
		return noted;
	}
	public void setNoted(Boolean noted) {
		this.noted = noted;
	}
	
	/**
	 * Der Liefertermin der Position
	 * 
	 * @return Liefertermin der Position
	 */
	public Long getDeliveryDateMs() {
		return deliveryDateMs;
	}
	public void setDeliveryDateMs(Long deliveryDateMs) {
		this.deliveryDateMs = deliveryDateMs;
	}
}
