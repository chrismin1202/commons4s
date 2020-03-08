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
package com.chrism.commons

final class FunTestSuiteTest extends FunTestSuite {

  import FunTestSuiteTest._

  test("asserting floating point member with tolerance: Float") {
    assertProductWithFloatingPoints(FloatDummy(0.12f), FloatDummy(0.4f * 0.3f))
  }

  test("asserting floating point member with tolerance: Option[Float]") {
    assertProductWithFloatingPoints(FloatOptDummy(Some(0.49f)), FloatOptDummy(Some(0.7f * 0.7f)))
    assertProductWithFloatingPoints(FloatOptDummy(None), FloatOptDummy(None))
  }

  test("asserting floating point member with tolerance: Double") {
    assertProductWithFloatingPoints(DoubleDummy(0.15), DoubleDummy(0.5 * 0.3))
  }

  test("asserting floating point member with tolerance: Option[Double]") {
    assertProductWithFloatingPoints(DoubleOptDummy(Some(0.72)), DoubleOptDummy(Some(0.8 * 0.9)))
    assertProductWithFloatingPoints(DoubleOptDummy(None), DoubleOptDummy(None))
  }
}

private[this] object FunTestSuiteTest {

  private final case class FloatDummy(f: Float)

  private final case class FloatOptDummy(f: Option[Float])

  private final case class DoubleDummy(f: Double)

  private final case class DoubleOptDummy(f: Option[Double])
}
