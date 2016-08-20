package org.sqldroid;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The mapping in the Java programming language for the SQL <code>CLOB</code> type.
 * An SQL <code>CLOB</code> is a built-in type that stores a Character Large Object as a column value
 * in a row of a database table. By default drivers implement a <code>Clob</code> object using an SQL
 * <code>locator(CLOB)</code>, which means that a <code>Clob</code> object contains a logical pointer to the 
 * SQL <code>CLOB</code> data rather than the data itself. A <code>Clob</code> object is valid for the duration
 * of the transaction in which it was created.
 * <P>The <code>Clob</code> interface provides methods for getting the length of an SQL <code>CLOB</code> 
 * (Character Large Object) value, for materializing a <code>CLOB</code> value on the client, and for
 * searching for a substring or <code>CLOB</code> object within a <code>CLOB</code> value.
 * Methods in the interfaces {@link ResultSet}, {@link CallableStatement}, and {@link PreparedStatement}, such as
 * <code>getClob</code> and <code>setClob</code> allow a programmer to access an SQL <code>CLOB</code> value.
 * In addition, this interface has methods for updating a <code>CLOB</code> value.
 * 
 * Based on ClobImpl from DataNucleus project. Thanks to DataNucleus contributors.
 * @see <a href="https://github.com/datanucleus/datanucleus-rdbms/blob/master/src/main/java/org/datanucleus/store/rdbms/mapping/datastore/ClobImpl.java">ClobImpl from DataNucleus Project</a>
 * 
 */
public class SQLDroidClob implements Clob, NClob
{
    private String string;
    private long length;

    /** Reader for the operations that work that way. */
    private StringReader reader;

    /** InputStream for operations that work that way. TODO Rationalise with reader above. */
    private InputStream inputStream;

    /** Whether we have already freed resources. */
    boolean freed = false;

    /**
     * Constructor taking a string.
     * @param string The string.
     */
    public SQLDroidClob(String string)
    {
        if (string == null)
        {
            throw new IllegalArgumentException("String cannot be null");
        }
        this.string = string;
        this.length = string.length();
    }
    
    public long length() throws SQLException
    {
        if (freed)
        {
            throw new SQLException("free() has been called");
        }

        return length;
    }

    public void truncate(long len) throws SQLException
    {
        if (freed)
        {
            throw new SQLException("free() has been called");
        }

        throw new UnsupportedOperationException();        
    }

    public InputStream getAsciiStream() throws SQLException
    {
        if (freed)
        {
            throw new SQLException("free() has been called");
        }

        if (inputStream == null)
        {
            inputStream = new ByteArrayInputStream(string.getBytes());
        }
        return inputStream;
    }

    public OutputStream setAsciiStream(long pos) throws SQLException
    {
        if (freed)
        {
            throw new SQLException("free() has been called");
        }

        throw new UnsupportedOperationException();        
    }

    public Reader getCharacterStream() throws SQLException
    {
        if (freed)
        {
            throw new SQLException("free() has been called");
        }

        if (reader == null)
        {
            this.reader = new StringReader(string);
        }
        return reader;
    }

    public Writer setCharacterStream(long pos) throws SQLException
    {
        if (freed)
        {
            throw new SQLException("free() has been called");
        }

        throw new UnsupportedOperationException();        
    }

    /**
     * Free the Blob object and releases the resources that it holds.
     * The object is invalid once the free method is called.
     * @throws SQLException if an error occurs
     */
    public void free() throws SQLException
    {
        if (freed)
        {
            return;
        }

        string = null;
        if (reader != null)
        {
            reader.close();
        }
        if (inputStream != null)
        {
            try
            {
                inputStream.close();
            }
            catch (IOException ioe)
            {
                // Do nothing
            }
        }
        freed = true;
    }

    /**
     * Returns a Reader object that contains a partial Clob value, starting with the character specified by pos, 
     * which is length characters in length.
     * @param pos the offset to the first byte of the partial value to be retrieved. 
     *     The first byte in the Clob is at position 1
     * @param length the length in bytes of the partial value to be retrieved
     */
    public Reader getCharacterStream(long pos, long length) throws SQLException
    {
        if (freed)
        {
            throw new SQLException("free() has been called");
        }

        // TODO Use pos, length
        if (reader == null)
        {
            this.reader = new StringReader(string);
        }
        return reader;
    }

    public String getSubString(long pos, int length) throws SQLException
    {
        if (freed)
        {
            throw new SQLException("free() has been called");
        }

        if(pos > Integer.MAX_VALUE)
        {
            throw new IllegalArgumentException("Initial position cannot be larger than " + Integer.MAX_VALUE);
        }
        else if((pos + length - 1) > length())
        {
            throw new IndexOutOfBoundsException("The requested substring is greater than the actual length of the Clob String.");
        }
        return string.substring((int)pos -1, (int)pos + length -1);     
    }

    public int setString(long pos, String str) throws SQLException
    {
        if (freed)
        {
            throw new SQLException("free() has been called");
        }

        throw new UnsupportedOperationException();        
    }

    public int setString(long pos, String str, int offset, int len) throws SQLException
    {
        if (freed)
        {
            throw new SQLException("free() has been called");
        }

        throw new UnsupportedOperationException();        
    }

    public long position(String searchstr, long start) throws SQLException
    {
        if (freed)
        {
            throw new SQLException("free() has been called");
        }

        throw new UnsupportedOperationException();        
    }

    public long position(Clob searchstr, long start) throws SQLException
    {
        if (freed)
        {
            throw new SQLException("free() has been called");
        }

        throw new UnsupportedOperationException();        
    }
}