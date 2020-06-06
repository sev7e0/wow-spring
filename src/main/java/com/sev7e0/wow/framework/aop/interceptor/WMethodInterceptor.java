package com.sev7e0.wow.framework.aop.interceptor;

/**
 * Title:  WMethodInterceptor.java
 * description: TODO
 *
 * @author sev7e0
 * @version 1.0
 * @since 2020-06-04 23:04
 **/

public interface WMethodInterceptor {

	Object invoke(WMethodInvocation methodInvocation) throws Throwable;
}
