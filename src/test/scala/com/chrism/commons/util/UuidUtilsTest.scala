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

final class UuidUtilsTest extends FunTestSuite {

  test("generating UUID: case-sensitive, blank") {
    intercept[IllegalArgumentException] {
      UuidUtils.toUuidCaseSensitive(null)
    }
    assert(UuidUtils.toUuidCaseSensitiveOrNone(null).isEmpty)
  }

  test("generating UUID: case-sensitive, non-blank") {
    val mixedCased = UuidUtils.toUuidCaseSensitive("Saul.Goodman@BetterCallSaul.com").toString
    assert(mixedCased === "fff18fb7-e109-3758-bae8-4ae04bd636ab")
    assertOption(
      "2c041267-718e-3976-931b-572a13bd0fec",
      UuidUtils.toUuidCaseSensitiveOrNone("saul.goodman@betercallsaul.com").map(_.toString)
    )
  }

  test("generating UUID: case-insensitive, blank") {
    intercept[IllegalArgumentException] {
      UuidUtils.toUuidCaseInsensitive(null)
    }
    assert(UuidUtils.toUuidCaseInsensitiveOrNone(null).isEmpty)
  }

  test("generating UUID: case-insensitive, non-blank") {
    val lowerCased = UuidUtils.toUuidCaseInsensitive("saul.goodman@bettercallsaul.com").toString
    val upperCased = UuidUtils.toUuidCaseInsensitive("SAUL.GOODMAN@BETTERCALLSAUL.COM").toString
    val mixedCased = UuidUtils.toUuidCaseInsensitive("Saul.Goodman@BetterCallSaul.com").toString
    assert(lowerCased === upperCased)
    assert(lowerCased === mixedCased)
    assert(upperCased === mixedCased)
  }
}
