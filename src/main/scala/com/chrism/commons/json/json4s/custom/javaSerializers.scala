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

import java.{math => jm}
import java.time.{LocalDate, LocalDateTime}

import org.json4s.{JDecimal, JString}
import org.json4s.{CustomKeySerializer, CustomSerializer}

// Some CustomSerializers for commonly used classes in java.* packages.

case object BigIntegerSerializer
    extends CustomSerializer[jm.BigInteger](_ =>
      ({
        case jDec: JDecimal => jDec.num.toBigInt().bigInteger
      }, {
        case bigInt: jm.BigInteger => JDecimal(new jm.BigDecimal(bigInt))
      })
    )

case object BigIntegerKeySerializer
    extends CustomKeySerializer[jm.BigInteger](_ =>
      ({
        case s: String => new jm.BigInteger(s)
      }, {
        case bigInt: jm.BigInteger => bigInt.toString
      })
    )

/** A [[CustomSerializer]] for serializing/deserializing [[LocalDate]] to/from ISO-8601 formatted string. */
case object LocalDateSerializer
    extends CustomSerializer[LocalDate](_ =>
      ({
        case jStr: JString => LocalDate.parse(jStr.s)
      }, {
        case date: LocalDate => JString(date.toString)
      })
    )

/** A [[CustomKeySerializer]] serializing/deserializing [[LocalDate]] to/from ISO-8601 formatted string
  * when used as a key in map-like object.
  */
case object LocalDateKeySerializer
    extends CustomKeySerializer[LocalDate](_ =>
      ({
        case s: String => LocalDate.parse(s)
      }, {
        case date: LocalDate => date.toString
      })
    )

/** A [[CustomSerializer]] for serializing/deserializing [[LocalDateTime]] to/from ISO-8601 formatted string. */
case object LocalDateTimeSerializer
    extends CustomSerializer[LocalDateTime](_ =>
      ({
        case jStr: JString => LocalDateTime.parse(jStr.s)
      }, {
        case date: LocalDateTime => JString(date.toString)
      })
    )

/** A [[CustomKeySerializer]] serializing/deserializing [[LocalDateTime]] to/from ISO-8601 formatted string
  * when used as a key in map-like object.
  */
case object LocalDateTimeKeySerializer
    extends CustomKeySerializer[LocalDateTime](_ =>
      ({
        case s: String => LocalDateTime.parse(s)
      }, {
        case date: LocalDateTime => date.toString
      })
    )
