package com.sev7e0.wow.framework.beans.config;

/**
 * Title:  WBeanDefinition.java
 * description: WBeanDefinition主要用来保存由xml或者注解等方式声明的bean的相关信息。
 *
 * @author sev7e0
 * @version 1.0
 * @since 2020-05-27 22:39
 **/

public class WBeanDefinition {

	//bean类的全类名
	private String beanClassName;

	//是够延迟初始化
	private boolean lazyInit = true;

	//在IoC容器中的名字
	private String beanFactoryName;

	public String getBeanClassName() {
		return beanClassName;
	}

	public void setBeanClassName(String beanClassName) {
		this.beanClassName = beanClassName;
	}

	public boolean isLazyInit() {
		return lazyInit;
	}

	public void setLazyInit(boolean lazyInit) {
		this.lazyInit = lazyInit;
	}

	public String getBeanFactoryName() {
		return beanFactoryName;
	}

	public void setBeanFactoryName(String beanFactoryName) {
		this.beanFactoryName = beanFactoryName;
	}
}
