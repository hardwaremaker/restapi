package com.heliumv.api.invoice;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public enum InvoiceDocumentStatus {
	NOTINITIALIZED("UNBEKANNT"), 
	NEW("Angelegt       "), 
	OPEN("Offen          "), 
	DONE("Erledigt       "), 
	CANCELLED("Storniert      "), 
	PARTLYCLEARED("Teilbezahlt    "),
	CLEARED("Bezahlt        ");

	InvoiceDocumentStatus(String value) {
		this.value = value;
	}

	public String getText() {
		return value;
	}

	public static InvoiceDocumentStatus fromString(String text) {
		if (text != null) {
			for (InvoiceDocumentStatus status : InvoiceDocumentStatus.values()) {
				if (text.equalsIgnoreCase(status.value)) {
					return status;
				}
			}
		}
		throw new IllegalArgumentException("No enum value '" + text + "'");
	}

	private String value;

}
