package com.heliumv.factory.impl;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.factory.BaseCall;
import com.heliumv.factory.IGlobalInfo;
import com.heliumv.factory.ILieferantCall;
import com.heliumv.tools.CollectionTools;
import com.heliumv.tools.ISelect;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.LieferantFac;

public class LieferantCall extends BaseCall<LieferantFac> implements ILieferantCall {

	@Autowired
	private IGlobalInfo globalInfo ;

	public LieferantCall() { 
		super(LieferantFac.class);
	}

	@Override
	public LieferantDto lieferantFindByPrimaryKeyOhneExc(Integer lieferantId) throws RemoteException {
		return getFac().lieferantFindByPrimaryKeyOhneExc(lieferantId, globalInfo.getTheClientDto());
	}

	public LieferantDto[] lieferantFindByKontoId(Integer kreditorenKontoId) throws RemoteException {
		return getFac().lieferantfindByKontoIIdKreditorenkonto(kreditorenKontoId);
	}
	
	public LieferantDto[] lieferantFindByPartnerId(Integer partnerId) throws RemoteException {
		return getFac().lieferantFindByPartnerIId(partnerId, globalInfo.getTheClientDto());
	}
	
	@Override
	public Collection<LieferantDto> lieferantFindByPartnerIdMandant(Integer partnerId) throws RemoteException {
		LieferantDto[] dtos = lieferantFindByPartnerId(partnerId);
		final String mandantCnr = globalInfo.getMandant();
		return CollectionTools.select(dtos, new ISelect<LieferantDto>() {
			@Override
			public boolean select(LieferantDto element) {
				return mandantCnr.equals(element.getMandantCNr());
			}
		});
	}
}
