package com.heliumv.tools;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HvHttpServletHelper {
	private HttpServletRequest request;
	private HttpServletResponse response;
	
	public HvHttpServletHelper() {
	}

	public HvHttpServletHelper(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}
	
	public HttpServletRequest getRequest() {
		return this.request;
	}
	
	public HttpServletResponse getResponse() {
		return this.response;
	}
}
