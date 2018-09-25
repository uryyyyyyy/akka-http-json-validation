lazy val root = (project in file("."))
  .settings(
    name := "kamon-stackdriver-sample",
    scalaVersion := "2.12.6",
    libraryDependencies ++= Seq(
      "com.github.uryyyyyyy" %% "akka-http-json-validation" % "0.0.1-SNAPSHOT",
      "ch.qos.logback" % "logback-classic" % "1.2.3"
    ),
    resolvers += "Local Maven Repository" at s"file:/${System.getProperty("user.home")}/Desktop/"
  ).enablePlugins(JavaAppPackaging)
