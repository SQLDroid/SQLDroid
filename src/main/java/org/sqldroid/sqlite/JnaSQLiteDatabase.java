/*
 * Created on May 9, 2012
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.sqldroid.sqlite;

import java.util.Locale;

import android.database.SQLException;

import org.sqldroid.SQLiteCursor;
import org.sqldroid.SQLiteDatabase;
import org.sqldroid.SQLiteMatrixCursor;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

public class JnaSQLiteDatabase implements SQLiteDatabase {

  public static final int OPEN_READWRITE = 0;
  public static final int CREATE_IF_NECESSARY = 0x10000000;

  public static final int SQLITE_OPEN_READWRITE=0x00000002;  /* Ok for sqlite3_open_v2() */
  public static final int SQLITE_OPEN_CREATE=0x00000004;  /* Ok for sqlite3_open_v2() */
  public static final int NO_LOCALIZED_COLLATORS = 0x00000010;  /* Here for compatibility. */

  /** The JNA pointer to the native database. */
  protected Pointer pDb;

  public JnaSQLiteDatabase(String filename, int flags) {
      final PointerByReference ppDb = new PointerByReference();
      int res = SQLite.sqlite3_open_v2(filename, ppDb, flags, null);
      if (res != SQLite.SQLITE_OK) {
          if (ppDb.getValue() != null) {
            SQLite.sqlite3_close(ppDb.getValue());
          }
          throw new SQLException("error while opening a database connexion to " +  filename);
        }
      this.pDb = ppDb.getValue();
  }

  public boolean isOpen() {
    return pDb != null;
  }

  @Override
public SQLiteCursor rawQuery(String sql, String[] makeArgListQueryString)   {
    Pointer pStmt = null;
    SQLiteCursor cursor = null;
    try {
      pStmt = prepare(sql, makeArgListQueryString);
      cursor = new JnaSQLiteCursor(pStmt);
    } finally {
      if (cursor == null) {
        SQLite.sqlite3_finalize(pStmt);
        pStmt = null;
      }
    }
    return cursor;
  }

  /**
   * @param sql query
   * @return Prepared Statement
   * @throws ConnException
   */
  public Pointer prepare(String sql, Object[] makeArgListQueryString) throws SQLException {
    Pointer pStmt = null;
    if (sql != null && sql.length() > 0) {
      if ( !isOpen() ) {
        throw new SQLException("Database Not Open");
      }
      Pointer pSql = SQLite.nativeString(sql);
      PointerByReference ppStmt = new PointerByReference();
      PointerByReference ppTail = new PointerByReference();
      int res = SQLite.sqlite3_prepare_v2(pDb, pSql, -1, ppStmt, ppTail);
      if (res != SQLite.SQLITE_OK) {
        throw new SQLException("error preparing statement " +  sql);
      }
      if ( makeArgListQueryString != null ) {
        for ( int counter = 0 ; counter < makeArgListQueryString.length ; counter++  ) {
          int res1;
          if (makeArgListQueryString[counter] instanceof byte[] ) { // a blob
            byte[] blobData = (byte[])makeArgListQueryString[counter];
            res1 = SQLite.sqlite3_bind_blob(ppStmt.getValue(), counter+1, blobData, blobData.length, SQLite.SQLITE_TRANSIENT);
          }
          else {
            //res1 = SQLite.sqlite3_bind_text(ppStmt.getValue(), counter+1, makeArgListQueryString[counter].toString(), -1, SQLite.SQLITE_TRANSIENT);
            if (makeArgListQueryString[counter] != null) {
                res1 = SQLite.sqlite3_bind_text(ppStmt.getValue(), counter+1, makeArgListQueryString[counter].toString(), -1, SQLite.SQLITE_TRANSIENT);
            }
            else {
                res1 = SQLite.sqlite3_bind_null(ppStmt.getValue(), counter+1);
            }
          }
          if (res1 != SQLite.SQLITE_OK) {
            throw new SQLException("error binding " +   makeArgListQueryString[counter] + " to " + sql);

          }
        }
      }
      pStmt =  ppStmt.getValue();
    }
    return pStmt;
  }

  @Override
public void execSQL(String sql) throws SQLException {
    execSQL (sql, null);
  }

  @Override
public void execSQL(String sql, Object[] makeArgListQueryObject) {
    Pointer pStmt = null;
    try {
      pStmt = prepare(sql, makeArgListQueryObject);
      int res = SQLite.sqlite3_step(pStmt);
      if (res != SQLite.SQLITE_ROW && res != SQLite.SQLITE_DONE ) {
        throw new SQLException("error executing statement " +  sql);
      }

    } finally {
      if (pStmt != null) {
        SQLite.sqlite3_finalize(pStmt);
        pStmt = null;
      }
    }
  }

  @Override
public void setTransactionSuccessful() {
    // TODO Auto-generated method stub

  }

  @Override
public void beginTransaction() {
    // TODO Auto-generated method stub

  }

  @Override
public void endTransaction() {
    // TODO Auto-generated method stub

  }

  @Override
public void close() {
    SQLite.sqlite3_close(pDb);
    pDb = null;
  }

  @Override
public int getVersion() {
    return SQLite.sqlite3_libversion().hashCode();
  }

  /** For compatibility.  Does nothing. */
  public void setLocale(Locale default1) {
  }

  /** Returns the number of rows changed associated with the last update or insert. */
  public int changedRowCount () {
      return SQLite.sqlite3_changes(pDb);
  }

@Override
public String getDatabaseName() {
    return null;
}

@Override
public boolean isClosed() {
    // TODO Auto-generated method stub
    return false;
}

@Override
public boolean inTransaction() {
    // TODO Auto-generated method stub
    return false;
}

@Override
public SQLiteMatrixCursor createCursor(String[] columnNames, int count) {
    return new JnaMatrixCursor(columnNames, count);
}

@Override
public SQLiteCursor mergeCursors(SQLiteCursor[] cursors) {
    return new JnaMergeCursor(cursors);
}



}
