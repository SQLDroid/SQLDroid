PROJECT_DIR = File.expand_path('..', File.dirname(__FILE__)) unless defined? PROJECT_DIR
DATA_DIR = $activity.files_dir.path
DB_DIR = "#{DATA_DIR}/sqlite-test-125"
url = "jdbc:sqldroid:#{DB_DIR}"

java_import 'android.database.sqlite.SQLiteDatabase'
java_import 'java.sql.Types'
java_import 'org.sqldroid.SQLDroidConnection'

sqlitedb = SQLiteDatabase.openDatabase(
    DB_DIR, nil, SQLiteDatabase::CREATE_IF_NECESSARY | SQLiteDatabase::OPEN_READWRITE
)

def list_tables(sqlitedb)
  cursor = sqlitedb.rawQuery %q{SELECT name FROM sqlite_master WHERE type='table' ORDER BY name}, [].to_java(:string)
  while cursor.moveToNext
    print ' | '
    (0..(cursor.getColumnCount - 1)).each do |i|
      print " #{i} "
      print cursor.getString(i)
      print ' | '
    end
    puts
  end
  cursor.close
end
list_tables sqlitedb
sqlitedb.execSQL 'CREATE TABLE schema_migrations (sdversion varchar(255) NOT NULL)', [].to_java(:string)
list_tables sqlitedb
sqlitedb.close
