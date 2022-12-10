package com.heliumv.factory;

import java.rmi.RemoteException;

import com.lp.server.bestellung.service.BestellpositionDto;

public interface IBestellpositionCall {
	BestellpositionDto[] bestellpositionFindByBestellung(Integer bestellungId) throws RemoteException;
	BestellpositionDto bestellpositionFindByPrimaryKey(Integer positionId) throws RemoteException;
}
