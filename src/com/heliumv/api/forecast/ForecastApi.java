package com.heliumv.api.forecast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.util.List;

import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.heliumv.api.BaseApi;
import com.heliumv.api.HvValidateBadRequest;
import com.heliumv.factory.IArtikelCall;
import com.heliumv.factory.IFertigungCall;
import com.heliumv.factory.IFertigungReportCall;
import com.heliumv.factory.IForecastCall;
import com.heliumv.factory.IMandantCall;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.forecast.service.FclieferadresseDto;
import com.lp.server.forecast.service.FclieferadresseNoka;
import com.lp.server.forecast.service.FclieferadresseNokaDto;
import com.lp.server.forecast.service.ForecastpositionArtikelbuchungDto;
import com.lp.server.forecast.service.ForecastpositionDto;
import com.lp.server.forecast.service.ForecastpositionProduktionDto;
import com.lp.server.forecast.service.KommdruckerDto;
import com.lp.server.forecast.service.LinienabrufArtikelDto;
import com.lp.server.forecast.service.LinienabrufArtikelbuchungDto;
import com.lp.server.forecast.service.LinienabrufDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;

/**
 * 
 * @author andi
 *
 */
@Service("hvForecast")
@Path("/api/v1/forecast/")
public class ForecastApi extends BaseApi implements IForecastApi {

	public class ApiFilter extends Filter {
		public final static String DELIVERABLE = BASE + "deliverable";
		public final static String PICKINGTYPE = BASE + "pickingtype";
	}
	
	@Autowired
	private IMandantCall mandantCall;
	@Autowired
	private IForecastCall forecastCall;
	@Autowired
	private IArtikelCall artikelCall;
	@Autowired
	private IFertigungCall fertigungCall;
	@Autowired
	private IFertigungReportCall fertigungReportCall;
	
	@Autowired
	private ForecastPositionEntryMapper forecastPositionEntryMapper;
	@Autowired
	private LinecallEntryMapper linecallEntryMapper;
	@Autowired
	private ForecastDeliveryAddressEntryMapper forecastDeliveryAddressEntryMapper;
	@Autowired
	private PickingPrinterEntryMapper pickingPrinterEntryMapper;
	
	@GET
	@Path("/deliveryaddress/list")
	@Produces({FORMAT_JSON, FORMAT_XML})
	public ForecastDeliveryAddressEntryList getDeliveryAddresses(
			@QueryParam(Param.USERID) String userId,
			@QueryParam(ApiFilter.DELIVERABLE) Boolean filterDeliverable) throws RemoteException, EJBExceptionLP {
		ForecastDeliveryAddressEntryList entries = new ForecastDeliveryAddressEntryList();
		if (connectClient(userId) == null) return entries;
		
		if(!mandantCall.hasModulForecast()) {
			respondNotFound();
			return entries;
		}
		
		if (Boolean.TRUE.equals(filterDeliverable)) {
			List<FclieferadresseNokaDto> dtos = forecastCall.getLieferbareFclieferadressenByMandant();
			entries.setEntries(forecastDeliveryAddressEntryMapper.mapEntries(dtos));
			return entries;
		}
		
		// TODO alle Fclieferadressen pro Mandant
		
		return entries;
	}
	
	@GET
	@Path("/deliveryaddress/{" + Param.DELIVERYADDRESSID + "}/position/list")
	@Produces({FORMAT_JSON, FORMAT_XML})
	public ForecastPositionEntryList getPositionsByDeliveryAddress(
			@PathParam(Param.DELIVERYADDRESSID) Integer deliveryAddressId,
			@QueryParam(Param.USERID) String userId,
			@QueryParam(ApiFilter.DELIVERABLE) Boolean filterDeliverable,
			@QueryParam(ApiFilter.PICKINGTYPE) PickingType pickingType ) throws RemoteException, EJBExceptionLP, NamingException {
		ForecastPositionEntryList entries = new ForecastPositionEntryList();
		if (connectClient(userId) == null) return entries;
		
		if(!mandantCall.hasModulForecast()) {
			respondNotFound();
			return entries;
		}

		FclieferadresseDto lieferadresseDto = forecastCall.fclieferadresseFindByPrimaryKeyOhneExc(deliveryAddressId);
		if (lieferadresseDto == null) {
			respondNotFound("deliveryAddressId", deliveryAddressId);
			return entries;
		}
		
		if (Boolean.TRUE.equals(filterDeliverable)) {
			List<ForecastpositionProduktionDto> dtos = forecastCall
					.getLieferbareForecastpositionByFclieferadresseIId(
							deliveryAddressId, map(pickingType));
			entries.setEntries(forecastPositionEntryMapper.mapEntries(dtos));
			return entries;
		}
		
		// TODO alle Fcposition der Lieferadresse pro Mandant
		
		return entries;
	}

	private FclieferadresseNoka map(PickingType pickingType) {
		if(pickingType == null) return FclieferadresseNoka.NICHT_DEFINIERT;
		if(PickingType.CALLOFF.equals(pickingType)) return FclieferadresseNoka.LINIENABRUF;
		if(PickingType.ITEM.equals(pickingType)) return FclieferadresseNoka.ARTIKEL;
		if(PickingType.ADDRESS.equals(pickingType)) return FclieferadresseNoka.ADRESSE;
		return FclieferadresseNoka.NICHT_DEFINIERT;
	}
	
	@GET
	@Path("/position/list")
	@Produces({FORMAT_JSON, FORMAT_XML})
	public ForecastPositionEntryList getPositions(
			@QueryParam(Param.USERID) String userId,
			@QueryParam(ApiFilter.DELIVERABLE) Boolean filterDeliverable) throws RemoteException, EJBExceptionLP, NamingException {
		ForecastPositionEntryList entries = new ForecastPositionEntryList();
		if (connectClient(userId) == null) return entries;
		
		if(!mandantCall.hasModulForecast()) {
			respondNotFound();
			return entries;
		}
		
		if (Boolean.TRUE.equals(filterDeliverable)) {
			List<ForecastpositionProduktionDto> dtos = forecastCall.getLieferbareForecastpositionByMandant();
			entries.setEntries(forecastPositionEntryMapper.mapEntries(dtos));
			return entries;
		}
		
		return entries;
	}
	
	@GET
	@Path("/position/{" + Param.POSITIONID + "}")
	@Produces({FORMAT_JSON, FORMAT_XML})
	public ForecastPositionEntry findForecastposition(
			@PathParam(Param.POSITIONID) Integer positionid,
			@QueryParam(Param.USERID) String userId,
			@QueryParam(ApiFilter.DELIVERABLE) Boolean filterDeliverable) throws RemoteException, NamingException {
		if (connectClient(userId) == null) return null;
		
		if(!mandantCall.hasModulForecast()) {
			respondNotFound();
			return null;
		}
		
		ForecastpositionDto fcpositionDto = forecastCall.forecastpositionFindByPrimaryKey(positionid);
		if (fcpositionDto == null) {
			respondNotFound("positionid", positionid);
			return null;
		}
		
		if (Boolean.TRUE.equals(filterDeliverable)) {
			ForecastpositionProduktionDto dto = forecastCall.getLieferbareForecastpositionByIId(positionid, true);
			return forecastPositionEntryMapper.mapEntry(dto);
		}

		return forecastPositionEntryMapper.mapEntry(fcpositionDto);
	}
	
	
	@GET
	@Path("/linecall/{" + Param.LINECALLID + "}")
	@Produces({FORMAT_JSON, FORMAT_XML})
	public LinecallEntry findLinecall(
			@PathParam(Param.LINECALLID) Integer linecallid,
			@QueryParam(Param.USERID) String userId) {
		if (connectClient(userId) == null) return null;
		
		if(!mandantCall.hasModulForecast()) {
			respondNotFound();
			return null;
		}
		
		LinienabrufDto linienabrufDto = forecastCall.linienabrufFindByPrimaryKeyOhneExc(linecallid);
		if (linienabrufDto == null) {
			respondNotFound("linecallid", linecallid);
			return null;
		}
		
		return linecallEntryMapper.mapEntry(linienabrufDto);
	}
	
	@POST
	@Path("/position/{" + Param.POSITIONID + "}")
	@Consumes({FORMAT_JSON, FORMAT_XML})
	@Produces({FORMAT_JSON, FORMAT_XML})
	public ForecastPositionEntry createForecastpositionDelivery(
			@PathParam(Param.POSITIONID) Integer positionid,
			LinecallDeliveryPostEntry deliveryEntry) throws RemoteException, EJBExceptionLP, NamingException {
		HvValidateBadRequest.notNull(deliveryEntry, "deliveryEntry");
		HvValidateBadRequest.notNull(deliveryEntry.getItemId(), "itemId");
		HvValidateBadRequest.notNull(deliveryEntry.getQuantity(), "quantity");
		HvValidateBadRequest.notNull(deliveryEntry.getPickingType(), "pickingType");
		if (connectClient(deliveryEntry.getUserId()) == null) return null;
		
		if(!mandantCall.hasModulForecast()) {
			respondNotFound();
			return null;
		}
		
		ForecastpositionDto fcpositionDto = forecastCall.forecastpositionFindByPrimaryKey(positionid);
		if (fcpositionDto == null) {
			respondNotFound("positionid", positionid);
			return null;
		}
		
		ArtikelDto artikelDto = artikelCall.artikelFindByPrimaryKeySmallOhneExc(deliveryEntry.getItemId()); 
		if (artikelDto == null) {
			respondNotFound("itemId", deliveryEntry.getItemId());
			return null;
		}
		ForecastpositionArtikelbuchungDto fpaDto = new ForecastpositionArtikelbuchungDto();
		fpaDto.setArtikelIId(deliveryEntry.getItemId());
		fpaDto.setForecastpositionIId(positionid);
		fpaDto.setMenge(deliveryEntry.getQuantity());
		fpaDto.setKommissioniertyp(map(deliveryEntry.getPickingType()));
		
		return forecastPositionEntryMapper.mapEntry(forecastCall.produziereMaterial(fpaDto));
	}
	
	@POST
	@Path("/linecall/{" + Param.LINECALLID + "}")
	@Consumes({FORMAT_JSON, FORMAT_XML})
	@Produces({FORMAT_JSON, FORMAT_XML})
	public LinecallDeliveryResultEntry createDelivery(
			@PathParam(Param.LINECALLID) Integer linecallid,
			LinecallDeliveryPostEntry deliveryEntry) throws RemoteException, EJBExceptionLP {
		HvValidateBadRequest.notNull(deliveryEntry, "deliveryEntry");
		HvValidateBadRequest.notNull(deliveryEntry.getItemId(), "itemId");
		HvValidateBadRequest.notNull(deliveryEntry.getQuantity(), "quantity");
		if (connectClient(deliveryEntry.getUserId()) == null) return null;
		
		if(!mandantCall.hasModulForecast()) {
			respondNotFound();
			return null;
		}
		
		LinienabrufDto linienabrufDto = forecastCall.linienabrufFindByPrimaryKeyOhneExc(linecallid);
		if (linienabrufDto == null) {
			respondNotFound("linecallid", linecallid);
			return null;
		}
		
		ArtikelDto artikelDto = artikelCall.artikelFindByPrimaryKeySmallOhneExc(deliveryEntry.getItemId()); 
		if (artikelDto == null) {
			respondNotFound("itemId", deliveryEntry.getItemId());
			return null;
		}

		LinienabrufArtikelbuchungDto labDto = new LinienabrufArtikelbuchungDto();
		labDto.setArtikelIId(deliveryEntry.getItemId());
		labDto.setLinienabrufIId(linecallid);
		labDto.setMenge(deliveryEntry.getQuantity());
		
		LinienabrufArtikelDto laDto = forecastCall.produziereLinienabrufArtikel(labDto);
		LinecallDeliveryResultEntry resultEntry = new LinecallDeliveryResultEntry();
		resultEntry.setItemId(laDto.getArtikelIId());
		resultEntry.setLinecallId(linecallid);
		resultEntry.setOpenQuantity(laDto.getOffeneMenge());
		
		return resultEntry;
	}

	@POST
	@Path("/material/{" + Param.POSITIONID + "}/delivery")
	@Produces({FORMAT_JSON, FORMAT_XML})
	public ForecastPositionEntry startDelivery(
			@PathParam(Param.POSITIONID) Integer positionid,
			@QueryParam(Param.USERID) String userId) throws RemoteException, NamingException {
		if (connectClient(userId) == null) return null;

		if(!mandantCall.hasModulForecast()) {
			respondNotFound();
			return null;
		}
		
		ForecastpositionDto fcpositionDto = forecastCall.forecastpositionFindByPrimaryKey(positionid);
		if (fcpositionDto == null) {
			respondNotFound("positionid", positionid);
			return null;
		}
		
		forecastCall.starteLinienabrufProduktion(positionid);
//		forecastCall.bucheZeitAufForecastposition(positionid);

		ForecastpositionProduktionDto dto = forecastCall.getLieferbareForecastpositionByIId(positionid, true);
		ForecastPositionEntry fcpositionEntry = forecastPositionEntryMapper.mapEntry(dto);
		
		return fcpositionEntry;
	}

	@GET
	@Path("/material/{" + Param.POSITIONID + "}/delivery")
	@Produces({FORMAT_JSON, FORMAT_XML})
	public LinecallEntryList getDelivery(
			@PathParam(Param.POSITIONID) Integer positionid,
			@QueryParam(Param.USERID) String userId) throws RemoteException, NamingException {
		LinecallEntryList entries = new LinecallEntryList();
		if (connectClient(userId) == null) return entries;

		if(!mandantCall.hasModulForecast()) {
			respondNotFound();
			return entries;
		}
		
		ForecastpositionDto fcpositionDto = forecastCall.forecastpositionFindByPrimaryKey(positionid);
		if (fcpositionDto == null) {
			respondNotFound("positionid", positionid);
			return entries;
		}
		
		if (!forecastCall.isLinienabrufProduktionGestartet(positionid)) {
			return entries;
		}
		ForecastpositionProduktionDto dto = forecastCall.getLieferbareForecastpositionByIId(positionid, true);
		ForecastPositionEntry fcpositionEntry = forecastPositionEntryMapper.mapEntry(dto);
		entries.setEntries(fcpositionEntry.getLinecallEntries());

		return entries;
	}
	
	@GET
	@Path("/position/{" + Param.POSITIONID + "}/printdispatchlabel")
	public Response printDispatchLabel(
			@PathParam(Param.POSITIONID) Integer positionid,
			@QueryParam(Param.USERID) String userId) throws NamingException, IOException {
		if (connectClient(userId) == null) return null;
		
		HvValidateBadRequest.notNull(positionid, Param.POSITIONID);
		
		if(!mandantCall.hasModulForecast()) {
			respondNotFound() ;
			return null;
		}
		
		JasperPrintLP print = fertigungReportCall.printVersandetikettVorbereitungForecast(positionid);
		HvValidateBadRequest.notNull(print, "print");
		
		final ByteArrayOutputStream baos = new ByteArrayOutputStream(100000) ;
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(print.getPrint());
		oos.close();
		StreamingOutput so = new StreamingOutput() {				
			@Override
			public void write(OutputStream os) throws IOException, WebApplicationException {
				os.write(baos.toByteArray()) ;
			}
		} ;
		String filename = positionid + "dispatchlabel.jrprint" ;
		return Response.ok()
			.header("Content-Disposition", "filename=" + filename)
			.header("Content-Type", "application/octet-stream")
			.entity(so).build() ;
	}

	@POST
	@Path("/material/{" + Param.POSITIONID + "}")
	@Produces({FORMAT_JSON, FORMAT_XML})
	public void createDelivery(
			@PathParam(Param.POSITIONID) Integer positionid,
			@QueryParam(Param.USERID) String userId,
			@QueryParam(ApiFilter.PICKINGTYPE) PickingType pickingType) throws RemoteException, NamingException {
		if (connectClient(userId) == null) return;

		HvValidateBadRequest.notNull(pickingType, ApiFilter.PICKINGTYPE);
		if(!mandantCall.hasModulForecast()) {
			respondNotFound();
			return;
		}
		
		ForecastpositionDto fcpositionDto = forecastCall.forecastpositionFindByPrimaryKey(positionid);
		if (fcpositionDto == null) {
			respondNotFound("positionid", positionid);
			return;
		}
		
		forecastCall.createAblieferungLinienabrufProduktion(positionid, map(pickingType));
	}
	
	@GET
	@Path("pickingprinter/list")
	@Produces({FORMAT_JSON, FORMAT_XML})
	@Override
	public PickingPrinterEntryList getPickingPrinter(
			@QueryParam(Param.USERID) String userId) {
		if (connectClient(userId) == null) return new PickingPrinterEntryList();
		
		if(!mandantCall.hasModulForecast()) {
			respondNotFound();
			return null;
		}
		
		List<KommdruckerDto> kommdruckerDtos = forecastCall.getAllKommdrucker();
		PickingPrinterEntryList entries = new PickingPrinterEntryList();
		entries.setEntries(pickingPrinterEntryMapper.mapEntries(kommdruckerDtos));
		
		return entries;
	}
}
