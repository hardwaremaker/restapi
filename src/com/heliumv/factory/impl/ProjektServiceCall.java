package com.heliumv.factory.impl;

import com.heliumv.factory.BaseCall;
import com.heliumv.factory.IProjektServiceCall;
import com.lp.server.projekt.service.BereichDto;
import com.lp.server.projekt.service.ProjektServiceFac;

public class ProjektServiceCall extends BaseCall<ProjektServiceFac> implements IProjektServiceCall {

	public ProjektServiceCall() {
		super(ProjektServiceFac.class);
	}
	
	@Override
	public BereichDto bereichFindByPrimaryKey(Integer id) {
		return getFac().bereichFindByPrimaryKey(id);
	}
}
