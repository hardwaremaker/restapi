package com.heliumv.api.order;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public enum OrderDocumentStatus {
	/**
	 * Noch nicht initialisiert.
	 */
	NOTINITIALIZED("UNBEKANNT"),
	
	/**
	 * Ein angelegter Auftrag
	 */
	NEW("Angelegt       "), 
	
	/**
	 * Ein offener Auftrag
	 */
	OPEN("Offen          "),
	
	/**
	 * Der Auftrag ist teilerledigt, einige Positionen - oder auch Teile davon - 
	 * sind bereits geliefert
	 */
	PARTIALLYDONE("Teilerledigt   "),
	
	/**
	 * Alle Positionen des Auftrags sind geliefert - oder als erledigt markiert.
	 */
	DONE("Erledigt       "),
	
	/**
	 * der Auftrag wurde storniert
	 */
	CANCELLED("Storniert      ");

	OrderDocumentStatus(String value) {
		this.value = value;
	}

	public String getText() {
		return value ;
	}
	
	public static OrderDocumentStatus fromString(String text) {
		if(text != null) {
			for (OrderDocumentStatus status : OrderDocumentStatus.values()) {
				if(text.equalsIgnoreCase(status.value)) {
					return status ;
				}
			}
		}
		throw new IllegalArgumentException("No enum value '" + text + "'") ;
	}
	private String value;
}
