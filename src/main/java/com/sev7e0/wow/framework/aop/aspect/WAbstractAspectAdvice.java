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

	private final Method method;
	private final Object target;

	public WAbstractAspectAdvice(Method method, Object target) {
		this.method = method;
		this.target = target;
	}

	/**
	 * 模板方法
	 * @param joinPoint
	 * @param returnValue
	 * @param throwable
	 * @throws Throwable
	 */
	protected void invokeAdviceMethod(WJoinPoint joinPoint, Object returnValue, Throwable throwable) throws Throwable {
		Class<?>[] parameterTypes = this.method.getParameterTypes();
		if (parameterTypes.length == 0) {
			method.invoke(target);
			return;
		}
		Object[] objects = new Object[parameterTypes.length];
		for (int i = 0; i < parameterTypes.length; i++) {
			if (parameterTypes[i] == WJoinPoint.class) {
				objects[i] = joinPoint;
			} else if (parameterTypes[i] == Throwable.class) {
				objects[i] = throwable;
			} else if (parameterTypes[i] == Object.class) {
				objects[i] = returnValue;
			}
		}
		method.invoke(target, objects);
	}

}
