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
 
package org.apache.jdo.tck.query.jdoql.subqueries;

import org.apache.jdo.tck.query.QueryTest;

/**
 * Superclass for all subquery test classes.
 */
public abstract class SubqueriesTest extends QueryTest {

    /** */
    public static final String SUBQUERIES_TEST_COMPANY_TESTDATA = 
        "org/apache/jdo/tck/pc/company/companyForSubqueriesTests.xml";

    /**
     * Returns the name of the company test data resource.
     * @return name of the company test data resource. 
     */
    protected String getCompanyTestDataResource() {
        return SUBQUERIES_TEST_COMPANY_TESTDATA;
    }
}
