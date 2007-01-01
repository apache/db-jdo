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
 *<B>Title:</B> Iterator Next After Extent CloseAll
 *<BR>
 *<B>Keywords:</B> extent
 *<BR>
 *<B>Assertion ID:</B> A15.3-13.
 *<BR>
 *<B>Assertion Description: </B>
After a call to <code>Extent.closeAll()</code>, all iterators acquired from this
<code>Extent</code> will throw a <code>NoSuchElementException</code>
to <code>next()</code>.

 */

public class IteratorNextAfterExtentCloseAll extends ExtentTest {


    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A15.3-13 (IteratorNextAfterExtentCloseAll) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(IteratorNextAfterExtentCloseAll.class);
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
            ex.closeAll();

            if (!tryNext(it1)) {
                fail(ASSERTION_FAILED,
                     "iterator1.next() does not throw NoSuchElementException.");
            }
            if (!tryNext(it2)) {
                fail(ASSERTION_FAILED,
                     "iterator2.next() does not throw NoSuchElementException.");
            }
            if (!tryNext(it3)) {
                fail(ASSERTION_FAILED,
                     "iterator3.next() does not throw NoSuchElementException.");
            }
        } 
        finally  {
            rollbackTransaction();
            cleanup();
        }
    }
    
    /** */
    boolean tryNext (Iterator it) {
        try {
            it.next();
        } catch (NoSuchElementException expected) {
            return true;
        }
        return false;
    }

}

