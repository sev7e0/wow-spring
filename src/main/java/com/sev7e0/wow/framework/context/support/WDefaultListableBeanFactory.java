package com.sev7e0.wow.framework.context.support;

import com.sev7e0.wow.framework.beans.config.WBeanDefinition;
import com.sev7e0.wow.framework.context.WAbstractApplicationContext;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Title:  WDefaultListableBeanFactory.java
 * description: IoC容器实现的典型代表，本例中暂时只用来缓存bean的元数据信息的功能，提高在初始化对象时的获取速度
 * <p>
 * 在spring中还还实现了 {@link @ConfigurableListableBeanFactory}和{@link @BeanDefinitionRegistry}
 * 接口，是一个成熟的IoC容器实现。
 *
 * @author sev7e0
 * @version 1.0
 * @since 2020-05-27 23:13
 **/

public class WDefaultListableBeanFactory extends WAbstractApplicationContext {

	/**
	 * 基于WBeanDefinition映射表
	 */
	protected ConcurrentHashMap<String, WBeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

}
