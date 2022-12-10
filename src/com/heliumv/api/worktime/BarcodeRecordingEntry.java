package com.heliumv.api.worktime;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BarcodeRecordingEntry extends TimeRecordingEntry {

	private String barcode;
	private Integer machineId;
	
	public BarcodeRecordingEntry() {
	}

	/**
	 * Barcode
	 * 
	 * @return
	 */
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	
	/**
	 * Id einer Maschine zur &Uuml;bersteuerung einer Maschine aus Barcode.
	 * Optional je nach Barcode.
	 * 
	 * @return
	 */
	public Integer getMachineId() {
		return machineId;
	}
	public void setMachineId(Integer machineId) {
		this.machineId = machineId;
	}
}
