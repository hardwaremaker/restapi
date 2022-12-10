package com.heliumv.factory.impl;

import java.rmi.RemoteException;

import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.factory.BaseCall;
import com.heliumv.factory.IGlobalInfo;
import com.heliumv.factory.IPartnerCall;
import com.heliumv.factory.IPersonalCall;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.personal.service.PersonalDto;

public class PartnerCall extends BaseCall<PartnerFac> implements IPartnerCall {
	@Autowired
	private IGlobalInfo globalInfo ;
	@Autowired
	private IPersonalCall personalCall ;
	
	public PartnerCall() {
		super(PartnerFac.class);
	}

	public PartnerDto partnerFindByPrimaryKey(Integer partnerId) throws NamingException, RemoteException {
		return getFac().partnerFindByPrimaryKeyOhneExc(partnerId, globalInfo.getTheClientDto()) ;
	}
	
	@Override
	public PartnerDto partnerFindByAnsprechpartnerId(Integer ansprechpartnerId) throws NamingException, RemoteException{
		return getFac().partnerFindByAnsprechpartnerId(ansprechpartnerId, globalInfo.getTheClientDto()) ;
	}

	@Override
	public Integer partnerIdFindByAnsprechpartnerId(Integer ansprechpartnerId)
			throws NamingException, RemoteException {
		return getFac().partnerIdFindByAnsprechpartnerId(ansprechpartnerId);
	}

	@Override
	public PartnerDto partnerFindByPersonalId() throws NamingException, RemoteException {
		return partnerFindByPersonalId(globalInfo.getTheClientDto().getIDPersonal()) ;
	}
	
	@Override
	public PartnerDto partnerFindByPersonalId(Integer personalId) throws NamingException, RemoteException {
		PersonalDto personalDto = personalCall.byPrimaryKeySmall(personalId);
		return partnerFindByPrimaryKey(personalDto.getPartnerIId()) ;
	}
	
	@Override
	public String getPartnerTelefonnummerMitDurchwahl(Integer partnerId, String durchwahl) {
		return getFac().enrichNumber(partnerId, PartnerFac.KOMMUNIKATIONSART_TELEFON, globalInfo.getTheClientDto(), durchwahl, false);
	}
}
