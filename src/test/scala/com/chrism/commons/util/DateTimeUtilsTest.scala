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

import java.time.{LocalDate, LocalDateTime, LocalTime, OffsetDateTime, OffsetTime, ZoneOffset}

import com.chrism.commons.FunTestSuite

final class DateTimeUtilsTest extends FunTestSuite {

  import DateTimeUtils.implicits._

  test("implicitly converting java.time.LocalDate to java.sql.Date") {
    val expected = DateTimeUtils.sqlDateOf(2020, 1, 1)
    assert(LocalDate.of(2020, 1, 1).asSqlDate === expected)
  }

  test("implicitly converting java.time.LocalDate to java.sql.Timestamp") {
    val expected = DateTimeUtils.sqlTimestampOf(2020, 1, 2, 3, 4, 5, 6)
    val time = LocalTime.of(3, 4, 5, 6)
    assert(LocalDate.of(2020, 1, 2).asSqlTimestamp(time) === expected)
  }

  test("implicitly converting java.time.LocalTime to java.sql.Time") {
    val expected = DateTimeUtils.sqlTimeOf(1, 2, 3, 4)
    assert(LocalTime.of(1, 2, 3, 4).asSqlTime === expected)
  }

  test("implicitly converting java.time.LocalTime to java.sql.Timestamp") {
    val expected = DateTimeUtils.sqlTimestampOf(2020, 1, 2, 3, 4, 5, 6)
    val date = LocalDate.of(2020, 1, 2)
    assert(LocalTime.of(3, 4, 5, 6).asSqlTimestamp(date) === expected)
  }

  test("implicitly converting java.time.LocalDateTime to java.sql.Timestamp") {
    val expected = DateTimeUtils.sqlTimestampOf(2020, 1, 2, 3, 4, 5, 6)
    assert(LocalDateTime.of(2020, 1, 2, 3, 4, 5, 6).asSqlTimestamp === expected)
  }

  test("implicitly converting java.time.LocalDateTime to java.sql.Date") {
    val expected = DateTimeUtils.sqlDateOf(2020, 1, 2)
    assert(LocalDateTime.of(2020, 1, 2, 3, 4, 5, 6).asSqlDate === expected)
  }

  test("implicitly converting java.time.LocalDateTime to java.sql.Time") {
    val expected = DateTimeUtils.sqlTimeOf(3, 4, 5, 6)
    assert(LocalDateTime.of(2020, 1, 2, 3, 4, 5, 6).asSqlTime === expected)
  }

  test("implicitly converting java.time.Instant to java.sql.Timestamp") {
    val instant = OffsetDateTime.of(2020, 1, 2, 3, 4, 5, 6, ZoneOffset.of("Z")).toInstant
    val expected = DateTimeUtils.sqlTimestampOf(2020, 1, 2, 3, 4, 5, 6, ZoneOffset.of("Z"))
    assert(instant.asSqlTimestamp === expected)
  }

  test("implicitly converting java.time.OffsetTime to java.sql.Time") {
    val time = OffsetTime.of(1, 2, 3, 4, ZoneOffset.of("Z"))
    val expected = DateTimeUtils.sqlTimeOf(1, 2, 3, 4, ZoneOffset.of("Z"))
    assert(time.asSqlTime === expected)
  }

  test("implicitly converting java.time.OffsetTime to java.sql.Timestamp") {
    val time = OffsetTime.of(3, 4, 5, 6, ZoneOffset.of("Z"))
    val expected = DateTimeUtils.sqlTimestampOf(2020, 1, 2, 3, 4, 5, 6, ZoneOffset.of("Z"))
    assert(time.asSqlTimestamp(LocalDate.of(2020, 1, 2)) === expected)
  }

  test("implicitly converting java.time.OffsetDateTime to java.sql.Timestamp") {
    val time = OffsetDateTime.of(2020, 1, 2, 3, 4, 5, 6, ZoneOffset.of("Z"))
    val expected = DateTimeUtils.sqlTimestampOf(2020, 1, 2, 3, 4, 5, 6, ZoneOffset.of("Z"))
    assert(time.asSqlTimestamp === expected)
  }

  test("implicitly converting java.time.OffsetDateTime to java.sql.Date") {
    val dt = OffsetDateTime.of(2020, 1, 2, 3, 4, 5, 6, ZoneOffset.of("Z"))
    val expected = DateTimeUtils.sqlDateOf(2020, 1, 2)
    assert(dt.asSqlDate === expected)
  }

  test("implicitly converting java.time.OffsetDateTime to java.sql.Time") {
    val dt = OffsetDateTime.of(2020, 1, 2, 3, 4, 5, 6, ZoneOffset.of("Z"))
    val expected = DateTimeUtils.sqlTimeOf(3, 4, 5, 6, ZoneOffset.of("Z"))
    assert(dt.asSqlTime === expected)
  }
}
