package com.heliumv.api.purchaseorder;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.BaseEntryId;

@XmlRootElement
public class GoodsReceiptEntry extends BaseEntryId {
	private String deliverySlipCnr;
	private Integer purchaseOrderId;
	
	public String getDeliverySlipCnr() {
		return deliverySlipCnr;
	}

	/**
	 * Die Lieferscheinnummer des Lieferanten
	 * @param deliverySlipCnr die Lieferscheinnr des Lieferanten
	 */
	public void setDeliverySlipCnr(String deliverySlipCnr) {
		this.deliverySlipCnr = deliverySlipCnr;
	}

	/**
	 * Die Id der zugrundeliegenden Bestellung
	 * @return die Id der zugrundeliegenden Bestellung
	 */
	public Integer getPurchaseOrderId() {
		return purchaseOrderId;
	}

	public void setPurchaseOrderId(Integer purchaseOrderId) {
		this.purchaseOrderId = purchaseOrderId;
	}

}
