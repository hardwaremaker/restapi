package com.heliumv.api.finance;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.BaseEntryCnr;

@XmlRootElement
public class CurrencyEntry extends BaseEntryCnr {
	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
