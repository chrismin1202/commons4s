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

final class CollectionUtilsTest extends FunTestSuite {

  import CollectionUtils.implicits._

  test("optionizing Seq: empty string") {
    assert(CollectionUtils.toNoneIfEmpty(Seq.empty[String]).isEmpty)
  }

  test("optionizing Seq implicitly: empty string") {
    assert(Seq.empty[String].asNoneIfEmpty.isEmpty)
  }

  test("optionizing Seq: non-empty string") {
    val seq = CollectionUtils.toNoneIfEmpty(Seq("a", "b", "c")).getOrFail()
    seq should contain theSameElementsInOrderAs Seq("a", "b", "c")
  }

  test("optionizing Seq implicitly: non-empty string") {
    val seq = Seq("a", "b", "c").asNoneIfEmpty.getOrFail()
    seq should contain theSameElementsInOrderAs Seq("a", "b", "c")
  }

  test("optionizing Map: empty") {
    assert(CollectionUtils.toNoneIfEmpty(Map.empty[String, String]).isEmpty)
  }

  test("optionizing Map implicitly: empty") {
    assert(Map.empty[String, String].asNoneIfEmpty.isEmpty)
  }

  test("optionizing Map: non-empty") {
    val map = CollectionUtils.toNoneIfEmpty(Map("a" -> 1, "b" -> 2, "c" -> 3)).getOrFail()
    map should contain theSameElementsAs Map("a" -> 1, "b" -> 2, "c" -> 3)
  }

  test("optionizing Map implicitly: non-empty") {
    val map = Map("a" -> 1, "b" -> 2, "c" -> 3).asNoneIfEmpty.getOrFail()
    map should contain theSameElementsAs Map("a" -> 1, "b" -> 2, "c" -> 3)
  }

  test("filtering empty string values implicitly: all blank") {
    val seq = Seq("", " ")
    assert(seq.anyNonBlank === false)
    assert(seq.allBlank)
    assert(seq.filterNonBlankTrimmedOrNoneIfAllBlank.isEmpty)
  }

  test("filtering empty string values implicitly: some non-blank") {
    val seq = Seq("", " ", " non-blank ")
    assert(seq.anyNonBlank)
    assert(seq.allBlank === false)
    seq.filterNonBlankTrimmedOrNoneIfAllBlank.getOrFail() should contain theSameElementsInOrderAs Seq("non-blank")
  }

  test("filtering empty CanBeEmpty values implicitly: all empty") {
    val seq = Seq(CanBeEmptyDummy(None), CanBeEmptyDummy(None))
    assert(seq.anyNonEmpty === false)
    assert(seq.allEmpty)
    assert(seq.filterNonEmptyOrNoneIfAllEmpty.isEmpty)
  }

  test("filtering empty CanBeEmpty values implicitly: some non-empty") {
    val seq = Seq(CanBeEmptyDummy(None), CanBeEmptyDummy(None), CanBeEmptyDummy(Some(1)), CanBeEmptyDummy(Some(2)))
    assert(seq.anyNonEmpty)
    assert(seq.allEmpty === false)
    seq.filterNonEmptyOrNoneIfAllEmpty.getOrFail() should contain theSameElementsInOrderAs Seq(
      CanBeEmptyDummy(Some(1)),
      CanBeEmptyDummy(Some(2))
    )
  }
}
