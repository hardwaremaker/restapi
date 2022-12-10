package com.heliumv.api.order;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.BaseEntryId;

@XmlRootElement
public class OrderSortedEntry extends BaseEntryId {
	private boolean sorted;

	/**
	 * ist true wenn die Sortierung nach Artikelnummer 
	 * durchgef&uuml;hrt wurde.
	 * 
	 * @return true wenn nach Artikelnummer sortiert wurde
	 */
	public boolean isSorted() {
		return sorted;
	}

	public void setSorted(boolean sorted) {
		this.sorted = sorted;
	}
}
