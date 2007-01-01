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

package org.apache.jdo.tck.api.persistencemanager;

import javax.jdo.JDOUserException;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Close Throws Exception
 *<BR>
 *<B>Keywords:</B> exception
 *<BR>
 *<B>Assertion IDs:</B> A12.6-2.
 *<BR>
 *<B>Assertion Description: </B>
In a non-managed environment, if the current transaction is active, close throws JDOUserException.
 */

public class CloseThrowsExceptionWhenActiveTx extends PersistenceManagerTest {
    
    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A12.6-2 (CloseThrowsExceptionWhenActiveTx) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(CloseThrowsExceptionWhenActiveTx.class);
    }

    /** */
    public void test() {
        pm = getPM();
        pm.currentTransaction().begin();

		try {
			pm.close();
			fail(ASSERTION_FAILED, "JDOUserException was not thrown when closing PersistenceManager while transaction was open");
        }
		catch (JDOUserException juex) {
			// expected this exception
        }
		finally {
			pm.currentTransaction().rollback();
		}
    }
}
