package com.heliumv.api.production;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TestPlanEntryList {

	private List<TestPlanEntry> entries;
	
	public TestPlanEntryList() {
		setEntries(new ArrayList<TestPlanEntry>());
	}

	public List<TestPlanEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<TestPlanEntry> entries) {
		this.entries = entries;
	}

}
