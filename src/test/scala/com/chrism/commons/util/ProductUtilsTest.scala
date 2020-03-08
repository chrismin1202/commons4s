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

final class ProductUtilsTest extends FunTestSuite {

  import ProductUtilsTest._

  test("productEquals makes type-safe check") {
    val dummy1 = Dummy1("d1", 1, b = false)
    val dummy2 = Dummy1("d1", 1, b = false)
    assert(ProductUtils.productEquals(dummy1, dummy2))
    assert(ProductUtils.productEquals(dummy2, dummy1))

    val dummy3 = Dummy2("d1", 1, b = false)
    assert(ProductUtils.productEquals(dummy1, dummy3) === false)
    assert(ProductUtils.productEquals(dummy3, dummy1) === false)
    assert(ProductUtils.productEquals(dummy2, dummy3) === false)
    assert(ProductUtils.productEquals(dummy3, dummy2) === false)
  }
}

private[this] object ProductUtilsTest {

  private final case class Dummy1(s: String, i: Int, b: Boolean)

  private final case class Dummy2(s: String, i: Int, b: Boolean)
}
