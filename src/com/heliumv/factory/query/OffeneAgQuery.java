package com.heliumv.factory.query;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import com.heliumv.api.production.OpenWorkEntry;
import com.heliumv.api.production.OpenWorkEntryTransformer;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.fertigung.service.OffeneAgsQueryResult;
import com.lp.server.util.fastlanereader.service.query.FilterDslBuilder;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.fastlanereader.service.query.QueryResult;
import com.lp.util.Helper;

public class OffeneAgQuery extends BaseQuery<OpenWorkEntry> {
//	@Autowired
//	private IGlobalInfo globalInfo ;
//	@Autowired
//	private MandantFilterFactory mandantFilter ;
	
	public OffeneAgQuery() {
		super(QueryParameters.UC_ID_OFFENE_AGS) ;
	}

	@Override
	protected List<FilterKriterium> getRequiredFilters()
			throws NamingException, RemoteException {
		List<FilterKriterium> filters = new ArrayList<FilterKriterium>() ;
		filters.add(getFilterMandant()) ;
		return filters ;
	}
	
	@Override
	protected List<OpenWorkEntry> transform(QueryResult result) {
		if(result.hasFlrData()) {
			prepareTransformer((OffeneAgsQueryResult)result) ;			
		}

		return super.transform(result);
	}

	private void prepareTransformer(OffeneAgsQueryResult result) {
		OpenWorkEntryTransformer transformer = (OpenWorkEntryTransformer) getTransformer() ;
		transformer.setFlrData(result.getFlrData());
	}
	
	public FilterKriterium getFilterBeginnDatum(Long startDateMs) {
		if (startDateMs == null) return null;
		
		return new FilterKriterium("flroffeneags.t_agbeginn", true,
		"'" + Helper.formatDateWithSlashes(new java.sql.Date(startDateMs)) + "'",
		FilterKriterium.OPERATOR_GTE, false) ;	
	}

	public FilterKriterium getFilterEndeDatum(Long endDateMs) {
		if (endDateMs == null) return null;
		
		return new FilterKriterium("flroffeneags.t_agbeginn", true,
		"'" + Helper.formatDateWithSlashes(new java.sql.Date(endDateMs)) + "'",
		FilterKriterium.OPERATOR_LT, false) ;	
	}

	public FilterKriterium getFilterProductiongroupId(Integer productiongroupId) {
		if(productiongroupId == null) return null;
		
		return FilterDslBuilder
				.create("flrlos.fertigungsgruppe_i_id")
				.equal(productiongroupId).build();
	}
	
	public FilterKriterium getFilterExcludeStopped() {
		return FilterDslBuilder
				.create("flrlos.status_c_nr")
				.not().in(new String[]{FertigungFac.STATUS_GESTOPPT}).build();
	}
	
	public FilterKriterium getFilterExclude100Percent() {
		return FilterDslBuilder
				.create("sollarbeitsplan.f_fortschritt")
//				.isNotNull()
				.ltOrNull(100).build();
	}
	
	public FilterKriterium getFilterExcludeInfoArtikel() {
		return FilterDslBuilder
				.create("taetigkeit.b_nurzurinfo")
				.equal(0).build();
	}
	
	private FilterKriterium getFilterMandant() {
		return filterMandant("flroffeneags.mandant_c_nr");
//		return mandantFilter.offeneAg() ;
	}		
	
	public FilterKriterium getFilterMachinegroup(Integer machinegroupId) {
		if (machinegroupId == null) return null;
		
		return FilterDslBuilder
				.create("flrmaschine.maschinengruppe_i_id")
				.equal(machinegroupId).build();
	}
	
	public FilterKriterium getFilterInProductionOnly() {
		return FilterDslBuilder
				.create("flrlos.status_c_nr")
				.in(new String[]{FertigungFac.STATUS_IN_PRODUKTION, FertigungFac.STATUS_TEILERLEDIGT}).build();
	}
}
