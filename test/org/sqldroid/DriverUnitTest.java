package org.sqldroid;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.lang.reflect.Method;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class DriverUnitTest extends TestCase {

  /** This is the name of the database connection. */

  /** Going to use SQLDroid */
  protected String driverName = "org.sqldroid.SQLDroidDriver";

  /** The URL to an in-memory database. */
  protected String databaseURL = "jdbc:sqlite:dummydatabase.db";

  /** The table create statement. */
  protected String createTable = "CREATE TABLE dummytable (  name VARCHAR(254), value int)";

  /** Some data for the table. */
  protected String[] inserts = {
      "INSERT INTO dummytable(name,value) VALUES('Apple', 100)",
      "INSERT INTO dummytable(name,value) VALUES('Orange', 200)",
      "INSERT INTO dummytable(name,value) VALUES('Banana', 300)",
  "INSERT INTO dummytable(name,value) VALUES('Kiwi', 400)"};

  /** A select statement. */
  protected String select = "SELECT * FROM dummytable WHERE value < 250";

  /** Constructor. */
  public DriverUnitTest (String name) {
    super(name);
  }
  
  public Blob selectBlob (Connection con, int key ) throws Exception {
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
  public void testBlob () throws Exception {

    DriverManager.registerDriver((Driver)(Class.forName(driverName, true, getClass().getClassLoader()).newInstance()));

    File f = new File("blobtest");
    if ( f.exists() ) {
      f.delete();
    }
    Connection con = DriverManager.getConnection("jdbc:sqldroid:blobtest");

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

    con.createStatement().execute(blobInserts[0]);
    Blob b = selectBlob(con, 101);
    assertEquals ("String blob", stringBlob, new String(b.getBytes(0, (int)b.length())));

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

  public void testCursors () throws Exception {

      DriverManager.registerDriver((Driver)(Class.forName(driverName, true, getClass().getClassLoader()).newInstance()));

      File f = new File("cursortest");
      if ( f.exists() ) {
        f.delete();
      }
      Connection con = DriverManager.getConnection("jdbc:sqldroid:cursortest");

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
      assertEquals( "Enough rows ", 4, rs.getRow());

    }

  public void testExecute () throws Exception {

      DriverManager.registerDriver((Driver)(Class.forName(driverName, true, getClass().getClassLoader()).newInstance()));

      File f = new File("cursortest");
      if ( f.exists() ) {
        f.delete();
      }
      Connection con = DriverManager.getConnection("jdbc:sqldroid:cursortest");

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
      assertFalse("Should not return a result set", hasResultSet);
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
      hasResultSet = stmt.execute();  // no matching result
      assertFalse("Should return a result set", hasResultSet);
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

  public void testMetaData () throws Exception {
    // register the driver.
    DriverManager.registerDriver((Driver)(Class.forName(driverName, true, getClass().getClassLoader()).newInstance()));

    File f = new File("schematest");
    if ( f.exists() ) {
      f.delete();
    }
    Connection con = DriverManager.getConnection("jdbc:sqldroid:schematest");

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

  public void testAutoCommit() throws Exception {
        String databasePath = "autocommit.db";
                   
        String jdbcURL = "jdbc:sqldroid:" + databasePath;           
        Class.forName("org.sqldroid.SQLDroidDriver");
        
        // String jdbcURL = "jdbc:sqlite:" + databasePath;           
        // Class.forName("SQLite.JDBCDriver");

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



  public static Test suite () {
    TestSuite suite =  new TestSuite("SQLDroid Tests");
    suite.addTest(new DriverUnitTest("testAutoCommit"));
    suite.addTest(new DriverUnitTest("testBlob"));
    suite.addTest(new DriverUnitTest("testMetaData"));
    suite.addTest(new DriverUnitTest("testCursors"));
    suite.addTest(new DriverUnitTest("testExecute"));
    return  suite;
  }


  /** Run the test cases by hand. */
  @SuppressWarnings({ "rawtypes", "unchecked" })
public static void main(String[] argv) {
      //junit.textui.TestRunner.run(DriverUnitTest.suite());
      try {
          Class clz = Class.forName("junit.textui.TestRunner");
          Method m = clz.getMethod("run", new Class[]{Test.class});
          m.invoke(null, new Object[] {DriverUnitTest.suite()});
      } catch ( Exception any ) {
          any.printStackTrace();
      }
  }

}
