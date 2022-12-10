package com.heliumv.api.system;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.BaseEntryCnr;


@XmlRootElement
public class JudgeEntry extends BaseEntryCnr {
	private boolean allowed ;

	public JudgeEntry() {
	}
	
	public JudgeEntry(String cnr) {
		super(cnr) ;
		allowed = false ;
	}
	
	public boolean isAllowed() {
		return allowed;
	}

	public void setAllowed(boolean allowed) {
		this.allowed = allowed;
	}
}
