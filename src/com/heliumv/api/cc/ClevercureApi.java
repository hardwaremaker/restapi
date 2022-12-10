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
package com.heliumv.api.cc;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.heliumv.api.BaseApi;
import com.heliumv.factory.IAuftragCall;
import com.heliumv.factory.IAuftragRestCall;
import com.heliumv.factory.IGlobalInfo;
import com.heliumv.factory.ILieferscheinCall;
import com.heliumv.tools.StringHelper;
import com.lp.server.artikel.service.BaseRequestResult;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.CreateOrderResult;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.system.service.WebshopAuthHeader;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Service("hvClevercure")
@Path("/api/beta/cc/")
public class ClevercureApi extends BaseApi implements IClevercureApi {
	private static Logger log = LoggerFactory.getLogger(ClevercureApi.class) ;

	@Autowired
	private IGlobalInfo globalInfo ;
	
	@Autowired
	private ILieferscheinCall lieferscheinCall ;

	@Autowired
	private IAuftragRestCall auftragRestCall ;

	@Autowired
	private IAuftragCall auftragCall ;
	
	public static class Param {
		public final static String TOKEN = "token" ;
		public final static String COMPANYCODE = "companycode" ;
		public final static String DATATYPE = "datatype" ;
	}
	
	enum CCDataType {
		EMPTY,
		UNKNOWN,
		OSD,
		DFD
	}
	
	@Override
	@POST
	@Consumes("text/xml")
	public void receiveAnyCCData(
			@QueryParam(Param.COMPANYCODE ) String companyCode,
			@QueryParam(Param.TOKEN) String token,
//			@QueryParam("user") String user,
//			@QueryParam("password") String password, 
//			@QueryParam(Param.DATATYPE) @DefaultValue("osd") String datatype, 
			@QueryParam(Param.DATATYPE) String datatype, 
			String ccdata) {
		if(StringHelper.isEmpty(Param.COMPANYCODE)) {
			respondBadRequestValueMissing(Param.COMPANYCODE);
			return;
		}

		CCDataType detectedDatatype = detectDatatype(ccdata);
		try {
			if ("osd".equals(datatype) || detectedDatatype == CCDataType.OSD) {
//				try {
//					String oldString = "\u00DF";
//					String newString = new String(oldString.getBytes("UTF-8"), "UTF-8");
//					System.out.println(newString.equals(oldString));
//				} catch(UnsupportedEncodingException e) {
//					System.out.println("use " +  e.getMessage()) ;
//				}
//
				receiveCCDataOsd(companyCode, token, encodeUtf8(ccdata));
				return;
			}
			
			if("dfd".equals(datatype) || detectedDatatype == CCDataType.DFD) {
				receiveCCDataDfd(companyCode, token, encodeUtf8(ccdata));
				return;
			}
			
			respondNotFound();
		} catch (RemoteException e) {
			log.error("RemoteException", e);
			respondUnavailable(e);
		} catch (NamingException e) {
			log.error("NamingException", e);
			respondUnavailable(e);
		}
	}
	

	private String encodeUtf8(String ccdata) {
		String encoded = null ;
		try {
			encoded = new String(ccdata.getBytes("UTF-8"), "UTF-8") ;
		} catch(UnsupportedEncodingException e) {
			log.error("Couldn't decode!", e);
		}

		return encoded;		
	}
	
	private CCDataType detectDatatype(String ccdata) {
		if(ccdata == null || ccdata.length() == 0) return CCDataType.EMPTY;
		
		if(isOsd(ccdata)) return CCDataType.OSD;
		if(isDfd(ccdata)) return CCDataType.DFD;
	
		return CCDataType.UNKNOWN;
	}
	
	private boolean isOsd(String ccdata) {
		int idx = ccdata.indexOf("<ORDERRESPONSE ");
		if(idx == -1) return false;
		return ccdata.indexOf("<ORDERRESPONSE_HEADER>", idx) > idx;
	}
	
	private boolean isDfd(String ccdata) {
		return ccdata.indexOf("<del-for xmlns:") >= 0;
	}
	
	@POST
	@Path("/anydata")
	public void receiveAnyCCData(
			@QueryParam(Param.COMPANYCODE) String companyCode,
			@QueryParam(Param.TOKEN) String token,
//			@QueryParam("user") String user,
//			@QueryParam("password") String password,
			@QueryParam(Param.DATATYPE) @DefaultValue("osd") String datatype) {				
//		if(StringHelper.isEmpty(ccdata)) {
//			respondBadRequestValueMissing("file");
//		}

		String ccdata = "<xml></xml>";
		try {
			if ("osd".equals(datatype)) {
				receiveCCDataOsd(companyCode, token, ccdata);
				return;
			}
			
			respondNotFound();
		} catch (RemoteException e) {
			respondUnavailable(e);
		} catch (NamingException e) {
			respondUnavailable(e);
		}
	}

	@Override
	@POST
	@Path("/multipart")
	@Consumes("multipart/mixed;type=text/xml")
	public void receiveAnyCCDataMultipart(
			@QueryParam(Param.COMPANYCODE) String companyCode,
			@QueryParam(Param.TOKEN) String token,
//			@QueryParam("user") String user,
//			@QueryParam("password") String password,
			@QueryParam(Param.DATATYPE) @DefaultValue("osd") String datatype,
			@Multipart(value="file", type="text/xml") String ccdata) {				
		if(StringHelper.isEmpty(ccdata)) {
			respondBadRequestValueMissing("file");
		}
		
		try {
			if ("osd".equals(datatype)) {
				receiveCCDataOsd(companyCode, token, ccdata);
				return;
			}
			
			respondNotFound();
		} catch (RemoteException e) {
			respondUnavailable(e);
		} catch (NamingException e) {
			respondUnavailable(e);
		}
	}
	
	private void receiveCCDataDfd(String companyCode, String token, String ccdata) throws NamingException, RemoteException {
		Context env = (Context) new InitialContext().lookup("java:comp/env") ;
		Boolean hvStoreReceivedData = (Boolean) env.lookup("heliumv.cc.data.storebefore") ;
	
		log.info("Receiving dfd data...") ;

		if(hvStoreReceivedData) {
			persistDfdData(ccdata) ;
		}
		respondOkay();
	}
	
	private void receiveCCDataOsd(String companyCode, String token, String ccdata)  throws NamingException, RemoteException  {
		Context env = (Context) new InitialContext().lookup("java:comp/env") ;
		String hvUser = (String) env.lookup("heliumv.credentials.user") ;
		String hvPassword= (String) env.lookup("heliumv.credentials.password") ;
		String hvWebshop = (String) env.lookup("heliumv.credentials.webshop") ;
		Boolean hvStoreReceivedData = (Boolean) env.lookup("heliumv.cc.data.storebefore") ;
	
		log.info("Receiving osd data...") ;

		if(hvStoreReceivedData) {
			persistOsdData(ccdata) ;
		}
		
		WebshopAuthHeader authHeader = new WebshopAuthHeader() ;
		authHeader.setUser(hvUser) ;
		authHeader.setPassword(hvPassword) ;
//		authHeader.setIsoCountry("AT") ;
//		authHeader.setIsoLanguage("de") ;
		authHeader.setShopName(hvWebshop) ;
		authHeader.setToken(token) ;
		
		try {
			CreateOrderResult result = auftragRestCall.createOrder(authHeader, ccdata) ;
			if(Helper.isOneOf(result.getRc(), new int[]{
				CreateOrderResult.ERROR_EMPTY_ORDER, CreateOrderResult.ERROR_JAXB_EXCEPTION,
				CreateOrderResult.ERROR_SAX_EXCEPTION, CreateOrderResult.ERROR_UNMARSHALLING})) {
				persistOsdData(ccdata, "error");
				respondBadRequest(result.getRc());
				return ;
			}
			if(result.getRc() == CreateOrderResult.ERROR_AUTHENTIFICATION) {
				respondForbidden();
				persistOsdData(ccdata, "error_auth_.xml");
				return ;
			}
			if(result.getRc() == CreateOrderResult.ERROR_CUSTOMER_NOT_FOUND) {
				respondNotFound() ;
				persistOsdData(ccdata, "error_customer_.xml");
				return ;
			}

			if(result.getRc() >= CreateOrderResult.ERROR_EJB_EXCEPTION) {
				respondBadRequest(result.getRc()) ;
				persistOsdData(ccdata, "error_ejb_.xml");
				return ;
			}
			
			if(result.getRc() == BaseRequestResult.OKAY){
				respondOkay() ;
				persistOsdData(ccdata, "200_.xml");
			} else {
				respondExpectationFailed(result.getRc()) ;
			}
		} catch(EJBExceptionLP e) {
			respondBadRequest(e) ;

			persistOsdData(ccdata, "error_.xml");
		}
	}

	private void persistDfdData(String ccdata) {
		persistDatatype("Dfd", ccdata, null);
	}
	
	private void persistOsdData(String ccdata, String fileSuffix) {
		persistDatatype("Osd", ccdata, fileSuffix) ;
	}

	private void persistOsaData(String ccdata, String fileSuffix) {
		persistDatatype("Osa", ccdata, fileSuffix) ;
	}

	private void persistDatatype(String datatype, String ccdata, String fileSuffix) {
		try {
			File f = File.createTempFile("CC" + datatype + "_", fileSuffix != null ? ("_" + fileSuffix) : "_.xml") ;
			try(FileWriter fw = new FileWriter(f)) {
				fw.write(ccdata) ;
				log.info("Stored " + datatype + " data to file '" + f.getName() + "'.");				
			}
		} catch(IOException e) {
			log.error("Can't write to file" + e.getMessage()) ;
		}
	}

	private void persistOsdData(String ccdata) {
		persistOsdData(ccdata, null);
	}
	
	private void persistDndData(String ccdata, String fileSuffix) {
		persistDatatype("Dnd", ccdata, fileSuffix) ;
	}	

	@Override
	@POST
	@Path("/aviso")
	@Produces({FORMAT_XML})
	public String createDispatchNotification(
			@QueryParam(BaseApi.Param.USERID) String userId, 
			@QueryParam(BaseApi.Param.DELIVERYID) Integer deliveryId, 
			@QueryParam(BaseApi.Param.DELIVERYCNR) String deliveryCnr,
			@QueryParam("post") @DefaultValue(value="false") Boolean doPost) {
		try {
			if(connectClient(userId) == null) return null ;
			if(deliveryId != null) {
				return createDispatchNotificationId(deliveryId, doPost) ;
			}
			
			if(!StringHelper.isEmpty(deliveryCnr)) {
				return createDispatchNotificationCnr(deliveryCnr, doPost) ;
			}

			respondBadRequestValueMissing(BaseApi.Param.DELIVERYID) ;
		} catch(NamingException e) {
			respondUnavailable(e) ;
		} catch(RemoteException e) {
			respondUnavailable(e);
		} catch(EJBExceptionLP e) {
			respondBadRequest(e) ;
		}

		return null;
	}

	private String createDispatchNotificationId(Integer deliveryId, Boolean doPost) throws NamingException, RemoteException {
		LieferscheinDto lsDto = lieferscheinCall.lieferscheinFindByPrimaryKey(deliveryId) ; 
		if(lsDto == null) {
			respondNotFound(BaseApi.Param.DELIVERYID, deliveryId.toString());
			return null ;				
		}
		
		return createDispatchNotificationImpl(lsDto, doPost) ;
	}

	private String createDispatchNotificationCnr(String deliveryCnr, Boolean doPost) throws NamingException, RemoteException {
		LieferscheinDto lsDto = lieferscheinCall.lieferscheinFindByCNr(deliveryCnr) ;
		if(lsDto == null) {
			respondNotFound(BaseApi.Param.DELIVERYCNR, deliveryCnr);
			return null ;
		}
		
		return createDispatchNotificationImpl(lsDto, doPost) ;
	}

	private String createDispatchNotificationImpl(LieferscheinDto deliveryDto, Boolean doPost) throws NamingException, RemoteException {
		String avisoContent = null ;
		if(!doPost) {
			avisoContent = lieferscheinCall.createLieferscheinAvisoToString(deliveryDto.getIId(), globalInfo.getTheClientDto()) ;
		} else {
			avisoContent = lieferscheinCall.createLieferscheinAvisoPost(deliveryDto.getIId(), globalInfo.getTheClientDto());
			if(avisoContent != null) {
				persistDndData(avisoContent, null) ;
			}
		}
		
		return avisoContent ;
	}
	

//	private StatusLine postToCleverCure(String datatype, String content) throws NamingException, ClientProtocolException, IOException {
//		Context env = (Context) new InitialContext().lookup("java:comp/env") ;
//		String ccEndpoint = (String) env.lookup("clevercure.endpoint") ;
//		String uri = ccEndpoint + "&datatype=" + datatype ;
//		
//		HttpPost post = new HttpPost(uri) ;
//		StringEntity entity = new StringEntity(content, Charsets.UTF_8) ;
//		entity.setContentType("text/xml") ;
//		post.setEntity(entity) ;
//
//		HttpClient client = new DefaultHttpClient() ;
//		HttpResponse response = client.execute(post) ;
//		StatusLine status = response.getStatusLine() ;
////		HttpEntity anEntity = response.getEntity() ;
////		InputStream s = anEntity.getContent() ;
////		BufferedReader br = new BufferedReader(new InputStreamReader(s)) ;
////		String theContent = "" ;
////		String line = ""; 
////		while((line = br.readLine()) != null) {
////			theContent += line + "\n" ;
////		}		
//		return status ;
//	}
	
	
	@Override
	@POST
	@Path("/orderresponse")
	@Produces({FORMAT_XML})
	public String createOrderresponse(
			@QueryParam(BaseApi.Param.USERID) String userId, 
			@QueryParam(BaseApi.Param.ORDERID) Integer orderId, 
			@QueryParam(BaseApi.Param.ORDERCNR) String orderCnr,
			@QueryParam("post") @DefaultValue(value="false") Boolean doPost) {
		try {
			if(connectClient(userId) == null) return null ;
			if(orderId != null) {
				return createOrderResponseId(orderId, doPost) ;
			}
			
			if(!StringHelper.isEmpty(orderCnr)) {
				return createOrderResponseCnr(orderCnr, doPost) ;
			}

			respondBadRequestValueMissing(BaseApi.Param.ORDERID) ;
		} catch(NamingException e) {
			respondUnavailable(e) ;
		} catch(RemoteException e) {
			respondUnavailable(e);
		} catch(EJBExceptionLP e) {
			respondBadRequest(e) ;
		}

		return null;
	}

	private String createOrderResponseId(Integer orderId, Boolean doPost) throws NamingException, RemoteException {
		AuftragDto auftragDto = auftragCall.auftragFindByPrimaryKeyOhneExc(orderId) ; 
		if(auftragDto == null) {
			respondNotFound(BaseApi.Param.ORDERID, orderId.toString());
			return null ;				
		}
		
		return createOrderResponseImpl(auftragDto, doPost) ;
	}

	private String createOrderResponseCnr(String orderCnr, Boolean doPost) throws NamingException, RemoteException {
		AuftragDto auftragDto = auftragCall.auftragFindByCnr(orderCnr) ;
		if(auftragDto == null) {
			respondNotFound(BaseApi.Param.ORDERCNR, orderCnr);
			return null ;
		}
		
		return createOrderResponseImpl(auftragDto, doPost) ;
	}

	private String createOrderResponseImpl(AuftragDto auftragDto, Boolean doPost) throws NamingException, RemoteException {
		String responseContent = null ;
		if(!doPost) {
			responseContent = auftragCall.createOrderResponseToString(auftragDto) ;
		} else {
			responseContent = auftragCall.createOrderResponsePost(auftragDto);
			if(responseContent != null) {
				persistOsaData(responseContent, null) ;
			}
		}
		
		return responseContent ;
	}		
}
