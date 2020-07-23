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

import java.time.temporal.ChronoUnit
import java.time.{Instant, LocalDate, LocalDateTime, LocalTime}

import com.chrism.commons.FunTestSuite

final class OrderingUtilsTest extends FunTestSuite {

  import DateTimeUtils.implicits._
  import OrderingUtils.implicits._

  test("ordering java.time.LocalDate") {
    val t = LocalDate.now()
    val past = t.minus(1L, ChronoUnit.DAYS)
    val future = t.plus(1L, ChronoUnit.DAYS)
    Seq(future, past, t).sorted should contain theSameElementsInOrderAs Seq(past, t, future)
  }

  test("ordering java.time.LocalTime") {
    val t = LocalTime.now()
    val past = t.minus(1L, ChronoUnit.HOURS)
    val future = t.plus(1L, ChronoUnit.HOURS)
    Seq(future, past, t).sorted should contain theSameElementsInOrderAs Seq(past, t, future)
  }

  test("ordering java.time.LocalDateTime") {
    val t = LocalDateTime.now()
    val past = t.minus(1L, ChronoUnit.MONTHS)
    val future = t.plus(1L, ChronoUnit.MONTHS)
    Seq(future, past, t).sorted should contain theSameElementsInOrderAs Seq(past, t, future)
  }

  test("ordering java.time.Instant") {
    val t = Instant.now()
    val past = t.minus(1L, ChronoUnit.SECONDS)
    val future = t.plus(1L, ChronoUnit.SECONDS)
    Seq(future, past, t).sorted should contain theSameElementsInOrderAs Seq(past, t, future)
  }

  test("ordering java.sql.Date") {
    val t = LocalDate.now()
    val past = t.minus(1L, ChronoUnit.DAYS)
    val future = t.plus(1L, ChronoUnit.DAYS)

    val dates = Seq(future, past, t).map(_.asSqlDate)
    dates.sorted should contain theSameElementsInOrderAs Seq(past, t, future).map(_.asSqlDate)
  }

  test("ordering java.sql.Time") {
    val t = LocalTime.now()
    val past = t.minus(1L, ChronoUnit.HOURS)
    val future = t.plus(1L, ChronoUnit.HOURS)

    val times = Seq(future, past, t).map(_.asSqlTime)
    times.sorted should contain theSameElementsInOrderAs Seq(past, t, future).map(_.asSqlTime)
  }

  test("ordering java.sql.Timestamp") {
    val t = LocalDateTime.now()
    val past = t.minus(1L, ChronoUnit.MONTHS)
    val future = t.plus(1L, ChronoUnit.MONTHS)

    val times = Seq(future, past, t).map(_.asSqlTimestamp)
    times.sorted should contain theSameElementsInOrderAs Seq(past, t, future).map(_.asSqlTimestamp)
  }

  test("ordering Option of Comparable") {
    import OrderingUtilsTest.ComparableDummy

    val dummies = None +: Seq(ComparableDummy(3), ComparableDummy(1), ComparableDummy(2)).map(Some(_))
    dummies.sorted should contain theSameElementsInOrderAs Seq(
      Some(ComparableDummy(1)),
      Some(ComparableDummy(2)),
      Some(ComparableDummy(3)),
      None,
    )
  }

  test("ordering Option of Ordered") {
    import OrderingUtilsTest.OrderedDummy

    val dummies = None +: Seq(OrderedDummy(3), OrderedDummy(1), OrderedDummy(2)).map(Some(_))
    dummies.sorted should contain theSameElementsInOrderAs Seq(
      Some(OrderedDummy(1)),
      Some(OrderedDummy(2)),
      Some(OrderedDummy(3)),
      None,
    )
  }

  test("ordering Option of neither Comparable nor Ordered") {
    import OrderingUtilsTest.Dummy

    implicit val dummyOrdering: Ordering[Dummy] = (x: Dummy, y: Dummy) => x.i.compare(y.i)

    val dummies = None +: Seq(Dummy(3), Dummy(1), Dummy(2)).map(Some(_))
    dummies.sorted should contain theSameElementsInOrderAs Seq(
      Some(Dummy(1)),
      Some(Dummy(2)),
      Some(Dummy(3)),
      None,
    )
  }

  test("ordering Option of java.time.LocalDate") {
    val t = LocalDate.now()
    val past = t.minus(1L, ChronoUnit.DAYS)
    val future = t.plus(1L, ChronoUnit.DAYS)

    val dates = None +: Seq(future, past, t).map(Some(_))
    dates.sorted should contain theSameElementsInOrderAs Seq(
      Some(past),
      Some(t),
      Some(future),
      None,
    )
  }

  test("ordering Option of java.time.LocalTime") {
    val t = LocalTime.now()
    val past = t.minus(1L, ChronoUnit.HOURS)
    val future = t.plus(1L, ChronoUnit.HOURS)

    val times = None +: Seq(future, past, t).map(Some(_))
    times.sorted should contain theSameElementsInOrderAs Seq(
      Some(past),
      Some(t),
      Some(future),
      None,
    )
  }

  test("ordering Option of java.time.LocalDateTime") {
    val t = LocalDateTime.now()
    val past = t.minus(1L, ChronoUnit.MONTHS)
    val future = t.plus(1L, ChronoUnit.MONTHS)

    val dateTimes = None +: Seq(future, past, t).map(Some(_))
    dateTimes.sorted should contain theSameElementsInOrderAs Seq(
      Some(past),
      Some(t),
      Some(future),
      None,
    )
  }

  test("ordering Option of java.time.Instant") {
    val t = Instant.now()
    val past = t.minus(1L, ChronoUnit.SECONDS)
    val future = t.plus(1L, ChronoUnit.SECONDS)

    val instants = None +: Seq(future, past, t).map(Some(_))
    instants.sorted should contain theSameElementsInOrderAs Seq(
      Some(past),
      Some(t),
      Some(future),
      None,
    )
  }

  test("ordering Option of java.sql.Date") {
    val t = LocalDate.now()
    val past = t.minus(1L, ChronoUnit.DAYS)
    val future = t.plus(1L, ChronoUnit.DAYS)

    val dates = None +: Seq(future, past, t).map(_.asSqlDate).map(Some(_))
    dates.sorted should contain theSameElementsInOrderAs Seq(
      Some(past.asSqlDate),
      Some(t.asSqlDate),
      Some(future.asSqlDate),
      None,
    )
  }

  test("ordering Option of java.sql.Time") {
    val t = LocalTime.now()
    val past = t.minus(1L, ChronoUnit.HOURS)
    val future = t.plus(1L, ChronoUnit.HOURS)

    val times = None +: Seq(future, past, t).map(_.asSqlTime).map(Some(_))
    times.sorted should contain theSameElementsInOrderAs Seq(
      Some(past.asSqlTime),
      Some(t.asSqlTime),
      Some(future.asSqlTime),
      None,
    )
  }

  test("ordering Option of java.sql.Timestamp") {
    val t = LocalDateTime.now()
    val past = t.minus(1L, ChronoUnit.MONTHS)
    val future = t.plus(1L, ChronoUnit.MONTHS)

    val times = None +: Seq(future, past, t).map(_.asSqlTimestamp).map(Some(_))
    times.sorted should contain theSameElementsInOrderAs Seq(
      Some(past.asSqlTimestamp),
      Some(t.asSqlTimestamp),
      Some(future.asSqlTimestamp),
      None,
    )
  }
}

private[this] object OrderingUtilsTest {

  private final case class Dummy(i: Int)

  private final case class ComparableDummy(i: Int) extends Comparable[ComparableDummy] {

    override def compareTo(that: ComparableDummy): Int = i.compareTo(that.i)
  }

  private final case class OrderedDummy(i: Int) extends Ordered[OrderedDummy] {

    override def compare(that: OrderedDummy): Int = i.compare(that.i)
  }
}
