package com.heliumv.api.system;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LogBarcodeEntry {
	private String barcode;
	private long time;
	
	/**
	 * Der (gelesene) Barcode
	 * @return der Barcode
	 */
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	/** 
	 * Die Zeit in ms zu der der Barcode gelesen wurde</br>
	 * <p>Ist die Zeit 0, wird automatisch die Server-Zeit
	 * verwendet.</p>
	 * 
	 * @return 
	 */
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
}
