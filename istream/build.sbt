val scala3Version = "3.3.1"
val zioVersion: String = "2.0.20"
val zioJsonVersion = "0.5.0"
val zioHttpVersion = "3.0.0-RC3"
val scalaCsvVersion = "1.3.10"

lazy val root = project
  .in(file("."))
  .settings(
    name := "Istream",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    libraryDependencies ++= Seq(
      "dev.zio" %% "zio"         % zioVersion,
      "dev.zio" %% "zio-streams" % zioVersion,
      "dev.zio" %% "zio-json" % zioJsonVersion,
      "dev.zio" %% "zio-http" % zioHttpVersion,
      "com.github.tototoshi" %% "scala-csv" % scalaCsvVersion
    ),
    libraryDependencies += "org.scalameta" %% "munit" % "0.7.29" % Test
  )
