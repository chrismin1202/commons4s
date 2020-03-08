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
package com.chrism.commons.json.json4s

import com.chrism.commons.FunTestSuite
import org.json4s.JsonAST.{JInt, JObject}

final class JsonLetterCaseTest extends FunTestSuite {

  import JsonLetterCase.implicits._

  test("re-casing from snake_case to camelCase") {
    val snakeCased = JObject("a_field" -> JInt(1), "b_field" -> JInt(2))
    assert(snakeCased.camelCase === JObject("aField" -> JInt(1), "bField" -> JInt(2)))
  }

  test("re-casing from PascalCase to camelCase") {
    val pascalCased = JObject("AField" -> JInt(1), "BField" -> JInt(2))
    assert(pascalCased.camelCase === JObject("aField" -> JInt(1), "bField" -> JInt(2)))
  }

  test("re-casing from camelCase to snake_case") {
    val camelCased = JObject("aField" -> JInt(1), "bField" -> JInt(2))
    assert(camelCased.snakeCase === JObject("a_field" -> JInt(1), "b_field" -> JInt(2)))
  }

  test("re-casing from PascalCase to snake_case") {
    val pascalCased = JObject("AField" -> JInt(1), "BField" -> JInt(2))
    assert(pascalCased.snakeCase === JObject("a_field" -> JInt(1), "b_field" -> JInt(2)))
  }

  test("re-casing from snake_case to PascalCase") {
    val snakeCased = JObject("a_field" -> JInt(1), "b_field" -> JInt(2))
    assert(snakeCased.pascalCase === JObject("AField" -> JInt(1), "BField" -> JInt(2)))
  }

  test("re-casing from camelCase to PascalCase") {
    val camelCased = JObject("aField" -> JInt(1), "bField" -> JInt(2))
    assert(camelCased.pascalCase === JObject("AField" -> JInt(1), "BField" -> JInt(2)))
  }
}
