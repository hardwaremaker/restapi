package com.heliumv.api.order;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.factory.impl.JudgeCall;
import com.heliumv.factory.query.AuftragQuery;
import com.heliumv.factory.query.AuftragQueryOffline;
import com.heliumv.tools.FilterKriteriumCollector;
import com.heliumv.tools.StringHelper;
import com.lp.server.auftrag.service.AuftragHandlerFeature;
import com.lp.server.auftrag.service.AuftragQueryResult;
import com.lp.server.util.fastlanereader.service.query.QueryParametersFeatures;
import com.lp.server.util.fastlanereader.service.query.QueryResult;
import com.lp.util.Helper;

public class OrderService implements IOrderService {
	@Autowired
	private AuftragQuery orderQuery ;
	@Autowired
	private AuftragQueryOffline offlineOrderQuery ;
	@Autowired 
	private JudgeCall judgeCall;
	
	@Override
	public List<OrderEntry> getOrders(Integer limit, Integer startIndex,
			String filterCnr, String filterCustomer, String filterProject,
			Boolean filterWithHidden, Boolean filterMyOpen,
			String representativeSign) throws NamingException, RemoteException {
		if(!judgeCall.hasAuftragCRUD()) {
			return new ArrayList<OrderEntry>();
		}
	
		FilterKriteriumCollector collector = new FilterKriteriumCollector();
		collector.add(orderQuery.getFilterCnr(StringHelper.removeXssDelimiters(filterCnr)));
		collector.add(orderQuery.getFilterProject(StringHelper.removeXssDelimiters(filterProject)));
		collector.add(orderQuery.getFilterCustomer(StringHelper.removeXssDelimiters(filterCustomer)));
		collector.add(orderQuery.getFilterWithHidden(filterWithHidden));
		collector.addAll(orderQuery.getFilterMyOpen(filterMyOpen));
		collector.add(orderQuery.getFilterRepresentativeShortSign(representativeSign));
//		FilterBlock filterCrits = collector.createFilterBlock();
	
		QueryParametersFeatures params = orderQuery.getFeatureQueryParameters(collector);
		params.setLimit(limit) ;
		params.setKeyOfSelectedRow(startIndex) ;
		params.addFeature(AuftragHandlerFeature.KUNDEN_IIDS);

		QueryResult result = orderQuery.setQuery(params) ;
		List<OrderEntry> orders = orderQuery.getResultList(result) ;
		return orders;
	}
	
	public List<OrderEntry> getOfflineOrders(Integer limit, Integer startIndex, 
			String filterCnr, String filterCustomer, String filterProject,
			Boolean filterWithHidden, Boolean filterMyOpen,  String representativeSign) throws NamingException, RemoteException {		
		if(!judgeCall.hasAuftragCRUD()) {
			return new ArrayList<OrderEntry>();
		}
	
		FilterKriteriumCollector collector = new FilterKriteriumCollector() ;
		collector.add(offlineOrderQuery.getFilterCnr(StringHelper.removeXssDelimiters(filterCnr))) ;
		collector.add(offlineOrderQuery.getFilterProject(StringHelper.removeXssDelimiters(filterProject))) ;
		collector.add(offlineOrderQuery.getFilterCustomer(StringHelper.removeXssDelimiters(filterCustomer))) ;
//		collector.add(offlineOrderQuery.getFilterDeliveryCustomer(StringHelper.removeXssDelimiters(filterDeliveryCustomer))) ;
		collector.add(offlineOrderQuery.getFilterWithHidden(filterWithHidden)) ;
		collector.addAll(offlineOrderQuery.getFilterMyOpen(filterMyOpen));
		collector.add(offlineOrderQuery.getFilterRepresentativeShortSign(representativeSign));
//		FilterBlock filterCrits = collector.createFilterBlock();
		
		QueryParametersFeatures params = offlineOrderQuery.getFeatureQueryParameters(collector) ;
		params.setLimit(limit) ;
		params.setKeyOfSelectedRow(startIndex) ;
		params.addFeature(AuftragHandlerFeature.ADRESSE_KOMPLETT) ;
		params.addFeature(AuftragHandlerFeature.ADRESSE_ANSCHRIFT);
//		params.addFeature(AuftragHandlerFeature.ADRESSE_IST_LIEFERADRESSE);
		AuftragQueryResult result = (AuftragQueryResult) offlineOrderQuery.setQuery(params) ;
		List<OrderEntry> orders = offlineOrderQuery.getResultList(result) ;
		return orders;
	}
	
	@Override
	public List<OrderEntry> getMyOrders(Integer tageZieldatum,
			List<String> stati) throws NamingException, RemoteException {
		if(!judgeCall.hasAuftragCRUD()) {
			return new ArrayList<OrderEntry>();
		}
	
		FilterKriteriumCollector collector = new FilterKriteriumCollector() ;
		if(tageZieldatum != null) {
			Timestamp ts = Helper.cutTimestampAddDays(
					new Timestamp(System.currentTimeMillis()),
					1 + tageZieldatum);
			collector.add(offlineOrderQuery.getFilterLieferterminInTagen(ts));	
		}
		collector.add(offlineOrderQuery.getFilterStatus(stati));
		collector.addAll(offlineOrderQuery.getFilterMyOpen(Boolean.TRUE));
//		FilterBlock filterCrits = collector.createFilterBlock();
		
		QueryParametersFeatures params = offlineOrderQuery.getFeatureQueryParameters(collector);
		params.setLimit(0) ;
		params.setKeyOfSelectedRow(null) ;
		params.addFeature(AuftragHandlerFeature.ADRESSE_KOMPLETT) ;
		params.addFeature(AuftragHandlerFeature.ADRESSE_ANSCHRIFT);
//		params.addFeature(AuftragHandlerFeature.ADRESSE_IST_LIEFERADRESSE);
		AuftragQueryResult result = (AuftragQueryResult) offlineOrderQuery.setQuery(params) ;
		List<OrderEntry> orders = offlineOrderQuery.getResultList(result) ;
		return orders;
	}
}
