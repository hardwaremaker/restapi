package com.heliumv.api.item;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.BaseEntryId;
import com.heliumv.types.MimeTypeEnum;

@XmlRootElement
public class ItemCommentMediaInfoEntry extends BaseEntryId {

	private MimeTypeEnum mimeType;
	private Integer iSort;
	private String commentType;
	private String commentTypeDescription;
	private String filename;
	private Long size;
	private byte[] content;
	
	public ItemCommentMediaInfoEntry() {
	}

	public ItemCommentMediaInfoEntry(Integer id) {
		super(id);
	}

	public MimeTypeEnum getMimeType() {
		return mimeType;
	}

	public void setMimeType(MimeTypeEnum mimeType) {
		this.mimeType = mimeType;
	}

	public Integer getiSort() {
		return iSort;
	}

	public void setiSort(Integer iSort) {
		this.iSort = iSort;
	}

	public String getCommentType() {
		return commentType;
	}

	public void setCommentType(String commentType) {
		this.commentType = commentType;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public String getCommentTypeDescription() {
		return commentTypeDescription;
	}
	
	public void setCommentTypeDescription(String commentTypeDescription) {
		this.commentTypeDescription = commentTypeDescription;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}
}
