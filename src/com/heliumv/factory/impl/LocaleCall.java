package com.heliumv.factory.impl;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.factory.BaseCall;
import com.heliumv.factory.IGlobalInfo;
import com.heliumv.factory.ILocaleCall;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.WaehrungDto;
import com.lp.server.system.service.WechselkursDto;
import com.lp.util.EJBExceptionLP;

public class LocaleCall extends BaseCall<LocaleFac> implements ILocaleCall {
	@Autowired 
	private IGlobalInfo globalInfo ;

	public LocaleCall() {
		super(LocaleFac.class);
	}
	
	public WaehrungDto waehrungFindByPrimaryKey(String cNr) throws RemoteException {
		return getFac().waehrungFindByPrimaryKey(cNr) ;
	}	
	
	/**
	 * Wechselkurs von der Mandantenw&auml;hrung zur Zielw&auml;hrung
	 * 
	 * @param toWaehrungCnr ist die Ziel&auml;hrung
	 * @return den Wechselkurs von der Mandantenw&auml;hrung in die Zielw&auml;hrung
	 * @throws RemoteException
	 */
	public BigDecimal getWechselkurs2(String toWaehrungCnr) throws RemoteException {
		return getWechselkurs2(globalInfo.getTheClientDto().getSMandantenwaehrung(), toWaehrungCnr) ;
	}
	
	public BigDecimal getWechselkurs2(String fromWaehrungCnr, String toWaehrungCnr) throws RemoteException {
		return getFac().getWechselkurs2(fromWaehrungCnr, toWaehrungCnr, globalInfo.getTheClientDto()) ;
	}
	
	/**
	 * Ermittelt WechselkursDto f&uuml;r die Zielw&auml;hrung, ausgehend von der Mandantenw&auml;hrung, 
	 * passend zum angegebenen Datum
	 * @param toWaehrungCnr
	 * @param date
	 * @return den Wechselkurs zum angegebenen Datum, passend zur Zielw&auml;hrung, abh&auml;ngig von der
	 * Mandantenw&auml;hrung
	 * @throws RemoteException
	 */
	public WechselkursDto getKursZuDatum(String toWaehrungCnr, Date date) throws RemoteException {
		return getKursZuDatum(
				globalInfo.getTheClientDto().getSMandantenwaehrung(), toWaehrungCnr, date);
	}

	/**
	 * Ermittelt WechselkursDto passend zur Ausgangs- und Zielw&auml;hrung und dem angegebenen datum
	 * @param fromWaehrungCnr
	 * @param toWaehrungCnr
	 * @param date
	 * @return WechselkursDto passend zur Zielw&auml;hrung und dem gegebenen Datum
	 * @throws RemoteException
	 */
	public WechselkursDto getKursZuDatum(String fromWaehrungCnr,
			String toWaehrungCnr, Date date) throws RemoteException {
		WechselkursDto kursDto = getFac().getKursZuDatum(fromWaehrungCnr,
				toWaehrungCnr, date, globalInfo.getTheClientDto());
		if(kursDto == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_IM_WECHSELKURS_KEINE_MANDANTENWAEHRUNG_ENTHALTEN, toWaehrungCnr);
		}
		return kursDto;
	}
	
	public WaehrungDto waehrungFindByCnr(String cnr) throws RemoteException {
		return getFac().waehrungFindByPrimaryKeyWithNull(cnr);
	}
}

