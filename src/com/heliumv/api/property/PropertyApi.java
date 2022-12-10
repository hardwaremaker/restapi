package com.heliumv.api.property;

import java.rmi.RemoteException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.heliumv.api.BaseApi;
import com.heliumv.api.HvValidateBadRequest;
import com.heliumv.api.HvValidateNotFound;
import com.heliumv.api.item.ItemIdentityEntry;
import com.heliumv.api.item.ItemPropertyEntry;
import com.heliumv.api.item.ItemPropertyEntryList;
import com.heliumv.api.item.ItemPropertyEntryMapper;
import com.heliumv.api.system.PropertyLayoutEntry;
import com.heliumv.api.system.PropertyLayoutEntryList;
import com.heliumv.factory.IArtikelCall;
import com.heliumv.factory.ILagerCall;
import com.heliumv.factory.IPanelCall;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.system.service.PanelFac;
import com.lp.server.system.service.PanelbeschreibungDto;
import com.lp.server.system.service.PaneldatenDto;

/**
 * Stellt Funktionen f&uuml;r die Verarbeitung von Eigenschaften (eines Artikels)
 * zur Verf&uuml;gung</br> 
 * 
 * <p>Welche Eigenschaften prinzipiell f&uuml;r einen bestimmten Eigenschaftstyp zur 
 * Verf&uuml;gung stehen, kann &uuml;ber die Endpunkte mit der Endung "/layouts"
 * ermittelt werden. Aktuell werden <ul>
 * <li>Artikeleigenschaften (beginnend mit <code>/item/</code> und
 * <li>Chargeneigenschaften (beginnend mit <code>/identity/</code>
 * </ul>
 * unterst&uuml;tzt. Das Ergebnis wird als "Beschreibung (der Eigenschaften)" bezeichnet.</p>
 * <p></p>
 * <p>Artikelbezogenene Eigenschaften ben&ouml;tigen prinzipiel die Id des Artikels
 * (<code>itemid</code>). F&uuml;r die Chargeneigenschaften (die auch Eigenschaften
 * f&uuml;r einen seriennummernbehafteten Artikel inkludieren) muss zus&auml;tzlich
 * die Identit&auml;t (Chargen- bzw. Seriennummer) angegeben werden. Eigenschaften
 * die f&uuml;r eine bestimmte Charge- bzw. Seriennummer gesetzt werden, gelten
 * f&uuml;r die gesamte Charge.</p>
 * @author gerold
 */
@Service("hvProperty")
@Path("/api/v1/property")
public class PropertyApi extends BaseApi implements IPropertyApi {
	@Autowired
	private IArtikelCall artikelCall;
	@Autowired
	private ILagerCall lagerCall;
	@Autowired
	private IPanelCall panelCall;
	
	@Autowired
	private ItemPropertyEntryMapper itempropertyEntryMapper ;

	@Override
	@GET
	@Path("/item/{" + Param.ITEMID + "}/layouts")
	@Produces({FORMAT_JSON, FORMAT_XML})
	public PropertyLayoutEntryList getItemPropertyLayout(
			@PathParam(Param.ITEMID) Integer itemId,
			@QueryParam(Param.USERID) String userId,
			@HeaderParam(ParamInHeader.TOKEN) String headerToken) throws RemoteException {
		PropertyLayoutEntryList layouts = new PropertyLayoutEntryList();
		if (connectClient(headerToken, userId) == null) return layouts;

		HvValidateBadRequest.notNull(itemId, Param.ITEMID);
		ArtikelDto artikelDto = artikelCall.artikelFindByPrimaryKeySmallOhneExc(itemId);
		HvValidateNotFound.notNull(artikelDto, "itemId", itemId);
		 
		layouts.setEntries(getItemGroupPropertyLayoutsImpl(artikelDto.getArtgruIId()));
		return layouts;
	}
	
	private List<PropertyLayoutEntry> getItemGroupPropertyLayoutsImpl(Integer artgruIId) {
		PanelbeschreibungDto[] beschreibungen = panelCall.panelbeschreibungArtikelFindByArtikelgruppeId(artgruIId);
		List<PropertyLayoutEntry> layoutEntries = itempropertyEntryMapper.mapEntry(beschreibungen);

		return layoutEntries;
	}

	@Override
	@GET
	@Path("/identity/{" + Param.ITEMID + "}/layouts")
	@Produces({FORMAT_JSON, FORMAT_XML})
	public PropertyLayoutEntryList getItemIdentityPropertyLayout(
			@PathParam(Param.ITEMID) Integer itemId,
			@QueryParam(Param.USERID) String userId,
			@HeaderParam(ParamInHeader.TOKEN) String headerToken) throws RemoteException {
		PropertyLayoutEntryList layouts = new PropertyLayoutEntryList();
		if (connectClient(headerToken, userId) == null) return layouts;
		
		HvValidateBadRequest.notNull(itemId, Param.ITEMID);
		ArtikelDto artikelDto = artikelCall.artikelFindByPrimaryKeySmallOhneExc(itemId);
		HvValidateNotFound.notNull(artikelDto, "itemId", itemId);
		HvValidateBadRequest.notValid(artikelDto.istArtikelSnrOderchargentragend(), "identity");

		layouts.setEntries(getItemIdentityPropertyLayoutsImpl(artikelDto.getArtgruIId()));
		return layouts;
	}
	
	private List<PropertyLayoutEntry> getItemIdentityPropertyLayoutsImpl(Integer artgruIId) {
		PanelbeschreibungDto[] beschreibungen = panelCall.panelbeschreibungChargenFindByArtikelgruppeId(artgruIId);
		List<PropertyLayoutEntry> layoutEntries = itempropertyEntryMapper.mapEntry(beschreibungen);

		return layoutEntries;
	}
	
	@Override
	@GET
	@Path("/identity/{" + Param.ITEMID + "}/properties")
	@Produces({FORMAT_JSON, FORMAT_XML})
	public ItemPropertyEntryList getItemIdentityProperties(
			@PathParam(Param.ITEMID) Integer itemId,
			@QueryParam(Param.USERID) String userId,
			@HeaderParam(ParamInHeader.TOKEN) String headerToken,
			ItemIdentityEntry identity) throws RemoteException {
		ItemPropertyEntryList entries = new ItemPropertyEntryList();
		if (connectClient(headerToken, userId) == null) return entries;
		
		HvValidateBadRequest.notNull(itemId, Param.ITEMID);
		HvValidateBadRequest.notNull(identity, "identityEntry");
		HvValidateBadRequest.notEmpty(identity.getIdentity(), "identity");
		
		ArtikelDto artikelDto = artikelCall.artikelFindByPrimaryKeySmallOhneExc(itemId);
		HvValidateNotFound.notNull(artikelDto, "itemId", itemId);
		HvValidateBadRequest.notValid(artikelDto
				.istArtikelSnrOderchargentragend(), "identity");

		Integer keyId = lagerCall.artikelSerienChargenNrFindBy(
				artikelDto.getIId(), identity.getIdentity());
		HvValidateNotFound.notNull(keyId, "identity", identity.getIdentity());
		
		PaneldatenDto[] dtos = panelCall.paneldatenFindByPanelCnrKey(
				PanelFac.PANEL_CHARGENEIGENSCHAFTEN, keyId);
		List<ItemPropertyEntry> properties = itempropertyEntryMapper.mapEntry(dtos);
		entries.setEntries(properties);
		return entries;
	}
	
	@Override
	@GET
	@Path("/identity/{" + Param.ITEMID + "}/properties/{" + Param.LAYOUTID +"}")
	@Produces({FORMAT_JSON, FORMAT_XML})
	public ItemPropertyEntry getItemIdentityProperty(
			@PathParam(Param.ITEMID) Integer itemId,
			@QueryParam(Param.USERID) String userId,
			@HeaderParam(ParamInHeader.TOKEN) String headerToken,
			@PathParam(Param.LAYOUTID) Integer layoutId,
			ItemIdentityEntry identity) throws RemoteException {
		ItemPropertyEntry entry = new ItemPropertyEntry();
		if (connectClient(headerToken, userId) == null) return entry;
		
		HvValidateBadRequest.notNull(itemId, Param.ITEMID);
		HvValidateBadRequest.notNull(layoutId, Param.LAYOUTID);
		HvValidateBadRequest.notNull(identity, "identityEntry");
		HvValidateBadRequest.notEmpty(identity.getIdentity(), "identity");
		
		ArtikelDto artikelDto = artikelCall.artikelFindByPrimaryKeySmallOhneExc(itemId);
		HvValidateNotFound.notNull(artikelDto, "itemId", itemId);
		HvValidateBadRequest.notValid(artikelDto
				.istArtikelSnrOderchargentragend(), "identity");

		Integer keyId = lagerCall.artikelSerienChargenNrFindBy(
				artikelDto.getIId(), identity.getIdentity());
		HvValidateNotFound.notNull(keyId, "identity", identity.getIdentity());
		
		PaneldatenDto dto = panelCall.paneldatenFindByPanelCNrPanelbeschreibungIIdCKeyOhneExc(
				PanelFac.PANEL_CHARGENEIGENSCHAFTEN, layoutId, keyId.toString());
		HvValidateNotFound.notNull(dto, Param.LAYOUTID, layoutId);
		
		entry = itempropertyEntryMapper.mapEntry(dto);
		return entry;
	}
	
	@Override
	@POST
	@Path("/identity/{" + Param.ITEMID + "}/properties")
	@Consumes({FORMAT_JSON, FORMAT_XML})
	@Produces({FORMAT_JSON, FORMAT_XML})
	public ItemPropertyEntryList postItemIdentityProperties(
			@PathParam(Param.ITEMID) Integer itemId,
			@QueryParam(Param.USERID) String userId,
			@HeaderParam(ParamInHeader.TOKEN) String headerToken,
			CreateItemIdentityPropertyEntryList properties) throws RemoteException {
		ItemPropertyEntryList entries = new ItemPropertyEntryList();
		if (connectClient(headerToken, userId) == null) return entries;
		
		HvValidateBadRequest.notNull(itemId, Param.ITEMID);
		HvValidateBadRequest.notNull(properties, "propertiesEntry");
		HvValidateBadRequest.notEmpty(properties.getIdentity(), "identity");
		
		ArtikelDto artikelDto = artikelCall.artikelFindByPrimaryKeySmallOhneExc(itemId);
		HvValidateNotFound.notNull(artikelDto, "itemId", itemId);
		HvValidateBadRequest.notValid(artikelDto
				.istArtikelSnrOderchargentragend(), "identity");

		Integer keyId = lagerCall.artikelSerienChargenNrFindBy(
				artikelDto.getIId(), properties.getIdentity());
		HvValidateNotFound.notNull(keyId, "identity", properties.getIdentity());
		
		List<PaneldatenDto> paneldatenDtos = itempropertyEntryMapper.mapDto(keyId.toString(), properties.getEntries());

		panelCall.createPaneldaten(PanelFac.PANEL_CHARGENEIGENSCHAFTEN, 
				paneldatenDtos.toArray(new PaneldatenDto[0]));
		
		PaneldatenDto[] dtos = panelCall.paneldatenFindByPanelCnrKey(
				PanelFac.PANEL_CHARGENEIGENSCHAFTEN, keyId);
		List<ItemPropertyEntry> newProperties = itempropertyEntryMapper.mapEntry(dtos);
		entries.setEntries(newProperties);
		return entries;
	}
	
	@Override
	@PUT
	@Path("/identity/{" + Param.ITEMID + "}/properties")
	@Consumes({FORMAT_JSON, FORMAT_XML})
	@Produces({FORMAT_JSON, FORMAT_XML})
	public ItemPropertyEntryList putItemIdentityProperties(
			@PathParam(Param.ITEMID) Integer itemId,
			@QueryParam(Param.USERID) String userId,
			@HeaderParam(ParamInHeader.TOKEN) String headerToken,
			CreateItemIdentityPropertyEntryList properties) throws RemoteException {
		ItemPropertyEntryList entries = new ItemPropertyEntryList();
		if (connectClient(headerToken, userId) == null) return entries;
		
		HvValidateBadRequest.notNull(itemId, Param.ITEMID);
		HvValidateBadRequest.notNull(properties, "propertiesEntry");
		HvValidateBadRequest.notEmpty(properties.getIdentity(), "identity");
		
		ArtikelDto artikelDto = artikelCall.artikelFindByPrimaryKeySmallOhneExc(itemId);
		HvValidateNotFound.notNull(artikelDto, "itemId", itemId);
		HvValidateBadRequest.notValid(artikelDto
				.istArtikelSnrOderchargentragend(), "identity");

		Integer keyId = lagerCall.artikelSerienChargenNrFindBy(
				artikelDto.getIId(), properties.getIdentity());
		HvValidateNotFound.notNull(keyId, "identity", properties.getIdentity());
		
		List<PaneldatenDto> paneldatenDtos = itempropertyEntryMapper
				.mapDto(keyId.toString(), properties.getEntries());

		for (PaneldatenDto paneldatenDto : paneldatenDtos) {
			/*
			 * PUT sollte urspruenglich nur bereits bestehende Eigenschaften modifizieren. 
			 * Dahingehend geaendert, dass wenn die Eigenschaft nicht existiert, diese automatisch 
			 * angelegt wird.
			 */
			PaneldatenDto existingPaneldatenDto = panelCall
					.paneldatenFindByPanelCNrPanelbeschreibungIIdCKeyOhneExc(
							PanelFac.PANEL_CHARGENEIGENSCHAFTEN, 
							paneldatenDto.getPanelbeschreibungIId(), keyId.toString());
			if (existingPaneldatenDto == null) {
				// updatePanelDaten updatet nur dann, wenn vorhanden
				paneldatenDto.setPanelCNr(PanelFac.PANEL_CHARGENEIGENSCHAFTEN);
				panelCall.createPaneldaten(paneldatenDto);
			}			
		}
		
		panelCall.updatePaneldaten(PanelFac.PANEL_CHARGENEIGENSCHAFTEN, 
				paneldatenDtos.toArray(new PaneldatenDto[0]));
		
		PaneldatenDto[] dtos = panelCall.paneldatenFindByPanelCnrKey(
				PanelFac.PANEL_CHARGENEIGENSCHAFTEN, keyId);
		List<ItemPropertyEntry> newProperties = itempropertyEntryMapper.mapEntry(dtos);
		entries.setEntries(newProperties);
		return entries;
	}
	
	@Override
	@PUT
	@Path("/identity/{" + Param.ITEMID + "}/properties/{" + Param.LAYOUTID +"}")
	@Consumes({FORMAT_JSON, FORMAT_XML})
	@Produces({FORMAT_JSON, FORMAT_XML})
	public ItemPropertyEntryList putItemIdentityProperty(
			@PathParam(Param.ITEMID) Integer itemId,
			@QueryParam(Param.USERID) String userId,
			@HeaderParam(ParamInHeader.TOKEN) String headerToken,
			@PathParam(Param.LAYOUTID) Integer layoutId,
			CreateItemIdentityPropertyEntry property) throws RemoteException {
		ItemPropertyEntryList entries = new ItemPropertyEntryList();
		if (connectClient(headerToken, userId) == null) return entries;
		
		HvValidateBadRequest.notNull(itemId, Param.ITEMID);
		HvValidateBadRequest.notNull(layoutId, Param.LAYOUTID);
		HvValidateBadRequest.notNull(property, "propertiesEntry");
		HvValidateBadRequest.notEmpty(property.getIdentity(), "identity");
		
		ArtikelDto artikelDto = artikelCall.artikelFindByPrimaryKeySmallOhneExc(itemId);
		HvValidateNotFound.notNull(artikelDto, "itemId", itemId);
		HvValidateBadRequest.notValid(artikelDto
				.istArtikelSnrOderchargentragend(), "identity");

		Integer keyId = lagerCall.artikelSerienChargenNrFindBy(
				artikelDto.getIId(), property.getIdentity());
		HvValidateNotFound.notNull(keyId, "identity", property.getIdentity());

		property.setLayoutId(layoutId);
		PaneldatenDto paneldatenDto = itempropertyEntryMapper
				.mapDto(keyId.toString(), property);

		/*
		 * PUT sollte urspruenglich nur bereits bestehende Eigenschaften modifizieren. 
		 * Dahingehend geaendert, dass wenn die Eigenschaft nicht existiert, diese automatisch 
		 * angelegt wird.
		 */
		PaneldatenDto existingPaneldatenDto = panelCall
				.paneldatenFindByPanelCNrPanelbeschreibungIIdCKeyOhneExc(
						PanelFac.PANEL_CHARGENEIGENSCHAFTEN, layoutId, keyId.toString());
		if (existingPaneldatenDto != null) {
			panelCall.updatePaneldaten(PanelFac.PANEL_CHARGENEIGENSCHAFTEN, 
					new PaneldatenDto[] {paneldatenDto});			
		} else {
			paneldatenDto.setPanelCNr(PanelFac.PANEL_CHARGENEIGENSCHAFTEN);
			panelCall.createPaneldaten(paneldatenDto);
		}
		
		PaneldatenDto dto = panelCall.paneldatenFindByPanelCNrPanelbeschreibungIIdCKeyOhneExc(
				PanelFac.PANEL_CHARGENEIGENSCHAFTEN, layoutId, keyId.toString());
		
		List<ItemPropertyEntry> newProperties = itempropertyEntryMapper.mapEntry(
				new PaneldatenDto[] {dto});
		entries.setEntries(newProperties);
		return entries;
	}	
}
