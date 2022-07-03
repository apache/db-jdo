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

package org.apache.jdo.tck.query.jdoql.variables;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.pc.company.QEmployee;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

import javax.jdo.JDOQLTypedQuery;
import javax.jdo.query.NumericExpression;
import java.util.HashMap;
import java.util.Map;

/**
 *<B>Title:</B> Unconstrained Variables.
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6.5-1.
 *<BR>
 *<B>Assertion Description: </B>
 * A variable that is not constrained with an explicit contains clause 
 * is constrained by the extent of the persistence capable class 
 * (including subclasses).
 */
public class UnconstrainedVariable extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.5-1 (UnconstrainedVariable) failed: ";
            
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(UnconstrainedVariable.class);
    }
    
    /** */
    public void testPositive() {
        if (isUnconstrainedVariablesSupported()) {
            Object expected = getTransientCompanyModelInstancesAsList("emp2", "emp3", "emp4");

            JDOQLTypedQuery<Employee> query = getPM().newJDOQLTypedQuery(Employee.class);
            QEmployee cand = QEmployee.candidate();
            QEmployee e = QEmployee.variable("e");
            NumericExpression<?> param = query.numericParameter("id");
            query.filter(cand.hiredate.gt(e.hiredate).and(e.hiredate.eq(param)));

            Map<String, Object> paramValues = new HashMap<>();
            paramValues.put("id", Integer.valueOf(1));

            QueryElementHolder holder = new QueryElementHolder(
                    /*UNIQUE*/      null,
                    /*RESULT*/      null,
                    /*INTO*/        null,
                    /*FROM*/        Employee.class,
                    /*EXCLUDE*/     null,
                    /*WHERE*/       "this.hiredate > e.hiredate & e.personid = id",
                    /*VARIABLES*/   "Employee e",
                    /*PARAMETERS*/  "int id",
                    /*IMPORTS*/     null,
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
