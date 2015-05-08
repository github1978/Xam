package org.xam.datadriven;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.xam.controller.TestConfigration.Source;

/**
 * 数据库查询执行器
 * @author 朱晓峰
 * @testerhome umbrella1978
 * @email umbrella1978@live.cn
 * @github github1978
 */
public class JDBCExecutor {

	private Connection connection;
	private Statement stmt;

	public JDBCExecutor(Source ds) throws Exception {
		DBConnectEntity constr = new DBConnectEntity(ds);
		Class.forName(constr.getDriver());
		connection = DriverManager.getConnection(constr.getUrl(),
		constr.getUser(), constr.getPwd());
		stmt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
			ResultSet.CONCUR_UPDATABLE);
	}

	public ResultSet executeQuery(String sql) throws SQLException {
		ResultSet result = stmt.executeQuery(sql);
		return result;
	}

	/**
	 * 获取数据集中第1个字段的值 仅适用于数据集中只有一行且只有一列的数据
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