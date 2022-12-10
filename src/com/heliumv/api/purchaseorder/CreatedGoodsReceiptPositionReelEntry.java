package com.heliumv.api.purchaseorder;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Repr&auml;sentiert eine zu liefernde Position
 */
@XmlRootElement
public class CreatedGoodsReceiptPositionReelEntry {
	private Integer purchaseOrderPositionId ;
	private Integer goodsReceiptId;
	private Integer goodsReceiptPositionId;	
	private BigDecimal openAmount ;
	private String identity;
	
	/**
	 * Die noch offene Menge der Bestellung</br>
	 * @return die noch offene (noch nicht gelieferte) Menge
	 */
	public BigDecimal getOpenQuantity() {
		return openAmount;
	}
	public void setOpenQuantity(BigDecimal amount) {
		this.openAmount = amount;
	}

	public Integer getPurchaseOrderPositionId() {
		return purchaseOrderPositionId;
	}

	public void setPurchaseOrderPositionId(Integer purchaseOrderPositionId) {
		this.purchaseOrderPositionId = purchaseOrderPositionId;
	}


	/**
	 * Die Id des Wareneingangs</br>
	 * @return die Id des Wareneingangs
	 */
	public Integer getGoodsReceiptId() {
		return goodsReceiptId;
	}


	public void setGoodsReceiptId(Integer goodsReceiptId) {
		this.goodsReceiptId = goodsReceiptId;
	}
	
	/**
	 * Die Id der Wareneingangsposition
	 * @return die Id der gerade erzeugten/modifizierten Wareneingangsposition
	 */
	public Integer getGoodsReceiptPositionId() {
		return goodsReceiptPositionId;
	}
	public void setGoodsReceiptPositionId(Integer goodsReceiptPositionId) {
		this.goodsReceiptPositionId = goodsReceiptPositionId;
	}
	public String getIdentity() {
		return identity;
	}
	public void setIdentity(String identity) {
		this.identity = identity;
	}
}
