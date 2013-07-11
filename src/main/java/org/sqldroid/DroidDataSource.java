package org.sqldroid;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.sql.DataSource;

public class DroidDataSource implements DataSource {
    Connection connection = null;    
    protected String description = "Android Sqlite Data Source";
    protected String packageName;
    protected String databaseName;  

    public DroidDataSource() {

    }        
        
    public DroidDataSource(String packageName, String databaseName) {
      	setPackageName(packageName);
       	setDatabaseName(databaseName);
    }
        
    @Override
    public Connection getConnection() throws SQLException {
      	String url = "jdbc:sqldroid:" + "/data/data/" + packageName + "/" + databaseName + ".db";
        connection = new org.sqldroid.SQLDroidDriver().connect(url , new Properties());
        return connection;
    }

    @Override
    public Connection getConnection(String username, String password)
    			throws SQLException {
        return getConnection();
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        PrintWriter logWriter = null;
        try {
            logWriter = new PrintWriter("droid.log");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return logWriter;
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        try {
            DriverManager.setLogWriter(new PrintWriter("droid.log"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
    }

    public String getDescription() {
      	return description;
    }

    public void setDescription(String desc) {
        description = desc;
    }

    public String getPackageName() {
     	return packageName;
    }

    public void setPackageName(String packageName) {
     	this.packageName = packageName;
    }                

    public String getDatabaseName() {
    	return databaseName;
    }

    public void setDatabaseName(String databaseName) {
     	this.databaseName = databaseName;
    }             
        
    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        throw new UnsupportedOperationException("isWrapperfor");
    }

    @Override
    public  <T> T unwrap(Class<T> iface) throws SQLException {
        throw new UnsupportedOperationException("unwrap");
    }
        
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
      	return null;
    }
        
}
