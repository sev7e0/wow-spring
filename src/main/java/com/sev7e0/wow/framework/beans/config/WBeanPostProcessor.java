package com.sev7e0.wow.framework.beans.config;

/**
 * Title:  WBeanPostProcessor.java
 * description: TODO
 *
 * @author sev7e0
 * @version 1.0
 * @since 2020-05-28 23:59
 **/

public interface WBeanPostProcessor {


	default Object postProcessorBeforeInitialization(Object bean, String beanName) {
		return bean;
	}


	default Object postProcessorAfterInitialization(Object bean, String beanName) {
		return bean;
	}
}
