package com.heliumv.api.forecast;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.item.ItemV1Entry;

@XmlRootElement
public class LinecallItemEntry {

	private ItemV1Entry itemEntry;
	private BigDecimal quantity;
	private BigDecimal openQuantity;
	
	public LinecallItemEntry() {
	}

	public ItemV1Entry getItemEntry() {
		return itemEntry;
	}

	public void setItemEntry(ItemV1Entry itemEntry) {
		this.itemEntry = itemEntry;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public void setOpenQuantity(BigDecimal openQuantity) {
		this.openQuantity = openQuantity;
	}

	public BigDecimal getOpenQuantity() {
		return openQuantity;
	}
}
