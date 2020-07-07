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

import org.apache.log4j

trait Log4jLoggingLike {

  protected final def getOrCreateLog4jLogger(name: String, level: Option[log4j.Level] = None): log4j.Logger = {
    val log = log4j.Logger.getLogger(name)
    level.map(Log4jConfigurer.configureLogger(log, _)).getOrElse(log)
  }

  protected final def getOrCreateLog4jLoggerByClass(
    clazz: Class[_],
    level: Option[log4j.Level] = None
  ): log4j.Logger = {
    val log = log4j.Logger.getLogger(clazz)
    level.map(Log4jConfigurer.configureLogger(log, _)).getOrElse(log)
  }

  /** Sets/overrides the logging level for this given logger names.
    *
    * @param nameLevels the logger name and logging [[log4j.Level]] pairs
    */
  final def setLog4jLevels(nameLevels: Map[String, log4j.Level]): Unit = Log4jConfigurer.setLevels(nameLevels)

  /** Sets/Overrides the logging level for this given logger names.
    *
    * @param nameLevel the logger name logging [[log4j.Level]] pair
    * @param more more logger name and logging [[log4j.Level]] pairs
    */
  final def setLog4jLevels(nameLevel: (String, log4j.Level), more: (String, log4j.Level)*): Unit =
    Log4jConfigurer.setLevels(nameLevel +: more)
}
