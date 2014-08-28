package org.sqldroid;

import android.database.Cursor;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SQLDroidPreparedStatement implements PreparedStatement {

  protected SQLiteDatabase db;
  protected SQLDroidConnection sqldroidConnection;
  protected SQLDroidResultSet rs = null;
  protected String sql;
  protected ArrayList<Object> l = new ArrayList<Object>();
  protected ArrayList<ArrayList<Object>> lBatch = new ArrayList<ArrayList<Object>>();
  private Integer maxRows = null;

  /** True if the sql statement is a select. */
  protected boolean isSelect;

  /** True if the sql statement MAY produce a result set.  For example, "create" and "drop" command will
   * produce a "false" value for this. */
  protected boolean potentialResultSet;

  /** The update count.  We don't know this, but need to respond in such a way that:
   * (from getMoreResults) There are no more results when the following is true:
   *
   *      // stmt is a Statement object
   *      ((stmt.getMoreResults() == false) &amp;&amp; (stmt.getUpdateCount() == -1))

   * This is used by <code>getUpdateCount()</code>.  If there is a resultSet
   * then getUpdateCount will return -1.  If there is no result set, then, presumably,
   * <code>execute()</code> was called and we have one result and so can return something
   * other than -1 on the first call to getUpdateCount.   In this case, the second call to getUpdateCount
   * we should return -1;
   * We set this to zero on execute() and decrement it on getUpdateCount.  If the value of updateCount
   * is -1 then we just return it from getUpdateCount.
   */
  public int updateCount = -1;

  public SQLDroidPreparedStatement(String sql, SQLDroidConnection sqldroid) {
    Log.v("SQLDRoid", "new SqlDRoid prepared statement from " + sqldroid);
    this.sqldroidConnection = sqldroid;
    this.db = sqldroid.getDb();
    setSQL(sql);
  }


  private void ensureCap(int n) {
  }


  private void setObj(int n, Object obj) {

    // prapred statments count from 1, we count from 0 (in array)
    n--;

    // if arraylist is too small we add till it's grand enough
    // btw, why ain't there a l.setSize(n)?


    int additions = n - l.size() + 1;
    //System.out.println("adding " + additions + " elements");
    for(int i = 0 ; i < additions ; i++) {
     // System.out.println("ADD NULL");
      l.add(null);
    }

    //System.out.println("n = " + n + " size now " + l.size() + " we @ " + n);
    l.set(n, obj);
    //System.out.println("POST set n = " + n + " size now " + l.size() + " we @ " + n);
  }

  @Override
  public void addBatch(String sql) throws SQLException {
    //sql must be a static sql
    setSQL(getSQL() + sql);
  }

  /**
   * @return the sql
   */
  public String getSQL() {
    return sql;
  }

  /**
   * @param sql the sql to set
   */
  public void setSQL(String sql) {
    // this is loosely based on the codee in SqlDroidStatement
    this.sql = sql;
    isSelect = sql.toUpperCase().matches("(?m)(?s)\\s*SELECT.*");
    potentialResultSet = true;
    // examples of a failure on the next line (so isSelect is false and potentialResultSet remains true, are PRAGMA and INSERT (why INSERT?)
    if (!isSelect && (sql.toUpperCase().matches("(?m)(?s)\\s*CREATE.*") || sql.toUpperCase().matches("(?m)(?s)\\s*DROP.*")) ) {
      potentialResultSet = false;
    }
  }


  @Override
  public void cancel() throws SQLException {
    System.err.println(" ********************* not implemented @ "
        + DebugPrinter.getFileName() + " line "
        + DebugPrinter.getLineNumber());

  }

  @Override
  public void clearBatch() throws SQLException {
    sql = "";
    lBatch = new ArrayList<ArrayList<Object>>();
  }

  @Override
  public void clearWarnings() throws SQLException {
    System.err.println(" ********************* not implemented @ "
        + DebugPrinter.getFileName() + " line "
        + DebugPrinter.getLineNumber());

  }

  @Override
  public void close() throws SQLException {
    try {
      if ( rs != null ) {
        rs.close();
      }
    } finally {
      rs = null;
    }
  }

  private String[] makeArgListQueryString() {
    if ( l == null || l.size() == 0 ) {
      return new String[0];
    }
    // convert our parameter list objects to strings
    List<String> strList = new ArrayList<String>();

    for(Object o : l) {
      strList.add(o.toString());
    }

    return strList.toArray(new String [1]);
  }
  private Object[] makeArgListQueryObject() {
    return l.toArray();
  }

    @Override
    public boolean execute() throws SQLException {
        updateCount	= -1;
        closeResultSet();
        // problem, a PRAGMA statement (and maybe others) should also cause a result set
        if ( !isSelect && sql.toUpperCase().matches("(?m)(?s)\\s*PRAGMA.*") ) {
            isSelect = true;
        }
        if (isSelect) {
            String limitedSql = sql + (maxRows != null ? " LIMIT " + maxRows : "");
            Cursor c = db.rawQuery(limitedSql, makeArgListQueryString());
            rs = new SQLDroidResultSet(c);
        } else {
            db.execSQL(sql, makeArgListQueryObject());
            updateCount = db.changedRowCount();
        }
        return isSelect;
    }

  /** Close the result set (if open) and null the rs variable. */
  public void closeResultSet() throws SQLException {
    if (rs != null && !rs.isClosed()) {
      if (!rs.isClosed()) {
        rs.close();
      }
      rs = null;
    }
  }

  @Override
  public ResultSet executeQuery() throws SQLException {
    updateCount = -1;
    closeResultSet();
    //Log.d("sqldroid", "executeQuery " + sql);
    // when querying, all ? values must be converted to Strings for some reason
    Cursor c = db.rawQuery(sql, makeArgListQueryString());
    //Log.d("sqldroid", "executeQuery " + 2);
    rs = new SQLDroidResultSet(c);
    //Log.d("sqldroid", "executeQuery " + 3);
    return rs;
  }

  @Override
  public int executeUpdate() throws SQLException {
    // TODO we can't count the actual number of updates .... :S
    execute();
    return updateCount;
  }

  @Override
  public boolean execute(String sql) throws SQLException {
    setSQL(sql);
    return execute();
  }
  @Override
  public boolean execute(String sql, int autoGeneratedKeys)
  throws SQLException {

    System.err.println(" ********************* not implemented @ "
        + DebugPrinter.getFileName() + " line "
        + DebugPrinter.getLineNumber());

    return false;
  }

  @Override
  public boolean execute(String sql, int[] columnIndexes) throws SQLException {

    System.err.println(" ********************* not implemented @ "
        + DebugPrinter.getFileName() + " line "
        + DebugPrinter.getLineNumber());

    return false;
  }

  @Override
  public boolean execute(String sql, String[] columnNames)
  throws SQLException {

    System.err.println(" ********************* not implemented @ "
        + DebugPrinter.getFileName() + " line "
        + DebugPrinter.getLineNumber());

    return false;
  }

  @Override
  public int[] executeBatch() throws SQLException {
    int[] results = new int[lBatch.size()];
    for(int i=0; i < lBatch.size(); i++) {
      updateCount = -1;
      results[i] = EXECUTE_FAILED;
      db.execSQL(sql, lBatch.get(i).toArray());
      results[i] = db.changedRowCount();
      updateCount = results[i];
    }
    return results;
  }

  @Override
  public ResultSet executeQuery(String sql) throws SQLException {
    setSQL(sql);
    return executeQuery();
  }


  @Override
  public int executeUpdate(String sql) throws SQLException {
    setSQL(sql);
    return executeUpdate();
  }

  @Override
  public int executeUpdate(String sql, int autoGeneratedKeys)
  throws SQLException {
    System.err.println(" ********************* not implemented @ "
        + DebugPrinter.getFileName() + " line "
        + DebugPrinter.getLineNumber());
    return 0;
  }

  @Override
  public int executeUpdate(String sql, int[] columnIndexes)
  throws SQLException {
    System.err.println(" ********************* not implemented @ "
        + DebugPrinter.getFileName() + " line "
        + DebugPrinter.getLineNumber());
    return 0;
  }

  @Override
  public int executeUpdate(String sql, String[] columnNames)
  throws SQLException {
    System.err.println(" ********************* not implemented @ "
        + DebugPrinter.getFileName() + " line "
        + DebugPrinter.getLineNumber());
    return 0;
  }

  @Override
  public Connection getConnection() throws SQLException {
    return sqldroidConnection;
  }

  @Override
  public int getFetchDirection() throws SQLException {
    System.err.println(" ********************* not implemented @ "
        + DebugPrinter.getFileName() + " line "
        + DebugPrinter.getLineNumber());
    return 0;
  }

  @Override
  public int getFetchSize() throws SQLException {
    System.err.println(" ********************* not implemented @ "
        + DebugPrinter.getFileName() + " line "
        + DebugPrinter.getLineNumber());
    return 0;
  }

  @Override
  public ResultSet getGeneratedKeys() throws SQLException {
    System.err.println(" ********************* not implemented @ "
        + DebugPrinter.getFileName() + " line "
        + DebugPrinter.getLineNumber());
    return null;
  }

  @Override
  public int getMaxFieldSize() throws SQLException {
    System.err.println(" ********************* not implemented @ "
        + DebugPrinter.getFileName() + " line "
        + DebugPrinter.getLineNumber());
    return 0;
  }

  @Override
  public int getMaxRows() throws SQLException {
    return maxRows;
  }

  @Override
  /** There are no more results when the following is true:

	     // stmt is a Statement object
	     ((stmt.getMoreResults() == false) && (stmt.getUpdateCount() == -1))*/
  public boolean getMoreResults() throws SQLException {
    return getMoreResults(CLOSE_CURRENT_RESULT);
  }

  @Override
  public boolean getMoreResults(int current) throws SQLException {
    if ( current == CLOSE_CURRENT_RESULT ) {
      closeResultSet();
    }
    return false;
  }

  @Override
  public int getQueryTimeout() throws SQLException {
    System.err.println(" ********************* not implemented @ "
        + DebugPrinter.getFileName() + " line "
        + DebugPrinter.getLineNumber());
    return 0;
  }

  @Override
  public ResultSet getResultSet() throws SQLException {
    return rs;
  }

  @Override
  public int getResultSetConcurrency() throws SQLException {
    System.err.println(" ********************* not implemented @ "
        + DebugPrinter.getFileName() + " line "
        + DebugPrinter.getLineNumber());
    return 0;
  }

  @Override
  public int getResultSetHoldability() throws SQLException {
    System.err.println(" ********************* not implemented @ "
        + DebugPrinter.getFileName() + " line "
        + DebugPrinter.getLineNumber());
    return 0;
  }

  @Override
  public int getResultSetType() throws SQLException {
    System.err.println(" ********************* not implemented @ "
        + DebugPrinter.getFileName() + " line "
        + DebugPrinter.getLineNumber());
    return 0;
  }

  /**Retrieves the current result as an update count; if the result is a ResultSet object or there are no more results, -1 is returned. This method should be called only once per result.
	Returns:
	the current result as an update count; -1 if the current result is a ResultSet object or there are no more results*/
  @Override
  public int getUpdateCount() throws SQLException {
    if ( updateCount != -1 ) {  // for any successful update/insert, update count will have been set
      // the documenation states that you're only supposed to call this once per result.
      // on subsequent calls, we'll return -1 (which would appear to be the correct return
      int tmp = updateCount;
      updateCount = -1;
      return tmp;
    }
    return updateCount;  // if the result was a result set, or this is the second call, then this will be -1
  }


  @Override
  public SQLWarning getWarnings() throws SQLException {
    System.err.println(" ********************* not implemented @ "
        + DebugPrinter.getFileName() + " line "
        + DebugPrinter.getLineNumber());
    return null;
  }

  @Override
  public void setCursorName(String name) throws SQLException {
    System.err.println(" ********************* not implemented @ "
        + DebugPrinter.getFileName() + " line "
        + DebugPrinter.getLineNumber());
  }

  @Override
  public void setEscapeProcessing(boolean enable) throws SQLException {
    System.err.println(" ********************* not implemented @ "
        + DebugPrinter.getFileName() + " line "
        + DebugPrinter.getLineNumber());
  }

  @Override
  public void setFetchDirection(int direction) throws SQLException {
    System.err.println(" ********************* not implemented @ "
        + DebugPrinter.getFileName() + " line "
        + DebugPrinter.getLineNumber());
  }

  @Override
  public void setFetchSize(int rows) throws SQLException {
    System.err.println(" ********************* not implemented @ "
        + DebugPrinter.getFileName() + " line "
        + DebugPrinter.getLineNumber());
  }

  @Override
  public void setMaxFieldSize(int max) throws SQLException {
    System.err.println(" ********************* not implemented @ "
        + DebugPrinter.getFileName() + " line "
        + DebugPrinter.getLineNumber());
  }

  @Override
  public void setMaxRows(int max) throws SQLException {
      if (isClosed()) {
          throw new SQLException("Statement is closed.");
        } else if (max < 0) {
          throw new SQLException("Max rows must be zero or positive. Got " + max);
        } else if (max == 0) {
          maxRows = null;
        } else {
          maxRows = max;
        }
  }

  @Override
  public void setQueryTimeout(int seconds) throws SQLException {
    System.err.println(" ********************* not implemented @ "
        + DebugPrinter.getFileName() + " line "
        + DebugPrinter.getLineNumber());
  }

  @Override
  public void addBatch() throws SQLException {
    int n = lBatch.size();
    lBatch.add(null);
    lBatch.set(n, l);
    clearParameters();
  }

  @Override
  public void clearParameters() throws SQLException {
    l = new ArrayList<Object>();
  }

  @Override
  public ResultSetMetaData getMetaData() throws SQLException {
    System.err.println(" ********************* not implemented @ "
        + DebugPrinter.getFileName() + " line "
        + DebugPrinter.getLineNumber());
    return null;
  }

  @Override
  public ParameterMetaData getParameterMetaData() throws SQLException {
    System.err.println(" ********************* not implemented @ "
        + DebugPrinter.getFileName() + " line "
        + DebugPrinter.getLineNumber());
    return null;
  }

  @Override
  public void setArray(int parameterIndex, Array theArray)
  throws SQLException {
    System.err.println(" ********************* not implemented @ "
        + DebugPrinter.getFileName() + " line "
        + DebugPrinter.getLineNumber());
  }

  @Override
  public void setAsciiStream(int parameterIndex, InputStream theInputStream,
      int length) throws SQLException {
    System.err.println(" ********************* not implemented @ "
        + DebugPrinter.getFileName() + " line "
        + DebugPrinter.getLineNumber());
  }

  @Override
  public void setBigDecimal(int parameterIndex, BigDecimal theBigDecimal)
  throws SQLException {
    System.err.println(" ********************* not implemented @ "
        + DebugPrinter.getFileName() + " line "
        + DebugPrinter.getLineNumber());
  }
  /**
   * Set the parameter from the contents of a binary stream.
   * @param parameterIndex the index of the parameter to set
   * @param inputStream the input stream from which a byte array will be read and set as the value.  If inputStream is null
   * this method will throw a SQLException
   * @param length a positive non-zero length values
   * @exception SQLException thrown if the length is &lt;= 0, the inputStream is null,
   * there is an IOException reading the inputStream or if "setBytes" throws a SQLException
   */
  @Override
   public void setBinaryStream(int parameterIndex, InputStream inputStream, int length) throws SQLException {
    if (length <= 0) {
      throw new SQLException ("Invalid length " + length);
    }
    if (inputStream == null ) {
      throw new SQLException ("Input Stream cannot be null");
    }
    final int bufferSize = 8192;
    byte[] buffer = new byte[bufferSize];
    ByteArrayOutputStream outputStream = null;
    try {
      outputStream = new ByteArrayOutputStream();
      int bytesRemaining = length;
      int bytesRead;
      int maxReadSize;
      while (bytesRemaining > 0) {
        maxReadSize = (bytesRemaining > bufferSize) ? bufferSize : bytesRemaining;
        bytesRead = inputStream.read(buffer, 0, maxReadSize);
        if (bytesRead == -1) { // inputStream exhausted
          break;
        }
        outputStream.write(buffer, 0, bytesRead);
        bytesRemaining = bytesRemaining - bytesRead;
      }
      setBytes(parameterIndex, outputStream.toByteArray());
      outputStream.close();
    } catch (IOException e) {
      e.printStackTrace();
      throw new SQLException(e.getMessage());
    }
  }

  @Override
  public void setBlob(int parameterIndex, Blob theBlob) throws SQLException {
    ensureCap(parameterIndex);
    setObj(parameterIndex, theBlob.getBytes(1, (int)theBlob.length()));
  }

  @Override
  public void setBoolean(int parameterIndex, boolean theBoolean) throws SQLException {
    ensureCap(parameterIndex);
    setObj(parameterIndex, theBoolean);

  }

  @Override
  public void setByte(int parameterIndex, byte theByte) throws SQLException {
    ensureCap(parameterIndex);
    setObj(parameterIndex, theByte);
  }

  @Override
  public void setBytes(int parameterIndex, byte[] theBytes)
  throws SQLException {
    ensureCap(parameterIndex);
    setObj(parameterIndex, theBytes);
  }

  @Override
  public void setCharacterStream(int parameterIndex, Reader reader, int length)
  throws SQLException {
    System.err.println(" ********************* not implemented @ "
        + DebugPrinter.getFileName() + " line "
        + DebugPrinter.getLineNumber());
  }

  @Override
  public void setClob(int parameterIndex, Clob theClob) throws SQLException {
    System.err.println(" ********************* not implemented @ "
        + DebugPrinter.getFileName() + " line "
        + DebugPrinter.getLineNumber());

  }

  @Override
  public void setDate(int parameterIndex, Date theDate) throws SQLException {
    System.err.println(" ********************* not implemented @ "
        + DebugPrinter.getFileName() + " line "
        + DebugPrinter.getLineNumber());
  }

  @Override
  public void setDate(int parameterIndex, Date theDate, Calendar cal)  throws SQLException {
    System.err.println(" ********************* not implemented @ "
        + DebugPrinter.getFileName() + " line "
        + DebugPrinter.getLineNumber());
  }

  @Override
  public void setDouble(int parameterIndex, double theDouble) throws SQLException {

    ensureCap(parameterIndex);
    setObj(parameterIndex, new Double(theDouble));
  }

  @Override
  public void setFloat(int parameterIndex, float theFloat) throws SQLException {
    ensureCap(parameterIndex);
    setObj(parameterIndex, new Double(theFloat));
  }

  @Override
  public void setInt(int parameterIndex, int theInt) throws SQLException {
    ensureCap(parameterIndex);
    setObj(parameterIndex, new Long(theInt));
  }

  @Override
  public void setLong(int parameterIndex, long theLong) throws SQLException {
    ensureCap(parameterIndex);
    setObj(parameterIndex, new Long(theLong));
  }

  @Override
  public void setNull(int parameterIndex, int sqlType) throws SQLException {
    ensureCap(parameterIndex);
    setObj(parameterIndex, null);
  }

  @Override
  public void setNull(int paramIndex, int sqlType, String typeName) throws SQLException {
    ensureCap(paramIndex);
    setObj(paramIndex, null);
  }

  @Override
  public void setObject(int parameterIndex, Object theObject)  throws SQLException {
    ensureCap(parameterIndex);
    setObj(parameterIndex, theObject);
  }

  @Override
  public void setObject(int parameterIndex, Object theObject,
      int targetSqlType) throws SQLException {
    System.err.println(" ********************* not implemented @ "
        + DebugPrinter.getFileName() + " line "
        + DebugPrinter.getLineNumber());
  }

  @Override
  public void setObject(int parameterIndex, Object theObject,
      int targetSqlType, int scale) throws SQLException {
    System.err.println(" ********************* not implemented @ "
        + DebugPrinter.getFileName() + " line "
        + DebugPrinter.getLineNumber());
  }

  @Override
  public void setRef(int parameterIndex, Ref theRef) throws SQLException {
    System.err.println(" ********************* not implemented @ "
        + DebugPrinter.getFileName() + " line "
        + DebugPrinter.getLineNumber());
  }

  @Override
  public void setShort(int parameterIndex, short theShort)
  throws SQLException {
    ensureCap(parameterIndex);
    setObj(parameterIndex, new Long(theShort));
  }

  @Override
  public void setString(int parameterIndex, String theString) {
    ensureCap(parameterIndex);
    setObj(parameterIndex, theString);
  }

  @Override
  public void setTime(int parameterIndex, Time theTime) throws SQLException {
    System.err.println(" ********************* not implemented @ "
        + DebugPrinter.getFileName() + " line "
        + DebugPrinter.getLineNumber());
  }

  @Override
  public void setTime(int parameterIndex, Time theTime, Calendar cal)
  throws SQLException {
    System.err.println(" ********************* not implemented @ "
        + DebugPrinter.getFileName() + " line "
        + DebugPrinter.getLineNumber());
  }

  @Override
  public void setTimestamp(int parameterIndex, Timestamp theTimestamp)
  throws SQLException {
    ensureCap(parameterIndex);
    setObj(parameterIndex, theTimestamp);
  }

  @Override
  public void setTimestamp(int parameterIndex, Timestamp theTimestamp, Calendar cal)
  throws SQLException {
    System.err.println(" ********************* not implemented @ "
        + DebugPrinter.getFileName() + " line "
        + DebugPrinter.getLineNumber());
  }

  @Override
  public void setURL(int parameterIndex, URL theURL) throws SQLException {
    System.err.println(" ********************* not implemented @ "
        + DebugPrinter.getFileName() + " line "
        + DebugPrinter.getLineNumber());
  }

  @Override
  public void setUnicodeStream(int parameterIndex,
      InputStream theInputStream, int length) throws SQLException {
    System.err.println(" ********************* not implemented @ "
        + DebugPrinter.getFileName() + " line "
        + DebugPrinter.getLineNumber());
  }


  @Override
  public boolean isClosed() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }


  @Override
  public boolean isPoolable() throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }


  @Override
  public void setPoolable(boolean poolable) throws SQLException {
    // TODO Auto-generated method stub

  }


  @Override
  public boolean isWrapperFor(Class<?> arg0) throws SQLException {
    // TODO Auto-generated method stub
    return false;
  }


  @Override
  public <T> T unwrap(Class<T> arg0) throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }


  @Override
  public void setAsciiStream(int parameterIndex, InputStream inputStream)
  throws SQLException {
    // TODO Auto-generated method stub

  }


  @Override
  public void setAsciiStream(int parameterIndex, InputStream inputStream,
      long length) throws SQLException {
    // TODO Auto-generated method stub

  }

  /** Read the byte stream and set the object as a byte[].  This is a pass through to
   *     <code>setBinaryStream(parameterIndex, inputStream, Integer.MAX_VALUE);</code>
   * @see #setBinaryStream(int, InputStream, int)
   */
  @Override
  public void setBinaryStream(int parameterIndex, InputStream inputStream) throws SQLException {
     setBinaryStream(parameterIndex, inputStream, Integer.MAX_VALUE);
  }


  /** This is a pass through to the integer version of the same method.  That is, the long is downcast to an
   * integer.  If the length is greater than Integer.MAX_VALUE this method will throw a SQLException.
   * @see #setBinaryStream(int, InputStream, int)
   * @exception SQLException thrown if length is greater than Integer.MAX_VALUE or if there is a database error.
   */
  @Override
  public void setBinaryStream(int parameterIndex, InputStream inputStream, long length) throws SQLException {
    if ( length > Integer.MAX_VALUE ) {
      throw new SQLException ("SQLDroid does not allow input stream data greater than " + Integer.MAX_VALUE );
    }
    setBinaryStream(parameterIndex, inputStream, (int)length);
  }


  @Override
  public void setBlob(int parameterIndex, InputStream inputStream)  throws SQLException {
    // TODO Auto-generated method stub
  }


  @Override
  public void setBlob(int parameterIndex, InputStream inputStream, long length)
  throws SQLException {
    // TODO Auto-generated method stub
  }


  @Override
  public void setCharacterStream(int parameterIndex, Reader reader)
  throws SQLException {
    // TODO Auto-generated method stub
  }


  @Override
  public void setCharacterStream(int parameterIndex, Reader reader,
      long length) throws SQLException {
    // TODO Auto-generated method stub
  }


  @Override
  public void setClob(int parameterIndex, Reader reader) throws SQLException {
    // TODO Auto-generated method stub
  }


  @Override
  public void setClob(int parameterIndex, Reader reader, long length)
  throws SQLException {
    // TODO Auto-generated method stub
  }


  @Override
  public void setNCharacterStream(int parameterIndex, Reader reader)
  throws SQLException {
    // TODO Auto-generated method stub
  }


  @Override
  public void setNCharacterStream(int parameterIndex, Reader reader,
      long length) throws SQLException {
    // TODO Auto-generated method stub

  }


  @Override
  public void setNClob(int parameterIndex, NClob value) throws SQLException {
    // TODO Auto-generated method stub

  }


  @Override
  public void setNClob(int parameterIndex, Reader reader) throws SQLException {
    // TODO Auto-generated method stub

  }


  @Override
  public void setNClob(int parameterIndex, Reader reader, long length)
  throws SQLException {
    // TODO Auto-generated method stub

  }


  @Override
  public void setNString(int parameterIndex, String theString)
  throws SQLException {
    // TODO Auto-generated method stub

  }


  @Override
  public void setRowId(int parameterIndex, RowId theRowId)
  throws SQLException {
    // TODO Auto-generated method stub

  }


  @Override
  public void setSQLXML(int parameterIndex, SQLXML xmlObject)
  throws SQLException {
    // TODO Auto-generated method stub

  }

  // methods added for JDK7 compilation

  public boolean isCloseOnCompletion() throws SQLException {
      //TODO auto generated code
      return false;
  }

  public void closeOnCompletion() throws SQLException {
      //TODO auto generated code
  }


}
