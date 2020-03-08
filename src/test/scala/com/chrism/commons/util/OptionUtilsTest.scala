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
package com.chrism.commons.util

import com.chrism.commons.FunTestSuite
import com.chrism.commons.io.CanBeEmptyDummy

final class OptionUtilsTest extends FunTestSuite {

  import OptionUtils.implicits._

  test("checking for Option of empty collection") {
    assert(OptionUtils.isValueEmpty(Some(Seq.empty[String])))
    assert(OptionUtils.isValueEmpty(Some(Map.empty[Int, Long])))
  }

  test("checking for Option of empty collection implicitly") {
    assert(Some(Seq.empty[String]).isValueEmpty)
    assert(Some(Map.empty[Int, Long]).isValueEmpty)
  }

  test("checking for Option of non-empty collection") {
    assert(OptionUtils.isValueEmpty(Some(Seq("a", "b"))) === false)
    assert(OptionUtils.isValueEmpty(Some(Map(1 -> "one", 2 -> "two"))) === false)
  }

  test("checking for Option of non-empty collection implicitly") {
    assert(Some(Seq("a", "b")).isValueEmpty === false)
    assert(Some(Map(1 -> "one", 2 -> "two")).isValueEmpty === false)
  }

  test("returning None for Option of empty collection") {
    assert(OptionUtils.noneIfEmpty(Some(Seq.empty[String])).isEmpty)
    assert(OptionUtils.noneIfEmpty(Some(Map.empty[Int, Long])).isEmpty)
  }

  test("returning None for Option of empty collection implicitly") {
    assert(Some(Seq.empty[String]).noneIfEmpty.isEmpty)
    assert(Some(Map.empty[Int, Long]).noneIfEmpty.isEmpty)
  }

  test("returning None for Option of non-empty collection") {
    assert(OptionUtils.noneIfEmpty(Some(Seq("a", "b"))) === Some(Seq("a", "b")))
    assert(OptionUtils.noneIfEmpty(Some(Map(1 -> "one", 2 -> "two"))) === Some(Map(1 -> "one", 2 -> "two")))
  }

  test("returning None for Option of non-empty collection implicitly") {
    assert(Some(Seq("a", "b")).noneIfEmpty === Some(Seq("a", "b")))
    assert(Some(Map(1 -> "one", 2 -> "two")).noneIfEmpty === Some(Map(1 -> "one", 2 -> "two")))
  }

  test("filtering empty string values implicitly: all blank") {
    val o = Some(Seq("", " "))
    assert(o.anyNonBlank === false)
    assert(o.allBlank)
    assert(o.filterNonBlankTrimmedOrNoneIfAllBlank.isEmpty)
  }

  test("filtering empty string values implicitly: some non-blank") {
    val o = Some(Seq("", " ", " non-blank "))
    assert(o.anyNonBlank)
    assert(o.allBlank === false)
    o.filterNonBlankTrimmedOrNoneIfAllBlank.getOrFail() should contain theSameElementsInOrderAs Seq("non-blank")
  }

  test("filtering empty CanBeEmpty values implicitly: all empty") {
    val o = Some(Seq(CanBeEmptyDummy(None), CanBeEmptyDummy(None)))
    assert(o.anyNonEmpty === false)
    assert(o.allEmpty)
    assert(o.filterNonEmptyOrNoneIfAllEmpty.isEmpty)
  }

  test("filtering empty CanBeEmpty values implicitly: some non-empty") {
    val o =
      Some(Seq(CanBeEmptyDummy(None), CanBeEmptyDummy(None), CanBeEmptyDummy(Some(1)), CanBeEmptyDummy(Some(2))))
    assert(o.anyNonEmpty)
    assert(o.allEmpty === false)
    o.filterNonEmptyOrNoneIfAllEmpty.getOrFail() should contain theSameElementsInOrderAs Seq(
      CanBeEmptyDummy(Some(1)),
      CanBeEmptyDummy(Some(2))
    )
  }
}
