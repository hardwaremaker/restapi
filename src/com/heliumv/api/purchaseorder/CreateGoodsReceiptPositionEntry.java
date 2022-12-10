package com.heliumv.api.purchaseorder;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.item.ItemIdentityEntryList;

/**
 * Repr&auml;sentiert eine zu liefernde Position
 */
@XmlRootElement
public class CreateGoodsReceiptPositionEntry {
	private Integer purchaseOrderPositionId ;
	private String deliverySlipCnr;
	private Integer goodsReceiptId;
	private ItemIdentityEntryList itemIdentity ;	
	private BigDecimal amount ;
	
	public CreateGoodsReceiptPositionEntry() {
		setItemIdentity(new ItemIdentityEntryList());
	}
	

	/**
	 * Wenn es sich um einen Artikel mit Serien- oder Chargennr handelt, ist
	 * hier die jeweilige Identit&auml;tsinformation (Seriennr(n) mit Menge(n),
	 * Chargennummern mit den jeweiligen Mengen) anzugeben
	 * @return die (leere) Liste von Serien- oder Chargennummern
	 */
	public ItemIdentityEntryList getItemIdentity() {
		return itemIdentity;
	}
	public void setItemIdentity(ItemIdentityEntryList itemIdentity) {
		this.itemIdentity = itemIdentity;
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
}
