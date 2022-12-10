package com.heliumv.factory.impl;

import java.rmi.RemoteException;
import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.annotation.HvJudge;
import com.heliumv.factory.BaseCall;
import com.heliumv.factory.IEingangsrechnungCall;
import com.heliumv.factory.IGlobalInfo;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.eingangsrechnung.service.EingangsrechnungAuftragszuordnungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungFac;
import com.lp.server.system.jcr.service.JCRDocDto;

public class EingangsrechnungCall extends BaseCall<EingangsrechnungFac> implements IEingangsrechnungCall {

	@Autowired
	private IGlobalInfo globalInfo ;

	public EingangsrechnungCall() {
		super(EingangsrechnungFac.class);
	}

	@Override
	@HvJudge(rechtOder={RechteFac.RECHT_ER_EINGANGSRECHNUNG_CUD, RechteFac.RECHT_ER_EINGANGSRECHNUNG_R})
	public EingangsrechnungDto eingangsrechnungFindByPrimaryKeyOhneExc(Integer eingangsrechnungIId) throws RemoteException {
		return getFac().eingangsrechnungFindByPrimaryKeyOhneExc(eingangsrechnungIId);
	}
	
	@Override
	@HvJudge(rechtOder={RechteFac.RECHT_ER_EINGANGSRECHNUNG_CUD, RechteFac.RECHT_ER_EINGANGSRECHNUNG_R})
	public EingangsrechnungDto eingangsrechnungFindByCNrMandantCNr(String cnr) {
		return getFac().eingangsrechnungFindByCNrMandantCNr(cnr, globalInfo.getMandant(),false);
	}
	
	@Override
	public Date getDefaultFreigabeDatum() throws RemoteException {
		return getFac().getDefaultFreigabeDatum();
	}
	
	// TODO Absichtlich keine Rechteueberpruefung, da der Verursacher des Belegs 
	// nicht zwangslaeufig Eingangsrechnungen anlegen koennen muss.
	// In diesem Falle legen "wir" (api) dies fuer ihn an
	@Override
	@HvJudge(rechtOder={RechteFac.RECHT_ER_EINGANGSRECHNUNG_CUD})
	public EingangsrechnungDto createEingangsrechnung(EingangsrechnungDto erDto) throws RemoteException {
		return getFac().createEingangsrechnung(erDto, globalInfo.getTheClientDto());
	}
	
	@Override
	@HvJudge(rechtOder={RechteFac.RECHT_ER_EINGANGSRECHNUNG_CUD})
	public EingangsrechnungAuftragszuordnungDto createEingangsrechnungAuftragszuordnung(
			EingangsrechnungAuftragszuordnungDto zuordnungDto) throws RemoteException {
		return getFac().createEingangsrechnungAuftragszuordnung(
				zuordnungDto, globalInfo.getTheClientDto());
	}
	
	@Override
	@HvJudge(rechtOder={RechteFac.RECHT_ER_EINGANGSRECHNUNG_CUD})
	public EingangsrechnungDto createEingangsrechnungMitDokument(
			EingangsrechnungDto erDto, Integer auftragId, JCRDocDto jcrDto) throws RemoteException {
		return getFac().createEingangsrechnungMitDokument(
				erDto, auftragId, jcrDto, globalInfo.getTheClientDto());
	}
}
