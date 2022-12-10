package com.heliumv.api.delivery;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DeliveryTextPositionEntryList {
	private List<DeliveryTextPositionEntry> entries;
	
	public DeliveryTextPositionEntryList() {
		setEntries(new ArrayList<DeliveryTextPositionEntry>());
	}
	
	public DeliveryTextPositionEntryList(List<DeliveryTextPositionEntry> entries) {
		setEntries(entries);
	}

	public List<DeliveryTextPositionEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<DeliveryTextPositionEntry> entries) {
		this.entries = entries;
	}
}
