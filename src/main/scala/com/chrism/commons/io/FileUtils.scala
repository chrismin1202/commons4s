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

import java.nio.{file => jnf}
import java.util.Base64
import java.{io => jio}

import com.chrism.commons.util.WithResource
import org.apache.commons.io.{IOUtils, FileUtils => JFileUtils}

import scala.util.{Failure, Success, Try}

object FileUtils {

  def base64EncodeByteArray(bytes: Array[Byte]): String = Base64.getEncoder.encodeToString(bytes)

  def base64EncodePath(path: jnf.Path): String = base64EncodeFile(path.toFile)

  def base64EncodeFile(file: jio.File): String =
    Try {
      val bytes = WithResource(new jio.FileInputStream(file)) { fis =>
        WithResource(new jio.BufferedInputStream(fis)) { inputStream =>
          val bytes = new Array[Byte](file.length.toInt)
          inputStream.read(bytes)
          bytes
        }
      }
      base64EncodeByteArray(bytes)
    } match {
      case Success(value) => value
      case Failure(e)     => throw e
    }

  def base64EncodeInputStream(is: jio.InputStream): String = base64EncodeByteArray(inputStreamToByteArray(is))

  def inputStreamToByteArray(is: jio.InputStream): Array[Byte] = IOUtils.toByteArray(is)

  def decodeBase64String(base64: String): Array[Byte] = Base64.getDecoder.decode(base64)

  def readPathAsByteArray(path: jnf.Path): Array[Byte] = jnf.Files.readAllBytes(path)

  def readFileAsByteArray(file: jio.File): Array[Byte] = readPathAsByteArray(file.toPath)

  def readPathAsString(path: jnf.Path): String = new String(readPathAsByteArray(path))

  def readFileAsString(file: jio.File): String = readPathAsString(file.toPath)

  def writeByteArrayToPath(bytes: Array[Byte], dest: jnf.Path): jnf.Path = {
    writeByteArrayToFile(bytes, dest.toFile)
    dest
  }

  def writeByteArrayToFile(bytes: Array[Byte], dest: jio.File): jio.File = {
    JFileUtils.writeByteArrayToFile(dest, bytes)
    dest
  }

  def writeBase64ToPath(base64: String, dest: jnf.Path): jnf.Path =
    writeByteArrayToPath(decodeBase64String(base64), dest)

  def writeBase64ToFile(base64: String, dest: jio.File): jio.File =
    writeByteArrayToFile(decodeBase64String(base64), dest)

  def copyInputStreamToPath(source: jio.InputStream, dest: jnf.Path): jnf.Path = {
    copyInputStreamToFile(source, dest.toFile)
    dest
  }

  def copyInputStreamToFile(source: jio.InputStream, dest: jio.File): jio.File = {
    JFileUtils.copyInputStreamToFile(source, dest)
    dest
  }

  /** Creates a temporary directory.
    *
    * If {{{ baseDir }}} is specified, the directory is created in the specified base directory; otherwise,
    * the default temporary directory of the file system.
    *
    * If {{{ prefix }}} is specified, the directory is created with the given prefix.
    *
    * If {{{ deleteOnExit }}} is {{{ true }}}, an exit hook is created to the directory.
    *
    * @param prefix the prefix string to be used in generating the directory's name (default: [[None]])
    * @param baseDir the path to directory in which to create the directory (default: [[None]])
    * @param deleteOnExit creates an exit hook for deleting the directory if {{{ true }}} (default: {{{ true }}})
    * @return the path to the newly created directory that did not exist before this method was invoked
    * @throws IllegalArgumentException thrown if the prefix cannot be used to generate a candidate directory name
    * @throws java.io.IOException thrown if an I/O error occurs or the temporary-file directory does not exist
    * @throws SecurityException thrown if a security manager is installed and
    *                           {{{ SecurityManager.checkWrite }}} method is invoked to check write access
    *                           when creating the directory
    */
  def createTempDirectory(
    baseDir: Option[jnf.Path] = None,
    prefix: Option[String] = None,
    deleteOnExit: Boolean = true
  ): jnf.Path = {
    val path = baseDir
      .map(jnf.Files.createTempDirectory(_, prefix.orNull))
      .getOrElse(jnf.Files.createTempDirectory(prefix.orNull))
    if (deleteOnExit) {
      pathWithDeleteShutdownHook(path)
    }
    path
  }

  def withTempDirectory[R](baseDir: Option[jnf.Path] = None, prefix: Option[String] = None)(f: jnf.Path => R): R = {
    var tempDir: jnf.Path = null
    try {
      tempDir = createTempDirectory(baseDir = baseDir, prefix = prefix, deleteOnExit = false)
      f(tempDir)
    } finally {
      if (tempDir != null) {
        forceDeletePath(tempDir)
      }
    }
  }

  def newFile(parentDir: jnf.Path, fileName: String, deleteOnExit: Boolean = false): jio.File = {
    val file = new jio.File(parentDir.toFile, fileName)
    if (deleteOnExit) fileWithDeleteShutdownHook(file) else file
  }

  def pathExists(path: jnf.Path): Boolean = jnf.Files.exists(path)

  def pathNotExists(path: jnf.Path): Boolean = jnf.Files.notExists(path)

  def pathWithDeleteShutdownHook(path: jnf.Path): jnf.Path = {
    fileWithDeleteShutdownHook(path.toFile)
    path
  }

  def fileWithDeleteShutdownHook(file: jio.File): jio.File = {
    sys.addShutdownHook {
      forceDeleteFile(file)
    }
    file
  }

  def forceDeletePath(path: jnf.Path): Unit = forceDeleteFile(path.toFile)

  def forceDeleteFile(file: jio.File): Unit = JFileUtils.forceDelete(file)

  object implicits {

    implicit final class PathOps(path: jnf.Path) {

      def exists(): Boolean = pathExists(path)

      def notExists(): Boolean = pathNotExists(path)

      def forceDelete(): Unit = forceDeletePath(path)

      def withDeleteShutdownHook(): jnf.Path = pathWithDeleteShutdownHook(path)
    }

    implicit final class FileOps(file: jio.File) {

      def forceDelete(): Unit = forceDeleteFile(file)

      def withDeleteShutdownHook(): jio.File = fileWithDeleteShutdownHook(file)
    }
  }
}
