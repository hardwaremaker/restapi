package com.heliumv.api.delivery;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.BaseEntryId;

/**
 * Die Metadaten einer Position.
 * Id der Position
 * Positionsart
 * isort 
 * @author gerold
 */
@XmlRootElement
public class PositionDataEntry extends BaseEntryId {
	private PositionDataType dataType;
	private int isort;
	
	public PositionDataEntry() {
	}
	
	public PositionDataEntry(Integer positionId) {
		super(positionId);
	}

	public PositionDataType getDataType() {
		return dataType;
	}

	public void setDataType(PositionDataType dataType) {
		this.dataType = dataType;
	}

	public int getIsort() {
		return isort;
	}

	public void setIsort(int isort) {
		this.isort = isort;
	}
}
