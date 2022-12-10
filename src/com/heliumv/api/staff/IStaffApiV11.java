package com.heliumv.api.staff;

import java.rmi.RemoteException;
import java.util.List;

import javax.naming.NamingException;
import javax.ws.rs.QueryParam;

import com.lp.util.EJBExceptionLP;

public interface IStaffApiV11 {

	/**
	 * Eine Liste aller Mitarbeiter ermitteln</br>
	 * 
	 * @param userId des am HELIUM V Servers angemeldeten Benutzers
	 * @param limit die Anzahl der auszugebenden Datens&auml;tze. Wird der Parameter nicht angegeben,
	 *  so werden bis zu 50 Datens&auml;tze geliefert. Wird 0 angegeben, werden alle S&auml;tze 
	 *  ausgegeben 
	 * @param startIndex ist jene (staffEntry.)Id ab der die Auflistung erfolgen soll. Speziell bei
	 *  seitenweiser Auflistung - Parameter limit ist gesetzt - hilfreich um den Startdatensatz der
	 *  Seite zu erhalten
	 * @return eine (leere) Liste aller Mitarbeiter auf die der angemeldete Benutzer zugreifen darf
	 * @throws NamingException 
	 * @throws EJBExceptionLP 
	 * @throws RemoteException 
	 */
	List<StaffEntry> getStaff(
			@QueryParam("userid") String userId,
			@QueryParam("limit") Integer limit,
			@QueryParam("startIndex") Integer startIndex) throws RemoteException, EJBExceptionLP, NamingException ;

	/**
	 * Einen Mitarbeiter liefern.
	 * 
	 * @param userId des am HELIUM V Servers angemeldeten Benutzers
	 * @param forStaffId ist die optionale PersonalId des Mitarbeiters, der abgerufen werden soll. 
	 * @param forStaffCnr ist die optionale Personalnummer des Mitarbeiters, der abgerufen werden soll.
	 * @param forStaffIdCard ist die optionale Ausweisnummer des Mitarbeiters, der abgerufen werden soll.
	 *  Sind zumindest zwei der drei Parameter forStaffId, forStaffCnr und forStaffIdCard angegeben, 
	 *  werden die Parameter nach dieser Reihenfolge priorisiert verwendet. Sind alle drei nicht angegeben, 
	 *  wird der Mitarbeiter geliefert, der dem angemeldeten Benutzer zugewiesen ist.
	 * @return der gefundene Mitarbeiter als <code>StaffEntry</code>
	 * @throws NamingException
	 * @throws RemoteException
	 */
	StaffEntry findStaff(
			String userId,
			Integer forStaffId,
			String forStaffCnr,
			String forStaffIdCard) throws NamingException, RemoteException;
	
	/**
	 * Der Mitarbeiter hat synchronisiert.
	 * 
	 * @param entry
	 * @throws NamingException
	 * @throws RemoteException
	 */
	void synch(SynchEntry entry) throws NamingException, RemoteException;
}
