package com.heliumv.api.production;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.BaseEntryId;

@XmlRootElement
public class StopProductionEntry extends BaseEntryId {
	private boolean stopped;
	private String postMessage;
	
	public StopProductionEntry() {
		setStopped(false);
	}
	
	/**
	 * Wurde das Los gestoppt?
	 * 
	 * @return true wenn das Los gestoppt wurde
	 */
	public boolean isStopped() {
		return stopped;
	}
	
	public void setStopped(boolean stopped) {
		this.stopped = stopped;
	}
	
	/**
	 * Die (optionale) Fehlermeldung, die beim Benachrichtigen &uuml;ber das
	 * Stoppen der Produktion beim Fremdsystem entstanden ist
	 * 
	 * @return die (optionale) Fehlermeldung
	 */
	public String getPostMessage() {
		return postMessage;
	}
	public void setPostMessage(String postMessage) {
		this.postMessage = postMessage;
	}
}
