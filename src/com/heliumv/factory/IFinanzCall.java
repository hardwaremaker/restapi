package com.heliumv.factory;

import java.rmi.RemoteException;
import java.util.List;

import com.lp.server.finanz.service.BankverbindungDto;
import com.lp.server.finanz.service.KontoDto;

public interface IFinanzCall {
	List<BankverbindungDto> bankverbindungFindByMandantCNrOhneExc() ;
	List<BankverbindungDto> bankverbindungFindByMandantCNrOhneExc(String mandantCnr) ;
	
	KontoDto kontoFindByCnrKontotypMandantOhneExc(
			String kontoCnr, String kontotypCnr) throws RemoteException;
	
	KontoDto kontoFindByCnrKontotypMandantOhneExc(
			String kontoCnr, String kontotypCnr, String mandantCnr) throws RemoteException;

}
