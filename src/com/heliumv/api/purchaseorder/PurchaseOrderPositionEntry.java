package com.heliumv.api.purchaseorder;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.BaseEntryId;
import com.heliumv.api.item.ItemV1Entry;

@XmlRootElement
public class PurchaseOrderPositionEntry extends BaseEntryId {
	private Integer itemId;
	private BigDecimal openQuantity;
	private ItemV1Entry itemEntry;
	private BigDecimal quantity;
	
	public ItemV1Entry getItemEntry() {
		return itemEntry;
	}
	public void setItemEntry(ItemV1Entry itemEntry) {
		this.itemEntry = itemEntry;
	}
	
	public BigDecimal getOpenQuantity() {
		return openQuantity;
	}
	public void setOpenQuantity(BigDecimal openQuantity) {
		this.openQuantity = openQuantity;
	}
	
	public Integer getItemId() {
		return itemId;
	}
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}
}
