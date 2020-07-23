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

import java.time.{Instant, LocalDate, LocalDateTime, LocalTime}
import java.{sql => js}

object OrderingUtils {

  /** Used for defining [[Ordering]] of a type that extends [[Comparable]] interface.
    * Scala class typically extends [[Ordered]] trait, which in turn extends [[Comparable]] interface, but
    * if the class extends [[Comparable]] interface directly or is a Java class, this trait can be used to define
    * [[Ordering]] for the class.
    *
    * @tparam C the type that extends [[Comparable]] interface
    */
  trait ComparableOrdering[C <: Comparable[C]] extends Ordering[C] {

    override /* overridable */ def compare(x: C, y: C): Int = x.compareTo(y)
  }

  /** Used for defining [[Ordering]] of [[Option]] of [[T]].
    * As opposed to Scala's default [[Option]] [[Ordering]], which orders [[None]] before [[Some]],
    * this trait sorts [[Some]] before [[None]].
    * - If both [[Option]] instances being compared are [[Some]], {{{ def compareT(x: T, y: T): Int }}} is invoked.
    * - If the first instance {{{ x }}} is [[Some]], but the second instance {{{ y }}} is [[None]],
    *   a negative integer is returned.
    * - If the first instance {{{ x }}} is [[None]], but the second instance {{{ y }}} is [[Some]],
    *   a positive integer is returned.
    * - If both [[Option]] instances being compared are [[None]], 0 is returned.
    *
    * @tparam T the type to compare
    */
  trait OptionOrdering[T] extends Ordering[Option[T]] {

    /** Compares the two given objects for order.
      *
      * @param x the first value
      * @param y the second value
      * @return a negative integer if {{{ x }}} should be placed before {{{ y }}},
      *         a positive integer if {{{ y }}} should be placed before {{{ x }}},
      *         else 0
      */
    def compareT(x: T, y: T): Int

    override /* overridable */ def compare(x: Option[T], y: Option[T]): Int =
      (x, y) match {
        case (Some(xVal), Some(yVal)) => compareT(xVal, yVal)
        case (Some(_), _)             => -1
        case (_, Some(_))             => 1
        case _                        => 0
      }
  }

  trait OrderedOptionOrdering[T <: Ordered[T]] extends OptionOrdering[T] {

    override def compareT(x: T, y: T): Int = x.compare(y)
  }

  trait ComparableOptionOrdering[T <: Comparable[T]] extends OptionOrdering[T] {

    override def compareT(x: T, y: T): Int = x.compareTo(y)
  }

  object LocalDateOrdering extends Ordering[LocalDate] {

    override def compare(x: LocalDate, y: LocalDate): Int = x.compareTo(y)
  }

  object LocalTimeOrdering extends ComparableOrdering[LocalTime]

  object LocalDateTimeOrdering extends Ordering[LocalDateTime] {

    override def compare(x: LocalDateTime, y: LocalDateTime): Int = x.compareTo(y)
  }

  object InstantOrdering extends ComparableOrdering[Instant]

  object SqlDateOrdering extends Ordering[js.Date] {

    override def compare(x: js.Date, y: js.Date): Int = x.compareTo(y)
  }

  object SqlTimeOrdering extends Ordering[js.Time] {

    override def compare(x: js.Time, y: js.Time): Int = x.compareTo(y)
  }

  object SqlTimestampOrdering extends Ordering[js.Timestamp] {

    override def compare(x: js.Timestamp, y: js.Timestamp): Int = x.compareTo(y)
  }

  object LocalDateOptionOrdering extends OptionOrdering[LocalDate] {

    override def compareT(x: LocalDate, y: LocalDate): Int = x.compareTo(y)
  }

  object LocalTimeOptionOrdering extends OptionOrdering[LocalTime] {

    override def compareT(x: LocalTime, y: LocalTime): Int = x.compareTo(y)
  }

  object LocalDateTimeOptionOrdering extends OptionOrdering[LocalDateTime] {

    override def compareT(x: LocalDateTime, y: LocalDateTime): Int = x.compareTo(y)
  }

  object InstantOptionOrdering extends ComparableOptionOrdering[Instant]

  object SqlDateOptionOrdering extends OptionOrdering[js.Date] {

    override def compareT(x: js.Date, y: js.Date): Int = x.compareTo(y)
  }

  object SqlTimeOptionOrdering extends OptionOrdering[js.Time] {

    override def compareT(x: js.Time, y: js.Time): Int = x.compareTo(y)
  }

  object SqlTimestampOptionOrdering extends OptionOrdering[js.Timestamp] {

    override def compareT(x: js.Timestamp, y: js.Timestamp): Int = x.compareTo(y)
  }

  def optionOrderingOf[T](implicit ordering: Ordering[T]): Ordering[Option[T]] =
    new OptionOrdering[T] {
      override def compareT(x: T, y: T): Int = ordering.compare(x, y)
    }

  def orderedOptionOrderingOf[T <: Ordered[T]]: Ordering[Option[T]] = new OrderedOptionOrdering[T] {}

  def comparableOptionOrderingOf[T <: Comparable[T]]: Ordering[Option[T]] =
    new ComparableOptionOrdering[T] {}

  object implicits {

    implicit final val localDateOptionOrdering: Ordering[Option[LocalDate]] = LocalDateOptionOrdering

    implicit final val localTimeOptionOrdering: Ordering[Option[LocalTime]] = LocalTimeOptionOrdering

    implicit final val localDateTimeOptionOrdering: Ordering[Option[LocalDateTime]] = LocalDateTimeOptionOrdering

    implicit final val instantOptionOrdering: Ordering[Option[Instant]] = InstantOptionOrdering

    implicit final val sqlDateOptionOrdering: Ordering[Option[js.Date]] = SqlDateOptionOrdering

    implicit final val sqlTimeOptionOrdering: Ordering[Option[js.Time]] = SqlTimeOptionOrdering

    implicit final val sqlTimestampOptionOrdering: Ordering[Option[js.Timestamp]] = SqlTimestampOptionOrdering

    implicit final val localDateOrdering: Ordering[LocalDate] = LocalDateOrdering

    implicit final val localTimeOrdering: Ordering[LocalTime] = LocalTimeOrdering

    implicit final val localDateTimeOrdering: Ordering[LocalDateTime] = LocalDateTimeOrdering

    implicit final val instantOrdering: Ordering[Instant] = InstantOrdering

    implicit final val sqlDateOrdering: Ordering[js.Date] = SqlDateOrdering

    implicit final val sqlTimeOrdering: Ordering[js.Time] = SqlTimeOrdering

    implicit final val sqlTimestampOrdering: Ordering[js.Timestamp] = SqlTimestampOrdering

    implicit final def newOptionOrdering[T](implicit ordering: Ordering[T]): Ordering[Option[T]] = optionOrderingOf[T]

    implicit final def newOrderedOptionOrdering[T <: Ordered[T]]: Ordering[Option[T]] = orderedOptionOrderingOf[T]

    implicit final def newComparableOptionOrdering[T <: Comparable[T]]: Ordering[Option[T]] =
      comparableOptionOrderingOf[T]
  }
}
