package com.heliumv.factory.query;

import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.api.system.ItemUnitEntry;
import com.heliumv.api.system.ItemUnitEntryTransformer;
import com.heliumv.tools.StringHelper;
import com.lp.server.util.fastlanereader.service.query.FilterDslBuilder;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

public class ItemUnitQuery extends BaseQuery<ItemUnitEntry> {
	@Autowired
	private ItemUnitEntryTransformer itemUnitEntryTransformer;
	
	public ItemUnitQuery() {
		super(QueryParameters.UC_ID_EINHEIT);
		setTransformer(itemUnitEntryTransformer);
	}
	
	@Override
	protected List<FilterKriterium> getRequiredFilters() throws NamingException {
		return new ArrayList<FilterKriterium>();
	}
	
	public FilterKriterium getCnrFilter(String cnr) {
		if(StringHelper.isEmpty(cnr)) return null ;
		
		return FilterDslBuilder
				.create("c_nr")
				.equal(StringHelper.removeSqlDelimiters(cnr))
				.build();
	}
}
