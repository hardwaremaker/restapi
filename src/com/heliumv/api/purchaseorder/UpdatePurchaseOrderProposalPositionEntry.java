package com.heliumv.api.purchaseorder;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Repr&auml;sentiert die optional zu ver&auml;ndernden Eigenschaften
 * einer Position des Bestellvorschlags
 */
@XmlRootElement
public class UpdatePurchaseOrderProposalPositionEntry {

	private BigDecimal amount;
	private Long deliveryDateMs;
	
	/**
	 * Die zu bestellende Menge der Position
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
