package com.sev7e0.wow.framework.context;

/**
 * Title:  WApplicationContextAware.java
 * description: 通过监听机制的到一个回调方法，从而获取到IoC容器的上下文，
 * <p>
 * 通过实现一个监听器去扫描所有的类，只要实现了该接口那么将会调用其{@link #setApplicationContext(WApplicationContext)}
 * 方法将当前容器注入到目标类中
 *
 * @author sev7e0
 * @version 1.0
 * @since 2020-05-28 23:49
 **/

public interface WApplicationContextAware {

	void setApplicationContext(WApplicationContext applicationContext);
}
