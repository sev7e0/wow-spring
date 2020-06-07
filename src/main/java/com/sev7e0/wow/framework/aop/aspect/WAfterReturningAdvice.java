package com.sev7e0.wow.framework.aop.aspect;

import com.sev7e0.wow.framework.aop.interceptor.WMethodInterceptor;
import com.sev7e0.wow.framework.aop.interceptor.WMethodInvocation;

import java.lang.reflect.Method;

/**
 * Title:  WAfterReturningAdvice.java
 * description: TODO
 *
 * @author sev7e0
 * @version 1.0
 * @since 2020-06-07 18:28
 **/

public class WAfterReturningAdvice extends WAbstractAspectAdvice implements WAdvice, WMethodInterceptor {


	public WAfterReturningAdvice(Method method, Object target) {
		super(method, target);
	}

	@Override
	public Object invoke(WMethodInvocation methodInvocation) throws Throwable {
		return null;
	}
}
