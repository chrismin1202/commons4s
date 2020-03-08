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

import scala.reflect.ClassTag
import scala.runtime.ScalaRunTime

object ProductUtils {

  /** The {{{ equals }}} implementation for classes that implements [[Product]].
    *
    * Note that this implementation is similar to ScalaRunTime._equals, which became obsolete in 2.12.
    *
    * @param x an instance of [[P]]
    * @param y an instance to compare
    * @tparam P a type that extends [[Product]]
    * @return {{{ true }}} if {{{ x }}} and {{{ y }}} are both of type [[P]] and are content-wise equal
    *         else {{{ false }}}
    */
  def productEquals[P <: Product: ClassTag](x: P, y: Any): Boolean =
    y match {
      case that: P => x.productArity == that.productArity && x.productIterator.sameElements(that.productIterator)
      case _       => false
    }

  def productHashCode(p: Product): Int = ScalaRunTime._hashCode(p)

  def productToString(p: Product): String = ScalaRunTime._toString(p)
}
