package com.heliumv.api.delivery;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DeliveryData {
	private PositionDataEntryList positionData;
	private Integer deliveryId;
	private DeliveryPositionEntryList itemPositions;
	private DeliveryTextPositionEntryList textPositions;
	
	public DeliveryData() {
		setPositionData(new PositionDataEntryList());
		setItemPositions(new DeliveryPositionEntryList());
		setTextPositions(new DeliveryTextPositionEntryList());
	}
	
	/**
	 * Die Metadaten der Positionen
	 */
	public PositionDataEntryList getPositionData() {
		return positionData;
	}

	public void setPositionData(PositionDataEntryList positionData) {
		this.positionData = positionData;
	}

	/**
	 * Die Id des Lieferscheins.
	 * @return die Id des Lieferscheins
	 */
	public Integer getDeliveryId() {
		return deliveryId;
	}

	public void setDeliveryId(Integer deliveryId) {
		this.deliveryId = deliveryId;
	}

	public DeliveryPositionEntryList getItemPositions() {
		return itemPositions;
	}

	public void setItemPositions(DeliveryPositionEntryList itemPositions) {
		this.itemPositions = itemPositions;
	}

	public DeliveryTextPositionEntryList getTextPositions() {
		return textPositions;
	}

	public void setTextPositions(DeliveryTextPositionEntryList textPositions) {
		this.textPositions = textPositions;
	}
}
