package com.heliumv.api.document;

public class RawDocument {
	
	private DocumentInfoEntry documentInfoEntry;
	private byte[] rawData;
	
	public RawDocument() {
	}

	public DocumentInfoEntry getDocumentInfoEntry() {
		return documentInfoEntry;
	}
	
	public void setDocumentInfoEntry(DocumentInfoEntry documentInfoEntry) {
		this.documentInfoEntry = documentInfoEntry;
	}
	
	public byte[] getRawData() {
		return rawData;
	}
	
	public void setRawData(byte[] rawData) {
		this.rawData = rawData;
	}
}
