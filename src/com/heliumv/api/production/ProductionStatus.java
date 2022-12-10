package com.heliumv.api.production;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public enum ProductionStatus {
	NOTINITIALIZED("UNBEKANNT"),
	NEW("Angelegt       "),
	EDITED("Ausgegeben     "),
	DONE("Erledigt       "),
	INPRODUCTION("In Produktion  "),
	CANCELLED("Storniert      "),
	PARTIALLYDONE("Teilerledigt   "),
	STOPPED("Gestoppt       ");
	
	private String value;
	
	private ProductionStatus(String value) {
		this.value = value;
	}
	
	public String getText() {
		return value;
	}
	
	public static ProductionStatus fromString(String text) {
		if (text != null) {
			for (ProductionStatus status : ProductionStatus.values()) {
				if (text.equalsIgnoreCase(status.value)) {
					return status;
				}
			}
		}
		throw new IllegalArgumentException("No enum value '" + text + "'");
	}
	
	public static ProductionStatus lookup(String id) {
		for (ProductionStatus status : ProductionStatus.values()) {
			if (status.toString().equalsIgnoreCase(id)) {
				return status;
			}
		}

		throw new IllegalArgumentException("No enum '" + id + "'");
	}
}
