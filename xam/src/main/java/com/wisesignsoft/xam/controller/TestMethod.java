package com.wisesignsoft.xam.controller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解用于测试场景的调度以及场景描述的存储
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface TestMethod {

	String scenario() default "";
	
	int iterations() default 1;
}
