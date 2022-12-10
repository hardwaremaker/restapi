package com.heliumv.api.system;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.BaseEntryCnr;

@XmlRootElement
public class ItemUnitEntry extends BaseEntryCnr {
	private String description;
	private int dimension;
	
	/**
	 * Die (locale-abh&auml;ngige) Bezeichnung dieser Einheit
	 * @return
	 */
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Die Anzahl der Dimensionen dieser Einheit</br>
	 * <p>0 ... kg, l</br>
	 * 1 ... cm, m</br>
	 * 2 ... cm2, m2</br>
	 * 3 ... cm3, m3</br> 
	 * <p>
	 * @return die Anzahl der Dimensionen dieser Einheit
	 */
	public int getDimension() {
		return dimension;
	}
	public void setDimension(int dimension) {
		this.dimension = dimension;
	}
}
