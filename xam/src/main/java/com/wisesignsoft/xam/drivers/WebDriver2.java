package com.wisesignsoft.xam.drivers;

import java.net.URL;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * 说明:封装webdriver某些复杂的操作
 *
 */
public class WebDriver2 extends RemoteWebDriver implements WebDriver{

	public WebDriver2(URL url, DesiredCapabilities dc) {
		super(url, dc);
	}

	/**
	 * Webdriver等待页面单个元素出现后，进行捕捉并返回WebElement
	 * @param by
	 * @param timeout
	 * @return
	 */
	public WebElement findElementByWait(final By by,long timeout){
		new WebDriverWait(this, timeout).until(new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver driver) {
				Boolean result = false;
				try {
					driver.findElement(by);
					result = true;
				} catch (Exception e) {
				}
				return result;
			}
		});
		return this.findElement(by);
	}
}
