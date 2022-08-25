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

package org.apache.jdo.tck.query.jdoql;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Department;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.pc.company.Person;
import org.apache.jdo.tck.pc.company.QDepartment;
import org.apache.jdo.tck.pc.company.QEmployee;
import org.apache.jdo.tck.pc.company.QPerson;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

import javax.jdo.JDOQLTypedQuery;
import javax.jdo.query.Expression;
import java.util.HashMap;
import java.util.Map;

/**
 *<B>Title:</B> Namespace of Type Names Separate From Fields, Variables, Parameters
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.4-1.
 *<BR>
 *<B>Assertion Description: </B>
Type names have their own namespace that is separate 
from the namespace for fields, variables and parameters.

 */

public class SeparateNamespaceForTypeNames extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.4-1 (SeparateNamespaceForTypeNames) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(SeparateNamespaceForTypeNames.class);
    }

    /**
     *
     */
    public void testParameterName() {
        Object expected = getTransientCompanyModelInstancesAsList(new String[]{"emp1", "emp2", "emp3"});

        JDOQLTypedQuery<Employee> query = getPM().newJDOQLTypedQuery(Employee.class);
        QEmployee cand = QEmployee.candidate();
        Expression<Department> empParam = query.parameter("Department", Department.class);
        query.filter(cand.department.eq(empParam));

        Map<String, Object> paramValues = new HashMap<>();
        paramValues.put("Department", getPersistentCompanyModelInstance("dept1"));

        QueryElementHolder holder = new QueryElementHolder(
                /*UNIQUE*/      null,
                /*RESULT*/      null,
                /*INTO*/        null,
                /*FROM*/        Employee.class,
                /*EXCLUDE*/     null,
                /*WHERE*/       "department == Department",
                /*VARIABLES*/   null,
                /*PARAMETERS*/  "Department Department",
                /*IMPORTS*/     "import org.apache.jdo.tck.pc.company.Department",
                /*GROUP BY*/    null,
                /*ORDER BY*/    null,
                /*FROM*/        null,
                /*TO*/          null,
                /*JDOQLTyped*/  query,
                /*paramValues*/ paramValues);

        executeAPIQuery(ASSERTION_FAILED, holder, expected);
        executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
        executeJDOQLTypedQuery(ASSERTION_FAILED, holder, expected);
    }

    /**
     * 
     */
    public void testVaiableName() {
        Object expected = getTransientCompanyModelInstancesAsList(new String[]{"dept1"});

        JDOQLTypedQuery<Department> query = getPM().newJDOQLTypedQuery(Department.class);
        QDepartment cand = QDepartment.candidate();
        QEmployee variable = QEmployee.variable("Employee");
        query.filter(cand.employees.contains(variable).and(variable.firstname.eq("emp1First")));

        QueryElementHolder holder = new QueryElementHolder(
                /*UNIQUE*/      null,
                /*RESULT*/      null,
                /*INTO*/        null,
                /*FROM*/        Department.class,
                /*EXCLUDE*/     null,
                /*WHERE*/       "employees.contains(Employee) && Employee.firstname == \"emp1First\"",
                /*VARIABLES*/   "Employee Employee",
                /*PARAMETERS*/  null,
                /*IMPORTS*/     "import org.apache.jdo.tck.pc.company.Employee",
                /*GROUP BY*/    null,
                /*ORDER BY*/    null,
                /*FROM*/        null,
                /*TO*/          null,
                /*JDOQLTyped*/  query,
                /*paramValues*/ null);

        executeAPIQuery(ASSERTION_FAILED, holder, expected);
        executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
        executeJDOQLTypedQuery(ASSERTION_FAILED, holder, expected);
    }

    /**
     * @see org.apache.jdo.tck.JDO_Test#localSetUp()
     */
    @Override
    protected void localSetUp() {
        addTearDownClass(CompanyModelReader.getTearDownClasses());
        loadAndPersistCompanyModel(getPM());
    }
}
