package com.heliumv.api.finance;

import java.rmi.RemoteException;

import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.heliumv.api.BaseApi;
import com.heliumv.factory.query.CurrencyQuery;
import com.heliumv.tools.FilterKriteriumCollector;
import com.lp.server.util.fastlanereader.service.query.FilterBlock;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.fastlanereader.service.query.QueryResult;

/**
 * Funktionalit&auml;t rund um die Resource <b>Finanzwesen</b></br>
 * 
 * @author Gerold
 */
@Service("hvFinance")
@Path("/api/v1/finance/")
public class FinanceApi extends BaseApi implements IFinanceApi {
	@Autowired
	private CurrencyQuery currencyQuery;
	
	@GET
	@Path("/currency")
	@Produces({FORMAT_JSON, FORMAT_XML, "text/html"})
	@Override
	public CurrencyEntryList getCurrencies(
			@QueryParam(Param.USERID) String userId,
			@QueryParam(Param.LIMIT) Integer limit,
			@QueryParam(Param.STARTINDEX) String startKey) throws RemoteException, NamingException {
		if(connectClient(userId) == null) return new CurrencyEntryList();
		
		FilterKriteriumCollector collector = new FilterKriteriumCollector();		
		QueryParameters params = currencyQuery.getDefaultQueryParameters(collector);
		params.setLimit(limit);
		params.setKeyOfSelectedRow(startKey);
	
		QueryResult result = currencyQuery.setQuery(params);
		return new CurrencyEntryList(currencyQuery.getResultList(result));
	}
}
