package com.heliumv.api.system;

import java.rmi.RemoteException;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.factory.IParameterCall;
import com.heliumv.tools.StringHelper;

public class CnrFormatter {

	@Autowired
	private IParameterCall parameterCall;
	
	public String trimToCnrLength(String cnr) throws RemoteException {
		if (StringHelper.isEmpty(cnr)) return cnr;
		
		CnrFormat format = parameterCall.getBelegnummernformat();
		return cnr.length() > format.getLength() ? cnr.substring(0, format.getLength()) : cnr;
	}
}
