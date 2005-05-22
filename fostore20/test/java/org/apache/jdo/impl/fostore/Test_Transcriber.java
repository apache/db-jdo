/*
 * Copyright 2005 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

// Note the package: This way, we can access things the way we want the rest
// of JDO to do so, and don't have to "publicize" any classes nor methods for
// the sake of testing.
//
package org.apache.jdo.impl.fostore;

import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;

import javax.jdo.JDOUserException;

import org.apache.jdo.test.util.AbstractTest;
import org.apache.jdo.test.util.JDORITestRunner;

/**
* This test is FOStore-specific; it tests only those transcribers.  It does
* not test for the ability to transcribe null values for objects; for that
* see Test_Primitives which tests transcribing *and* storing of nulls.  Also
* see Test_Collections, which tests transcribing and storing of collections.
*/
public class Test_Transcriber extends AbstractTest {
    //
    // Transcribe these
    // For now, don't transcribe object (we don't do objects yet), just
    // transcribe their fields.
    //
    static boolean primitiveBoolean = true;
    static char primitiveChar       = 'z';
    static byte primitiveByte       = 0xf;
    static short primitiveShort     = 12;
    static int primitiveInt         = 42;
    static long primitiveLong       = Long.MAX_VALUE;
    static float primitiveFloat     = 123.45f;
    static double primitiveDouble   = 3.14159;
    static String primitiveObject = "hello, world";

    private FOStorePMF pmf;

    /** */
    public static void main(String args[]) {
        JDORITestRunner.run(Test_Transcriber.class);
    }

    /** Sets up the PMF for the test. */
    protected void setUp() throws Exception 
    {  
        pmf = new FOStorePMF();  
    }

    /** */
    protected void tearDown()
    {
        if (pmf != null) {
            AccessController.doPrivileged(
                new PrivilegedAction () {
                    public Object run () {
                        try {
                            pmf.close();
                        } catch (JDOUserException ex) {
                            System.out.println("pmf.close threw " + ex);
                            System.out.println("forcing close");
                            if( pmf instanceof FOStorePMF )
                                ((FOStorePMF)pmf).close(true);
                        }
                        return null;
                    }});
        }
        pmf = null;
    }

    // The idea is that we're going to write a bunch of stuff to a data
    // output stream, then read it back in; we should get the same data
    // back.
    public void test() throws Exception {
        // Step 1: store
        //
        FOStoreOutput fos = new FOStoreOutput();
        storePrimitives(fos);

        // Step 2: fetch
        //
        byte data[] = fos.getBuf();
        FOStoreInput fis = new FOStoreInput(data, 0, data.length);
        fetchPrimitives(fis);
    }

    void storePrimitives(FOStoreOutput fos) throws Exception {
        FOStoreTranscriberFactory tf = FOStoreTranscriberFactory.getInstance();
        FOStoreTranscriber t = null;
        
        try {
            t = (FOStoreTranscriber)tf.getTranscriber(boolean.class)[0];
            t.storeBoolean(primitiveBoolean, fos);
            assertEquals("Stored primitiveBoolean transcriber", 
                         "org.apache.jdo.impl.fostore.BooleanTranscriber",
                         t.getClass().getName());

            t = (FOStoreTranscriber)tf.getTranscriber(char.class)[0];
            t.storeChar(primitiveChar, fos);
            assertEquals("Stored primitiveChar transcriber",
                         "org.apache.jdo.impl.fostore.CharTranscriber",
                         t.getClass().getName());

            t = (FOStoreTranscriber)tf.getTranscriber(byte.class)[0];
            t.storeByte(primitiveByte, fos);
            assertEquals("Stored primitiveByte transcriber ",
                         "org.apache.jdo.impl.fostore.ByteTranscriber",
                         t.getClass().getName());

            t = (FOStoreTranscriber)tf.getTranscriber(short.class)[0];
            t.storeShort(primitiveShort, fos);
            assertEquals("Stored primitiveShort transcriber ",
                         "org.apache.jdo.impl.fostore.ShortTranscriber",
                         t.getClass().getName());

            t = (FOStoreTranscriber)tf.getTranscriber(int.class)[0];
            t.storeInt(primitiveInt, fos);
            assertEquals("Stored primitiveInt transcriber ",
                         "org.apache.jdo.impl.fostore.IntTranscriber",
                         t.getClass().getName());

            t = (FOStoreTranscriber)tf.getTranscriber(long.class)[0];
            t.storeLong(primitiveLong, fos);
            assertEquals("Stored primitiveLong transcriber ",
                         "org.apache.jdo.impl.fostore.LongTranscriber",
                         t.getClass().getName());

            t = (FOStoreTranscriber)tf.getTranscriber(float.class)[0];
            t.storeFloat(primitiveFloat, fos);
            assertEquals("Stored primitiveFloat transcriber ",
                         "org.apache.jdo.impl.fostore.FloatTranscriber",
                         t.getClass().getName());

            t = (FOStoreTranscriber)tf.getTranscriber(double.class)[0];
            t.storeDouble(primitiveDouble, fos);
            assertEquals("Stored primitiveDouble transcriber ",
                         "org.apache.jdo.impl.fostore.DoubleTranscriber",
                         t.getClass().getName());

            t = (FOStoreTranscriber)tf.getTranscriber(primitiveObject.getClass())[0];
            t.storeObject(primitiveObject, fos);
            assertEquals("Stored primitiveObject transcriber ",
                         "org.apache.jdo.impl.fostore.ObjectTranscriber",
                         t.getClass().getName());
        } catch (IOException ex) {
            throw new Exception("Couldn't store a primitive: " + ex);
        }
    }

    void fetchPrimitives(FOStoreInput fis) throws Exception {
        FOStoreTranscriberFactory tf = FOStoreTranscriberFactory.getInstance();
        FOStoreTranscriber t;
        boolean aBoolean;
        char aChar;
        byte aByte;
        short aShort;
        int anInt;
        long aLong;
        float aFloat;
        double aDouble;
        Object anObject;
        
        try {
            t = (BooleanTranscriber)tf.getTranscriber(boolean.class)[0];
            aBoolean = t.fetchBoolean(fis);
        } catch (IOException ex) {
            throw new Exception("Couldn't fetch primitiveBoolean: " + ex);
        }
        assertEquals("Wrong value retrieving primitiveBoolean", primitiveBoolean, aBoolean);
        assertEquals("Fetched primitiveBoolean transcriber",
                     "org.apache.jdo.impl.fostore.BooleanTranscriber",
                     t.getClass().getName());

        try {
            t = (CharTranscriber)tf.getTranscriber(char.class)[0];
            aChar = t.fetchChar(fis);
        } catch (IOException ex) {
            throw new Exception("Couldn't fetch primitiveChar: " + ex);
        }
        assertEquals("Wrong value retrieving primitiveChar", primitiveChar, aChar);
        assertEquals("Fetched primitiveChar transcriber",
                     "org.apache.jdo.impl.fostore.CharTranscriber",
                     t.getClass().getName());
        try {
            t = (ByteTranscriber)tf.getTranscriber(byte.class)[0];
            aByte = t.fetchByte(fis);
        } catch (IOException ex) {
            throw new Exception("Couldn't fetch primitiveByte: " + ex);
        }
        assertEquals("Wrong value retrieving primitiveByte", primitiveByte, aByte);
        assertEquals("Fetched primitiveByte transcriber",
                     "org.apache.jdo.impl.fostore.ByteTranscriber",
                     t.getClass().getName());

        try {
            t = (ShortTranscriber)tf.getTranscriber(short.class)[0];
            aShort = t.fetchShort(fis);
        } catch (IOException ex) {
            throw new Exception("Couldn't fetch primitiveShort: " + ex);
        }
        assertEquals("Wrong value retrieving primitiveShort", primitiveShort, aShort);
        assertEquals("Fetched primitiveShort transcriber",
                     "org.apache.jdo.impl.fostore.ShortTranscriber",
                     t.getClass().getName());

        try {
            t = (IntTranscriber)tf.getTranscriber(int.class)[0];
            anInt = t.fetchInt(fis);
        } catch (IOException ex) {
            throw new Exception("Couldn't fetch primitiveInt: " + ex);
        }
        assertEquals("Wrong value retrieving primitiveInt", primitiveInt, anInt);
        assertEquals("Fetched primitiveInt transcriber", 
                     "org.apache.jdo.impl.fostore.IntTranscriber",
                     t.getClass().getName());

        try {
            t = (LongTranscriber)tf.getTranscriber(long.class)[0];
            aLong = t.fetchLong(fis);
        } catch (IOException ex) {
            throw new Exception("Couldn't fetch primitiveLong: " + ex);
        }
        assertEquals("Wrong value retrieving primitiveLong", primitiveLong, aLong);
        assertEquals("Fetched primitiveLong transcribers", 
                     "org.apache.jdo.impl.fostore.LongTranscriber",
                     t.getClass().getName());

        try {
            t = (FloatTranscriber)tf.getTranscriber(float.class)[0];
            aFloat = t.fetchFloat(fis);
        } catch (IOException ex) {
            throw new Exception("Couldn't fetch primitiveFloat: " + ex);
        }
        assertEquals("Wrong value retrieving primitiveFloat", primitiveFloat, aFloat, 1e-2f);
        assertEquals("Fetched primitiveFloat transcriber", 
                     "org.apache.jdo.impl.fostore.FloatTranscriber",
                     t.getClass().getName());

        try {
            t = (DoubleTranscriber)tf.getTranscriber(double.class)[0];
            aDouble = t.fetchDouble(fis);
        } catch (IOException ex) {
            throw new Exception("Couldn't fetch primitiveDouble: " + ex);
        }
        assertEquals("Wrong value retrieving primitiveDouble", primitiveDouble, aDouble, 1e-4);
        assertEquals("Fetched primitiveDouble transcriber",
                     "org.apache.jdo.impl.fostore.DoubleTranscriber",
                     t.getClass().getName());

        try {
            t = (ObjectTranscriber)tf.getTranscriber(Object.class)[0];
            anObject = t.fetchObject(fis, null, -1);
        } catch (IOException ex) {
            throw new Exception("Couldn't fetch primitiveObject: " + ex);
        }

        assertEquals("Wrong value retrieving primitiveObject", primitiveObject, anObject);
        assertEquals("Fetched primitiveObject transcriber",
                     "org.apache.jdo.impl.fostore.ObjectTranscriber",
                     t.getClass().getName());
    }
}
