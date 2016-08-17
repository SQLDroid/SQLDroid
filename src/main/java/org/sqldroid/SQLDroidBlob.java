package org.sqldroid;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;

public class SQLDroidBlob implements Blob {

	byte [] b;
	
	public SQLDroidBlob(byte [] b) {
		this.b = b;
	}
	
	@Override
	public InputStream getBinaryStream() throws SQLException {
		return getBinaryStream(0, b.length);
	}

	@Override
  public InputStream getBinaryStream(long pos, long length) throws SQLException {
  	return new ByteArrayInputStream(b, (int)pos, (int)length);
  }

  @Override
	public byte[] getBytes(long pos, int length) throws SQLException {
    if (pos < 0) {
      throw new SQLException("pos must be > 0");
    }
    if (length < 0) {
      throw new SQLException("length must be > 0");
    }
    if (pos > 0 || length < b.length) {
      byte[] tmp = new byte[length];
      System.arraycopy(b, (int)pos, tmp, 0, length);
      return tmp;
    }
		return b;
	}

	@Override
	public long length() throws SQLException {
		return b.length;
	}

	@Override
	public long position(Blob pattern, long start) throws SQLException {
	  return position(pattern.getBytes(0, (int) pattern.length()), start);
	}

	@Override
	public long position(byte[] pattern, long start) throws SQLException {
	  throw new SQLFeatureNotSupportedException("position not supported");
	}

	@Override
	public OutputStream setBinaryStream(long pos) throws SQLException {
	  throw new SQLFeatureNotSupportedException("setBinaryStream not supported");
	}

	@Override
	public int setBytes(long pos, byte[] theBytes) throws SQLException {
	  return setBytes(pos, theBytes, 0, theBytes.length);
	}

	@Override
	public int setBytes(long pos, byte[] theBytes, int offset, int len) throws SQLException {
	  throw new SQLFeatureNotSupportedException("setBytes not supported");
	}

	@Override
	public void truncate(long len) throws SQLException {
	  throw new SQLFeatureNotSupportedException("truncate not supported");
	}

	@Override
	public void free() throws SQLException {
	  throw new SQLFeatureNotSupportedException("free not supported");
	}

	/** Print the length of the blob along with the first 10 characters.
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    StringBuffer sb = new StringBuffer();
    if ( b == null ) {
      sb.append("Null Blob");
    }
    sb.append ("Blob length ");
    long length = 0;
    try {
      length = length();
    } catch (SQLException e) {
      // never thrown
    }
    sb.append (length);
    sb.append(" ");
    if ( length > 10 ) {
      length = 10;
    }
    for ( int counter = 0 ; counter < length ; counter++ ) {
      sb.append ("0x");
      sb.append (Integer.toHexString(b[counter]));
      sb.append(" ");
    }
    sb.append("(");
    for ( int counter = 0 ; counter < length ; counter++ ) {
      sb.append (Character.toString((char)b[counter]));
      if ( counter == length-1 ) {
        sb.append(")");
      } else {
        sb.append(" ");
      }
    }
    return sb.toString();
  }


}
