package com.wisesignsoft.xam.controller;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.wisesignsoft.xam.controller.TestConfigration.Source;
import com.wisesignsoft.xam.core.BasicCaseTemplete;
import com.wisesignsoft.xam.core.XamConstants;
import com.wisesignsoft.xam.core.XamCore;
import com.wisesignsoft.xam.report.Report;
import com.wisesignsoft.xam.report.Scenario;
import com.wisesignsoft.xam.util.ReflectionUtils;
import com.wisesignsoft.xam.util.SystemUtils;

import junit.framework.ComparisonFailure;

/**
 * 说明:调度执行案例，采集测试结果
 *
 */
public class TestController {

	private TestConfigration config;
	private Report report;

	public static List<Source> sources;

	public TestController() {
		try {
			config = generateConfig();
			report = new Report(config.outPath + "/"
					+ config.report.getReportName() + "/report.html");
			report.setTitle(config.report.getReportTitle());
			report.setReportPath(config.outPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 无数据源，直接运行
	 * 
	 * @param testCase
	 * @throws Exception
	 */
	public void RunAllTest(BasicCaseTemplete testCase) throws Exception {
		InvokeAllTestMethod(testCase, 1, null);
		report.export2xml(config.report.getReportName());
	}

	/**
	 * 加载数据源,数据源中有多少行,就会调用多少次测试案例类中所有标注了@TestMethods的方法
	 * 
	 * @param testCase
	 *            测试案例类
	 * @param xlsx
	 *            指定的excel路径
	 * @throws Exception
	 */
	public void RunAllTest(BasicCaseTemplete testCase,
			List<Map<String, String>> datas) throws Exception {
		RunAllTest(testCase, datas.size(), datas);
	}

	/**
	 * 由脚本指定运行次数,可结合数据输入使用
	 * 
	 * @param testCase
	 * @param iterations
	 *            重复运行次数
	 * @throws Exception
	 */
	public void RunAllTest(BasicCaseTemplete testCase, int iterations,
			List<Map<String, String>> datas) throws Exception {
		if (datas == null) {
			throw new Exception("执行案例设置为使用导入数据，但未找到可用数据，执行失败！");
		}
		for (int i = 0; i < iterations; i++) {
			InvokeAllTestMethod(testCase, i, datas);
		}
		report.export2xml(config.report.getReportName());
	}

	/**
	 * 调用所有标注了@TestMethods的方法 并在调用前为变量赋值
	 * 
	 * @param testCase
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void InvokeAllTestMethod(BasicCaseTemplete testCase,
			int iterations, List<Map<String, String>> datas) {

		Method[] methods = testCase.getClass().getDeclaredMethods();
		Field[] fields = testCase.getClass().getDeclaredFields();
		String outputpath = config.outPath + "/"
				+ config.report.getReportName() + "/";

		report.setId(testCase.getClass().getSimpleName());
		report.setFearture(testCase.getClass().getAnnotation(Case.class)
				.feature());

		// 若导入数据不为空则为案例中的输入参数赋值
		if (datas != null) {
			for (Field field : fields) {
				if (!field.isAnnotationPresent(Params.class)) {
					continue;
				}
				field.setAccessible(true);
				Params param = (Params) field.getAnnotation(Params.class);
				try {
					field.set(testCase, datas.get(iterations).get(param.name()));
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}

		// 调用所有测试方法
		for (Method method : methods) {

			ReflectionUtils.setFieldValue(testCase, "outputpath", outputpath);
			ReflectionUtils.setFieldValue(testCase, "iterations", iterations);
			String methName = method.getName() + "_" + iterations;

			if (!method.isAnnotationPresent(TestMethod.class)) {
				continue;
			}
			Scenario scenario = new Scenario();
			scenario.getContext().put("场景名称", method.getAnnotation(TestMethod.class)
					.scenario());
			List<Map<String, String>> screenshots = new ArrayList<Map<String, String>>();
			try {
				for (int i = 0; i < method.getAnnotation(TestMethod.class)
						.iterations(); i++) {
					method.invoke(testCase);
				}
				// 测试通过的处理
				screenshots = (List<Map<String, String>>)ReflectionUtils.getFieldValue(
						testCase, XamConstants.CASE_SHOTS);
				scenario.getContext().put("测试结果", "通过");
				scenario.setEvidences(screenshots) ;
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				String result;
				// 用于测试失败处理
				if (e.getCause().getClass().equals(ComparisonFailure.class)) {
					result = e.getCause().toString()
							.split("junit.framework.ComparisonFailure: ")[1];
				} else {
					result = e.getTargetException().getMessage();
				}
				XamCore core = (XamCore) ReflectionUtils.getFieldValue(
						testCase, "core");
				String ssfileName = methName + "_"
						+ SystemUtils.getCurrentDate("yyyyMMdd") + ".jpg";
				String sspathfile = outputpath + "screenshot/" + ssfileName;
				core.getScreenShot(sspathfile);
				scenario.getContext().put("测试结果", "失败<br/>原因:" + result);
				Map<String,String> screenshot = new HashMap<String,String>();
				screenshot.put("shotname", ssfileName);
				screenshot.put("shotlink", "screenshot/" + ssfileName);
				scenario.getEvidences().add(screenshot);
			} finally {
				report.getScenarios().add(scenario);
				ReflectionUtils.setFieldValue(testCase,
						XamConstants.CASE_SHOTS, (new ArrayList<Map<String, String>>()));
			}
		}
	}

	/**
	 * 读取测试配置
	 * 
	 * @return
	 * @throws Exception
	 */
	private TestConfigration generateConfig() throws Exception {

		SAXReader sr = new SAXReader();
		Document configDoc = sr.read(this.getClass().getClassLoader()
				.getResource("TestConfigration.xml"));
		XStream xs = new XStream(new DomDriver());
		xs.processAnnotations(TestConfigration.class);
		TestConfigration config = (TestConfigration) xs.fromXML(configDoc
				.asXML());
		if (!config.outPath.isEmpty()) {
			if (!(new File(config.outPath)).exists()) {
				throw new Exception("TestConfigration.xml文件中OutPath路径不存在或者不正确！");
			}
		} else {
			config.outPath = this.getClass().getClassLoader().getResource("")
					.getPath();
		}
		if (config.report.getReportTitle().isEmpty()) {
			config.report.setReportTitle("测试报告"
					+ (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
							.format(new Date())));
		}

		return config;
	}
	
	public Source getSourceFromConfigById(String id) throws Exception{
		return config.dataSource.getSourceById(id);
	}
	

}
