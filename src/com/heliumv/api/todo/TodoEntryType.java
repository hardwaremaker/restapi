package com.heliumv.api.todo;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public enum TodoEntryType {
	NOTINITIALIZED,
	PROJECT,
	ORDER,
	PRODUCTION
}
