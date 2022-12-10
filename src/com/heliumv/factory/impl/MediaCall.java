package com.heliumv.factory.impl;

import java.rmi.RemoteException;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.factory.BaseCall;
import com.heliumv.factory.IGlobalInfo;
import com.heliumv.factory.IMediaCall;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.MediastandardDto;
import com.lp.server.util.HvOptional;

public class MediaCall extends BaseCall<MediaFac> implements IMediaCall {
	@Autowired
	private IGlobalInfo globalInfo;
	
	protected MediaCall() {
		super(MediaFac.class);
	}

	@Override
	public HvOptional<MediastandardDto> mediaFindByPrimaryKey(
			Integer mediaStandardId) throws RemoteException {
		return getFac().mediastandardOptFindByPrimaryKey(mediaStandardId);
	}
	
	@Override
	public HvOptional<MediastandardDto> mediaFindByCnr(
			String mediaCnr, HvOptional<String> locale) {
		return getFac().mediastandardOptFindByCnrMandantCnr(
				mediaCnr, globalInfo.getMandant(), locale, globalInfo.getTheClientDto());	
	}
}
