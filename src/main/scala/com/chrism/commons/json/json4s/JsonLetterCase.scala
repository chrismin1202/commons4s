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

import java.{util => ju}

import org.json4s.{JField, JValue}

sealed trait JsonLetterCase extends Product with Serializable {

  def format(jv: JValue): JValue
}

object JsonLetterCase {

  case object Camel extends JsonLetterCase {

    override def format(jv: JValue): JValue = jv.camelizeKeys
  }

  case object Snake extends JsonLetterCase {

    override def format(jv: JValue): JValue = jv.snakizeKeys
  }

  case object Pascal extends JsonLetterCase {

    override def format(jv: JValue): JValue = rewriteJsonAST(jv)(pascalize)

    // The methods below have been backported from json4s 3.6.x
    // https://github.com/json4s/json4s/blob/3.6/core/src/main/scala/org/json4s/MonadicJValue.scala

    private[this] def pascalize(word: String): String = {
      val lst = word.split("_").toList
      (lst.headOption.map(s => s.substring(0, 1).toUpperCase(ju.Locale.ENGLISH) + s.substring(1)).get ::
        lst.tail.map(s => s.substring(0, 1).toUpperCase + s.substring(1))).mkString("")
    }

    private[this] def rewriteJsonAST(jv: JValue)(keyCaseTransform: String => String): JValue =
      jv.transformField {
        case JField(nm, x) if !nm.startsWith("_") => JField(keyCaseTransform(nm), x)
      }
  }

  object implicits {

    implicit final class JValueCaseOps(jv: JValue) {

      def camelCase: JValue = Camel.format(jv)

      def snakeCase: JValue = Snake.format(jv)

      def pascalCase: JValue = Pascal.format(jv)
    }
  }
}
