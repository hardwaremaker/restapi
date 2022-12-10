package com.heliumv.api.forecast;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.factory.IForecastCall;
import com.heliumv.factory.IKundeCall;
import com.lp.server.forecast.service.FclieferadresseDto;
import com.lp.server.forecast.service.FclieferadresseNokaDto;
import com.lp.server.forecast.service.KommdruckerDto;
import com.lp.server.partner.service.KundeDto;

public class ForecastDeliveryAddressEntryMapper {

	@Autowired
	private IKundeCall kundeCall;
	@Autowired
	private IForecastCall forecastCall;
	
	public ForecastDeliveryAddressEntry mapEntry(FclieferadresseDto dto) {
		return mapEntryImpl(dto);
	}

	private ForecastDeliveryAddressEntry mapEntryImpl(FclieferadresseDto dto) { 
		ForecastDeliveryAddressEntry entry = new ForecastDeliveryAddressEntry();
		entry.setId(dto.getiId());
		entry.setForecastId(dto.getForecastIId());
		
		try {
			KundeDto kundeDto = kundeCall.kundeFindByPrimaryKeyOhneExc(
					dto.getKundeIIdLieferadresse());
			if (kundeDto != null && kundeDto.getPartnerDto() != null) {
				StringBuilder builder = new StringBuilder();
				builder.append(kundeDto.getPartnerDto().getCName1nachnamefirmazeile1());
				if (kundeDto.getPartnerDto().getCName2vornamefirmazeile2() != null) {
					builder.append(" ")
						.append(kundeDto.getPartnerDto().getCName2vornamefirmazeile2());
				}
				entry.setDeliveryAddress(builder.toString());
				entry.setDeliveryAddressShort(kundeDto.getPartnerDto().getCKbez());
			}
		} catch (RemoteException e) {
		}
		entry.setPickingType(PickingType.NOTINITIALIZED);
		
		if (dto.getKommdruckerIId() != null) {
			KommdruckerDto kommdruckerDto = forecastCall.kommdruckerFindByPrimaryKey(dto.getKommdruckerIId());
			if (kommdruckerDto != null) {
				entry.setPickingPrinterCnr(kommdruckerDto.getCNr());
			}
		}
		return entry;		
	}
	
	public ForecastDeliveryAddressEntry mapEntry(FclieferadresseNokaDto dto){
		ForecastDeliveryAddressEntry entry = mapEntryImpl(dto);
		String s = dto.getKommissionieren().toString();
		entry.setPickingType(PickingType.fromString(s));
		return entry;
	}
	
	public List<ForecastDeliveryAddressEntry> mapEntries(List<FclieferadresseNokaDto> dtos) {
		List<ForecastDeliveryAddressEntry> entries = new ArrayList<ForecastDeliveryAddressEntry>();
		for (FclieferadresseNokaDto dto : dtos) {
			entries.add(mapEntry(dto));
		}
		
		return entries;
	}
}
