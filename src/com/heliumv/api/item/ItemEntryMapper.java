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
package com.heliumv.api.item;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.factory.IArtikelCall;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelMitVerpackungsgroessenDto;
import com.lp.server.artikel.service.ArtikelsprDto;
import com.lp.server.artikel.service.EinkaufseanDto;

public class ItemEntryMapper {
	@Autowired
	private IArtikelCall artikelCall;

	public ItemEntryInternal mapEntry(ArtikelDto artikelDto) {
		ItemEntryInternal entry = new ItemEntryInternal() ;
		entry.setId(artikelDto.getIId()); 
		entry.setCnr(artikelDto.getCNr());
		ArtikelsprDto artikelSprDto = artikelDto.getArtikelsprDto() ;
		if(artikelSprDto != null) {
			entry.setName(artikelSprDto.getCBez());
			entry.setShortName(artikelSprDto.getCKbez());
			entry.setDescription(artikelSprDto.getCZbez());
			entry.setDescription2(artikelSprDto.getCZbez2());
		}
		entry.setHidden(artikelDto.getBVersteckt() == null ? false : artikelDto.getBVersteckt() > 0);
		entry.setUnitCnr(artikelDto.getEinheitCNr());
		entry.setTypeCnr(artikelDto.getArtikelartCNr());
		entry.setRevision(artikelDto.getCRevision());
		entry.setReferenceNumber(artikelDto.getCReferenznr());
		entry.setIndex(artikelDto.getCIndex());
		entry.setHasSerialnr(artikelDto.getBSeriennrtragend() == null ? false : artikelDto.getBSeriennrtragend() > 0);
		entry.setHasChargenr(artikelDto.getBChargennrtragend() == null ? false : artikelDto.getBChargennrtragend() > 0);
		mapItemGroup(entry, artikelDto) ;
		mapItemClass(entry, artikelDto) ;
		
		return entry ;
	}
	
	public ItemEntryInternal mapEntry(ArtikelMitVerpackungsgroessenDto artikelMitVerpackungDto) {
		ItemEntryInternal entry = mapEntry(artikelMitVerpackungDto.getArtikelDto()) ;
		List<EinkaufseanDto> eanDtos = artikelMitVerpackungDto.getEanDtos() ; 
		if(eanDtos == null || eanDtos.size() == 0) {
			mapPackagingInfo(entry, artikelMitVerpackungDto.getArtikelDto());
			return entry ;
		}
		
		mapPackagingInfo(entry, eanDtos) ;		
		return entry ;
	}
	
	public ItemV1Entry mapV1EntrySmall(ArtikelDto artikelDto) {
		ItemV1Entry entry = new ItemV1Entry();
		entry.setId(artikelDto.getIId()); 
		entry.setCnr(artikelDto.getCNr());
		
		ArtikelsprDto artikelSprDto = artikelDto.getArtikelsprDto() ;
		if (artikelSprDto == null) {
			try {
				artikelSprDto = artikelCall.artikelSprFindByArtikelIIdOhneExc(artikelDto.getIId());
			} catch (RemoteException e) {
			}
		}
		if(artikelSprDto != null) {
			entry.setName(artikelSprDto.getCBez());
			entry.setShortName(artikelSprDto.getCKbez());
			entry.setDescription(artikelSprDto.getCZbez());
			entry.setDescription2(artikelSprDto.getCZbez2());
		}
		
		entry.setHidden(artikelDto.getBVersteckt() == null ? false : artikelDto.getBVersteckt() > 0);
		entry.setUnitCnr(artikelDto.getEinheitCNr());
		entry.setTypeCnr(artikelDto.getArtikelartCNr());
		entry.setPackagingAmount(artikelDto.getFVerpackungsmenge() != null ? 
				new BigDecimal(artikelDto.getFVerpackungsmenge()) : null);
		entry.setBatchSize(artikelDto.getFFertigungssatzgroesse() != null ?
				new BigDecimal(artikelDto.getFFertigungssatzgroesse()) : null);
		entry.setPackagingAverageAmount(artikelDto.getNVerpackungsmittelmenge());
		
		return entry;
	}
	
	private void mapItemGroup(ItemEntryInternal entry, ArtikelDto artikelDto) {
		if(artikelDto.getArtgruDto() != null) {
			entry.setItemgroupCnr(artikelDto.getArtgruDto().getCNr()) ;
		}		
	}
	
	private void mapItemClass(ItemEntryInternal entry, ArtikelDto artikelDto) {
		if(artikelDto.getArtklaDto() != null) {
			entry.setItemclassCnr(artikelDto.getArtklaDto().getCNr()) ;			
		}
	}
	
	private void mapPackagingInfo(ItemEntryInternal entry, ArtikelDto artikelDto) {
		if(artikelDto.getCVerpackungseannr() == null) return ; 
		
		PackagingInfoEntry packagingEntry = 
				new PackagingInfoEntry(artikelDto.getCVerpackungseannr(), 
						new BigDecimal(artikelDto.getFVerpackungsmenge())) ;
		if(entry.getPackagingEntries() != null) {
			entry.setPackagingEntries(new PackagingInfoEntryList());
		}
		entry.getPackagingEntries().getEntries().add(packagingEntry) ; 
	}
	
	private void mapPackagingInfo(ItemEntryInternal entry, List<EinkaufseanDto> eanDtos) {
		List<PackagingInfoEntry> entries = new ArrayList<PackagingInfoEntry>() ;
		for (EinkaufseanDto eanDto : eanDtos) {
			PackagingInfoEntry packagingEntry = new PackagingInfoEntry(
					eanDto.getCEan(), eanDto.getNMenge()) ;
			entries.add(packagingEntry) ;
		} 
		entry.setPackagingEntries(new PackagingInfoEntryList(entries)); 
	}
	
	public List<ItemV1Entry> mapV1EntriesSmall(List<ArtikelDto> artikelDtos) {
		List<ItemV1Entry> entries = new ArrayList<ItemV1Entry>();
		for (ArtikelDto dto : artikelDtos) {
			entries.add(mapV1EntrySmall(dto));
		}
		return entries;
	}
}
