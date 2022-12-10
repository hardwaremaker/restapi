package com.heliumv.session;

public class HvSessionManagerWithoutContext extends HvSessionManager {
	
	private HvSessionMap sessionMap ;
	
	@Override
	protected HvSessionMap getMap() {
		if(sessionMap == null) {
			sessionMap = new HvSessionMap() ;
		}
		
		return sessionMap ;
	}
}
