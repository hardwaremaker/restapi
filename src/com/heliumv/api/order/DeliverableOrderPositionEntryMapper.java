package com.heliumv.api.order;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.api.item.ItemIdentityEntry;
import com.heliumv.api.item.ItemIdentityEntryList;
import com.heliumv.api.item.ItemPropertyEnum;
import com.heliumv.api.stock.StockInfoEntry;
import com.heliumv.api.stock.StockInfoEntryMapper;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.LagerplatzDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.auftrag.service.AuftragpositionDto;

public class DeliverableOrderPositionEntryMapper {
	
	@Autowired
	private StockInfoEntryMapper stockInfoEntryMapper;
	
	public DeliverableOrderPositionEntry mapEntry(AuftragpositionDto positionDto, ArtikelDto artikelDto) {
		DeliverableOrderPositionEntry positionEntry = new DeliverableOrderPositionEntry() ;
		mapArtikelDto(positionEntry, artikelDto) ;
		mapPositionDto(positionEntry, positionDto) ;
		return positionEntry ;
	}
	
	public DeliverableOrderPositionEntry mapEntry(AuftragpositionDto positionDto, ArtikelDto artikelDto, List<LagerplatzDto> lagerplatzDtos) {
		DeliverableOrderPositionEntry positionEntry = mapEntry(positionDto, artikelDto);
		mapLagerplatzDtos(positionEntry, lagerplatzDtos);
		return positionEntry;
	}
	
	private void mapLagerplatzDtos(DeliverableOrderPositionEntry positionEntry, List<LagerplatzDto> lagerplatzDtos) {
		List<StockInfoEntry> stockInfoEntries = stockInfoEntryMapper.mapEntries(lagerplatzDtos);
		if (stockInfoEntries.size() < 1) return;
		
		positionEntry.setStockinfoEntry(stockInfoEntries.get(0));
	}

	protected DeliverableOrderPositionEntryMapper mapPositionDto(
			DeliverableOrderPositionEntry positionEntry, AuftragpositionDto positionDto) {		
		positionEntry.setId(positionDto.getIId());
		positionEntry.setItemId(positionDto.getArtikelIId());
		positionEntry.setAmount(positionDto.getNMenge()) ;
		positionEntry.setOpenAmount(positionDto.getNOffeneMenge());
		positionEntry.setStatus(positionDto.getAuftragpositionstatusCNr()) ;
		positionEntry.setUnitCnr(positionDto.getEinheitCNr());
		positionEntry.setDescription(positionDto.getCBez()) ;

		if(ItemPropertyEnum.NOIDENTIY != positionEntry.getItemProperty()) {
			List<ItemIdentityEntry> identityList = new ArrayList<ItemIdentityEntry>() ;
			if(positionDto.getSeriennrChargennrMitMenge() != null) {
				for (SeriennrChargennrMitMengeDto snrDto : positionDto.getSeriennrChargennrMitMenge()) {
					ItemIdentityEntry identityEntry = new ItemIdentityEntry() ;
					identityEntry.setIdentity(snrDto.getCSeriennrChargennr());
					identityEntry.setAmount(snrDto.getNMenge()); 
					identityList.add(identityEntry);
				}				
			}
			positionEntry.setItemIdentity(new ItemIdentityEntryList(identityList));
		}

		return this ;
	}
	
	protected DeliverableOrderPositionEntryMapper mapArtikelDto(
			DeliverableOrderPositionEntry positionEntry, ArtikelDto artikelDto) {
		ItemPropertyEnum itemProperty = ItemPropertyEnum.NOIDENTIY ;
		if(artikelDto.isChargennrtragend()) {
			itemProperty = ItemPropertyEnum.BATCHNR ;
		}
		if(artikelDto.isSeriennrtragend()) {
			itemProperty = ItemPropertyEnum.SERIALNR ;
		}
		positionEntry.setItemProperty(itemProperty);
		positionEntry.setItemCnr(artikelDto.getCNr());
		return this ;
	 }
 }
