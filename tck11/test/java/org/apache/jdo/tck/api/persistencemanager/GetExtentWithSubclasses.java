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

import java.util.Iterator;
import java.util.Date;

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.tck.pc.company.Person;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.pc.company.PartTimeEmployee;
import org.apache.jdo.tck.pc.company.FullTimeEmployee;

import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> GetExtentWithSubclasses
 *<BR>
 *<B>Keywords:</B> inheritance extent
 *<BR>
 *<B>Assertion ID:</B> A12.5.4-3.
 *<BR>
 *<B>Assertion Description: </B>
The getExtent method returns an Extent that contains all of the instances in the parameter class or interface, and if the subclasses flag is true, all of the instances of the parameter class and its subclasses.
 */

public class GetExtentWithSubclasses extends PersistenceManagerTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A12.5.4-3 (GetExtentWithSubclasses) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(GetExtentWithSubclasses.class);
    }

    /** */
    public void test() {
        pm = getPM();
        createObjects(pm);
        runTest(pm);
    }

    /** */
    private void createObjects(PersistenceManager pm) {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        Date date = new Date();
        Person p1 = new Person(1L, "Craig", "Russell", null, date, null);
        Employee p2 = new FullTimeEmployee(2L, "Michael", "Bouschen", null, date, null, date, 0.0);
        Employee p3 = new PartTimeEmployee(3L, "Michelle", "Caisse", null, date, null, date, 0.0);
        Employee p4 = new FullTimeEmployee(4L, "Victor", "Kirkebo", null, date, null, date, 0.0);
        pm.makePersistent(p1);
        pm.makePersistent(p2);
        pm.makePersistent(p3);
        pm.makePersistent(p4);
        tx.commit();
    }

    /** */
    private void runTest(PersistenceManager pm) {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        Extent e = pm.getExtent(Person.class, true);

        boolean foundCraig = false;
        boolean foundMichael = false;
        boolean foundMichelle = false;
        boolean foundVictor = false;

        for (Iterator i = e.iterator(); i.hasNext();) {
            Person p = (Person) i.next();
            if ((p.getFirstname().equals("Craig")) && (p.getLastname().equals("Russell")))
                foundCraig=true;
            else if ((p.getFirstname().equals("Michael")) && (p.getLastname().equals("Bouschen")))
                foundMichael=true;
            else if ((p.getFirstname().equals("Michelle")) && (p.getLastname().equals("Caisse")))
                foundMichelle=true;
            else if ((p.getFirstname().equals("Victor")) && (p.getLastname().equals("Kirkebo")))
                foundVictor=true;
        }

        if (!foundCraig) {
            fail(ASSERTION_FAILED,
                "Extent of class " + Person.class.getName() + 
                " does not include instance of class " + Person.class.getName() + " with personid 1L");
        }
        if (!foundMichael) {
            fail(ASSERTION_FAILED,
                "Extent of class " + Person.class.getName() + 
                " does not include instance of class " + Employee.class.getName() + " with personid 2L");
        }
        if (!foundMichelle) {
            fail(ASSERTION_FAILED,
                "Extent of class " + Person.class.getName() + 
                " does not include instance of class " + PartTimeEmployee.class.getName() + " with personid 3L");
        }
        if (!foundVictor) {
            fail(ASSERTION_FAILED,
                "Extent of class " + Person.class.getName() + 
                " does not include instance of class " + FullTimeEmployee.class.getName() + " with personid 4L");
        }

        tx.commit();
    }
}
