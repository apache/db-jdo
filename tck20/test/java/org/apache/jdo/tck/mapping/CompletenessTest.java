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

package org.apache.jdo.tck.mapping;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.util.BatchTestRunner;
import org.apache.jdo.tck.util.DeepEquality;
import org.apache.jdo.tck.util.EqualityHelper;

//import org.springframework.beans.factory.xml.XmlBeanFactory;

/**
 *<B>Title:</B>Completeness Test
 *<BR>
 *<B>Keywords:</B> mapping
 *<BR>
 *<B>Assertion ID:</B> A18.[not identified]
 *<BR>
 *<B>Assertion Description: </B>
 */

public class CompletenessTest extends JDO_Test {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A18-[not identified] failed: ";
    
    /** */
    protected List rootOids;
    
    protected String inputFilename = System.getProperty("jdo.tck.testdata");
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(CompletenessTest.class);
    }
    
    /**
     * @see JDO_Test#localSetUp()
     */
    protected void localSetUp() {
        CompanyModelReader reader = new CompanyModelReader(inputFilename);
        // persist test data
        getPM();
        pm.currentTransaction().begin();
        List rootList = reader.getRootList();
        pm.makePersistentAll(rootList);
        addTearDownClass(reader.getTearDownClasses());
        rootOids = new ArrayList();
        for (Iterator i = rootList.iterator(); i.hasNext(); ) {
            Object pc = i.next();
            rootOids.add(pm.getObjectId(pc));
        }
        pm.currentTransaction().commit();
        cleanupPM();
    }

    /** */
    public void test() {
        
        // get new obj graph
        CompanyModelReader reader = new CompanyModelReader(inputFilename);
        List rootList = reader.getRootList();
        
        getPM();
        pm.currentTransaction().begin();
        // compare persisted and new
        int size = rootList.size();
        StringBuffer msg = new StringBuffer();
        for (int i = 0; i < size; i++) {
            DeepEquality expected = (DeepEquality) rootList.get(i);
            Object oid = rootOids.get(i);
            DeepEquality persisted = (DeepEquality) pm.getObjectById(oid);
            if (!expected.deepCompareFields(persisted, new EqualityHelper())) {
                if (msg.length() > 0) {
                    msg.append("\n");
                }
                msg.append("  Persistent instance " + persisted + 
                           " not equal to expected instance " + expected);
            }
        }
        pm.currentTransaction().commit();
        // fail test if at least one of the instances is not the expected one
        if (msg.length() > 0) {
            fail("CompletenessTest failed; see list of failed instances below:", msg.toString());
        }
    }
}
