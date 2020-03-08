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

import com.chrism.commons.FunTestSuite

final class XmlUtilsTest extends FunTestSuite {

  import XmlUtils.implicits._

  test("trimming leading and trailing whitespaces") {
    val xml =
      <names>
        <name>
          Saul Goodman
        </name>
      </names>
    assert((xml \ "name").asNonBlankTextOrNone.getOrFail() !== "Saul Goodman")
    assert((xml \ "name").asNonBlankTrimmedTextOrNone.getOrFail() === "Saul Goodman")
  }

  test("parsing ISO date") {
    val xml =
      <dates>
        <date>
          2001-04-30
        </date>
      </dates>
    assert((xml \ "date").asLocalDate() === LocalDate.of(2001, 4, 30))
    assertOption(LocalDate.of(2001, 4, 30), (xml \ "date").asLocalDateOrNone)
  }

  test("parsing BigDecimal") {
    val xml = <salary>14054990324.560</salary>
    assertBigDecimal(BigDecimal("14054990324.560"), xml.asBigDecimal())
    assertBigDecimalOption(BigDecimal("14054990324.560"), xml.asBigDecimalOrNone)
  }

  test("parsing BigInt") {
    val xml = <salary>123456789012345678901234567890</salary>
    assert(xml.asBigInt() === BigInt("123456789012345678901234567890"))
    assertOption(BigInt("123456789012345678901234567890"), xml.asBigIntOrNone)
  }

  test("parsing attribute") {
    val xml =
      <titles>
        <title id="1">
          Pulp Fiction
        </title>
      </titles>
    assertOption("1", xml \ "title" \@? "id")
    assert((xml \ "title" \@? "idNot").isEmpty)
  }
}
