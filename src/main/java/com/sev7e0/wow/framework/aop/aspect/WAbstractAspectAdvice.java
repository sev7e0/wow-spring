package com.sev7e0.wow.framework.aop.aspect;

import java.lang.reflect.Method;

/**
 * Title:  WAbstractAspectAdvice.java
 * description: TODO
 *
 * @author sev7e0
 * @version 1.0
 * @since 2020-06-07 18:21
 **/

public class WAbstractAspectAdvice {

	private Method method;
	private Object target;

	public WAbstractAspectAdvice(Method method, Object target) {
		this.method = method;
		this.target = target;
	}

	protected Object invokeAdviceMethod(WJoinPoint joinPoint, Object returnValue, Throwable throwable) throws Throwable{
		return null;
	}

}
