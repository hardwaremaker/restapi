package com.heliumv.factory.impl;

public class TransformParamValue {

	public static <G> G transform(String value) {
		return null;
	}
	
	public static <G> G transform(String value, G defaultResult) {
		G result = transform(value);
		return result == null ? defaultResult : result;
	}
}
