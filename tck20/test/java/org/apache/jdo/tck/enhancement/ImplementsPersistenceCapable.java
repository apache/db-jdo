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

package org.apache.jdo.tck.enhancement;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import org.apache.jdo.impl.enhancer.util.AugmentationTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Implements PersistenceCapable Added
 *<BR>
 *<B>Keywords:</B> enhancement
 *<BR>
 *<B>Assertion ID:</B> A20.3-0.
 *<BR>
 *<B>Assertion Description: </B>
The Reference Enhancer makes the following change
to persistence-capable classes:
it adds <code>&quot;implements javax.jdo.PersistenceCapable</code>&quot;
to the class definition.

 */

public class ImplementsPersistenceCapable extends EnhancerTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A20.3-0 (ImplementsPersistenceCapable) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(ImplementsPersistenceCapable.class);
    }

    /** */
    public void test() {
        if (debug)
            logger.debug("org.apache.jdo.tck.enhancement.ImplementsPersistenceCapable.run");
        runTestAllPackages();
    }

    /** */
    protected void runTestOnePackage (String packageName, List classNames) {
        if (debug) 
            logger.debug("ImplementsPersistenceCapable.testOnePackage: " + 
                         packageName + " classes " + classNames);
        
        PrintWriter out = new PrintWriter(System.out);
        final AugmentationTest test = new AugmentationTest(out, out);
        final String classpath = System.getProperty("java.class.path");
        final String jdoPropertiesFileName = 
            packageName.replace ('.', '/') + "/jdoTest.properties"; //NOI18N        
        final String[] args = new String[classNames.size() + 6];
        int index = 0;
        // init arguments for AugmentationTest.run call
        // specify properties file
        args[index++] = "--properties";
        args[index++] = jdoPropertiesFileName;
        // specify jdo path to find the properties file
        args[index++] = "-j";
        args[index++] = classpath;
        // specify source path to find the classes
        args[index++] = "-s";
        args[index++] = classpath;
        // add class names
        for (java.util.Iterator i = classNames.iterator(); i.hasNext();) {
            args[index++] = (String)i.next();
        }
        if (debug)
            logger.debug ("Run AugmentationTest with args " + Arrays.asList(args));
        int errors = test.run(args);
        if (errors > 0) {
            fail(ASSERTION_FAILED,
                 "AugmentationTest with args " + Arrays.asList(args) +
                 " results in " + errors + " errors.");
        }
    }
}
