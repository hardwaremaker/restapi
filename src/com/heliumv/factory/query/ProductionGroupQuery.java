package com.heliumv.factory.query;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import com.heliumv.api.partlist.ProductionGroupEntry;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

public class ProductionGroupQuery extends BaseQuery<ProductionGroupEntry> {
//	@Autowired
//	private MandantFilterFactory mandantFilter ;

	public ProductionGroupQuery() {
		super(QueryParameters.UC_ID_FERTIGUNGSGRUPPE);
	}
	
	@Override
	protected List<FilterKriterium> getRequiredFilters()
			throws NamingException, RemoteException {
		List<FilterKriterium> filters = new ArrayList<FilterKriterium>();
		filters.add(filterMandant());
		return filters;
	}
	
//	private FilterKriterium getFilterMandant() {
//		return mandantFilter.fertigunsgruppe();
//	}	
}
