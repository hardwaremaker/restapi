package com.heliumv.factory;

import java.rmi.RemoteException;

import com.lp.server.system.service.MediastandardDto;
import com.lp.server.util.HvOptional;

public interface IMediaCall {

	HvOptional<MediastandardDto> mediaFindByPrimaryKey(Integer mediaStandardId) throws RemoteException;

	HvOptional<MediastandardDto> mediaFindByCnr(String mediaCnr, HvOptional<String> locale);

}
