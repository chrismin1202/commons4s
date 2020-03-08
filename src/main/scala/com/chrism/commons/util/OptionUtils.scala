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

import com.chrism.commons.io.CanBeEmpty

object OptionUtils {

  def isValueEmpty[E <: { def isEmpty: Boolean }](o: Option[E]): Boolean = o.isEmpty || o.exists(_.isEmpty)

  def noneIfEmpty[E <: { def isEmpty: Boolean }](o: Option[E]): Option[E] = o.filterNot(_.isEmpty)

  object implicits {

    import CollectionUtils.implicits._

    implicit final class CanBeEmptySeqOptOps[E <: CanBeEmpty](o: Option[Seq[E]]) {

      def filterNonEmptyOrNoneIfAllEmpty: Option[Seq[E]] = o.flatMap(_.filterNonEmptyOrNoneIfAllEmpty)

      def anyNonEmpty: Boolean = o.exists(_.anyNonEmpty)

      def allEmpty: Boolean = o.isEmpty || o.exists(_.allEmpty)
    }

    implicit final class StrSeqOptOps(o: Option[Seq[String]]) {

      def filterNonBlankTrimmedOrNoneIfAllBlank: Option[Seq[String]] =
        o.flatMap(_.filterNonBlankTrimmedOrNoneIfAllBlank)

      def anyNonBlank: Boolean = o.exists(_.anyNonBlank)

      def allBlank: Boolean = o.isEmpty || o.exists(_.allBlank)
    }

    implicit final class IsEmptyOptOps[E <: { def isEmpty: Boolean }](o: Option[E]) {

      def isValueEmpty: Boolean = o.isEmpty || OptionUtils.isValueEmpty(o)

      def noneIfEmpty: Option[E] = OptionUtils.noneIfEmpty(o)
    }
  }
}
