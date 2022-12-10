package com.heliumv.api.item;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.BaseEntryId;
import com.heliumv.types.MimeTypeEnum;

@XmlRootElement
public class ItemHintEntry extends BaseEntryId {
	private String cnr;
	private String content;
	private MimeTypeEnum mimeType;
	
	public ItemHintEntry() {
	}

	public ItemHintEntry(Integer itemId) {
		super(itemId);
	}

	/**
	 * Der (html) Hinweistext
	 * @return der Hinweistext, der auch html enthalten kann
	 */
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * Der Mimetype des Hinweises
	 * @return bisher nur TEXTHTML
	 */
	public MimeTypeEnum getMimeType() {
		return mimeType;
	}

	public void setMimeType(MimeTypeEnum mimeType) {
		this.mimeType = mimeType;
	}

	/**
	 * Die Artikelkommentarart des Hinweises
	 * @return die Artikelkommentarart
	 */
	public String getCnr() {
		return cnr;
	}

	public void setCnr(String cnr) {
		this.cnr = cnr;
	}
}
