package org.xam.datadriven;

import org.xam.controller.TestConfigration.Source;

/**
 * 数据库连接串实体模型
 * @author 朱晓峰
 * @testerhome umbrella1978
 * @email umbrella1978@live.cn
 * @github github1978
 */
public class DBConnectEntity {
	
	private String Driver;
	private String Url;
	private String User;
	private String Pwd;
	
	private static final String MYSQL_DRIVER="com.mysql.jdbc.Driver";
	
	public DBConnectEntity(Source ds) throws Exception{
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
