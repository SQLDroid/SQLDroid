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
                android.database.sqlite.SQLiteDatabase.CREATE_IF_NECESSARY
                        | android.database.sqlite.SQLiteDatabase.OPEN_READWRITE);

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
    public void shouldDealWithInvalidDirectoryGivenAsFile() throws SQLException, IOException {
        File dbFile = cleanDbFile("db-as-dir.db");
        final String jdbcUrl = "jdbc:sqlite:" + dbFile.getParentFile().getAbsolutePath();
        assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            @Override
            public void call() throws Throwable {
                new SQLDroidDriver().connect(jdbcUrl, new Properties());
            }
        }).isInstanceOf(SQLException.class)
                .hasMessageContaining("Can't create")
                .hasMessageContaining(dbFile.getParent());
    }

    @Test
    public void shouldDealWithDirectoryNameAsExistingFile() throws SQLException, IOException {
        File dbDir = cleanDbFile("subdir");
        try (FileOutputStream output = new FileOutputStream(dbDir)) {
        }
        File dbFile = new File(dbDir, "dbfile.db");
        final String jdbcUrl = "jdbc:sqlite:" + dbFile.getAbsolutePath();
        assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            @Override
            public void call() throws Throwable {
                new SQLDroidDriver().connect(jdbcUrl, new Properties());
            }
        }).isInstanceOf(SQLException.class)
                .hasMessageContaining("Can't create")
                .hasMessageContaining(dbFile.getAbsolutePath());
    }

    @Test
    public void shouldCreateMissingSubdirectory() throws SQLException {
        DB_DIR.mkdirs();
        assertThat(DB_DIR).isDirectory();
        File dbSubdir = new File(DB_DIR, "non-existing-dir");
        File dbFile = new File(dbSubdir, "database.db");
        dbFile.delete();
        dbSubdir.delete();
        assertThat(dbSubdir).doesNotExist();
        final String jdbcUrl = "jdbc:sqlite:" + dbFile.getAbsolutePath();
        new SQLDroidDriver().connect(jdbcUrl, new Properties()).close();
        assertThat(dbFile).exists();
    }

    @Test
    public void shouldSupportReconnectAfterAbortedTransaction() throws SQLException {
        File dbFile = cleanDbFile("aborted-transaction.db");
        final String jdbcUrl = "jdbc:sqlite:" + dbFile.getAbsolutePath();
        try (Connection connection = new SQLDroidDriver().connect(jdbcUrl, new Properties())) {
            connection.setAutoCommit(false);
        }
        Connection conn = new SQLDroidDriver().connect(jdbcUrl, new Properties());
        assertThat(conn.isClosed()).isFalse();
        conn.close();
    }

    @Test
    public void shouldAllowNewTransactionAfterCommit() throws SQLException {
        File dbFile = cleanDbFile("transaction.db");
        final String jdbcUrl = "jdbc:sqlite:" + dbFile.getAbsolutePath();
        try (Connection connection = new SQLDroidDriver().connect(jdbcUrl, new Properties())) {
            connection.setAutoCommit(false);
            connection.commit();
        }

        try (Connection connection = new SQLDroidDriver().connect(jdbcUrl, new Properties())) {
            // The following line should not throw an exception "database is
            // locked"
            connection.setAutoCommit(false);
        }
    }

    @Test
    public void shouldAllowMultipleConnections() throws SQLException {
        File dbFile = cleanDbFile("multiconnect.db");
        final String jdbcUrl = "jdbc:sqlite:" + dbFile.getAbsolutePath();
        Connection connection1 = new SQLDroidDriver().connect(jdbcUrl, new Properties());
        Connection connection2 = new SQLDroidDriver().connect(jdbcUrl, new Properties());
        connection1.close();
        connection2.createStatement().executeQuery("select 1");
        connection2.close();
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
