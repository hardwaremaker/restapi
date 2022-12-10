package com.heliumv.api.production;

import java.math.BigDecimal;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.worktime.TimeRecordingEntry;

@XmlRootElement
public class DeliveryPostEntry {

	private String userId;
	private BigDecimal amount;
	private Integer stockId;
	private TestResultEntryList testResultEntries;
	private Boolean bookStopping;
	private TimeRecordingEntry timeRecordingEntry;
	private Boolean bookMachineStopping;
	private Integer productionWorkplanId;
	private BigDecimal scrapAmount;
	private Boolean increaseLotSizeByScrapAmount;
	
	private List<IdentityAmountEntry> identities;

	public DeliveryPostEntry() {
	}

	/**
	 * Menge der Losablieferung</br>
	 * Handelt es sich um eine Ablieferung mit Seriennr/Chargennr, so ist sie die
	 * Summe der Mengen der <code>identities</code>
	 * 
	 * @return Menge der Losablieferung
	 */
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * Pruefergebnisse der Losablieferung (optional)
	 * 
	 * @return Pruefergebnisse der Losablieferung
	 */
	public TestResultEntryList getTestResultEntries() {
		return testResultEntries;
	}
	public void setTestResultEntries(TestResultEntryList testResultEntries) {
		this.testResultEntries = testResultEntries;
	}

	/**
	 * Ziellager der Losablieferung (optional) <br>
	 * Wenn nicht angegeben, wird als Ziellager das im Los definierte verwendet.
	 * 
	 * @return Ziellager der Losablieferung
	 */
	public Integer getStockId() {
		return stockId;
	}
	public void setStockId(Integer stockId) {
		this.stockId = stockId;
	}

	/**
	 * Ist der beim Logon ermittelte "Token"
	 * 
	 * @return der beim Logon ermittelte "Token"
	 */
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * Initiiert eine ENDE Zeitbuchung (optional)<br>
	 * Wenn auf <code>true</code>, dann muss zus&auml;tzlich das <code>TimeRecordingEntry</code>
	 * mitgeliefert werden, aus dem die erforderlichen Daten f&uuml;r die Zeitbuchung geholt werden.
	 * 
	 * @return <code>true</code> wenn eine ENDE Zeitbuchung erfolgen soll, sonst <code>false</code>
	 */
	public Boolean getBookStopping() {
		return bookStopping;
	}
	public void setBookStopping(Boolean bookStopping) {
		this.bookStopping = bookStopping;
	}

	/**
	 * Beinhaltet die Eigenschaften f&uuml;r eine Zeitbuchung (optional)<br>
	 * Wird verwendet f&uuml;r die ENDE Zeitbuchung, wenn <code>bookStopping</code> auf <code>true</code> ist 
	 * und/oder der Zeitbuchung f&uuml;r den Stopp der Maschine,
	 * wenn <code>bookMachineStopping</code> auf <code>true</code> ist.
	 * 
	 * @return Eigenschaften einer Zeitbuchung
	 */
	public TimeRecordingEntry getTimeRecordingEntry() {
		return timeRecordingEntry;
	}
	public void setTimeRecordingEntry(TimeRecordingEntry timeRecordingEntry) {
		this.timeRecordingEntry = timeRecordingEntry;
	}

	/**
	 * Initiiert eine Maschinenstoppzeitbuchung (optional)<br>
	 * Wenn auf <code>true</code>, dann muss zus&auml;tzlich das <code>TimeRecordingEntry</code>
	 * mitgeliefert werden, aus dem die erforderlichen Daten f&uuml;r die Zeitbuchung geholt werden,
	 * und der Arbeitsgang aus dem Los <code>productionWorkplanId</code>. 
	 * 
	 * @return <code>true</code> wenn eine Maschinenstoppzeitbuchung erfolgen soll, sonst <code>false</code>
	 */
	public Boolean getBookMachineStopping() {
		return bookMachineStopping;
	}
	public void setBookMachineStopping(Boolean bookMachineStopping) {
		this.bookMachineStopping = bookMachineStopping;
	}

	/**
	 * Id des Arbeitsgang des Lossollarbeitsplans (optional)<br>
	 * Erforderlich, wenn <code>bookMachineStopping</code> auf <code>true</code> ist.
	 * 
	 * @return Id des Arbeitsgang des Lossollarbeitsplans
	 */
	public Integer getProductionWorkplanId() {
		return productionWorkplanId;
	}
	public void setProductionWorkplanId(Integer productionWorkplanId) {
		this.productionWorkplanId = productionWorkplanId;
	}

	/**
	 * Schrottmenge der Losablieferung (optional)<br>
	 * Diese Menge wird auf das definierte Schrottlager gebucht.
	 * 
	 * @return Schrottmenge der Losablieferung
	 */
	public BigDecimal getScrapAmount() {
		return scrapAmount;
	}
	public void setScrapAmount(BigDecimal scrapAmount) {
		this.scrapAmount = scrapAmount;
	}

	/**
	 * Initiiert eine Aenderung der Losgr&ouml;&szlig;e um die Schrottmenge
	 * 
	 * @return <code>true</code> wenn die Losgr&ouml;&szlig;e um die Schrottmenge erh&ouml;ht werden soll, sonst <code>false</code>
	 */
	public Boolean getIncreaseLotSizeByScrapAmount() {
		return increaseLotSizeByScrapAmount;
	}
	public void setIncreaseLotSizeByScrapAmount(
			Boolean increaseLotSizeByScrapAmount) {
		this.increaseLotSizeByScrapAmount = increaseLotSizeByScrapAmount;
	}

	/**
	 * Die Liste der Seriennr/Chargennr Informationen (optional)</br>
	 * Wenn angegeben, muss die Summe dieser Mengen die Menge der Losablieferung (<code>amount</code>) ergeben.
	 * 
	 * @return die (leere) Liste
	 */
	public List<IdentityAmountEntry> getIdentities() {
		return identities;
	}
	public void setIdentities(List<IdentityAmountEntry> identities) {
		this.identities = identities;
	}
}
