package com.heliumv.factory.query;

import java.util.List;

import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.api.system.CountryEntry;
import com.heliumv.api.system.CountryEntryTransformer;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

public class CountryQuery extends BaseQuery<CountryEntry> {
	@Autowired
	private CountryEntryTransformer countryEntryTransformer ;
	
	public CountryQuery() {
		super(QueryParameters.UC_ID_LAND) ;
		setTransformer(countryEntryTransformer);
	}
	
	@Override
	protected List<FilterKriterium> getRequiredFilters() throws NamingException {
		return null;
	}
}
