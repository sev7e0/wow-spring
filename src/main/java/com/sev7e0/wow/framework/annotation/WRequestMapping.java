package com.sev7e0.wow.framework.annotation;

import java.lang.annotation.*;

/**
 * Title:  WRequestMapping.java
 * description: 用于映射{@link WController}类和方法的注解，可同用于类和方法
 * 采用类注解value/方法注解value形式
 *
 * @author sev7e0
 * @version 1.0
 * @since 2020-05-27 22:11
 **/

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface WRequestMapping {

	/**
	 * 配置请求路径
	 *
	 * @return value
	 */
	String value() default "";

}
