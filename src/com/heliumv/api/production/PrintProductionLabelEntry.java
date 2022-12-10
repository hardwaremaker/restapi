package com.heliumv.api.production;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.BaseEntryId;

@XmlRootElement
public class PrintProductionLabelEntry extends BaseEntryId {

	private boolean printLabel;
	
	/**
	 * Wurde das Losetikett gedruckt?
	 * 
	 * @return
	 */
	public boolean isPrintLabel() {
		return printLabel;
	}
	public void setPrintLabel(boolean printLabel) {
		this.printLabel = printLabel;
	}
	
}
