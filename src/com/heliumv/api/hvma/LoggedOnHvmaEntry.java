package com.heliumv.api.hvma;

import com.heliumv.api.user.LoggedOnEntry;

public class LoggedOnHvmaEntry extends LoggedOnEntry {
	private String resource;

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}
}
