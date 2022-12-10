package com.heliumv.api.invoice;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public enum InvoiceCashPaymentEnum {
	CASH("Bar"), 
	ATM("Bankomat"), 
	CREDITCARD("Kreditkarte") ;

	InvoiceCashPaymentEnum(String value) {
		this.value = value;
	}

	public String getText() {
		return value;
	}

	public static InvoiceCashPaymentEnum fromString(String text) {
		if (text != null) {
			for (InvoiceCashPaymentEnum status : InvoiceCashPaymentEnum.values()) {
				if (text.equalsIgnoreCase(status.value)) {
					return status;
				}
			}
		}
		throw new IllegalArgumentException("No enum value '" + text + "'");
	}

	private String value;
}
