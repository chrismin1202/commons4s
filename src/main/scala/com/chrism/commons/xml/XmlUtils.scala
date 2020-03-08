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
package com.chrism.commons.xml

import java.time.LocalDate

import com.chrism.commons.util.StringUtils

import scala.util.Try
import scala.xml.NodeSeq

object XmlUtils {

  object implicits {

    implicit final class ElemOps(xml: NodeSeq) {

      def asTrimmedText: String = xml.text.trim

      def asNonBlankTextOrNone: Option[String] = {
        val text = xml.text
        if (StringUtils.isBlank(text)) None else Some(text)
      }

      def asNonBlankTrimmedTextOrNone: Option[String] = {
        val text = xml.text
        if (StringUtils.isBlank(text)) None else Some(text.trim)
      }

      /** Converts the text in the node to [[Boolean]].
        *
        * @return the [[Boolean]] value parsed from the text value in the node
        * @throws IllegalArgumentException thrown if the node does not contain a parsable [[Boolean]] value
        */
      def asBoolean(): Boolean = asTrimmedText.toBoolean

      def asBooleanOrNone: Option[Boolean] = asNonBlankTrimmedTextOrNone.flatMap(t => Try(t.toBoolean).toOption)

      /** Converts the text in the node to [[Int]].
        *
        * @return the [[Int]] value parsed from the text value in the node
        * @throws IllegalArgumentException thrown if the node does not contain a parsable [[Int]] value
        */
      def asInt(): Int = asTrimmedText.toInt

      def asIntOrNone: Option[Int] = asNonBlankTrimmedTextOrNone.flatMap(t => Try(t.toInt).toOption)

      /** Converts the text in the node to [[Double]].
        *
        * @return the [[Double]] value parsed from the text value in the node
        * @throws IllegalArgumentException thrown if the node does not contain a parsable [[Double]] value
        */
      def asDouble(): Double = asTrimmedText.toDouble

      def asDoubleOrNone: Option[Double] = asNonBlankTrimmedTextOrNone.flatMap(t => Try(t.toDouble).toOption)

      /** Converts the text in the node to [[T]].
        *
        * @return the [[T]] value parsed from the text value in the node
        * @throws IllegalArgumentException thrown if the node cannot be converted to [[T]]
        */
      def asTyped[T](convert: String => T): T = convert(asTrimmedText)

      def asTypedOrNone[T](convert: String => T): Option[T] =
        asNonBlankTrimmedTextOrNone.flatMap(t => Try(convert(t)).toOption)

      /** Converts ISO-formatted date, i.e., {{{ yyyy-MM-dd }}}, to [[LocalDate]].
        *
        * @return the [[LocalDate]] parsed from ISO-formatted date
        * @throws IllegalArgumentException thrown if the node cannot be converted to [[LocalDate]]
        * @throws java.time.format.DateTimeParseException thrown if the text value in the node is not ISO-formatted date
        */
      def asLocalDate(): LocalDate = asTyped(LocalDate.parse)

      def asLocalDateOrNone: Option[LocalDate] = asTypedOrNone(LocalDate.parse)

      def asBigDecimal(): BigDecimal = asTyped(BigDecimal(_))

      def asBigDecimalOrNone: Option[BigDecimal] = asTypedOrNone(BigDecimal(_))

      def asBigInt(): BigInt = asTyped(BigInt(_))

      def asBigIntOrNone: Option[BigInt] = asTypedOrNone(BigInt(_))

      def \@?(attributeName: String): Option[String] = StringUtils.someTrimmedIfNotBlank(xml \@ attributeName)
    }
  }
}
