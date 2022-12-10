package com.heliumv.api.forecast;

import java.util.ArrayList;
import java.util.List;

public class ForecastPositionEntryList {

	private List<ForecastPositionEntry> entries;
	
	public ForecastPositionEntryList() {
		setEntries(new ArrayList<ForecastPositionEntry>());
	}

	public List<ForecastPositionEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<ForecastPositionEntry> entries) {
		this.entries = entries;
	}

}
