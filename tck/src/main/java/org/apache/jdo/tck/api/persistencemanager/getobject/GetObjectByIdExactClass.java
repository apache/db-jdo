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

package org.apache.jdo.tck.api.persistencemanager.getobject;

import java.util.Date;
import javax.jdo.Transaction;
import javax.jdo.JDOException;

import javax.jdo.identity.LongIdentity;

import org.apache.jdo.tck.util.BatchTestRunner;
import org.apache.jdo.tck.api.persistencemanager.PersistenceManagerTest;

import org.apache.jdo.tck.pc.singlefieldidentity.Person;
import org.apache.jdo.tck.pc.singlefieldidentity.Employee;
import org.apache.jdo.tck.pc.singlefieldidentity.PartTimeEmployee;
import org.apache.jdo.tck.pc.singlefieldidentity.FullTimeEmployee;

/**
 * <B>Title:</B> Get Object By Id <br>
 * <B>Keywords:</B> identity cache <br>
 * <B>Assertion ID:</B> A12.5.6-2. <br>
 * <B>Assertion Description: </B> If the validate flag is false, the user asserts that the instance
 * exists and the object id represents the exact class of the persistent instance (specifically not
 * a subclass or an interface). If the class is abstract, throw JDOUserException (we cannot
 * construct a hollow instance of an abstract class). If the class is actually not correct, an
 * exception with an error message will be thrown later if any field other than the identity field
 * id accessed.
 */
public class GetObjectByIdExactClass extends PersistenceManagerTest {

  /** */
  private static final String ASSERTION_FAILED = "Assertion A12.5.6-2 (GetObjectById) failed: ";

  /** */
  private LongIdentity oid;

  /** */
  private long id;

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(GetObjectByIdExactClass.class);
  }

  @Override
  public void localSetUp() {
    if (!runsWithApplicationIdentity()) {
      printNonApplicableIdentityType("GetObjectIdExactClass", APPLICATION_IDENTITY);
      return;
    }
    pm = getPM();
    pm.currentTransaction().begin();
    // create an instance of FullTimeEmployee.
    FullTimeEmployee instance = new FullTimeEmployee();
    instance.setPersonid(1000000L);
    instance.setFirstname("Full");
    instance.setLastname("Timer");
    instance.setBirthdate(new Date());
    pm.makePersistent(instance);
    pm.currentTransaction().commit();
    oid = (LongIdentity) pm.getObjectId(instance);
    id = oid.getKey();
    pm.close();
    pm = null;
    addTearDownClass(FullTimeEmployee.class);
  }

  /** */
  public void testAbstractSuperclassExact() {
    if (!runsWithApplicationIdentity()) return;
    Transaction tx = null;
    try {
      pm = getPM();
      pm.currentTransaction().begin();
      // create the oid
      Object abstractOid = new LongIdentity(Employee.class, id);
      pm.getObjectById(abstractOid, false);
      appendMessage(
          ASSERTION_FAILED + "getObjectById exact " + "for abstract superclass must fail.");
    } catch (JDOException ex) {
      // good catch
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
    failOnError();
  }

  /** */
  public void testAbstractSuperclassNotExact() {
    if (!runsWithApplicationIdentity()) return;
    Transaction tx = null;
    try {
      pm = getPM();
      pm.currentTransaction().begin();
      // create the oid
      Object abstractOid = new LongIdentity(Employee.class, id);
      Object abstractInstance = pm.getObjectById(abstractOid, true);
      if (abstractInstance.getClass() != FullTimeEmployee.class) {
        appendMessage(
            ASSERTION_FAILED
                + "getObjectById not exact "
                + "for abstract superclass returned wrong type "
                + abstractInstance.getClass().getName());
      }
    } catch (JDOException ex) {
      appendMessage(
          ASSERTION_FAILED
              + "getObjectById not exact "
              + "for abstract superclass threw exception "
              + ex.getMessage());
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
    failOnError();
  }

  /** */
  public void testConcreteSuperclassExact() {
    if (!runsWithApplicationIdentity()) return;
    Transaction tx = null;
    Person instance = null;
    try {
      pm = getPM();
      pm.currentTransaction().begin();
      // create the oid
      Object superclassOid = new LongIdentity(Person.class, id);
      instance = (Person) pm.getObjectById(superclassOid, false);
      if (instance.getClass() != Person.class) {
        appendMessage(
            ASSERTION_FAILED
                + "getObjectById exact for "
                + "concrete superclass should return "
                + "Person.class but returned wrong type "
                + instance.getClass().getName());
      }
    } catch (Exception ex) {
      appendMessage(
          ASSERTION_FAILED
              + "getObjectById exact for "
              + "concrete superclass must succeed before accessing database.");
    }
    try {
      instance.toString(); // accesses non-key fields
      appendMessage(
          ASSERTION_FAILED
              + "getObjectById exact for "
              + "concrete superclass must fail when accessing database.");
    } catch (JDOException ex) {
      // good catch
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
    failOnError();
  }

  /** */
  public void testConcreteSuperclassNotExact() {
    if (!runsWithApplicationIdentity()) return;
    Transaction tx = null;
    try {
      pm = getPM();
      pm.currentTransaction().begin();
      // create the oid
      Object superclassOid = new LongIdentity(Person.class, id);
      Object instance = pm.getObjectById(superclassOid, true);
      if (instance.getClass() != FullTimeEmployee.class) {
        appendMessage(
            ASSERTION_FAILED
                + "getObjectById not exact for "
                + "concrete superclass should return FullTimeEmployee "
                + "but returned wrong type "
                + instance.getClass().getName());
      }
    } catch (JDOException ex) {
      appendMessage(
          ASSERTION_FAILED
              + " getObjectById not exact for "
              + "concrete superclass must succeed but threw "
              + ex.getMessage());
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
    failOnError();
  }

  /** */
  public void testWrongClass() {
    if (!runsWithApplicationIdentity()) return;
    Transaction tx = null;
    try {
      pm = getPM();
      pm.currentTransaction().begin();
      // create the oid
      Object wrongOid = new LongIdentity(PartTimeEmployee.class, id);
      PartTimeEmployee wrongInstance = (PartTimeEmployee) pm.getObjectById(wrongOid, false);
      wrongInstance.toString();
      appendMessage(
          ASSERTION_FAILED
              + " getObjectById exact "
              + "for wrong class must throw JDOUserException.");
    } catch (JDOException ex) {
      // good catch
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
    failOnError();
  }

  /** */
  public void testRightClassNotExact() {
    if (!runsWithApplicationIdentity()) return;
    Transaction tx = null;
    try {
      pm = getPM();
      pm.currentTransaction().begin();
      // create the oid
      Object rightOid = new LongIdentity(FullTimeEmployee.class, id);
      FullTimeEmployee rightInstance = (FullTimeEmployee) pm.getObjectById(rightOid, true);
      rightInstance.toString();
    } catch (JDOException ex) {
      appendMessage(
          ASSERTION_FAILED + " getObjectById not exact " + "for right class must succeed.");
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
    failOnError();
  }

  /** */
  public void testRightClassExact() {
    if (!runsWithApplicationIdentity()) return;
    Transaction tx = null;
    try {
      pm = getPM();
      pm.currentTransaction().begin();
      // create the oid
      Object rightOid = new LongIdentity(FullTimeEmployee.class, id);
      FullTimeEmployee rightInstance = (FullTimeEmployee) pm.getObjectById(rightOid, false);
      rightInstance.toString();
    } catch (JDOException ex) {
      appendMessage(ASSERTION_FAILED + " getObjectById exact " + "for right class must succeed.");
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
    failOnError();
  }
}
