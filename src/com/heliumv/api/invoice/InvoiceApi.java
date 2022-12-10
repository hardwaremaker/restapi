package com.heliumv.api.invoice;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.heliumv.api.BaseApi;
import com.heliumv.api.HvValidateBadRequest;
import com.heliumv.api.HvValidateNotFound;
import com.heliumv.api.customer.CustomerService;
import com.heliumv.factory.IFinanzCall;
import com.heliumv.factory.IGlobalInfo;
import com.heliumv.factory.IJCRDocCall;
import com.heliumv.factory.IJudgeCall;
import com.heliumv.factory.IKundeCall;
import com.heliumv.factory.IParameterCall;
import com.heliumv.factory.IPartnerCall;
import com.heliumv.factory.IRechnungCall;
import com.heliumv.factory.query.InvoiceQuery;
import com.heliumv.tools.CollectionTools;
import com.heliumv.tools.FilterKriteriumCollector;
import com.heliumv.tools.IDetect;
import com.heliumv.tools.StringHelper;
import com.lp.server.finanz.service.BankverbindungDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.rechnung.service.RechnungHandlerFeature;
import com.lp.server.rechnung.service.RechnungzahlungDto;
import com.lp.server.system.jcr.service.JCRDocDto;
import com.lp.server.system.jcr.service.JCRDocFac;
import com.lp.server.system.jcr.service.docnode.DocNodeFile;
import com.lp.server.system.jcr.service.docnode.DocNodeRechnungZahlung;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.service.ArbeitsplatzDto;
import com.lp.server.system.service.ArbeitsplatzkonfigurationDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.util.fastlanereader.service.query.QueryParametersFeatures;
import com.lp.server.util.fastlanereader.service.query.QueryResult;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

/**
 * Stellt Funktionen zur Ermittlung und Bezahlung von Rechnungen zur Verf&uuml;gung</br>
 * 
 * @author gerold
 */
@Service("hvInvoice")
@Path("/api/v1/invoice")
public class InvoiceApi extends BaseApi implements IInvoiceApi {
	private static Logger log = LoggerFactory.getLogger(InvoiceApi.class) ;
	
	/**
	 * Die maximale Gr&ouml;&szlig;e beim Upload eines Zahlungsbelegs
	 */
	public final static int MAX_CONTENT_LENGTH_PAYMENT = 500000 ;
	
	@Autowired
	private InvoiceQuery invoiceQuery ;
	@Autowired
	private IRechnungCall rechnungCall ;
	@Autowired
	private IKundeCall kundeCall ;
	@Autowired
	private IPartnerCall partnerCall ;
	@Autowired
	private IFinanzCall finanzCall ;
	@Autowired
	private IParameterCall parameterCall ;
	@Autowired
	private IJCRDocCall jcrdocCall ;
	@Autowired
	private IGlobalInfo globalInfo ;	
	@Autowired
	private CustomerService customerService ;
	@Autowired
	private IJudgeCall judgeCall;

	public class InvoiceFilter extends Filter {
		public final static String OPEN = BASE + "open" ;
		public final static String CUSTOMER = BASE + "customer" ;
		public final static String PROJECT = BASE + "project" ;
		public final static String STATISTICS_ADDRESS = BASE + "statisticsaddress" ;
	}		
	
	public class PaymentParam {
		public final static String INVOICEID = "invoiceid" ;
		public final static String INVOICECNR = "invoicecnr" ;	
		public final static String PAYMENTID  = "paymentid" ;
		public final static String AMOUNT     = "amount";
		public final static String YEAR       = "year";
		public final static String MONTH      = "month";
		public final static String DAY        = "day";
	}
	
	@GET
	@Produces({FORMAT_JSON, FORMAT_XML})
	public InvoiceEntryList getInvoices(
			@QueryParam(Param.USERID) String userId,
			@QueryParam(Param.LIMIT) Integer limit,
			@QueryParam(Param.STARTINDEX) Integer startIndex, 
			@QueryParam(Filter.CNR) String filterCnr,
			@QueryParam(InvoiceFilter.CUSTOMER) String filterCustomer,
			@QueryParam(InvoiceFilter.PROJECT) String filterProject,		
			@QueryParam(Filter.TEXTSEARCH) String textSearch,		
			@QueryParam(InvoiceFilter.STATISTICS_ADDRESS) String statisticsAddress,					
			@QueryParam(InvoiceFilter.OPEN) Boolean filterWithOpen,
			@QueryParam("addCustomerDetail") Boolean addCustomerDetail) throws RemoteException, NamingException, EJBExceptionLP {
		if(connectClient(userId) == null) {
			return new InvoiceEntryList() ;
		}
	
		if(!judgeCall.hasRechnungCRUD()) {
			respondUnauthorized();
			return new InvoiceEntryList() ;
		}
		
		FilterKriteriumCollector collector = new FilterKriteriumCollector() ;
		collector.addAll(invoiceQuery.getFilterOpen(filterWithOpen));
		collector.addAll(invoiceQuery.getFilterCnr(filterCnr)) ;
		collector.addAll(invoiceQuery.getFilterCustomername(filterCustomer));		
		collector.addAll(invoiceQuery.getFilterProject(filterProject)) ;
		collector.addAll(invoiceQuery.getFilterTextsearch(textSearch)) ;
		collector.addAll(invoiceQuery.getFilterStatisticsAddress(statisticsAddress)) ;

		QueryParametersFeatures params = invoiceQuery.getFeatureQueryParameters(collector) ;
		params.setLimit(limit) ;
		params.setKeyOfSelectedRow(startIndex) ;
		params.addFeature(RechnungHandlerFeature.BRUTTOWERTE);
		
		QueryResult result = invoiceQuery.setQuery(params) ;
		List<InvoiceEntry> entries = invoiceQuery.getResultList(result) ;
		
		if(Boolean.TRUE.equals(addCustomerDetail)) {
			for (InvoiceEntry invoiceEntry : entries) {
				KundeDto kundeDto = kundeCall.kundeFindByPrimaryKeyOhneExc(invoiceEntry.getCustomerId()) ;
				if(kundeDto != null ) {
					invoiceEntry.setCustomerEntry(customerService.getCustomerDetailEntry(kundeDto));
				}
			}
		}
		return new InvoiceEntryList(entries) ;
	}
	
//	@GET
//	@Path("/payment")
//	@Produces({FORMAT_JSON, FORMAT_XML})
//	public InvoicePaymentEntryList getPayment(
//			@QueryParam(Param.USERID) String userId,
//			@QueryParam(PaymentParam.INVOICEID) Integer invoiceId,
//			@QueryParam(PaymentParam.INVOICECNR) String invoiceCnr) throws RemoteException {
//		if(connectClient(userId) == null) {
//			return new InvoicePaymentEntryList() ;
//		}
//		
//		RechnungDto dto = findRechnungDto(invoiceId, invoiceCnr) ;
//		if(dto == null) {
//			return new InvoicePaymentEntryList();
//		}
//		
//		return new InvoicePaymentEntryList() ;
//	}
	
//	@POST 
//	@Path("{" + PaymentParam.INVOICEID + "}/payment")
//	public Integer createPayment(
//			@QueryParam(Param.USERID) String userId,
//			@PathParam(PaymentParam.INVOICEID) Integer invoiceId,
//			InvoicePaymentEntry paymentEntry) {
//		if(connectClient(userId) == null) {
//			return null ;
//		}
//		if(invoiceId == null) {
//			respondBadRequestValueMissing(PaymentParam.INVOICEID);
//			return null ;
//		}
//		if(paymentEntry == null) {			
//			respondBadRequestValueMissing("payment");
//			return null ;
//		}
//		
//		RechnungDto dto = rechnungCall.rechnungFindByPrimaryKeyOhneExc(invoiceId);
//		if(dto == null) {
//			respondNotFound(PaymentParam.INVOICEID, invoiceId.toString());
//			return null ;
//		}
//		
//		createZahlung(dto, paymentEntry) ;
//		return null ;
//	}
	
	@POST 
	@Path("{" + PaymentParam.INVOICEID + "}/cashpayment/{" + PaymentParam.PAYMENTID + "}")
//	@Consumes({FORMAT_JSON, FORMAT_XML})
	public Integer attachDocumentToCashPayment(
			@QueryParam(Param.USERID) String userId,
			@PathParam(PaymentParam.INVOICEID) Integer invoiceId,
			@PathParam(PaymentParam.PAYMENTID) Integer paymentId,			
			@QueryParam(Param.CNR) String cashCnr,
			MultipartBody body) throws NamingException, RemoteException {
		if(connectClient(userId, MAX_CONTENT_LENGTH_PAYMENT) == null) {
			return null ;
		}

		HvValidateBadRequest.notNull(invoiceId, PaymentParam.INVOICEID);
		HvValidateBadRequest.notNull(paymentId, PaymentParam.PAYMENTID); 
		HvValidateBadRequest.notNull(body, "body"); 
		
		ArbeitsplatzkonfigurationDto konfigurationDto = getArbeitsplatzkonfiguration(cashCnr) ;
		if(konfigurationDto == null) {
			return null ;
		}
		
		RechnungDto dto = rechnungCall.rechnungFindByPrimaryKeyOhneExc(invoiceId);
		HvValidateNotFound.notNull(dto, PaymentParam.INVOICEID, invoiceId); 
		
		RechnungzahlungDto zahlungDto = rechnungCall.rechnungzahlungFindByPrimaryKeyOhneExc(paymentId) ;
		HvValidateNotFound.notNull(zahlungDto, PaymentParam.PAYMENTID, paymentId);
 		
		if(!dto.getIId().equals(zahlungDto.getRechnungIId())) {
			respondExpectationFailed();
			return null ;
		}
		
		List<Attachment> attachments = body.getAllAttachments();
		for (Attachment attachment : attachments) {
			JCRDocDto jcrDto = createJcrZahlungbeleg(attachment, zahlungDto, dto, cashCnr) ;
			if(jcrDto == null) {
				return 0 ;
			}
			jcrdocCall.addNewDocumentOrNewVersionOfDocument(jcrDto);
		}

		return 42 ;
	}
	
	private JCRDocDto createJcrZahlungbeleg(Attachment attachment,
			RechnungzahlungDto zahlungDto, RechnungDto rechnungDto, String cashCnr) throws RemoteException, NamingException {
		DocPath dp = new DocPath(new DocNodeRechnungZahlung(zahlungDto, rechnungDto));
		dp.add(new DocNodeFile(attachment.getDataHandler().getName()));
		JCRDocDto jcrDocDto = new JCRDocDto();
		jcrDocDto.setDocPath(dp);
		
		try {
			InputStream is = attachment.getDataHandler().getInputStream() ;
			jcrDocDto.setbData(IOUtils.readBytesFromStream(is)) ;
			jcrDocDto.setbVersteckt(false);
		} catch(IOException e) {
			log.error("Read from Attachment failed", e); 
			respondBadRequest("attachment", attachment.getDataHandler().getName());
			return null ;
		}

//		PartnerDto partnerDto = partnerCall.partnerFindByPersonalId() ;
//		jcrDocDto.setlAnleger(partnerDto.getIId());
		jcrDocDto.setlAnleger(globalInfo.getTheClientDto().getIDPersonal());
		
		Integer kundeId = rechnungDto.getKundeIId() ;
		KundeDto kundeDto = kundeCall.kundeFindByPrimaryKeyOhneExc(kundeId) ;

		jcrDocDto.setlPartner(kundeDto.getPartnerIId());
		jcrDocDto.setlSicherheitsstufe(JCRDocFac.SECURITY_ARCHIV);
		jcrDocDto.setlZeitpunkt(System.currentTimeMillis());
		jcrDocDto.setsBelegart(JCRDocFac.DEFAULT_ARCHIV_BELEGART);
		jcrDocDto.setsGruppierung(JCRDocFac.DEFAULT_ARCHIV_GRUPPE);
		String filename = attachment.getDataHandler().getDataSource().getName().replace('/', '.') ;
		jcrDocDto.setsBelegnummer(Helper.getName(filename));
		jcrDocDto.setsMIME(Helper.getMime(filename));
		jcrDocDto.setsFilename(filename);		
		jcrDocDto.setsName(cashCnr);
		jcrDocDto.setsRow(zahlungDto.getIId().toString());
		jcrDocDto.setsTable("RE ZAHLUNG");
		String sSchlagworte = "Zahlungsbest\u00e4titgung " + cashCnr ;
		jcrDocDto.setsSchlagworte(sSchlagworte);
		return jcrDocDto ;
	}

	private RechnungzahlungDto createKassaZahlung(
			RechnungDto rechnungDto, InvoiceCashPaymentPostEntry postEntry) throws RemoteException {
		BankverbindungDto bankDto = getBankverbindung(postEntry) ;
		if(bankDto == null) {
			return null ;
		}
		
		RechnungzahlungDto zahlungDto = new RechnungzahlungDto() ;
		zahlungDto.setDZahldatum(new Date(System.currentTimeMillis()));
		zahlungDto.setZahlungsartCNr(RechnungFac.ZAHLUNGSART_BANK);
		zahlungDto.setRechnungIId(rechnungDto.getIId());
		zahlungDto.setBankkontoIId(bankDto.getIId());
		zahlungDto.setIAuszug(createAuszugsnummer(zahlungDto.getDZahldatum()));
		
		// Zahlung erfolgt in Mandantenwaehrung 
		zahlungDto.setNKurs(BigDecimal.ONE);
		BigDecimal ustBetrag = rechnungCall.getWertUstAnteiligZuRechnungUst(
				rechnungDto.getIId(), postEntry.getAmount()) ;
		zahlungDto.setNBetragfw(postEntry.getAmount().subtract(ustBetrag));
		zahlungDto.setNBetrag(zahlungDto.getNBetragfw());
		zahlungDto.setNBetragUstfw(ustBetrag);
		zahlungDto.setNBetragUst(ustBetrag) ;

		BigDecimal bezahltBrutto = rechnungCall.getBereitsBezahltWertBruttoVonRechnungFwOhneFSkonto(
						rechnungDto.getIId(), null) ;
		boolean bezahlt = rechnungDto.getNWertfw().add(rechnungDto.getNWertustfw())
								.subtract(bezahltBrutto)
								.subtract(zahlungDto.getNBetragfw()).subtract(zahlungDto.getNBetragUstfw()).signum() == 0;
		return rechnungCall.createZahlung(zahlungDto, bezahlt) ;
	}

	private ArbeitsplatzkonfigurationDto getArbeitsplatzkonfiguration(String arbeitsplatzCnr) throws RemoteException {
		ArbeitsplatzDto arbeitsplatzDto = 
				parameterCall.arbeitsplatzFindByCTypCGeraetecode(
						ParameterFac.ARBEITSPLATZ_TYP_KASSA, arbeitsplatzCnr) ;
		if(arbeitsplatzDto == null) {
			respondUnauthorized();
			return null ;
		}
		
		ArbeitsplatzkonfigurationDto konfigurationDto = 
				parameterCall.arbeitsplatzkonfigurationFindByPrimaryKey(arbeitsplatzDto.getIId()) ;
		if(konfigurationDto == null || StringHelper.isEmpty(konfigurationDto.getCSystem())) {
			respondUnauthorized();
			return null ;
		}
		
		return konfigurationDto; 
	}
	
	
	private BankverbindungDto getBankverbindung(InvoiceCashPaymentPostEntry postEntry) throws RemoteException {
		ArbeitsplatzkonfigurationDto konfigurationDto = getArbeitsplatzkonfiguration(postEntry.getCnr()) ;
		if(konfigurationDto == null) {
			return null ;
		}
		BankverbindungDto bankDto = findBankverbindung(postEntry, konfigurationDto.getCSystem()) ;
		if(bankDto == null) {
			respondUnauthorized() ;
		}
		return bankDto ;
	}

	private BankverbindungDto findBankverbindung(InvoiceCashPaymentPostEntry postEntry, String config) {
		final String expectedBank = postEntry.getCnr() + "-" + postEntry.getPaymentType().getText() ;		
		
		List<BankverbindungDto> banks = finanzCall.bankverbindungFindByMandantCNrOhneExc();
		return CollectionTools.detect(banks, new IDetect<BankverbindungDto>() {
			public boolean accept(BankverbindungDto bank) {
				return expectedBank.equals(bank.getCBez());
			}
		});
	}
	
	private Integer createAuszugsnummer(Date paymentDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd") ;
		return Integer.valueOf(sdf.format(paymentDate)) ; 
	}
	

//	private RechnungDto findRechnungDto(Integer rechnungId, String rechnungCnr) {
//		if(rechnungId == null && StringHelper.isEmpty(rechnungCnr)) {
//			respondBadRequestValueMissing(Param.ITEMID);
//			return null ;			
//		}
//		RechnungDto rechnungDto = null ;
//		if(rechnungId != null) {
//			rechnungDto = rechnungCall.rechnungFindByPrimaryKeyOhneExc(rechnungId);
//		}
//		if(rechnungDto == null && !StringHelper.isEmpty(rechnungCnr)) {
//			rechnungDto = rechnungCall.rechnungFindByRechnungartCNrMandantCNrOhneExc(rechnungCnr);
//		}
//		if(rechnungDto == null) {
//			respondInvoiceNotFound(rechnungId, rechnungCnr);
//			return null ;
//		}
//		
//		return rechnungDto ;
//	}
	
//	private void respondInvoiceNotFound(Integer id, String cnr) {
//		if(id != null) {
//			respondNotFound(PaymentParam.INVOICEID, id);
//		} else {
//			respondNotFound(PaymentParam.INVOICECNR, cnr) ;
//		}
//	}

	
	@POST
	@Path("{" + PaymentParam.INVOICEID + "}/cashpayment")
	@Consumes({FORMAT_JSON, FORMAT_XML})
	public Integer createCashPayment(
			@QueryParam(Param.USERID) String userId,
			@PathParam(PaymentParam.INVOICEID) Integer invoiceId,
			InvoiceCashPaymentPostEntry paymentEntry) throws RemoteException {
		if(connectClient(userId) == null) {
			return null ;
		}
		HvValidateBadRequest.notNull(invoiceId, PaymentParam.INVOICEID) ;
 		HvValidateBadRequest.notNull(paymentEntry, "payment");
 		
		RechnungDto dto = rechnungCall.rechnungFindByPrimaryKeyOhneExc(invoiceId);
		HvValidateNotFound.notNull(dto, PaymentParam.INVOICEID, invoiceId);
 		
		RechnungzahlungDto zahlungDto = createKassaZahlung(dto, paymentEntry) ;
		return zahlungDto == null ? null : zahlungDto.getIId() ;
	}
	
	@DELETE
	@Path("{" + PaymentParam.INVOICEID + "}/cashpayment/{" + PaymentParam.YEAR + "}/{" +PaymentParam.MONTH + "}/{" + PaymentParam.DAY + "}")
	@Consumes({FORMAT_JSON, FORMAT_XML})
	public void removeCashPayment(
			@QueryParam(Param.USERID) String userId,
			@PathParam(PaymentParam.INVOICEID) Integer invoiceId,
			@PathParam(PaymentParam.YEAR) Integer year,
			@PathParam(PaymentParam.MONTH) Integer month,
			@PathParam(PaymentParam.DAY) Integer day,			
			@QueryParam(PaymentParam.AMOUNT) BigDecimal amount) throws RemoteException {
		if(connectClient(userId) == null) {
			return;
		}
		
		HvValidateBadRequest.notNull(invoiceId, PaymentParam.INVOICEID) ;
		RechnungDto dto = rechnungCall.rechnungFindByPrimaryKeyOhneExc(invoiceId);
		HvValidateNotFound.notNull(dto, PaymentParam.INVOICEID, invoiceId);
		HvValidateNotFound.notValid(
				globalInfo.getMandant().equals(dto.getMandantCNr()), 
				PaymentParam.INVOICEID, invoiceId.toString());

		HvValidateBadRequest.notNull(amount,  PaymentParam.AMOUNT);		
		
		Calendar c = Calendar.getInstance();
		c.clear();
		c.set(year, month - 1, day);
		RechnungzahlungDto[] zahlungDtos = rechnungCall.zahlungFindByRechnungId(invoiceId);
		if(zahlungDtos != null) {
			for (RechnungzahlungDto zahlungDto : zahlungDtos) {
				BigDecimal sum = zahlungDto.getNBetragfw().add(zahlungDto.getNBetragUstfw());
				if(sum.compareTo(amount) == 0 
						&& zahlungDto.getDZahldatum().compareTo(c.getTime()) == 0) {
					rechnungCall.removeZahlung(zahlungDto);
					log.info("Removed payment Id=" + zahlungDto.getIId() 
							+ ", date=" + c.getTime().toString() 
							+ ", amount=" + amount.toPlainString() + ".");
					return;
				}
			}
		}

		respondNotFound(PaymentParam.AMOUNT, amount.toPlainString());
	}
}
