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

package org.apache.jdo.test;

import java.util.ArrayList;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.pc.PCPoint;
import org.apache.jdo.pc.PCStroke;
import org.apache.jdo.test.util.JDORITestRunner;

/**
* Test a PC that contains a Collection of PC's.
*
* @author Dave Bristor
*/
public class Test_Stroke extends Test_Fetch {

    /** */
    public static void main(String args[]) {
        JDORITestRunner.run(Test_Stroke.class);
    }

    /** */
    public void test() throws Exception {
        insertObjects();
        readObjects();
        checkExtent(PCPoint.class, 2);
        checkExtent(PCStroke.class, 1);
    }

    /** */
    protected void insertObjects() {
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();

            PCPoint p1 = new PCPoint(1, 3);
            PCPoint p2 = new PCPoint(13, 42);
            ArrayList points = new ArrayList();
            points.add(p1);
            points.add(p2);
            PCStroke pcStroke = new PCStroke(points);

            if (debug) logger.debug("Before insert: " + pcStroke);
            // Next 2 statements allow this test to work whether or not
            // reachability or navigation work.
            pm.makePersistent(p1);
            pm.makePersistent(p2);

            pm.makePersistent(pcStroke);
            tx.commit();
            
            // Next 4 statements allow this test to work whether or not
            // reachability or navigation work.
            Object oid_p1 = JDOHelper.getObjectId(p1);
            Object oid_p2 = JDOHelper.getObjectId(p2);
            oids.add(oid_p1);
            oids.add(oid_p2);
            
            Object oid1 = JDOHelper.getObjectId(pcStroke);
            if (debug) logger.debug("inserted pcStroke: " + oid1);
            oids.add(oid1);
        }
        finally {
            if (tx != null && tx.isActive())
                tx.rollback();
            if (pm != null && !pm.isClosed())
                pm.close();
        }
    }    
    
    /**
     * redefine verify called by readObjects to check whether the read
     * instance is correct.
     */
    protected void verify(int i, Object pc) {
        if (debug) logger.debug("verify (i = " + i + ") " + pc); 
        PCPoint p1 = null;
        PCPoint p2 = null;
        // PCPoint and PCRefArrays do not redefine equals,
        // so use the string representation.
        switch(i) {
        case 0 : 
            p1 = new PCPoint(1, 3);
            assertEquals("Wrong instance returned from datastore",
                         p1.toString(), pc.toString());
            break;
        case 1:
            p2 = new PCPoint(13, 42);
            assertEquals("Wrong instance returned from datastore",
                         p2.toString(), pc.toString());
            break; 
        case 2:
            p1 = new PCPoint(1, 3);
            p2 = new PCPoint(13, 42);
            ArrayList points = new ArrayList();
            points.add(p1);
            points.add(p2);
            PCStroke pcStroke = new PCStroke(points);
            assertEquals("Wrong instance returned from datastore",
                         pcStroke.toString(), pc.toString());
            break;
        default:
            fail("Wrong number of inserted objects, expected three");
            break;
        }
    }
}
