package com.heliumv.factory;

import java.rmi.RemoteException;
import java.util.Collection;

import com.lp.server.partner.service.LieferantDto;

public interface ILieferantCall {
	LieferantDto lieferantFindByPrimaryKeyOhneExc(Integer lieferantId) throws RemoteException;
	LieferantDto[] lieferantFindByKontoId(Integer kreditorenKontoId) throws RemoteException;
	Collection<LieferantDto> lieferantFindByPartnerIdMandant(Integer partnerId) throws RemoteException;
}
