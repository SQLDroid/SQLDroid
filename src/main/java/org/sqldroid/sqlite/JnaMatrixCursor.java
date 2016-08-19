/*
 * Created on May 9, 2012
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.sqldroid.sqlite;

import org.sqldroid.SQLiteMatrixCursor;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class JnaMatrixCursor implements SQLiteMatrixCursor {

    public static final int FIELD_TYPE_STRING = 3;

    /** The list of column names. */
    protected String[] columnNames;

    /**
     * The ArrayList of values. This is populated by the "addRow" method and so
     * is an list of object arrays.
     */
    List<Object[]> rows;

    /** The current position in the cursor. */
    protected int currentPosition;

    public JnaMatrixCursor(String[] columnNames, int initialCapacity) {
        this.columnNames = columnNames;
        rows = new ArrayList<Object[]>(initialCapacity);
        currentPosition = -1;
    }

    public JnaMatrixCursor(String[] columnNames) {
        this.columnNames = columnNames;
        rows = new ArrayList<Object[]>();
        currentPosition = -1;
    }

    @Override
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
        return (int) getLong(i);
    }

    @Override
    public void close() {
        rows = null;
    }

    @Override
    public boolean moveToLast() {
        currentPosition = getCount() - 1;
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
    public int getColumnIndexOrThrow(String columnName) throws SQLException {
        for (int counter = 0; counter < columnNames.length; counter++) {
            if (columnNames[counter].equals(columnName)) {
                return counter;
            }
        }
        throw new SQLException();
    }

    @Override
    public byte[] getBlob(int ci) {
        return null;
    }

    @Override
    public short getShort(int ci) {
        return (short) getInt(ci);
    }

    @Override
    public double getDouble(int ci) {
        return Double.parseDouble(rows.get(currentPosition)[ci].toString());
    }

    @Override
    public float getFloat(int ci) {
        return (float) getDouble(ci);
    }

    @Override
    public long getLong(int ci) {
        return Long.parseLong(rows.get(currentPosition)[ci].toString());
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
        return (currentPosition < 0);
    }

    @Override
    public boolean isFirst() {
        return (currentPosition == 0);
    }

    @Override
    public boolean isLast() {
        return (currentPosition > 0 && currentPosition == getCount() - 1);
    }

    @Override
    public boolean requery() {
        return true;
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
    public boolean moveToPosition(int oldPos) {
        currentPosition = oldPos;
        return true;
    }

    @Override
    public int getType(int ci) {
        // this should really intelligently determine the type and return a
        // worthwhile value,
        // but, for now, I'll just return "string".
        return FIELD_TYPE_STRING;
    }

}
