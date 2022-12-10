package com.heliumv.api.delivery;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.item.ItemIdentityEntryList;

/**
 * Repr&auml;sentiert eine zu liefernde Position
 */
@XmlRootElement
public class CreateDeliveryPositionEntry {
	private Integer deliveryId ;
	private Integer orderId ;
	private Integer orderPositionId ;
	private ItemIdentityEntryList itemIdentity ;	
	private BigDecimal amount ;

	public CreateDeliveryPositionEntry() {
		setItemIdentity(new ItemIdentityEntryList());
	}
	
	/**
	 * Die (optionale) Lieferschein-Id, auf die diese Position geliefert werden soll.</br>
	 * <p>Wird die Id nicht angegeben, wird ein neuer Lieferschein generiert und auf diesen
	 * neuen Lieferschein geliefert. Ansonsten wird der hier angegebene Lieferschein verwendet</p>
	 * @return die (optionale) Id des Lieferscheins
	 */
	public Integer getDeliveryId() {
		return deliveryId;
	}
	public void setDeliveryId(Integer deliveryId) {
		this.deliveryId = deliveryId;
	}
	
	/**
	 * Die Auftragsnummer f&uuml;r die die Lieferung erfolgen soll
	 * @return die Auftragsnummer f&uuuml;r die diese Position gelten soll
	 */
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}	
	
	/**
	 * Die Auftragsposition die (teil)geliefert werden soll
	 * @return die Auftragsposition die (teil)geliefert werden soll
	 */
	public Integer getOrderPositionId() {
		return orderPositionId;
	}
	public void setOrderPositionId(Integer orderPositionId) {
		this.orderPositionId = orderPositionId;
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
	 * @return die zu liefernde Menge
	 */
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
}
