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


package org.apache.jdo.tck.extents;

import java.util.Collection;
import java.util.Iterator;

import javax.jdo.Extent;
import javax.jdo.Query;

import org.apache.jdo.tck.util.BatchTestRunner;


/**
 *<B>Title:</B> Close of Extent Iterator is Iterator Specific
 *<BR>
 *<B>Keywords:</B> extent
 *<BR>
 *<B>Assertion ID:</B> A15.3-11.
 *<BR>
 *<B>Assertion Description: </B>
After a call to <code>Extent.close(Iterator i)</code>,
the <code>Extent</code> itself can still be used to acquire other
iterators and can be used as the <code>Extent</code> for queries.

 */

public class CloseOfExtentIteratorIsIteratorSpecific extends ExtentTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A15.3-11 (CloseOfExtentIteratorIsIteratorSpecific) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(CloseOfExtentIteratorIsIteratorSpecific.class);
    }

    /** */
    public void test() {
        Extent ex = getExtent();
        Iterator it1 = ex.iterator();
        ex.close(it1);
        int count = countIterator(ex.iterator());
        if (count != 2) {
            fail(ASSERTION_FAILED,
                 "iterating Employees after close of first iterator; counted " + count + " instances; should be 2");
        }
        Query q = getPM().newQuery(ex);
        Collection c = (Collection)q.execute();
        int count2 = countIterator(c.iterator());
        if (count2 != 2) {
            fail(ASSERTION_FAILED,
                 "in query after closing iterator; counted " + count + " instances; should be 2");
        }
        if (debug) logger.debug("Assertion A15.3-11 passed");
    }
}
