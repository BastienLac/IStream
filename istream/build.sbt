val scala3Version = "3.3.1"
val zioVersion: String = "2.0.20"

lazy val root = project
  .in(file("."))
  .settings(
    name := "Istream",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    libraryDependencies ++= Seq(
      "dev.zio" %% "zio"         % zioVersion,
      "dev.zio" %% "zio-streams" % zioVersion
    ),
    libraryDependencies += "org.scalameta" %% "munit" % "0.7.29" % Test
  )
