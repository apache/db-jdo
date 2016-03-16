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

package org.apache.jdo.tck.query.jdoql.parameters;

import java.util.ArrayList;
import java.util.Collection;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Department;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.pc.company.FullTimeEmployee;
import org.apache.jdo.tck.pc.company.MedicalInsurance;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Domain Objects as Parameters.
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> 
 *<BR>
 *<B>Assertion Description: </B>
 */
public class DomainObjectsAsParameters extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.x (DomainObjectsAsParameters) failed: ";

    private Object oidDept1Copy;
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(DomainObjectsAsParameters.class);
    }

    /**
     * This methods runs a JDOQL query with an equal operator comparing a field 
     * with a parameter of a domain object type.
     */
    public void testParameterEqual() {
        PersistenceManager pm  = getPM();
        Transaction tx = pm.currentTransaction();
         try {
            tx.begin();

            String filter = "this.department == d";
            Collection<Employee> expectedResult = new ArrayList<>();
            expectedResult.add((Employee)getPersistentCompanyModelInstance("emp1"));
            expectedResult.add((Employee)getPersistentCompanyModelInstance("emp2"));
            expectedResult.add((Employee)getPersistentCompanyModelInstance("emp3"));
            Query q =  pm.newQuery(Employee.class);
            q.declareParameters("org.apache.jdo.tck.pc.company.Department d");
            q.setFilter(filter);
            Collection results = (Collection)q.execute(getPersistentCompanyModelInstance("dept1"));
            checkQueryResultWithoutOrder(ASSERTION_FAILED, filter, results, expectedResult);
            
            tx.commit();
            tx = null;
        } 
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    /**
     * This methods runs a JDOQL query with an equal operator comparing a field
     * with a parameter of a domain object type. The actual parameter is a copy of the
     * domain object referenced by the employees, thus the expected result is empty.
     */
    public void testParameterEqualCopy() {
        PersistenceManager pm  = getPM();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();

            String filter = "this.department == d";
            Collection<Employee> expectedResult = new ArrayList<>();
            Query q =  pm.newQuery(Employee.class);
            q.declareParameters("org.apache.jdo.tck.pc.company.Department d");
            q.setFilter(filter);
            Collection results = (Collection)q.execute(getPM().getObjectById(oidDept1Copy));
            checkQueryResultWithoutOrder(ASSERTION_FAILED, filter, results, expectedResult);

            tx.commit();
            tx = null;
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }
    
    /**
     * This methods runs a JDOQL query with a not equal operator comparing a field
     * with a parameter of a domain object type.
     */
    public void testParameterNotEqual() {
        PersistenceManager pm  = getPM();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();

            String filter = "this.department != d";
            Collection<Employee> expectedResult = new ArrayList<>();
            expectedResult.add((Employee)getPersistentCompanyModelInstance("emp4"));
            expectedResult.add((Employee)getPersistentCompanyModelInstance("emp5"));
            Query q =  pm.newQuery(Employee.class);
            q.declareParameters("org.apache.jdo.tck.pc.company.Department d");
            q.setFilter(filter);
            Collection results = (Collection)q.execute(getPersistentCompanyModelInstance("dept1"));
            checkQueryResultWithoutOrder(ASSERTION_FAILED, filter, results, expectedResult);
            
            tx.commit();
            tx = null;
        } 
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }
    
    /**
     * This methods runs a JDOQL query with an equal operator comparing a field 
     * with a domain object navigated from a parameter.
     */
    public void testParameterNavigationToDomainObject() {
        PersistenceManager pm  = getPM();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();

            String filter = "this.department == e.department";
            Collection<Employee> expectedResult = new ArrayList<>();
            expectedResult.add((Employee)getPersistentCompanyModelInstance("emp1"));
            expectedResult.add((Employee)getPersistentCompanyModelInstance("emp2"));
            expectedResult.add((Employee)getPersistentCompanyModelInstance("emp3"));
            Query q =  pm.newQuery(Employee.class);
            q.declareParameters("org.apache.jdo.tck.pc.company.Employee e");
            q.setFilter(filter);
            Collection results = (Collection)q.execute(getPersistentCompanyModelInstance("emp1"));
            checkQueryResultWithoutOrder(ASSERTION_FAILED, filter, results, expectedResult);
            
            tx.commit();
            tx = null;
        } 
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    /**
     * This methods runs a JDOQL query with an equal operator comparing a field
     * with a domain object navigated from a parameter.
     */
    public void testParameterNavigationToPrimitiveField() {
        PersistenceManager pm  = getPM();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();

            String filter = "this.salary > e.salary";
            Collection<Employee> expectedResult = new ArrayList<>();
            expectedResult.add((Employee)getPersistentCompanyModelInstance("emp5"));
            Query q =  pm.newQuery(FullTimeEmployee.class);
            q.declareParameters("org.apache.jdo.tck.pc.company.FullTimeEmployee e");
            q.setFilter(filter);
            Collection results = (Collection)q.execute(getPersistentCompanyModelInstance("emp1"));
            checkQueryResultWithoutOrder(ASSERTION_FAILED, filter, results, expectedResult);

            tx.commit();
            tx = null;
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    /**
     * This methods runs a JDOQL query with an equal operator comparing a field
     * with a domain object navigated from a parameter.
     */
    public void testDirtyParameterNavigationToPrimitiveField() {
        PersistenceManager pm  = getPM();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();

            FullTimeEmployee emp1 = (FullTimeEmployee)getPersistentCompanyModelInstance("emp1");
            emp1.setSalary(5000d);

            String filter = "this.salary > e.salary";
            Collection<Employee> expectedResult = new ArrayList<>();
            expectedResult.add((Employee)getPersistentCompanyModelInstance("emp2"));
            expectedResult.add((Employee)getPersistentCompanyModelInstance("emp5"));
            Query q =  pm.newQuery(FullTimeEmployee.class);
            q.declareParameters("org.apache.jdo.tck.pc.company.FullTimeEmployee e");
            q.setFilter(filter);
            Collection results = (Collection)q.execute(emp1);
            checkQueryResultWithoutOrder(ASSERTION_FAILED, filter, results, expectedResult);

            tx.commit();
            tx = null;
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    /**
     * This methods runs a JDOQL query with a contains clause using a domain object parameter.
     */
    public void testContainsParameter() {
        PersistenceManager pm  = getPM();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();

            String filter = "this.employees.contains(e)";
            Collection<Department> expectedResult = new ArrayList<>();
            expectedResult.add((Department)getPersistentCompanyModelInstance("dept1"));
            Query q =  pm.newQuery(Department.class);
            q.declareParameters("org.apache.jdo.tck.pc.company.Employee e");
            q.setFilter(filter);
            Collection results = (Collection)q.execute(getPersistentCompanyModelInstance("emp1"));
            checkQueryResultWithoutOrder(ASSERTION_FAILED, filter, results, expectedResult);

            tx.commit();
            tx = null;
        } 
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    /**
     * This methods runs a JDOQL query with a contains clause using a navigated domain object parameter.
     */
    public void testContainsParameterNavigation() {
        PersistenceManager pm  = getPM();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();

            String filter = "this.employees.contains(ins.employee)";
            Collection<Department> expectedResult = new ArrayList<>();
            expectedResult.add((Department)getPersistentCompanyModelInstance("dept1"));
            Query q =  pm.newQuery(Department.class);
            q.declareParameters("org.apache.jdo.tck.pc.company.Insurance ins");
            q.setFilter(filter);
            Collection results = (Collection)q.execute(getPersistentCompanyModelInstance("medicalIns1"));
            checkQueryResultWithoutOrder(ASSERTION_FAILED, filter, results, expectedResult);
            
            tx.commit();
            tx = null;
        } 
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    /**
     * This methods runs a JDOQL query with a contains clause using a collection parameter.
     */
    public void testParameterCollection() {
        PersistenceManager pm  = getPM();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();

            String filter = "insurances.contains(this.medicalInsurance)";
            Collection<Employee> expectedResult = new ArrayList<>();
            expectedResult.add((Employee)getPersistentCompanyModelInstance("emp1"));
            expectedResult.add((Employee)getPersistentCompanyModelInstance("emp3"));
            expectedResult.add((Employee)getPersistentCompanyModelInstance("emp4"));
            Query q =  pm.newQuery(Employee.class);
            q.declareParameters("java.util.Collection insurances");
            q.setFilter(filter);
            Collection<MedicalInsurance> parameters = new ArrayList<>();
            parameters.add((MedicalInsurance)getPersistentCompanyModelInstance("medicalIns1"));
            parameters.add((MedicalInsurance)getPersistentCompanyModelInstance("medicalIns3"));
            parameters.add((MedicalInsurance)getPersistentCompanyModelInstance("medicalIns4"));
            Collection results = (Collection)q.execute(parameters);
            checkQueryResultWithoutOrder(ASSERTION_FAILED, filter, results, expectedResult);
            
            tx.commit();
            tx = null;
        } 
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }
     
    /**
     * @see JDO_Test#localSetUp()
     */
    protected void localSetUp() {
        PersistenceManager pm  = getPM();
        addTearDownClass(CompanyModelReader.getTearDownClasses());
        loadAndPersistCompanyModel(pm);

        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            Department dept1 = (Department) getPersistentCompanyModelInstance("dept1");
            Department dept1Copy = new Department (9999, dept1.getName(), dept1.getCompany(), dept1.getEmployeeOfTheMonth());
            pm.makePersistent(dept1Copy);
            oidDept1Copy = pm.getObjectId(dept1Copy);
            tx.commit();
            tx = null;
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }
}
