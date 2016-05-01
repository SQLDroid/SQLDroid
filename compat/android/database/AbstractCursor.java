/*
 * Created on May 9, 2012
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package android.database;

import com.sun.jna.Pointer;

public class AbstractCursor implements Cursor {  
  
  protected Pointer pStmt;
  
  /** The row count. */
  protected int rowCount;
  
  /** The current position in the cursor. */
  protected int currentPosition;
  
  public AbstractCursor ( Pointer pStmt ) {
    this.pStmt = pStmt;
    rowCount = -1;
    rowCount = getCount();
    currentPosition = -1;
  }

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

  public String getString(int i) {
    return SQLite.sqlite3_column_text(pStmt, i);
  }

  public int getInt(int i) {
    return SQLite.sqlite3_column_int(pStmt, i);
  }

  public void close() {
    SQLite.sqlite3_finalize(pStmt);
    pStmt = null;
  }

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

  public boolean moveToFirst() {
    if ( SQLite.sqlite3_reset(pStmt) == SQLite.SQLITE_OK ) {
      currentPosition = 0;
      return true;
    }
    return false;
 }

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

  public int getColumnIndex(String columnName) {
    int colCount = SQLite.sqlite3_column_count(pStmt);
    for ( int counter = 0 ; counter < colCount ; counter++ ) {
      if ( SQLite.sqlite3_column_name(pStmt, counter).equalsIgnoreCase(columnName)) {
        return counter;
      }
    }
    return -1;
  }

  public byte[] getBlob(int ci) {
    return SQLite.sqlite3_column_blob(pStmt, ci).getByteArray(0,  SQLite.sqlite3_column_bytes(pStmt, ci));
  }

  public byte getShort(int ci) {
    return (byte)getInt(ci);
  }

  public double getDouble(int ci) {
    return SQLite.sqlite3_column_double(pStmt, ci);
  }

  public float getFloat(int ci) {
    return (float)getDouble(ci);
  }

  public long getLong(int ci) {
    return SQLite.sqlite3_column_int64(pStmt, ci);

  }

  public int getPosition() {
    return currentPosition;
  }

  public boolean isAfterLast() {
    return (currentPosition > 0 && currentPosition >= getCount());
  }

  public boolean isBeforeFirst() {
    return (currentPosition < 0 );
  }

  public boolean isFirst() {
    return (currentPosition == 0 );
  }

  public boolean isLast() {
    return (currentPosition >= 0 && currentPosition == getCount()-1);
  }

  public void requery() {
    moveToFirst(); // not quite right, but close enough    
  }

  public boolean isNull(int ci) {
    if ( SQLite.sqlite3_column_text(pStmt, ci) == null ) {
      return true;  
    }
    return false;
  }

  public boolean isClosed() {
    return pStmt == null;
  }

  public int getColumnCount() {
    return SQLite.sqlite3_column_count(pStmt);
  }

  public String getColumnName(int i) {
    return SQLite.sqlite3_column_name(pStmt, i);
  }

  public void moveToPosition(int oldPos) {
    SQLite.sqlite3_reset(pStmt);
    for ( int counter = 0 ; counter <= oldPos ; counter++ ) {
      if ( SQLite.sqlite3_step(pStmt) != SQLite.SQLITE_ROW) {
        return;
      }
    }
    currentPosition = oldPos;
  }

  @Override
  public int getType(int ci) {
    int columnType = SQLite.sqlite3_column_type(pStmt,ci);
    return columnType;
  }
  
}
