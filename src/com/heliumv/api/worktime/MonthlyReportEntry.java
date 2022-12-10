package com.heliumv.api.worktime;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MonthlyReportEntry {
	private byte[] pdfContent;
	private byte[] lastPagePng;

	public byte[] getPdfContent() {
		return pdfContent;
	}
	public void setPdfContent(byte[] pdfContent) {
		this.pdfContent = pdfContent;
	}
	public byte[] getLastPagePng() {
		return lastPagePng;
	}
	public void setLastPagePng(byte[] lastPagePng) {
		this.lastPagePng = lastPagePng;
	}
}
