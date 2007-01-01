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

package org.apache.jdo.impl.fostore;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.jdo.Extent;
import javax.jdo.JDOUserException;
import javax.jdo.PersistenceManager;

import org.apache.jdo.impl.pm.PersistenceManagerWrapper;
import org.apache.jdo.pm.PersistenceManagerInternal;
import org.apache.jdo.util.I18NHelper;


/**
* Provides information such as metadata, extents, objects about the store.
* <pre>
* FOStore dumper program usage:
*     -DdumpOption=OPTION -DclassNames=CLASSNAMES
* OPTION parameter can be one of the following:
*     dbInfo: prints general information about the store.
*     classMetadata: prints the metadata for the classes CLASSNAMES.
*     classInstances: prints all instances of the classes CLASSNAMES.
*     classSubclasses: prints all information about the subclasses 
*     of the classes CLASSNAMES.
* </pre>
* This class is <code>public</code> because it has a <code>main</code> entry
* point for running as a standalone program.
*
* @author Markus Fuchs
* @author Dave Bristor
*/
public class Dumper {

    /** Maps <code>DumpOptions</code> to
     * <code>DumpOptionSubRequests</code>. The option table must match
     * the <code>optionTable</code> in <code>DumpHandler</code>.
     * <p>
     * Because there is no non-static state to be shared between
     * <code>Dumper</code> and the <code>DumpOptionSubRequest</code>s, 
     * the <code>optionTable</code> can be initilialzed only once.
     * @see org.apache.jdo.impl.fostore.DumpOption
     */
    private static final HashMap optionTable = new HashMap();

    private static FOStorePMF pmf;

    /** Class names to dump informations about. */
    private static String classNames;

    /** I18N support. */
    private static final I18NHelper msg = I18NHelper.getInstance(I18N.NAME);

    /**
     * Initializes the <code>optionTable</code>.
     * The option table must match the <code>optionTable</code> in
     * <code>DumpHandler</code>.
     * @see org.apache.jdo.impl.fostore.DumpHandler
     */
    static {
        optionTable.put(DumpOption.DBINFO,
                        new DBInfoRequest());
        optionTable.put(DumpOption.CLASS_METADATA,
                        new ClassMetadataRequest());
        optionTable.put(DumpOption.CLASS_INSTANCES,
                        new ClassInstancesRequest());
        optionTable.put(DumpOption.CLASS_SUBCLASSES,
                        new ClassSubclassesRequest());
    }

    /**
     * Given a command line argument that specifies what information
     * to dump, gets that information from the database and prints it
     * on standard output.
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        String optionName = System.getProperty("dumpOption"); // NOI18N
        if (null == optionName) {
            exit(msg.msg("MSG_MissingDumpOption")); // NOI18N
        }
        classNames = System.getProperty("classNames"); // NOI18N

        try {
            DumpOption option = DumpOption.forName(optionName);
            
            DumpOptionSubRequest r = 
                (DumpOptionSubRequest)optionTable.get(option);
            r.run();
        } catch (Exception ex) {
            exit(msg.msg("MSG_DumperException", ex)); // NOI18N
        }
    }

    /**
     * Abstract class for dumping database information. Clients create
     * instances of subclasses and then invoke <code>run</code>. Subclasses
     * implement <code>dump</code> method to do the real work.
     */
    static abstract class DumpOptionSubRequest {
        protected PersistenceManager pm;
        protected FOStoreStoreManager srm;
        protected StringTokenizer st;

        /** Called by clients to cause information to be dumped.
         */
        void run() {
            try {
                setupPMF();
                pm = pmf.getPersistenceManager();
                srm = (FOStoreStoreManager)pmf.getStoreManager(pm);
                dump();
            } catch (Exception ex) {
                exit(msg.msg("MSG_DumperException", ex)); // NOI18N
            }
        }

        /** Subclasses must implement, to dump their particular kind of info.
         */
        protected abstract void dump();
    }

    /**
     * DumpRequest that can dump DBInfo.
     */
    static class DBInfoRequest extends DumpOptionSubRequest {
        protected void dump() {
            println(srm.dump(DumpOption.DBINFO, "")); // NOI18N
        }
    }

    /**
     * Abstract DumpRequest that assists in dumping
     * information about classes.
     */
    static abstract class ClassRequest extends DumpOptionSubRequest {
        protected void dump() {
            StringTokenizer st = 
                new StringTokenizer(classNames, ","); // NOI18N
            while (st.hasMoreElements()) {
                subDump((String)st.nextElement());
            }
        }

        /** Subclasses must implement, to dump their particular kind
         * of info.
         */
        protected abstract void subDump(String className);
    }

    /**
     * DumpRequest that dumps metadata about a class.
     */
    static class ClassMetadataRequest extends ClassRequest {
        protected void subDump(String className) {
            println(srm.dump(DumpOption.CLASS_METADATA, className));
        }
    }

    /**
     * DumpRequest that dumps information about the instances
     * of a class.
     */
    static class ClassInstancesRequest extends ClassRequest {
        protected void subDump(String className) {
            int objCount = 0;
            Class cls = null;
            try {
                cls = Class.forName(className);
            } catch (ClassNotFoundException ex) {
                throw new JDOUserException(
                  msg.msg("EXC_InstantiateClass", className)); // NOI18N
            }

            println("\n" + msg.msg("MSG_ExtentName", className)); // NOI18N
            Extent e = pm.getExtent(cls, false);
            for (Iterator i = e.iterator(); i.hasNext();) {
                Object pc = i.next();
                println("" + pc); // NOI18N
                objCount++;
            }
            println(msg.msg("MSG_ExtentCount", className, // NOI18N 
                            new Integer(objCount)));
        }
    }

    /**
     * DumpRequest that dumps information about the subclasses
     * of a class.
     */
    static class ClassSubclassesRequest extends ClassRequest {
        protected void subDump(String className) {
            int objCount = 0;
            Class cls = null;
            try {
                cls = Class.forName(className);
            } catch (ClassNotFoundException ex) {
                throw new JDOUserException(
                  msg.msg("EXC_InstantiateClass", className)); // NOI18N
            }

            println(srm.dump(DumpOption.CLASS_SUBCLASSES, className));
        }
    }

    /** Print an error message and exit.
     */
    private static void exit(String message) {
        println(message);
        usage();
        System.exit(1);
    }

    /** Print the usage message on standard output.
     */    
    private static void usage() {
        println(msg.msg("MSG_Usage")); // NOI18N
    }
        
    /**
     * Configures a PMF with some basic properties, and creates the
     * corresponding database.
     */
    private static void setupPMF() throws Exception {
        pmf = new FOStorePMF();
        pmf.setConnectionCreate(false);

        pmf.setConnectionUserName(System.getProperty("user", // NOI18N
                                                     "fred")); // NOI18N
        pmf.setConnectionPassword(System.getProperty("password", // NOI18N
                                                     "wombat")); // NOI18N

        // Create url in string form.
        String path = ""; // NOI18N

        String dir = System.getProperty("dir"); // NOI18N
        if (null == dir) {
            dir = System.getProperty("user.dir"); // NOI18N
        }

        String name = System.getProperty("name"); // NOI18N
        if (null == name) {
            name = "FOStoreTestDB"; // NOI18N
        }

        path += dir + File.separator;
        path += name;
        
        path = "fostore:" + path; // NOI18N

        pmf.setConnectionURL(path);
    }

    /** Print a message on the standard output.
     * @param s the message to print.
     */    
    private static void println(String s) {
        System.out.println(s);
    }

}
