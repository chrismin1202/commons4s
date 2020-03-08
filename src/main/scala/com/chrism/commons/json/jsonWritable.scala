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
package com.chrism.commons.json

import java.nio.{file => jnf}
import java.{io => jio}

import com.chrism.commons.json.json4s.JsonLetterCase
import org.json4s.{Formats, JNull, JValue, JsonInput}
import play.api.libs.json.JsValue

sealed trait JsonWritable[+A] {

  /** Specifies the case used in the class that extends this trait.
    *
    * @return the [[JsonLetterCase]] used in the class
    */
  protected def classLetterCase: JsonLetterCase

  /** Override if JSON serialization needs to be customized.
    *
    * @param formats the [[Formats]] to used for serializing
    * @param m the [[Manifest]] of the object being serialized
    * @tparam B the type parameter that has [[A]] as a lower type bound
    * @return the [[JValue]] converted from the object
    */
  def toJValue[B >: A](implicit formats: Formats, m: Manifest[B]): JValue = JsonUtils.decomposeToJValue(this)

  final def toJValueWithLetterCase[B >: A](
    jsonCase: JsonLetterCase
  )(
    implicit
    formats: Formats,
    m: Manifest[B]
  ): JValue = {
    val jv = toJValue[B]
    if (jsonCase != classLetterCase) jsonCase.format(jv) else jv
  }

  final def toCamelCasedJValue[B >: A](implicit formats: Formats, m: Manifest[B]): JValue =
    toJValueWithLetterCase[B](JsonLetterCase.Camel)(formats, m)

  final def toSnakeCasedJValue[B >: A](implicit formats: Formats, m: Manifest[B]): JValue =
    toJValueWithLetterCase[B](JsonLetterCase.Snake)(formats, m)

  final def toPascalCasedJValue[B >: A](implicit formats: Formats, m: Manifest[B]): JValue =
    toJValueWithLetterCase[B](JsonLetterCase.Pascal)(formats, m)

  final def toJsValue[B >: A](implicit formats: Formats, m: Manifest[B]): JsValue =
    JsonUtils.convertToJsValue(toJValue[B])

  final def toJsValueWithLetterCase[B >: A](
    jsonCase: JsonLetterCase
  )(
    implicit
    formats: Formats,
    m: Manifest[B]
  ): JsValue =
    JsonUtils.convertToJsValue(toJValueWithLetterCase[B](jsonCase))

  final def toCamelCasedJsValue[B >: A](implicit formats: Formats, m: Manifest[B]): JsValue =
    toJsValueWithLetterCase[B](JsonLetterCase.Camel)(formats, m)

  final def toSnakeCasedJsValue[B >: A](implicit formats: Formats, m: Manifest[B]): JsValue =
    toJsValueWithLetterCase[B](JsonLetterCase.Snake)(formats, m)

  final def toPascalCasedJsValue[B >: A](implicit formats: Formats, m: Manifest[B]): JsValue =
    toJsValueWithLetterCase[B](JsonLetterCase.Pascal)(formats, m)

  final def toJson[B >: A](implicit formats: Formats, m: Manifest[B]): String = JsonUtils.writeJValueAsJson(toJValue[B])

  final def toJsonWithLetterCase[B >: A](jsonCase: JsonLetterCase)(implicit formats: Formats, m: Manifest[B]): String =
    JsonUtils.writeJValueAsJson(toJValueWithLetterCase[B](jsonCase))

  final def toCamelCasedJson[B >: A](implicit formats: Formats, m: Manifest[B]): String =
    toJsonWithLetterCase[B](JsonLetterCase.Camel)(formats, m)

  final def toSnakeCasedJson[B >: A](implicit formats: Formats, m: Manifest[B]): String =
    toJsonWithLetterCase[B](JsonLetterCase.Snake)(formats, m)

  final def toPascalCasedJson[B >: A](implicit formats: Formats, m: Manifest[B]): String =
    toJsonWithLetterCase[B](JsonLetterCase.Pascal)(formats, m)

  final def toByteArray[B >: A](implicit formats: Formats, m: Manifest[B]): Array[Byte] = toJson[B].getBytes

  final def toByteArrayWithLetterCase[B >: A](
    jsonCase: JsonLetterCase
  )(
    implicit
    formats: Formats,
    m: Manifest[B]
  ): Array[Byte] =
    toJsonWithLetterCase[B](jsonCase).getBytes

  final def toCamelCasedByteArray[B >: A](implicit formats: Formats, m: Manifest[B]): Array[Byte] =
    toByteArrayWithLetterCase[B](JsonLetterCase.Camel)(formats, m)

  final def toSnakeCasedByteArray[B >: A](implicit formats: Formats, m: Manifest[B]): Array[Byte] =
    toByteArrayWithLetterCase[B](JsonLetterCase.Snake)(formats, m)

  final def toPascalCasedByteArray[B >: A](implicit formats: Formats, m: Manifest[B]): Array[Byte] =
    toByteArrayWithLetterCase[B](JsonLetterCase.Pascal)(formats, m)

  final def writeAsFile[B >: A](file: jio.File)(implicit formats: Formats, m: Manifest[B]): jio.File = {
    writeToPath[B](file.toPath)
    file
  }

  final def writeAsFileWithLetterCase[B >: A](
    file: jio.File,
    jsonCase: JsonLetterCase
  )(
    implicit
    formats: Formats,
    m: Manifest[B]
  ): jio.File = {
    writeToPathWithLetterCase[B](file.toPath, jsonCase)
    file
  }

  final def writeAsFileCamelCased[B >: A](file: jio.File)(implicit formats: Formats, m: Manifest[B]): jio.File =
    writeAsFileWithLetterCase[B](file, JsonLetterCase.Camel)(formats, m)

  final def writeAsFileSnakeCased[B >: A](file: jio.File)(implicit formats: Formats, m: Manifest[B]): jio.File =
    writeAsFileWithLetterCase[B](file, JsonLetterCase.Snake)(formats, m)

  final def writeAsFilePascalCased[B >: A](file: jio.File)(implicit formats: Formats, m: Manifest[B]): jio.File =
    writeAsFileWithLetterCase[B](file, JsonLetterCase.Pascal)(formats, m)

  final def writeToPath[B >: A](path: jnf.Path)(implicit formats: Formats, m: Manifest[B]): jnf.Path =
    jnf.Files.write(path, toByteArray[B])

  final def writeToPathWithLetterCase[B >: A](
    path: jnf.Path,
    jsonCase: JsonLetterCase
  )(
    implicit
    formats: Formats,
    m: Manifest[B]
  ): jnf.Path =
    jnf.Files.write(path, toByteArrayWithLetterCase[B](jsonCase))

  final def writeToPathCamelCased[B >: A](path: jnf.Path)(implicit formats: Formats, m: Manifest[B]): jnf.Path =
    writeToPathWithLetterCase[B](path, JsonLetterCase.Camel)(formats, m)

  final def writeToPathSnakeCased[B >: A](path: jnf.Path)(implicit formats: Formats, m: Manifest[B]): jnf.Path =
    writeToPathWithLetterCase[B](path, JsonLetterCase.Snake)(formats, m)

  final def writeToPathPascalCased[B >: A](path: jnf.Path)(implicit formats: Formats, m: Manifest[B]): jnf.Path =
    writeToPathWithLetterCase[B](path, JsonLetterCase.Pascal)(formats, m)
}

/** A trait for serializing as JSON in camelCasing.
  *
  * @tparam A a case class with its members in camelCase
  */
trait CamelCasedJsonWritable[+A] extends JsonWritable[A] {

  override protected final val classLetterCase: JsonLetterCase = JsonLetterCase.Camel
}

/** A trait for serializing as JSON in snake_casing.
  *
  * @tparam A a case class with its members in snake_case
  */

trait SnakeCasedJsonWritable[+A] extends JsonWritable[A] {

  override protected final val classLetterCase: JsonLetterCase = JsonLetterCase.Snake
}

/** A trait for serializing as JSON in PascalCasing.
  *
  * @tparam A a case class with its members in PascalCase
  */
trait PascalCasedJsonWritable[+A] extends JsonWritable[A] {

  override protected final val classLetterCase: JsonLetterCase = JsonLetterCase.Pascal
}

sealed trait JsonWritableCompanionLike[A <: JsonWritable[A]] {

  /** Specifies the case used in [[A]].
    *
    * @return the [[JsonLetterCase]] used in [[A]]
    */
  protected def classLetterCase: JsonLetterCase

  final def fromJValueOrNone(jv: JValue)(implicit formats: Formats, m: Manifest[A]): Option[A] = jv.extractOpt[A]

  final def fromJValueWithLetterCaseOrNone(
    jv: JValue,
    jsonCase: JsonLetterCase
  )(
    implicit
    formats: Formats,
    m: Manifest[A]
  ): Option[A] =
    fromJValueOrNone(if (jsonCase != classLetterCase) classLetterCase.format(jv) else jv)

  final def fromCamelCasedJValueOrNone(jv: JValue)(implicit formats: Formats, m: Manifest[A]): Option[A] =
    fromJValueWithLetterCaseOrNone(jv, JsonLetterCase.Camel)

  final def fromSnakeCasedJValueOrNone(jv: JValue)(implicit formats: Formats, m: Manifest[A]): Option[A] =
    fromJValueWithLetterCaseOrNone(jv, JsonLetterCase.Snake)

  final def fromPascalCasedJValueOrNone(jv: JValue)(implicit formats: Formats, m: Manifest[A]): Option[A] =
    fromJValueWithLetterCaseOrNone(jv, JsonLetterCase.Pascal)

  final def fromJValue(jv: JValue)(implicit formats: Formats, m: Manifest[A]): A =
    jv match {
      case null  => null.asInstanceOf[A]
      case JNull => null.asInstanceOf[A]
      case other => other.extract[A]
    }

  final def fromJValueWithLetterCase(
    jv: JValue,
    jsonCase: JsonLetterCase
  )(
    implicit
    formats: Formats,
    m: Manifest[A]
  ): A =
    fromJValue(if (jsonCase != classLetterCase) classLetterCase.format(jv) else jv)

  final def fromCamelCasedJValue(jv: JValue)(implicit formats: Formats, m: Manifest[A]): A =
    fromJValueWithLetterCase(jv, JsonLetterCase.Camel)

  final def fromSnakeCasedJValue(jv: JValue)(implicit formats: Formats, m: Manifest[A]): A =
    fromJValueWithLetterCase(jv, JsonLetterCase.Snake)

  final def fromPascalCasedJValue(jv: JValue)(implicit formats: Formats, m: Manifest[A]): A =
    fromJValueWithLetterCase(jv, JsonLetterCase.Pascal)

  final def fromJsValueOrNone(jsv: JsValue)(implicit formats: Formats, m: Manifest[A]): Option[A] =
    fromJValueOrNone(JsonUtils.convertToJValue(jsv))

  final def fromJsValueWithLetterCaseOrNone(
    jsv: JsValue,
    jsonCase: JsonLetterCase
  )(
    implicit
    formats: Formats,
    m: Manifest[A]
  ): Option[A] =
    fromJValueWithLetterCaseOrNone(JsonUtils.convertToJValue(jsv), jsonCase)

  final def fromCamelCasedJsValueOrNone(jsv: JsValue)(implicit formats: Formats, m: Manifest[A]): Option[A] =
    fromJsValueWithLetterCaseOrNone(jsv, JsonLetterCase.Camel)

  final def fromSnakeCasedJsValueOrNone(jsv: JsValue)(implicit formats: Formats, m: Manifest[A]): Option[A] =
    fromJsValueWithLetterCaseOrNone(jsv, JsonLetterCase.Snake)

  final def fromPascalCasedJsValueOrNone(jsv: JsValue)(implicit formats: Formats, m: Manifest[A]): Option[A] =
    fromJsValueWithLetterCaseOrNone(jsv, JsonLetterCase.Pascal)

  final def fromJsValue(jsv: JsValue, jsonCase: JsonLetterCase)(implicit formats: Formats, m: Manifest[A]): A =
    fromJValue(JsonUtils.convertToJValue(jsv))

  final def fromJsValueWithLetterCase(
    jsv: JsValue,
    jsonCase: JsonLetterCase
  )(
    implicit
    formats: Formats,
    m: Manifest[A]
  ): A =
    fromJValueWithLetterCase(JsonUtils.convertToJValue(jsv), jsonCase)

  final def fromCamelCasedJsValue(jsv: JsValue)(implicit formats: Formats, m: Manifest[A]): A =
    fromJsValueWithLetterCase(jsv, JsonLetterCase.Camel)

  final def fromSnakeCasedJsValue(jsv: JsValue)(implicit formats: Formats, m: Manifest[A]): A =
    fromJsValueWithLetterCase(jsv, JsonLetterCase.Snake)

  final def fromPascalCasedJsValue(jsv: JsValue)(implicit formats: Formats, m: Manifest[A]): A =
    fromJsValueWithLetterCase(jsv, JsonLetterCase.Pascal)

  final def fromJsonInputOrNone(json: JsonInput)(implicit formats: Formats, m: Manifest[A]): Option[A] =
    JsonUtils
      .fromJsonInputOrNone(json)
      .flatMap(fromJValueOrNone)

  final def fromJsonInputWithLetterCaseOrNone(
    json: JsonInput,
    jsonCase: JsonLetterCase
  )(
    implicit
    formats: Formats,
    m: Manifest[A]
  ): Option[A] =
    JsonUtils
      .fromJsonInputOrNone(json)
      .flatMap(fromJValueWithLetterCaseOrNone(_, jsonCase))

  final def fromCamelCasedJsonInputOrNone(json: JsonInput)(implicit formats: Formats, m: Manifest[A]): Option[A] =
    fromJsonInputWithLetterCaseOrNone(json, JsonLetterCase.Camel)

  final def fromSnakeCasedJsonInputOrNone(json: JsonInput)(implicit formats: Formats, m: Manifest[A]): Option[A] =
    fromJsonInputWithLetterCaseOrNone(json, JsonLetterCase.Snake)

  final def fromPascalCasedJsonInputOrNone(json: JsonInput)(implicit formats: Formats, m: Manifest[A]): Option[A] =
    fromJsonInputWithLetterCaseOrNone(json, JsonLetterCase.Pascal)

  final def fromJsonInput(json: JsonInput)(implicit formats: Formats, m: Manifest[A]): A =
    if (json == null) null.asInstanceOf[A]
    else fromJValue(JsonUtils.fromJsonInput(json))

  final def fromJsonInputWithLetterCase(
    json: JsonInput,
    jsonCase: JsonLetterCase
  )(
    implicit
    formats: Formats,
    m: Manifest[A]
  ): A =
    fromJValueWithLetterCase(JsonUtils.fromJsonInput(json), jsonCase)

  final def fromCamelCasedJsonInput(json: JsonInput)(implicit formats: Formats, m: Manifest[A]): A =
    fromJsonInputWithLetterCase(json, JsonLetterCase.Camel)

  final def fromSnakeCasedJsonInput(json: JsonInput)(implicit formats: Formats, m: Manifest[A]): A =
    fromJsonInputWithLetterCase(json, JsonLetterCase.Snake)

  final def fromPascalCasedJsonInput(json: JsonInput)(implicit formats: Formats, m: Manifest[A]): A =
    fromJsonInputWithLetterCase(json, JsonLetterCase.Pascal)

  final def fromJsonOrNone(json: String)(implicit formats: Formats, m: Manifest[A]): Option[A] =
    fromJsonInputOrNone(json)

  final def fromJsonWithLetterCaseOrNone(
    json: String,
    jsonCase: JsonLetterCase
  )(
    implicit
    formats: Formats,
    m: Manifest[A]
  ): Option[A] =
    fromJsonInputWithLetterCaseOrNone(json, jsonCase)

  final def fromCamelCasedJsonOrNone(json: String)(implicit formats: Formats, m: Manifest[A]): Option[A] =
    fromJsonWithLetterCaseOrNone(json, JsonLetterCase.Camel)

  final def fromSnakeCasedJsonOrNone(json: String)(implicit formats: Formats, m: Manifest[A]): Option[A] =
    fromJsonWithLetterCaseOrNone(json, JsonLetterCase.Snake)

  final def fromPascalCasedJsonOrNone(json: String)(implicit formats: Formats, m: Manifest[A]): Option[A] =
    fromJsonWithLetterCaseOrNone(json, JsonLetterCase.Pascal)

  final def fromJson(json: String)(implicit formats: Formats, m: Manifest[A]): A =
    fromJsonInput(json)

  final def fromJsonWithLetterCase(
    json: String,
    jsonCase: JsonLetterCase
  )(
    implicit
    formats: Formats,
    m: Manifest[A]
  ): A =
    fromJsonInputWithLetterCase(json, jsonCase)

  final def fromCamelCasedJson(json: String)(implicit formats: Formats, m: Manifest[A]): A =
    fromJsonWithLetterCase(json, JsonLetterCase.Camel)

  final def fromSnakeCasedJson(json: String)(implicit formats: Formats, m: Manifest[A]): A =
    fromJsonWithLetterCase(json, JsonLetterCase.Snake)

  final def fromPascalCasedJson(json: String)(implicit formats: Formats, m: Manifest[A]): A =
    fromJsonWithLetterCase(json, JsonLetterCase.Pascal)

  final def fromByteArrayOrNone(bytes: Array[Byte])(implicit formats: Formats, m: Manifest[A]): Option[A] =
    fromJsonOrNone(new String(bytes))

  final def fromByteArrayWithLetterCaseOrNone(
    bytes: Array[Byte],
    jsonCase: JsonLetterCase
  )(
    implicit
    formats: Formats,
    m: Manifest[A]
  ): Option[A] =
    fromJsonWithLetterCaseOrNone(new String(bytes), jsonCase)

  final def fromCamelCasedByteArrayOrNone(bytes: Array[Byte])(implicit formats: Formats, m: Manifest[A]): Option[A] =
    fromByteArrayWithLetterCaseOrNone(bytes, JsonLetterCase.Camel)

  final def fromSnakeCasedByteArrayOrNone(bytes: Array[Byte])(implicit formats: Formats, m: Manifest[A]): Option[A] =
    fromByteArrayWithLetterCaseOrNone(bytes, JsonLetterCase.Snake)

  final def fromPascalCasedByteArrayOrNone(bytes: Array[Byte])(implicit formats: Formats, m: Manifest[A]): Option[A] =
    fromByteArrayWithLetterCaseOrNone(bytes, JsonLetterCase.Pascal)

  final def fromByteArray(bytes: Array[Byte])(implicit formats: Formats, m: Manifest[A]): A =
    fromJson(new String(bytes))

  final def fromByteArrayWithLetterCase(
    bytes: Array[Byte],
    jsonCase: JsonLetterCase
  )(
    implicit
    formats: Formats,
    m: Manifest[A]
  ): A =
    fromJsonWithLetterCase(new String(bytes), jsonCase)

  final def fromCamelCasedByteArray(bytes: Array[Byte])(implicit formats: Formats, m: Manifest[A]): A =
    fromByteArrayWithLetterCase(bytes, JsonLetterCase.Camel)

  final def fromSnakeCasedByteArray(bytes: Array[Byte])(implicit formats: Formats, m: Manifest[A]): A =
    fromByteArrayWithLetterCase(bytes, JsonLetterCase.Snake)

  final def fromPascalCasedByteArray(bytes: Array[Byte])(implicit formats: Formats, m: Manifest[A]): A =
    fromByteArrayWithLetterCase(bytes, JsonLetterCase.Pascal)

  final def fromFileOrNone(file: jio.File)(implicit formats: Formats, m: Manifest[A]): Option[A] =
    fromJsonInputOrNone(file)

  final def fromFileWithLetterCaseOrNone(
    file: jio.File,
    jsonCase: JsonLetterCase
  )(
    implicit
    formats: Formats,
    m: Manifest[A]
  ): Option[A] =
    fromJsonInputWithLetterCaseOrNone(file, jsonCase)

  final def fromCamelCasedFileOrNone(file: jio.File)(implicit formats: Formats, m: Manifest[A]): Option[A] =
    fromFileWithLetterCaseOrNone(file, JsonLetterCase.Camel)

  final def fromSnakeCasedFileOrNone(file: jio.File)(implicit formats: Formats, m: Manifest[A]): Option[A] =
    fromFileWithLetterCaseOrNone(file, JsonLetterCase.Snake)

  final def fromPascalCasedFileOrNone(file: jio.File)(implicit formats: Formats, m: Manifest[A]): Option[A] =
    fromFileWithLetterCaseOrNone(file, JsonLetterCase.Pascal)

  final def fromFile(file: jio.File)(implicit formats: Formats, m: Manifest[A]): A = fromJsonInput(file)

  final def fromFileWithLetterCase(
    file: jio.File,
    jsonCase: JsonLetterCase
  )(
    implicit
    formats: Formats,
    m: Manifest[A]
  ): A =
    fromJsonInputWithLetterCase(file, jsonCase)

  final def fromCamelCasedFile(file: jio.File)(implicit formats: Formats, m: Manifest[A]): A =
    fromFileWithLetterCase(file, JsonLetterCase.Camel)

  final def fromSnakeCasedFile(file: jio.File)(implicit formats: Formats, m: Manifest[A]): A =
    fromFileWithLetterCase(file, JsonLetterCase.Snake)

  final def fromPascalCasedFile(file: jio.File)(implicit formats: Formats, m: Manifest[A]): A =
    fromFileWithLetterCase(file, JsonLetterCase.Pascal)

  final def fromPathOrNone(path: jnf.Path)(implicit formats: Formats, m: Manifest[A]): Option[A] =
    fromFileOrNone(path.toFile)

  final def fromPathWithLetterCaseOrNone(
    path: jnf.Path,
    jsonCase: JsonLetterCase
  )(
    implicit
    formats: Formats,
    m: Manifest[A]
  ): Option[A] =
    fromFileWithLetterCaseOrNone(path.toFile, jsonCase)

  final def fromCamelCasedPathOrNone(path: jnf.Path)(implicit formats: Formats, m: Manifest[A]): Option[A] =
    fromPathWithLetterCaseOrNone(path, JsonLetterCase.Camel)

  final def fromSnakeCasedPathOrNone(path: jnf.Path)(implicit formats: Formats, m: Manifest[A]): Option[A] =
    fromPathWithLetterCaseOrNone(path, JsonLetterCase.Snake)

  final def fromPascalCasedPathOrNone(path: jnf.Path)(implicit formats: Formats, m: Manifest[A]): Option[A] =
    fromPathWithLetterCaseOrNone(path, JsonLetterCase.Pascal)

  final def fromPath(path: jnf.Path)(implicit formats: Formats, m: Manifest[A]): A = fromFile(path.toFile)

  final def fromPathWithLetterCase(
    path: jnf.Path,
    jsonCase: JsonLetterCase
  )(
    implicit
    formats: Formats,
    m: Manifest[A]
  ): A =
    fromFileWithLetterCase(path.toFile, jsonCase)

  final def fromCamelCasedPath(path: jnf.Path)(implicit formats: Formats, m: Manifest[A]): A =
    fromPathWithLetterCase(path, JsonLetterCase.Camel)

  final def fromSnakeCasedPath(path: jnf.Path)(implicit formats: Formats, m: Manifest[A]): A =
    fromPathWithLetterCase(path, JsonLetterCase.Snake)

  final def fromPascalCasedPath(path: jnf.Path)(implicit formats: Formats, m: Manifest[A]): A =
    fromPathWithLetterCase(path, JsonLetterCase.Pascal)

  final def fromInputStreamOrNone(is: jio.InputStream)(implicit formats: Formats, m: Manifest[A]): Option[A] =
    fromJsonInputOrNone(is)

  final def fromInputStreamWithLetterCaseOrNone(
    is: jio.InputStream,
    jsonCase: JsonLetterCase
  )(
    implicit
    formats: Formats,
    m: Manifest[A]
  ): Option[A] =
    fromJsonInputWithLetterCaseOrNone(is, jsonCase)

  final def fromCamelCasedInputStreamOrNone(is: jio.InputStream)(implicit formats: Formats, m: Manifest[A]): Option[A] =
    fromInputStreamWithLetterCaseOrNone(is, JsonLetterCase.Camel)

  final def fromSnakeCasedInputStreamOrNone(is: jio.InputStream)(implicit formats: Formats, m: Manifest[A]): Option[A] =
    fromInputStreamWithLetterCaseOrNone(is, JsonLetterCase.Snake)

  final def fromPascalCasedInputStreamOrNone(
    is: jio.InputStream
  )(
    implicit
    formats: Formats,
    m: Manifest[A]
  ): Option[A] =
    fromInputStreamWithLetterCaseOrNone(is, JsonLetterCase.Pascal)

  final def fromInputStream(is: jio.InputStream)(implicit formats: Formats, m: Manifest[A]): A =
    fromJsonInput(is)

  final def fromInputStreamWithLetterCase(
    is: jio.InputStream,
    jsonCase: JsonLetterCase
  )(
    implicit
    formats: Formats,
    m: Manifest[A]
  ): A =
    fromJsonInputWithLetterCase(is, jsonCase)

  final def fromCamelCasedInputStream(is: jio.InputStream)(implicit formats: Formats, m: Manifest[A]): A =
    fromInputStreamWithLetterCase(is, JsonLetterCase.Camel)

  final def fromSnakeCasedInputStream(is: jio.InputStream)(implicit formats: Formats, m: Manifest[A]): A =
    fromInputStreamWithLetterCase(is, JsonLetterCase.Snake)

  final def fromPascalCasedInputStream(is: jio.InputStream)(implicit formats: Formats, m: Manifest[A]): A =
    fromInputStreamWithLetterCase(is, JsonLetterCase.Pascal)

  final def fromResourceOrNone(resourcePath: String)(implicit formats: Formats, m: Manifest[A]): Option[A] =
    fromInputStreamOrNone(getClass.getResourceAsStream(resourcePath))

  final def fromResourceWithLetterCaseOrNone(
    resourcePath: String,
    jsonCase: JsonLetterCase
  )(
    implicit
    formats: Formats,
    m: Manifest[A]
  ): Option[A] =
    fromInputStreamWithLetterCaseOrNone(getClass.getResourceAsStream(resourcePath), jsonCase)

  final def fromCamelCasedResourceOrNone(resourcePath: String)(implicit formats: Formats, m: Manifest[A]): Option[A] =
    fromResourceWithLetterCaseOrNone(resourcePath, JsonLetterCase.Camel)

  final def fromSnakeCasedResourceOrNone(resourcePath: String)(implicit formats: Formats, m: Manifest[A]): Option[A] =
    fromResourceWithLetterCaseOrNone(resourcePath, JsonLetterCase.Snake)

  final def fromPascalCasedResourceOrNone(resourcePath: String)(implicit formats: Formats, m: Manifest[A]): Option[A] =
    fromResourceWithLetterCaseOrNone(resourcePath, JsonLetterCase.Pascal)

  final def fromResource(resourcePath: String)(implicit formats: Formats, m: Manifest[A]): A =
    fromInputStream(getClass.getResourceAsStream(resourcePath))

  final def fromResourceWithLetterCase(
    resourcePath: String,
    jsonCase: JsonLetterCase
  )(
    implicit
    formats: Formats,
    m: Manifest[A]
  ): A =
    fromInputStreamWithLetterCase(getClass.getResourceAsStream(resourcePath), jsonCase)

  final def fromCamelCasedResource(resourcePath: String)(implicit formats: Formats, m: Manifest[A]): A =
    fromResourceWithLetterCase(resourcePath, JsonLetterCase.Camel)

  final def fromSnakeCasedResource(resourcePath: String)(implicit formats: Formats, m: Manifest[A]): A =
    fromResourceWithLetterCase(resourcePath, JsonLetterCase.Snake)

  final def fromPascalCasedResource(resourcePath: String)(implicit formats: Formats, m: Manifest[A]): A =
    fromResourceWithLetterCase(resourcePath, JsonLetterCase.Pascal)
}

/** A trait intended for the companion object of [[A]] that deserializes JSON.
  * It assumes that the members in [[A]] are camelCased.
  *
  * @tparam A a case class with its members in camelCase
  */
trait CamelCasedJsonWritableCompanionLike[A <: CamelCasedJsonWritable[A]] extends JsonWritableCompanionLike[A] {

  override protected final val classLetterCase: JsonLetterCase = JsonLetterCase.Camel
}

/** A trait intended for the companion object of [[A]] that deserializes JSON.
  * It assumes that the members in [[A]] are snake_cased.
  *
  * @tparam A a case class with its members in snake_case
  */
trait SnakeCasedJsonWritableCompanionLike[A <: SnakeCasedJsonWritable[A]] extends JsonWritableCompanionLike[A] {

  override protected final val classLetterCase: JsonLetterCase = JsonLetterCase.Snake
}

/** A trait intended for the companion object of [[A]] that deserializes JSON.
  * It assumes that the members in [[A]] are PascalCased.
  *
  * @tparam A a case class with its members in PascalCase
  */
trait PascalCasedJsonWritableCompanionLike[A <: PascalCasedJsonWritable[A]] extends JsonWritableCompanionLike[A] {

  override protected final val classLetterCase: JsonLetterCase = JsonLetterCase.Pascal
}
