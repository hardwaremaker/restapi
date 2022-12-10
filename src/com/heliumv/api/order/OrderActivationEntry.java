package com.heliumv.api.order;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.BaseEntryId;

@XmlRootElement
public class OrderActivationEntry extends BaseEntryId {
	private long calculationTimestampMs ;
	private boolean customerExpectsVATorderHasNoneVATpositions;
	private boolean customerHasNoVATorderHasVATpositions;
	
	/**
	 * 
	 * @return Berechnungszeitpunkt bei Erfolg
	 */
	public long getCalculationTimestampMs() {
		return calculationTimestampMs;
	}

	public void setCalculationTimestampMs(long calculationTimestampMs) {
		this.calculationTimestampMs = calculationTimestampMs;
	}

	/**
	 * 
	 * @return sollte false sein, ist true wenn der Auftragskunde
	 *   steuerbehaftete Positionen erwartet, im Auftrag aber Ident/Handeingabepositionen
	 *   ohne Steuer enthalten sind.
	 */
	public boolean isCustomerExpectsVATorderHasNoneVATpositions() {
		return customerExpectsVATorderHasNoneVATpositions;
	}

	public void setCustomerExpectsVATorderHasNoneVATpositions(boolean customerExpectsVATorderHasNoneVATpositions) {
		this.customerExpectsVATorderHasNoneVATpositions = customerExpectsVATorderHasNoneVATpositions;
	}

	/**
	 * 
	 * @return sollte false sein, ist true wenn der Auftragskunde
	 *   steuerfreie Positionen erwartet, im Auftrag aber Ident/Handeingabepositionen
	 *   mit Steuer enthalten sind.
	 */
	public boolean isCustomerHasNoVATorderHasVATpositions() {
		return customerHasNoVATorderHasVATpositions;
	}

	public void setCustomerHasNoVATorderHasVATpositions(boolean customerHasNoVATorderHasVATpositions) {
		this.customerHasNoVATorderHasVATpositions = customerHasNoVATorderHasVATpositions;
	}
}
