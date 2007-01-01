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

package org.apache.jdo.tck.enhancement;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import org.apache.jdo.impl.enhancer.util.AnnotationTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> FieldAccessModified
 *<BR>
 *<B>Keywords:</B> enhancement
 *<BR>
 *<B>Assertion ID:</B> .
 *<BR>
 *<B>Assertion Description: </B>
The enhancer modifies field accesses to guarantee that the values of fields
are retrieved from the data store prior to application usage.
<OL TYPE="A">
<LI>For any field access that reads the value of a field, the getfield byte code
is replaced with a call to a generated local method, <code>jdoGetXXX</code></LI>
<LI>For any field access that stores the new value of a field, the putfield
byte code is replaced with a call to a generated local method,
<code>jdoSetXXX</code></LI>
</OL>

 */

public class FieldAccessModified extends EnhancerTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion (FieldAccessModified) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(FieldAccessModified.class);
    }

    
    /** */
    public void test() {
        if (debug)
            logger.debug("org.apache.jdo.tck.enhancement.FieldAccessModified.run");
        PMFPropertiesObject = loadProperties(PMFProperties); // will exit here if no properties
        runTestAllPackages();
        cleanup();
    }

    /** */
    protected void runTestOnePackage (String packageName, List classNames) {
        if (debug)
            logger.debug("FieldAccessModified.testOnePackage: " + 
                         packageName + " classes " + classNames);
        
        PrintWriter out = new PrintWriter(System.out);
        final AnnotationTest test = new AnnotationTest(out, out);
        final String classpath = System.getProperty("java.class.path");
        final String jdoPropertiesFileName = 
            packageName.replace ('.', '/') + "/jdoTest.properties"; //NOI18N        
        final String[] args = new String[classNames.size() + 6];
        int index = 0;
        // init arguments for AnnotationTest.run call
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
            logger.debug ("Run AnnotationTest with args " + Arrays.asList(args));
        int errors = test.run(args);
        if (errors > 0) {
            fail(ASSERTION_FAILED,
                 "AnnotationTest with args " + Arrays.asList(args) +
                 " results in " + errors + " errors.");
        }
    }
}
