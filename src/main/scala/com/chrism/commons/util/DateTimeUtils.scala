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

import java.time.{Instant, LocalDate, LocalDateTime, LocalTime, Month, OffsetDateTime, OffsetTime, ZoneId, ZoneOffset}
import java.{sql => js}

object DateTimeUtils {

  def toSqlDate(ld: LocalDate): js.Date = js.Date.valueOf(ld)

  def toSqlDate(odt: OffsetDateTime): js.Date = toSqlDate(odt.toLocalDate)

  def toSqlTime(lt: LocalTime): js.Time = js.Time.valueOf(lt)

  def toSqlTime(ot: OffsetTime): js.Time = toSqlTime(ot.toLocalTime)

  def toSqlTimestamp(instant: Instant): js.Timestamp = js.Timestamp.from(instant)

  def toSqlTimestamp(ldt: LocalDateTime): js.Timestamp = js.Timestamp.valueOf(ldt)

  def toSqlTimestamp(odt: OffsetDateTime): js.Timestamp = toSqlTimestamp(odt.toInstant)

  def toOffsetTime(time: js.Time, offset: ZoneOffset): OffsetTime = OffsetTime.of(time.toLocalTime, offset)

  def toOffsetDateTime(timestamp: js.Timestamp, offset: ZoneOffset): OffsetDateTime =
    OffsetDateTime.of(timestamp.toLocalDateTime, offset)

  def sqlDateNow(): js.Date = toSqlDate(LocalDate.now())

  def sqlDateNowAtZone(zone: ZoneId): js.Date = toSqlDate(OffsetDateTime.now(zone))

  def sqlDateOf(year: Int, month: Int, dayOfMonth: Int): js.Date = toSqlDate(LocalDate.of(year, month, dayOfMonth))

  def sqlDateOf(year: Int, month: Month, dayOfMonth: Int): js.Date = toSqlDate(LocalDate.of(year, month, dayOfMonth))

  def sqlTimeNow(): js.Time = toSqlTime(LocalTime.now())

  def sqlTimeNowAtZone(zone: ZoneId): js.Time = toSqlTime(OffsetTime.now(zone))

  def sqlTimeOf(hour: Int, minute: Int, second: Int, nanoOfSecond: Int): js.Time =
    toSqlTime(LocalTime.of(hour, minute, second, nanoOfSecond))

  def sqlTimeOf(hour: Int, minute: Int, second: Int): js.Time = toSqlTime(LocalTime.of(hour, minute, second))

  def sqlTimeOf(hour: Int, minute: Int): js.Time = toSqlTime(LocalTime.of(hour, minute))

  def sqlTimeOf(hour: Int, minute: Int, second: Int, nanoOfSecond: Int, offset: ZoneOffset): js.Time =
    toSqlTime(OffsetTime.of(hour, minute, second, nanoOfSecond, offset))

  def sqlTimeOf(hour: Int, minute: Int, second: Int, offset: ZoneOffset): js.Time =
    sqlTimeOf(hour, minute, second, 0, offset)

  def sqlTimeOf(hour: Int, minute: Int, offset: ZoneOffset): js.Time = sqlTimeOf(hour, minute, 0, offset)

  def sqlTimestampNow(): js.Timestamp = toSqlTimestamp(LocalDateTime.now())

  def sqlTimestampNowAtZone(zone: ZoneId): js.Timestamp = toSqlTimestamp(OffsetDateTime.now(zone))

  def sqlTimestampOf(
    year: Int,
    month: Int,
    dayOfMonth: Int,
    hour: Int,
    minute: Int,
    second: Int,
    nanoOfSecond: Int
  ): js.Timestamp =
    toSqlTimestamp(LocalDateTime.of(year, month, dayOfMonth, hour, minute, second, nanoOfSecond))

  def sqlTimestampOf(
    year: Int,
    month: Month,
    dayOfMonth: Int,
    hour: Int,
    minute: Int,
    second: Int,
    nanoOfSecond: Int
  ): js.Timestamp =
    toSqlTimestamp(LocalDateTime.of(year, month, dayOfMonth, hour, minute, second, nanoOfSecond))

  def sqlTimestampOf(year: Int, month: Int, dayOfMonth: Int, hour: Int, minute: Int, second: Int): js.Timestamp =
    toSqlTimestamp(LocalDateTime.of(year, month, dayOfMonth, hour, minute, second))

  def sqlTimestampOf(year: Int, month: Month, dayOfMonth: Int, hour: Int, minute: Int, second: Int): js.Timestamp =
    toSqlTimestamp(LocalDateTime.of(year, month, dayOfMonth, hour, minute, second))

  def sqlTimestampOf(year: Int, month: Int, dayOfMonth: Int, hour: Int, minute: Int): js.Timestamp =
    toSqlTimestamp(LocalDateTime.of(year, month, dayOfMonth, hour, minute))

  def sqlTimestampOf(year: Int, month: Month, dayOfMonth: Int, hour: Int, minute: Int): js.Timestamp =
    toSqlTimestamp(LocalDateTime.of(year, month, dayOfMonth, hour, minute))

  def sqlTimestampOf(
    year: Int,
    month: Int,
    dayOfMonth: Int,
    hour: Int,
    minute: Int,
    second: Int,
    nanoOfSecond: Int,
    offset: ZoneOffset
  ): js.Timestamp =
    toSqlTimestamp(OffsetDateTime.of(year, month, dayOfMonth, hour, minute, second, nanoOfSecond, offset))

  def sqlTimestampOf(
    year: Int,
    month: Month,
    dayOfMonth: Int,
    hour: Int,
    minute: Int,
    second: Int,
    nanoOfSecond: Int,
    offset: ZoneOffset
  ): js.Timestamp =
    sqlTimestampOf(year, month.getValue, dayOfMonth, hour, minute, second, nanoOfSecond, offset)

  def sqlTimestampOf(
    year: Int,
    month: Int,
    dayOfMonth: Int,
    hour: Int,
    minute: Int,
    second: Int,
    offset: ZoneOffset
  ): js.Timestamp =
    sqlTimestampOf(year, month, dayOfMonth, hour, minute, second, 0, offset)

  def sqlTimestampOf(
    year: Int,
    month: Month,
    dayOfMonth: Int,
    hour: Int,
    minute: Int,
    second: Int,
    offset: ZoneOffset
  ): js.Timestamp =
    sqlTimestampOf(year, month.getValue, dayOfMonth, hour, minute, second, offset)

  def sqlTimestampOf(year: Int, month: Int, dayOfMonth: Int, hour: Int, minute: Int, offset: ZoneOffset): js.Timestamp =
    sqlTimestampOf(year, month, dayOfMonth, hour, minute, 0, offset)

  def sqlTimestampOf(
    year: Int,
    month: Month,
    dayOfMonth: Int,
    hour: Int,
    minute: Int,
    offset: ZoneOffset
  ): js.Timestamp =
    sqlTimestampOf(year, month.getValue, dayOfMonth, hour, minute, offset)

  object implicits {

    implicit final class LocalDateOps(ld: LocalDate) {

      def asSqlDate: js.Date = toSqlDate(ld)

      def asSqlTimestamp(time: LocalTime): js.Timestamp = toSqlTimestamp(ld.atTime(time))

      def asSqlTimestampAtStartOfDay: js.Timestamp = asSqlTimestamp(LocalTime.MIDNIGHT)
    }

    implicit final class LocalTimeOps(lt: LocalTime) {

      def asSqlTime: js.Time = toSqlTime(lt)

      def asSqlTimestamp(date: LocalDate): js.Timestamp = toSqlTimestamp(lt.atDate(date))
    }

    implicit final class LocalDateTimeOps(ldt: LocalDateTime) {

      def asSqlTimestamp: js.Timestamp = toSqlTimestamp(ldt)

      def asSqlDate: js.Date = ldt.toLocalDate.asSqlDate

      def asSqlTime: js.Time = ldt.toLocalTime.asSqlTime
    }

    implicit final class InstantOps(instant: Instant) {

      def asSqlTimestamp: js.Timestamp = js.Timestamp.from(instant)
    }

    implicit final class OffsetTimeOps(ot: OffsetTime) {

      def asSqlTime: js.Time = toSqlTime(ot)

      def asSqlTimestamp(date: LocalDate): js.Timestamp = toSqlTimestamp(ot.atDate(date))
    }

    implicit final class OffsetDateTimeOps(odt: OffsetDateTime) {

      def asSqlTimestamp: js.Timestamp = toSqlTimestamp(odt)

      def asSqlDate: js.Date = toSqlDate(odt)

      def asSqlTime: js.Time = odt.toOffsetTime.asSqlTime
    }
  }
}
