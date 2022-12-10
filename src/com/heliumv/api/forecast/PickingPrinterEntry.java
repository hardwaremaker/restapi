package com.heliumv.api.forecast;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.BaseEntryId;

@XmlRootElement
public class PickingPrinterEntry extends BaseEntryId {

	private String cnr;
	private String description;
	
	public PickingPrinterEntry() {
	}

	public String getCnr() {
		return cnr;
	}
	
	public void setCnr(String cnr) {
		this.cnr = cnr;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
}
