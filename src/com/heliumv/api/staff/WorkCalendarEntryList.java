package com.heliumv.api.staff;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class WorkCalendarEntryList {
	private List<WorkCalendarEntry> entries ;

	public WorkCalendarEntryList() {
		this(new ArrayList<WorkCalendarEntry>()) ;
	}
	
	public WorkCalendarEntryList(List<WorkCalendarEntry> entries) {
		this.entries = entries ;
	}
	
	/**
	 * Die (leere) Liste aller Kalendereintr&auml;ge
	 * @return die (leere) Liste aller Kalendereintr&auml;ge
	 */
	public List<WorkCalendarEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<WorkCalendarEntry> entries) {
		this.entries = entries;
	}
}
