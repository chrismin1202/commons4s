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

final class SoapUtilsTest extends FunTestSuite with SoapTestHelper {

  import SoapUtils.implicits._

  test("converting SOAPMessage to scala.xml.Elem") {
    val message = newMessage()
    val body = message.getSOAPPart.getEnvelope.getBody
    val requestElem = body.addChildElement("request")
    requestElem.addChildElement("name").addTextNode("Jules Winnfield")
    val orderElem = requestElem.addChildElement("order")
    orderElem.addChildElement("item").addTextNode("Kahuna Burger")
    orderElem.addChildElement("item").addTextNode("Royale with Cheese")

    val xml = message.toXml

    // The XML should look something like this:
    // <SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
    //     <SOAP-ENV:Header/>
    //     <SOAP-ENV:Body>
    //         <request>
    //             <name>Jules Winnfield</name>
    //             <order>
    //                 <item>Kahuna Burger</item>
    //                 <item>Royale with Cheese</item>
    //             </order>
    //         </request>
    //     </SOAP-ENV:Body>
    // </SOAP-ENV:Envelope>

    val request = xml \ "Body" \ "request"
    assert((request \ "name").text === "Jules Winnfield")
    val items = request \ "order" \ "item"
    items.map(_.text) should contain theSameElementsInOrderAs Seq("Kahuna Burger", "Royale with Cheese")
  }
}
