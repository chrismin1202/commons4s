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
package com.chrism.commons.json.json4s.custom

import com.chrism.commons.datatype.{EnumLike, EnumLikeCompanionLike}
import org.json4s.{CustomKeySerializer, CustomSerializer, JInt, JString}

final class EnumLikeNameSerializer[E <: EnumLike, C <: EnumLikeCompanionLike[E]] private (
  companion: C
)(
  implicit
  m: Manifest[E])
    extends CustomSerializer[E](_ =>
      ({
        case raw: JString => companion.valueOf(raw.s)
      }, {
        case e: E => JString(e.name)
      }))

object EnumLikeNameSerializer {

  def of[E <: EnumLike, C <: EnumLikeCompanionLike[E]](
    companion: C
  )(
    implicit
    m: Manifest[E]
  ): EnumLikeNameSerializer[E, C] =
    new EnumLikeNameSerializer[E, C](companion)
}

final class EnumLikeNameKeySerializer[E <: EnumLike, C <: EnumLikeCompanionLike[E]] private (
  companion: C
)(
  implicit
  m: Manifest[E])
    extends CustomKeySerializer[E](_ =>
      ({
        case raw: String => companion.valueOf(raw)
      }, {
        case e: E => e.name
      }))

object EnumLikeNameKeySerializer {

  def of[E <: EnumLike, C <: EnumLikeCompanionLike[E]](
    companion: C
  )(
    implicit
    m: Manifest[E]
  ): EnumLikeNameKeySerializer[E, C] =
    new EnumLikeNameKeySerializer[E, C](companion)
}

final class EnumLikeOrdinalSerializer[E <: EnumLike, C <: EnumLikeCompanionLike[E]] private (
  companion: C
)(
  implicit
  m: Manifest[E])
    extends CustomSerializer[E](_ =>
      ({
        case raw: JInt => companion.valueByOrdinalOf(raw.num.intValue())
      }, {
        case e: E => JInt(BigInt(e.customOrdinal.getOrElse(companion.ordinalOf(e))))
      }))

object EnumLikeOrdinalSerializer {

  def of[E <: EnumLike, C <: EnumLikeCompanionLike[E]](
    companion: C
  )(
    implicit
    m: Manifest[E]
  ): EnumLikeOrdinalSerializer[E, C] =
    new EnumLikeOrdinalSerializer[E, C](companion)
}

final class EnumLikeOrdinalKeySerializer[E <: EnumLike, C <: EnumLikeCompanionLike[E]] private (
  companion: C
)(
  implicit
  m: Manifest[E])
    extends CustomKeySerializer[E](_ =>
      ({
        case raw: String => companion.valueByOrdinalOf(raw.toInt)
      }, {
        case e: E => e.customOrdinal.getOrElse(companion.ordinalOf(e)).toString
      }))

object EnumLikeOrdinalKeySerializer {

  def of[E <: EnumLike, C <: EnumLikeCompanionLike[E]](
    companion: C
  )(
    implicit
    m: Manifest[E]
  ): EnumLikeOrdinalKeySerializer[E, C] =
    new EnumLikeOrdinalKeySerializer[E, C](companion)
}
