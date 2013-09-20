package com.heliumv.api.system;

/**
 * Systeminformationen abfragen
 * 
 * @author Gerold
 */
public interface ISystemApi {
	/**
	 * Pr�ft, ob vom API-Server ein Zugriff auf den HELIUM V Server m�glich ist.
	 *  
	 * @return verschiedene Informationen �ber das System
	 */
	PingResult ping() ;
}
