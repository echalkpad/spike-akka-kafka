name := "kafka-akka"

version := "1.0"

scalaVersion := "2.10.4"

resolvers ++= Seq(
  "Local Maven Repository" at "file://" + Path.userHome.absolutePath + "/.m2/repository",
  "Sonatype Releases" at "http://oss.sonatype.org/content/repositories/releases",
  "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
  "Typesafe snapshots" at "http://repo.typesafe.com/typesafe/snapshots/",
  "Hotels.com releases" at "http://bin-uk.hotels.com/artifactory/libs-release",
  "Hotels.com snapshots" at "http://bin-uk.hotels.com/artifactory/libs-snapshot"
)

libraryDependencies ++= Seq(
	"org.slf4j" % "slf4j-api" % "1.7.7",
	"org.slf4j" % "log4j-over-slf4j" % "1.7.7",
  "com.typesafe.akka" %% "akka-slf4j" % "2.3.2",
	"ch.qos.logback" % "logback-classic" % "1.1.2" % "runtime",
	"ch.qos.logback" % "logback-core" % "1.1.2" % "runtime"
)


libraryDependencies ++= Seq(
	"org.apache.kafka" %% "kafka" % "0.8.1.1"
		exclude("log4j", "log4j"),
	"com.google.protobuf" % "protobuf-java" % "2.5.0",
	"com.typesafe.akka" %% "akka-actor" % "2.3.2",
	"org.mockito" % "mockito-all" % "1.9.5"
)

libraryDependencies ++= Seq(
	"com.typesafe.akka" %% "akka-testkit" % "2.3.2" % "test",
	"org.scalacheck" %% "scalacheck" % "1.11.3" % "test",
	"org.specs2" %% "specs2" % "1.14" % "test",
	"org.mockito" % "mockito-all" % "1.9.5" % "test"
)