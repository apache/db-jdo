/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
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
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.jdo.JDOException;
import javax.jdo.JDOFatalInternalException;
import javax.jdo.JDOHelper;
import javax.jdo.JDOQLTypedQuery;
import javax.jdo.JDOUserException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import org.apache.jdo.tck.AbstractReaderTest;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.mylib.MylibReader;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.pc.mylib.PrimitiveTypes;
import org.apache.jdo.tck.util.ConversionHelper;
import org.apache.jdo.tck.util.EqualityHelper;

public abstract class QueryTest extends AbstractReaderTest {

  /** */
  public static final String SERIALZED_QUERY = "query.ser";

  /** */
  public static final String COMPANY_TESTDATA =
      "org/apache/jdo/tck/pc/company/companyForQueryTests.xml";

  /** */
  public static final String MYLIB_TESTDATA = "org/apache/jdo/tck/pc/mylib/mylibForQueryTests.xml";

  /** List of inserted instances (see methods insertPCPoints and getFromInserted). */
  protected final List<PCPoint> persistentPCPoints = new ArrayList<>();

  /**
   * List of transient copies of inserted instances (see methods insertPCPoints and
   * getFromInserted).
   */
  protected final List<PCPoint> transientPCPoints = new ArrayList<>();

  /**
   * The company model reader is used to read company model instances from an XML file. Instances
   * refered by this reader are made persistent by {@link
   * QueryTest#loadAndPersistCompanyModel(PersistenceManager)}.
   */
  private CompanyModelReader companyModelReaderForPersistentInstances;

  /**
   * The company model reader is used to read company model instances from an XML file. Instances
   * refered by this reader remain transient.
   */
  private CompanyModelReader companyModelReaderForTransientInstances;

  /**
   * The mylib reader is used to read mylib instances from an XML file. Instances refered by this
   * reader are made persistent by {@link QueryTest#loadAndPersistMylib(PersistenceManager)}.
   */
  private MylibReader mylibReaderForPersistentInstances;

  /**
   * The mylib reader is used to read mylib instances from an XML file. Instances refered by this
   * reader are made persistent by
   */
  private MylibReader mylibReaderForTransientInstances;

  // Helper methods to create persistent PCPoint instances

  /**
   * @param pm the PersistenceManager
   */
  public void loadAndPersistPCPoints(PersistenceManager pm) {
    insertPCPoints(pm, 5);
  }

  /**
   * @param pm the PersistenceManager
   * @param numInsert number of instances to be inserted
   */
  protected void insertPCPoints(PersistenceManager pm, int numInsert) {
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      for (int i = 0; i < numInsert; i++) {
        PCPoint pc = new PCPoint(i, i);
        pm.makePersistent(pc);
        persistentPCPoints.add(pc);
        transientPCPoints.add(new PCPoint(pc));
      }
      tx.commit();
      tx = null;
      if (debug) logger.debug("Total objects inserted : " + numInsert);
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }

  /**
   * @param list list
   * @return list
   */
  public List<PCPoint> getFromInserted(List<PCPoint> list) {
    if (list == null) return null;

    List<PCPoint> result = new ArrayList<>();
    for (PCPoint pc : list) {
      for (PCPoint pci : persistentPCPoints) {
        if (pc.getX() == pci.getX()) {
          result.add(pci);
          break;
        }
      }
    }
    return result;
  }

  public PCPoint getTransientPCPoint(int x) {
    PCPoint result = null;
    for (PCPoint pc : transientPCPoints) {
      if (x == pc.getX()) {
        result = pc;
        break;
      }
    }
    return result;
  }

  public PCPoint getPersistentPCPoint(PersistenceManager pm, int x) {
    PCPoint result = null;
    for (PCPoint pc : persistentPCPoints) {
      if (x == pc.getX()) {
        Object oid = JDOHelper.getObjectId(pc);
        result = (PCPoint) pm.getObjectId(oid);
      }
    }
    return result;
  }

  // Company model and mylib helper methods

  /**
   * Returns the name of the company test data resource.
   *
   * @return name of the company test data resource.
   */
  protected String getCompanyTestDataResource() {
    return COMPANY_TESTDATA;
  }

  /**
   * Initializes and returns the company model reader for persistent instances.
   *
   * @return the company model reader for persistent instances.
   */
  private CompanyModelReader getCompanyModelReaderForPersistentInstances() {
    if (companyModelReaderForPersistentInstances == null) {
      companyModelReaderForPersistentInstances =
          new CompanyModelReader(getCompanyTestDataResource());
    }
    return companyModelReaderForPersistentInstances;
  }

  /**
   * Initializes and returns the company model reader for transient instances.
   *
   * @return the company model reader for transient instances.
   */
  private CompanyModelReader getCompanyModelReaderForTransientInstances() {
    if (companyModelReaderForTransientInstances == null) {
      companyModelReaderForTransientInstances =
          new CompanyModelReader(getCompanyTestDataResource());
    }
    return companyModelReaderForTransientInstances;
  }

  /**
   * Initializes and returns the mylib reader for persistent instances.
   *
   * @return the mylib reader for persistent instances.
   */
  private MylibReader getMylibReaderForPersistentInstances() {
    if (mylibReaderForPersistentInstances == null) {
      mylibReaderForPersistentInstances = new MylibReader(MYLIB_TESTDATA);
    }
    return mylibReaderForPersistentInstances;
  }

  /**
   * Initializes and returns the mylib reader for transient instances.
   *
   * @return the mylib reader for transient instances.
   */
  private MylibReader getMylibReaderForTransientInstances() {
    if (mylibReaderForTransientInstances == null) {
      mylibReaderForTransientInstances = new MylibReader(MYLIB_TESTDATA);
    }
    return mylibReaderForTransientInstances;
  }

  /**
   * Reads a graph of company model objects from the internal reader. This methods explictly calls
   * makePersistent for all named instances using the specified PersistenceManager. The method
   * returns the CompanyModelReader instance allowing to access a compay model instance by name.
   *
   * @param pm the PersistenceManager
   * @return the company model reader
   */
  public CompanyModelReader loadAndPersistCompanyModel(PersistenceManager pm) {
    makePersistentAll(pm, getRootList(getCompanyModelReaderForPersistentInstances()));
    return getCompanyModelReaderForPersistentInstances();
  }

  /**
   * Reads a graph of mylib objects from the internal reader. This methods explictly calls
   * makePersistent for all named instances using the specified PersistenceManager. The method
   * returns the CompanyModelReader instance allowing to access a compay model instance by name.
   *
   * @param pm the PersistenceManager
   * @return mylib reader
   */
  public MylibReader loadAndPersistMylib(PersistenceManager pm) {
    makePersistentAll(pm, getRootList(getMylibReaderForPersistentInstances()));
    return getMylibReaderForPersistentInstances();
  }

  /**
   * Persists the given pc instances.
   *
   * @param pm the PersistenceManager
   * @param pcInstances the pc instances to persist
   */
  private void makePersistentAll(PersistenceManager pm, List<?> pcInstances) {
    Transaction tx = pm.currentTransaction();
    tx.begin();
    try {
      pm.makePersistentAll(pcInstances);
      if (debug) logger.debug("inserted " + pcInstances);
      tx.commit();
    } finally {
      if (tx.isActive()) {
        tx.rollback();
      }
    }
  }

  /**
   * Returns a persistent company model instance for the given bean name.
   *
   * @param clazz class of the returned instance
   * @param beanName the bean name.
   * @return the persistent company model instance.
   */
  protected <T> T getPersistentCompanyModelInstance(Class<T> clazz, String beanName) {
    return beanName == null
        ? null
        : getBean(getCompanyModelReaderForPersistentInstances(), clazz, beanName);
  }

  protected <T> T getPersistentCompanyModelInstance(
      PersistenceManager pm, Class<T> clazz, String beanName) {
    T instance = null;
    if (beanName != null) {
      instance = getBean(getCompanyModelReaderForPersistentInstances(), clazz, beanName);
      Object id = JDOHelper.getObjectId(instance);
      instance = (T) pm.getObjectById(id, false);
    }
    return instance;
  }

  /**
   * Returns a transient company model instance for the given bean name.
   *
   * @param clazz the class of teh returned instance
   * @param beanName the bean name.
   * @return the transient company model instance.
   */
  protected <T> T getTransientCompanyModelInstance(Class<T> clazz, String beanName) {
    return beanName == null
        ? null
        : getBean(getCompanyModelReaderForTransientInstances(), clazz, beanName);
  }

  /**
   * Returns a list of persistent company model instances for beans names in the given argument.
   *
   * @param elementType the element type of the returned list
   * @param beanNames the bean names of company model instances.
   * @return the list of persistent company model instances.
   */
  protected <T> List<T> getPersistentCompanyModelInstancesAsList(
      Class<T> elementType, String... beanNames) {
    List<T> result = new ArrayList<>(beanNames.length);
    for (String beanName : beanNames) {
      result.add(getPersistentCompanyModelInstance(elementType, beanName));
    }
    return result;
  }

  protected <T> List<T> getPersistentCompanyModelInstancesAsList(
      PersistenceManager pm, Class<T> elementType, String... beanNames) {
    List<T> result = new ArrayList<>(beanNames.length);
    for (String beanName : beanNames) {
      result.add(getPersistentCompanyModelInstance(pm, elementType, beanName));
    }
    return result;
  }

  /**
   * Returns a list of transient company model instances for beans names in the given argument.
   *
   * @param elementType the element type of the returned list
   * @param beanNames the bean names of company model instances.
   * @return the list of transient company model instances.
   */
  protected <T> List<T> getTransientCompanyModelInstancesAsList(
      Class<T> elementType, String... beanNames) {
    List<T> result = new ArrayList<>(beanNames.length);
    for (String beanName : beanNames) {
      result.add(getTransientCompanyModelInstance(elementType, beanName));
    }
    return result;
  }

  /**
   * Returns a persistent mylib instance for the given bean name.
   *
   * @param beanName the bean name.
   * @return the persistent mylib instance.
   */
  protected Object getPersistentMylibInstance(String beanName) {
    return beanName == null ? null : getBean(getMylibReaderForPersistentInstances(), beanName);
  }

  /**
   * Returns a transient mylib instance for the given bean name.
   *
   * @param beanName the bean name.
   * @return the transient mylib instance.
   */
  protected Object getTransientMylibInstance(String beanName) {
    return beanName == null ? null : getBean(getMylibReaderForTransientInstances(), beanName);
  }

  /**
   * Returns an array of persistent mylib instances for beans names in the given argument.
   *
   * @param beanNames the bean names of mylib instances.
   * @return the array of persistent mylib instances.
   */
  protected Object[] getPersistentMylibInstances(String... beanNames) {
    Object[] result = new Object[beanNames.length];
    for (int i = 0; i < beanNames.length; i++) {
      result[i] = getPersistentMylibInstance(beanNames[i]);
    }
    return result;
  }

  /**
   * Returns an array of transient mylib instances for beans names in the given argument.
   *
   * @param beanNames the bean names of mylib instances.
   * @return the array of transient mylib instances.
   */
  protected Object[] getTransientMylibInstances(String... beanNames) {
    Object[] result = new Object[beanNames.length];
    for (int i = 0; i < beanNames.length; i++) {
      result[i] = getTransientMylibInstance(beanNames[i]);
    }
    return result;
  }

  /**
   * Returns a list of persistent mylib instances for beans names in the given argument.
   *
   * @param beanNames the bean names of mylib instances.
   * @return the list of persistent mylib instances.
   */
  protected List<Object> getPersistentMylibInstancesAsList(String... beanNames) {
    return Arrays.asList(getPersistentMylibInstances(beanNames));
  }

  /**
   * Returns a list of transient mylib instances for beans names in the given argument.
   *
   * @param beanNames the bean names of mylib instances.
   * @return the list of transient mylib instances.
   */
  protected List<Object> getTransientMylibInstancesAsList(String... beanNames) {
    return Arrays.asList(getTransientMylibInstances(beanNames));
  }

  // PrimitiveTypes helper methods (creation and query)

  /**
   * @param pm the PersistenceManager
   */
  public void loadAndPersistPrimitiveTypes(PersistenceManager pm) {
    insertPrimitiveTypes(pm);
  }

  /**
   * @param pm the PersistenceManager
   */
  protected void insertPrimitiveTypes(PersistenceManager pm) {
    boolean bFlag = false;
    String strValue = "";
    char charValue = '\u0000';
    int numInsert = 10;

    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      for (int i = 1; i <= numInsert; i++) {
        if (i % 2 == 1) {
          bFlag = true;
          strValue = "Odd" + i;
          charValue = 'O';
        } else {
          bFlag = false;
          strValue = "Even" + i;
          charValue = 'E';
        }
        PrimitiveTypes primitiveObject =
            new PrimitiveTypes(
                i,
                bFlag,
                Boolean.valueOf(bFlag),
                (byte) i,
                Byte.valueOf((byte) i),
                (short) i,
                Short.valueOf((short) i),
                i,
                Integer.valueOf(i),
                i,
                Long.valueOf(i),
                i,
                Float.valueOf(i),
                i,
                Double.valueOf(i),
                charValue,
                Character.valueOf(charValue),
                Calendar.getInstance().getTime(),
                strValue,
                new BigDecimal(String.valueOf(i)),
                new BigInteger(String.valueOf(i)),
                Long.valueOf(i));
        pm.makePersistent(primitiveObject);
      }
      tx.commit();
      tx = null;
      if (debug) logger.debug("Total objects inserted : " + numInsert);
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }

  /**
   * Creates and executes a PrimitiveTypes query with the specified filter. The method checks
   * whether the query returns the expected result.
   *
   * @param filter the filter
   * @param pm the PersistenceManager
   * @param expected expected
   * @param assertion assertion
   */
  protected void runSimplePrimitiveTypesQuery(
      String filter, PersistenceManager pm, Collection<PrimitiveTypes> expected, String assertion) {
    Query<PrimitiveTypes> q = pm.newQuery(PrimitiveTypes.class);
    q.setFilter(filter);
    List<PrimitiveTypes> results = q.executeList();
    if (debug) logger.debug("execute '" + filter + "' returns " + results.size() + " instance(s)");
    checkQueryResultWithoutOrder(assertion, filter, results, expected);
  }

  /**
   * Creates and executes a PrimitiveTypes query with the specified filter, parameter declarations
   * and parameter values. The method checks whether the query returns the expected result.
   *
   * @param filter the filter
   * @param paramDecl the parameter declaration
   * @param paramValue the parameter values
   * @param pm the PersistenceManager
   * @param expected expected
   * @param assertion assertion
   */
  protected void runParameterPrimitiveTypesQuery(
      String filter,
      String paramDecl,
      Object paramValue,
      PersistenceManager pm,
      Collection<PrimitiveTypes> expected,
      String assertion) {
    Query<PrimitiveTypes> q = pm.newQuery(PrimitiveTypes.class);
    q.setFilter(filter);
    q.declareParameters(paramDecl);
    q.setParameters(paramValue);
    List<PrimitiveTypes> results = q.executeList();
    if (debug)
      logger.debug(
          "execute '"
              + filter
              + "' with param '"
              + paramValue
              + "' returns "
              + results.size()
              + " instance(s)");
    checkQueryResultWithoutOrder(assertion, filter, results, expected);
  }

  // Helper methods to check query result

  /**
   * Verify that expected equals result, including the order of the elements. If not equal, fail the
   * test. If there is a filter != null, do not use this method. Use the method of the same name
   * that takes a String as the second argument.
   *
   * @param assertion assertion
   * @param result the result
   * @param expected the expected
   */
  protected void checkQueryResultWithOrder(String assertion, Object result, Object expected) {
    if (!equals(result, expected)) {
      queryFailed(assertion, "null", result, expected);
    }
  }

  /**
   * Verify that expected equals result, ignoring the order of the elements. If not equal, fail the
   * test. If there is a filter != null, do not use this method. Use the method of the same name
   * that takes a String as the second argument.
   *
   * @param assertion assertion
   * @param result the result
   * @param expected the expected
   */
  protected void checkQueryResultWithoutOrder(String assertion, Object result, Object expected) {
    // We need to explicitly check on collections passed as parameters
    // because equals(Object, Object) checks on lists and afterwards
    // on collections. This ensures, lists are compared without order
    // for queries without an ordering specification.
    if (result instanceof Collection && expected instanceof Collection) {
      if (!equalsCollection((Collection<?>) result, (Collection<?>) expected)) {
        queryFailed(assertion, "null", result, expected);
      }
    } else {
      if (!equals(result, expected)) {
        queryFailed(assertion, "null", result, expected);
      }
    }
  }

  private void queryFailed(String assertion, String query, Object result, Object expected) {
    String lf = System.getProperty("line.separator");
    result = ConversionHelper.convertObjectArrayElements(result);
    expected = ConversionHelper.convertObjectArrayElements(expected);
    fail(
        assertion,
        "Wrong query result: "
            + lf
            + "query: "
            + query
            + lf
            + "expected: "
            + expected.getClass().getName()
            + " of size "
            + size(expected)
            + lf
            + expected
            + lf
            + "got:      "
            + result.getClass().getName()
            + " of size "
            + size(result)
            + lf
            + result);
  }

  /**
   * @param assertion assertion
   * @param query the query
   * @param result the result
   * @param expected expected
   */
  protected void checkQueryResultWithOrder(
      String assertion, String query, Object result, Object expected) {
    if (!equals(result, expected)) {
      queryFailed(assertion, query, result, expected);
    }
  }

  /**
   * @param assertion assertion
   * @param query the query
   * @param result the result
   * @param expected expected
   */
  protected void checkQueryResultWithoutOrder(
      String assertion, String query, Object result, Object expected) {
    // We need to explicitly check on collections passed as parameters
    // because equals(Object, Object) checks on lists and afterwards
    // on collections. This ensures, lists are compared without order
    // for queries without an ordering specification.
    if (result instanceof Collection && expected instanceof Collection) {
      if (!equalsCollection((Collection<?>) result, (Collection<?>) expected)) {
        queryFailed(assertion, query, result, expected);
      }
    } else {
      if (!equals(result, expected)) {
        queryFailed(assertion, query, result, expected);
      }
    }
  }

  /**
   * Returns <code>true</code> if <code>o1</code> and <code>o2</code> equal. This method is capable
   * to compare object arrays, collections of object arrays, maps of object arrays. This method
   * implements a narrowing in case of floating point values. In case of big decimals it calls
   * {@link BigDecimal#compareTo(java.math.BigDecimal)}. It allows <code>o1</code> and/or <code>o2
   * </code> to be <code>null</code>.
   *
   * @param o1 the first object
   * @param o2 the second object
   * @return <code>true</code> if <code>o1</code> and <code>o2</code> equal.
   */
  protected boolean equals(Object o1, Object o2) {
    boolean result;
    if (o1 == o2) {
      result = true;
    } else if ((o1 instanceof Object[]) && (o2 instanceof Object[])) {
      result = equalsObjectArray((Object[]) o1, (Object[]) o2);
    } else if ((o1 instanceof List) && (o2 instanceof List)) {
      result = equalsList((List<?>) o1, (List<?>) o2);
    } else if ((o1 instanceof Collection) && (o2 instanceof Collection)) {
      result = equalsCollection((Collection<?>) o1, (Collection<?>) o2);
    } else if ((o1 instanceof Map) && (o2 instanceof Map)) {
      result = equalsMap((Map<?, ?>) o1, (Map<?, ?>) o2);
    } else if ((o1 instanceof Float) && (o2 instanceof Float)) {
      result = closeEnough(((Float) o1).floatValue(), ((Float) o2).floatValue());
    } else if ((o1 instanceof Double) && (o2 instanceof Double)) {
      result = closeEnough(((Double) o1).floatValue(), ((Double) o2).floatValue());
    } else if ((o1 instanceof BigDecimal) && (o2 instanceof BigDecimal)) {
      result = ((BigDecimal) o1).compareTo((BigDecimal) o2) == 0;
    } else if (o1 != null) {
      result = o1.equals(o2);
    } else {
      // Due to the first if and due to the last if, we have:
      // o1 == null && o2 != null
      result = false;
    }
    return result;
  }

  /**
   * Returns <code>true</code> if <code>o1</code> and <code>o2</code> equal. This method iterates
   * over both object arrays and calls {@link QueryTest#equals(Object, Object)} passing
   * corresponding instances. {@link QueryTest#equals(Object, Object)} is called rather than {@link
   * Object#equals(java.lang.Object)} because object arrays having equal elements cannot be compared
   * calling {@link Object#equals(java.lang.Object)}. This method does not allow <code>o1</code> and
   * <code>o2</code> to be <code>null</code> both.
   *
   * @param o1 the first object array
   * @param o2 the second object array
   * @return <code>true</code> if <code>o1</code> and <code>o2</code> equal.
   */
  protected boolean equalsObjectArray(Object[] o1, Object[] o2) {
    boolean result = true;
    if (o1 != o2) {
      if (o1.length != o2.length) {
        result = false;
      } else {
        for (int i = 0; i < o1.length; i++) {
          if (!equals(o1[i], o2[i])) {
            result = false;
            break;
          }
        }
      }
    }
    return result;
  }

  /**
   * Returns <code>true</code> if <code>o1</code> and <code>o2</code> equal. This method iterates
   * both lists and calls {@link QueryTest#equals(Object, Object)} on corresponding elements. {@link
   * QueryTest#equals(Object, Object)} is called rather than {@link Object#equals(java.lang.Object)}
   * because object arrays having equal elements cannot be compared calling {@link
   * Object#equals(java.lang.Object)}. This method does not allow <code>o1</code> and <code>o2
   * </code> to be <code>null</code> both.
   *
   * @param o1 the first list
   * @param o2 the second list
   * @return <code>true</code> if <code>o1</code> and <code>o2</code> equal.
   */
  protected <S, T> boolean equalsList(List<S> o1, List<T> o2) {
    boolean result = true;
    if (o1 != o2) {
      if (o1.size() != o2.size()) {
        result = false;
      } else {
        Iterator<S> i = o1.iterator();
        Iterator<T> ii = o2.iterator();
        while (i.hasNext()) {
          Object firstObject = i.next();
          Object secondObject = ii.next();
          if (!equals(firstObject, secondObject)) {
            result = false;
            break;
          }
        }
      }
    }
    return result;
  }

  /**
   * Returns <code>true</code> if <code>o1</code> and <code>o2</code> equal. This method iterates
   * over the first collection and checks if each instance is contained in the second collection by
   * calling remove(Collection, Object). This guarantees that the cardinality of each instance in
   * the first collection matches the cardinality of each instance in the second collection. This
   * method does not allow <code>o1</code> and <code>o2</code> to be <code>null</code> both.
   *
   * @param o1 the first collection
   * @param o2 the second collection
   * @return <code>true</code> if <code>o1</code> and <code>o2</code> equal.
   */
  protected <S, T> boolean equalsCollection(Collection<S> o1, Collection<T> o2) {
    // make a copy of o2 so we can destroy it
    Collection<T> o2copy = new ArrayList<>();
    for (T t : o2) {
      o2copy.add(t);
    }
    boolean result = true;
    if (o1 != o2) {
      if (o1.size() != o2.size()) {
        result = false;
      } else {
        for (S oo1 : o1) {
          if (!remove(o2copy, oo1)) {
            result = false;
            break;
          }
        }
      }
    }
    return result;
  }

  /**
   * Returns <code>true</code> if <code>o1</code> and <code>o2</code> equal. This method checks if
   * the key sets and the value sets of both maps equal calling equalsCollection(Collection,
   * Collection). This method does not allow <code>o1</code> and <code>o2</code> to be <code>null
   * </code> both.
   *
   * @param o1 the first map
   * @param o2 the second map
   * @return <code>true</code> if <code>o1</code> and <code>o2</code> equal.
   */
  protected <K1, V1, K2, V2> boolean equalsMap(Map<K1, V1> o1, Map<K2, V2> o2) {
    boolean result = true;
    if (o1 != o2) {
      if (o1.size() != o2.size()) {
        result = false;
      } else {
        for (Map.Entry<K1, V1> entry : o1.entrySet()) {
          K1 key = entry.getKey();
          V1 value = entry.getValue();
          V2 value2 = o2.get(key);
          if (!equals(value, value2)) {
            result = false;
            break;
          }
        }
      }
    }
    return result;
  }

  /**
   * Returns <code>true</code> if <code>o</code> is contained in the given collection. This method
   * iterates the given collection and calls {@link QueryTest#equals(Object, Object)} for each
   * instance. {@link QueryTest#equals(Object, Object)} is called rather than {@link
   * Object#equals(java.lang.Object)} because object arrays having equal elements cannot be compared
   * calling {@link Object#equals(java.lang.Object)}.
   *
   * @param col the collection
   * @param o the object
   * @return <code>true</code> if <code>o</code> is contained in the given collection.
   */
  private boolean contains(Collection<Object> col, Object o) {
    for (Object value : col) {
      if (equals(o, value)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Returns <code>true</code> if <code>o</code> is contained in the given collection and was
   * removed. This method iterates the given collection and calls {@link QueryTest#equals(Object,
   * Object)} for each instance. {@link QueryTest#equals(Object, Object)} is called rather than
   * {@link Object#equals(java.lang.Object)} because object arrays having equal elements cannot be
   * compared calling {@link Object#equals(java.lang.Object)}.
   *
   * @param col the collection
   * @param o the object
   * @return <code>true</code> if <code>o</code> is contained in the given collection and was
   *     removed.
   */
  private <T> boolean remove(Collection<T> col, Object o) {
    for (Iterator<T> i = col.iterator(); i.hasNext(); ) {
      if (equals(o, i.next())) {
        i.remove();
        return true;
      }
    }
    return false;
  }

  /**
   * Returns <code>true</code> if the specified float values are close enough to be considered to be
   * equal for a deep equals comparison. Floating point values are not exact, so comparing them
   * using <code>==</code> might not return useful results. This method checks that both double
   * values are within some percent of each other.
   *
   * @param d1 one double to be tested for close enough
   * @param d2 the other double to be tested for close enough
   * @return <code>true</code> if the specified values are close enough.
   */
  public boolean closeEnough(double d1, double d2) {
    if (d1 == d2) return true;

    double diff = Math.abs(d1 - d2);
    return diff < Math.abs((d1 + d2) * EqualityHelper.DOUBLE_EPSILON);
  }

  /**
   * Returns <code>true</code> if the specified float values are close enough to be considered to be
   * equal for a deep equals comparison. Floating point values are not exact, so comparing them
   * using <code>==</code> might not return useful results. This method checks that both float
   * values are within some percent of each other.
   *
   * @param f1 one float to be tested for close enough
   * @param f2 the other float to be tested for close enough
   * @return <code>true</code> if the specified values are close enough.
   */
  public boolean closeEnough(float f1, float f2) {
    if (f1 == f2) return true;

    float diff = Math.abs(f1 - f2);
    return diff < Math.abs((f1 + f2) * EqualityHelper.FLOAT_EPSILON);
  }

  /**
   * Returns the size of the object. If it is a multivalued object (Collection, Map, or array)
   * return the number of elements. If not, return 1.
   *
   * @param o the object
   * @return size
   */
  protected int size(Object o) {
    if (o instanceof Collection) {
      return ((Collection<?>) o).size();
    }
    if (o instanceof Object[]) {
      return ((Object[]) o).length;
    }
    if (o instanceof Map) {
      return ((Map<?, ?>) o).size();
    }
    return 1;
  }

  // Debugging helper methods

  /**
   * @param results the results
   * @param expected expected values
   */
  protected void printOutput(Object results, Collection<?> expected) {
    if (!debug) return;

    Iterator<?> iter = null;
    PCPoint pcp = null;
    if (results == null) {
      logger.debug("Query returns null");
      return;
    }
    if (!(results instanceof Collection)) {
      logger.debug(
          "Query result is not a collection: "
              + (results == null ? "null" : results.getClass().getName()));
    }
    logger.debug("Retrived Objects are:");
    iter = ((Collection<?>) results).iterator();
    while (iter.hasNext()) {
      pcp = (PCPoint) iter.next();
      logger.debug("X = " + pcp.getX() + "\tY = " + pcp.getY());
    }

    logger.debug("Expected Objects are:");
    iter = expected.iterator();
    while (iter.hasNext()) {
      pcp = (PCPoint) iter.next();
      logger.debug("X = " + pcp.getX() + "\tY = " + pcp.getY());
    }
  }

  // compile query methods

  /**
   * Compiles the given query element holder instance as a JDO API query. Argument <code>positive
   * </code> determines if the compilation is supposed to succeed or to fail. If <code>true</code>
   * and the compilation fails, then the test case fails prompting argument <code>assertion</code>.
   * If <code>false</code> and the compilation succeeds, then the test case fails prompting argument
   * <code>assertion</code>. Otherwise the test case succeeds.
   *
   * @param assertion the assertion to prompt if the test case fails.
   * @param queryElementHolder the query to execute.
   * @param positive determines if the compilation is supposed to succeed or to fail.
   */
  protected void compileAPIQuery(
      String assertion,
      PersistenceManager pm,
      QueryElementHolder<?> queryElementHolder,
      boolean positive) {
    if (logger.isDebugEnabled()) {
      logger.debug("Compiling API query: " + queryElementHolder);
    }
    compile(assertion, pm, queryElementHolder, false, queryElementHolder.toString(), positive);
  }

  /**
   * Compiles the given query element holder instance as a JDO single string query. Argument <code>
   * positive</code> determines if the compilation is supposed to succeed or to fail. If <code>true
   * </code> and the compilation fails, then the test case fails prompting argument <code>assertion
   * </code>. If <code>false</code> and the compilation succeeds, then the test case fails prompting
   * argument <code>assertion</code>. Otherwise the test case succeeds.
   *
   * @param assertion the assertion to prompt if the test case fails.
   * @param queryElementHolder the query to execute.
   * @param positive determines if the compilation is supposed to succeed or to fail.
   */
  protected void compileSingleStringQuery(
      String assertion,
      PersistenceManager pm,
      QueryElementHolder<?> queryElementHolder,
      boolean positive) {
    if (logger.isDebugEnabled())
      logger.debug("Compiling single string query: " + queryElementHolder);
    compile(assertion, pm, queryElementHolder, true, queryElementHolder.toString(), positive);
  }

  /**
   * Compiles the given single string query. Argument <code>positive</code> determines if the
   * compilation is supposed to succeed or to fail. If <code>true</code> and the compilation fails,
   * then the test case fails prompting argument <code>assertion</code>. If <code>false</code> and
   * the compilation succeeds, then the test case fails prompting argument <code>assertion</code>.
   * Otherwise the test case succeeds.
   *
   * @param assertion the assertion to prompt if the test case fails.
   * @param singleStringQuery the single string query
   * @param positive determines if the compilation is supposed to succeed or to fail.
   */
  protected void compileSingleStringQuery(
      String assertion, PersistenceManager pm, String singleStringQuery, boolean positive) {
    if (logger.isDebugEnabled())
      logger.debug("Compiling single string query: " + singleStringQuery);
    compile(assertion, pm, null, true, singleStringQuery, positive);
  }

  /**
   * Compiles the given query element holder instance as a JDO API query or single string query,
   * depending on argument <code>asSingleString</code>. Argument <code>singleStringQuery</code> is
   * used to support queries which cannot be expressed as query element holder instances. That
   * argument is ignored if argument <code>queryElementHolder</code> is set. Argument <code>positive
   * </code> determines if the compilation is supposed to succeed or to fail. If <code>true</code>
   * and the compilation fails, then the test case fails prompting argument <code>assertion</code>.
   * If <code>false</code> and the compilation succeeds, then the test case fails prompting argument
   * <code>assertion</code>. Otherwise the test case succeeds.
   *
   * @param assertion the assertion to prompt if the test case fails.
   * @param queryElementHolder the query to compile.
   * @param asSingleString determines if the query specified by <code>queryElementHolder</code> is
   *     compiled as single string query or as API query.
   * @param singleStringQuery the query to compile as a JDO single string query if there is no query
   *     element holder.
   * @param positive determines if the compilation is supposed to succeed or to fail.
   */
  private void compile(
      String assertion,
      PersistenceManager pm,
      QueryElementHolder<?> queryElementHolder,
      boolean asSingleString,
      String singleStringQuery,
      boolean positive) {
    try {
      Query<?> query;
      if (queryElementHolder != null) {
        if (asSingleString) {
          query = queryElementHolder.getSingleStringQuery(pm);
        } else {
          query = queryElementHolder.getAPIQuery(pm);
        }
      } else {
        query = pm.newQuery(singleStringQuery);
      }
      compile(assertion, pm, query, singleStringQuery, positive);
    } catch (JDOUserException e) {
      // This exception handler considers a JDOUserException
      // to be thrown in newQuery methods.
      // A JDOUserException may be expected in case of negative tests.
      if (positive) {
        fail(
            assertion
                + "Query '"
                + queryElementHolder
                + "' must be compilable. The exception message is: "
                + e.getMessage());
      }
    }
  }

  /**
   * Compiles the given query instance. Argument <code>positive</code> determines if the compilation
   * is supposed to succeed or to fail. If <code>true</code> and the compilation fails, then the
   * test case fails prompting arguments <code>assertion</code> and <code>queryText</code>. If
   * <code>false</code> and the compilation succeeds, then the test case fails prompting argument
   * <code>assertion</code> and <code>queryText</code>. Otherwise the test case succeeds.
   *
   * @param assertion asserting text
   * @param query the query instance
   * @param queryText the text representation of the query
   * @param positive positive test
   */
  protected void compile(
      String assertion, PersistenceManager pm, Query<?> query, String queryText, boolean positive) {
    Transaction tx = pm.currentTransaction();
    tx.begin();
    try {
      query.compile();
      if (!positive) {
        fail(assertion, "Query compilation must throw JDOUserException: " + queryText);
      }
    } catch (JDOUserException e) {
      if (positive) {
        fail(
            assertion,
            "Query '"
                + queryText
                + "' must be compilable. The exception message is: "
                + e.getMessage());
      }
    } finally {
      if (tx.isActive()) {
        tx.rollback();
      }
    }
  }

  // execute query methods

  /**
   * Executes the given query element holder instance as a JDO API query. The result of that query
   * is compared against the given argument <code>expectedResult</code>. If the expected result does
   * not match the returned query result, then the test case fails prompting argument <code>
   * assertion</code>.
   *
   * @param assertion the assertion to prompt if the test case fails.
   * @param queryElementHolder the query to execute.
   * @param expectedResult the expected query result.
   */
  protected void executeAPIQuery(
      String assertion,
      PersistenceManager pm,
      QueryElementHolder<?> queryElementHolder,
      Object expectedResult) {
    if (logger.isDebugEnabled()) {
      logger.debug("Executing API query: " + queryElementHolder);
    }
    execute(assertion, pm, queryElementHolder, false, expectedResult);
  }

  /**
   * Executes the given query element holder instance as a JDO single string query. The result of
   * that query is compared against the given argument <code>expectedResult</code>. If the expected
   * result does not match the returned query result, then the test case fails prompting argument
   * <code>assertion</code>.
   *
   * @param assertion the assertion to prompt if the test case fails.
   * @param queryElementHolder the query to execute.
   * @param expectedResult the expected query result.
   */
  protected void executeSingleStringQuery(
      String assertion,
      PersistenceManager pm,
      QueryElementHolder<?> queryElementHolder,
      Object expectedResult) {
    if (logger.isDebugEnabled())
      logger.debug("Executing single string query: " + queryElementHolder);
    execute(assertion, pm, queryElementHolder, true, expectedResult);
  }

  /**
   * @param assertion assertion text
   * @param queryElementHolder query elements
   * @param expectedResult expected query result
   */
  protected void executeJDOQLTypedQuery(
      String assertion,
      PersistenceManager pm,
      QueryElementHolder<?> queryElementHolder,
      Object expectedResult) {
    executeJDOQLTypedQuery(assertion, pm, queryElementHolder, null, false, expectedResult);
  }

  /**
   * @param assertion assertion text
   * @param queryElementHolder query elements
   * @param resultClass result class object
   * @param resultClauseSpecified flag whether result clause is specified
   * @param expectedResult the expected query result
   */
  protected void executeJDOQLTypedQuery(
      String assertion,
      PersistenceManager pm,
      QueryElementHolder<?> queryElementHolder,
      Class<?> resultClass,
      boolean resultClauseSpecified,
      Object expectedResult) {
    String singleStringQuery = queryElementHolder.toString();
    Transaction tx = pm.currentTransaction();
    tx.begin();
    try {
      JDOQLTypedQuery<?> query = queryElementHolder.getJDOQLTypedQuery();
      if (query == null) {
        throw new JDOFatalInternalException("Missing JDOQLTyped instance");
      }
      Object result = null;
      try {
        Map<String, ?> paramValues = queryElementHolder.getParamValues();
        if (paramValues != null) {
          query.setParameters(paramValues);
        }

        if (resultClass != null) {
          if (queryElementHolder.isUnique()) {
            // result class specified and unique result
            result = query.executeResultUnique(resultClass);
          } else {
            // result class specified and list result
            result = query.executeResultList(resultClass);
          }
        } else if (resultClauseSpecified) {
          if (queryElementHolder.isUnique()) {
            // result class specified and unique result
            result = query.executeResultUnique();
          } else {
            // result class specified and list result
            result = query.executeResultList();
          }
        } else {
          if (queryElementHolder.isUnique()) {
            // no result class and unique result
            result = query.executeUnique();
          } else {
            // no result class and list result
            result = query.executeList();
          }
        }

        if (logger.isDebugEnabled()) {
          logger.debug("Query result: " + ConversionHelper.convertObjectArrayElements(result));
        }

        checkResult(
            assertion,
            singleStringQuery,
            queryElementHolder.hasOrdering(),
            result,
            expectedResult,
            true);
      } finally {
        query.close(result);
      }
    } catch (JDOUserException e) {
      String msg = "JDOUserException thrown while executing query:\n" + singleStringQuery;
      throw new JDOException(msg, e);
    } catch (JDOException e) {
      String msg = "JDOException thrown while executing query:\n" + singleStringQuery;
      throw new JDOException(msg, e);
    } finally {
      if (tx.isActive()) {
        tx.rollback();
      }
    }
  }

  /**
   * Converts the given query element holder instance to a JDO query instance, based on argument
   * <code>asSingleString</code>. Afterwards, delegates to method {@link QueryTest#execute(String,
   * PersistenceManager, Query, String, boolean, Object, Object, boolean)}.
   *
   * @param assertion the assertion to prompt if the test case fails.
   * @param queryElementHolder the query to execute.
   * @param asSingleString determines if the query is executed as single string query or as API
   *     query.
   * @param expectedResult the expected query result.
   * @return the query result
   */
  private Object execute(
      String assertion,
      PersistenceManager pm,
      QueryElementHolder<?> queryElementHolder,
      boolean asSingleString,
      Object expectedResult) {
    Query<?> query =
        asSingleString
            ? queryElementHolder.getSingleStringQuery(pm)
            : queryElementHolder.getAPIQuery(pm);
    Object result =
        execute(
            assertion,
            pm,
            query,
            queryElementHolder.toString(),
            queryElementHolder.hasOrdering(),
            queryElementHolder.getParamValues(),
            expectedResult,
            true);
    return result;
  }

  /**
   * Executes the given query instance delegating to execute(String, Query, String, boolean, Object,
   * Object, boolean). Logs argument <code>singleStringQuery</code> if debug logging is enabled.
   *
   * @param assertion the assertion to prompt if the test case fails.
   * @param query the query to execute.
   * @param singleStringQuery the single string representation of the query. This parameter is only
   *     used as part of the falure message.
   * @param hasOrdering indicates if the query has an ordering clause.
   * @param parameters the parmaters of the query.
   * @param expectedResult the expected query result.
   * @param positive indicates if query execution is supposed to fail
   * @return the query result
   */
  protected Object executeJDOQuery(
      String assertion,
      PersistenceManager pm,
      Query<?> query,
      String singleStringQuery,
      boolean hasOrdering,
      Object[] parameters,
      Object expectedResult,
      boolean positive) {
    if (logger.isDebugEnabled()) {
      logger.debug("Executing JDO query: " + singleStringQuery);
    }
    return execute(
        assertion, pm, query, singleStringQuery, hasOrdering, parameters, expectedResult, positive);
  }

  /**
   * Executes the given SQL string as a JDO SQL query. The result of that query is compared against
   * the given argument <code>expectedResult</code>. If the expected result does not match the
   * returned query result, then the test case fails prompting argument <code>assertion</code>.
   * Argument <code>unique</code> indicates, if the query is supposed to return a single result.
   * Argument <code>sql</code> may contain the substring <code>"{0}"</code>. All occurences of this
   * substring are replaced by the value of PMF property <code>"javax.jdo.mapping.Schema"</code>.
   *
   * @param assertion the assertion to prompt if the test case fails.
   * @param sql the SQL string.
   * @param candidateClass the candidate class.
   * @param resultClass the result class.
   * @param positive positive option
   * @param parameters the parameters of the query.
   * @param expectedResult the expected query result.
   * @param unique indicates, if the query is supposed to return a single result.
   */
  @SuppressWarnings("unchecked")
  protected <T> void executeSQLQuery(
      String assertion,
      PersistenceManager pm,
      String sql,
      Class<T> candidateClass,
      Class<?> resultClass,
      boolean positive,
      Object parameters,
      Object expectedResult,
      boolean unique) {
    String schema = getPMFProperty("javax.jdo.mapping.Schema");
    sql = MessageFormat.format(sql, new Object[] {schema});
    if (logger.isDebugEnabled()) logger.debug("Executing SQL query: " + sql);
    Query<T> query = pm.newQuery("javax.jdo.query.SQL", sql);
    if (unique) {
      query.setUnique(unique);
    }
    if (candidateClass != null) {
      query.setClass(candidateClass);
    }
    if (resultClass != null) {
      query.setResultClass(resultClass);
    }
    execute(assertion, pm, query, sql, false, parameters, expectedResult, positive);
  }

  /**
   * Executes the given query instance. If argument <code>parameters</code> is an object array, then
   * it is passed as an argument to the method {@link Query#executeWithArray(java.lang.Object[])}.
   * If argument <code>parameters</code> is a map, then it is passed as an argument to the method
   * {@link Query#executeWithMap(java.util.Map)}. If argument <code>parameters</code> is a list,
   * then the list elements are passed as arguments to the execute methods taking actual parameter
   * values. If argument <code>parameters</code> is <code>null</code>, then method {@link
   * Query#execute()} is called on the given query instance instead.
   *
   * <p>The result of query execution is compared against the argument <code>expectedResult</code>.
   * If the two values differ, then this method throws an {@link
   * junit.framework.AssertionFailedError} and the calling test case fails prompting argument <code>
   * assertion</code>.
   *
   * <p>If argument <code>positive</code> is <code>false</code>, then the test case invoking this
   * method is considered to be a negative test case. Then, query execution is expected to throw a
   * {@link JDOUserException}. If query execution succeeds in this case, then this method throws an
   * {@link junit.framework.AssertionFailedError} and the calling test case fails prompting argument
   * <code>assertion</code>.
   *
   * <p>
   *
   * @param assertion the assertion to prompt if the test case fails.
   * @param query the query to execute.
   * @param singleStringQuery the single string representation of the query. This parameter is only
   *     used as part of the falure message.
   * @param hasOrdering indicates if the query has an ordering clause.
   * @param parameters the parmaters of the query.
   * @param expectedResult the expected query result.
   * @param positive indicates if query execution is supposed to fail
   * @return the query result
   */
  @SuppressWarnings("unchecked")
  private Object execute(
      String assertion,
      PersistenceManager pm,
      Query<?> query,
      String singleStringQuery,
      boolean hasOrdering,
      Object parameters,
      Object expectedResult,
      boolean positive) {
    Object result = null;
    Transaction tx = pm.currentTransaction();
    tx.begin();
    try {
      try {
        if (parameters == null) {
          result = query.execute();
        } else if (parameters instanceof Object[]) {
          result = query.executeWithArray((Object[]) parameters);
        } else if (parameters instanceof Map) {
          result = query.executeWithMap((Map<String, Object>) parameters);
        } else if (parameters instanceof List) {
          List<?> list = (List<?>) parameters;
          switch (list.size()) {
            case 1:
              result = query.execute(list.get(0));
              break;
            case 2:
              result = query.execute(list.get(0), list.get(1));
              break;
            case 3:
              result = query.execute(list.get(0), list.get(1), list.get(2));
              break;
            default:
              throw new IllegalArgumentException(
                  "Argument parameters is a list " + "and must have 1, 2, or 3 elements.");
          }
        } else {
          throw new IllegalArgumentException(
              "Argument parameters " + "must be instance of List, Map, Object[], or null.");
        }

        if (logger.isDebugEnabled()) {
          logger.debug("Query result: " + ConversionHelper.convertObjectArrayElements(result));
        }

        checkResult(assertion, singleStringQuery, hasOrdering, result, expectedResult, positive);

      } finally {
        query.close(result);
      }
    } catch (JDOUserException e) {
      if (positive) {
        String msg = "JDOUserException thrown while executing query:\n" + singleStringQuery;
        throw new JDOException(msg, e);
      }
    } catch (JDOException e) {
      String msg = "JDOException thrown while executing query:\n" + singleStringQuery;
      throw new JDOException(msg, e);
    } finally {
      if (tx.isActive()) {
        tx.rollback();
      }
    }
    return result;
  }

  /**
   * @param assertion the assertion to prompt if the test case fails.
   * @param singleStringQuery the single string representation of the query. This parameter is only
   *     used as part of the falure message.
   * @param hasOrdering indicates if the query has an ordering clause.
   * @param positive indicates if query execution is supposed to fail
   * @param result the query result.
   * @param expectedResult the expected query result.
   */
  private void checkResult(
      String assertion,
      String singleStringQuery,
      boolean hasOrdering,
      Object result,
      Object expectedResult,
      boolean positive) {
    if (positive) {
      if (hasOrdering) {
        checkQueryResultWithOrder(assertion, singleStringQuery, result, expectedResult);
      } else {
        checkQueryResultWithoutOrder(assertion, singleStringQuery, result, expectedResult);
      }
    } else {
      fail(assertion, "Query must throw JDOUserException: " + singleStringQuery);
    }
  }

  /**
   * Converts the given query element holder instance to a JDO query instance. Calls {@link
   * Query#deletePersistentAll()}, or {@link Query#deletePersistentAll(java.util.Map)}, or {@link
   * Query#deletePersistentAll(java.lang.Object[])} depending on the type of argument <code>
   * parameters</code>. If the number of deleted objects does not match <code>
   * expectedNrOfDeletedObjects</code>, then the test case fails prompting argument <code>assertion
   * </code>.
   *
   * @param assertion the assertion to prompt if the test case fails.
   * @param queryElementHolder the query to execute.
   * @param expectedNrOfDeletedObjects the expected number of deleted objects.
   */
  protected void deletePersistentAllByAPIQuery(
      String assertion,
      PersistenceManager pm,
      QueryElementHolder<?> queryElementHolder,
      long expectedNrOfDeletedObjects) {
    if (logger.isDebugEnabled()) {
      logger.debug("Deleting persistent by API query: " + queryElementHolder);
    }
    delete(assertion, pm, queryElementHolder, false, expectedNrOfDeletedObjects);
  }

  /**
   * Converts the given query element holder instance to a JDO query instance. Calls {@link
   * Query#deletePersistentAll()}, or {@link Query#deletePersistentAll(java.util.Map)}, or {@link
   * Query#deletePersistentAll(java.lang.Object[])} depending on the type of argument <code>
   * parameters</code>. If the number of deleted objects does not match <code>
   * expectedNrOfDeletedObjects</code>, then the test case fails prompting argument <code>assertion
   * </code>.
   *
   * @param assertion the assertion to prompt if the test case fails.
   * @param queryElementHolder the query to execute.
   * @param expectedNrOfDeletedObjects the expected number of deleted objects.
   */
  protected void deletePersistentAllBySingleStringQuery(
      String assertion,
      PersistenceManager pm,
      QueryElementHolder<?> queryElementHolder,
      long expectedNrOfDeletedObjects) {
    if (logger.isDebugEnabled()) {
      logger.debug("Deleting persistent by single string query: " + queryElementHolder);
    }
    delete(assertion, pm, queryElementHolder, true, expectedNrOfDeletedObjects);
  }

  /**
   * Converts the given query element holder instance to a
   * JDO query based on argument <code>asSingleString</code>.
   * Calls {@link Query#deletePersistentAll()}, or
   * {@link Query#deletePersistentAll(java.util.Map), or
   * {@link Query#deletePersistentAll(java.lang.Object[])
   * depending on the type of argument <code>parameters</code>.
   * If the number of deleted objects does not
   * match <code>expectedNrOfDeletedObjects</code>,
   * then the test case fails prompting argument <code>assertion</code>.
   * @param assertion the assertion to prompt if the test case fails.
   * @param queryElementHolder the query to execute.
   * @param asSingleString determines if the query is executed as
   * single string query or as API query.
   * @param parameters the parmaters of the query.
   * @param expectedNrOfDeletedObjects the expected number of deleted objects.
   */
  private void delete(
      String assertion,
      PersistenceManager pm,
      QueryElementHolder<?> queryElementHolder,
      boolean asSingleString,
      long expectedNrOfDeletedObjects) {
    Query<?> query =
        asSingleString
            ? queryElementHolder.getSingleStringQuery(pm)
            : queryElementHolder.getAPIQuery(pm);
    delete(
        assertion,
        pm,
        query,
        queryElementHolder.toString(),
        queryElementHolder.getParamValues(),
        expectedNrOfDeletedObjects);
    boolean positive = expectedNrOfDeletedObjects >= 0;
    if (positive) {
      execute(
          assertion,
          pm,
          queryElementHolder,
          asSingleString,
          queryElementHolder.isUnique() ? null : Collections.emptyList());
    }
  }

  /**
   * Calls {@link Query#deletePersistentAll()}, or
   * {@link Query#deletePersistentAll(java.util.Map), or
   * {@link Query#deletePersistentAll(java.lang.Object[])
   * depending on the type of argument <code>parameters</code>.
   * If the number of deleted objects does not
   * match <code>expectedNrOfDeletedObjects</code>,
   * then the test case fails prompting argument <code>assertion</code>.
   * Argument <code>singleStringQuery</code> is only used as part
   * of the failure message.
   * @param assertion the assertion to prompt if the test case fails.
   * @param query the query to execute.
   * @param singleStringQuery the single string representation of the query.
   * @param parameters the parmaters of the query.
   * @param expectedNrOfDeletedObjects the expected number of deleted objects.
   */
  @SuppressWarnings("unchecked")
  private void delete(
      String assertion,
      PersistenceManager pm,
      Query<?> query,
      String singleStringQuery,
      Object parameters,
      long expectedNrOfDeletedObjects) {
    boolean positive = expectedNrOfDeletedObjects >= 0;
    Transaction tx = pm.currentTransaction();
    tx.begin();
    try {
      try {
        long nr;
        if (parameters == null) {
          nr = query.deletePersistentAll();
        } else if (parameters instanceof Object[]) {
          nr = query.deletePersistentAll((Object[]) parameters);
        } else if (parameters instanceof Map) {
          nr = query.deletePersistentAll((Map<String, Object>) parameters);
        } else {
          throw new IllegalArgumentException(
              "Argument parameters " + "must be instance of Object[], Map, or null.");
        }
        if (logger.isDebugEnabled()) {
          logger.debug(nr + " objects deleted.");
        }

        if (positive) {
          if (nr != expectedNrOfDeletedObjects) {
            fail(
                assertion,
                "deletePersistentAll returned "
                    + nr
                    + ", expected is "
                    + expectedNrOfDeletedObjects
                    + ". Query: "
                    + singleStringQuery);
          }
        } else {
          fail(assertion, "deletePersistentAll must throw JDOUserException: " + singleStringQuery);
        }
      } finally {
        query.closeAll();
      }
      tx.commit();
    } catch (JDOUserException e) {
      if (positive) {
        String msg = "JDOUserException thrown while executing query:\n" + singleStringQuery;
        throw new JDOException(msg, e);
      }
    } catch (JDOException e) {
      String msg = "JDOException thrown while executing query:\n" + singleStringQuery;
      throw new JDOException(msg, e);
    } finally {
      if (tx.isActive()) {
        tx.rollback();
      }
    }
  }
}
