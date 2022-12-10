package com.heliumv.api.delivery;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DeliveryEntryList {
	private List<DeliveryEntry> list ;
	
	public DeliveryEntryList() {
		setEntries(new ArrayList<DeliveryEntry>()) ;
	}

	public DeliveryEntryList(List<DeliveryEntry> entries) {
		setEntries(entries) ;
	}
	
	public List<DeliveryEntry> getEntries() {
		return list;
	}

	public void setEntries(List<DeliveryEntry> list) {
		this.list = list;
	}
}
