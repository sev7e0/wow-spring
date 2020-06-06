package com.sev7e0.wow.framework.aop.proxy;

/**
 * Title:  WAopProxy.java
 * description: TODO
 *
 * @author sev7e0
 * @version 1.0
 * @since 2020-06-04 23:19
 **/

public interface WAopProxy {

	Object getProxy();
	Object getProxy(ClassLoader classLoader);
}
