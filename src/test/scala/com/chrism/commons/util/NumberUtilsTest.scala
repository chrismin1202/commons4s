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

import java.{lang => jl, math => jm}
import com.chrism.commons.FunTestSuite

final class NumberUtilsTest extends FunTestSuite {

  import NumberUtils.implicits._

  test("instantiating java.math.BigInteger from Byte") {
    assert(NumberUtils.equals(NumberUtils.bigInteger(3.toByte), BigInt(3)))
  }

  test("instantiating java.math.BigInteger from Short") {
    assert(NumberUtils.equals(NumberUtils.bigInteger(255.toShort), BigInt(255)))
  }

  test("instantiating java.math.BigDecimal with precision and scale") {
    val bd = NumberUtils.jBigDecimal(1.2345, 4, 2)
    assert(bd.precision() <= 4)
    assert(bd.scale() === 2)
  }

  test("copy-constructing java.math.BigDecimal") {
    val bd = NumberUtils.jBigDecimal(3.140)
    val copied = NumberUtils.jBigDecimal(bd)
    assert(bd === copied)
  }

  test("reflectively instantiating min value for java.lang.Number values") {
    assertJByte(Byte.MinValue, NumberUtils.jMinValue[jl.Byte]())
    assertJShort(Short.MinValue, NumberUtils.jMinValue[jl.Short]())
    assertJInt(Int.MinValue, NumberUtils.jMinValue[jl.Integer]())
    assertJLong(Long.MinValue, NumberUtils.jMinValue[jl.Long]())
    assertJFloat(jl.Float.MIN_VALUE, NumberUtils.jMinValue[jl.Float]())
    assertJDouble(jl.Double.MIN_VALUE, NumberUtils.jMinValue[jl.Double]())
    intercept[UnsupportedOperationException] {
      NumberUtils.jMinValue[jm.BigInteger]()
    }
    assert(NumberUtils.jMinValueOrNone[jm.BigInteger].isEmpty)
    intercept[UnsupportedOperationException] {
      NumberUtils.jMinValue[jm.BigDecimal]()
    }
    assert(NumberUtils.jMinValueOrNone[jm.BigDecimal].isEmpty)
  }

  test("reflectively instantiating zero for java.lang.Number values") {
    assertJByte(0, NumberUtils.jZero[jl.Byte]())
    assertJShort(0, NumberUtils.jZero[jl.Short]())
    assertJInt(0, NumberUtils.jZero[jl.Integer]())
    assertJLong(0L, NumberUtils.jZero[jl.Long]())
    assertJFloat(0.0f, NumberUtils.jZero[jl.Float]())
    assertJDouble(0.0, NumberUtils.jZero[jl.Double]())
    assertJBigInteger(NumberUtils.JBigZeroInt, NumberUtils.jZero[jm.BigInteger]())
    assertJBigDecimal(NumberUtils.JBigZeroDecimal, NumberUtils.jZero[jm.BigDecimal]())
  }

  test("reflectively instantiating max value for java.lang.Number values") {
    assertJByte(Byte.MaxValue, NumberUtils.jMaxValue[jl.Byte]())
    assertJShort(Short.MaxValue, NumberUtils.jMaxValue[jl.Short]())
    assertJInt(Int.MaxValue, NumberUtils.jMaxValue[jl.Integer]())
    assertJLong(Long.MaxValue, NumberUtils.jMaxValue[jl.Long]())
    assertJFloat(jl.Float.MAX_VALUE, NumberUtils.jMaxValue[jl.Float]())
    assertJDouble(jl.Double.MAX_VALUE, NumberUtils.jMaxValue[jl.Double]())
    intercept[UnsupportedOperationException] {
      NumberUtils.jMaxValue[jm.BigInteger]()
    }
    assert(NumberUtils.jMaxValueOrNone[jm.BigInteger].isEmpty)
    intercept[UnsupportedOperationException] {
      NumberUtils.jMaxValue[jm.BigDecimal]()
    }
    assert(NumberUtils.jMaxValueOrNone[jm.BigDecimal].isEmpty)
  }

  test("reflectively converting integral java.lang.Number values to java.math.BigInteger") {
    assertJBigInteger(NumberUtils.JBigOneInt, NumberUtils.toBigInteger(1.toByte.asInstanceOf[jl.Byte]))
    assertJBigInteger(NumberUtils.JBigOneInt, NumberUtils.toBigInteger(1.toShort.asInstanceOf[jl.Short]))
    assertJBigInteger(NumberUtils.JBigOneInt, NumberUtils.toBigInteger(1.asInstanceOf[jl.Integer]))
    assertJBigInteger(NumberUtils.JBigOneInt, NumberUtils.toBigInteger(1.toLong.asInstanceOf[jl.Long]))
    assertJBigInteger(NumberUtils.JBigOneInt, NumberUtils.toBigInteger(NumberUtils.JBigOneInt))
    intercept[UnsupportedOperationException] {
      NumberUtils.toBigInteger(0.0f.asInstanceOf[jl.Float])
    }
    intercept[UnsupportedOperationException] {
      NumberUtils.toBigInteger(0.0.asInstanceOf[jl.Double])
    }
    intercept[UnsupportedOperationException] {
      NumberUtils.toBigInteger(NumberUtils.JBigOneDecimal)
    }
  }

  test("reflectively converting java.math.BigInteger to an integral java.lang.Number type") {
    assertJByte(Byte.MaxValue, NumberUtils.toJNum[jl.Byte](NumberUtils.bigInteger(Byte.MaxValue)))

    assertJShort(255.toShort, NumberUtils.toJNum[jl.Short](NumberUtils.bigInteger(255.toShort)))
    assertJInt(255, NumberUtils.toJNum[jl.Integer](NumberUtils.bigInteger(255)))

    assertJInt(65535, NumberUtils.toJNum[jl.Integer](NumberUtils.bigInteger(65535)))

    assertJLong(4294967295L, NumberUtils.toJNum[jl.Long](NumberUtils.bigInteger(4294967295L)))

    assertJBigInteger(
      NumberUtils.bigInteger("18446744073709551615"),
      NumberUtils.toJNum[jm.BigInteger](NumberUtils.bigInteger("18446744073709551615")))
  }

  test("checking equality between scala.math.BigDecimal and scala.math.BigDecimal") {
    assert(NumberUtils.equals(BigDecimal(10), BigDecimal(10)))
    assert(NumberUtils.equals(BigDecimal(9), BigDecimal(10)) === false)
    assert(NumberUtils.equals(BigDecimal(11), BigDecimal(10)) === false)
    assert(NumberUtils.equals(null.asInstanceOf[BigDecimal], BigDecimal(10)) === false)
    assert(NumberUtils.equals(BigDecimal(2), null.asInstanceOf[BigDecimal]) === false)
    assert(NumberUtils.equals(null.asInstanceOf[BigDecimal], null.asInstanceOf[BigDecimal]))
  }

  test("checking equality between scala.math.BigDecimal and java.math.BigDecimal") {
    assert(NumberUtils.equals(BigDecimal(10), jm.BigDecimal.TEN))
    assert(NumberUtils.equals(BigDecimal(9), jm.BigDecimal.TEN) === false)
    assert(NumberUtils.equals(BigDecimal(11), jm.BigDecimal.TEN) === false)
    assert(NumberUtils.equals(null.asInstanceOf[BigDecimal], jm.BigDecimal.TEN) === false)
    assert(NumberUtils.equals(BigDecimal(2), null.asInstanceOf[jm.BigDecimal]) === false)
    assert(NumberUtils.equals(null.asInstanceOf[BigDecimal], null.asInstanceOf[jm.BigDecimal]))
  }

  test("checking equality between java.math.BigDecimal and scala.math.BigDecimal") {
    assert(NumberUtils.equals(jm.BigDecimal.TEN, BigDecimal(10)))
    assert(NumberUtils.equals(jm.BigDecimal.TEN, BigDecimal(9)) === false)
    assert(NumberUtils.equals(jm.BigDecimal.TEN, BigDecimal(11)) === false)
    assert(NumberUtils.equals(null.asInstanceOf[jm.BigDecimal], BigDecimal(2)) === false)
    assert(NumberUtils.equals(jm.BigDecimal.TEN, null.asInstanceOf[BigDecimal]) === false)
    assert(NumberUtils.equals(null.asInstanceOf[jm.BigDecimal], null.asInstanceOf[BigDecimal]))
  }

  test("checking equality between java.math.BigDecimal and java.math.BigDecimal") {
    assert(NumberUtils.equals(jm.BigDecimal.ONE, jm.BigDecimal.ONE))
    assert(NumberUtils.equals(jm.BigDecimal.ONE, jm.BigDecimal.ZERO) === false)
    assert(NumberUtils.equals(jm.BigDecimal.ONE, jm.BigDecimal.TEN) === false)
    assert(NumberUtils.equals(null.asInstanceOf[jm.BigDecimal], jm.BigDecimal.ONE) === false)
    assert(NumberUtils.equals(jm.BigDecimal.ONE, null.asInstanceOf[jm.BigDecimal]) === false)
    assert(NumberUtils.equals(null.asInstanceOf[jm.BigDecimal], null.asInstanceOf[jm.BigDecimal]))
  }

  test("comparing scala.math.BigDecimal with scala.math.BigDecimal") {
    assert(NumberUtils.compare(BigDecimal(10), BigDecimal(10)) === 0)
    assert(NumberUtils.compare(BigDecimal(9), BigDecimal(10)) < 0)
    assert(NumberUtils.compare(BigDecimal(11), BigDecimal(10)) > 0)
    assert(NumberUtils.compare(null.asInstanceOf[BigDecimal], BigDecimal(10)) > 0)
    assert(NumberUtils.compare(BigDecimal(2), null.asInstanceOf[BigDecimal]) < 0)
    assert(NumberUtils.compare(null.asInstanceOf[BigDecimal], null.asInstanceOf[BigDecimal]) == 0)
  }

  test("comparing scala.math.BigDecimal with java.math.BigDecimal") {
    assert(NumberUtils.compare(BigDecimal(10), jm.BigDecimal.TEN) === 0)
    assert(NumberUtils.compare(BigDecimal(9), jm.BigDecimal.TEN) < 0)
    assert(NumberUtils.compare(BigDecimal(11), jm.BigDecimal.TEN) > 0)
    assert(NumberUtils.compare(null.asInstanceOf[BigDecimal], jm.BigDecimal.TEN) > 0)
    assert(NumberUtils.compare(BigDecimal(2), null.asInstanceOf[jm.BigDecimal]) < 0)
    assert(NumberUtils.compare(null.asInstanceOf[BigDecimal], null.asInstanceOf[jm.BigDecimal]) == 0)
  }

  test("comparing java.math.BigDecimal with scala.math.BigDecimal") {
    assert(NumberUtils.compare(jm.BigDecimal.TEN, BigDecimal(10)) === 0)
    assert(NumberUtils.compare(jm.BigDecimal.TEN, BigDecimal(9)) > 0)
    assert(NumberUtils.compare(jm.BigDecimal.TEN, BigDecimal(11)) < 0)
    assert(NumberUtils.compare(null.asInstanceOf[jm.BigDecimal], BigDecimal(2)) > 0)
    assert(NumberUtils.compare(jm.BigDecimal.TEN, null.asInstanceOf[BigDecimal]) < 0)
    assert(NumberUtils.compare(null.asInstanceOf[jm.BigDecimal], null.asInstanceOf[BigDecimal]) == 0)
  }

  test("comparing java.math.BigDecimal with java.math.BigDecimal") {
    assert(NumberUtils.compare(jm.BigDecimal.ONE, jm.BigDecimal.ONE) === 0)
    assert(NumberUtils.compare(jm.BigDecimal.ONE, jm.BigDecimal.ZERO) > 0)
    assert(NumberUtils.compare(jm.BigDecimal.ONE, jm.BigDecimal.TEN) < 0)
    assert(NumberUtils.compare(null.asInstanceOf[jm.BigDecimal], jm.BigDecimal.ONE) > 0)
    assert(NumberUtils.compare(jm.BigDecimal.ONE, null.asInstanceOf[jm.BigDecimal]) < 0)
    assert(NumberUtils.compare(null.asInstanceOf[jm.BigDecimal], null.asInstanceOf[jm.BigDecimal]) == 0)
  }

  test("checking equality between scala.math.BigInt and scala.math.BigInt") {
    assert(NumberUtils.equals(BigInt(10), BigInt(10)))
    assert(NumberUtils.equals(BigInt(9), BigInt(10)) === false)
    assert(NumberUtils.equals(BigInt(11), BigInt(10)) === false)
    assert(NumberUtils.equals(null.asInstanceOf[BigInt], BigInt(10)) === false)
    assert(NumberUtils.equals(BigInt(2), null.asInstanceOf[BigInt]) === false)
    assert(NumberUtils.equals(null.asInstanceOf[BigInt], null.asInstanceOf[BigInt]))
  }

  test("checking equality between scala.math.BigInt and java.math.BigInteger") {
    assert(NumberUtils.equals(BigInt(10), jm.BigInteger.TEN))
    assert(NumberUtils.equals(BigInt(9), jm.BigInteger.TEN) === false)
    assert(NumberUtils.equals(BigInt(11), jm.BigInteger.TEN) === false)
    assert(NumberUtils.equals(null.asInstanceOf[BigInt], jm.BigInteger.TEN) === false)
    assert(NumberUtils.equals(BigInt(2), null.asInstanceOf[jm.BigInteger]) === false)
    assert(NumberUtils.equals(null.asInstanceOf[BigInt], null.asInstanceOf[jm.BigInteger]))
  }

  test("checking equality between java.math.BigInteger and scala.math.BigInt") {
    assert(NumberUtils.equals(jm.BigInteger.TEN, BigInt(10)))
    assert(NumberUtils.equals(jm.BigInteger.TEN, BigInt(9)) === false)
    assert(NumberUtils.equals(jm.BigInteger.TEN, BigInt(11)) === false)
    assert(NumberUtils.equals(null.asInstanceOf[jm.BigInteger], BigInt(2)) === false)
    assert(NumberUtils.equals(jm.BigInteger.TEN, null.asInstanceOf[BigInt]) === false)
    assert(NumberUtils.equals(null.asInstanceOf[jm.BigInteger], null.asInstanceOf[BigInt]))
  }

  test("checking equality between java.math.BigInteger and java.math.BigInteger") {
    assert(NumberUtils.equals(jm.BigInteger.ONE, jm.BigInteger.ONE))
    assert(NumberUtils.equals(jm.BigInteger.ONE, jm.BigInteger.ZERO) === false)
    assert(NumberUtils.equals(jm.BigInteger.ONE, jm.BigInteger.TEN) === false)
    assert(NumberUtils.equals(null.asInstanceOf[jm.BigInteger], jm.BigInteger.ONE) === false)
    assert(NumberUtils.equals(jm.BigInteger.ONE, null.asInstanceOf[jm.BigInteger]) === false)
    assert(NumberUtils.equals(null.asInstanceOf[jm.BigInteger], null.asInstanceOf[jm.BigInteger]))
  }

  test("comparing scala.math.BigInt with scala.math.BigInt") {
    assert(NumberUtils.compare(BigInt(10), BigInt(10)) === 0)
    assert(NumberUtils.compare(BigInt(9), BigInt(10)) < 0)
    assert(NumberUtils.compare(BigInt(11), BigInt(10)) > 0)
    assert(NumberUtils.compare(null.asInstanceOf[BigInt], BigInt(10)) > 0)
    assert(NumberUtils.compare(BigInt(2), null.asInstanceOf[BigInt]) < 0)
    assert(NumberUtils.compare(null.asInstanceOf[BigInt], null.asInstanceOf[BigInt]) == 0)
  }

  test("comparing scala.math.BigInt with java.math.BigInteger") {
    assert(NumberUtils.compare(BigInt(10), jm.BigInteger.TEN) === 0)
    assert(NumberUtils.compare(BigInt(9), jm.BigInteger.TEN) < 0)
    assert(NumberUtils.compare(BigInt(11), jm.BigInteger.TEN) > 0)
    assert(NumberUtils.compare(null.asInstanceOf[BigInt], jm.BigInteger.TEN) > 0)
    assert(NumberUtils.compare(BigInt(2), null.asInstanceOf[jm.BigInteger]) < 0)
    assert(NumberUtils.compare(null.asInstanceOf[BigInt], null.asInstanceOf[jm.BigInteger]) == 0)
  }

  test("comparing java.math.BigInteger with scala.math.BigInt") {
    assert(NumberUtils.compare(jm.BigInteger.TEN, BigInt(10)) === 0)
    assert(NumberUtils.compare(jm.BigInteger.TEN, BigInt(9)) > 0)
    assert(NumberUtils.compare(jm.BigInteger.TEN, BigInt(11)) < 0)
    assert(NumberUtils.compare(null.asInstanceOf[jm.BigInteger], BigInt(2)) > 0)
    assert(NumberUtils.compare(jm.BigInteger.TEN, null.asInstanceOf[BigInt]) < 0)
    assert(NumberUtils.compare(null.asInstanceOf[jm.BigInteger], null.asInstanceOf[BigInt]) == 0)
  }

  test("comparing java.math.BigInteger with java.math.BigInteger") {
    assert(NumberUtils.compare(jm.BigInteger.ONE, jm.BigInteger.ONE) === 0)
    assert(NumberUtils.compare(jm.BigInteger.ONE, jm.BigInteger.ZERO) > 0)
    assert(NumberUtils.compare(jm.BigInteger.ONE, jm.BigInteger.TEN) < 0)
    assert(NumberUtils.compare(null.asInstanceOf[jm.BigInteger], jm.BigInteger.ONE) > 0)
    assert(NumberUtils.compare(jm.BigInteger.ONE, null.asInstanceOf[jm.BigInteger]) < 0)
    assert(NumberUtils.compare(null.asInstanceOf[jm.BigInteger], null.asInstanceOf[jm.BigInteger]) == 0)
  }

  test("pimping java.math.BigDecimal: adding java.math.BigDecimal") {
    val bd1 = NumberUtils.jBigDecimal(1.0)
    val bd2 = NumberUtils.jBigDecimal(2.0)
    assert(NumberUtils.equals(bd1 + bd2, NumberUtils.jBigDecimal(3.0)))
  }

  test("pimping java.math.BigDecimal: adding scala.math.BigDecimal") {
    val bd1 = NumberUtils.jBigDecimal(1.0)
    val bd2 = BigDecimal(2.0)
    assert(NumberUtils.equals(bd1 + bd2, NumberUtils.jBigDecimal(3.0)))
  }

  test("pimping java.math.BigDecimal: subtracting java.math.BigDecimal") {
    val bd1 = NumberUtils.jBigDecimal(1.0)
    val bd2 = NumberUtils.jBigDecimal(2.0)
    assert(NumberUtils.equals(bd1 - bd2, NumberUtils.jBigDecimal(-1.0)))
  }

  test("pimping java.math.BigDecimal: subtracting scala.math.BigDecimal") {
    val bd1 = NumberUtils.jBigDecimal(1.0)
    val bd2 = BigDecimal(2.0)
    assert(NumberUtils.equals(bd1 - bd2, NumberUtils.jBigDecimal(-1.0)))
  }

  test("pimping java.math.BigDecimal: rational operators with java.math.BigDecimal") {
    val bd1 = NumberUtils.jBigDecimal(1.0)
    val bd2 = NumberUtils.jBigDecimal(2.0)
    assert(bd1 < bd2)
    assert(bd1 <= bd2)
    assert((bd1 > bd2) === false)
    assert((bd1 >= bd2) === false)
  }

  test("pimping java.math.BigDecimal: rational operators with scala.math.BigDecimal") {
    val bd1 = NumberUtils.jBigDecimal(1.0)
    val bd2 = BigDecimal(2.0)
    assert(bd1 < bd2)
    assert(bd1 <= bd2)
    assert((bd1 > bd2) === false)
    assert((bd1 >= bd2) === false)
  }

  test("pimping java.math.BigInteger: adding java.math.BigInteger") {
    val bd1 = NumberUtils.bigInteger(1)
    val bd2 = NumberUtils.bigInteger(2)
    assert(NumberUtils.equals(bd1 + bd2, NumberUtils.bigInteger(3)))
  }

  test("pimping java.math.BigInteger: adding scala.math.BigInt") {
    val bd1 = NumberUtils.bigInteger(1)
    val bd2 = BigInt(2)
    assert(NumberUtils.equals(bd1 + bd2, NumberUtils.bigInteger(3)))
  }

  test("pimping java.math.BigInteger: subtracting java.math.BigInt") {
    val bd1 = NumberUtils.bigInteger(1)
    val bd2 = NumberUtils.bigInteger(2)
    assert(NumberUtils.equals(bd1 - bd2, NumberUtils.bigInteger(-1)))
  }

  test("pimping java.math.BigInteger: subtracting scala.math.BigInt") {
    val bd1 = NumberUtils.bigInteger(1)
    val bd2 = BigInt(2)
    assert(NumberUtils.equals(bd1 - bd2, NumberUtils.bigInteger(-1)))
  }

  test("pimping java.math.BigInteger: rational operators with java.math.BigInteger") {
    val bd1 = NumberUtils.bigInteger(1)
    val bd2 = NumberUtils.bigInteger(2)
    assert(bd1 < bd2)
    assert(bd1 <= bd2)
    assert((bd1 > bd2) === false)
    assert((bd1 >= bd2) === false)
  }

  test("pimping java.math.BigInteger: rational operators with scala.math.BigInt") {
    val bd1 = NumberUtils.bigInteger(1)
    val bd2 = BigInt(2)
    assert(bd1 < bd2)
    assert(bd1 <= bd2)
    assert((bd1 > bd2) === false)
    assert((bd1 >= bd2) === false)
  }

  test("pimping java.lang.Byte: adding integer") {
    assertJByte(Byte.MaxValue, NumberUtils.jByte(Byte.MaxValue - 1).add(1))
  }

  test("pimping java.lang.Short: adding integer") {
    assertJShort(Short.MaxValue, NumberUtils.jShort(Short.MaxValue - 1).add(1))
  }
}
