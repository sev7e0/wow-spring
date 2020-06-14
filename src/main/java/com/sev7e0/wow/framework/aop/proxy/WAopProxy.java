package com.sev7e0.wow.framework.aop.proxy;

/**
 * Title:  WAopProxy.java
 * description: 代理工厂的顶层入口，提供获取代理对象的顶层入口
 *
 * @author sev7e0
 * @version 1.0
 * @since 2020-06-04 23:19
 **/

public interface WAopProxy {

	/**
	 * 获取一个代理对象
	 *
	 * @return 获得代理对象
	 */
	Object getProxy();

	/**
	 * 定过自定义类加载气获取代理对象
	 *
	 * @param classLoader 自定义类加载器
	 * @return 代理对象
	 */
	Object getProxy(ClassLoader classLoader);
}
