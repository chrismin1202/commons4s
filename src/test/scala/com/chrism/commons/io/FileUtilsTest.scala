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
package com.chrism.commons.io

import java.{io => jio}

import com.chrism.commons.FunTestSuite

final class FileUtilsTest extends FunTestSuite {

  import FileUtilsTest._

  test("encoding InputStream as Base64") {
    val base64 = FileUtils.base64EncodeInputStream(testResourceAsStream())
    assert(base64 === ExpectedBase64)
  }

  test("reading file as string") {
    FileUtils.withTempDirectory(prefix = Some("FileUtilsTest_file_read")) { dir =>
      val file = FileUtils.copyInputStreamToFile(testResourceAsStream(), FileUtils.newFile(dir, FileName))
      assert(FileUtils.readFileAsString(file) === FileContent)
    }
  }

  test("writing InputStream to a file") {
    FileUtils.withTempDirectory(prefix = Some("FileUtilsTest_InputStream")) { dir =>
      val file = FileUtils.copyInputStreamToFile(testResourceAsStream(), FileUtils.newFile(dir, FileName))
      val base64 = FileUtils.base64EncodeFile(file)
      assert(base64 === ExpectedBase64)
    }
  }

  test("writing Base64 to a file") {
    FileUtils.withTempDirectory(prefix = Some("FileUtilsTest_Base64")) { dir =>
      val base64 = FileUtils.base64EncodeInputStream(testResourceAsStream())
      val file = FileUtils.writeBase64ToFile(base64, FileUtils.newFile(dir, FileName))
      val base64FromFile = FileUtils.base64EncodeFile(file)
      assert(base64FromFile === base64)
    }
  }

  test("deleting a directory") {
    import FileUtils.implicits._

    val tempDir = FileUtils.createTempDirectory(prefix = Some("FileUtilsTest_delete"), deleteOnExit = false)
    assert(tempDir.exists())

    tempDir.forceDelete()
    assert(tempDir.notExists())
  }

  private[this] def testResourceAsStream(): jio.InputStream = getClass.getResourceAsStream(ResourcePath)
}

private[this] object FileUtilsTest {

  private val FileName: String = "hello-world.txt"
  private val FileContent: String = "Hello World"
  private val ResourcePath: String = s"/com/chrism/commons/io/$FileName"
  private val ExpectedBase64: String = "SGVsbG8gV29ybGQ="
}
