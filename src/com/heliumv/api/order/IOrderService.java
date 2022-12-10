package com.heliumv.api.order;

import java.rmi.RemoteException;
import java.util.List;

import javax.naming.NamingException;

public interface IOrderService {
	List<OrderEntry> getOrders(Integer limit, Integer startIndex,
			String filterCnr, String filterCustomer, String filterProject,
			Boolean filterWithHidden, Boolean filterMyOpen,
			String representativeShortSign)  throws NamingException, RemoteException;
	List<OrderEntry> getOfflineOrders(Integer limit, Integer startIndex, 
			String filterCnr, String filterCustomer, String filterProject,
			Boolean filterWithHidden, Boolean filterMyOpen, 
			String representativeShortSign) throws NamingException, RemoteException;
	List<OrderEntry> getMyOrders(Integer tageZieldatum, 
			List<String> stati) throws NamingException, RemoteException;
}
