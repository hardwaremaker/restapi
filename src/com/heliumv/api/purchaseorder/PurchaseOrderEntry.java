package com.heliumv.api.purchaseorder;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.BaseEntryId;
import com.heliumv.api.supplier.SupplierDetailEntry;

@XmlRootElement
public class PurchaseOrderEntry extends BaseEntryId {
	private String cnr;
	private Integer supplierId;
	private PurchaseOrderStatus status;
	private String supplierName;
	private String supplierCity;
	private SupplierDetailEntry supplierEntry;
	
	/**
	 * Die Bestellnummer
	 * @return die Bestellnummer
	 */
	public String getCnr() {
		return cnr;
	}

	public void setCnr(String cnr) {
		this.cnr = cnr;
	}

	/**
	 * Die Id des Lieferanten - bei dem die Bestellung aufgegeben worden ist.
	 * 
	 * @return die Id des Lieferanten
	 */
	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	/**
	 * Der Status der Bestellung
	 * 
	 * @return der Status der Bestellung
	 */
	public PurchaseOrderStatus getStatus() {
		return status;
	}

	public void setStatus(PurchaseOrderStatus status) {
		this.status = status;
	}
	
	/**
	 * Die Lieferantendetaildaten sofern explizit angefordert
	 * 
	 * @return Daten des Lieferanten
	 */
	public SupplierDetailEntry getSupplierEntry() {
		return supplierEntry;
	}
	
	public void setSupplierEntry(SupplierDetailEntry supplierEntry) {
		this.supplierEntry = supplierEntry;
	}
	
	/**
	 * Der Lieferantenname
	 * 
	 * @return
	 */
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	
	/**
	 * Der Ort im Format "<LKZ>-<PLZ> <ORT>", also zum Beispiel "AT-5301 Eugendorf"
	 * 
	 * @return
	 */
	public String getSupplierCity() {
		return supplierCity;
	}
	public void setSupplierCity(String supplierCity) {
		this.supplierCity = supplierCity;
	}
}
