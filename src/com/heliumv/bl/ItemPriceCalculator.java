package com.heliumv.bl;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;

import com.heliumv.factory.IArtikelCall;
import com.heliumv.factory.IMandantCall;
import com.heliumv.factory.IMaterialCall;
import com.heliumv.factory.IParameterCall;
import com.heliumv.factory.IVkPreisfindungCall;
import com.lp.client.frame.ExceptionLP;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.VerkaufspreisDto;
import com.lp.server.artikel.service.VkpfMengenstaffelDto;
import com.lp.server.artikel.service.VkpreisfindungDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.util.Validator;
import com.lp.util.Helper;

public class ItemPriceCalculator {
//	@Autowired
//	private IKundeCall kundeCall ;
	@Autowired
	private IArtikelCall artikelCall ;
	@Autowired
	private IParameterCall parameterCall ;
	@Autowired
	private IMandantCall mandantCall ;
	@Autowired
	private IVkPreisfindungCall vkPreisfindungCall ;
	@Autowired
	private IMaterialCall materialCall ;
	
	private MwstsatzDto mwstSatzDto ;
	private Timestamp timestamp ;
	private Date timestampAsDate ;
//	private Double wechselkurs ;
	private String waehrungCnr ;
	
	private Integer kundenStandardPreislisteId  ;
	
	private IItemPriceCalculatorValidation validator ;
	
	public ItemPriceCalculator() {
	}
	
	public ItemPriceCalculator(IItemPriceCalculatorValidation validator) {
		this.validator = validator ;
	}
	
	protected IItemPriceCalculatorValidation getValidator() {
		return validator ;
	}
	
//	protected ItemPriceCalculationResult calculate(Integer customerId, Integer itemId, BigDecimal amount) throws RemoteException, ExceptionLP {
//		Validator.notNull(customerId, "customerId") ;
//		Validator.notNull(itemId, "itemId") ;
//		
//		validator.setKundeDto(kundeCall.kundeFindByPrimaryKeyOhneExc(customerId)) ;
//		validator.setArtikelDto(artikelCall.artikelFindByPrimaryKeySmallOhneExc(itemId));
//		setMwstSatzDto(null);
//		
//		return calculate(amount) ;
//	}
	
	public ItemPriceCalculationResult calculate(Integer itemId, BigDecimal amount) throws RemoteException  {
		Validator.notNull(itemId, "itemId") ;
		validator.setArtikelDto(artikelCall.artikelFindByPrimaryKeySmallOhneExc(itemId));
		
		return calculate(amount) ;
	}

	public ItemPriceCalculationResult calculate(BigDecimal amount) throws RemoteException  {
		Validator.notNull(getKundeDto(), "customerId");
		Validator.notNull(getArtikelDto(), "itemId");
		Validator.notNull(amount, "amount");
//		Validator.notNull(getWechselkurs(), "wechselkurs");
		Validator.notNull(getTimestamp(), "timestamp") ;
		Validator.notNull(getWaehrungCnr(), "waehrungCnr") ;
		
		presetMwstSatz() ;
		Validator.notNull(getMwstSatzDto(), "mwstsatzDto") ;
		
		presetKundenpreisliste() ;
		// presetMengenstaffeln(amount) ;
				
		return calculateImpl(amount) ;
	}

	protected ItemPriceCalculationResult calculateImpl(BigDecimal amount) throws RemoteException {
 		VkpreisfindungDto vkpreisfindungDto = vkPreisfindungCall 
				.verkaufspreisfindung(getArtikelDto().getIId(),
						getKundeDto().getIId(), amount,
						getTimestampAsDate(),
						getKundenStandardPreislisteId(), getMwstSatzDto().getIId(), getWaehrungCnr()) ;	
 		VerkaufspreisDto vkZielDto = calculateVerkaufspreisDto(vkpreisfindungDto) ;
 		ItemPriceCalculationResult result = new ItemPriceCalculationResult(vkpreisfindungDto) ;
		result.setVerkaufspreisDtoZielwaehrung(vkZielDto);
		return result ;
	}
	
	protected VerkaufspreisDto calculateVerkaufspreisDto(VkpreisfindungDto vkpreisfindungDto) {
		VerkaufspreisDto vkZielDto = Helper.getVkpreisBerechnet(vkpreisfindungDto) ;
 		if(vkZielDto == null) {
 			vkZielDto = new VerkaufspreisDto() ;

 			if(getArtikelDto().getMaterialIId() != null) {
 				BigDecimal materialZuschlag = 
 						materialCall.getMaterialzuschlagVKInZielwaehrung(
 								getArtikelDto().getIId(), getTimestampAsDate(), getWaehrungCnr()) ;
 				if(materialZuschlag != null) {
 					vkZielDto.bdMaterialzuschlag = materialZuschlag  ;
 					vkZielDto.nettopreis = materialZuschlag ;
 				}
 			}
 		}
 		
 		return vkZielDto ;
	}

	protected void presetMwstSatz() throws RemoteException {
		Integer mwstsatzbezId = isArtikelBestimmtMwstSatz() 
				? getArtikelDto().getMwstsatzbezIId() : getKundeDto().getMwstsatzbezIId();
		setMwstSatzDto(mandantCall.mwstsatzDtoZuDatum(mwstsatzbezId, getTimestamp())) ;
	}

	protected void presetKundenpreisliste() throws RemoteException {
		if(validator.hasFailedValidations()) return ;
		
		// VKPF Stufe 1: Standardpreisliste des Kunden bestimmen, ist eine
		// Eigenschaft des Kunden und darf nicht null sein
		Integer preislisteId = getKundeDto().getVkpfArtikelpreislisteIIdStdpreisliste();
		if(getValidator().isValidKundenPreislisteId(preislisteId)) {
			kundenStandardPreislisteId = preislisteId ; 
		}
	}

	protected void presetMengenstaffeln(BigDecimal amount) throws RemoteException, ExceptionLP {
		// VKPF Stufe 2: die passende Mengenstaffel vorbelegen
		VkpfMengenstaffelDto[] aVkpfMengenstaffelDtos = 
				vkPreisfindungCall.vkpfMengenstaffelFindByArtikelIIdGueltigkeitsdatum(
						getArtikelDto().getIId(),
						getTimestampAsDate(), kundenStandardPreislisteId);

		// die passende Mengenstaffel
		VkpfMengenstaffelDto vkpfMengenstaffelDtoPassend = 
				vkPreisfindungCall.vkpfMengenstaffelFindByArtikelIIdNMengeGueltigkeitsdatum(
						getArtikelDto().getIId(),
						amount, getTimestampAsDate(),kundenStandardPreislisteId);

		// PJ 09/0013865
//		if (vkpfMengenstaffelDtoPassend != null)
//			if (vkpfMengenstaffelDtoPassend.getNArtikelfixpreis() != null) {
//				wnfNettopreis.getWrbFixNumber().setSelected(true);
//			}

		if (aVkpfMengenstaffelDtos != null && aVkpfMengenstaffelDtos.length > 0
				&& vkpfMengenstaffelDtoPassend != null) {
			for (int i = 0; i < aVkpfMengenstaffelDtos.length; i++) {
				if (aVkpfMengenstaffelDtos[i].getIId().intValue() == vkpfMengenstaffelDtoPassend
						.getIId().intValue()) {
					Integer vkStaffelmengeIId = aVkpfMengenstaffelDtos[i].getIId();
				}
			}
		}
	}
	
	
	private boolean isArtikelBestimmtMwstSatz() throws RemoteException {
		return parameterCall.isArtikelBestimmtMwstsatz() ;
	}
	
	public KundeDto getKundeDto() {
		return validator.getKundeDto() ;
	}

	protected void setKundeDto(KundeDto kundeDto) {
		validator.setKundeDto(kundeDto);
	}

	public ArtikelDto getArtikelDto() {
		return validator.getArtikelDto() ;
	}

	protected void setArtikelDto(ArtikelDto artikelDto) {
		validator.setArtikelDto(artikelDto) ;
	}

	public MwstsatzDto getMwstSatzDto() {
		return mwstSatzDto;
	}

	protected void setMwstSatzDto(MwstsatzDto mwstSatzDto) {
		this.mwstSatzDto = mwstSatzDto;
	}

	/**
	 * Zeit zu der der Artikel berechnet werden soll</br>
	 * <p>&Uuml;blicherweise ist das das Belegdatum</p>
	 * 
	 * @return die Zeit/Timestamp zu der der Artikel berechnet werden soll.
	 */
	public Timestamp getTimestamp() {
		return timestamp;
	}

	public Date getTimestampAsDate() {
		if(timestampAsDate == null) {
			timestampAsDate = new Date(timestamp.getTime()) ;
		}
		
		return timestampAsDate ;
	}
	
	protected void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
		this.timestampAsDate = null ;
	}

//	public Double getWechselkurs() {
//		return wechselkurs;
//	}
//
//	protected void setWechselkurs(Double wechselkurs) {
//		this.wechselkurs = wechselkurs;
//	}
	
	protected Integer getKundenStandardPreislisteId() {
		return kundenStandardPreislisteId ;
	}

	public String getWaehrungCnr() {
		return waehrungCnr;
	}

	protected void setWaehrungCnr(String waehrungCnr) {
		this.waehrungCnr = waehrungCnr;
	}
}
