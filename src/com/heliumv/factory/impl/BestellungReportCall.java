package com.heliumv.factory.impl;

import java.rmi.RemoteException;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.factory.BaseCall;
import com.heliumv.factory.IBestellungReportCall;
import com.heliumv.factory.IGlobalInfo;
import com.lp.server.bestellung.service.BestellungReportFac;

public class BestellungReportCall extends BaseCall<BestellungReportFac> implements IBestellungReportCall {
	@Autowired
	private IGlobalInfo globalInfo;
	
	
	public BestellungReportCall() {
		super(BestellungReportFac.class);
	}
	
	public void printWepEtikett(Integer wareneingangspositionId) throws RemoteException {
		getFac().printWepEtikettOnServer(wareneingangspositionId, null, globalInfo.getTheClientDto());
	}
	
	public void printWepEtikett(Integer wareneingangspositionId, String chargennummer) throws RemoteException {
		getFac().printWepEtikettOnServer(wareneingangspositionId, chargennummer, globalInfo.getTheClientDto());
	}
}
