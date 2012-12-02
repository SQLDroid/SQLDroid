# SQLDroid

SQLDroid is a JDBC driver for Android's sqlite database (android.database.sqlite.SQLiteDatabase) originally conceived by Kristian Lein-Mathisen. See http://sqldroid.org/.

SQLDroid lets you access your app's database through JDBC. Android ships with the necessary interfaces needed to use JDBC drivers, but it does not officially ship with a driver for its built-in SQLite database engine.  When porting code from other projects, you can conveniently replace the JDBC url to jdbc:sqlite to access an SQLite database on Android.

The SQLDroid JAR with the JDBC driver for Android is 33KB.  We also offer a RubyGem "sqldroid" for use with [Ruboto](http://ruboto.org/).

## Community

* Project site: https://github.com/SQLDroid/SQLDroid
* Mailing list: http://groups.google.com/group/sqldroid
* Wiki: https://github.com/SQLDroid/SQLDroid/wiki
* Old project site: http://code.google.com/p/sqldroid

## Download

A prebuilt JAR is available at

https://github.com/SQLDroid/SQLDroid/downloads

## Usage

```
// Insert example here
```

You can find an example of how to use SQLDroid with ActiveRecord on Ruboto here:

https://github.com/ruboto/ruboto/wiki/Tutorial%3A-Using-an-SQLite-database-with-ActiveRecord

## Building

The SQLDroid JAR file is a straight collection of the compiled classes.  If you have Ruby installed,
you can generate the JAR using

```rake jar```

To make a gem for use with Ruboto run

```rake gem```

To release the gem to rubygems.org (requires permissions on rubygems.org) run

```rake release```
