package com.heliumv.api.order;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.heliumv.api.BaseApi;
import com.heliumv.factory.IClientCall;
import com.heliumv.factory.IParameterCall;
import com.heliumv.factory.impl.AuftragQuery;
import com.heliumv.tools.FilterKriteriumCollector;
import com.heliumv.tools.StringHelper;
import com.lp.server.auftrag.service.AuftragFac;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterBlock;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.fastlanereader.service.query.QueryResult;

@Service("hvOrder")
@Path("/api/v1/order/")
public class OrderApi extends BaseApi implements IOrderApi  {

	@Autowired
	private IClientCall clientCall ;
	@Autowired
	private IParameterCall parameterCall ;
	
	@GET
	@Path("/{userid}")
	@Produces({"application/json", "application/xml"})
	public List<OrderEntry> getOrders(
			@PathParam("userid") String userId,
			@QueryParam("limit") Integer limit,
			@QueryParam("startIndex") Integer startIndex,
			@QueryParam("filter_cnr") String filterCnr,
			@QueryParam("filter_customer") String filterCustomer, 
			@QueryParam("filter_project") String filterProject,
			@QueryParam("filter_withHidden") Boolean filterWithHidden) {
		List<OrderEntry> orders = new ArrayList<OrderEntry>() ;
	
		try {
			if(null == connectClient(userId)) return orders ;
			
			FilterKriteriumCollector collector = new FilterKriteriumCollector() ;
			collector.add(buildFilterCnr(filterCnr)) ;
			collector.add(buildFilterProject(filterProject)) ;
			collector.add(buildFilterCustomer(filterCustomer)) ;
			collector.add(buildFilterWithHidden(filterWithHidden)) ;
			FilterBlock filterCrits = new FilterBlock(collector.asArray(), "AND")  ;
			
			AuftragQuery query = new AuftragQuery(parameterCall) ;
//			AuftragQuery query = new AuftragQuery() ;
			QueryParameters params = query.getDefaultQueryParameters(filterCrits) ;
			params.setLimit(limit) ;
			params.setKeyOfSelectedRow(startIndex) ;

			QueryResult result = query.setQuery(params) ;
			orders = query.getResultList(result) ;
		} catch(NamingException e) {
			e.printStackTrace() ;
		} catch(RemoteException e) {
			e.printStackTrace() ;
		}
		
		return orders ;
	}
	
	private FilterKriterium buildFilterCnr(String cnr) {
		if(cnr == null || cnr.trim().length() == 0) return null ;
		
		FilterKriteriumDirekt fk = new FilterKriteriumDirekt("c_nr", StringHelper.removeSqlDelimiters(cnr), 
				FilterKriterium.OPERATOR_LIKE, "", 
				FilterKriteriumDirekt.PROZENT_LEADING, 
				true, false, Facade.MAX_UNBESCHRAENKT) ;
		fk.wrapWithProzent() ;
		fk.wrapWithSingleQuotes() ;
		return fk ;
	}
	
	private FilterKriterium buildFilterProject(String project) {
		if(null == project || project.trim().length() == 0) return null ;
		
		FilterKriteriumDirekt fk = new FilterKriteriumDirekt("c_bez", StringHelper.removeSqlDelimiters(project), 
				FilterKriterium.OPERATOR_LIKE, "",
				FilterKriteriumDirekt.PROZENT_TRAILING, 
				true, true, Facade.MAX_UNBESCHRAENKT) ;
		fk.wrapWithProzent() ;
		fk.wrapWithSingleQuotes() ;
		return fk ;
	}
	
	private FilterKriterium buildFilterCustomer(String customer) throws NamingException, RemoteException {
		if(null == customer || customer.trim().length() == 0) return null ;

//		int percentType = getServer().getParameterCall()
//				.isPartnerSucheWildcardBeidseitig() ? FilterKriteriumDirekt.PROZENT_BOTH : FilterKriteriumDirekt.PROZENT_TRAILING ;

		int percentType = parameterCall
				.isPartnerSucheWildcardBeidseitig() ? FilterKriteriumDirekt.PROZENT_BOTH : FilterKriteriumDirekt.PROZENT_TRAILING ;

		FilterKriteriumDirekt fk = new FilterKriteriumDirekt(AuftragFac.FLR_AUFTRAG_FLRKUNDE
				+ "." + KundeFac.FLR_PARTNER + "."
				+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1, StringHelper.removeSqlDelimiters(customer),
				FilterKriterium.OPERATOR_LIKE, "",
				percentType, true, true, Facade.MAX_UNBESCHRAENKT); 
		fk.wrapWithProzent() ;
		fk.wrapWithSingleQuotes() ;
		return fk ;
	}
	
	private FilterKriterium buildFilterWithHidden(Boolean withHidden) {
		if(null == withHidden) return null ;
		
		if(!withHidden) {
			FilterKriterium fkVersteckt = new FilterKriterium(
				AuftragFac.FLR_AUFTRAG_B_VERSTECKT, true, "(1)",
				FilterKriterium.OPERATOR_NOT_IN, false);

			return fkVersteckt;
		}
		
		return null ;
	}
}
