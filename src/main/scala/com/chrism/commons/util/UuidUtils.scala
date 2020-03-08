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

import java.util.UUID

object UuidUtils {

  /** Converts the given string to [[UUID]] v3, which is MD5-based.
    *
    * @param s the string to convert to [[UUID]]
    * @return the [[UUID]] v3 generated from the given string
    * @throws IllegalArgumentException thrown if the given string is null or blank
    */
  def toUuidCaseSensitive(s: String): UUID =
    CaseSensitiveUuidGenerator.generate(StringUtils.requireNonBlank(s))

  /** Converts the given string to [[UUID]] v3, which is MD5-based.
    *
    * As opposed to [[toUuidCaseSensitive()]], no exception is thrown.
    *
    * @param s the string to convert to [[UUID]]
    * @return the [[UUID]] v3 generated from the given string or [[None]]
    */
  def toUuidCaseSensitiveOrNone(s: String): Option[UUID] =
    StringUtils.someIfNotBlank(s).map(CaseSensitiveUuidGenerator.generate)

  /** Converts the given string to [[UUID]] v3, which is MD5-based, case-insensitively,
    * that is, by lower-casing the given string.
    *
    * @param s the string to convert to [[UUID]]
    * @return the [[UUID]] v3 generated from the given string after lower-casing
    * @throws IllegalArgumentException thrown if the given string is null or blank
    */
  def toUuidCaseInsensitive(s: String): UUID = CaseInsensitiveUuidGenerator.generate(StringUtils.requireNonBlank(s))

  /** Converts the given string to [[UUID]] v3, which is MD5-based, case-insensitively,
    * that is, by lower-casing the given string.
    *
    * As opposed to [[toUuidCaseSensitive()]], no exception is thrown.
    *
    * @param s the string to convert to [[UUID]]
    * @return the [[UUID]] v3 generated from the given string after lower-casing or [[None]]
    */
  def toUuidCaseInsensitiveOrNone(s: String): Option[UUID] =
    StringUtils.someIfNotBlank(s).map(CaseInsensitiveUuidGenerator.generate)

  private[this] sealed trait UuidGenerator {

    protected def sanitize(s: String): String

    final def generate(s: String): UUID = UUID.nameUUIDFromBytes(sanitize(s).getBytes("UTF-8"))
  }

  private[this] case object CaseSensitiveUuidGenerator extends UuidGenerator {

    override protected def sanitize(s: String): String = s
  }

  private[this] case object CaseInsensitiveUuidGenerator extends UuidGenerator {

    override protected def sanitize(s: String): String = s.toLowerCase
  }
}
