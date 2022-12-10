package com.heliumv.api.production;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MaterialRequirementPostEntry {

	private String userId;
	private MaterialRequirementEntryList materialRequirementEntries;
	private Boolean printSynchronisationPaper;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public MaterialRequirementEntryList getMaterialRequirementEntries() {
		return materialRequirementEntries;
	}

	public void setMaterialRequirementEntries(MaterialRequirementEntryList materialRequirementEntries) {
		this.materialRequirementEntries = materialRequirementEntries;
	}

	public Boolean getPrintSynchronisationPaper() {
		return printSynchronisationPaper;
	}

	public void setPrintSynchronisationPaper(Boolean printSynchronisationPaper) {
		this.printSynchronisationPaper = printSynchronisationPaper;
	}
}
