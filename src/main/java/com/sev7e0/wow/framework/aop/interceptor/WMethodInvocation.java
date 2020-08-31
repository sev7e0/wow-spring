package com.sev7e0.wow.framework.aop.interceptor;

import com.sev7e0.wow.framework.aop.aspect.WJoinPoint;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Title:  WMethodInvocation.java
 * description: TODO
 *
 * @author sev7e0
 * @version 1.0
 * @since 2020-06-04 23:05
 **/

public class WMethodInvocation implements WJoinPoint {

	private final Object proxy;
	private final Object target;
	private final Method method;
	private final Object[] args;
	private final Class<?> targetClass;
	private final List<Object> matchersList;

	private Map<String, Object> customAttribute;


	public WMethodInvocation(Object proxy, Object target, Method method, Object[] args, Class<?> targetClass, List<Object> matchersList) {
		this.proxy = proxy;
		this.target = target;
		this.method = method;
		this.args = args;
		this.targetClass = targetClass;
		this.matchersList = matchersList;
	}

	@Override
	public Method getMethod() {
		return method;
	}

	@Override
	public Object[] getArguments() {
		return args;
	}

	@Override
	public Object getThis() {
		return this.target;
	}

	@Override
	public void setUserAttribute(String key, Object value) {
		if (Objects.nonNull(value)) {
			if (Objects.isNull(customAttribute)) {
				customAttribute = new HashMap<>();
			}
			customAttribute.put(key, value);
		} else {
			if (Objects.nonNull(customAttribute)) {
				customAttribute.remove(key);
			}
		}

	}

	@Override
	public Object getUserAttribute(String key) {
		if (Objects.nonNull(customAttribute)) return customAttribute.get(key);
		return null;
	}

	private int currentInterceptorIndex = -1;

	/**
	 * @return
	 * @throws Throwable
	 */
	public Object proceed() throws Throwable {
		//如果拦截器链为空，或者已经完成了那么直接反射调用方法
		if (this.currentInterceptorIndex == this.matchersList.size() - 1) {
			return this.method.invoke(this.target, this.args);
		}

		Object interceptorOrInterceptionAdvice = this.matchersList.get(++this.currentInterceptorIndex);
		//如果要动态匹配joinPoint
		if (Objects.nonNull(interceptorOrInterceptionAdvice)
			&& interceptorOrInterceptionAdvice instanceof WMethodInterceptor) {
			WMethodInterceptor mi = (WMethodInterceptor) interceptorOrInterceptionAdvice;
			return mi.invoke(this);
		} else {
			//动态匹配失败时,略过当前Intercetpor,调用下 一个Interceptor
			return proceed();
		}
	}
}
