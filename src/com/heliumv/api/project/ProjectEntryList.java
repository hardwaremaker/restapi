package com.heliumv.api.project;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ProjectEntryList {
	private List<ProjectEntry> entries ;

	public ProjectEntryList() {
		this(new ArrayList<ProjectEntry>()) ;
	}
	
	public ProjectEntryList(List<ProjectEntry> entries) {
		this.entries = entries ;
	}
	
	/**
	 * Die (leere) Liste aller Projekte
	 * @return die (leere) Liste aller Projekte
	 */
	public List<ProjectEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<ProjectEntry> entries) {
		this.entries = entries;
	}
}
