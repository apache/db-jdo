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

package javax.jdo.spi;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;

import javax.jdo.util.AbstractTest;
import javax.jdo.util.BatchTestRunner;

/**
 * This class tests the StateInterrogation interface. The test is in
 * several parts:
 * <ul><li>  Add and remove the StateInterrogation instance
 * </li><li> test interrogatives to return the correct answer
 * </li><li> test getters to return the correct answer
 * </li><li> test makeDirty to return.
 * </li></ul>
 * We use an mock implementation of StateInterrogation interface to 
 * log when calls are received and to return the correct replies.
 * We use a java.lang.reflect.Proxy for the getPersistenceManager call
 * because it's too much work to mock it.
 */
public class StateInterrogationTest extends AbstractTest {
    
    private JDOImplHelper implHelper = JDOImplHelper.getInstance();
    
    private JDOHelper helper = new JDOHelper();
    
    /** Creates a new instance of StateInterrogationTest */
    public StateInterrogationTest() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        BatchTestRunner.run(StateInterrogationTest.class);
    }
    
    public void testGetObjectIdNull() {
        Object id2 = helper.getObjectId(nbcpc2);
        assertNull("ObjectId should be null before addStateInterrogations",
                id2);
        addStateInterrogations();
        Object id = helper.getObjectId(Boolean.TRUE);
        assertNull("ObjectId should be null for non-pc instances", 
                id);
    }

    public void testGetObjectId() {
        addStateInterrogations();
        Object id2 = helper.getObjectId(nbcpc2);
        assertNotNull("ObjectId should not be null", 
                id2);
        assertEquals("ObjectId should be 2", 
                2, id2.hashCode());
        Object id20 = helper.getObjectId(nbcpc2);
        assertEquals("ObjectIds from same object should be equal", 
                id2, id20);
    }

    public void testRemoveStateInterrogation() {
        addStateInterrogations();
        Object id2 = helper.getObjectId(nbcpc2);
        assertNotNull("ObjectId should not be null", 
                id2);
        assertEquals("ObjectId should be 2", 
                2, id2.hashCode());
        implHelper.removeStateInterrogation(si2);
        implHelper.removeStateInterrogation(si0);
        Object id21 = helper.getObjectId(nbcpc2);
        assertNull("ObjectId should be null after RemoveStateInterrogation",
                id21);
        Object id1 = helper.getObjectId(nbcpc1);
        assertNotNull("ObjectId should not be null", 
                id1);
        assertEquals("ObjectId should be 1", 
                1, id1.hashCode());
    }

    public void testGetTransactionalObjectIdNull() {
        Object id2 = helper.getTransactionalObjectId(nbcpc2);
        assertNull("TransactionalObjectId should be null before addStateInterrogations",
                id2);
        addStateInterrogations();
        Object id = helper.getTransactionalObjectId(Boolean.TRUE);
        assertNull("TransactionalObjectId should be null for non-pc instances", 
                id);
    }

    public void testGetTransactionalObjectId() {
        addStateInterrogations();
        Object id2 = helper.getTransactionalObjectId(nbcpc2);
        assertNotNull("TransactionalObjectId should not be null", 
                id2);
        assertEquals("TransactionalObjectId should be 2", 
                2, id2.hashCode());
        Object id20 = helper.getTransactionalObjectId(nbcpc2);
        assertEquals("TransactionalObjectIds from same object should be equal", 
                id2, id20);
    }

    public void testGetPersistenceManagerNull() {
        Object pm2 = helper.getPersistenceManager(nbcpc2);
        assertNull("PersistenceManager should be null before addStateInterrogations",
                pm2);
        addStateInterrogations();
        Object pm = helper.getPersistenceManager(Boolean.TRUE);
        assertNull("PersistenceManager should be null for non-pc instances", 
                pm);
    }

    public void testGetPersistenceManager() {
        addStateInterrogations();
        Object pm2 = helper.getPersistenceManager(nbcpc2);
        assertNotNull("PersistenceManager should not be null", 
                pm2);
        assertEquals("PersistenceManager should be 2", 
                2, pm2.hashCode());
        Object pm20 = helper.getPersistenceManager(nbcpc2);
        assertEquals("PersistenceManagers from same object should be equal", 
                pm2, pm20);
    }

    public void testGetVersionNull() {
        Object id2 = helper.getVersion(nbcpc2);
        assertNull("Version should be null before addStateInterrogations",
                id2);
        addStateInterrogations();
        Object id = helper.getVersion(Boolean.TRUE);
        assertNull("Version should be null for non-pc instances", 
                id);
    }

    public void testGetVersion() {
        addStateInterrogations();
        Object id2 = helper.getVersion(nbcpc2);
        assertNotNull("Version should not be null", 
                id2);
        assertEquals("Version should be 2", 
                2, id2.hashCode());
        Object id20 = helper.getVersion(nbcpc2);
        assertEquals("Versions from same object should be equal", 
                id2, id20);
    }

    public void testIsDeletedFalse() {
        assertFalse("IsDeleted should be false before addStateInterrogations",
                helper.isDeleted(nbcpc2));
        addStateInterrogations();
        assertFalse("IsDeleted should be false for non-pc instances", 
                helper.isDeleted(Boolean.TRUE));
        implHelper.removeStateInterrogation(si2);
        assertFalse("IsDeleted should be false after removeStateInterrogations", 
                helper.isDeleted(nbcpc2));
    }

    public void testIsDeletedMine() {
        addStateInterrogations();
        assertTrue("IsDeleted should be true for nbcpc1", 
                helper.isDeleted(nbcpc1));
        assertFalse("IsDeleted should be false for nbcpc2", 
                helper.isDeleted(nbcpc2));
    }

    public void testIsDetachedFalse() {
        assertFalse("IsDetached should be false before addStateInterrogations",
                helper.isDetached(nbcpc2));
        addStateInterrogations();
        assertFalse("IsDetached should be false for non-pc instances", 
                helper.isDetached(Boolean.TRUE));
        implHelper.removeStateInterrogation(si2);
        assertFalse("IsDetached should be false after removeStateInterrogations", 
                helper.isDetached(nbcpc2));
    }

    public void testIsDetachedMine() {
        addStateInterrogations();
        assertTrue("IsDetached should be true for nbcpc1", 
                helper.isDetached(nbcpc1));
        assertFalse("IsDetached should be false for nbcpc2", 
                helper.isDetached(nbcpc2));
    }

    public void testIsDirtyFalse() {
        assertFalse("IsDirty should be false before addStateInterrogations",
                helper.isDirty(nbcpc2));
        addStateInterrogations();
        assertFalse("IsDirty should be false for non-pc instances", 
                helper.isDirty(Boolean.TRUE));
        implHelper.removeStateInterrogation(si2);
        nbcpc2.setDirty(true);
        assertFalse("IsDirty should be false after removeStateInterrogations", 
                helper.isDirty(nbcpc2));
        nbcpc2.setDirty(false);
    }

    public void testIsDirtyMine() {
        addStateInterrogations();
        nbcpc1.setDirty(true);
        assertTrue("IsDirty should be true for nbcpc1 after setDirty(true)", 
                helper.isDirty(nbcpc1));
        nbcpc1.setDirty(false);
        assertFalse("IsDirty should be false for nbcpc1 after setDirty(false)", 
                helper.isDirty(nbcpc1));
        assertFalse("IsDirty should be false for nbcpc2", 
                helper.isDirty(nbcpc2));
    }

    public void testIsNewFalse() {
        assertFalse("IsNew should be false before addStateInterrogations",
                helper.isNew(nbcpc2));
        addStateInterrogations();
        assertFalse("IsNew should be false for non-pc instances", 
                helper.isNew(Boolean.TRUE));
        implHelper.removeStateInterrogation(si2);
        assertFalse("IsNew should be false after removeStateInterrogations", 
                helper.isNew(nbcpc2));
    }

    public void testIsNewMine() {
        addStateInterrogations();
        assertTrue("IsNew should be true for nbcpc1", 
                helper.isNew(nbcpc1));
        assertFalse("IsNew should be false for nbcpc2", 
                helper.isNew(nbcpc2));
    }

    public void testIsPersistentFalse() {
        assertFalse("IsPersistent should be false before addStateInterrogations",
                helper.isPersistent(nbcpc2));
        addStateInterrogations();
        assertFalse("IsPersistent should be false for non-pc instances", 
                helper.isPersistent(Boolean.TRUE));
        implHelper.removeStateInterrogation(si2);
        assertFalse("IsPersistent should be false after removeStateInterrogations", 
                helper.isPersistent(nbcpc2));
    }

    public void testIsPersistentMine() {
        addStateInterrogations();
        assertTrue("IsPersistent should be true for nbcpc1", 
                helper.isPersistent(nbcpc1));
        assertFalse("IsPersistent should be false for nbcpc2", 
                helper.isPersistent(nbcpc2));
    }

    public void testIsTransactionalFalse() {
        assertFalse("IsTransactional should be false before addStateInterrogations",
                helper.isTransactional(nbcpc2));
        addStateInterrogations();
        assertFalse("IsTransactional should be false for non-pc instances", 
                helper.isTransactional(Boolean.TRUE));
        implHelper.removeStateInterrogation(si2);
        assertFalse("IsTransactional should be false after removeStateInterrogations", 
                helper.isTransactional(nbcpc2));
    }

    public void testIsTransactionalMine() {
        addStateInterrogations();
        assertTrue("IsTransactional should be true for nbcpc1", 
                helper.isTransactional(nbcpc1));
        assertFalse("IsTransactional should be false for nbcpc2", 
                helper.isTransactional(nbcpc2));
    }

    public void testMakeDirtyFalse() {
        helper.makeDirty(nbcpc2, "");
        nbcpc2.setDirty(true);
        assertFalse("IsDirty should be false before addStateInterrogations",
                helper.isDirty(nbcpc2));
        addStateInterrogations();
        implHelper.removeStateInterrogation(si2);
        nbcpc2.setDirty(false);
        helper.makeDirty(nbcpc2, "");
        assertFalse("IsDirty should be false after removeStateInterrogations", 
                helper.isDirty(nbcpc2));
    }

    public void testMakeDirtyMine() {
        addStateInterrogations();
        helper.makeDirty(nbcpc1, "");
        assertTrue("IsDirty should be true for nbcpc1", 
                helper.isDirty(nbcpc1));
        nbcpc1.setDirty(false);
        assertFalse("IsDirty should be false after setDirty(false)",
                helper.isDirty(nbcpc1));
    }

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
    
    /** 
     * The non-binary-compatible PersistenceManager class instances.
     */
    static private PersistenceManager pmProxy0 = (PersistenceManager)
            Proxy.newProxyInstance(
                PersistenceManager.class.getClassLoader(),
                new Class[] {PersistenceManager.class},
                new InvocationHandlerImpl(0));

    /** 
     * The non-binary-compatible PersistenceManager class instances.
     */
    static private PersistenceManager pmProxy1 = (PersistenceManager)
            Proxy.newProxyInstance(
                PersistenceManager.class.getClassLoader(),
                new Class[] {PersistenceManager.class},
                new InvocationHandlerImpl(1));

    /** 
     * The non-binary-compatible PersistenceManager class instances.
     */
    static private PersistenceManager pmProxy2 = (PersistenceManager)
            Proxy.newProxyInstance(
                PersistenceManager.class.getClassLoader(),
                new Class[] {PersistenceManager.class},
                new InvocationHandlerImpl(2));

    /**
     * The array of PersistenceManager proxies
     */
    static PersistenceManager[] pmProxies = {pmProxy0, pmProxy1, pmProxy2};
    
    /** 
     * The array of NonBinaryCompatiblePersistenceCapable instances.
     */
    NonBinaryCompatiblePersistenceCapable nbcpc0 = 
       new NonBinaryCompatiblePersistenceCapable(0);
    NonBinaryCompatiblePersistenceCapable nbcpc1 = 
       new NonBinaryCompatiblePersistenceCapable(1);
    NonBinaryCompatiblePersistenceCapable nbcpc2 = 
       new NonBinaryCompatiblePersistenceCapable(2);
    
    NonBinaryCompatiblePersistenceCapable[] nbcpcs = {nbcpc0, nbcpc1, nbcpc2};
    
    /** 
     * The array of StateInterrogations
     */
    static StateInterrogation si0 = new StateInterrogationImpl(0);
    static StateInterrogation si1 = new StateInterrogationImpl(1);
    static StateInterrogation si2 = new StateInterrogationImpl(2);
    static StateInterrogation[] sis = {si0, si1, si2};
    
    /** 
     * The StateInterrogation implementation manages 
     * NonBinaryCompatiblePersistenceCapable instances that have a 
     * hashCode equal to their own.
     *
     * <P>For the methods returning Object, return null if the object
     * is not managed by this StateInterrogation.
     *
     * <P>For the methods returning Boolean, return null if the object
     * is not managed by this StateInterrogation.
     *
     * <P>For the makeDirty method, return false if the object
     * is not managed by this StateInterrogation.
     */
    private static class StateInterrogationImpl implements StateInterrogation {
        
        private int id;
        
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
            return isMine(pc)?pmProxies[id]:null;
        }

        public Object getObjectId(Object pc) {
            return isMine(pc)?new ObjectIdImpl(id):null;
        }
        
        public Object getTransactionalObjectId(Object pc) {
            return isMine(pc)?new ObjectIdImpl(id):null;
        }

        public Object getVersion(Object pc) {
            return isMine(pc)?new ObjectIdImpl(id):null;
        }

        public Boolean isDeleted(Object pc) {
            if (isMine(pc)) {
                return (pc.hashCode()==1)?Boolean.TRUE:Boolean.FALSE;
            } else return null;
        }

        public Boolean isDetached(Object pc) {
            if (isMine(pc)) {
                return (pc.hashCode()==1)?Boolean.TRUE:Boolean.FALSE;
            } else return null;
        }

        public Boolean isDirty(Object pc) {
            if (isMine(pc)) {
                return ((NonBinaryCompatiblePersistenceCapable)pc).isDirty()
                    ?Boolean.TRUE:Boolean.FALSE;
            } else return null;
        }

        public Boolean isNew(Object pc) {
            if (isMine(pc)) {
                return (pc.hashCode()==1)?Boolean.TRUE:Boolean.FALSE;
            } else return null;
        }

        public Boolean isPersistent(Object pc) {
            if (isMine(pc)) {
                return (pc.hashCode()==1)?Boolean.TRUE:Boolean.FALSE;
            } else return null;
        }

        public Boolean isTransactional(Object pc) {
            if (isMine(pc)) {
                return (pc.hashCode()==1)?Boolean.TRUE:Boolean.FALSE;
            } else return null;
        }

        public boolean makeDirty(Object pc, String fieldName) {
            if (isMine(pc)) {
                ((NonBinaryCompatiblePersistenceCapable)pc).setDirty(true);
                return true;
            } else return false;
        }
    }
    
    /** 
     * The non-binary-compatible PersistenceCapable class.
     */
    public static class NonBinaryCompatiblePersistenceCapable {
        private int id;
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
    
    /** 
     * The non-binary-compatible object id class.
     */
    public static class ObjectIdImpl {
        private int id;
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
    
    /** 
     * The non-binary-compatible InvocationHandler class
     *  for PersistenceManager proxy.
     */
    private static class InvocationHandlerImpl implements InvocationHandler {
        private int id;
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
        public Object invoke(Object obj, java.lang.reflect.Method method, 
                Object[] params) throws Throwable {
            String name = method.getName();
            if (name == "hashCode") {
                return new Integer(id);
            } else if (name == "equals") {
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