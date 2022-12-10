package com.heliumv.api.finance;

import java.rmi.RemoteException;

import javax.naming.NamingException;

public interface IFinanceApi {
	CurrencyEntryList getCurrencies(String userId, Integer limit, String startKey)
			throws RemoteException, NamingException;
}
