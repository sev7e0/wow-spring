package com.sev7e0.wow.framework.aop.proxy;

import com.sev7e0.wow.framework.aop.interceptor.WMethodInvocation;
import com.sev7e0.wow.framework.aop.support.WAdvisedSupport;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * Title:  WJdkAopProxy.java
 * description: 基于jdk动态代理实现 代理类的生成
 *
 * @author sev7e0
 * @version 1.0
 * @since 2020-06-04 23:21
 **/

public class WJdkAopProxy implements WAopProxy, InvocationHandler {
	private final WAdvisedSupport config;

	public WJdkAopProxy(WAdvisedSupport config) {
		this.config = config;
	}

	@Override
	public Object getProxy() {
		return getProxy(config.getTargetClass().getClassLoader());
	}

	@Override
	public Object getProxy(ClassLoader classLoader) {
		return Proxy.newProxyInstance(classLoader, config.getTargetClass().getInterfaces(), this);
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		//获取拦截器链
		List<Object> matchersList = config.getInterceptorAndDynamicInterceptionAdvice(method, config.getTargetClass());
		WMethodInvocation invocation = new WMethodInvocation(proxy, this.config.getTarget(), method, args, config.getTargetClass(), matchersList);
		try {
			return invocation.proceed();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
}
