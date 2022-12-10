package com.heliumv.api.delivery;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.BaseEntryId;
import com.heliumv.api.item.ItemIdentityEntryList;
import com.heliumv.api.item.ItemPropertyEnum;

@XmlRootElement
public class DeliveryPositionEntry extends BaseEntryId {
	private String itemCnr;
	private Integer itemId;
	private String description;
	private BigDecimal amount;
	private String unitCnr;
	private ItemPropertyEnum itemProperty;
	private ItemIdentityEntryList itemIdentity;
	private Integer deliveryId;
	private Integer orderPositionId;
	
	public DeliveryPositionEntry() {
		setItemIdentity(new ItemIdentityEntryList());
	}
	
	public DeliveryPositionEntry(Integer id) {
		super(id);
		setItemIdentity(new ItemIdentityEntryList());
	}
	
	/**
	 * Die Artikelnummer
	 * @return die Artikelnummer
	 */
	public String getItemCnr() {
		return itemCnr;
	}
	public void setItemCnr(String itemCnr) {
		this.itemCnr = itemCnr;
	}
	
	/**
	 * Die Id des Artikels
	 * @return die Id des Artikel
	 */
	public Integer getItemId() {
		return itemId;
	}
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
	
	/**
	 * Die Artikelbezeichnung 
	 * @return die Artikelbezeichnung
	 */
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Auftragsmenge 
	 * 
	 * @return die Menge des Artikels in der Position
	 */
	public BigDecimal getAmount() {
		return amount;
	}
	
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	
	/**
	 * Die Einheit des Artikels im Auftrag
	 * @return die Einheit des Artikels
	 */
	public String getUnitCnr() {
		return unitCnr;
	}
	public void setUnitCnr(String unitCnr) {
		this.unitCnr = unitCnr;
	}


	public ItemPropertyEnum getItemProperty() {
		return itemProperty;
	}
	public void setItemProperty(ItemPropertyEnum itemProperty) {
		this.itemProperty = itemProperty;
	}

	public ItemIdentityEntryList getItemIdentity() {
		return itemIdentity;
	}
	public void setItemIdentity(ItemIdentityEntryList itemIdentity) {
		this.itemIdentity = itemIdentity;
	}

	/**
	 * Die LieferscheinId
	 * @return die Lieferschein-Id
	 */
	public Integer getDeliveryId() {
		return deliveryId;
	}

	public void setDeliveryId(Integer deliveryId) {
		this.deliveryId = deliveryId;
	}

	public Integer getOrderPositionId() {
		return orderPositionId;
	}

	public void setOrderPositionId(Integer orderPositionId) {
		this.orderPositionId = orderPositionId;
	}		
}


