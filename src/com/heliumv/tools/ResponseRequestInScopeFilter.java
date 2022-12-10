package com.heliumv.tools;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ResponseRequestInScopeFilter implements Filter {
	private ThreadLocal<HttpServletResponse> responses = new ThreadLocal<HttpServletResponse>();
	private ThreadLocal<HttpServletRequest> requests = new ThreadLocal<HttpServletRequest>();
	
	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		responses.set(response);
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		requests.set(request);
		
		filterChain.doFilter(servletRequest, servletResponse);
		
		responses.remove();
		requests.remove();
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {		
	}

	/** Only to be used by the BeanFactory */
    public HttpServletResponse getHttpServletResponse() {
        return responses.get();
    }
    
	/** Only to be used by the BeanFactory */
    public HttpServletRequest getHttpServletRequest() {
        return requests.get();
    }
}
