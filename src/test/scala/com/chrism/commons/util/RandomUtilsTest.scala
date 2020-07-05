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
import java.time.{Instant, LocalDate, LocalDateTime}
import java.{lang => jl}

import com.chrism.commons.FunTestSuite

final class RandomUtilsTest extends FunTestSuite {

  test("generating random positive Byte with upper bound") {
    val randomInt = RandomUtils.randomPositiveByte(upperBound = 10)
    assert(randomInt > 0)
    assert(randomInt <= 10)
  }

  test("generating random positive Short with upper bound") {
    val randomInt = RandomUtils.randomPositiveShort(upperBound = 10)
    assert(randomInt > 0)
    assert(randomInt <= 10)
  }

  test("generating random positive Int with upper bound") {
    val randomInt = RandomUtils.randomPositiveInt(upperBound = 10)
    assert(randomInt > 0)
    assert(randomInt <= 10)
  }

  test("generating random positive Long with upper bound") {
    val randomLong = RandomUtils.randomPositiveLong(upperBound = 666L)
    assert(randomLong > 0L)
    assert(randomLong <= 666L)
  }

  test("generating random positive BigInt") {
    val randomBigInt = RandomUtils.randomPositiveBigInt(5)
    assert(randomBigInt > NumberUtils.BigZeroInt)
    assert(randomBigInt < BigInt(100000))
  }

  test("generating random negative BigInt") {
    val randomBigInt = RandomUtils.randomNegativeBigInt(5)
    assert(randomBigInt < NumberUtils.BigZeroInt)
    assert(randomBigInt > BigInt(-100000))
  }

  test("generating random unsigned BigInt when lowerBound == upperBound - 1") {
    val maxUnsignedBigInt = RandomUtils.randomUnsignedBigInt(lowerBound = RandomUtils.MaxUnsignedBigInt)
    assert(maxUnsignedBigInt === RandomUtils.MaxUnsignedBigInt)

    val minUnsignedBigInt =
      RandomUtils.randomUnsignedBigInt(lowerBound = NumberUtils.BigZeroInt, upperBound = NumberUtils.BigOneInt)
    assert(minUnsignedBigInt === NumberUtils.BigZeroInt)
  }

  test("generating random unsigned BigInt when lowerBound < upperBound") {
    val unsignedBigInt = RandomUtils.randomUnsignedBigInt(lowerBound = 2, upperBound = 5)
    assert(unsignedBigInt >= BigInt(2))
    assert(unsignedBigInt < BigInt(5))
  }

  test("generating random positive BigDecimal with scale") {
    val randomDecimal = RandomUtils.randomPositiveBigDecimal(precision = 666, scale = 2)
    assert(randomDecimal > NumberUtils.BigZeroDecimal)
    assert(randomDecimal.precision === 666)
    assert(randomDecimal.scale === 2)
  }

  test("generating random negative BigDecimal with scale") {
    val randomDecimal = RandomUtils.randomNegativeBigDecimal(precision = 666, scale = 4)
    assert(randomDecimal < NumberUtils.BigZeroDecimal)
    assert(randomDecimal.precision === 666)
    assert(randomDecimal.scale === 4)
  }

  test("generating random BigDecimal with precision == scale") {
    val randomDecimal = RandomUtils.randomBigDecimal(precision = 4, scale = 4)
    assert(randomDecimal.precision === randomDecimal.scale)
  }

  test("generating random BigDecimal with precision > scale") {
    val randomDecimal = RandomUtils.randomBigDecimal(precision = 4, scale = 3)
    assert(randomDecimal.precision === 4)
    assert(randomDecimal.scale === 3)
  }

  test("generating random BigDecimal with precision < scale") {
    val randomDecimal = RandomUtils.randomBigDecimal(precision = 2, scale = 3)
    assert(randomDecimal.precision === 2)
    assert(randomDecimal.scale === 3)
  }

  test("generating random LocalDate within range") {
    import OrderingUtils.implicits._

    val upperBound = LocalDate.now()
    val lowerBound = upperBound.minusYears(1L)

    val randomDate = RandomUtils.randomLocalDate(lowerBound, upperBound)
    randomDate should be >= lowerBound
    randomDate should be < upperBound
  }

  test("generating random LocalDate: lowerBound == upperBound") {
    val lowerBound = LocalDate.now()
    val upperBound = lowerBound

    val randomDate = RandomUtils.randomLocalDate(lowerBound, upperBound)
    assert(randomDate === lowerBound)
    assert(randomDate === upperBound)
  }

  test("generating random LocalDate: IllegalArgumentException is thrown when lower bound is greater") {
    val lowerBound = LocalDate.now()
    val upperBound = lowerBound.minusYears(1L)

    intercept[IllegalArgumentException] {
      RandomUtils.randomLocalDate(lowerBound, upperBound)
    }
  }

  test("generating random LocalDateTime within range") {
    import OrderingUtils.implicits._

    val upperBound = LocalDateTime.now()
    val lowerBound = upperBound.minusYears(1L)

    val randomDateTime = RandomUtils.randomLocalDateTime(lowerBound, upperBound)
    randomDateTime should be >= lowerBound
    randomDateTime should be < upperBound
  }

  test("generating random LocalDateTime: lowerBound == upperBound") {
    val lowerBound = LocalDateTime.now()
    val upperBound = lowerBound

    val randomDateTime = RandomUtils.randomLocalDateTime(lowerBound, upperBound)
    assert(randomDateTime === lowerBound)
    assert(randomDateTime === upperBound)
  }

  test("generating random LocalDateTime: IllegalArgumentException is thrown when lower bound is greater") {
    val lowerBound = LocalDateTime.now()
    val upperBound = lowerBound.minusYears(1L)

    intercept[IllegalArgumentException] {
      RandomUtils.randomLocalDateTime(lowerBound, upperBound)
    }
  }

  test("generating random Instant within range") {
    import OrderingUtils.implicits._

    val upperBound = Instant.now()
    val lowerBound = upperBound.minus(1L, ChronoUnit.DAYS)

    val instant = RandomUtils.randomInstant(lowerBound, upperBound)
    instant should be >= lowerBound
    instant should be < upperBound
  }

  test("generating random Instant: lowerBound == upperBound") {
    val lowerBound = Instant.now()
    val upperBound = lowerBound

    val instant = RandomUtils.randomInstant(lowerBound, upperBound)
    assert(instant === lowerBound)
    assert(instant === upperBound)
  }

  test("generating random Instant: IllegalArgumentException is thrown when lower bound is greater") {
    val lowerBound = Instant.now()
    val upperBound = lowerBound.minus(1L, ChronoUnit.DAYS)

    intercept[IllegalArgumentException] {
      RandomUtils.randomInstant(lowerBound, upperBound)
    }
  }

  test("generating random string with printable characters") {
    val randomString = RandomUtils.randomPrintableString(7)
    randomString should have length 7
  }

  test("generating random alphabetic string") {
    val randomString = RandomUtils.randomAlphabetic(10)
    randomString should have length 10
    assert(randomString.forall(jl.Character.isAlphabetic(_)))
  }

  test("generating random alphanumeric string") {
    val randomString = RandomUtils.randomAlphanumeric(8)
    randomString should have length 8
    assert(randomString.forall(c => jl.Character.isAlphabetic(c) || jl.Character.isDigit(c)))
  }

  test("generating random integers: IllegalArgumentException is thrown when number of values is negative") {
    intercept[IllegalArgumentException] {
      RandomUtils.randomInts(-1, 1, 2)
    }
  }

  test(
    "generating random integers: IllegalArgumentException is thrown when lower bound is equal to upper bound (exclusive)") {
    intercept[IllegalArgumentException] {
      RandomUtils.randomInts(1, 3, 3)
    }
  }

  test("generating random integers: IllegalArgumentException is thrown when lower bound is greater than upper bound") {
    intercept[IllegalArgumentException] {
      RandomUtils.randomInts(1, 4, 3)
    }
  }

  test(
    "generating random integers: IllegalArgumentException is thrown when number of values exceeds the maximum number of values") {
    intercept[IllegalArgumentException] {
      RandomUtils.randomInts(3, 3, 4, upperBoundInclusive = true)
    }
  }

  test("generating multiple random integers in a given range") {
    val ints = RandomUtils.randomInts(10, -10, -1, upperBoundInclusive = true)
    ints should contain theSameElementsInOrderAs (-10 to -1)
  }

  test("selecting a random element from an iterable") {
    val nums = Seq(1, 2, 3, 4)
    val randomInt = RandomUtils.randomElementFrom(nums)
    assert(nums.contains(randomInt))
  }

  test("randomElementFrom: IllegalArgumentException is thrown when the iterable is empty") {
    intercept[IllegalArgumentException] {
      RandomUtils.randomElementFrom(Seq.empty[String])
    }
  }

  test("randomElementsFrom: selecting all elements") {
    val elements = Set(1, 2, 3, 4)
    RandomUtils.randomElementsFrom(elements, elements.size) should contain theSameElementsAs elements
  }

  test("randomElementsFrom: selecting one element") {
    val elements = Set(1, 2, 3, 4)
    val selectedElements = RandomUtils.randomElementsFrom(elements, 1)
    selectedElements should have size 1
    elements should contain allElementsOf selectedElements
  }

  test("randomElementsFrom: selecting more than one element") {
    val elements = Set(1, 2, 3, 4)
    val selectedElements = RandomUtils.randomElementsFrom(elements, 2)
    selectedElements should have size 2
    elements should contain allElementsOf selectedElements
  }

  test("randomElementsFrom: IllegalArgumentException is thrown when the number of elements to select is zero") {
    intercept[IllegalArgumentException] {
      RandomUtils.randomElementsFrom(Seq(1), 0)
    }
  }

  test("randomElementsFrom: IllegalArgumentException is thrown when the number of elements to select is negative") {
    intercept[IllegalArgumentException] {
      RandomUtils.randomElementsFrom(Seq(1), -1)
    }
  }

  test("randomElementsFrom: IllegalArgumentException is thrown when the iterable is empty") {
    intercept[IllegalArgumentException] {
      RandomUtils.randomElementsFrom(Seq.empty[String], 1)
    }
  }

  test("randomElementsFrom: IllegalArgumentException is thrown when the size of the iterable is smaller") {
    intercept[IllegalArgumentException] {
      RandomUtils.randomElementsFrom(Seq(1), 2)
    }
  }

  test("shuffling a sequence") {
    val nums = Seq(1, 2, 3, 4)
    val shuffled = RandomUtils.shuffle(nums)

    // the shuffled sequence should contain the same elements
    shuffled should contain theSameElementsAs nums
    // but they should be in different order
    assert(shuffled.zip(nums).exists(t => t._1 != t._2))
  }
}
