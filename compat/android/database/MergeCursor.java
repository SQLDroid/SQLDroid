/*
 * Created on May 9, 2012
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package android.database;

public class MergeCursor implements Cursor {

  /** The array of cursors. */
  protected Cursor[] cursors;

  /** The cursor that is currently in play. */
  protected Cursor currentCursor;

  /** Keep track of the current position globally in all the cursors. */
  protected int currentPosition;

  /** The current cursor index in the cursors array. */
  protected int currentCursorIndex;

  public MergeCursor(Cursor[] cursors) {
    this.cursors = cursors;
    currentCursorIndex = 0;
    currentCursor = cursors[currentCursorIndex];
    currentPosition = -1;
  }

  /**
   * @return
   * @see android.database.Cursor#getCount()
   */
  public int getCount() {
    int count = 0;
    for ( int counter = 0 ; counter < cursors.length ; counter++ ) {
      Cursor tmpCursor = cursors[counter];
      count += tmpCursor.getCount();
    }
    return count;
  }

  /**
   * @return
   * @see android.database.Cursor#moveToNext()
   */
  public boolean moveToNext() {
    return moveTo(getPosition()+1);
  }

  /**
   * @param i
   * @return
   * @see android.database.Cursor#getString(int)
   */
  public String getString(int i) {
    return currentCursor.getString(i);
  }

  /**
   * @param i
   * @return
   * @see android.database.Cursor#getInt(int)
   */
  public int getInt(int i) {
    return currentCursor.getInt(i);
  }

  /**
   * 
   * @see android.database.Cursor#close()
   */
  public void close() {
    for ( int counter = 0 ; counter < cursors.length ; counter++ ) {
      currentCursor = cursors[counter];
      currentCursor.close();
    }
  }

  /**
   * @return
   * @see android.database.Cursor#moveToLast()
   */
  public boolean moveToLast() {
    currentPosition = getCount()-1;
    currentCursorIndex = cursors.length-1;
    return currentCursor.moveToLast();
  }

  /**
   * @return
   * @see android.database.Cursor#moveToFirst()
   */
  public boolean moveToFirst() {
    currentPosition = 0;
    currentCursor = cursors[0];
    currentCursorIndex=0;
    return currentCursor.moveToFirst();
  }

  /**
   * @return
   * @see android.database.Cursor#moveToPrevious()
   */
  public boolean moveToPrevious() {
    return moveTo(getPosition()-1);
  }

  /**
   * @param columnName
   * @return
   * @see android.database.Cursor#getColumnIndex(java.lang.String)
   */
  public int getColumnIndex(String columnName) {
    return currentCursor.getColumnIndex(columnName);
  }

  /**
   * @param ci
   * @return
   * @see android.database.Cursor#getBlob(int)
   */
  public byte[] getBlob(int ci) {
    return currentCursor.getBlob(ci);
  }

  /**
   * @param ci
   * @return
   * @see android.database.Cursor#getShort(int)
   */
  public byte getShort(int ci) {
    return currentCursor.getShort(ci);
  }

  /**
   * @param ci
   * @return
   * @see android.database.Cursor#getDouble(int)
   */
  public double getDouble(int ci) {
    return currentCursor.getDouble(ci);
  }

  /**
   * @param ci
   * @return
   * @see android.database.Cursor#getFloat(int)
   */
  public float getFloat(int ci) {
    return currentCursor.getFloat(ci);
  }

  /**
   * @param ci
   * @return
   * @see android.database.Cursor#getLong(int)
   */
  public long getLong(int ci) {
    return currentCursor.getLong(ci);
  }
  /**
   * @param ci
   * @return
   * @see android.database.Cursor#getType(int)
   */
  public int getType(int ci) {
    return currentCursor.getType(ci);
  }

  /**
   * @return
   * @see android.database.Cursor#getPosition()
   */
  public int getPosition() {
    return currentPosition;
  }

  /**
   * @return
   * @see android.database.Cursor#isAfterLast()
   */
  public boolean isAfterLast() {
    if (currentCursorIndex != cursors.length-1) {
      return false;
    }
    return currentCursor.isAfterLast();
  }

  /**
   * @return
   * @see android.database.Cursor#isBeforeFirst()
   */
  public boolean isBeforeFirst() {
    if ( currentCursorIndex != 0 ) {
      return false;
    }
    return currentCursor.isBeforeFirst();
  }

  /**
   * @return
   * @see android.database.Cursor#isFirst()
   */
  public boolean isFirst() {
    if ( currentCursorIndex != 0 ) {
      return false;
    }
    return currentCursor.isFirst();
  }

  /**
   * @return
   * @see android.database.Cursor#isLast()
   */
  public boolean isLast() {
    if (currentCursorIndex != cursors.length-1) {
      return false;
    }
    return currentCursor.isLast();
  }

  /**
   * 
   * @see android.database.Cursor#requery()
   */
  public void requery() {
    moveToFirst();  // not right, but not interesting.
  }

  /**
   * @param ci
   * @return
   * @see android.database.Cursor#isNull(int)
   */
  public boolean isNull(int ci) {
    return currentCursor.isNull(ci);
  }

  /**
   * @return
   * @see android.database.Cursor#isClosed()
   */
  public boolean isClosed() {
    return currentCursor.isClosed();
  }

  /**
   * @return
   * @see android.database.Cursor#getColumnCount()
   */
  public int getColumnCount() {
    return currentCursor.getColumnCount();
  }

  /**
   * @param i
   * @return
   * @see android.database.Cursor#getColumnName(int)
   */
  public String getColumnName(int i) {
    return currentCursor.getColumnName(i);
  }

  /**
   * @param oldPos
   * @see android.database.Cursor#moveToPosition(int)
   */
  public boolean moveTo(int oldPos) {
    int posn = oldPos +1;
    if ( posn > getCount() ) {
      return false;
    }
    for ( int counter = 0 ; counter < cursors.length ; counter++ ) {
      currentCursor = cursors[counter];
      currentCursorIndex = counter;
      if ( posn <= currentCursor.getCount() ) {
        break;
      }
      posn -= currentCursor.getCount();
    }
    currentPosition = oldPos;
    currentCursor.moveToPosition(posn-1);
    if ( currentCursor.isAfterLast() ) {
      return false;
    }
    return true;
  }

  public void moveToPosition(int oldPos ) {
    moveTo(oldPos);
  }

}
