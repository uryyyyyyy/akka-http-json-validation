val akkaVersion = "2.5.18"

lazy val root = (project in file("."))
  .settings(
    name := "kamon-stackdriver-sample",
    scalaVersion := "2.12.7",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-stream" % akkaVersion,
      "com.github.uryyyyyyy" %% "akka-http-json-validation" % "0.2.0",
      "ch.qos.logback" % "logback-classic" % "1.2.3"
    )
    //resolvers += "Local Maven Repository" at s"file:/${System.getProperty("user.home")}/Desktop/"
  ).enablePlugins(JavaAppPackaging)
