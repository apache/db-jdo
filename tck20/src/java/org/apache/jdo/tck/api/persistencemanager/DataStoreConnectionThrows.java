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

package org.apache.jdo.tck.api.persistencemanager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import javax.jdo.datastore.JDOConnection;

import org.apache.jdo.tck.pc.mylib.PCPoint;

import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> DataStoreConnectionThrows
 *<BR>
 *<B>Keywords:</B>
 *<BR>
 *<B>Assertion ID:</B> A12.16-2
 *<BR>
 *<B>Assertion Description: </B>
For portability, a JDBC-based JDO implementation 
will return an instance that implements 
java.sql.Connection. The instance 
will throw an exception for any of the 
following method calls: commit, getMetaData, 
releaseSavepoint, rollback, setAutoCommit, 
setCatalog, setHoldability, setReadOnly, 
setSavepoint, setTransactionIsolation, and 
setTypeMap.
 */

public class DataStoreConnectionThrows extends PersistenceManagerTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A12.16-2 (DataStoreConnectionThrows) failed: ";
    
    protected PCPoint goldenPoint;
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(DataStoreConnectionThrows.class);
    }
    
    /** */
    protected void checkThrow(Connection conn, Call call) {
        try {
            call.execute(conn);
            appendMessage(ASSERTION_FAILED +
                    "Failed to throw an exception for " + call.getName());
        } catch (SQLException ex) {
            appendMessage(ASSERTION_FAILED +
                    "Threw a SQLException for " + call.getName() + NL + ex);
            return;
        } catch (Exception ex) {
            return;
        }
    }

    /** */
    interface Call {
        String getName();
        void execute(Connection conn) throws SQLException;
    }

    /** */
    public void testDataStoreConnectionThrows() {
        if (!(isDataStoreConnectionSupported() && isSQLSupported())) {
            printUnsupportedOptionalFeatureNotTested(
                    this.getClass().getName(),
                    "getDataStoreConnection AND SQLSupported.");
            return;
        }
        JDOConnection jconn = getPM().getDataStoreConnection();
        Connection conn = (Connection)jconn;
        check13Methods(conn);
        if (isJRE14orBetter()) {
            check14Methods(conn);
        }
        jconn.close();
        failOnError();
    }

   /** 
    * These methods are defined in Java 1.3 Connection.
    */
    protected void check13Methods(Connection conn) {
        checkThrow(conn,
                new Call() {
                    public String getName() {return "commit";}
                    public void execute(Connection conn) 
                        throws SQLException {conn.commit();}
                }
            );
        checkThrow(conn,
                new Call() {
                    public String getName() {return "rollback";}
                    public void execute(Connection conn) 
                        throws SQLException {conn.rollback();}
                }
            );
        checkThrow(conn,
                new Call() {
                    public String getName() {return "setTransactionIsolation";}
                    public void execute(Connection conn) 
                        throws SQLException {
                        conn.setTransactionIsolation(
                                Connection.TRANSACTION_READ_COMMITTED);}
                }
            );
        checkThrow(conn,
                new Call() {
                    public String getName() {return "setAutoCommit";}
                    public void execute(Connection conn) 
                        throws SQLException {conn.setAutoCommit(true);}
                }
            );
        checkThrow(conn,
                new Call() {
                    public String getName() {return "setCatalog";}
                    public void execute(Connection conn) 
                        throws SQLException {conn.setCatalog("NONE");}
                }
            );
    }

    /**
     * These methods are defined in Java 1.4 Connection.
     */
    protected void check14Methods(Connection conn) {
        checkThrow(conn,
                new Call() {
                    public String getName() {return "setSavepoint";}
                    public void execute(Connection conn) 
                        throws SQLException {conn.setSavepoint();}
                }
            );
        checkThrow(conn,
                new Call() {
                    public String getName() {return "releaseSavepoint";}
                    public void execute(Connection conn) 
                        throws SQLException {conn.releaseSavepoint(null);}
                }
            );
        checkThrow(conn,
                new Call() {
                    public String getName() {return "setHoldability";}
                    public void execute(Connection conn) 
                        throws SQLException {
                        conn.setHoldability(
                                ResultSet.CLOSE_CURSORS_AT_COMMIT);}
                }
            );
        checkThrow(conn,
                new Call() {
                    public String getName() {return "setTypeMap";}
                    public void execute(Connection conn) 
                        throws SQLException {conn.setTypeMap(new HashMap());}
                }
            );
     }
}
