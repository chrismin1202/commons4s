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
package com.chrism.commons.datatype

import com.chrism.commons.FunTestSuite

final class EnumLikeTest extends FunTestSuite {

  test("duplicate enum values are not allowed") {
    intercept[IllegalArgumentException] {
      new CaseSensitiveEnumLikeCompanionLike[Alias] {
        override val values: IndexedSeq[Alias] = IndexedSeq(JamesMcGill, JimmyMcGill, SaulGoodman, SlippinJimmy)
      }.ordinalOf(SlippinJimmy)
    }
  }

  test("valueOf: the name cannot be null or blank") {
    intercept[IllegalArgumentException] {
      CaseInsensitiveAlias.valueOf(null)
    }
    intercept[IllegalArgumentException] {
      CaseInsensitiveAlias.valueOf("")
    }
  }

  test("valueOfOrNone: returns None if the name is null or blank") {
    assert(CaseInsensitiveAlias.valueOfOrNone(null).isEmpty)
    assert(CaseInsensitiveAlias.valueOfOrNone("").isEmpty)
  }

  test("ordinalOf: returns the index at which the enum entry appears in `values`") {
    assert(CaseInsensitiveAlias.ordinalOf(JamesMcGill) === 0)
    assert(CaseInsensitiveAlias.ordinalOf(JimmyMcGill) === 1)
    assert(CaseInsensitiveAlias.ordinalOf(SaulGoodman) === 2)
  }

  test("ordinalOf: throws NoSuchElementException if the enum value is not registered") {
    intercept[NoSuchElementException] {
      CaseInsensitiveAlias.ordinalOf(SlippinJimmy)
    }
  }

  test("valueOf: case-insensitive comparison") {
    assert(CaseInsensitiveAlias.valueOf("james mcgill") === JamesMcGill)
  }

  test("valueOfOrNone: case-sensitive comparison") {
    assert(CaseSensitiveAlias.valueOfOrNone("james mcgill").isEmpty)
  }

  test("valueByOrdinalOf: existing ordinal") {
    assert(PiedPiper.valueByOrdinalOf(1) === ErlichBachman)
    assert(PiedPiper.valueByOrdinalOf(2) === RichardHendricks)
    assert(PiedPiper.valueByOrdinalOf(3) === BertramGilfoyle)
    assert(PiedPiper.valueByOrdinalOf(4) === DineshChugtai)
  }

  test("valueByOrdinalOf: non-existing ordinal") {
    intercept[IllegalArgumentException] {
      PiedPiper.valueByOrdinalOf(666)
    }
  }

  test("valueByOrdinalOfOrNone: existing ordinal") {
    assertOption(ErlichBachman, PiedPiper.valueByOrdinalOfOrNone(1))
    assertOption(RichardHendricks, PiedPiper.valueByOrdinalOfOrNone(2))
    assertOption(BertramGilfoyle, PiedPiper.valueByOrdinalOfOrNone(3))
    assertOption(DineshChugtai, PiedPiper.valueByOrdinalOfOrNone(4))
  }

  test("valueByOrdinalOfOrNone: non-existing ordinal") {
    assert(PiedPiper.valueByOrdinalOfOrNone(666).isEmpty)
  }
}

private[this] sealed abstract class Alias(
  override final val name: String,
  override final val aliases: Set[String] = Set.empty,
) extends EnumLike

private[this] object CaseInsensitiveAlias extends CaseInsensitiveEnumLikeCompanionLike[Alias] {

  override lazy val values: IndexedSeq[Alias] = IndexedSeq(JamesMcGill, JimmyMcGill, SaulGoodman)
}

private[this] object CaseSensitiveAlias extends CaseSensitiveEnumLikeCompanionLike[Alias] {

  override lazy val values: IndexedSeq[Alias] = IndexedSeq(JamesMcGill, JimmyMcGill, SaulGoodman)
}

private[this] case object JamesMcGill extends Alias("James McGill")

private[this] case object JimmyMcGill extends Alias("Jimmy McGill")

private[this] case object SaulGoodman extends Alias("Saul Goodman")

private[this] case object SlippinJimmy extends Alias("Slippin' Jimmy", aliases = Set("James McGill"))

private sealed abstract class PiedPiper(override final val name: String, override final val ordinal: Int)
    extends EnumWithOrdinalLike

private object PiedPiper extends CaseInsensitiveEnumLikeCompanionLike[PiedPiper] {

  override lazy val values: IndexedSeq[PiedPiper] = IndexedSeq(
    ErlichBachman,
    RichardHendricks,
    BertramGilfoyle,
    DineshChugtai,
  )
}

private case object ErlichBachman extends PiedPiper("ErlichBachman", 1)

private case object RichardHendricks extends PiedPiper("RichardHendricks", 2)

private case object BertramGilfoyle extends PiedPiper("BertramGilfoyle", 3)

private case object DineshChugtai extends PiedPiper("DineshChugtai", 4)
