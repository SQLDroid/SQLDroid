require 'fileutils'
require File.expand_path 'lib/sqldroid/version', File.dirname(__FILE__)
require 'rake/clean'

unless ENV['ANDROID_HOME'] && Dir.exist?(ENV['ANDROID_HOME'])
  dx_location = `which dx`
  unless $? == 0
    raise 'Unable to find ANDROID_HOME environment variable or the "dx" command.'
  end
  ENV['ANDROID_HOME'] = File.dirname(File.dirname(File.dirname(dx_location)))
end

TARGET_DIR       = File.expand_path 'target'
JAR              = "sqldroid-#{SQLDroid::MAVEN_VERSION}.jar"
JAR_IN_TARGET    = "#{TARGET_DIR}/#{JAR}"
LIB_DIR          = File.expand_path 'lib/sqldroid'
JAR_IN_GEM       = "#{LIB_DIR}/#{JAR}"
GEM_BASE_FILE    = "sqldroid-#{SQLDroid::VERSION}-java.gem"
GEM_FILE_TARGET  = "#{TARGET_DIR}/#{GEM_BASE_FILE}"
JAVA_SRC_FILES   = Dir[File.expand_path 'src/main/java/**/*.java']

CLEAN.include('target')
CLOBBER.include('target', 'lib/sqldroid/sqldroid-*.jar')

desc 'Generate the binary and source jars'
task :jar => JAR_IN_TARGET

file JAR_IN_TARGET => JAVA_SRC_FILES do
  sh 'mvn install -B'
end

file JAR_IN_GEM => JAR_IN_TARGET do
  FileUtils.rm_rf Dir["#{LIB_DIR}/sqldroid-*.jar"]
  FileUtils.cp JAR_IN_TARGET, JAR_IN_GEM
end

desc 'Create a RubyGem for SQLDroid'
task :gem => GEM_FILE_TARGET
  
file GEM_FILE_TARGET => JAR_IN_GEM do
  sh 'gem build sqldroid.gemspec'
  FileUtils.mv GEM_BASE_FILE, GEM_FILE_TARGET
end

desc 'Tag the project and push the tag to GitHub'
task :tag do
  output = `git status --porcelain`
  raise "Workspace not clean!\n#{output}" unless output.empty?
  sh "git tag #{SQLDroid::VERSION}"
  sh 'git push --tags'
end

desc 'Release SQLDroid as a Ruby gem to rubygems.org'
task :release => [:tag, GEM_FILE_TARGET] do
  sh "gem push #{GEM_FILE_TARGET}"
end
