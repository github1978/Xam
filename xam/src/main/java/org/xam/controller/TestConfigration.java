package org.xam.controller;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 说明:xml转对象
 */
@XStreamAlias("TestConfigration")
public class TestConfigration {

	@XStreamAlias("OutPath")
	public String outPath;
	
	@XStreamAlias("Report")
	public Report report;

	
	public static class Report{
		
		@XStreamAlias("ReportName")
		private String reportName;
		
		@XStreamAlias("ReportTitle")
		private String reportTitle;

		public String getReportName() {
			return reportName;
		}

		public void setReportName(String reportName) {
			this.reportName = reportName;
		}

		public String getReportTitle() {
			return reportTitle;
		}

		public void setReportTitle(String reportTitle) {
			this.reportTitle = reportTitle;
		}
	}

	@XStreamAlias("DataSource")
	public DataSource dataSource;

	public static class DataSource {

		@XStreamImplicit(itemFieldName = "Source")
		private List<Source> sources;

		public List<Source> getParam() {
			return sources;
		}

		public void setParam(List<Source> param) {
			this.sources = param;
		}
		
		public Source getSourceById(String id) throws Exception{
			for(Source source:this.sources){
				if(source.getId().equals(id)){
					return source;
				}
			}
			throw new Exception("在配置文件TestConfigration.xml中未找到id为{"+id+"}的source");
		}

	}

	@XStreamAlias("Source")
	public static class Source {

		@XStreamAsAttribute
		private String type;

		@XStreamAlias("path")
		private String path;
		
		@XStreamAlias("dbtype")
		private String dbtype;
		
		@XStreamAlias("url")
		private String url;
		
		@XStreamAlias("dbname")
		private String dbname;
		
		@XStreamAlias("user")
		private String user;
		
		@XStreamAlias("pwd")
		private String pwd;
		
		@XStreamAsAttribute
		private String id;

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

		public String getDbtype() {
			return dbtype;
		}

		public void setDbtype(String dbtype) {
			this.dbtype = dbtype;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getDbname() {
			return dbname;
		}

		public void setDbname(String dbname) {
			this.dbname = dbname;
		}

		public String getUser() {
			return user;
		}

		public void setUser(String user) {
			this.user = user;
		}

		public String getPwd() {
			return pwd;
		}

		public void setPwd(String pwd) {
			this.pwd = pwd;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getType() {
			return type;
		}

		public void setType(String name) {
			this.type = name;
		}

	}

}
