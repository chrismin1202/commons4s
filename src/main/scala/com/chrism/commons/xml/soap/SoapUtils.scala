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

import java.util.UUID
import java.{io => jio}

import com.chrism.commons.util.WithResource
import javax.xml.soap.{SOAPElement, SOAPEnvelope, SOAPMessage}

import scala.xml.{Elem, XML}

object SoapUtils {

  object implicits {

    implicit final class EnvelopeOps(envelope: SOAPEnvelope) {

      /** Adds a namespace declaration with the specified prefix and URI.
        *
        * @param namespace an instance of [[SoapNamespace]] to add
        * @return the instance of [[SOAPElement]] into which this namespace declaration was inserted
        * @throws javax.xml.soap.SOAPException thrown if there is an error in creating the namespace
        */
      def withNamespace(namespace: SoapNamespace): SOAPEnvelope = {
        envelope.addNamespaceDeclaration(namespace.prefix, namespace.uri)
        envelope
      }
    }

    implicit final class MessageOps(message: SOAPMessage) {

      /** Write [[SOAPMessage]] as [[Elem]].
        *
        * @return the [[SOAPMessage]] as [[Elem]]
        */
      def toXml: Elem =
        WithResource(new jio.ByteArrayOutputStream()) { os =>
          message.writeTo(os)
          WithResource(new jio.ByteArrayInputStream(os.toByteArray))(XML.load)
        }
    }

    implicit final class ElementOps(elem: SOAPElement) {

      def addBooleanNode(b: Boolean): SOAPElement = elem.addTextNode(b.toString)

      def addIntNode(i: Int): SOAPElement = elem.addTextNode(i.toString)

      def addLongNode(l: Long): SOAPElement = elem.addTextNode(l.toString)

      def addFloatNode(f: Float): SOAPElement = elem.addTextNode(f.toString)

      def addDoubleNode(d: Double): SOAPElement = elem.addTextNode(d.toString)

      def addUuidNode(u: UUID): SOAPElement = elem.addTextNode(u.toString)
    }
  }
}
