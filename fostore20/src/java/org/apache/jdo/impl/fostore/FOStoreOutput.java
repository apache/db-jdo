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
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;

import org.apache.jdo.util.I18NHelper;


/**
* Extend ByteArrayOutputStream so that we can get ahold of the byte array
* and current position, and can make sure we have enough space to write an
* object.  We also allow getting and changing the current position.  Also,
* implement DataOutput so that we can write easily to this output.
*
* @author Dave Bristor
*/
class FOStoreOutput implements DataOutput {
    // Once closed, no more writing allowed.
    private boolean closed = false;
    
    private final LocalByteArrayOutputStream stream;
    private final DataOutputStream dos;

    /** I18N support. */
    private static final I18NHelper msg = I18NHelper.getInstance(I18N.NAME);
    
    FOStoreOutput() {
        stream = new LocalByteArrayOutputStream();
        dos = new DataOutputStream(stream);
    }

    /** Close the stream.  The stream can no longer be written.
     */
    public void close() {
        closed = true;
    }
    
    /** Reset the stream.  Discard the current contents, and reset the count
     * to 0 and the current position to 0.  The current buffer is retained.
     */
    public void reset() {
        stream.reset();
    }

    // In a simpler world, FOStoreOutput would extend ByteArrayOutputStream
    // and implement DataOutput.  However.  The creators of the early Java
    // classes wisely noted that, in ByteArrayOutputStream, the write methods
    // could not throw IOException, after all, there's no I/O happening.  Of
    // course, with DataOutput, you never know what's behind the scenes, so
    // it's write methods *do* throw IOException.  Since we need the
    // functionality of both, and these are in conflice, we cannot both
    // extend ByteArrayOutputStream *and* implement DataOutput.  So we
    // implement DataOutput and delegate to this extension.
    //
    class LocalByteArrayOutputStream extends ByteArrayOutputStream {
        LocalByteArrayOutputStream() {
            super();
        }

        byte[] getBuf() {
            return buf;
        }

        int getCurrentPosition() {
            return count;
        }

        void seek(int pos) {
            if (pos < 0 || pos > buf.length) {
                throw new FOStoreFatalInternalException(
                    this.getClass(), "seek", // NOI18N
                    msg.msg("ERR_InvalidSeekPos", // NOI18N
                            new Integer(pos), new Integer(buf.length)));
            }
            this.count = pos;
        }
    }

    /**
    * Provides no-copy access to the buffer.
    * @return The byte array representing this stream.  <em>Not a copy.</em>
    */
    byte[] getBuf() {
        return stream.getBuf();
    }

    //
    // A common need of RequestHandlers is to write a nonsense number, which
    // is later filled in with a count or length (etc.) appropriate to the
    // reply's acutal data.  These help do that.  Use them instead of
    // getPos/setPos, if/when you can.
    //
    // This is easy to do by hand, but I've burned myself too many times
    // by being imprecise when doing it that way!
    //

    /**
    * Write a nonsense int value at the current position, and return that
    * position for later use with endStash
    * @return Position in this output for later use in writing a 'real'
    * value.
    * @see #endStash
    */
    int beginStash() throws IOException {
        int rc = getPos();
        writeInt(0xbadbad10);
        return rc;
    }

    /**
    * Write the given value at the given position, and reset the position to
    * what it was before the write occurred.
    * @param value Value to be written
    * @param pos Position in this output at which value is to be written
    * @see #beginStash
    */
    void endStash(int value, int pos) throws IOException {
        int savedPos = getPos();
        setPos(pos);
        writeInt(value);
        setPos(savedPos);
    }
    
    /**
    * Provides the stream's current writing position.
    * @return The current writing position of the stream.
    */
    int getPos() {
        return stream.getCurrentPosition();
    }

    /**
    * Allows for setting the current writing position.
    * @param pos Position at which future write operations will take
    * place.
    */
    void setPos(int pos) throws IOException {
        stream.seek(pos);
    }
    
    //
    // Implement DataOutput by forwarding onto our private DataOutputStream
    //

    public void write(byte[] b) throws IOException {
        assertNotClosed();
        dos.write(b);
    }

    public void write(int b) throws IOException {
        assertNotClosed();
        dos.write(b);
    }

    public void write(byte[] b, int off, int len) throws IOException {
        assertNotClosed();
        dos.write(b, off, len);
    }

    public void writeBoolean(boolean v) throws IOException {
        assertNotClosed();
        dos.writeBoolean(v);
    }

    public void writeByte(int v) throws IOException {
        assertNotClosed();
        dos.writeByte(v);
    }

    public void writeBytes(String s) throws IOException {
        assertNotClosed();
        dos.writeBytes(s);
    }

    public void writeChar(int v) throws IOException {
        assertNotClosed();
        dos.writeChar(v);
    }

    public void writeChars(String s) throws IOException {
        assertNotClosed();
        dos.writeChars(s);
    }

    public void writeDouble(double v) throws IOException {
        assertNotClosed();
        dos.writeDouble(v);
    }

    public void writeFloat(float v) throws IOException {
        assertNotClosed();
        dos.writeFloat(v);
    }

    public void writeInt(int v) throws IOException {
        assertNotClosed();
        dos.writeInt(v);
    }

    public void writeLong(long v) throws IOException {
        assertNotClosed();
        dos.writeLong(v);
    }

    public void writeShort(int v) throws IOException {
        assertNotClosed();
        dos.writeShort(v);
    }

    public void writeUTF(String str) throws IOException {
        assertNotClosed();
        dos.writeUTF(str);
    }   

    //
    // Private implementation methods
    //
    
    private void assertNotClosed()  {
        if (closed) {
            throw new FOStoreFatalInternalException(
                getClass(), "assertNotClosed", // NOI18N
                msg.msg("ERR_Closed")); // NOI18N
        }
    }
}
