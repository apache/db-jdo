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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.jdo.spi.PersistenceCapable;

import javax.jdo.util.AbstractTest;
import javax.jdo.util.BatchTestRunner;

/*
 * ObjectStateTest.java
 * This test class verifies the proper returned ObjectState for
 * each life cycle state. See Table 3 in section 7.4.
 *
 * @since 2.1
 */
public class ObjectStateTest extends AbstractTest {

    static final int PERSISTENT = 1;
    static final int TRANSACTIONAL = 2;
    static final int DIRTY = 4;
    static final int NEW = 8;
    static final int DELETED = 16;
    static final int DETACHED = 32;

    private static final Method jdoIsPersistent = getDeclaredMethod(
            PersistenceCapable.class,
            "jdoIsPersistent",
            null);
    private static final Method jdoIsTransactional = getDeclaredMethod(
            PersistenceCapable.class,
            "jdoIsTransactional",
            null);
    private static final Method jdoIsDirty = getDeclaredMethod(
            PersistenceCapable.class,
            "jdoIsDirty",
            null);
    private static final Method jdoIsNew = getDeclaredMethod(
            PersistenceCapable.class,
            "jdoIsNew",
            null);
    private static final Method jdoIsDeleted = getDeclaredMethod(
            PersistenceCapable.class,
            "jdoIsDeleted",
            null);
    private static final Method jdoIsDetached = getDeclaredMethod(
            PersistenceCapable.class,
            "jdoIsDetached",
            null);

    /** */
    public static void main(String args[]) {
        BatchTestRunner.run(ObjectStateTest.class);
    }

    public void testNull() {
        PersistenceCapable mock = null;
        assertObjectState("null", null, mock);
    }

    public void testTransient() {
        PersistenceCapable mock = newMock(0);
        assertObjectState("transient", ObjectState.TRANSIENT, mock);
    }

    public void testTransientClean() {
        PersistenceCapable mock = newMock(TRANSACTIONAL);
        assertObjectState("transient-clean", ObjectState.TRANSIENT_CLEAN, mock);
    }

    public void testTransientDirty() {
        PersistenceCapable mock = newMock(TRANSACTIONAL+DIRTY);
        assertObjectState("transient-dirty", ObjectState.TRANSIENT_DIRTY, mock);
    }

    public void testPersistentNew() {
        PersistenceCapable mock = newMock(PERSISTENT+TRANSACTIONAL+NEW+DIRTY);
        assertObjectState("persistent-new", ObjectState.PERSISTENT_NEW, mock);
    }

    public void testPersistentNontransactional() {
        PersistenceCapable mock = newMock(PERSISTENT);
        assertObjectState("persistent-nontransactional", ObjectState.HOLLOW_PERSISTENT_NONTRANSACTIONAL, mock);
    }

    public void testPersistentNontransactionalDirty() {
        PersistenceCapable mock = newMock(PERSISTENT+DIRTY);
        assertObjectState("persistent-nontransactional-dirty", ObjectState.PERSISTENT_NONTRANSACTIONAL_DIRTY, mock);
    }

    public void testPersistentClean() {
        PersistenceCapable mock = newMock(PERSISTENT+TRANSACTIONAL);
        assertObjectState("persistent-clean", ObjectState.PERSISTENT_CLEAN, mock);
    }

    public void testPersistentDirty() {
        PersistenceCapable mock = newMock(PERSISTENT+TRANSACTIONAL+DIRTY);
        assertObjectState("persistent-dirty", ObjectState.PERSISTENT_DIRTY, mock);
    }

    public void testPersistentDeleted() {
        PersistenceCapable mock = newMock(PERSISTENT+TRANSACTIONAL+DIRTY+DELETED);
        assertObjectState("persistent-deleted", ObjectState.PERSISTENT_DELETED, mock);
    }

    public void testPersistentNewDeleted() {
        PersistenceCapable mock = newMock(PERSISTENT+TRANSACTIONAL+NEW+DIRTY+DELETED);
        assertObjectState("persistent-new-deleted", ObjectState.PERSISTENT_NEW_DELETED, mock);
    }

    public void testDetachedClean() {
        PersistenceCapable mock = newMock(DETACHED);
        assertObjectState("detached-clean", ObjectState.DETACHED_CLEAN, mock);
    }

    public void testDetachedDirty() {
        PersistenceCapable mock = newMock(DETACHED+DIRTY);
        assertObjectState("detached-dirty", ObjectState.DETACHED_DIRTY, mock);
    }

    private void assertObjectState(String string, 
            ObjectState expected, 
            PersistenceCapable pc) {
        ObjectState actual = JDOHelper.getObjectState(pc);
        // test for == here because enums should be singleton
        if (actual == expected)
            return;
        fail("ObjectState failure for " + string + NL +
                "expected: " + expected +
                ", actual: " + actual);
    }

    /**
     * Construct a new mock instance of PersistenceCapable that responds
     * to methods jdoIsXXX by returning the bit value of its constructor
     * masked with the value of XXX.
     * @param i the sum of bit masks representing the life cycle state
     * @return a mock instance of PersistenceCapable
     */
    private PersistenceCapable newMock(final int i) {
        return (PersistenceCapable)
            Proxy.newProxyInstance(
                PersistenceCapable.class.getClassLoader(),
                new Class[] {PersistenceCapable.class},
                new MockInvocationHandler(i));
    }

    private class MockInvocationHandler implements InvocationHandler {
        /** States is the sum of all life cycle interrogatives.
         */
        private int states;

        /**
         * Constructs an invocation handler with the specified bit fields set
         * according to the sum of values of PERSISTENT, TRANSACTIONAL, DIRTY, 
         * NEW, DELETED, and DETACHED.
         * @param i the bit field values for the life cycle interrogatives
         */
        private MockInvocationHandler(int i) {
            states = i;
        }

        /**
         * 
         * @param object the PersistenceCapable instance
         * @param method the method being invoked
         * @param parameters parameters (should be null)
         * @throws java.lang.Throwable unused
         * @return for jdoIsXXX methods only, returns whether the 
         * bit field selected by the method is set in the
         * mock handler
         */
        public Object invoke(Object object, Method method, Object[] parameters) 
                throws Throwable {
            if (method.equals(jdoIsPersistent)) {
                return (0 != (states & PERSISTENT));
            }
            if (method.equals(jdoIsTransactional)) {
                return (0 != (states & TRANSACTIONAL));
            }
            if (method.equals(jdoIsDirty)) {
                return (0 != (states & DIRTY));
            }
            if (method.equals(jdoIsNew)) {
                return (0 != (states & NEW));
            }
            if (method.equals(jdoIsDeleted)) {
                return (0 != (states & DELETED));
            }
            if (method.equals(jdoIsDetached)) {
                return (0 != (states & DETACHED));
            }
            fail("Unexpected method called: " + method.getName());
            return Boolean.FALSE; // not reached
        }
    }

    private static Method getDeclaredMethod
            (Class clazz, String methodName, Class[] parameters) {
        try {
            Method result = clazz.getDeclaredMethod(methodName, parameters);
            return result;
        } catch (Exception ex) {
            // human-readable class.methodName(parameter[,parameter])
            StringBuffer sb = new StringBuffer(methodName);
            String delimiter = "(";
            for (Class parameter: parameters) {
                sb.append(delimiter);
                sb.append(parameter.getName());
                delimiter = ",";
            }
            sb.append(")");
            throw new RuntimeException
                ("getDeclaredMethod for " + clazz.getName() + "." +
                    sb + " threw..." + ex);
        }
    }
}
