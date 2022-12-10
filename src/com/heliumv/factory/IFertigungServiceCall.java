package com.heliumv.factory;

import java.util.List;

import com.lp.server.fertigung.service.LospruefplanDto;
import com.lp.server.fertigung.service.PruefergebnisDto;

public interface IFertigungServiceCall {

	void updatePruefergebnisse(List<PruefergebnisDto> dtos, Integer losablieferungIId);

	List<LospruefplanDto> lospruefplanFindByLosId(Integer productionId);
	
	LospruefplanDto lospruefplanFindByPrimaryKey(Integer lospruefplanId);
}
