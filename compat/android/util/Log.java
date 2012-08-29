/*
 * Created on May 9, 2012
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package android.util;

import org.apache.log4j.Logger;

public class Log {

  public static void i(String string, String sql) {
    Logger.getLogger(Log.class).info(string + ":" + sql);    
  }

  public static void v(String string, String sql) {
    Logger.getLogger(Log.class).debug(string + ":" + sql);    
  }

  public static void e(String string, String sql) {
    Logger.getLogger(Log.class).error(string + ":" + sql);    
  }

  public static void d(String string, String sql) {
    Logger.getLogger(Log.class).debug(string + ":" + sql);    
  }

  public static void e(String string, String string2, Exception e) {
    Logger.getLogger(Log.class).error(string + ":" + string2, e);
  }

}
