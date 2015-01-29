package com.wisesignsoft.xam.core;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.remote.BrowserType;

/**
 * 说明:常量类
 */
public interface XamConstants<T> extends BrowserType,OutputType<T>{
	
	public static final String CASE_SHOTS="screenshots";
	
}
