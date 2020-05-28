package com.sev7e0.wow.framework.annotation;

import java.lang.annotation.*;

/**
 * Title:  WAutowired.java
 * description: 用于类内部变量注入注解
 * 作用于类内部属性字段
 *
 * @author sev7e0
 * @version 1.0
 * @since 2020-05-27 22:06
 **/

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface WAutowired {

	/**
	 * @return
	 */
	String value() default "";

}
