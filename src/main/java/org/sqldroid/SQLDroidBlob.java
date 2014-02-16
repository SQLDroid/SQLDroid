package org.sqldroid;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;

public class SQLDroidBlob implements Blob {

	private final byte[] bytes;

	public SQLDroidBlob(byte [] bytes) {
		this.bytes = bytes;
	}
	
	@Override
	public InputStream getBinaryStream() throws SQLException {
        return new ByteArrayInputStream(bytes);
	}

	@Override
	public byte[] getBytes(long pos, int length) throws SQLException {
		return bytes;
	}

	@Override
	public long length() throws SQLException {
		return bytes.length;
	}

	@Override
	public long position(Blob pattern, long start) throws SQLException {
        throw new UnsupportedOperationException();
	}

	@Override
	public long position(byte[] pattern, long start) throws SQLException {
        throw new UnsupportedOperationException();
	}

	@Override
	public OutputStream setBinaryStream(long pos) throws SQLException {
        throw new UnsupportedOperationException();
	}

	@Override
	public int setBytes(long pos, byte[] theBytes) throws SQLException {
        throw new UnsupportedOperationException();
	}

	@Override
	public int setBytes(long pos, byte[] theBytes, int offset, int len) throws SQLException {
        throw new UnsupportedOperationException();
	}

	@Override
	public void truncate(long len) throws SQLException {
        throw new UnsupportedOperationException();
	}

	@Override
	public void free() throws SQLException {
        throw new UnsupportedOperationException();
	}

	@Override
	public InputStream getBinaryStream(long pos, long length) throws SQLException {
        throw new UnsupportedOperationException();
	}

  /** Print the length of the blob along with the first 10 characters.
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    StringBuffer sb = new StringBuffer();
    if ( bytes == null ) {
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
      sb.append (Integer.toHexString(bytes[counter]));
      sb.append(" ");
    }
    sb.append("(");
    for ( int counter = 0 ; counter < length ; counter++ ) {
      sb.append (Character.toString((char) bytes[counter]));
      if ( counter == length-1 ) {
        sb.append(")");
      } else {
        sb.append(" ");
      }
    }
    return sb.toString();
  }


}
