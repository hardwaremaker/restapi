package com.heliumv.api.document;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.imageio.ImageIO;
import javax.jcr.RepositoryException;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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
import com.heliumv.factory.IGlobalInfo;
import com.heliumv.factory.IJCRDocCall;
import com.heliumv.factory.IJudgeCall;
import com.heliumv.factory.IMandantCall;
import com.heliumv.tools.CollectionTools;
import com.heliumv.tools.ISelect;
import com.heliumv.tools.SelectChain;
import com.heliumv.tools.TextToGraphics;
import com.lp.server.system.jcr.service.DokumentbelegartDto;
import com.lp.server.system.jcr.service.DokumentgruppierungDto;
import com.lp.server.system.jcr.service.JCRDocDto;
import com.lp.server.system.jcr.service.JCRDocFac;
import com.lp.server.system.jcr.service.docnode.DocNodeBase;
import com.lp.server.system.jcr.service.docnode.DocNodeFile;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.service.MandantDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.HVPDFExporter;
import com.lp.util.Helper;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;

@Service("hvDocument")
@Path("/api/v1/document/")
public class DocumentApi extends BaseApi implements IDocumentApi, IDocumentService {
	private static Logger log = LoggerFactory.getLogger(DocumentApi.class) ;

	public final static int MAX_CONTENT_LENGTH_DOCUMENT = 50*1024*1024;
	
	@Autowired
	private DocumentCategoryServiceFactory documentCategoryServiceFactory;
	@Autowired
	private IGlobalInfo globalInfo;	
	@Autowired
	private IJCRDocCall jcrdocCall ;
	@Autowired
	private IMandantCall mandantCall;
	@Autowired
	private IJudgeCall judgeCall;

	@POST
	public void createDocument(			
			@QueryParam(Param.USERID) String userId,
			@QueryParam(Param.ID) Integer id,			
			@QueryParam(Param.CNR) String cnr,
			@QueryParam(Param.CATEGORY) DocumentCategory category,
			@QueryParam(Param.TYPE) String type,
			@QueryParam(Param.KEYWORDS) String keywords,
			@QueryParam(Param.GROUPING) String grouping,
			@QueryParam(Param.SECURITYLEVEL) Long securitylevel,
			MultipartBody body) throws RemoteException, NamingException {
		HvValidateBadRequest.notNull(category, "category"); 
		HvValidateBadRequest.notValid(id != null || cnr != null, "id and cnr");
		HvValidateBadRequest.notNull(body, "body"); 
		
		if(connectClient(userId, MAX_CONTENT_LENGTH_DOCUMENT) == null) {
			return;
		}

		IDocumentCategoryService docCategoryService = 
				documentCategoryServiceFactory.getDocumentCategoryService(category);
		HvValidateNotFound.notNull(docCategoryService, "category", category.getText());
		
		List<Attachment> attachments = body.getAllAttachments();
		for (Attachment attachment : attachments) {
			createDoc(docCategoryService, id, cnr, 
					new DocumentMetadata(type, keywords, grouping, securitylevel), attachment);
		}
	}

	@Override
	public void createDoc(IDocumentCategoryService docService, Integer id, String cnr, DocumentMetadata metadata, 
			Attachment attachment) throws RemoteException, NamingException {
		DocumentMetadataValidator validator = new DocumentMetadataValidator();
		validator.validate(metadata);
		
		JCRDocDto jcrDocDto = docService.setupDoc(id, cnr, metadata);
		HvValidateBadRequest.notNull(jcrDocDto, "id or cnr");
		
		jcrDocDto = fillJCRDocDto(jcrDocDto, metadata, attachment);
		
		jcrdocCall.addNewDocumentOrNewVersionOfDocument(jcrDocDto);
	}
	
	private JCRDocDto fillJCRDocDto(JCRDocDto jcrDocDto, DocumentMetadata metadata, 
			Attachment attachment) throws RemoteException, EJBExceptionLP {
		jcrDocDto.getDocPath().add(new DocNodeFile(attachment.getDataHandler().getName()));
		
		try {
			InputStream is = attachment.getDataHandler().getInputStream();
			jcrDocDto.setbData(IOUtils.readBytesFromStream(is));
			jcrDocDto.setbVersteckt(false);
		} catch(IOException e) {
			log.error("Read from Attachment failed", e); 
			respondBadRequest("attachment", attachment.getDataHandler().getName());
			return null;
		}
		jcrDocDto.setlAnleger(globalInfo.getTheClientDto().getIDPersonal());
		jcrDocDto.setlZeitpunkt(System.currentTimeMillis());
		if (jcrDocDto.getsBelegart() == null) {
			jcrDocDto.setsBelegart(metadata.getType() != null ? 
					metadata.getType() : JCRDocFac.DEFAULT_ARCHIV_BELEGART);
		}
		if (jcrDocDto.getsGruppierung() == null) {
			jcrDocDto.setsGruppierung(metadata.getGrouping() != null ? 
					metadata.getGrouping() : JCRDocFac.DEFAULT_ARCHIV_GRUPPE);
		}
		String filename = attachment.getDataHandler().getDataSource().getName().replace('/', '.') ;
		jcrDocDto.setsMIME(Helper.getMime(filename));
		jcrDocDto.setsFilename(filename);
		jcrDocDto.setsName(Helper.getName(filename));
		if (jcrDocDto.getlPartner() < 1) {
			MandantDto mandantDto = mandantCall.mandantFindByPrimaryKey(globalInfo.getMandant());
			jcrDocDto.setlPartner(mandantDto.getPartnerIId());
		}
//		jcrDocDto.setlSicherheitsstufe(JCRDocFac.SECURITY_ARCHIV);
//		jcrDocDto.setsBelegnummer(Helper.getName(filename));
//		jcrDocDto.setsRow(zahlungDto.getIId().toString());
//		jcrDocDto.setsTable("RE ZAHLUNG");
		String sSchlagworte = !jcrDocDto.getsSchlagworte().isEmpty() ? 
				jcrDocDto.getsSchlagworte() + " " : "";
		sSchlagworte = sSchlagworte + (metadata.getKeywords() != null ? metadata.getKeywords() : "");
		jcrDocDto.setsSchlagworte(sSchlagworte);
		
		return createImageFromTextIfMissing(jcrDocDto);
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
	
	protected class DocumentMetadataValidator {
		public void validate(DocumentMetadata metadata) {
			validateType(metadata.getType());
			validateGrouping(metadata.getGrouping());
			validateSecurityLevel(metadata.getSecurityLevel());
		}
		
		private void validateSecurityLevel(Long securityLevel) {
			if (securityLevel == null) return;
			if (securityLevel == JCRDocFac.SECURITY_NONE || securityLevel == JCRDocFac.SECURITY_LOW 
					|| securityLevel == JCRDocFac.SECURITY_MEDIUM || securityLevel == JCRDocFac.SECURITY_HIGH 
					|| securityLevel == JCRDocFac.SECURITY_ARCHIV) {
				return;
			}
			
			HvValidateBadRequest.notValid(false, Param.SECURITYLEVEL, securityLevel.toString());
		}
		
		private void validateType(String type) {
			if (type == null) return;
			
			DokumentbelegartDto dokBelegartDto = jcrdocCall.dokumentbelegartFindByCnrMandantCnrOhneExc(type);
			HvValidateNotFound.notNull(dokBelegartDto, Param.TYPE, type);
		}
		
		private void validateGrouping(String grouping) {
			if (grouping == null) return;
			
			DokumentgruppierungDto dokGruppierungDto = jcrdocCall.dokumentgruppierungFindByCnrMandantCnrOhneExc(grouping);
			HvValidateNotFound.notNull(dokGruppierungDto, Param.GROUPING, grouping);
		}
	}
	
	@GET
	@Path("/list")
	@Produces({FORMAT_JSON, FORMAT_XML})
	public DocumentInfoEntryList getDocuments(
			@QueryParam(Param.USERID) String userId,
			@QueryParam(Param.ID) Integer id,			
			@QueryParam(Param.CNR) String cnr,
			@QueryParam(Param.CATEGORY) DocumentCategory category) throws RepositoryException, IOException {
		if (connectClient(userId) == null) return null;
		
		HvValidateBadRequest.notNull(category, "category"); 
		HvValidateBadRequest.notValid(id != null || cnr != null, "id and cnr");
		
		IDocumentCategoryService docCategoryService = 
				documentCategoryServiceFactory.getDocumentCategoryService(category);
		
		return getDocs(docCategoryService, id, cnr);
	}
	
	@Override
	public DocumentInfoEntryList getDocs(IDocumentCategoryService docService, Integer id, String cnr) throws RepositoryException, IOException {
		DocPath docPath = docService.getDocPath(id, cnr);
		
		return getDocsImpl(docPath, createDefaultDocFilter());
	}
	
	private ISelect<JcrDocDtoExtended> createDefaultDocFilter() {
		SelectChain<JcrDocDtoExtended> jcrDocFilter = new SelectChain<JcrDocDtoExtended>();
		jcrDocFilter
			.and(new NotHiddenDocSelector())
			.and(new SecurityLevelSelector())
			.and(new ImageFileSelector()).or(new PdfFileSelector()).or(new JasperFileSelector());
		return jcrDocFilter;
	}
	
	private DocumentInfoEntryList getDocsImpl(DocPath docPath, ISelect<JcrDocDtoExtended> filter) throws RepositoryException, IOException {
		Collection<JcrDocDtoExtended> filterdDocs = findJcrDocs(docPath, filter);
		
		return mapDocumentInfoEntry(filterdDocs);
	}
	
	private Collection<JcrDocDtoExtended> findJcrDocs(DocPath docPath, ISelect<JcrDocDtoExtended> filter) throws RepositoryException, IOException {
		Collection<JcrDocDtoExtended> foundDocs = findJcrDocs(docPath);
		return CollectionTools.select(foundDocs, filter);
	}

	private Collection<JcrDocDtoExtended> findJcrDocs(DocPath docPath) throws RepositoryException, IOException {
		List<DocNodeBase> children = jcrdocCall.getDocNodeChildrenFromNode(docPath);
		List<JcrDocDtoExtended> foundDocs = new ArrayList<JcrDocDtoExtended>();
		
		for (DocNodeBase docNode : children) {
			if (docNode instanceof DocNodeFile) {
				JcrDocDtoExtended jcrDocExtended = new JcrDocDtoExtended();
				jcrDocExtended.setJcrDocDto(jcrdocCall.getData(((DocNodeFile)docNode).getJcrDocDto()));
				jcrDocExtended.setUUId(docNode.getUUId());
				foundDocs.add(jcrDocExtended);
			}
		}
		return foundDocs;
	}
	
	private DocumentInfoEntryList mapDocumentInfoEntry(Collection<JcrDocDtoExtended> filterdDocs) {
		DocumentInfoEntryList infoEntries = new DocumentInfoEntryList();
		
		for (JcrDocDtoExtended jcrDocExtended : filterdDocs) {
			DocumentInfoEntry infoEntry = mapDocumentInfoEntry(jcrDocExtended);
			infoEntries.getEntries().add(infoEntry);
		}
		return infoEntries;
	}

	private DocumentInfoEntry mapDocumentInfoEntry(JcrDocDtoExtended jcrDocExtended) {
		DocumentInfoEntry infoEntry = new DocumentInfoEntry();
		infoEntry.setCnr(jcrDocExtended.getUUId());
		infoEntry.setName(jcrDocExtended.getJcrDocDto().getsName());
		if (isJasperPrintDoc(jcrDocExtended.getJcrDocDto())) {
			String docName = jcrDocExtended.getJcrDocDto().getsName();
			infoEntry.setFilename(docName);
		} else {
			infoEntry.setFilename(jcrDocExtended.getJcrDocDto().getsFilename());
		}
		infoEntry.setSize(new Long(jcrDocExtended.getJcrDocDto().getbData().length));
		return infoEntry;
	}

	private class NotHiddenDocSelector implements ISelect<JcrDocDtoExtended> {
		@Override
		public boolean select(JcrDocDtoExtended jcrDoc) {
			return !jcrDoc.getJcrDocDto().getbVersteckt();
		}
	}
	
	private class ImageFileSelector extends FileSelector {
		public boolean select(JcrDocDtoExtended element) {
			return hasExtensions(element.getJcrDocDto(), ".png", ".jpg", ".jpeg");
		}
	}
	
	private class PdfFileSelector extends FileSelector {
		public boolean select(JcrDocDtoExtended element) {
			return hasExtensions(element.getJcrDocDto(), ".pdf");
		}
	}
	
	private class JasperFileSelector implements ISelect<JcrDocDtoExtended> {
		public boolean select(JcrDocDtoExtended element) {
			return isJasperPrintDoc(element.getJcrDocDto());
		}
	}
	
	private abstract class FileSelector implements ISelect<JcrDocDtoExtended> {

		protected String getExtension(JCRDocDto jcrDoc) {
			String filename = jcrDoc.getsFilename();
			if (filename == null) {
				return null;
			}
			
			int idxExtension = filename.lastIndexOf(".");
			if (idxExtension < 0) {
				return null;
			}
			
			return filename.toLowerCase().substring(idxExtension);
		}
		
		protected boolean hasExtensions(JCRDocDto jcrDoc, String... extensions) {
			String extension = getExtension(jcrDoc);
			return extension != null 
					? Helper.isOneOf(extension, extensions)
					: false;
		}
	}
	
	private class SecurityLevelSelector implements ISelect<JcrDocDtoExtended> {
		private List<Long> userSecurityLevels;
		
		public SecurityLevelSelector() {
			setupUserSecurityLevels();
		}
		
		private void setupUserSecurityLevels() {
			userSecurityLevels = new ArrayList<Long>();
			if (judgeCall.hasDokumenteSicherheitsstufe0CU()) {
				userSecurityLevels.add(JCRDocFac.SECURITY_NONE);
			}
			if (judgeCall.hasDokumenteSicherheitsstufe1CU()) {
				userSecurityLevels.add(JCRDocFac.SECURITY_LOW);
			}
			if (judgeCall.hasDokumenteSicherheitsstufe2CU()) {
				userSecurityLevels.add(JCRDocFac.SECURITY_MEDIUM);
			}
			if (judgeCall.hasDokumenteSicherheitsstufe3CU()) {
				userSecurityLevels.add(JCRDocFac.SECURITY_HIGH);
			}
			if (judgeCall.hasDokumenteSicherheitsstufe99CU()) {
				userSecurityLevels.add(JCRDocFac.SECURITY_ARCHIV);
			}
		}

		@Override
		public boolean select(JcrDocDtoExtended element) {
			return Helper.isOneOf(new Long(element.getJcrDocDto().getlSicherheitsstufe()), userSecurityLevels);
		}
	}
	
	private class JcrDocDtoExtended {
		private JCRDocDto jcrDocDto;
		private String uuid;
		
		public JCRDocDto getJcrDocDto() {
			return jcrDocDto;
		}
		
		public void setJcrDocDto(JCRDocDto jcrDocDto) {
			this.jcrDocDto = jcrDocDto;
		}
		
		public void setUUId(String uuid) {
			this.uuid = uuid;
		}
		
		public String getUUId() {
			return uuid;
		}
	}
		
	@Override
	public RawDocument getDoc(IDocumentCategoryService docService, Integer id, String cnr, String documentCnr) throws RepositoryException, IOException {
		Collection<JcrDocDtoExtended> jcrDocs = findJcrDocs(docService.getDocPath(id, cnr), createDefaultDocFilter());
		
		for (JcrDocDtoExtended entry : jcrDocs) {
			if (entry.getUUId().equals(documentCnr)) {
				return mapRawDocument(entry);
			}
		}
		return null;
	}
	
	private boolean isJasperPrintDoc(JCRDocDto jcrDocDto) {
		return jcrDocDto.getsPath().endsWith(".jasper");
	}
	
	private byte[] exportToPdfData(JCRDocDto jcrDocDto) {
		ByteArrayInputStream bais = null;
		ObjectInputStream ois = null;
		ByteArrayOutputStream baos = null;
		
		try {
			bais = new ByteArrayInputStream(jcrDocDto.getbData());
			ois = new ObjectInputStream(bais);
			JasperPrint jPrint = (JasperPrint) ois.readObject();
			baos = new ByteArrayOutputStream(100000) ;
			HVPDFExporter exporter = new HVPDFExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
			exporter.exportReport();
			return baos.toByteArray();
		} catch (Exception exc) {
			log.error("Error exporting jasper print '" + jcrDocDto.getsFilename() + "'", exc);
			return null;
		} finally {
			try {
				if (baos != null) baos.close();
				if (ois != null) ois.close();
				if (bais != null) bais.close();
			} catch (IOException exc) {
				log.error("IOException closing streams", exc);
			}
		}
	}
	
	private RawDocument mapRawDocument(JcrDocDtoExtended jcrDocExtended) {
		RawDocument rawDoc = new RawDocument();
		rawDoc.setDocumentInfoEntry(mapDocumentInfoEntry(jcrDocExtended));
		
		if (isJasperPrintDoc(jcrDocExtended.getJcrDocDto())) {
			rawDoc.setRawData(exportToPdfData(jcrDocExtended.getJcrDocDto()));
			String docName = jcrDocExtended.getJcrDocDto().getsName();
			rawDoc.getDocumentInfoEntry().setFilename(docName.replace(".jasper", ".pdf"));
		} else {
			rawDoc.setRawData(jcrDocExtended.getJcrDocDto().getbData());
		}
		
		return rawDoc;
	}
}
