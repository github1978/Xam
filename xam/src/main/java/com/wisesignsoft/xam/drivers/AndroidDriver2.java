package com.wisesignsoft.xam.drivers;

import java.net.URL;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;

/**
 * 说明:改造中
 *
 */
public class AndroidDriver2 extends AndroidDriver{
	
	public TouchAction touch;

	public AndroidDriver2(URL remoteAddress, Capabilities desiredCapabilities) {
		super(remoteAddress, desiredCapabilities);
	}

	/**
	 * 实现Appium在Hybrid app中输入中文
	 * @param id web元素的id
	 * @param text
	 */
	public void enterTextByIdForWebView(String id, String text) {
		JavascriptExecutor jse = (JavascriptExecutor) this;
		jse.executeScript("document.getElementById('" + id + "').value='"
				+ text + "'");
	}
	
	/**
	 * 长按元素后，拖动到指定坐标
	 * @param element
	 * @param x
	 * @param y
	 */
	public void dragAndDrop(WebElement element,int x,int y){
		touch.longPress(element).moveTo(x, y);
	}
	
}
