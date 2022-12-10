package com.heliumv.api.delivery;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public enum PositionDataType {
	NOTINITIALIZED(0),
	ITEM(1),
	TEXT(2);
	
	PositionDataType(int value) {
		this.value = value;
	}

	private int value;
}
