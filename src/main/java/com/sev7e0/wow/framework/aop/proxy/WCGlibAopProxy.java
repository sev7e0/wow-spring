package com.sev7e0.wow.framework.aop.proxy;

import com.sev7e0.wow.framework.aop.support.WAdvisedSupport;

/**
 * Title:  WCGlibAopProxt.java
 * description: TODO
 *
 * @author sev7e0
 * @version 1.0
 * @since 2020-06-04 23:20
 **/

public class WCGlibAopProxy implements WAopProxy {

	private final WAdvisedSupport config;

	public WCGlibAopProxy(WAdvisedSupport config) {
		this.config = config;
	}

	@Override
	public Object getProxy() {
		return null;
	}

	@Override
	public Object getProxy(ClassLoader classLoader) {
		return null;
	}
}
