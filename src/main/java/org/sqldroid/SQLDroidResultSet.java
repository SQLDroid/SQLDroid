package org.sqldroid;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;

import android.database.Cursor;

public class SQLDroidResultSet implements ResultSet {
    public static boolean dump = false;

    private final Cursor c;
    private int lastColumnRead; // JDBC style column index starting from 1

    public SQLDroidResultSet(final Cursor c) throws SQLException {
        this.c = c;
        if (dump) {
            this.dumpResultSet();
        }
    }

    private void dumpResultSet() throws SQLException {
        final ResultSet rs = this;
        boolean headerDrawn = false;
        while (rs.next()) {
          if (!headerDrawn) {
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
              System.out.print(" | ");
              System.out.print(rs.getMetaData().getColumnLabel(i));
            }
            System.out.println(" | ");
            headerDrawn = true;
          }
          for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
            System.out.print(" | ");
            System.out.print(rs.getString(i));
            if (rs.getString(i) != null) {
                System.out.print(" (" + rs.getString(i).length() + ")");
            }
          }
          System.out.println(" | ");
        }
        rs.beforeFirst();
    }


  /**
   * convert JDBC column index (one-based) to sqlite column index (zero-based)
   * @param colID
   */
  private int ci(final int colID) {
    return colID - 1;
  }

  @Override
  public boolean absolute(final int row) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
    return false;
  }

  @Override
  public void afterLast() throws SQLException {
    try {
      this.c.moveToLast();
      this.c.moveToNext();
    } catch (final android.database.SQLException e) {
      throw SQLDroidConnection.chainException(e);
    }
  }

  @Override
  public void beforeFirst() throws SQLException {
    try {
      this.c.moveToFirst();
      this.c.moveToPrevious();
    } catch (final android.database.SQLException e) {
      throw SQLDroidConnection.chainException(e);
    }
  }

  @Override
  public void cancelRowUpdates() throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
  }

  @Override
  public void clearWarnings() throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void close() throws SQLException {
    try {
      if (this.c != null) {
        this.c.close();
      }
    } catch (final android.database.SQLException e) {
      throw SQLDroidConnection.chainException(e);
    }
  }

  @Override
  public void deleteRow() throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
  }

  @Override
  public int findColumn(final String columnName) throws SQLException {
    try {
      // JDBC style column index starts from 1; Android database cursor has zero-based index
      return (this.c.getColumnIndexOrThrow(columnName) + 1);
    } catch (final android.database.SQLException e) {
      throw SQLDroidConnection.chainException(e);
    }
  }

  @Override
  public boolean first() throws SQLException {
    try {
      return this.c.moveToFirst();
    } catch (final android.database.SQLException e) {
      throw SQLDroidConnection.chainException(e);
    }
  }

  @Override
  public Array getArray(final int colID) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
    return null;
  }


  @Override
  public Array getArray(final String colName) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
    return null;
  }

  @Override
  public InputStream getAsciiStream(final int colID) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
    return null;
  }

  @Override
  public InputStream getAsciiStream(final String columnName) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
    return null;
  }

  @Override
  public BigDecimal getBigDecimal(final int colID) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
    return null;
  }

  @Override
  public BigDecimal getBigDecimal(final String columnName) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
    return null;
  }

  @Override
  public BigDecimal getBigDecimal(final int colID, final int scale)
  throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
    return null;
  }

  @Override
  public BigDecimal getBigDecimal(final String columnName, final int scale)
  throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
    return null;
  }

  @Override
  public InputStream getBinaryStream(final int colID) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
    return null;
  }

  @Override
  public InputStream getBinaryStream(final String columnName) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
    return null;
  }

  @Override
  public Blob getBlob(final int index) throws SQLException {
    try {
      final byte [] b = this.getBytes(index);
      return new SQLDroidBlob(b);
    } catch (final android.database.SQLException e) {
      throw SQLDroidConnection.chainException(e);
    }
  }

  @Override
  public Blob getBlob(final String columnName) throws SQLException {
    final int index = this.findColumn(columnName);
    return this.getBlob(index);
  }

  @Override
  public boolean getBoolean(final int index) throws SQLException {
    try {
      this.lastColumnRead = index;
      return this.c.getInt(this.ci(index)) != 0;
    } catch (final android.database.SQLException e) {
      throw SQLDroidConnection.chainException(e);
    }
  }

  @Override
  public boolean getBoolean(final String columnName) throws SQLException {
    final int index = this.findColumn(columnName);
    return this.getBoolean(index);
  }

  @Override
  public byte getByte(final int index) throws SQLException {
    try {
      this.lastColumnRead = index;
      return (byte)this.c.getShort(this.ci(index));
    } catch (final android.database.SQLException e) {
      throw SQLDroidConnection.chainException(e);
    }
  }

  @Override
  public byte getByte(final String columnName) throws SQLException {
    final int index = this.findColumn(columnName);
    return this.getByte(index);
  }

    @Override
    public byte[] getBytes(final int index) throws SQLException {
        try {
            this.lastColumnRead = index;
            byte [] bytes = this.c.getBlob(this.ci(index));
            // SQLite includes the zero-byte at the end for Strings.
            if (SQLDroidResultSetMetaData.getType(this.c, this.ci(index)) == 3) { //  Cursor.FIELD_TYPE_STRING
		        bytes = Arrays.copyOf(bytes, bytes.length - 1);
            }
            return bytes;
        } catch (final android.database.SQLException e) {
            throw SQLDroidConnection.chainException(e);
        }
    }

  @Override
  public byte[] getBytes(final String columnName) throws SQLException {
    final int index = this.findColumn(columnName);
    return this.getBytes(index);
  }

  @Override
  public Reader getCharacterStream(final int colID) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
    return null;
  }

  @Override
  public Reader getCharacterStream(final String columnName) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
    return null;
  }

  @Override
  public Clob getClob(final int colID) throws SQLException {
    final String clobString = this.getString(colID);
    if(clobString == null){
    	return null;
    }
		return new SQLDroidClob(clobString);
  }

  @Override
  public Clob getClob(final String colName) throws SQLException {
  	final int index = this.findColumn(colName);
  	return this.getClob(index);
  }

  @Override
  public int getConcurrency() throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
    return 0;
  }

  @Override
  public String getCursorName() throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
    return null;
  }

  @Override
  public Date getDate(final int colID) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
    return null;
  }

  @Override
  public Date getDate(final String columnName) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
    return null;
  }

  @Override
  public Date getDate(final int colID, final Calendar cal) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
    return null;
  }

  @Override
  public Date getDate(final String columnName, final Calendar cal) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
    return null;
  }

  @Override
  public double getDouble(final int index) throws SQLException {
    try {
      this.lastColumnRead = index;
      return this.c.getDouble(this.ci(index));
    } catch (final android.database.SQLException e) {
      return 0;
    }
  }

  @Override
  public double getDouble(final String columnName) throws SQLException {
    final int index = this.findColumn(columnName);
    return this.getDouble(index);
  }

  @Override
  public int getFetchDirection() throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
    return 0;
  }

  @Override
  public int getFetchSize() throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
    return 0;
  }

  @Override
  public float getFloat(final int index) throws SQLException {
    try {
      this.lastColumnRead = index;
      return this.c.getFloat(this.ci(index));
    } catch (final android.database.SQLException e) {
      throw SQLDroidConnection.chainException(e);
    }
  }

  @Override
  public float getFloat(final String columnName) throws SQLException {
    final int index = this.findColumn(columnName);
    return this.getFloat(index);
  }

  @Override
  public int getInt(final int index) throws SQLException {
    try {
      this.lastColumnRead = index;
      return this.c.getInt(this.ci(index));
    } catch (final android.database.SQLException e) {
      throw SQLDroidConnection.chainException(e);
    }
  }

  @Override
  public int getInt(final String columnName) throws SQLException {
    final int index = this.findColumn(columnName);
    return this.getInt(index);
  }

  @Override
  public long getLong(final int index) throws SQLException {
    try {
      this.lastColumnRead = index;
      return this.c.getLong(this.ci(index));
    } catch (final android.database.SQLException e) {
      throw SQLDroidConnection.chainException(e);
    }
  }

  @Override
  public long getLong(final String columnName) throws SQLException {
    final int index = this.findColumn(columnName);
    return this.getLong(index);
  }

  @Override
  public ResultSetMetaData getMetaData() throws SQLException {
    return new SQLDroidResultSetMetaData(this.c);
  }

    @Override
    public Object getObject(final int colID) throws SQLException {
        this.lastColumnRead = colID;
        final int newIndex = this.ci(colID);
        switch(SQLDroidResultSetMetaData.getType(this.c, newIndex)) {
            case 4: // Cursor.FIELD_TYPE_BLOB:
                //CONVERT TO BYTE[] OBJECT
                return new SQLDroidBlob(this.c.getBlob(newIndex));
            case 2: // Cursor.FIELD_TYPE_FLOAT:
                return new Float(this.c.getFloat(newIndex));
            case 1: // Cursor.FIELD_TYPE_INTEGER:
                return new Integer(this.c.getInt(newIndex));
            case 3: // Cursor.FIELD_TYPE_STRING:
                return this.c.getString(newIndex);
            case 0: // Cursor.FIELD_TYPE_NULL:
                return null;
            default:
                return this.c.getString(newIndex);
        }
    }

  @Override
  public Object getObject(final String columnName) throws SQLException {
    final int index = this.findColumn(columnName);
    return this.getObject(index);
  }

  @Override
  public Object getObject(final int arg0, final Map<String, Class<?>> arg1)
  throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
    return null;
  }

  @Override
  public Object getObject(final String arg0, final Map<String, Class<?>> arg1)
  throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
    return null;
  }

  public <T> T getObject(final int columnIndex, final Class<T> clazz) throws SQLException {
    // This method is entitled to throw if the conversion is not supported, so,
    // since we don't support any conversions we'll throw.
    // The only problem with this is that we're required to support certain conversion as specified in the docs.

    throw new SQLException("Conversion not supported.  No conversions are supported.  This method will always throw.");
  }

  public <T> T getObject(final String columnLabel, final Class<T> clazz) throws SQLException {
    // This method is entitled to throw if the conversion is not supported, so,
    // since we don't support any conversions we'll throw.
    // The only problem with this is that we're required to support certain conversion as specified in the docs.
    throw new SQLException("Conversion not supported.  No conversions are supported.  This method will always throw.");
  }

  @Override
  public Ref getRef(final int colID) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
    return null;
  }

  @Override
  public Ref getRef(final String colName) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
    return null;
  }

  @Override
  public int getRow() throws SQLException {
    try {
      // convert to jdbc standard (counting from one)
      return this.c.getPosition() + 1;
    } catch (final android.database.SQLException e) {
      throw SQLDroidConnection.chainException(e);
    }
  }

  @Override
  public short getShort(final int index) throws SQLException {
    try {
      this.lastColumnRead = index;
      return this.c.getShort(this.ci(index));
    } catch (final android.database.SQLException e) {
      throw SQLDroidConnection.chainException(e);
    }
  }

  @Override
  public short getShort(final String columnName) throws SQLException {
    final int index = this.findColumn(columnName);
    return this.getShort(index);
  }

  @Override
  public Statement getStatement() throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
    return null;
  }

  @Override
  public String getString(final int index) throws SQLException {
    try {
      this.lastColumnRead = index;
      return this.c.getString(this.ci(index));
    } catch (final android.database.SQLException e) {
      throw SQLDroidConnection.chainException(e);
    }
  }

  @Override
  public String getString(final String columnName) throws SQLException {
    final int index = this.findColumn(columnName);
    return this.getString(index);
  }

  @Override
  public Time getTime(final int colID) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
    return null;
  }

  @Override
  public Time getTime(final String columnName) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
    return null;
  }

  @Override
  public Time getTime(final int colID, final Calendar cal) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
    return null;
  }

  @Override
  public Time getTime(final String columnName, final Calendar cal) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
    return null;
  }

  @Override
  public Timestamp getTimestamp(final int index) throws SQLException {
    try {
      final ResultSetMetaData md = this.getMetaData();
      Timestamp timestamp = null;
      switch ( md.getColumnType(index)) {
        case Types.INTEGER:
        case Types.BIGINT:
          timestamp = new Timestamp(this.getLong(index));
          break;
        case Types.DATE:
          timestamp = new Timestamp(this.getDate(index).getTime());
          break;
        default:
          // format 2011-07-11 11:36:30.0
          try {
            final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
            final java.util.Date parsedDate = dateFormat.parse(this.getString(index));
            timestamp = new Timestamp(parsedDate.getTime());
          } catch ( final Exception any ) {
            any.printStackTrace();
          }
          break;

      }
      return timestamp;
    } catch (final android.database.SQLException e) {
      throw SQLDroidConnection.chainException(e);
    }
  }

  @Override
  public Timestamp getTimestamp(final String columnName) throws SQLException {
    final int index = this.findColumn(columnName);
    return this.getTimestamp(index);
  }

  @Override
  public Timestamp getTimestamp(final int colID, final Calendar cal)
  throws SQLException {
    System.err.println(" ********************* not implemented correctly - Calendar is ignored. @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
    return this.getTimestamp(colID);
  }

  @Override
  public Timestamp getTimestamp(final String columnName, final Calendar cal)
  throws SQLException {
    System.err.println(" ********************* not implemented correctly - Calendar is ignored. @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
    return this.getTimestamp(columnName);
  }

  @Override
  public int getType() throws SQLException {
    return ResultSet.TYPE_SCROLL_SENSITIVE;
  }

  @Override
  public URL getURL(final int colID) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
    return null;
  }

  @Override
  public URL getURL(final String columnName) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
    return null;
  }

  @Override
  public InputStream getUnicodeStream(final int colID) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
    return null;
  }

  @Override
  public InputStream getUnicodeStream(final String columnName) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
    return null;
  }

  @Override
  public SQLWarning getWarnings() throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
    return null;
  }

  @Override
  public void insertRow() throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public boolean isAfterLast() throws SQLException {
    if ( this.isClosed() ) {
      return false;
    }
    try {
      return this.c.isAfterLast();
    } catch (final android.database.SQLException e) {
      throw SQLDroidConnection.chainException(e);
    }
  }

  @Override
  public boolean isBeforeFirst() throws SQLException {
    if ( this.isClosed() ) {
      return false;
    }
    try {
      return this.c.isBeforeFirst();
    } catch (final android.database.SQLException e) {
      throw SQLDroidConnection.chainException(e);
    }
  }

  @Override
  public boolean isFirst() throws SQLException {
    if ( this.isClosed() ) {
      return false;
    }
    try {
      return this.c.isFirst();
    } catch (final android.database.SQLException e) {
      throw SQLDroidConnection.chainException(e);
    }
  }

  @Override
  public boolean isLast() throws SQLException {
    if ( this.isClosed() ) {
      return false;
    }
    try {
      return this.c.isLast();
    } catch (final android.database.SQLException e) {
      throw SQLDroidConnection.chainException(e);
    }
  }

  @Override
  public boolean last() throws SQLException {
    try {
      return this.c.moveToLast();
    } catch (final android.database.SQLException e) {
      throw SQLDroidConnection.chainException(e);
    }
  }

  @Override
  public void moveToCurrentRow() throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void moveToInsertRow() throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public boolean next() throws SQLException {
    try {
      return this.c.moveToNext();
    } catch (final android.database.SQLException e) {
      throw SQLDroidConnection.chainException(e);
    }
  }

  @Override
  public boolean previous() throws SQLException {
    try {
      return this.c.moveToPrevious();
    } catch (final android.database.SQLException e) {
      throw SQLDroidConnection.chainException(e);
    }
  }

  @Override
  public void refreshRow() throws SQLException {
    try {
      this.c.requery();
    } catch (final android.database.SQLException e) {
      throw SQLDroidConnection.chainException(e);
    }
  }

  @Override
  public boolean relative(final int rows) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
    return false;
  }

  @Override
  public boolean rowDeleted() throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
    return false;
  }

  @Override
  public boolean rowInserted() throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
    return false;
  }

  @Override
  public boolean rowUpdated() throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
    return false;
  }

  @Override
  public void setFetchDirection(final int direction) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void setFetchSize(final int rows) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void updateArray(final int colID, final Array x) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void updateArray(final String columnName, final Array x) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void updateAsciiStream(final int colID, final InputStream x, final int length)
  throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void updateAsciiStream(final String columnName, final InputStream x, final int length)
  throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void updateBigDecimal(final int colID, final BigDecimal x)
  throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void updateBigDecimal(final String columnName, final BigDecimal x)
  throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void updateBinaryStream(final int colID, final InputStream x, final int length)
  throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void updateBinaryStream(final String columnName, final InputStream x, final int length)
  throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void updateBlob(final int colID, final Blob x) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void updateBlob(final String columnName, final Blob x) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void updateBoolean(final int colID, final boolean x) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void updateBoolean(final String columnName, final boolean x) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void updateByte(final int colID, final byte x) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void updateByte(final String columnName, final byte x) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void updateBytes(final int colID, final byte[] x) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void updateBytes(final String columnName, final byte[] x) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void updateCharacterStream(final int colID, final Reader x, final int length)
  throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void updateCharacterStream(final String columnName, final Reader reader,
      final int length) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void updateClob(final int colID, final Clob x) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void updateClob(final String columnName, final Clob x) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void updateDate(final int colID, final Date x) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void updateDate(final String columnName, final Date x) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void updateDouble(final int colID, final double x) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void updateDouble(final String columnName, final double x) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void updateFloat(final int colID, final float x) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void updateFloat(final String columnName, final float x) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void updateInt(final int colID, final int x) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void updateInt(final String columnName, final int x) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void updateLong(final int colID, final long x) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void updateLong(final String columnName, final long x) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void updateNull(final int colID) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void updateNull(final String columnName) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void updateObject(final int colID, final Object x) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void updateObject(final String columnName, final Object x) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void updateObject(final int colID, final Object x, final int scale)
  throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void updateObject(final String columnName, final Object x, final int scale)
  throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void updateRef(final int colID, final Ref x) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void updateRef(final String columnName, final Ref x) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void updateRow() throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void updateShort(final int colID, final short x) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void updateShort(final String columnName, final short x) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void updateString(final int colID, final String x) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void updateString(final String columnName, final String x) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void updateTime(final int colID, final Time x) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void updateTime(final String columnName, final Time x) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void updateTimestamp(final int colID, final Timestamp x)
  throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void updateTimestamp(final String columnName, final Timestamp x)
  throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
  }

  @Override
  public boolean wasNull() throws SQLException {
    try {
      return this.c.isNull(this.ci(this.lastColumnRead));
    } catch (final android.database.SQLException e) {
      throw SQLDroidConnection.chainException(e);
    }
  }

  @Override
  public boolean isWrapperFor(final Class<?> arg0) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
    return false;
  }

  @Override
  public <T> T unwrap(final Class<T> arg0) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
    return null;
  }

  @Override
  public int getHoldability() throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
    return 0;
  }

  @Override
  public Reader getNCharacterStream(final int columnIndex) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
    return null;
  }

  @Override
  public Reader getNCharacterStream(final String columnLabel) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
    return null;
  }

  @Override
  public NClob getNClob(final int columnIndex) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
    return null;
  }

  @Override
  public NClob getNClob(final String columnLabel) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
    return null;
  }

  @Override
  public String getNString(final int columnIndex) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
    return null;
  }

  @Override
  public String getNString(final String columnLabel) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
    return null;
  }

  @Override
  public RowId getRowId(final int columnIndex) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
    return null;
  }

  @Override
  public RowId getRowId(final String columnLabel) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
    return null;
  }

  @Override
  public SQLXML getSQLXML(final int columnIndex) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
    return null;
  }

  @Override
  public SQLXML getSQLXML(final String columnLabel) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
    return null;
  }

  @Override
  public boolean isClosed() throws SQLException {
    return this.c.isClosed();
  }

  @Override
  public void updateAsciiStream(final int columnIndex, final InputStream x)
  throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void updateAsciiStream(final String columnLabel, final InputStream x)
  throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void updateAsciiStream(final int columnIndex, final InputStream x, final long length)
  throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void updateAsciiStream(final String columnLabel, final InputStream x, final long length)
  throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void updateBinaryStream(final int columnIndex, final InputStream x)
  throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void updateBinaryStream(final String columnLabel, final InputStream x)
  throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void updateBinaryStream(final int columnIndex, final InputStream x, final long length)
  throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void updateBinaryStream(final String columnLabel, final InputStream x,
      final long length) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void updateBlob(final int columnIndex, final InputStream inputStream)
  throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void updateBlob(final String columnLabel, final InputStream inputStream)
  throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void updateBlob(final int columnIndex, final InputStream inputStream, final long length)
  throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void updateBlob(final String columnLabel, final InputStream inputStream,
      final long length) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void updateCharacterStream(final int columnIndex, final Reader x)
  throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void updateCharacterStream(final String columnLabel, final Reader reader)
  throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void updateCharacterStream(final int columnIndex, final Reader x, final long length)
  throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void updateCharacterStream(final String columnLabel, final Reader reader,
      final long length) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void updateClob(final int columnIndex, final Reader reader) throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void updateClob(final String columnLabel, final Reader reader)
  throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void updateClob(final int columnIndex, final Reader reader, final long length)
  throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void updateClob(final String columnLabel, final Reader reader, final long length)
  throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void updateNCharacterStream(final int columnIndex, final Reader x)
  throws SQLException {
    System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

  }

  @Override
  public void updateNCharacterStream(final String columnLabel, final Reader reader)
  throws SQLException {
    // TODO Auto-generated method stub

  }

  @Override
  public void updateNCharacterStream(final int columnIndex, final Reader x, final long length)
  throws SQLException {
    // TODO Auto-generated method stub

  }

  @Override
  public void updateNCharacterStream(final String columnLabel, final Reader reader,
      final long length) throws SQLException {
    // TODO Auto-generated method stub

  }

  @Override
  public void updateNClob(final int columnIndex, final NClob nClob) throws SQLException {
    // TODO Auto-generated method stub

  }

  @Override
  public void updateNClob(final String columnLabel, final NClob nClob)
  throws SQLException {
    // TODO Auto-generated method stub

  }

  @Override
  public void updateNClob(final int columnIndex, final Reader reader) throws SQLException {
    // TODO Auto-generated method stub

  }

  @Override
  public void updateNClob(final String columnLabel, final Reader reader)
  throws SQLException {
    // TODO Auto-generated method stub

  }

  @Override
  public void updateNClob(final int columnIndex, final Reader reader, final long length)
  throws SQLException {
    // TODO Auto-generated method stub

  }

  @Override
  public void updateNClob(final String columnLabel, final Reader reader, final long length)
  throws SQLException {
    // TODO Auto-generated method stub

  }

  @Override
  public void updateNString(final int columnIndex, final String nString)
  throws SQLException {
    // TODO Auto-generated method stub

  }

  @Override
  public void updateNString(final String columnLabel, final String nString)
  throws SQLException {
    // TODO Auto-generated method stub

  }

  @Override
  public void updateRowId(final int columnIndex, final RowId value) throws SQLException {
    // TODO Auto-generated method stub

  }

  @Override
  public void updateRowId(final String columnLabel, final RowId value)
  throws SQLException {
    // TODO Auto-generated method stub

  }

  @Override
  public void updateSQLXML(final int columnIndex, final SQLXML xmlObject)
  throws SQLException {
    // TODO Auto-generated method stub

  }

  @Override
  public void updateSQLXML(final String columnLabel, final SQLXML xmlObject)
  throws SQLException {
    // TODO Auto-generated method stub

  }

}
