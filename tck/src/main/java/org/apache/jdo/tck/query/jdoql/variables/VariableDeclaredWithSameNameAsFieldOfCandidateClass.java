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

package org.apache.jdo.tck.query.jdoql.variables;

import javax.jdo.JDOUserException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import org.apache.jdo.tck.pc.company.Project;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Variable Declared with Same Name as Field of Candidate Class
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.4-3.
 *<BR>
 *<B>Assertion Description: </B>
A field of the candidate class of a <code>Query</code> can be hidden if a
variable is declared with the same name.

 */

public class VariableDeclaredWithSameNameAsFieldOfCandidateClass extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.4-3 (VariableDeclaredWithSameNameAsFieldOfCandidateClass) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(VariableDeclaredWithSameNameAsFieldOfCandidateClass.class);
    }

    /** */
    public void test() {
        pm = getPM();

        checkQueryVariables(pm);

        pm.close();
        pm = null;
    }

    /** */
    void checkQueryVariables(PersistenceManager pm) {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            Query<Project> query = pm.newQuery(Project.class);
            query.setCandidates(pm.getExtent(Project.class, false));
            try {
                query.declareVariables( "org.apache.jdo.tck.pc.company.Person reviewers;" );
                query.setFilter( "reviewers.contains(reviewers)" );
                query.execute();
                fail(ASSERTION_FAILED,
                     "Variable declaration \"Person reviewers\" did not hide field Person.reviewers");
            }
            catch (JDOUserException e) {
                // expected exception
                if (debug) logger.debug( "Caught expected " + e);
            }
        } 
        finally {
            if (tx.isActive())
                tx.rollback();
        }

        try {
            tx.begin();
            Query<Project> query = pm.newQuery(Project.class);
            query.setCandidates(pm.getExtent(Project.class, false));
            query.declareVariables( "org.apache.jdo.tck.pc.company.Person reviewers;" );
            query.setFilter( "this.reviewers.contains(reviewers) && reviewers.firstname==\"brazil\"" );
            query.execute();
        } 
        finally {
            if (tx.isActive())
                tx.rollback();
        }
    }
}
