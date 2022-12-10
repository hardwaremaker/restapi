package com.heliumv.factory.query;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import com.heliumv.api.machine.MachineGroupEntry;
import com.lp.server.util.fastlanereader.service.query.FilterDslBuilder;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

public class MachineGroupQuery extends BaseQuery<MachineGroupEntry> {
//	@Autowired
//	private MandantFilterFactory mandantFilter ;

	public MachineGroupQuery() {
		super(QueryParameters.UC_ID_MASCHINENGRUPPE) ;
	}
	
	@Override
	protected List<FilterKriterium> getRequiredFilters()
			throws NamingException, RemoteException {
		List<FilterKriterium> filters = new ArrayList<FilterKriterium>() ;
		filters.add(filterMandant()) ;
		return filters ;
	}
	
	public FilterKriterium getFilterPlanningView(Boolean planningView) {
		if(Boolean.TRUE.equals(planningView)) {
			return FilterDslBuilder
					.create("b_auslastungsanzeige").build();
		}
		return null;
	}	

	public FilterKriterium getFilterProductiongroupId(Integer productiongroupId) {
		if(productiongroupId != null) {
			return FilterDslBuilder
					.create("fertigungsgruppe_i_id")
					.equal(productiongroupId).build();
		}
		return null;
	}
	
//	private FilterKriterium getFilterMandant() {
//		return mandantFilter.maschinengruppe() ;
//	}		
}
