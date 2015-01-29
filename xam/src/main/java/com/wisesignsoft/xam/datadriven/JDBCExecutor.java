package com.wisesignsoft.xam.datadriven;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import com.wisesignsoft.xam.controller.TestConfigration.Source;

public class JDBCExecutor {

	// 连接对象
	private Connection connection;
	// Statement对象,可以执行SQL语句并返回结果
	private Statement stmt;

	public JDBCExecutor(Source ds) {
		try {
			ConnStr constr = new ConnStr(ds);
			// 初始化JDBC驱动并让驱动加载到jvm中
			Class.forName(constr.getDriver());
			// 创建数据库连接
			connection = DriverManager.getConnection(constr.getUrl(),
					constr.getUser(), constr.getPwd());
			// 创建Statement对象
			stmt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * 执行一句查询的sql
	 */
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

	// 执行单句INSERT、UPDATE 或 DELETE 语句, 如果执行INSERT时, 返回主键
	public int executeUpdate(String sql) {
		int result = -1;
		try {
			// 执行SQL语句
			stmt.executeUpdate(sql);
			// 获得主键
			ResultSet rs = stmt.getGeneratedKeys();
			while (rs.next()) {
				// 返回最后一个主键
				result = rs.getInt(1);
			}
			rs.close();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
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

	public int getRsRowCount(ResultSet rs) {
		int rowCount = 0;
		try {
			rs.last();
			rowCount = rs.getRow();
			rs.first();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return rowCount;
	}
}