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

import javax.jdo.Extent;

import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Iterator HasNext returns False after Extent.close
 *<BR>
 *<B>Keywords:</B> extent
 *<BR>
 *<B>Assertion ID:</B> A15.3-9.
 *<BR>
 *<B>Assertion Description: </B>
After a call to <code>Extent.close(Iterator i)</code>,
the parameter <code>Iterator</code> will return <code>false</code>
to <code>hasNext()</code>.

 */

public class IteratorHasNextFalseAfterExtentClose extends ExtentTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A15.3-9 (IteratorHasNextFalseAfterExtentClose) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(IteratorHasNextFalseAfterExtentClose.class);
    }

    /** */
    public void test() {

        beginTransaction();
        Extent<Employee> ex = getPM().getExtent (Employee.class, true);
        Iterator<Employee> it1 = ex.iterator();
        deleteEmployee(it1.next());
        Iterator<Employee> it2 = ex.iterator();
        addEmployee();
        Iterator<Employee> it3 = ex.iterator();
        ex.close(it1);
        ex.close(it2);
        ex.close(it3);
        rollbackTransaction();

        beginTransaction();
        Iterator<Employee> it4 = ex.iterator();
        int count4 = countIterator(it4);
        rollbackTransaction();
        if (debug) logger.debug ("Iterator4 after rollback: " + count4);

        if (it1.hasNext()) {
            fail(ASSERTION_FAILED,
                 "iterator1.hasNext(): " + it1.hasNext());
        }
        if (it2.hasNext()) {
            fail(ASSERTION_FAILED,
                 "iterator2.hasNext(): " + it2.hasNext());
        }
        if (it3.hasNext()) {
            fail(ASSERTION_FAILED,
                 "iterator3.hasNext(): " + it3.hasNext());
        }
        if (count4 != 2) {
            fail(ASSERTION_FAILED,
                 "Iterator " + count4 + " should be 2");
        }
    }
}
