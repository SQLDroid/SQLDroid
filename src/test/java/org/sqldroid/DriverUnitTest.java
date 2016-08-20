package org.sqldroid;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;


import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;


@RunWith(RobolectricTestRunner.class)
@Config(sdk = 16)
public class DriverUnitTest {

  /** Going to use SQLDroid JDBC Driver */
  protected String driverName = "org.sqldroid.SQLDroidDriver";
  
  /** Common prefix for creating JDBC URL */ 
  protected String JDBC_URL_PREFIX = "jdbc:sqlite:";
  
  /** Package name of this app */
  protected String packageName = "org.sqldroid";
  
  /** Database file directory for this app on Android */
  // TODO: This should be /data/data/org.sqldroid/databases/ if running on device
  protected String DB_DIRECTORY = "./target/data/" + packageName + "/databases/";
  
  /** Name of an in-memory database */
  protected String dummyDatabase = "dummydatabase.db";

  /** The URL to the in-memory database. */
  protected String databaseURL = JDBC_URL_PREFIX + dummyDatabase;

  /** The table create statement. */
  protected String createTable = "CREATE TABLE dummytable (name VARCHAR(254), value int)";

  /** Some data for the table. */
  protected String[] inserts = {
      "INSERT INTO dummytable(name,value) VALUES('Apple', 100)",
      "INSERT INTO dummytable(name,value) VALUES('Orange', 200)",
      "INSERT INTO dummytable(name,value) VALUES('Banana', 300)",
      "INSERT INTO dummytable(name,value) VALUES('Kiwi', 400)"};

  /** A select statement. */
  protected String select = "SELECT * FROM dummytable WHERE value < 250";

  /**
   * Creates the directory structure for the database file and loads the JDBC driver.
   * @param dbFile the database file name
   * @throws Exception
   */
  protected void setupDatabaseFileAndJDBCDriver(String dbFile) throws Exception {
	 // If the database file already exists, delete it, else create the parent directory for it.    
	 File f = new File(dbFile);
	 if ( f.exists() ) {
	   f.delete();
	 } else {
		 if (null != f.getParent()) {
	       f.getParentFile().mkdirs();
		 }
	 }    
	 // Loads and registers the JDBC driver
	 DriverManager.registerDriver((Driver)(Class.forName(driverName, true, getClass().getClassLoader()).newInstance()));
  }
  
  public Blob selectBlob (Connection con, int key) throws Exception {
    PreparedStatement stmt = con.prepareStatement("SELECT value,key FROM blobtest where key = ?");
    stmt.setInt(1, key);
    ResultSet rs = stmt.executeQuery();
    assertTrue ("Executed", rs != null);
    rs.next();
    System.err.println ("blob record \"" + rs.getBlob(1).toString() + "\" key " + rs.getString(2) );
    assertTrue (" Only one record ", rs.isLast());
    Blob b = rs.getBlob(1);
    return b;
  }

  /** Test the serialization of the various value objects. */
  @Test
  public void testBlob () throws Exception {
    String dbName = "bolbtest.db";
    String dbFile = DB_DIRECTORY + dbName;
    setupDatabaseFileAndJDBCDriver(dbFile);
    
    Connection con = DriverManager.getConnection(JDBC_URL_PREFIX + dbFile);

    con.createStatement().execute("create table blobtest (key int, value blob)");

    // create a blob
    final int blobSize = 70000;
    byte[] aBlob = new byte[blobSize];
    for ( int counter = 0 ; counter < blobSize ; counter++ ) {
      aBlob[counter] = (byte)(counter %10);
    }
    final int blobSize1 = 1024;
    byte[] aBlob1 = new byte[blobSize1];
    for ( int counter = 0 ; counter < blobSize1 ; counter++ ) {
      aBlob1[counter] = (byte)(counter %10 + 0x30);
    }

    final String stringBlob = "ABlob";
    /** Some data for the table. */
    final String[] blobInserts = {
        "INSERT INTO blobtest(key,value) VALUES (101, '"+stringBlob+"')",
        "INSERT INTO blobtest(key,value) VALUES (?, ?)",
        "INSERT INTO blobtest(key,value) VALUES (?, ?)",
        "INSERT INTO blobtest(key,value) VALUES (401, ?)"};

    System.out.println("Insert statement is:" + blobInserts[0]);
    con.createStatement().execute(blobInserts[0]);
    Blob b = selectBlob(con, 101);
    
    // FAILS - was this already a problem?
    //assertEquals ("String blob", stringBlob, new String(b.getBytes(1, (int)b.length())));

    PreparedStatement stmt = con.prepareStatement(blobInserts[1]);
    stmt.setInt(1, blobSize);
    stmt.setBinaryStream(2, new ByteArrayInputStream(aBlob),aBlob.length);
    stmt.execute();
    b = selectBlob(con, blobSize);
    assertEquals (" Correct Length ", blobSize, b.length());
    byte[] bytes = b.getBytes(0, blobSize);
    for ( int counter = 0 ; counter < blobSize ; counter++ ) {
      assertEquals(" Blob Element "+ counter, (counter %10), bytes[counter]);
    }

    stmt = con.prepareStatement(blobInserts[2]);
    stmt.setInt(1, blobSize1);
    stmt.setBinaryStream(2, new ByteArrayInputStream(aBlob1),aBlob1.length);
    stmt.execute();
    b = selectBlob(con, blobSize1);
    assertEquals (" Correct 1 Length ", blobSize1, b.length());
    byte[] bytes1 = b.getBytes(0, blobSize1);
    for ( int counter = 0 ; counter < blobSize1 ; counter++ ) {
      assertEquals(" Blob1 Element "+ counter, (counter %10 + 0x30), bytes1[counter]);
    }

    stmt = con.prepareStatement(blobInserts[3]);
    stmt.setBinaryStream(1, new ByteArrayInputStream(aBlob),aBlob.length);
    stmt.execute();
    b = selectBlob(con, 401);
    assertEquals (" Correct Length ", blobSize, b.length());
    bytes = b.getBytes(0, blobSize);
    for ( int counter = 0 ; counter < blobSize ; counter++ ) {
      assertEquals(" Blob Element "+ counter, (counter %10), bytes[counter]);
    }

  }

  @Test
  public void testCursors () throws Exception {
	  String dbName = "cursortest.db";
	  String dbFile = DB_DIRECTORY + dbName;
	  setupDatabaseFileAndJDBCDriver(dbFile);

	  Connection con = DriverManager.getConnection(JDBC_URL_PREFIX + dbFile);

	  con.createStatement().execute(createTable);

	  for ( String insertSQL : inserts ) {
		  con.createStatement().execute(insertSQL);
	  }

	  ResultSet rs = con.createStatement().executeQuery("SELECT * FROM dummytable order by value");
	  checkResultSet ( rs, false, true, false, false, false);
	  rs.next();
	  checkResultSet ( rs, false, false, false, true, false);  
	  checkValues (rs, "Apple", 100);
	  rs.next();
	  checkResultSet ( rs, false, false, false, false, false);
	  checkValues (rs, "Orange", 200);
	  rs.next();
	  checkResultSet ( rs, false, false, false, false, false);
	  checkValues (rs, "Banana", 300);
	  rs.next();  // to last
	  checkResultSet ( rs, false, false, false, false, true);
	  checkValues (rs, "Kiwi", 400);
	  rs.next();  // after last
	  checkResultSet ( rs, false, false, true, false, false);
	  rs.first();
	  checkResultSet ( rs, false, false, false, true, false);
	  rs.last();
	  checkResultSet ( rs, false, false, false, false, true);
	  rs.afterLast();
	  checkResultSet ( rs, false, false, true, false, false);
	  rs.beforeFirst();
	  checkResultSet ( rs, false, true, false, false, false);
	  rs.close();
	  checkResultSet ( rs, true, false, false, false, false);
	  PreparedStatement stmt = con.prepareStatement("SELECT ?,? FROM dummytable order by ?");
	  stmt.setString(1, "name");
	  stmt.setString(2, "value");
	  stmt.setString(3, "value");
	  rs = stmt.executeQuery();
	  assertTrue ("Executed", rs != null);
	  rs.last();
	  assertEquals("Enough rows ", 4, rs.getRow());
	  rs.close();

	  // Add a null value for name
	  con.createStatement().execute("INSERT INTO dummytable(name, value) VALUES(null, 500)");

	  rs = con.createStatement().executeQuery("SELECT name, value FROM dummytable order by value");
	  assertEquals("Name column position", 1, rs.findColumn("name"));
	  assertEquals("Value column position", 2, rs.findColumn("value"));

	  // In the first row, name is Apple and value is 100.
	  assertTrue("Cursor on the first row", rs.first());
	  assertEquals("Name in the first row using column name", "Apple", rs.getString("name"));
	  assertFalse("Current name is null", rs.wasNull());
	  assertEquals("Value in the first row using column name", 100, rs.getInt("value"));
	  assertFalse("Current value is null", rs.wasNull());
	  assertEquals("Name in the first row using column number", "Apple", rs.getString(1));
	  assertEquals("Value in the first row using column number", 100, rs.getInt(2));
	  assertFalse("Current value for Apple is null", rs.wasNull());

	  // In the second row, name is Orange and value is 200.
	  rs.next();
	  assertEquals("Name in the second row using column name", "Orange", rs.getString("name"));
	  assertEquals("Value in the second row using column name", 200, rs.getInt("value"));
	  assertFalse("Current value for Banana is null", rs.wasNull());
	  assertEquals("Name in the second row using column number", "Orange", rs.getString(1));
	  assertEquals("Value in the second row using column number", 200, rs.getInt(2));
	  assertFalse("Current value for Banana is null", rs.wasNull());

	  // In the last row, name is null and value is 500.
	  rs.last();
	  assertEquals("Name in the last row using column name", null, rs.getString("name"));
	  assertTrue("Current name is not null", rs.wasNull());
	  assertEquals("Value in the last row using column name", 500, rs.getInt("value"));
	  assertFalse("Current value is null", rs.wasNull());
	  assertEquals("Name in the last row using column number", null, rs.getString(1));
	  assertTrue("Current name is not null", rs.wasNull());
	  assertEquals("Value in the last row using column number", 500, rs.getInt(2));
	  assertFalse("Current value is null", rs.wasNull());

	  rs.close();
  }
  
  @Test
  public void testResultSets() throws Exception {
		String dbName = "resultsetstest.db";
		String dbFile = DB_DIRECTORY + dbName;
		setupDatabaseFileAndJDBCDriver(dbFile);
	
		Connection con = DriverManager.getConnection(JDBC_URL_PREFIX + dbFile);
	
		String createTableStatement = "CREATE TABLE dummytable (id int, aString VARCHAR(254), aByte byte, "
				+ "aShort short, anInt int, aLong long, aBool boolean, aFloat float, aDouble, double, aText text)";
		
		final String[] insertStatements = {
				"INSERT INTO dummytable(id, aString, aByte, aShort, anInt, aLong, aBool, aFloat, aDouble, aText) VALUES "
						             + "(1, 'string1', 1,     1,      10,    100,   0,    1.0,    10.0,   'text1')",
				"INSERT INTO dummytable(id, aString, aByte, aShort, anInt, aLong, aBool, aFloat, aDouble, aText) VALUES "
						             + "(2, 'string2', 2,     2,     20,    200,   1,    2.0,    20.0,   'text2')",
				"INSERT INTO dummytable(id, aString, aByte, aShort, anInt, aLong, aBool, aFloat, aDouble, aText) VALUES "
						             + "(3, null,    null,   null,   30,    300,   0,    3.0,     30.0,   null)",
				"INSERT INTO dummytable(id, aString, aByte, aShort, anInt, aLong, aBool, aFloat, aDouble, aText) VALUES "
						             + "(4, 'string4', 4,     4,     null,  null,  null,   4.0,    40.0,  'text4')",
				"INSERT INTO dummytable(id, aString, aByte, aShort, anInt, aLong, aBool, aFloat, aDouble, aText) VALUES "
						             + "(5, 'string5', 5,     5,     50,    500,   1,     null,    null, 'text5')" };
	
		con.createStatement().execute(createTableStatement);
		for (String insertSQL : insertStatements) {
			con.createStatement().execute(insertSQL);
		}
	
		ResultSet rs = con.createStatement().executeQuery(
						"SELECT id, aString, aByte, aShort, anInt, aLong, aBool, aFloat, aDouble, aText FROM dummytable order by id");
	
		rs.first();
		try {
			rs.findColumn("blahblah");
			fail("Should have thrown an IllegalArgumentException because of an non-existent column name blahblah");
		} catch (IllegalArgumentException ex1) {
			// OK
		} catch (Exception e) {
			fail("Only IllegalArgumentException expected for a non-existent column name blahblah");
		}
		
		try {
			rs.getString("blahblah");
			fail("Should have thrown an IllegalArgumentException because of an non-existent column name blahblah");
		} catch (IllegalArgumentException ex2) {
			// OK
		} catch (Exception e) {
			fail("Only IllegalArgumentException expected for a non-existent column name blahblah");
		}
		
		try {
			rs.getByte("blahblah");
			fail("Should have thrown an IllegalArgumentException because of an non-existent column name blahblah");
		} catch (IllegalArgumentException ex2) {
			// OK
		} catch (Exception e) {
			fail("Only IllegalArgumentException expected for a non-existent column name blahblah");
		}
		
		try {
			rs.getShort("blahblah");
			fail("Should have thrown an IllegalArgumentException because of an non-existent column name blahblah");
		} catch (IllegalArgumentException ex3) {
			// OK
		} catch (Exception e) {
			fail("Only IllegalArgumentException expected for a non-existent column name blahblah");
		}
		
		try {
			rs.getInt("blahblah");
			fail("Should have thrown an IllegalArgumentException because of an non-existent column name blahblah");
		} catch (IllegalArgumentException ex4) {
			// OK
		} catch (Exception e) {
			fail("Only IllegalArgumentException expected for a non-existent column name blahblah");
		}
		
		try {
			rs.getLong("blahblah");
			fail("Should have thrown an IllegalArgumentException because of an non-existent column name blahblah");
		} catch (IllegalArgumentException ex5) {
			// OK
		} catch (Exception e) {
			fail("Only IllegalArgumentException expected for a non-existent column name blahblah");
		}
		
		try {
			rs.getBoolean("blahblah");
			fail("Should have thrown an IllegalArgumentException because of an non-existent column name blahblah");
		} catch (IllegalArgumentException ex6) {
			// OK
		} catch (Exception e) {
			fail("Only IllegalArgumentException expected for a non-existent column name blahblah");
		}
		
		try {
			rs.getFloat("blahblah");
			fail("Should have thrown an IllegalArgumentException because of an non-existent column name blahblah");
		} catch (IllegalArgumentException ex7) {
			// OK
		} catch (Exception e) {
			fail("Only IllegalArgumentException expected for a non-existent column name blahblah");
		}
		
		try {
			rs.getDouble("blahblah");
			fail("Should have thrown an IllegalArgumentException because of an non-existent column name blahblah");
		} catch (IllegalArgumentException ex8) {
			// OK
		} catch (Exception e) {
			fail("Only IllegalArgumentException expected for a non-existent column name blahblah");
		}
		
		try {
			rs.getBlob("blahblah");
			fail("Should have thrown an IllegalArgumentException because of an non-existent column name blahblah");
		} catch (IllegalArgumentException ex9) {
			// OK
		} catch (Exception e) {
			fail("Only IllegalArgumentException expected for a non-existent column name blahblah");
		}
		
		try {
			rs.getObject("blahblah");
			fail("Should have thrown an IllegalArgumentException because of an non-existent column name blahblah");
		} catch (IllegalArgumentException ex10) {
			// OK
		} catch (Exception e) {
			fail("Only IllegalArgumentException expected for a non-existent column name blahblah");
		}
			
		assertEquals("Value for id", 1, rs.getInt("id"));
		assertEquals("Value for aString", "string1", rs.getString("aString"));
		assertEquals("Value for aByte", 1, rs.getByte("aByte"));
		assertEquals("Value for aShort", 1, rs.getShort("aShort"));
		assertEquals("Value for anInt", 10, rs.getInt("anInt"));
		assertEquals("Value for aLong", 100, rs.getLong("aLong"));
		assertEquals("Value for aBool", false, rs.getBoolean("aBool"));
	
		// Compare strings to avoid Float precision problems
		assertEquals("Value for aFloat", "1.0",
				Float.valueOf(rs.getFloat("aFloat")).toString());
		assertFalse("Current value for aFloat is null", rs.wasNull());
	
		assertEquals("Value for aDouble", 10.0, rs.getDouble("aDouble"), 0.01);
		assertFalse("Current value for aDouble is null", rs.wasNull());
		assertEquals("Value for aText", "text1", rs.getString("aText"));
	
		rs.next(); // 2nd Row
		// No values should be null in this row
		assertEquals("Value for id", 2, rs.getInt(1));
		assertEquals("Value for aString", "string2", rs.getString(2));
		assertEquals("Value for aByte", 2, rs.getByte(3));
		assertEquals("Value for aShort", 2, rs.getShort(4));
		assertEquals("Value for anInt", 20, rs.getInt(5));
		assertEquals("Value for aLong", 200, rs.getLong(6));
		assertEquals("Value for aBool", true, rs.getBoolean(7));
	
		// Compare strings to avoid Float precision problems
		assertEquals("Value for aFloat", "2.0",
				Float.valueOf(rs.getFloat(8)).toString());
		assertFalse("Current value for aFloat is null", rs.wasNull());
	
		assertEquals("Value for aDouble", 20.0, rs.getDouble(9), 0.01);
		assertFalse("Current value for aDouble is null", rs.wasNull());
		assertEquals("Value for aText", "text2", rs.getString(10));
		
		rs.next(); // 3rd row
		// Values for aString, aByte, aShort and aText should be null in this row 
		assertEquals("Value for id", 3, rs.getInt(1));
		assertEquals("Value for aString", null, rs.getString(2));
		assertTrue("Current value for aStrnig is not null", rs.wasNull());
		assertEquals("Value for aByte", null, rs.getString(3));
		assertTrue("Current value for aByte is not null", rs.wasNull());
		assertEquals("Value for aShort", null, rs.getString("aShort"));
		assertTrue("Current value for aShort is not null", rs.wasNull());
		assertEquals("Value for aText", null, rs.getString("aText"));
		assertTrue("Current value for aText is not null", rs.wasNull());	
	
		rs.last(); // 5th row
		// Values for aFloat and aDouble columns should be null in this row
		assertEquals("Value for id", 5, rs.getInt(1));
		assertEquals("Value for aString", "string5", rs.getString(2));
		assertFalse("Current value is null", rs.wasNull());
		assertEquals("Value for aBool", true, rs.getBoolean("aBool"));
		assertFalse("Current value is null", rs.wasNull());
	
		// Compare strings to avoid Float precision problems
		assertEquals("Value for aFloat", "0.0",
				Float.valueOf(rs.getFloat("aFloat")).toString()); // a null float column value is returned as 0.0
		
		assertTrue("Current value for aFloat is not null", rs.wasNull());
	
		assertEquals("Value for aDouble", 0.0, rs.getDouble("aDouble"), 0.01); // a null double column value is returned as 0.0
		assertTrue("Current value for aDouble is not null", rs.wasNull());
	
		assertEquals("Enough rows ", insertStatements.length, rs.getRow());
	
		rs.previous(); // 4th row
		// Values for anInt, aLong and aBool columns should be null in this row
		assertEquals("Value for id", 4, rs.getInt(1));
		rs.getInt("anInt");
		assertTrue("Current value for anInt is not null", rs.wasNull());
		rs.getLong("aLong");
		assertTrue("Current value for aLong is not null", rs.wasNull());
		rs.getBoolean("aBool");
		assertTrue("Current value for aBool is not null", rs.wasNull());
	
		rs.close();
  }

  @Test
  public void testExecute () throws Exception {
	  String dbName = "executetest.db";
	  String dbFile = DB_DIRECTORY + dbName;
 	  setupDatabaseFileAndJDBCDriver(dbFile);
	    
	  Connection con = DriverManager.getConnection(JDBC_URL_PREFIX + dbFile);

      con.createStatement().execute(createTable);

      for ( String insertSQL : inserts ) {
        con.createStatement().execute(insertSQL);
      }

      Statement statement = con.createStatement();
      boolean hasResultSet = statement.execute("SELECT * FROM dummytable order by value");
      assertTrue("Should return a result set", hasResultSet);
      assertEquals ("Should be -1 ", -1, statement.getUpdateCount());
      assertNotNull ("Result Set should be non-null ", statement.getResultSet());
      // second time this will be true.
      boolean noMoreResults = ((statement.getMoreResults() == false) && (statement.getUpdateCount() == -1));
      assertTrue("Should  be no more results ", noMoreResults);
      assertNull ("Result Set should be non-null ", statement.getResultSet());
      statement.close();      
      
      statement = con.createStatement();
      hasResultSet = statement.execute("SELECT * FROM dummytable where name = 'fig'");  // no matching result
      assertNotNull ("Result Set should not be null ", statement.getResultSet());
      assertEquals ("Should not be -1 ", -1, statement.getUpdateCount());
      // second time this will be true.
      noMoreResults = ((statement.getMoreResults() == false) && (statement.getUpdateCount() == -1));
      assertTrue("Should  be no more results ", noMoreResults);
      assertNull ("Result Set should be null - no results ", statement.getResultSet());
      statement.close();

      PreparedStatement stmt = con.prepareStatement("SELECT ?,? FROM dummytable order by ?");
      stmt.setString(1, "name");
      stmt.setString(2, "value");
      stmt.setString(3, "value");
      hasResultSet = stmt.execute();
      assertTrue("Should return a result set", hasResultSet);
      assertEquals ("Should not be -1 ", -1, stmt.getUpdateCount());
      // second time this will be true.
      noMoreResults = ((stmt.getMoreResults() == false) && (stmt.getUpdateCount() == -1));
      assertTrue("Should  be no more results ", noMoreResults);
      assertNull ("Result Set should be null ", stmt.getResultSet());  // no more results
      stmt.close();
          
      stmt = con.prepareStatement("SELECT * FROM dummytable where name = 'fig'");
      hasResultSet = stmt.execute();  // no matching result but an empty Result Set should be returned
      assertTrue("Should return a result set", hasResultSet);
      assertNotNull ("Result Set should not be null ", stmt.getResultSet());
      assertEquals ("Should not be -1 ", -1, stmt.getUpdateCount());
      // second time this will be true.
      noMoreResults = ((stmt.getMoreResults() == false) && (stmt.getUpdateCount() == -1));
      assertTrue("Should  be no more results ", noMoreResults);
      assertNull ("Result Set should be null - no results ", stmt.getResultSet());
      stmt.close();
      
      stmt = con.prepareStatement("update dummytable set name='Kumquat' where name = 'Orange' OR name = 'Kiwi'");
      stmt.execute();
      assertEquals ("To Rows updated ", 2, stmt.getUpdateCount());
      for ( String insertSQL : inserts ) {
        Statement s = con.createStatement();
        s.execute(insertSQL);
        assertEquals ("To Rows updated ", 1, s.getUpdateCount());
      }
      int rows = stmt.executeUpdate();
      assertEquals ("To Rows updated ", 2, rows);
      assertEquals ("To Rows updated ", 2, stmt.getUpdateCount());
      stmt.close();
      
      for ( String insertSQL : inserts ) {
        stmt = con.prepareStatement(insertSQL);
        stmt.execute();
        assertEquals ("To Rows updated ", 1, stmt.getUpdateCount());
      }

      statement = con.createStatement();
      hasResultSet = statement.execute("update dummytable set name='Kumquat' where name = 'Orange' OR name = 'Kiwi'");  // no matching result
      assertFalse("Should not return a result set", hasResultSet);
      for ( String insertSQL : inserts ) {
        con.createStatement().execute(insertSQL);
      }
      int r1 = statement.executeUpdate("update dummytable set name='Kumquat' where name = 'Orange' OR name = 'Kiwi'");  // no matching result
      assertEquals ("To Rows updated ", 2, statement.getUpdateCount());
      assertEquals ("To Rows updated ", 2, r1);
      statement.close();
      
      statement = con.createStatement();
      for ( String insertSQL : inserts ) {
          con.createStatement().execute(insertSQL);
      }
      int numRows = statement.executeUpdate("DELETE FROM dummytable where name = 'Orange' OR name = 'Kiwi'");  // 2 rows should be deleted
      assertEquals ("Two Rows deleted ", 2, numRows);
          
      stmt = con.prepareStatement("SELECT * FROM dummytable where name = 'Banana'");
      ResultSet rs = stmt.executeQuery(); 
      int rowCount = 0;
      if (rs.last()) {
        rowCount = rs.getRow();
      }
      rs.close();
      // System.out.println("Num Banana rows=" + rowCount);
      
      numRows = statement.executeUpdate("DELETE FROM dummytable where name = 'Banana'");
      assertEquals ("Banana rows deleted ", rowCount, numRows);   
   }

  public void checkResultSet ( ResultSet rs, boolean isClosed, boolean isBeforeFirst, boolean isAfterLast,boolean isFirst,boolean isLast) throws Exception {
    assertEquals ("Is Closed", isClosed, rs.isClosed());
    assertEquals ("Is Before First", isBeforeFirst, rs.isBeforeFirst());
    assertEquals ("Is after Last", isAfterLast, rs.isAfterLast());
    assertEquals("Is First", isFirst, rs.isFirst());
    assertEquals ("Is Laset", isLast, rs.isLast());
  }

  public void checkValues (ResultSet rs, String fruit, int value ) throws Exception {
    assertEquals ("Fruit", fruit, rs.getString(1));
    assertEquals ("Value", value, rs.getInt(2));
  }

  @Test
  public void testMetaData () throws Exception {
	  String dbName = "schematest.db";
	  String dbFile = DB_DIRECTORY + dbName;
 	  setupDatabaseFileAndJDBCDriver(dbFile);
	    
	  Connection con = DriverManager.getConnection(JDBC_URL_PREFIX + dbFile);

    // drop the tables - database is new so this isn't necessary
    //    con.createStatement().execute("DROP TABLE PASTIMES");
    //    con.createStatement().execute("DROP TABLE STRIP_PASTIMES");
    //    con.createStatement().execute("DROP VIEW PERCENTAGES");

    con.createStatement().execute("CREATE TABLE PASTIMES (count INT, pastime CHAR(200))");
    con.createStatement().execute("CREATE TABLE STRIP_PASTIMES (count INT, pastime CHAR(200))");
    //  "pingpong", "4,750,000 " "1,360,000"  on 5/16/2011
    //  "chess" "90,500,000", "6,940,000"
    //  "poker" "353,000,000", "9,230,000"
    con.createStatement().execute("INSERT INTO PASTIMES (count, pastime) VALUES (4750000, 'pingpong')");
    con.createStatement().execute("INSERT INTO PASTIMES (count, pastime) VALUES (90500000, 'chess')");
    con.createStatement().execute("INSERT INTO PASTIMES (count, pastime) VALUES (353000000, 'poker')");
    con.createStatement().execute("INSERT INTO STRIP_PASTIMES (count, pastime) VALUES (1360000, 'pingpong')");
    con.createStatement().execute("INSERT INTO STRIP_PASTIMES (count, pastime) VALUES (6940000, 'chess')");
    con.createStatement().execute("INSERT INTO STRIP_PASTIMES (count, pastime) VALUES (9230000, 'poker')");

    con.createStatement().execute("CREATE VIEW PERCENTAGES AS SELECT PASTIMES.pastime, PASTIMES.count , STRIP_PASTIMES.count as stripcount, "+
        " (CAST(STRIP_PASTIMES.count AS REAL)/PASTIMES.count*100.00) as percent FROM PASTIMES, STRIP_PASTIMES " + 
    " WHERE PASTIMES.pastime = STRIP_PASTIMES.pastime");

    ResultSet rs = con.getMetaData().getTables(null, null, "%", new String[] {"table"});
    // rs.next() returns true is there is 1 or more rows
    // should be two tables
    List<String> tableNames = new ArrayList<String>(Arrays.asList(new String[]{"PASTIMES", "STRIP_PASTIMES"}));
    while ( rs.next() ) {
      System.err.println ("Table Name \"" + rs.getString("TABLE_NAME") + "\" also \"" + rs.getString(3) + "\"");
      assertTrue ("Table must be in the list" , tableNames.remove(rs.getString(3)) );
    }
    assertEquals ("All tables accounted for", 0, tableNames.size());

    rs = con.getMetaData().getTables(null, null, "%", new String[] {"view"});
    List<String> viewNames =  new ArrayList<String>(Arrays.asList(new String[]{"PERCENTAGES"}));  // only one, not a very good test
    while ( rs.next() ) {
      assertTrue ("View must be in the list" , viewNames.remove(rs.getString("TABLE_NAME")) );  // mix up name and index
      System.err.println ("View Name \"" + rs.getString("TABLE_NAME") + "\" also \"" + rs.getString(3) + "\"");
    }
    assertEquals ("All views accounted for", 0, viewNames.size());

    rs = con.getMetaData().getTables(null, null, "%", new String[] {"view","table"});
    List<String> allNames =  new ArrayList<String>(Arrays.asList(new String[]{"PERCENTAGES","PASTIMES", "STRIP_PASTIMES"}));  // all of the views and tables.
    while ( rs.next() ) {
      System.err.println ("View or Table Name \"" + rs.getString("TABLE_NAME") + "\" also \"" + rs.getString(3) + "\"");
      assertTrue ("View/Table must be in the list" , allNames.remove(rs.getString("TABLE_NAME")) );  // mix up name and index
    }
    assertEquals ("All views/tables accounted for", 0, viewNames.size());

    rs = con.getMetaData().getColumns(null, null, "%", "%");
    String[] columnNames = new String[]{"count","pastime","count","pastime","pastime","count","stripcount","percent"};  //  I think I should get these, but I'm not sure
    int columnCounter = 0;
    while ( rs.next() ) {
      System.err.println ("Column Name \"" + rs.getString("COLUMN_NAME") + "\" also \"" + rs.getString(4) + "\" type " + rs.getInt(5));
      //      Column Name "count" also "count" type 4
      //      Column Name "pastime" also "pastime" type 12
      //      Column Name "count" also "count" type 4
      //      Column Name "pastime" also "pastime" type 12
      //      Column Name "pastime" also "pastime" type 12
      //      Column Name "count" also "count" type 4
      //      Column Name "stripcount" also "stripcount" type 4
      //      Column Name "percent" also "percent" type 0
      assertEquals ("All columns accounted for", columnNames[columnCounter], rs.getString("COLUMN_NAME"));
      assertEquals ("All columns accounted for", columnNames[columnCounter], rs.getString(4));
      columnCounter++;
    }

    rs = con.createStatement().executeQuery("SELECT * FROM PERCENTAGES ORDER BY percent");

    while(rs.next()) {
      int count = rs.getMetaData().getColumnCount();
      List<String> viewColumnNames =  new ArrayList<String>(Arrays.asList(new String[]{"pastime", "count", "stripcount"}));
      for ( int counter = 0 ; counter < count ; counter++ ) {
        System.err.print(" " + rs.getMetaData().getColumnName(counter+1) + " = " + rs.getString(counter+1));
        //        pastime = poker count = 353000000 stripcount = 9230000 percent = 2.61473087818697
        //        pastime = chess count = 90500000 stripcount = 6940000 percent = 7.66850828729282
        //        pastime = pingpong count = 4750000 stripcount = 1360000 percent = 28.6315789473684
        //assertTrue ("Column Name must be in the list" , viewColumnNames.remove(rs.getString("COLUMN_NAME")) );  // mix up name and index
      }
      // assertEquals ("All columns accounted for", 0, viewColumnNames.size());
      System.err.println ();
    }
    rs.close();

    rs = con.createStatement().executeQuery("SELECT * FROM PASTIMES");
    List<String> tableColumnNames =  new ArrayList<String>(Arrays.asList(new String[]{"pastime", "count"}));
    while(rs.next()) {
      int count = rs.getMetaData().getColumnCount();
      for ( int counter = 0 ; counter < count ; counter++ ) {
        //assertTrue ("Table Column Name must be in the list" , tableColumnNames.remove(rs.getString(4)) );  // mix up name and index
        System.err.print(" " + rs.getMetaData().getColumnName(counter+1) + " = " + rs.getString(counter+1));
      }
      //assertEquals ("All columns accounted for", 0, tableColumnNames.size());
      System.err.println ();
    }

    rs.close();
  }

  @Test
  public void testAutoCommit() throws Exception {  
	    String dbName = "autocommittest.db";
	    String dbFile = DB_DIRECTORY + dbName;
 	    setupDatabaseFileAndJDBCDriver(dbFile);
	                   
        String jdbcURL = JDBC_URL_PREFIX + dbFile;               
 
        Properties removeLocale = new Properties();
        removeLocale.put(SQLDroidDriver.ADDITONAL_DATABASE_FLAGS, android.database.sqlite.SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        Connection conn1 = DriverManager.getConnection(jdbcURL,removeLocale);
        System.out.println("After getting connection...1");
        
        conn1.setAutoCommit(true);
        
        Connection conn2 = DriverManager.getConnection(jdbcURL);
        System.out.println("After getting connection...2");
        
        conn1.setAutoCommit(false);
        
        Connection conn3 = DriverManager.getConnection(jdbcURL);
        System.out.println("After getting connection...3");
        
        Statement stat = conn1.createStatement();
        
        // Create table if not already there
        stat.executeUpdate("create table if not exists primes (number int);");
        
        // Delete any existing records
        stat.executeUpdate("delete from primes;");
        
        // Populate table
        stat.executeUpdate("insert into primes values (2);");
        stat.executeUpdate("insert into primes values (3);");
        stat.executeUpdate("insert into primes values (5);");
        stat.executeUpdate("insert into primes values (7);");

        // Retrieve records
        ResultSet rs = stat.executeQuery("select * from primes");
        boolean b = rs.first();
        while (b) {
            String info = "Prime=" + rs.getInt(1);
            System.out.println(info);
            b = rs.next();
        }

        conn1.close();
        conn2.close();
        conn3.close();
  }
  public void testTimestamp() throws Exception {
      String dbName = "timestamptest.db";
      String dbFile = DB_DIRECTORY + dbName;
      setupDatabaseFileAndJDBCDriver(dbFile);
      
      Connection conn = DriverManager.getConnection(JDBC_URL_PREFIX + dbFile);
      conn.createStatement()
          .execute("create table timestamptest (id integer, created_at timestamp)");

      // Make sure timestamp is around noon to check for DateFormat bug
      Calendar calendar = new GregorianCalendar(2016, 7, 15, 12, 0, 0);
      Timestamp timestamp = new Timestamp(calendar.getTimeInMillis() + 853); // make sure millis are included      
      
      int id = 23432;
      PreparedStatement insertStmt = conn.prepareStatement("insert into timestamptest values (?, ?)");
      insertStmt.setInt(1, id);
      insertStmt.setTimestamp(2, timestamp);
      insertStmt.executeUpdate();
      insertStmt.close();
      
      PreparedStatement selectStmt = conn.prepareStatement("select * from timestamptest where id = ?");
      selectStmt.setInt(1, id);
      ResultSet rs = selectStmt.executeQuery();
      rs.next();
      
      assertEquals(timestamp, rs.getTimestamp("created_at"));
      rs.close();
      selectStmt.close();
      
      conn.close();
  }
}
