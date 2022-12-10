package com.heliumv.api.project;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public enum ProjectDocumentStatus {
	NOTINITIALIZED("UNBEKANNT"),
	NEW("Angelegt       "), 
	OPEN("Offen          "), 
	DONE("Geliefert      "),
	CANCELLED("Storniert      ");

	ProjectDocumentStatus(String value) {
		this.value = value ;
	}
	
	public String getText() {
		return value ;
	}
	
	public static ProjectDocumentStatus fromString(String text) {
		if(text != null) {
			for (ProjectDocumentStatus status : ProjectDocumentStatus.values()) {
				if(text.equalsIgnoreCase(status.value)) {
					return status ;
				}
			}
		}
		throw new IllegalArgumentException("No enum value '" + text + "'") ;
	}
	
	private String value;
}
