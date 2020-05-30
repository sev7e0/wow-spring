package com.sev7e0.wow.framework.webmvc;


import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * Title:  WHandlerMapping.java
 * description: TODO
 *
 * @author sev7e0
 * @version 1.0
 * @since 2020-05-30 22:22
 **/

public class WHandlerMapping {

	private Object controller;
	private Method method;
	private Pattern pattern;

	public WHandlerMapping(Object controller, Method method, Pattern pattern) {
		this.controller = controller;
		this.method = method;
		this.pattern = pattern;
	}


	public Object getController() {
		return controller;
	}

	public void setController(Object controller) {
		this.controller = controller;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public Pattern getPattern() {
		return pattern;
	}

	public void setPattern(Pattern pattern) {
		this.pattern = pattern;
	}
}
