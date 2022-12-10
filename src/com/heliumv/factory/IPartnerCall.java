package com.heliumv.factory;

import java.rmi.RemoteException;

import javax.naming.NamingException;

import com.lp.server.partner.service.PartnerDto;

public interface IPartnerCall {
	PartnerDto partnerFindByPrimaryKey(Integer partnerId) throws NamingException, RemoteException ;
	
	PartnerDto partnerFindByAnsprechpartnerId(Integer ansprechpartnerId) 
			throws NamingException, RemoteException ; 
	Integer partnerIdFindByAnsprechpartnerId(Integer ansprechpartnerId) throws NamingException, RemoteException ;
	
	/**
	 * Ermittelt den zugeh&ouml;rigen Partner zum angemeldeten Benutzer/Personal
	 * @return das zugeh&ouml;rige PartnerDto zum angemeldeten Benutzer (und damit Personal)
	 * @throws NamingException
	 * @throws RemoteException
	 */
	PartnerDto partnerFindByPersonalId() throws NamingException, RemoteException ;
	PartnerDto partnerFindByPersonalId(Integer personalId) throws NamingException, RemoteException;

	String getPartnerTelefonnummerMitDurchwahl(Integer partnerId, String durchwahl);
}
