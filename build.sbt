import ReleaseTransformations._

organization := "com.github.bezsias"

name := "compact-multimap"

scalaVersion in ThisBuild := "2.11.12"

scalacOptions += "-target:jvm-1.8"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.0" % "test"

crossScalaVersions in ThisBuild := Seq(scalaVersion.value, "2.12.4")

releaseCrossBuild := true

releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  ReleaseStep(action = Command.process("publishSigned", _), enableCrossBuild = true),
  setNextVersion,
  commitNextVersion,
  ReleaseStep(action = Command.process("sonatypeRelease", _), enableCrossBuild = true),
  pushChanges
)
