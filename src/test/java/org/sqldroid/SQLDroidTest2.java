package org.sqldroid;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import junit.framework.AssertionFailedError;


/**
 * This is a refactoring is SQLDroidTest that shares quite a bit of common code between test methods.
 * I'm not sure which approach reads the best.
 * 
 * @author Johannes Brodwall <johannes@brodwall.com>
 */
@RunWith(RobolectricTestRunner.class)
@Config(sdk = 16)
public class SQLDroidTest2 {

    // TODO: This should be /data/data/org.sqldroid/databases/ if running on device
    private static final File DB_DIR = new File("./target/data/org.sqldroid/databases/");

    private Connection conn;

    @Test
    public void shouldRetrieveInsertedBasicTypes() throws SQLException {
        String createTableStatement = "CREATE TABLE dummytable (id int, aString VARCHAR(254), aByte byte, "
                + "aShort short, anInt int, aLong long, aBool boolean, aFloat float, aDouble double, aText text)";
        conn.createStatement().execute(createTableStatement);

        int id = 4325;
        String string = "test";
        byte b = 23;
        short s = 421;
        int i = 12551;
        long l = 23423525322L;
        boolean bool = false;
        float f = 324235.0f;
        double d = 123425.125;
        String text = "some potentially very long text";

        String insertStmt = "insert into dummytable "
                + "(id, aString, aByte, aShort, anInt, aLong, aBool, aFloat, aDouble, aText) "
                + "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(insertStmt)) {
            stmt.setInt(1, id);
            stmt.setString(2, string);
            stmt.setByte(3, b);
            stmt.setShort(4, s);
            stmt.setInt(5, i);
            stmt.setLong(6, l);
            stmt.setBoolean(7, bool);
            stmt.setFloat(8, f);
            stmt.setDouble(9, d);
            stmt.setString(10, text);
            int rowCount = stmt.executeUpdate();
            assertThat(rowCount).as("rowCount").isEqualTo(1);
        }

        String selectStmt = "SELECT aString, aByte, aShort, anInt, aLong, aBool, aFloat, aDouble, aText "
                + " FROM dummytable where id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(selectStmt)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();

                assertThat(string)
                        .isEqualTo(rs.getString(1)).isEqualTo(rs.getString("aString"))
                        .isEqualTo(rs.getObject(1)).isEqualTo(rs.getObject("aString"));
                assertThat(b)
                        .isEqualTo(rs.getByte(2)).isEqualTo(rs.getByte("aByte"));
                assertThat(s)
                        .isEqualTo(rs.getShort(3)).isEqualTo(rs.getShort("aShort"))
                        .isEqualTo((short) rs.getInt(3)).isEqualTo((short) rs.getInt("aShort"));
                assertThat(i)
                        .isEqualTo(rs.getInt(4)).isEqualTo(rs.getInt("anInt"))
                        .isEqualTo((int) rs.getLong(4)).isEqualTo((int) rs.getLong("anInt"))
                        .isEqualTo(rs.getObject(4)).isEqualTo(rs.getObject("anInt"));
                assertThat(l)
                        .isEqualTo(rs.getLong(5)).isEqualTo(rs.getLong("aLong"));
                assertThat(bool)
                        .isEqualTo(rs.getBoolean(6)).isEqualTo(rs.getBoolean("aBool"))
                        .isEqualTo(rs.getInt(6) == 1).isEqualTo(rs.getInt("aBool") == 1)
                        .isEqualTo((int) rs.getObject(6) == 1);
                assertThat(f)
                        .isEqualTo(rs.getFloat(7)).isEqualTo(rs.getFloat("aFloat"))
                        .isEqualTo(rs.getObject(7)).isEqualTo(rs.getObject("aFloat"))
                        .isEqualTo((float) rs.getDouble(7)).isEqualTo((float) rs.getDouble("aFloat"));
                assertThat(d)
                        .isEqualTo(rs.getDouble(8)).isEqualTo(rs.getDouble("aDouble"))
                        .isEqualTo((double) (Float) rs.getObject(8)); // Is this
                                                                      // intended?
                assertThat(text)
                        .isEqualTo(rs.getString(9)).isEqualTo(rs.getString("aText"))
                        .isEqualTo(rs.getObject(9)).isEqualTo(rs.getObject("aText"));
            }
        }
    }
    
    @Test
    public void shouldRetrieveInsertedBigDecimals() throws SQLException {
        String createTableStatement = "CREATE TABLE bdTable (id int, aBigDecimal numeric)";
        conn.createStatement().execute(createTableStatement);

        int id = 100500;
        // Potential accuracy loss
        BigDecimal bigDecimal = new BigDecimal("10005000.00050001");

        String insertStmt = "insert into bdTable (id, aBigDecimal) values (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(insertStmt)) {
            stmt.setInt(1, id);
            stmt.setBigDecimal(2, bigDecimal);
            int rowCount = stmt.executeUpdate();
            assertThat(rowCount).as("rowCount").isEqualTo(1);
        }

        String selectStmt = "SELECT aBigDecimal FROM bdTable where id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(selectStmt)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();

                assertThat(bigDecimal)
                        .isEqualTo(rs.getBigDecimal(1)).isEqualTo(rs.getBigDecimal("aBigDecimal"));
            }
        }
    }

    @Test
    public void shouldRetrieveInsertedNullValues() throws SQLException {
        String createTableStatement = "CREATE TABLE dummytable (id int, aString VARCHAR(254), aByte byte, "
                + "aShort short, anInt int, aLong long, aBool boolean, aFloat float, aDouble double, aText text)";
        conn.createStatement().execute(createTableStatement);

        int id = 13155;

        String insertStmt = "insert into dummytable "
                + "(id, aString, aByte, aShort, anInt, aLong, aBool, aFloat, aDouble, aText) "
                + "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(insertStmt)) {
            stmt.setInt(1, id);
            for (int i = 2; i <= 10; i++) {
                stmt.setObject(i, null);
            }
            stmt.executeUpdate();
        }

        String selectStmt = "SELECT aString, aByte, aShort, anInt, aLong, aBool, aFloat, aDouble, aText "
                + " FROM dummytable where id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(selectStmt)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();

                assertThat(rs.getString(1)).isNull();

                assertThat(rs.getByte(2)).isEqualTo((byte) 0);
                assertThat(rs.wasNull()).isTrue();

                assertThat(rs.getShort(3)).isEqualTo((short) 0);
                assertThat(rs.wasNull()).isTrue();

                assertThat(rs.getObject(4)).isNull();
                assertThat(rs.getInt(4)).isEqualTo(0);
                assertThat(rs.wasNull()).isTrue();

                assertThat(rs.getLong(5)).isEqualTo(0);
                assertThat(rs.wasNull()).isTrue();

                assertThat(rs.getBoolean(6)).isEqualTo(false);
                assertThat(rs.wasNull()).isTrue();

                assertThat(rs.getObject(7)).isNull();
                assertThat(rs.getFloat(7)).isEqualTo(0.0f);
                assertThat(rs.wasNull()).isTrue();

                assertThat(rs.getDouble(8)).isEqualTo(0.0);
                assertThat(rs.wasNull()).isTrue();

                assertThat(rs.getString(9)).isNull();
            }
        }
    }

    @Test
    public void shouldRetrieveSavedBlob() throws SQLException {
        conn.createStatement().execute("create table blobtest (key int, value blob)");

        byte[] byteArray = randomByteArray();

        int id = 441;
        try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO blobtest(key,value) VALUES (?, ?)")) {
            stmt.setInt(1, id);
            stmt.setBinaryStream(2, new ByteArrayInputStream(byteArray), byteArray.length);
            stmt.executeUpdate();
        }

        try (PreparedStatement stmt = conn.prepareStatement("SELECT value,key FROM blobtest where key = ?")) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                Blob blob = rs.getBlob(1);
                assertThat(blob.length()).isEqualTo(byteArray.length);
                assertThat(blob.getBytes(0, byteArray.length)).isEqualTo(byteArray);
                assertThat(blob.getBytes(1, byteArray.length-2))
                  .hasSize(byteArray.length-2)
                  .startsWith(byteArray[1])
                  .endsWith(byteArray[byteArray.length-2]);
                assertThat(byteArray).containsSubsequence(blob.getBytes(1, byteArray.length-2));
                
                Blob blobAsObj = (Blob)rs.getObject(1);
                assertThat(blobAsObj.getBytes(0, (int)blobAsObj.length()))
                  .isEqualTo(byteArray);
            }
        }
    }

    @Test
    @Ignore("TODO This seems to have been broken by c6a59b700c81c223936f2d38aef13d42cf1f91ca to fix #24")
    public void shouldRetrieveSavedStringAsBlob() throws SQLException {
        conn.createStatement().execute("CREATE TABLE stringblobtest (value TEXT)");
        
        String s = "a random test string";
        byte[] byteArray = s.getBytes();
  
        try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO stringblobtest (value) VALUES (?)")) {
            stmt.setString(1, s);
            stmt.executeUpdate();
        }
        
        try (PreparedStatement stmt = conn.prepareStatement("SELECT value FROM stringblobtest")) {
            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                Blob blob = rs.getBlob(1);
                assertThat(blob.getBytes(0, (int) blob.length()))
                  .isEqualTo(byteArray);
            }
        }
      }

    @Test
    public void shouldSaveAndRetrieveTimestamps() throws SQLException {
        conn.createStatement()
                .execute("create table timestamptest (id integer primary key autoincrement, created_at timestamp)");

        Timestamp timestamp = new Timestamp(new GregorianCalendar(2016, 7, 15, 12, 0, 0).getTimeInMillis());
        long id = executeForGeneratedKey("insert into timestamptest (created_at) values (?)", timestamp);

        try (PreparedStatement stmt = conn.prepareStatement("select created_at from timestamptest where id = ?")) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                assertThat(rs.getTimestamp(1)).isEqualTo(timestamp);
            }
        }
    }

    @Test
    public void shouldReturnGeneratedKeys() throws SQLException {
        conn.createStatement()
                .execute("create table simpletest (id integer primary key autoincrement, value varchar(255))");

        String randomString = UUID.randomUUID().toString();
        long id = executeForGeneratedKey("insert into simpletest (value) values (?)", randomString);

        try (PreparedStatement stmt = conn.prepareStatement("select value from simpletest where id = ?")) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                assertThat(rs.getString(1)).isEqualTo(randomString);
            }
        }
    }

    @Test
    public void shouldSaveAndRetrieveDates() throws SQLException {
        conn.createStatement().execute("create table datetest (id integer primary key autoincrement, created_at date)");

        Date date = new Date(new GregorianCalendar(2016, 7, 15).getTimeInMillis());

        long id = executeForGeneratedKey("insert into datetest (created_at) values (?)", date);

        try (PreparedStatement stmt = conn.prepareStatement("select created_at from datetest where id = ?")) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                assertThat(date)
                        .isEqualTo(rs.getDate(1)).isEqualTo(rs.getDate("created_at"));
            }
        }
    }

    @Test
    public void shouldRetrieveDefaultDates() throws SQLException {
        conn.createStatement().execute("CREATE TABLE datetime_now_test (datetimecol TEXT NOT NULL DEFAULT (datetime('now')), unused TEXT)");
        conn.createStatement().executeUpdate("INSERT INTO datetime_now_test (unused) VALUES (null)");

        try (PreparedStatement stmt = conn.prepareStatement("SELECT datetimecol FROM datetime_now_test")) {
            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                assertThat(rs.getTimestamp("datetimecol").toString())
                  .matches("20\\d\\d-\\d\\d-\\d\\d.*");
            }
        }
    }

    private long executeForGeneratedKey(String query, Object... parameters) throws SQLException {
        return executeForGeneratedKeyWithList(query, Arrays.asList(parameters));
    }
    
    private long executeForGeneratedKeyWithList(String query, List<Object> parameters) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            for (int i = 0; i < parameters.size(); i++) {
                stmt.setObject(i + 1, parameters.get(i));
            }
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                rs.next();
                return rs.getLong(1);
            }
        }
    }

    @Before
    public void createConnection() throws SQLException {
        try {
            DriverManager.registerDriver((Driver) (Class
                    .forName("org.sqldroid.SQLDroidDriver", true, SQLDroidTest2.class.getClassLoader()).newInstance()));
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
            throw new AssertionFailedError(e.toString());
        }
        this.conn = DriverManager.getConnection(createDatabase());
    }

    @After
    public void closeConnection() throws SQLException {
        conn.close();
    }

    private String createDatabase() {
        String filename = "sqldroid-test.db";
        DB_DIR.mkdirs();
        assertThat(DB_DIR).exists();

        File dbFile = new File(DB_DIR, filename);
        dbFile.delete();
        assertThat(dbFile).doesNotExist();

        return "jdbc:sqlite:" + dbFile.getAbsolutePath();
    }

    private static Random random = new Random();

    private byte[] randomByteArray() {
        int blobSize = 1000 + random.nextInt(10_000);
        byte[] aBlob = new byte[blobSize];
        random.nextBytes(aBlob);
        return aBlob;
    }
}
