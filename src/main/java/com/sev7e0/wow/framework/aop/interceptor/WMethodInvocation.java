package com.sev7e0.wow.framework.aop.interceptor;

import com.sev7e0.wow.framework.aop.aspect.WJoinPoint;

import java.lang.reflect.Method;

/**
 * Title:  WMethodInvocation.java
 * description: TODO
 *
 * @author sev7e0
 * @version 1.0
 * @since 2020-06-04 23:05
 **/

public class WMethodInvocation implements WJoinPoint {

	@Override
	public Method getMethod() {
		return null;
	}

	@Override
	public Object[] getArguments() {
		return new Object[0];
	}

	@Override
	public Object getThis() {
		return null;
	}

	@Override
	public void setUserAttribute(String key, Object value) {

	}

	@Override
	public Object getUserAttribute(String key) {
		return null;
	}
}
