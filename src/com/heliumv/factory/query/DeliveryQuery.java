package com.heliumv.factory.query;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.api.delivery.DeliveryDocumentStatus;
import com.heliumv.api.delivery.DeliveryEntry;
import com.heliumv.api.delivery.DeliveryEntryTransformer;
import com.heliumv.factory.IParameterCall;
import com.heliumv.tools.StringHelper;
import com.lp.server.lieferschein.service.LieferscheinFac;
import com.lp.server.lieferschein.service.LieferscheinQueryResult;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.util.fastlanereader.service.query.FilterDslBuilder;
import com.lp.server.util.fastlanereader.service.query.FilterDslLikeStringValue.Wildcard;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.fastlanereader.service.query.QueryResult;

public class DeliveryQuery extends BaseQuery<DeliveryEntry> {
//	@Autowired
//	private MandantFilterFactory mandantFilter ;
	@Autowired
	private IParameterCall parameterCall;

	public DeliveryQuery() {
		super(QueryParameters.UC_ID_LIEFERSCHEIN);
	}

	@Override
	protected List<DeliveryEntry> transform(QueryResult result) {
		if (result.hasFlrData()) {
			prepareTransformer((LieferscheinQueryResult)result);
		}
		return super.transform(result);
	}
	
	private void prepareTransformer(LieferscheinQueryResult result) {
		DeliveryEntryTransformer transformer = (DeliveryEntryTransformer) getTransformer();
		transformer.setFlrData(result.getFlrData());
	}
	
	@Override
	protected List<FilterKriterium> getRequiredFilters() throws NamingException, RemoteException {
		List<FilterKriterium> filters = new ArrayList<FilterKriterium>();
		filters.add(filterMandant());
		return filters;
	}
	
	
	public FilterKriterium getFilterCnr(String cnr) {
		if (StringHelper.isEmpty(cnr)) return null;
		
		return FilterDslBuilder
				.create(LieferscheinFac.FLR_LIEFERSCHEIN_C_NR)
				.like(cnr)
				.leading().build();
	}

	public FilterKriterium getFilterStatus(List<DeliveryDocumentStatus> status) {
		if (status == null || status.size() == 0) return null;
		
		List<String> ejbStatus = new ArrayList<String>();
		for (DeliveryDocumentStatus deliveryStatus : status) {
			ejbStatus.add(deliveryStatus.getText());
		}
		
		return FilterDslBuilder
				.create(LieferscheinFac.FLR_LIEFERSCHEIN_LIEFERSCHEINSTATUS_STATUS_C_NR)
				.inString(ejbStatus).build();
	}
	
	public FilterKriterium getFilterOrder(String orderCnr) {
		if (StringHelper.isEmpty(orderCnr)) return null;
		
		return FilterDslBuilder
				.create("flrauftrag.c_nr")
				.like(orderCnr)
				.both().build();
	}

	public FilterKriterium getFilterCustomername(String customername) throws RemoteException {
		if (StringHelper.isEmpty(customername)) return null;
		
		Wildcard position = parameterCall
				.isPartnerSucheWildcardBeidseitig() ? Wildcard.BOTH : Wildcard.TRAILING;
		return FilterDslBuilder.create(LieferscheinFac.FLR_LIEFERSCHEIN_FLRKUNDE + "." 
					+ KundeFac.FLR_PARTNER + "." 
					+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1)
				.like(customername).ignoreCase()
				.wildcard(position).build();
	}

}
