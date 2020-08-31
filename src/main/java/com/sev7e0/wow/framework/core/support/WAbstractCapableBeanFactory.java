package com.sev7e0.wow.framework.core.support;

import com.sev7e0.wow.framework.beans.config.WBeanPostProcessor;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Title:  WAbstractAutowireCapableBeanFactory.java
 * description: TODO
 *
 * @author sev7e0
 * @version 1.0
 * @since 2020-06-29 15:58
 **/

public abstract class WAbstractCapableBeanFactory {

	private final List<WBeanPostProcessor> beanPostProcessors = new CopyOnWriteArrayList<>();

	public List<WBeanPostProcessor> getBeanPostProcessors() {
		return beanPostProcessors;
	}

	public void addBeanPostProcessor(WBeanPostProcessor beanPostProcessor){
		beanPostProcessors.remove(beanPostProcessor);
		beanPostProcessors.add(beanPostProcessor);
	}

}
