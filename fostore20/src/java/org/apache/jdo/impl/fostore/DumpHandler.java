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

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

import org.apache.jdo.util.I18NHelper;


/**
* Process dump requests. A dump request provides information about the
* store. Currently, two options are supported:
* <ul>
* <li>dbInfo: Provide information about all classes stored in the store.</li>
* <li>ClassMetadata: Provide the stored metadata information for the class
* <code>className</code>.</li>
* </ul>
*
* @see org.apache.jdo.impl.fostore.DumpOption
* @author Markus Fuchs
*/
// This is server-side code.  It does not need to live in the client.
class DumpHandler extends RequestHandler {

    /** Maps <code>DumpOptions</code> to
     * <code>DumpOptionSubHandlers</code>. The option table must match
     * the <code>optionTable</code> in <code>Dumper</code>.
     * <p>
     * Because the <code>DumpOptionSubHandler</code> are bound to 
     * the state of the enclosing <code>DumpHander</code> instance, the
     * <code>optionTable</code> has to be non-static. */
    private static HashMap optionTable = new HashMap();

    /** List of results. A SubRequest might report more than one result. */
    private final ArrayList results = new ArrayList();

    /** Return status. */
    private Status status;

    /** I18N support. */
    private static final I18NHelper msg = I18NHelper.getInstance(I18N.NAME);

    /** Initializes the <code>optionTable</code>.
     * The option table must match the <code>optionTable</code> in
     * <code>Dumper</code>.
     * @see Dumper
     */
    // Note that there is no entry for DumpOption.CLASS_INSTANCES: that option
    // is handled entirely by the Dumper on the client side, with help of
    // FOStoreStoreManager.getExtent.
    private void initOptionTable() {
        optionTable.put(DumpOption.DBINFO,
                        new DBInfoHandler());
        optionTable.put(DumpOption.CLASS_METADATA,
                        new ClassMetadataHandler());
        optionTable.put(DumpOption.CLASS_SUBCLASSES,
                        new ClassSubclassesHandler());
    }

    private DumpHandler(Reply reply, int length,
                         FOStoreServerConnection con) {

        super(reply, length, con);
        initOptionTable();
    }
    
    public static final HandlerFactory factory =
        new HandlerFactory() {
                public RequestHandler getHandler(Reply reply, int length,
                                             FOStoreServerConnection con) {
                return new DumpHandler(reply, length, con);
            }};

    RequestFinisher handleRequest()
        throws IOException, FOStoreDatabaseException {

        FOStoreInput in = con.getInputFromClient();
        FOStoreDatabase db = con.getDatabase();

        // read arguments
        DumpOption option = DumpOption.read(in);
        String className = in.readUTF();
        
        DumpOptionSubHandler h = 
            (DumpOptionSubHandler) optionTable.get(option);
        if (null == h) {
            reply.setStatus(Status.FATAL,msg.msg("ERR_NoSubHandler", option)); // NOI18N
        } else {
            h.run(db, className);

            int size = results.size();
            reply.writeInt(size);
            if (size > 0) {
                for (int i = 0; i < size; i++) {
                    Object o = results.get(i);
                    if (null != o) {
                        reply.writeUTF(o.toString());
                    }
                }
            }
            reply.setStatus(status);
        }

        return null;
    }

    /**
     * Abstract class for dumping database information. The server creates
     * instances of subclasses and then invoke <code>run</code>. Subclasses
     * implement <code>run</code> method to do the real work.
     */
    abstract class DumpOptionSubHandler {

        /** Must be implemented to dump particular kind of info.
         */
        protected abstract void run(FOStoreDatabase db, String className)
            throws IOException, FOStoreDatabaseException;
    }

    /**
     * OptionHandler that dumps DBInfo.
     */
    class DBInfoHandler extends DumpOptionSubHandler {
      protected void run(FOStoreDatabase db, String className) 
          throws IOException, FOStoreDatabaseException {

          results.add(db.getDBInfo());
          status = Status.OK;
        }
    }

    /**
     * Abstract OptionHandler that assists in dumping information about
     * classes.
     */
    abstract class ClassHandler extends DumpOptionSubHandler {
        protected void run(FOStoreDatabase db, String className) 
            throws IOException, FOStoreDatabaseException {

            if (null == className) {
                reply.setStatus(
                    Status.ERROR,
                    msg.msg("MSG_MissingParameter", className)); //NOI18N
            } else {
                _run(db, className);
            }
        }

        DBClass[] getDBClasses(FOStoreDatabase db, String className)
            throws IOException, FOStoreDatabaseException {

            DBClass rc[] = null;

            // look for class info in store
            DBInfo dbInfo = db.getDBInfo();
            ArrayList found = new ArrayList();
            for (Iterator i = dbInfo.getDBClasses(); i.hasNext();) {
                DBClass dbClass = (DBClass)i.next();
                if (className.equals(dbClass.getName())) {
                    found.add(dbClass);
                }
            }
            int size = found.size();
            if (size > 0) {
                rc = new DBClass[size];
                for (int i = 0; i < size; i++) {
                    rc[i] = (DBClass)found.get(i);
                }
            }
            return rc;
        }

        /** Subclasses must implement, to dump their particular kind of info.
        */
        abstract void _run(FOStoreDatabase db, String className) 
            throws IOException, FOStoreDatabaseException;
    }


    /**
     * OptionHandler that dumps metadata about a class.
     */
    class ClassMetadataHandler extends ClassHandler {
        protected void _run(FOStoreDatabase db, String className) 
            throws IOException, FOStoreDatabaseException {

            DBClass dbClasses[] = getDBClasses(db, className);
            for (int i = 0; i < dbClasses.length; i++) {
                results.add(dbClasses[i]);
            }
            status = Status.OK;
        }
    }

    /**
    * OptionHandler that dumps information about the subclasses of a class.
    */
    class ClassSubclassesHandler extends ClassHandler {
        protected void _run(FOStoreDatabase db, String className)
            throws IOException, FOStoreDatabaseException {

            // Sort results in String order.
            TreeSet sorted = new TreeSet();

            DBClass dbClasses[] = getDBClasses(db, className);
            if (null == dbClasses || 0 == dbClasses.length){
                results.add(
                    msg.msg("MSG_DbClassNotFound", className)); // NOI18N
            } else {
                // For each DBClass known by className, get all of its
                // subclasses.
                for (int i = 0; i < dbClasses.length; i++) {
                    String result = null;
                    CLID clid = dbClasses[i].getCLID();
                    OID ssOID = DBInfo.getSubclassSetOID(clid);
                    SubclassSet sl = (SubclassSet)db.getIfExists(ssOID);
                    if (null == sl) {
                        result = msg.msg("MSG_NoSubclasses", className); // NOI18N
                    } else {
                        result = msg.msg("MSG_Subclasses", className); // NOI18N
                        // Get results into the TreeSet
                        for (Iterator j = sl.iterator(); j.hasNext();) {
                            CLID subCLID = (CLID)j.next();
                            OID subDBClassOID = DBInfo.getDBClassOID(subCLID);
                            DBClass subDBClass =
                                (DBClass)db.get(subDBClassOID);
                            sorted.add("\n\t" + subDBClass.getName()); // NOI18N
                        }

                        // Put sorted subclasses from TreeSet to string
                        for (Iterator k = sorted.iterator(); k.hasNext();) {
                            result += (String)k.next();
                        }
                    }
                    results.add(result);
                }
            }
            status = Status.OK;
        }
    }
}
