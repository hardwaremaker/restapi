package com.lp.server.system.ejbfac;

import java.rmi.RemoteException;
import java.text.MessageFormat;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.factory.impl.SystemCall;
import com.lp.server.artikel.service.BaseRequestResult;
import com.lp.server.system.service.WebshopError;
import com.lp.server.system.service.WebshopErrorResult;
import com.lp.server.system.service.WebshopPingResult;

@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@WebService(targetNamespace="http://ejbfac.system.server.lp.com")
// @BindingType(value = "http://schemas.xmlsoap.org/wsdl/soap/http?mtom=true")

public class ShopSystemInfo implements IShopSystemInfo {
	private static Logger log = LoggerFactory.getLogger(ShopSystemInfo.class) ;

	@Autowired
	private SystemCall systemCall;

	@Override
	public WebshopPingResult ping() {
		WebshopPingResult result = new WebshopPingResult() ;

		try {
			result.setTimestamp(systemCall.getServerTimestamp().getTime()) ;
			result.setSystemBuildNumber(systemCall.getServerBuildNumber().toString()) ;
			result.setOkay();
		} catch(NamingException e) {
			return new WebshopPingResult(BaseRequestResult.ERROR_RMI_EXCEPTION, e.getMessage()) ;
		} catch(RemoteException re) {
			return new WebshopPingResult(BaseRequestResult.ERROR_RMI_EXCEPTION, re.getMessage()) ;
		} 		

		return result;
	}

	@Override
	public WebshopErrorResult error(WebshopError anError) {
		log.error(asString(anError)) ;
		WebshopErrorResult result = new WebshopErrorResult() ;
		result.setOkay() ;
		return result ;
	}

	private String asString(WebshopError theError) {
		return MessageFormat.format(
				"External Error: id {0}, level {1}, category {2}, text ''{3}'', dataId {4}, errorData ''{5}''",
				new Object[] {
						theError.getId(), theError.getLevel(), theError.getCategory(),
						theError.getText(), theError.getDataId(), theError.getErrorData()
				}) ;
	}	
}
