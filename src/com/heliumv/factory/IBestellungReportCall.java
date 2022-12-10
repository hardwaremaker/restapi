package com.heliumv.factory;

import java.rmi.RemoteException;

public interface IBestellungReportCall {
	void printWepEtikett(Integer wareneingangspositionId) throws RemoteException;
	void printWepEtikett(Integer wareneingangspositionId, String chargennummer) throws RemoteException;
}
