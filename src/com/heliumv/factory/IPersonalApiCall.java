package com.heliumv.factory;

import java.math.BigDecimal;
import java.rmi.RemoteException;

import javax.naming.NamingException;

public interface IPersonalApiCall {
	int bucheLosGroessenAenderung(String cSeriennummerLeser,
			String idUser, String station, String losCNr, Integer menge,
			String cAusweis) throws NamingException, RemoteException ;
	int bucheLosAblieferung(String cSeriennummerLeser, String idUser,
			String station, String losCNr, Integer menge, String cAusweis)  throws NamingException, RemoteException;
	
	int bucheLosAblieferungSeriennummer(String idUser,
			String station, String losCNr, String artikelCNr, String cSeriennummer, String cVersion)  throws NamingException, RemoteException;
	
	public int bucheLosAblieferungChargennummer(String idUser,
			String station, String losCNr, String artikelCNr, String cChargennummer, BigDecimal menge) throws NamingException, RemoteException ;
	
}
