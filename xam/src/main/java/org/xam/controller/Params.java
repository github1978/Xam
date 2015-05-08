package org.xam.controller;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * 用于数据源匹配的注解类
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Params {
	
	/**
	 * 数据源中对应的属性名称
	 */
	String name() default "";
}
