package com.heliumv.api.production;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TestResultEntryList {

	private List<TestResultEntry> entries;
	
	public TestResultEntryList() {
		setEntries(new ArrayList<TestResultEntry>());
	}

	public List<TestResultEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<TestResultEntry> entries) {
		this.entries = entries;
	}

}
