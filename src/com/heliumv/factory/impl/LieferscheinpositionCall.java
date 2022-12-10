package com.heliumv.factory.impl;

import java.rmi.RemoteException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.annotation.HvJudge;
import com.heliumv.annotation.HvModul;
import com.heliumv.factory.BaseCall;
import com.heliumv.factory.IGlobalInfo;
import com.heliumv.factory.ILieferscheinpositionCall;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.lieferschein.service.LieferscheinpositionDto;
import com.lp.server.lieferschein.service.LieferscheinpositionFac;
import com.lp.server.system.service.LocaleFac;

public class LieferscheinpositionCall extends BaseCall<LieferscheinpositionFac> implements ILieferscheinpositionCall {
	@Autowired
	private IGlobalInfo globalInfo ;

	public LieferscheinpositionCall() {
		super(LieferscheinpositionFac.class);
	}
	
	@HvModul(modul=LocaleFac.BELEGART_LIEFERSCHEIN)
	@HvJudge(recht=RechteFac.RECHT_LS_LIEFERSCHEIN_CUD)	
	public Integer createLieferscheinposition(LieferscheinpositionDto lsposDto, 
			boolean bArtikelSetAufloesen, List<SeriennrChargennrMitMengeDto> identities) throws RemoteException {
		return getFac().createLieferscheinposition(lsposDto, 
				bArtikelSetAufloesen, identities, globalInfo.getTheClientDto()) ;
 	}
	
	@HvModul(modul=LocaleFac.BELEGART_LIEFERSCHEIN)
	@HvJudge(recht=RechteFac.RECHT_LS_LIEFERSCHEIN_CUD)	
	public Integer createLieferscheinpositionVerteilen(LieferscheinpositionDto lsposDto, 
			boolean bArtikelSetAufloesen, List<SeriennrChargennrMitMengeDto> identities) throws RemoteException {
		return getFac().createLieferscheinpositionService(lsposDto, 
				bArtikelSetAufloesen, identities, true, globalInfo.getTheClientDto()) ;
 	}

	@HvModul(modul=LocaleFac.BELEGART_LIEFERSCHEIN)
	@HvJudge(recht=RechteFac.RECHT_LS_LIEFERSCHEIN_CUD)	
	public void removeLieferscheinposition(Integer positionId) throws RemoteException {
		LieferscheinpositionDto lsposDto = 
				getFac().lieferscheinpositionFindByPrimaryKey(positionId, globalInfo.getTheClientDto()) ;
		getFac().removeLieferscheinposition(lsposDto, globalInfo.getTheClientDto());
	}
	
	@HvModul(modul=LocaleFac.BELEGART_LIEFERSCHEIN)
	@HvJudge(rechtOder={RechteFac.RECHT_LS_LIEFERSCHEIN_CUD, RechteFac.RECHT_LS_LIEFERSCHEIN_R})	
	public LieferscheinpositionDto lieferscheinpositionFindByPrimaryKey(Integer positionId) throws RemoteException {
		LieferscheinpositionDto lsposDto = 
				getFac().lieferscheinpositionFindByPrimaryKeyOhneExc(positionId, globalInfo.getTheClientDto()) ;
		return lsposDto ;
	}
	
	@HvModul(modul=LocaleFac.BELEGART_LIEFERSCHEIN)
	@HvJudge(recht=RechteFac.RECHT_LS_LIEFERSCHEIN_CUD)	
	public LieferscheinpositionDto[] lieferscheinpositionFindByAuftragpositionIId(
			Integer iIdAuftragpositionI) throws RemoteException {
		return getFac().lieferscheinpositionFindByAuftragpositionIId(iIdAuftragpositionI, globalInfo.getTheClientDto()) ;
	}
	
	
	@HvModul(modul=LocaleFac.BELEGART_LIEFERSCHEIN)
	@HvJudge(recht=RechteFac.RECHT_LS_LIEFERSCHEIN_CUD)	
	public Integer updateLieferscheinposition(LieferscheinpositionDto lsposDto, 
			List<SeriennrChargennrMitMengeDto> identities) throws RemoteException {
		return getFac().updateLieferscheinposition(
				lsposDto, identities, globalInfo.getTheClientDto()) ;
 	}
	
	@Override
	@HvModul(modul=LocaleFac.BELEGART_LIEFERSCHEIN)
	@HvJudge(rechtOder={RechteFac.RECHT_LS_LIEFERSCHEIN_CUD, RechteFac.RECHT_LS_LIEFERSCHEIN_R})
	public LieferscheinpositionDto[] getLieferscheinPositionenByLieferschein(Integer lieferscheinIId) throws RemoteException {
		return getFac().getLieferscheinPositionenByLieferschein(lieferscheinIId, globalInfo.getTheClientDto());
	}
	
	@Override
	@HvModul(modul=LocaleFac.BELEGART_LIEFERSCHEIN)
	public void sortiereNachArtikelnummer(Integer lieferscheinId) throws RemoteException {
		getFac().sortiereNachArtikelnummer(lieferscheinId, globalInfo.getTheClientDto());
	}
	
	@Override
	@HvModul(modul=LocaleFac.BELEGART_LIEFERSCHEIN)
	public void sortiereNachAuftragsnummer(Integer lieferscheinId) throws RemoteException {
		getFac().sortiereNachAuftragsnummer(lieferscheinId, globalInfo.getTheClientDto());
	}
}
