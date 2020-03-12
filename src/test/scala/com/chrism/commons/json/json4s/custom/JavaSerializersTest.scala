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

import java.time.{LocalDate, LocalDateTime, Year}
import java.{math => jm}

import com.chrism.commons.FunTestSuite
import com.chrism.commons.json.JsonUtils
import org.json4s.DefaultFormats

final class JavaSerializersTest extends FunTestSuite {

  import JavaSerializersTest.{BigIntObj, LocalDateObj, LocalDateTimeObj, YearObj}

  test("serializing with java.math.BigInteger CustomSerializer: Some") {
    val json = JsonUtils.writeAsJson(BigIntObj(Some(jm.BigInteger.TEN)))(DefaultFormats + BigIntegerSerializer)
    assert(json === """{"bigIntOpt":10}""")
  }

  test("serializing with java.math.BigInteger CustomSerializer: None") {
    val json = JsonUtils.writeAsJson(BigIntObj(None))(DefaultFormats + BigIntegerSerializer)
    assert(json.contains(""""bigIntOpt":""") === false)
  }

  test("serializing with java.math.BigInteger CustomKeySerializer") {
    val map = Map(
      jm.BigInteger.ZERO -> 0,
      jm.BigInteger.ONE -> 1,
      jm.BigInteger.TEN -> 10,
    )
    val json = JsonUtils.writeAsJson(map)(DefaultFormats + BigIntegerKeySerializer)
    assert(json.contains(""""0":0"""))
    assert(json.contains(""""1":1"""))
    assert(json.contains(""""10":10"""))
  }

  test("serializing with java.time.LocalDate CustomSerializer: Some") {
    val date = LocalDateObj(Some(LocalDate.of(2020, 5, 6)))
    val json = JsonUtils.writeAsJson(date)(DefaultFormats + LocalDateSerializer)
    assert(json === """{"date":"2020-05-06"}""")
  }

  test("serializing with java.time.LocalDate CustomSerializer: None") {
    val json = JsonUtils.writeAsJson(LocalDateObj(None))(DefaultFormats + LocalDateSerializer)
    assert(json.contains(""""date":""") === false)
  }

  test("serializing with java.time.LocalDate CustomKeySerializer") {
    val map = Map(
      LocalDate.of(2020, 4, 16) -> 1,
      LocalDate.of(2020, 10, 7) -> 2,
      LocalDate.of(2020, 12, 31) -> 3,
    )
    val json = JsonUtils.writeAsJson(map)(DefaultFormats + LocalDateKeySerializer)
    assert(json.contains(""""2020-04-16":1"""))
    assert(json.contains(""""2020-10-07":2"""))
    assert(json.contains(""""2020-12-31":3"""))
  }

  test("serializing with java.time.LocalDateTime CustomSerializer: Some") {
    val date = LocalDateTimeObj(Some(LocalDateTime.of(2020, 1, 2, 3, 4, 5, 6)))
    val json = JsonUtils.writeAsJson(date)(DefaultFormats + LocalDateTimeSerializer)
    assert(json === """{"dateTime":"2020-01-02T03:04:05.000000006"}""")
  }

  test("serializing with java.time.LocalDateTime CustomSerializer: None") {
    val json = JsonUtils.writeAsJson(LocalDateTimeObj(None))(DefaultFormats + LocalDateTimeSerializer)
    assert(json.contains(""""dateTime":""") === false)
  }

  test("serializing with java.time.LocalDateTime CustomKeySerializer") {
    val map = Map(
      LocalDateTime.of(2020, 1, 2, 3, 4, 5, 6) -> 1,
      LocalDateTime.of(2020, 2, 3, 4, 5, 6, 7) -> 2,
      LocalDateTime.of(2020, 3, 4, 5, 6, 7, 8) -> 3,
    )
    val json = JsonUtils.writeAsJson(map)(DefaultFormats + LocalDateTimeKeySerializer)
    assert(json.contains(""""2020-01-02T03:04:05.000000006":1"""))
    assert(json.contains(""""2020-02-03T04:05:06.000000007":2"""))
    assert(json.contains(""""2020-03-04T05:06:07.000000008":3"""))
  }

  test("serializing with java.time.Year CustomSerializer: Some") {
    val year = YearObj(Some(Year.of(1999)))
    val json = JsonUtils.writeAsJson(year)(DefaultFormats + YearSerializer)
    assert(json === """{"year":"1999"}""")
  }

  test("serializing with java.time.Year CustomSerializer: None") {
    val year = YearObj(None)
    val json = JsonUtils.writeAsJson(year)(DefaultFormats + YearSerializer)
    assert(json.contains(""""year":""") === false)
  }

  test("serializing with java.time.Year CustomKeySerializer") {
    val map = Map(
      Year.of(1999) -> 1,
      Year.of(1888) -> 2,
      Year.of(1777) -> 3,
    )
    val json = JsonUtils.writeAsJson(map)(DefaultFormats + YearKeySerializer)
    assert(json.contains(""""1999":1"""))
    assert(json.contains(""""1888":2"""))
    assert(json.contains(""""1777":3"""))
  }
}

private[this] object JavaSerializersTest {

  private final case class BigIntObj(bigIntOpt: Option[jm.BigInteger])

  private final case class LocalDateObj(date: Option[LocalDate])

  private final case class LocalDateTimeObj(dateTime: Option[LocalDateTime])

  private final case class YearObj(year: Option[Year])
}
