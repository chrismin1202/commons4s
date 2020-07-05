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

import java.time.{Instant, LocalDate, LocalDateTime}

object OrderingUtils {

  object LocalDateOrdering extends Ordering[LocalDate] {

    override def compare(x: LocalDate, y: LocalDate): Int = x.compareTo(y)
  }

  object LocalDateTimeOrdering extends Ordering[LocalDateTime] {

    override def compare(x: LocalDateTime, y: LocalDateTime): Int = x.compareTo(y)
  }

  object InstantOrdering extends Ordering[Instant] {

    override def compare(x: Instant, y: Instant): Int = x.compareTo(y)
  }

  object implicits {

    implicit final val localDateOrdering: Ordering[LocalDate] = LocalDateOrdering

    implicit final val localDateTimeOrdering: Ordering[LocalDateTime] = LocalDateTimeOrdering

    implicit final val instantOrdering: Ordering[Instant] = InstantOrdering
  }
}
