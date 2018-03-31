name := """com.silenceonthewire.esb"""

version := "1.0.1-SNAPSHOT"

lazy val core = (project in file("modules/core")).enablePlugins(PlayJava, PlayEbean)
lazy val users = (project in file("modules/users")).enablePlugins(PlayJava, PlayEbean)
lazy val root = (project in file(".")).enablePlugins(PlayJava).dependsOn(core, users).aggregate(core, users)

scalaVersion := "2.12.4"

crossScalaVersions := Seq("2.11.12", "2.12.4")

libraryDependencies += guice

// Test Database
libraryDependencies += "com.h2database" % "h2" % "1.4.196"

libraryDependencies += javaJdbc

libraryDependencies += ws
libraryDependencies += ehcache

libraryDependencies += jdbc
libraryDependencies += evolutions

// Testing libraries for dealing with CompletionStage...
libraryDependencies += "org.assertj" % "assertj-core" % "3.6.2" % Test
libraryDependencies += "org.awaitility" % "awaitility" % "2.0.0" % Test

// Make verbose tests
testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v"))
