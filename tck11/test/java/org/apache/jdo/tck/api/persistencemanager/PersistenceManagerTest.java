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


import java.util.Collection;
import java.util.Vector;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
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

    /** */
    protected void tearDown() {
        Throwable cleanupFailure = null;
        try {
            cleanup();
            cleanupMylib();
            cleanupCompany();
        }
        catch (Throwable ex) {
            cleanupFailure = ex;
            // set testSucceeded to false, otherwise a failure during
            // super.tearDown would swallow this exception
            testSucceeded = false;
        }

        // cleanup pmf
        super.tearDown();

        // fail if there was an exception during cleanup
        if (cleanupFailure != null) {
            fail("Exception during cleanupMylib: " + cleanupFailure);
        }
    }
    
    /** */
    protected void cleanupMylib() {
        PersistenceManager pm = getPM();
        Transaction tx = null;
        try {
            pm = pmf.getPersistenceManager();
            tx = pm.currentTransaction();
            tx.begin();
            // Note, remove PCRect instances first because of FK constraints
            Collection c = getAllObjects(pm, PCRect.class);
            pm.deletePersistentAll(c);
            c = getAllObjects(pm, PCPoint.class);
            pm.deletePersistentAll(c);
            tx.commit();
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
            if ((pm != null) && pm.isClosed())
                pm.close();
        }
  }

     /** */
    protected void cleanupCompany() {
        PersistenceManager pm = getPM();
        Transaction tx = null;
        try {
            pm = pmf.getPersistenceManager();
            tx = pm.currentTransaction();
            tx.begin();
            Collection c = getAllObjects(pm, Department.class);
            pm.deletePersistentAll(c);
            c = getAllObjects(pm, Company.class);
            pm.deletePersistentAll(c);
            tx.commit();
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
            if ((pm != null) && pm.isClosed())
                pm.close();
        }
    }
  
    /** */
    protected Object createPCPointInstance(PersistenceManager pm) {
        PCPoint p1 = new PCPoint(8,8);
        Transaction tx = pm.currentTransaction();
        tx.begin();
        pm.makePersistent(p1);
        Object oid = pm.getObjectId(p1);
        tx.commit();
        return oid;
    }
  
    /** */
    public void deletePCPointInstance (PersistenceManager pm, Object oid) {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        Object p1 = pm.getObjectById(oid, true);
        pm.deletePersistent(p1);
        tx.commit();
    }

    /** */
    protected Collection getAllObjects(PersistenceManager pm, Class pcClass) {
        Collection col = new Vector() ;
        try {
            Query query = pm.newQuery();
            query.setClass(pcClass);
            query.setCandidates(pm.getExtent(pcClass, false));
            Object result = query.execute();
            col = (Collection)result;
        } catch (Exception e) {
            fail("Exception in getAllObjects()" + e);
        }
        return col ;
    }

    /** */
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

    /** */
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
    
    /** */
    public void assertGetX(PCPoint p, int value, String assertion, String label) {
        if (p.getX() !=  value) {
            fail(assertion, label);
        }
    }
}


