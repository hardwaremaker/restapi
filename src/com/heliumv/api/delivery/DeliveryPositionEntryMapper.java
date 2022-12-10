package com.heliumv.api.delivery;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.api.item.ItemIdentityEntryList;
import com.heliumv.api.item.ItemIdentyEntryMapper;
import com.heliumv.api.item.ItemPropertyEnum;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.lieferschein.service.LieferscheinpositionDto;

public class DeliveryPositionEntryMapper {
	@Autowired
	ItemIdentyEntryMapper itemMapper;
	
	public DeliveryPositionEntry mapEntry(LieferscheinDto lsDto, 
			LieferscheinpositionDto posDto, ArtikelDto artikelDto) {
		DeliveryPositionEntry positionEntry = new DeliveryPositionEntry();
		positionEntry.setDeliveryId(lsDto.getIId());
		mapPositionDto(positionEntry, posDto);
		mapArtikelDto(positionEntry, artikelDto);
		mapIdentities(positionEntry, posDto);
		return positionEntry;
	}
	
	protected DeliveryPositionEntryMapper mapPositionDto(
			DeliveryPositionEntry positionEntry, LieferscheinpositionDto posDto) {
		positionEntry.setId(posDto.getIId());
		positionEntry.setItemId(posDto.getArtikelIId());
		positionEntry.setAmount(posDto.getNMenge()) ;
		positionEntry.setUnitCnr(posDto.getEinheitCNr());
		positionEntry.setDescription(posDto.getCBez());
		positionEntry.setOrderPositionId(posDto.getAuftragpositionIId());
		return this;
	}
	
	protected DeliveryPositionEntryMapper mapArtikelDto(
			DeliveryPositionEntry positionEntry, ArtikelDto artikelDto) {
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
	
	protected DeliveryPositionEntryMapper mapIdentities(
			DeliveryPositionEntry positionEntry, LieferscheinpositionDto posDto) {
		if(posDto.getSeriennrChargennrMitMenge() == null || posDto.getSeriennrChargennrMitMenge().size() == 0) {
			return this;
		}

		ItemIdentityEntryList itemIdentity = new ItemIdentityEntryList();
		for(SeriennrChargennrMitMengeDto identity : posDto.getSeriennrChargennrMitMenge()) {
			itemIdentity.getEntries().add(itemMapper.mapEntry(identity));
		}
		positionEntry.setItemIdentity(itemIdentity);
		
		return this;
	}
}
