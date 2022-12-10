package com.heliumv.api.forecast;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PickingPrinterEntryList {

	private List<PickingPrinterEntry> entries;
	
	public PickingPrinterEntryList() {
		setEntries(new ArrayList<PickingPrinterEntry>());
	}

	public void setEntries(List<PickingPrinterEntry> entries) {
		this.entries = entries;
	}
	
	public List<PickingPrinterEntry> getEntries() {
		return entries;
	}
}
