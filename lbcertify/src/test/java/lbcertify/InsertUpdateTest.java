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

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class InsertUpdateTest {
	private Database db;
	private Liquibase liquibase;

	@BeforeClass
	public void setUp() throws Exception {
		db = H2Client.getInstance().createLiquibaseDatabase();
	}

	@AfterClass
	public void tearDown() throws Exception {
	}

	@Test
	public void shouldGenerateInsert() throws ClassNotFoundException, SQLException, LiquibaseException, IOException {
		SqlCatcher sql = new SqlCatcher();
		ClassLoaderResourceAccessor fileOpener = new ClassLoaderResourceAccessor();
		liquibase = new Liquibase("insert1.xml", fileOpener, db);
		liquibase.update("", sql.getWriter());
		assertThat(sql.getSqlLines()).hasSize(7);
		assertThat(sql.getSqlLines("test1")).hasSize(3);
	}

	@Test(dependsOnMethods = "shouldGenerateInsert")
	public void shoudDoInsert() throws ClassNotFoundException, SQLException, LiquibaseException, IOException {
		liquibase.update("");
		DbWatcher db = new DbWatcher(this.db);
		assertThat(db.getRowCount("test1")).isEqualTo(1);
		assertThat(db.getColumnValue("test1", 2)).isEqualTo("25");
	}
}
