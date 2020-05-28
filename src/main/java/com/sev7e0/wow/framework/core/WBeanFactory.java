package com.sev7e0.wow.framework.core;


/**
 * Title:  WBeanFactory.java
 * description: ioc的基础类，在Spring中也是最高级的抽象，根据工厂模式实现，包含了各种bean的定义，控制bean的生命
 * 周期，维护bean之间的关系。
 *
 * @author sev7e0
 * @version 1.0
 * @since 2020-05-27 22:25
 **/

public interface WBeanFactory {

	/**
	 * 根据beanName从容器中获取一个实例Bean
	 *
	 * @param beanName beanName
	 * @return 实例bean
	 * @throws Exception e
	 */
	Object getBean(String beanName) throws Exception;

	/**
	 * 根据bean类获取一个实例
	 *
	 * @param beanClass bean class
	 * @return 实例bean
	 * @throws Exception e
	 */
	Object getBean(Class<?> beanClass) throws Exception;
}
