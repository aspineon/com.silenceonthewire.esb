name := "users"

lazy val core = (project in file("../core")).enablePlugins(PlayJava, PlayEbean)

lazy val users = (project in file(".")).enablePlugins(PlayJava, PlayEbean).dependsOn(core)

libraryDependencies += guice

libraryDependencies += jdbc
libraryDependencies += evolutions

libraryDependencies += "com.h2database" % "h2" % "1.4.196"

libraryDependencies += "javax.el" % "javax.el-api" % "3.0.0"
libraryDependencies += "org.glassfish.web" % "javax.el" % "2.2.6"