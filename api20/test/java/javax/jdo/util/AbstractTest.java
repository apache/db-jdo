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
     */
    protected void println(String s) {
        if (verbose) 
            out.println(s);
    }
}

