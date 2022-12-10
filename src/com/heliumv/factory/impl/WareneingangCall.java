package com.heliumv.factory.impl;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.annotation.HvJudge;
import com.heliumv.factory.BaseCall;
import com.heliumv.factory.IGlobalInfo;
import com.heliumv.factory.IWareneingangCall;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.bestellung.service.RueckgabeWEPMitReelIDDto;
import com.lp.server.bestellung.service.WareneingangDto;
import com.lp.server.bestellung.service.WareneingangFac;
import com.lp.server.bestellung.service.WareneingangspositionDto;

public class WareneingangCall extends BaseCall<WareneingangFac> implements IWareneingangCall {
	@Autowired
	private IGlobalInfo globalInfo ;
	
	public WareneingangCall() {
		 super(WareneingangFac.class);
	}

	@Override
	@HvJudge(rechtOder={RechteFac.RECHT_BES_BESTELLUNG_CUD, RechteFac.RECHT_BES_BESTELLUNG_R})
	public WareneingangDto wareneingangFindByPrimaryKeyOhneExc(Integer wareneingangId) throws RemoteException {
		return getFac().wareneingangFindByPrimaryKey(wareneingangId);
	}
	
	@HvJudge(rechtOder={RechteFac.RECHT_BES_BESTELLUNG_CUD, RechteFac.RECHT_BES_BESTELLUNG_R})
	public WareneingangDto[] wareneingangFindByBestellungIId(Integer bestellungId) throws RemoteException {
		return getFac().wareneingangFindByBestellungIId(bestellungId);
	}
 
	@Override
	@HvJudge(recht=RechteFac.RECHT_BES_WARENEINGANG_CUD)
	public Integer createWareneingang(WareneingangDto wareneingangDto) throws RemoteException {
		return getFac().createWareneingang(wareneingangDto, globalInfo.getTheClientDto());
	}
	
	@Override
	@HvJudge(rechtOder={RechteFac.RECHT_BES_WARENEINGANG_CUD, RechteFac.RECHT_BES_BESTELLUNG_CUD, RechteFac.RECHT_BES_BESTELLUNG_R})
	public WareneingangspositionDto[] wareneingangspositionFindByWareneingangIId(
			Integer wareneingangId) throws RemoteException {
		return getFac().wareneingangspositionFindByWareneingangIId(wareneingangId);
	}
	
	@Override
	@HvJudge(rechtOder={RechteFac.RECHT_BES_WARENEINGANG_CUD, RechteFac.RECHT_BES_BESTELLUNG_CUD, RechteFac.RECHT_BES_BESTELLUNG_R})
	public WareneingangspositionDto[] wareneingangspositionFindByBestellpositionIId(Integer bestellpositionId) throws RemoteException {
		return getFac().wareneingangspositionFindByBestellpositionIId(bestellpositionId);
	}
	
	@Override
	@HvJudge(rechtOder={RechteFac.RECHT_BES_WARENEINGANG_CUD, RechteFac.RECHT_BES_BESTELLUNG_CUD, RechteFac.RECHT_BES_BESTELLUNG_R})
	public List<WareneingangDto> wareneingangFindByLieferscheinnummer(
			Integer bestellungId, String lieferscheinCnr) throws RemoteException {
		return getFac().wareneingangFindByLieferscheinnummer(bestellungId, lieferscheinCnr);
	}
	
	@Override
	@HvJudge(recht=RechteFac.RECHT_BES_WARENEINGANG_CUD)
	public Integer createWareneingangsposition(WareneingangspositionDto posDto) throws RemoteException {
		return getFac().createWareneingangsposition(posDto, false, globalInfo.getTheClientDto());
	}
	
	@Override
	@HvJudge(rechtOder={RechteFac.RECHT_BES_WARENEINGANG_CUD, RechteFac.RECHT_BES_BESTELLUNG_CUD, RechteFac.RECHT_BES_BESTELLUNG_R})
	public WareneingangspositionDto wareneingangspositionFindByPrimaryKey(Integer positionId) throws RemoteException {
		return getFac().wareneingangspositionFindByPrimaryKeyOhneExc(positionId);
	}
	
	@Override
	@HvJudge(recht=RechteFac.RECHT_BES_WARENEINGANG_CUD)
	public void updateWareneingangsposition(WareneingangspositionDto positionDto,
			boolean setartikelAufloesen) throws RemoteException {
		getFac().updateWareneingangsposition(positionDto, setartikelAufloesen, globalInfo.getTheClientDto());
	}
	
	@Override
	@HvJudge(recht=RechteFac.RECHT_BES_WARENEINGANG_CUD)
	public RueckgabeWEPMitReelIDDto wareneingangspositionMitReelIDBuchen(
			Integer wareneingangIId, Integer bestellpositionIId,
			BigDecimal nMenge, String datecode, String expirationDate) {
		return getFac().wareneingangspositionMitReelIDBuchen(wareneingangIId,
				bestellpositionIId, nMenge, datecode, expirationDate, globalInfo.getTheClientDto());
	}
}
