package com.heliumv.api.todo;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.heliumv.api.BaseApi;
import com.heliumv.api.LpClientText;
import com.heliumv.api.hvma.IHvmaService;
import com.heliumv.api.order.IOrderService;
import com.heliumv.api.order.OrderEntry;
import com.heliumv.api.project.IProjectService;
import com.heliumv.api.project.ProjectEntry;
import com.heliumv.factory.IArtikelCall;
import com.heliumv.factory.IAuftragCall;
import com.heliumv.factory.IAuftragpositionCall;
import com.heliumv.factory.IAuftragreportCall;
import com.heliumv.factory.IFertigungCall;
import com.heliumv.factory.IHvmaCall;
import com.heliumv.factory.IKundeCall;
import com.heliumv.factory.IMandantCall;
import com.heliumv.factory.IPartnerCall;
import com.heliumv.factory.IProjektCall;
import com.heliumv.factory.ISystemCall;
import com.heliumv.factory.ISystemMultilanguageCall;
import com.heliumv.tools.ShortHelper;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikelsprDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LossollarbeitsplanDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.personal.service.HvmaLizenzEnum;
import com.lp.server.personal.service.HvmaRechtEnum;
import com.lp.server.personal.service.HvmarechtDto;
import com.lp.server.projekt.service.HistoryDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;
import com.lp.util.HVPDFExporter;
import com.lp.util.barcode.HvBarcodeDecoder;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;

@Service("hvTodo")
@Path("/api/v1/todo/")
public class TodoApi extends BaseApi implements ITodoApi {
	private static Logger log = LoggerFactory.getLogger(TodoApi.class);
	
	@Autowired
	private IProjectService projectService ;
	@Autowired
	private IOrderService orderService ;
	@Autowired
	private IProjektCall projektCall;
	@Autowired
	private IAuftragpositionCall auftragpositionCall;
	@Autowired
	private IArtikelCall artikelCall;
	@Autowired
	private IMandantCall mandantCall;
	@Autowired
	private ISystemCall systemCall;
	@Autowired
	private IFertigungCall fertigungCall;
	@Autowired
	private IKundeCall kundeCall;
	@Autowired
	private IAuftragCall auftragCall;
	@Autowired
	private IPartnerCall partnerCall;
	@Autowired
	private IHvmaCall hvmaCall;
	@Autowired
	private IAuftragreportCall auftragreportCall;
	@Autowired 
	private LpClientText lpclientText;
	@Autowired
	private ISystemMultilanguageCall systemMultilanguageCall;
	@Autowired
	private IHvmaService hvmaService;
	
	@GET
	@Produces({FORMAT_JSON, FORMAT_XML})
	public TodoEntryList getTodos(
			@QueryParam(Param.USERID) String userId,
			@QueryParam(Param.LIMIT) Integer limit,
			@QueryParam(Param.STARTINDEX) Integer startIndex,
			@QueryParam("filter_partnername") String filterPartnerName) throws NamingException, RemoteException {
		if(connectClient(userId) == null) return new TodoEntryList();
		validateApi();

		Integer serverId = systemCall.getServerId();
		if(serverId == null) {
			respondExpectationFailed();
			return new TodoEntryList();
		}
	
		List<TodoEntry> entries = getProductions();
//		entries.addAll(getProjects(limit, filterPartnerName)) ;
		entries.addAll(getProjects());
//		entries.addAll(getOrders(limit, filterPartnerName));
		entries.addAll(getOrders());
		HvBarcodeDecoder decoder = systemCall.createHvBarcodeDecoder();
		
		TodoEntryList l = new TodoEntryList(entries, serverId, decoder);
		l.setHvmaEnums(hvmaService.mobilePrivileges());
		l.setHvmaParams(hvmaService.mobileParameters());
		l.setJudgeEnums(hvmaCall.getRechte());

		return l;
	}
	
	private void validateApi() throws NamingException, RemoteException {
		if(mandantCall.hasFunctionHvma2()) return;
		if(mandantCall.hasFunctionHvmaZeiterfassung()) return;
		
		throw new EJBExceptionLP(
				EJBExceptionLP.FEHLER_BERECHTIGUNG_ZUSATZFUNKTION,
				MandantFac.ZUSATZFUNKTION_HVMA2) ;
	}
	
	private List<TodoEntry> getProductions() throws RemoteException, NamingException {
		List<LosDto> losDtos = fertigungCall.losFindOffeneByMe(
				hvmaCall.getBelegStatusLos(), hvmaCall.getZielterminLos());
		List<TodoEntry> todos = transformProductions(losDtos);
		return todos;
	}
	
	private List<TodoEntry> transformProductions(List<LosDto> losDtos) throws NamingException, RemoteException {
		List<TodoEntry> todoEntries = new ArrayList<TodoEntry>();
		for (LosDto losDto : losDtos) {
			todoEntries.add(transformProduction(losDto)) ;
		}
		return todoEntries;		
	}
	
	private TodoEntry transformProduction(LosDto losDto) throws NamingException, RemoteException {
		TodoEntry entry = new TodoEntry();
		entry.setId(losDto.getIId());
		entry.setCnr(losDto.getCNr());
		entry.setType(TodoEntryType.PRODUCTION);
		entry.setDueDateMs(losDto.getTProduktionsende().getTime());
		entry.setTitle(losDto.getCProjekt());
		entry.setComment(losDto.getCKommentar());
		if(entry.getPartnerName() == null && losDto.getAuftragIId() != null) {
			AuftragDto auftragDto = auftragCall
					.auftragFindByPrimaryKeyOhneExc(losDto.getAuftragIId());
			if(auftragDto != null) {
				KundeDto kundeDto = kundeCall.kundeFindByPrimaryKeyOhneExc(
						auftragDto.getKundeIIdAuftragsadresse());
				entry.setPartnerName(kundeDto.getPartnerDto().formatName());
				entry.setOrderCnr(auftragDto.getCNr());
	
				entry.setFormattedDeliveryAddress(formatAddress(auftragDto));
			}
		}

		if(entry.getPartnerName() == null && losDto.getKundeIId() != null) {
			KundeDto kundeDto = kundeCall.kundeFindByPrimaryKeyOhneExc(losDto.getKundeIId());
			entry.setPartnerName(kundeDto.getPartnerDto().formatName());
			
			entry.setFormattedDeliveryAddress(formatAddress(kundeDto.getIId(), null));
		}
		
		if(losDto.getPartnerIIdFertigungsort() != null) {
			PartnerDto partnerDto = partnerCall
					.partnerFindByPrimaryKey(losDto.getPartnerIIdFertigungsort());
			entry.setManufacturingPlace(partnerDto.formatTitelAnrede());
		}
		entry.setStartDateMs(losDto.getTProduktionsbeginn().getTime());
		entry.setDetails(new TodoDetailEntryList(transformProductionDetails(losDto)));
		return entry;
	}
	
	private String formatAddress(AuftragDto auftragDto) throws RemoteException, NamingException {
		Integer lieferKundeId = auftragDto.getKundeIIdLieferadresse();
		Integer lieferAnsprechpartnerId = auftragDto.getAnsprechpartnerIIdLieferadresse();
	
		if(lieferKundeId == null) {
			return "";
		}
		
		return formatAddress(lieferKundeId, lieferAnsprechpartnerId);
	}
	
	private String formatAddress(Integer kundeId, 
			Integer ansprechpartnerId) throws RemoteException, NamingException {
		return auftragCall.formatAddress(kundeId, ansprechpartnerId);
	}
	
	private List<TodoDetailEntry> transformProductionDetails(LosDto losDto) throws RemoteException {
		List<TodoDetailEntry> entries = new ArrayList<TodoDetailEntry>();
		LossollarbeitsplanDto[] arbeitsplanDtos = fertigungCall
				.lossollarbeitsplanFindByLosIId(losDto.getIId());
		for (LossollarbeitsplanDto arbeitsplanDto : arbeitsplanDtos) {		
// PJ20723 auch fertige uebertragen
//			if(ShortHelper.isSet(arbeitsplanDto.getBFertig())) continue;
			if(ShortHelper.isSet(arbeitsplanDto.getBNurmaschinenzeit())) continue;
			
			ArtikelDto itemDto = artikelCall.artikelFindByPrimaryKeySmall(
					arbeitsplanDto.getArtikelIIdTaetigkeit());
			entries.add(
					transformProductionPersonalIdent(losDto, arbeitsplanDto, itemDto));
		}
		return entries;
	}
	
	private TodoDetailEntry transformProductionPersonalIdent(
			LosDto losDto, LossollarbeitsplanDto arbeitsplanDto, ArtikelDto artikelDto) throws RemoteException {
		TodoDetailIdentEntry entry = new TodoDetailIdentEntry(arbeitsplanDto.getIId());
		entry.setItemId(arbeitsplanDto.getArtikelIIdTaetigkeit());
		entry.setItemCnr(artikelDto.getCNr());
		entry.setAmount(arbeitsplanDto.getNGesamtzeit());
		entry.setUnitCnr(artikelDto.getEinheitCNr());
		ArtikelsprDto artikelsprDto = artikelCall
				.artikelSprFindByArtikelIIdOhneExc(arbeitsplanDto.getArtikelIIdTaetigkeit());
		entry.setDescription(artikelsprDto.getCBez());
		entry.setDescription2(artikelsprDto.getCZbez());
		entry.setRecordable(
				ArtikelFac.ARTIKELART_ARBEITSZEIT.equals(artikelDto.getArtikelartCNr()));
		entry.setDocumentObligation(artikelDto.getBDokumentenpflicht() > 0);
		entry.setDueDateMs(losDto.getTProduktionsende().getTime());

		if(arbeitsplanDto.getFFortschritt() != null) {
			entry.setProgressPercent(new BigDecimal(arbeitsplanDto.getFFortschritt().toString()));
		}
		entry.setPositionNr(arbeitsplanDto.getIArbeitsgangnummer().toString());
		if(arbeitsplanDto.getIUnterarbeitsgang() != null) {
			entry.setSubPositionNr(arbeitsplanDto.getIUnterarbeitsgang().toString());
		}
		entry.setDone(ShortHelper.isSet(arbeitsplanDto.getBFertig()));
		return entry;
	}
	
	private List<TodoEntry> getProjects(Integer limit, 
			String filterPartnerName) throws RemoteException, NamingException {
		List<ProjectEntry> entries = projectService.getProjects(
				limit, null, null, filterPartnerName, Boolean.TRUE, Boolean.FALSE);
		return transformProjects(entries);
	}
	
	private List<TodoEntry> getProjects() throws RemoteException, NamingException {
		List<ProjectEntry> entries = projectService
				.getMyProjects(hvmaCall.getZielterminProjekt(), 
						hvmaCall.getBelegStatusProjekt());
		return transformProjects(entries);
	}
	
	private List<TodoEntry> transformProjects(List<ProjectEntry> projectEntries) {
		List<TodoEntry> todoEntries = new ArrayList<TodoEntry>();
		for (ProjectEntry projectEntry : projectEntries) {
			todoEntries.add(transformProject(projectEntry)) ;
		}
		return todoEntries;
	}
	
	private TodoEntry transformProject(ProjectEntry projectEntry) {
		TodoEntry entry = new TodoEntry();
		entry.setId(projectEntry.getId());
		entry.setCnr(projectEntry.getCnr());
		entry.setType(TodoEntryType.PROJECT);
		entry.setPartnerName(projectEntry.getCustomerName());
		entry.setTitle(projectEntry.getTitle());
		entry.setDueDateMs(projectEntry.getDeadlineMs());
		entry.setComment(projectEntry.getInternalComment());
		
		entry.setDetails(new TodoDetailEntryList(transformProjectDetails(projectEntry)));
		return entry ;
	}
	
	private List<TodoDetailEntry> transformProjectDetails(ProjectEntry projectEntry) {
		HistoryDto[] historyDtos = projektCall.historyFindByProjektIid(projectEntry.getId());
		List<TodoDetailEntry> entries = new ArrayList<TodoDetailEntry>();
		
		for (HistoryDto historyDto : historyDtos) {
			TodoDetailProjectEntry entry = new TodoDetailProjectEntry(historyDto.getIId());
			entry.setText(historyDto.getXText());
			entry.setSubject(historyDto.getCTitel());
			entry.setHtml(historyDto.getBHtml() > 0);
			entry.setTimeMs(historyDto.getTBelegDatum().getTime());
			
			entries.add(entry);
		}

		return entries;
	}
	
	private List<TodoEntry> getOrders(Integer limit, String filterPartnerName) 
			throws RemoteException, NamingException {
		List<OrderEntry> entries = orderService.getOfflineOrders(limit, 
				null, null, filterPartnerName, null, null, Boolean.TRUE, null) ;
		return transformOrders(entries) ;
	}
	
	private List<TodoEntry> getOrders() throws RemoteException, NamingException {
		List<OrderEntry> entries = orderService.getMyOrders(
				hvmaCall.getZielterminAuftrag(), hvmaCall.getBelegStatusAuftrag());
		return transformOrders(entries) ;
	}
	
	private List<TodoEntry> transformOrders(List<OrderEntry> orderEntries) 
			throws RemoteException, NamingException {
		List<HvmarechtDto> rechte = hvmaCall
				.getHvmaRechte(HvmaLizenzEnum.Offline);
		boolean hasPackliste = hasHvmaRecht(rechte, HvmaRechtEnum.Packliste);
		boolean hasKommissionierliste = hasHvmaRecht(rechte, HvmaRechtEnum.Kommissionierliste);
	
		log.info("Working on '" + orderEntries.size() + "' order entries...");
		List<TodoEntry> todoEntries = new ArrayList<TodoEntry>();
		for(OrderEntry orderEntry : orderEntries) {
			log.info("Transform order '" + orderEntry.getCnr() + "'...");
			TodoEntry todoEntry = transformOrder(orderEntry);
			todoEntries.add(todoEntry);
			try {
				if(hasPackliste) {
					log.info("Transform packlist '" + orderEntry.getCnr() + "'...");
					todoEntry.getDocuments().add(producePackliste(orderEntry));
					log.info("Transform packlist '" + orderEntry.getCnr() + "' done");
				}
				if(hasKommissionierliste) {	
					log.info("Transform kommissionierlist '" + orderEntry.getCnr() + "'...");
					todoEntry.getDocuments().add(produceKommissionierliste(orderEntry));
					log.info("Transform kommissionierlist '" + orderEntry.getCnr() + "' done");
				}			
			} catch(JRException e) {
				log.error("printing failed for orderEntry '" + 
						orderEntry.getId() + "' (cnr: " + orderEntry.getCnr() + ")", e);
			}
		}
		log.info("Working on '" + orderEntries.size() + "' order entries done.");
		return todoEntries ;
	}
	
	private TodoDocumentEntry producePackliste(OrderEntry orderEntry) throws RemoteException, JRException {
		try {
			JasperPrintLP printLp = auftragreportCall.printAuftragPackliste(orderEntry.getId());
			return produceDocumentEntry(orderEntry, HvmaRechtEnum.Packliste.toString(), printLp);		
		} catch(Exception e) {
			log.error("packliste...", e);
			throw e;
		}
	}
	
	private TodoDocumentEntry produceKommissionierliste(OrderEntry orderEntry) throws RemoteException, JRException { 
		JasperPrintLP printLp = auftragreportCall.printKommissionierung(orderEntry.getId());
		String s = systemMultilanguageCall.getTextRespectUISpr("auft.menu.datei.kommissionierung");
		if(s == null || s.startsWith("resourcebundletext fehlt")) {
			 s = lpclientText.get("auft.menu.datei.kommissionierung");			
		}
		TodoDocumentEntry document = produceDocumentEntry(orderEntry, 
				HvmaRechtEnum.Kommissionierliste.toString(), printLp);
		document.setDescription(s);
		return document;
	}
	
	private TodoDocumentEntry produceDocumentEntry(
			OrderEntry orderEntry, String documentType, JasperPrintLP print) throws JRException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream(100000) ;
		HVPDFExporter exporter = new HVPDFExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, print.getPrint());
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);

		exporter.exportReport();
		TodoDocumentEntry docEntry = new TodoDocumentEntry();
		docEntry.setContent(baos.toByteArray());
		docEntry.setMimeType("pdf");
		docEntry.setDocumentType(documentType);
		docEntry.setName(orderEntry.getCnr());
		return docEntry;
	}
	
	private boolean hasHvmaRecht(List<HvmarechtDto> rechte, HvmaRechtEnum recht) {
		for (HvmarechtDto hvmarechtDto : rechte) {
			if(recht.equals(hvmarechtDto.getCnrAsEnum())) return true;
		}
		return false;
	}
	
	private TodoEntry transformOrder(OrderEntry orderEntry) throws RemoteException, NamingException {
		TodoEntry entry = new TodoEntry() ;	
		entry.setId(orderEntry.getId());
		entry.setCnr(orderEntry.getCnr());
		entry.setType(TodoEntryType.ORDER);
		entry.setPartnerName(orderEntry.getCustomerName());
		entry.setTitle(orderEntry.getProjectName());
		entry.setDueDateMs(orderEntry.getDeliveryDateMs());
		entry.setComment(orderEntry.getInternalCommentText());

		// TODO: Uebergangsweise durch erneutes Holen der Daten
		// Sinnvoller waere, den Handler entsprechend zu erweitern.
		AuftragDto auftragDto = auftragCall.auftragFindByPrimaryKeyOhneExc(orderEntry.getId());
		entry.setFormattedDeliveryAddress(formatAddress(auftragDto));
		
		entry.setDetails(new TodoDetailEntryList(transformOrderDetails(orderEntry)));
		return entry ;
	}
	
	private List<TodoDetailEntry> transformOrderDetails(OrderEntry orderEntry) throws RemoteException {
		List<TodoDetailEntry> srcEntries = buildOrderDetailEntries(orderEntry);
		
		List<TodoDetailEntry> entries = new ArrayList<TodoDetailEntry>();
		for (TodoDetailEntry entry : srcEntries) {
			if(entry.getDetailType().equals(TodoDetailEntryType.ZWS)) {
				entries.add(((TodoDetailZwsEntry)entry).asTextEntry());
			} else {
				entries.add(entry);
			}
		}
		return entries;
	}
	

	private List<TodoDetailEntry> buildOrderDetailEntries(OrderEntry orderEntry) throws RemoteException {
		List<TodoDetailEntry> entries = new ArrayList<TodoDetailEntry>();
		
		AuftragpositionDto[] positionDtos = auftragpositionCall.auftragpositionFindByAuftrag(orderEntry.getId());
		List<AuftragpositionDto> srcDtos = filterAuftragpositions(positionDtos);
		for (AuftragpositionDto positionDto : srcDtos) {
			if(!positionDto.isUebertragen()) continue;
			
			if(positionDto.isIdent()) {
				ArtikelDto itemDto = artikelCall.artikelFindByPrimaryKeySmall(positionDto.getArtikelIId());
				entries.add(transformOrderPositionIdent(positionDto, itemDto));
				continue;
			}
			
			if(positionDto.isHandeingabe()) {
				ArtikelDto itemDto = artikelCall.artikelFindByPrimaryKeySmall(positionDto.getArtikelIId());
				entries.add(transformOrderPositionManual(positionDto, itemDto));
				continue;
			}
			
			if(positionDto.isTexteingabe()) {
				entries.add(transformOrderPositionText(positionDto));
				continue;
			}
			
			if(positionDto.isIntelligenteZwischensumme()) {
				int posIndex = findPositionIndex(positionDto.getZwsVonPosition(), entries);
				if(posIndex == -1) {
					posIndex = findNextPositionIndex(
							positionDto.getZwsVonPosition(),
							positionDto.getIId(), srcDtos, entries);
				}
				if(posIndex > -1) {
					TodoDetailZwsEntry entry = transformOrderPositionZws(positionDto);
					addZwsEntry(entries, posIndex, entry);
					continue;
				}
			}
		}

		return entries;		
	}
	
	private int findNextPositionIndex(Integer zwsVonPosition, Integer zwsPosition, List<AuftragpositionDto> srcDtos,
			List<TodoDetailEntry> entries) {
		int posIndex = -1;
		for(int i = 0; i < srcDtos.size(); i++) {
			if(zwsVonPosition.equals(srcDtos.get(i).getIId())) {
				posIndex = i;
				break;
			}
		}
		if(posIndex == -1) return posIndex;
		
		// Die naechste(n) AB-Pos finden, die in den entries vorhanden sind
		while(!zwsPosition.equals(srcDtos.get(++posIndex).getIId())) {
			int index = findPositionIndex(srcDtos.get(posIndex).getIId(), entries);
			if(index != -1) return index;
		}

		// Alle Position der Zwischensumme wurden nicht uebertragen, dann bringt 
		// die Uebertragung der Zwsposition auch nichts.!?
		return -1;
	}

	private void addZwsEntry(List<TodoDetailEntry> entries, int posIndex, TodoDetailZwsEntry newEntry) {
		while(posIndex > 0) {
			// Mehrfach verschachtelte Zws auf die gleiche Von-Position.
			// Sicherstellen, dass die einzufuegende die vorderste ist
			if(entries.get(posIndex - 1).getDetailType().equals(TodoDetailEntryType.ZWS)) {
				posIndex--;
			} else {
				break;
			}
		}
	
		entries.add(posIndex, newEntry);		
	}
	
	private List<AuftragpositionDto> filterAuftragpositions(AuftragpositionDto[] positionDtos) {
		List<AuftragpositionDto> positions = new ArrayList<AuftragpositionDto>();
		for(AuftragpositionDto posDto : positionDtos) {
			// Die wollen wir nie
			if(posDto.isErledigt() || posDto.isStorniert()) continue;
		
			// Die, die wir wollen
			if(posDto.isIdent() || posDto.isHandeingabe() ||
					posDto.isTexteingabe() || posDto.isIntelligenteZwischensumme()) {
				positions.add(posDto);
				continue;
			}
			
			// anderes wollen wir nicht
		}
		
		return positions;
	}	
	
	private int findPositionIndex(Integer positionId, List<TodoDetailEntry> detailEntries) {
		for(int i = 0; i < detailEntries.size(); i++ ) {
			if(positionId.equals(detailEntries.get(i).getId())) {
				return i;
			}
		}
		return -1;
	}
	
	private TodoDetailIdentEntry transformOrderPositionIdent(
			AuftragpositionDto positionDto, ArtikelDto artikelDto) throws RemoteException {
		TodoDetailIdentEntry entry = new TodoDetailIdentEntry(positionDto.getIId());
		entry.setItemId(positionDto.getArtikelIId());
		entry.setItemCnr(artikelDto.getCNr());
		entry.setAmount(positionDto.getNMenge());
		entry.setUnitCnr(positionDto.getEinheitCNr());
		entry.setDueDateMs(positionDto.getTUebersteuerbarerLiefertermin().getTime());
		if(positionDto.getBArtikelbezeichnunguebersteuert() > 0) {
			entry.setDescription(positionDto.getCBez());
			entry.setDescription2(positionDto.getCZusatzbez());			
		} else {
			ArtikelsprDto artikelsprDto = artikelCall
					.artikelSprFindByArtikelIIdOhneExc(positionDto.getArtikelIId());
			entry.setDescription(artikelsprDto.getCBez());
			entry.setDescription2(artikelsprDto.getCZbez());
		}
		entry.setRecordable(
				ArtikelFac.ARTIKELART_ARBEITSZEIT.equals(artikelDto.getArtikelartCNr()));
		entry.setDocumentObligation(artikelDto.getBDokumentenpflicht() > 0);
		entry.setTravelInfo(TodoDetailTravelEnum.lookup(artikelDto.getIPassiveReisezeit()));
		return entry;
	}

	private TodoDetailManualEntry transformOrderPositionManual(
			AuftragpositionDto positionDto, ArtikelDto artikelDto) throws RemoteException {
		TodoDetailManualEntry entry = new TodoDetailManualEntry(positionDto.getIId());
		entry.setItemId(positionDto.getArtikelIId());
		entry.setItemCnr(artikelDto.getCNr());
		entry.setAmount(positionDto.getNMenge());
		entry.setUnitCnr(positionDto.getEinheitCNr());
		entry.setDueDateMs(positionDto.getTUebersteuerbarerLiefertermin().getTime());
		
		if(positionDto.getBArtikelbezeichnunguebersteuert() > 0) {
			entry.setDescription(positionDto.getCBez());
			entry.setDescription2(positionDto.getCZusatzbez());			
		} else {
			ArtikelsprDto artikelsprDto = artikelCall.artikelSprFindByArtikelIIdOhneExc(positionDto.getArtikelIId());
			entry.setDescription(artikelsprDto.getCBez());
			entry.setDescription2(artikelsprDto.getCZbez());
		}
		entry.setRecordable(false);
		entry.setDocumentObligation(false);
		return entry;
	}
	
	private TodoDetailTextEntry transformOrderPositionText(AuftragpositionDto positionDto) {
		TodoDetailTextEntry entry = new TodoDetailTextEntry(positionDto.getIId());
		entry.setText(positionDto.getXTextinhalt());
		return entry;
	}
	
	private TodoDetailZwsEntry transformOrderPositionZws(AuftragpositionDto positionDto) {
		TodoDetailZwsEntry entry = new TodoDetailZwsEntry(positionDto.getIId());
		entry.setText(positionDto.getCBez());
		return entry;
	}
}
