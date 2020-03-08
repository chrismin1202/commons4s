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

object CollectionUtils {

  def toNoneIfEmpty[A, CC[X] <: TraversableOnce[X]](traversable: CC[A]): Option[CC[A]] =
    if (traversable.isEmpty) None else Some(traversable)

  def toNoneIfEmpty[K, V](map: Map[K, V]): Option[Map[K, V]] = if (map.isEmpty) None else Some(map)

  object implicits {

    implicit final class CanBeEmptySeqOps[E <: CanBeEmpty](col: Seq[E]) {

      def filterNonEmptyOrNoneIfAllEmpty: Option[Seq[E]] = col.filterNot(_.isEmpty).asNoneIfEmpty

      def anyNonEmpty: Boolean = col.exists(_.nonEmpty)

      def allEmpty: Boolean = col.isEmpty || col.forall(_.isEmpty)
    }

    implicit final class StrSeqOps(col: Seq[String]) {

      def filterNonBlankTrimmedOrNoneIfAllBlank: Option[Seq[String]] =
        col.flatMap(StringUtils.someTrimmedIfNotBlank).asNoneIfEmpty

      def anyNonBlank: Boolean = col.exists(StringUtils.isNotBlank)

      def allBlank: Boolean = col.isEmpty || col.forall(StringUtils.isBlank)
    }

    implicit final class TraOnceOps[A, CC[X] <: TraversableOnce[X]](col: CC[A]) {

      def asNoneIfEmpty: Option[CC[A]] = if (col.isEmpty) None else Some(col)
    }

    implicit final class MapOps[K, V](map: Map[K, V]) {

      def asNoneIfEmpty: Option[Map[K, V]] = if (map.isEmpty) None else Some(map)
    }
  }
}
