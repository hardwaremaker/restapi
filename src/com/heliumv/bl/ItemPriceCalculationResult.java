package com.heliumv.bl;

import com.lp.server.artikel.service.VerkaufspreisDto;
import com.lp.server.artikel.service.VkpreisfindungDto;

public class ItemPriceCalculationResult {
	private VkpreisfindungDto vkPreisfindungDto ;
	private VerkaufspreisDto verkaufspreisDtoZielwaehrung ;
	
	public ItemPriceCalculationResult() {	
	}
	
	public ItemPriceCalculationResult(VkpreisfindungDto vkpreisfindungDto) {
		this.vkPreisfindungDto = vkpreisfindungDto ;
	}
		
	public VkpreisfindungDto getVkPreisfindungDto() {
		return vkPreisfindungDto;
	}

	public void setVkPreisfindungDto(VkpreisfindungDto vkPreisfindungDto) {
		this.vkPreisfindungDto = vkPreisfindungDto;
	}

	public VerkaufspreisDto getVerkaufspreisDtoZielwaehrung() {
		return verkaufspreisDtoZielwaehrung;
	}

	public void setVerkaufspreisDtoZielwaehrung(
			VerkaufspreisDto verkaufspreisDtoZielwaehrung) {
		this.verkaufspreisDtoZielwaehrung = verkaufspreisDtoZielwaehrung;
	}
}
