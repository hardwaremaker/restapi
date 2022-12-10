package com.heliumv.api.forecast;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.api.item.ItemEntryMapper;
import com.heliumv.factory.IArtikelCall;
import com.heliumv.factory.IPartnerCall;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.forecast.service.ForecastpositionDto;
import com.lp.server.forecast.service.ForecastpositionProduktionDto;
import com.lp.server.forecast.service.ForecastpositionWare;
import com.lp.server.partner.service.PartnerDto;

public class ForecastPositionEntryMapper {

	@Autowired
	private ItemEntryMapper itemEntryMapper;
	@Autowired
	private IArtikelCall artikelCall;
	@Autowired
	private LinecallEntryMapper linecallEntryMapper;
	@Autowired
	private IPartnerCall partnerCall;

	public ForecastPositionEntry mapEntry(ForecastpositionProduktionDto dto) throws RemoteException, NamingException {
		ForecastPositionEntry entry = mapEntry((ForecastpositionDto)dto);
		if (entry.getItemEntry() != null) {
			entry.getItemEntry().setStockAmount(dto.getLagerstand());
		}
		
		if (!dto.getLinienabrufe().isEmpty()) {
			entry.setLinecallEntries(linecallEntryMapper.mapEntries(dto));
		}
		
		if (dto.getPersonalIIdKommissionierer() != null) {
			PartnerDto partnerDto = partnerCall.partnerFindByPersonalId(dto.getPersonalIIdKommissionierer());
			entry.setStaffId(dto.getPersonalIIdKommissionierer());
			String vorname = partnerDto.getCName2vornamefirmazeile2();
			StringBuilder nameBuilder = new StringBuilder(vorname != null ? vorname : "");
			
			if (nameBuilder.length() > 0 && partnerDto.getCName1nachnamefirmazeile1() != null) 
				nameBuilder.append(" ");
			
			nameBuilder.append(partnerDto.getCName1nachnamefirmazeile1());
			entry.setStaffDescription(nameBuilder.toString());
		}
		entry.setProductionCnr(dto.getLosCnr());
		entry.setProductType(mapProductType(dto.getWarentyp()));
		return entry;
	}

	private ProductType mapProductType(ForecastpositionWare warentyp) {
		if (ForecastpositionWare.KOMMISSIONIERWARE.equals(warentyp)) return ProductType.PICKING;
		if (ForecastpositionWare.LAGERWARE.equals(warentyp)) return ProductType.STOCK;
		return null;
	}

	public List<ForecastPositionEntry> mapEntries(List<ForecastpositionProduktionDto> dtos) throws RemoteException, NamingException {
		List<ForecastPositionEntry> entries = new ArrayList<ForecastPositionEntry>();
		for (ForecastpositionProduktionDto dto : dtos) {
			entries.add(mapEntry(dto));
		}
		return entries;
	}

	public ForecastPositionEntry mapEntry(ForecastpositionDto dto) {
		ForecastPositionEntry entry = new ForecastPositionEntry();
		entry.setId(dto.getIId());
		entry.setDateMs(dto.getTTermin().getTime());
		entry.setOrdernumber(dto.getCBestellnummer());
		entry.setQuantity(dto.getNMenge());

		try {
			ArtikelDto artikelDto = artikelCall.artikelFindByPrimaryKeySmallOhneExc(dto.getArtikelIId());
			entry.setItemEntry(itemEntryMapper.mapV1EntrySmall(artikelDto));
		} catch (RemoteException e) {
		}

		entry.setLinecallEntries(null);
		
		return entry;
	}

}
