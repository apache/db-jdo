/*
 * Copyright 2005 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
 
package org.apache.jdo.tck.query;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.jdo.Extent;
import javax.jdo.JDOFatalInternalException;
import javax.jdo.JDOUserException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import junit.framework.AssertionFailedError;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.company.Company;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Department;
import org.apache.jdo.tck.pc.company.Insurance;
import org.apache.jdo.tck.pc.company.Person;
import org.apache.jdo.tck.pc.company.Project;
import org.apache.jdo.tck.pc.mylib.MylibReader;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.pc.mylib.PrimitiveTypes;
import org.apache.jdo.tck.util.ConversionHelper;
import org.apache.jdo.tck.util.EqualityHelper;

public abstract class QueryTest extends JDO_Test {

    /** */
    public static final String SERIALZED_QUERY = "query.ser";

    /** */
    public static final String COMPANY_TESTDATA = 
        "org/apache/jdo/tck/pc/company/companyForQueryTests.xml";

    /** */
    public static final String MYLIB_TESTDATA = 
        "org/apache/jdo/tck/pc/mylib/mylibForQueryTests.xml";

    /** 
     * List of inserted instances (see methods insertPCPoints and
     * getFromInserted). 
     */
    protected List inserted = new ArrayList();

    // Helper methods to create persistent PCPoint instances
    
    /**
     * @see JDO_Test#localSetUp()
     */
    protected void localSetUp() {
        addTearDownClass(PCPoint.class);
    }
    
    /** */
    public void initDatabase(PersistenceManager pm, Class cls){
        cleanupDatabase(pm, cls);
        if (cls == PCPoint.class)
            insertPCPoints(pm, 5);
        else
            throw new JDOFatalInternalException(
                "Unsupported pc class for initDatabase " + cls);
    }

    /** */
    protected void insertPCPoints(PersistenceManager pm, int numInsert) {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            for(int i = 0; i<numInsert; i++) {
                Object pc = new PCPoint(i, i);
                pm.makePersistent(pc);
                inserted.add(pc);
            }
            tx.commit();
            tx = null;
            if (debug) logger.debug("Total objects inserted : " + numInsert);
        } 
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }
       
    /** */
    public List getFromInserted(List list) {
        if (list == null)
            return null;
        
        List result = new ArrayList();
        for (Iterator iter = list.iterator(); iter.hasNext();) {
            Object pc = iter.next();
            for (Iterator iteri = inserted.iterator(); iteri.hasNext();) {
                Object pci = iteri.next();
                if (((PCPoint)pc).getX() == ((PCPoint)pci).getX()) {
                    result.add(pci);
                    break;
                }
            }
        }
        return result;
    }
    
    // Company model helper methods
    
    /** 
     * Reads a graph of company model objects from the specified xml file. This 
     * methods explictly calls makePersistent for all named instances using the
     * specified PersistenceManager. The method returns the CompanyModelReader 
     * instance allowing to access a compay model instance by name.
     */
    public CompanyModelReader loadCompanyModel(PersistenceManager pm, 
                                               String filename) {
        CompanyModelReader reader = new CompanyModelReader(filename);
        Transaction tx = pm.currentTransaction();
        tx.begin();
        List rootList = (List)reader.getRootList();
        pm.makePersistentAll(rootList);
        if (debug) logger.debug("inserted " + rootList);
        tx.commit();
        tx = null;
        return reader;
    }
    
    /** 
     * Reads a graph of company model objects from the specified xml file. This 
     * methods explictly calls makePersistent for all named instances using the
     * specified PersistenceManager. The method returns the CompanyModelReader 
     * instance allowing to access a compay model instance by name.
     */
    public MylibReader loadMylib(PersistenceManager pm, String filename) {
        MylibReader reader = new MylibReader(filename);
        Transaction tx = pm.currentTransaction();
        tx.begin();
        List rootList = (List)reader.getRootList();
        pm.makePersistentAll(rootList);
        if (debug) logger.debug("inserted " + rootList);
        tx.commit();
        tx = null;
        return reader;
    }
    
    /** */
    public void cleanupCompanyModel(PersistenceManager pm) {
        Transaction tx = pm.currentTransaction();
        if (tx.isActive()) tx.rollback();
        try {
            tx.begin();
            cleanupDatabaseInternal(pm, Company.class);
            cleanupDatabaseInternal(pm, Department.class);
            cleanupDatabaseInternal(pm, Person.class);
            cleanupDatabaseInternal(pm, Insurance.class);
            cleanupDatabaseInternal(pm, Project.class);
            tx.commit();
            tx = null;
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }
    
    /**
     * Returns an array of company model instances for beans names 
     * in the given argument.
     * @param beanNames the bean names of company mode instances.
     * @return the array of company model instances. 
     */
    protected Object[] getCompanyModelInstances(String[] beanNames) {
        CompanyModelReader reader = new CompanyModelReader(COMPANY_TESTDATA);
        Object[] result = new Object[beanNames.length];
        for (int i = 0; i < beanNames.length; i++) {
            result[i] = beanNames[i] == null ? 
                    null : reader.getBean(beanNames[i]);
        }
        return result;
    }
    
    /**
     * Returns an array of mylib instances for beans names 
     * in the given argument.
     * @param beanNames the bean names of mylib instances.
     * @return the array of mylib instances. 
     */
    protected Object[] getMylibInstances(String[] beanNames) {
        MylibReader reader = new MylibReader(MYLIB_TESTDATA);
        Object[] result = new Object[beanNames.length];
        for (int i = 0; i < beanNames.length; i++) {
            result[i] = reader.getBean(beanNames[i]);
        }
        return result;
    }
    
    // PrimitiveTypes helper methods (creation and query)

    /** */
    public void loadPrimitiveTypes(PersistenceManager pm) {
        cleanupDatabase(pm, PrimitiveTypes.class);
        insertPrimitiveTypes(pm);
    }

    /** */
    protected void insertPrimitiveTypes(PersistenceManager pm) {
        boolean bFlag = false;
        String strValue = "";
        char charValue = '\u0000';
        int numInsert = 10;
        
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            for (int i = 1; i <= numInsert; i++ ) {
                if (i%2 == 1) {
                    bFlag = true;
                    strValue = "Odd" + i;
                    charValue = 'O';
                } 
                else {
                    bFlag = false;
                    strValue = "Even" + i;
                    charValue = 'E';
                }
                PrimitiveTypes primitiveObject = new PrimitiveTypes(
                    (long)i, bFlag, new Boolean(bFlag), (byte)i, new Byte((byte)i),
                    (short)i, new Short((short)i), (int) i, new Integer(i),
                    (long)i, new Long(i), (float)i, new Float(i), 
                    (double)i, new Double(i), charValue, new Character(charValue),
                    Calendar.getInstance().getTime(), strValue,
                    new BigDecimal(String.valueOf(i)), 
                    new BigInteger(String.valueOf(i)),
                    new Long(i));
                pm.makePersistent(primitiveObject);
            }
            tx.commit();
            tx = null;
            if (debug) logger.debug("Total objects inserted : " + numInsert);
        } 
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }
    
    /**
     * Creates and executes a PrimitiveTypes query with the specified filter. 
     * The method checks whether the query returns the expected result.
     */
    protected void runSimplePrimitiveTypesQuery(String filter, 
                                                PersistenceManager pm,
                                                Collection expected,
                                                String assertion) {
        Query q = pm.newQuery();
        q.setClass(PrimitiveTypes.class);
        q.setFilter(filter);
        Collection results = (Collection)q.execute();
        if (debug)
            logger.debug("execute '" + filter + "' returns " +  results.size() +
                         " instance(s)");
        checkQueryResultWithoutOrder(assertion, results, expected);
    }

    /**
     * Creates and executes a PrimitiveTypes query with the specified filter,
     * parameter declarations and parameter values. The method checks whether 
     * the query returns the expected result.
     */
    protected void runParameterPrimitiveTypesQuery(String filter,
                                                   String paramDecl,
                                                   Object paramValue,
                                                   PersistenceManager pm,
                                                   Collection expected,
                                                   String assertion) {
        Query q = pm.newQuery();
        q.setClass(PrimitiveTypes.class);
        q.setFilter(filter);
        q.declareParameters(paramDecl);
        Collection results = (Collection)q.execute(paramValue);
        if (debug)
            logger.debug("execute '" + filter + "' with param '" + paramValue +
                         "' returns " +  results.size() + " instance(s)");
        checkQueryResultWithoutOrder(assertion, results, expected);
    }

    // Helper methods to cleanup the datastore
    
    /** */
    public void cleanupDatabase(PersistenceManager pm, Class cls) {
        Transaction tx = pm.currentTransaction();
        if (tx.isActive()) tx.rollback();
        try {
            tx.begin();
            cleanupDatabaseInternal(pm, cls);
            tx.commit();
            tx = null;
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }
    
    /** */
    protected void cleanupDatabaseInternal(PersistenceManager pm, Class cls) {
        Extent e = pm.getExtent(cls, true);
        for (Iterator i = e.iterator(); i.hasNext();) {
            Object pc = i.next();
            if (debug) 
                logger.debug("Deleted the existing " + cls.getName() +
                             " object " + pc);
            pm.deletePersistent(pc);
        }
    }
    
    // Helper methods to checka query result
    
    /** */
    protected void checkQueryResultWithOrder(String assertion, 
                                             Object result, 
                                             Collection expected) {
        if (result == null) {
            fail(assertion, "Query returns null");
        }
        if (!(result instanceof Collection)) {
            fail(assertion, 
                 "Query result is not a collection: " + 
                 result.getClass().getName());
        }
        if (!compareOrderedResults((Collection)result, expected)) {
            String lf = System.getProperty("line.separator");
            result = 
                ConversionHelper.convertObjectArrayElements(result);
            expected = 
                ConversionHelper.convertsElementsOfTypeObjectArray(expected);
            fail(assertion,
                 "Wrong query result: " + lf +
                 "query returns: " + result + lf +
                 "expected result: " + expected);
        }
    }
    
    /** This method implements the semantics of AbstractList.equals but
     * does not require that the parameters actually be Lists.
     */
    protected boolean compareOrderedResults(Collection first, Collection second) {
        if (first == second) {
            return true;
        } else {
            Iterator firstIterator = first.iterator();
            Iterator secondIterator = second.iterator();
            while (firstIterator.hasNext()) {
                if (!secondIterator.hasNext()) {
                    // semantics of first.size() != second.size() without using size()
                    return false;
                }
                Object firstObject = firstIterator.next();
                Object secondObject = secondIterator.next();
                if (!equals(firstObject, secondObject)) {
                    return false;
                }
            }
            if (secondIterator.hasNext()) {
                // semantics of first.size() != second.size() without using size()
                return false;
            } else {
                return true;
            }
        }
    }
                        
    /** */
    protected void checkQueryResultWithoutOrder(String assertion, 
                                                Object result, 
                                                Collection expected) {
        if (result == null) {
            fail(assertion, "Query returns null");
        }
        if (!(result instanceof Collection)) {
            fail(assertion, "Query result is not a collection: " +
                 result.getClass().getName());
        }

        if (!equalsCollection((Collection)result, expected)) {
            result = 
                ConversionHelper.convertObjectArrayElements(result);
            expected = 
                ConversionHelper.convertsElementsOfTypeObjectArray(expected);
            fail(assertion, "Wrong query result" + 
                "\nexpected: " + expected +
                "\ngot:      " + result);
        }
    }

    /** */
    protected void checkUniqueResult(String assertion, 
                                     Object result, 
                                     Object expected) {
        if ((result != null && expected == null) ||
            (result == null && expected != null) || 
            (result != null && expected != null)) {
            if (!equals(result, expected)) {
                String lf = System.getProperty("line.separator");
                result = ConversionHelper.
                convertObjectArrayElements(result);
                expected = ConversionHelper.
                convertObjectArrayElements(expected);
                fail(assertion, "Wrong query result: " + lf +
                        "query returns: " + result + lf +
                        "expected result: " + expected);
            }
        }
    }

    /**
     * Returns <code>true</code> 
     * if <code>o1</code> and <code>o2</code> equal.
     * This method is capable to compare object arrays, 
     * collections of object arrays, maps of object arrays.
     * This method implements a narrowing in case of floating point values.
     * In case of big decimals it calls 
     * {@link BigDecimal#compareTo(java.lang.Object)}.
     * It allows <code>o1</code> and/or <code>o2</code>
     * to be <code>null</code>. 
     * @param o1 the first object
     * @param o2 the second object
     * @return <code>true</code> if <code>o1</code> and <code>o2</code> equal.
     */
    private boolean equals(Object o1, Object o2) {
        boolean result;
        if (o1 == o2) {
            result = true;
        } if ((o1 instanceof Object[]) && (o2 instanceof Object[])) {
            result = equalsObjectArray((Object[])o1, (Object[])o2);
        } else if ((o1 instanceof Collection) && (o2 instanceof Collection)) {
            result = equalsCollection((Collection)o1, (Collection)o2);
        } else if ((o1 instanceof Map) && (o2 instanceof Map)) {
            result = equalsMap((Map)o1, (Map)o2);
        }else if ((o1 instanceof Float) && (o2 instanceof Float)) {
            result = closeEnough(((Float)o1).floatValue(), 
                    ((Float)o2).floatValue());
        } else if ((o1 instanceof Double) && (o2 instanceof Double)) {
            result = closeEnough(((Double)o1).floatValue(), 
                    ((Double)o2).floatValue());
        } else if ((o1 instanceof BigDecimal) && (o2 instanceof BigDecimal)) {
            result = ((BigDecimal)o1).compareTo(o2) == 0;
        } else if (o1 != null) {
            result = o1.equals(o2);
        } else {
            // Due to the first if and the last if we have:
            // o1 == null && o2 != null
            result = false;
        }
        return result;
    }
    
    /**
     * Returns <code>true</code> 
     * if <code>o1</code> and <code>o2</code> equal.
     * This method iterates over both object arrays and calls
     * {@link QueryTest#equals(Object, Object)} passing
     * corresponding instances.
     * This method does not allow <code>o1</code> and <code>o2</code>
     * to be <code>null</code> both. 
     * @param o1 the first object array
     * @param o2 the second object array
     * @return <code>true</code> if <code>o1</code> and <code>o2</code> equal.
     */
    private boolean equalsObjectArray(Object[] o1, Object[] o2) {
        boolean result = true;
        if (o1.length != o2.length) {
            result = false;
        } else {
            for (int i = 0; i < o1.length; i++ ) {
                if (!equals(o1[i], o2[i])) {
                    result = false;
                    break;
                }
            }
        } 
        return result;
    }

    /**
     * Returns <code>true</code> 
     * if <code>o1</code> and <code>o2</code> equal.
     * This method iterates over the first collection and 
     * checks if each instance is contained in the second collection
     * by calling {@link QueryTest#contains(Collection, Object)}.
     * This method does not allow <code>o1</code> and <code>o2</code>
     * to be <code>null</code> both. 
     * @param o1 the first collection
     * @param o2 the second collection
     * @return <code>true</code> if <code>o1</code> and <code>o2</code> equal.
     */
    private boolean equalsCollection(Collection o1, Collection o2) {
        boolean result = true;
        if (o1.size() != o2.size()) {
            result = false;
        } else {
            for (Iterator i = o1.iterator(); i.hasNext(); ) {
                Object oo1 = i.next();
                if (!contains(o2, oo1)) {
                    result = false;
                    break;
                }
            }
        } 
        return result;
    }
    
    /**
     * Returns <code>true</code> 
     * if <code>o1</code> and <code>o2</code> equal.
     * This method checks if the key sets and the value sets of both
     * maps equal calling 
     * {@link QueryTest#equalsCollection(Collection, Collection).
     * This method does not allow <code>o1</code> and <code>o2</code>
     * to be <code>null</code> both. 
     * @param o1 the first map
     * @param o2 the second map
     * @return <code>true</code> if <code>o1</code> and <code>o2</code> equal.
     */
    private boolean equalsMap(Map o1, Map o2) {
        boolean result = true;
        if (o1.size() != o2.size()) {
            result = false;
        } else if (!equalsCollection(o1.keySet(), o2.keySet()) ||
                   !equalsCollection(o1.values(), o2.values())) {
            result = false;
        } 
        return result;
    }
    
    /**
     * Returns <code>true</code> if <code>o</code> is contained
     * in the given collection.
     * This method iterates the given collection and calls
     * {@link QueryTest#equals(Object, Object)} for each instance.
     * @param col the collection
     * @param o the object
     * @return <code>true</code> if <code>o</code> is contained
     * in the given collection.
     */
    private boolean contains(Collection col, Object o) {
        for (Iterator i = col.iterator(); i.hasNext(); ) {
            if (equals(o, i.next())) {
                return true;
            }
        }
        return false;
    }
    
    /** Returns <code>true</code> if the specified float values are close
     * enough to be considered to be equal for a deep equals
     * comparison. Floating point values are not exact, so comparing them
     * using <code>==</code> might not return useful results. This method
     * checks that both double values are within some percent of each
     * other. 
     * @param d1 one double to be tested for close enough 
     * @param d2 the other double to be tested for close enough 
     * @return <code>true</code> if the specified values are close enough.
     */
    public boolean closeEnough(double d1, double d2) {
        if (d1 == d2)
            return true;

        double diff = Math.abs(d1 - d2);
        return diff < Math.abs((d1 + d2) * EqualityHelper.DOUBLE_EPSILON);
    }

    /**
     * Returns <code>true</code> if the specified float values are close
     * enough to be considered to be equal for a deep equals
     * comparison. Floating point values are not exact, so comparing them 
     * using <code>==</code> might not return useful results. This method
     * checks that both float values are within some percent of each
     * other. 
     * @param f1 one float to be tested for close enough 
     * @param f2 the other float to be tested for close enough 
     * @return <code>true</code> if the specified values are close enough.
     */
    public boolean closeEnough(float f1, float f2) {
        if (f1 == f2)
            return true;

        float diff = Math.abs(f1 - f2);
        return diff < Math.abs((f1 + f2) * EqualityHelper.FLOAT_EPSILON);
    }

    // Debugging helper methods
     
    /** */
    protected void printOutput(Object results, Collection expected) {
        if (!debug)
            return;

        Iterator iter = null;
        PCPoint pcp = null;
        if (results == null) {
            logger.debug("Query returns null");
        }
        if (!(results instanceof Collection)) {
            logger.debug("Query result is not a collection: " +
                         results.getClass().getName());
        }
        logger.debug("Retrived Objects are:");
        iter = ((Collection)results).iterator();
        while (iter.hasNext()) {
            pcp = (PCPoint)iter.next();
            logger.debug("X = " + pcp.getX() + "\tY = " + pcp.getY());
        }
            
        logger.debug("Expected Objects are:");
        iter = ((Collection)expected).iterator();
        while (iter.hasNext()) {
            pcp = (PCPoint)iter.next();
            logger.debug("X = " + pcp.getX() + "\tY = " + pcp.getY());
        }
    }
    
    // compile query methods

    /**
     * Compiles the given query element holder instance as a JDO API query.
     * Argument <code>positive</code> determines if the compilation is supposed
     * to succeed or to fail. If <code>true</code> and the compilation fails,
     * then the test case fails prompting argument <code>assertion</code>.
     * If <code>false</code> and the compilation succeeds,
     * then the test case fails prompting argument <code>assertion</code>.
     * Otherwise the test case succeeds. 
     * @param assertion the assertion to prompt if the test case fails.
     * @param queryElementHolder the query to execute.
     * @param positive determines if the compilation is supposed
     * to succeed or to fail.
     */
    protected void compileAPIQuery(String assertion,
            QueryElementHolder queryElementHolder, boolean positive) {
        if (logger.isDebugEnabled()) {
            logger.debug("Compiling API query: " + queryElementHolder);
        }
        compile(assertion, queryElementHolder, false, null, positive);
    }

    /**
     * Compiles the given query element holder instance 
     * as a JDO single string query.
     * Argument <code>positive</code> determines if the compilation is supposed
     * to succeed or to fail. If <code>true</code> and the compilation fails,
     * then the test case fails prompting argument <code>assertion</code>.
     * If <code>false</code> and the compilation succeeds,
     * then the test case fails prompting argument <code>assertion</code>.
     * Otherwise the test case succeeds. 
     * @param assertion the assertion to prompt if the test case fails.
     * @param queryElementHolder the query to execute.
     * @param positive determines if the compilation is supposed
     * to succeed or to fail.
     */
    protected void compileSingleStringQuery(String assertion,
            QueryElementHolder queryElementHolder, boolean positive) {
        if (logger.isDebugEnabled())
            logger.debug("Compiling single string query: " + 
                    queryElementHolder);
        compile(assertion, queryElementHolder, true, null, positive);
    }
    
    /**
     * Compiles the given single string query.
     * Argument <code>positive</code> determines if the compilation is supposed
     * to succeed or to fail. If <code>true</code> and the compilation fails,
     * then the test case fails prompting argument <code>assertion</code>.
     * If <code>false</code> and the compilation succeeds,
     * then the test case fails prompting argument <code>assertion</code>.
     * Otherwise the test case succeeds. 
     * @param assertion the assertion to prompt if the test case fails.
     * @param singleStringQuery the single string query
     * @param positive determines if the compilation is supposed
     * to succeed or to fail.
     */
    protected void compileSingleStringQuery(String assertion, 
            String singleStringQuery, boolean positive) {
        if (logger.isDebugEnabled())
            logger.debug("Compiling single string query: " + 
                    singleStringQuery);
        compile(assertion, null, true, singleStringQuery, positive);
    }

    /**
     * Compiles the given query element holder instance 
     * as a JDO API query or single string query, 
     * depending on argument <code>asSingleString</code>.
     * Argument <code>singleStringQuery</code> exists to support queries
     * which cannot be expressed as query element holder instances.
     * That argument is ignored if argument <code>queryElementHolder</code> 
     * is set.
     * Argument <code>positive</code> determines if the compilation is supposed
     * to succeed or to fail. If <code>true</code> and the compilation fails,
     * then the test case fails prompting argument <code>assertion</code>.
     * If <code>false</code> and the compilation succeeds,
     * then the test case fails prompting argument <code>assertion</code>.
     * Otherwise the test case succeeds. 
     * @param assertion the assertion to prompt if the test case fails.
     * @param queryElementHolder the query to compile.
     * @param asSingleString determines if the query specified by 
     * <code>queryElementHolder</code> is compiled as single string query 
     * or as API query. 
     * @param singleStringQuery the query to compile 
     * as a JDO single string query if there is no query element holder.
     * @param positive determines if the compilation is supposed
     * to succeed or to fail.
     */
    private void compile(String assertion, 
            QueryElementHolder queryElementHolder, boolean asSingleString,  
            String singleStringQuery, boolean positive) {
        PersistenceManager pm = getPM();
        Transaction tx = pm.currentTransaction();
        tx.begin();
        try {
            Query query;
            if (queryElementHolder != null) {
                if (asSingleString) {
                    query = queryElementHolder.getSingleStringQuery(pm);
                } else {
                    query = queryElementHolder.getAPIQuery(pm);
                }
            } else {
                query = getPM().newQuery(singleStringQuery);
            }
            query.compile();
            if (!positive) {
                String queryText = queryElementHolder != null ? 
                        queryElementHolder.toString() :
                        singleStringQuery;
                fail(assertion + 
                        "Query compilation must throw JDOUserException: " + 
                        queryText);
            }
        } catch (JDOUserException e) {
            if (positive) {
                String queryText = queryElementHolder != null ? 
                        queryElementHolder.toString() :
                        singleStringQuery;
                fail(assertion + "Query '" + queryText +
                        "' must be compilable. The exception message is: " + 
                        e.getMessage());
            }
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }
    
    // execute query methods
    
    /**
     * Executes the given query element holder instance as a JDO API query.
     * The result of that query is compared against the given argument 
     * <code>expectedResult</code>. The array elements of that argument
     * must match the content of the result collection 
     * returned by {@link Query#execute()}.
     * If the expected result does not match the returned query result,
     * then the test case fails prompting argument <code>assertion</code>.
     * @param assertion the assertion to prompt if the test case fails.
     * @param queryElementHolder the query to execute.
     * @param expectedResult the expected query result.
     */
    protected void executeAPIQuery(String assertion,
            QueryElementHolder queryElementHolder, Object[] expectedResult) {
        executeAPIQuery(assertion, queryElementHolder, null, expectedResult);
    }

    /**
     * Executes the given query element holder instance as a JDO API query.
     * The result of that query is compared against the given argument 
     * <code>expectedResult</code>. The array elements of that argument
     * must match the content of the result collection 
     * returned by {@link Query#executeWithArray(java.lang.Object[])}.
     * Argument <code>parameters</code> is passed as the parameter 
     * to that method.
     * If the expected result does not match the returned query result,
     * then the test case fails prompting argument <code>assertion</code>.
     * @param assertion the assertion to prompt if the test case fails.
     * @param queryElementHolder the query to execute.
     * @param parameters the parmaters of the query.
     * @param expectedResult the expected query result.
     */
    protected void executeAPIQuery(String assertion,
            QueryElementHolder queryElementHolder, 
            Object[] parameters, Object[] expectedResult) {
        if (logger.isDebugEnabled()) {
            logger.debug("Executing API query: " + queryElementHolder);
        }
        execute(assertion, queryElementHolder, false, 
                parameters, expectedResult);
    }
    
    /**
     * Executes the given query element holder instance 
     * as a JDO single string query.
     * The result of that query is compared against the given argument 
     * <code>expectedResult</code>. The array elements of that argument
     * must match the content of the result collection 
     * returned by {@link Query#execute()}.
     * If the expected result does not match the returned query result,
     * then the test case fails prompting argument <code>assertion</code>.
     * @param assertion the assertion to prompt if the test case fails.
     * @param queryElementHolder the query to execute.
     * @param expectedResult the expected query result.
     */
    protected void executeSingleStringQuery(String assertion,
            QueryElementHolder queryElementHolder, Object[] expectedResult) {
        executeSingleStringQuery(assertion, queryElementHolder, 
                null, expectedResult);
    }
    
    /**
     * Executes the given query element holder instance 
     * as a JDO single string query.
     * The result of that query is compared against the given argument 
     * <code>expectedResult</code>. The array elements of that argument
     * must match the content of the result collection 
     * returned by {@link Query#executeWithArray(java.lang.Object[])}.
     * Argument <code>parameters</code> is passed as the parameter 
     * to that method.
     * If the expected result does not match the returned query result,
     * then the test case fails prompting argument <code>assertion</code>.
     * @param assertion the assertion to prompt if the test case fails.
     * @param queryElementHolder the query to execute.
     * @param parameters the parmaters of the query.
     * @param expectedResult the expected query result.
     */
    protected void executeSingleStringQuery(String assertion,
            QueryElementHolder queryElementHolder, 
            Object[] parameters, Object[] expectedResult) {
        if (logger.isDebugEnabled())
            logger.debug("Executing single string query: " + 
                    queryElementHolder);
        execute(assertion, queryElementHolder, true, 
                parameters, expectedResult);
    }
    
    /**
     * Executes the given query element holder instance 
     * as a JDO API query of a single string query,
     * depending on argument <code>asSingleString</code>.
     * The result of that query is compared against the given argument 
     * <code>expectedResult</code>. The array elements of that argument
     * must match the content of the result collection 
     * returned by {@link Query#executeWithArray(java.lang.Object[])}.
     * Argument <code>parameters</code> is passed as the parameter 
     * to that method.
     * If the expected result does not match the returned query result,
     * then the test case fails prompting argument <code>assertion</code>.
     * @param assertion the assertion to prompt if the test case fails.
     * @param queryElementHolder the query to execute.
     * @param asSingleString determines if the query is executed as
     * single string query or as API query.
     * @param parameters the parmaters of the query.
     * @param expectedResult the expected query result.
     * @return
     */
    private Object execute(String assertion, 
            QueryElementHolder queryElementHolder, boolean asSingleString,
            Object[] parameters, Object[] expectedResult) {
        Query query = asSingleString ?
                queryElementHolder.getSingleStringQuery(pm) :
                    queryElementHolder.getAPIQuery(pm);
        Object result = execute(assertion, query, queryElementHolder.toString(),
                queryElementHolder.isUnique(), queryElementHolder.hasOrdering(),
                parameters, expectedResult);
        return result;
    }

    /**
     * Executes the given query instance.
     * Argument <code>parameters</code> is passed as an argument
     * to the method {@link Query#executeWithArray(java.lang.Object[])}.
     * If <code>parameters</code> are <code>null</code>,
     * then method {@link Query#execute()} is called 
     * on the given query instance instead.<p>
     * 
     * The result of query execution is compared against the argument 
     * <code>expectedResult</code>. The array elements of that argument
     * must match the content of the query result collection,
     * otherwise this method throws an {@link AssertionFailedError} and 
     * the calling test case fails prompting argument 
     * <code>assertion</code>.<p>
     * 
     * In case of a unique query, only the first element 
     * of the result collection is compared against the first array element 
     * of argument <code>expectedResult</code>. 
     * A <code>null</code> as the first element indicates, that the given query 
     * has no results, e.g. the filter does not match 
     * any persistent instances.<p>
     * 
     * If argument <code>expectedResult</code> is <code>null</code>,
     * then the test case invoking this method is considered to be
     * a negative test case. 
     * Then, query execution is expected to throw a {@link JDOUserException}.
     * If query execution succeeds in this case, then this method throws
     * an {@link AssertionFailedError} and the calling test case fails
     * prompting argument <code>assertion</code>.<p>
     * 
     * @param assertion the assertion to prompt if the test case fails.
     * @param query the query to execute.
     * @param asSingleString the single string representation of the query.
     * This parameter is only used as part of the falure message.
     * @param isUnique indicates if the query has a unique result.
     * @param hasOrdering indicates if the query has an ordering clause.
     * @param parameters the parmaters of the query.
     * @param expectedResult the expected query result.
     * @return the result collection
     */
    protected Object execute(String assertion, Query query, 
            String singleStringQuery,
            boolean isUnique, boolean hasOrdering,
            Object[] parameters, Object[] expectedResult) {
        boolean positive = expectedResult != null;
        Object result = null;
        PersistenceManager pm = getPM();
        Transaction tx = pm.currentTransaction();
        tx.begin();
        try {
            try {
                result = parameters != null ? 
                        query.executeWithArray(parameters) : query.execute();
    
                if (positive) {
                    if (isUnique) {
                        checkUniqueResult(assertion, result, expectedResult[0]);
                    } else if (hasOrdering) {
                        List expectedResultList = 
                            Arrays.asList(expectedResult);
                        checkQueryResultWithOrder(assertion, result, 
                                expectedResultList);
                    } else {
                        Collection expectedResultCollection = 
                            Arrays.asList(expectedResult);
                        checkQueryResultWithoutOrder(assertion, result, 
                                expectedResultCollection);
                    }
                } else {
                    fail(assertion + "Query must throw JDOUserException: " + 
                            singleStringQuery);
                }
            } finally {
                query.close(result);
            }
        } catch (JDOUserException e) {
            if (positive) {
                throw e;
            }
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
        return result;
    }
}
