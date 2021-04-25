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
package org.apache.jdo.tck.api.converter;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.mylib.PCRect2;
import org.apache.jdo.tck.pc.mylib.Point;
import org.apache.jdo.tck.util.BatchTestRunner;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

/**
 *<B>Title:</B>AttributeConverterTest
 *<BR>
 *<B>Keywords:</B> mapping
 *<BR>
 *<B>Assertion ID:</B> [not identified]
 *<BR>
 *<B>Assertion Description: </B>
 */
public class AttributeConverterTest extends JDO_Test {

    private static int UL_X = 1;
    private static int UL_Y = 10;
    private static int LR_X = 9;
    private static int LR_Y = 2;

    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(AttributeConverterTest.class);
    }

    /**
     * @see org.apache.jdo.tck.JDO_Test#localSetUp()
     */
    @Override
    protected void localSetUp() {
        addTearDownClass(PCRect2.class);
    }

    /**
     * Test method creating a PCRect2 instance and reading it back from the datastore.
     * A PCRect2 instance refers two Point instances, that are stored as strings in the datastore.
     * A Point instance is converted using an AttribueConverter.
     */
    public void testCreatePCRect2Instance() {
        pm = getPM();

        // Create a persistent PCRect2 instance
        Object oid = createPCRect2Instance(pm);

        // cleanup the 2nd-level cache and close the pm
        // to make sure the PCRect instance is not cached and has to be reloaded from the datastore
        pm.getPersistenceManagerFactory().getDataStoreCache().evict(oid);
        if (pm != null) {
            pm.close();
            pm = null;
        }

        pm = getPM();
        Transaction tx = pm.currentTransaction();
        tx.begin();
        // Read the PCRect2 instance from the datastore
        PCRect2 rect = (PCRect2)pm.getObjectById(oid);
        Point ul = rect.getUpperLeft();
        Point lr = rect.getLowerRight();
        tx.commit();

        // Check the coordinates of the associated Point instances
        assertEquals(UL_X, ul.getX());
        assertEquals(UL_Y, ul.getY() == null ? 0 : ul.getY().intValue());
        assertEquals(LR_X, lr.getX());
        assertEquals(LR_Y, lr.getY() == null ? 0 : lr.getY().intValue());
    }

    private Object createPCRect2Instance(PersistenceManager pm) {
        Point ul = new Point(UL_X, UL_Y);
        Point lr = new Point(LR_X, LR_Y);
        PCRect2 rect = new PCRect2(ul, lr);
        Transaction tx = pm.currentTransaction();
        tx.begin();
        pm.makePersistent(rect);
        Object oid = pm.getObjectId(rect);
        tx.commit();
        return oid;
    }

}
