/*
 * Created on May 9, 2012
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package android.database.sqlite;

import java.util.Locale;

import android.database.AbstractCursor;
import android.database.Cursor;
import android.database.SQLite;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

public class SQLiteDatabase {

  public static final int OPEN_READWRITE = 0;
  public static final int CREATE_IF_NECESSARY = 0x10000000;

  public static final int SQLITE_OPEN_READWRITE=0x00000002;  /* Ok for sqlite3_open_v2() */
  public static final int SQLITE_OPEN_CREATE=0x00000004;  /* Ok for sqlite3_open_v2() */
  public static final int NO_LOCALIZED_COLLATORS = 0x00000010;  /* Here for compatibility. */

  /** The JNA pointer to the native database. */
  protected Pointer pDb;

  public SQLiteDatabase(Pointer pDb) {
    this.pDb = pDb;
  }

  public boolean isOpen() {
    return pDb != null;
  }

  public static SQLiteDatabase openDatabase(String dbQname, Object object, int i) throws SQLiteException {
    //SQLite.
    final PointerByReference ppDb = new PointerByReference();
    //SQLite.sqlite3_db_filename(null, dbQname);
    final int res = SQLite.sqlite3_open_v2(dbQname, ppDb, SQLITE_OPEN_READWRITE | SQLITE_OPEN_CREATE, null);
    if (res != SQLite.SQLITE_OK) {
      if (ppDb.getValue() != null) {
        SQLite.sqlite3_close(ppDb.getValue());
      }
      throw new SQLiteException("error while opening a database connexion to " +  dbQname);
    }
    return new SQLiteDatabase(ppDb.getValue());
  }

  public Cursor rawQuery(String sql, String[] makeArgListQueryString)   {
    Pointer pStmt = null;
    Cursor cursor = null;
    try {
      pStmt = prepare(sql, makeArgListQueryString);
      cursor = new AbstractCursor(pStmt);
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
  public Pointer prepare(String sql, Object[] makeArgListQueryString) throws SQLiteException {
    Pointer pStmt = null;
    if (sql != null && sql.length() > 0) {
      if ( !isOpen() ) {
        throw new SQLiteException("Database Not Open");
      }
      Pointer pSql = SQLite.nativeString(sql);
      PointerByReference ppStmt = new PointerByReference();
      PointerByReference ppTail = new PointerByReference();
      int res = SQLite.sqlite3_prepare_v2(pDb, pSql, -1, ppStmt, ppTail);
      if (res != SQLite.SQLITE_OK) {
        throw new SQLiteException("error preparing statement " +  sql);
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
            throw new SQLiteException("error binding " +   makeArgListQueryString[counter] + " to " + sql);

          }
        }
      }
      pStmt =  ppStmt.getValue();
    }
    return pStmt;
  }

  public void execSQL(String sql) throws SQLiteException {
    execSQL (sql, null);
  }

  public void execSQL(String sql, Object[] makeArgListQueryObject) {
    Pointer pStmt = null;
    try {
      pStmt = prepare(sql, makeArgListQueryObject);
      int res = SQLite.sqlite3_step(pStmt);
      if (res != SQLite.SQLITE_ROW && res != SQLite.SQLITE_DONE ) {
        throw new SQLiteException("error executing statement " +  sql);
      }

    } finally {
      if (pStmt != null) {
        SQLite.sqlite3_finalize(pStmt);
        pStmt = null;
      }
    }
  }

  public void setTransactionSuccessful() {
    // TODO Auto-generated method stub

  }

  public void beginTransaction() {
    // TODO Auto-generated method stub

  }

  public void endTransaction() {
    // TODO Auto-generated method stub

  }

  public void close() {
    SQLite.sqlite3_close(pDb);
    pDb = null;
  }

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



}
