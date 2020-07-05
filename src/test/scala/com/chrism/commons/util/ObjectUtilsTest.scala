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

final class ObjectUtilsTest extends FunTestSuite {

  test("requireNonNull") {
    val nullStr: String = null
    intercept[NullPointerException] {
      ObjectUtils.requireNonNull(nullStr)
    }
  }

  test("requireNonNull with message") {
    val nullStr: String = null
    intercept[NullPointerException] {
      ObjectUtils.requireNonNull(nullStr, "The string is empty!")
    }
  }

  test("requireNonNull with Throwable") {
    val nullStr: String = null
    intercept[IllegalArgumentException] {
      ObjectUtils.requireNonNullOrThrow(nullStr, new IllegalArgumentException("Missing"))
    }
  }

  test("coalesce returns the first non-null element") {
    assert(ObjectUtils.coalesce(null, "s1", "s2", null, "s3") === "s1")
  }

  test("coalesce returns null when all elements are null") {
    assert(ObjectUtils.coalesce[String](null, null, null) === null)
  }

  test("firstNonEmpty returns the first non-empty element") {
    assertOption("s1", ObjectUtils.firstNonEmpty(None, Some("s1"), Some("s2"), None, Some("s3")))
  }

  test("firstNonEmpty returns None when all elements are empty") {
    assert(ObjectUtils.firstNonEmpty[String](None, None, None).isEmpty)
  }

  test("firstNonEmpty returns the first non-null element") {
    assertOption("s1", ObjectUtils.firstNonEmpty(null, Some("s1"), Some("s2"), null, Some("s3")))
  }

  test("firstNonEmpty returns None when all elements are null") {
    assert(ObjectUtils.firstNonEmpty[String](null, null, null).isEmpty)
  }

  test("nonEmptyOrNone returns None when the given collection is empty") {
    assert(ObjectUtils.nonEmptyOrNone(Seq.empty[String]).isEmpty)
  }

  test("nonEmptyOrNone returns None when the given string is empty") {
    import ObjectUtilsTest._

    assert(ObjectUtils.nonEmptyOrNone(DummyEmptiable("")).isEmpty)
  }

  test("hashOf: 0 element results in IllegalArgumentException") {
    intercept[IllegalArgumentException] {
      ObjectUtils.hashOf(Seq.empty)
    }
  }

  test("hashOf: 1 element") {
    assert(ObjectUtils.hashOf("") === "".hashCode)
    assert(ObjectUtils.hashOf(Seq("")) === "".hashCode)
  }

  test("hashOf: more than 1 element") {
    val hash1 = ObjectUtils.hashOf("", 1, 2.0, false, true)
    val hash2 = ObjectUtils.hashOf(Seq("", 1, 2.0, false, true))
    assert(hash1 === hash2)
  }

  test("hashOf consistently generates the same hash value") {
    val hashes = (1 to 1000).map(_ => ObjectUtils.hashOf("", 1, 2L, 3.0f, 4.0, false, true))
    val expectedHash = hashes.head
    assert(hashes.tail.forall(_ == expectedHash))
  }
}

private[this] object ObjectUtilsTest {

  private final case class DummyEmptiable(s: String) {

    def isEmpty: Boolean = StringUtils.isBlank(s)
  }
}
