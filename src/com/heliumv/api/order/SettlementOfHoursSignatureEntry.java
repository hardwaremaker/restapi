package com.heliumv.api.order;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.BaseEntryId;

@XmlRootElement
public class SettlementOfHoursSignatureEntry extends BaseEntryId {
	private String userId;
	
	private String remark;
	private String pdfContent;
	private String signatureContent;
	private String signatureType;
	
	private long signatureMs;
	private Integer orderId;
	private Integer serialNumber;
	private String signerName;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}

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
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public String getSignatureType() {
		return signatureType;
	}
	public void setSignatureType(String signatureType) {
		this.signatureType = signatureType;
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
