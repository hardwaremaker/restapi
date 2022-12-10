package com.heliumv.api.worktime;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public enum TimeRecordingBatchEnum {
	Start,
	Stop,
	Pause,
	Coming,
	Leaving,
	Text,
	Media
}
