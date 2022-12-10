package com.heliumv.api.system;

import com.lp.server.system.service.PanelbeschreibungDto;
import com.lp.server.system.service.PaneldatenDto;

public interface IPropertyValidator {

	PanelbeschreibungDto validPanelbeschreibungDto(Integer panelbeschreibungId);
	PaneldatenDto validPaneldaten(Integer paneldatenId);
	PaneldatenDto setupDefault(PanelbeschreibungDto panelbeschreibung);
}
