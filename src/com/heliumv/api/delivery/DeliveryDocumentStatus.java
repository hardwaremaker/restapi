package com.heliumv.api.delivery;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public enum DeliveryDocumentStatus {
	NOTINITIALIZED("UNBEKANNT"),
	NEW("Angelegt       "), 
	OPEN("Offen          "), 
	DONE("Geliefert      "),
	CANCELLED("Storniert      "),
	CLEARED("Verrechnet     "),
	COMPLETED("Erledigt       ");

	DeliveryDocumentStatus(String value) {
		this.value = value;
	}

	public String getText() {
		return value ;
	}
	
	public static DeliveryDocumentStatus fromString(String text) {
		if(text != null) {
			for (DeliveryDocumentStatus status : DeliveryDocumentStatus.values()) {
				if(text.equalsIgnoreCase(status.value)) {
					return status ;
				}
			}
		}
		throw new IllegalArgumentException("No enum value '" + text + "'") ;
	}
	
	private String value;
	
	public static DeliveryDocumentStatus lookup(String id) {
		for (DeliveryDocumentStatus status : DeliveryDocumentStatus.values()) {
			if (status.toString().equalsIgnoreCase(id)) {
				return status;
			}
		}
		throw new IllegalArgumentException("No enum '" + id + "'");
	}
}
