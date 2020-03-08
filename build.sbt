import Dependencies._

organization := "com.chrism"
name := "commons4s"

version := "0.0.1"

scalaVersion := "2.12.10"

parallelExecution in Test := false

fork in Test := true

javaOptions in Test += "-Djdk.logging.allowStackWalkSearch=true"

connectInput in Test := true

libraryDependencies ++= Seq(
  CommonsLang3,
  CommonsIo,
  JacksonCore,
  JacksonAnnotations,
  JacksonDataTypeJdk8,
  JacksonDataTypeJsr310,
  JacksonDatabind,
  JacksonModuleScala,
  Json4sNative,
  Json4sExt,
  Json4sJackson,
  PlayJson,
  ScalaXml,
  SaajImpl % Test,
  Scalacheck % Test,
  Scalatest % Test,
  Specs2Core % Test,
)

val meta = "META.INF(.)*".r
assemblyMergeStrategy in assembly := {
  case PathList("javax", "servlet", _ @_*)           => MergeStrategy.first
  case PathList(ps @ _*) if ps.last endsWith ".html" => MergeStrategy.first
  case n if n.startsWith("reference.conf")           => MergeStrategy.concat
  case n if n.endsWith(".conf")                      => MergeStrategy.concat
  case meta(_)                                       => MergeStrategy.discard
  case _                                             => MergeStrategy.first
}

homepage := Some(url("https://github.com/chrismin1202/commons4s"))
scmInfo := Some(ScmInfo(url("https://github.com/chrismin1202/commons4s"), "git@github.com:chrismin1202/commons4s.git"))
developers := List(
  Developer("chrismin1202", "Donatello", "chrism.1202@gmail.com", url("https://github.com/chrismin1202")),
)
licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0"))
publishMavenStyle := true
publishArtifact in Test := true
updateOptions := updateOptions.value.withGigahorse(false)
credentials += Credentials(Path.userHome / ".sbt" / ".credentials")
publishTo := Some(
  "chrism commons4s GitHub Package Registry" at "https://maven.pkg.github.com/chrismin1202/commons4s"
)
