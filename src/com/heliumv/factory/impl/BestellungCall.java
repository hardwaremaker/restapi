package com.heliumv.factory.impl;

import java.rmi.RemoteException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.annotation.HvJudge;
import com.heliumv.factory.BaseCall;
import com.heliumv.factory.IBestellungCall;
import com.heliumv.factory.IGlobalInfo;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.bestellung.service.BestellungFac;

public class BestellungCall extends BaseCall<BestellungFac> implements IBestellungCall {
	
	@Autowired
	private IGlobalInfo globalInfo; 
	
	public BestellungCall() {
		super(BestellungFac.class);
	}
	
	@Override
	@HvJudge(rechtOder={RechteFac.RECHT_BES_BESTELLUNG_CUD, RechteFac.RECHT_BES_BESTELLUNG_R})
	public BestellungDto bestellungFindByCNrMandantCNr(String cnr) {
		return getFac().bestellungFindByCNrMandantCNr(cnr, globalInfo.getMandant());
	}

	@Override
	@HvJudge(rechtOder={RechteFac.RECHT_BES_BESTELLUNG_CUD, RechteFac.RECHT_BES_BESTELLUNG_R})
	public BestellungDto bestellungFindByCNrMandantCNr(String cnr, String mandantCnr) {
		return getFac().bestellungFindByCNrMandantCNr(cnr, mandantCnr);
	}
	
	@Override
	@HvJudge(rechtOder={RechteFac.RECHT_BES_BESTELLUNG_CUD, RechteFac.RECHT_BES_BESTELLUNG_R})
	public BestellungDto bestellungFindByPrimaryKeyOhneExc(Integer id) {
		return getFac().bestellungFindByPrimaryKeyWithNull(id);
	}
	
	@Override
	@HvJudge(rechtOder={RechteFac.RECHT_BES_BESTELLUNG_CUD, RechteFac.RECHT_BES_BESTELLUNG_R})
	public List<BestellungDto> bestellungFindByLieferantIIdBestelladresseMandantCNrFilter(
			Integer lieferantId, boolean receiptPossible) throws RemoteException {
		String[] filter = receiptPossible ? new String[]{
				BestellungFac.BESTELLSTATUS_OFFEN, BestellungFac.BESTELLSTATUS_TEILERLEDIGT,
				BestellungFac.BESTELLSTATUS_BESTAETIGT
		} : null;
		return getFac().bestellungFindByLieferantIIdBestelladresseMandantCNrFilter(
				lieferantId, globalInfo.getMandant(), filter);
	}
}
