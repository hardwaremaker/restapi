package com.heliumv.factory.query;

import java.util.List;

import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.api.traveltime.DailyAllowanceEntry;
import com.heliumv.api.traveltime.DailyAllowanceEntryTransformer;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

public class DailyAllowanceQuery extends BaseQuery<DailyAllowanceEntry> {
	@Autowired
	private DailyAllowanceEntryTransformer dailyAllowanceEntryTransformer ;
	
	public DailyAllowanceQuery() {
		super(QueryParameters.UC_ID_DIAETEN) ;
		setTransformer(dailyAllowanceEntryTransformer);
	}
	
	@Override
	protected List<FilterKriterium> getRequiredFilters() throws NamingException {
		return null;
	}
}
