package com.heliumv.api.delivery;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.item.ItemIdentityEntryList;

@XmlRootElement
public class CreateDeliveryItemPositionEntry {
	private Integer itemId ;
	private String itemCnr ;
	private ItemIdentityEntryList itemIdentity ;	
	private BigDecimal amount ;
	
	private Integer orderPositionId;

	public CreateDeliveryItemPositionEntry() {
		setItemIdentity(new ItemIdentityEntryList());
	}
	

	/**
	 * Die (optionale) Artikel-Id</br>
	 * <p>Wenn {@link #getOrderPositionId()} angegeben ist, dann kann die 
	 * Angabe von ItemCnr oder ItemId entfallen. Andernfalls muss entweder
	 * die Id des Artikels ({@link #getItemId()}) oder die Artikelnummer
	 * ({@link #getItemCnr()} angegeben werden</p>
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
	 * <p>Wenn {@link #getOrderPositionId()} angegeben ist, dann kann die 
	 * Angabe von ItemCnr oder ItemId entfallen. Andernfalls muss entweder
	 * die Id des Artikels ({@link #getItemId()}) oder die Artikelnummer
	 * ({@link #getItemCnr()} angegeben werden</p>
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


	/**
	 * Die Id der Auftragsposition die geliefert werden soll</br>
	 * <p>Ist die PositionId angegeben, wird eine etwaig vorhandene itemId
	 * oder itemCnr ignoriert.</p>
	 * <p>Der Auftrag der Position muss dem Auftrag des Lieferscheins 
	 * entsprechen</p>
	 * 
	 * @return die zu liefernde Id der Auftragsposition
	 */
	public Integer getOrderPositionId() {
		return orderPositionId;
	}


	public void setOrderPositionId(Integer orderpositionId) {
		this.orderPositionId = orderpositionId;
	}
}
