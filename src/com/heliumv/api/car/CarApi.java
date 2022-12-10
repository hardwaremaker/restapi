package com.heliumv.api.car;

import java.rmi.RemoteException;

import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.heliumv.api.BaseApi;
import com.heliumv.api.BaseApi.Filter;
import com.heliumv.factory.query.CarQuery;
import com.heliumv.tools.FilterHelper;
import com.heliumv.tools.FilterKriteriumCollector;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.fastlanereader.service.query.QueryResult;

@Service("hvCar")
@Path("/api/v1/car")
public class CarApi extends BaseApi {
	@Autowired
	private CarQuery carQuery;

	@GET
	@Produces({FORMAT_JSON, FORMAT_XML})
	public CarEntryList getCars(
			@QueryParam(Param.USERID) String userId,
			@QueryParam(Param.LIMIT) Integer limit,
			@QueryParam(Param.STARTINDEX) Integer startIndex,
			@QueryParam(Filter.HIDDEN) Boolean filterWithHidden) throws RemoteException, NamingException {
		if(connectClient(userId) == null) return new CarEntryList();
	
		FilterKriteriumCollector collector = new FilterKriteriumCollector();
		collector.add(buildFilterWithHidden(filterWithHidden)) ;
		QueryParameters params = carQuery.getDefaultQueryParameters(collector);
		params.setLimit(limit);
		params.setKeyOfSelectedRow(startIndex);
 
		QueryResult result = carQuery.setQuery(params);
		return new CarEntryList(carQuery.getResultList(result));
	}
	
	private FilterKriterium buildFilterWithHidden(Boolean withHidden) {
		// TODO PJ21891 (b_versteckt, sic)
		return FilterHelper.createWithHidden(withHidden, "b_versteckt") ;
	}
}
