package com.heliumv.factory.query;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.api.machine.MachineEntry;
import com.heliumv.api.machine.MachineEntryTransformer;
import com.heliumv.factory.filter.MandantFilterFactory;
import com.heliumv.tools.FilterHelper;
import com.lp.server.personal.service.MaschineQueryResult;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.util.fastlanereader.service.query.FilterDslBuilder;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.fastlanereader.service.query.QueryResult;

public class MachineQuery extends BaseQuery<MachineEntry> {
//	@Autowired
//	private MandantFilterFactory mandantFilter ;

	public MachineQuery() {
		super(QueryParameters.UC_ID_MASCHINE) ;
	}

	@Override
	protected List<FilterKriterium> getRequiredFilters()
			throws NamingException, RemoteException {
		List<FilterKriterium> filters = new ArrayList<FilterKriterium>() ;
		filters.add(filterMandant()) ;
		return filters ;
	}
	
	@Override
	protected List<MachineEntry> transform(QueryResult result) {
		if(result.hasFlrData()) {
			prepareTransformer((MaschineQueryResult)result) ;			
		}

		return super.transform(result);
	}

	private void prepareTransformer(MaschineQueryResult result) {
		MachineEntryTransformer transformer = (MachineEntryTransformer) getTransformer() ;
		transformer.setFlrData(result.getFlrData());
	}

	public FilterKriterium getFilterWithHidden(Boolean withHidden) {
		return FilterHelper.createWithHidden(withHidden, ZeiterfassungFac.FLR_MASCHINE_B_VERSTECKT) ;
	}
	
	public FilterKriterium getFilterPlanningView(Boolean planningView) {
		if(Boolean.TRUE.equals(planningView)) {
			return FilterDslBuilder.create("b_auslastungsanzeige").build();
		}
		return null;
	}
	
	public FilterKriterium getFilterProductionGroup(Integer productionGroupId) {
		if(productionGroupId != null) {
			return FilterDslBuilder
					.create("fertigungsgruppe_i_id")
					.equal(productionGroupId).build();
		}
		return null;
	}

	public FilterKriterium getFilterStaff(Integer staffId) {
		if (staffId != null) {
			return FilterDslBuilder
					.create("personal_i_id_gestartet")
					.equal(staffId).build();
		}
		return null;
	}
	
	public FilterKriterium getFilterInMachineIds(List<Integer> machineIds) {
		if (machineIds == null || machineIds.isEmpty()) return null;
		
		return FilterDslBuilder.create("i_id")
				.inInteger(machineIds).build();
	}
}
