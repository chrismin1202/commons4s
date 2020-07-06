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
package com.chrism.commons.log

import org.apache.log4j.{Level, Logger}

object LoggingConfigurer {

  /** Sets/overrides the logging level for this given logger names.
    *
    * @param nameLevels the logger name and logging [[Level]] pairs
    */
  def setAllLevels(nameLevels: Seq[(String, Level)]): Unit =
    nameLevels.foreach { case (name, level) => Logger.getLogger(name).setLevel(level) }

  /** Sets/Overrides the logging level for this given logger names.
    *
    * @param nameLevel the logger name logging [[Level]] pair
    * @param more more logger name and logging [[Level]] pairs
    */
  def setLevels(nameLevel: (String, Level), more: (String, Level)*): Unit = setAllLevels(nameLevel +: more)
}
