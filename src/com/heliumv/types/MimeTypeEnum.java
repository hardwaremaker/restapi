package com.heliumv.types;

public enum MimeTypeEnum {
	
	/**
	 * Datenformat text/html
	 */
	TEXTHTML("text/html"),
	/**
	 * Datenformat image/jpeg
	 */
	IMAGEJPEG("image/jpeg"),
	/**
	 * Datenformat image/png
	 */
	IMAGEPNG("image/png"),
	/**
	 * Datenformat application/pdf
	 */
	APPPDF("application/pdf");
	
	private String value;
	
	private MimeTypeEnum(String value) {
		this.value = value;
	}
	
	public String getText() {
		return value;
	}
	
	public static MimeTypeEnum fromString(String text) {
		if(text != null) {
			for(MimeTypeEnum mimeType : MimeTypeEnum.values()) {
				if(text.equalsIgnoreCase(mimeType.getText())) {
					return mimeType;
				}
			}
		}
		throw new IllegalArgumentException("No enum value '" + text + "'");
	}
}
