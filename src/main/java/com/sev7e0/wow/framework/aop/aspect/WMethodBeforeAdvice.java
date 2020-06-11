package com.sev7e0.wow.framework.aop.aspect;

import com.sev7e0.wow.framework.aop.interceptor.WMethodInterceptor;
import com.sev7e0.wow.framework.aop.interceptor.WMethodInvocation;
import lombok.SneakyThrows;

import java.lang.reflect.Method;

/**
 * Title:  WMethodBeforeAdvice.java
 * description: TODO
 *
 * @author sev7e0
 * @version 1.0
 * @since 2020-06-07 18:25
 **/

public class WMethodBeforeAdvice extends WAbstractAspectAdvice implements WAdvice, WMethodInterceptor {
	private WJoinPoint joinPoint;

	public WMethodBeforeAdvice(Method method, Object target) {
		super(method, target);
	}

	@SneakyThrows
	public void before(Method method, Object[] args, Object target) {
		//调用模板方法
		invokeAdviceMethod(this.joinPoint, null, null);
	}

	/**
	 *
	 * @param methodInvocation
	 * @return
	 * @throws Throwable
	 */
	@Override
	public Object invoke(WMethodInvocation methodInvocation) throws Throwable {
		this.joinPoint = methodInvocation;
		this.before(methodInvocation.getMethod(), methodInvocation.getArguments(), methodInvocation.getThis());
		return methodInvocation.proceed();
	}
}
