package com.heliumv.api.production;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ProductionAdditionalStatusEntry {

	private String status;
	private Long dateMs;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Long getDateMs() {
		return dateMs;
	}
	public void setDateMs(Long dateMs) {
		this.dateMs = dateMs;
	}
	
}
