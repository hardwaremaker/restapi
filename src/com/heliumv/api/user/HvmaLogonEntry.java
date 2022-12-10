package com.heliumv.api.user;

import javax.xml.bind.annotation.XmlRootElement;

import com.lp.server.personal.service.HvmaLizenzEnum;

@XmlRootElement
public class HvmaLogonEntry extends LogonEntry {
	private HvmaLizenzEnum licence;
	private String resource;
	
	public HvmaLizenzEnum getLicence() {
		return licence;
	}

	public void setLicence(HvmaLizenzEnum licence) {
		this.licence = licence;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}
}
