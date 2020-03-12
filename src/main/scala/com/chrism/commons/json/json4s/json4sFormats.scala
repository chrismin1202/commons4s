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

import com.chrism.commons.json.json4s.custom.{
  BigIntegerKeySerializer,
  BigIntegerSerializer,
  LocalDateKeySerializer,
  LocalDateSerializer,
  LocalDateTimeKeySerializer,
  LocalDateTimeSerializer,
  YearKeySerializer,
  YearSerializer
}
import org.json4s.{DefaultFormats, Formats}

trait Json4sFormatsLike {

  @transient
  protected implicit final lazy val formats: Formats = loadFormats()

  /** Override and add custom serializers to {{{ super.loadFormats }}}.
    *
    * @return the [[Formats]]
    */
  protected def loadFormats(): Formats =
    DefaultFormats ++
      org.json4s.ext.JavaTypesSerializers.all +
      BigIntegerSerializer +
      BigIntegerKeySerializer +
      LocalDateSerializer +
      LocalDateKeySerializer +
      LocalDateTimeSerializer +
      LocalDateTimeKeySerializer +
      YearSerializer +
      YearKeySerializer
}

object Json4sFormats extends Json4sFormatsLike {

  @transient
  implicit final lazy val defaultFormats: Formats = loadFormats()
}
