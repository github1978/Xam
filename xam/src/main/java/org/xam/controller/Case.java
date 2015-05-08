package org.xam.controller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解用于存储测试报告内容
 * @author 朱晓峰
 * @testerhome umbrella1978
 * @email umbrella1978@live.cn
 * @github github1978
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Case {
	String feature() default "";
}
