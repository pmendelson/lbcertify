package lbcertify.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import liquibase.database.Database;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;

public class DbWatcher {
	private Connection conn;

	public DbWatcher(Database db) {
		this(((JdbcConnection) db.getConnection()).getWrappedConnection());
	}

	public DbWatcher(Connection conn) {
		this.conn = conn;
	}

	public int getRowCount(String tabName) throws ClassNotFoundException, SQLException, LiquibaseException {
		int r = 0;
		ResultSet rs = conn.createStatement().executeQuery("select * from " + tabName);
		while (rs.next()) {
			System.out.println("col 1=" + rs.getString(1));
			r += 1;
		}
		rs.close();
		return r;
	}

	public String getColumnValue(String tabName, int colSlot) throws ClassNotFoundException, SQLException, LiquibaseException {
		String r = null;
		ResultSet rs = conn.createStatement().executeQuery("select * from " + tabName);
		if (rs.next()) {
			r = rs.getString(colSlot);
		}
		rs.close();
		return r;
	}
}
