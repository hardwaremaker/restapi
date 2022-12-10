package com.heliumv.factory.impl;

import java.rmi.RemoteException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.annotation.HvJudge;
import com.heliumv.annotation.HvModul;
import com.heliumv.factory.BaseCall;
import com.heliumv.factory.IBestellvorschlagCall;
import com.heliumv.factory.IGlobalInfo;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.bestellung.service.BestellvorschlagDto;
import com.lp.server.bestellung.service.BestellvorschlagFac;
import com.lp.server.bestellung.service.CreateBestellvorschlagDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.util.ArtikelId;
import com.lp.util.EJBExceptionLP;

public class BestellvorschlagCall extends BaseCall<BestellvorschlagFac> implements IBestellvorschlagCall {

	@Autowired
	private IGlobalInfo globalInfo; 

	public BestellvorschlagCall() {
		super(BestellvorschlagFac.class);
	}

	@Override
	@HvModul(modul=LocaleFac.BELEGART_BESTELLUNG)
	@HvJudge(rechtOder={RechteFac.RECHT_BES_BESTELLUNG_CUD, RechteFac.RECHT_BES_BESTELLUNG_R})
	public List<BestellvorschlagDto> bestellvorschlagFindByArtikelIdVormerkungMandantCNr(Integer artikelIId) {
		return getFac().bestellvorschlagFindByArtikelIdVormerkungMandantCNr(new ArtikelId(artikelIId), globalInfo.getMandant());
	}
	
	@Override
	@HvModul(modul=LocaleFac.BELEGART_BESTELLUNG)
	@HvJudge(recht=RechteFac.RECHT_BES_BESTELLUNG_CUD)
	public Integer createBestellvorschlag(CreateBestellvorschlagDto createDto) {
		return getFac().setupCreateBestellvorschlag(createDto, globalInfo.getTheClientDto());
	}
	
	@Override
	@HvModul(modul=LocaleFac.BELEGART_BESTELLUNG)
	@HvJudge(rechtOder={RechteFac.RECHT_BES_BESTELLUNG_CUD, RechteFac.RECHT_BES_BESTELLUNG_R})
	public void pruefeBearbeitenDesBestellvorschlagsErlaubt() throws EJBExceptionLP, RemoteException {
		getFac().pruefeBearbeitenDesBestellvorschlagsErlaubt(globalInfo.getTheClientDto());
	}
	
	@Override
	@HvModul(modul=LocaleFac.BELEGART_BESTELLUNG)
	@HvJudge(rechtOder={RechteFac.RECHT_BES_BESTELLUNG_CUD, RechteFac.RECHT_BES_BESTELLUNG_R})
	public void removeLockDesBestellvorschlagesWennIchIhnSperre() throws EJBExceptionLP, RemoteException {
		getFac().removeLockDesBestellvorschlagesWennIchIhnSperre(globalInfo.getTheClientDto());
	}
	
	@Override
	@HvModul(modul=LocaleFac.BELEGART_BESTELLUNG)
	@HvJudge(rechtOder={RechteFac.RECHT_BES_BESTELLUNG_CUD, RechteFac.RECHT_BES_BESTELLUNG_R})
	public BestellvorschlagDto bestellvorschlagFindByPrimaryKeyOhneExc(Integer bestellvorschlagIId) {
		return getFac().bestellvorschlagFindByPrimaryKeyOhneExc(bestellvorschlagIId);
	}
	
	@Override
	@HvModul(modul=LocaleFac.BELEGART_BESTELLUNG)
	@HvJudge(recht=RechteFac.RECHT_BES_BESTELLUNG_CUD)
	public void updateBestellvorschlag(BestellvorschlagDto bestellvorschlagDto) throws EJBExceptionLP, RemoteException {
		getFac().updateBestellvorschlag(bestellvorschlagDto, globalInfo.getTheClientDto());
	}
	
	@Override
	@HvModul(modul=LocaleFac.BELEGART_BESTELLUNG)
	@HvJudge(recht=RechteFac.RECHT_BES_BESTELLUNG_CUD)
	public void removeBestellvorschlag(Integer bestellvorschlagIId) throws EJBExceptionLP, RemoteException {
		getFac().removeBestellvorschlag(bestellvorschlagIId);
	}
}
