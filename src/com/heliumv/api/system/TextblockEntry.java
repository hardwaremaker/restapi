package com.heliumv.api.system;

import javax.xml.bind.annotation.XmlRootElement;

import com.heliumv.api.BaseEntryId;
import com.heliumv.types.MimeTypeEnum;

@XmlRootElement
public class TextblockEntry extends BaseEntryId {
	private String description;
	private MimeTypeEnum mimeType;
	private boolean hidden;
	private String text;
	private String localeCnr;
	private String filename;
	private byte[] blob;	
	
	public TextblockEntry() {
	}
	
	public TextblockEntry(Integer unitId) {
		super(unitId);
	}

	/**
	 * Die Kennung des Textbausteins
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Ist der Textbaustein versteckt?
	 * 
	 * @return true wenn der Textbaustein versteckt ist
	 */
	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	/**
	 * Das "Datenformat" des Textbausteins</br>
	 * 
	 * @return
	 */
	public MimeTypeEnum getMimeType() {
		return mimeType;
	}

	public void setMimeType(MimeTypeEnum mimeType) {
		this.mimeType = mimeType;
	}

	/**
	 * Die Sprache in der der Textbaustein angelegt ist
	 * @return
	 */
	public String getLocaleCnr() {
		return localeCnr;
	}

	public void setLocaleCnr(String localeCnr) {
		this.localeCnr = localeCnr;
	}

	/**
	 * Der Dateiname eines Bin&auml;rtextbausteins
	 * @return
	 */
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	/**
	 * Der komplette Text des Textbausteins</br>
	 * <p>Der Text kann auch Jasperstyle Auszeichnungen enthalten</p>
	 * @return
	 */
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Der bin&auml;re Inhalt des "Text"bausteins, sofern es
	 * sich um einen bin&auml;rer Textbaustein handelt.</br>
	 * 
	 * @return
	 */
	public byte[] getBlob() {
		return blob;
	}

	public void setBlob(byte[] blob) {
		this.blob = blob;
	}
}
