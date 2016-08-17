package org.sqldroid;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.assertj.core.api.ThrowableAssert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 16)
public class SQLDroidConnectionTest {
  
  @Test
  public void shouldConnectToEmptyFile() throws SQLException, IOException {
    Properties properties = new Properties();
    properties.put(SQLDroidDriver.ADDITONAL_DATABASE_FLAGS, 
        android.database.sqlite.SQLiteDatabase.CREATE_IF_NECESSARY | android.database.sqlite.SQLiteDatabase.OPEN_READWRITE);
    
    File dbFile = cleanDbFile("exisising-file.db");
    try (FileOutputStream output = new FileOutputStream(dbFile)) {
    }
    assertThat(dbFile).exists();

    String jdbcUrl = "jdbc:sqlite:" + dbFile.getAbsolutePath();
    Connection conn = new SQLDroidDriver().connect(jdbcUrl, properties);
    assertThat(conn.isClosed()).isFalse();
    conn.close();
  }
  
  @Test
  public void shouldSupportQueryPartOfURL() throws SQLException {
    File dbFile = cleanDbFile("query-test.db");
    String jdbcUrl = "jdbc:sqlite:" + dbFile.getAbsolutePath() + "?timeout=30";
    Connection conn = new SQLDroidDriver().connect(jdbcUrl, new Properties());
    assertThat(conn.isClosed()).isFalse();
    conn.close();    
  }
  
  @Test
  public void shouldDealWithDatabaseAsDirectory() throws SQLException {
    File dbFile = cleanDbFile("db-as-dir.db");
    final String jdbcUrl = "jdbc:sqlite:" + dbFile.getParentFile().getAbsolutePath();
    assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
      @Override
      public void call() throws Throwable {
        new SQLDroidDriver().connect(jdbcUrl, new Properties());
      }
    }).isInstanceOf(SQLException.class)
      .hasMessageContaining("SQLiteCantOpenDatabaseException");
  }
  
  @Test
  // TODO: Many issues seem to stem from users expecting subdirectories to be created. Should this be supported?
  public void shouldFailOnMissingSubdirectory() throws SQLException {
    DB_DIR.mkdirs();
    assertThat(DB_DIR).isDirectory();
    File dbSubdir = new File(DB_DIR, "non-existing-dir");
    assertThat(dbSubdir).doesNotExist();
    File dbFile = new File(dbSubdir, "database.db");
    final String jdbcUrl = "jdbc:sqlite:" + dbFile.getAbsolutePath();
    assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
      @Override
      public void call() throws Throwable {
        new SQLDroidDriver().connect(jdbcUrl, new Properties());
      }
    }).isInstanceOf(SQLException.class)
      .hasMessageContaining("SQLiteCantOpenDatabaseException");
    assertThat(dbSubdir).doesNotExist();
  }
  

  private static final File DB_DIR = new File("./target/data/org.sqldroid/databases/");

  private File cleanDbFile(String filename) {
    DB_DIR.mkdirs();
    assertThat(DB_DIR).isDirectory();

    File dbFile = new File(DB_DIR, filename);
    dbFile.delete();
    assertThat(dbFile).doesNotExist();
    return dbFile;
  }
}
