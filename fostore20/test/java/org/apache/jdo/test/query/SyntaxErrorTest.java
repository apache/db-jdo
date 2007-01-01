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
 * SyntaxErrorTest.java
 *
 * Created on March 31, 2000
 */

package org.apache.jdo.test.query;

import java.util.Collection;
import java.util.ArrayList;
import java.io.PrintStream;

import javax.jdo.*;

import org.apache.jdo.jdoql.JDOQueryException;
import org.apache.jdo.pc.xempdept.Employee;

/** 
 *
 * @author  Michael Bouschen
 */
public class SyntaxErrorTest
    extends NegativeTest
{
    public SyntaxErrorTest(PersistenceManagerFactory pmf, PrintStream log)
    {
        super(pmf, log);
    }

    protected boolean isTestMethodName(String methodName)
    {
       return methodName.startsWith("syntaxError");
    }
    
    // ==========================================================================
    // Test methods
    // ==========================================================================

    /**
     * Testcase: lexical error unknown character %
     */
    public void syntaxError001()
        throws Exception
    {
        String expectedMsg = "setFilter column(1): Syntax error unexpected char '%'.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.setFilter("%");
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
     * Testcase: lexical error empty char literal
     */
    public void syntaxError002()
        throws Exception
    {
        String expectedMsg = "setFilter column(2): Syntax error unexpected char '''";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.setFilter("'' == 'c'");
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
     * Testcase: lexical error missing closing parenthesis
     */
    public void syntaxError003()
        throws Exception
    {
        String expectedMsg = "setFilter column(12): Unexpected end of text.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.setFilter("(1 + 2 == 3");
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
     * Testcase: lexical error missing closing double quote
     */
    public void syntaxError004()
        throws Exception
    {
        String expectedMsg = "setFilter column(22): Unexpected end of text.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.setFilter("firstname == \"Michael");
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
     * Testcase: lexical error missing closing single quote
     */
    public void syntaxError005()
        throws Exception
    {
        String expectedMsg = "setFilter column(10): Syntax error expected char ''', found ' '.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.setFilter("'a' == 'b ");
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
     * Testcase: lexical error invalid interger literal 12a2
     */
    public void syntaxError006()
        throws Exception
    {
        String expectedMsg = "setFilter column(3): Syntax error unexpected token 'a2'.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.setFilter("12a2");
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
     * Testcase: lexical error invalid octal literal 09
     */
    public void syntaxError007()
        throws Exception
    {
        String expectedMsg = "setFilter column(2): Syntax error unexpected token '9'.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.setFilter("09");
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
     * Testcase: lexical error invalid hex literal 0x9g
     */
    public void syntaxError008()
        throws Exception
    {
        String expectedMsg = "setFilter column(4): Syntax error unexpected token 'g'.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.setFilter("0x9g");
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
     * Testcase: lexical error invalid float literal 1.23.45
     */
    public void syntaxError009()
        throws Exception
    {
        String expectedMsg = "setFilter column(5): Syntax error unexpected token '.45'.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.setFilter("1.23.45");
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
     * Testcase: lexical error invalid float literal suffix 1.23g
     */
    public void syntaxError010()
        throws Exception
    {
        String expectedMsg = "setFilter column(5): Syntax error unexpected token 'g'.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.setFilter("1.23g");
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
     * Testcase: lexical error unknown keyword/identifier
     */
    public void syntaxError011()
        throws Exception
    {
        String expectedMsg = "setFilter column(1): Undefined identifier 'select'.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.setFilter("select");
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
     * Testcase: lexical error keyword in mixed case
     */
    public void syntaxError012()
        throws Exception
    {
        String expectedMsg = "declareImports column(1): Syntax error unexpected token 'iMpOrT'.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.declareImports("iMpOrT org.apache.jdo.pc.xempdept.Department;");
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
     * Testcase: syntax error missing semicolon in import declaration
     */
    public void syntaxError013()
        throws Exception
    {
        String expectedMsg = "declareImports column(44): Syntax error unexpected token 'import'.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.declareImports("import org.apache.jdo.pc.xempdept.Employee import org.apache.jdo.pc.xempdept.Department");
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
     * Testcase: syntax error missing type specification in import declaration
     */
    public void syntaxError014()
        throws Exception
    {
        String expectedMsg = "declareImports column(7): Syntax error at ';'.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.declareImports("import;");
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
     * Testcase: syntax error missing comma in parameter declaration
     */
    public void syntaxError015()
        throws Exception
    {
        String expectedMsg = "declareParameters column(8): Syntax error unexpected token 'int'.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.declareParameters("long l int i");
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
     * Testcase: syntax error missing type specification in parameter declaration
     */
    public void syntaxError016()
        throws Exception
    {
        String expectedMsg = "declareParameters column(2): Unexpected end of text.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.declareParameters("l");
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
     * Testcase: syntax error missing name specification in parameter declaration
     */
    public void syntaxError017()
        throws Exception
    {
        String expectedMsg = "declareParameters column(4): Unexpected end of text.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.declareParameters("int");
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
     * Testcase: syntax error missing semicolon in variable declaration
     */
    public void syntaxError018()
        throws Exception
    {
        String expectedMsg = "declareVariables column(13): Syntax error unexpected token 'Employee'.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.declareImports("import org.apache.jdo.pc.xempdept.Employee");
            query.declareVariables("Employee e1 Employee e2");
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
     * Testcase: syntax error missing type specification in variable declaration
     */
    public void syntaxError019()
        throws Exception
    {
        String expectedMsg = "declareVariables column(2): Unexpected end of text.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.declareImports("import org.apache.jdo.pc.xempdept.Employee");
            query.declareVariables("v");
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
     * Testcase: syntax error missing name specification in variable declaration
     */
    public void syntaxError020()
        throws Exception
    {
        String expectedMsg = "declareVariables column(9): Unexpected end of text.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.declareImports("import org.apache.jdo.pc.xempdept.Employee");
            query.declareVariables("Employee");
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
     * Testcase: syntax error missing comma in ordering specification
     */
    public void syntaxError021()
        throws Exception
    {
        String expectedMsg = "setOrdering column(18): Syntax error unexpected token 'firstname'.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.setOrdering("deptid ascending firstname descending");
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
     * Testcase: syntax error missing field specification in ordering specification
     */
    public void syntaxError022()
        throws Exception
    {
        String expectedMsg = "setOrdering column(1): Syntax error unexpected token 'ascending'.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.setOrdering("ascending");
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
     * Testcase: syntax error missing argument of unary operator
     */
    public void syntaxError023()
        throws Exception
    {
        String expectedMsg = "setFilter column(2): Unexpected end of text.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.setFilter("!");
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
     * Testcase: syntax error missing argument of logical operator
     */
    public void syntaxError024()
        throws Exception
    {
        String expectedMsg = "setFilter column(7): Unexpected end of text.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.setFilter("true &");
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
     * Testcase: syntax error missing argument of conditional operator
     */
    public void syntaxError025()
        throws Exception
    {
        String expectedMsg = "setFilter column(8): Unexpected end of text.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.setFilter("true ||");
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
     * Testcase: syntax error missing argument of relational operator
     */
    public void syntaxError026()
        throws Exception
    {
        String expectedMsg = "setFilter column(5): Unexpected end of text.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.setFilter("1 ==");
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
     * Testcase: syntax error missing argument of arithmetic operator
     */
    public void syntaxError027()
        throws Exception
    {
        String expectedMsg = "setFilter column(5): Syntax error unexpected token '=='.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.setFilter("1 + == 3");
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
     * Testcase: syntax error missing field in navigation path
     */
    public void syntaxError028()
        throws Exception
    {
        String expectedMsg = "setFilter column(7): Syntax error at '=='.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.setFilter("dept. == 1");
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
     * Testcase: syntax error multiple import keyword in import declaration
     */
    public void syntaxError029()
        throws Exception
    {
        String expectedMsg = "declareImports column(8): Syntax error at 'import'.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.declareImports("import import org.apache.jdo.pc.xempdept.Employee");
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
     * Testcase: syntax error multiple type specification in import declaration
     */
    public void syntaxError030()
        throws Exception
    {
        String expectedMsg = "declareImports column(44): Syntax error unexpected token 'org'.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.declareImports("import org.apache.jdo.pc.xempdept.Employee org.apache.jdo.pc.xempdept.Employee");
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
     * Testcase: syntax error multiple comma in parameter declaration
     */
    public void syntaxError031()
        throws Exception
    {
        String expectedMsg = "declareParameters column(8): Syntax error at ','.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.declareParameters("int i, , long l");
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
     * Testcase: syntax error multiple type specification in parameter declaration
     */
    public void syntaxError032()
        throws Exception
    {
        String expectedMsg = "declareParameters column(5): Syntax error at 'long'.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.declareParameters("int long l");
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
     * Testcase: syntax error multiple name specification in parameter declaration
     */
    public void syntaxError033()
        throws Exception
    {
        String expectedMsg = "declareParameters column(7): Syntax error unexpected token 'l'.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.declareParameters("int i l");
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
     * Testcase: syntax error multiple semicolon in variable declaration
     */
    public void syntaxError034()
        throws Exception
    {
        String expectedMsg = "declareVariables column(16): Syntax error at ';'.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.declareImports("import org.apache.jdo.pc.xempdept.Department");
            query.declareVariables("Department d1; ;Department d2");
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
     * Testcase: syntax error multiple type specification in variable declaration
     */
    public void syntaxError035()
        throws Exception
    {
        String expectedMsg = "declareVariables column(23): Syntax error unexpected token 'd'.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.declareVariables("Department Department d");
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
     * Testcase: syntax error multiple name specification in variable declaration
     */
    public void syntaxError036()
        throws Exception
    {
        String expectedMsg = "declareVariables column(15): Syntax error unexpected token 'd2'.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.declareVariables("Department d1 d2");
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
     * Testcase: syntax error multiple comma in ordering specification
     */
    public void syntaxError037()
        throws Exception
    {
        String expectedMsg = "setOrdering column(6): Syntax error unexpected token ','.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.setOrdering("empid, , firstname");
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
     * Testcase: syntax error multiple field specification in ordering specification
     */
    public void syntaxError038()
        throws Exception
    {
        String expectedMsg = "setOrdering column(7): Syntax error unexpected token 'firstname'.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.setOrdering("empid firstname ascending");
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
     * Testcase: syntax error multiple direction in ordering specification
     */
    public void syntaxError039()
        throws Exception
    {
        String expectedMsg = "setOrdering column(18): Syntax error unexpected token 'ascending'.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.setOrdering("empid descending ascending");
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
     * Testcase: syntax error multiple arguments of unary operation
     */
    public void syntaxError040()
        throws Exception
    {
        String expectedMsg = "setFilter column(8): Syntax error unexpected token 'false'.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.setFilter("! true false");
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
     * Testcase: syntax error multiple arguments of logical operator
     */
    public void syntaxError041()
        throws Exception
    {
        String expectedMsg = "setFilter column(19): Syntax error unexpected token 'false'.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.setFilter("empid == 1 | true false");
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
     * Testcase: syntax error multiple arguments of conditinal operator
     */
    public void syntaxError042()
        throws Exception
    {
        String expectedMsg = "setFilter column(20): Syntax error unexpected token 'false'.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.setFilter("empid == 1 && true false");
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
     * Testcase: syntax error multiple arguments of relational operator
     */
    public void syntaxError043()
        throws Exception
    {
        String expectedMsg = "setFilter column(13): Syntax error unexpected token '2'.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.setFilter("empid >= 1  2");
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
     * Testcase: syntax error multiple arguments of arithmetic operator
     */
    public void syntaxError044()
        throws Exception
    {
        String expectedMsg = "setFilter column(17): Syntax error unexpected token '4'.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.setFilter("empid == (1 + 3 4)");
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
     * Testcase: syntax error unexpected parenthesis
     */
    public void syntaxError045()
        throws Exception
    {
        String expectedMsg = "setFilter column(7): Syntax error unexpected token '('.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.setFilter("empid ( empid");
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
     * Testcase: syntax error unexpected identifier
     */
    public void syntaxError046()
        throws Exception
    {
        String expectedMsg = "setFilter column(7): Syntax error unexpected token 'empid'.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.setFilter("empid empid");
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
     * Testcase: syntax error invalid pre-increment operator
     */
    public void syntaxError047()
        throws Exception
    {
        String expectedMsg = "setFilter column(1): Syntax error unexpected token '++'.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.setFilter("++empid == 1");
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
     * Testcase: syntax error invalid pre-decrement operator
     */
    public void syntaxError048()
        throws Exception
    {
        String expectedMsg = "setFilter column(1): Syntax error unexpected token '--'.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.setFilter("--empid == 1");
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
     * Testcase: syntax error invalid assignemnt operator 
     */
    public void syntaxError049()
        throws Exception
    {
        String expectedMsg = "setFilter column(7): Syntax error unexpected token '='.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.setFilter("empid = 1");
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
     * Testcase: syntax error invalid compound assignment operator 
     */
    public void syntaxError050()
        throws Exception
    {
        String expectedMsg = "setFilter column(7): Syntax error unexpected token '+='.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.setFilter("empid += 1");
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
     * Testcase: syntax error invalid shift operator
     */
    public void syntaxError051()
        throws Exception
    {
        String expectedMsg = "setFilter column(7): Syntax error unexpected token '<<'.";
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.setFilter("empid << 1");
            query.compile();
            checkMissingException(JDOQueryException.class, expectedMsg);
        }
        catch (JDOException ex)
        {
            checkJDOException(ex, JDOQueryException.class, expectedMsg);
        }
        
        tx.rollback();
    }

}
