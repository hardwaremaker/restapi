package com.heliumv.api.production;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public enum TestResultMaterialStatus {
	NOTSTARTED,
	INCOMPLETE,
	COMPLETE;
}
