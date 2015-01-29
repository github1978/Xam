package com.wisesignsoft.xam.datadriven;

import com.wisesignsoft.xam.controller.TestConfigration.Source;

public class ConnStr {
	
	private String Driver;
	private String Url;
	private String User;
	private String Pwd;
	
	private static final String MYSQL_DRIVER="com.mysql.jdbc.Driver";
	
	public ConnStr(Source ds) throws Exception{
		if(!ds.getType().equals("database")){
			throw new Exception("使用的数据源不是数据库！");
		}
		if(ds.getDbtype().equals("mysql")){
			this.Driver = MYSQL_DRIVER;
			this.Url = "jdbc:mysql://"+ds.getUrl()+"/"+ds.getDbname();
		}else{
			throw new Exception("不支持的数据库类型！");
		}
		this.User = ds.getUser();
		this.Pwd = ds.getPwd();
	}
	
	public String getDriver() {
		return Driver;
	}
	public void setDriver(String driver) {
		Driver = driver;
	}
	public String getUrl() {
		return Url;
	}
	public void setUrl(String url) {
		Url = url;
	}
	public String getUser() {
		return User;
	}
	public void setUser(String user) {
		User = user;
	}
	public String getPwd() {
		return Pwd;
	}
	public void setPwd(String pwd) {
		Pwd = pwd;
	}
	
	

}
