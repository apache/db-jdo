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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.jdo.Transaction;

import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> ObjectId Unique Among Instances
 *<BR>
 *<B>Keywords:</B> identity
 *<BR>
 *<B>Assertion ID:</B> A12.5.6-12.
 *<BR>
 *<B>Assertion Description: </B>
Within a transaction, the <code>ObjectId</code> returned will compare equal
to the <code>ObjectId</code> returned by only one among all JDO instances
associated with the <code>PersistenceManager</code> regardless of the type of
<code>ObjectId</code>.

 */

public class ObjectIdUniqueAmongInstances extends PersistenceManagerTest {
    
    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A12.5.6-12 (ObjectIdUniqueAmongInstances) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(ObjectIdUniqueAmongInstances.class);
    }

    /** */
    public void test() {
        final int count = 1000;
    
        Set<PCPoint> instances = new HashSet<>(count);
        Set<Object> oids = new HashSet<>(count);

        pm = getPM();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
    
            // Construct "count" persistent instances, and save them in a
            // hashset.
            for (int i = 0; i < count; ++i) {
                PCPoint p = new PCPoint (i, count-i);
                pm.makePersistent(p);
                instances.add(p);
            }
    
            // For all new persistent instances, get the object ids and
            // save them in another hashset.  There should be the same
            // number if the ids  are all unique.
            for (Iterator<PCPoint> it = instances.iterator(); it.hasNext();) {
                oids.add(pm.getObjectId(it.next()));
            }
    
            tx.commit();
            tx = null;
    
            if (oids.size() != instances.size()) {
                fail(ASSERTION_FAILED,
                     "Oids has size: " + oids.size() + "; expected: " + instances.size());
            }
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
        pm.close();
        pm = null;
    }
}
