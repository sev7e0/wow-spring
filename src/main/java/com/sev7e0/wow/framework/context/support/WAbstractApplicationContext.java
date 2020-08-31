package com.sev7e0.wow.framework.context.support;

import com.sev7e0.wow.framework.beans.config.WBeanDefinition;
import com.sev7e0.wow.framework.core.support.WDefaultListableBeanFactory;

import java.util.List;

/**
 * Title:  WAbstractApplicationContext.java
 * description: IoC容器的顶层抽象类，定义了一些容器的公共方法，本例中暂时只提供一个
 * 最基本的容器入口方法{@link #refresh()}。
 *
 * @author sev7e0
 * @version 1.0
 * @since 2020-05-27 22:59
 **/

public abstract class WAbstractApplicationContext extends WDefaultListableBeanFactory {

	/**
	 * 只能由子类实现
	 *
	 * @throws Exception e
	 */
	protected void refresh() throws Exception {
	}

	protected void prepareBeanPostProcessor(List<WBeanDefinition> beanDefinitions) {
		if (beanDefinitions.isEmpty())
			return;
		for (WBeanDefinition beanDefinition : beanDefinitions) {

			WBeanDefinition beanDefinition1 = beanDefinitionMap.get(beanDefinition.getBeanClassName());
		}

	}

}
