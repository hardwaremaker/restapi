package com.heliumv.api.forecast;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LinecallDeliveryResultEntry {

	private Integer linecallId;
	private Integer itemId;
	private BigDecimal openQuantity;
	
	public LinecallDeliveryResultEntry() {
	}

	public Integer getLinecallId() {
		return linecallId;
	}

	public void setLinecallId(Integer linecallId) {
		this.linecallId = linecallId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public BigDecimal getOpenQuantity() {
		return openQuantity;
	}

	public void setOpenQuantity(BigDecimal openQuantity) {
		this.openQuantity = openQuantity;
	}

}
