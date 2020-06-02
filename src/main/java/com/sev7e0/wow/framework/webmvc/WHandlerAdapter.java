package com.sev7e0.wow.framework.webmvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Title:  WHandlerAdapter.java
 * description: TODO
 *
 * @author sev7e0
 * @version 1.0
 * @since 2020-05-30 22:33
 **/

public class WHandlerAdapter {

	public boolean support(WHandlerMapping mapping) {
		return false;
	}

	public WModelAndView handler(HttpServletRequest request, HttpServletResponse response, WHandlerMapping handler) {
		return null;
	}
}
