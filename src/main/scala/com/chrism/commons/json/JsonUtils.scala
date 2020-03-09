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
  JNothing,
  JNull,
  JObject,
  JString,
  JValue,
  JsonInput,
  MappingException,
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
        JsonMethods.parseOpt(_, useBigDecimalForDouble = useBigDecimalForDouble, useBigIntForLong = useBigIntForLong))

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
      case JsNull            => JNull
      case jsBool: JsBoolean => JBool(jsBool.value)
      case jsNum: JsNumber   => JDecimal(jsNum.value)
      case jsStr: JsString   => JString(jsStr.value)
      case jsArr: JsArray    => if (jsArr.value.isEmpty) JNothing else JArray(jsArr.value.map(convertToJValue).toList)
      case jsObj: JsObject =>
        if (jsObj.value.isEmpty) JNothing
        else JObject(jsObj.value.toSeq.map(kv => kv._1 -> convertToJValue(kv._2)): _*)
      case other => throw new UnsupportedOperationException(s"$other is not a supported data type!")
    }

  def convertToJsValue(jv: JValue): JsValue =
    jv match {
      case JNothing | JNull => JsNull
      case jBool: JBool     => JsBoolean(jBool.value)
      case jInt: JInt       => JsNumber(BigDecimal(jInt.num))
      case jLong: JLong     => JsNumber(BigDecimal(jLong.num))
      case jDouble: JDouble => JsNumber(jDouble.num)
      case jDec: JDecimal   => JsNumber(jDec.num)
      case jStr: JString    => JsString(jStr.s)
      case jArr: JArray     => if (jArr.arr.isEmpty) JsNull else JsArray(jArr.arr.map(convertToJsValue))
      case jObj: JObject =>
        if (jObj.obj.isEmpty) JsNull
        else JsObject(jObj.obj.map(kv => kv._1 -> convertToJsValue(kv._2)).toMap)
      case other => throw new UnsupportedOperationException(s"$other is not a supported data type!")
    }

  def convertToJBool(jv: JValue): JBool =
    jv match {
      case jBool: JBool => jBool
      case other        => throw new MappingException(s"Expected JBool, but $other found!")
    }

  def convertToJBoolOrNone(jv: JValue): Option[JBool] =
    jv match {
      case jBool: JBool => Some(jBool)
      case _            => None
    }

  def convertToJInt(jv: JValue): JInt =
    jv match {
      case jInt: JInt => jInt
      case other      => throw new MappingException(s"Expected JInt, but $other found!")
    }

  def convertToJIntOrNone(jv: JValue): Option[JInt] =
    jv match {
      case jInt: JInt => Some(jInt)
      case _          => None
    }

  def convertToJLong(jv: JValue): JLong =
    jv match {
      case jLong: JLong => jLong
      case other        => throw new MappingException(s"Expected JLong, but $other found!")
    }

  def convertToJLongOrNone(jv: JValue): Option[JLong] =
    jv match {
      case jLong: JLong => Some(jLong)
      case _            => None
    }

  def convertToJDouble(jv: JValue): JDouble =
    jv match {
      case jDouble: JDouble => jDouble
      case other            => throw new MappingException(s"Expected JDouble, but $other found!")
    }

  def convertToJDoubleOrNone(jv: JValue): Option[JDouble] =
    jv match {
      case jDouble: JDouble => Some(jDouble)
      case _                => None
    }

  def convertToJDecimal(jv: JValue): JDecimal =
    jv match {
      case jDecimal: JDecimal => jDecimal
      case other              => throw new MappingException(s"Expected JDecimal, but $other found!")
    }

  def convertToJDecimalOrNone(jv: JValue): Option[JDecimal] =
    jv match {
      case jDecimal: JDecimal => Some(jDecimal)
      case _                  => None
    }

  def convertToJString(jv: JValue): JString =
    jv match {
      case jStr: JString => jStr
      case other         => throw new MappingException(s"Expected JString, but $other found!")
    }

  def convertToJStringOrNone(jv: JValue): Option[JString] =
    jv match {
      case jStr: JString => Some(jStr)
      case _             => None
    }

  def convertToJArray(jv: JValue): JArray =
    jv match {
      case jArr: JArray => jArr
      case other        => throw new MappingException(s"Expected JArray, but $other found!")
    }

  def convertToJArrayOrNone(jv: JValue): Option[JArray] =
    jv match {
      case jArr: JArray => Some(jArr)
      case _            => None
    }

  def convertToJObject(jv: JValue): JObject =
    jv match {
      case jObj: JObject => jObj
      case other         => throw new MappingException(s"Expected JObject, but $other found!")
    }

  def convertToJObjectOrNone(jv: JValue): Option[JObject] =
    jv match {
      case jObj: JObject => Some(jObj)
      case _             => None
    }

  def writeJValueAsJson(jv: JValue)(implicit formats: Formats): String =
    jv match {
      case null | JNothing | JNull => null
      case other                   => Serialization.write(other)
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
      case null => true
      case strIn: StringInput =>
        StringUtils.isBlank(strIn.string) || strIn.string == EmptyJsonArray || strIn.string == EmptyJsonObject
      case rIn: ReaderInput => rIn.reader == null
      case sIn: StreamInput => sIn.stream == null
      case fIn: FileInput   => fIn.file == null
      case _                => false
    }

  private def nonEmptyJsonInputOrNone(json: JsonInput): Option[JsonInput] =
    if (isJsonInputNullOrEmpty(json)) None else Some(json)

  object implicits {

    implicit final class JValueSerializationOps(jv: JValue) {

      def asJsValue: JsValue = convertToJsValue(jv)

      def asJBool: JBool = convertToJBool(jv)

      def asJBoolOrNone: Option[JBool] = convertToJBoolOrNone(jv)

      def asJInt: JInt = convertToJInt(jv)

      def asJIntOrNone: Option[JInt] = convertToJIntOrNone(jv)

      def asJLong: JLong = convertToJLong(jv)

      def asJLongOrNone: Option[JLong] = convertToJLongOrNone(jv)

      def asJDouble: JDouble = convertToJDouble(jv)

      def asJDoubleOrNone: Option[JDouble] = convertToJDoubleOrNone(jv)

      def asJDecimal: JDecimal = convertToJDecimal(jv)

      def asJDecimalOrNone: Option[JDecimal] = convertToJDecimalOrNone(jv)

      def asJString: JString = convertToJString(jv)

      def asJStringOrNone: Option[JString] = convertToJStringOrNone(jv)

      def asJArray: JArray = convertToJArray(jv)

      def asJArrayOrNone: Option[JArray] = convertToJArrayOrNone(jv)

      def asJObject: JObject = convertToJObject(jv)

      def asJObjectOrNone: Option[JObject] = convertToJObjectOrNone(jv)
    }

    implicit final class JsValueSerializationOps(jsv: JsValue) {

      def asJValue: JValue = convertToJValue(jsv)
    }
  }
}
