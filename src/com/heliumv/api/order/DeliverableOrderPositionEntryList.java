package com.heliumv.api.order;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DeliverableOrderPositionEntryList {
	private List<DeliverableOrderPositionEntry> entries ;
	
	public DeliverableOrderPositionEntryList() {
		setEntries(new ArrayList<DeliverableOrderPositionEntry>());
	}
	
	public DeliverableOrderPositionEntryList(List<DeliverableOrderPositionEntry> entries) {
		setEntries(entries) ;
	}
	
	public List<DeliverableOrderPositionEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<DeliverableOrderPositionEntry> entries) {
		this.entries = entries;
	}	
}
