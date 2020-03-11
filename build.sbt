/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import Dependencies._

organization := "com.chrism"
name := "commons4s"

version := "0.0.5"

scalaVersion := "2.12.10"

licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0"))

homepage := Some(url("https://github.com/chrismin1202/commons4s"))
scmInfo := Some(ScmInfo(url("https://github.com/chrismin1202/commons4s"), "git@github.com:chrismin1202/commons4s.git"))
developers := List(
  Developer("chrismin1202", "Donatello", "chrism.1202@gmail.com", url("https://github.com/chrismin1202")),
)

credentials += Credentials(Path.userHome / ".sbt" / ".credentials")

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

publishMavenStyle := true
publishArtifact in Test := true
updateOptions := updateOptions.value.withGigahorse(false)
publishTo := Some(
  "chrism commons4s GitHub Package Registry" at "https://maven.pkg.github.com/chrismin1202/commons4s"
)
