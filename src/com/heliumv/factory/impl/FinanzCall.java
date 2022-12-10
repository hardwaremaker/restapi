package com.heliumv.factory.impl;

import java.rmi.RemoteException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.factory.BaseCall;
import com.heliumv.factory.IFinanzCall;
import com.heliumv.factory.IGlobalInfo;
import com.lp.server.finanz.service.BankverbindungDto;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.KontoDto;

public class FinanzCall extends BaseCall<FinanzFac> implements IFinanzCall {
	@Autowired
	private IGlobalInfo globalInfo ;
	
	public FinanzCall() {
		super(FinanzFac.class) ;
	}
	
	public List<BankverbindungDto> bankverbindungFindByMandantCNrOhneExc() {
		return bankverbindungFindByMandantCNrOhneExc(globalInfo.getMandant());
	}

	public List<BankverbindungDto> bankverbindungFindByMandantCNrOhneExc(String mandantCnr) {
		return getFac().bankverbindungFindByMandantCNrOhneExc(mandantCnr) ;
	}
	
	public KontoDto kontoFindByCnrKontotypMandantOhneExc(
			String kontoCnr, String kontotypCnr) throws RemoteException {
		return kontoFindByCnrKontotypMandantOhneExc(kontoCnr, kontotypCnr, globalInfo.getMandant());
	}
	
	public KontoDto kontoFindByCnrKontotypMandantOhneExc(
			String kontoCnr, String kontotypCnr, String mandantCnr) throws RemoteException {
		return getFac().kontoFindByCnrKontotypMandantOhneExc(kontoCnr,
				kontotypCnr, mandantCnr, globalInfo.getTheClientDto());
	}
}
