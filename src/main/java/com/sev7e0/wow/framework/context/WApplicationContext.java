package com.sev7e0.wow.framework.context;

import com.sev7e0.wow.framework.annotation.WAutowired;
import com.sev7e0.wow.framework.annotation.WController;
import com.sev7e0.wow.framework.annotation.WService;
import com.sev7e0.wow.framework.beans.WBeanWrapper;
import com.sev7e0.wow.framework.beans.config.WBeanDefinition;
import com.sev7e0.wow.framework.beans.config.WBeanPostProcessor;
import com.sev7e0.wow.framework.beans.support.WBeanDefinitionReader;
import com.sev7e0.wow.framework.context.support.WDefaultListableBeanFactory;
import com.sev7e0.wow.framework.core.WBeanFactory;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Title:  WApplicationContext.java
 * description: TODO
 *
 * @author sev7e0
 * @version 1.0
 * @since 2020-05-27 23:36
 **/

@Slf4j
public class WApplicationContext extends WDefaultListableBeanFactory implements WBeanFactory {

	private final String[] configLocations;

	private WBeanDefinitionReader beanDefinitionReader;

	//用于缓存单例的IoC容器
	private final Map<String, Object> factoryBeanObjectCache = new ConcurrentHashMap<>();

	//用于缓存通用包装过的Bean的IoC容器
	private final Map<String, WBeanWrapper> factoryBeanInstanceCache = new ConcurrentHashMap<>();

	public WApplicationContext(String... configLocations) {
		this.configLocations = configLocations;
		try {
			refresh();
		} catch (Exception e) {
			log.error("init WApplicationContext error: {}", e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	protected void refresh() throws Exception {
		/*
		 * 容器初始化主要流程
		 */
		//1. 定位配置文件
		beanDefinitionReader = new WBeanDefinitionReader(this.configLocations);

		//2. 加载配置文件 3. 解析（此步在当前实现中使用`注解方式`免去了解析xml等过程，针对不同的方式有不同的策略）
		List<WBeanDefinition> beanDefinitions = beanDefinitionReader.loadBeanDefinitions();

		//4. 向容器注册（在spring中会有多种不同的注册策略，需要提前分配）
		doRegisterBeanDefinition(beanDefinitions);
		/*
		 * 针对不是延迟初始化的对象（lazyInit设置为false）调用getBean执行第一次初始化
		 */
		doAutoWired();
	}

	/**
	 * 向容器注册，也就是添加到父类{@link WDefaultListableBeanFactory#beanDefinitionMap)}中。
	 * <p>
	 * 在spring中会有多种不同的注册策略，需要提前分配。
	 *
	 * @param beanDefinitions 由{@link WBeanDefinitionReader}封装的beanDefinitions集合，
	 *                        主要包括了全类名、是否延迟初始化、以及在容器中的名字。
	 */
	private void doRegisterBeanDefinition(List<WBeanDefinition> beanDefinitions) throws Exception {
		for (WBeanDefinition beanDefinition : beanDefinitions) {
			if (super.beanDefinitionMap.containsKey(beanDefinition.getBeanFactoryName())) {
				throw new Exception("");
			}
			//如果beanDefinitionMap中不存在那么就将其注册到容器中。
			super.beanDefinitionMap.put(beanDefinition.getBeanFactoryName(), beanDefinition);
		}

	}

	/**
	 * 针对不是延迟初始化的对象（lazyInit设置为false，表示注册到容器后就进行初始化）调用getBean执行第一次初始化
	 */
	private void doAutoWired() throws Exception {

		for (Map.Entry<String, WBeanDefinition> entry : super.beanDefinitionMap.entrySet()) {
			//判断其lazyInit的状态
			if (!entry.getValue().isLazyInit()) {
				getBean(entry.getKey());
			}
		}

	}


	@Override
	public Object getBean(Class<?> beanClass) throws Exception {
		return getBean(beanClass.getName());
	}

	/**
	 * 进行对象初始化的步骤，无论是第一次使用调用，还是延迟加载关闭都是通过这个方法进行实现的。
	 * 这也是DI的入口方法。
	 * <p>
	 * 在spring中通过反射将对象生成，并且使用{@link WBeanWrapper}进行装饰。
	 *
	 * @param beanName beanName
	 * @return
	 * @throws Exception
	 */
	@Override
	public Object getBean(String beanName) throws Exception {

		WBeanDefinition beanDefinition = super.beanDefinitionMap.get(beanName);

		WBeanPostProcessor postProcessor = new WBeanPostProcessor();

		Object bean = instantiateBean(beanDefinition);

		if (Objects.isNull(bean))return null;

		postProcessor.postProcessorBeforeInitialization(bean, beanName);


		WBeanWrapper wBeanWrapper = new WBeanWrapper();
		wBeanWrapper.setWrappedInstance(bean);

		postProcessor.postProcessorAfterInitialization(bean, beanName);

		//DI的主要工作，根据类中的字段进行判断类型进行注入
		populateBean(beanName, bean);

		return this.factoryBeanInstanceCache.get(beanName).getWrappedInstance();
	}

	private void populateBean(String beanName, Object bean) {
		Class<?> beanClass = bean.getClass();
		if (!beanClass.isAnnotationPresent(WController.class) || !beanClass.isAnnotationPresent(WService.class)){
			return;
		}
		Field[] classFields = beanClass.getFields();
		for (Field field : classFields){
			if (field.isAnnotationPresent(WAutowired.class)){
				WAutowired autowired = field.getAnnotation(WAutowired.class);
				String fieldName = autowired.value().trim();
				if ("".equals(fieldName)){
					fieldName = field.getType().getName();
				}
				field.setAccessible(true);
				try {
					field.set(bean, this.factoryBeanInstanceCache.get(fieldName).getWrappedInstance());
				} catch (IllegalAccessException e) {
					log.error("Dependency injection error：{}",e.getMessage());
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 *
	 * @param beanDefinition
	 * @return
	 * @throws ClassNotFoundException
	 */
	private Object instantiateBean(WBeanDefinition beanDefinition) throws ClassNotFoundException {
		String beanClassName = beanDefinition.getBeanClassName();
		Object instance = null;
		//首先先从缓存中获取
		if (this.factoryBeanObjectCache.containsKey(beanClassName)){
			return this.factoryBeanObjectCache.get(beanClassName);
		}else {
			//如果缓存中没有该对象那么就反射出来一个
			Class<?> beanClass = Class.forName(beanClassName);
			try {
				instance = beanClass.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				log.error("Get new instance error: {}", e.getMessage());
				e.printStackTrace();
			}
			//将新实例化的对象放入缓存中
			this.factoryBeanObjectCache.put(beanClassName, instance);
		}
		return instance;
	}

	/**
	 * 获取配置文件信息
	 *
	 * @return 系统配置文件
	 */
	public Properties getProperties() {
		return this.beanDefinitionReader.getProperties();
	}

}
