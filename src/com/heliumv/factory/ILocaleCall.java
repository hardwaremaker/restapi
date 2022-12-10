package com.heliumv.factory;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;

import com.lp.server.system.service.WaehrungDto;
import com.lp.server.system.service.WechselkursDto;

public interface ILocaleCall {
	WaehrungDto waehrungFindByPrimaryKey(String cNr) throws RemoteException ;
	
	/**
	 * Wechselkurs von der Mandantenw&auml;hrung zur Zielw&auml;hrung
	 * 
	 * @param toWaehrungCnr ist die Ziel&auml;hrung
	 * @return den Wechselkurs von der Mandantenw&auml;hrung in die Zielw&auml;hrung
	 * @throws RemoteException
	 */
	BigDecimal getWechselkurs2(String toWaehrungCnr) throws RemoteException ;

	/**
	 * Wechselkurs von der Quellw&auml;hrung zur Zielw&auml;hrung
	 * 
	 * @param fromWaehrungCnr die Ausgangs/Quellw&auml;hrung
	 * @param toWaehrungCnr die Zielw&auml;hrung
	 * @return der Wechselkurs
	 * @throws RemoteException
	 */
	BigDecimal getWechselkurs2(String fromWaehrungCnr, String toWaehrungCnr) throws RemoteException ;
	
	/**
	 * Ermittelt WechselkursDto f&uuml;r die Zielw&auml;hrung, ausgehend von der Mandantenw&auml;hrung, 
	 * passend zum angegebenen Datum
	 * @param toWaehrungCnr
	 * @param date
	 * @return den Wechselkurs zum angegebenen Datum, passend zur Zielw&auml;hrung, abh&auml;ngig von der
	 * Mandantenw&auml;hrung
	 * @throws RemoteException
	 */
	public WechselkursDto getKursZuDatum(String toWaehrungCnr, Date date) throws RemoteException;
	
	/**
	 * Ermittelt WechselkursDto passend zur Ausgangs- und Zielw&auml;hrung und dem angegebenen datum
	 * @param fromWaehrungCnr
	 * @param toWaehrungCnr
	 * @param date
	 * @return WechselkursDto passend zur Zielw&auml;hrung und dem gegebenen Datum
	 * @throws RemoteException
	 */
	public WechselkursDto getKursZuDatum(String fromWaehrungCnr, String toWaehrungCnr, Date date) throws RemoteException;

}
