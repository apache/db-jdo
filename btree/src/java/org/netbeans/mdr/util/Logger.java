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
package org.netbeans.mdr.util;

import java.io.PrintStream;

/** 
 * Logger utility.
 * 
 * This class replaces the implementation of the Logger class in package 
 * org.netbeans.mdr.util from the NetBeans open source mdr project. 
 * The NetBeans implemenation uses a non-mdr class
 * (org.openide.ErrorManager). This imposes a dependency on other NetBeans 
 * modules which makes it harder to use the btree implemenatation as a
 * library. 
 * 
 * @author Michael Bouschen
 */
public class Logger {

    /** */
    public static final int INFORMATIONAL = 0x00000001;
    public static final int WARNING = 0x00000010;
    public static final int USER = 0x00000100;
    public static final int EXCEPTION = 0x00001000;
    public static final int ERROR = 0x00010000;

    /** */
    public static final PrintStream out = System.out;
    
    /** */
    private static final Logger logger = new Logger();
    

    /** The name of the boolean system property to enable btree logging. */
    public static final String VERBOSE_PROPERTY = 
        "org.netbeans.mdr.persistence.verbose";

    /** */
    private boolean verbose;

    /** */
    public static Logger getDefault() {
        return logger;
    }

    /** */
    protected Logger() {
        verbose = Boolean.getBoolean(VERBOSE_PROPERTY);
    }

    /** */
    public void notify(int level, Exception e) {
        if (verbose && (e != null))
            log(e.toString());
    }
    
    /** */
    public void log(String msg) {
        if (verbose) out.println(msg);
    }

    /** */
    public void log(int severity, String msg) {
        if (verbose) out.println(msg);
    }

    /** */
    public final Throwable annotate(Throwable t, String localizedMessage) {
        return t;
    }

    /** */
    public final Throwable annotate(Throwable target, Throwable t) {
        return target;
    }
}
