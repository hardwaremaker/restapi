package com.heliumv.api.system;

import java.rmi.RemoteException;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.factory.IPanelCall;
import com.lp.server.system.service.CreatePaneldatenResult;
import com.lp.server.system.service.PanelbeschreibungDto;
import com.lp.server.system.service.PaneldatenDto;

public class PropertyService {

	@Autowired
	private IPanelCall panelCall;
	
	public CreatePaneldatenResult createPaneldaten(ICreatePropertyEntry createEntry, IPropertyValidator validator) {
		PanelbeschreibungDto beschreibung = validator.validPanelbeschreibungDto(createEntry.getLayoutId());
		PaneldatenDto paneldatenDto = validator.setupDefault(beschreibung);
		paneldatenDto.setXInhalt(createEntry.getContent());

		return panelCall.createPaneldaten(paneldatenDto);
	}
	
	public void updatePaneldaten(IUpdatePropertyEntry updateEntry, IPropertyValidator validator) throws RemoteException {
		PaneldatenDto paneldatenDto = validator.validPaneldaten(updateEntry.getPropertyId());
		paneldatenDto.setXInhalt(updateEntry.getContent() != null ? updateEntry.getContent() : "");
		panelCall.updatePaneldaten(paneldatenDto);
	}
}
