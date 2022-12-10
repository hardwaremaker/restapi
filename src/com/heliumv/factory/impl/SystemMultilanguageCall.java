package com.heliumv.factory.impl;

import java.rmi.RemoteException;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.factory.BaseCall;
import com.heliumv.factory.IGlobalInfo;
import com.heliumv.factory.ISystemMultilanguageCall;
import com.lp.server.system.service.SystemMultilanguageFac;

public class SystemMultilanguageCall extends BaseCall<SystemMultilanguageFac> implements ISystemMultilanguageCall {
	@Autowired
	private IGlobalInfo globalInfo;
	
	public SystemMultilanguageCall() {
		super(SystemMultilanguageFac.class);
	}
	
	public String getTextRespectUISpr(String sTokenI) throws RemoteException {
		return getFac().getTextRespectUISpr(sTokenI, globalInfo.getMandant(),
				globalInfo.getTheClientDto().getLocUi());
	}

	public String getTextRespectUISpr(String sTokenI, String mandantCNr,
			Locale loI) throws RemoteException {
		return getFac().getTextRespectUISpr(sTokenI, mandantCNr, loI);
	}
}
