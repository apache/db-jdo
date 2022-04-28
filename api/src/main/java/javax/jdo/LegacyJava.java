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
package javax.jdo;

import javax.jdo.spi.JDOPermission;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.*;

/**
 * This class provides wrapper implementations for the Java SecurityManager and AccessController classes
 * that were deprecated in Java 17/18.
 * <p>
 * For pre-17 this class provides access to the expected SecurityManager and AccessController.
 * For 17+ the getSecurityManager() will always return 'null' and the doPrivileged() methods will simply execute
 * the lambda argument without any security checks.
 * <p>
 * Solution for AccessControl: This class uses reflection to detect whether AccessControl is deprecated.
 * If it is deprecated we provide dummy implementation of 'doPrivileged'. If it is not deprecated we use the
 * original implementation.
 * Implementations are used by finding the desired methods and store them in a static field in LegacyJava.
 * Whenever we need to call a method we can use reflection again to call the stored method in LegacyJava.
 * <p>
 * Please note that the call to the stored method needs to happen from the original calling class. We cannot provide
 * such a generic doPrivileged() in LegacyJava because it would be accessible from other classes and thus
 * allow *any* caller to execute *any* code with doPrivileged() with the security context of LegacyJava. For the same
 * reason the doPrivileged() implementations in the calling classes must be *private*.
 */
public class LegacyJava {

    private static final boolean isSecurityDeprecated = initIsSecurityDeprecated();
    private static final Method getSecurityManager = isSecurityDeprecated ? null :
            findMethod("java.lang.System", "getSecurityManager");
    private static final SecurityManager securityManager = getSecurityManager == null ? null : new SecurityManager();
    public static final Method doPrivilegedAction =
            findMethod("java.security.AccessController", "doPrivileged", LegacyJava.class.getName(), PrivilegedAction.class);
    public static final Method doPrivilegedExceptionAction =
            findMethod("java.security.AccessController", "doPrivileged", LegacyJava.class.getName(), PrivilegedExceptionAction.class);

    /**
     * @return A wrapper around the java SecurityManager or 'null' if no SecurityManager is available or if it
     * is deprecated (Java 17 and later).
     */
    public static SecurityManager getSecurityManager() {
        if (getSecurityManager == null) {
            return null;
        }
        Object sm;
        try {
            sm = getSecurityManager.invoke(null);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            throw new JDOFatalInternalException(e.getMessage());
        }
        if (sm == null) {
            return null;
        }

        securityManager.updateSecurityManager(sm);
        return securityManager;
    }

    /**
     * @return 'true' if the security manager is deprecated or does not exist.
     */
    public static boolean isSecurityManagerDeprecated() {
        return isSecurityDeprecated;
    }

    /**
     * The SecurityManager is only instantiated if it is not deprecated/removed in the Java API.
     * It wraps the java SecurityManager and will forward any calls to it.
     * It is updated in every call to getSecurityManager() to ensure that it uses the latest Java SecurityManager
     * instance.
     */
    public static class SecurityManager {
        Object sm = null;
        Method checkPermissionMethod = null;

        public void checkPermission(JDOPermission permission) {
            try {
                checkPermissionMethod.invoke(null, permission);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                throw new JDOFatalInternalException(e.getMessage());
            }
        }

        public void updateSecurityManager(Object sm) {
            if (this.sm != sm) {
                // We have a new security manager!
                this.sm = sm;
                if (sm != null) {
                    checkPermissionMethod =
                            findMethod("java.lang.SecurityManager", "checkPermission", Permission.class);
                } else {
                    checkPermissionMethod = null;
                }
            }
        }
    }

    /**
     * @return 'true' if the security manager is deprecated or does not exist.
     */
    private static boolean initIsSecurityDeprecated() {
        try {
            Method getSecurityManager = System.class.getMethod("getSecurityManager");
            return getSecurityManager.isAnnotationPresent(Deprecated.class);
        } catch (NoSuchMethodException e) {
            return true;
        }
    }

    private static Method findMethod(String cName, String mName, Class<?>... args) {
        if (isSecurityDeprecated) {
            return null;
        }
        try {
            Class<?> accessController = Class.forName(cName);
            return accessController.getMethod(mName, args);
        } catch (NoSuchMethodException | ClassNotFoundException e) {
            throw new JDOFatalInternalException(e.getMessage());
        }
    }

    private static Method findMethod(String cName, String mName, String cNameAlt, Class<?>... args) {
        if (isSecurityDeprecated) {
            cName = cNameAlt;
        }
        try {
            Class<?> accessController = Class.forName(cName);
            return accessController.getMethod(mName, args);
        } catch (NoSuchMethodException | ClassNotFoundException e) {
            throw new JDOFatalInternalException(e.getMessage());
        }
    }

    /**
     * This is a replacement for the old Java AccessControl.doPrivileged(). This replacement is used when the
     * old doPrivileged() is deprecated or not available.
     * This replacement simply execute the action without any further checks.
     *
     * @param privilegedAction The action to execute.
     * @param <T>              Return type.
     * @return Return value of the action.
     */
    public static <T> T doPrivileged(PrivilegedAction<T> privilegedAction) {
        return privilegedAction.run();
    }

    /**
     * This is a replacement for the old Java AccessControl.doPrivileged(). This replacement is used when the
     * old doPrivileged() is deprecated or not available.
     * This replacement simply execute the action without any further checks.
     *
     * @param privilegedAction The action to execute.
     * @param <T>              Return type.
     * @return Return value of the action.
     * @throws PrivilegedActionException Never thrown.
     */
    public static <T> T doPrivileged(PrivilegedExceptionAction<T> privilegedAction)
            throws PrivilegedActionException {
        try {
            return privilegedAction.run();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new PrivilegedActionException(e);
        }
    }
}
