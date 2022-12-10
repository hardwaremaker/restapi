package com.heliumv.api.document;

import org.springframework.beans.factory.annotation.Autowired;

public class DocumentCategoryServiceFactory {
	@Autowired
	private IDocumentCategoryService productionDocService;
	@Autowired
	private IDocumentCategoryService purchaseInvoiceDocService;
	@Autowired
	private IDocumentCategoryService orderDocService;
	@Autowired
	private IDocumentCategoryService deliveryDocService;
	@Autowired
	private IDocumentCategoryService projectDocService;
	
	public IDocumentCategoryService getDocumentCategoryService(DocumentCategory category) {
		if (DocumentCategory.PRODUCTION.equals(category)) {
			return productionDocService;
		} else if (DocumentCategory.PURCHASEINVOICE.equals(category)) {
			return purchaseInvoiceDocService;
		} else if (DocumentCategory.ORDER.equals(category)) {
			return orderDocService;
		} else if (DocumentCategory.DELIVERYNOTE.equals(category)) {
			return deliveryDocService;
		} else if (DocumentCategory.PROJECT.equals(category)) {
			return projectDocService;
		}
		
		return null;
	}
}
