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

package javax.jdo.spi;

import java.util.Collection;

import javax.jdo.pc.PCPoint;
import javax.jdo.util.AbstractTest;
import javax.jdo.util.BatchTestRunner;

/** 
 * Tests class javax.jdo.spi.JDOImplHelper.
 * <p>
 * Missing: testNewInstance + testNewObjectIdInstance
 * Missing: tests for JDOImplHelper methods: copyKeyFieldsToObjectId and 
 * copyKeyFieldsFromObjectId.
 */
public class JDOImplHelperTest extends AbstractTest {
    
    /** */
    private RegisterClassEvent event;

    /** */
    public static void main(String args[]) {
        BatchTestRunner.run(JDOImplHelperTest.class);
    }

    /** */
    public void testGetFieldNames() {
        JDOImplHelper implHelper = JDOImplHelper.getInstance();
        String[] fieldNames = implHelper.getFieldNames(PCPoint.class);
        if (fieldNames == null) {
            fail("array of field names is null");
        }
        if (fieldNames.length != 2) {
            fail("Unexpected length of fieldNames; expected 2, got " + 
                 fieldNames.length);
        }
        if (!fieldNames[0].equals("x")) {
            fail("Unexpected field; expected x, got " + fieldNames[0]);
        }
        if (!fieldNames[1].equals("y")) {
            fail("Unexpected field; expected y, got " + fieldNames[1]);
        }
    }

    /** */
    public void testGetFieldTypes() {
        JDOImplHelper implHelper = JDOImplHelper.getInstance();
        Class[] fieldTypes = implHelper.getFieldTypes(PCPoint.class);
        if (fieldTypes == null) {
            fail("array of field types is null");
        }
        if (fieldTypes.length != 2) {
            fail("Unexpected length of fieldTypes; expected 2, got " + 
                 fieldTypes.length);
        }
        if (fieldTypes[0] != int.class) {
            fail("Unexpected field type; expected int, got " + 
                 fieldTypes[0]);
        }
        if (fieldTypes[1] != Integer.class) {
            fail("Unexpected field type; expected Integer, got " + 
                 fieldTypes[1]);
        }
    }
    
    /** */
    public void testGetFieldFlags() {
        byte expected = (byte) (PersistenceCapable.CHECK_READ +
            PersistenceCapable.CHECK_WRITE + PersistenceCapable.SERIALIZABLE);
            
        JDOImplHelper implHelper = JDOImplHelper.getInstance();
        byte[] fieldFlags = implHelper.getFieldFlags(PCPoint.class);
        if (fieldFlags == null) {
            fail("array of field flags is null");
        }
        if (fieldFlags.length != 2) {
            fail("Unexpected length of fieldFlags; expected 2, got " + 
                 fieldFlags.length);
        }
        if (fieldFlags[0] != expected) {
            fail("Unexpected field flag; expected " + expected + 
                 ", got " + fieldFlags[0]);
        }
        if (fieldFlags[1] != expected) {
            fail("Unexpected field flag; expected " + expected + 
                 ", got " + fieldFlags[1]);
        }
    }

    /** */
    public void testGetPCSuperclass() {
        JDOImplHelper implHelper = JDOImplHelper.getInstance();
        Class pcSuper = 
            implHelper.getPersistenceCapableSuperclass(PCPoint.class);
        if (pcSuper != null) {
            fail("Wrong pc superclass of PCPoint; expected null, got " + 
                 pcSuper);
        }
    }

    /** */
    public void testNewInstance() {
        // TBD: test JDOImplHelper.newInstance(pcClass, sm) and
        // JDOImplHelper.newInstance(pcClass, sm, oid)  
    }

    /** */
    public void testNewObjectIdInstance() {
        // TBD: test JDOImplHelper.newObjectIdInstance(pcClass)
    }
    
    /** */
    public void testClassRegistration() {
        JDOImplHelper implHelper = JDOImplHelper.getInstance();
        // make sure PCClass is loaded
        PCPoint p = new PCPoint(1, new Integer(1));

        Collection registeredClasses = implHelper.getRegisteredClasses();
        // test whether PCPoint is registered
        if (!registeredClasses.contains(PCPoint.class)) {
            fail("Missing registration of pc class PCPoint");
        }

        // Save registered meta data for restoring
        String[] fieldNames = implHelper.getFieldNames(PCPoint.class);
        Class[] fieldTypes = implHelper.getFieldTypes(PCPoint.class);
        byte[] fieldFlags = implHelper.getFieldFlags(PCPoint.class);
        Class pcSuperclass = implHelper.getPersistenceCapableSuperclass(PCPoint.class);
        
        // test unregisterClass with null parameter
        try {
            implHelper.unregisterClass(null);
            fail("Missing exception when calling unregisterClass(null)");
        }
        catch (NullPointerException ex) {
            // expected exception => OK
        }

        // test unregister PCPoint class
        implHelper.unregisterClass(PCPoint.class);
        registeredClasses = implHelper.getRegisteredClasses();
        if (registeredClasses.contains(PCPoint.class)) {
            fail("PCPoint still registered");
        }

        // register PCPoint again
        JDOImplHelper.registerClass(PCPoint.class, fieldNames, fieldTypes, 
                                    fieldFlags, pcSuperclass, new PCPoint());
    }

    /** */
    public void testClassListenerRegistration() {
        JDOImplHelper implHelper = JDOImplHelper.getInstance();

        // add listener and check event
        event = null;
        RegisterClassListener listener = new SimpleListener();
        implHelper.addRegisterClassListener(listener);
        JDOImplHelper.registerClass(JDOImplHelperTest.class, new String[0], 
                                    new Class[0], new byte[0], null, null);
        if (event == null) {
            fail("Missing event "); 
        }

        // remove listener and check event
        event = null;
        implHelper.removeRegisterClassListener(listener);
        JDOImplHelper.registerClass(JDOImplHelperTest.class, new String[0], 
                                    new Class[0], new byte[0], null, null);
        if (event != null) {
            fail("Unexpected event " + event);
        }
    }

    /** */
    class SimpleListener implements RegisterClassListener {

        /** */
        public void registerClass(RegisterClassEvent event) {
            JDOImplHelperTest.this.event = event;
        }
        
    }
    
}

