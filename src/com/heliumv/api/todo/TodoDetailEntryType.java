package com.heliumv.api.todo;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public enum TodoDetailEntryType {
	NOTINITIALIZED,
	PROJECTDETAIL,
	IDENT,
	MANUAL,
	TEXT,
	ZWS
}
