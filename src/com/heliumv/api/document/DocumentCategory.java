package com.heliumv.api.document;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public enum DocumentCategory {
	PRODUCTION("Los            "),
	PURCHASEINVOICE("Eingangsrechng "),
	ORDER("Auftrag        "),
	DELIVERYNOTE("Lieferschein   "),
	ITEM("Artikel        "),
	PROJECT("Projekt        ");
	
	private String value;

	DocumentCategory(String value){
		this.value = value;
	}

	public String getText() {
		return value ;
	}

	public static DocumentCategory fromString(String text) {
		if(text != null) {
			for (DocumentCategory status : DocumentCategory.values()) {
				if(text.equalsIgnoreCase(status.value) || text.equals(status.toString())) {
					return status ;
				}
			}
		}
		
		throw new IllegalArgumentException("No enum value '" + text + "'") ;
	}
}
