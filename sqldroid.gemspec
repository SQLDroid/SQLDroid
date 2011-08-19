# -*- encoding: utf-8 -*-
$:.push File.expand_path("../lib", __FILE__)
require "sqldroid/version"

Gem::Specification.new do |s|
  s.name        = "sqldroid"
  s.version     = Sqldroid::VERSION
  s.authors     = ["donv"]
  s.email       = ["uwe@kubosch.no"]
  s.homepage    = "http://sqldroid.org/"
  s.summary     = %q{SQLDroid is a JDBC driver for Android's sqlite database (android.database.sqlite.SQLiteDatabase).}
  s.description = %q{SQLDroid is a JDBC driver for Android's sqlite database (android.database.sqlite.SQLiteDatabase).}

  # s.rubyforge_project = "sqldroid"

  s.files         = Dir['lib/**/*']
  s.test_files    = []
  s.executables   = []
  s.require_paths = ["lib"]
  s.platform      = 'android' # Should be 'android'
end
