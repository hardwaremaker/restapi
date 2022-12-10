package com.heliumv.factory;

import java.rmi.RemoteException;
import java.util.Locale;

public interface ISystemMultilanguageCall {

	String getTextRespectUISpr(String sTokenI) throws RemoteException;
	String getTextRespectUISpr(String sTokenI, String mandant, Locale locUi) throws RemoteException;
}
