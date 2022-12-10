package com.heliumv.api.invoice;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.BaseEntryCnr;

/**
 * Properties f&uuml;r die Zahlung</br>
 * <p>Die Property cnr muss mit der eindeutigen Kassa-Nummer bef&uuml;llt werden</p>
 * <p>F&uuml;r eine erfolgreiche Zahlung m&uuml;ssen der Betrag {@link #getAmount()},
 * die Zahlungsart {@link #getPaymentType()} und die Kassanummer {@link #getCnr()}
 * gesetzt werden.</p>
 */
@XmlRootElement
public class InvoiceCashPaymentPostEntry extends BaseEntryCnr {
	private BigDecimal amount ;
	private InvoiceCashPaymentEnum paymentType ;
	
	/**
	 * Der bezahlte Betrag
	 * @return der bezahlte Betrag
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * Die Zahlungsart CASH("Bar"), ATM("Bankomat") oder CREDITCARD("Kreditkarte") ;
	 * @return die Zahlungsart CASH, ATM, CREDITCARD
	 */
	public InvoiceCashPaymentEnum getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(InvoiceCashPaymentEnum paymentType) {
		this.paymentType = paymentType;
	}
}
