package com.heliumv.factory.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.factory.BaseCall;
import com.heliumv.factory.IFertigungServiceCall;
import com.heliumv.factory.IGlobalInfo;
import com.lp.server.fertigung.service.FertigungServiceFac;
import com.lp.server.fertigung.service.LospruefplanDto;
import com.lp.server.fertigung.service.PruefergebnisDto;

public class FertigungServiceCall extends BaseCall<FertigungServiceFac> implements IFertigungServiceCall {

	@Autowired
	private IGlobalInfo globalInfo ;

	public FertigungServiceCall() {
		super(FertigungServiceFac.class);
	}

	@Override
	public void updatePruefergebnisse(List<PruefergebnisDto> dtos, Integer losablieferungIId) {
		getFac().updatePruefergebnisse(new ArrayList<PruefergebnisDto>(dtos), 
				losablieferungIId, globalInfo.getTheClientDto());
	}

	@Override
	public List<LospruefplanDto> lospruefplanFindByLosId(Integer losIId) {
		return getFac().lospruefplanFindyByLosIId(losIId);
	}

	@Override
	public LospruefplanDto lospruefplanFindByPrimaryKey(Integer lospruefplanId) {
		return getFac().lospruefplanFindByPrimaryKey(lospruefplanId);
	}

}
