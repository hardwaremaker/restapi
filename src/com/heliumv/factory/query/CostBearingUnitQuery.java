package com.heliumv.factory.query;

import java.util.List;

import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.api.system.CostBearingUnitEntry;
import com.heliumv.api.system.CostBearingUnitEntryTransformer;
import com.heliumv.tools.FilterKriteriumCollector;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

public class CostBearingUnitQuery extends BaseQuery<CostBearingUnitEntry> {
	@Autowired
	private CostBearingUnitEntryTransformer costBearingUnitEntryTransformer ;

	public CostBearingUnitQuery() {
		super(QueryParameters.UC_ID_KOSTENTRAEGER);
		setTransformer(costBearingUnitEntryTransformer);
	}
	
	@Override
	protected List<FilterKriterium> getRequiredFilters() throws NamingException {
		FilterKriteriumCollector collector = new FilterKriteriumCollector();
		collector.add(filterMandant());
		return collector.getFilters() ;
	}
}
