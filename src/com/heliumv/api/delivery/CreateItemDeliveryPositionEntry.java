package com.heliumv.api.delivery;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.item.ItemIdentityEntryList;

/**
 * Repr&auml;sentiert eine zu liefernde Artikelposition
 */
@XmlRootElement
public class CreateItemDeliveryPositionEntry {
	private Integer deliveryId ;
	private Integer itemId ;
	private String itemCnr ;
	private ItemIdentityEntryList itemIdentity ;	
	private BigDecimal amount ;

	public CreateItemDeliveryPositionEntry() {
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
	 * Die (optionale) Artikel-Id</br>
	 * <p>Entweder die Artikel-Id oder die Artikelnummer ({@link #getItemCnr()} muss angegeben werden</p>
	 * 
	 * @return die Id des Artikels
	 */
	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	/**
	 * Die (optionale) Artikelnummer</br>
	 * <p>Entweder die Artikelnummer oder die Artikel-Id {@link #getItemId()} muss angegeben werden</p>
	 * 
	 * @return die Artikelnummer
	 */
	public String getItemCnr() {
		return itemCnr;
	}

	public void setItemCnr(String itemCnr) {
		this.itemCnr = itemCnr;
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
