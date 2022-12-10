package com.heliumv.factory.query;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import com.heliumv.api.item.ShopGroupEntry;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

public class ShopGroupQuery extends BaseQuery<ShopGroupEntry> {	
	public ShopGroupQuery() {
		super(QueryParameters.UC_ID_SHOPGRUPPE) ;
	}
	
	@Override
	protected List<FilterKriterium> getRequiredFilters()
			throws NamingException, RemoteException {
		List<FilterKriterium> filters = new ArrayList<FilterKriterium>() ;
		filters.add(getFilterMandant()) ;
		return filters;
	}

	private FilterKriterium getFilterMandant() throws NamingException {
		return filterMandant("shopgruppe.mandant_c_nr");
//		String mandant = globalInfo.getMandant() ;
//		return new FilterKriterium("shopgruppe.mandant_c_nr",
//					true, StringHelper.asSqlString(mandant),
//					FilterKriterium.OPERATOR_EQUAL, false) ;
	}	
}
