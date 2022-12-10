package com.heliumv.api;

import java.rmi.RemoteException;

import javax.naming.NamingException;

public class HvLegacyCheckedException extends RuntimeException {
	private static final long serialVersionUID = 3567513778851800086L;

	public HvLegacyCheckedException(RemoteException e) {
		super(e) ;
	}
	
	public HvLegacyCheckedException(NamingException e) {
		super(e) ;
	}
}
