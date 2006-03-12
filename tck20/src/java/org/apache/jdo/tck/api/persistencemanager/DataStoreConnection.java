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

package org.apache.jdo.tck.api.persistencemanager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.Collection;
import java.util.HashSet;

import javax.jdo.datastore.JDOConnection;

import org.apache.jdo.tck.pc.mylib.PCPoint;

import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> DataStoreConnection
 *<BR>
 *<B>Keywords:</B>
 *<BR>
 *<B>Assertion ID:</B> A12.5.2-1
 *<BR>
 *<B>Assertion Description: </B>
In order for the application to perform some 
datastore-specific functions, such as to execute 
a query that is not directly supported by JDO, 
applications might need access to the 
datastore connection used by the JDO implementation. 
This method returns a wrapped 
connection that can be cast to the appropriate 
datastore connection and used by the application. 
The capability to get the datastore connection is 
indicated by the optional feature string 
javax.jdo.option.GetDataStoreConnection. 

 */

public class DataStoreConnection extends PersistenceManagerTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A12.5.2-1 (DataStoreConnection) failed: ";
    
    protected PCPoint goldenPoint;
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(DataStoreConnection.class);
    }
    
    /** */
    protected void localSetUp() {
        addTearDownClass(PCPoint.class);
        PCPoint point = new PCPoint(50, 100);
        goldenPoint = new PCPoint(point.getX(), point.getY());
        getPM().currentTransaction().begin();
        pm.makePersistent(point);
        pm.currentTransaction().commit();
    }

    /** */
    public void testDataStoreConnection() {
        if (!(isDataStoreConnectionSupported() && isSQLSupported())) {
            printUnsupportedOptionalFeatureNotTested(
                    this.getClass().getName(),
                    "getDataStoreConnection AND SQLSupported.");
            return;
        }
        String schema = getPMFProperty("javax.jdo.mapping.Schema");
        String sql = "SELECT X, Y FROM " + schema + ".PCPoint";
        JDOConnection jconn = pm.getDataStoreConnection();
        Connection conn = (Connection)jconn;
        try {
            getPM().currentTransaction().begin();
            if (conn.getAutoCommit()) {
                appendMessage(ASSERTION_FAILED + 
                        "Autocommit must not be true in JDO connection.");
            };
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            Collection actuals = new HashSet();
            while (rs.next()) {
                PCPoint p = new PCPoint(rs.getInt(1), rs.getInt(2));
                actuals.add(p);
            }
            if (actuals.size() != 1) {
                appendMessage(ASSERTION_FAILED + "Wrong size of result of " +
                        sql + NL + "expected: 1, actual: " + actuals.size());
            } else {
                PCPoint actual = (PCPoint)actuals.iterator().next();
                if (goldenPoint.getX() != actual.getX() ||
                        !goldenPoint.getY().equals(actual.getY())) {
                    appendMessage(ASSERTION_FAILED + 
                            "Wrong values of PCPoint from SQL" +
                            "expected x: " + goldenPoint.getX() +
                            ", y: " + goldenPoint.getX() + NL +
                            "actual x: " + actual.getX() +
                            ", y: " + actual.getX()
                            );
                }
            }
        } catch (Exception ex) {
            appendMessage(ASSERTION_FAILED + " caught exception:" + ex);
        } finally {
            jconn.close();
            failOnError();
        }
    }
}
