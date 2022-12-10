package com.heliumv.api.traveltime;

import java.rmi.RemoteException;

import javax.naming.NamingException;

public interface ITraveltimeApi {

	void bookBatch(String userId, TravelTimeRecordingBatchEntryList batchEntries)
			throws NamingException, RemoteException;

	DailyAllowanceEntryList getDailyAllowances(String userId, Integer limit, String startKey)
			throws RemoteException, NamingException;

}
