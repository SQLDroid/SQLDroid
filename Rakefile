require 'fileutils'
require 'lib/sqldroid/version'

JAR = "sqldroid-#{Sqldroid::VERSION}.jar"
GEM_FILE = "sqldroid-#{Sqldroid::VERSION}-unknown.gem" # Platform should be 'android'
GEM_FILE_PKG = "pkg/sqldroid-#{Sqldroid::VERSION}-android.gem" # Platform should be 'android'

file JAR do
  sh 'javac -d bin -sourcepath src'
end

task :gem do
  sh 'gem build sqldroid.gemspec'
  FileUtils.mv GEM_FILE, GEM_FILE_PKG
end

task :release do
  sh "gem push #{GEM_FILE_PKG}"
end
