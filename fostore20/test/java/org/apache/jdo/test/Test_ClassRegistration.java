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

package org.apache.jdo.test;

import java.util.*;
import java.lang.reflect.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

import org.apache.jdo.test.util.AbstractTest;
import org.apache.jdo.test.util.JDORITestRunner;

/**
 * Test registration and unregistration of pc classes.
 *
 * @author Michael Bouschen
 * @since 1.0.2
 */
public class Test_ClassRegistration extends AbstractTest {
    
    /** */
    public static void main(String args[]) {
        JDORITestRunner.run(Test_ClassRegistration.class);
    }

    /** */
    protected void setUp() { }

    /** */
    protected void tearDown()  { }

    /** */
    public void test() throws Throwable {
        String jdoapi = System.getProperty("jdoapi");
        String pcclasses = System.getProperty("pcclasses");
        final URL jdoapiURL = new URL("file:" + jdoapi);
        final URL pcclassesURL = new URL("file:" + pcclasses);
        ClassLoader cl = (ClassLoader)AccessController.doPrivileged(
            new PrivilegedAction () {
                public Object run () {
                    return new URLClassLoader(new URL[] {pcclassesURL, jdoapiURL}, null);
                }});
        Class implHelperClass = getImplHelperClass(cl);
        Object implHelper = callGetInstance(implHelperClass);

        // -------------------------------------------
        // Test registration
        // -------------------------------------------

        // This loads the pc class
        Class employeeClass = loadPCClass("org.apache.jdo.pc.empdept.PCEmployee", cl);
        
        Collection registeredClasses = 
            callGetRegisteredClasses(implHelperClass, implHelper);
        int nrOfRegisteredClasses = registeredClasses.size();

        // test whether PCEmployee is registered
        assertTrue("Missing registered class " + employeeClass, 
                   registeredClasses.contains(employeeClass));

        // -------------------------------------------
        // Test unregistration by class
        // -------------------------------------------

        // test unregisterClass with null parameter
        try {
            callUnregisterClass(implHelperClass, implHelper, null);
            fail("Missing exception when calling unregisterClass(null)");
        }
        catch (NullPointerException ex) {
            // expected exception => OK
        }
        
        // test unregister by class
        callUnregisterClass(implHelperClass, implHelper, employeeClass);
        registeredClasses = callGetRegisteredClasses(implHelperClass, implHelper);
        int newNrOfRegisteredClasses = registeredClasses.size();
        // test nr of registered classes is decremented
        assertEquals("Wrong number of registered classes", 
                     (nrOfRegisteredClasses - 1), newNrOfRegisteredClasses);

        // test PCEmployee not registered anymore
        assertFalse("Class " + employeeClass + " still registered", 
                    registeredClasses.contains(employeeClass));
        
        // -------------------------------------------
        // Test unregistration by class loader
        // -------------------------------------------
         
        // test unregister by classLoader
        callUnregisterClasses(implHelperClass, implHelper, cl);
        registeredClasses = callGetRegisteredClasses(implHelperClass, implHelper);
        assertTrue("Unregistration failed, set of registered classes is not empty: " + registeredClasses,
                   registeredClasses.isEmpty());

        // Test succeeded
    }
 
    /** */
    private Class loadPCClass(String className, ClassLoader cl)
        throws Exception
    {
        Class pcClass = Class.forName(className, true, cl);
        assertEquals("PC class " + className + " loaded by wrong class loader",
                     cl, getClassLoaderForClass(pcClass));
        return pcClass;
    }

    /** */
    private Class getImplHelperClass(final ClassLoader cl)
        throws Throwable
    {
        Class implHelperClass = null;
        try {
            implHelperClass = (Class)AccessController.doPrivileged(
                new PrivilegedExceptionAction () {
                    public Object run () throws Exception {
                        return Class.forName("javax.jdo.spi.JDOImplHelper", true, cl);
                    }});
        }
        catch (PrivilegedActionException pae) {
            Throwable e = pae.getException();
            if (e instanceof InvocationTargetException)
                e = ((InvocationTargetException)e).getTargetException();
            throw e;
        }
        assertEquals("JDOImplHelper loaded by wrong class loader", 
                     cl, getClassLoaderForClass(implHelperClass));
        return implHelperClass;
    }

    /** */
    private Object callGetInstance(Class implHelperClass)
        throws Throwable
    {
        final Method method = implHelperClass.getMethod("getInstance", null);
        try {
            return AccessController.doPrivileged(
                new PrivilegedExceptionAction () {
                    public Object run () throws Exception {
                        return method.invoke(null, null);
                    }});
        }
        catch (PrivilegedActionException pae) {
            Throwable e = pae.getException();
            if (e instanceof InvocationTargetException)
                e = ((InvocationTargetException)e).getTargetException();
            throw e;
        }
    }
    
    /** */
    private Collection callGetRegisteredClasses(Class implHelperClass, Object implHelper)
        throws Throwable
    {
        Method method = implHelperClass.getMethod("getRegisteredClasses", null);
        try {
            return (Collection)method.invoke(implHelper, null);
        }
        catch (InvocationTargetException ex) {
            throw ((InvocationTargetException)ex).getTargetException();
        }
    }

    /** */
    private void callUnregisterClass(final Class implHelperClass, 
                                     final Object implHelper, 
                                     final Class parameter)
        throws Throwable
    {
        final Method method = implHelperClass.getMethod(
            "unregisterClass", new Class[] {Class.class});
        try {
            AccessController.doPrivileged(
                new PrivilegedExceptionAction () {
                    public Object run () throws Exception {
                        return method.invoke(implHelper, new Object[] {parameter});
                    }});
        }
        catch (PrivilegedActionException pae) {
            Throwable e = pae.getException();
            if (e instanceof InvocationTargetException)
                e = ((InvocationTargetException)e).getTargetException();
            throw e;
        }
    }

    /** */
    private void callUnregisterClasses(final Class implHelperClass, 
                                       final Object implHelper, 
                                       final ClassLoader parameter)
        throws Throwable
    {
        final Method method = implHelperClass.getMethod(
            "unregisterClasses", new Class[] {ClassLoader.class});
        try {
            AccessController.doPrivileged(
                new PrivilegedExceptionAction () {
                    public Object run () throws Exception {
                        return method.invoke(implHelper, new Object[] {parameter});
                    }});
        }
        catch (PrivilegedActionException pae) {
            Throwable e = pae.getException();
            if (e instanceof InvocationTargetException)
                e = ((InvocationTargetException)e).getTargetException();
            throw e;
        }
    }

    /** */
    private ClassLoader getClassLoaderForClass(final Class clazz)
    {
        if (clazz == null) 
            return null;
        return(ClassLoader)AccessController.doPrivileged(
            new PrivilegedAction () {
                public Object run () {
                    return clazz.getClassLoader();
                }});
    }
    
}
