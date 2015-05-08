package org.xam.core;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.xam.controller.Params;
import org.xam.controller.TestController;
import org.xam.report.Report;
import org.xam.util.ExcelUtils;
import org.xam.util.SystemUtils;


/**
 * 说明:基础案例
 */
@RunWith(BlockJUnit4ClassRunner.class)
public abstract class BasicCaseTemplete extends TestCase {

	protected XamCore core = new XamCore();
	
	protected TestController controller = new TestController();
	
	protected Report report;
	
	protected String outputpath;

	protected int iterations;

	protected List<Map<String, String>> screenshots = new ArrayList<Map<String, String>>();

	protected List<Map<String, String>> datas = new ArrayList<Map<String, String>>();

	public BasicCaseTemplete() {
		super();
	}

	public BasicCaseTemplete(String methodname) {
		super(methodname);
	}

	@Before
	public abstract void start() throws Exception;

	@Test
	public abstract void main() throws Exception;

	@After
	public abstract void stop();

	public void checkPoint() {

	}
	
	protected void doScreenShot(String detail){
		String filename = iterations + "_" + detail + "_"
				+ SystemUtils.getCurrentDate("yyyyMMddHHmmss") + ".jpg";
		String shotpath = outputpath + "screenshot/" + filename;
		Map<String,String> screenshot = new HashMap<String,String>();
		screenshot.put("shotname", filename);
		screenshot.put("shotlink", shotpath);
		screenshots.add(screenshot);
		core.getScreenShot(shotpath);
	}

	/**
	 * 导入数据<br>
	 * 数据格式为List：[{"列名":"值"},{"列名":"值"}]
	 * 
	 * @param data
	 */
	protected void importData(List<Map<String, String>> data) {
		this.datas = data;
	}

	/**
	 * 从excel中导入数据
	 * 
	 * @param testCase
	 * @param excelpath
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
	protected void importData(BasicCaseTemplete testCase, String excelpath)
			throws InvalidFormatException, IOException {
		this.datas = getRowData(testCase, excelpath);
	}

	/**
	 * 根据案例中的变量从excel中提取对应的数据
	 * 
	 * @param testCase
	 * @param xlsx
	 * @return
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	private static List<Map<String, String>> getRowData(
			BasicCaseTemplete testCase, String xlsx)
			throws InvalidFormatException, IOException {
		List<Map<String, String>> datas = new ArrayList<Map<String, String>>();
		OPCPackage pkg = OPCPackage.open(new File(xlsx));
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(pkg);
		XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
		Field[] fields = testCase.getClass().getDeclaredFields();
		for (int i = 0; i < sheet.getLastRowNum(); i++) {
			Map<String, String> rowdata = new HashMap<String, String>();
			for (Field field : fields) {
				if (!field.isAnnotationPresent(Params.class)) {
					continue;
				}
				Params param = (Params) field.getAnnotation(Params.class);
				String cellcontent = ExcelUtils.getCellContentByName(
						param.name(), sheet, i + 1);
				if (cellcontent != null) {
					rowdata.put(param.name(), cellcontent);
				}
			}
			if (rowdata.size() > 0) {
				datas.add(rowdata);
			}
		}
		return datas;
	}
}
