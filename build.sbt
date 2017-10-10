organization := "com.example"
name := "Marvel Characters"
version := "0.1.0-SNAPSHOT"
scalaVersion := "2.12.2"

val ScalatraVersion = "2.5.+"

libraryDependencies ++= Seq(
  "org.json4s"              %% "json4s-native"     % "3.5.2",
  "org.scalatra"            %% "scalatra-swagger"  % ScalatraVersion,
  "org.scalatra"            %% "scalatra"          % ScalatraVersion,
  "org.scalatra"            %% "scalatra-scalate"  % ScalatraVersion,
  "org.eclipse.jetty"       %  "jetty-webapp"      % "9.4.6.v20170531"  % "provided",
  "javax.servlet"           %  "javax.servlet-api" % "3.1.0"            % "provided"
)

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % "test"
libraryDependencies += "io.spray" %%  "spray-json" % "1.3.3"
libraryDependencies +=  "org.scalaj" %% "scalaj-http" % "2.3.0"

enablePlugins(JettyPlugin)
