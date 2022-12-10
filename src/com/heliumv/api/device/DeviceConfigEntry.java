package com.heliumv.api.device;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.BaseEntryCnr;

/**
 * Die Daten der Ger&auml;tekonfiguration</br>
 * <p>Teile der Konfiguration k&ouml;nnen nur durch den HELIUM V Anwender
 * abge&auml;ndert werden.</p>
 * 
 * @author gerold
 */
@XmlRootElement
public class DeviceConfigEntry extends BaseEntryCnr {
	private String systemConfig ;
	private String userConfig ;
	private String deviceType ;
	private String deviceTag ;

	/**
	 * Die "System" konfiguration. Diese Daten werden beim Einrichten des angegebenen
	 * Ger&auml;tes innerhalb des HELIUM V Systems gesetzt und k&ouml;nnen auch nur 
	 * vom HELIUM V System abge&auml;ndert werden.
	 * 
	 * @return die Systemkonfiguration
	 */
	public String getSystemConfig() {
		return systemConfig;
	}
	public void setSystemConfig(String systemConfig) {
		this.systemConfig = systemConfig;
	}
	
	/**
	 * Die benutzerspezifische Konfiguration. Diese kann vom API Anwender abge&auml;ndert werden.
	 * 
	 * @return die vom API Anwender hinterlegte Konfiguration
	 */
	public String getUserConfig() {
		return userConfig;
	}
	public void setUserConfig(String userConfig) {
		this.userConfig = userConfig;
	}
	
	/**
	 * Der Ger&auml;tetyp wie er im HELIUM V System definiert wurde
	 * @return der (optionale) Ger&auml;tetyp
	 */
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	
	public String getDeviceTag() {
		return deviceTag;
	}
	public void setDeviceTag(String deviceTag) {
		this.deviceTag = deviceTag;
	}
}
