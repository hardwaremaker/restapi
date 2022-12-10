package com.heliumv.api.partlist;

public enum TestTypeEnum {
	CRIMP_WITH_ISOLATION("Crimpen mit ISO"),
	CRIMP_WITHOUT_ISOLATION("Crimpen ohne ISO"),
	FORCE_MEASUREMENT("Kraftmessung"),
	DIMENSIONAL_TEST("Masspruefung"),
	OPTICAL_TEST("Optische Pruefung"),
	ELECTRICAL_TEST("Elektrische Pruefung"),
	MULTIPLE_CRIMP_WITH_ISOLATION("Crimpen mit ISO und Doppelanschlag"),
	MULTIPLE_CRIMP_WITHOUT_ISOLATION("Crimpen ohne ISO und Doppelanschlag"),
	MATERIAL_STATUS("Materialstatus"),
	OPEN_TEST("Freie Pruefung");
	
	private String value;
	
	private TestTypeEnum(String value) {
		this.value = value;
	}
	
	public String getText() {
		return value;
	}
	
	public static TestTypeEnum fromString(String text) {
		if (text != null) {
			for (TestTypeEnum status : TestTypeEnum.values()) {
				if (text.equalsIgnoreCase(status.value)) {
					return status;
				}
			}
		}
		throw new IllegalArgumentException("No enum value '" + text + "'");
	}

}
