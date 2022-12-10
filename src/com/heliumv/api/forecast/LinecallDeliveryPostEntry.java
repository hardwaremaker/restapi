package com.heliumv.api.forecast;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LinecallDeliveryPostEntry {

	private String userId;
	private Integer itemId;
	private BigDecimal quantity;
	private PickingType pickingType;
	
	public LinecallDeliveryPostEntry() {
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public PickingType getPickingType() {
		return pickingType;
	}

	public void setPickingType(PickingType pickingType) {
		this.pickingType = pickingType;
	}

}
