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

import static org.junit.jupiter.api.Assertions.*;

import java.security.Permission;
import java.security.PrivilegedAction;
import java.security.PrivilegedExceptionAction;
import javax.jdo.spi.JDOImplHelper;
import javax.jdo.spi.JDOPermission;
import org.junit.jupiter.api.Test;

public class LegacyJavaTest {

  @Test
  public void testJDOImplHelper() {
    // Try without security manager
    assertNotNull(JDOImplHelper.getInstance());

    if (LegacyJava.isSecurityManagerDeprecated()) {
      return;
    }
    SecurityManager oldSecMgr = System.getSecurityManager();
    try {
      System.setSecurityManager(new MySecurityManager(JDOPermission.GET_METADATA));
    } catch (UnsupportedOperationException e) {
      // Running 24, SecurityManager is present but disabled by default.
      return;
    }

    try {
      // Try with security manager: a denial must surface as the SecurityException
      // documented on JDOImplHelper.getInstance(), not as an internal error.
      assertThrows(SecurityException.class, JDOImplHelper::getInstance);
    } finally {
      System.setSecurityManager(oldSecMgr);
    }
  }

  @Test
  public void testPublicDoPrivilegedFallbacksAreGuarded() throws Exception {
    if (LegacyJava.isSecurityManagerDeprecated()) {
      // AccessController has been removed: the fallbacks simply run the action.
      assertEquals("ok", LegacyJava.doPrivileged((PrivilegedAction<String>) () -> "ok"));
      assertEquals("ok", LegacyJava.doPrivileged((PrivilegedExceptionAction<String>) () -> "ok"));
    } else {
      // AccessController still exists: the public fallbacks must refuse to run
      // (class javadoc invariant - callers go through the Method handles instead).
      assertThrows(
          JDOFatalInternalException.class,
          () -> LegacyJava.doPrivileged((PrivilegedAction<String>) () -> "ok"));
      assertThrows(
          JDOFatalInternalException.class,
          () -> LegacyJava.doPrivileged((PrivilegedExceptionAction<String>) () -> "ok"));
    }
  }

  public class MySecurityManager extends SecurityManager {
    private final Permission invalidPermission;

    public MySecurityManager(JDOPermission invalidPermission) {
      this.invalidPermission = invalidPermission;
    }

    @Override
    public void checkPermission(Permission perm) {
      if (perm == invalidPermission)
        throw new SecurityException("Permission not given: " + perm.getName());
    }
  }
}
