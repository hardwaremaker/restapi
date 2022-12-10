package com.heliumv.api.forecast;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.BaseEntryId;

@XmlRootElement
public class ForecastDeliveryAddressEntry extends BaseEntryId {

	private Integer forecastId;
	private Integer customerId;
	private String deliveryAddress;
	private String deliveryAddressShort;
	private PickingType pickingType;
	private String pickingPrinterCnr;
	
	public ForecastDeliveryAddressEntry() {
	}

	public Integer getForecastId() {
		return forecastId;
	}

	public void setForecastId(Integer forecastId) {
		this.forecastId = forecastId;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public PickingType getPickingType() {
		return pickingType;
	}

	public void setPickingType(PickingType pickingType) {
		this.pickingType = pickingType;
	}

	public String getDeliveryAddressShort() {
		return deliveryAddressShort;
	}

	public void setDeliveryAddressShort(String deliveryAddressShort) {
		this.deliveryAddressShort = deliveryAddressShort;
	}

	public String getPickingPrinterCnr() {
		return pickingPrinterCnr;
	}
	
	public void setPickingPrinterCnr(String pickingPrinterCnr) {
		this.pickingPrinterCnr = pickingPrinterCnr;
	}
}
