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

import org.apache.commons.{lang3 => l3}

import scala.util.matching.Regex

/** Object for string related utility methods and a proxy for [[l3.StringUtils]]. */
object StringUtils {

  val LineSeparator: String = System.lineSeparator()
  private val LineSeparatorRegex: Regex = new Regex(LineSeparator)
  private val WhitespaceRegex: Regex = "\\s+".r

  /** Checks if the given [[CharSequence]] is {{{ null }}}, empty.
    *
    * @param cs the [[CharSequence]] to check, may be {{{ null }}}
    * @return {{{ true }}} if the CharSequence is {{{ null }}}, empty
    */
  def isEmpty(cs: CharSequence): Boolean = l3.StringUtils.isEmpty(cs)

  /** Checks if the given [[CharSequence]] is not {{{ null }}}, or empty.
    *
    * @param cs the [[CharSequence]] to check, may be {{{ null }}}
    * @return {{{ true }}} if the CharSequence is neither {{{ null }}} nor empty
    */
  def isNotEmpty(cs: CharSequence): Boolean = l3.StringUtils.isNotEmpty(cs)

  /** Checks if the given [[CharSequence]] is {{{ null }}}, empty, or whitespace.
    * As opposed to [[isEmpty()]],
    * this method returns {{{ true }}} if the given [[CharSequence]] contains nothing but whitespace.
    *
    * @param cs the [[CharSequence]] to check, may be {{{ null }}}
    * @return {{{ true }}} if the CharSequence is {{{ null }}}, empty, or whitespace
    */
  def isBlank(cs: CharSequence): Boolean = l3.StringUtils.isBlank(cs)

  /** Checks if the given [[CharSequence]] is not {{{ null }}}, empty, or whitespace.
    *
    * As opposed to [[isNotEmpty()]],
    * this method returns {{{ false }}} if the given [[CharSequence]] contains nothing but whitespace.
    *
    * @param cs the [[CharSequence]] to check, may be {{{ null }}}
    * @return {{{ true }}} if the CharSequence is  not {{{ null }}}, not empty, and not whitespace
    */
  def isNotBlank(cs: CharSequence): Boolean = l3.StringUtils.isNotBlank(cs)

  /** Checks if the given [[CS]] is not {{{ null }}}, empty, or whitespace and
    * throws [[IllegalArgumentException]] if the given [[CS]] is null or blank; otherwise,
    * the given instance of [[CS]] is returned
    *
    * @param cs the [[CS]] instance to check
    * @return the given [[CS]] instance if not blank
    * @throws IllegalArgumentException thrown if the given [[CS]] instance is null or blank
    */
  def requireNonBlank[CS <: CharSequence](cs: CS): CS = {
    require(isNotBlank(cs), "The given CharSequence is null or blank!")
    cs
  }

  /** Checks if the given string is not {{{ null }}}, empty, or whitespace and
    * throws [[IllegalArgumentException]] if the given string is null or blank; otherwise,
    * the given string is returned
    *
    * @param cs the string to check
    * @return the given string if not blank
    * @throws IllegalArgumentException thrown if the given string is null or blank
    */
  def requireNonBlankTrimmed(cs: String): String = requireNonBlank(cs).trim

  def someIfNotBlank[CS <: CharSequence](cs: CS): Option[CS] = if (isBlank(cs)) None else Some(cs)

  def someTrimmedIfNotBlank(cs: String): Option[String] = if (isBlank(cs)) None else Some(cs.trim)

  def noneIfBlank[CS <: CharSequence](cs: Option[CS]): Option[CS] = cs.filter(StringUtils.isNotBlank)

  /** Returns {{{ true }}} if the given value is either [[None]] or blank, i.e.,
    * {{{
    *   isEmptyOrBlank(None) == true
    *   isEmptyOrBlank(Some("")) == true
    *   isEmptyOrBlank(Some("not!")) == false
    * }}}
    *
    * @param cs the [[Option]] of [[CS]] to check
    * @tparam CS a subclass of [[CharSequence]]
    * @return {{{ true }}} if the given value is either [[None]] or blank
    */
  def isEmptyOrBlank[CS <: CharSequence](cs: Option[CS]): Boolean = cs.isEmpty || cs.exists(isBlank)

  /** Returns {{{ true }}} if the given value is [[Some]] and non-blank, i.e.,
    * {{{
    *   isNeitherEmptyNorBlank(None) == false
    *   isNeitherEmptyNorBlank(Some("")) == false
    *   isNeitherEmptyNorBlank(Some("not!")) == true
    * }}}
    *
    * @param cs the [[Option]] of [[CS]] to check
    * @tparam CS a subclass of [[CharSequence]]
    * @return {{{ true }}} if the given value is [[Some]] and non-blank
    */
  def isNeitherEmptyNorBlank[CS <: CharSequence](cs: Option[CS]): Boolean = cs.isDefined && cs.exists(isNotBlank)

  def containsIgnoreCase(cs: CharSequence, searchStr: CharSequence): Boolean =
    l3.StringUtils.containsIgnoreCase(cs, searchStr)

  def startsWithIgnoreCase(cs: CharSequence, searchStr: CharSequence): Boolean =
    l3.StringUtils.startsWithIgnoreCase(cs, searchStr)

  def endsWithIgnoreCase(cs: CharSequence, searchStr: CharSequence): Boolean =
    l3.StringUtils.endsWithIgnoreCase(cs, searchStr)

  def containsWhitespace(str: CharSequence): Boolean = l3.StringUtils.containsWhitespace(str)

  def startsWithLowerCase(cs: CharSequence): Boolean =
    cs != null && cs.length() > 0 && Character.isLowerCase(cs.charAt(0))

  def containsUpperCase(cs: CharSequence): Boolean =
    if (isBlank(cs)) false
    else {
      var i = 0
      var contains = false
      while (!contains && i < cs.length()) {
        if (Character.isUpperCase(cs.charAt(i))) {
          contains = true
        }
        i += 1
      }
      contains
    }

  def startsWithUpperCase(cs: CharSequence): Boolean =
    cs != null && cs.length() > 0 && Character.isUpperCase(cs.charAt(0))

  def containsUnderscore(cs: CharSequence): Boolean = l3.StringUtils.contains(cs, '_')

  def startsWithUnderscore(cs: CharSequence): Boolean = cs != null && cs.length() > 0 && cs.charAt(0) == '_'

  def startsWithDigit(cs: CharSequence): Boolean = cs != null && cs.length() > 0 && Character.isDigit(cs.charAt(0))

  def countMatches(cs: CharSequence, subCs: CharSequence): Int = l3.StringUtils.countMatches(cs, subCs)

  def countMatchesIgnoreCase(cs: CharSequence, subCs: CharSequence): Int =
    (cs, subCs) match {
      case (null, null)                       => 0
      case (null, _)                          => 0
      case (_, null)                          => 0
      case (a, b) if isEmpty(a) || isEmpty(b) => 0
      case (str: String, sub: String) =>
        val len = sub.length
        val max = str.length - len
        (0 until max).count(str.regionMatches(true, _, sub, 0, len))
      case (otherCs, otherSubCs) =>
        val len = otherCs.length()
        val subLen = otherSubCs.length()
        val max = len - subLen
        var count = 0
        var i = 0
        while (i <= max) {
          var index1 = i
          var index2 = 0
          var tmpLen = subLen

          // Extract these first so we detect NPEs the same as the java.lang.String version
          val srcLen = len - i

          // Check that the regions are long enough
          if (srcLen >= subLen) {
            var exit = false
            var matched = true
            while (!exit && tmpLen >= 0 && index1 < len && index2 < subLen) {
              val c1 = otherCs.charAt(index1)
              val c2 = otherSubCs.charAt(index2)

              if (c1 != c2 && c1.toUpper != c2.toUpper && c1.toLower != c2.toLower) {
                exit = true
                matched = false
              }

              index1 += 1
              index2 += 1
              tmpLen -= 1
            }

            if (matched) {
              count += 1
              i += subLen
            } else {
              i += 1
            }
          }
        }
        count
    }

  def replaceAllLineSeparators(input: String, replacement: String): String =
    LineSeparatorRegex.replaceAllIn(input, replacement)

  def stripAllLineSeparators(input: String): String = replaceAllLineSeparators(input, "")

  def replaceAllWhitespaces(input: String, replacement: String): String =
    WhitespaceRegex.replaceAllIn(input, replacement)

  def stripAllWhitespaces(input: String): String = replaceAllWhitespaces(input, "")

  /** Checks whether the given instance of [[CharSequence]] is camelCased.
    * {{{ true }}} is returned if the given value is blank or
    * neither starts with uppercase nor contains underscore ('_').
    * Otherwise, {{{ false }}} is returned.
    *
    * @param cs the [[CharSequence]] instance to check
    * @return {{{ true }}} if the given value does not start with uppercase and does not contain underscore ('_') or
    *        is blank else {{{ false }}}
    */
  def isCamelCased(cs: CharSequence): Boolean =
    isBlank(cs) || (!startsWithUpperCase(cs) && !startsWithDigit(cs) && !containsUnderscore(cs))

  /** Checks whether the given instance of [[CharSequence]] is PascalCased.
    * {{{ true }}} is returned if the given value is blank or
    * starts with uppercase and does not contain underscore ('_').
    * Otherwise, {{{ false }}} is returned.
    *
    * @param cs the [[CharSequence]] instance to check
    * @return {{{ true }}} if the given value is blank or starts with uppercase and does not contain underscore ('_')
    *         else {{{ false }}}
    */
  def isPascalCased(cs: CharSequence): Boolean =
    isBlank(cs) || (startsWithUpperCase(cs) && !startsWithDigit(cs) && !containsUnderscore(cs))

  /** Checks whether the given instance of [[CharSequence]] is snake_cased.
    * {{{ true }}} is returned if the given value is blank or snake_cased.
    * Otherwise, {{{ false }}} is returned.
    *
    * Note that even if the given value does not contain any underscore ('_'),
    * but if it neither starts with digit nor contains any uppercase, {{{ true }}} is returned.
    * However, if {{{ mixedCase == true }}} and the given value does not contain any underscore,
    * but it contains uppercase, {{{ false }}} is returned.
    *
    * @param cs the [[CharSequence]] instance to check
    * @param mixedCase indicates whether the given instance contains mixed case
    * @return {{{ true }}} if the given value is blank or snake_cased else {{{ false }}}
    */
  def isSnakeCased(cs: CharSequence, mixedCase: Boolean = false): Boolean =
    isBlank(cs) || (!startsWithDigit(cs) && (mixedCase || !containsUpperCase(cs)))

  /** Tries to camelCase the given string.
    *
    * The value is convertible only if it is PascalCased, snake_cased with mixed case, or already camelCased.
    *
    * @param s an instance of string
    * @return camelCased version of the given string
    * @throws IllegalArgumentException thrown if the string cannot be camelCased
    */
  def camelCase(s: String): String =
    s match {
      case camel if isCamelCased(camel) => camel
      case pascal if isPascalCased(pascal) =>
        if (pascal.length == 1) Character.toLowerCase(pascal.charAt(0)).toString
        else {
          val sb = new StringBuilder(pascal.length)
          sb += Character.toLowerCase(pascal.charAt(0)) ++= pascal.substring(1)
          sb.toString()
        }
      case snake if isSnakeCased(snake, mixedCase = true) =>
        if (l3.StringUtils.containsOnly(snake, '_')) {
          throw new IllegalArgumentException(s"The input string '$snake' cannot be camelCased!")
        }

        val sb = new StringBuilder(snake.length)
        val tokens = l3.StringUtils.split(snake, '_')
        var i = 0
        var firstToken = true
        while (i < tokens.length) {
          val token = tokens(i)
          if (isNotBlank(token)) {
            val firstChar = token.charAt(0)
            if (firstToken) {
              if (Character.isDigit(firstChar)) {
                throw new IllegalArgumentException(s"The first letter of first token cannot be a digit!")
              }
              firstToken = false
              if (Character.isUpperCase(firstChar)) {
                sb += Character.toLowerCase(firstChar)
                if (token.length > 1) {
                  sb ++= token.substring(1)
                }
              } else sb ++= token
            } else {
              if (Character.isLowerCase(firstChar)) {
                sb += Character.toUpperCase(firstChar)
                if (token.length > 1) {
                  sb ++= token.substring(1)
                }
              } else sb ++= token
            }
          }
          i += 1
        }
        sb.toString()
      case other =>
        throw new IllegalArgumentException(
          s"$other cannot be camelCased! Only camelCased, PascalCased, and snake_cased strings are supported."
        )
    }

  object implicits {

    implicit final class StringOptOps(cs: Option[String]) {

      def someTrimmedIfNotBlank: Option[String] = cs.flatMap(StringUtils.someTrimmedIfNotBlank)
    }

    implicit final class CharSeqOptOps[CS <: CharSequence](cs: Option[CS]) {

      def isEmptyOrBlank: Boolean = StringUtils.isEmptyOrBlank(cs)

      def isNeitherEmptyNorBlank: Boolean = StringUtils.isNeitherEmptyNorBlank(cs)

      def noneIfBlank: Option[CS] = StringUtils.noneIfBlank(cs)
    }

    implicit final class StringOps(s: String) {

      def replaceAllLineSeparators(replacement: String): String = StringUtils.replaceAllLineSeparators(s, replacement)

      def stripAllLineSeparators: String = StringUtils.stripAllLineSeparators(s)

      def replaceAllWhitespaces(replacement: String): String = StringUtils.replaceAllWhitespaces(s, replacement)

      def stripAllWhitespaces: String = StringUtils.stripAllWhitespaces(s)

      def camelCase: String = StringUtils.camelCase(s)
    }
  }
}
