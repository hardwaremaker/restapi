package com.heliumv.api.hvma;

import java.rmi.RemoteException;

import javax.naming.NamingException;

import com.heliumv.api.user.HvmaLogonEntry;
import com.heliumv.api.user.LoggedOnEntry;

public interface IHvmaApi {

	LoggedOnEntry logonHvma(
			HvmaLogonEntry logonEntry) throws NamingException, RemoteException;


	HvmaParamEntryList getHvmaParams(
			String headerToken, String userId) throws NamingException, RemoteException;

	/**
	 * Die Liste der verf&uuml;gbaren HVMARechte ausgeben
	 * 
	 * @param headerToken
	 * @param userId ist der angemeldete Benutzer
	 * @param licenceCnr ist optional. Ist der Parameter angegeben, werden die
	 * im System verf&uuml;gbaren HVMA-Rechte f&uuml;r die angegebene Lizenz
	 * ermittelt. Ist der Parameter nicht angegeben, werden die HVMA-Rechte des
	 * angemeldeten HVMA-Benutzers ausgegeben.
	 * @return eine (leere) Liste der verf&uuml;gbaren HVMA-Rechte 
	 * @throws NamingException
	 * @throws RemoteException
	 */
	HvmarechtEnumEntryList getHvmaRechte(String headerToken, 
			String userId, String licenceCnr)
			throws NamingException, RemoteException;
}
