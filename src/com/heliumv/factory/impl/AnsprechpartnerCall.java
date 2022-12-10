package com.heliumv.factory.impl;

import java.rmi.RemoteException;

import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.factory.BaseCall;
import com.heliumv.factory.IAnsprechpartnerCall;
import com.heliumv.factory.IGlobalInfo;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.AnsprechpartnerFac;
import com.lp.server.partner.service.AnsprechpartnerfunktionDto;
import com.lp.util.EJBExceptionLP;

public class AnsprechpartnerCall extends BaseCall<AnsprechpartnerFac> implements IAnsprechpartnerCall {
	@Autowired
	private IGlobalInfo globalInfo ;
	
	public AnsprechpartnerCall() {
		super(AnsprechpartnerFac.class) ;
	}
	
	public AnsprechpartnerDto ansprechpartnerFindByPrimaryKey(Integer ansprechpartnerId) throws EJBExceptionLP, NamingException, RemoteException{
		return getFac().ansprechpartnerFindByPrimaryKeyOhneExc(ansprechpartnerId, globalInfo.getTheClientDto()) ;
	}
	
	public void updateAnsprechpartner(AnsprechpartnerDto ansprechpartnerDto) throws EJBExceptionLP, NamingException, RemoteException {
		getFac().updateAnsprechpartner(ansprechpartnerDto, globalInfo.getTheClientDto());
	}
	
	public AnsprechpartnerDto[] ansprechpartnerFindByPartnerIId(Integer partnerId) throws EJBExceptionLP, RemoteException {
		return getFac().ansprechpartnerFindByPartnerIId(partnerId, globalInfo.getTheClientDto());
	}
	
	public AnsprechpartnerfunktionDto ansprechpartnerfunktionFindByPrimaryKey(Integer ansprechpartnerfunktionId) throws EJBExceptionLP, RemoteException {
		return getFac().ansprechpartnerfunktionFindByPrimaryKey(ansprechpartnerfunktionId, globalInfo.getTheClientDto());
	}
}
