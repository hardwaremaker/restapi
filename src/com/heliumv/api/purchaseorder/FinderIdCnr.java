package com.heliumv.api.purchaseorder;

import java.rmi.RemoteException;

import com.heliumv.api.BaseApi;
import com.heliumv.tools.StringHelper;

public abstract class FinderIdCnr<T> {
	private BaseApi api;
	
	public FinderIdCnr(BaseApi api) { 
		this.api = api;
	}

	public T find(Integer id, String requestKey) throws RemoteException {
		return find(id, null, requestKey);
	}
	
	public T find(String cnr, String requestKey) throws RemoteException {
		return find(null, cnr, requestKey);
	}
	
	public T find(Integer id, String cnr, String requestKey) throws RemoteException {
		if(id == null && StringHelper.isEmpty(cnr)) {
			api.respondBadRequestValueMissing(requestKey);
			return null;
		}
		
		T entity = null;
		if(id != null) {
			entity = findById(id);
		}
		if(entity == null && !StringHelper.isEmpty(cnr)) {
			entity = findByCnr(cnr);
		}
		if(entity == null || !accept(entity)) {
			api.respondNotFound();
		}
		return entity;
	}
	
	protected abstract T findById(Integer id) throws RemoteException;
	protected abstract T findByCnr(String cnr) throws RemoteException;
	protected abstract boolean accept(T entity) throws RemoteException;
}
