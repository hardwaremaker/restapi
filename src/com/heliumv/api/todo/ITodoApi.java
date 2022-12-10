package com.heliumv.api.todo;

import java.rmi.RemoteException;

import javax.naming.NamingException;

public interface ITodoApi {

	TodoEntryList getTodos(String userId, Integer limit, Integer startIndex,
			String filterPartnerName) throws NamingException, RemoteException;
}
