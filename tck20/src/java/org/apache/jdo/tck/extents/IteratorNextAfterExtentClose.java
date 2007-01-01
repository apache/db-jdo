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


package org.apache.jdo.tck.extents;

import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.jdo.Extent;

import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Iterator Next After Extent Close
 *<BR>
 *<B>Keywords:</B> extent exception
 *<BR>
 *<B>Assertion ID:</B> A15.3-10.
 *<BR>
 *<B>Assertion Description: </B>
After a call to <code>Extent.close(Iterator i)</code>,
the parameter <code>Iterator</code>
will throw a <code>NoSuchElementException</code> to <code>next()</code>. 

 */

public class IteratorNextAfterExtentClose extends ExtentTest {


    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A15.3-10 (IteratorNextAfterExtentClose) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(IteratorNextAfterExtentClose.class);
    }

    /** */
    public void test() {

        try {
            beginTransaction();
            Extent ex = getPM().getExtent (Employee.class, true);
            Iterator it1 = ex.iterator();
            deleteEmployee((Employee)it1.next());
            Iterator it2 = ex.iterator();
            addEmployee();
            Iterator it3 = ex.iterator();
            ex.close(it1);
            ex.close(it2);
            ex.close(it3);

            try {
                tryNext(it1);
                tryNext(it2);
                tryNext(it3);
                rollbackTransaction();
    
                beginTransaction();
                Iterator it4 = ex.iterator();
                int count4 = countIterator(it4);
                commitTransaction();
                
                if (count4 != 2) {
                    fail(ASSERTION_FAILED,
                         "Iterator4 after rollback: " + count4 + "; should be 2");
                }
            } 
            catch (Exception unexpected) {
                fail(ASSERTION_FAILED,
                     "unexpected exception " + unexpected);
            }
        } 
        catch (Exception unexpected) {
            fail(ASSERTION_FAILED,
                 "unexpected exception " + unexpected);
        } 
    }

    /** */
    void tryNext (Iterator it) throws Exception {
        try {
            it.next();
            fail(ASSERTION_FAILED,
                 "expected NoSuchElementException thrown by iterator.next().");
        } catch (NoSuchElementException expected) {
            // expecteed exception
        }
    }
}
