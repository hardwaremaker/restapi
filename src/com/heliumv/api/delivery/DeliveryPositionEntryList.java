package com.heliumv.api.delivery;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DeliveryPositionEntryList {
	private List<DeliveryPositionEntry> entries ;
	
	public DeliveryPositionEntryList() {
		setEntries(new ArrayList<DeliveryPositionEntry>());
	}
	
	public DeliveryPositionEntryList(List<DeliveryPositionEntry> entries) {
		setEntries(entries) ;
	}
	
	public List<DeliveryPositionEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<DeliveryPositionEntry> entries) {
		this.entries = entries;
	}	
}
