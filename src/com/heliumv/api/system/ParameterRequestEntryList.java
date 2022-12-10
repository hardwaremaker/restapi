package com.heliumv.api.system;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ParameterRequestEntryList {
	private List<ParameterRequestEntry> entries ;
	
	public ParameterRequestEntryList() {
		entries = new ArrayList<ParameterRequestEntry>() ;
	}
	
	public ParameterRequestEntryList(List<ParameterRequestEntry> entries) {
		this.entries = entries ;
	}
	
	/**
	 * Eine Liste aller Parameter</br>
	 * @return eine (leere) Liste aller angeforderten Parameter
	 */
	public List<ParameterRequestEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<ParameterRequestEntry> entries) {
		this.entries = entries;
	}
}
