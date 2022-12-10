package com.heliumv.api.edifact;


import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.heliumv.api.BaseApi;
import com.heliumv.api.HvValidateBadRequest;
import com.heliumv.factory.IEdifactOrdersImportCall;
import com.lp.server.auftrag.service.EdifactOrdersImportResult;
import com.lp.server.system.service.WebshopAuthHeader;

@Service("hvEdifact")
@Path("/api/beta/edifact")
public class EdifactApi extends BaseApi {
	private static Logger log = LoggerFactory.getLogger(EdifactApi.class);

	@Autowired
	IEdifactOrdersImportCall edifactOrdersImportCall;
	
	public static class Param {
		public static final String COMPANYCODE = "companycode";
		public static final String TOKEN = "token";
	}
	
	@POST
	@Path("/orders")
	@Consumes({"text/plain", "text/xml"})
	public void receiveOrders(
			@QueryParam(Param.TOKEN) String token,
			String content) throws NamingException {
		HvValidateBadRequest.notEmpty(token, Param.TOKEN);
		HvValidateBadRequest.notEmpty(content, "content");
		
		receiveEdifactOrders(token, content);
	}
	
	private EdifactOrdersImportResult receiveEdifactOrders(String token, String content) throws NamingException {
		Context env = (Context) new InitialContext().lookup("java:comp/env");
		String hvUser = (String) env.lookup("heliumv.credentials.user");
		String hvPassword= (String) env.lookup("heliumv.credentials.password");
//		String hvWebshop = (String) env.lookup("heliumv.credentials.webshop");
//		Boolean hvStoreReceivedData = (Boolean) env.lookup("heliumv.edifact.data.storebefore");

		WebshopAuthHeader authHeader = new WebshopAuthHeader();
		authHeader.setUser(hvUser);
		authHeader.setPassword(hvPassword);
//		authHeader.setShopName(hvWebshop);
		authHeader.setToken(token);

		log.info("Receiving edifact orders data...");
		EdifactOrdersImportResult rc = edifactOrdersImportCall
				.importWebMessage(content, authHeader);
		return rc;
	}
}
