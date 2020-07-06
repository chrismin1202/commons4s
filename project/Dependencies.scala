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
object Dependencies {

  import sbt._

  private val CommonsLang3Version: String = "3.5"
  private val CommonsIoVersion: String = "2.6"
  private val ScalacheckVersion: String = "1.14.0"
  private val ScalatestVersion: String = "3.0.8"
  private val Specs2CoreVersion: String = "4.7.0"

  val CommonsLang3: ModuleID = "org.apache.commons" % "commons-lang3" % CommonsLang3Version
  val CommonsIo: ModuleID = "commons-io" % "commons-io" % CommonsIoVersion

  val Log4j: ModuleID = "log4j" % "log4j" % "1.2.17"
  val LogbackClassic: ModuleID = "ch.qos.logback" % "logback-classic" % "1.2.3"
  val Log4s: ModuleID = "org.log4s" %% "log4s" % "1.8.2"

  val ScalaXml: ModuleID = "org.scala-lang.modules" %% "scala-xml" % "1.2.0"
  val SaajImpl: ModuleID = "com.sun.xml.messaging.saaj" % "saaj-impl" % "1.5.1"

  val Scalacheck: ModuleID = "org.scalacheck" %% "scalacheck" % ScalacheckVersion
  val Scalatest: ModuleID = "org.scalatest" %% "scalatest" % ScalatestVersion
  val Specs2Core: ModuleID = "org.specs2" %% "specs2-core" % Specs2CoreVersion
}
