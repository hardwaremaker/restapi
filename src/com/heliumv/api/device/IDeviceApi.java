package com.heliumv.api.device;

import java.rmi.RemoteException;

import javax.naming.NamingException;

/**
 * Rund um die Ger&auml;tekonfiguration
 * 
 * @author gerold
 */
public interface IDeviceApi {
	/**
	 * Die ger&auml;tespezifische Konfiguration ermitteln
	 * 
	 * @param userId des bei HELIUM V angemeldeten API Benutzer
	 * @param deviceCnr die eindeutige Ger&auml;tenummer
	 * @param deviceType der Ger&auml;tetyp
	 * @param deviceTag eine optionale zus&auml;tzliche Kennung des Ger&auml;tes 
	 * @return die Konfiguration sofern deviceCnr vorhanden, ansonsten NOT FOUND
	 * @throws NamingException
	 * @throws RemoteException
	 */
	DeviceConfigEntry getConfig(String userId,
			String deviceCnr, String deviceType, String deviceTag) throws NamingException, RemoteException ;

	/**
	 * Ab&auml;ndern der benutzerdefinierten Ger&auml;tekonfiguration
	 * @param userId des bei HELIUM V angemeldeten API Benutzer
	 * @param entry die Daten der neuen Konfiguration
	 * @throws NamingException
	 * @throws RemoteException
	 */
	void updateConfig(String userId,
			DeviceConfigEntry entry) throws NamingException, RemoteException ;
}
