package com.heliumv.api.item;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.api.HvValidateNotFound;
import com.heliumv.api.system.IPropertyValidator;
import com.heliumv.factory.IPanelCall;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.system.service.PanelFac;
import com.lp.server.system.service.PanelbeschreibungDto;
import com.lp.server.system.service.PaneldatenDto;

public class ItemPropertyValidator implements IPropertyValidator {
	
	@Autowired
	private IPanelCall panelCall;

	private ArtikelDto artikelDto;
	
	public ItemPropertyValidator() {
	}
	
	public void setArtikelDto(ArtikelDto artikelDto) {
		this.artikelDto = artikelDto;
	}
	
	public ArtikelDto getArtikelDto() {
		return artikelDto;
	}
	
	@Override
	public PanelbeschreibungDto validPanelbeschreibungDto(Integer panelbeschreibungId) {
		PanelbeschreibungDto beschreibung = panelCall.panelbeschreibungFindByPrimaryKeyOhneExc(panelbeschreibungId);
		HvValidateNotFound.notValid(artikelDto.getArtgruIId().equals(beschreibung.getArtgruIId()), "layoutId", panelbeschreibungId.toString());
		
		return beschreibung;
	}
	
	@Override
	public PaneldatenDto validPaneldaten(Integer paneldatenId) {
		PaneldatenDto daten = panelCall.paneldatenFindByPrimaryKeyOhneExc(paneldatenId);
		HvValidateNotFound.notNull(daten, "propertyId", paneldatenId);
		HvValidateNotFound.notValid(PanelFac.PANEL_ARTIKELEIGENSCHAFTEN.equals(daten.getPanelCNr()), "propertyId", paneldatenId.toString());
		HvValidateNotFound.notValid(daten.getCKey().equals(artikelDto.getIId().toString()), "propertyId", paneldatenId.toString());
		
		return daten;
	}
	
	@Override
	public PaneldatenDto setupDefault(PanelbeschreibungDto panelbeschreibung) {
		return panelCall.setupDefaultArtikelPaneldaten(panelbeschreibung, artikelDto.getIId());
	}
}
