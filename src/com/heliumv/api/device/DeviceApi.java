/**
 * 
 */
package com.heliumv.api.device;

import java.rmi.RemoteException;

import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.heliumv.api.BaseApi;
import com.heliumv.api.HvValidateBadRequest;
import com.heliumv.api.HvValidateNotFound;
import com.heliumv.factory.IParameterCall;
import com.lp.server.system.service.ArbeitsplatzDto;
import com.lp.server.system.service.ArbeitsplatzkonfigurationDto;

/**
 * Stellt Funktionen f&uuml;r die ger&auml;tespezifische Konfiguration zur Verf&uuml;gung</br> 
 * 
 * @author gerold
 */
@Service("hvDevice")
@Path("/api/v1/device")
public class DeviceApi extends BaseApi implements IDeviceApi {
	@Autowired
	private IParameterCall parameterCall ;

	@Autowired
	private ArbeitsplatzkonfigurationMapper arbeitsplatzkonfigurationMapper ;
	
	public static class DeviceParam {
		public final static String CNR  = "devicecnr" ;
		public final static String TYPE = "devicetype" ;
		public final static String TAG  = "devicetag" ;		
 	}
	
	@Override
	@GET
	@Path("/config")
	@Produces({FORMAT_JSON, FORMAT_XML})
	public DeviceConfigEntry getConfig(
			@QueryParam(Param.USERID) String userId,
			@QueryParam(DeviceParam.CNR) String deviceCnr,
			@QueryParam(DeviceParam.TYPE) String deviceType,			
			@QueryParam(DeviceParam.TAG) String deviceTag) throws NamingException, RemoteException {
		if(connectClient(userId) == null) return null ;
		HvValidateBadRequest.notEmpty(deviceCnr, DeviceParam.CNR);
		HvValidateBadRequest.notEmpty(deviceType, DeviceParam.TYPE);
		
		ArbeitsplatzDto arbeitsplatzDto = 
				parameterCall.arbeitsplatzFindByCTypCGeraetecode(deviceType, deviceCnr) ;
		HvValidateNotFound.notNull(arbeitsplatzDto, DeviceParam.CNR, deviceCnr) ;
		
		ArbeitsplatzkonfigurationDto konfigurationDto = 
				parameterCall.arbeitsplatzkonfigurationFindByPrimaryKey(arbeitsplatzDto.getIId()) ;
		
		return arbeitsplatzkonfigurationMapper
				.mapArbeitsplatzkonfiguration(arbeitsplatzDto, konfigurationDto) ;
	}
	
	@PUT
	@Path("/config")
	@Consumes({FORMAT_JSON, FORMAT_XML})	
	public void updateConfig(
			@QueryParam(Param.USERID) String userId,
			DeviceConfigEntry entry) throws NamingException, RemoteException {
		if(connectClient(userId) == null) return ;

		HvValidateBadRequest.notNull(entry, "entry") ;
 		HvValidateBadRequest.notEmpty(entry.getCnr(), "cnr");
 
		HvValidateBadRequest.notEmpty(entry.getDeviceType(), "deviceType");
 
		ArbeitsplatzDto arbeitsplatzDto = 
				parameterCall.arbeitsplatzFindByCTypCGeraetecode(entry.getDeviceType(), entry.getCnr()) ;
		HvValidateNotFound.notNull(arbeitsplatzDto, DeviceParam.CNR, entry.getCnr());
 		
		ArbeitsplatzkonfigurationDto konfigurationDto = 
				parameterCall.arbeitsplatzkonfigurationFindByPrimaryKey(arbeitsplatzDto.getIId()) ;
	
		konfigurationDto.setCUser(entry.getUserConfig());
		parameterCall.updateArbeitsplatzkonfiguration(konfigurationDto);
	}
}
