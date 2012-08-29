/*
 * Created on May 9, 2012
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package android.database;

import java.util.ArrayList;
import java.util.List;

public class MatrixCursor implements Cursor {

  /** The list of column names. */
  protected String[] columnNames;

  /** The ArrayList of values.  This is populated by the "addRow" method and so is an list of object arrays. */
  List<Object[]> rows;

  /** The current position in the cursor. */
  protected int currentPosition;

  public MatrixCursor(String[] columnNames, int initialCapacity ) {
    this.columnNames=columnNames;
    rows = new ArrayList<Object[]>(initialCapacity);
    currentPosition = -1;
  }

  public MatrixCursor(String[] columnNames) {
    this.columnNames=columnNames;
    rows = new ArrayList<Object[]>();
    currentPosition = -1;
  }

  public void addRow(Object[] column) {
    rows.add(column);
  }

  @Override
  public int getCount() {
    return rows.size();
  }

  @Override
  public boolean moveToNext() {
    currentPosition++;
    return true;
  }

  @Override
  public String getString(int i) {
    return rows.get(currentPosition)[i].toString();
  }

  @Override
  public int getInt(int i) {
    return (int)getLong(i);
  }

  @Override
  public void close() {
    rows = null;    
  }

  @Override
  public boolean moveToLast() {
    currentPosition = getCount()-1;
    return false;
  }

  @Override
  public boolean moveToFirst() {
    currentPosition = 0;
    return false;
  }

  @Override
  public boolean moveToPrevious() {
    currentPosition--;
    return false;
  }

  @Override
  public int getColumnIndex(String columnName) {
    for ( int counter = 0 ; counter < columnNames.length ; counter++ ) {
      if ( columnNames[counter].equals(columnName)) {
        return counter;
      }
    }
    return -1;
  }

  @Override
  public byte[] getBlob(int ci) {
    return null;
  }

  @Override
  public byte getShort(int ci) {
    return (byte)getInt(ci);
  }

  @Override
  public double getDouble(int ci) {
    return Double.parseDouble(rows.get(currentPosition)[ci].toString());
  }

  @Override
  public float getFloat(int ci) {
    return (float)getDouble(ci);
  }

  @Override
  public long getLong(int ci) {
    return Long.parseLong(rows.get(currentPosition)[ci].toString());
  }

  @Override
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
    return (currentPosition > 0 && currentPosition == getCount()-1);
  }

  @Override
  public void requery() {

  }

  @Override
  public boolean isNull(int ci) {
    return false;
  }

  @Override
  public boolean isClosed() {
    return rows != null;
  }

  @Override
  public int getColumnCount() {
    return columnNames.length;
  }

  @Override
  public String getColumnName(int i) {
    return columnNames[i];
  }

  @Override
  public void moveToPosition(int oldPos) {
    currentPosition = oldPos;    
  }

  @Override
  public int getType(int ci) {
    // this should really intelligently determine the type and return a worthwhile value,
    // but, for now, I'll just return "string".
    return FIELD_TYPE_STRING;
  }



}
