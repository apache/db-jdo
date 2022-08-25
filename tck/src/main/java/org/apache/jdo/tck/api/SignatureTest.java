/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.jdo.tck.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.util.BatchTestRunner;
import org.apache.jdo.tck.util.signature.SignatureVerifier;

/**
 * <B>Title:</B> SignatureTest <br>
 * <B>Keywords:</B> API <br>
 * <B>Assertion IDs:</B> <br>
 * <B>Assertion Description: </B> This test verifies that a set of JDO API classes and interfaces is
 * compliant with a "signature" description, which has been verified against the JDO specification
 * document. Any discrepancies between the feature declarations in the API classes and the signature
 * descriptor are reported, including any public non-standard features found only in the API
 * classes.
 */
public class SignatureTest extends JDO_Test {

  private static String newLine = System.getProperty("line.separator");

  /** */
  private static final String ASSERTION_FAILED = "API Signature Test failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(SignatureTest.class);
  }

  /**
   * @see org.apache.jdo.tck.JDO_Test#localSetUp()
   */
  @Override
  protected void localSetUp() {}

  /** Runs the API Signature Test. */
  public void testSignatures() {
    // create a SignatureVerifier instance
    final StringWriter sw = new StringWriter();
    final PrintWriter pw = new PrintWriter(sw); // auto-flushes on println
    final boolean quiet = false;
    final boolean verbose = debug;
    final SignatureVerifier verifier = new SignatureVerifier(pw, quiet, verbose);

    // run the signature test
    try {
      // fetch name of signature file
      final String signatureFileName = System.getProperty("jdo.tck.signaturefile");
      if (signatureFileName == null) {
        fail(ASSERTION_FAILED, "No system property defined: jdo.tck.signaturefile");
        return;
      }

      // run the test
      final List signatureFileNames = new ArrayList();
      signatureFileNames.add(signatureFileName);
      int status = verifier.test(signatureFileNames);

      // print test's output in case of problems or debugging
      if (status != 0) {
        fail(
            ASSERTION_FAILED,
            ("Found problems or signature descrepancies."
                + newLine
                + "Test Output: "
                + newLine
                + sw.toString()));
        logger.debug(sw.toString());
      }

      // print test's output in case of problems or debugging
      if (debug) {
        logger.debug(sw.toString());
      }
    } catch (IOException ex) {
      fail(
          ASSERTION_FAILED,
          ("Exception caught: " + ex + newLine + "Test Output: " + newLine + sw.toString()));
    } catch (ParseException ex) {
      fail(
          ASSERTION_FAILED,
          ("Exception caught: " + ex + newLine + "Test Output: " + newLine + sw.toString()));
    }
  }
}
