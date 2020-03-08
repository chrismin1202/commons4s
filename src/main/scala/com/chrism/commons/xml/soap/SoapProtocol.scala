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
package com.chrism.commons.xml.soap

import com.chrism.commons.datatype.{CaseInsensitiveEnumLikeCompanionLike, EnumLike}
import javax.xml.soap.SOAPConstants

sealed abstract class SoapProtocol(override final val name: String, override final val aliases: Set[String])
    extends EnumLike

object SoapProtocol extends CaseInsensitiveEnumLikeCompanionLike[SoapProtocol] {

  @transient
  lazy val defaultProtocol: SoapProtocol = valueOf(SOAPConstants.DEFAULT_SOAP_PROTOCOL)

  override lazy val values: IndexedSeq[SoapProtocol] = IndexedSeq(`1.1`, `1.2`, Dynamic)

  case object `1.1` extends SoapProtocol(SOAPConstants.SOAP_1_1_PROTOCOL, Set("1.1"))

  case object `1.2` extends SoapProtocol(SOAPConstants.SOAP_1_2_PROTOCOL, Set("1.2"))

  case object Dynamic extends SoapProtocol(SOAPConstants.DYNAMIC_SOAP_PROTOCOL, Set("Dynamic"))
}
