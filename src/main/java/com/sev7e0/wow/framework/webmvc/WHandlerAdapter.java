package com.sev7e0.wow.framework.webmvc;

import com.sev7e0.wow.framework.annotation.WRequestParam;
import com.sev7e0.wow.framework.utils.Strings;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Title:  WHandlerAdapter.java
 * description: 主要用来完成请求传递到服务端的参数列表与
 * Method对象参数列表的的对应工作，完成参数类型的转换
 *
 * @author sev7e0
 * @version 1.0
 * @since 2020-05-30 22:33
 **/

public class WHandlerAdapter {

	public boolean support(Object mapping) {
		return mapping instanceof WHandlerMapping;
	}

	/**
	 * 用反射来调用对应的方法，并将转换包装好的方法参数列表进行返回
	 *
	 * @param request  请求
	 * @param response 响应
	 * @param handler  处理对象
	 * @return 返回对象视图
	 */
	public WModelAndView handler(HttpServletRequest request, HttpServletResponse response, Object handler) throws InvocationTargetException, IllegalAccessException {
		WHandlerMapping handlerMapping = (WHandlerMapping) handler;

		HashMap<String, Integer> paraMapping = new HashMap<>();

		Annotation[][] annotations = handlerMapping.getMethod().getParameterAnnotations();

		//将所有方法的添加了注解的参数位置映射起来
		for (int i = 0; i < annotations.length; i++) {
			for (Annotation annotation : annotations[i]) {
				//获取添加了`WRequestParam`注解的参数
				if (annotation instanceof WRequestParam) {
					String paramName = ((WRequestParam) annotation).value();
					if (Strings.noEmpty(paramName)) {
						paraMapping.put(paramName, i);
					}
				}
			}
		}


		Class<?>[] parameterTypes = handlerMapping.getMethod().getParameterTypes();
		for (int i = 0; i < parameterTypes.length; i++) {
			Class<?> parameterType = parameterTypes[i];
			if (parameterType == HttpServletRequest.class || parameterType == HttpServletResponse.class) {
				paraMapping.put(parameterType.getName(), i);
			}
		}

		//获取用户传递过来的参数列表
		Map<String, String[]> parameterMap = request.getParameterMap();

		//构造实参列表
		Object[] paramValue = new Object[parameterTypes.length];

		for (Map.Entry<String, String[]> param : parameterMap.entrySet()) {
			String value = Arrays.toString(param.getValue()).replaceAll("^\\[*|\\]*$", "").replace("\\s", "");
			if (!paraMapping.containsKey(param.getKey())) continue;

			//参数所在的位置
			Integer index = paraMapping.get(param.getKey());
			paramValue[index] = castRequestParamType(value, parameterTypes[index]);
		}
		Object result = handlerMapping.getMethod().invoke(handlerMapping.getController(), paramValue);

		if (Objects.isNull(result)) return null;
		Class<?> returnType = handlerMapping.getMethod().getReturnType();

		if (returnType == WModelAndView.class) {
			return (WModelAndView) result;
		}
		return null;
	}

	private Object castRequestParamType(String value, Class<?> type) {
		if (type.getName().equals("java.lang.String")) {
			return value;
		} else if (type == Integer.class) {
			return Integer.valueOf(value);
		} else if (type == int.class) {
			return Integer.valueOf(value).intValue();
		}
		return null;
	}
}
