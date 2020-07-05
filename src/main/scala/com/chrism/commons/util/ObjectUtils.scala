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

import java.{lang => jl, util => ju}

object ObjectUtils {

  import jl.Integer.rotateLeft

  private val HashSeed: Int = 0xcafebabe

  def requireNonNull[T](obj: T): T = ju.Objects.requireNonNull(obj)

  def requireNonNull[T](obj: T, message: => String): T = ju.Objects.requireNonNull(obj, message)

  def requireNonNullOrThrow[T](obj: T, throwable: => Throwable): T =
    if (obj == null) throw throwable
    else obj

  /** Similar to {{{ COALESCE }}} function in SQL, this returns the first non-null value if exists; otherwise,
    * null is returned.
    *
    * @param obj      an object of type [[T]]
    * @param moreObjs more objects of type [[T]]
    * @return the first non-null instance of [[T]] or null if not exists
    */
  def coalesce[T](obj: T, moreObjs: T*): T =
    if (obj == null) moreObjs.find(_ != null).getOrElse(obj)
    else obj

  /** Similar to [[coalesce()]], this returns the first occurrence of [[Some]] if exists; otherwise, None is returned.
    * Note that this method is null-safe.
    *
    * @param opt    an [[Option]] of type [[T]]
    * @param moreOs more instances of [[Option]] of type [[T]]
    * @return the first instance of [[Some]] or [[None]] if not exists
    */
  def firstNonEmpty[T](opt: Option[T], moreOs: Option[T]*): Option[T] =
    if (opt == null || opt.isEmpty) moreOs.find(o => o != null && o.isDefined).getOrElse(None.asInstanceOf[Option[T]])
    else opt

  /** Returns [[Some]] of the given instance if it is neither null nor empty;
    * otherwise, [[None]] is returned.
    *
    * @param e an instance of type [[E]] with {{{ def isEmpty: Boolean }}}
    * @tparam E a type that has {{{ def isEmpty: Boolean }}} as one of the methods
    * @return [[Some]] of the given instance or [[None]] if {{{ e.isEmpty == true }}}
    */
  def nonEmptyOrNone[E <: { def isEmpty: Boolean }](e: E): Option[E] = if (e == null || e.isEmpty) None else Some(e)

  /** Generates the hash of the given sequence of elements.
    * Note that the implementation was based on [[scala.runtime.ScalaRunTime._hashCode()]].
    *
    * @param objs a sequence of objects to generate hash
    * @return the hash
    * @throws IllegalArgumentException thrown if the given sequence is empty
    */
  def hashOf(objs: Seq[Any]): Int =
    objs.size match {
      case 0    => throw new IllegalArgumentException("There must be 1 or more objects to generate hash!")
      case 1    => objs.head.##
      case size => finalizeHash(objs.foldLeft(HashSeed)((h, o) => mix(h, o.##)), size)
    }

  /** Generates the hash of the given sequence of elements.
    * Refer to the other overload for more details.
    *
    * @param obj      an object
    * @param moreObjs additional objects
    * @return the hash
    */
  def hashOf(obj: Any, moreObjs: Any*): Int = hashOf(obj +: moreObjs)

  /** Mixes in a block of data into an intermediate hash value.
    * Note that this method is a literal copy of [[scala.util.hashing.MurmurHash3.mix()]].
    * As [[scala.util.hashing.MurmurHash3]] is not publicly visible, this method had to be copied.
    *
    * @param hash an intermediate hash
    * @param data the data to mix in
    * @return the mixed hash value
    */
  private def mix(hash: Int, data: Int): Int = {
    var h = mixLast(hash, data)
    h = rotateLeft(h, 13)
    h * 5 + 0xe6546b64
  }

  /** Used as the last mixing step.
    * Note that this method is a literal copy of [[scala.util.hashing.MurmurHash3.mixLast()]].
    * As [[scala.util.hashing.MurmurHash3]] is not publicly visible, this method had to be copied.
    * Refer to the ScalaDoc of [[scala.util.hashing.MurmurHash3.mixLast()]] for details.
    *
    * @param hash an intermediate hash
    * @param data the data to mix in
    * @return the mixed hash value
    */
  private def mixLast(hash: Int, data: Int): Int = {
    var k = data

    k *= 0xcc9e2d51
    k = rotateLeft(k, 15)
    k *= 0x1b873593

    hash ^ k
  }

  /** Finalizes a hash to incorporate the length and make sure all bits avalanche.
    * Note that this method is a literal copy of [[scala.util.hashing.MurmurHash3.finalizeHash()]].
    * As [[scala.util.hashing.MurmurHash3]] is not publicly visible, this method had to be copied.
    *
    * @param hash   the hash
    * @param length the number of elements involved in the hash
    * @return the finalized hash
    */
  private def finalizeHash(hash: Int, length: Int): Int = avalanche(hash ^ length)

  /** Forces all bits of the hash to avalanche. Used for finalizing the hash.
    * Note that this method is a literal copy of {{{ private final def avalanche(hash: Int): Int }}}
    * in [[scala.util.hashing.MurmurHash3]].
    * As neither [[scala.util.hashing.MurmurHash3]] nor the method {{{avalanche}}} is publicly visible,
    * this method had to be copied.
    *
    * @param hash the hash
    * @return
    */
  private def avalanche(hash: Int): Int = {
    var h = hash
    h ^= h >>> 16
    h *= 0x85ebca6b
    h ^= h >>> 13
    h *= 0xc2b2ae35
    h ^= h >>> 16
    h
  }
}
