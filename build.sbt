import ReleaseTransformations._

organization := "com.github.bezsias"

name := "compact-multimap"

scalaVersion in ThisBuild := "2.11.12"

scalacOptions += "-target:jvm-1.8"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.0" % "test"

releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  ReleaseStep(action = Command.process("publishSigned", _)),
  setNextVersion,
  commitNextVersion,
  ReleaseStep(action = Command.process("sonatypeRelease", _)),
  pushChanges
)
