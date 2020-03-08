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

import com.chrism.commons.FunTestSuite

final class StringUtilsTest extends FunTestSuite {

  import StringUtils.implicits._
  import StringUtilsTest.toNonStringCharSequence

  test("checking whether string is empty: null") {
    assert(StringUtils.isEmpty(null))
    assert(StringUtils.isNotEmpty(null) === false)
  }

  test("checking whether string is empty: empty") {
    assert(StringUtils.isEmpty(""))
    assert(StringUtils.isNotEmpty("") === false)
  }

  test("checking whether string is empty: blank") {
    assert(StringUtils.isEmpty("\n") === false)
    assert(StringUtils.isNotEmpty("\n"))
  }

  test("checking whether string is empty: non-empty") {
    assert(StringUtils.isEmpty("str") === false)
    assert(StringUtils.isNotEmpty("str"))
  }

  test("optionizing string: blank") {
    assert(StringUtils.someIfNotBlank("").isEmpty)
    assert(StringUtils.someIfNotBlank("\n").isEmpty)
  }

  test("optionizing string: non-blank") {
    assertOption("  string  ", StringUtils.someIfNotBlank("  string  "))
  }

  test("checking blank Option[String]: blank") {
    val opt = Some("")
    assert(StringUtils.isEmptyOrBlank(opt))
    assert(StringUtils.isNeitherEmptyNorBlank(opt) === false)
  }

  test("checking blank Option[String]: non-blank") {
    val opt = Some("non-blank")
    assert(StringUtils.isEmptyOrBlank(opt) === false)
    assert(StringUtils.isNeitherEmptyNorBlank(opt))
  }

  test("checking blank Option[String] implicitly: blank") {
    val opt = Some("")
    assert(opt.isEmptyOrBlank)
    assert(opt.isNeitherEmptyNorBlank === false)
  }

  test("checking blank Option[String] implicitly: non-blank") {
    val opt = Some("non-blank")
    assert(opt.isEmptyOrBlank === false)
    assert(opt.isNeitherEmptyNorBlank)
  }

  test("requiring string to be non-blank") {
    intercept[IllegalArgumentException] {
      StringUtils.requireNonBlank(null)
    }
    intercept[IllegalArgumentException] {
      StringUtils.requireNonBlank(" ")
    }
  }

  test("requiring string to be non-blank and returning trimmed") {
    assert(StringUtils.requireNonBlankTrimmed("    str   ") === "str")
  }

  test("filtering blank Option[String]: blank") {
    val opt = Some("")
    assert(StringUtils.noneIfBlank(opt).isEmpty)
  }

  test("filtering blank Option[String]: non-blank") {
    val opt = Some("non-blank")
    assert(StringUtils.noneIfBlank(opt).isDefined)
  }

  test("filtering blank Option[String] implicitly: blank") {
    val opt = Some("")
    assert(opt.noneIfBlank.isEmpty)
  }

  test("filtering blank Option[String] implicitly: non-blank") {
    val opt = Some("non-blank")
    assert(opt.noneIfBlank.isDefined)
  }

  test("trimming Option[String]: blank") {
    val opt = Some("  ")
    assert(opt.someTrimmedIfNotBlank.isEmpty)
  }

  test("trimming Option[String]: non-blank") {
    val opt = Some(" non-blank ")
    assertOption("non-blank", opt.someTrimmedIfNotBlank)
  }

  test("checking whether string contains ignore case") {
    assert(StringUtils.containsIgnoreCase("sTRIng", "tri"))
  }

  test("checking whether string starts with ignore case") {
    assert(StringUtils.startsWithIgnoreCase("sTRIng", "str"))
    assert(StringUtils.startsWithIgnoreCase("sTRIng", "ing") === false)
  }

  test("checking whether string ends with ignore case") {
    assert(StringUtils.endsWithIgnoreCase("sTRIng", "str") === false)
    assert(StringUtils.endsWithIgnoreCase("sTRIng", "ing"))
  }

  test("checking whether string contains whitespaces") {
    assert(StringUtils.containsWhitespace("has whitespace"))
    assert(StringUtils.containsWhitespace("has\twhitespace"))
    assert(StringUtils.containsWhitespace("has\nwhitespace"))
    assert(StringUtils.containsWhitespace("has\r\nwhitespace"))
    assert(StringUtils.containsWhitespace("has_whitespace_NOT") === false)
  }

  test("checking whether string starts with lowercase: blank") {
    assert(StringUtils.startsWithLowerCase(null) === false)
    assert(StringUtils.startsWithLowerCase("") === false)
  }

  test("checking whether string starts with lowercase: non-blank") {
    assert(StringUtils.startsWithLowerCase("PascalCase") === false)
    assert(StringUtils.startsWithLowerCase("a"))
    assert(StringUtils.startsWithLowerCase("snake_case"))
    assert(StringUtils.startsWithLowerCase("camelCase"))
  }

  test("checking whether string contains uppercase: blank") {
    assert(StringUtils.containsUpperCase(null) === false)
    assert(StringUtils.containsUpperCase("") === false)
  }

  test("checking whether string contains uppercase: non-blank") {
    assert(StringUtils.containsUpperCase("not") === false)
    assert(StringUtils.containsUpperCase("Yes"))
    assert(StringUtils.containsUpperCase("yEs"))
    assert(StringUtils.containsUpperCase("yeS"))
  }

  test("checking whether string starts with uppercase: blank") {
    assert(StringUtils.startsWithUpperCase(null) === false)
    assert(StringUtils.startsWithUpperCase("") === false)
  }

  test("checking whether string starts with uppercase: non-blank") {
    assert(StringUtils.startsWithUpperCase("camelCase") === false)
    assert(StringUtils.startsWithUpperCase("snake_case") === false)
    assert(StringUtils.startsWithUpperCase("A"))
    assert(StringUtils.startsWithUpperCase("PascalCase"))
  }

  test("checking whether string contains _: blank") {
    assert(StringUtils.containsUnderscore(null) === false)
    assert(StringUtils.containsUnderscore("") === false)
  }

  test("checking whether string contains _: non-blank") {
    assert(StringUtils.containsUnderscore("camelCase") === false)
    assert(StringUtils.containsUnderscore("_"))
    assert(StringUtils.containsUnderscore("snake_case"))
  }

  test("checking whether string starts with _: blank") {
    assert(StringUtils.startsWithUnderscore(null) === false)
    assert(StringUtils.startsWithUnderscore("") === false)
  }

  test("checking whether string starts with _: non-blank") {
    assert(StringUtils.startsWithUnderscore("camelCase") === false)
    assert(StringUtils.startsWithUnderscore("snake_case") === false)
    assert(StringUtils.startsWithUnderscore("_"))
    assert(StringUtils.startsWithUnderscore("_snake_case"))
  }

  test("checking whether string starts with digit: blank") {
    assert(StringUtils.startsWithDigit(null) === false)
    assert(StringUtils.startsWithDigit("") === false)
  }

  test("checking whether string starts with digit: non-blank") {
    assert(StringUtils.startsWithDigit("camelCase") === false)
    assert(StringUtils.startsWithDigit("PascalCase") === false)
    assert(StringUtils.startsWithDigit("snake_case") === false)
    assert(StringUtils.startsWithDigit("1"))
    assert(StringUtils.startsWithDigit("1_snake_case"))
  }

  test("counting substring matches: case sensitive") {
    assert(StringUtils.countMatches("abababba", "ab") === 3)
    assert(StringUtils.countMatches("ABAbabaB", "ab") === 1)
    assert(StringUtils.countMatches("", " ") === 0)
    assert(StringUtils.countMatches(" \n", "\r") === 0)
    assert(StringUtils.countMatches("has \t\n\rwhitespace", " ") === 1)
  }

  test("counting substring matches: case insensitive, both null") {
    assert(StringUtils.countMatchesIgnoreCase(null, null) === 0)
  }

  test("counting substring matches: case insensitive, source null") {
    assert(StringUtils.countMatchesIgnoreCase(null, "sub") === 0)
  }

  test("counting substring matches: case insensitive, source empty") {
    assert(StringUtils.countMatchesIgnoreCase("", "sub") === 0)
  }

  test("counting substring matches: case insensitive, sub-string null") {
    assert(StringUtils.countMatchesIgnoreCase("str", null) === 0)
  }

  test("counting substring matches: case insensitive, sub-string empty") {
    assert(StringUtils.countMatchesIgnoreCase("str", "") === 0)
  }

  test("counting substring matches: case insensitive") {
    val count = StringUtils.countMatchesIgnoreCase(
      toNonStringCharSequence(
        "Azure Search offers the ability to analyze and " +
          "monitor the performance of your servICe through search traffiC analytIcs."
      ),
      toNonStringCharSequence("IC")
    )
    assert(count === 3)
  }

  test("counting substring matches: case insensitive, repeated consecutively") {
    assert(StringUtils.countMatchesIgnoreCase(toNonStringCharSequence("ABAbabaB"), toNonStringCharSequence("AB")) === 4)
  }

  test("replacing all line-separators") {
    val multiLine =
      """line1
        |line2
        |line3
        |line4""".stripMargin
    assert(StringUtils.replaceAllLineSeparators(multiLine, " ") === "line1 line2 line3 line4")
  }

  test("stripping all line-separators") {
    val multiLine =
      """line1
        |line2
        |line3
        |line4""".stripMargin
    assert(StringUtils.stripAllLineSeparators(multiLine) === "line1line2line3line4")
  }

  test("replacing all whitespaces") {
    val multiLine =
      """line1
        |line2
        |line3
        |line4""".stripMargin
    assert(StringUtils.replaceAllWhitespaces(multiLine, "A ") === "line1A line2A line3A line4")
  }

  test("stripping all whitespaces") {
    val multiLine =
      """line1 line1
        |line2 line2
        |line3 line3
        |line4 line4""".stripMargin
    assert(StringUtils.stripAllWhitespaces(multiLine) === "line1line1line2line2line3line3line4line4")
  }

  test("implicitly replacing all line-separators") {
    val multiLine =
      """line1
        |line2
        |line3""".stripMargin
    assert(multiLine.replaceAllLineSeparators(" ") === "line1 line2 line3")
  }

  test("implicitly stripping all line-separators") {
    val multiLine =
      """line1
        |line2
        |line3""".stripMargin
    assert(multiLine.stripAllLineSeparators === "line1line2line3")
  }

  test("implicitly replacing all whitespaces") {
    val multiLine =
      """line1
        |line2
        |line3""".stripMargin
    assert(multiLine.replaceAllWhitespaces("A ") === "line1A line2A line3")
  }

  test("implicitly stripping all whitespaces") {
    val multiLine =
      """line1 line1
        |line2 line2
        |line3""".stripMargin
    assert(multiLine.stripAllWhitespaces === "line1line1line2line2line3")
  }

  test("checking whether string is camelCased: blank") {
    assert(StringUtils.isCamelCased(null))
    assert(StringUtils.isCamelCased(""))
  }

  test("checking whether string is camelCased: non-blank") {
    assert(StringUtils.isCamelCased("PascalCase") === false)
    assert(StringUtils.isCamelCased("snake_case") === false)
    assert(StringUtils.isCamelCased("1StartsWithNumber") === false)
    assert(StringUtils.isCamelCased("camelCase"))
  }

  test("checking whether string is PascalCased: blank") {
    assert(StringUtils.isPascalCased(null))
    assert(StringUtils.isPascalCased(""))
  }

  test("checking whether string is PascalCased: non-blank") {
    assert(StringUtils.isPascalCased("camelCase") === false)
    assert(StringUtils.isPascalCased("snake_case") === false)
    assert(StringUtils.isPascalCased("1StartsWithNumber") === false)
    assert(StringUtils.isPascalCased("PascalCase"))
  }

  test("checking whether string is snake_cased: blank") {
    assert(StringUtils.isSnakeCased(null))
    assert(StringUtils.isSnakeCased(""))
  }

  test("checking whether string is snake_cased: non-blank") {
    assert(StringUtils.isSnakeCased("camelCase") === false)
    assert(StringUtils.isSnakeCased("PascalCase") === false)
    assert(StringUtils.isSnakeCased("1_starts_with_number") === false)
    assert(StringUtils.isSnakeCased("Not_snake_case") === false)
    assert(StringUtils.isSnakeCased("not_Snake_case") === false)
    assert(StringUtils.isSnakeCased("Not_Snake_Case") === false)
    assert(StringUtils.isSnakeCased("Not_snake_case", mixedCase = true))
    assert(StringUtils.isSnakeCased("not_Snake_case", mixedCase = true))
    assert(StringUtils.isSnakeCased("Not_Snake_Case", mixedCase = true))
    assert(StringUtils.isSnakeCased("_"))
    assert(StringUtils.isSnakeCased("snake"))
    assert(StringUtils.isSnakeCased("snake_case"))
    assert(StringUtils.isSnakeCased("_snake_case"))
  }

  test("camelCasing string: blank") {
    assert(StringUtils.camelCase(null) === null)
    assert(StringUtils.camelCase("") === "")
  }

  test("camelCasing string: non-blank, already camelCased") {
    val alreadyCamelCased = "camelCase"
    val camelCased = StringUtils.camelCase(alreadyCamelCased)
    // making sure that the same string is returned
    assert(camelCased eq alreadyCamelCased)
  }

  test("camelCasing string: non-blank") {
    assert(StringUtils.camelCase("PascalCase") === "pascalCase")
    assert(StringUtils.camelCase("snake_case") === "snakeCase")
    assert(StringUtils.camelCase("Snake_case_with_first_letter_capitalized") === "snakeCaseWithFirstLetterCapitalized")
    assert(StringUtils.camelCase("__Snake_case__") === "snakeCase")
  }

  test("camelCasing string: non-blank, inconvertible") {
    intercept[IllegalArgumentException] {
      StringUtils.camelCase("1PascalCase")
    }
    intercept[IllegalArgumentException] {
      StringUtils.camelCase("1snake_case")
    }
    intercept[IllegalArgumentException] {
      StringUtils.camelCase("1Snake_case_with_first_letter_capitalized")
    }
    intercept[IllegalArgumentException] {
      StringUtils.camelCase("__1Snake_case__")
    }
  }

  test("implicitly camelCasing string: non-blank") {
    assert("PascalCase".camelCase === "pascalCase")
    assert("snake_case".camelCase === "snakeCase")
    assert("Snake_case_with_first_letter_capitalized".camelCase === "snakeCaseWithFirstLetterCapitalized")
    assert("__Snake_case__".camelCase === "snakeCase")
  }
}

private[this] object StringUtilsTest {

  private def toNonStringCharSequence(s: String): CharSequence = if (s == null) null else new StringBuffer(s)
}
