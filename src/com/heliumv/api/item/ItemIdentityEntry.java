package com.heliumv.api.item;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Die Seriennummer bzw. Chargennummer mit der zugehoerigen Menge 
 * @author Gerold
 */
@XmlRootElement
public class ItemIdentityEntry {
	private String identity ;
	private BigDecimal amount ;
	
	private String version;
	private String bundleIdentity ;
	private BigDecimal bundleAmount ;
	
	/**
	 * Die Serien- oder Chargennummer
	 * @return die Serien- oder Chargennummer
	 */
	public String getIdentity() {
		return identity;
	}
	public void setIdentity(String identity) {
		this.identity = identity;
	}
	
	/**
	 * Die Menge dieser Seriennr (1) bzw. Chargennummer (>= 0)
	 * @return die Menge dieser Serien- bzw. Chargennummer
	 */
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	/**
	 * Die Versionsinformation dieser Serien- bzw. Chargennummer
	 * @return
	 */
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * Die Gebindenummer
	 * @return die Gebindenummer
	 */
	public String getBundleIdentity() {
		return bundleIdentity;
	}
	public void setBundleIdentity(String bundleIdentity) {
		this.bundleIdentity = bundleIdentity;
	}
	
	/**
	 * Die Gebindemenge
	 * @return die Gebindemenge
	 */
	public BigDecimal getBundleAmount() {
		return bundleAmount;
	}
	public void setBundleAmount(BigDecimal bundleAmount) {
		this.bundleAmount = bundleAmount;
	}
}
