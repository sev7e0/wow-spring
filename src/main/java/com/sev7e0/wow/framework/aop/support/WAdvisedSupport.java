package com.sev7e0.wow.framework.aop.support;

import com.sev7e0.wow.framework.aop.aspect.WMethodAfterReturningAdvice;
import com.sev7e0.wow.framework.aop.aspect.WMethodAfterThrowingAdvice;
import com.sev7e0.wow.framework.aop.aspect.WMethodBeforeAdvice;

import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Title:  WAdvisedSupport.java
 * description: 与spring IoC一样整个aop流程最开始阶段还是加载配置
 * 这其中包括{@link WAopConfig}中所有的属性
 *
 * @author sev7e0
 * @version 1.0
 * @since 2020-06-04 23:18
 **/

public class WAdvisedSupport {

	private Class<?> targetClass;
	private Object target;
	private Pattern pointCutClassPatter;
	private transient Map<Method, List<Object>> methodCache;
	private WAopConfig config;


	/**
	 * 默认构造方法
	 *
	 * @param config    加载自定义aop配置
	 * @param instance
	 * @param beanClass
	 */
	public WAdvisedSupport(WAopConfig config, Object instance, Class<?> beanClass) {
		this.config = config;
		this.target = instance;
		this.targetClass = beanClass;
		parse();
	}

	public WAdvisedSupport(WAopConfig config) {
		this.config = config;
	}

	/**
	 * 主要是根据AOP配置，将需要回调的方法封装成一个拦截器链并对外提供。
	 *
	 * @param method      方法对象
	 * @param targetClass 方法所在目标累
	 * @return 返回拦截器链
	 * @throws NoSuchMethodException 当前类中没有找到这个方法时抛出
	 */
	public List<Object> getInterceptorAndDynamicInterceptionAdvice(Method method, Class<?> targetClass) throws NoSuchMethodException {
		List<Object> matchersList = methodCache.get(method);


		if (Objects.isNull(matchersList)) {
			Method classMethod = targetClass.getMethod(method.getName(), method.getParameterTypes());
			//TODO
			matchersList = methodCache.get(classMethod);
			this.methodCache.put(classMethod, matchersList);
		}
		return matchersList;
	}

	/**
	 * 用来判断目标类是否满足切面规则
	 *
	 * @return 是否满足
	 */
	public boolean pointCutMatch() {
		return pointCutClassPatter.matcher(this.targetClass.toString()).matches();
	}


	private void parse() {
		String pointCut = config.getPointCut()
			.replaceAll("\\.", "\\\\.")
			.replaceAll("\\\\.\\*", ".*")
			.replaceAll("\\(", "\\\\(")
			.replaceAll("\\)", "\\\\)");
		//pointCut=public .* com.gupaoedu.vip.spring.demo.service..*Service..*(.*)
		String pointCutForClassRegex = pointCut.substring(0, pointCut.lastIndexOf("\\(") - 4);
		pointCutClassPatter = Pattern.compile("class " + pointCutForClassRegex.substring(
			pointCutForClassRegex.lastIndexOf(" ") + 1));

		try {

			methodCache = new HashMap<>();
			Pattern pattern = Pattern.compile(pointCut);


			Class aspectClass = Class.forName(this.config.getAspectClass());
			Map<String, Method> aspectMethods = new HashMap<>();
			for (Method m : aspectClass.getMethods()) {
				aspectMethods.put(m.getName(), m);
			}
			for (Method m : this.targetClass.getMethods()) {
				String methodString = m.toString();
				if (methodString.contains("throws")) {
					methodString = methodString.substring(0, methodString.lastIndexOf("throws")).trim();
				}

				Matcher matcher = pattern.matcher(methodString);
				if (matcher.matches()) {
					//执行器链
					List<Object> advices = new LinkedList<Object>();
					//把每一个方法包装成 MethodIterceptor
					//before
					if (!(null == config.getAspectBefore() || "".equals(config.getAspectBefore()))) {
						//创建一个Advivce
						advices.add(new WMethodBeforeAdvice(aspectMethods.get(config.getAspectBefore()), aspectClass.newInstance()));
					}
					//after
					if (!(null == config.getAspectAfter() || "".equals(config.getAspectAfter()))) {
						//创建一个Advivce
						advices.add(new WMethodAfterReturningAdvice(aspectMethods.get(config.getAspectAfter()), aspectClass.newInstance()));
					}
					//afterThrowing
					if (!(null == config.getAspectThrow() || "".equals(config.getAspectThrow()))) {
						//创建一个Advivce
						WMethodAfterThrowingAdvice throwingAdvice =
							new WMethodAfterThrowingAdvice(
								aspectMethods.get(config.getAspectThrow()),
								aspectClass.newInstance());
						throwingAdvice.setThrowName(config.getAspectAfterThrowName());
						advices.add(throwingAdvice);
					}
					methodCache.put(m, advices);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	public Map<Method, List<Object>> getMethodCache() {
		return methodCache;
	}

	public void setMethodCache(Map<Method, List<Object>> methodCache) {
		this.methodCache = methodCache;
	}

	public WAopConfig getConfig() {
		return config;
	}

	public void setConfig(WAopConfig config) {
		this.config = config;
	}

	public Class<?> getTargetClass() {
		return targetClass;
	}

	public void setTargetClass(Class<?> targetClass) {
		this.targetClass = targetClass;
	}

	public Object getTarget() {
		return target;
	}

	public void setTarget(Object target) {
		this.target = target;
	}

	public Pattern getPointCutClassPatter() {
		return pointCutClassPatter;
	}

	public void setPointCutClassPatter(Pattern pointCutClassPatter) {
		this.pointCutClassPatter = pointCutClassPatter;
	}
}
