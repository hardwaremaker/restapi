package com.heliumv.api.document;

public class DocumentMetadata {

	private String type;
	private String keywords;
	private String grouping;
	private Long securityLevel;
	
	public DocumentMetadata() {
	}

	public DocumentMetadata(String type, String keywords, String grouping,
			Long securityLevel) {
		setType(type);
		setKeywords(keywords);
		setGrouping(grouping);
		setSecurityLevel(securityLevel);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getGrouping() {
		return grouping;
	}

	public void setGrouping(String grouping) {
		this.grouping = grouping;
	}

	public Long getSecurityLevel() {
		return securityLevel;
	}

	public void setSecurityLevel(Long securityLevel) {
		this.securityLevel = securityLevel;
	}

}
