val akkaVersion = "2.5.18"
val akkaHttpVersion = "10.1.5"

lazy val root = (project in file("."))
  .settings(
    name := "akka-http-json-validation",
    organization := "com.github.uryyyyyyy",
    version := "0.1.0",
    licenses := Seq("MIT" -> url("http://opensource.org/licenses/MIT")),
    homepage := Some(url("https://github.com/uryyyyyyy/akka-http-json-validation")),
    scmInfo := Some(
      ScmInfo(
        url("https://github.com/uryyyyyyy/akka-http-json-validation"),
        "scm:git@github.com:uryyyyyyy/akka-http-json-validation.git"
      )
    ),
    developers := List(
      Developer(
        id = "uryyyyyyy",
        name = "Koki Shibata",
        email = "koki305@gmail.com",
        url = url("https://github.com/uryyyyyyy")
      )
    ),
    publishMavenStyle := true,
    scalaVersion := "2.12.7",
    crossScalaVersions := Seq("2.11.12", "2.12.7"),
    publishTo := {
      val nexus = "https://oss.sonatype.org/"
      if (isSnapshot.value)
        Some("snapshots" at nexus + "content/repositories/snapshots")
      else
        Some("releases"  at nexus + "service/local/staging/deploy/maven2")
    },
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-stream" % akkaVersion % Provided,
      "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test,
      "org.scalatest" %% "scalatest" % "3.0.5" % Test
    )
    //publishTo := Some(Resolver.file("file",  new File(s"${System.getProperty("user.home")}/Desktop/")) )
  )
