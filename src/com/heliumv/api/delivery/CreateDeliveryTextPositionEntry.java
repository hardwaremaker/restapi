package com.heliumv.api.delivery;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class CreateDeliveryTextPositionEntry {
	private String text;

	public CreateDeliveryTextPositionEntry() {
	}

	/**
	 * Der Positionstext
	 * 
	 * @return Positionstext
	 */
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
