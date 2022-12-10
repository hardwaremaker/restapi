package com.heliumv.api.system;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public enum PropertyLayoutFill {
	HORIZONTAL("HORIZONTAL"),
	VERTICAL("VERTICAL"),
	BOTH("BOTH"),
	NONE("NONE");
	
	private String value;
	
	private PropertyLayoutFill(String value) {
		this.value = value;
	}
	
	public static PropertyLayoutFill fromString(String text) {
		if (text != null) {
			for (PropertyLayoutFill fill : PropertyLayoutFill.values()) {
				if (text.equalsIgnoreCase(fill.value)) {
					return fill;
				}
			}
		}
		throw new IllegalArgumentException("No enum value '" + text + "'");
	}
}
