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

package org.apache.jdo.test.util;

import java.util.*;

import javax.jdo.*;

import org.apache.jdo.test.util.AbstractTest;
import org.apache.jdo.test.util.JDORITestRunner;

/**
* This test retrieves an extent-full of instances.
*
* @author Dave Bristor
*/
public class DumpExtent extends AbstractTest {

    /** If true, get subclass instances too. */
    private final boolean subclasses;

    /** class names */
    private final String classNames;
    
    /** */
    public static void main(String args[]) {
        new DumpExtent().start();
    }

    /** */
    public DumpExtent() {
        super();
        this.subclasses = Boolean.getBoolean("subclasses");
        this.classNames = System.getProperty("class", "org.apache.jdp.pc.PCPoint");
        this.existing = true;
    }

    /** */
    public void start() {
        PersistenceManager pm = null;
        try {
            setUp();
            pm = pmf.getPersistenceManager();
            dumpExtents(pm, getClassList());
        }
        catch (Exception ex) {
            logger.debug("caught " + ex);
        }
        finally {
            if (pm != null && !pm.isClosed())
                pm.close();
            tearDown();
        }
    }

    /** */
    protected List getClassList() {
        List classes = new ArrayList();
        StringTokenizer st = new StringTokenizer(classNames, ",");
        while (st.hasMoreElements()) {
            String className = (String)st.nextElement();
            try {
                classes.add(Class.forName(className));
            } catch (ClassNotFoundException ex) {
                logger.debug("Could not load specified class " + className);
            }
        }
        return classes;
    }

    /** */
    protected void dumpExtents(PersistenceManager pm, List classes) {
        for (Iterator i = classes.iterator(); i.hasNext();) {
            dumpExtent(pm, (Class)i.next());
        }
    }
    
    /** */
    protected void dumpExtent(PersistenceManager pm, Class clazz) {
        logger.debug("\nEXTENT of " + clazz.getName());
        Extent e = pm.getExtent(clazz, subclasses);
        int objCount = 0;
        TreeSet elements = new TreeSet();
        for (Iterator i = e.iterator(); i.hasNext();) {
            Object pc = i.next();
            elements.add(pc.toString());
            objCount++;
        }
        for (Iterator k = elements.iterator(); k.hasNext();) {
            logger.debug((String)k.next());
        }
        logger.debug("extent of " + clazz.getName() + " has " +
                     objCount + " objects");
    }
}
