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
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.jdo.PersistenceManager;
import javax.jdo.Extent;
import javax.jdo.JDOFatalInternalException;
import javax.jdo.Query;
import javax.jdo.Transaction;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.company.Company;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Department;
import org.apache.jdo.tck.pc.company.Insurance;
import org.apache.jdo.tck.pc.company.Person;
import org.apache.jdo.tck.pc.company.Project;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.pc.mylib.PrimitiveTypes;

public abstract class QueryTest extends JDO_Test {

   /** */
    public static final String SERIALZED_QUERY = "query.ser";
    
    /** 
     * List of inserted instances (see methods insertPCPoints and
     * getFromInserted). 
     */
    protected List inserted = new ArrayList();

    // Helper methods to create persistent PCPoint instances
    
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
        String[] names = reader.getBeanDefinitionNames();
        for (int i = 0; i < names.length; i++) {
            Object bean = reader.getBean(names[i]);
            pm.makePersistent(bean);
            if (debug) logger.debug("inserted " + bean);
        }
        tx.commit();
        tx = null;
        return reader;
    }
    
    /** 
     * Reads a graph of company model objects from the specified xml file. This 
     * methods calls makePersistent for all instances of the specified 
     * pcRootClass which then runs the reachability algorithm. 
     * The method returns the CompanyModelReader instance allowing to access 
     * a compay model instance by name.
     */
    public CompanyModelReader loadCompanyModel(PersistenceManager pm,            
                                               String filename,
                                               Class pcRootClass) {
        CompanyModelReader reader = new CompanyModelReader(filename);
        Transaction tx = pm.currentTransaction();
        tx.begin();
        Map rootBeans = reader.getBeansOfType(pcRootClass);
        for (Iterator i = rootBeans.values().iterator(); i.hasNext();) {
            Object bean = i.next();
            pm.makePersistent(bean);
            if (debug) logger.debug("inserted " + bean);
        }
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
                if (firstObject == null) {
                    if (secondObject != null) {
                        return false;
                    }
                } else if (!firstObject.equals(secondObject)) {
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

        if (((Collection)result).size() != expected.size() ||
            !((Collection)result).containsAll(expected)) {
            fail(assertion, "Wrong query result" + 
                 "\nexpected: " + new ArrayList(expected) +
                 "\ngot:      " + new ArrayList((Collection)result));
        }
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
}
