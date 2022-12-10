package com.heliumv.api.machine;

import java.rmi.RemoteException;

import javax.naming.NamingException;

import com.lp.util.EJBExceptionLP;

public interface IMachineApi {
	/**
	 * Eine Liste aller verf&uuml;gbaren Maschinen</br>
	 * 
	 * @param userId
	 * @param limit
	 * @param startIndex
	 * @param filterWithHidden
	 * @param filterProductiongrupId optional die Fertigungsgruppe-Id auf die die Auflistung
	 *   der Maschinen eingeschr&auml;nkt werden soll
	 * @param filterPlanningView mit TRUE nur jene Maschinen liefern, die sich in einer
	 * Maschinengruppe befinden, die in der Planungsanzeige dargestellt werden soll 
	 * @param filterStaffId optional die Personal-Id jener Person, die eine laufende Maschine 
	 * zuletzt gestartet hat
	 * 
	 * @return
	 * @throws RemoteException
	 * @throws NamingException
	 * @throws EJBExceptionLP
	 */
	MachineEntryList getMachines(
			String userId,
			Integer limit,
			Integer startIndex,			
			Boolean filterWithHidden, 
			Integer productiongroupId,
			Boolean filterPlanningView,
			Integer filterStaffId) throws RemoteException, NamingException, EJBExceptionLP ;

	/**
	 * Eine Liste aller Maschinengruppen</br>
	 * 
	 * @param userId
	 * @param limit
	 * @param startIndex
	 * @param filterProductiongroupId optional die Id der Fertigungsgruppe auf die die Maschinengruppen 
	 *   eingeschr&auml;nkt werden sollen
	 * @param filterPlanningView mit TRUE nur jene Maschinen liefern, die sich in einer
	 * Maschinengruppe befinden, die in der Planungsanzeige dargestellt werden soll 
	 * @return
	 * @throws RemoteException
	 * @throws NamingException
	 * @throws EJBExceptionLP
	 */
	MachineGroupEntryList getMachineGroups(
			String userId,
			Integer limit,
			Integer startIndex,
			Integer filterProductiongroupId,
			Boolean filterPlanningView) throws RemoteException, NamingException, EJBExceptionLP;
}
