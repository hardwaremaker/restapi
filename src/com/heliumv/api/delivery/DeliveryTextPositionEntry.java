package com.heliumv.api.delivery;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.BaseEntryId;

@XmlRootElement
public class DeliveryTextPositionEntry extends BaseEntryId {
	private String text;
	
	public DeliveryTextPositionEntry() {
	}
	
	public DeliveryTextPositionEntry(Integer positionId) {
		super(positionId);
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
