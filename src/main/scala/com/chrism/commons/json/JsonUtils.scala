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

import com.chrism.commons.json.json4s.JsonLetterCase
import com.chrism.commons.util.StringUtils
import org.json4s.native.{JsonMethods, Serialization}
import org.json4s.{
  Extraction,
  FileInput,
  Formats,
  JArray,
  JBool,
  JDecimal,
  JDouble,
  JInt,
  JLong,
  JNull,
  JObject,
  JString,
  JValue,
  JsonInput,
  ReaderInput,
  StreamInput,
  StringInput
}
import play.api.libs.json.{JsArray, JsBoolean, JsNull, JsNumber, JsObject, JsString, JsValue}

object JsonUtils {

  // TODO 1: Prevent the keys in Map type to change case
  // TODO 2: Implement orNone versions to avoid serializing empty JSON

  val EmptyJsonArray: String = "[]"
  val EmptyJsonObject: String = "{}"

  def fromJsonInput(json: JsonInput, useBigDecimalForDouble: Boolean = true, useBigIntForLong: Boolean = true): JValue =
    if (isJsonInputNullOrEmpty(json)) JNull
    else JsonMethods.parse(json, useBigDecimalForDouble = useBigDecimalForDouble, useBigIntForLong = useBigIntForLong)

  def fromJsonInputOrNone(
    json: JsonInput,
    useBigDecimalForDouble: Boolean = true,
    useBigIntForLong: Boolean = true
  ): Option[JValue] =
    nonEmptyJsonInputOrNone(json)
      .flatMap(
        JsonMethods.parseOpt(_, useBigDecimalForDouble = useBigDecimalForDouble, useBigIntForLong = useBigIntForLong)
      )

  def fromJson(json: String, useBigDecimalForDouble: Boolean = true, useBigIntForLong: Boolean = true): JValue =
    fromJsonInput(json, useBigDecimalForDouble = useBigDecimalForDouble, useBigIntForLong = useBigIntForLong)

  def fromJsonOrNone(
    json: String,
    useBigDecimalForDouble: Boolean = true,
    useBigIntForLong: Boolean = true
  ): Option[JValue] =
    fromJsonInputOrNone(json, useBigDecimalForDouble = useBigDecimalForDouble, useBigIntForLong = useBigIntForLong)

  def decomposeToJValue(a: Any)(implicit formats: Formats): JValue = if (a == null) JNull else Extraction.decompose(a)

  def decomposeToJValueWithLetterCase(a: Any)(implicit formats: Formats, jsonCase: JsonLetterCase): JValue =
    jsonCase.format(decomposeToJValue(a))

  def decomposeToCamelCasedJValue(a: Any)(implicit formats: Formats): JValue =
    decomposeToJValueWithLetterCase(a)(formats, JsonLetterCase.Camel)

  def decomposeToSnakeCasedJValue(a: Any)(implicit formats: Formats): JValue =
    decomposeToJValueWithLetterCase(a)(formats, JsonLetterCase.Snake)

  def decomposeToPascalCasedJValue(a: Any)(implicit formats: Formats): JValue =
    decomposeToJValueWithLetterCase(a)(formats, JsonLetterCase.Pascal)

  def decomposeToJsValue(a: Any)(implicit formats: Formats): JsValue = convertToJsValue(decomposeToJValue(a))

  def decomposeToJsValueWithLetterCase(a: Any)(implicit formats: Formats, jsonCase: JsonLetterCase): JsValue =
    convertToJsValue(decomposeToJValueWithLetterCase(a))

  def decomposeToCamelCasedJsValue(a: Any)(implicit formats: Formats): JsValue =
    decomposeToJsValueWithLetterCase(a)(formats, JsonLetterCase.Camel)

  def decomposeToSnakeCasedJsValue(a: Any)(implicit formats: Formats): JsValue =
    decomposeToJsValueWithLetterCase(a)(formats, JsonLetterCase.Snake)

  def decomposeToPascalCasedJsValue(a: Any)(implicit formats: Formats): JsValue =
    decomposeToJsValueWithLetterCase(a)(formats, JsonLetterCase.Pascal)

  def convertToJValue(jsv: JsValue): JValue =
    jsv match {
      case JsNull       => JNull
      case JsBoolean(b) => JBool(b)
      case JsNumber(n)  => JDecimal(n)
      case JsString(s)  => JString(s)
      case JsArray(a)   => if (a.isEmpty) JNull else JArray(a.map(convertToJValue).toList)
      case JsObject(o)  => if (o.isEmpty) JNull else JObject(o.toSeq.map(kv => kv._1 -> convertToJValue(kv._2)): _*)
      case other        => throw new UnsupportedOperationException(s"$other is not a supported data type!")
    }

  def convertToJsValue(jv: JValue): JsValue =
    jv match {
      case JNull         => JsNull
      case JBool(b)      => JsBoolean(b)
      case JInt(i)       => JsNumber(BigDecimal(i))
      case JLong(l)      => JsNumber(BigDecimal(l))
      case JDouble(d)    => JsNumber(d)
      case JDecimal(dec) => JsNumber(dec)
      case JString(s)    => JsString(s)
      case JArray(a)     => if (a.isEmpty) JsNull else JsArray(a.map(convertToJsValue))
      case JObject(o)    => if (o.isEmpty) JsNull else JsObject(o.map(kv => kv._1 -> convertToJsValue(kv._2)).toMap)
      case other         => throw new UnsupportedOperationException(s"$other is not a supported data type!")
    }

  def writeJValueAsJson(jv: JValue)(implicit formats: Formats): String =
    jv match {
      case null  => null
      case JNull => null
      case other => Serialization.write(other)
    }

  def writeJValueAsJsonWithLetterCase(jv: JValue, jsonCase: JsonLetterCase)(implicit formats: Formats): String =
    Serialization.write(jsonCase.format(jv))

  def writeJValueAsCamelCasedJson(jv: JValue)(implicit formats: Formats): String =
    writeJValueAsJsonWithLetterCase(jv, JsonLetterCase.Camel)(formats)

  def writeJValueAsSnakeCasedJson(jv: JValue)(implicit formats: Formats): String =
    writeJValueAsJsonWithLetterCase(jv, JsonLetterCase.Snake)(formats)

  def writeJValueAsPascalCasedJson(jv: JValue)(implicit formats: Formats): String =
    writeJValueAsJsonWithLetterCase(jv, JsonLetterCase.Pascal)(formats)

  def writeJsValueAsJson(jsv: JsValue)(implicit formats: Formats): String = writeJValueAsJson(convertToJValue(jsv))

  def writeJsValueAsJsonWithLetterCase(jsv: JsValue, jsonCase: JsonLetterCase)(implicit formats: Formats): String =
    writeJValueAsJsonWithLetterCase(convertToJValue(jsv), jsonCase)

  def writeJsValueAsCamelCasedJson(jsv: JsValue)(implicit formats: Formats): String =
    writeJsValueAsJsonWithLetterCase(jsv, JsonLetterCase.Camel)(formats)

  def writeJsValueAsSnakeCasedJson(jsv: JsValue)(implicit formats: Formats): String =
    writeJsValueAsJsonWithLetterCase(jsv, JsonLetterCase.Snake)(formats)

  def writeJsValueAsPascalCasedJson(jsv: JsValue)(implicit formats: Formats): String =
    writeJsValueAsJsonWithLetterCase(jsv, JsonLetterCase.Pascal)(formats)

  def writeAsJson[A <: AnyRef](a: A)(implicit formats: Formats): String =
    if (a == null) null else Serialization.write(a)

  def writeAsJsonWithLetterCase[A <: AnyRef](a: A, jsonCase: JsonLetterCase)(implicit formats: Formats): String =
    writeJValueAsJsonWithLetterCase(decomposeToJValue(a), jsonCase)

  def writeAsCamelCasedJson[A <: AnyRef](a: A)(implicit formats: Formats): String =
    writeAsJsonWithLetterCase(a, JsonLetterCase.Camel)(formats)

  def writeAsSnakeCasedJson[A <: AnyRef](a: A)(implicit formats: Formats): String =
    writeAsJsonWithLetterCase(a, JsonLetterCase.Snake)(formats)

  def writeAsPascalCasedJson[A <: AnyRef](a: A)(implicit formats: Formats): String =
    writeAsJsonWithLetterCase(a, JsonLetterCase.Pascal)(formats)

  private def isJsonInputNullOrEmpty(json: JsonInput): Boolean =
    json match {
      case null           => true
      case StringInput(s) => StringUtils.isBlank(s) || s == EmptyJsonArray || s == EmptyJsonObject
      case ReaderInput(r) => r == null
      case StreamInput(s) => s == null
      case FileInput(f)   => f == null
      case _              => false
    }

  private def nonEmptyJsonInputOrNone(json: JsonInput): Option[JsonInput] =
    if (isJsonInputNullOrEmpty(json)) None else Some(json)

  object implicits {

    implicit final class JValueSerializationOps(jv: JValue) {

      def asJsValue: JsValue = convertToJsValue(jv)
    }

    implicit final class JsValueSerializationOps(jsv: JsValue) {

      def asJValue: JValue = convertToJValue(jsv)
    }
  }
}
