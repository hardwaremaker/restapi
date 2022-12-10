/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2014 HELIUM V IT-Solutions GmbH
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published 
 * by the Free Software Foundation, either version 3 of theLicense, or 
 * (at your option) any later version.
 * 
 * According to sec. 7 of the GNU Affero General Public License, version 3, 
 * the terms of the AGPL are supplemented with the following terms:
 * 
 * "HELIUM V" and "HELIUM 5" are registered trademarks of 
 * HELIUM V IT-Solutions GmbH. The licensing of the program under the 
 * AGPL does not imply a trademark license. Therefore any rights, title and
 * interest in our trademarks remain entirely with us. If you want to propagate
 * modified versions of the Program under the name "HELIUM V" or "HELIUM 5",
 * you may only do so if you have a written permission by HELIUM V IT-Solutions 
 * GmbH (to acquire a permission please contact HELIUM V IT-Solutions
 * at trademark@heliumv.com).
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact: developers@heliumv.com
 ******************************************************************************/
package com.heliumv.api;

import java.io.IOException;
import java.rmi.RemoteException;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;
import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.factory.IClientCall;
import com.heliumv.factory.IGlobalInfo;
import com.heliumv.tools.HvHttpServletHelper;
import com.heliumv.tools.StringHelper;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.client.pc.LPMessages;
import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;

public class BaseApi implements IBaseApiHeaderConstants {
	private static Logger log = LoggerFactory.getLogger(BaseApi.class) ;

	@Context 
	private HttpServletResponse response ;
	
	@Autowired
	private HvHttpServletHelper servletHelper;
	
	@Autowired
	private IClientCall clientCall ;
	
	@Autowired
	private IGlobalInfo globalInfo ;
	
	
	public static final int  UNPROCESSABLE_ENTITY  = 422 ;  /* RFC 4918 */
	public static final int  LOCKED                = 423 ;
	public static final int  TOO_MANY_REQUESTS     = 429 ;
	
	public static final String FORMAT_JSON = "application/json;charset=UTF-8" ;
	public static final String FORMAT_JSONutf8 = "application/json;charset=utf-8" ;
	public static final String FORMAT_XML = "application/xml;charset=UTF-8" ;
	
	protected static final String[] FORMAT_JSON_XML = {FORMAT_JSON, FORMAT_XML} ;

	public static class HvErrorCode {
		private HvErrorCode() {
		}
		
		public static final Integer REMOTE_EXCEPTION         = 1;
		public static final Integer NAMING_EXCEPTION         = 2;
		public static final Integer UNPROCESSABLE_ENTITY     = 3;
		public static final Integer UNKNOWN_ENTITY           = 4;
		public static final Integer EJB_EXCEPTION            = 5;
		public static final Integer CLIENTPROTOCOL_EXCEPTION = 6;
		public static final Integer IO_EXCEPTION             = 7;
		public static final Integer EXPECTATION_FAILED       = 8;
		public static final Integer VALIDATION_FAILED        = 9;
		public static final Integer THROWABLE_EXCEPTION      = 10;
	}
	
	public static class Param {
		private Param() {
		}
		
		public static final String USERID = "userid" ;
		public static final String LIMIT = "limit" ;
		public static final String STARTINDEX = "startIndex" ;	
		
		public static final String ITEMCNR = "itemCnr" ;
		public static final String ITEMID  = "itemid" ;
		public static final String ITEMGROUPCNR = "itemGroupCnr";
		
		public static final String DELIVERYCNR = "deliveryCnr" ;
		public static final String DELIVERYID  = "deliveryid" ;
	
		public static final String ORDERCNR    = "orderCnr" ;
		public static final String ORDERID     = "orderid" ;
		public static final String ORDERPOSITIONID = "orderpositionid" ;
		
		public static final String CUSTOMERID = "customerid" ;
		public static final String CUSTOMERCNR = "customercnr";
		
		public static final String POSITIONID  = "positionid" ;
		public static final String PARTLISTID  = "partlistid" ;
		public static final String PARTLISTCNR = "partlistCnr";
		
		public static final String MACHINEID   = "machineid" ;
		
		public static final String CATEGORY    = "category" ;
		public static final String CNR         = "cnr" ;
		
		public static final String ORDERBY     = "$orderby" ;
		
		public static final String PRODUCTIONID = "productionid";
		public static final String PRODUCTIONCNR = "productionCnr";
		
		public static final String PROJECTID     = "projectid";
		
		public static final String FILTER_CNR = "filter_cnr";
		
		public static final String LINECALLID = "linecallid";
		public static final String DELIVERYADDRESSID = "deliveryaddressid";
		public static final String PRODUCTIONWORKSTEPID = "productionworkstepid";
		
		public static final String PURCHASEORDERID  = "purchaseOrderid";
		public static final String PURCHASEORDERCNR = "purchaseOrderCnr";
		
		public static final String SUPPLIERCNR = "supplierCnr";
		
		public static final String PURCHASEINVOICEID = "purchaseInvoiceid";
		public static final String ID = "id";
		public static final String RECEIPTID = "receiptid";
		
		public static final String GROUPING = "grouping";
		public static final String SECURITYLEVEL = "securitylevel";
		public static final String TYPE = "type";
		public static final String KEYWORDS = "keywords";
		
		public static final String STOCKID = "stockid";
		public static final String STOCKPLACEID = "stockplaceid";
		public static final String STOCKPLACENAME = "stockplacename";
		public static final String STOCKPLACETYPE = "stockplacetype";
		
		public static final String CHARGENR = "chargenr";
		public static final String AMOUNT = "amount";
		
		public static final String DOCUMENTCNR = "documentcnr";
		public static final String ITEMCOMMENTID = "itemcommentid";
		public static final String PROPERTYID = "propertyid";
		public static final String LAYOUTID = "layoutid";
		public static final String COPIES = "copies";
		
		public static final String LICENCECNR = "licenceCnr";
		
		public static final String PRICELISTCNR = "pricelistCnr";

		public static final String PERSONALID = "personalid";
		public static final String PROPOSALPOSITIONID = "proposalpositionid";
		
		/**
		 * Ist der Parameter gesetzt / hat er einen Wert?
		 * @param param der (null) String
		 * @return true wenn der trimmed String eine Laenge > 0 hat 
		 */
		public static boolean isString(String param) {
			return StringHelper.hasContent(param);
		}
		
		/**
		 * Ist der Parameter gesetzt / hat er einen Wert?
		 * @param param der (null) Integer
		 * @return true wenn param != null
		 */
		public static boolean isInteger(Integer param) {
			return param != null;
		}
	}
	
	public static class ParamInHeader {
		private ParamInHeader() {
		}
		
		public static final String TOKEN = "hvtoken" ;
	}
	
	public static class Filter {
		protected Filter() {
		}

		protected static final String BASE = "filter_" ;
		public static final String CNR    = BASE + "cnr" ;
  		public static final String HIDDEN = BASE + "withHidden" ;
  		public static final String TEXTSEARCH = BASE + "textsearch" ;
  		public static final String STATUS = BASE + "status";
		public static final String SUPPLIER = BASE + "supplier";
		public static final String ORDERCNR = BASE + "ordercnr";
		public static final String CUSTOMER = BASE + "customer";
		public static final String ITEMCNR = BASE + "itemcnr";
		public static final String NOTED = BASE + "noted";
 	}
	
	public static class With {
		protected With() {
		}

		private static final String BASE = "with_" ;
		public static final String DESCRIPTION = BASE + "description" ;
	}

	public static class Add {
		private Add() {
		}
		
		private static final String BASE = "add";
		public static final String COMMENTS = BASE + "Comments";
		public static final String PRODUCERINFOS = BASE + "ProducerInfos";
		public static final String STOCKAMOUNTINFOS = BASE + "StockAmountInfos";
		public static final String STOCKPLACEINFOS = BASE + "StockPlaceInfos";
		public static final String SUPPLIERDETAIL = BASE + "SupplierDetail";
		public static final String CUSTOMERDETAIL = BASE + "CustomerDetail";
		public static final String COMMENTSMEDIA = BASE + "CommentsMedia";
		public static final String DOCUMENTS = BASE + "Documents";
		public static final String CONTACTS = BASE + "Contacts";
		public static final String TESTPLAN = BASE + "TestPlan";
		public static final String PURCHASEORDERINFOS = BASE + "PurchaseOrderInfos";
	}
	
	public static class Return {
		private Return() {
		}
		
		private static final String BASE = "return";
		public static final String ITEMINFO = BASE + "ItemInfo";
		public static final String ALLSTOCKS = BASE + "AllStocks";
	}
	
	private ResponseBuilder getResponseBuilder() {
		return new ResponseBuilderImpl() ;
	}
		
	
	public TheClientDto connectClient(String headerUserId, String paramUserId) {
		if(!StringHelper.isEmpty(headerUserId)) return connectClient(headerUserId) ;
		return connectClient(paramUserId) ;
	}
	
	public TheClientDto connectClient(String userId) {
		return connectClient(userId, 10000) ;
	}

	public TheClientDto connectClient(String userId, int maxContentLength) {
		globalInfo.setTheClientDto(null) ;
//		Globals.setTheClientDto(null) ;
		
		if(!verifyConnectParams(userId, maxContentLength)) {
			return null ;			
		}
		
		TheClientDto theClientDto = connectClientWithServer(userId) ;
		if(theClientDto == null) {
			log.error("no clientDto!");
		}
		return theClientDto ;		
	}
	
	protected boolean verifyConnectParams(String userId) {
		if(StringHelper.isEmpty(userId)) {
			respondBadRequestValueMissing("userid") ;
			return false ;
		}
		
		return true ;
	}

	protected boolean verifyConnectParams(String userId, int maxContentLength) {
		if(!verifyConnectParams(userId)) {
			return false ;
		}
		
		if(getServletRequest() != null && getServletRequest().getContentLength() > maxContentLength) {
			respondBadRequestValueMissing("userid") ;
			return false ;
		}
		
		return true ;			
	}
	
	protected TheClientDto connectClientWithServer(String userId) {
		TheClientDto theClientDto = clientCall.theClientFindByUserLoggedIn(userId) ;
		if (null == theClientDto || null == theClientDto.getIDPersonal()) {
			respondUnauthorized() ; 
		} else {
//			Globals.setTheClientDto(theClientDto) ;			
			globalInfo.setTheClientDto(theClientDto);
			log.info("GI: " + globalInfo.toString() + " set to [" + theClientDto.toString() + "]");
		}

		return theClientDto ;
	}
	
	/**
	 * Den Servlet Response auf "UNAUTHORIZED" setzen
	 */
	public void respondUnauthorized() {
		getServletResponse().setStatus(Response.Status.UNAUTHORIZED.getStatusCode()) ;		
	}
	
	public void respondTooManyRequests() {
		getServletResponse().setStatus(TOO_MANY_REQUESTS);
	}
	
	public Response getUnauthorized() {
		return getResponseBuilder().status(Response.Status.UNAUTHORIZED).build() ;
	}
	
	public Response getNoContent() {
		return getResponseBuilder().status(Response.Status.NO_CONTENT).build() ;
	}

	public Response getInternalServerError() {
		return getResponseBuilder().status(Response.Status.INTERNAL_SERVER_ERROR)
			.build() ;
	}

	public Response getInternalServerError(String cause) {
		return getResponseBuilder().status(Response.Status.INTERNAL_SERVER_ERROR)
			.header("x-hv-cause", cause).build() ;
	}
	
	public Response getInternalServerError(EJBExceptionLP e) {
		return getResponseBuilder().status(Response.Status.INTERNAL_SERVER_ERROR)
				.header(X_HV_ERROR_CODE, HvErrorCode.EJB_EXCEPTION.toString())
				.header(X_HV_ERROR_CODE_EXTENDED, Integer.toString(e.getCode()))
				.header(X_HV_ERROR_CODE_DESCRIPTION, e.getCause().getMessage()).build() ;		
	}

	public Response getUnavailable(RemoteException e) {
		return getResponseBuilder().status(Response.Status.INTERNAL_SERVER_ERROR)
				.header(X_HV_ERROR_CODE, HvErrorCode.REMOTE_EXCEPTION.toString())
				.header(X_HV_ERROR_CODE_DESCRIPTION, e.getMessage()).build() ;		
	}
	
	public Response getUnavailable(NamingException e) {
		return getResponseBuilder().status(Response.Status.INTERNAL_SERVER_ERROR)
				.header(X_HV_ERROR_CODE, HvErrorCode.NAMING_EXCEPTION.toString())
				.header(X_HV_ERROR_CODE_DESCRIPTION, e.getMessage()).build() ;		
	}


	public void respondForbidden() {
		getServletResponse().setStatus(Response.Status.FORBIDDEN.getStatusCode()) ;
	}

	public void respondExpectationFailed() {
//      Enunciate kennt EXPECTATION_FAILED nicht? Obwohl das im jaxws-api-m10.jar enthalten ist?
//		getServletResponse().setStatus(Response.Status.EXPECTATION_FAILED.getStatusCode()) ;
		getServletResponse().setStatus(417) ;
	}
	
	public void respondExpectationFailed(Integer hvErrorCode) {
		getServletResponse().setStatus(417) ;
		getServletResponse().setHeader(X_HV_ERROR_CODE, HvErrorCode.EXPECTATION_FAILED.toString()) ;
		getServletResponse().setHeader(X_HV_ERROR_CODE_EXTENDED, hvErrorCode.toString()) ;
//		getServletResponse().setStatus(Response.Status.EXPECTATION_FAILED.getStatusCode()) ;
	}

	public void respondExpectationFailed(String key, String value) {
		getServletResponse().setStatus(417) ;		
		getServletResponse().setHeader(X_HV_ERROR_CODE, HvErrorCode.EXPECTATION_FAILED.toString()) ;
		getServletResponse().setHeader(X_HV_ERROR_KEY, key) ;
		getServletResponse().setHeader(X_HV_ERROR_VALUE, value) ;
	}
	
	public void respondExpectationFailedAdditionalHeader(String additionalKey, String additionalValue) {
		getServletResponse().setStatus(417) ;		
		getServletResponse().setHeader(X_HV_ERROR_CODE, HvErrorCode.EXPECTATION_FAILED.toString()) ;
		getServletResponse().setHeader(X_HV_ADDITIONAL_ERROR_KEY, additionalKey) ;
		getServletResponse().setHeader(X_HV_ADDITIONAL_ERROR_VALUE, additionalValue) ;
	}
	
	public void respondUnavailable(NamingException e) {
		log.info("default-log", e);
		getServletResponse().setStatus(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()) ;		
		getServletResponse().setHeader(X_HV_ERROR_CODE, HvErrorCode.NAMING_EXCEPTION.toString()) ;
		getServletResponse().setHeader(X_HV_ERROR_CODE_DESCRIPTION, e.getMessage()) ;		
	}
	
	public void respondUnavailable(RemoteException e) {
		log.info("default-log", e);
		getServletResponse().setStatus(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()) ;		
		getServletResponse().setHeader(X_HV_ERROR_CODE, HvErrorCode.REMOTE_EXCEPTION.toString()) ;
		getServletResponse().setHeader(X_HV_ERROR_CODE_DESCRIPTION, e.getMessage()) ;		
	}

	public void respondUnavailable(ClientProtocolException e) {
		log.info("default-log", e);
// TODO: Enunciate kennt "BAD_GATEWAY" nicht??
//		getServletResponse().setStatus(Response.Status.BAD_GATEWAY.getStatusCode()) ;			
		getServletResponse().setStatus(502) ;
		getServletResponse().setHeader(X_HV_ERROR_CODE, HvErrorCode.CLIENTPROTOCOL_EXCEPTION.toString()) ;
		getServletResponse().setHeader(X_HV_ERROR_CODE_DESCRIPTION, e.getMessage()) ;		
	}

	public void respondUnavailable(IOException e) {
		log.info("default-log", e);
// TODO: Enunciate kennt "BAD_GATEWAY" nicht??
//		getServletResponse().setStatus(Response.Status.BAD_GATEWAY.getStatusCode()) ;			
		getServletResponse().setStatus(502) ;			
		getServletResponse().setHeader(X_HV_ERROR_CODE, HvErrorCode.IO_EXCEPTION.toString()) ;
		getServletResponse().setHeader(X_HV_ERROR_CODE_DESCRIPTION, e.getMessage()) ;		
	}
	
	public void buildResponse(EJBExceptionLP e, String key, String value) {
		Status responseStatus = Response.Status.BAD_REQUEST;
		if (EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE_EXTENDED == e.getCode()) {
			responseStatus = Response.Status.CONFLICT;
		}
		getServletResponse().setStatus(responseStatus.getStatusCode());
		getServletResponse().setHeader(X_HV_ERROR_CODE, HvErrorCode.EJB_EXCEPTION.toString());
		getServletResponse().setHeader(X_HV_ERROR_CODE_EXTENDED, Integer.toString(e.getCode()));
		getServletResponse().setHeader(X_HV_ERROR_CODE_DESCRIPTION, e.getCause().getMessage());
		getServletResponse().setHeader(X_HV_ERROR_KEY, key);
		getServletResponse().setHeader(X_HV_ERROR_VALUE, value);
		setServletResponseCodeTranslated(e);
	}
	
	private void setServletResponseCodeTranslated(EJBExceptionLP e) {
		try {
			ExceptionLP elp = handleThrowable(e);
			LPMain.getInstance().setUISprLocale(globalInfo.getTheClientDto().getLocUi());
			String clientErrorMessage = new LPMessages().getMsg(elp) ;
			if(clientErrorMessage != null) {
				getServletResponse().setHeader(X_HV_ERROR_CODE_TRANSLATED, clientErrorMessage);
			}
		} catch(Throwable t) {
			log.error("Throwable on handleThrowable", t);
		}
	}


	public void respondBadRequest(EJBExceptionLP e) {
		log.info("default-log", e);
		if(e.getCode() == EJBExceptionLP.FEHLER_FALSCHER_MANDANT) {
			respondNotFound() ;
			return ;
		}

		getServletResponse().setStatus(Response.Status.BAD_REQUEST.getStatusCode()) ;		
		getServletResponse().setHeader(X_HV_ERROR_CODE, HvErrorCode.EJB_EXCEPTION.toString()) ;
		getServletResponse().setHeader(X_HV_ERROR_CODE_EXTENDED, Integer.toString(e.getCode())) ;
		getServletResponse().setHeader(X_HV_ERROR_CODE_DESCRIPTION, e.getCause().getMessage()) ;	
		setServletResponseCodeTranslated(e);
	}

	public void respondBadRequest(Integer hvErrorCode) {
		getServletResponse().setStatus(Response.Status.BAD_REQUEST.getStatusCode()) ;				
		getServletResponse().setHeader(X_HV_ERROR_CODE, HvErrorCode.EXPECTATION_FAILED.toString()) ;
		getServletResponse().setHeader(X_HV_ERROR_CODE_EXTENDED, hvErrorCode.toString()) ;
	}
	
	public void appendBadRequestData(String key, String value) {
		getServletResponse().addHeader(X_HV_ADDITIONAL_ERROR_KEY, key) ;				
		getServletResponse().addHeader(X_HV_ADDITIONAL_ERROR_VALUE, value) ;				
	}
	
	public Response getBadRequest(String key, Object value) {
		return getResponseBuilder().status(Response.Status.BAD_REQUEST)
				.header(X_HV_ERROR_CODE, HvErrorCode.VALIDATION_FAILED)
				.header(X_HV_ERROR_KEY, key)
				.header(X_HV_ERROR_VALUE, value == null ? "null" : value).build() ;
	}

	public Response getBadRequestValueMissing(String key) {
		return getResponseBuilder().status(Response.Status.BAD_REQUEST)
				.header(X_HV_ERROR_CODE, HvErrorCode.VALIDATION_FAILED.toString())
				.header(X_HV_ERROR_KEY, key)
				.header(X_HV_ERROR_VALUE, "{empty}").build() ;	
	}

	public Response getBadRequest(EJBExceptionLP e) {
		return getResponseBuilder().status(Response.Status.BAD_REQUEST)
				.header(X_HV_ERROR_CODE, HvErrorCode.EJB_EXCEPTION)
				.header(X_HV_ERROR_CODE_EXTENDED, Integer.toString(e.getCode()))
				.header(X_HV_ERROR_CODE_DESCRIPTION, e.getCause().getMessage()).build() ;
	}
	
	public Response asResponse() {
		return (Response)getServletResponse();
	}
	/**
	 * Den Servlet Response auf "BAD_REQUEST" setzen
	 */
	public void respondBadRequest(String key, String value) {
		getServletResponse().setStatus(Response.Status.BAD_REQUEST.getStatusCode()) ;		
		getServletResponse().setHeader(X_HV_ERROR_CODE, HvErrorCode.VALIDATION_FAILED.toString()) ;
		getServletResponse().setHeader(X_HV_ERROR_KEY, key) ;
		getServletResponse().setHeader(X_HV_ERROR_VALUE, value) ;
	}
	
	public void respondBadRequestValueMissing(String key) {
		getServletResponse().setStatus(Response.Status.BAD_REQUEST.getStatusCode()) ;		
		getServletResponse().setHeader(X_HV_ERROR_CODE, HvErrorCode.VALIDATION_FAILED.toString()) ;
		getServletResponse().setHeader(X_HV_ERROR_KEY, key) ;
		getServletResponse().setHeader(X_HV_ERROR_VALUE, "{empty}") ;
	}
	
	public void respondNotFound(String key, String value) {
		getServletResponse().setStatus(Response.Status.NOT_FOUND.getStatusCode()) ;				
		getServletResponse().setHeader(X_HV_ERROR_CODE, HvErrorCode.UNKNOWN_ENTITY.toString()) ;
		getServletResponse().setHeader(X_HV_ERROR_KEY, key) ;
		getServletResponse().setHeader(X_HV_ERROR_VALUE, value) ;
	}

	public void respondNotFound(String key, Integer value) {
		respondNotFound(key, value.toString());				
	}
	
	public void respondModified() {
		getServletResponse().setStatus(Response.Status.CONFLICT.getStatusCode());
	}

	public void respondGone() {
		getServletResponse().setStatus(Response.Status.GONE.getStatusCode());		
	}
	
	public void respondLocked() {
//		getServletResponse().setHeader(X_HV_ERROR_CODE, HvErrorCode.UNKNOWN_ENTITY.toString()) ;
//		getServletResponse().setHeader(X_HV_ERROR_KEY, key) ;
//		getServletResponse().setHeader(X_HV_ERROR_VALUE, value) ;
		getServletResponse().setStatus(LOCKED) ;				
	}
	
	public void respondNotFound() {
		getServletResponse().setStatus(Response.Status.NOT_FOUND.getStatusCode()) ;		
	}
	
	public void respondOkay() {
		getServletResponse().setStatus(Response.Status.OK.getStatusCode()) ;				
	}
	
	public void respondUnprocessableEntity(String key, String value) throws IOException {
		getServletResponse().setStatus(UNPROCESSABLE_ENTITY) ;
		getServletResponse().setHeader(X_HV_ERROR_CODE, HvErrorCode.UNPROCESSABLE_ENTITY.toString()) ;
		getServletResponse().setHeader(X_HV_ERROR_KEY, key) ;
		getServletResponse().setHeader(X_HV_ERROR_VALUE, value) ;
//		getServletResponse().sendError(UNPROCESSABLE_ENTITY, "");
	}
	
	public void setHttpServletResponse(HttpServletResponse theResponse) {
		response = theResponse ;
	}
	
	public HttpServletResponse getServletResponse() {
		return response;
	}	
	
	protected HttpServletRequest getServletRequest() {
		return servletHelper.getRequest();
	}
	
	/**
	 * Ein bewusstes Duplikat von Delegate.handleThrowable im LPClientPC</br>
	 * <p>Traue mich momentan noch nicht dr&uuml;ber das zu refaktoren</p>
	 * 
	 * @param t
	 * @return
	 */
	protected ExceptionLP handleThrowable(Throwable t) {
		if(t instanceof ExceptionLP) return (ExceptionLP) t ;
		
		if(t instanceof RuntimeException) {
			RuntimeException reI = (RuntimeException) t;
			// Throwable t2 = reI.getCause();
			// if (t2 != null && t2 instanceof ServerException) {
			Throwable t3 = reI.getCause();
			if (t3 instanceof EJBExceptionLP) {
				EJBExceptionLP ejbt4 = (EJBExceptionLP) t3;
				if (ejbt4 instanceof EJBExceptionLP) {
					Throwable ejbt5 = ejbt4.getCause();
					if (ejbt5 instanceof EJBExceptionLP) {
						// wegen zB. unique key knaller
						EJBExceptionLP ejbt6 = (EJBExceptionLP) ejbt5;
						return new ExceptionLP(ejbt6.getCode(),
								ejbt6.getMessage(),
								ejbt6.getAlInfoForTheClient(), ejbt6.getCause());
					} else if (ejbt5 != null) {
						Throwable ejbt7 = ejbt5.getCause();
						if (ejbt7 instanceof EJBExceptionLP) {
							// zB. fuer WARNUNG_KTO_BESETZT
							EJBExceptionLP ejbt8 = (EJBExceptionLP) ejbt7;
							return new ExceptionLP(ejbt8.getCode(),
									ejbt8.getMessage(),
									ejbt8.getAlInfoForTheClient(),
									ejbt8.getCause());
						} else {
							return new ExceptionLP(ejbt4.getCode(),
									ejbt4.getMessage(),
									ejbt4.getAlInfoForTheClient(),
									ejbt4.getCause());
						}
					} else {
						return new ExceptionLP(ejbt4.getCode(),
								ejbt4.getMessage(),
								ejbt4.getAlInfoForTheClient(), ejbt4.getCause());
					}
				}
			} else if (reI instanceof EJBExceptionLP) {
				EJBExceptionLP exc = (EJBExceptionLP) reI;
				return new ExceptionLP(exc.getCode(), exc.getMessage(),
						exc.getAlInfoForTheClient(), exc.getCause());
			} else if (t3 instanceof EJBExceptionLP) {
				// MB 13. 03. 06 wird ausgeloest, wenn belegnummern ausserhalb
				// des gueltigen bereichs generiert werden
				// (liegt vermutlich am localen interface des BN-Generators)
				EJBExceptionLP ejbt6 = (EJBExceptionLP) t3;
				return new ExceptionLP(ejbt6.getCode(), ejbt6.getMessage(),
						ejbt6.getAlInfoForTheClient(), ejbt6.getCause());
			} else if (t3 instanceof java.io.InvalidClassException) {
				// zB. unique key knaller.
				java.io.InvalidClassException ejb = (java.io.InvalidClassException) t3;
				return new ExceptionLP(EJBExceptionLP.FEHLER_BUILD_CLIENT,
						ejb.getMessage(), null, ejb.getCause());
			} else if (t3 instanceof java.lang.NoClassDefFoundError) {
				// zB. unique key knaller.
				java.lang.NoClassDefFoundError ejb = (java.lang.NoClassDefFoundError) t3;
				return new ExceptionLP(
						EJBExceptionLP.FEHLER_NOCLASSDEFFOUNDERROR,
						ejb.getMessage(), null, ejb.getCause());
			}
		}
		
		if (t instanceof java.lang.IllegalStateException) {
			return new ExceptionLP(EJBExceptionLP.FEHLER_TRANSACTION_TIMEOUT, t);
		}
		
		if (t != null && t.getCause() != null) {
			return new ExceptionLP(EJBExceptionLP.FEHLER, t.getMessage(), null,
					t.getCause());
		}
		
		if(t != null) {
			return new ExceptionLP(
					EJBExceptionLP.FEHLER, t.getMessage(), null, t);
		} else {
			return new ExceptionLP(EJBExceptionLP.FEHLER, "null exception", new Exception()) ;
		}
	}
	
}
