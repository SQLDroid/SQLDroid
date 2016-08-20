module SQLDroid
  pom = File.read(File.expand_path('../../pom.xml', File.dirname(__FILE__)))
  MAVEN_VERSION = pom[%r{(?<=<version>)([0-9a-zA-Z.-]*)(?=</version>)}]
  VERSION = MAVEN_VERSION.gsub('-SNAPSHOT', '.alpha')
end
