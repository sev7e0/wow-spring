package com.sev7e0.wow.framework.context;

/**
 * Title:  WAbstractApplicationContext.java
 * description: IoC容器的顶层抽象类，定义了一些容器的公共方法，本例中暂时只提供一个
 * 最基本的容器入口方法{@link #refresh()}。
 *
 * @author sev7e0
 * @version 1.0
 * @since 2020-05-27 22:59
 **/

public abstract class WAbstractApplicationContext {

	/**
	 * 只能由子类实现
	 *
	 * @throws Exception e
	 */
	protected void refresh() throws Exception {
	}

}
