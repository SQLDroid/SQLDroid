module SQLDroid
  pom = File.read(File.expand_path('../../pom.xml', File.dirname(__FILE__)))
  MAVEN_VERSION = pom[%r{(?<=<version>)([0-9a-zA-Z.-]*)(?=</version>)}]
  VERSION = Gem::Version.new(MAVEN_VERSION.gsub('-SNAPSHOT', '')).send(:_segments).join('.')
end
