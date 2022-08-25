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


import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.company.Company;
import org.apache.jdo.tck.pc.company.Department;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.pc.mylib.PCPoint2;
import org.apache.jdo.tck.pc.mylib.PCRect;

public abstract class PersistenceManagerTest extends JDO_Test {
    
    /** */
    protected PersistenceManagerTest() { }

    /** 
     * @see org.apache.jdo.tck.JDO_Test#localSetUp()
     */
    @Override
    protected void localSetUp() {
        // The order of addTearDownClass calls is significant
        // as it takes into account database FKs.
        addTearDownClass(PCRect.class);
        addTearDownClass(PCPoint.class);
        addTearDownClass(Department.class);
        addTearDownClass(Company.class);
    }

    /**
     *
     * @param pm the PersistenceManager
     * @return PCPOint instance
     */
    protected Object createPCPointInstance(PersistenceManager pm) {
        PCPoint p1 = new PCPoint(8,8);
        Transaction tx = pm.currentTransaction();
        tx.begin();
        pm.makePersistent(p1);
        Object oid = pm.getObjectId(p1);
        tx.commit();
        return oid;
    }

    /**
     *
     * @param pm the PersistenceManager
     * @param oid the ID of the instance to be deleted
     */
    public void deletePCPointInstance (PersistenceManager pm, Object oid) {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        Object p1 = pm.getObjectById(oid, true);
        pm.deletePersistent(p1);
        tx.commit();
    }

    /**
     *
     * @param obj PCPoint instance
     * @param expectState expected state
     * @param str state description
     * @return true if test successful
     */
    public boolean testState(PCPoint obj, int expectState, String str) {
        int actualState = currentState(obj);
        if (actualState != expectState) {
            if (debug) {
                logger.debug(" Object not in " + str + " state for X = " +
                             obj.getX());
                logger.debug(" current state: " + actualState +
                             " expected state: " + expectState);
            }
            return false;
        }
        return true;
    }

    /**
     *
     * @param obj PCPoint instance
     * @param expectState expected state
     * @param str state description
     * @return true if test successful
     */
    public boolean testState(PCPoint2 obj, int expectState, String str) {
        int actualState = currentState(obj);
        if (actualState != expectState) {
            if (debug) {
                logger.debug(" Object not in " + str + " state for X = " +
                             obj.getX() );
                logger.debug(" current state: " + actualState +
                             " expected state: " + expectState);
            }
            return false;
        }
        return true;
    }

    /**
     *
     * @param p PCPoint instance
     * @param value value
     * @param assertion assertion
     * @param label label
     */
    public void assertGetX(PCPoint p, int value, String assertion, String label) {
        if (p.getX() !=  value) {
            fail(assertion, label);
        }
    }
}


