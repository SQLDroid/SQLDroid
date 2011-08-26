require 'fileutils'
require 'lib/sqldroid/version'

ANDROID_SDK_HOME = File.dirname(File.dirname(`which dx`))
PKG_DIR          = File.expand_path "pkg"
JAR              = "sqldroid-#{Sqldroid::VERSION}.jar"
JAR_IN_PKG       = "#{PKG_DIR}/#{JAR}"
LIB_DIR          = File.expand_path 'lib/sqldroid'
JAR_IN_GEM       = "#{LIB_DIR}/#{JAR}"
GEM_FILE         = "sqldroid-#{Sqldroid::VERSION}.gem"
GEM_FILE_PKG     = "#{PKG_DIR}/#{GEM_FILE}"
JAVA_SRC_FILES   = Dir[File.expand_path 'src/**/*.java']


desc 'Generate the binary and source jars'
task :jar => JAR_IN_PKG

file JAR_IN_PKG => JAVA_SRC_FILES do
  sh "javac -cp #{ANDROID_SDK_HOME}/platforms/android-11/android.jar -d bin -sourcepath src src/*/*/*.java"
  Dir.chdir 'bin' do
    sh "jar cf #{PKG_DIR}/#{JAR} org"
  end
end

file JAR_IN_GEM => JAR_IN_PKG do
  FileUtils.rm_rf Dir["#{LIB_DIR}/sqldroid-*.jar"]
  FileUtils.cp JAR_IN_PKG, JAR_IN_GEM
end

task :gem => GEM_FILE_PKG
  
file GEM_FILE_PKG => JAR_IN_GEM do
  sh 'gem build sqldroid.gemspec'
  FileUtils.mv GEM_FILE, GEM_FILE_PKG
end

task :release => GEM_FILE_PKG do
  sh "gem push #{GEM_FILE_PKG}"
end
