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
package com.chrism.commons.datatype

import com.chrism.commons.util.StringUtils

trait EnumLike extends Product with Serializable {

  /** @return the name of the enum entry */
  def name: String

  /** If the ordinal needs to be customized, the ordinal of all enum entries must be explicitly specified.
    *
    * @return the optional ordinal of the enum entry
    */
  def customOrdinal: Option[Int] = None

  /** @return the alternate names of the enum entry (if any) */
  def aliases: Set[String] = Set.empty

  override final def toString: String = name
}

trait EnumWithOrdinalLike extends EnumLike {

  override final lazy val customOrdinal: Option[Int] = Some(ordinal)

  def ordinal: Int
}

trait EnumLikeCompanionLike[E <: EnumLike] {

  private lazy val ordinalsCustomized: Boolean = {
    val entries = values
    if (entries.exists(_.customOrdinal.isDefined)) {
      require(
        entries.flatMap(_.customOrdinal).distinct.size == entries.size,
        "When ordinals are customized, each and every enum entry must be assigned unique value!"
      )
      true
    } else false
  }

  private lazy val requireUniqueness: Unit = {
    val entries = values
    // Require name uniqueness
    require(
      entries.tails
        .forall(vs =>
          vs.size match {
            case size if size < 2  => true
            case size if size == 2 => !matches(vs.head, vs.last)
            case _ =>
              val head = vs.head
              vs.tail.forall(!matches(head, _))
          }
        ),
      "All names must be unique!"
    )
    // Require all ordinals to be unique
    ordinalsCustomized
    // Enforce other requirements
    require(requirements.forall(r => r(entries)), "One or more of the requirements is/are not met!")
  }

  /** The order in which the values are added defines its ordinal, i.e., its index is its ordinal.
    * It is okay for this method to be overridden as `def`, but this should be overridden as `lazy val`.
    * Note that overriding as `val` can result in initialization errors.
    *
    * @return the enum values
    */
  def values: IndexedSeq[E]

  /** Compares the given name to the name and aliases of the given value and returns true if there is a match.
    *
    * @param name  the name to find
    * @param value the value to compare
    * @return {{{ true }}} if either the given name matches that of the value or one of its aliases
    */
  protected def nameExists(name: String, value: E): Boolean

  /** Returns an additional sequence of requirements to enforce (default: none).
    * A requirement is a function that takes in all the values and
    * returns {{{ true }}} if the values meet the requirement.
    *
    * @return a sequence of requirements to enforce
    */
  protected def requirements: Seq[IndexedSeq[E] => Boolean] = Seq.empty

  /** Returns the [[E]] instance that corresponds to the given name
    * or throws [[IllegalArgumentException]] if there is no [[E]] associated with the given name.
    *
    * @param name the name to find
    * @return the corresponding instance of [[E]]
    * @throws IllegalArgumentException thrown if the given name is {{{ null }}} or blank
    *                                  or there is no corresponding instance of [[E]]
    */
  final def valueOf(name: String): E = {
    require(StringUtils.isNotBlank(name), "The name cannot be blank!")

    valueOfOrNone(name)
      .getOrElse(throw new IllegalArgumentException(s"$name is not a valid name!"))
  }

  /** Returns the [[E]] instance that corresponds to the given name
    * or [[None]] if there is no [[E]] associated with the given name.
    *
    * @param name the name to find
    * @return the corresponding instance of [[E]] or [[None]]
    */
  final def valueOfOrNone(name: String): Option[E] = {
    requireUniqueness

    if (StringUtils.isBlank(name)) None
    else values.find(nameExists(name, _))
  }

  /** Returns the [[E]] instance that corresponds to the given ordinal
    * or throws [[IllegalArgumentException]] if there is no [[E]] associated with the given ordinal.
    *
    * @param ordinal the ordinal to find
    * @return the corresponding instance of [[E]]
    * @throws IllegalArgumentException thrown if the given ordinal does not exist
    */
  final def valueByOrdinalOf(ordinal: Int): E =
    valueByOrdinalOfOrNone(ordinal).getOrElse(throw new IllegalArgumentException(s"$ordinal does not exist!"))

  /** Returns the [[E]] instance that corresponds to the given ordinal
    * or [[None]] if there is no [[E]] associated with the given ordinal.
    *
    * @param ordinal the ordinal to find
    * @return the corresponding instance of [[E]] or [[None]]
    */
  final def valueByOrdinalOfOrNone(ordinal: Int): Option[E] = {
    requireUniqueness

    if (ordinalsCustomized) values.find(_.customOrdinal.exists(_ == ordinal))
    else {
      val iterator = values.iterator
      var i = 0
      var e: Option[E] = None
      while (e.isEmpty && iterator.hasNext) {
        val v = iterator.next()
        if (i == ordinal) {
          e = Some(v)
        }
        i += 1
      }
      e
    }
  }

  /** Finds and returns the ordinal of the given value. An ordinal is the index of the element in [[values]].
    *
    * @param value the value to find
    * @return the ordinal of the value
    * @throws NoSuchElementException thrown if the value does not exist in [[values]]
    */
  final def ordinalOf(value: E): Int = {
    requireUniqueness

    val ordinal =
      if (ordinalsCustomized) {
        val iterator = values.iterator
        var i = 0
        var o = -1
        while (o < 0 && iterator.hasNext) {
          val v = iterator.next()
          if (v == value) {
            o = v.customOrdinal.getOrElse(i)
          }
          i += 1
        }

        if (o < 0) {
          throw new NoSuchElementException(s"$value does not exist in this enumeration!")
        }
        o
      } else values.indexOf(value)

    if (ordinal < 0) {
      throw new NoSuchElementException(s"$value does not exist in this enumeration!")
    }
    ordinal
  }

  private final def matches(v1: E, v2: E): Boolean = nameExists(v1.name, v2) || v1.aliases.exists(nameExists(_, v2))
}

trait CaseSensitiveEnumLikeCompanionLike[E <: EnumLike] extends EnumLikeCompanionLike[E] {

  override protected final def nameExists(name: String, value: E): Boolean =
    name == value.name || value.aliases.contains(name)
}

trait CaseInsensitiveEnumLikeCompanionLike[E <: EnumLike] extends EnumLikeCompanionLike[E] {

  override protected final def nameExists(name: String, value: E): Boolean =
    name.equalsIgnoreCase(value.name) || value.aliases.exists(_.equalsIgnoreCase(name))
}
