# -*- encoding: utf-8 -*-
$:.unshift File.expand_path('lib', File.dirname(__FILE__))
require 'sqldroid/version'

Gem::Specification.new do |s|
  s.name        = 'sqldroid'
  s.version     = SQLDroid::VERSION
  s.authors     = %w(donv)
  s.email       = %w(uwe@kubosch.no)
  s.homepage    = 'http://sqldroid.org/'
  s.summary     = %q{SQLDroid is a JDBC driver for Android's sqlite database (android.database.sqlite.SQLiteDatabase).}
  s.description = %q{SQLDroid is a JDBC driver for Android's sqlite database (android.database.sqlite.SQLiteDatabase).
This gem makes the JAR available to your JRuby app.}

  # s.rubyforge_project = "sqldroid"

  s.files         = Dir['lib/**/*']
  s.test_files    = []
  s.executables   = []
  s.require_paths = %w(lib)
  s.platform = 'java'
end
