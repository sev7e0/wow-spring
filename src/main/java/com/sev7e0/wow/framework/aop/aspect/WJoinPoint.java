package com.sev7e0.wow.framework.aop.aspect;

import java.lang.reflect.Method;

/**
 * Title:  WJoinPoint.java
 * description: TODO
 *
 * @author sev7e0
 * @version 1.0
 * @since 2020-06-04 23:01
 **/

public interface WJoinPoint {

	Method getMethod();

	Object[] getArguments();

	Object getThis();

	void setUserAttribute(String key, Object value);

	Object getUserAttribute(String key);

}
