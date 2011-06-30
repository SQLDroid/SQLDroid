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
import java.util.Calendar;
import java.util.Map;

import android.database.Cursor;

public class SqldroidResultSet implements ResultSet {
	private Cursor c;
	private int lastColumnRead;
	
	public SqldroidResultSet(Cursor c) {
		this.c = c;
	}
	
	/**
	 * convert JDBC columns index count (from one) to sqlite (from zero)
	 * @param colID
	 */
	private int ci(int colID) {
		return colID - 1;
	}
	
	@Override
	public boolean absolute(int row) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public void afterLast() throws SQLException {
		c.moveToLast();
		c.moveToNext();

	}

	@Override
	public void beforeFirst() throws SQLException {
		c.moveToFirst();
		c.moveToPrevious();

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
		if (c != null) {
			c.close();
			c = null;
		}
	}

	@Override
	public void deleteRow() throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());


	}

	@Override
	public int findColumn(String columnName) throws SQLException {
		return c.getColumnIndex(columnName);
	}

	@Override
	public boolean first() throws SQLException {
		return c.moveToFirst();
	}

	@Override
	public Array getArray(int colID) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public Array getArray(String colName) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public InputStream getAsciiStream(int colID) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public InputStream getAsciiStream(String columnName) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public BigDecimal getBigDecimal(int colID) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public BigDecimal getBigDecimal(String columnName) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public BigDecimal getBigDecimal(int colID, int scale)
			throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public BigDecimal getBigDecimal(String columnName, int scale)
			throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public InputStream getBinaryStream(int colID) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public InputStream getBinaryStream(String columnName) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public Blob getBlob(int index) throws SQLException {
		lastColumnRead = index;
		byte [] b = c.getBlob(ci(index));
		return new SqldroidBlob(b);
	}

	@Override
	public Blob getBlob(String columnName) throws SQLException {
		int index = c.getColumnIndex(columnName);
		lastColumnRead = index;
		return new SqldroidBlob(c.getBlob(index));
	}

	@Override
	public boolean getBoolean(int index) throws SQLException {
		lastColumnRead = index;
		return c.getInt(ci(index)) != 0;
	}

	@Override
	public boolean getBoolean(String columnName) throws SQLException {
		int index = c.getColumnIndex(columnName);
		lastColumnRead = index;
		return c.getInt(index) != 0;
	}

	@Override
	public byte getByte(int index) throws SQLException {
		lastColumnRead = index;
		return (byte)c.getShort(ci(index));
	}

	@Override
	public byte getByte(String columnName) throws SQLException {
		int index = c.getColumnIndex(columnName);
		lastColumnRead = index;
		return (byte)c.getShort(index);
	}

	@Override
	public byte[] getBytes(int index) throws SQLException {
		lastColumnRead = index;
		return c.getBlob(ci(index));
	}

	@Override
	public byte[] getBytes(String columnName) throws SQLException {
		int index = c.getColumnIndex(columnName);
		lastColumnRead = index;
		return c.getBlob(index);
	}

	@Override
	public Reader getCharacterStream(int colID) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public Reader getCharacterStream(String columnName) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public Clob getClob(int colID) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public Clob getClob(String colName) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		return null;
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
	public Date getDate(int colID) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public Date getDate(String columnName) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public Date getDate(int colID, Calendar cal) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public Date getDate(String columnName, Calendar cal) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public double getDouble(int index) throws SQLException {
		lastColumnRead = index;
		return c.getDouble(ci(index));
	}

	@Override
	public double getDouble(String columnName) throws SQLException {
		int index = c.getColumnIndex(columnName);
		lastColumnRead = index;
		return c.getDouble(index);
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
	public float getFloat(int index) throws SQLException {
		lastColumnRead = index;
		return c.getFloat(ci(index));
	}

	@Override
	public float getFloat(String columnName) throws SQLException {
		int index = c.getColumnIndex(columnName);
		lastColumnRead = index;
		return c.getFloat(index);
	}

	@Override
	public int getInt(int index) throws SQLException {
		lastColumnRead = index;
		return c.getInt(ci(index));
	}

	@Override
	public int getInt(String columnName) throws SQLException {
		int index = c.getColumnIndex(columnName);
		lastColumnRead = index;
		return c.getInt(index);
	}

	@Override
	public long getLong(int index) throws SQLException {
		lastColumnRead = index;
		return c.getLong(ci(index));
	}

	@Override
	public long getLong(String columnName) throws SQLException {
		int index = c.getColumnIndex(columnName);
		lastColumnRead = index;
		return c.getLong(index);
	}

	@Override
	public ResultSetMetaData getMetaData() throws SQLException {
		return new SqldroidResultSetMetaData(c);
	}

	@Override
	public Object getObject(int colID) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public Object getObject(String columnName) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public Object getObject(int arg0, Map<String, Class<?>> arg1)
			throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public Object getObject(String arg0, Map<String, Class<?>> arg1)
			throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public Ref getRef(int colID) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public Ref getRef(String colName) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public int getRow() throws SQLException {
		// convert to jdbc standard (counting from one)
		return c.getPosition() + 1;
	}

	@Override
	public short getShort(int index) throws SQLException {
		lastColumnRead = index;
		return c.getShort(ci(index));
	}

	@Override
	public short getShort(String columnName) throws SQLException {
		int index = c.getColumnIndex(columnName);
		lastColumnRead = index;
		return c.getShort(index);
	}

	@Override
	public Statement getStatement() throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public String getString(int index) throws SQLException {
		lastColumnRead = index;
		return c.getString(ci(index));
	}

	@Override
	public String getString(String columnName) throws SQLException {
		int index = c.getColumnIndex(columnName);
		lastColumnRead = index;
		return c.getString(index);
	}

	@Override
	public Time getTime(int colID) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public Time getTime(String columnName) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public Time getTime(int colID, Calendar cal) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public Time getTime(String columnName, Calendar cal) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public Timestamp getTimestamp(int colID) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public Timestamp getTimestamp(String columnName) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public Timestamp getTimestamp(int colID, Calendar cal)
			throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public Timestamp getTimestamp(String columnName, Calendar cal)
			throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public int getType() throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		return 0;
	}

	@Override
	public URL getURL(int colID) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public URL getURL(String columnName) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public InputStream getUnicodeStream(int colID) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public InputStream getUnicodeStream(String columnName) throws SQLException {
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
		return c.isAfterLast();
	}

	@Override
	public boolean isBeforeFirst() throws SQLException {
		return c.isBeforeFirst();
	}

	@Override
	public boolean isFirst() throws SQLException {
		return c.isFirst();
	}

	@Override
	public boolean isLast() throws SQLException {
		return c.isLast();
	}

	@Override
	public boolean last() throws SQLException {
		return c.moveToLast();
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
		return c.moveToNext();
	}

	@Override
	public boolean previous() throws SQLException {
		return c.moveToPrevious();
	}

	@Override
	public void refreshRow() throws SQLException {
		c.requery();
	}

	@Override
	public boolean relative(int rows) throws SQLException {
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
	public void setFetchDirection(int direction) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

	}

	@Override
	public void setFetchSize(int rows) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

	}

	@Override
	public void updateArray(int colID, Array x) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

	}

	@Override
	public void updateArray(String columnName, Array x) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

	}

	@Override
	public void updateAsciiStream(int colID, InputStream x, int length)
			throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

	}

	@Override
	public void updateAsciiStream(String columnName, InputStream x, int length)
			throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

	}

	@Override
	public void updateBigDecimal(int colID, BigDecimal x)
			throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

	}

	@Override
	public void updateBigDecimal(String columnName, BigDecimal x)
			throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

	}

	@Override
	public void updateBinaryStream(int colID, InputStream x, int length)
			throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

	}

	@Override
	public void updateBinaryStream(String columnName, InputStream x, int length)
			throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

	}

	@Override
	public void updateBlob(int colID, Blob x) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

	}

	@Override
	public void updateBlob(String columnName, Blob x) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

	}

	@Override
	public void updateBoolean(int colID, boolean x) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

	}

	@Override
	public void updateBoolean(String columnName, boolean x) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

	}

	@Override
	public void updateByte(int colID, byte x) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

	}

	@Override
	public void updateByte(String columnName, byte x) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

	}

	@Override
	public void updateBytes(int colID, byte[] x) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

	}

	@Override
	public void updateBytes(String columnName, byte[] x) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

	}

	@Override
	public void updateCharacterStream(int colID, Reader x, int length)
			throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

	}

	@Override
	public void updateCharacterStream(String columnName, Reader reader,
			int length) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

	}

	@Override
	public void updateClob(int colID, Clob x) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

	}

	@Override
	public void updateClob(String columnName, Clob x) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

	}

	@Override
	public void updateDate(int colID, Date x) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

	}

	@Override
	public void updateDate(String columnName, Date x) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

	}

	@Override
	public void updateDouble(int colID, double x) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

	}

	@Override
	public void updateDouble(String columnName, double x) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

	}

	@Override
	public void updateFloat(int colID, float x) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

	}

	@Override
	public void updateFloat(String columnName, float x) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

	}

	@Override
	public void updateInt(int colID, int x) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

	}

	@Override
	public void updateInt(String columnName, int x) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

	}

	@Override
	public void updateLong(int colID, long x) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

	}

	@Override
	public void updateLong(String columnName, long x) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

	}

	@Override
	public void updateNull(int colID) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

	}

	@Override
	public void updateNull(String columnName) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

	}

	@Override
	public void updateObject(int colID, Object x) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

	}

	@Override
	public void updateObject(String columnName, Object x) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

	}

	@Override
	public void updateObject(int colID, Object x, int scale)
			throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

	}

	@Override
	public void updateObject(String columnName, Object x, int scale)
			throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

	}

	@Override
	public void updateRef(int colID, Ref x) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

	}

	@Override
	public void updateRef(String columnName, Ref x) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

	}

	@Override
	public void updateRow() throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

	}

	@Override
	public void updateShort(int colID, short x) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

	}

	@Override
	public void updateShort(String columnName, short x) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

	}

	@Override
	public void updateString(int colID, String x) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

	}

	@Override
	public void updateString(String columnName, String x) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

	}

	@Override
	public void updateTime(int colID, Time x) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

	}

	@Override
	public void updateTime(String columnName, Time x) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

	}

	@Override
	public void updateTimestamp(int colID, Timestamp x)
			throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());

	}

	@Override
	public void updateTimestamp(String columnName, Timestamp x)
			throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
	}

	@Override
	public boolean wasNull() throws SQLException {
		return c.isNull(ci(lastColumnRead));
	}

	@Override
	public boolean isWrapperFor(Class<?> arg0) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public <T> T unwrap(Class<T> arg0) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public int getHoldability() throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		return 0;
	}

	@Override
	public Reader getNCharacterStream(int columnIndex) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public Reader getNCharacterStream(String columnLabel) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public NClob getNClob(int columnIndex) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public NClob getNClob(String columnLabel) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public String getNString(int columnIndex) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public String getNString(String columnLabel) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public RowId getRowId(int columnIndex) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public RowId getRowId(String columnLabel) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public SQLXML getSQLXML(int columnIndex) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public SQLXML getSQLXML(String columnLabel) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		return null;
	}

	@Override
	public boolean isClosed() throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		return false;
	}

	@Override
	public void updateAsciiStream(int columnIndex, InputStream x)
			throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		
	}

	@Override
	public void updateAsciiStream(String columnLabel, InputStream x)
			throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		
	}

	@Override
	public void updateAsciiStream(int columnIndex, InputStream x, long length)
			throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		
	}

	@Override
	public void updateAsciiStream(String columnLabel, InputStream x, long length)
			throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		
	}

	@Override
	public void updateBinaryStream(int columnIndex, InputStream x)
			throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		
	}

	@Override
	public void updateBinaryStream(String columnLabel, InputStream x)
			throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		
	}

	@Override
	public void updateBinaryStream(int columnIndex, InputStream x, long length)
			throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		
	}

	@Override
	public void updateBinaryStream(String columnLabel, InputStream x,
			long length) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		
	}

	@Override
	public void updateBlob(int columnIndex, InputStream inputStream)
			throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		
	}

	@Override
	public void updateBlob(String columnLabel, InputStream inputStream)
			throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		
	}

	@Override
	public void updateBlob(int columnIndex, InputStream inputStream, long length)
			throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		
	}

	@Override
	public void updateBlob(String columnLabel, InputStream inputStream,
			long length) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		
	}

	@Override
	public void updateCharacterStream(int columnIndex, Reader x)
			throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		
	}

	@Override
	public void updateCharacterStream(String columnLabel, Reader reader)
			throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		
	}

	@Override
	public void updateCharacterStream(int columnIndex, Reader x, long length)
			throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		
	}

	@Override
	public void updateCharacterStream(String columnLabel, Reader reader,
			long length) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		
	}

	@Override
	public void updateClob(int columnIndex, Reader reader) throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		
	}

	@Override
	public void updateClob(String columnLabel, Reader reader)
			throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		
	}

	@Override
	public void updateClob(int columnIndex, Reader reader, long length)
			throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		
	}

	@Override
	public void updateClob(String columnLabel, Reader reader, long length)
			throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		
	}

	@Override
	public void updateNCharacterStream(int columnIndex, Reader x)
			throws SQLException {
		System.err.println(" ********************* not implemented @ " + DebugPrinter.getFileName() + " line " + DebugPrinter.getLineNumber());
		
	}

	@Override
	public void updateNCharacterStream(String columnLabel, Reader reader)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateNCharacterStream(int columnIndex, Reader x, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateNCharacterStream(String columnLabel, Reader reader,
			long length) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateNClob(int columnIndex, NClob nClob) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateNClob(String columnLabel, NClob nClob)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateNClob(int columnIndex, Reader reader) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateNClob(String columnLabel, Reader reader)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateNClob(int columnIndex, Reader reader, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateNClob(String columnLabel, Reader reader, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateNString(int columnIndex, String nString)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateNString(String columnLabel, String nString)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateRowId(int columnIndex, RowId value) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateRowId(String columnLabel, RowId value)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateSQLXML(int columnIndex, SQLXML xmlObject)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateSQLXML(String columnLabel, SQLXML xmlObject)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

}
