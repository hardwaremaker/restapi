package com.heliumv.api.order;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class CreateOrderTextPositionEntry {
	private String text;

	public CreateOrderTextPositionEntry() {
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
