package com.heliumv.factory;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.List;

import javax.naming.NamingException;

import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungzahlungDto;

public interface IRechnungCall {
	void repairRechnungZws2276(Integer rechnungId) throws NamingException  ;
	
	List<Integer> repairRechnungZws2276GetList() throws NamingException ;

	RechnungDto rechnungFindByPrimaryKeyOhneExc(Integer iId) ;
	RechnungDto rechnungFindByRechnungartCNrMandantCNrOhneExc(
			String rechnungCnr) ;
	RechnungzahlungDto createZahlung(
			RechnungzahlungDto zahlungDto, boolean bErledigt) throws RemoteException;	
	RechnungzahlungDto rechnungzahlungFindByPrimaryKeyOhneExc(Integer zahlungId) throws RemoteException; 
	
	BigDecimal getBereitsBezahltWertBruttoVonRechnungFwOhneFSkonto(
					Integer rechnungId, Integer excludeZahlungsId) throws RemoteException;
	
	BigDecimal getWertUstAnteiligZuRechnungUst(Integer rechnungIId,
			BigDecimal bruttoBetrag);
	
	RechnungzahlungDto[] zahlungFindByRechnungId(Integer rechnungId) throws RemoteException;
	void removeZahlung(RechnungzahlungDto zahlungDto) throws RemoteException;	
}
