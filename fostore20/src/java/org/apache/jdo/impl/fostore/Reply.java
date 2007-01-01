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
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;

import javax.jdo.JDOFatalUserException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.jdo.util.I18NHelper;

/**
* Represents the result of processing a request.
*
* @author Dave Bristor
*/
class Reply implements DataOutput {
    // Id of the request to which this Reply corresponds.
    private final RequestId requestId;

    // Output to which reply data is written.
    private final FOStoreOutput out;

    // Status value of this Reply
    private Status status;
    
    // Position in Reply's buffer at which Status is written.
    private final int statusPosition;

    // Position in Reply's buffer after at which the Reply's actual data
    // starts.
    private final int replyDataPosition;

    // Once closed, disallow further writes.  A Reply closes once status is
    // set on it.
    private boolean closed = false;

    /**  The version number of the current protocol.  In future, this version
     * number can be used to identify mismatches in protocol.  The format is
     * (short)major; (byte)minor; (byte)patch Only use major for compatibility
     * checks; always bump major when incompatibly changing protocol.
     * <p>
     * Note that this version number corresponds to that used in the entire
     * stream of reply data, not for an individual reply.
     */
    private static final int VERSION_NUMBER = 0x00010000; // version 1.0.0

    /** I18N support. */
    private static final I18NHelper msg = I18NHelper.getInstance(I18N.NAME);

    /** Logger */
    static final Log logger = LogFactory.getFactory().getInstance(
        "org.apache.jdo.impl.fostore"); // NOI18N

    // If the setStatus(Status, Throwable) is invoked and verbose is false,
    // include the Throwable's toString(); if true include it's
    // printStackTrace().
    private static boolean verbose = false;

    static {
        try {
            String property = (String)AccessController.doPrivileged(
                new PrivilegedAction () {
                    public Object run () {
                        return System.getProperty("status.verbose"); // NOI18N
                    }
                }
                );
            verbose = Boolean.valueOf(property).booleanValue();
        } catch (SecurityException ex) {
            // cannot read verbose flag => log warning
            if (logger.isWarnEnabled())
                logger.warn(msg.msg("MSG_CannotGetSystemProperty", //NOI18N
                    "status.verbose", ex.toString())); //NOI18N
        }
    }

    Reply(RequestId requestId, FOStoreOutput out) throws IOException {
        this.requestId = requestId;
        this.out = out;
        
        requestId.write(out);

        // Save position in stream where we will write status, length.  We
        // will replace these with the real values in setStatus().
        this.statusPosition = out.getPos();

        Status.initialize(this);
        
        // Will write position of Reply's error message here.  If 0, then
        // there is no message.
        out.writeInt(0x10badbad);
        
        // will write Reply's length here.  This is of the reply-specific
        // data, and does not include the length of the message.
        out.writeInt(0x10badbad);

        this.replyDataPosition = out.getPos();
    }

    /**
     * Indicate whether status reported to client is to be verbose or not.
     */
    static String getExceptionMessage(Throwable t) {
        String rc = ""; // NOI18N
        if (null != t) {
            if (true) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                t.printStackTrace(pw);
                rc = sw.toString();
            } else {
                rc = t.toString();
            }
        }
        return rc;
    }

    /**
    * Sets the status of this reply.  Status can be set only one time.
    * Once it has been set, further write methods will throw a 
    * FOStoreFatalInternalException.
    * @param status Indication of result of processing a request.
    */
    void setStatus(Status status) throws IOException {
        setStatus(status, (String)null);
    }

    /**
    * Sets the status of this reply.  Status can be set only one time.
    * Once it has been set, further write methods will throw a
    * FOStoreFatalInternalException.
    * @param status Indication of result of processing a request.
    * @param t Detail on cause of errors.
    */
    void setStatus(Status status, Throwable t) throws IOException {
        String message = getExceptionMessage(t);
        setStatus(status, message);
    }

    /**
    * Sets the status of this reply.  Status can be set only one time.
    * Once it has been set, further write methods will throw a
    * FOStoreFatalInternalException.
    * @param status Indication of result of processing a request.
    * @param message detailed message.
    * @param t Detail on cause of errors.
    */
    void setStatus(Status status, String message, Throwable t)
        throws IOException {
        String throwableMessage = getExceptionMessage(t);
        setStatus(status, message+throwableMessage);
    }

    /**
    * Sets the status of this reply.  Status can be set only one time.
    * Once it has been set, further write methods will throw a
    * FOStoreFatalInternalException.
    * @param status Indication of result of processing a request.
    * @param message Detail on cause of errors.
    */
    void setStatus(Status status, String message) throws IOException {

        assertNotClosed();
        closed = true;

        this.status = status;

        // Save end-of-stream position
        int savedPos = out.getPos();

        // Write status, message position, and reply data length, as was
        // provided for in constructor.
        out.setPos(statusPosition);

        // Status
        status.write(out);

        // Message length
        if (null == message) {
            out.writeInt(0);
        } else {
            out.writeInt(message.length());
        }

        // Reply data length
        int dataLength = savedPos - replyDataPosition;
        out.writeInt(dataLength);

        // Seek back to the end of the reply data, and write message
        out.setPos(savedPos);
        if (null != message) {
            out.writeUTF(message);
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug("Reply: Created for " + requestId + // NOI18N
                           ", length=" + dataLength + // NOI18N
                           ", status=" + status + // NOI18N
                           ", message=" + message); // NOI18N
        }
    }

    /**
    * @return The Status value of the reply.  May return null.
    */
    Status getStatus() {
        return status;
    }

    /**
     * Write the version number at the current position.
     */
    static void writeVersionNumber(DataOutput out) throws IOException {
        out.writeInt(VERSION_NUMBER);
    }

    /**
     * Verify the Reply version number.
     * @throws JDOFatalUserException if the version number does not match
     * that in the caller's JVM.
     */
    static void verifyVersionNumber(DataInput in) throws IOException {
        int verNum = in.readInt();
        if (VERSION_NUMBER != verNum) {
            throw new JDOFatalUserException(
                msg.msg("EXC_ReplyVersionMismatch", // NOI18N
                        new Integer(verNum), new Integer(VERSION_NUMBER)));
        }
    }

    /**
    * Writes an OID in this reply at the current position.
    * @param oid The OID which is written.
    */
    void writeOID(OID oid) throws IOException {
        assertNotClosed();
        oid.write(out);
    }

    /**
    * Writes a CLID in this reply at the current position.
    * @param clid The CLID which is written.
    */
    void writeCLID(CLID clid) throws IOException {
        assertNotClosed();
        clid.write(out);
    }

    /**
    * @see FOStoreOutput#beginStash
    */
    int beginStash() throws IOException {
        return out.beginStash();
    }

    /**
    * @see FOStoreOutput#endStash
    */
    void endStash(int length, int pos) throws IOException {
        out.endStash(length, pos);
    }
    
    //
    // Allow seeking in output
    //

    /** @return Current position in Reply's output stream.
     */
    int getPos() {
        return out.getPos();
    }

    /** @param pos New postion in Reply's output stream.
     */
    void setPos(int pos) throws IOException {
        out.setPos(pos);
    }

    //
    // Implement DataOutput
    //

    public void write(byte[] b) throws IOException {
        assertNotClosed();
        out.write(b);
    }

    public void write(int b) throws IOException {
        assertNotClosed();
        out.write(b);
    }

    public void write(byte[] b, int off, int len) throws IOException {
        assertNotClosed();
        out.write(b, off, len);
    }

    public void writeBoolean(boolean v) throws IOException {
        assertNotClosed();
        out.writeBoolean(v);
    }

    public void writeByte(int v) throws IOException {
        assertNotClosed();
        out.writeByte(v);
    }

    public void writeBytes(String s) throws IOException {
        assertNotClosed();
        out.writeBytes(s);
    }

    public void writeChar(int v) throws IOException {
        assertNotClosed();
        out.writeChar(v);
    }

    public void writeChars(String s) throws IOException {
        assertNotClosed();
        out.writeChars(s);
    }

    public void writeDouble(double v) throws IOException {
        assertNotClosed();
        out.writeDouble(v);
    }

    public void writeFloat(float v) throws IOException {
        assertNotClosed();
        out.writeFloat(v);
    }

    public void writeInt(int v) throws IOException {
        assertNotClosed();
        out.writeInt(v);
    }

    public void writeLong(long v) throws IOException {
        assertNotClosed();
        out.writeLong(v);
    }

    public void writeShort(int v) throws IOException {
        assertNotClosed();
        out.writeShort(v);
    }

    public void writeUTF(String str) throws IOException {
        assertNotClosed();
        out.writeUTF(str);
    }   

    //
    // Private implementation methods
    //
    
    private void assertNotClosed() {
        if (closed) {
            throw new FOStoreFatalInternalException(
                getClass(), "assertNotClosed", // NOI18N
                msg.msg("ERR_Closed")); // NOI18N
        }
    }
}
