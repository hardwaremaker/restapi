package com.heliumv.tools;

import java.util.ArrayList;
import java.util.List;

public class SelectChain<T> implements ISelect<T> {

	private List<SelectOrGroup> chain;
	
	private List<SelectOrGroup> getChain() {
		if (chain == null) {
			chain = new ArrayList<SelectOrGroup>();
		}
		return chain;
	}
	
	public SelectChain<T> and(ISelect<T> selector) {
		getChain().add(new SelectOrGroup(selector));
		return this;
	}

	public SelectChain<T> or(ISelect<T> selector) {
		if (getChain().isEmpty()) {
			return and(selector);
		}
		
		SelectOrGroup lastSelector = getChain().get(getChain().size() - 1);
		lastSelector.add(selector);
		
		return this;
	}
	
	@Override
	public boolean select(T element) {
		for (ISelect<T> selector : getChain()) {
			if (!selector.select(element)) {
				return false;
			}
		}
		return true;
	}
	
	private class SelectOrGroup implements ISelect<T> {
		private List<ISelect<T>> orSelectors;
		
		public SelectOrGroup(ISelect<T> selector) {
			orSelectors = new ArrayList<ISelect<T>>();
			orSelectors.add(selector);
		}
		
		@Override
		public boolean select(T element) {
			for (ISelect<T> selector : orSelectors) {
				if (selector.select(element)) {
					return true;
				}
			}
			return false;
		}
		
		public void add(ISelect<T> selector) {
			orSelectors.add(selector);
		}
	}
}
