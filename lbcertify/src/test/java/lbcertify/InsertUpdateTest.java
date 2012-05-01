package lbcertify;

import static org.fest.assertions.api.Assertions.assertThat;

import java.io.IOException;
import java.sql.SQLException;

import lbcertify.util.DbWatcher;
import lbcertify.util.H2Client;
import lbcertify.util.SqlCatcher;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class InsertUpdateTest {
	private Database db;
	private Liquibase liquibase;

	@BeforeClass
	public void setUp() throws Exception {
		db = H2Client.getInstance().createLiquibaseDatabase();
	}

	@Test
	public void shouldGenerateInsert() throws ClassNotFoundException, SQLException, LiquibaseException, IOException {
		SqlCatcher sql = new SqlCatcher();
		ClassLoaderResourceAccessor fileOpener = new ClassLoaderResourceAccessor();
		liquibase = new Liquibase("insert1.xml", fileOpener, db);
		liquibase.update("", sql.getWriter());
		assertThat(sql.getSqlLines()).hasSize(6);
		assertThat(sql.getSqlLines("test1")).hasSize(2);
		System.err.println("1 done");
	}

	@Test(dependsOnMethods = "shouldGenerateInsert")
	public void shoudDoInsert() throws ClassNotFoundException, SQLException, LiquibaseException, IOException {
		liquibase.update("");
		DbWatcher db = new DbWatcher(this.db);
		assertThat(db.getRowCount("test1")).isEqualTo(1);
		// conn.close();
		// String[] lines = buff.toString().split("\n");
		// System.err.println("lines=" + lines.length);
		// for (String line : lines) {
		// if (!line.startsWith("-") && line.trim().length() > 0)
		// System.out.println("<" + line.trim() + ">");
		// }
		System.err.println("2 done");
	}
}
