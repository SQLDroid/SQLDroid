require 'fileutils'
require File.expand_path 'lib/sqldroid/version', File.dirname(__FILE__)
require 'rake/clean'

if ENV['ANDROID_HOME']
  ANDROID_SDK_HOME = ENV['ANDROID_HOME']
else
  dx_location = `which dx`
  raise 'Unable to find ANDROID_HOME environment variable or the "dx" command.' unless $? == 0
  ANDROID_SDK_HOME = File.dirname(File.dirname(dx_location))
end
PKG_DIR          = File.expand_path 'pkg'
JAR              = "sqldroid-#{SQLDroid::VERSION}.jar"
JAR_IN_PKG       = "#{PKG_DIR}/#{JAR}"
LIB_DIR          = File.expand_path 'lib/sqldroid'
JAR_IN_GEM       = "#{LIB_DIR}/#{JAR}"
GEM_FILE         = "sqldroid-#{SQLDroid::VERSION}-java.gem"
GEM_FILE_PKG     = "#{PKG_DIR}/#{GEM_FILE}"
JAVA_SRC_FILES   = Dir[File.expand_path 'src/**/*.java']
ANDROID_TARGET   = File.read('project.properties').slice(/^target=.*$/)[7..-1]

CLEAN.include('pkg')
CLOBBER.include('pkg')

desc 'Generate the binary and source jars'
task :jar => JAR_IN_PKG

file JAR_IN_PKG => JAVA_SRC_FILES do
  FileUtils.mkdir_p 'bin'
  jar = "#{ANDROID_SDK_HOME}/platforms/#{ANDROID_TARGET}/android.jar"
  raise "Expected '#{jar}' file missing." unless File.exists?(jar)
  sh "javac -source 1.6 -target 1.6 -bootclasspath #{jar} -d bin -sourcepath src src/*/*/*.java"
  FileUtils.mkdir_p PKG_DIR
  Dir.chdir 'bin' do
    sh "jar cf #{PKG_DIR}/#{JAR} org"
  end
end

file JAR_IN_GEM => JAR_IN_PKG do
  FileUtils.rm_rf Dir["#{LIB_DIR}/sqldroid-*.jar"]
  FileUtils.cp JAR_IN_PKG, JAR_IN_GEM
end

desc 'Create a RubyGem for SQLDroid'
task :gem => GEM_FILE_PKG
  
file GEM_FILE_PKG => JAR_IN_GEM do
  sh 'gem build sqldroid.gemspec'
  FileUtils.mv GEM_FILE, GEM_FILE_PKG
end

desc 'Tag the project and push the tag to GitHub'
task :tag do
  output = `git status --porcelain`
  raise "Workspace not clean!\n#{output}" unless output.empty?
  sh "git tag #{SQLDroid::VERSION}"
  sh 'git push --tags'
end

desc 'Release SQLDroid as a Ruby gem to rubygems.org'
task :release => [:tag, GEM_FILE_PKG] do
  sh "gem push #{GEM_FILE_PKG}"
end
