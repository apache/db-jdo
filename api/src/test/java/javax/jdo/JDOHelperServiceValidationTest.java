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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;
import java.util.Enumeration;
import javax.jdo.util.AbstractTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests that service-file discovery verifies the candidate class before running any of its code:
 * a class named by a META-INF/services/javax.jdo.JDOEnhancer resource that does not implement
 * JDOEnhancer must be rejected without executing its static initializer or constructor.
 */
class JDOHelperServiceValidationTest extends AbstractTest {

  /** Set by NotAnEnhancer's static and instance initializers; must remain false. */
  static volatile boolean notAnEnhancerCodeRan = false;

  /** A services-file candidate that is not a JDOEnhancer. */
  public static class NotAnEnhancer {
    static {
      notAnEnhancerCodeRan = true;
    }

    public NotAnEnhancer() {
      notAnEnhancerCodeRan = true;
    }
  }

  @Test
  void testGetEnhancerRejectsNonEnhancerWithoutRunningItsCode() throws IOException {
    File services = File.createTempFile("javax.jdo.JDOEnhancer", ".services");
    services.deleteOnExit();
    Files.write(
        services.toPath(), (NotAnEnhancer.class.getName() + "\n").getBytes(StandardCharsets.UTF_8));
    final URL servicesURL = services.toURI().toURL();

    // a loader whose only JDOEnhancer services entry names NotAnEnhancer
    ClassLoader loader =
        new ClassLoader(getClass().getClassLoader()) {
          @Override
          public Enumeration<URL> getResources(String name) throws IOException {
            if ("META-INF/services/javax.jdo.JDOEnhancer".equals(name)) {
              return Collections.enumeration(Collections.singletonList(servicesURL));
            }
            return super.getResources(name);
          }
        };

    Assertions.assertThrows(
        JDOFatalUserException.class,
        () -> JDOHelper.getEnhancer(loader),
        "getEnhancer with only a non-enhancer candidate should fail");
    Assertions.assertFalse(
        notAnEnhancerCodeRan,
        "Code of the non-enhancer candidate must not run before the type check");
  }
}
