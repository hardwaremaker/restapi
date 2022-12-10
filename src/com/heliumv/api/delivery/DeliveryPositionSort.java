package com.heliumv.api.delivery;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public enum DeliveryPositionSort {
	NOTINITIALIZED("UNBEKANNT"),
	ITEMNUMBER("ARTIKELNUMMER"),
	ORDERPOSITION("AUFTRAGSPOSITION"),
	NOTSORTED("KEINESORTIERUNG");

	DeliveryPositionSort(String value) {
		this.value = value;
	}
	
	public String getText() {
		return value ;
	}
	
	public static DeliveryPositionSort fromString(String text) {
		if(text != null) {
			for (DeliveryPositionSort status : DeliveryPositionSort.values()) {
				if(text.equalsIgnoreCase(status.value)) {
					return status ;
				}
			}
		}
		throw new IllegalArgumentException("No enum value '" + text + "'") ;
	}

	public static DeliveryPositionSort fromId(String idText) {
		if(idText != null) {
			for (DeliveryPositionSort status : DeliveryPositionSort.values()) {
				if(idText.equalsIgnoreCase(status.name())) {
					return status ;
				}
			}
		}
		throw new IllegalArgumentException("No enum value for id '" + idText + "'") ;	
	}

	private String value;
}
