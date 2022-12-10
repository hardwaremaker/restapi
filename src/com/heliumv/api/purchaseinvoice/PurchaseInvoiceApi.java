package com.heliumv.api.purchaseinvoice;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Collection;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.imageio.ImageIO;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MultivaluedHashMap;

import org.apache.commons.codec.binary.Base64;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.heliumv.api.BaseApi;
import com.heliumv.api.HvValidateBadRequest;
import com.heliumv.api.document.BelegkopieMetadata;
import com.heliumv.api.document.DocumentMetadata;
import com.heliumv.api.document.IDocumentCategoryService;
import com.heliumv.api.document.IDocumentService;
import com.heliumv.factory.IAuftragCall;
import com.heliumv.factory.IEingangsrechnungCall;
import com.heliumv.factory.IGlobalInfo;
import com.heliumv.factory.IJudgeCall;
import com.heliumv.factory.ILieferantCall;
import com.heliumv.factory.ILocaleCall;
import com.heliumv.factory.IMandantCall;
import com.heliumv.factory.IPersonalCall;
import com.heliumv.tools.StringHelper;
import com.heliumv.tools.TextToGraphics;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.eingangsrechnung.service.EingangsrechnungAuftragszuordnungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungFac;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.system.jcr.service.JCRDocDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.WaehrungDto;
import com.lp.server.system.service.WechselkursDto;
import com.lp.server.system.service.ZahlungszielDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Service("hvPurchaseInvoice")
@Path("/api/v1/purchaseinvoice")
public class PurchaseInvoiceApi extends BaseApi implements IPurchaseInvoiceApi {
	private static Logger log = LoggerFactory.getLogger(PurchaseInvoiceApi.class) ;

	public final static int MAX_CONTENT_LENGTH_PURCHASEINVOICE = 500000;
	public final static int MAX_CONTENT_LENGTH_MOBILEPURCHASES = 20*1024*1024;
	
	@Autowired
	private IDocumentService documentService;
	@Autowired
	private IDocumentCategoryService purchaseInvoiceDocService;
	@Autowired
	private IGlobalInfo globalInfo;
	@Autowired
	private ILocaleCall localeCall;
	@Autowired
	private IEingangsrechnungCall eingangsrechnungCall;
	@Autowired
	private IPersonalCall personalCall;
	@Autowired
	private IMandantCall mandantCall;
	@Autowired
	private IAuftragCall auftragCall;
	@Autowired
	private ILieferantCall lieferantCall;
	@Autowired
	private IJudgeCall judgeCall;
	
	@POST
	@Path("{" + Param.PURCHASEINVOICEID + "}/document")
	public void createDocument(
			@PathParam(Param.PURCHASEINVOICEID) Integer purchaseinvoiceId,
			@QueryParam(Param.USERID) String userId,
			@QueryParam(Param.TYPE) String type,
			@QueryParam(Param.KEYWORDS) String keywords,
			@QueryParam(Param.GROUPING) String grouping,
			@QueryParam(Param.SECURITYLEVEL) Long securitylevel,
			MultipartBody body) throws RemoteException, NamingException {
		if(connectClient(userId, MAX_CONTENT_LENGTH_PURCHASEINVOICE) == null) {
			return;
		}

		HvValidateBadRequest.notNull(purchaseinvoiceId, Param.PRODUCTIONID);
		HvValidateBadRequest.notNull(body, "body"); 
		
		for (Attachment attachment : body.getAllAttachments()) {
			documentService.createDoc(purchaseInvoiceDocService, purchaseinvoiceId, 
					null, new DocumentMetadata(type, keywords, grouping, securitylevel), attachment);
		}
	}

	@POST
	@Path("/mobilebatch")
	@Consumes({FORMAT_JSON, FORMAT_XML})
	public void createMobileInvoices(
			@QueryParam(Param.USERID) String userId,
			PostPurchaseInvoices invoices) throws RemoteException {		
		HvValidateBadRequest.notNull(invoices, "invoices");		
		if(connectClient(userId,
				MAX_CONTENT_LENGTH_MOBILEPURCHASES) == null) {
			return;
		}
		
		if(!judgeCall.hatRecht(RechteFac.RECHT_ER_EINGANGSRECHNUNG_CUD)) {
			log.warn("Client token '" + userId + "' has no '" + RechteFac.RECHT_ER_EINGANGSRECHNUNG_CUD);
			respondUnauthorized();
			return;
		}
		if(!judgeCall.hasAuftragCRUD()) {
			log.warn("Client token '" + userId + "' has no AuftragC(R)UD'");
			respondUnauthorized();
			return;
		}
		
		boolean success = true;
		for (PurchaseInvoiceEntry entry : invoices.getInvoices().getEntries()) {
			try {
				EingangsrechnungDto erDto = bookBatchEntry(entry, userId);
				success &= (erDto != null);
			} catch(Exception e) {
				log.error("Storing eingangsrechnung", e);
				success = false;
			}
		}

		if(!success) {
		}
	}
	
	private EingangsrechnungDto bookBatchEntry0(PurchaseInvoiceEntry entry, String userId) throws NamingException, RemoteException {
		EingangsrechnungDto erDto = prepareEr(entry);
		EingangsrechnungAuftragszuordnungDto zoDto = assignOrderToInvoice(erDto, entry);
		assignDocument(erDto, entry);
		return erDto;
	}

	private EingangsrechnungDto bookBatchEntry(PurchaseInvoiceEntry entry, String userId) throws NamingException, RemoteException {
		EingangsrechnungDto erDto = prepareEr(entry);
		JCRDocDto jcrDto = prepareDocument(erDto, entry);
		return eingangsrechnungCall
				.createEingangsrechnungMitDokument(erDto, entry.getOrderId(), jcrDto);
/*		
		EingangsrechnungAuftragszuordnungDto zoDto = assignOrderToInvoice(erDto, entry);
		assignDocument(erDto, entry);
		return erDto;
*/
	}
	
	private EingangsrechnungDto prepareEr(PurchaseInvoiceEntry entry) throws RemoteException {
		EingangsrechnungDto erDto = new EingangsrechnungDto();
		erDto.setEingangsrechnungartCNr(EingangsrechnungFac
				.EINGANGSRECHNUNGART_EINGANGSRECHNUNG);
		erDto.setCText(StringHelper.trimCut(entry.getDescription(), 40));
		erDto.setMandantCNr(globalInfo.getMandant());
		erDto.setDBelegdatum(Helper.cutDate(new Date(entry.getTimestampMs())));
		erDto.setBIgErwerb(Helper.getShortFalse());
		erDto.setCLieferantenrechnungsnummer("0");
		erDto.setDFreigabedatum(eingangsrechnungCall.getDefaultFreigabeDatum());
		
		setBetrag(erDto, entry.getCurrency(), entry.getPrice());
		setPersonalName(erDto, globalInfo.getTheClientDto().getIDPersonal());
		setZahlungsziel(erDto, entry.getPaymentType());
		setLieferant(erDto);
//		return eingangsrechnungCall.createEingangsrechnung(erDto);
		return erDto;
	}
	
	private EingangsrechnungAuftragszuordnungDto assignOrderToInvoice(EingangsrechnungDto erDto, PurchaseInvoiceEntry entry) throws RemoteException {
		AuftragDto abDto = auftragCall.auftragFindByPrimaryKeyOhneExc(entry.getOrderId());
		if(abDto == null) {
			return null;
		}
		if(!globalInfo.getMandant().equals(abDto.getMandantCNr())) {
			return null;
		}
		EingangsrechnungAuftragszuordnungDto zoDto = new EingangsrechnungAuftragszuordnungDto();
		zoDto.setAuftragIId(entry.getOrderId());
		zoDto.setEingangsrechnungIId(erDto.getIId());
		zoDto.setFVerrechenbar(new Double("100"));
		zoDto.setNBetrag(erDto.getNBetragfw());
		
		return eingangsrechnungCall.createEingangsrechnungAuftragszuordnung(zoDto);
	}
	
	private JCRDocDto prepareDocument(final EingangsrechnungDto erDto, final PurchaseInvoiceEntry entry) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy_HH.mm");
		JCRDocDto jcrDto = new JCRDocDto();
		String keywords = "HVMA:" + sdf.format(new Date(entry.getTimestampMs()));
		if(!StringHelper.isEmpty(entry.getDescription())) {
			keywords += " " + entry.getDescription().trim() ;
		}
		jcrDto.setsSchlagworte(keywords);
		
		if(entry.getImage() != null) {
			jcrDto.setbData(Base64.decodeBase64(entry.getImage()));
		} else {
			jcrDto.setbData(new byte[] {});
		}
		jcrDto.setsMIME("." + entry.getImageType());
		jcrDto.setsName("image");
		jcrDto.setsFilename("image." + entry.getImageType());
		return createImageFromTextIfMissing(jcrDto);
	}

	private JCRDocDto createImageFromTextIfMissing(JCRDocDto jcrDocDto) {
		if(jcrDocDto.getbData().length == 0) {
			try {
				BufferedImage img = TextToGraphics.fromText(jcrDocDto.getsSchlagworte());
				ByteArrayOutputStream baos = new ByteArrayOutputStream(100000);
				if(ImageIO.write(img, jcrDocDto.getsMIME().replace(".", ""), baos)) {
					jcrDocDto.setbData(baos.toByteArray());
				}				
			} catch(IOException e) {
				log.error("Write text to image failed", e); 
			}
		}
		return jcrDocDto;
	}
	
	private void assignDocument(final EingangsrechnungDto erDto, final PurchaseInvoiceEntry entry) throws NamingException, RemoteException {
		if(StringHelper.isEmpty(entry.getImage()) || StringHelper.isEmpty(entry.getImageType())) {
			log.warn("There is no document for ER '" + erDto.getCNr() + "'.");
			return;
		}
		
		DocumentMetadata dmd = new BelegkopieMetadata();
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy_HH.mm");
		dmd.setKeywords("HVMA:" + sdf.format(new Date(entry.getTimestampMs())));
		
		final DataSource ds = new DataSource() {
			private byte[] content;
			private InputStream stream;

			@Override
			public OutputStream getOutputStream() throws IOException {
				return null;
			}
			
			@Override
			public String getName() {
				return "image." + entry.getImageType();
			}
			
			@Override
			public InputStream getInputStream() throws IOException {
				if(stream == null) {
					try {
						content = entry.getImage() != null 
								? Base64.decodeBase64(entry.getImage())
								: new byte[] {};
						stream = new ByteArrayInputStream(content);
					} catch (Throwable e) {
						log.error("Could not decode data string with base64 decoder to byte array. ", e);
					}
				}
				return stream;
			}
			
			@Override
			public String getContentType() {
				return "image/" + entry.getImageType();
			}
		};
		DataHandler dh = new DataHandler(ds);
		Attachment attachment = new Attachment("42", dh, 
				new MultivaluedHashMap<String, String>());
		documentService.createDoc(purchaseInvoiceDocService, erDto.getIId(), 
				null, dmd, attachment);	
	}
	
	private void setBetrag(EingangsrechnungDto erDto, String currency, BigDecimal value) throws RemoteException {
		WaehrungDto wDto = null;
		if(!StringHelper.isEmpty(currency)) {
			wDto = localeCall.waehrungFindByPrimaryKey(currency);			
		}
		currency = wDto == null ? "EUR" : wDto.getCNr();
		erDto.setWaehrungCNr(currency);
		
		if(value == null) {
			value = BigDecimal.ZERO;
		}

		MandantDto mandantDto = mandantCall.mandantFindByPrimaryKey();
		String mandantWaehrungCnr = mandantDto.getWaehrungCNr();
		WechselkursDto kursDto = localeCall.getKursZuDatum(
				currency, mandantWaehrungCnr, erDto.getDBelegdatum());
		BigDecimal bdKurs = kursDto.getNKurs().setScale(
				LocaleFac.ANZAHL_NACHKOMMASTELLEN_WECHSELKURS,
				BigDecimal.ROUND_HALF_EVEN);
		erDto.setNBetrag(Helper.rundeKaufmaennisch(
				value.multiply(bdKurs), FinanzFac.NACHKOMMASTELLEN));
		erDto.setNBetragfw(value);
		erDto.setNKurs(bdKurs);	
		erDto.setNUstBetrag(BigDecimal.ZERO);
		erDto.setNUstBetragfw(BigDecimal.ZERO);
	}
	
	private void setPersonalName(EingangsrechnungDto erDto, Integer personalId) {
		PersonalDto pDto = personalCall.byPrimaryKey(personalId);
		erDto.setCWeartikel(pDto.getPartnerDto().formatTitelAnrede());
	}
	
	private void setZahlungsziel(EingangsrechnungDto erDto, PaymentEnum paymentType) {
		ZahlungszielDto zDto = mandantCall
				.zahlungszielFindByCnr(transformPaymentType(paymentType));
		if(zDto != null) {
			erDto.setZahlungszielIId(zDto.getIId());
		} else {
			log.error("PaymentType '" + paymentType.toString() + "' not found as Zahlungsziel.");
		}
	}
	
	private String transformPaymentType(PaymentEnum paymentType) {
		if(paymentType.equals(PaymentEnum.ME)) return "HVMA_PRIVAT";
		if(paymentType.equals(PaymentEnum.CARD)) return "HVMA_CARD";
		return "HVMA_OTHER";
	}
	
	private void setLieferant(EingangsrechnungDto erDto) throws RemoteException {
		MandantDto mandantDto = mandantCall.mandantFindByPrimaryKey();
		Collection<LieferantDto> lieferanten = lieferantCall
				.lieferantFindByPartnerIdMandant(mandantDto.getPartnerIId());
		if(lieferanten.size() > 0) {
			erDto.setLieferantIId(lieferanten.iterator().next().getIId());
		} else {
			log.error("There is no 'Lieferant' for tenant '" +
					mandantDto.getCNr() + "', partnerId=" + mandantDto.getPartnerIId() + ".");
			
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_ER_LIEFERANT_FUER_MANDANT_FEHLT,
					"Kein Lieferant fuer Mandant " + mandantDto.getCNr() + " definiert", 
					mandantDto.getCNr());
		}
	}
}
