/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package javax.jdo;

import javax.jdo.spi.JDOImplHelper;
import javax.jdo.util.AbstractTest;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests that JDOHelper re-applies the secure XML parsing defaults to a DocumentBuilderFactory
 * registered via JDOImplHelper before it is used for jdoconfig.xml parsing.
 */
class JDOHelperDocumentBuilderFactoryTest extends AbstractTest {

  private static final String DISALLOW_DOCTYPE_DECL =
      "http://apache.org/xml/features/disallow-doctype-decl";

  @AfterEach
  void cleanup() {
    JDOImplHelper.getInstance().registerDocumentBuilderFactory(null);
    System.clearProperty(JDOHelper.PROPERTY_ALLOW_UNSAFE_DOCUMENT_BUILDER_FACTORY);
  }

  /** The default factory is hardened. */
  @Test
  void testDefaultFactoryIsHardened() throws ParserConfigurationException {
    DocumentBuilderFactory factory = JDOHelper.getDocumentBuilderFactory();
    Assertions.assertTrue(
        factory.getFeature(DISALLOW_DOCTYPE_DECL),
        "Default DocumentBuilderFactory must disallow DOCTYPE declarations");
    Assertions.assertFalse(
        factory.isExpandEntityReferences(),
        "Default DocumentBuilderFactory must not expand entity references");
  }

  /** A registered, unhardened factory is re-hardened before use. */
  @Test
  void testRegisteredFactoryIsRehardened() throws ParserConfigurationException {
    DocumentBuilderFactory unhardened = DocumentBuilderFactory.newInstance();
    Assertions.assertFalse(
        unhardened.getFeature(DISALLOW_DOCTYPE_DECL),
        "Precondition: a factory from newInstance() allows DOCTYPE declarations");
    JDOImplHelper.getInstance().registerDocumentBuilderFactory(unhardened);

    DocumentBuilderFactory factory = JDOHelper.getDocumentBuilderFactory();
    Assertions.assertSame(unhardened, factory, "The registered factory must be preferred");
    Assertions.assertTrue(
        factory.getFeature(DISALLOW_DOCTYPE_DECL),
        "The registered DocumentBuilderFactory must have DOCTYPE declarations re-disabled");
    Assertions.assertFalse(
        factory.isExpandEntityReferences(),
        "The registered DocumentBuilderFactory must not expand entity references");
  }

  /** The documented opt-out restores the previous behavior. */
  @Test
  void testRegisteredFactoryOptOut() throws ParserConfigurationException {
    System.setProperty(JDOHelper.PROPERTY_ALLOW_UNSAFE_DOCUMENT_BUILDER_FACTORY, "true");
    DocumentBuilderFactory unhardened = DocumentBuilderFactory.newInstance();
    JDOImplHelper.getInstance().registerDocumentBuilderFactory(unhardened);

    DocumentBuilderFactory factory = JDOHelper.getDocumentBuilderFactory();
    Assertions.assertSame(unhardened, factory, "The registered factory must be preferred");
    Assertions.assertFalse(
        factory.getFeature(DISALLOW_DOCTYPE_DECL),
        "With the opt-out property set, the registered factory must not be modified");
  }
}
