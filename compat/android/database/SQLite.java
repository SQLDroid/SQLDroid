/*
 * The author disclaims copyright to this source code.  In place of
 * a legal notice, here is a blessing:
 *
 *    May you do good and not evil.
 *    May you find forgiveness for yourself and forgive others.
 *    May you share freely, never taking more than you give.
 */
package android.database;

import com.sun.jna.Library;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

public class SQLite implements Library {
  public static final String JNA_LIBRARY_NAME = "sqlite3";
  // public static final NativeLibrary JNA_NATIVE_LIB = NativeLibrary.getInstance(SQLite.JNA_LIBRARY_NAME);
  static {
    Native.register(JNA_LIBRARY_NAME);
  }

  public static final int SQLITE_OK = 0;

  public static final int SQLITE_ROW = 100;
  public static final int SQLITE_DONE = 101;

  public static final int SQLITE_TRANSIENT = -1;

  public static native String sqlite3_libversion();
  public static native boolean sqlite3_threadsafe();

  public static native String sqlite3_errmsg(Pointer pDb);
  public static native int sqlite3_errcode(Pointer pDb);

  public static native int sqlite3_extended_result_codes(Pointer pDb, boolean onoff);
  public static native int sqlite3_extended_errcode(Pointer pDb);

  public static native int sqlite3_open_v2(String filename, PointerByReference ppDb, int flags, String vfs);
  public static native int sqlite3_close(Pointer pDb);
  public static native void sqlite3_interrupt(Pointer pDb);
  public static native int sqlite3_busy_timeout(Pointer pDb, int ms);

  // int sqlite3_limit(sqlite3*, int id, int newVal);

  public static native int sqlite3_changes(Pointer pDb);
  public static native int sqlite3_total_changes(Pointer pDb);
  public static native long sqlite3_last_insert_rowid(Pointer pDb);

  //public static native String sqlite3_db_filename(Pointer pDb, String dbName);
  //public static native int sqlite3_db_readonly(Pointer pDb, String dbName);

  public static native Pointer sqlite3_next_stmt(Pointer pDb, Pointer pStmt);

  public static native int sqlite3_table_column_metadata(Pointer pDb, String dbName, String tableName, String columnName,
                                                  PointerByReference pzDataType, PointerByReference pzCollSeq,
                                                  PointerByReference pNotNull, PointerByReference pPrimaryKey, PointerByReference pAutoinc);

  public static native int sqlite3_prepare_v2(Pointer pDb, Pointer sql, int nByte, PointerByReference ppStmt,
                                       PointerByReference pTail);
  public static native String sqlite3_sql(Pointer pStmt);
  public static native int sqlite3_finalize(Pointer pStmt);
  public static native int sqlite3_step(Pointer pStmt);
  public static native int sqlite3_reset(Pointer pStmt);
  public static native int sqlite3_clear_bindings(Pointer pStmt);
  //public static native boolean sqlite3_stmt_busy(Pointer pStmt);

  public static native int sqlite3_column_count(Pointer pStmt);
  public static native int sqlite3_data_count(Pointer pStmt);
  public static native int sqlite3_column_type(Pointer pStmt, int iCol);
  public static native String sqlite3_column_name(Pointer pStmt, int iCol);
  public static native String sqlite3_column_origin_name(Pointer pStmt, int iCol);
  public static native String sqlite3_column_table_name(Pointer pStmt, int iCol);
  public static native String sqlite3_column_database_name(Pointer pStmt, int iCol);

  /*
  const char *sqlite3_column_decltype(sqlite3_stmt*,int);
  */

  public static native Pointer sqlite3_column_blob(Pointer pStmt, int iCol);
  public static native int sqlite3_column_bytes(Pointer pStmt, int iCol);
  public static native double sqlite3_column_double(Pointer pStmt, int iCol);
  public static native int sqlite3_column_int(Pointer pStmt, int iCol);
  public static native long sqlite3_column_int64(Pointer pStmt, int iCol);
  public static native String sqlite3_column_text(Pointer pStmt, int iCol);
  //const void *sqlite3_column_text16(Pointer pStmt, int iCol);
  //sqlite3_value *sqlite3_column_value(Pointer pStmt, int iCol);

  public static native int sqlite3_bind_parameter_count(Pointer pStmt);
  public static native int sqlite3_bind_parameter_index(Pointer pStmt, String name);
  public static native String sqlite3_bind_parameter_name(Pointer pStmt, int i);

  public static native int sqlite3_bind_blob(Pointer pStmt, int i, byte[] value, int n, long xDel);
  public static native int sqlite3_bind_double(Pointer pStmt, int i, double value);
  public static native int sqlite3_bind_int(Pointer pStmt, int i, int value);
  public static native int sqlite3_bind_int64(Pointer pStmt, int i, long value);
  public static native int sqlite3_bind_null(Pointer pStmt, int i);
  public static native int sqlite3_bind_text(Pointer pStmt, int i, String value, int n, long xDel);
  //public static native int sqlite3_bind_text16(Pointer pStmt, int i, const void*, int, void(*)(void*));
  //public static native int sqlite3_bind_value(Pointer pStmt, int i, const sqlite3_value*);
  public static native int sqlite3_bind_zeroblob(Pointer pStmt, int i, int n);

  public static native Pointer sqlite3_mprintf(String zFormat, String arg);
  public static native void sqlite3_free(Pointer p);

  public static Pointer nativeString(String sql) { // TODO Check encoding?
    byte[] data = sql.getBytes();
    final Pointer pointer = new Memory(data.length + 1);
    pointer.write(0, data, 0, data.length);
    pointer.setByte(data.length, (byte) 0);
    return pointer;
  }
}