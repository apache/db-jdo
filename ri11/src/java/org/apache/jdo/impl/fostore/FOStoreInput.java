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

package org.apache.jdo.impl.fostore;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;

import org.apache.jdo.util.I18NHelper;


/**
* Extend ByteArrayInputStream so that we can get ahold of the byte array
* and current position, and can change the current position.
*
* @author Dave Bristor
*/
class FOStoreInput extends ByteArrayInputStream implements DataInput {
    private final DataInputStream dis;

    /** I18N support. */
    private static final I18NHelper msg = I18NHelper.getInstance(I18N.NAME);
    
    FOStoreInput(byte data[], int off, int len) {
        super(data, off, len);
        dis = new DataInputStream(this);
    }

    byte[] getBuf() {
        return buf;
    }

    int getPos() {
        return pos;
    }

    void setPos(int pos) {
        if (pos < 0 || pos > buf.length) {
            throw new FOStoreFatalInternalException(
                this.getClass(), "setPos", // NOI18N
                msg.msg("ERR_InvalidSeekPos", // NOI18N
                        new Integer(pos), new Integer(buf.length)));
        }
        this.pos = pos;
    }

    // Advance pos by len, but don't break invariant required by
    // ByteArrayInputStream (see ByteArrayInputStrea.pos's javadoc).
    void advance(int len) {
        if (pos + len <= count) {
            pos += len;
        } else {
            pos = count;
        }
    }

    //
    // Implement DataInput
    //

    public boolean readBoolean() throws IOException {
        return dis.readBoolean();
    }

    public byte readByte() throws IOException {
        return dis.readByte();
    }

    public char readChar() throws IOException {
        return dis.readChar();
    }

    public double readDouble() throws IOException {
        return dis.readDouble();
    }

    public float readFloat() throws IOException {
        return dis.readFloat();
    }

    public void readFully(byte[] b) throws IOException {
        dis.readFully(b);
    }

    public void readFully(byte[] b, int off, int len) throws IOException {
        dis.readFully(b, off, len);
    }

    public int readInt() throws IOException {
        return dis.readInt();
    }

    public String readLine() throws IOException {
        return dis.readLine();
    }

    public long readLong() throws IOException {
        return dis.readLong();
    }

    public short readShort() throws IOException {
        return dis.readShort();
    }

    public int readUnsignedByte() throws IOException {
        return dis.readUnsignedByte();
    }

    public int readUnsignedShort() throws IOException {
        return dis.readUnsignedShort();
    }

    public String readUTF() throws IOException {
        return dis.readUTF();
    }

    public int skipBytes(int n) throws IOException {
        return dis.skipBytes(n);
    }
}
