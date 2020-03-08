object Dependencies {

  import sbt._

  private val JacksonBaseGroupId: String = "com.fasterxml.jackson"
  private val JacksonCoreGroupId: String = s"$JacksonBaseGroupId.core"
  private val JacksonDataTypeGroupId: String = s"$JacksonBaseGroupId.datatype"
  private val JacksonModuleGroupId: String = s"$JacksonBaseGroupId.module"

  // Note that is project is used in Spark applications; therefore,
  // the versions of some of these dependencies are set to match those of Spark dependencies.
  private val CommonsLang3Version: String = "3.5"
  private val CommonsIoVersion: String = "2.6"
  private val JacksonCoreVersion: String = "2.6.7"
  private val JacksonAnnotationsVersion: String = "2.6.0"
  private val JacksonDatabindVersion: String = s"$JacksonCoreVersion.1"
  private val JacksonDataTypeJdk8Version: String = JacksonCoreVersion
  private val JacksonDataTypeJsr310Version: String = JacksonCoreVersion
  private val JacksonModuleVersion: String = s"$JacksonCoreVersion.1"
  private val Json4sVersion: String = "3.5.3"
  private val PlayVersion: String = "2.7.3"
  private val ScalacheckVersion: String = "1.14.0"
  private val ScalatestVersion: String = "3.0.8"
  private val Specs2CoreVersion: String = "4.7.0"

  val CommonsLang3: ModuleID = "org.apache.commons" % "commons-lang3" % CommonsLang3Version
  val CommonsIo: ModuleID = "commons-io" % "commons-io" % CommonsIoVersion

  val JacksonCore: ModuleID = JacksonCoreGroupId % "jackson-core" % JacksonCoreVersion
  val JacksonAnnotations: ModuleID = JacksonCoreGroupId % "jackson-annotations" % JacksonAnnotationsVersion
  val JacksonDataTypeJdk8: ModuleID = JacksonDataTypeGroupId % "jackson-datatype-jdk8" % JacksonDataTypeJdk8Version

  val JacksonDataTypeJsr310: ModuleID =
    JacksonDataTypeGroupId % "jackson-datatype-jsr310" % JacksonDataTypeJsr310Version
  val JacksonDatabind: ModuleID = JacksonCoreGroupId % "jackson-databind" % JacksonDatabindVersion
  val JacksonModuleScala: ModuleID = JacksonModuleGroupId %% "jackson-module-scala" % JacksonModuleVersion

  val Json4sNative: ModuleID = "org.json4s" %% "json4s-native" % Json4sVersion
  val Json4sExt: ModuleID = "org.json4s" %% "json4s-ext" % Json4sVersion
  val Json4sJackson: ModuleID = "org.json4s" %% "json4s-jackson" % Json4sVersion

  val PlayJson: ModuleID = ("com.typesafe.play" %% "play-json" % PlayVersion)
    .exclude("com.fasterxml.jackson.core", "jackson-core")
    .exclude("com.fasterxml.jackson.core", "jackson-annotations")
    .exclude("com.fasterxml.jackson.core", "jackson-databind")
    .exclude("com.fasterxml.jackson.core", "jackson-jackson-datatype-jdk8")
    .exclude("com.fasterxml.jackson.core", "jackson-datatype-jsr310")

  val ScalaXml: ModuleID = "org.scala-lang.modules" %% "scala-xml" % "1.2.0"
  val SaajImpl: ModuleID = "com.sun.xml.messaging.saaj" % "saaj-impl" % "1.5.1"

  val Scalacheck: ModuleID = "org.scalacheck" %% "scalacheck" % ScalacheckVersion
  val Scalatest: ModuleID = "org.scalatest" %% "scalatest" % ScalatestVersion
  val Specs2Core: ModuleID = "org.specs2" %% "specs2-core" % Specs2CoreVersion
}
