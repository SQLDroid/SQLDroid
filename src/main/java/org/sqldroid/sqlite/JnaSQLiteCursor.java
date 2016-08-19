package org.sqldroid.sqlite;

import org.sqldroid.SQLiteCursor;

import com.sun.jna.Pointer;

import android.database.SQLException;

class JnaSQLiteCursor implements SQLiteCursor {

    protected Pointer pStmt;

    /** The row count. */
    protected int rowCount;

    /** The current position in the cursor. */
    protected int currentPosition;

    public JnaSQLiteCursor(Pointer pStmt) {
      this.pStmt = pStmt;
      rowCount = -1;
      rowCount = getCount();
      currentPosition = -1;
    }

    @Override
    public int getCount() {
      if ( rowCount < 0 ) {
        rowCount = 0;
        while ( SQLite.sqlite3_step(pStmt) == SQLite.SQLITE_ROW ) {
          //System.out.println ("getCount " + getString(1));
          rowCount++;
        }
        SQLite.sqlite3_reset(pStmt);
      }
      return rowCount;
    }

    @Override
    public boolean moveToNext() {
      int result;
      result = SQLite.sqlite3_step(pStmt);
      if ( result == SQLite.SQLITE_ROW ) {
        currentPosition++;
        return true;
      }
      if ( result == SQLite.SQLITE_DONE ) {
        currentPosition = getCount()+1;
      }
      return false;
    }

    @Override
    public String getString(int i) {
      return SQLite.sqlite3_column_text(pStmt, i);
    }

    @Override
    public int getInt(int i) {
      return SQLite.sqlite3_column_int(pStmt, i);
    }

    @Override
    public void close() {
      SQLite.sqlite3_finalize(pStmt);
      pStmt = null;
    }

    @Override
    public boolean moveToLast() {
      int result;
      do {
        result = SQLite.sqlite3_step(pStmt);
      } while( result == SQLite.SQLITE_ROW );
      if ( result == SQLite.SQLITE_DONE ) {
        currentPosition = getCount()-1;
        return true;
      }
      return false;
    }

    @Override
    public boolean moveToFirst() {
      if ( SQLite.sqlite3_reset(pStmt) == SQLite.SQLITE_OK ) {
        currentPosition = 0;
        return true;
      }
      return false;
   }

    @Override
    public boolean moveToPrevious() {
      SQLite.sqlite3_reset(pStmt);
      if ( currentPosition > 0 ) {
        for ( int counter = 0 ; counter < (currentPosition-1) ; counter++ ) {
          if ( SQLite.sqlite3_step(pStmt) != SQLite.SQLITE_ROW) {
            return false;
          }
        }
        currentPosition--;
      }
      else {
        currentPosition = -1;
      }
      return true;
    }

    @Override
    public int getColumnIndexOrThrow(String columnName) {
      int colCount = SQLite.sqlite3_column_count(pStmt);
      for ( int counter = 0 ; counter < colCount ; counter++ ) {
        if ( SQLite.sqlite3_column_name(pStmt, counter).equalsIgnoreCase(columnName)) {
          return counter;
        }
      }
      // TODO: How should this exception be?
      throw new SQLException();
    }

    @Override
    public byte[] getBlob(int ci) {
      return SQLite.sqlite3_column_blob(pStmt, ci).getByteArray(0,  SQLite.sqlite3_column_bytes(pStmt, ci));
    }

    @Override
    public short getShort(int ci) {
      return (short)getInt(ci);
    }

    @Override
    public double getDouble(int ci) {
      return SQLite.sqlite3_column_double(pStmt, ci);
    }

    @Override
    public float getFloat(int ci) {
      return (float)getDouble(ci);
    }

    @Override
    public long getLong(int ci) {
      return SQLite.sqlite3_column_int64(pStmt, ci);

    }

    @Override
    public int getPosition() {
      return currentPosition;
    }

    @Override
    public boolean isAfterLast() {
      return (currentPosition > 0 && currentPosition >= getCount());
    }

    @Override
    public boolean isBeforeFirst() {
      return (currentPosition < 0 );
    }

    @Override
    public boolean isFirst() {
      return (currentPosition == 0 );
    }

    @Override
    public boolean isLast() {
      return (currentPosition >= 0 && currentPosition == getCount()-1);
    }

    @Override
    public boolean requery() {
      moveToFirst(); // not quite right, but close enough
      return true;
    }

    @Override
    public boolean isNull(int ci) {
      if ( SQLite.sqlite3_column_text(pStmt, ci) == null ) {
        return true;
      }
      return false;
    }

    @Override
    public boolean isClosed() {
      return pStmt == null;
    }

    @Override
    public int getColumnCount() {
      return SQLite.sqlite3_column_count(pStmt);
    }

    @Override
    public String getColumnName(int i) {
      return SQLite.sqlite3_column_name(pStmt, i);
    }

    @Override
    public boolean moveToPosition(int oldPos) {
      SQLite.sqlite3_reset(pStmt);
      for ( int counter = 0 ; counter <= oldPos ; counter++ ) {
        if ( SQLite.sqlite3_step(pStmt) != SQLite.SQLITE_ROW) {
          return false;
        }
      }
      currentPosition = oldPos;
      return true;
    }

    @Override
    public int getType(int ci) {
      int columnType = SQLite.sqlite3_column_type(pStmt,ci);
      return columnType;
    }
}
