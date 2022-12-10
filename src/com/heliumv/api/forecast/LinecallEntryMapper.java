package com.heliumv.api.forecast;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.api.item.ItemEntryMapper;
import com.heliumv.factory.IArtikelCall;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.forecast.service.ForecastpositionProduktionDto;
import com.lp.server.forecast.service.LinienabrufArtikelDto;
import com.lp.server.forecast.service.LinienabrufDto;
import com.lp.server.forecast.service.LinienabrufProduktionDto;

public class LinecallEntryMapper {

	@Autowired
	private ItemEntryMapper itemEntryMapper;
	@Autowired
	private IArtikelCall artikelCall;

	public LinecallEntry mapEntry(LinienabrufDto dto) {
		LinecallEntry entry = new LinecallEntry();
		entry.setId(dto.getIId());
		entry.setLine(dto.getCLinie());
		entry.setOrdernumber(dto.getCBestellnummer());
		entry.setProductionDateMs(dto.getTProduktionstermin() != null ? dto.getTProduktionstermin().getTime() : null);
		entry.setQuantity(dto.getNMenge());
		entry.setSector(dto.getCBereichNr());
		entry.setSectorDescription(dto.getCBereichBez());
		entry.setLinecallItemEntries(null);
		
		return entry;
	}

	public LinecallEntry mapEntry(LinienabrufProduktionDto dto) {
		LinecallEntry entry = mapEntry((LinienabrufDto)dto);
		
		if (!dto.getLinienabrufArtikel().isEmpty()) {
			entry.setLinecallItemEntries(mapLinecallItemEntries(dto.getLinienabrufArtikel()));
		}
		
		return entry;
	}
	
	public List<LinecallEntry> mapEntries(List<LinienabrufProduktionDto> dtos) {
		List<LinecallEntry> entries = new ArrayList<LinecallEntry>();
		for (LinienabrufProduktionDto dto : dtos) {
			entries.add(mapEntry(dto));
		}
		return entries;
	}
	
	public LinecallItemEntry mapLinecallItemEntry(LinienabrufArtikelDto dto) {
		return mapLinecallItemEntry(dto, null);
	}
	
	private LinecallItemEntry mapLinecallItemEntry(LinienabrufArtikelDto dto, ArtikelDto artikelDto) {
		LinecallItemEntry entry = new LinecallItemEntry();
		entry.setQuantity(dto.getMenge());
		entry.setOpenQuantity(dto.getOffeneMenge());
		
		if (artikelDto == null) {
			try {
				artikelDto = artikelCall.artikelFindByPrimaryKeySmallOhneExc(dto.getArtikelIId());
			} catch (RemoteException e) {
			}
		}
		entry.setItemEntry(itemEntryMapper.mapV1EntrySmall(artikelDto));
		entry.getItemEntry().setStockAmount(dto.getLagerstand());
		return entry;
	}

	public List<LinecallItemEntry> mapLinecallItemEntries(List<LinienabrufArtikelDto> dtos) {
		List<LinecallItemEntry> entries = new ArrayList<LinecallItemEntry>();
		for (LinienabrufArtikelDto dto : dtos) {
			entries.add(mapLinecallItemEntry(dto));
		}
		return entries;
	}
	
	public List<LinecallEntry> mapEntries(ForecastpositionProduktionDto dto) {
		List<LinecallEntry> entries = new ArrayList<LinecallEntry>();
		for (LinienabrufProduktionDto lapDto : dto.getLinienabrufe()) {
			LinecallEntry entry = mapEntry((LinienabrufDto) lapDto);
			entry.setLinecallItemEntries(mapLinecallItemEntries(lapDto.getLinienabrufArtikel(), dto.getHmArtikelLinienabrufe()));
			entries.add(entry);
		}
		
		return entries;
	}

	private List<LinecallItemEntry> mapLinecallItemEntries(List<LinienabrufArtikelDto> dtos,
			Map<Integer, ArtikelDto> hmArtikelLinienabrufe) {
		List<LinecallItemEntry> entries = new ArrayList<LinecallItemEntry>();
		for (LinienabrufArtikelDto dto : dtos) {
			entries.add(mapLinecallItemEntry(dto, hmArtikelLinienabrufe.get(dto.getArtikelIId())));
		}
		return entries;
	}
}
