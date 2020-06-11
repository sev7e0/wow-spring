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

	private WJoinPoint joinPoint;

	public WAfterReturningAdvice(Method method, Object target) {
		super(method, target);
	}

	@Override
	public Object invoke(WMethodInvocation methodInvocation) throws Throwable {
		Object returnV = methodInvocation.proceed();
		this.joinPoint = methodInvocation;
		this.afterReturning(returnV, joinPoint.getMethod(), joinPoint.getArguments(), joinPoint.getThis());
		return returnV;
	}

	public void afterReturning(Object returnValue, Method method, Object[] args, Object target) {
		try {
			invokeAdviceMethod(this.joinPoint, returnValue, null);
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
	}
}
