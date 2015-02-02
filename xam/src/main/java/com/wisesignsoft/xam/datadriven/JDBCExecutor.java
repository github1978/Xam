package com.wisesignsoft.xam.datadriven;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.wisesignsoft.xam.controller.TestConfigration.Source;

public class JDBCExecutor {

	// 连接对象
	private Connection connection;
	// Statement对象,可以执行SQL语句并返回结果
	private Statement stmt;

	public JDBCExecutor(Source ds) throws Exception {
		ConnStr constr = new ConnStr(ds);
		Class.forName(constr.getDriver());
		connection = DriverManager.getConnection(constr.getUrl(),
		constr.getUser(), constr.getPwd());
		stmt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
			ResultSet.CONCUR_UPDATABLE);
	}

	public ResultSet executeQuery(String sql) {
		try {
			// 利用Statement对象执行参数的sql
			ResultSet result = stmt.executeQuery(sql);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取数据集中第1个字段的值 仅适用于数据集中只有一行且只有一列的数据
	 * 
	 * @param rs
	 *            数据集
	 * @return
	 * @throws Exception 
	 */
	public String parseRsSingleValue(ResultSet rs) throws Exception {
			if (rs.getMetaData().getColumnCount() != 1
					|| getRsRowCount(rs) != 1) {
				throw new Exception("不支持多行多列数据集或者返回数据为空");
			}
		return rs.getString(1);
	}

	public int getRsRowCount(ResultSet rs) throws SQLException {
		int rowCount = 0;
		rs.last();
		rowCount = rs.getRow();
		rs.first();
		return rowCount;
	}
}