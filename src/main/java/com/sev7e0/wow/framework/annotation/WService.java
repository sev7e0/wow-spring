package com.sev7e0.wow.framework.annotation;

import java.lang.annotation.*;

/**
 * Title:  WService.java
 * description: 业务层注解，将Controller与Service分开是为了在扫描是更快的区分
 * 出不同层面的的类。
 *
 * @author sev7e0
 * @version 1.0
 * @since 2020-05-27 22:04
 **/

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface WService {

	String value() default "";

}
