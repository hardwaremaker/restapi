package com.heliumv.api.order;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DeliveryOrderEntryList {
	private List<DeliveryOrderEntry> list ;
	
	public DeliveryOrderEntryList() {
		setEntries(new ArrayList<DeliveryOrderEntry>()) ;
	}

	public DeliveryOrderEntryList(List<DeliveryOrderEntry> entries) {
		setEntries(entries) ;
	}
	
	public List<DeliveryOrderEntry> getEntries() {
		return list;
	}

	public void setEntries(List<DeliveryOrderEntry> list) {
		this.list = list;
	}
}
