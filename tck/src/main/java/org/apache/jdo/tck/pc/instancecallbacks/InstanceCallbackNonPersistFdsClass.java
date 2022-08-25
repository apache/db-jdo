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
 

package org.apache.jdo.tck.pc.instancecallbacks;

import java.io.*;
import java.util.*;
import javax.jdo.*;

public class InstanceCallbackNonPersistFdsClass implements InstanceCallbacks {
    public int i;      // non-managed
    public char c;     // non-managed
    public double d;           // transactional
    public short s;            // transactional
    public HashSet children;   // non-managed
    public Date loadTime;      // non-managed
    
    private int keyValue;  // persistent--used as key field in application identity
    public float floatValue; // persistent
    public int intValue;  // persistent  --primary key field
    
    private static int nextKeyValue = 1;
    
    public static int savedIntValue;
    public static float savedFloatValue;
    
    public static Date savedLoadTime;
    public static String member1 = "one";
    public static String member2 = "two";
    public static String member3 = "three";
    
    public static boolean preClearCalled = false;
    public static boolean preStoreCalled = false;
    public static boolean preDeleteCalled = false;
    public static boolean postloadCalled = false;
    public static boolean postloadCalledMultipleTimes = false;
    
    // used in CallingJdoPostload test
    public static int beforeGetObjectById = 1;
    public static int afterGetObjectById = 2;
    public static int savedApplicationStep;
    public static int applicationStep;  // values are 0, beforeGetObjectById and afterGetObjectById 
    
    // used in ModificationOfNontransactionalNonpersistentFields test
    public static ArrayList exceptions = new ArrayList();
    public static ArrayList callbackCalled = new ArrayList();
    public static ArrayList attributeOpCausingExceptions = new ArrayList();

    public static void initializeStaticsForTest()
    {
        savedIntValue = 0;
        savedFloatValue = 0.0f;
        savedLoadTime = null;   
        preClearCalled = false;
        preStoreCalled = false;
        preDeleteCalled = false;
        postloadCalled = false;
        postloadCalledMultipleTimes = false;
        savedApplicationStep = 0;
        applicationStep = 0;
    
        exceptions = new ArrayList();
        callbackCalled = new ArrayList();
        attributeOpCausingExceptions = new ArrayList();
    }    

    public static void removeAllInstances(PersistenceManager pm)
    {
        Extent e = pm.getExtent(org.apache.jdo.tck.pc.instancecallbacks.InstanceCallbackNonPersistFdsClass.class, true);
        Iterator i = e.iterator();
        while( i.hasNext() ){
            pm.deletePersistent(i.next());
        }        
    }

    public InstanceCallbackNonPersistFdsClass()
    {
    }
    
    public InstanceCallbackNonPersistFdsClass(float floatValue,int intValue) {
        keyValue = nextKeyValue++;
        this.floatValue = floatValue;
        this.intValue = intValue;
    }
    
    public void setNonPersist(int i, char c, double d, short s) {
        this.i = i;
        this.c = c;
        this.d = d;
        this.s = s;
    }
    
    public void setNonManaged(int i, char c) {
        this.i = i;
        this.c = c;
    }
    
    public int calcIntValue() {
        return i * c;
    }
    
    public float calcFloatValue () {
        return (float)(d * s);
    }
    
    public void incrementIntValue() {
        intValue++;
    }
    
    public void jdoPreStore() {
        preStoreCalled = true;
        intValue = calcIntValue();
        floatValue = calcFloatValue();
        
        try {
            i = -30;
        }catch(Exception e) {
            callbackCalled.add("jdoPreStore ");
            exceptions.add(e);
            attributeOpCausingExceptions.add("i = -30;");
        }
        try {
            c = '\u0000';
        }catch(Exception e) {
            callbackCalled.add("jdoPreStore ");
            exceptions.add(e);
            attributeOpCausingExceptions.add("c = '\u0000';");
        }
        try {
            d = 362.5;
        }catch(Exception e) {
            callbackCalled.add("jdoPreStore ");
            exceptions.add(e);
            attributeOpCausingExceptions.add("d = 362.5;");
        }
        try {
            s = 0;
        }catch(Exception e) {
            callbackCalled.add("jdoPreStore ");
            exceptions.add(e);
            attributeOpCausingExceptions.add("s = 0;");
        }
        try {
            loadTime = null;
        }catch(Exception e) {
            callbackCalled.add("jdoPreStore ");
            exceptions.add(e);
            attributeOpCausingExceptions.add("loadTime = null;");
        }
        try {
            children = null;
        }catch(Exception e) {
            callbackCalled.add("jdoPreStore ");
            exceptions.add(e);
            attributeOpCausingExceptions.add("children = null;");
        }
    }
    
    public void jdoPreDelete() {
        preDeleteCalled = true;
        try {
            i = 0;
        }catch(Exception e) {
            callbackCalled.add("jdoPreDelete ");
            exceptions.add(e);
            attributeOpCausingExceptions.add("i = 0;");
        }
        try {
            c = 'x';
        }catch(Exception e) {
            callbackCalled.add("jdoPreDelete ");
            exceptions.add(e);
            attributeOpCausingExceptions.add("c = 'x';");
        }
        try {
            d = 0.0;
        }catch(Exception e) {
            callbackCalled.add("jdoPreDelete ");
            exceptions.add(e);
            attributeOpCausingExceptions.add("d = 0.0;");
        }
        try {
            s = -5;
        }catch(Exception e) {
            callbackCalled.add("jdoPreDelete ");
            exceptions.add(e);
            attributeOpCausingExceptions.add("s = -5;");
        }
        try {
            loadTime = null;
        }catch(Exception e) {
            callbackCalled.add("jdoPreDelete ");
            exceptions.add(e);
            attributeOpCausingExceptions.add("loadTime = null;");
        }
        try {
            children = null;
        }catch(Exception e) {
            callbackCalled.add("jdoPreDelete ");
            exceptions.add(e);
            attributeOpCausingExceptions.add("children = null;");
        }
    }
    
    public void jdoPostLoad() {
        postloadCalled = true;
        savedApplicationStep = applicationStep;
        i = -10;
        c = '2';
        d = 30.0;
        s = 40;
        savedIntValue = intValue;
        savedFloatValue = floatValue;
        loadTime = new Date();
        savedLoadTime = loadTime;
        children = new HashSet();
        children.add(member1);
        children.add(member2);
        children.add(member3);
    }
    
    public void jdoPreClear() {
        preClearCalled = true;
        try {
            i = 1;
        }catch(Exception e) {
            callbackCalled.add("jdoPreClear ");
            exceptions.add(e);
            attributeOpCausingExceptions.add("i = 1;");
        }
        try {
            c = '2';
        }catch(Exception e) {
            callbackCalled.add("jdoPreClear ");
            exceptions.add(e);
            attributeOpCausingExceptions.add("c = '2';");
        }
        try {
            d = 3.0;
        }catch(Exception e) {
            callbackCalled.add("jdoPreClear ");
            exceptions.add(e);
            attributeOpCausingExceptions.add("d = 3.0;");
        }
        try {
            s = 4;
        }catch(Exception e) {
            callbackCalled.add("jdoPreClear ");
            exceptions.add(e);
            attributeOpCausingExceptions.add("s = 4;");
        }
        try {
            loadTime = null;
        }catch(Exception e) {
            callbackCalled.add("jdoPreClear ");
            exceptions.add(e);
            attributeOpCausingExceptions.add("loadTime = null;");
        }
        try {
            children = null;
        }catch(Exception e) {
            callbackCalled.add("jdoPreClear ");
            exceptions.add(e);
            attributeOpCausingExceptions.add("children = null;");
        }
    }
    
public static class KeyClass implements Serializable {
    public int keyValue;

    public KeyClass() {
    }

    public KeyClass(String s) {
        try{ keyValue = Integer.parseInt(s);}
        catch(NumberFormatException e){
            keyValue = 0;}
    }    
    
    public boolean equals(Object obj) {
        if( obj == null || !this.getClass().equals(obj.getClass()) ) return false;
        else return keyValue == ((KeyClass)obj).keyValue;
    }
    
    public int hashCode() {
        return keyValue;
    }

    public String toString() {
        return Integer.toString(keyValue);
    }
}
}
