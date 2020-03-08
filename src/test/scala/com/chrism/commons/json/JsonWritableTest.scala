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

import com.chrism.commons.FunTestSuite
import com.chrism.commons.io.FileUtils
import com.chrism.commons.json.json4s.Json4sFormatsLike
import org.json4s.{CustomSerializer, Formats, JArray, JInt, JNull, JObject, JString, MappingException}
import play.api.libs.json.{JsNumber, JsObject, JsString}

final class JsonWritableTest extends FunTestSuite with Json4sFormatsLike {

  import JsonWritableTest.{Dummy23, OptionDummy, SeqDummy, SeqDummySerializer, SimpleDummy}

  test("converting Scala object to JValue") {
    val obj = SimpleDummy("a", 2)
    assert(obj.toCamelCasedJValue === JObject("fieldA" -> JString("a"), "fieldB" -> JInt(2)))
    assert(obj.toSnakeCasedJValue === JObject("field_a" -> JString("a"), "field_b" -> JInt(2)))
    assert(obj.toPascalCasedJValue === JObject("FieldA" -> JString("a"), "FieldB" -> JInt(2)))
  }

  test("converting Scala object to JsValue") {
    val obj = SimpleDummy("b", 100)
    assert(obj.toCamelCasedJsValue === JsObject(Seq("fieldA" -> JsString("b"), "fieldB" -> JsNumber(100))))
    assert(obj.toSnakeCasedJsValue === JsObject(Seq("field_a" -> JsString("b"), "field_b" -> JsNumber(100))))
    assert(obj.toPascalCasedJsValue === JsObject(Seq("FieldA" -> JsString("b"), "FieldB" -> JsNumber(100))))
  }

  test("converting Scala object to JSON") {
    val obj = SimpleDummy("c", 5)

    val camelCased = obj.toCamelCasedJson
    assert(camelCased.contains(""""fieldA":"c""""))
    assert(camelCased.contains(""""fieldB":5"""))

    val snakeCased = obj.toSnakeCasedJson
    assert(snakeCased.contains(""""field_a":"c""""))
    assert(snakeCased.contains(""""field_b":5"""))

    val pascalCased = obj.toPascalCasedJson
    assert(pascalCased.contains(""""FieldA":"c""""))
    assert(pascalCased.contains(""""FieldB":5"""))
  }

  test("converting JValue to Scala") {
    val expected = SimpleDummy("a", 666)
    assert(SimpleDummy.fromCamelCasedJValue(JObject("fieldA" -> JString("a"), "fieldB" -> JInt(666))) === expected)
    assert(SimpleDummy.fromSnakeCasedJValue(JObject("field_a" -> JString("a"), "field_b" -> JInt(666))) === expected)
    assert(SimpleDummy.fromPascalCasedJValue(JObject("FieldA" -> JString("a"), "FieldB" -> JInt(666))) === expected)
  }

  test("converting JsValue to Scala") {
    val expected = SimpleDummy("d", 666)

    val camelCased = JsObject(Seq("fieldA" -> JsString("d"), "fieldB" -> JsNumber(666)))
    assert(SimpleDummy.fromCamelCasedJsValue(camelCased) === expected)

    val snakeCased = JsObject(Seq("field_a" -> JsString("d"), "field_b" -> JsNumber(666)))
    assert(SimpleDummy.fromSnakeCasedJsValue(snakeCased) === expected)

    val pascalCased = JsObject(Seq("FieldA" -> JsString("d"), "FieldB" -> JsNumber(666)))
    assert(SimpleDummy.fromPascalCasedJsValue(pascalCased) === expected)
  }

  test("converting JSON to Scala") {
    val expected = SimpleDummy("g", 8)
    assert(SimpleDummy.fromCamelCasedJson("""{"fieldA": "g", "fieldB": 8}""") === expected)
    assert(SimpleDummy.fromSnakeCasedJson("""{"field_a": "g", "field_b": 8}""") === expected)
    assert(SimpleDummy.fromPascalCasedJson("""{"FieldA": "g", "FieldB": 8}""") === expected)
  }

  test("serializing case class with more than 22 fields") {
    val obj = Dummy23(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23)
    val pascalCased = obj.toPascalCasedJson
    assert(pascalCased.contains(""""IntField1":1"""))
    assert(pascalCased.contains(""""IntField2":2"""))
    assert(pascalCased.contains(""""IntField3":3"""))
    assert(pascalCased.contains(""""IntField4":4"""))
    assert(pascalCased.contains(""""IntField5":5"""))
    assert(pascalCased.contains(""""IntField6":6"""))
    assert(pascalCased.contains(""""IntField7":7"""))
    assert(pascalCased.contains(""""IntField8":8"""))
    assert(pascalCased.contains(""""IntField9":9"""))
    assert(pascalCased.contains(""""IntField10":10"""))
    assert(pascalCased.contains(""""IntField11":11"""))
    assert(pascalCased.contains(""""IntField12":12"""))
    assert(pascalCased.contains(""""IntField13":13"""))
    assert(pascalCased.contains(""""IntField14":14"""))
    assert(pascalCased.contains(""""IntField15":15"""))
    assert(pascalCased.contains(""""IntField16":16"""))
    assert(pascalCased.contains(""""IntField17":17"""))
    assert(pascalCased.contains(""""IntField18":18"""))
    assert(pascalCased.contains(""""IntField19":19"""))
    assert(pascalCased.contains(""""IntField20":20"""))
    assert(pascalCased.contains(""""IntField21":21"""))
    assert(pascalCased.contains(""""IntField22":22"""))
    assert(pascalCased.contains(""""IntField23":23"""))
  }

  test("converting JsValue with more than 22 fields to case class") {
    val snakeCasedJsv = JsObject(
      Seq(
        "int_field1" -> JsNumber(1),
        "int_field2" -> JsNumber(2),
        "int_field3" -> JsNumber(3),
        "int_field4" -> JsNumber(4),
        "int_field5" -> JsNumber(5),
        "int_field6" -> JsNumber(6),
        "int_field7" -> JsNumber(7),
        "int_field8" -> JsNumber(8),
        "int_field9" -> JsNumber(9),
        "int_field10" -> JsNumber(10),
        "int_field11" -> JsNumber(11),
        "int_field12" -> JsNumber(12),
        "int_field13" -> JsNumber(13),
        "int_field14" -> JsNumber(14),
        "int_field15" -> JsNumber(15),
        "int_field16" -> JsNumber(16),
        "int_field17" -> JsNumber(17),
        "int_field18" -> JsNumber(18),
        "int_field19" -> JsNumber(19),
        "int_field20" -> JsNumber(20),
        "int_field21" -> JsNumber(21),
        "int_field22" -> JsNumber(22),
        "int_field23" -> JsNumber(23),
      )
    )
    val expected = Dummy23(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23)
    assert(Dummy23.fromSnakeCasedJsValue(snakeCasedJsv) === expected)
  }

  test("reading/writing JSON from/to file: camelCase") {
    val dummy = OptionDummy("dummy", intValue = Some(1))
    val deserialized = FileUtils.withTempDirectory(prefix = Some("json_test_camelCase")) { dir =>
      val file = dummy.writeAsFileCamelCased(FileUtils.newFile(dir, "camelCasedJson.json"))
      OptionDummy.fromCamelCasedFile(file)
    }
    assert(dummy === deserialized)
  }

  test("reading/writing JSON from/to file: snake_case") {
    val dummy = OptionDummy("dummy", booleanValue = Some(false))
    val deserialized = FileUtils.withTempDirectory(prefix = Some("json_test_snake_case")) { dir =>
      val file = dummy.writeAsFileSnakeCased(FileUtils.newFile(dir, "snake_case_dummy.json"))
      OptionDummy.fromSnakeCasedFile(file)
    }
    assert(dummy === deserialized)
  }

  test("reading/writing JSON from/to file: PascalCase") {
    val dummy = OptionDummy("dummy", doubleValue = Some(1.0))
    val deserialized = FileUtils.withTempDirectory(prefix = Some("json_test_PascalCase")) { dir =>
      val file = dummy.writeAsFilePascalCased(FileUtils.newFile(dir, "PascalCasedJson.json"))
      OptionDummy.fromPascalCasedFile(file)
    }
    assert(dummy === deserialized)
  }

  test("reading JSON from resource path: camelCase") {
    val dummy = OptionDummy.fromCamelCasedResource("/com/chrism/commons/json/camelCasedJson.json")
    assert(dummy === OptionDummy("camelCased", intValue = Some(665), booleanValue = Some(false)))
  }

  test("reading JSON from resource path: snake_case") {
    val dummy = OptionDummy.fromSnakeCasedResource("/com/chrism/commons/json/snake_cased_json.json")
    val expected = OptionDummy("snake_cased", intValue = Some(666), booleanValue = Some(true), doubleValue = Some(2.0))
    assert(dummy === expected)
  }

  test("reading JSON from resource path: PascalCase") {
    val dummy = OptionDummy.fromPascalCasedResource("/com/chrism/commons/json/PascalCasedJson.json")
    assert(dummy === OptionDummy("PascalCased", booleanValue = Some(true), doubleValue = Some(3.0)))
  }

  test("custom-serializing case class to JSON array: empty") {
    assertNull(SeqDummy(Some(Seq.empty)).toJson)
    assertNull(SeqDummy(None).toJson)
  }

  test("custom-serializing case class to JSON array: non-empty") {
    assert(SeqDummy(Some(Seq("your", "mom"))).toJson === """["your","mom"]""")
  }

  test("custom-deserializing case class from JSON array: empty") {
    assertNull(SeqDummy.fromJson(null))
    assert(SeqDummy.fromJsonOrNone(null).isEmpty)
    assertNull(SeqDummy.fromJson("[]"))
    assert(SeqDummy.fromJsonOrNone("[]").isEmpty)
  }

  test("custom-deserializing case class from JSON array: non-empty") {
    assert(SeqDummy.fromJson("""["your", "mom"]""") === SeqDummy(Some(Seq("your", "mom"))))
  }

  override protected def loadFormats(): Formats = super.loadFormats() + SeqDummySerializer
}

private[this] object JsonWritableTest {

  private final case class SimpleDummy(fieldA: String, fieldB: Int) extends CamelCasedJsonWritable[SimpleDummy]

  private object SimpleDummy extends CamelCasedJsonWritableCompanionLike[SimpleDummy]

  private final case class Dummy23(
    intField1: Int,
    intField2: Int,
    intField3: Int,
    intField4: Int,
    intField5: Int,
    intField6: Int,
    intField7: Int,
    intField8: Int,
    intField9: Int,
    intField10: Int,
    intField11: Int,
    intField12: Int,
    intField13: Int,
    intField14: Int,
    intField15: Int,
    intField16: Int,
    intField17: Int,
    intField18: Int,
    intField19: Int,
    intField20: Int,
    intField21: Int,
    intField22: Int,
    intField23: Int)
      extends CamelCasedJsonWritable[Dummy23]

  private object Dummy23 extends CamelCasedJsonWritableCompanionLike[Dummy23]

  private final case class OptionDummy(
    name: String,
    intValue: Option[Int] = None,
    booleanValue: Option[Boolean] = None,
    doubleValue: Option[Double] = None)
      extends CamelCasedJsonWritable[OptionDummy]

  private object OptionDummy extends CamelCasedJsonWritableCompanionLike[OptionDummy]

  private final case class SeqDummy(seq: Option[Seq[String]]) extends CamelCasedJsonWritable[SeqDummy]

  private object SeqDummy extends CamelCasedJsonWritableCompanionLike[SeqDummy]

  private case object SeqDummySerializer
      extends CustomSerializer[SeqDummy](_ =>
        ({
          case jArr: JArray =>
            val arr = jArr.arr
              .map({
                case s: JString => s.s
                case other      => throw new MappingException(s"Expected JString, but $other is found!")
              })
            if (arr.isEmpty) SeqDummy(None) else SeqDummy(Some(arr))
        }, {
          case seq: SeqDummy =>
            seq.seq.filter(_.nonEmpty).map(_.map(JString(_))).map(_.toList).map(JArray(_)).getOrElse(JNull)
        })
      )

}
