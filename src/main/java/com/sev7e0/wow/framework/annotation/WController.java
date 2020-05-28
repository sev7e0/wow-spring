package com.sev7e0.wow.framework.annotation;

import java.lang.annotation.*;

/**
 * Title:  WController.java
 * description: controller层注解
 *
 * @author sev7e0
 * @version 1.0
 * @since 2020-05-27 22:05
 **/

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface WController {


	String value() default "";

}
