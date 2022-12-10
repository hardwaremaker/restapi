package com.heliumv.api.purchaseinvoice;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public enum PaymentEnum {
	NOPAYMENT(0),
	ME(1),
	CARD(2),
	OTHER(3);
	
	PaymentEnum(int value) {
		this.value = value;
	}
	
	private int value;
}
