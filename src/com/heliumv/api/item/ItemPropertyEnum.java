package com.heliumv.api.item;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public enum ItemPropertyEnum {
	NOIDENTIY(0), // Artikel hat keine Auspraegung
	SERIALNR(1),  // Seriennummer
	BATCHNR(2) ;  // Chargennummer

	ItemPropertyEnum(int value) {
		this.value = value;
	}

	private int value;
}
