package com.heliumv.api.production;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.BaseEntryId;

@XmlRootElement
public class DoneProductionEntry extends BaseEntryId {
	private boolean done;
	private boolean missingAmount;
	private BigDecimal amount;
	private BigDecimal deliveredAmount;
	private String postMessage;
	private String nowProcessedBy;

	/**
	 * Ist das Los als erledigt markiert worden 
	 * @return true wenn das Los erfolgreich als erledigt markiert wurde
	 */
	public boolean isDone() {
		return done;
	}
	public void setDone(boolean done) {
		this.done = done;
	}
	
	/**
	 * Wurde zu wenig abgeliefert?
	 * 
	 * @return true wenn die Abliefermenge < Losgr&ouml;&szlig;e
	 */
	public boolean isMissingAmount() {
		return missingAmount;
	}
	public void setMissingAmount(boolean missingAmount) {
		this.missingAmount = missingAmount;
	}
	
	/**
	 * Die Losgr&ouml;&szlig;e
	 * @return die Losgr&ouml;&szlig;e
	 */
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	/** 
	 * Die abgelieferte Menge
	 * @return die abgelieferte Menge
	 */
	public BigDecimal getDeliveredAmount() {
		return deliveredAmount;
	}
	public void setDeliveredAmount(BigDecimal deliveredAmount) {
		this.deliveredAmount = deliveredAmount;
	}
	
	/** 
	 * Die (optionale) Fehlermeldung die beim Benachrichtigen 
	 * &uuml;ber die &Auml;nderung des Losstatus beim Fremdsystem
	 * entstanden ist
	 * 
	 * @return die (optionale) Fehlermeldung 
	 */
	public String getPostMessage() {
		return postMessage;
	}
	public void setPostMessage(String postMessage) {
		this.postMessage = postMessage;
	}
	public String getNowProcessedBy() {
		return nowProcessedBy;
	}
	public void setNowProcessedBy(String nowProcessedBy) {
		this.nowProcessedBy = nowProcessedBy;
	}
}
