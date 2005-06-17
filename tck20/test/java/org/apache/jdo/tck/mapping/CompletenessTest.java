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

import java.util.Collection;
import java.util.HashSet;

import javax.jdo.PersistenceManager;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Company;
import org.apache.jdo.tck.util.BatchTestRunner;
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
    
    protected Object rootOid;
    
        // todo: get filename from property
    protected String inputFilename = "org/apache/jdo/tck/pc/company/companyNoRelationships.xml";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(CompletenessTest.class);
    }
    
    protected void localSetUp() {
        CompanyModelReader reader = new CompanyModelReader(inputFilename);
            // persist test data
        getPM();
        pm.currentTransaction().begin();
        Object root = reader.getCompany("company1");
        pm.makePersistent(root);
        addTearDownClass(reader.getAllClasses());
        rootOid = pm.getObjectId(root);
        pm.currentTransaction().commit();
        cleanupPM();
    }
    /** */
    public void test() {
        
        // get new obj graph
        CompanyModelReader reader = new CompanyModelReader(inputFilename);
        Company companyExpected = reader.getCompany("company1");

        getPM();
        pm.currentTransaction().begin();
        // compare persisted and new
        if (!companyExpected.deepCompareFields(
            (Company)pm.getObjectById(rootOid), new EqualityHelper())) {
            fail("Persistent company not equal to expected company");
        }
        pm.currentTransaction().commit();
    }
}
