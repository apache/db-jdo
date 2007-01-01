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

package javax.jdo.util;

import java.io.PrintStream;

import junit.framework.TestCase;

/** */
public class AbstractTest extends TestCase {

    /** */
    protected static PrintStream out = System.out;
    
    /** If true, print extra messages. */
    protected boolean verbose;

    /**
     * Construct and initialize from properties.
     */
    protected AbstractTest() {
        super(null);
        verbose = Boolean.getBoolean("verbose");
    }
    
    /**
     * Determine if a class is loadable in the current environment.
     */
    protected static boolean isClassLoadable(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException ex) {
            return false;
        }
    }
    
    /**
     */
    protected void println(String s) {
        if (verbose) 
            out.println(s);
    }
    
    /** New line.
     */
    public static final String NL = System.getProperty("line.separator");
    
    /** A buffer of of error messages.
     */
    protected static StringBuffer messages;
    
    /** Appends to error messages.
     */
    protected static synchronized void appendMessage(String message) {
        if (message != null) {
            if (messages == null) {
                messages = new StringBuffer();
            }
            messages.append(message);
            messages.append(NL);
        }
    }
    
    /**
     * Returns collected error messages, or <code>null</code> if there
     * are none, and clears the buffer.
     */
    protected static synchronized String retrieveMessages() {
        if (messages == null) {
            return null;
        }
        final String msg = messages.toString();
        messages = null;
        return msg;
    }
    
    /** 
     * Fail the test if there are any error messages.
     */
    protected void failOnError() {
        String errors = retrieveMessages();
        if (errors != null) {
            fail (errors);
        }
    }
}

