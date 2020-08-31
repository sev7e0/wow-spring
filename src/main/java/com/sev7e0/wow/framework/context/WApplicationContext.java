package com.sev7e0.wow.framework.context;

import com.sev7e0.wow.framework.annotation.WAutowired;
import com.sev7e0.wow.framework.annotation.WController;
import com.sev7e0.wow.framework.annotation.WService;
import com.sev7e0.wow.framework.aop.proxy.WAopProxy;
import com.sev7e0.wow.framework.aop.proxy.WCGlibAopProxy;
import com.sev7e0.wow.framework.aop.proxy.WJdkAopProxy;
import com.sev7e0.wow.framework.aop.support.WAdvisedSupport;
import com.sev7e0.wow.framework.aop.support.WAopConfig;
import com.sev7e0.wow.framework.beans.WBeanWrapper;
import com.sev7e0.wow.framework.beans.config.WBeanDefinition;
import com.sev7e0.wow.framework.beans.support.IWBeanDefinitionReader;
import com.sev7e0.wow.framework.beans.support.WBeanDefinitionReader;
import com.sev7e0.wow.framework.context.support.WAbstractApplicationContext;
import com.sev7e0.wow.framework.core.WBeanFactory;
import com.sev7e0.wow.framework.core.support.WDefaultListableBeanFactory;
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
public class WApplicationContext extends WAbstractApplicationContext implements WBeanFactory {

	private final String[] configLocations;

	private IWBeanDefinitionReader beanDefinitionReader;

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

	/**
	 * 容器初始化主入口
	 *
	 * @throws Exception e
	 */
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
	 * 向容器注册，也就是添加到父类{@link WDefaultListableBeanFactory#beanDefinitionMap}中。
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


	/**
	 * 通过类获取一个实例，内部通过类名再调用{@link #getBean(String)}方法获取
	 *
	 * @param beanClass bean class
	 * @return 在spring中通过反射将对象生成，并且使用{@link WBeanWrapper}进行装饰。
	 * @throws Exception e
	 */
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

		Object bean = instantiateBean(beanDefinition);
		if (Objects.isNull(bean)) return null;
		WBeanWrapper wBeanWrapper = new WBeanWrapper();
		wBeanWrapper.setWrappedInstance(bean);
		this.factoryBeanInstanceCache.put(beanName, wBeanWrapper);

		//DI的主要工作，根据类中的字段进行判断类型进行注入
		//当前模式也就是bean在实例化后并没有进行依赖注入，只有在第一次调用时才会注入
		populateBean(bean);

		return this.factoryBeanInstanceCache.get(beanName);
	}

	/**
	 * DI（依赖注入）主要工作，主要就是判断当前的对象是否添加了注解
	 * 对添加了注解的类获取其{@link Field}取到所有属性，并判断其是否使用了
	 * {@link WAutowired}注解，是，则将其注入实例化后的bean，从{@link #factoryBeanInstanceCache}
	 * 中获取。
	 *
	 * @param bean
	 */
	private void populateBean(Object bean) throws IllegalAccessException {
		Class<?> beanClass = bean.getClass();
		if (beanClass.isAnnotationPresent(WController.class) || beanClass.isAnnotationPresent(WService.class)) {
			Field[] classFields = beanClass.getDeclaredFields();
			for (Field field : classFields) {
				field.setAccessible(true);
				//判断，如果其字段属性都已经不为null那么将不在进行注入
				if (Objects.nonNull(field.get(bean))) return;
				if (field.isAnnotationPresent(WAutowired.class)) {
					WAutowired autowired = field.getAnnotation(WAutowired.class);
					String fieldName = autowired.value().trim();
					if ("".equals(fieldName)) {
						fieldName = field.getName();
					}

					try {
						field.set(bean, this.factoryBeanInstanceCache.get(fieldName).getWrappedInstance());
					} catch (IllegalAccessException e) {
						log.error("Dependency injection error：{}", e.getMessage());
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * @param beanDefinition
	 * @return
	 * @throws ClassNotFoundException
	 */
	private Object instantiateBean(WBeanDefinition beanDefinition) throws ClassNotFoundException {
		String beanClassName = beanDefinition.getBeanClassName();
		Object instance = null;
		//首先先从缓存中获取
		if (this.factoryBeanObjectCache.containsKey(beanClassName)) {
			instance = this.factoryBeanObjectCache.get(beanClassName);
		} else {
			//如果缓存中没有该对象那么就反射出来一个
			Class<?> beanClass = Class.forName(beanClassName);
			try {
				instance = beanClass.newInstance();
				WAdvisedSupport advisedSupport = initAopConfig(instance, beanClass);
				if (advisedSupport.pointCutMatch()) {
					instance = createProxy(advisedSupport).getProxy();
				}
			} catch (Exception e) {
				log.error("Get new instance error: {}", e.getMessage());
				e.printStackTrace();
			}
			//将新实例化的对象放入缓存中
			this.factoryBeanObjectCache.put(beanClassName, instance);
		}
		return instance;
	}

	/**
	 * 基于不同
	 *
	 * @param config
	 * @return
	 */
	private WAopProxy createProxy(WAdvisedSupport config) {
		Class<?> targetClass = config.getTargetClass();
		//如果使用实现接口的方式，那么使用jdk代理
		if (targetClass.getInterfaces().length > 0) {
			return new WJdkAopProxy(config);
		} else {
			//没有采用实现接口，那么将采用CGlib
			return new WCGlibAopProxy(config);
		}
	}

	/**
	 * 未完成，初始化配置
	 *
	 * @param instance
	 * @param beanClass
	 * @return
	 * @throws Exception
	 */
	private WAdvisedSupport initAopConfig(Object instance, Class<?> beanClass) throws Exception {
		WAopConfig aopConfig = new WAopConfig();
		aopConfig.setPointCut(beanDefinitionReader.getProperties().getProperty("pointCut"));
		aopConfig.setAspectAfter(beanDefinitionReader.getProperties().getProperty("aspectAfter"));
		aopConfig.setAspectAfterThrowName(beanDefinitionReader.getProperties().getProperty("afterThrowName"));
		aopConfig.setAspectBefore(beanDefinitionReader.getProperties().getProperty("aspectBefore"));
		aopConfig.setAspectClass(beanDefinitionReader.getProperties().getProperty("aspectClass"));
		aopConfig.setAspectThrow(beanDefinitionReader.getProperties().getProperty("afterThrow"));
		return new WAdvisedSupport(aopConfig, instance, beanClass);
	}

	public String[] getBeanDefinitionNames() {
		return this.beanDefinitionMap.keySet().toArray(new String[0]);
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
