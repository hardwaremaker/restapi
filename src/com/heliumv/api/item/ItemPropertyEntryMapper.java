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

import com.heliumv.api.system.PropertyLayoutEntry;
import com.heliumv.api.system.PropertyLayoutFill;
import com.heliumv.api.system.PropertyLayoutOrientation;
import com.heliumv.api.system.PropertyLayoutType;
import com.heliumv.factory.ISystemMultilanguageCall;
import com.heliumv.factory.legacy.PaneldatenPair;
import com.heliumv.tools.ShortHelper;
import com.lp.server.system.service.PanelbeschreibungDto;
import com.lp.server.system.service.PaneldatenDto;
import com.lp.util.Helper;

public class ItemPropertyEntryMapper {
	@Autowired
	private ISystemMultilanguageCall systemMultilanguageCall;
	
	public ItemPropertyEntry mapEntry(PaneldatenDto paneldatenDto) {
		return mapEntryImpl(paneldatenDto) ;
	}
	
	public List<ItemPropertyEntry> mapEntry(PaneldatenDto[] dtos) {
		List<ItemPropertyEntry> properties = new ArrayList<ItemPropertyEntry>() ;
		if(dtos == null) return properties ;
		
		for (PaneldatenDto paneldatenDto : dtos) {
			properties.add(mapEntryImpl(paneldatenDto)) ;
		}
		
		return properties ;
	}

	public List<ItemPropertyEntry> mapEntry(List<PaneldatenPair> panelEntries) {
		List<ItemPropertyEntry> properties = new ArrayList<ItemPropertyEntry>() ;

		for (PaneldatenPair panelPair : panelEntries) {
			properties.add(mapEntryImpl(panelPair)) ;
		}
		return properties ;
	}
	
	private ItemPropertyEntry mapEntryImpl(PaneldatenPair pair) {
		ItemPropertyEntry entry = mapEntryImpl(pair.getPaneldatenDto()) ;
		PanelbeschreibungDto beschreibungDto = pair.getPanelbeschreibungDto() ;
		entry.setName(beschreibungDto.getCDruckname()) ;
//		entry.setName(beschreibungDto.getCName());
		entry.setMandatory(ShortHelper.isSet(beschreibungDto.getBMandatory())) ;
		entry.setItemgroupId(beschreibungDto.getArtgruIId()) ;
		entry.setLayoutId(beschreibungDto.getIId());
		
		return entry ;
	}
	
	private ItemPropertyEntry mapEntryImpl(PaneldatenDto paneldatenDto) {
		ItemPropertyEntry entry = new ItemPropertyEntry();
		if (paneldatenDto == null) return entry;
		
		entry.setId(paneldatenDto.getIId());
		entry.setDatatype(paneldatenDto.getCDatentypkey());
		entry.setContent(paneldatenDto.getXInhalt());
		entry.setLayoutId(paneldatenDto.getPanelbeschreibungIId());
		return entry ;
	}
	
	public PropertyLayoutEntry mapEntry(PanelbeschreibungDto panelbeschreibungDto) {
		PropertyLayoutEntry layoutEntry = new PropertyLayoutEntry();
		layoutEntry.setId(panelbeschreibungDto.getIId());
		layoutEntry.setType(PropertyLayoutType.fromString(panelbeschreibungDto.getCTyp()));
		layoutEntry.setOrientation(PropertyLayoutOrientation.fromString(panelbeschreibungDto.getCAnchor()));
		layoutEntry.setFill(PropertyLayoutFill.fromString(panelbeschreibungDto.getCFill()));
		layoutEntry.setColumn(panelbeschreibungDto.getIGridx());
		layoutEntry.setRow(panelbeschreibungDto.getIGridy());
		layoutEntry.setHeight(panelbeschreibungDto.getIGridheigth());
		layoutEntry.setWidth(panelbeschreibungDto.getIGridwidth());
		layoutEntry.setGapBottom(panelbeschreibungDto.getIInsetsbottom());
		layoutEntry.setGapLeft(panelbeschreibungDto.getIInsetsleft());
		layoutEntry.setGapRight(panelbeschreibungDto.getIInsetsright());
		layoutEntry.setGapTop(panelbeschreibungDto.getIInsetstop());
		layoutEntry.setMandatory(ShortHelper.isSet(panelbeschreibungDto.getBMandatory()));
		layoutEntry.setHeading(ShortHelper.isSet(panelbeschreibungDto.getBUeberschrift()));
		layoutEntry.setName(panelbeschreibungDto.getCName());
		layoutEntry.setPaddingX(panelbeschreibungDto.getIIpadx());
		layoutEntry.setPaddingY(panelbeschreibungDto.getIIpady());
		layoutEntry.setWeightX(new BigDecimal(panelbeschreibungDto.getFWeightx().toString()));
		layoutEntry.setWeightY(new BigDecimal(panelbeschreibungDto.getFWeighty().toString()));
		setLayoutText(layoutEntry, panelbeschreibungDto);
		
		return layoutEntry;
	}
	
	private void setLayoutText(PropertyLayoutEntry entry, PanelbeschreibungDto panelbeschreibungDto) {
		if (Helper.isOneOf(entry.getType(), 
				PropertyLayoutType.LABEL, PropertyLayoutType.CHECKBOX, 
				PropertyLayoutType.PRINTBUTTON, PropertyLayoutType.EXECUTEBUTTON)) {
			String text = null;
			try {
				text = systemMultilanguageCall.getTextRespectUISpr(panelbeschreibungDto.getCTokeninresourcebundle());
			} catch (RemoteException e) {
			}
			entry.setText(text == null || text.startsWith("resourcebundletext fehlt") 
					? panelbeschreibungDto.getCTokeninresourcebundle() : text);
		} else {
			entry.setText(panelbeschreibungDto.getCTokeninresourcebundle());
		}
	}

	public List<PropertyLayoutEntry> mapEntry(PanelbeschreibungDto[] beschreibungen) {
		List<PropertyLayoutEntry> entries = new ArrayList<PropertyLayoutEntry>();
		if (beschreibungen == null) return entries;

		for (PanelbeschreibungDto dto : beschreibungen) {
			entries.add(mapEntry(dto));
		}
		return entries;
	}

	public ItemPropertyEntry mapEntry(PaneldatenPair pair) {
		return mapEntryImpl(pair);
	}
	
	public List<PaneldatenDto> mapDto(String key, ItemPropertyEntryList entry) {
		return mapDto(key, entry.getEntries());
	}
	
	public List<PaneldatenDto> mapDto(String key, List<ItemPropertyEntry> entries) {
		List<PaneldatenDto> dtos = new ArrayList<PaneldatenDto>();
		for (ItemPropertyEntry propertyEntry : entries) {
			PaneldatenDto dto = mapDto(key, propertyEntry);
			dtos.add(dto);
		}

		return dtos;
	}

	public PaneldatenDto mapDto(String key, ItemPropertyEntry entry) {
		PaneldatenDto dto = mapDto(entry);
		dto.setCKey(key);
		return dto;		
	}
	
	
	public PaneldatenDto mapDto(ItemPropertyEntry entry) {
		PaneldatenDto dto = new PaneldatenDto();
		dto.setPanelbeschreibungIId(entry.getLayoutId());
		dto.setXInhalt(Helper.emptyString(entry.getContent()));
		dto.setIId(entry.getId());
		dto.setCDatentypkey(entry.getDatatype());
		return dto;
	}
}
