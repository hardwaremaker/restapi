package com.heliumv.api.purchaseinvoice;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PostPurchaseInvoices {
	private PurchaseInvoiceEntryList invoices;
	
	public PurchaseInvoiceEntryList getInvoices() {
		return invoices;
	}

	public void setInvoices(PurchaseInvoiceEntryList invoices) {
		this.invoices = invoices;
	}
}
