package com.heliumv.factory.query;

import java.util.List;

import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.api.system.TextblockEntry;
import com.heliumv.api.system.TextblockEntryTransformer;
import com.heliumv.tools.FilterHelper;
import com.heliumv.tools.FilterKriteriumCollector;
import com.lp.server.system.service.MediaFac;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

public class TextblockQuery extends BaseQuery<TextblockEntry> {
	@Autowired
	private TextblockEntryTransformer textblockEntryTransformer ;

	public TextblockQuery() {
		super(QueryParameters.UC_ID_MEDIASTANDARD);
		setTransformer(textblockEntryTransformer);
	}
	
	@Override
	protected List<FilterKriterium> getRequiredFilters() throws NamingException {
		FilterKriteriumCollector collector = new FilterKriteriumCollector();
		collector.add(filterMandant());
		return collector.getFilters() ;
	}
	
	public FilterKriterium buildFilterWithHidden(Boolean withHidden) {
		return FilterHelper.createWithHidden(withHidden,
				MediaFac.FLR_MEDIASTANDARD_B_VERSTECKT) ;
	}
}
