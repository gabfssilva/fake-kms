name := "fake-kms"

version := "0.1"

scalaVersion := "2.12.8"

scalacOptions ++= Seq(
  "-Ypartial-unification",
  "-Xfatal-warnings",
  "-unchecked",
  "-deprecation",
  "-explaintypes",
  "-feature",
  "-language:postfixOps"
)

assemblyJarName in assembly := "fake-kms-fat.jar"
test in assembly := {}

val akkaHttp = "10.1.7"
val akka = "2.5.19"
val heikoseebergerAkkaHttpCirce = "1.24.3"
val enumeratum = "1.5.12"
val awsSdk = "2.4.7"
val circe = "0.11.1"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % akkaHttp,
  "com.typesafe.akka" %% "akka-stream" % akka,
  "com.typesafe.akka" %% "akka-slf4j" % akka,

  "de.heikoseeberger" %% "akka-http-circe" % heikoseebergerAkkaHttpCirce,
  
  "com.beachape" %% "enumeratum" % enumeratum,
  
  "ch.qos.logback" % "logback-classic" % "1.2.3",

  "software.amazon.awssdk" % "kms" % awsSdk excludeAll (ExclusionRule(
    organization = "software.amazon.awssdk",
    name = "netty-nio-client"
  ), ExclusionRule(organization = "io.netty")),

  "com.github.matsluni" %% "aws-spi-akka-http" % "0.0.4",

  "io.circe" %% "circe-core" % circe,

  "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  "com.typesafe.akka" %% "akka-http-testkit" % akkaHttp % "test",
  "com.typesafe.akka" %% "akka-testkit" % akka % "test",
  "com.typesafe.akka" %% "akka-stream-testkit" % akka % "test"
)
