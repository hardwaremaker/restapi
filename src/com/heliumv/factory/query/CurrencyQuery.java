package com.heliumv.factory.query;

import java.util.List;

import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.api.finance.CurrencyEntry;
import com.heliumv.api.finance.CurrencyEntryTransformer;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

public class CurrencyQuery extends BaseQuery<CurrencyEntry> {	
	@Autowired
	private CurrencyEntryTransformer currencyEntryTransformer ;
	
	public CurrencyQuery() {
		super(QueryParameters.UC_ID_WAEHRUNG) ;
		setTransformer(currencyEntryTransformer);
	}
	
	@Override
	protected List<FilterKriterium> getRequiredFilters() throws NamingException {
		return null;
	}
}
