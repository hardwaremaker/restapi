package com.heliumv.tools;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

public class HvStringSerializer extends JsonSerializer<String> {
	@Override
	public void serialize(String value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		jgen.writeString(value) ;
	}

	@Override
	public Class<String> handledType() {
//		return super.handledType();
		return String.class ;
	}
}
