package com.heliumv.api.forecast;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ForecastDeliveryAddressEntryList {

	private List<ForecastDeliveryAddressEntry> entries;
	
	public ForecastDeliveryAddressEntryList() {
		setEntries(new ArrayList<ForecastDeliveryAddressEntry>());
	}

	public List<ForecastDeliveryAddressEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<ForecastDeliveryAddressEntry> entries) {
		this.entries = entries;
	}

}
