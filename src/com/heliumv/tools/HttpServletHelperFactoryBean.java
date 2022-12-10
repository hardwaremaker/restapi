package com.heliumv.tools;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

// TODO FactoryBean ohne <Parameter> wegen enunciate!
public class HttpServletHelperFactoryBean implements FactoryBean, ApplicationContextAware {
	@Autowired 
	private ResponseRequestInScopeFilter responseRequestInScopeFilter;
	private ApplicationContext applicationContext ;

	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		this.applicationContext = arg0;
	}

	public HvHttpServletHelper getObject() throws Exception {
		return new HvHttpServletHelper(
				responseRequestInScopeFilter.getHttpServletRequest(),
				responseRequestInScopeFilter.getHttpServletResponse());
	}

	public Class<HvHttpServletHelper> getObjectType() {
		return HvHttpServletHelper.class;
	}


	public boolean isSingleton() {
		return false;
	}
}
