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

import java.{lang => jl, math => jm}

import com.chrism.commons.FunTestSuite
import com.chrism.commons.json.json4s.Json4sFormatsLike
import org.json4s.{JArray, JBool, JDecimal, JDouble, JInt, JLong, JNull, JObject, JString, JValue}
import play.api.libs.json.{JsArray, JsBoolean, JsNull, JsNumber, JsObject, JsString, JsValue}

final class JsonUtilsTest extends FunTestSuite with Json4sFormatsLike {

  import JsonUtils.implicits._
  import JsonUtilsTest.Foo

  test("decomposing a data structure into JValue") {
    val map = Map("a" -> 1, "b" -> 2)
    assert(JsonUtils.decomposeToJValue(map) === JObject("a" -> JInt(1), "b" -> JInt(2)))
  }

  test("converting from JValue to JsValue: empty") {
    val jv = JsonUtils.decomposeToJValue(Foo())
    assert(JsonUtils.convertToJsValue(jv) === JsNull)
  }

  test("converting from JsValue to JValue: empty") {
    val jsv = JsonUtils.convertToJsValue(JsonUtils.decomposeToJValue(Foo()))
    assert(JsonUtils.convertToJValue(jsv) === JNull)
  }

  test("converting from JValue to JsValue: non-empty") {
    val foo = Foo(
      boolOpt = Some(true),
      jBoolOpt = Some(true),
      byteOpt = Some(1),
      jByteOpt = Some(1.toByte.asInstanceOf[jl.Byte]),
      shortOpt = Some(1),
      jShortOpt = Some(1.toShort.asInstanceOf[jl.Short]),
      intOpt = Some(1),
      jIntOpt = Some(1.asInstanceOf[jl.Integer]),
      longOpt = Some(1L),
      jLongOpt = Some(1L.asInstanceOf[jl.Long]),
      bigIntOpt = Some(BigInt(1)),
      jBigIntOpt = Some(jm.BigInteger.valueOf(1L)),
      floatOpt = Some(1.0f),
      jFloatOpt = Some(1.0f),
      doubleOpt = Some(1.0),
      jDoubleOpt = Some(1.0),
      bigDecimalOpt = Some(BigDecimal("1.0")),
      jBigDecimalOpt = Some(jm.BigDecimal.ONE),
      sOpt = Some("mom"),
      seqOpt = Some(Seq("your", "mom")),
      mapOpt = Some(Map("your" -> 1L, "mom" -> 2L)),
    )
    val jv = JsonUtils.decomposeToJValue(foo)
    val expected = JsObject(
      Seq(
        "boolOpt" -> JsBoolean(true),
        "jBoolOpt" -> JsBoolean(true),
        "byteOpt" -> JsNumber(1),
        "jByteOpt" -> JsNumber(1),
        "shortOpt" -> JsNumber(1),
        "jShortOpt" -> JsNumber(1),
        "intOpt" -> JsNumber(1),
        "jIntOpt" -> JsNumber(1),
        "longOpt" -> JsNumber(1L),
        "jLongOpt" -> JsNumber(1L),
        "bigIntOpt" -> JsNumber(1),
        "jBigIntOpt" -> JsNumber(1),
        "floatOpt" -> JsNumber(BigDecimal(1.0f)),
        "jFloatOpt" -> JsNumber(BigDecimal(1.0f)),
        "doubleOpt" -> JsNumber(BigDecimal(1.0)),
        "jDoubleOpt" -> JsNumber(BigDecimal(1.0)),
        "bigDecimalOpt" -> JsNumber(BigDecimal("1")),
        "jBigDecimalOpt" -> JsNumber(BigDecimal("1")),
        "sOpt" -> JsString("mom"),
        "seqOpt" -> JsArray(Seq(JsString("your"), JsString("mom"))),
        "mapOpt" -> JsObject(Seq("your" -> JsNumber(1L), "mom" -> JsNumber(2L))),
      )
    )
    assert(JsonUtils.convertToJsValue(jv) === expected)
  }

  test("converting from JsValue to JValue: non-empty") {
    val foo = Foo(
      boolOpt = Some(false),
      jBoolOpt = Some(false),
      byteOpt = Some(0),
      jByteOpt = Some(0.toByte.asInstanceOf[jl.Byte]),
      shortOpt = Some(0),
      jShortOpt = Some(0.toShort.asInstanceOf[jl.Short]),
      intOpt = Some(0),
      jIntOpt = Some(0.asInstanceOf[jl.Integer]),
      longOpt = Some(0L),
      jLongOpt = Some(0L.asInstanceOf[jl.Long]),
      bigIntOpt = Some(BigInt(0)),
      jBigIntOpt = Some(jm.BigInteger.valueOf(0L)),
      floatOpt = Some(0.0f),
      jFloatOpt = Some(0.0f),
      doubleOpt = Some(0.0),
      jDoubleOpt = Some(0.0),
      bigDecimalOpt = Some(BigDecimal("0.0")),
      jBigDecimalOpt = Some(jm.BigDecimal.ZERO),
      sOpt = Some("mom"),
      seqOpt = Some(Seq("your", "mom")),
      mapOpt = Some(Map("your" -> 1L, "mom" -> 2L))
    )
    val jsv = JsonUtils.decomposeToJsValue(foo)
    val expected = JObject(
      "boolOpt" -> JBool(false),
      "jBoolOpt" -> JBool(false),
      "byteOpt" -> JDecimal(0),
      "jByteOpt" -> JDecimal(0),
      "shortOpt" -> JDecimal(0),
      "jShortOpt" -> JDecimal(0),
      "intOpt" -> JDecimal(0),
      "jIntOpt" -> JDecimal(0),
      "longOpt" -> JDecimal(0),
      "jLongOpt" -> JDecimal(0),
      "bigIntOpt" -> JDecimal(0),
      "jBigIntOpt" -> JDecimal(0),
      "floatOpt" -> JDecimal(0.0),
      "jFloatOpt" -> JDecimal(0.0),
      "doubleOpt" -> JDecimal(0.0),
      "jDoubleOpt" -> JDecimal(0.0),
      "bigDecimalOpt" -> JDecimal(0),
      "jBigDecimalOpt" -> JDecimal(0),
      "sOpt" -> JString("mom"),
      "seqOpt" -> JArray(List(JString("your"), JString("mom"))),
      "mapOpt" -> JObject("your" -> JDecimal(1L), "mom" -> JDecimal(2L)),
    )
    assert(JsonUtils.convertToJValue(jsv) === expected)
  }

  test("converting from JValue to JBool") {
    val jv: JValue = JBool(false)
    assert(jv.asJBool === JBool(false))
  }

  test("converting from JValue to JInt") {
    val jv: JValue = JInt(1)
    assert(jv.asJInt === JInt(1))
  }

  test("converting from JValue to JLong") {
    val jv: JValue = JLong(1L)
    assert(jv.asJLong === JLong(1L))
  }

  test("converting from JValue to JDouble") {
    val jv: JValue = JDouble(1.0)
    assert(jv.asJDouble === JDouble(1.0))
  }

  test("converting from JValue to JDecimal") {
    val jv: JValue = JDecimal(BigDecimal(1.0))
    assert(jv.asJDecimal === JDecimal(BigDecimal(1.0)))
  }

  test("converting from JValue to JString") {
    val jv: JValue = JString("s")
    assert(jv.asJString === JString("s"))
  }

  test("converting from JValue to JArray") {
    val jv: JValue = JArray(List(JInt(1), JInt(2), JInt(3)))
    assert(jv.asJArray === JArray(List(JInt(1), JInt(2), JInt(3))))
  }

  test("converting from JValue to JObject") {
    val jv: JValue = JObject("ks" -> JString("s"), "ki" -> JInt(1), "kl" -> JLong(1L))
    assert(jv.asJObject === JObject("ks" -> JString("s"), "ki" -> JInt(1), "kl" -> JLong(1L)))
  }

  test("converting from JsValue to JsBoolean") {
    val jsv: JsValue = JsBoolean(true)
    assert(jsv.asJsBoolean === JsBoolean(true))
  }

  test("converting from JsValue to JsNumber") {
    val jsv: JsValue = JsNumber(1)
    assert(jsv.asJsNumber === JsNumber(1))
  }

  test("converting from JsValue to JsString") {
    val jsv: JsValue = JsString("s")
    assert(jsv.asJsString === JsString("s"))
  }

  test("converting from JsValue to JsArray") {
    val jsv: JsValue = JsArray(Seq(JsString("one"), JsString("two"), JsString("three")))
    assert(jsv.asJsArray === JsArray(Seq(JsString("one"), JsString("two"), JsString("three"))))
  }

  test("converting from JsValue to JsObject") {
    val jsv: JsValue = JsObject(Seq("kn" -> JsNumber(1), "ks" -> JsString("s")))
    assert(jsv.asJsObject === JsObject(Seq("kn" -> JsNumber(1), "ks" -> JsString("s"))))
  }

  test("writing JValue as JSON") {
    // TODO: update the assertions for 'mapOpt' after fixing the letter case issue

    val jv = JObject(
      "boolOpt" -> JBool(false),
      "jBoolOpt" -> JBool(false),
      "byteOpt" -> JInt(0),
      "jByteOpt" -> JInt(0),
      "shortOpt" -> JInt(0),
      "jShortOpt" -> JInt(0),
      "intOpt" -> JInt(0),
      "jIntOpt" -> JInt(0),
      "longOpt" -> JLong(0L),
      "jLongOpt" -> JLong(0L),
      "bigIntOpt" -> JDecimal(0),
      "jBigIntOpt" -> JDecimal(0),
      "floatOpt" -> JDouble(0.0f),
      "jFloatOpt" -> JDouble(0.0f),
      "doubleOpt" -> JDouble(0.0),
      "jDoubleOpt" -> JDouble(0.0),
      "bigDecimalOpt" -> JDecimal(0),
      "jBigDecimalOpt" -> JDecimal(0),
      "sOpt" -> JString("mom"),
      "seqOpt" -> JArray(List(JString("your"), JString("mom"))),
      "mapOpt" -> JObject("yourMom" -> JDecimal(1L), "yourAnotherMom" -> JDecimal(2L)),
    )

    val camelCased = JsonUtils.writeJValueAsCamelCasedJson(jv)
    assert(camelCased.contains(""""boolOpt":false"""))
    assert(camelCased.contains(""""jBoolOpt":false"""))
    assert(camelCased.contains(""""byteOpt":0"""))
    assert(camelCased.contains(""""jByteOpt":0"""))
    assert(camelCased.contains(""""shortOpt":0"""))
    assert(camelCased.contains(""""jShortOpt":0"""))
    assert(camelCased.contains(""""intOpt":0"""))
    assert(camelCased.contains(""""jIntOpt":0"""))
    assert(camelCased.contains(""""longOpt":0"""))
    assert(camelCased.contains(""""jLongOpt":0"""))
    assert(camelCased.contains(""""bigIntOpt":0"""))
    assert(camelCased.contains(""""jBigIntOpt":0"""))
    assert(camelCased.contains(""""floatOpt":0"""))
    assert(camelCased.contains(""""jFloatOpt":0"""))
    assert(camelCased.contains(""""doubleOpt":0"""))
    assert(camelCased.contains(""""jDoubleOpt":0"""))
    assert(camelCased.contains(""""bigDecimalOpt":0"""))
    assert(camelCased.contains(""""jBigDecimalOpt":0"""))
    assert(camelCased.contains(""""sOpt":"mom""""))
    assert(camelCased.contains(""""seqOpt":["your","mom"]"""))
    assert(camelCased.contains(""""mapOpt":"""))

    val snakeCased = JsonUtils.writeJValueAsSnakeCasedJson(jv)
    assert(snakeCased.contains(""""bool_opt":false"""))
    assert(snakeCased.contains(""""j_bool_opt":false"""))
    assert(snakeCased.contains(""""byte_opt":0"""))
    assert(snakeCased.contains(""""j_byte_opt":0"""))
    assert(snakeCased.contains(""""short_opt":0"""))
    assert(snakeCased.contains(""""j_short_opt":0"""))
    assert(snakeCased.contains(""""int_opt":0"""))
    assert(snakeCased.contains(""""j_int_opt":0"""))
    assert(snakeCased.contains(""""long_opt":0"""))
    assert(snakeCased.contains(""""j_long_opt":0"""))
    assert(snakeCased.contains(""""big_int_opt":0"""))
    assert(snakeCased.contains(""""j_big_int_opt":0"""))
    assert(snakeCased.contains(""""float_opt":0"""))
    assert(snakeCased.contains(""""j_float_opt":0"""))
    assert(snakeCased.contains(""""double_opt":0"""))
    assert(snakeCased.contains(""""j_double_opt":0"""))
    assert(snakeCased.contains(""""big_decimal_opt":0"""))
    assert(snakeCased.contains(""""j_big_decimal_opt":0"""))
    assert(snakeCased.contains(""""s_opt":"mom""""))
    assert(snakeCased.contains(""""seq_opt":["your","mom"]"""))
    assert(snakeCased.contains(""""map_opt":"""))

    val pascalCased = JsonUtils.writeJValueAsPascalCasedJson(jv)
    assert(pascalCased.contains(""""BoolOpt":false"""))
    assert(pascalCased.contains(""""JBoolOpt":false"""))
    assert(pascalCased.contains(""""ByteOpt":0"""))
    assert(pascalCased.contains(""""JByteOpt":0"""))
    assert(pascalCased.contains(""""ShortOpt":0"""))
    assert(pascalCased.contains(""""JShortOpt":0"""))
    assert(pascalCased.contains(""""IntOpt":0"""))
    assert(pascalCased.contains(""""JIntOpt":0"""))
    assert(pascalCased.contains(""""LongOpt":0"""))
    assert(pascalCased.contains(""""JLongOpt":0"""))
    assert(pascalCased.contains(""""BigIntOpt":0"""))
    assert(pascalCased.contains(""""JBigIntOpt":0"""))
    assert(pascalCased.contains(""""FloatOpt":0"""))
    assert(pascalCased.contains(""""JFloatOpt":0"""))
    assert(pascalCased.contains(""""DoubleOpt":0"""))
    assert(pascalCased.contains(""""JDoubleOpt":0"""))
    assert(pascalCased.contains(""""BigDecimalOpt":0"""))
    assert(pascalCased.contains(""""JBigDecimalOpt":0"""))
    assert(pascalCased.contains(""""SOpt":"mom""""))
    assert(pascalCased.contains(""""SeqOpt":["your","mom"]"""))
    assert(pascalCased.contains(""""MapOpt":"""))
  }

  test("writing JsValue as JSON") {
    // TODO: update the assertions for 'mapOpt' after fixing the letter case issue

    val jsv = JsObject(
      Seq(
        "boolOpt" -> JsBoolean(true),
        "jBoolOpt" -> JsBoolean(true),
        "byteOpt" -> JsNumber(1),
        "jByteOpt" -> JsNumber(1),
        "shortOpt" -> JsNumber(1),
        "jShortOpt" -> JsNumber(1),
        "intOpt" -> JsNumber(1),
        "jIntOpt" -> JsNumber(1),
        "longOpt" -> JsNumber(1L),
        "jLongOpt" -> JsNumber(1L),
        "bigIntOpt" -> JsNumber(1),
        "jBigIntOpt" -> JsNumber(1),
        "floatOpt" -> JsNumber(BigDecimal(1.0f)),
        "jFloatOpt" -> JsNumber(BigDecimal(1.0f)),
        "doubleOpt" -> JsNumber(BigDecimal(1.0)),
        "jDoubleOpt" -> JsNumber(BigDecimal(1.0)),
        "bigDecimalOpt" -> JsNumber(BigDecimal("1")),
        "jBigDecimalOpt" -> JsNumber(BigDecimal("1")),
        "sOpt" -> JsString("mom"),
        "seqOpt" -> JsArray(Seq(JsString("your"), JsString("mom"))),
        "mapOpt" -> JsObject(Seq("your" -> JsNumber(1L), "mom" -> JsNumber(2L))),
      )
    )

    val camelCased = JsonUtils.writeJsValueAsCamelCasedJson(jsv)
    assert(camelCased.contains(""""boolOpt":true"""))
    assert(camelCased.contains(""""jBoolOpt":true"""))
    assert(camelCased.contains(""""byteOpt":1"""))
    assert(camelCased.contains(""""jByteOpt":1"""))
    assert(camelCased.contains(""""shortOpt":1"""))
    assert(camelCased.contains(""""jShortOpt":1"""))
    assert(camelCased.contains(""""intOpt":1"""))
    assert(camelCased.contains(""""jIntOpt":1"""))
    assert(camelCased.contains(""""longOpt":1"""))
    assert(camelCased.contains(""""jLongOpt":1"""))
    assert(camelCased.contains(""""bigIntOpt":1"""))
    assert(camelCased.contains(""""jBigIntOpt":1"""))
    assert(camelCased.contains(""""floatOpt":1"""))
    assert(camelCased.contains(""""jFloatOpt":1"""))
    assert(camelCased.contains(""""doubleOpt":1"""))
    assert(camelCased.contains(""""jDoubleOpt":1"""))
    assert(camelCased.contains(""""bigDecimalOpt":1"""))
    assert(camelCased.contains(""""jBigDecimalOpt":1"""))
    assert(camelCased.contains(""""sOpt":"mom""""))
    assert(camelCased.contains(""""seqOpt":["your","mom"]"""))
    assert(camelCased.contains(""""mapOpt":"""))

    val snakeCased = JsonUtils.writeJsValueAsSnakeCasedJson(jsv)
    assert(snakeCased.contains(""""bool_opt":true"""))
    assert(snakeCased.contains(""""j_bool_opt":true"""))
    assert(snakeCased.contains(""""byte_opt":1"""))
    assert(snakeCased.contains(""""j_byte_opt":1"""))
    assert(snakeCased.contains(""""short_opt":1"""))
    assert(snakeCased.contains(""""j_short_opt":1"""))
    assert(snakeCased.contains(""""int_opt":1"""))
    assert(snakeCased.contains(""""j_int_opt":1"""))
    assert(snakeCased.contains(""""long_opt":1"""))
    assert(snakeCased.contains(""""j_long_opt":1"""))
    assert(snakeCased.contains(""""big_int_opt":1"""))
    assert(snakeCased.contains(""""j_big_int_opt":1"""))
    assert(snakeCased.contains(""""float_opt":1"""))
    assert(snakeCased.contains(""""j_float_opt":1"""))
    assert(snakeCased.contains(""""double_opt":1"""))
    assert(snakeCased.contains(""""j_double_opt":1"""))
    assert(snakeCased.contains(""""big_decimal_opt":1"""))
    assert(snakeCased.contains(""""j_big_decimal_opt":1"""))
    assert(snakeCased.contains(""""s_opt":"mom""""))
    assert(snakeCased.contains(""""seq_opt":["your","mom"]"""))
    assert(snakeCased.contains(""""map_opt":"""))

    val pascalCased = JsonUtils.writeJsValueAsPascalCasedJson(jsv)
    assert(pascalCased.contains(""""BoolOpt":true"""))
    assert(pascalCased.contains(""""JBoolOpt":true"""))
    assert(pascalCased.contains(""""ByteOpt":1"""))
    assert(pascalCased.contains(""""JByteOpt":1"""))
    assert(pascalCased.contains(""""ShortOpt":1"""))
    assert(pascalCased.contains(""""JShortOpt":1"""))
    assert(pascalCased.contains(""""IntOpt":1"""))
    assert(pascalCased.contains(""""JIntOpt":1"""))
    assert(pascalCased.contains(""""LongOpt":1"""))
    assert(pascalCased.contains(""""JLongOpt":1"""))
    assert(pascalCased.contains(""""BigIntOpt":1"""))
    assert(pascalCased.contains(""""JBigIntOpt":1"""))
    assert(pascalCased.contains(""""FloatOpt":1"""))
    assert(pascalCased.contains(""""JFloatOpt":1"""))
    assert(pascalCased.contains(""""DoubleOpt":1"""))
    assert(pascalCased.contains(""""JDoubleOpt":1"""))
    assert(pascalCased.contains(""""BigDecimalOpt":1"""))
    assert(pascalCased.contains(""""JBigDecimalOpt":1"""))
    assert(pascalCased.contains(""""SOpt":"mom""""))
    assert(pascalCased.contains(""""SeqOpt":["your","mom"]"""))
    assert(pascalCased.contains(""""MapOpt":"""))
  }
}

private[this] object JsonUtilsTest {

  private final case class Foo(
    boolOpt: Option[Boolean] = None,
    jBoolOpt: Option[jl.Boolean] = None,
    byteOpt: Option[Byte] = None,
    jByteOpt: Option[jl.Byte] = None,
    shortOpt: Option[Short] = None,
    jShortOpt: Option[jl.Short] = None,
    intOpt: Option[Int] = None,
    jIntOpt: Option[jl.Integer] = None,
    longOpt: Option[Long] = None,
    jLongOpt: Option[jl.Long] = None,
    bigIntOpt: Option[BigInt] = None,
    jBigIntOpt: Option[jm.BigInteger] = None,
    floatOpt: Option[Float] = None,
    jFloatOpt: Option[jl.Float] = None,
    doubleOpt: Option[Double] = None,
    jDoubleOpt: Option[jl.Double] = None,
    bigDecimalOpt: Option[BigDecimal] = None,
    jBigDecimalOpt: Option[jm.BigDecimal] = None,
    sOpt: Option[String] = None,
    seqOpt: Option[Seq[String]] = None,
    mapOpt: Option[Map[String, Long]] = None)
}
