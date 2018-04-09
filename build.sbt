organization in ThisBuild := "com.example"
version in ThisBuild := "1.1-SNAPSHOT"

// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.12.4"

lazy val `company` = (project in file("."))
  .aggregate(`exception-api`, `type-api`, `type-impl`, `company-api`, `company-impl`)

lazy val `type-api` = (project in file("type-api"))
  .settings(common: _*)
  .settings(
    libraryDependencies ++= Seq(
      lagomJavadslApi,
      lombok
    )
  ).dependsOn(`exception-api`)

lazy val `type-impl` = (project in file("type-impl"))
  .enablePlugins(LagomJava)
  .settings(common: _*)
  .settings(
    libraryDependencies ++= Seq(
      lagomJavadslPersistenceCassandra,
      lagomJavadslKafkaBroker,
      lagomLogback,
      lagomJavadslTestKit,
      lombok
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(`type-api`)

lazy val `company-api` = (project in file("company-api"))
  .settings(common: _*)
  .settings(
    libraryDependencies ++= Seq(
      lagomJavadslApi,
      lombok
    )
  ).dependsOn(`exception-api`)

lazy val `exception-api` = (project in file("exception-api"))
  .settings(common: _*)
  .settings(
    libraryDependencies ++= Seq(
      lagomJavadslApi,
      lombok
    )
  )

lazy val `company-impl` = (project in file("company-impl"))
  .enablePlugins(LagomJava)
  .settings(common: _*)
  .settings(
    libraryDependencies ++= Seq(
      lagomJavadslPersistenceCassandra,
      lagomJavadslKafkaBroker,
      lagomLogback,
      lagomJavadslTestKit,
      lombok
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(`company-api`)

val lombok = "org.projectlombok" % "lombok" % "1.16.18"

def common = Seq(
  javacOptions in compile += "-parameters"
)
