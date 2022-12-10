package com.heliumv.api.system;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.BaseEntryId;

@XmlRootElement
public class CostBearingUnitEntry extends BaseEntryId {
	private String description;
	
	public CostBearingUnitEntry() {
	}
	
	public CostBearingUnitEntry(Integer unitId) {
		super(unitId);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
