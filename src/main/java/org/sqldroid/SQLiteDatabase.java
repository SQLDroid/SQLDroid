package org.sqldroid;

import java.lang.reflect.Method;
import java.sql.SQLException;

import android.database.Cursor;
import android.database.sqlite.SQLiteException;

/** A  proxy class for the database that allows actions to be retried without forcing every method 
 * through the reflection process.  This was originally implemented as an interface and a Proxy.
 * While quite slick, it always used reflection which can be horribly slow.
 * 
 * SQLiteDatabaseLockedException did not exist before API 11 so this resolves that problem by
 * using reflection to determine if the Exception is a SQLiteDatabaseLockedException which will,
 * presumably, only work on API 11 or later.
 * 
 * This is still pretty ugly.  Part of the problem is an architectural decision to handle errors within
 * the JDBC driver.
 * 
 * ALL databases fail. Any code that used JDBC has to be able to handle those failures.
 * The locking errors in SQLite are analogous to network failures in other DBs  Handling them within the
 * driver deprives the upper level code of the opportunity to handle these in an application specific 
 * manner.  The timouts in the first implementation were extreme.  The loops would wait one second between attempts 
 * to retry that, like, the middle of next week in computer terms.  
 * 
 * To provide a specific problem case for the timout code.  We routinely read data on sub-second intervals.
 * Frequently on a 100ms timebase.  The data collection and insert usually occur on the same thread.  This 
 * isn't a requirement, but it helps to have the code synchronized. If we attempt an insert, we want the 
 * code to fail fast.  That insert is cached and will be completed when the next insert is attempted.  So 100ms
 * later there will be two inserts (or 3,  200ms later, etc.).
 * 
 * This code now also makes the timeouts optional and attempts to minimize the performance penalties in this case.
 */
public class SQLiteDatabase {

  protected static enum Transaction {
    setTransactionSuccessful,endTransaction,close,beginTransaction
  }

  /** The actual android database. */
  protected android.database.sqlite.SQLiteDatabase sqliteDatabase;

  /** The timeout in milliseconds.  Zero indicated that there should be no retries.  
   * Any other value allows an action to be retried until success or the timeout has expired.
   */
  protected long timeout;

  /** The delay in milliseconds between retries when timeout is given.
   * The value is ignored if timeout is not given.
   */
  protected long retryInterval;

  /** The name of the database. */
  protected String dbQname;
  
  /** The method to invoke to get the changed row count. */
  protected Method getChangedRowCount;

  /** Returns true if the exception is an instance of "SQLiteDatabaseLockedException".  Since this exception does not exist
   * in APIs below 11 this code uses reflection to check the exception type. 
   */
  protected boolean isLockedException ( SQLiteException maybeLocked ) {
    try {
      if (Class.forName("android.database.sqlite.SQLiteDatabaseLockedException", false, getClass().getClassLoader()).isAssignableFrom(maybeLocked.getClass()) ) {
        return true;
      }
    } catch (ClassNotFoundException e) {
      // no android.database.sqlite.SQLiteDatabaseLockedException
    }
    return false;
  }

  /** 
   * @param dbQname
   * @param timeout
   * @param retryInterval
   * @throws SQLException thrown if the attempt to connect to the database throws an exception
   * other than a locked exception or throws a locked exception after the timeout has expired.
   */
  public SQLiteDatabase(String dbQname, long timeout, long retryInterval, int flags) throws SQLException {
    super();
    this.dbQname = dbQname;
    this.timeout = timeout;
    this.retryInterval = retryInterval;
    long timeNow = System.currentTimeMillis();
    long delta = 0;
    while (sqliteDatabase == null) {
      try {
        sqliteDatabase = android.database.sqlite.SQLiteDatabase.openDatabase(dbQname, null, flags );
      } catch (SQLiteException e) {
        if ( isLockedException(e) ) {
          try {
            Thread.sleep(retryInterval);
          } catch (InterruptedException e1) {
            // ignore
          }
          delta = System.currentTimeMillis() - timeNow;
          if ( delta >= timeout ) {
            throw SQLDroidConnection.chainException(e);
          }
        } else {
          throw SQLDroidConnection.chainException(e);
        }
      }
    }
  }

  /** Proxy for the "rawQuery" command. 
   * @throws SQLException 
   */
  public Cursor rawQuery(String sql, String[] makeArgListQueryString) throws SQLException {
    Log.v("SQLiteDatabase rawQuery: " + Thread.currentThread().getId() + " \"" + Thread.currentThread().getName() + "\" " + sql);
    long timeNow = System.currentTimeMillis();
    long delta = 0;
    do {
      try {
        Cursor cursor = sqliteDatabase.rawQuery(sql, makeArgListQueryString);
        Log.v("SQLiteDatabase rawQuery OK: " + Thread.currentThread().getId() + " \"" + Thread.currentThread().getName() + "\" " + sql);
        return cursor;
      } catch (SQLiteException e) {
        if ( isLockedException(e) ) {
          delta = System.currentTimeMillis() - timeNow;
        } else {
          throw SQLDroidConnection.chainException(e);
        }
      }
    } while (delta < timeout);
    throw new SQLException ("Timeout Expired");
  }

  /** Proxy for the "execSQL" command. 
   * @throws SQLException 
   */
  public void execSQL(String sql, Object[] makeArgListQueryObject) throws SQLException {
    Log.v("SQLiteDatabase execSQL: " + Thread.currentThread().getId() + " \"" + Thread.currentThread().getName() + "\" " + sql);
    long timeNow = System.currentTimeMillis();
    long delta = 0;
    do {
      try {
        sqliteDatabase.execSQL(sql, makeArgListQueryObject);
        Log.v("SQLiteDatabase execSQL OK: " + Thread.currentThread().getId() + " \"" + Thread.currentThread().getName() + "\" " + sql);
        return;
      } catch (SQLiteException e) {
        if ( isLockedException(e) ) {
          delta = System.currentTimeMillis() - timeNow;
        } else {
          throw SQLDroidConnection.chainException(e);
        }
      }
    } while (delta < timeout);
    throw new SQLException ("Timeout Expired");
  }

  /** Proxy for the "execSQL" command. 
   * @throws SQLException 
   */
  public void execSQL(String sql) throws SQLException {
    Log.v("SQLiteDatabase execSQL: " + Thread.currentThread().getId() + " \"" + Thread.currentThread().getName() + "\" " + sql);
    long timeNow = System.currentTimeMillis();
    long delta = 0;
    do {
      try {
        sqliteDatabase.execSQL(sql);
        Log.v("SQLiteDatabase execSQL OK: " + Thread.currentThread().getId() + " \"" + Thread.currentThread().getName() + "\" " + sql);
        return;
      } catch (SQLiteException e) {
        if ( isLockedException(e) ) {
          delta = System.currentTimeMillis() - timeNow;
        } else {
          throw SQLDroidConnection.chainException(e);
        }
      }
    } while (delta < timeout);
    throw new SQLException ("Timeout Expired");
  }

  /** Returns the android SQLiteDatabase that we are delegating for.
   * @return the sqliteDatabase
   */
  public android.database.sqlite.SQLiteDatabase getSqliteDatabase() {
    return sqliteDatabase;
  }

  /** Executes one of the methods in the "transactions" enum. This just allows the
   * timeout code to be combined in one method. 
   * @throws SQLException thrown if the timeout expires before the method successfully executes.
   */
  public void execNoArgVoidMethod (Transaction transaction ) throws SQLException {
    long timeNow = System.currentTimeMillis();
    long delta = 0;
    do {
      try {
        switch ( transaction ) {
          case setTransactionSuccessful:
            sqliteDatabase.setTransactionSuccessful();
            return;
          case beginTransaction:
            sqliteDatabase.beginTransaction();
            return;
          case endTransaction:
            sqliteDatabase.endTransaction();
            return;
          case close:
            sqliteDatabase.close();
            return;
        }
      } catch (SQLiteException e) {
        if ( isLockedException(e) ) {
          delta = System.currentTimeMillis() - timeNow;
        } else {
          throw SQLDroidConnection.chainException(e);
        }
      }
    } while (delta < timeout);
    throw new SQLException ("Timeout Expired");
  }

  /** Call the "setTransactionSuccessful" method on the database. 
   * @throws SQLException */
  public void setTransactionSuccessful() throws SQLException {
    execNoArgVoidMethod(Transaction.setTransactionSuccessful);
  }

  /** Call the "beginTransaction" method on the database. 
   * @throws SQLException */
  public void beginTransaction() throws SQLException {    
    execNoArgVoidMethod(Transaction.beginTransaction);
  }

  /** Call the "endTransaction" method on the database. 
   * @throws SQLException */
  public void endTransaction() throws SQLException {    
    execNoArgVoidMethod(Transaction.endTransaction);
  }

  /** Call the "close" method on the database. 
   * @throws SQLException */
  public void close() throws SQLException {    
    execNoArgVoidMethod(Transaction.close);
  }

  
  /** The count of changed rows.  On JNA platforms, this is a call to sqlite3_changes
   * On Android, it's a convoluted call to a package-private method (or, if that fails, the
   * response is '1'.
   */
  public int changedRowCount () {
    if ( getChangedRowCount == null ) {
      try {  // JNA/J2SE compatibility method.
        getChangedRowCount = sqliteDatabase.getClass().getMethod("changedRowCount", (Class<?>[])null);
      } catch ( Exception any ) {
        try {
          // Android
          getChangedRowCount = sqliteDatabase.getClass().getDeclaredMethod("lastChangeCount", (Class<?>[])null);
          getChangedRowCount.setAccessible(true);
        } catch (Exception e) {
          // ignore
        } 
      }
    }
    if ( getChangedRowCount != null ) {
      try {
        return ((Integer)getChangedRowCount.invoke(sqliteDatabase, (Object[])null)).intValue();
      } catch (Exception e) {
        // ignore
      } 
    }
    return 1;  // assume that the insert/update succeeded in changing exactly one row (terrible assumption, but I have nothing better).
  }

}
