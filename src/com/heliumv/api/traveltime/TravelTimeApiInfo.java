package com.heliumv.api.traveltime;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TravelTimeApiInfo {
	private boolean enabled;

	public TravelTimeApiInfo() {
		this.enabled = false;
	}

	/**
	 * Liefert true wenn die API benutzt werden kann
	 * @return true wenn die API benutzt werden kann
	 */
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
