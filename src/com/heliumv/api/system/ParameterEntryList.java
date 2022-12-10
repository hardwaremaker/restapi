package com.heliumv.api.system;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ParameterEntryList {
	private List<ParameterEntry> entries ;
	
	public ParameterEntryList() {
		entries = new ArrayList<ParameterEntry>() ;
	}
	
	public ParameterEntryList(List<ParameterEntry> entries) {
		this.entries = entries ;
	}
	
	/**
	 * Eine Liste aller Parameter</br>
	 * @return eine (leere) Liste aller angeforderten Parameter
	 */
	public List<ParameterEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<ParameterEntry> entries) {
		this.entries = entries;
	}
}
