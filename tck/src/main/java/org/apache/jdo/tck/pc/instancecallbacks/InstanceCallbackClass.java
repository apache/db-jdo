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
 

package org.apache.jdo.tck.pc.instancecallbacks;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.jdo.Extent;
import javax.jdo.InstanceCallbacks;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.tck.JDO_Test;

public class InstanceCallbackClass implements InstanceCallbacks {
    public static boolean performPreClearTests;
    public static boolean performPreDeleteTests;
    public static boolean performPreStoreTests;
    
    // The following two variables used in CallingJdoPreDelete tests
    public static boolean preDeleteCalled;
    public static int objectState;
       
    // The rest of these variables used in FieldsInPredelete tests, except to set a variable to make object dirty.
    public static final int arraySize = 5;
    public static final String[] capturedName = new String[arraySize];
    public static final Date[] capturedTimeStamp = new Date[arraySize];
    public static final double[] capturedDoubleValue = new double[arraySize];
    public static final short[] capturedChildToDelete = new short[arraySize];
    public static final char[] capturedCharValue = new char[arraySize];
    public static final String[] capturedNextObjName = new String[arraySize];
    public static final int[] numberOfChildren = new int[arraySize];
    public static final int[] sumOfChildrenIntValue = new int[arraySize];
    public static final boolean[] processedIndex = new boolean[arraySize];
    
    public static final boolean[] transactionActive = new boolean[arraySize];
    
    private static int nextKeyValue = 1;
    private int keyValue;  // persistent--used as key field in application identity
    public String           name;
    public Date            timeStamp;
    public InstanceCallbackClass  nextObj;
    public Set<InstanceCallbackClass> children;
    public int             intValue;
    public double          doubleValue;
    public short           childToDelete;
    public char            charValue;
    
    public static void initializeStaticsForTest()
    {
        performPreClearTests = false;
        performPreDeleteTests = false;
        performPreStoreTests = false;
        preDeleteCalled = false;
        for( int i = 0; i < arraySize; ++i){
            processedIndex[i] = false;
            transactionActive[i] = false;
        }      
    }

    public static void removeAllInstances(PersistenceManager pm)
    {
        Extent<InstanceCallbackClass> e = pm.getExtent(InstanceCallbackClass.class, true);
        for (InstanceCallbackClass instanceCallbackClass : e) {
            pm.deletePersistent(instanceCallbackClass);
        }        
    }
    
    public InstanceCallbackClass() { // not used by application
    }
    
    public InstanceCallbackClass(String label,Date createTime,int intValue,double doubleValue,short childToDelete,char charValue,InstanceCallbackClass obj) {
        keyValue = nextKeyValue++;
        name = label;
        timeStamp = createTime;
        nextObj = obj;
        children = new HashSet<>();
        this.intValue = intValue;
        this.doubleValue = doubleValue;
        this.childToDelete = childToDelete;
        this.charValue = charValue;
    }
    
    public void addChild(InstanceCallbackClass child) {
        children.add(child);
    }
    
    public void jdoPreStore() {
        if(!performPreStoreTests) { return; }
        captureValues();
        PersistenceManager pm = JDOHelper.getPersistenceManager(this);
        usePM(pm);
    }
    
    public void jdoPreDelete() {
        if(!performPreDeleteTests) { return; }
        // The following two variables set for CallingJdoPreDelete tests
        preDeleteCalled = true;
        objectState = JDO_Test.currentState(this);
        
        captureValues();
        
        // The rest of this routine used for Accessing FieldsInPredelete tests
        // check that intValue is a valid index
        if(intValue >= 0 & intValue < arraySize) {
            PersistenceManager pm = JDOHelper.getPersistenceManager(this);
            usePM(pm);
            if(nextObj != null) {
                pm.deletePersistent(nextObj);  // delete referenced object

                // delete designated child
                for (InstanceCallbackClass obj : children) {
                    if (obj.intValue == childToDelete) {
                        pm.deletePersistent(obj);
                        break;
                    }
                }
            }
        }
    }
    
    public void jdoPostLoad() {
    }
    
    public void jdoPreClear() {
        if(!performPreClearTests) { return; }
        // the following code is copied from captureValues, because it must
        // not be enhanced for execution during jdoPreClear.
        // check that intValue is a valid index
        if(intValue >= 0 & intValue < arraySize) {
            processedIndex[intValue] = true;
            
            // capture values of the attributes
            capturedName[intValue] =  name;
            capturedTimeStamp[intValue] = timeStamp;
            capturedDoubleValue[intValue] = doubleValue;
            numberOfChildren[intValue] = children.size();
            sumOfChildrenIntValue[intValue] = 0;
            for (InstanceCallbackClass o : children) {
                sumOfChildrenIntValue[intValue] += o.intValue;
            }
            capturedChildToDelete[intValue] = childToDelete;
            capturedCharValue[intValue] = charValue;
            if(nextObj != null) {
                capturedNextObjName[intValue] = nextObj.name;
            } else {
                capturedNextObjName[intValue] = null;
            }
        }
    }
    
    void captureValues() {
        // check that intValue is a valid index
        if(intValue >= 0 & intValue < arraySize) {
            processedIndex[intValue] = true;
            
            // capture values of the attributes
            capturedName[intValue] =  name;
            capturedTimeStamp[intValue] = timeStamp;
            capturedDoubleValue[intValue] = doubleValue;
            numberOfChildren[intValue] = children.size();
            sumOfChildrenIntValue[intValue] = 0;
            for (InstanceCallbackClass o : children) {
                sumOfChildrenIntValue[intValue] += o.intValue;
            }
            capturedChildToDelete[intValue] = childToDelete;
            capturedCharValue[intValue] = charValue;
            if(nextObj != null) {
                capturedNextObjName[intValue] = nextObj.name;
            } else {
                capturedNextObjName[intValue] = null;
            }
        }
    }
    
    void usePM(PersistenceManager pm) {
        if(intValue >= 0 & intValue < arraySize) {
            Transaction t = pm.currentTransaction();
            if(t.isActive()) {
                transactionActive[intValue] = true;
            }
        }
    }
    
public static class KeyClass implements Serializable, Comparable<KeyClass> {
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
    
    public int compareTo(KeyClass obj) {
        // may throw ClassCastException to be handled by user.
        return keyValue - obj.keyValue;
    }
    
}
}
