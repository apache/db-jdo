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


package org.apache.jdo.tck.api.persistencemanager.nullargs;

import java.util.Collection;

import javax.jdo.JDOUserException;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> detachCopy with Null Arguments
 *<BR>
 *<B>Keywords:</B>
 *<BR>
 *<B>Assertion IDs:</B> A12.6-3, A12.6-4, A12.6-5
 *<BR>
 *<B>Assertion Description: </B>
A12.6-3 [Null arguments to APIs that take an Object parameter cause the API to have no effect.] A12.6-4 [Null arguments to APIs that take Object[] or Collection will cause the API to throw NullPointerException.] A12.6-5 [Non-null Object[] or Collection arguments that contain null elements will have the documented behavior for non-null elements, and the null elements will be ignored.]
 */

public class DetachCopyNullArgs extends PersistenceManagerNullsTest {
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(DetachCopyNullArgs.class);
    }

    static MethodUnderTest detachCopy = new MethodUnderTestDetachCopy();
    static class MethodUnderTestDetachCopy extends MethodUnderTest {
        public Object pmApiReturn(PersistenceManager pm, Object pc) {
            return pm.detachCopy(pc);
        }
        public Collection pmApiReturn(PersistenceManager pm, Collection pcs) {
            return pm.detachCopyAll(pcs);
        }
        public Object[] pmApiReturn(PersistenceManager pm, Object[] pcs) {
            return pm.detachCopyAll(pcs);
        }
    };

    /** 
     * Test that detachCopy() with null valued argument does nothing.
     */
    public void testDetachCopyNullObject() {
        executeNullObjectParameterReturn(detachCopy, "detachCopy(null)");
    }

    /** 
     * Test that detachCopyAll() with null valued Collection argument
     * throws NullPointerException.
     */
    public void testDetachCopyNullCollection() {
        executeNullCollectionParameterReturn(detachCopy, 
                "detachCopyAll((Collection)null)");
    }

    /** 
     * Test that detachCopyAll() with null valued array argument
     * throws NullPointerException.
     */
    public void testDetachCopyNullArray() {
        executeNullArrayParameterReturn(detachCopy, 
                "detachCopyAll((Array)null)");
    }

    /** 
     * Test that detachCopyAll() with a null element of a 
     * Collection argument throws NullPointerException.
     */
    public void testDetachCopyCollectionNullElement() {
        executeCollectionNullElementReturn(collNullElem, detachCopy, 
                "detachCopyAll(Collection)");
    }

    /** 
     * Test that detachCopyAll() with a null element of a 
     * array argument throws NullPointerException.
     */
    public void testDetachCopyArrayNullElement() {
        executeArrayNullElementReturn(arrayNullElem, detachCopy, 
                "detachCopyAll(Array)");
    }

}
