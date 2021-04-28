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
import org.apache.jdo.tck.pc.mylib.PCRectString;
import org.apache.jdo.tck.pc.mylib.Point;
import org.apache.jdo.tck.util.BatchTestRunner;

import javax.jdo.Query;
import javax.jdo.Transaction;
import java.util.List;

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

    private static final int UL_X = 1;
    private static final int UL_Y = 10;
    private static final int LR_X = 10;
    private static final int LR_Y = 1;

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
        addTearDownClass(PCRectString.class);
    }

    /**
     * Test method creating a PCRectString instance and reading it back from the datastore.
     * A PCRectString instance refers two Point instances, that are stored as strings in the datastore.
     * A Point instance is converted using an AttributeConverter.
     */
    public void testCreatePCRectStringInstance() {
        Transaction tx;
        PCRectString rect;
        Object oid;
        int nrOfDbCalls;
        int nrOfAttrCalls;

        // Test: AttributeConverter method convertToDatastore is called when persisting instance
        nrOfDbCalls = PCRectString.PointToStringConverter.getNrOfConvertToDatastoreCalls();
        nrOfAttrCalls = PCRectString.PointToStringConverter.getNrOfConvertToAttributeCalls();
        pm = getPM();
        tx = pm.currentTransaction();
        tx.begin();
        // Create a persistent PCRectString instance and store its oid
        rect = new PCRectString(new Point(UL_X, UL_Y), new Point(LR_X, LR_Y));
        pm.makePersistent(rect);
        oid = pm.getObjectId(rect);
        tx.commit();
        // convertToDatastore should be called twice
        assertEquals(2, PCRectString.PointToStringConverter.getNrOfConvertToDatastoreCalls() - nrOfDbCalls);
        // convertToAttribute should not be called
        assertEquals(0, PCRectString.PointToStringConverter.getNrOfConvertToAttributeCalls() - nrOfAttrCalls);

        // Cleanup the 2nd-level cache and close the pm to make sure PCRect instances are not cached
        pm.getPersistenceManagerFactory().getDataStoreCache().evictAll(false, PCRectString.class);
        pm.close();
        pm = null;

        // Test: AttributeConverter method convertToAttribute is called when loading instance from the datastore
        nrOfDbCalls = PCRectString.PointToStringConverter.getNrOfConvertToDatastoreCalls();
        nrOfAttrCalls = PCRectString.PointToStringConverter.getNrOfConvertToAttributeCalls();
        pm = getPM();
        tx = pm.currentTransaction();
        tx.begin();
        // Read the PCRectString instance from the datastore
        rect = (PCRectString)pm.getObjectById(oid);
        Point ul = rect.getUpperLeft();
        Point lr = rect.getLowerRight();
        tx.commit();
        // convertToDatastore should not be called
        assertEquals(0, PCRectString.PointToStringConverter.getNrOfConvertToDatastoreCalls() - nrOfDbCalls);
        // convertToAttribute should be called twice
        assertEquals(2, PCRectString.PointToStringConverter.getNrOfConvertToAttributeCalls() - nrOfAttrCalls);

        // Check the coordinates of the associated Point instances
        assertEquals(UL_X, ul.getX());
        assertEquals(Integer.valueOf(UL_Y), ul.getY());
        assertEquals(LR_X, lr.getX());
        assertEquals(LR_Y, lr.getY() == null ? 0 : lr.getY().intValue());
    }

    /**
     * Test method running a query with a Point parameter.
     * This parameter value is converted using the AttributeConverter.
     * @throws Exception
     */
    public void testQueryPCRectStringInstance() throws Exception {
        Transaction tx;
        int nrOfDbCalls;
        int nrOfAttrCalls;

        // Test: AttributeConverter method convertToDatastore is called when persisting instances
        nrOfDbCalls = PCRectString.PointToStringConverter.getNrOfConvertToDatastoreCalls();
        nrOfAttrCalls = PCRectString.PointToStringConverter.getNrOfConvertToAttributeCalls();
        pm = getPM();
        tx = pm.currentTransaction();
        tx.begin();
        // create 5 persistent PCRectString instances
        for (int i = 0; i < 5; i++) {
            pm.makePersistent(new PCRectString(new Point(UL_X + i, UL_Y + i), new Point(LR_X + i, LR_Y + i)));
        }
        tx.commit();
        // convertToDatastore should be called twice per instance = 10 times
        assertEquals(10, PCRectString.PointToStringConverter.getNrOfConvertToDatastoreCalls() - nrOfDbCalls);
        // convertToAttribute should not be called
        assertEquals(0, PCRectString.PointToStringConverter.getNrOfConvertToAttributeCalls() - nrOfAttrCalls);

        // Cleanup the 2nd-level cache and close the pm to make sure PCRect instances are not cached
        pm.getPersistenceManagerFactory().getDataStoreCache().evictAll(false, PCRectString.class);
        pm.close();
        pm = null;

        // Test: AttributeConverter method convertToAttribute is called when loading instance from the datastore
        nrOfDbCalls = PCRectString.PointToStringConverter.getNrOfConvertToDatastoreCalls();
        nrOfAttrCalls = PCRectString.PointToStringConverter.getNrOfConvertToAttributeCalls();
        pm = getPM();
        tx = pm.currentTransaction();
        tx.begin();
        try (Query<PCRectString> q = pm.newQuery(PCRectString.class, "this.upperLeft == :point")) {
            q.setParameters(new Point(UL_X+1, UL_Y+1));
            List<PCRectString> res = q.executeList();
            assertEquals(1, res.size());
            PCRectString rect = res.get(0);
            Point ul = rect.getUpperLeft();
            Point lr = rect.getLowerRight();

            // Check the coordinates of the associated Point instances
            assertEquals(UL_X+1, ul.getX());
            assertEquals(UL_Y+1, ul.getY() == null ? 0 : ul.getY().intValue());
            assertEquals(LR_X+1, lr.getX());
            assertEquals(LR_Y+1, lr.getY() == null ? 0 : lr.getY().intValue());
        } finally {
            tx.commit();
        }

        // convertToDatastore should not be called to handle the query parameter
        assertTrue(PCRectString.PointToStringConverter.getNrOfConvertToDatastoreCalls() - nrOfDbCalls >= 1);
        // convertToAttribute should be called at least twice
        assertTrue(PCRectString.PointToStringConverter.getNrOfConvertToAttributeCalls() - nrOfAttrCalls >= 2);
    }

}
