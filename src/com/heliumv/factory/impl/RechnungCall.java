package com.heliumv.factory.impl;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.List;

import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.annotation.HvJudge;
import com.heliumv.annotation.HvModul;
import com.heliumv.factory.BaseCall;
import com.heliumv.factory.IGlobalInfo;
import com.heliumv.factory.IRechnungCall;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.rechnung.service.RechnungzahlungDto;
import com.lp.server.system.service.LocaleFac;

public class RechnungCall extends BaseCall<RechnungFac> implements
		IRechnungCall {
	
	@Autowired
	private IGlobalInfo globalInfo ;
	
	public RechnungCall() {
		super(RechnungFac.class);
	}
	
	@Override
	public void repairRechnungZws2276(Integer rechnungId) throws NamingException {
		getFac().repairRechnungZws2276(rechnungId, globalInfo.getTheClientDto()) ;
	}

	@Override
	public List<Integer> repairRechnungZws2276GetList() throws NamingException {
		return getFac().repairRechnungZws2276GetList(globalInfo.getTheClientDto());
	}

	@HvModul(modul=LocaleFac.BELEGART_RECHNUNG)
	@HvJudge(rechtOder={RechteFac.RECHT_RECH_RECHNUNG_CUD, RechteFac.RECHT_RECH_RECHNUNG_R})
	public RechnungDto rechnungFindByPrimaryKeyOhneExc(Integer iId) {
		return getFac().rechnungFindByPrimaryKeyOhneExc(iId) ;
	}
	
	@HvModul(modul=LocaleFac.BELEGART_RECHNUNG)
	@HvJudge(rechtOder={RechteFac.RECHT_RECH_RECHNUNG_CUD, RechteFac.RECHT_RECH_RECHNUNG_R})
	public RechnungzahlungDto rechnungzahlungFindByPrimaryKeyOhneExc(Integer zahlungId) throws RemoteException {
		return getFac().rechnungzahlungFindByPrimaryKey(zahlungId) ;
	}
	
	@HvModul(modul=LocaleFac.BELEGART_RECHNUNG)
	@HvJudge(rechtOder={RechteFac.RECHT_RECH_RECHNUNG_CUD, RechteFac.RECHT_RECH_RECHNUNG_R})
	public RechnungDto rechnungFindByRechnungartCNrMandantCNrOhneExc(
			String rechnungCnr) {
		RechnungDto[] entries = getFac()
				.rechnungFindByRechnungartCNrMandantCNrOhneExc(rechnungCnr, globalInfo.getMandant());
		if(entries == null || entries.length == 0) {
			return null ;
		}
		if(entries.length != 1) {
			throw new IllegalArgumentException(
					"1 RechnungDto expected, got " + entries.length + " for cnr '" + rechnungCnr + "'.") ;
		}
		return entries[0] ; 
	}
	
	@Override
	@HvModul(modul=LocaleFac.BELEGART_RECHNUNG)
	@HvJudge(rechtOder={RechteFac.RECHT_LP_DARF_PREISE_SEHEN_VERKAUF, RechteFac.RECHT_LP_DARF_PREISE_AENDERN_VERKAUF})
	public BigDecimal getBereitsBezahltWertBruttoVonRechnungFwOhneFSkonto(
			Integer rechnungId, Integer excludeZahlungsId) throws RemoteException {
		return getFac().getBereitsBezahltWertBruttoVonRechnungFwOhneFSkonto(rechnungId, excludeZahlungsId) ;
	}
	
	@HvModul(modul=LocaleFac.BELEGART_RECHNUNG)
	@HvJudge(rechtOder={RechteFac.RECHT_LP_DARF_PREISE_SEHEN_VERKAUF, RechteFac.RECHT_LP_DARF_PREISE_AENDERN_VERKAUF})
	public BigDecimal getWertUstAnteiligZuRechnungUst(Integer rechnungIId,
			BigDecimal bruttoBetrag) {
		return getFac().getWertUstAnteiligZuRechnungUst(rechnungIId, bruttoBetrag) ;
	}
		
	@HvModul(modul=LocaleFac.BELEGART_RECHNUNG)
	@HvJudge(recht=RechteFac.RECHT_RECH_RECHNUNG_CUD)
	public RechnungzahlungDto createZahlung(
			RechnungzahlungDto zahlungDto, boolean bErledigt) throws RemoteException {
		return getFac().createZahlung(zahlungDto, bErledigt, globalInfo.getTheClientDto()) ;
	}
	
	@HvModul(modul=LocaleFac.BELEGART_RECHNUNG)
	@HvJudge(rechtOder={RechteFac.RECHT_RECH_RECHNUNG_CUD, RechteFac.RECHT_RECH_RECHNUNG_R})
	public RechnungzahlungDto[] zahlungFindByRechnungId(Integer rechnungId) throws RemoteException {
		return getFac().zahlungFindByRechnungIId(rechnungId);
	}
	
	@HvModul(modul=LocaleFac.BELEGART_RECHNUNG)
	@HvJudge(recht=RechteFac.RECHT_RECH_RECHNUNG_CUD)
	public void removeZahlung(RechnungzahlungDto zahlungDto) throws RemoteException {
		getFac().removeZahlung(zahlungDto, globalInfo.getTheClientDto());  
	}
}
