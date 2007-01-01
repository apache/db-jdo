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

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import org.netbeans.mdr.persistence.StorageException;
import org.netbeans.mdr.persistence.Streamable;

/**
* Represents an object stored in the database as a semi-opaque value.  While
* it has no methods which allow interesting access to itself, the store has
* metadata which describes its contents.
* <p>
* This class is <code>public</code> so that it can be used as a
* <code>Streamable</code> and stored in the database.
*
* @author Dave Bristor
*/
// XXX PERF Fix this and/or the btree package to avoid the extra copy.
// Right now, we're making a copy of the data in the client's Message.  We
// don't want to do that, eventually; we want to use the bytes that are
// there.
public class Block implements Streamable {
    // Our copy of data from the client
    private byte data[] = null;

    Block(FOStoreInput in, int length) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        
        try {
            dos.write(in.getBuf(), in.getPos(), length);
            data = baos.toByteArray();
            in.advance(length);
        } catch (IOException ex) {
            throw new FOStoreFatalIOException(
                this.getClass(), "constructor(FOStoreInput, int)", ex); // NOI18N
        }
    }

    byte[] getData() {
        return data;
    }
    
    //
    // Implement Streamable
    //
    
    public Block() { }

    public void write(OutputStream os) throws StorageException {
        try {
            DataOutputStream dos = new DataOutputStream(os);
            dos.writeInt(data.length);
            dos.write(data, 0, data.length);
        } catch (IOException ex) {
            throw new FOStoreFatalIOException(
                this.getClass(), "write", ex); // NOI18N
        }
    }

    public void read(InputStream is) throws StorageException {
        try {
            DataInputStream dis = new DataInputStream(is);

            int length = dis.readInt();

            data = new byte[length];
            dis.readFully(data, 0, length);

        } catch (IOException ex) {
            throw new FOStoreFatalIOException(
                this.getClass(), "read", ex); // NOI18N
        }
    }

    // Debug support
    //
    public void dump() {
        Tester.dump("BLK", data, data.length); // NOI18N
    }
}
