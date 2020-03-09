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
package com.chrism.commons

import java.{lang => jl, math => jm}

import org.scalactic.TypeCheckedTripleEquals
import org.scalatest
import org.scalatest.{BeforeAndAfter, BeforeAndAfterAll, FunSuite, Matchers}

import scala.reflect.ClassTag

/** The base class for writing a suite of tests using {{{ FunSuite }}} in which
  * each test is represented as a function value.
  */
abstract class FunTestSuite
    extends FunSuite
    with TypeCheckedTripleEquals
    with Matchers
    with BeforeAndAfter
    with BeforeAndAfterAll {

  import FunTestSuite.{DefaultBigDecimalTolerance, DefaultDoubleTolerance, DefaultFloatTolerance}

  protected final def assertNull(actual: AnyRef): scalatest.Assertion = assert(actual === null)

  protected final def assertNotNull(actual: AnyRef): scalatest.Assertion = assert(actual !== null)

  protected final def assertOption[A](expected: A, actualOpt: Option[A]): scalatest.Assertion = {
    assertDefined(actualOpt)
    assert(actualOpt.get === expected)
  }

  protected final def assertFloat(
    expected: Float,
    actual: Float,
    tolerance: Float = DefaultFloatTolerance
  ): scalatest.Assertion =
    assert(floatApproximatelyEquals(expected, actual, tolerance = tolerance))

  protected final def assertFloatOption(
    expected: Float,
    actualOpt: Option[Float],
    tolerance: Float = DefaultFloatTolerance
  ): scalatest.Assertion = {
    assertDefined(actualOpt)
    assertFloat(expected, actualOpt.get, tolerance)
  }

  protected final def assertDouble(
    expected: Double,
    actual: Double,
    tolerance: Double = DefaultDoubleTolerance
  ): scalatest.Assertion =
    assert(doubleApproximatelyEquals(expected, actual, tolerance = tolerance))

  protected final def assertDoubleOption(
    expected: Double,
    actualOpt: Option[Double],
    tolerance: Double = DefaultDoubleTolerance
  ): scalatest.Assertion = {
    assertDefined(actualOpt)
    assertDouble(expected, actualOpt.get, tolerance)
  }

  protected final def assertBigDecimal(
    expected: BigDecimal,
    actual: BigDecimal,
    tolerance: BigDecimal = DefaultBigDecimalTolerance
  ): scalatest.Assertion = {
    val lowerBound = expected - tolerance
    val upperBound = expected + tolerance
    assert(
      (actual >= lowerBound) && (actual <= upperBound),
      s"The amount $actual is not within tolerable range [$lowerBound, $upperBound]"
    )
  }

  protected final def assertBigDecimalOption(
    expected: BigDecimal,
    actual: Option[BigDecimal],
    tolerance: BigDecimal = DefaultBigDecimalTolerance
  ): scalatest.Assertion = {
    assertDefined(actual)
    assertBigDecimal(expected, actual.get, tolerance)
  }

  protected final def assertJFloat(
    expected: Float,
    actual: jl.Float,
    tolerance: Float = DefaultFloatTolerance
  ): scalatest.Assertion =
    assertFloat(expected, actual.floatValue(), tolerance = tolerance)

  protected final def assertJDouble(
    expected: Double,
    actual: jl.Double,
    tolerance: Double = DefaultDoubleTolerance
  ): scalatest.Assertion =
    assertDouble(expected, actual.doubleValue(), tolerance = tolerance)

  protected final def assertJByte(expected: Byte, actual: jl.Byte): scalatest.Assertion =
    assertJInteger(expected, actual)(i => i.byteValue())

  protected final def assertJShort(expected: Short, actual: jl.Short): scalatest.Assertion =
    assertJInteger(expected, actual)(i => i.shortValue())

  protected final def assertJInt(expected: Int, actual: jl.Integer): scalatest.Assertion =
    assertJInteger(expected, actual)(i => i.intValue())

  protected final def assertJLong(expected: Long, actual: jl.Long): scalatest.Assertion =
    assertJInteger(expected, actual)(l => l.longValue())

  protected final def assertJByteBetween(lowerBound: Byte, upperBound: Byte, actual: jl.Byte): scalatest.Assertion =
    assert(
      actual.byteValue() >= lowerBound && actual.byteValue() <= upperBound,
      s"The value ($actual) is not within range [$lowerBound, $upperBound]!"
    )

  protected final def assertJShortBetween(lowerBound: Short, upperBound: Short, actual: jl.Short): scalatest.Assertion =
    assert(
      actual.shortValue() >= lowerBound && actual.shortValue() <= upperBound,
      s"The value ($actual) is not within range [$lowerBound, $upperBound]!"
    )

  protected final def assertJIntBetween(lowerBound: Int, upperBound: Int, actual: jl.Integer): scalatest.Assertion =
    assert(
      actual.intValue() >= lowerBound && actual.intValue() <= upperBound,
      s"The value ($actual) is not within range [$lowerBound, $upperBound]!"
    )

  protected final def assertJLongBetween(lowerBound: Long, upperBound: Long, actual: jl.Long): scalatest.Assertion =
    assert(
      actual.longValue() >= lowerBound && actual.longValue() <= upperBound,
      s"The value ($actual) is not within range [$lowerBound, $upperBound]!"
    )

  protected final def assertDefined[A](opt: Option[A]): scalatest.Assertion =
    assert(opt.isDefined, "The value is not defined!")

  protected final def assertProductWithFloatingPoints[P <: Product: ClassTag](
    expected: P,
    actual: P
  ): scalatest.Assertion =
    // TODO: support BigDecimal and jm.BigDecimal
    assert(
      actual.productIterator
        .zip(expected.productIterator)
        .map(t =>
          matchFloatingPoint(t._1, t._2) match {
            case Some(result) => result
            case _ =>
              t match {
                case (eo: Option[_], ao: Option[_]) =>
                  if (eo == ao) true
                  else {
                    // check
                    if (eo.isDefined && ao.isDefined) {
                      matchFloatingPoint(eo.getOrFail(), ao.getOrFail()) match {
                        case Some(result) => result
                        case _            => t._1 == t._2
                      }
                    } else false
                  }
                case (em, am) => em == am
              }
          })
        .forall(_ == true)
    )

  protected def floatApproximatelyEquals(
    expected: Float,
    actual: Float,
    tolerance: Float = DefaultFloatTolerance
  ): Boolean = actual === expected +- tolerance

  protected def jFloatApproximatelyEquals(
    expected: Float,
    actual: jl.Float,
    tolerance: Float = DefaultFloatTolerance
  ): Boolean =
    actual != null && floatApproximatelyEquals(expected, actual.floatValue(), tolerance = tolerance)

  protected def doubleApproximatelyEquals(
    expected: Double,
    actual: Double,
    tolerance: Double = DefaultDoubleTolerance
  ): Boolean = actual === expected +- tolerance

  protected def jDoubleApproximatelyEquals(
    expected: Double,
    actual: jl.Double,
    tolerance: Double = DefaultDoubleTolerance
  ): Boolean =
    actual != null & doubleApproximatelyEquals(expected, actual, tolerance = tolerance)

  private[this] def assertJInteger[SI <: AnyVal, JI <: jl.Number](
    expected: SI,
    actual: JI
  )(
    valueFunc: JI => SI
  ): scalatest.Assertion = {
    assertNotNull(actual)
    assert(valueFunc(actual) === expected)
  }

  private[this] def matchFloatingPoint(a: Any, b: Any): Option[Boolean] =
    (a, b) match {
      case (ef: Float, af: Float)   => Some(floatApproximatelyEquals(ef, af))
      case (ed: Double, ad: Double) => Some(doubleApproximatelyEquals(ed, ad))
      case (ejf: jl.Float, ajf: jl.Float) =>
        if (ejf == null) Some(ajf == null)
        else Some(jFloatApproximatelyEquals(ejf.floatValue(), ajf))
      case (ejd: jl.Double, ajd: jl.Double) =>
        if (ejd == null) Some(ajd == null)
        else Some(jDoubleApproximatelyEquals(ejd.doubleValue(), ajd))
      case _ => None
    }

  protected implicit final class OptionOps[A](opt: Option[A]) {

    def getOrFail(): A = opt.getOrElse(fail("expected Some!"))
  }

}

object FunTestSuite {

  val DefaultFloatTolerance: Float = jl.Float.intBitsToFloat((127 - 24) << 23)
  val DefaultDoubleTolerance: Double = jl.Double.longBitsToDouble((1023L - 53L) << 52)
  val DefaultBigDecimalTolerance: BigDecimal = BigDecimal(DefaultDoubleTolerance)
  val DefaultJBigDecimalTolerance: jm.BigDecimal = DefaultBigDecimalTolerance.bigDecimal
}
