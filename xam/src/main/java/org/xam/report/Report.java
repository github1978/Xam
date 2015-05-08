package org.xam.report;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xam.util.FileUtils;

import com.wisesignsoft.xam.ImportResources;


/**
 * 生成测试报告
 * @author 朱晓峰
 * @testerhome umbrella1978
 * @email umbrella1978@live.cn
 * @github github1978
 */
public class Report {

	private static final ImportResources RESOURCE_CLASS = new ImportResources();

	/**
	 * 报告标题
	 */
	String title = "";

	String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 报告结果表的表头 对应Column类中的key属性
	 */
	String[] resulthead = Scenario.HEADERS;
	/**
	 * 报告结果集
	 */
	private List<Scenario> Scenarios = new ArrayList<Scenario>();

	public List<Scenario> getScenarios() {
		return Scenarios;
	}

	public void setScenarios(List<Scenario> scenarios) {
		Scenarios = scenarios;
	}

	String fearture = "";

	Document document = DocumentHelper.createDocument();

	String reportPath;

	public String getReportPath() {
		return reportPath;
	}

	public void setReportPath(String reportPath) {
		this.reportPath = reportPath;
	}

	public Report(String path) throws DocumentException {
		if (new File(path).exists()) {
			initReportFromXml(path);
		}
	}

	private void initReportFromXml(String path) throws DocumentException {
		SAXReader sr = new SAXReader();
		document = sr.read(path);
	}

	public void export2xml(String reportName) {

		reportPath = reportPath + "/" + reportName;
		FileUtils.createDirectoryRecursively(new File(reportPath));
		FileWriter out;
		
		try {
			if (document.hasContent()) {
				applyReport(document);
			} else {
				init_report();
				copyReportFiles();
			}
			out = new FileWriter(reportPath + "/report.html");
			out.write(document.asXML());
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void init_report() {

		Element html = document.addElement("html");

		Element head = html.addElement("head");
		head.addElement("title").addText(this.title);
		head.addElement("link").addAttribute("href", "jquery-ui.min.css")
				.addAttribute("rel", "stylesheet");
		head.addElement("link").addAttribute("href", "style.css")
				.addAttribute("rel", "stylesheet");
		head.addElement("meta").addAttribute("charset", "UTF-8");

		Element body = html.addElement("body");
		Element north = body.addElement("div")
				.addAttribute("class", "ui-layout-north")
				.addAttribute("ID", "north");
		body.addElement("div").addAttribute("class", "ui-layout-center")
				.addAttribute("ID", "center");
		Element west = body.addElement("div")
				.addAttribute("class", "ui-layout-east")
				.addAttribute("ID", "east");

		west.addElement("ul");
		north.addElement("h1").addAttribute("ID", "report_title")
				.addText(this.title);

		body.addElement("script").addAttribute("src", "jquery.js");
		body.addElement("script").addAttribute("src", "jquery-ui.min.js");
		body.addElement("script")
				.addAttribute("src", "jquery.layout-latest.js");
		body.addElement("script").addAttribute("src", "layer.min.js");
		body.addElement("script").addAttribute("src", "x.js");

		applyReport(document);
	}

	@SuppressWarnings("unchecked")
	private void applyReport(Document document) {
		Element body = document.getRootElement().element("body");
		Element center = body.elementByID("center");
		Element westul = body.elementByID("east").element("ul");

		List<Element> scripts = body.elements("script");

		long ctime = (new Date()).getTime();
		this.id = this.id + "_" + ctime;
		this.fearture = this.fearture + "_" + ctime;

		// 组装左侧链接列表
		westul.addElement("li").addElement("a")
				.addAttribute("href", "#" + this.id).addText(this.fearture);
		// 组装结果列表
		Element result = center.addElement("div")
				.addAttribute("class", "result").addAttribute("name", this.id);

		result.addElement("h2").addAttribute("ID", "feature").addText(fearture);

		// 组装表格
		Element resultTable = result.addElement("table")
				.addAttribute("ID", "results" + "_" + ctime)
				.addAttribute("class", "result_table");

		Element th = resultTable.addElement("tr");
		for (String thead : this.resulthead) {// 组成列头
			th.addElement("th").addText(thead);
		}
		for (Scenario sce : Scenarios) {// 组成列表
			Element row = resultTable.addElement("tr");
			for (String header : this.resulthead) {
				if (header == "结果证迹") {
					Element pic = row.addElement("td");
					if (sce.getEvidences() != null) {
						for(Map<String,String> evi:sce.getEvidences()){
							pic.addElement("a")
								.addAttribute("class", "evidence")
								.addAttribute("href", "javascript:void(0)")
								.addAttribute("hreflang", evi.get("shotlink"))
								.addAttribute("onclick", "showImage(this)")
								.addText(evi.get("shotname"));
						}
					}
				} else {
					row.addElement("td").addText(sce.getContext().get(header));
				}
			}
		}

		for (Element script : scripts) {
			script.addText("1");
		}
	}

	private void copyReportFiles() throws Exception {
		String resourcepath = RESOURCE_CLASS.getClass().getProtectionDomain()
				.getCodeSource().getLocation().getFile();
		FileUtils.unCompressFromJar(resourcepath, reportPath+"/");
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFearture() {
		return fearture;
	}

	public void setFearture(String fearture) {
		this.fearture = fearture;
	}
}
