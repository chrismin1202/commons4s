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
package com.chrism.commons.json.json4s.custom

import com.chrism.commons.FunTestSuite
import com.chrism.commons.datatype.{CaseInsensitiveEnumLikeCompanionLike, EnumWithOrdinalLike}
import com.chrism.commons.json.json4s.Json4sFormats
import com.chrism.commons.json.{CamelCasedJsonWritable, CamelCasedJsonWritableCompanionLike, JsonUtils}
import org.json4s.{Formats, JInt, JObject}

final class EnumLikeSerializersTest extends FunTestSuite {

  import EnumLikeSerializersTest.{BertramGilfoyle, DineshChugtai, Employee, ErlichBachman, PiedPiper, RichardHendricks}

  test("serializing EnumLike with name") {
    implicit val formats: Formats = Json4sFormats.defaultFormats + Employee.nameSerializer

    assert(PiedPiper(ErlichBachman).toJson === """{"employee":"ErlichBachman"}""")
    assert(PiedPiper(RichardHendricks).toJson === """{"employee":"RichardHendricks"}""")
    assert(PiedPiper(BertramGilfoyle).toJson === """{"employee":"BertramGilfoyle"}""")
    assert(PiedPiper(DineshChugtai).toJson === """{"employee":"DineshChugtai"}""")
  }

  test("deserializing EnumLike with name") {
    implicit val formats: Formats = Json4sFormats.defaultFormats + Employee.nameSerializer

    assert(PiedPiper.fromJson("""{"employee": "ErlichBachman"}""").employee === ErlichBachman)
    assert(PiedPiper.fromJson("""{"employee": "richardHendricks"}""").employee === RichardHendricks)
    assert(PiedPiper.fromJson("""{"employee": "BertramGILFOYLE"}""").employee === BertramGilfoyle)
    assert(PiedPiper.fromJson("""{"employee": "dineshchugtai"}""").employee === DineshChugtai)
  }

  test("serializing EnumLike as key with name") {
    implicit val formats: Formats = Json4sFormats.defaultFormats + Employee.nameKeySerializer

    val jv = JsonUtils.decomposeToJValue(
      Map(ErlichBachman -> 1, RichardHendricks -> 2, BertramGilfoyle -> 3, DineshChugtai -> 4)
    )
    val expected = JObject(
      ErlichBachman.name -> JInt(1),
      RichardHendricks.name -> JInt(2),
      BertramGilfoyle.name -> JInt(3),
      DineshChugtai.name -> JInt(4),
    )
    assert(jv === expected)
  }

  test("deserializing EnumLike as key with name") {
    implicit val formats: Formats = Json4sFormats.defaultFormats + Employee.nameKeySerializer

    val jv =
      JsonUtils.fromJson("""{"ErlichBachman": 1, "richardHendricks": 2, "BertramGILFOYLE": 3, "dineshchugtai": 4}""")
    jv.extract[Map[Employee, Int]] should contain theSameElementsAs Map(
      ErlichBachman -> 1,
      RichardHendricks -> 2,
      BertramGilfoyle -> 3,
      DineshChugtai -> 4,
    )
  }

  test("serializing EnumLike with ordinal") {
    implicit val formats: Formats = Json4sFormats.defaultFormats + Employee.ordinalSerializer

    assert(PiedPiper(ErlichBachman).toJson === """{"employee":1}""")
    assert(PiedPiper(RichardHendricks).toJson === """{"employee":2}""")
    assert(PiedPiper(BertramGilfoyle).toJson === """{"employee":3}""")
    assert(PiedPiper(DineshChugtai).toJson === """{"employee":4}""")
  }

  test("deserializing EnumLike with ordinal") {
    implicit val formats: Formats = Json4sFormats.defaultFormats + Employee.ordinalSerializer

    assert(PiedPiper.fromJson("""{"employee": 1}""").employee === ErlichBachman)
    assert(PiedPiper.fromJson("""{"employee": 2}""").employee === RichardHendricks)
    assert(PiedPiper.fromJson("""{"employee": 3}""").employee === BertramGilfoyle)
    assert(PiedPiper.fromJson("""{"employee": 4}""").employee === DineshChugtai)
  }

  test("serializing EnumLike as key with ordinal") {
    implicit val formats: Formats = Json4sFormats.defaultFormats + Employee.ordinalKeySerializer

    val jv = JsonUtils.decomposeToJValue(
      Map(ErlichBachman -> 1, RichardHendricks -> 2, BertramGilfoyle -> 3, DineshChugtai -> 4)
    )
    val expected = JObject(
      ErlichBachman.ordinal.toString -> JInt(1),
      RichardHendricks.ordinal.toString -> JInt(2),
      BertramGilfoyle.ordinal.toString -> JInt(3),
      DineshChugtai.ordinal.toString -> JInt(4),
    )
    assert(jv === expected)
  }

  test("deserializing EnumLike as key with ordinal") {
    implicit val formats: Formats = Json4sFormats.defaultFormats + Employee.ordinalKeySerializer

    val jv =
      JsonUtils.fromJson("""{"1": 1, "2": 2, "3": 3, "4": 4}""")
    jv.extract[Map[Employee, Int]] should contain theSameElementsAs Map(
      ErlichBachman -> 1,
      RichardHendricks -> 2,
      BertramGilfoyle -> 3,
      DineshChugtai -> 4,
    )
  }
}

private[this] object EnumLikeSerializersTest {

  private sealed abstract class Employee(override final val name: String, override final val ordinal: Int)
      extends EnumWithOrdinalLike

  private object Employee extends CaseInsensitiveEnumLikeCompanionLike[Employee] {

    @transient
    lazy val nameSerializer: EnumLikeNameSerializer[Employee, Employee.type] =
      EnumLikeNameSerializer.of[Employee, Employee.type](Employee)

    @transient
    lazy val nameKeySerializer: EnumLikeNameKeySerializer[Employee, Employee.type] =
      EnumLikeNameKeySerializer.of[Employee, Employee.type](Employee)

    @transient
    lazy val ordinalSerializer: EnumLikeOrdinalSerializer[Employee, Employee.type] =
      EnumLikeOrdinalSerializer.of[Employee, Employee.type](Employee)

    @transient
    lazy val ordinalKeySerializer: EnumLikeOrdinalKeySerializer[Employee, Employee.type] =
      EnumLikeOrdinalKeySerializer.of[Employee, Employee.type](Employee)

    override lazy val values: IndexedSeq[Employee] = IndexedSeq(
      ErlichBachman,
      RichardHendricks,
      BertramGilfoyle,
      DineshChugtai,
    )
  }

  private case object ErlichBachman extends Employee("ErlichBachman", 1)

  private case object RichardHendricks extends Employee("RichardHendricks", 2)

  private case object BertramGilfoyle extends Employee("BertramGilfoyle", 3)

  private case object DineshChugtai extends Employee("DineshChugtai", 4)

  private final case class PiedPiper(employee: Employee) extends CamelCasedJsonWritable[PiedPiper]

  private object PiedPiper extends CamelCasedJsonWritableCompanionLike[PiedPiper]
}
