package com.heliumv.factory.query;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.api.purchaseorder.PurchaseOrderEntry;
import com.heliumv.api.purchaseorder.PurchaseOrderEntryTransformer;
import com.heliumv.api.purchaseorder.PurchaseOrderStatus;
import com.heliumv.factory.IParameterCall;
import com.heliumv.tools.StringHelper;
import com.lp.server.bestellung.service.BestellungFac;
import com.lp.server.bestellung.service.BestellungQueryResult;
import com.lp.server.partner.service.LieferantFac;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.util.fastlanereader.service.query.FilterDslBuilder;
import com.lp.server.util.fastlanereader.service.query.FilterDslLikeStringValue.Wildcard;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.fastlanereader.service.query.QueryResult;

public class PurchaseOrderQuery extends BaseQuery<PurchaseOrderEntry> {
//	@Autowired
//	private MandantFilterFactory mandantFilter ;
	@Autowired
	private IParameterCall parameterCall ;

	public PurchaseOrderQuery() {
		super(QueryParameters.UC_ID_BESTELLUNG);
	}

	@Override
	protected List<PurchaseOrderEntry> transform(QueryResult result) {
		if (result.hasFlrData()) {
			prepareTransformer((BestellungQueryResult) result);
		}
		return super.transform(result);
	}
	
	private void prepareTransformer(BestellungQueryResult result) {
		PurchaseOrderEntryTransformer transformer = (PurchaseOrderEntryTransformer) getTransformer();
		transformer.setFlrData(result.getFlrData());
	}

	@Override
	protected List<FilterKriterium> getRequiredFilters() throws NamingException, RemoteException {
		List<FilterKriterium> filters = new ArrayList<FilterKriterium>();
		filters.add(filterMandant());
		return filters;
	}

//	private FilterKriterium getFilterMandant() {
//		return mandantFilter.bestellung();
//	}
	
	public FilterKriterium getFilterStatus(List<PurchaseOrderStatus> status) {
		if (status == null || status.size() == 0) return null;
		
		List<String> ejbStatus = new ArrayList<String>();
		for (PurchaseOrderStatus purchaseOrderStatus : status) {
			ejbStatus.add(purchaseOrderStatus.getText());
		}
		
		return FilterDslBuilder
				.create(BestellungFac.FLR_BESTELLUNG_BESTELLUNGSTATUS_C_NR)
				.inString(ejbStatus).build();
	}

	public FilterKriterium getFilterCnr(String cnr) {
		if (StringHelper.isEmpty(cnr)) return null;
		
		return FilterDslBuilder
				.create(BestellungFac.FLR_BESTELLUNG_C_NR)
				.like(cnr)
				.leading().build();
	}
	
	public FilterKriterium getFilterSuppliername(String suppliername) throws RemoteException {
		if (StringHelper.isEmpty(suppliername)) return null;
		
		Wildcard position = parameterCall
				.isPartnerSucheWildcardBeidseitig() ? Wildcard.BOTH : Wildcard.TRAILING;
		return FilterDslBuilder.create(BestellungFac.FLR_BESTELLUNG_FLRLIEFERANT + "." 
					+ LieferantFac.FLR_PARTNER + "." 
					+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1)
				.like(suppliername).ignoreCase()
				.wildcard(position).build();
	}
	
	public FilterKriterium getFilterItemCnr(String itemCnr) throws RemoteException {
		if (StringHelper.isEmpty(itemCnr)) return null;

		return FilterDslBuilder.create( // Spezialbehandlung im BestellungHandler
				BestellungFac.FLR_BESTELLUNG_FLRPOSITONEN_FLRARTIKEL + ".c_nr") 
				.like(itemCnr).ignoreCase().wildcard(Wildcard.BOTH).build();
	}
}
