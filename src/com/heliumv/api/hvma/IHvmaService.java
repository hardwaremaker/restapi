package com.heliumv.api.hvma;

import java.rmi.RemoteException;
import java.util.List;

import com.lp.server.personal.service.HvmaLizenzEnum;
import com.lp.server.personal.service.HvmaRechtEnum;

public interface IHvmaService {

	List<HvmaParamEntry> mobileParameters();
	List<HvmaRechtEnum> mobilePrivileges() throws RemoteException;
	List<HvmaRechtEnum> mobilePrivileges(HvmaLizenzEnum licence) throws RemoteException;
}
