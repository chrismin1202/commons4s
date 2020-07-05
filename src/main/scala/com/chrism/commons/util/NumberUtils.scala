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

import scala.reflect.runtime.universe.{typeOf, TypeTag}

object NumberUtils {

  val JBigZeroInt: jm.BigInteger = jm.BigInteger.ZERO

  val JBigOneInt: jm.BigInteger = jm.BigInteger.ONE

  val BigZeroInt: BigInt = JBigZeroInt

  val BigOneInt: BigInt = JBigOneInt

  val JBigZeroDecimal: jm.BigDecimal = jm.BigDecimal.ZERO

  val JBigOneDecimal: jm.BigDecimal = jm.BigDecimal.ONE

  val BigZeroDecimal: BigDecimal = JBigZeroDecimal

  val BigOneDecimal: BigDecimal = JBigOneDecimal

  private[this] val DefaultRoundingMode: jm.RoundingMode = jm.RoundingMode.HALF_UP

  def jByte(i: Int): jl.Byte = i.toByte

  def jShort(i: Int): jl.Short = i.toShort

  def bigInteger(b: Byte): jm.BigInteger = bigInteger(b.toLong)

  def bigInteger(s: Short): jm.BigInteger = bigInteger(s.toLong)

  def bigInteger(i: Int): jm.BigInteger = bigInteger(i.toLong)

  def bigInteger(l: Long): jm.BigInteger = jm.BigInteger.valueOf(l)

  def bigInteger(v: String): jm.BigInteger = {
    require(StringUtils.isNotBlank(v), "The value cannot be blank!")
    new jm.BigInteger(v)
  }

  def jBigDecimal(f: Float): jm.BigDecimal = jBigDecimal(f.toDouble)

  def jBigDecimal(f: Float, mc: jm.MathContext): jm.BigDecimal = jBigDecimal(f.toDouble, mc)

  /** Instantiates an instance of [[jm.BigDecimal]] from the given instance of [[Float]] with the given precision.
    * Note that the scale is adjusted only if the scale of the generated instance of [[jm.BigDecimal]]
    * is strictly larger than the given scale,
    * i.e., {{{ bd.scale() > scale }}} where {{{ bd }}} is of type [[jm.BigDecimal]].
    * Also note that when the scale is adjusted, the precision can go down.
    *
    * @param f         a [[Float]]
    * @param precision the precision
    * @param scale     the scale
    * @return an instance of [[jm.BigDecimal]] with the given precision and scale
    */
  def jBigDecimal(f: Float, precision: Int, scale: Int): jm.BigDecimal = jBigDecimal(f.toDouble, precision, scale)

  def jBigDecimal(d: Double): jm.BigDecimal = new jm.BigDecimal(d)

  def jBigDecimal(d: Double, mc: jm.MathContext): jm.BigDecimal = new jm.BigDecimal(d, mc)

  /** Instantiates an instance of [[jm.BigDecimal]] from the given instance of [[Double]] with the given precision.
    * Note that the scale is adjusted only if the scale of the generated instance of [[jm.BigDecimal]]
    * is strictly larger than the given scale,
    * i.e., {{{ bd.scale() > scale }}} where {{{ bd }}} is of type [[jm.BigDecimal]].
    * Also note that when the scale is adjusted, the precision can go down.
    *
    * @param d         a [[Double]]
    * @param precision the precision
    * @param scale     the scale
    * @return an instance of [[jm.BigDecimal]] with the given precision and scale
    */
  def jBigDecimal(d: Double, precision: Int, scale: Int): jm.BigDecimal =
    adjustJBigDecimal(jBigDecimal(d, mathContext(precision)), precision, scale)

  def jBigDecimal(s: String): jm.BigDecimal = new jm.BigDecimal(s)

  def jBigDecimal(s: String, mc: jm.MathContext): jm.BigDecimal = new jm.BigDecimal(s, mc)

  /** Instantiates an instance of [[jm.BigDecimal]] from the given instance of [[String]] with the given precision.
    * The given [[String]] is expected to contain parsable decimal.
    * Note that the scale is adjusted only if the scale of the generated instance of [[jm.BigDecimal]]
    * is strictly larger than the given scale,
    * i.e., {{{ bd.scale() > scale }}} where {{{ bd }}} is of type [[jm.BigDecimal]].
    * Also note that when the scale is adjusted, the precision can go down.
    *
    * @param s         a stringified decimal value
    * @param precision the precision
    * @param scale     the scale
    * @return an instance of [[jm.BigDecimal]] with the given precision and scale
    */
  def jBigDecimal(s: String, precision: Int, scale: Int): jm.BigDecimal =
    adjustJBigDecimal(jBigDecimal(s, mathContext(precision)), precision, scale)

  /** Copy-constructs the given instance of [[jm.BigDecimal]].
    *
    * @param bd an instance of [[jm.BigDecimal]]
    * @return a copy-constructed instance of `bd`
    */
  def jBigDecimal(bd: jm.BigDecimal): jm.BigDecimal = new jm.BigDecimal(bd.unscaledValue(), bd.scale())

  /** Copies the given instance of [[jm.BigDecimal]] with the given [[jm.MathContext]].
    *
    * @param bd an instance of [[jm.BigDecimal]]
    * @return a copied instance of `bd`
    */
  def jBigDecimal(bd: jm.BigDecimal, mc: jm.MathContext): jm.BigDecimal =
    new jm.BigDecimal(bd.unscaledValue(), bd.scale(), mc)

  def jBigDecimal(bi: jm.BigInteger): jm.BigDecimal = new jm.BigDecimal(bi)

  def mathContext(precision: Int): jm.MathContext = new jm.MathContext(precision, DefaultRoundingMode)

  /** Adjusts the given instance of [[jm.BigDecimal]]
    * if the precision and/or scale is larger than the given precision and scale.
    *
    * @param bd        a [[jm.BigDecimal]]
    * @param precision the precision
    * @param scale     the scale
    * @return an instance of [[jm.BigDecimal]] adjusted with the given precision and scale if adjustment is necessary
    *         else the given instance as is
    */
  def adjustJBigDecimal(bd: jm.BigDecimal, precision: Int, scale: Int): jm.BigDecimal = {
    val adjustedBd = if (bd.precision() > precision) jBigDecimal(bd, mathContext(precision)) else bd
    if (adjustedBd.scale() > scale) adjustedBd.setScale(scale, DefaultRoundingMode) else adjustedBd
  }

  /** Reflectively instantiates the minimum value for the given subtype of [[jl.Number]].
    * Note that as opposed to [[jZero()]], this method does not support [[jm.BigInteger]] and [[jm.BigDecimal]]
    * as there is no minimum value of those types.
    *
    * @tparam JNum a subtype of [[jl.Number]]
    * @return the minimum value as instance of [[JNum]]
    * @throws UnsupportedOperationException thrown if the given type is not one of the supported subtypes
    *                                       of [[jl.Number]]
    */
  def jMinValue[JNum <: jl.Number: TypeTag](): JNum =
    ObjectUtils.requireNonNullOrThrow(
      jMinValueOrNull[JNum],
      throw new UnsupportedOperationException(s"The type ${typeOf[JNum]} is not supported!"))

  /** Reflectively instantiates the minimum value for the given subtype of [[jl.Number]] if the minimum value exists,
    * else returns [[None]].
    * Note that as opposed to [[jZero()]], this method does not support [[jm.BigInteger]] and [[jm.BigDecimal]]
    * as there is no minimum value of those types.
    *
    * @tparam JNum a subtype of [[jl.Number]]
    * @return the minimum value as instance of [[JNum]]
    */
  def jMinValueOrNone[JNum <: jl.Number: TypeTag]: Option[JNum] = Option(jMinValueOrNull[JNum])

  /** Reflectively instantiates the minimum value for the given subtype of [[jl.Number]] if the minimum value exists,
    * else returns the supplied default value.
    *
    * @param defaultValue the default value of type [[JNum]]
    * @tparam JNum a subtype of [[jl.Number]]
    * @return the minimum value as instance of [[JNum]]
    */
  def jMinValueOrDefault[JNum <: jl.Number: TypeTag](defaultValue: => JNum): JNum = {
    val min = jMinValueOrNull[JNum]
    if (min == null) defaultValue else min
  }

  def jMinValueOrNull[JNum <: jl.Number: TypeTag]: JNum = {
    val t = typeOf[JNum]
    val min: Any =
      if (t <:< typeOf[jl.Byte]) jl.Byte.MIN_VALUE
      else if (t <:< typeOf[jl.Short]) jl.Short.MIN_VALUE
      else if (t <:< typeOf[jl.Integer]) jl.Integer.MIN_VALUE
      else if (t <:< typeOf[jl.Long]) jl.Long.MIN_VALUE
      else if (t <:< typeOf[jl.Float]) jl.Float.MIN_VALUE
      else if (t <:< typeOf[jl.Double]) jl.Double.MIN_VALUE
      else null
    min.asInstanceOf[JNum]
  }

  /** Reflectively instantiates zero value for the given subtype of [[jl.Number]].
    *
    * @tparam JNum a subtype of [[jl.Number]]
    * @return the zero as instance of [[JNum]]
    * @throws UnsupportedOperationException thrown if the given type is not one of the supported subtypes
    *                                       of [[jl.Number]]
    */
  def jZero[JNum <: jl.Number: TypeTag](): JNum = {
    val t = typeOf[JNum]
    val zero: Any =
      if (t <:< typeOf[jl.Byte]) 0.toByte
      else if (t <:< typeOf[jl.Short]) 0.toShort
      else if (t <:< typeOf[jl.Integer]) 0
      else if (t <:< typeOf[jl.Long]) 0.toLong
      else if (t <:< typeOf[jl.Float]) 0.toFloat
      else if (t <:< typeOf[jl.Double]) 0.toDouble
      else if (t <:< typeOf[jm.BigInteger]) JBigZeroInt
      else if (t <:< typeOf[jm.BigDecimal]) JBigZeroDecimal
      else throw new UnsupportedOperationException(s"The type $t is not supported!")
    zero.asInstanceOf[JNum]
  }

  /** Reflectively instantiates the maximum value for the given subtype of [[jl.Number]].
    * Note that as opposed to [[jZero()]], this method does not support [[jm.BigInteger]] and [[jm.BigDecimal]]
    * as there is no maximum value of those types.
    *
    * @tparam JNum a subtype of [[jl.Number]]
    * @return the maximum value as instance of [[JNum]]
    * @throws UnsupportedOperationException thrown if the given type is not one of the supported subtypes
    *                                       of [[jl.Number]]
    */
  def jMaxValue[JNum <: jl.Number: TypeTag](): JNum =
    ObjectUtils.requireNonNullOrThrow(
      jMaxValueOrNull[JNum],
      throw new UnsupportedOperationException(s"The type ${typeOf[JNum]} is not supported!"))

  /** Reflectively instantiates the maximum value for the given subtype of [[jl.Number]] if the maximum value exists,
    * else returns [[None]].
    * Note that as opposed to [[jZero()]], this method does not support [[jm.BigInteger]] and [[jm.BigDecimal]]
    * as there is no maximum value of those types.
    *
    * @tparam JNum a subtype of [[jl.Number]]
    * @return the maximum value as instance of [[JNum]]
    */
  def jMaxValueOrNone[JNum <: jl.Number: TypeTag]: Option[JNum] = Option(jMaxValueOrNull[JNum])

  /** Reflectively instantiates the maximum value for the given subtype of [[jl.Number]] if the maximum value exists,
    * else returns the supplied default value.
    *
    * @param defaultValue the default value of type [[JNum]]
    * @tparam JNum a subtype of [[jl.Number]]
    * @return the maximum value as instance of [[JNum]]
    */
  def jMaxValueOrDefault[JNum <: jl.Number: TypeTag](defaultValue: => JNum): JNum = {
    val max = jMaxValueOrNull[JNum]
    if (max == null) defaultValue else max
  }

  private def jMaxValueOrNull[JNum <: jl.Number: TypeTag]: JNum = {
    val t = typeOf[JNum]
    val max: Any =
      if (t <:< typeOf[jl.Byte]) jl.Byte.MAX_VALUE
      else if (t <:< typeOf[jl.Short]) jl.Short.MAX_VALUE
      else if (t <:< typeOf[jl.Integer]) jl.Integer.MAX_VALUE
      else if (t <:< typeOf[jl.Long]) jl.Long.MAX_VALUE
      else if (t <:< typeOf[jl.Float]) jl.Float.MAX_VALUE
      else if (t <:< typeOf[jl.Double]) jl.Double.MAX_VALUE
      else null
    max.asInstanceOf[JNum]
  }

  /** Reflectively casts the given value to [[jm.BigInteger]].
    * Note that this method only supports the integral subtypes of [[jl.Number]]:
    * [[jl.Byte]], [[jl.Short]], [[jl.Integer]], [[jl.Long]], and [[jm.BigInteger]].
    *
    * @param n an instance of [[JIntegral]]
    * @tparam JIntegral an integral subtype of [[jl.Number]]
    * @return the given value as an instance of [[jm.BigInteger]]
    * @throws UnsupportedOperationException thrown if the given type is not one of the supported subtypes
    *                                       of [[jl.Number]], i.e., it is not an integral type
    */
  def toBigInteger[JIntegral <: jl.Number: TypeTag](n: JIntegral): jm.BigInteger = {
    val t = typeOf[JIntegral]
    if (t <:< typeOf[jl.Byte]) bigInteger(n.byteValue())
    else if (t <:< typeOf[jl.Short]) bigInteger(n.shortValue())
    else if (t <:< typeOf[jl.Integer]) bigInteger(n.intValue())
    else if (t <:< typeOf[jl.Long]) bigInteger(n.longValue())
    else if (t <:< typeOf[jm.BigInteger]) n.asInstanceOf[jm.BigInteger]
    else throw new UnsupportedOperationException(s"The type $t is not supported!")
  }

  def toBigInt[JIntegral <: jl.Number: TypeTag](n: JIntegral): BigInt = toBigInteger(n)

  /** Reflectively casts the given instance of [[jm.BigInteger]] to [[JIntegral]].
    * Note that this method only supports the integral subtypes of [[jl.Number]]:
    * [[jl.Byte]], [[jl.Short]], [[jl.Integer]], [[jl.Long]], and [[jm.BigInteger]].
    *
    * @param bi an instance of [[jm.BigInteger]]
    * @tparam JIntegral an integral subtype of [[jl.Number]]
    * @return the given [[jm.BigInteger]] value as an instance of [[JIntegral]]
    * @throws UnsupportedOperationException thrown in the given type is not one of the supported subtypes
    *                                       of [[jl.Number]], i.e., it is not an integral type
    * @throws ArithmeticException           thrown if the given [[jm.BigInteger]] value cannot be converted
    *                                       to [[JIntegral]]
    */
  def toJNum[JIntegral <: jl.Number with Comparable[JIntegral]: TypeTag](bi: jm.BigInteger): JIntegral = {
    val t = typeOf[JIntegral]
    val n: jl.Number =
      if (t <:< typeOf[jl.Byte]) bi.byteValueExact()
      else if (t <:< typeOf[jl.Short]) bi.shortValueExact()
      else if (t <:< typeOf[jl.Integer]) bi.intValueExact()
      else if (t <:< typeOf[jl.Long]) bi.longValueExact()
      else if (t <:< typeOf[jm.BigInteger]) bi
      else throw new UnsupportedOperationException(s"The type $t is not supported!")
    n.asInstanceOf[JIntegral]
  }

  /** @param decimal1 an instance of [[BigDecimal]] (nullable)
    * @param decimal2 another instance of [[BigDecimal]] (nullable)
    * @return false if one of them is null or true if both of them are null;
    *         otherwise, {{{ decimal1 == decimal2 }}} is returned
    */
  def equals(decimal1: BigDecimal, decimal2: BigDecimal): Boolean =
    (decimal1, decimal2) match {
      case (null, null)          => true
      case (_, null) | (null, _) => false
      case (d1, d2)              => d1 == d2
    }

  /** Note that {{{ equals }}} of [[jm.BigDecimal]] compares not only the precision but also the scale,
    * i.e., 0.0 is different from 0.00. This method ignores equality in scale.
    *
    * @param decimal1 an instance of [[BigDecimal]] (nullable)
    * @param decimal2 another instance of [[jm.BigDecimal]] (nullable)
    * @return false if one of them is null or true if both of them are null;
    *         otherwise, {{{ decimal1.bigDecimal.compareTo(decimal2) == 0 }}} is returned
    */
  def equals(decimal1: BigDecimal, decimal2: jm.BigDecimal): Boolean =
    (decimal1, decimal2) match {
      case (null, null)          => true
      case (_, null) | (null, _) => false
      case (d1, d2)              => equalsJBigDecimals(d1.bigDecimal, d2)
    }

  /** Note that {{{ equals }}} of [[jm.BigDecimal]] compares not only the precision but also the scale,
    * i.e., 0.0 is different from 0.00. This method ignores equality in scale.
    *
    * @param decimal1 an instance of [[jm.BigDecimal]] (nullable)
    * @param decimal2 another instance of [[BigDecimal]] (nullable)
    * @return false if one of them is null or true if both of them are null;
    *         otherwise, {{{ decimal1.compareTo(decimal2.bigDecimal) == 0 }}} is returned
    */
  def equals(decimal1: jm.BigDecimal, decimal2: BigDecimal): Boolean =
    (decimal1, decimal2) match {
      case (null, null)          => true
      case (_, null) | (null, _) => false
      case (d1, d2)              => equalsJBigDecimals(d1, d2.bigDecimal)
    }

  /** Note that {{{ equals }}} of [[jm.BigDecimal]] compares not only the precision but also the scale,
    * i.e., 0.0 is different from 0.00. This method ignores equality in scale.
    *
    * @param decimal1 an instance of [[jm.BigDecimal]] (nullable)
    * @param decimal2 another instance of [[jm.BigDecimal]] (nullable)
    * @return false if one of them is null or true if both of them are null;
    *         otherwise, {{{ decimal1.compareTo(decimal2) == 0 }}} is returned
    */
  def equals(decimal1: jm.BigDecimal, decimal2: jm.BigDecimal): Boolean =
    (decimal1, decimal2) match {
      case (null, null)          => true
      case (_, null) | (null, _) => false
      case (d1, d2)              => equalsJBigDecimals(d1, d2)
    }

  private def equalsJBigDecimals(decimal1: jm.BigDecimal, decimal2: jm.BigDecimal): Boolean =
    decimal1.compareTo(decimal2) == 0

  /** @param decimal1 an instance of [[BigDecimal]] (nullable)
    * @param decimal2 another instance of [[BigDecimal]] (nullable)
    * @return 0 if both are null, a negative number if `decimal2` is null, or a positive number if `decimal1` is null;
    *         otherwise, {{{ decimal1.compare(decimal2) }}} is returned
    */
  def compare(decimal1: BigDecimal, decimal2: BigDecimal): Int =
    (decimal1, decimal2) match {
      case (null, null) => 0
      case (_, null)    => -1
      case (null, _)    => 1
      case (d1, d2)     => d1.compare(d2)
    }

  /** @param decimal1 an instance of [[BigDecimal]] (nullable)
    * @param decimal2 another instance of [[jm.BigDecimal]] (nullable)
    * @return 0 if both are null, a negative number if `decimal2` is null, or a positive number if `decimal1` is null;
    *         otherwise, {{{ decimal1.bigDecimal.compareTo(decimal2) }}} is returned
    */
  def compare(decimal1: BigDecimal, decimal2: jm.BigDecimal): Int =
    (decimal1, decimal2) match {
      case (null, null) => 0
      case (_, null)    => -1
      case (null, _)    => 1
      case (d1, d2)     => compareJBigDecimals(d1.bigDecimal, d2)
    }

  /** @param decimal1 an instance of [[jm.BigDecimal]] (nullable)
    * @param decimal2 another instance of [[BigDecimal]] (nullable)
    * @return 0 if both are null, a negative number if `decimal2` is null, or a positive number if `decimal1` is null;
    *         otherwise, {{{ decimal1.compareTo(decimal2.bigDecimal) }}} is returned
    */
  def compare(decimal1: jm.BigDecimal, decimal2: BigDecimal): Int =
    (decimal1, decimal2) match {
      case (null, null) => 0
      case (_, null)    => -1
      case (null, _)    => 1
      case (d1, d2)     => compareJBigDecimals(d1, d2.bigDecimal)
    }

  /** @param decimal1 an instance of [[jm.BigDecimal]] (nullable)
    * @param decimal2 another instance of [[jm.BigDecimal]] (nullable)
    * @return 0 if both are null, a negative number if `decimal2` is null, or a positive number if `decimal1` is null;
    *         otherwise, {{{ decimal1.compareTo(decimal2) }}} is returned
    */
  def compare(decimal1: jm.BigDecimal, decimal2: jm.BigDecimal): Int =
    (decimal1, decimal2) match {
      case (null, null) => 0
      case (_, null)    => -1
      case (null, _)    => 1
      case (d1, d2)     => compareJBigDecimals(d1, d2)
    }

  private def compareJBigDecimals(decimal1: jm.BigDecimal, decimal2: jm.BigDecimal): Int = decimal1.compareTo(decimal2)

  /** @param int1 an instance of [[BigInt]] (nullable)
    * @param int2 another instance of [[BigInt]] (nullable)
    * @return false if one of them is null or true if both of them are null;
    *         otherwise, {{{ int1 == int2 }}} is returned
    */
  def equals(int1: BigInt, int2: BigInt): Boolean =
    (int1, int2) match {
      case (null, null)          => true
      case (_, null) | (null, _) => false
      case (i1, i2)              => i1 == i2
    }

  /** @param int1 an instance of [[BigInt]] (nullable)
    * @param int2 another instance of [[jm.BigInteger]] (nullable)
    * @return false if one of them is null or true if both of them are null;
    *         otherwise, {{{ int1.bigInteger.compareTo(int2) == 0 }}} is returned
    */
  def equals(int1: BigInt, int2: jm.BigInteger): Boolean =
    (int1, int2) match {
      case (null, null)          => true
      case (_, null) | (null, _) => false
      case (i1, i2)              => equalsBigIntegers(i1.bigInteger, i2)
    }

  /** @param int1 an instance of [[jm.BigInteger]] (nullable)
    * @param int2 another instance of [[BigInt]] (nullable)
    * @return false if one of them is null or true if both of them are null;
    *         otherwise, {{{ int1.compareTo(int2.bigInteger) == 0 }}} is returned
    */
  def equals(int1: jm.BigInteger, int2: BigInt): Boolean =
    (int1, int2) match {
      case (null, null)          => true
      case (_, null) | (null, _) => false
      case (i1, i2)              => equalsBigIntegers(i1, i2.bigInteger)
    }

  /** @param int1 an instance of [[jm.BigInteger]] (nullable)
    * @param int2 another instance of [[jm.BigInteger]] (nullable)
    * @return false if one of them is null or true if both of them are null;
    *         otherwise, {{{ int1.compareTo(int2) == 0 }}} is returned
    */
  def equals(int1: jm.BigInteger, int2: jm.BigInteger): Boolean =
    (int1, int2) match {
      case (null, null)          => true
      case (_, null) | (null, _) => false
      case (i1, i2)              => equalsBigIntegers(i1, i2)
    }

  private def equalsBigIntegers(int1: jm.BigInteger, int2: jm.BigInteger): Boolean = int1.compareTo(int2) == 0

  /** @param int1 an instance of [[BigInt]] (nullable)
    * @param int2 another instance of [[BigInt]] (nullable)
    * @return 0 if both are null, a negative number if `int2` is null, or a positive number if `int1` is null;
    *         otherwise, {{{ int1.compare(int2) }}} is returned
    */
  def compare(int1: BigInt, int2: BigInt): Int =
    (int1, int2) match {
      case (null, null) => 0
      case (_, null)    => -1
      case (null, _)    => 1
      case (i1, i2)     => i1.compare(i2)
    }

  /** @param int1 an instance of [[BigInt]] (nullable)
    * @param int2 another instance of [[jm.BigInteger]] (nullable)
    * @return 0 if both are null, a negative number if `int2` is null, or a positive number if `int1` is null;
    *         otherwise, {{{ int1.bigInteger.compareTo(int2) }}} is returned
    */
  def compare(int1: BigInt, int2: jm.BigInteger): Int =
    (int1, int2) match {
      case (null, null) => 0
      case (_, null)    => -1
      case (null, _)    => 1
      case (i1, i2)     => compareBigIntegers(i1.bigInteger, i2)
    }

  /** @param int1 an instance of [[jm.BigInteger]] (nullable)
    * @param int2 another instance of [[BigInt]] (nullable)
    * @return 0 if both are null, a negative number if `int2` is null, or a positive number if `int1` is null;
    *         otherwise, {{{ int1.compareTo(int2.bigInteger) }}} is returned
    */
  def compare(int1: jm.BigInteger, int2: BigInt): Int =
    (int1, int2) match {
      case (null, null) => 0
      case (_, null)    => -1
      case (null, _)    => 1
      case (i1, i2)     => compareBigIntegers(i1, i2.bigInteger)
    }

  /** @param int1 an instance of [[jm.BigInteger]] (nullable)
    * @param int2 another instance of [[jm.BigInteger]] (nullable)
    * @return 0 if both are null, a negative number if `int2` is null, or a positive number if `int1` is null;
    *         otherwise, {{{ int1.compareTo(int2) }}} is returned
    */
  def compare(int1: jm.BigInteger, int2: jm.BigInteger): Int =
    (int1, int2) match {
      case (null, null) => 0
      case (_, null)    => -1
      case (null, _)    => 1
      case (i1, i2)     => compareBigIntegers(i1, i2)
    }

  private def compareBigIntegers(int1: jm.BigInteger, int2: jm.BigInteger): Int = int1.compareTo(int2)

  object implicits {

    implicit final class JBigDecimalOps(bd: jm.BigDecimal) {

      def +(that: jm.BigDecimal): jm.BigDecimal = bd.add(that)

      def +(that: BigDecimal): jm.BigDecimal = this + that.bigDecimal

      def -(that: jm.BigDecimal): jm.BigDecimal = bd.subtract(that)

      def -(that: BigDecimal): jm.BigDecimal = this - that.bigDecimal

      def <(that: jm.BigDecimal): Boolean = bd.compareTo(that) < 0

      def <(that: BigDecimal): Boolean = bd.compareTo(that.bigDecimal) < 0

      def >(that: jm.BigDecimal): Boolean = bd.compareTo(that) > 0

      def >(that: BigDecimal): Boolean = bd.compareTo(that.bigDecimal) > 0

      def <=(that: jm.BigDecimal): Boolean = bd.compareTo(that) <= 0

      def <=(that: BigDecimal): Boolean = bd.compareTo(that.bigDecimal) <= 0

      def >=(that: jm.BigDecimal): Boolean = bd.compareTo(that) >= 0

      def >=(that: BigDecimal): Boolean = bd.compareTo(that.bigDecimal) >= 0
    }

    implicit final class JBigIntegerOps(bd: jm.BigInteger) {

      def +(that: jm.BigInteger): jm.BigInteger = bd.add(that)

      def +(that: BigInt): jm.BigInteger = this + that.bigInteger

      def -(that: jm.BigInteger): jm.BigInteger = bd.subtract(that)

      def -(that: BigInt): jm.BigInteger = this - that.bigInteger

      def <(that: jm.BigInteger): Boolean = bd.compareTo(that) < 0

      def <(that: BigInt): Boolean = bd.compareTo(that.bigInteger) < 0

      def >(that: jm.BigInteger): Boolean = bd.compareTo(that) > 0

      def >(that: BigInt): Boolean = bd.compareTo(that.bigInteger) > 0

      def <=(that: jm.BigInteger): Boolean = bd.compareTo(that) <= 0

      def <=(that: BigInt): Boolean = bd.compareTo(that.bigInteger) <= 0

      def >=(that: jm.BigInteger): Boolean = bd.compareTo(that) >= 0

      def >=(that: BigInt): Boolean = bd.compareTo(that.bigInteger) >= 0
    }

    private[util] sealed abstract class JNumOps[JNum <: jl.Number](n: JNum) {

      def add(that: jl.Byte): JNum = add(that.intValue())

      def add(that: Byte): JNum = add(that.toInt)

      def add(that: jl.Short): JNum = add(that.intValue())

      def add(that: Short): JNum = add(that.toInt)

      def add(that: jl.Integer): JNum = add(that.intValue())

      def add(that: Int): JNum
    }

    implicit final class JByteOps(b: jl.Byte) extends JNumOps[jl.Byte](b) {

      override def add(that: Int): jl.Byte = (b.toInt + that).toByte
    }

    implicit final class JShortOps(s: jl.Short) extends JNumOps[jl.Short](s) {

      override def add(that: Int): jl.Short = (s.toInt + that).toShort
    }
  }
}
