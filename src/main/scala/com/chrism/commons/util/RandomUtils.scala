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
import java.time.{Instant, LocalDate, LocalDateTime, ZoneOffset}
import java.util.concurrent.ThreadLocalRandom

import org.apache.commons.lang3.RandomStringUtils

import scala.collection.generic.CanBuildFrom

object RandomUtils {

  import NumberUtils.{BigOneInt, BigZeroDecimal, BigZeroInt}

  private val MaxSignedSqlBigInt: BigInt = BigInt(Long.MaxValue)
  private[util] val MaxUnsignedBigInt: BigInt = MaxSignedSqlBigInt * 2 + BigOneInt

  private val DefaultBigDecimalPrecision: Int = 38
  private val DefaultBigDecimalScale: Int = 10

  private val ByteRange: AllRange = AllRange(Byte.MinValue, Byte.MaxValue)
  private val PositiveByteRange: PositiveRange = PositiveRange(ByteRange.maxValue)
  private val NegativeByteRange: NegativeRange = NegativeRange(ByteRange.minValue)

  private val ShortRange: AllRange = AllRange(Short.MinValue, Short.MaxValue)
  private val PositiveShortRange: PositiveRange = PositiveRange(ShortRange.maxValue)
  private val NegativeShortRange: NegativeRange = NegativeRange(ShortRange.minValue)

  private val IntRange: AllRange = AllRange(Int.MinValue, Int.MaxValue)
  private val PositiveIntRange: PositiveRange = PositiveRange(IntRange.maxValue)
  private val NegativeIntRange: NegativeRange = NegativeRange(IntRange.minValue)

  private val LongRange: AllRange = AllRange(Long.MinValue, Long.MaxValue)
  private val PositiveLongRange: PositiveRange = PositiveRange(LongRange.maxValue)
  private val NegativeLongRange: NegativeRange = NegativeRange(LongRange.minValue)

  private lazy val currentDate: LocalDate = LocalDate.now()
  private lazy val defaultDateLowerBound: LocalDate = currentDate.minusYears(20L)

  private lazy val currentDateTime: LocalDateTime = LocalDateTime.now()
  private lazy val defaultDateTimeLowerBound: LocalDateTime = currentDateTime.minusYears(20L)

  private lazy val currentInstant: Instant = Instant.now()
  private lazy val defaultInstantLowerBound: Instant = currentInstant.minus(365L * 20L, ChronoUnit.DAYS)

  def randomBoolean(/* IO */ ): Boolean = localRandom().nextBoolean()

  def randomByte(
    lowerBound: Byte = Byte.MinValue,
    upperBound: Byte = Byte.MaxValue,
    upperBoundInclusive: Boolean = true
  ): Byte = {
    ByteRange.validate(lowerBound, upperBound, upperBoundInclusive)
    localRandom().nextInt(lowerBound, if (upperBoundInclusive) upperBound + 1 else upperBound).toByte
  }

  def randomPositiveByte(
    lowerBound: Byte = 1,
    upperBound: Byte = Byte.MaxValue,
    upperBoundInclusive: Boolean = true
  ): Byte = {
    PositiveByteRange.validate(lowerBound, upperBound, upperBoundInclusive)
    localRandom().nextInt(lowerBound, if (upperBoundInclusive) upperBound + 1 else upperBound).toByte
  }

  def randomNegativeByte(lowerBound: Byte = Byte.MinValue, upperBound: Byte = 0): Byte = {
    NegativeByteRange.validate(lowerBound, upperBound, upperBoundInclusive = false)
    localRandom().nextInt(lowerBound, upperBound).toByte
  }

  def randomShort(
    lowerBound: Short = Short.MinValue,
    upperBound: Short = Short.MaxValue,
    upperBoundInclusive: Boolean = true
  ): Short = {
    ShortRange.validate(lowerBound, upperBound, upperBoundInclusive)
    localRandom().nextInt(lowerBound, if (upperBoundInclusive) upperBound + 1 else upperBound).toShort
  }

  def randomPositiveShort(
    lowerBound: Short = 1,
    upperBound: Short = Short.MaxValue,
    upperBoundInclusive: Boolean = true
  ): Short = {
    PositiveShortRange.validate(lowerBound, upperBound, upperBoundInclusive)
    localRandom().nextInt(lowerBound, if (upperBoundInclusive) upperBound + 1 else upperBound).toShort
  }

  def randomNegativeShort(lowerBound: Short = Short.MinValue, upperBound: Short = 0): Short = {
    NegativeShortRange.validate(lowerBound, upperBound, upperBoundInclusive = false)
    localRandom().nextInt(lowerBound, upperBound).toShort
  }

  def randomInt(
    lowerBound: Int = Int.MinValue,
    upperBound: Int = Int.MaxValue,
    upperBoundInclusive: Boolean = true
  ): Int = {
    IntRange.validate(lowerBound, upperBound, upperBoundInclusive)
    if (upperBoundInclusive) localRandom().nextLong(lowerBound, upperBound + 1L).intValue()
    else localRandom().nextInt(lowerBound, upperBound)
  }

  def randomPositiveInt(
    lowerBound: Int = 1,
    upperBound: Int = Int.MaxValue,
    upperBoundInclusive: Boolean = true
  ): Int = {
    PositiveIntRange.validate(lowerBound, upperBound, upperBoundInclusive)
    val upper = if (upperBoundInclusive && upperBound < Int.MaxValue) upperBound + 1 else upperBound
    localRandom().nextInt(lowerBound, upper)
  }

  def randomNegativeInt(lowerBound: Int = Int.MinValue, upperBound: Int = 0): Int = {
    NegativeIntRange.validate(lowerBound, upperBound, upperBoundInclusive = false)
    localRandom().nextInt(lowerBound, upperBound)
  }

  def randomLong(
    lowerBound: Long = Long.MinValue,
    upperBound: Long = Long.MaxValue,
    upperBoundInclusive: Boolean = true
  ): Long = {
    LongRange.validate(lowerBound, upperBound, upperBoundInclusive)
    if (upperBoundInclusive)
      if (upperBound == Long.MaxValue)
        if (localRandom().nextLong() == Long.MaxValue) Long.MaxValue
        else localRandom().nextLong(lowerBound, upperBound)
      else localRandom().nextLong(lowerBound, upperBound)
    else localRandom().nextLong(lowerBound, upperBound)
  }

  def randomPositiveLong(
    lowerBound: Long = 1L,
    upperBound: Long = Long.MaxValue,
    upperBoundInclusive: Boolean = true
  ): Long = {
    PositiveLongRange.validate(lowerBound, upperBound, upperBoundInclusive)
    val upper = if (upperBoundInclusive && upperBound < Long.MaxValue) upperBound + 1L else upperBound
    localRandom().nextLong(lowerBound, upper)
  }

  def randomNegativeLong(lowerBound: Long = Long.MinValue, upperBound: Long = 0L): Long = {
    NegativeLongRange.validate(lowerBound, upperBound, upperBoundInclusive = false)
    localRandom().nextLong(lowerBound, upperBound)
  }

  def randomBigInt(numDigits: Int): BigInt =
    randomBigNumber(BigZeroInt, randomPositiveBigInt(numDigits), randomNegativeBigInt(numDigits))

  def randomPositiveBigInt(numDigits: Int): BigInt = buildNonZeroInteger(numDigits)

  def randomNegativeBigInt(numDigits: Int): BigInt = buildNonZeroInteger(numDigits, negative = true)

  /** Returns a random unsigned (either 0 or positive) [[BigInt]].
    * For efficiency, this implementation does not support any number larger than [[MaxUnsignedBigInt]].
    *
    * @param lowerBound the lower bound (min: [[BigZeroInt]], max: [[MaxUnsignedBigInt]]), inclusive
    * @param upperBound the upper bound (min: {{{ lowerBound + 1 }}}, max: [[MaxUnsignedBigInt]]), inclusive
    * @return a random [[BigInt]] between the given range
    */
  def randomUnsignedBigInt(lowerBound: BigInt = BigZeroInt, upperBound: BigInt = MaxUnsignedBigInt): BigInt = {
    require(lowerBound >= BigZeroInt, s"The lower bound must be greater than or equal to $BigZeroInt!")
    require(upperBound <= MaxUnsignedBigInt, s"The upper bound must be less than or equal to $MaxUnsignedBigInt!")
    require(lowerBound <= upperBound, "The lower bound must be less than or equal to the upper bound!")

    (lowerBound, upperBound) match {
      case (BigZeroInt, BigZeroInt)               => BigZeroInt
      case (BigOneInt, BigOneInt)                 => BigOneInt
      case (MaxUnsignedBigInt, MaxUnsignedBigInt) => MaxUnsignedBigInt
      case (l, u) if l == u                       => l
      case (l, u) if l <= MaxSignedSqlBigInt && u <= MaxSignedSqlBigInt =>
        BigInt(randomLong(lowerBound = lowerBound.longValue(), upperBound = upperBound.longValue()))
      case (l, u) if l >= MaxSignedSqlBigInt =>
        val diff = u - l
        val randomDelta = randomLong(lowerBound = 0L, upperBound = diff.longValue())
        l + randomDelta
      case (l, u) =>
        l + BigInt(randomLong(lowerBound = 0L, upperBound = (u - MaxSignedSqlBigInt - BigOneInt).longValue()))
    }
  }

  /** Returns a random [[BigDecimal]].
    * Default precision is [[DefaultBigDecimalPrecision]].
    * If the random [[BigDecimal]] happens to be zero, the precision is ignored.
    *
    * @param precision the precision greater than 0
    * @param scale     the scale greater than or equal to 0
    * @return a random [[BigDecimal]] number
    * @throws IllegalArgumentException thrown when precision is less than or equal to zero
    */
  def randomBigDecimal(
    precision: Int = DefaultBigDecimalPrecision,
    scale: Int = DefaultBigDecimalScale
  ): BigDecimal =
    randomBigNumber(
      BigZeroDecimal,
      randomPositiveBigDecimal(precision = precision, scale = scale),
      randomNegativeBigDecimal(precision = precision, scale = scale))

  /** Returns a positive random [[BigDecimal]].
    * Default precision is [[DefaultBigDecimalPrecision]].
    *
    * @param precision the precision greater than 0
    * @param scale     the scale greater than or equal to 0
    * @return a random [[BigDecimal]] number
    * @throws IllegalArgumentException thrown when precision is less than or equal to zero
    */
  def randomPositiveBigDecimal(
    precision: Int = DefaultBigDecimalPrecision,
    scale: Int = DefaultBigDecimalScale
  ): BigDecimal =
    buildNonZeroDecimal(precision, scale, negative = false)

  /** Returns a negative random [[BigDecimal]].
    * Default precision is [[DefaultBigDecimalPrecision]].
    *
    * @param precision the precision greater than 0
    * @param scale     the scale greater than or equal to 0
    * @return a random [[BigDecimal]] number
    * @throws IllegalArgumentException thrown when precision is less than or equal to zero
    */
  def randomNegativeBigDecimal(
    precision: Int = DefaultBigDecimalPrecision,
    scale: Int = DefaultBigDecimalScale
  ): BigDecimal =
    buildNonZeroDecimal(precision, scale, negative = true)

  /** Returns a [[LocalDate]] between the specified lower (inclusive) and upper (exclusive) bounds.
    * If the lower bound is same as the upper bound, the lower bound is returned as is.
    * If the upper bound is less than the lower bound, [[IllegalArgumentException]] is thrown.
    *
    * @param lowerBound the lower bound
    * @param upperBound the upper bound
    * @return a random [[LocalDate]] between lower bound and upper bound
    * @throws IllegalArgumentException thrown when lower bound is greater than upper bound
    */
  def randomLocalDate(
    lowerBound: LocalDate = defaultDateLowerBound,
    upperBound: LocalDate = currentDate
  ): LocalDate =
    lowerBound.compareTo(upperBound) match {
      case 0 => lowerBound
      case neg if neg < 0 =>
        LocalDate.ofEpochDay(randomLong(lowerBound = lowerBound.toEpochDay, upperBound = upperBound.toEpochDay))
      case _ =>
        throw new IllegalArgumentException(s"The upper bound ($upperBound) is less than the lower bound ($lowerBound)")
    }

  /** Returns a [[LocalDateTime]] between the specified lower (inclusive) and upper (exclusive) bounds.
    * If the lower bound is same as the upper bound, the lower bound is returned as is.
    * If the upper bound is less than the lower bound, [[IllegalArgumentException]] is thrown.
    *
    * @param lowerBound the lower bound
    * @param upperBound the upper bound
    * @return a random [[LocalDateTime]] between lower bound and upper bound
    * @throws IllegalArgumentException thrown when lower bound is greater than upper bound
    */
  def randomLocalDateTime(
    lowerBound: LocalDateTime = defaultDateTimeLowerBound,
    upperBound: LocalDateTime = currentDateTime
  ): LocalDateTime =
    lowerBound.compareTo(upperBound) match {
      case 0 => lowerBound
      case neg if neg < 0 =>
        val diff = upperBound.toEpochSecond(ZoneOffset.UTC) - lowerBound.toEpochSecond(ZoneOffset.UTC)
        val secondsToAdd = randomLong(lowerBound = 0L, upperBound = diff)
        lowerBound.plus(secondsToAdd, ChronoUnit.SECONDS)
      case _ =>
        throw new IllegalArgumentException(s"The upper bound ($upperBound) is less than the lower bound ($lowerBound)")
    }

  /** Returns a [[Instant]] between the specified lower (inclusive) and upper (exclusive) bounds.
    * If the lower bound is same as the upper bound, the lower bound is returned as is.
    * If the upper bound is less than the lower bound, [[IllegalArgumentException]] is thrown.
    *
    * @param lowerBound the lower bound
    * @param upperBound the upper bound
    * @return a random [[Instant]] between lower bound and upper bound
    * @throws IllegalArgumentException thrown when lower bound is greater than upper bound
    */
  def randomInstant(
    lowerBound: Instant = defaultInstantLowerBound,
    upperBound: Instant = currentInstant
  ): Instant =
    lowerBound.compareTo(upperBound) match {
      case 0 => lowerBound
      case neg if neg < 0 =>
        Instant.ofEpochMilli(randomLong(lowerBound = lowerBound.toEpochMilli, upperBound = upperBound.toEpochMilli))
      case _ =>
        throw new IllegalArgumentException(s"The upper bound ($upperBound) is less than the lower bound ($lowerBound)")
    }

  def randomPrintableString(length: Int): String = RandomStringUtils.randomPrint(length)

  def randomAlphabetic(length: Int): String = RandomStringUtils.randomAlphabetic(length)

  def randomAlphanumeric(length: Int): String = RandomStringUtils.randomAlphanumeric(length)

  def randomInts(
    numValues: Int,
    lowerBound: Int,
    upperBound: Int,
    upperBoundInclusive: Boolean = false
  ): Seq[Int] = {
    require(numValues >= 0, "The number of values must be non-negative!")

    val ub = if (upperBoundInclusive) upperBound + 1 else upperBound
    require(lowerBound < ub, "The upper bound must be greater than the lower bound!")

    val maxNumValues = ub - lowerBound
    numValues match {
      case max if max == maxNumValues => lowerBound to upperBound
      case lessThanMax if lessThanMax < maxNumValues =>
        localRandom().ints(lowerBound, ub).distinct().limit(numValues).sorted().toArray
      case _ => throw new IllegalArgumentException(s"The number of values cannot exceed $maxNumValues!")
    }
  }

  def randomElementFrom[T](iterable: Iterable[T]): T = {
    val size = iterable.size
    require(size > 0, "The TraversableOnce instance is empty!")

    if (size == 1)
      iterable.head
    else {
      val i = randomPositiveInt(upperBound = size)
      val iterator = iterable.iterator

      var currIdx = 0
      var element: T = null.asInstanceOf[T]
      while (currIdx < i) {
        element = iterator.next()
        currIdx += 1
      }

      element
    }
  }

  def randomElementsFrom[T](iterable: Iterable[T], numElements: Int): Seq[T] = {
    require(numElements > 0, "The number of elements must be positive!")

    iterable.size match {
      case size if size < 1 => throw new IllegalArgumentException("The iterable is empty!")
      case size if size < numElements =>
        throw new IllegalArgumentException(
          s"The number of elements ($numElements) is smaller than the size of the given iterable (${iterable.size})")
      case size if size == numElements => iterable.toSeq
      case size =>
        val seq = iterable.toIndexedSeq
        if (numElements == 1) Seq(seq(randomInt(lowerBound = 0, upperBound = size, upperBoundInclusive = false)))
        else randomInts(numElements, 0, size).map(seq(_))
    }
  }

  def shuffle[T, CC[X] <: TraversableOnce[X]](traversable: CC[T])(implicit bf: CanBuildFrom[CC[T], T, CC[T]]): CC[T] = {
    val shuffled = util.Random.shuffle(traversable)
    shuffled.size match {
      case 0 | 1 => shuffled // shuffled is a copied instance
      case _ =>
        val inIterator = traversable.toIterator
        val outIterator = shuffled.toIterator
        var diff = false
        while (!diff && inIterator.hasNext && outIterator.hasNext) {
          val in = inIterator.next()
          val out = outIterator.next()
          if (in != out)
            diff = true
        }
        if (diff) shuffled else shuffle(traversable)
    }
  }

  private def localRandom(/* IO */ ): ThreadLocalRandom = ThreadLocalRandom.current()

  private def randomBigNumber[BigN](zero: => BigN, positive: => BigN, negative: => BigN): BigN =
    randomPositiveInt(upperBound = 104729) % 499 match {
      case 263                     => zero
      case even if (even % 2) == 0 => positive
      case _                       => negative
    }

  private def buildNonZeroInteger(numDigits: Int, negative: Boolean = false): BigInt = {
    require(numDigits > 0, "The number of digits must be positive!")

    val sb = new StringBuilder(numDigits + (if (negative) 1 else 0))
    if (negative)
      sb ++= "-"

    var i = 0
    while (i < numDigits) {
      val digit = if (i == 0) randomNonZeroDigit() else randomDigit()
      sb ++= digit.toString
      i += 1
    }

    BigInt(sb.toString)
  }

  private def buildNonZeroDecimal(precision: Int, scale: Int, negative: Boolean): BigDecimal = {
    require(precision > 0, "The precision must be greater than 0!")
    require(scale >= 0, "The scale must be non-negative!")

    (precision, scale) match {
      case (1, 0) =>
        // return an integer between 1 and 9 or between -1 and -9 if negative == true
        val digit = randomNonZeroDigit()
        BigDecimal(if (negative) -digit else digit)
      case (1, 1)           => BigDecimal((if (negative) "-0." else "0.") + randomNonZeroDigit())
      case (p, s) if s == 0 =>
        // return an integer
        val sb = new StringBuilder(p + (if (negative) 1 else 0))
        if (negative)
          sb ++= "-"

        var i = 0
        while (i < precision) {
          val digit = if (i == 0) randomNonZeroDigit() else randomDigit()
          sb ++= digit.toString
          i += 1
        }
        BigDecimal(sb.toString)
      case (p, s) if p == s =>
        val sb = new StringBuilder(p + 2 + (if (negative) 1 else 0)) // sufficient capacity to avoid resizing
        if (negative)
          sb ++= "-"
        sb ++= "0."

        val lastIdx = p - 1
        var i = 0
        while (i <= lastIdx) {
          val digit = if (i == 0 || i == lastIdx) randomNonZeroDigit() else randomDigit()
          sb ++= digit.toString
          i += 1
        }
        BigDecimal(sb.toString)
      case (p, s) if p < s =>
        // when precision is less than scale, the range is (-1, 1)
        val sb = new StringBuilder(s + 2 + (if (negative) 1 else 0))
        if (negative)
          sb ++= "-"
        sb ++= "0."

        // prepend leading 0
        sb ++= "0" * (s - p)

        val lastIdx = p - 1
        var i = 0
        while (i <= lastIdx) {
          val digit = if (i == 0 || i == lastIdx) randomNonZeroDigit() else randomDigit()
          sb ++= digit.toString
          i += 1
        }
        BigDecimal(sb.toString)
      case (p, s) /* if p > s */ =>
        // when precision is greater than scale, scale is the number of decimal places
        val sb = new StringBuilder(p + 1 + (if (negative) 1 else 0))
        if (negative)
          sb ++= "-"

        val lastIdxBeforeDecimalPoint = p - s - 1
        var nonDecimalIdx = 0
        while (nonDecimalIdx <= lastIdxBeforeDecimalPoint) {
          val digit = if (nonDecimalIdx == 0) randomNonZeroDigit() else randomDigit()
          sb ++= digit.toString
          nonDecimalIdx += 1
        }

        sb ++= "."

        val lastIdx = s - 1
        var decimalIdx = 0
        while (decimalIdx <= lastIdx) {
          val digit = if (decimalIdx == lastIdx) randomNonZeroDigit() else randomDigit()
          sb ++= digit.toString
          decimalIdx += 1
        }

        BigDecimal(sb.toString)
    }
  }

  private def randomDigit(lowerBound: Int = 0, upperBound: Int = 10): Int = {
    require(lowerBound >= 0, "The lower bound must be greater than or equal to 0!")
    require(lowerBound <= 10, "The upper bound must be less than or equal to 10!")
    randomInt(lowerBound = lowerBound, upperBound = upperBound, upperBoundInclusive = false)
  }

  private def randomNonZeroDigit(lowerBound: Int = 1, upperBound: Int = 10): Int = {
    require(lowerBound >= 1, "The lower bound must be greater than or equal to 1!")
    require(lowerBound <= 10, "The upper bound must be less than or equal to 10!")
    randomInt(lowerBound = lowerBound, upperBound = upperBound, upperBoundInclusive = false)
  }

  private sealed trait NumRange {

    def validate(lowerBound: Long, upperBound: Long, upperBoundInclusive: Boolean): Unit =
      require(
        if (upperBoundInclusive) lowerBound <= upperBound else lowerBound < upperBound,
        s"The lower bound ($lowerBound) must be less than the upper bound ($upperBound)!"
      )
  }

  private final case class AllRange(minValue: Long, maxValue: Long) extends NumRange {

    override def validate(lowerBound: Long, upperBound: Long, upperBoundInclusive: Boolean): Unit = {
      require(lowerBound >= minValue, s"The lower bound must be greater than or equal to $minValue!")
      require(upperBound <= maxValue, s"The upper bound must be less than or equal to $maxValue!")
      super.validate(lowerBound, upperBound, upperBoundInclusive)
    }
  }

  private final case class PositiveRange(maxValue: Long) extends NumRange {

    require(maxValue > 0L, "The maximum value must be positive!")

    override def validate(lowerBound: Long, upperBound: Long, upperBoundInclusive: Boolean): Unit = {
      require(lowerBound > 0L, "The lower bound must be positive!")
      require(upperBound > 0L, "The upper bound must be positive!")
      super.validate(lowerBound, upperBound, upperBoundInclusive)
    }
  }

  private final case class NegativeRange(minValue: Long) extends NumRange {

    require(minValue < 0L, "The minimum value must be negative!")

    override def validate(lowerBound: Long, upperBound: Long, upperBoundInclusive: Boolean): Unit = {
      require(lowerBound < 0L, "The lower bound must be negative!")
      require(upperBound <= 0L, "The upper bound must be negative or zero!")
      super.validate(lowerBound, upperBound, upperBoundInclusive)
    }
  }
}
