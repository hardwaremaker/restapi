package com.heliumv.api.worktime;


public enum MachineRecordingType {
	START("Start"),
	STOP("Stop");

	MachineRecordingType(String value) {
		this.value = value;
	}

	public String getText() {
		return value ;
	}
	
	public static MachineRecordingType fromString(String text) {
		if(text != null) {
			for (MachineRecordingType status : MachineRecordingType.values()) {
				if(text.equalsIgnoreCase(status.value)) {
					return status ;
				}
			}
		}
		throw new IllegalArgumentException("No enum value '" + text + "'") ;
	}
	
	private String value;
}
