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

package org.apache.jdo.test;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.lang.reflect.Constructor;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import javax.jdo.spi.PersistenceCapable;

import org.apache.jdo.pc.PCPoint;
import org.apache.jdo.test.util.AbstractTest;
import org.apache.jdo.test.util.JDORITestRunner;

/**
* Tests that we can insert, fetch, and get extent of instances of 2 distinct
* classes which have the same name.  For this test, they are both
* org.apache.jdo.pc.PCPoint, but one of them has 2 coordinates and the
* other has 3. We use our own ClassLoader to be able to load them both in
* the same JVM. 
*
* @author Dave Brigstor
*/
public class Test_FSUID2 extends AbstractTest {
    /** Maps class names to classes. */
    // This is only for use byt FSUIDLoader, but inner classes cannot have
    // static members, so here it is.
    private static final HashMap cache = new HashMap();

    /** Name of the jarfile property. */
    private static final String JARFILE_PROPERTY = "fsuidjar";

    /** Default jarfile name, used if property is undefined. */
    private static final String DEFAULT_JARFILE = "fsuid2.jar";

    /** Name of the jar file for class loading. */
    private String jarfile;

    /** Reflection constructor instance for 3D point class. */
    private Constructor p3Constructor;

    /** */
    public static void main(String args[]) {
        JDORITestRunner.run(Test_FSUID2.class);
    }

    /** */
    public Test_FSUID2() {
        super();
        jarfile = System.getProperty(JARFILE_PROPERTY, DEFAULT_JARFILE);
    }
    
    /** */
    public void test() throws Exception {
        insertObjects();
        writeOIDs();
        readObjects();
    }

    /**
     * Create and store instances of PCPoint whose classes are loaded from
     * different class loaders and have different structure (and hence
     * different FOStoreSchemaUID's).
     */
    protected void insertObjects() throws Exception {
        PersistenceManager pm = null;
        Transaction tx = null;
        
        try {
            if (debug) logger.debug("\nINSERT");

            pm = pmf.getPersistenceManager();
            tx = pm.currentTransaction();
            tx.begin();

            // Get loader for and load class of 3-D point.
            p3Constructor = get3DPointConstructor();

            Object inserted1[] = new Object[numInsert];
            Object inserted2[] = new Object[numInsert];
            for (int i = 0; i < numInsert; i++) {
                // create 2D point
                Object pc = new PCPoint(i, new Integer(i));
                if (debug)
                    logger.debug("cl " + i + " " + pc.getClass().getClassLoader());
                pm.makePersistent(pc);
                inserted1[i] = pc;

                // create 3D point
                pc = (PersistenceCapable)p3Constructor.newInstance(
                    new Object[] {
                        new Integer(i), new Integer(i), new Float(1.0 * i)});
                if (debug)
                    logger.debug("cl " + i + " " + pc.getClass().getClassLoader());
                pm.makePersistent(pc);
                inserted2[i] = pc;
            }

            tx.commit();

            for (int i = 0; i < numInsert; i++) {
                announce("inserted ", inserted1[i]);
                announce("inserted ", inserted2[i]);
            }

            if (debug) logger.debug("inserted " + insertedCount + " objects");
        }
        finally {
            if (tx != null && tx.isActive())
                tx.rollback();
            if (pm != null && !pm.isClosed())
                pm.close();
        }
    }

    /** 
     * Loads and verifies the pc instances specified by the stored oids.
     */
    protected void readObjects() throws Exception {
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try {
            ObjectInputStream in = getOIDFileInputStream();
            tx.begin();
            try {
                int count = 0;
                while (true) {
                    Object oid = in.readObject();
                    Object pc = pm.getObjectById(oid, true);
                    if (debug) logger.debug("fetching: " + oid + " -> " + pc);
                    // The first numInsert instances are supposed to be
                    // 2D points and the next are supposed to be 3D points.
                    if (count < numInsert)
                        verify2DPoint(count, pc);
                    else
                        verify3DPoint(count - numInsert, pc);
                    count++;
                }
            } 
            catch (EOFException ex) {
                // OK
            }
            tx.commit();
        }
        finally {
            if (tx != null && tx.isActive())
                tx.rollback();
            if (pm != null && !pm.isClosed())
                pm.close();
        }
    }

    /** */
    private void verify2DPoint(int i, Object pc) {
        Object expected = new PCPoint(i, new Integer(i));
        assertEquals("Wrong 2D point instance", expected, pc);
    }

    /** */
    private void verify3DPoint(int i, Object pc) throws Exception {
        Object expected = p3Constructor.newInstance(new Object[] {
            new Integer(i), new Integer(i), new Float(1.0 * i)});
        assertEquals("Wrong 2D point instance", expected, pc);
    }
    
    /** */
    protected int getDefaultInsert()
    {
        return 4;
    }   

    private Constructor get3DPointConstructor() throws Exception {
        try {
            return (Constructor)AccessController.doPrivileged( 
                new PrivilegedExceptionAction () {
                    public Object run () throws Exception {
                        FSUIDLoader l = new FSUIDLoader(jarfile);
                        Class p3Class = l.loadClass("org.apache.jdo.pc.PCPoint", true);
                        return p3Class.getDeclaredConstructor(
                            new Class[] { int.class, Integer.class, float.class });
                    }});
        }
        catch (PrivilegedActionException ex) {
            // unwrap FileNotFoundException
            throw ex.getException();
        }
    }
    
    /**
     * Expects to load classes from within a specified jar file.
     */
    class FSUIDLoader extends ClassLoader {
        /** Name of the jar file for class loading. */
        private final String filename;
        
        FSUIDLoader(String filename) {
            this.filename = filename;
        }
        
        public InputStream getResourceAsStream(String name) {
            if (!name.startsWith("org/apache/jdo/pc")) {
                return getSystemResourceAsStream(name);
            }

            try {
                JarFile jarFile = getJarFile();
                JarEntry entry = jarFile.getJarEntry(name);
                if (entry != null) {
                    return jarFile.getInputStream(entry);
                }
            } 
            catch (IOException ex) {
                ex.printStackTrace();
            }
            return null;
        }

        public synchronized Class loadClass(String name, boolean resolve)
            throws ClassNotFoundException {
            
            Class c = (Class)cache.get(name);
            
            try {
                if (null == c && ! name.startsWith("org.apache.jdo.pc")) {
                    try {
                        c = findSystemClass(name);
                    } catch (ClassNotFoundException ex) {
                        ;
                    } catch (NoClassDefFoundError ex) {
                        ;
                    }
                }

                if (null == c) {                    
                    byte b[] = loadClassData(name);
                    if (null == b) {
                        throw new NoClassDefFoundError(name);
                    }
                    c = defineClass(name, b, 0, b.length);
                    cache.put(name, c);
                }
                if (resolve) {
                    resolveClass(c);
                }
            } catch (NoClassDefFoundError ex) {
                throw new ClassNotFoundException("loadClass " + name, ex);
            }
            return c;
        }

        private byte[] loadClassData(String name)
            throws ClassNotFoundException {
            byte rc[] = null;

            // Class names in jar files have file separators...I hope they're
            // the same on Un*x and Windows.
            name = name.replace('.', '/') + ".class";
            try {
                JarFile jf = getJarFile();
                for (Enumeration e = jf.entries(); e.hasMoreElements();) {
                    JarEntry je = (JarEntry)e.nextElement();
                    if (name.equals(je.getName())) {
                        InputStream is = jf.getInputStream(je);
                        int avail = is.available();
                        if (-1 != avail) {
                            int size = avail;
                            rc = new byte[size];
                            int off = 0;
                            int count = 0;
                            while (size > 0) {
                                count = is.read(rc, off, size);
                                if (count <= 0) {
                                    is.close();
                                    break;
                                }
                                off += count;
                                size -= count;
                            }
                            if (off != avail) {
                                throw new IOException(
                                    "failed to read complete class " + name);
                            }
                        }
                        break;
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                throw new ClassNotFoundException("loadClassData " + name, ex);
            }
            return rc;
        }

        private JarFile getJarFile() throws IOException {
            try {
                return (JarFile)AccessController.doPrivileged(
                    new PrivilegedExceptionAction () {
                        public Object run () throws IOException {
                            return new JarFile(filename);
                        }});
            }
            catch (PrivilegedActionException ex) {
                // unwrap FileNotFoundException
                throw (IOException)ex.getException();
            }
        }

    }

}
