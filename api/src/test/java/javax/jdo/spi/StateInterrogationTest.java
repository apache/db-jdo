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

package javax.jdo.spi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.util.AbstractTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * This class tests the StateInterrogation interface. The test is in several parts:
 *
 * <ul>
 *   <li>Add and remove the StateInterrogation instance
 *   <li>test interrogatives to return the correct answer
 *   <li>test getters to return the correct answer
 *   <li>test makeDirty to return.
 * </ul>
 *
 * We use an mock implementation of StateInterrogation interface to log when calls are received and
 * to return the correct replies. We use a java.lang.reflect.Proxy for the getPersistenceManager
 * call because it's too much work to mock it.
 */
class StateInterrogationTest extends AbstractTest {

  private final JDOImplHelper implHelper = JDOImplHelper.getInstance();

  /** Creates a new instance of StateInterrogationTest */
  public StateInterrogationTest() {}

  @Test
  void testGetObjectIdNull() {
    Object id2 = JDOHelper.getObjectId(nbcpc2);
    assertNull(id2, "ObjectId should be null before addStateInterrogations");
    addStateInterrogations();
    Object id = JDOHelper.getObjectId(Boolean.TRUE);
    assertNull(id, "ObjectId should be null for non-pc instances");
  }

  @Test
  void testGetObjectId() {
    addStateInterrogations();
    Object id2 = JDOHelper.getObjectId(nbcpc2);
    assertNotNull(id2, "ObjectId should not be null");
    assertEquals(2, id2.hashCode(), "ObjectId should be 2");
    Object id20 = JDOHelper.getObjectId(nbcpc2);
    assertEquals(id2, id20, "ObjectIds from same object should be equal");
  }

  @Test
  void testRemoveStateInterrogation() {
    addStateInterrogations();
    Object id2 = JDOHelper.getObjectId(nbcpc2);
    assertNotNull(id2, "ObjectId should not be null");
    assertEquals(2, id2.hashCode(), "ObjectId should be 2");
    implHelper.removeStateInterrogation(si2);
    implHelper.removeStateInterrogation(si0);
    Object id21 = JDOHelper.getObjectId(nbcpc2);
    assertNull(id21, "ObjectId should be null after RemoveStateInterrogation");
    Object id1 = JDOHelper.getObjectId(nbcpc1);
    assertNotNull(id1, "ObjectId should not be null");
    assertEquals(1, id1.hashCode(), "ObjectId should be 1");
  }

  @Test
  void testGetTransactionalObjectIdNull() {
    Object id2 = JDOHelper.getTransactionalObjectId(nbcpc2);
    assertNull(id2, "TransactionalObjectId should be null before addStateInterrogations");
    addStateInterrogations();
    Object id = JDOHelper.getTransactionalObjectId(Boolean.TRUE);
    assertNull(id, "TransactionalObjectId should be null for non-pc instances");
  }

  @Test
  void testGetTransactionalObjectId() {
    addStateInterrogations();
    Object id2 = JDOHelper.getTransactionalObjectId(nbcpc2);
    assertNotNull(id2, "TransactionalObjectId should not be null");
    assertEquals(2, id2.hashCode(), "TransactionalObjectId should be 2");
    Object id20 = JDOHelper.getTransactionalObjectId(nbcpc2);
    assertEquals(id2, id20, "TransactionalObjectIds from same object should be equal");
  }

  @Test
  void testGetPersistenceManagerNull() {
    Object pm2 = JDOHelper.getPersistenceManager(nbcpc2);
    assertNull(pm2, "PersistenceManager should be null before addStateInterrogations");
    addStateInterrogations();
    Object pm = JDOHelper.getPersistenceManager(Boolean.TRUE);
    assertNull(pm, "PersistenceManager should be null for non-pc instances");
  }

  @Test
  void testGetPersistenceManager() {
    addStateInterrogations();
    Object pm2 = JDOHelper.getPersistenceManager(nbcpc2);
    assertNotNull(pm2, "PersistenceManager should not be null");
    assertEquals(2, pm2.hashCode(), "PersistenceManager should be 2");
    Object pm20 = JDOHelper.getPersistenceManager(nbcpc2);
    assertEquals(pm2, pm20, "PersistenceManagers from same object should be equal");
  }

  @Test
  void testGetVersionNull() {
    Object id2 = JDOHelper.getVersion(nbcpc2);
    assertNull(id2, "Version should be null before addStateInterrogations");
    addStateInterrogations();
    Object id = JDOHelper.getVersion(Boolean.TRUE);
    assertNull(id, "Version should be null for non-pc instances");
  }

  @Test
  void testGetVersion() {
    addStateInterrogations();
    Object id2 = JDOHelper.getVersion(nbcpc2);
    assertNotNull(id2, "Version should not be null");
    assertEquals(2, id2.hashCode(), "Version should be 2");
    Object id20 = JDOHelper.getVersion(nbcpc2);
    assertEquals(id2, id20, "Versions from same object should be equal");
  }

  @Test
  void testIsDeletedFalse() {
    assertFalse(
        JDOHelper.isDeleted(nbcpc2), "IsDeleted should be false before addStateInterrogations");
    addStateInterrogations();
    assertFalse(
        JDOHelper.isDeleted(Boolean.TRUE), "IsDeleted should be false for non-pc instances");
    implHelper.removeStateInterrogation(si2);
    assertFalse(
        JDOHelper.isDeleted(nbcpc2), "IsDeleted should be false after removeStateInterrogations");
  }

  @Test
  void testIsDeletedMine() {
    addStateInterrogations();
    assertTrue(JDOHelper.isDeleted(nbcpc1), "IsDeleted should be true for nbcpc1");
    assertFalse(JDOHelper.isDeleted(nbcpc2), "IsDeleted should be false for nbcpc2");
  }

  @Test
  void testIsDetachedFalse() {
    assertFalse(
        JDOHelper.isDetached(nbcpc2), "IsDetached should be false before addStateInterrogations");
    addStateInterrogations();
    assertFalse(
        JDOHelper.isDetached(Boolean.TRUE), "IsDetached should be false for non-pc instances");
    implHelper.removeStateInterrogation(si2);
    assertFalse(
        JDOHelper.isDetached(nbcpc2), "IsDetached should be false after removeStateInterrogations");
  }

  @Test
  void testIsDetachedMine() {
    addStateInterrogations();
    assertTrue(JDOHelper.isDetached(nbcpc1), "IsDetached should be true for nbcpc1");
    assertFalse(JDOHelper.isDetached(nbcpc2), "IsDetached should be false for nbcpc2");
  }

  @Test
  void testIsDirtyFalse() {
    assertFalse(JDOHelper.isDirty(nbcpc2), "IsDirty should be false before addStateInterrogations");
    addStateInterrogations();
    assertFalse(JDOHelper.isDirty(Boolean.TRUE), "IsDirty should be false for non-pc instances");
    implHelper.removeStateInterrogation(si2);
    nbcpc2.setDirty(true);
    assertFalse(
        JDOHelper.isDirty(nbcpc2), "IsDirty should be false after removeStateInterrogations");
    nbcpc2.setDirty(false);
  }

  @Test
  void testIsDirtyMine() {
    addStateInterrogations();
    nbcpc1.setDirty(true);
    assertTrue(JDOHelper.isDirty(nbcpc1), "IsDirty should be true for nbcpc1 after setDirty(true)");
    nbcpc1.setDirty(false);
    assertFalse(
        JDOHelper.isDirty(nbcpc1), "IsDirty should be false for nbcpc1 after setDirty(false)");
    assertFalse(JDOHelper.isDirty(nbcpc2), "IsDirty should be false for nbcpc2");
  }

  @Test
  void testIsNewFalse() {
    assertFalse(JDOHelper.isNew(nbcpc2), "IsNew should be false before addStateInterrogations");
    addStateInterrogations();
    assertFalse(JDOHelper.isNew(Boolean.TRUE), "IsNew should be false for non-pc instances");
    implHelper.removeStateInterrogation(si2);
    assertFalse(JDOHelper.isNew(nbcpc2), "IsNew should be false after removeStateInterrogations");
  }

  @Test
  void testIsNewMine() {
    addStateInterrogations();
    assertTrue(JDOHelper.isNew(nbcpc1), "IsNew should be true for nbcpc1");
    assertFalse(JDOHelper.isNew(nbcpc2), "IsNew should be false for nbcpc2");
  }

  @Test
  void testIsPersistentFalse() {
    assertFalse(
        JDOHelper.isPersistent(nbcpc2),
        "IsPersistent should be false before addStateInterrogations");
    addStateInterrogations();
    assertFalse(
        JDOHelper.isPersistent(Boolean.TRUE), "IsPersistent should be false for non-pc instances");
    implHelper.removeStateInterrogation(si2);
    assertFalse(
        JDOHelper.isPersistent(nbcpc2),
        "IsPersistent should be false after removeStateInterrogations");
  }

  @Test
  void testIsPersistentMine() {
    addStateInterrogations();
    assertTrue(JDOHelper.isPersistent(nbcpc1), "IsPersistent should be true for nbcpc1");
    assertFalse(JDOHelper.isPersistent(nbcpc2), "IsPersistent should be false for nbcpc2");
  }

  @Test
  void testIsTransactionalFalse() {
    assertFalse(
        JDOHelper.isTransactional(nbcpc2),
        "IsTransactional should be false before addStateInterrogations");
    addStateInterrogations();
    assertFalse(
        JDOHelper.isTransactional(Boolean.TRUE),
        "IsTransactional should be false for non-pc instances");
    implHelper.removeStateInterrogation(si2);
    assertFalse(
        JDOHelper.isTransactional(nbcpc2),
        "IsTransactional should be false after removeStateInterrogations");
  }

  @Test
  void testIsTransactionalMine() {
    addStateInterrogations();
    assertTrue(JDOHelper.isTransactional(nbcpc1), "IsTransactional should be true for nbcpc1");
    assertFalse(JDOHelper.isTransactional(nbcpc2), "IsTransactional should be false for nbcpc2");
  }

  @Test
  void testMakeDirtyFalse() {
    JDOHelper.makeDirty(nbcpc2, "");
    nbcpc2.setDirty(true);
    assertFalse(JDOHelper.isDirty(nbcpc2), "IsDirty should be false before addStateInterrogations");
    addStateInterrogations();
    implHelper.removeStateInterrogation(si2);
    nbcpc2.setDirty(false);
    JDOHelper.makeDirty(nbcpc2, "");
    assertFalse(
        JDOHelper.isDirty(nbcpc2), "IsDirty should be false after removeStateInterrogations");
  }

  @Test
  void testMakeDirtyMine() {
    addStateInterrogations();
    JDOHelper.makeDirty(nbcpc1, "");
    assertTrue(JDOHelper.isDirty(nbcpc1), "IsDirty should be true for nbcpc1");
    nbcpc1.setDirty(false);
    assertFalse(JDOHelper.isDirty(nbcpc1), "IsDirty should be false after setDirty(false)");
  }

  @AfterEach
  public void tearDown() {
    removeStateInterrogations();
  }

  public void addStateInterrogations() {
    implHelper.addStateInterrogation(si0);
    implHelper.addStateInterrogation(si1);
    implHelper.addStateInterrogation(si2);
  }

  public void removeStateInterrogations() {
    implHelper.removeStateInterrogation(si0);
    implHelper.removeStateInterrogation(si1);
    implHelper.removeStateInterrogation(si2);
  }

  /** The non-binary-compatible PersistenceManager class instances. */
  private static final PersistenceManager pmProxy0 =
      (PersistenceManager)
          Proxy.newProxyInstance(
              PersistenceManager.class.getClassLoader(),
              new Class[] {PersistenceManager.class},
              new InvocationHandlerImpl(0));

  /** The non-binary-compatible PersistenceManager class instances. */
  private static final PersistenceManager pmProxy1 =
      (PersistenceManager)
          Proxy.newProxyInstance(
              PersistenceManager.class.getClassLoader(),
              new Class[] {PersistenceManager.class},
              new InvocationHandlerImpl(1));

  /** The non-binary-compatible PersistenceManager class instances. */
  private static final PersistenceManager pmProxy2 =
      (PersistenceManager)
          Proxy.newProxyInstance(
              PersistenceManager.class.getClassLoader(),
              new Class[] {PersistenceManager.class},
              new InvocationHandlerImpl(2));

  /** The array of PersistenceManager proxies */
  static final PersistenceManager[] pmProxies = {pmProxy0, pmProxy1, pmProxy2};

  /** The array of NonBinaryCompatiblePersistenceCapable instances. */
  final NonBinaryCompatiblePersistenceCapable nbcpc0 = new NonBinaryCompatiblePersistenceCapable(0);

  final NonBinaryCompatiblePersistenceCapable nbcpc1 = new NonBinaryCompatiblePersistenceCapable(1);
  final NonBinaryCompatiblePersistenceCapable nbcpc2 = new NonBinaryCompatiblePersistenceCapable(2);

  final NonBinaryCompatiblePersistenceCapable[] nbcpcs = {nbcpc0, nbcpc1, nbcpc2};

  /** The array of StateInterrogations */
  static final StateInterrogation si0 = new StateInterrogationImpl(0);

  static final StateInterrogation si1 = new StateInterrogationImpl(1);
  static final StateInterrogation si2 = new StateInterrogationImpl(2);
  static final StateInterrogation[] sis = {si0, si1, si2};

  /**
   * The StateInterrogation implementation manages NonBinaryCompatiblePersistenceCapable instances
   * that have a hashCode equal to their own.
   *
   * <p>For the methods returning Object, return null if the object is not managed by this
   * StateInterrogation.
   *
   * <p>For the methods returning Boolean, return null if the object is not managed by this
   * StateInterrogation.
   *
   * <p>For the makeDirty method, return false if the object is not managed by this
   * StateInterrogation.
   */
  private static class StateInterrogationImpl implements StateInterrogation {

    private final int id;

    public int hashCode() {
      return id;
    }

    private StateInterrogationImpl(int id) {
      this.id = id;
    }

    public boolean equals(Object other) {
      if (other.getClass() != StateInterrogationImpl.class) return false;
      return (other.hashCode() == id);
    }

    private boolean isMine(Object pc) {
      return pc.hashCode() == id;
    }

    public javax.jdo.PersistenceManager getPersistenceManager(Object pc) {
      return isMine(pc) ? pmProxies[id] : null;
    }

    public Object getObjectId(Object pc) {
      return isMine(pc) ? new ObjectIdImpl(id) : null;
    }

    public Object getTransactionalObjectId(Object pc) {
      return isMine(pc) ? new ObjectIdImpl(id) : null;
    }

    public Object getVersion(Object pc) {
      return isMine(pc) ? new ObjectIdImpl(id) : null;
    }

    public Boolean isDeleted(Object pc) {
      if (isMine(pc)) {
        return (pc.hashCode() == 1) ? Boolean.TRUE : Boolean.FALSE;
      } else return null;
    }

    public Boolean isDetached(Object pc) {
      if (isMine(pc)) {
        return (pc.hashCode() == 1) ? Boolean.TRUE : Boolean.FALSE;
      } else return null;
    }

    public Boolean isDirty(Object pc) {
      if (isMine(pc)) {
        return ((NonBinaryCompatiblePersistenceCapable) pc).isDirty()
            ? Boolean.TRUE
            : Boolean.FALSE;
      } else return null;
    }

    public Boolean isNew(Object pc) {
      if (isMine(pc)) {
        return (pc.hashCode() == 1) ? Boolean.TRUE : Boolean.FALSE;
      } else return null;
    }

    public Boolean isPersistent(Object pc) {
      if (isMine(pc)) {
        return (pc.hashCode() == 1) ? Boolean.TRUE : Boolean.FALSE;
      } else return null;
    }

    public Boolean isTransactional(Object pc) {
      if (isMine(pc)) {
        return (pc.hashCode() == 1) ? Boolean.TRUE : Boolean.FALSE;
      } else return null;
    }

    public boolean makeDirty(Object pc, String fieldName) {
      if (isMine(pc)) {
        ((NonBinaryCompatiblePersistenceCapable) pc).setDirty(true);
        return true;
      } else return false;
    }
  }

  /** The non-binary-compatible PersistenceCapable class. */
  public static class NonBinaryCompatiblePersistenceCapable {
    private final int id;
    private boolean dirty = false;

    private NonBinaryCompatiblePersistenceCapable(int id) {
      this.id = id;
    }

    public int hashCode() {
      return id;
    }

    public void setDirty(boolean dirty) {
      this.dirty = dirty;
    }

    public boolean isDirty() {
      return dirty;
    }
  }

  /** The non-binary-compatible object id class. */
  public static class ObjectIdImpl {
    private final int id;

    private ObjectIdImpl(int id) {
      this.id = id;
    }

    public int hashCode() {
      return id;
    }

    public boolean equals(Object other) {
      if (other.getClass() != ObjectIdImpl.class) return false;
      return (other.hashCode() == id);
    }
  }

  /** The non-binary-compatible InvocationHandler class for PersistenceManager proxy. */
  private static class InvocationHandlerImpl implements InvocationHandler {
    private final int id;

    private InvocationHandlerImpl(int id) {
      this.id = id;
    }

    public int hashCode() {
      return id;
    }

    public boolean equals(Object other) {
      if (other.getClass() != ObjectIdImpl.class) return false;
      return (other.hashCode() == id);
    }

    public Object invoke(Object obj, java.lang.reflect.Method method, Object[] params)
        throws Throwable {
      String name = method.getName();
      if ("hashCode".equals(name)) {
        return Integer.valueOf(id);
      } else if ("equals".equals(name)) {
        Object other = params[0];
        if (!(other instanceof PersistenceManager)) return Boolean.FALSE;
        int otherid = Proxy.getInvocationHandler(other).hashCode();
        if (otherid == id) return Boolean.TRUE;
        return Boolean.FALSE;
      }
      return null;
    }
  }
}
