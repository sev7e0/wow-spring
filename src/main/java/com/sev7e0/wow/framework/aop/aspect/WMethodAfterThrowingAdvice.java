package com.sev7e0.wow.framework.aop.aspect;

import com.sev7e0.wow.framework.aop.interceptor.WMethodInterceptor;
import com.sev7e0.wow.framework.aop.interceptor.WMethodInvocation;

import java.lang.reflect.Method;

/**
 * Title:  WAfterThrowingAdvice.java
 * description: TODO
 *
 * @author sev7e0
 * @version 1.0
 * @since 2020-06-07 18:29
 **/

public class WMethodAfterThrowingAdvice extends WAbstractAspectAdvice implements WAdvice, WMethodInterceptor {

	private String throwName;

	public WMethodAfterThrowingAdvice(Method method, Object target) {
		super(method, target);
	}

	public void setThrowName(String throwName) {
		this.throwName = throwName;
	}

	@Override
	public Object invoke(WMethodInvocation methodInvocation) throws Throwable {
		try {
			return methodInvocation.proceed();
		} catch (Throwable throwable) {
			invokeAdviceMethod(methodInvocation, null, throwable);
			throw throwable;
		}
	}
}
