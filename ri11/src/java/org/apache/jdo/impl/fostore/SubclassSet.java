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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Iterator;

import org.netbeans.mdr.persistence.Streamable;
import org.netbeans.mdr.persistence.StorageException;
import org.netbeans.mdr.persistence.StorageIOException;

/**
* Represents a set of CLIDs of subclasses of a given class.
* <p>
* This class is <code>public</code> so that it can be used as a
* <code>Streamable</code> and stored in the database.
*
* @author Dave Bristor
*/
public class SubclassSet implements Streamable {
    /** The oid of this list of subclasses. */
    private OID oid;

    /** List of the clids which represent classes that are subclasses of the
    * class indicated by the CLID in our oid. */
    private HashSet clids;

    /**
    * Given clid is the first entry in the list.
    */
    private SubclassSet(OID oid, CLID clid) {
        this.oid = oid;
        clids = new HashSet();
        clids.add(clid);
    }

    static SubclassSet create(OID oid, CLID clid) {
        return new SubclassSet(oid, clid);
    }

    /**
    * Add the given clid to the list.
    */
    void add(CLID clid) {
        clids.add(clid);
    }

    /**
    * @return Iterator over the CLID's in this subclass list.
    */
    Iterator iterator() {
        return clids.iterator();
    }        

    //
    // Implement Streamable
    //

    public SubclassSet() { }

    /**
    * Write this SubclassSet to the given stream.
    */
    public void write(OutputStream os) throws StorageException {
        DataOutputStream dos = new DataOutputStream(os);

        try {
            oid.write(dos);
            int size = clids.size();
            dos.writeInt(size);
            for (Iterator i = clids.iterator(); i.hasNext();) {
                CLID clid = (CLID)i.next();
                clid.write(dos);
            }
        } catch (IOException ex) {
            throw new StorageIOException(ex);
        }
    }

    /**
    * Initialize this SubclassSet from the given stream.
    */
    public void read(InputStream is) throws StorageException {
        DataInputStream dis = new DataInputStream(is);
        try {
            this.oid = OID.read(dis);
            int size = dis.readInt();
            clids = new HashSet(size);
            for (int i = 0; i < size; i++) {
                clids.add(CLID.read(dis));
            }
        } catch (IOException ex) {
            throw new StorageIOException(ex);
        }
    }
}
