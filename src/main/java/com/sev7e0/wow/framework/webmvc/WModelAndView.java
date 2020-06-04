package com.sev7e0.wow.framework.webmvc;

import java.util.Map;

/**
 * Title:  WModelAndView.java
 * description: TODO
 *
 * @author sev7e0
 * @version 1.0
 * @since 2020-06-03 00:09
 **/

public class WModelAndView {

	private String viewName;
	private Map<String, ?> model;

	public WModelAndView(String viewName, Map<String, ?> model) {
		this.viewName = viewName;
		this.model = model;
	}

	public String getViewName() {
		return viewName;
	}

	public Map<String, ?> getModel() {
		return model;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	public void setModel(Map<String, ?> model) {
		this.model = model;
	}
}
