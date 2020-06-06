package com.sev7e0.wow.framework.aop.support;

import com.sev7e0.wow.framework.utils.Strings;

import javax.jws.Oneway;
import javax.management.ReflectionException;
import java.lang.reflect.Method;
import java.time.OffsetDateTime;
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
	 * @param config 加载自定义aop配置
	 */
	public WAdvisedSupport(WAopConfig config) {
		this.config = config;
	}

	/**
	 * 主要是根据AOP配置，将需要回调的方法封装成一个拦截器链并对外提供。
	 * @param method 方法对象
	 * @param targetClass 方法所在目标累
	 * @return 返回拦截器链
	 * @throws NoSuchMethodException 当前类中没有找到这个方法时抛出
	 */
	public List<Object> getInterceptorAndDynamicInterceptionAdvice(Method method, Class<?> targetClass) throws NoSuchMethodException {
		List<Object> objectList = methodCache.get(method);


		if (Objects.isNull(objectList)){
			Method classMethod = targetClass.getMethod(method.getName(), targetClass);
			//TODO
			objectList = methodCache.get(classMethod);
			this.methodCache.put(classMethod, objectList);
		}
		return objectList;
	}

	/**
	 * 用来判断目标类是否满足切面规则
	 * @return 是否满足
	 */
	public boolean pointCutMatch(){
		return pointCutClassPatter.matcher(this.targetClass.toString()).matches();
	}


	private void parse(){
		String pointCut = config.getPointCut().replaceAll("\\.", "\\\\.")
			.replaceAll("\\\\.\\*", ".*")
			.replaceAll("\\(", "\\\\(")
			.replaceAll("\\)", "\\\\)");
		String pointCutForClass = pointCut.substring(0, pointCut.lastIndexOf("\\(") - 4);
		pointCutClassPatter = Pattern.compile("class " + pointCut.substring(pointCutForClass.lastIndexOf(" ") + 1));

		methodCache = new HashMap<>();

		Pattern pattern = Pattern.compile(pointCut);

		try {
			Class<?> aClass = Class.forName(config.getAspectClass());
			HashMap<String, Method> methodHashMap = new HashMap<>();
			for (Method method : aClass.getMethods()){
				methodHashMap.put(method.getName(), method);
			}
			for (Method method : targetClass.getMethods()){
				String methodString = method.toString();
				if (methodString.contains("throws")){
					methodString = methodString.substring(0, methodString.lastIndexOf("throws")).trim();
				}
				Matcher matcher = pattern.matcher(methodString);
				if (matcher.matches()){
					LinkedList<Object> objects = new LinkedList<>();
					if (Strings.noEmpty(config.getAspectBefore()) || Strings.noEmpty(config.getAspectBefore().trim())){
						objects.add(new WMethodBeforeAdvice(methodHashMap.get(config.getAspectBefore()),aClass.newInstance()));
					}
					if (Strings.noEmpty(config.getAspectAfter()) || Strings.noEmpty(config.getAspectAfter().trim())){
						objects.add(new WMethodAfterAdvice(methodHashMap.get(config.getAspectBefore()),aClass.newInstance()));
					}
					if (Strings.noEmpty(config.getAspectAfterThrowName()) || Strings.noEmpty(config.getAspectAfterThrowName().trim())){
						objects.add(new WMethodAfterThrowAdvice(methodHashMap.get(config.getAspectBefore()),aClass.newInstance()));
					}
					methodCache.put(method, objects);
				}
			}
		}catch (ClassNotFoundException e){

		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
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
