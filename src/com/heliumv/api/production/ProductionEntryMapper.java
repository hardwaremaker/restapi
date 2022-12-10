package com.heliumv.api.production;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.api.item.ItemEntryMapper;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LossollmaterialDto;
import com.lp.util.Helper;

public class ProductionEntryMapper {
	@Autowired
	private ItemEntryMapper itemEntryMapper;
	
	public ProductionEntry mapEntry(LosDto losDto) {
		ProductionEntry entry = new ProductionEntry();
		entry.setId(losDto.getIId());
		entry.setCnr(losDto.getCNr());
		entry.setPartlistId(losDto.getStuecklisteIId());
		entry.setAmount(losDto.getNLosgroesse());
		entry.setComment(losDto.getCKommentar());
		entry.setEndDateMs(losDto.getTProduktionsende().getTime());
		entry.setStartDateMs(losDto.getTProduktionsbeginn().getTime());
		entry.setProject(losDto.getCProjekt());
		entry.setStatus(ProductionStatus.fromString(losDto.getStatusCNr()));
		entry.setTargetStockId(losDto.getLagerIIdZiel());
		
		return entry;
	}

	public ProductionTargetMaterialEntry mapEntry(LossollmaterialDto dto) {
		ProductionTargetMaterialEntry entry = new ProductionTargetMaterialEntry();
		entry.setId(dto.getIId());
		entry.setAmount(dto.getNMenge());
		entry.setComment(dto.getCKommentar());
		entry.setiSort(dto.getISort());
		entry.setPosition(dto.getCPosition());
		entry.setPrice(dto.getNSollpreis());
		entry.setProductionId(dto.getLosIId());
		entry.setUnitCnr(dto.getEinheitCNr());
		entry.setMountingMethodId(dto.getMontageartIId());
		entry.setBelatedWithdrawn(Helper.short2Boolean(dto.getBNachtraeglich()));
		
		return entry;
	}
	
	public ProductionEntry mapEntryIdCnr(LosDto losDto) {
		ProductionEntry entry = new ProductionEntry();
		entry.setId(losDto.getIId());
		entry.setCnr(losDto.getCNr());
		return entry;
	}
	
	public List<ProductionEntry> mapEntriesIdCnr(List<LosDto> losDtos) {
		List<ProductionEntry> entries = new ArrayList<ProductionEntry>();
		for (LosDto dto : losDtos) {
			entries.add(mapEntryIdCnr(dto));
		}
		return entries;
	}
}
