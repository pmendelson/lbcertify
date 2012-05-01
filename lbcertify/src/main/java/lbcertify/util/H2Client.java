package lbcertify.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import liquibase.database.Database;
import liquibase.database.core.H2Database;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;

public class H2Client {

	/**
	 * SingletonHolder is loaded on the first execution of Singleton.getInstance() or the first access to
	 * SingletonHolder.INSTANCE, not before.
	 */
	private static class SingletonHolder {
		private static final H2Client INSTANCE = new H2Client();
	}

	public static H2Client getInstance() {
		return SingletonHolder.INSTANCE;
	}

	public Database createLiquibaseDatabase() throws ClassNotFoundException, SQLException, LiquibaseException {
		Class.forName("org.h2.Driver");
		Connection conn = DriverManager.getConnection("jdbc:h2:mem:c1;MODE=Oracle", "sa", "");
		Database db = new H2Database();
		db.setConnection(new JdbcConnection(conn));
		return db;
	}
}
