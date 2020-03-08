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

import com.chrism.commons.FunTestSuite
import javax.xml.soap.SOAPConstants

final class SoapProtocolTest extends FunTestSuite {

  test(s"parsing ${SOAPConstants.SOAP_1_1_PROTOCOL}") {
    assert(SoapProtocol.valueOf(SOAPConstants.SOAP_1_1_PROTOCOL) === SoapProtocol.`1.1`)
    assertOption(SoapProtocol.`1.1`, SoapProtocol.valueOfOrNone(SOAPConstants.SOAP_1_1_PROTOCOL))

    assert(SoapProtocol.valueOf("SOAP 1.1 Protocol") === SoapProtocol.`1.1`)
    assertOption(SoapProtocol.`1.1`, SoapProtocol.valueOfOrNone("soap 1.1 protocol"))

    assert(SoapProtocol.valueOf("1.1") === SoapProtocol.`1.1`)
    assertOption(SoapProtocol.`1.1`, SoapProtocol.valueOfOrNone("1.1"))
  }

  test(s"parsing ${SOAPConstants.SOAP_1_2_PROTOCOL}") {
    assert(SoapProtocol.valueOf(SOAPConstants.SOAP_1_2_PROTOCOL) === SoapProtocol.`1.2`)
    assertOption(SoapProtocol.`1.2`, SoapProtocol.valueOfOrNone(SOAPConstants.SOAP_1_2_PROTOCOL))

    assert(SoapProtocol.valueOf("SOAP 1.2 Protocol") === SoapProtocol.`1.2`)
    assertOption(SoapProtocol.`1.2`, SoapProtocol.valueOfOrNone("soap 1.2 protocol"))

    assert(SoapProtocol.valueOf("1.2") === SoapProtocol.`1.2`)
    assertOption(SoapProtocol.`1.2`, SoapProtocol.valueOfOrNone("1.2"))
  }

  test(s"parsing ${SOAPConstants.DYNAMIC_SOAP_PROTOCOL}") {
    assert(SoapProtocol.valueOf(SOAPConstants.DYNAMIC_SOAP_PROTOCOL) === SoapProtocol.Dynamic)
    assertOption(SoapProtocol.Dynamic, SoapProtocol.valueOfOrNone(SOAPConstants.DYNAMIC_SOAP_PROTOCOL))

    assert(SoapProtocol.valueOf("Dynamic Protocol") === SoapProtocol.Dynamic)
    assertOption(SoapProtocol.Dynamic, SoapProtocol.valueOfOrNone("dynamic protocol"))

    assert(SoapProtocol.valueOf("Dynamic") === SoapProtocol.Dynamic)
    assertOption(SoapProtocol.Dynamic, SoapProtocol.valueOfOrNone("dynamic"))
  }
}
