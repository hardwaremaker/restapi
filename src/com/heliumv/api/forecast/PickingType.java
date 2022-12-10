package com.heliumv.api.forecast;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public enum PickingType {
	NOTINITIALIZED("UNBEKANNT"),
	CALLOFF("LINIENABRUF"), 
	ADDRESS("ADRESSE"), 
	ITEM("ARTIKEL");

	PickingType(String value){
		this.value = value;
	}

	public String getText() {
		return value ;
	}

	public static PickingType fromString(String text) {
		if(text != null) {
			for (PickingType status : PickingType.values()) {
				if(text.equalsIgnoreCase(status.value) || text.equals(status.toString())) {
					return status ;
				}
			}
		}
		
		throw new IllegalArgumentException("No enum value '" + text + "'") ;
	}

	private String value;
}
