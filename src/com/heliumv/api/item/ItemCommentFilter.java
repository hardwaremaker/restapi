package com.heliumv.api.item;

import java.util.EnumSet;

import com.heliumv.api.document.DocumentCategory;
import com.heliumv.types.MimeTypeEnum;

public class ItemCommentFilter {
	
	private EnumSet<MimeTypeEnum> mimeTypes;
	private EnumSet<DocumentCategory> docCategories;
	
	public ItemCommentFilter() {
	}

	public EnumSet<DocumentCategory> getDocCategories() {
		if (docCategories == null) {
			docCategories = EnumSet.noneOf(DocumentCategory.class);
		}
		return docCategories;
	}
	public void setDocCategories(EnumSet<DocumentCategory> docCategories) {
		this.docCategories = docCategories;
	}
	
	public EnumSet<MimeTypeEnum> getMimeTypes() {
		if (mimeTypes == null) {
			mimeTypes = EnumSet.allOf(MimeTypeEnum.class);
		}
		return mimeTypes;
	}
	public void setMimeTypes(EnumSet<MimeTypeEnum> mimeTypes) {
		this.mimeTypes = mimeTypes;
	}
}
