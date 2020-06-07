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

	private Object proxy;
	private Object target;
	private Method method;
	private Object[] args;
	private Class<?> targetClass;
	private List<Object> objectList;

	private Map<String, Object> customAttribute;


	public WMethodInvocation(Object proxy, Object target, Method method, Object[] args, Class<?> targetClass, List<Object> objectList) {

		this.proxy = proxy;
		this.target = target;
		this.method = method;
		this.args = args;
		this.targetClass = targetClass;
		this.objectList = objectList;
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
		if (Objects.nonNull(value)){
			if (Objects.isNull(customAttribute)){
				customAttribute = new HashMap<>();
			}
			customAttribute.put(key, value);
		}else {
			if (Objects.nonNull(customAttribute)){
				customAttribute.remove(key);
			}
		}

	}

	@Override
	public Object getUserAttribute(String key) {
		if (Objects.nonNull(customAttribute)) return customAttribute.get(key);
		return null;
	}

	/**
	 *
	 * @return
	 * @throws Throwable
	 */
	public Object proceed() throws Throwable {
		int index = 0;

		if (this.objectList.isEmpty()){
			return method.invoke(this.target, this.args);
		}
		Object advice = this.objectList.get(index);
		if (advice instanceof WMethodInterceptor){
			WMethodInterceptor interceptor = (WMethodInterceptor) advice;
			return interceptor.invoke(this);
		}else {
			return proceed();
		}
	}
}
