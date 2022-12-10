package com.heliumv.factory.query;import java.util.List;

import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.api.system.TaxDescriptionEntry;
import com.heliumv.api.system.TaxDescriptionEntryTransformer;
import com.heliumv.tools.FilterKriteriumCollector;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

public class TaxDescriptionQuery extends BaseQuery<TaxDescriptionEntry> {
	@Autowired
	private TaxDescriptionEntryTransformer taxDescriptionEntryTransformer;
	
	public TaxDescriptionQuery() {
		super(QueryParameters.UC_ID_MWSTSATZBEZ);
		setTransformer(taxDescriptionEntryTransformer);
	}
	
	
	@Override
	protected List<FilterKriterium> getRequiredFilters() throws NamingException {
		FilterKriteriumCollector collector = new FilterKriteriumCollector();
		collector.add(filterMandant());
		return collector.getFilters() ;
	}
}
