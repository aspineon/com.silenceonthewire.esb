name := "core"

lazy val core = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

libraryDependencies += guice

libraryDependencies += jdbc
libraryDependencies += evolutions

libraryDependencies += "com.h2database" % "h2" % "1.4.196"