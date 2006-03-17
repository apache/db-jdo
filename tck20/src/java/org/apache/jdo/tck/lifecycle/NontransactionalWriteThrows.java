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
 
package org.apache.jdo.tck.lifecycle;

import javax.jdo.JDOUnsupportedOptionException;
import javax.jdo.PersistenceManager;

import org.apache.jdo.tck.JDO_Test;

import org.apache.jdo.tck.pc.mylib.VersionedPCPoint;

import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Test NontransactionalWriteThrows
 *<BR>
 *<B>Keywords:</B> NontransactionalWrite
 *<BR>
 *<B>Assertion ID:</B> A5.6.2-4, A5.6.2-6, A5.6.2-8, A5.6.2-10.
 *<BR>
 *<B>Assertion Description: </B>
A5.6.2-4 [If a datastore transaction is begun, commit will write 
the changes to the datastore with no checking as to 
the current state of the instances in the datastore. 
That is, the changes made outside the transaction 
together with any changes made inside the transaction 
will overwrite the current state of the datastore.] 

A5.6.2-6 [If a datastore transaction is begun, rollback will not write 
any changes to the datastore.] 

A5.6.2-8 [If an optimistic transaction is begun, commit will write 
the changes to the datastore after checking as to the current state 
of the instances in the datastore. The changes made outside 
the transaction together with any changes made inside the transaction 
will update the current state of the datastore if the version 
checking is successful.] 

A5.6.2-10 [If an optimistic transaction is begun, rollback will not write 
any changes to the datastore. The persistent-nontransactional-dirty 
instances will transition according to the RestoreValues flag. ] 
 */

public class NontransactionalWriteThrows extends NontransactionalWriteTest {

    /** */
    protected static final String ASSERTION_FAILED = 
        "Assertion A5.6.2 (NontransactionalWriteThrows) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(NontransactionalWriteThrows.class);
    }

    /**
     * Make sure that if NontransactionalWrite is not supported,
     * an exception is thrown when setting NontransactionalWrite.
     */
    public void testNontransactionalWriteThrows() {
        if (!checkNontransactionalWriteSupported()) {
            try {
                getPM().currentTransaction().setNontransactionalWrite(true);
            } catch (JDOUnsupportedOptionException ex) {
                return; // good catch
            } catch (Exception ex) {
                // threw wrong exception
            appendMessage (ASSERTION_FAILED + "threw wrong exception " + ex);
            }
            appendMessage (ASSERTION_FAILED + "failed to throw " +
                    "JDOUnsupportedOptionException");
        }
        failOnError();
    }

}