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
 
package org.apache.jdo.tck.api.persistencemanagerfactory;

import java.util.Collection;
import java.util.Iterator;

import javax.jdo.PersistenceManagerFactory;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.util.BatchTestRunner;


/**
 *<B>Title:</B>SupportedOptions of persistencemanagerfactory  
 *<BR>
 *<B>Keywords:</B> persistencemanagerfactory
 *<BR>
 *<B>Assertion IDs:</B> A11.5-1.
 *<BR>
 *<B>Assertion Description: </B>
 * PersistenceManagerFactory.supportedOptions() returns a Collection
of String, each String instance representing an optional feature of the
implementation or a supported query language. 
 */

/* 
 * Revision History
 * ================
 * Author         :   Linga Neerathilingam
 * Date   :  10/22/01
 *
 */

public class SupportedOptions extends JDO_Test {
    
    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A11.5-1 (SupportedOptions) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(SupportedOptions.class);
    }

    /** */
    public void test() {
        PersistenceManagerFactory pmf = getPMF();
        if (debug) logger.debug("Options supported by this implementation:");
        Collection<String> c = pmf.supportedOptions();
        for (String option : c) {
            if (debug) logger.debug(option);
        }
    }  
}

