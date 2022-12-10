package com.heliumv.api.system;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public enum PropertyLayoutType {
	LABEL("WrapperLabel        "),
	TEXTFIELD("WrapperTextField    "),
	TEXTAREA("WrapperTextArea     "),
	EDITOR("WrapperEditor       "),
	CHECKBOX("WrapperCheckbox     "),
	PRINTBUTTON("WrapperPrintButton  "),
	EXECUTEBUTTON("WrapperExecButton   "),
	COMBOBOX("WrapperComboBox     ");
	
	private String value;
	
	private PropertyLayoutType(String value) {
		this.value = value;
	}
	
	public static PropertyLayoutType fromString(String text) {
		if (text != null) {
			for (PropertyLayoutType type : PropertyLayoutType.values()) {
				if (text.equalsIgnoreCase(type.value)) {
					return type;
				}
			}
		}
		throw new IllegalArgumentException("No enum value '" + text + "'");
	}

}
