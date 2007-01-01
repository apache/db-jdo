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
 * SemanticErrorTest.java
 *
 * Created on April 6, 2000
 */

package org.apache.jdo.test.query;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Map;
import java.io.PrintStream;

import javax.jdo.*;

import org.apache.jdo.jdoql.JDOQueryException;
import org.apache.jdo.pc.xempdept.Department;
import org.apache.jdo.pc.xempdept.Employee;

/** 
 *
 * @author  Michael Bouschen
 */
public class SemanticErrorTest
  extends NegativeTest
{
    public SemanticErrorTest(PersistenceManagerFactory pmf, PrintStream log)
    {
        super(pmf, log);
    }

    protected boolean isTestMethodName(String methodName)
    {
       return methodName.startsWith("semanticError");
    }
    
    // ==========================================================================
    // Test methods
    // ==========================================================================
    
    /**
     * Testcase: semantic error undefined field
     */
    public void semanticError001()
        throws Exception
    {
        String expectedMsg = "setFilter column(6): Field 'michael' not defined for class 'org.apache.jdo.pc.xempdept.Employee'.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.setFilter("this.michael == 0");
            query.compile();
            checkMissingException(JDOQueryException.class, expectedMsg);
        }
        catch (JDOException ex)
        {
            checkJDOException(ex, JDOQueryException.class, expectedMsg);
        }
        
        tx.rollback();
    }
    /**
     * Testcase: semantic error left hand side of dot does not define object
     */
    public void semanticError002()
        throws Exception
    {
        String expectedMsg = "setFilter column(1): Expression of class type expected.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.setFilter("empid.unknownField == 0");
            query.compile();
            checkMissingException(JDOQueryException.class, expectedMsg);
        }
        catch (JDOException ex)
        {
            checkJDOException(ex, JDOQueryException.class, expectedMsg);
        }
        
        tx.rollback();
    }
    
    /**
     * Testcase: semantic error undefined expression
     */
    public void semanticError003()
        throws Exception
    {
        String expectedMsg = "setFilter column(8): Undefined expression 'michael.field'.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.setFilter("michael.field == 0");
            query.compile();
            checkMissingException(JDOQueryException.class, expectedMsg);
        }
        catch (JDOException ex)
        {
            checkJDOException(ex, JDOQueryException.class, expectedMsg);
        }
        
        tx.rollback();
    }

    /**
     * Testcase: invalid argument for logical operation &
     */
    public void semanticError004()
        throws Exception
    {
        String expectedMsg = "setFilter column(11): Invalid argument(s) for '&'.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.setFilter("\"michael\" & true");
            query.compile();
            checkMissingException(JDOQueryException.class, expectedMsg);
        }
        catch (JDOException ex)
        {
            checkJDOException(ex, JDOQueryException.class, expectedMsg);
        }
        
        tx.rollback();
    }
    
    /**
     * Testcase: invalid argument for logical operation |
     */
    public void semanticError005()
        throws Exception
    {
        String expectedMsg = "setFilter column(11): Invalid argument(s) for '|'.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.setFilter("\"michael\" | true");
            query.compile();
            checkMissingException(JDOQueryException.class, expectedMsg);
        }
        catch (JDOException ex)
        {
            checkJDOException(ex, JDOQueryException.class, expectedMsg);
        }
        
        tx.rollback();
    }
    
    /**
     * Testcase: invalid argument for conditional operation &&
     */
    public void semanticError006()
        throws Exception
    {
        String expectedMsg = "setFilter column(11): Invalid argument(s) for '&&'.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.setFilter("\"michael\" && false");
            query.compile();
            checkMissingException(JDOQueryException.class, expectedMsg);
        }
        catch (JDOException ex)
        {
            checkJDOException(ex, JDOQueryException.class, expectedMsg);
        }
        
        tx.rollback();
    }
    /**
     * Testcase: invalid argument for conditional operation ||
     */
    public void semanticError007()
        throws Exception
    {
        String expectedMsg = "setFilter column(11): Invalid argument(s) for '||'.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.setFilter("\"michael\" || false");
            query.compile();
            checkMissingException(JDOQueryException.class, expectedMsg);
        }
        catch (JDOException ex)
        {
            checkJDOException(ex, JDOQueryException.class, expectedMsg);
        }
        
        tx.rollback();
    }

    /**
     * Testcase: invalid argument for arithmetic operation +
     */
    public void semanticError008()
        throws Exception
    {
        String expectedMsg = "setFilter column(11): Invalid argument(s) for '+'.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.setFilter("\"michael\" + 3");
            query.compile();
            checkMissingException(JDOQueryException.class, expectedMsg);
        }
        catch (JDOException ex)
        {
            checkJDOException(ex, JDOQueryException.class, expectedMsg);
        }
        
        tx.rollback();
    }
    
    /**
     * Testcase: invalid argument for arithmetic operation -
     */
    public void semanticError009()
        throws Exception
    {
        String expectedMsg = "setFilter column(7): Invalid argument(s) for '-'.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.setFilter("false - 6");
            query.compile();
            checkMissingException(JDOQueryException.class, expectedMsg);
        }
        catch (JDOException ex)
        {
            checkJDOException(ex, JDOQueryException.class, expectedMsg);
        }
        
        tx.rollback();
    }
    
    /**
     * Testcase: invalid argument for arithmetic operation *
     */
    public void semanticError010()
        throws Exception
    {
        String expectedMsg = "setFilter column(6): Invalid argument(s) for '*'.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.setFilter("true * 5");
            query.compile();
            checkMissingException(JDOQueryException.class, expectedMsg);
        }
        catch (JDOException ex)
        {
            checkJDOException(ex, JDOQueryException.class, expectedMsg);
        }
        
        tx.rollback();
    }
    
    /**
     * Testcase: invalid argument for arithmetic operation /
     */
    public void semanticError011()
        throws Exception
    {
        String expectedMsg = "setFilter column(5): Invalid argument(s) for '/'.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.setFilter("4.8 / \"michael\"");
            query.compile();
            checkMissingException(JDOQueryException.class, expectedMsg);
        }
        catch (JDOException ex)
        {
            checkJDOException(ex, JDOQueryException.class, expectedMsg);
        }
        
        tx.rollback();
    }
    
    /**
     * Testcase: invalid argument for relational operation ==
     */
    public void semanticError012()
        throws Exception
    {
        String expectedMsg = "setFilter column(7): Invalid argument(s) for '=='.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.setFilter("false == 1");
            query.compile();
            checkMissingException(JDOQueryException.class, expectedMsg);
        }
        catch (JDOException ex)
        {
            checkJDOException(ex, JDOQueryException.class, expectedMsg);
        }
        
        tx.rollback();
    }
    
    /**
     * Testcase: invalid argument for relational operation !=
     */
    public void semanticError013()
        throws Exception
    {
        String expectedMsg = "setFilter column(5): Invalid argument(s) for '!='.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.setFilter("1.0 != \"michael\"");
            query.compile();
            checkMissingException(JDOQueryException.class, expectedMsg);
        }
        catch (JDOException ex)
        {
            checkJDOException(ex, JDOQueryException.class, expectedMsg);
        }
        
        tx.rollback();
    }
    
    /**
     * Testcase: invalid argument for relational operation <
     */
    public void semanticError014()
        throws Exception
    {
        String expectedMsg = "setFilter column(11): Invalid argument(s) for '<'.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.setFilter("\"michael\" < 1");
            query.compile();
            checkMissingException(JDOQueryException.class, expectedMsg);
        }
        catch (JDOException ex)
        {
            checkJDOException(ex, JDOQueryException.class, expectedMsg);
        }
        
        tx.rollback();
    }
    
    /**
     * Testcase: invalid argument for relational operation <=
     */
    public void semanticError015()
        throws Exception
    {
        String expectedMsg = "setFilter column(3): Invalid argument(s) for '<='.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.setFilter("1 <= \"michael\"");
            query.compile();
            checkMissingException(JDOQueryException.class, expectedMsg);
        }
        catch (JDOException ex)
        {
            checkJDOException(ex, JDOQueryException.class, expectedMsg);
        }
        
        tx.rollback();
    }
    
    /**
     * Testcase: invalid argument for relational operation >
     */
    public void semanticError016()
        throws Exception
    {
        String expectedMsg = "setFilter column(11): Invalid argument(s) for '>'.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.setFilter("\"michael\" > 1");
            query.compile();
            checkMissingException(JDOQueryException.class, expectedMsg);
        }
        catch (JDOException ex)
        {
            checkJDOException(ex, JDOQueryException.class, expectedMsg);
        }
        
        tx.rollback();
    }
    
    /**
     * Testcase: invalid argument for relational operation >=
     */
    public void semanticError017()
        throws Exception
    {
        String expectedMsg = "setFilter column(5): Invalid argument(s) for '>='.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.setFilter("2.0 >= \"michael\"");
            query.compile();
            checkMissingException(JDOQueryException.class, expectedMsg);
        }
        catch (JDOException ex)
        {
            checkJDOException(ex, JDOQueryException.class, expectedMsg);
        }
        
        tx.rollback();
    }
    
    /**
     * Testcase: invalid argument for unary !
     */
    public void semanticError018()
        throws Exception
    {
        String expectedMsg = "setFilter column(1): Invalid argument(s) for '!'.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.setFilter("! 1");
            query.compile();
            checkMissingException(JDOQueryException.class, expectedMsg);
        }
        catch (JDOException ex)
        {
            checkJDOException(ex, JDOQueryException.class, expectedMsg);
        }
        
        tx.rollback();
    }
    
    /**
     * Testcase: invalid argument for unary +
     */
    public void semanticError019()
        throws Exception
    {
        String expectedMsg = "setFilter column(1): Invalid argument(s) for '+'.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.setFilter("+ false");
            query.compile();
            checkMissingException(JDOQueryException.class, expectedMsg);
        }
        catch (JDOException ex)
        {
            checkJDOException(ex, JDOQueryException.class, expectedMsg);
        }
        
        tx.rollback();
    }
    
    /**
     * Testcase: invalid argument for unary -
     */
    public void semanticError020()
        throws Exception
    {
        String expectedMsg = "setFilter column(1): Invalid argument(s) for '-'.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.setFilter("- \"michael\"");
            query.compile();
            checkMissingException(JDOQueryException.class, expectedMsg);
        }
        catch (JDOException ex)
        {
            checkJDOException(ex, JDOQueryException.class, expectedMsg);
        }
        
        tx.rollback();
    }
    
    /**
     * Testcase: invalid argument for unary ~
     */
    public void semanticError021()
        throws Exception
    {
        String expectedMsg = "setFilter column(1): Invalid argument(s) for '~'.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.setFilter("~ true");
            query.compile();
            checkMissingException(JDOQueryException.class, expectedMsg);
        }
        catch (JDOException ex)
        {
            checkJDOException(ex, JDOQueryException.class, expectedMsg);
        }
        
        tx.rollback();
    }
    
    /**
     * Testcase: invalid argument for < (type does not define order)
     */
    public void semanticError022()
        throws Exception
    {
        String expectedMsg = "setFilter column(6): Operand type 'boolean' of < is not sortable.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.setFilter("true < false");
            query.compile();
            checkMissingException(JDOQueryException.class, expectedMsg);
        }
        catch (JDOException ex)
        {
            checkJDOException(ex, JDOQueryException.class, expectedMsg);
        }
        
        tx.rollback();
    }

    /**
     * Testcase: multiple declaration of identifier
     */
    /*
    public void semanticError023()
        throws Exception
    {
        String expectedMsg = "declareImports column(1): 'Employee' already declared as type name.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.declareImports("import org.apache.jdo.pc.xempdept.Employee;");
            query.compile();
            checkMissingException(JDOQueryException.class, expectedMsg);
        }
        catch (JDOException ex)
        {
            checkJDOException(ex, JDOQueryException.class, expectedMsg);
        }
        
        tx.rollback();
    }
    */
    /**
     * Testcase: multiple declaration of identifier
     */
    public void semanticError024()
        throws Exception
    {
        String expectedMsg = "declareParameters column(12): Multiple declaration of 'i'.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.declareParameters("int i, int i");
            query.compile();
            checkMissingException(JDOQueryException.class, expectedMsg);
        }
        catch (JDOException ex)
        {
            checkJDOException(ex, JDOQueryException.class, expectedMsg);
        }
        
        tx.rollback();
    }

    /**
     * Testcase: multiple declaration of identifier
     */
    public void semanticError025()
        throws Exception
    {
        String expectedMsg = "declareVariables column(5): Multiple declaration of 'i'.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.declareParameters("int i");
            query.declareVariables("int i");
            query.compile();
            checkMissingException(JDOQueryException.class, expectedMsg);
        }
        catch (JDOException ex)
        {
            checkJDOException(ex, JDOQueryException.class, expectedMsg);
        }
        
        tx.rollback();
    }

    /**
     * Testcase: unknown type in parameter declaration
     */
    public void semanticError026()
        throws Exception
    {
        String expectedMsg = "declareParameters column(1): Unknown type 'Michael'.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.declareParameters("Michael m");
            query.compile();
            checkMissingException(JDOQueryException.class, expectedMsg);
        }
        catch (JDOException ex)
        {
            checkJDOException(ex, JDOQueryException.class, expectedMsg);
        }
        
        tx.rollback();
    }

    /**
     * Testcase: unknown type in variable declaration
     */
    public void semanticError027()
        throws Exception
    {
        String expectedMsg = "declareVariables column(1): Unknown type 'Michael'.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.declareVariables("Michael m");
            query.compile();
            checkMissingException(JDOQueryException.class, expectedMsg);
        }
        catch (JDOException ex)
        {
            checkJDOException(ex, JDOQueryException.class, expectedMsg);
        }
        
        tx.rollback();
    }

    /**
     * Testcase: unknown type in import declaration
     */
    public void semanticError028()
        throws Exception
    {
        String expectedMsg = "declareImports column(1): Unknown type 'Michael'.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.declareImports("import Michael");
            query.compile();
            checkMissingException(JDOQueryException.class, expectedMsg);
        }
        catch (JDOException ex)
        {
            checkJDOException(ex, JDOQueryException.class, expectedMsg);
        }
        
        tx.rollback();
    }

    /**
     * Testcase: identifier used in parameter declaration does not define type
     */
    public void semanticError029()
        throws Exception
    {
        String expectedMsg = "declareParameters column(12): Unknown type 'index'.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.declareParameters("int index, index i");
            query.compile();
            checkMissingException(JDOQueryException.class, expectedMsg);
        }
        catch (JDOException ex)
        {
            checkJDOException(ex, JDOQueryException.class, expectedMsg);
        }
        
        tx.rollback();
    }

    /**
     * Testcase: too many actual parameters
     */
    public void semanticError030()
        throws Exception
    {
        String expectedMsg = "Wrong number of query parameter values.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.setFilter("empid == 1");
            query.compile();
            query.setCandidates(pm.getExtent(Employee.class, false));
            query.execute("InvalidParameterValue");
            checkMissingException(JDOQueryException.class, expectedMsg);
        }
        catch (JDOException ex)
        {
            checkJDOException(ex, JDOQueryException.class, expectedMsg);
        }
        
        tx.rollback();
    }

    /**
     * Testcase: Unbound query parameter
     */
    public void semanticError031()
        throws Exception
    {
        String expectedMsg = "Unbound query parameter 'l'.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.declareParameters("long l");
            query.setFilter("empid == l");
            query.compile();
            query.setCandidates(pm.getExtent(Employee.class, false));
            query.execute();
            checkMissingException(JDOQueryException.class, expectedMsg);
        }
        catch (JDOException ex)
        {
            checkJDOException(ex, JDOQueryException.class, expectedMsg);
        }
        
        tx.rollback();
    }

    /**
     * Testcase: type of actual parameter is not compatible with type of formal parameter
     */
    public void semanticError032()
        throws Exception
    {
        String expectedMsg = "Incompatible type of actual query parameter. Cannot convert 'java.lang.String' to 'long'";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.declareParameters("long l");
            query.setFilter("empid == l");
            query.compile();
            query.setCandidates(pm.getExtent(Employee.class, false));
            query.execute("InvalidParameterValue");
            checkMissingException(JDOQueryException.class, expectedMsg);
        }
        catch (JDOException ex)
        {
            checkJDOException(ex, JDOQueryException.class, expectedMsg);
        }
        
        tx.rollback();
    }

    /**
     * Testcase: map binds undefined parameter
     */
    public void semanticError033()
        throws Exception
    {
        String expectedMsg = "Undefined query parameter 'param'.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.declareParameters("long l");
            query.setFilter("empid == l");
            query.compile();
            query.setCandidates(pm.getExtent(Employee.class, false));
            Map actualParams = new java.util.HashMap();
            actualParams.put("param", "InvalidParameterValue");
            query.executeWithMap(actualParams);
            checkMissingException(JDOQueryException.class, expectedMsg);
        }
        catch (JDOException ex)
        {
            checkJDOException(ex, JDOQueryException.class, expectedMsg);
        }
        
        tx.rollback();
    }

    /**
     * Testcase: invalid field in ordering specification
     */
    public void semanticError034()
        throws Exception
    {
        String expectedMsg = "setOrdering column(1): Undefined identifier 'michael'.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.setFilter("empid > 2");
            query.setOrdering("michael ascending");
            query.compile();
            checkMissingException(JDOQueryException.class, expectedMsg);
        }
        catch (JDOException ex)
        {
            checkJDOException(ex, JDOQueryException.class, expectedMsg);
        }
        
        tx.rollback();
    }

    /**
     * Testcase: invalid field type in ordering specification
     */
    public void semanticError035()
        throws Exception
    {
        String expectedMsg = "setOrdering column(1): Type 'org.apache.jdo.pc.xempdept.Department' of ordering expression is not sortable.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.setFilter("empid > 2");
            query.setOrdering("department ascending");
            query.compile();
            checkMissingException(JDOQueryException.class, expectedMsg);
        }
        catch (JDOException ex)
        {
            checkJDOException(ex, JDOQueryException.class, expectedMsg);
        }
        
        tx.rollback();
    }

    /**
     * Testcase: invalid field type in ordering specification
     */
    public void semanticError036()
        throws Exception
    {
        String expectedMsg = "setOrdering column(1): Type 'java.util.HashSet' of ordering expression is not sortable.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Department.class);
            query.setFilter("deptid > 2");
            query.setOrdering("employees descending");
            query.compile();
            checkMissingException(JDOQueryException.class, expectedMsg);
        }
        catch (JDOException ex)
        {
            checkJDOException(ex, JDOQueryException.class, expectedMsg);
        }
        
        tx.rollback();
    }

    /**
     * Testcase: invalid method call
     */
    public void semanticError037()
        throws Exception
    {
        String expectedMsg = "setFilter column(5): Invalid method call.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.setFilter("this.getEmplId() == 2");
            query.compile();
            checkMissingException(JDOQueryException.class, expectedMsg);
        }
        catch (JDOException ex)
        {
            checkJDOException(ex, JDOQueryException.class, expectedMsg);
        }
        
        tx.rollback();
    }

    /**
     * Testcase: filter expression is not of type boolean
     */
    public void semanticError038()
        throws Exception
    {
        String expectedMsg = "setFilter column(1): Boolean expression expected, filter expression has type 'int'.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.setFilter("2");
            query.compile();
            checkMissingException(JDOQueryException.class, expectedMsg);
        }
        catch (JDOException ex)
        {
            checkJDOException(ex, JDOQueryException.class, expectedMsg);
        }
        
        tx.rollback();
    }

    /**
     * Testcase: static reference to non static field
     */
    public void semanticError039()
        throws Exception
    {
        String expectedMsg = "setFilter column(10): Cannot make a static reference to non-static variable 'empid' of class 'org.apache.jdo.pc.xempdept.Employee'.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.setFilter("Employee.empid == 2");
            query.compile();
            checkMissingException(JDOQueryException.class, expectedMsg);
        }
        catch (JDOException ex)
        {
            checkJDOException(ex, JDOQueryException.class, expectedMsg);
        }
        
        tx.rollback();
    }

    /**
     * Testcase: access of non public field of non persistence capable class
     */
    /*
    public void semanticError040()
        throws Exception
    {
        String expectedMsg = "setFilter column(3): Cannot access non-public field 'elementCount' of non-persistence-capable class 'java.util.Vector'.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.declareImports("import java.util.Vector");
            query.declareVariables("Vector v");
            query.setFilter("v.elementCount == 1");
            query.compile();
            checkMissingException(JDOQueryException.class, expectedMsg);
        }
        catch (JDOException ex)
        {
            checkJDOException(ex, JDOQueryException.class, expectedMsg);
        }
        
        tx.rollback();
    }
    */
    /**
     * Testcase: access of non public static field
     */
    /*
    public void semanticError041()
        throws Exception
    {
        String expectedMsg = "setFilter column(23): Cannot access non-public static field 'cache' of class 'java.text.Collator'.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.declareImports("import java.text.Collator");
            query.setFilter("firstname == Collator.cache");
            query.compile();
            checkMissingException(JDOQueryException.class, expectedMsg);
        }
        catch (JDOException ex)
        {
            checkJDOException(ex, JDOQueryException.class, expectedMsg);
        }
        
        tx.rollback();
    }
    */
    /**
     * Testcase: collection element type does not match variable type
     * checks bug report 4367808
     */
    public void semanticError042()
        throws Exception
    {
        String expectedMsg = "setFilter column(1): Collection element type 'org.apache.jdo.pc.xempdept.Employee' and argument type 'org.apache.jdo.pc.xempdept.Project' not compatible.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.declareImports("import org.apache.jdo.pc.xempdept.Project");
            query.declareVariables("Project p");
            query.setFilter("team.contains(p) && p.projid == 1");
            query.compile();
            checkMissingException(JDOQueryException.class, expectedMsg);
        }
        catch (JDOException ex)
        {
            checkJDOException(ex, JDOQueryException.class, expectedMsg);
        }
        
        tx.rollback();
    }

    /**
     * Testcase: unsupported operation in ordering expression
     */
    /*
    public void semanticError043()
        throws Exception
    {
        String expectedMsg = "setOrdering column(7): Unsupported expression '+' in ordering specification.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.setFilter("empid > 2");
            query.setOrdering("empid + 1 ascending");
            query.compile();
            checkMissingException(JDOQueryException.class, expectedMsg);
        }
        catch (JDOException ex)
        {
            checkJDOException(ex, JDOQueryException.class, expectedMsg);
        }
        
        tx.rollback();
    }
    */
    /**
     * Testcase: isEmpty with arguments
     */
    public void semanticError044()
        throws Exception
    {
        String expectedMsg = "setFilter column(14): Wrong number of arguments.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.setFilter("team.isEmpty(1)");
            query.compile();
            checkMissingException(JDOQueryException.class, expectedMsg);
        }
        catch (JDOException ex)
        {
            checkJDOException(ex, JDOQueryException.class, expectedMsg);
        }
        
        tx.rollback();
    }

    /**
     * Testcase: contains with too manay arguments
     */
    public void semanticError045()
        throws Exception
    {
        String expectedMsg = "setFilter column(18): Wrong number of arguments.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.declareVariables("Employee e");
            query.setFilter("team.contains(e, 1)");
            query.compile();
            checkMissingException(JDOQueryException.class, expectedMsg);
        }
        catch (JDOException ex)
        {
            checkJDOException(ex, JDOQueryException.class, expectedMsg);
        }
        
        tx.rollback();
    }

    /**
     * Testcase: contains without arguments
     */
    public void semanticError046()
        throws Exception
    {
        String expectedMsg = "setFilter column(6): Wrong number of arguments.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.setFilter("team.contains()");
            query.compile();
            checkMissingException(JDOQueryException.class, expectedMsg);
        }
        catch (JDOException ex)
        {
            checkJDOException(ex, JDOQueryException.class, expectedMsg);
        }
        
        tx.rollback();
    }

    /**
     * Testcase: invalid string method call
     * checks bug report 4469306
     */
    public void semanticError047()
        throws Exception
    {
        String expectedMsg = "setFilter column(9): Invalid method call.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.setFilter("lastname.endWith(\"en\")");
            query.compile();
            checkMissingException(JDOQueryException.class, expectedMsg);
        }
        catch (JDOException ex)
        {
            checkJDOException(ex, JDOQueryException.class, expectedMsg);
        }
        
        tx.rollback();
    }

    /**
     * Testcase: invalid string method call results in wrong error message
     * checks bug report 4472057
     */
    public void semanticError048()
        throws Exception
    {
        String expectedMsg = "setFilter column(9): Invalid method call.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.setFilter("lastname.charAt(10)");
            query.compile();
            checkMissingException(JDOQueryException.class, expectedMsg);
        }
        catch (JDOException ex)
        {
            checkJDOException(ex, JDOQueryException.class, expectedMsg);
        }
        
        tx.rollback();
    }

    /**
     * Testcase: invalid int literal
     * checks bug report 4818832
     */
    public void semanticError049()
        throws Exception
    {
        String expectedMsg = "setFilter column(10): Invalid int literal '12345678900'.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.setFilter("empid == 12345678900");
            query.compile();
            checkMissingException(JDOQueryException.class, expectedMsg);
        }
        catch (JDOException ex)
        {
            checkJDOException(ex, JDOQueryException.class, expectedMsg);
        }
        
        tx.rollback();
    }

    /**
     * Testcase: invalid long literal
     * checks bug report 4818832
     */
    public void semanticError050()
        throws Exception
    {
        String expectedMsg = "setFilter column(11): Invalid long literal '-9223372036854775809L'.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.setFilter("empid == -9223372036854775809L");
            query.compile();
            checkMissingException(JDOQueryException.class, expectedMsg);
        }
        catch (JDOException ex)
        {
            checkJDOException(ex, JDOQueryException.class, expectedMsg);
        }
        
        tx.rollback();
    }

    /**
     * Testcase: access of non persistent field of persistence capable class
     */
    /* TBD: need persistence capable class providing non persistent fields
    public void semanticError0xx()
        throws Exception
    {
        String expectedMsg = "setFilter column(10): cannot access non persistent field 'fieldName' of persistence capable class 'class'";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.compile();
            checkMissingException(JDOQueryException.class, expectedMsg);
        }
        catch (JDOException ex)
        {
            checkJDOException(ex, JDOQueryException.class, expectedMsg);
        }
        
        tx.rollback();
    }
    */
}
