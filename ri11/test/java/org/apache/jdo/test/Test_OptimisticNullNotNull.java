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

/*
 * Test_OptimisticNullNotNull.java
 *
 * Created on May 7, 2003, 1:49 PM
 */

package org.apache.jdo.test;

import java.util.Iterator;

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.pc.PCPoint;
import org.apache.jdo.pc.PCRect;
import org.apache.jdo.pc.RectFactory;
import org.apache.jdo.test.util.AbstractTest;
import org.apache.jdo.test.util.Factory;
import org.apache.jdo.test.util.JDORITestRunner;

/**
 *
 * @author  Craig Russell
 */
public class Test_OptimisticNullNotNull extends AbstractTest {
    
    /** */
    public static void main(String args[]) {
        JDORITestRunner.run(Test_OptimisticNullNotNull.class);
    }

    /**
     * Determines the kind of objects that are inserted.  Override this if
     * you want to insert some other kind of object.
     */
    protected Factory getFactory(int verify) {
        return new RectFactory();
    }

    /**
     * Inserts some number of objects in the database
     */
    public void test() throws Exception {
        insertObjects();
        updateObjects();
        updateObjects();
    }

    /**
     * Updates all the PCRect instances in the database.
     */
    protected void updateObjects() {
        int counter = 0;
        if (debug) logger.debug("\nUPDATE");
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        tx.begin();
        Extent rects = pm.getExtent(PCRect.class, false);
        Iterator it = rects.iterator();
        while (it.hasNext()) {
            PCRect rect = (PCRect)it.next();
            if (rect.getUpperLeft()==null) {
                rect.setUpperLeft(new PCPoint(counter++, counter++));
                rect.setLowerRight(new PCPoint(counter++, counter++));
            } else {
                rect.setUpperLeft(null);
                rect.setLowerRight(null);
            }
        }
        tx.commit();
    }

}
