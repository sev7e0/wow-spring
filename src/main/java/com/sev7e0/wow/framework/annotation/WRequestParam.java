package com.sev7e0.wow.framework.annotation;

import java.lang.annotation.*;

/**
 * Title:  WRequestParam.java
 * description: TODO
 *
 * @author sev7e0
 * @version 1.0
 * @since 2020-05-27 22:20
 **/

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WRequestParam {

	String value() default "";

	boolean require() default true;

}
