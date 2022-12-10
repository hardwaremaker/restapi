package com.heliumv.api.system;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public enum LogSeverity {
	NOTINITIALIZED("UNBEKANNT"),
	DEBUG("DEBUG"),
	INFO("INFO"),
	WARN("WARN"),
	ERROR("ERROR");
	
	LogSeverity(String value) {
		this.value = value;
	}
	
	public String getText() {
		return value;
	}
	
	public static LogSeverity fromString(String text) {
		if(text != null) {
			for (LogSeverity status : LogSeverity.values()) {
				if(text.equalsIgnoreCase(status.value)) {
					return status ;
				}
			}
		}
		throw new IllegalArgumentException("No enum value '" + text + "'") ;	
	}
	
	private String value;
}

