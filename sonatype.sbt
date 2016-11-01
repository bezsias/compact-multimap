// Your profile name of the sonatype account. The default is the same with the organization value
sonatypeProfileName := "bezsias"

// To sync with Maven central, you need to supply the following information:
pomExtra in Global := {
  <url>https://github.com/bezsias/compact-multimap</url>
  <licenses>
    <license>
      <name>MIT</name>
      <url>http://opensource.org/licenses/MIT</url>
    </license>
  </licenses>
  <scm>
    <connection>scm:git@github.com:bezsias/compact-multimap.git</connection>
    <developerConnection>scm:git@github.com:bezsias/compact-multimap.git</developerConnection>
    <url>github.com/bezsias/compact-multimap.git</url>
  </scm>
  <developers>
    <developer>
      <id>bezsias</id>
      <name>Bela Ezsias</name>
      <url>https://github.com/bezsias</url>
    </developer>
  </developers>
}
