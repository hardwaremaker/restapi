package com.heliumv.api.delivery;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.BaseEntryId;

@XmlRootElement
public class DeliverySlipSignaturePostEntry extends BaseEntryId {	
	private String remark;
	private String pdfContent;
	private String signatureContent;	
	private long signatureMs;
	private Integer serialNumber;
	private String signerName;
	

	/**
	 * Die (optionale) Bemerkung
	 * @return die (optionale) Bemerkung des Unterschreibenden
	 */
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	/**
	 * Das originale (zu unterschreibende) PDF 
	 * @return das PDF
	 */
	public String getPdfContent() {
		return pdfContent;
	}
	public void setPdfContent(String pdfContent) {
		this.pdfContent = pdfContent;
	}
	
	/**
	 * Die Unterschrift, die best&auml;tigt, dass das 
	 * pdf gelesen/akzeptiert wurde.
	 * 
	 * @return die Unterschrift
	 */
	public String getSignatureContent() {
		return signatureContent;
	}
	public void setSignatureContent(String signatureContent) {
		this.signatureContent = signatureContent;
	}
	
	/**
	 * Der Zeitpunkt der Unterschrift 
	 * 
	 * @return Zeitpunkt der Unterschrift (ms seit 1970)
	 */
	public long getSignatureMs() {
		return signatureMs;
	}
	public void setSignatureMs(long signatureMs) {
		this.signatureMs = signatureMs;
	}
	public Integer getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(Integer serialNumber) {
		this.serialNumber = serialNumber;
	}
	public String getSignerName() {
		return signerName;
	}
	public void setSignerName(String signerName) {
		this.signerName = signerName;
	}
}
