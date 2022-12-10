package com.heliumv.api.system;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public enum PropertyLayoutOrientation {
	NORTHWEST("NORTHWEST"),
	NORTH("NORTH"),
	NORTHEAST("NORTHEAST"),
	WEST("WEST"),
	CENTER("CENTER"),
	EAST("EAST"),
	SOUTHWEST("SOUTHWEST"),
	SOUTH("SOUTH"),
	SOUTHEAST("SOUTHEAST");

	private String value;
	
	private PropertyLayoutOrientation(String value) {
		this.value = value;
	}
	
	public static PropertyLayoutOrientation fromString(String text) {
		if (text != null) {
			for (PropertyLayoutOrientation orientation : PropertyLayoutOrientation.values()) {
				if (text.equalsIgnoreCase(orientation.value)) {
					return orientation;
				}
			}
		}
		throw new IllegalArgumentException("No enum value '" + text + "'");
	}
}


