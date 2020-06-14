package com.sev7e0.wow.web.aspect;

import com.sev7e0.wow.framework.aop.aspect.WJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Title:  WowAspect.java
 * description: TODO
 *
 * @author sev7e0
 * @version 1.0
 * @since 2020-06-13 18:29
 **/

public class WowAspect {

	Logger logger = LoggerFactory.getLogger(WowAspect.class);

	public void before(WJoinPoint joinPoint) {
		joinPoint.setUserAttribute(joinPoint.getMethod().getName(), System.currentTimeMillis());
		logger.info("Invoke Method: {}, Execute [BeforeAdvice].", joinPoint.getMethod().getName());
	}

	public void after(WJoinPoint joinPoint) {
		logger.info("Invoke Method: {}, Execute [AfterAdvice].", joinPoint.getMethod().getName());
		logger.info("Use Time: {}", System.currentTimeMillis() - (long) joinPoint.getUserAttribute(joinPoint.getMethod().getName()));
	}

	public void afterThrow(WJoinPoint joinPoint, Throwable throwable) {
		logger.info("Invoke Method: {}, Execute [AfterThrowAdvice]. Throw Exception: {}!",
			joinPoint.getMethod().getName(),
			throwable.getCause().getMessage());
	}


}
