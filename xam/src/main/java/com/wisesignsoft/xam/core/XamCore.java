package com.wisesignsoft.xam.core;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.thoughtworks.selenium.Selenium;
import com.wisesignsoft.xam.drivers.WebDriver2;

@SuppressWarnings("deprecation")
public class XamCore {

	private WebDriver webdriver;
	private Selenium selenium;

	/**
	 * 用Webdriver启动Web应用
	 * 
	 * @param appTestUrl
	 */
	public WebDriver2 startWithWebDriver(String host, String url,
			String browserType) {

		DesiredCapabilities dc;
		WebDriver2 webdriver2 = null;

		if (browserType.equals(XamConstants.IE)) {
			dc = DesiredCapabilities.internetExplorer();
		} else if (browserType.equals(XamConstants.CHROME)) {
			dc = DesiredCapabilities.chrome();
		} else {
			dc = DesiredCapabilities.firefox();
		}

		try {
			webdriver2 = new WebDriver2(new URL("http://"+host+":4444/wd/hub"), dc);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		webdriver2.get(url);
		this.webdriver = webdriver2;
		return webdriver2;
	}

//	/**
//	 * android驱动<br>
//	 * 基于Appium
//	 * 
//	 * @param remoteAddress
//	 *            Appium的服务端地址
//	 * @param appPath
//	 *            被测app apk包的位置
//	 * @param deviceName
//	 *            被测设备名称
//	 * @param platformVersion
//	 *            被测设备操作系统的版本
//	 * @param platformName
//	 *            被测设备的操作系统名称Android,IOS
//	 * @param appPackage
//	 *            被测app的Activity类所在包
//	 * @param appActivity
//	 *            被测app的Activity类
//	 * @return
//	 * @throws MalformedURLException
//	 */
//	public AndroidDriver2 startWithAndroidDriver(String remoteAddress,
//			String appPath, String deviceName, String platformVersion,
//			String platformName, String appPackage, String appActivity)
//			throws MalformedURLException {
//		DesiredCapabilities capabilities = new DesiredCapabilities();
//		capabilities.setCapability("deviceName", deviceName);
//		capabilities.setCapability("platformVersion", platformVersion);
//		capabilities.setCapability("platformName", platformName);
//		capabilities.setCapability("app", appPath);
//		capabilities.setCapability("appPackage", appPackage);
//		capabilities.setCapability("appActivity", appActivity);
//		if (Float.parseFloat(platformVersion) <= 4.2
//				&& platformName.equals("Android")) {
//			capabilities.setCapability("automationName", "Selendroid");
//		}
//		AndroidDriver2 androiddriver = (new AndroidDriver2(new URL(
//				remoteAddress), capabilities));
//		this.webdriver = androiddriver;
//		return androiddriver;
//	}

	/**
	 * 截图
	 * 
	 * @param path
	 *            例如:C:\1.jpg
	 */
	public void getScreenShot(String path) {
		if (webdriver != null) {
			File pic = ((TakesScreenshot) webdriver)
					.getScreenshotAs(XamConstants.FILE);
			try {
				FileUtils.copyFile(pic, new File(path));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (selenium != null) {
			selenium.captureScreenshot(path);
		} else {
			return;
		}
	}

}
