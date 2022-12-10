package com.heliumv.api.invoice;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.BaseEntryId;
import com.heliumv.api.customer.CustomerDetailEntry;

@XmlRootElement
public class InvoiceEntry extends BaseEntryId {
	private String cnr ;
	private String customerName ;
	private String customerCity ;
	private String project ;
	private BigDecimal grossValue ;
	private String currency ;
	private Integer customerId ;
	private BigDecimal netValue ;
	private BigDecimal openGrossValue ;
	private InvoiceDocumentStatus status ;
	private CustomerDetailEntry customerEntry ;
	
	/**
	 * Die Rechnungsnummer
	 * @return
	 */
	public String getCnr() {
		return cnr;
	}
	public void setCnr(String cnr) {
		this.cnr = cnr;
	}
	
	/**
	 * Der Kundenname
	 * @return
	 */
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	/**
	 * Der Ort im Format "<LKZ>-<PLZ> <ORT>", also zum Beispiel "AT-5301 Eugendorf"
	 * @return
	 */
	public String getCustomerCity() {
		return customerCity;
	}
	public void setCustomerCity(String customerCity) {
		this.customerCity = customerCity;
	}
	public String getProject() {
		return project;
	}
	
	/**
	 * Die Projekt- bzw. Bestellnummer
	 * @param project
	 */
	public void setProject(String project) {
		this.project = project;
	}
	
	/**
	 * Der Bruttobetrag der Rechnung
	 * @return
	 */
	public BigDecimal getGrossValue() {
		return grossValue;
	}
	public void setGrossValue(BigDecimal grossValue) {
		this.grossValue = grossValue;
	}
	
	/**
	 * Die W&auml;hrung in der die Rechnung ausgestellt ist
	 * @return
	 */
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	/**
	 * Die Id des Rechnungskunden
	 * @return
	 */
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	
	/**
	 * Der Nettobetrag der Rechnung
	 * @return
	 */
	public BigDecimal getNetValue() {
		return netValue;
	}
	public void setNetValue(BigDecimal netValue) {
		this.netValue = netValue;
	}
	
	/**
	 * Der noch offene Bruttobetrag der Rechnung
	 * @return
	 */
	public BigDecimal getOpenGrossValue() {
		return openGrossValue;
	}
	public void setOpenGrossValue(BigDecimal openGrossValue) {
		this.openGrossValue = openGrossValue;
	}
	
	/**
	 * Der Rechnungsstatus</br>
	 * <p>NEW ("Angelegt"), OPEN ("Offen"), DONE ("Erledigt"), CANCELLED ("storniert"), 
	 * PARTLYCLEARED ("Teilbezahlt"), CLEARED ("Bezahlt")</p>
	 * @return
	 */
	public InvoiceDocumentStatus getStatus() {
		return status;
	}
	public void setStatus(InvoiceDocumentStatus status) {
		this.status = status;
	}
	
	/**
	 * Die Kundendetaildaten sofern explizit angefordert
	 * @return
	 */
	public CustomerDetailEntry getCustomerEntry() {
		return customerEntry;
	}
	public void setCustomerEntry(CustomerDetailEntry customerEntry) {
		this.customerEntry = customerEntry;
	}	
}
