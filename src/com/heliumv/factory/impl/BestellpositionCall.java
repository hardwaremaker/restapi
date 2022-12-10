package com.heliumv.factory.impl;

import java.rmi.RemoteException;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.factory.BaseCall;
import com.heliumv.factory.IBestellpositionCall;
import com.heliumv.factory.IGlobalInfo;
import com.lp.server.bestellung.service.BestellpositionDto;
import com.lp.server.bestellung.service.BestellpositionFac;

public class BestellpositionCall extends BaseCall<BestellpositionFac> implements IBestellpositionCall {	
	@Autowired
	private IGlobalInfo globalInfo; 
	
	public BestellpositionCall() {
		super(BestellpositionFac.class);
	}

	public BestellpositionDto[] bestellpositionFindByBestellung(Integer bestellungId) throws RemoteException {
		return getFac().bestellpositionFindByBestellung(bestellungId, globalInfo.getTheClientDto());
	}
	
	public BestellpositionDto bestellpositionFindByPrimaryKey(Integer positionId) throws RemoteException {
		return getFac().bestellpositionFindByPrimaryKeyOhneExc(positionId);
	}
}
