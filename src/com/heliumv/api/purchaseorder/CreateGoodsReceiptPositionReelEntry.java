package com.heliumv.api.purchaseorder;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Repr&auml;sentiert eine zu liefernde Position
 */
@XmlRootElement
public class CreateGoodsReceiptPositionReelEntry {
	private Integer purchaseOrderPositionId ;
	private String deliverySlipCnr;
	private Integer goodsReceiptId;
	private BigDecimal amount ;
	private String dateCode;
	private String expirationDate;
	private String barcode;
	
	public CreateGoodsReceiptPositionReelEntry() {
	}
	

	
	/**
	 * Die zu lieferende Menge</br>
	 * <p>Bei identit&auml;tsbehafteten Artikeln (also jene mit Serien- oder 
	 * Chargennummer) ist die Gesamtmenge aus den einzelnen Serien- oder Chargennummern
	 * trotzdem anzugeben</p>
	 * @return die gelieferte Menge
	 */
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Integer getPurchaseOrderPositionId() {
		return purchaseOrderPositionId;
	}

	public void setPurchaseOrderPositionId(Integer purchaseOrderPositionId) {
		this.purchaseOrderPositionId = purchaseOrderPositionId;
	}


	/**
	 * Die optionale Lieferscheinnummer des Lieferanten</br>
	 * <p>Entweder die Lieferscheinnummer ist angegeben, oder die Id des Wareneingangs</p>
	 * <p>Ist noch kein Wareneingang mit dieser Lieferscheinnummer bekannt, wird
	 * ein neuer Wareneingang mit der Lieferscheinnummer angelegt</p>
	 *  
	 * @return die optionale Lieferscheinnummer
	 */
	public String getDeliverySlipCnr() {
		return deliverySlipCnr;
	}

	public void setDeliverySlipCnr(String deliverySlipCnr) {
		this.deliverySlipCnr = deliverySlipCnr;
	}

	/**
	 * Die optionale Id des Wareneingangs</br>
	 * <p>Die Wareneingangs-Id hat Vorrang vor der Lieferscheinnummer</p>
	 * @return
	 */
	public Integer getGoodsReceiptId() {
		return goodsReceiptId;
	}


	public void setGoodsReceiptId(Integer goodsReceiptId) {
		this.goodsReceiptId = goodsReceiptId;
	}


	public String getDateCode() {
		return dateCode;
	}


	public void setDateCode(String dateCode) {
		this.dateCode = dateCode;
	}


	public String getExpirationDate() {
		return expirationDate;
	}


	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}


	/** 
	 * Der (optionale) Barcode der zum Buchen dieser Wareneingangsposition verwendet wurde</br>
	 * <p>Jener Barcode, der dazu verwendet wurde, die Felder "DateCode" und "ExpirationDate"
	 * zu ermitteln</p>
	 * @return der f&uuml;r die Buchung verwendete Barcode
	 */
	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
}
