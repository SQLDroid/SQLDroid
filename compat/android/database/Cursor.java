/*
 * Created on May 9, 2012
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package android.database;

public interface Cursor {

  public static final int FIELD_TYPE_NULL = 0;
  public static final int FIELD_TYPE_INTEGER = 1;
  public static final int FIELD_TYPE_FLOAT = 2;
  public static final int FIELD_TYPE_STRING = 3;
  public static final int FIELD_TYPE_BLOB = 4;

  public int getCount();

  public boolean moveToNext();

  public String getString(int i);
  
  public int getInt(int i);

  public void close();
  
  public boolean moveToLast();

  public boolean moveToFirst();

  public boolean moveToPrevious();

  public int getColumnIndex(String columnName);

  public byte[] getBlob(int ci);

  public byte getShort(int ci);

  public double getDouble(int ci);

  public float getFloat(int ci);

  public long getLong(int ci);

  public int getPosition();
  
  public boolean isAfterLast();

  public boolean isBeforeFirst();

  public boolean isFirst();

  public boolean isLast();

  public void requery();

  public boolean isNull(int ci);

  public boolean isClosed();

  public int getColumnCount();

  public String getColumnName(int i);

  public void moveToPosition(int oldPos);
  
  public int getType(int ci);

}
