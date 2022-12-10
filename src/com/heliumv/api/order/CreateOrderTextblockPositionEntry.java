package com.heliumv.api.order;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class CreateOrderTextblockPositionEntry {
	private Integer textblockId;
	private String textblockCnr;
	private String localeCnr;
	
	public CreateOrderTextblockPositionEntry() {
	}

	/**
	 * Die Id des Textblocks
	 * 
	 * @return die Id des Textblocks
	 */
	public Integer getTextblockId() {
		return textblockId;
	}

	public void setTextblockId(Integer textblockId) {
		this.textblockId = textblockId;
	}

	public String getTextblockCnr() {
		return textblockCnr;
	}

	public void setTextblockCnr(String textblockCnr) {
		this.textblockCnr = textblockCnr;
	}

	public String getLocaleCnr() {
		return localeCnr;
	}

	public void setLocaleCnr(String localeCnr) {
		this.localeCnr = localeCnr;
	}
}
