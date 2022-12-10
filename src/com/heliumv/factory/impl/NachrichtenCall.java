package com.heliumv.factory.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.factory.BaseCall;
import com.heliumv.factory.IGlobalInfo;
import com.heliumv.factory.INachrichtenCall;
import com.lp.server.personal.service.NachrichtenFac;

public class NachrichtenCall extends BaseCall<NachrichtenFac> implements INachrichtenCall {

	@Autowired
	private IGlobalInfo globalInfo ;

	public NachrichtenCall() {
		super(NachrichtenFac.class);
	}
	
	@Override
	public void nachrichtZeitdatenpruefen(Integer personalId) {
		getFac().nachrichtZeitdatenpruefen(personalId, globalInfo.getTheClientDto());
	}
}
