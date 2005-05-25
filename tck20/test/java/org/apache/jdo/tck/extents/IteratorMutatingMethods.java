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

import java.util.Iterator;

import javax.jdo.Extent;

import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Iterator Mutating Methods
 *<BR>
 *<B>Keywords:</B> extent exception
 *<BR>
 *<B>Assertion ID:</B> A15.3-4.
 *<BR>
 *<B>Assertion Description: </B>
If any mutating method, including the <code>remove</code> method,
is called on the <code>Iterator</code> returned by <code>Extent.iterator()</code>,
an <code>UnsupportedOperationException</code> is thrown.

 */

public class IteratorMutatingMethods extends ExtentTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A15.3-4 (IteratorMutatingMethods) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(IteratorMutatingMethods.class);
    }

    /** */
    public void test() {
        Extent ex = getExtent();
        Iterator it = ex.iterator();
        it.next();
        try {
            it.remove(); 
            fail(ASSERTION_FAILED,
                 "extent.iterator().remove() should throw UnsupportedOperationException");
        }
        catch (UnsupportedOperationException expected) {
            // expected exception
            if (debug) logger.debug("expected exception " + ex);
        } 
        finally {
        }
    }
}
